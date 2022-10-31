import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.sound.midi.*;
import javax.swing.*;

/**
 * Implements a key on a simulated piano keyboard.
 */
public class Key {
	// You are free to add more instance variables if you wish.
	private Polygon _polygon;
	private int _pitch;
	private boolean _isOn;
	private boolean _isWhite;
	private Piano _piano;


	/**
	 * Returns the polygon associated with this key.
	 * @return the polygon associated with this key.
	 */
	public Polygon getPolygon () {
		return _polygon;
	}

	// You are free to modify the constructor if you wish.
	/**
	 * @param polygon the Polygon that describes the shape and position of this key.
	 * @param pitch the pitch value of the key.
	 * @param piano the Piano associated with this key.
	 */
	public Key (Polygon polygon, int pitch, Piano piano) {
		_polygon = polygon;
		_pitch = pitch;
		_piano = piano;
	}
	public Key (Polygon polygon, boolean isWhite, int pitch, Piano piano) {
		_polygon = polygon;
		_pitch = pitch;
		_piano = piano;
		_isWhite = isWhite;
	}

	// DO NOT MODIFY THIS METHOD.
	/**
	 * Turns the note either on or off.
	 * @param isOn whether the note should be turned on.
	 */
	public void play (boolean isOn) {
		try {
			// Some MIDI technicalities; don't worry too much about it.
			ShortMessage myMsg = new ShortMessage();
			final int VELOCITY = 93;
			myMsg.setMessage(isOn ? ShortMessage.NOTE_ON : ShortMessage.NOTE_OFF, 0, _pitch, VELOCITY);
			final int IMMEDIATELY = -1;
			// Send the message to the receiver (either local or remote).
			_piano.getReceiver().send(myMsg, IMMEDIATELY);
			// Set the key to "on".
			_isOn = isOn;
			// Ask the piano to redraw itself (since one of its keys has changed).
			_piano.repaint();
		} catch (InvalidMidiDataException imde) {
			System.out.println("Could not play key!");
		}
	}

	// TODO implement this method.
	/**
	 * Paints the key using the specified Swing Graphics object.
	 * @param g the Graphics object to be used for painting.
	 */
	public void paint (Graphics g) {
		// TODO: Change this to handle the different key colors
		// and different key states (pressed down or not).
		if(_isOn) {
			g.setColor(Color.GRAY);
		} else {
			if(_isWhite) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(Color.BLACK);
			}
		}
		g.fillPolygon(_polygon);
		g.setColor(Color.BLACK);
		g.drawPolygon(_polygon);
	}

	/**
	 * Returns a String representation describing the key.
	 * @return a String representation describing the key.
	 */
	public String toString () {
		return "Key: " + _pitch;
	}
}
