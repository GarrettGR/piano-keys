import javax.swing.*;
import java.awt.event.*;
import javax.sound.midi.*;
import java.util.*;

/**
 * Handles mouse press, release, and drag events on the Piano.
 */
public class PianoMouseListener extends MouseAdapter {
	// You are free to add more instance variables if you wish.
	private List<Key> _keys;
	private Key _key;

	/**
	 * @param keys the list of keys in the piano.
	 */
	public PianoMouseListener (List<Key> keys) {
		_keys = keys;
	}

	// TODO implement this method.
	@Override
	/**
	 * This method is called by Swing whenever the user drags the mouse.
	 * @param e the MouseEvent containing the (x,y) location, relative to the upper-left-hand corner
	 * of the entire piano, of where the mouse is currently located.
	 */
	public void mouseDragged (MouseEvent e) {
		/* for (Key key : _keys) {
			if (key.getPolygon().contains(e.getX(), e.getY())) {
				_key = key;
				key.play(true);
			} else {
				key.play(false);
			}
		} */
		for (Key key : _keys) {
			if(e.getX() < 0 || e.getY() < 0 || e.getX() > 840 || e.getY() > 200)
				key.play(false);
			if (!key.equals(_key)) {
				if (key.getPolygon().contains(e.getX(), e.getY())) {
					_key = key;
					key.play(true);
				} else {
					key.play(false);
				}
			}
		}
	}

	// TODO implement this method.
	@Override
	/**
	 * This method is called by Swing whenever the user presses the mouse.
	 * @param e the MouseEvent containing the (x,y) location, relative to the upper-left-hand corner
	 * of the entire piano, of where the mouse is currently located.
	 */
	public void mousePressed (MouseEvent e) {
		// To test whether a certain key received the mouse event, you could write something like:
		//	if (key.getPolygon().contains(e.getX(), e.getY())) {
		// To turn a key "on", you could then write:
		//      key.play(true);  // Note that the key should eventually be turned off!

		for (Key key : _keys)
			if (key.getPolygon().contains(e.getX(), e.getY()))
				key.play(true);
	}

	// TODO implement this method.
	@Override
	/**
	 * This method is called by Swing whenever the user releases the mouse.
	 * @param e the MouseEvent containing the (x,y) location, relative to the upper-left-hand corner
	 * of the entire piano, of where the mouse is currently located.
	 */
	public void mouseReleased (MouseEvent e) {
		for (Key key : _keys)
			if (key.getPolygon().contains(e.getX(), e.getY()))
				key.play(false);
	}
}
