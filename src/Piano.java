import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.sound.midi.*;

/**
 * Implements a simulated piano with 36 keys.
 */
public class Piano extends JPanel {
	// DO NOT MODIFY THESE CONSTANTS
	public static int START_PITCH = 48;
	public static int WHITE_KEY_WIDTH = 40;
	public static int BLACK_KEY_WIDTH = WHITE_KEY_WIDTH/2;
	public static int WHITE_KEY_HEIGHT = 200;
	public static int BLACK_KEY_HEIGHT = WHITE_KEY_HEIGHT/2;
	public static int NUM_WHITE_KEYS_PER_OCTAVE = 7;
	public static int NUM_OCTAVES = 3;
	public static int NUM_WHITE_KEYS = NUM_WHITE_KEYS_PER_OCTAVE * NUM_OCTAVES;
	public static int WIDTH = NUM_WHITE_KEYS * WHITE_KEY_WIDTH;
	public static int HEIGHT = WHITE_KEY_HEIGHT;

	private java.util.List<Key> _keys = new ArrayList<>();
	private Receiver _receiver;
	private PianoMouseListener _mouseListener;

	/**
	 * Returns the list of keys in the piano.
	 * @return the list of keys.
	 */
	public java.util.List<Key> getKeys () {
		return _keys;
	}

	/**
	 * Sets the MIDI receiver of the piano to the specified value.
	 * @param receiver the MIDI receiver 
	 */
	public void setReceiver (Receiver receiver) {
		_receiver = receiver;
	}

	/**
	 * Returns the current MIDI receiver of the piano.
	 * @return the current MIDI receiver 
	 */
	public Receiver getReceiver () {
		return _receiver;
	}

	// DO NOT MODIFY THIS METHOD.
	/**
	 * @param receiver the MIDI receiver to use in the piano.
	 */
	public Piano (Receiver receiver) {
		// Some Swing setup stuff; don't worry too much about it.
		setFocusable(true);
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		setReceiver(receiver);
		_mouseListener = new PianoMouseListener(_keys);
		addMouseListener(_mouseListener);
		addMouseMotionListener(_mouseListener);
		makeKeys();
	}

	/**
	 * Returns the PianoMouseListener associated with the piano.
	 * @return the PianoMouseListener associated with the piano.
	 */
	public PianoMouseListener getMouseListener () {
		return _mouseListener;
	}


	private Polygon whiteKeyPolygon(boolean leftNotch, boolean rightNotch, int topBorder, int leftBorder) {
		//  1---2       3-4       3---4
		//  |   |       | |       |   |
		//  |   |       | |       |   |
		//  |   3-4   1-2 5-6   1-2   |
		//  |     |   |     |   |     |
		//  |     |   |     |   |     |
		//  0-----5   0-----7   0-----5
		final int notchWidth = BLACK_KEY_WIDTH / 2;
		final int notchY = topBorder + BLACK_KEY_HEIGHT;
		final int rightBorder = leftBorder + WHITE_KEY_WIDTH;
		final int bottomBorder = topBorder + WHITE_KEY_HEIGHT;

		int numVertices = 4; // A rectangular key has 4 corners
		if(leftNotch) { // In order to draw the left notch in the key, 2 more vertices need to exist
			numVertices += 2;
		}
		if(rightNotch) { // right notch also requires 2 more vertices
			numVertices += 2;
		}

		int[] xCoords = new int[numVertices]; //initialize the lists that we will fill with vertices
		int[] yCoords = new int[numVertices];
		int idx = 0;

		xCoords[idx] = leftBorder; //Bottom Left Corner
		yCoords[idx] = bottomBorder;
		idx++;
		if(leftNotch) {
			xCoords[idx] = leftBorder; //Left Outside corner of Notch
			yCoords[idx] = notchY;
			idx++;

			xCoords[idx] = leftBorder + notchWidth; //Inside corner of Notch
			yCoords[idx] = notchY;
			idx++;

			xCoords[idx] = leftBorder + notchWidth; //Top Outside corner of Notch
			yCoords[idx] = topBorder;
			idx++;
		} else { // no notch
			xCoords[idx] = leftBorder; //Top Left Corner
			yCoords[idx] = topBorder;
			idx++;
		}

		if(rightNotch) {
			xCoords[idx] = rightBorder - notchWidth; //Top Outside corner of Notch
			yCoords[idx] = topBorder;
			idx++;

			xCoords[idx] = rightBorder - notchWidth; //Inside corner of Notch
			yCoords[idx] = notchY;
			idx++;

			xCoords[idx] = rightBorder; //Right Outside corner of Notch
			yCoords[idx] = notchY;
			idx++;
		} else {
			xCoords[idx] = rightBorder; //Top Right Corner
			yCoords[idx] = topBorder;
			idx++;
		}

		xCoords[idx] = rightBorder; //Bottom Right Corner
		yCoords[idx] = bottomBorder;
		idx++;

		Polygon output = new Polygon(xCoords, yCoords, numVertices);
		return output;
	}

	private void makeWhiteKeys() {
		int pitch = START_PITCH;
		int xPos = 0;
		//each octave looks the same. for each key in an octave, keep track of whether it has
		//a left notch with notches[i][0] and a right notch with notches[i][1].
		final boolean[][] notches = new boolean[][] {
				{false, true},  // █▙
				{true,  true},  // ▟▙
				{true,  false}, // ▟█
				{false, true},  // █▙
				{true,  true},  // ▟▙
				{true,  true},  // ▟▙
				{true,  false}  // ▟█
		};
		for(int i = 0; i < NUM_WHITE_KEYS; i++) {
			int keyType = i%7;
			Polygon p = whiteKeyPolygon(notches[keyType][0], notches[keyType][1], 0, xPos);
			Key k = new Key(p, true, pitch, this);
			_keys.add(k);
			xPos += WHITE_KEY_WIDTH;
			if(notches[keyType][1]) {
				// If the key has a right notch, then it is followed by a black key, so the next
				// white key has a pitch 2 notes higher.
				pitch += 2;
			} else {
				// otherwise, the next white key is the immediate next key and is only 1 note higher
				pitch += 1;
			}
		}
	}
	private Polygon blackKeyPolygon(int topBorder, int leftBorder) {
		//  1--2
		//  |  |
		//  |  |
		//  |  |
		//  |  |
		//  0--3
		final int rightBorder = leftBorder + BLACK_KEY_WIDTH;
		final int bottomBorder = topBorder + BLACK_KEY_HEIGHT;

		//there are always exactly 4 vertices
		int[] xCoords = new int[4];
		int[] yCoords = new int[4];

		xCoords[0] = leftBorder;
		yCoords[0] = bottomBorder;

		xCoords[1] = leftBorder;
		yCoords[1] = topBorder;

		xCoords[2] = rightBorder;
		yCoords[2] = topBorder;

		xCoords[3] = rightBorder;
		yCoords[3] = bottomBorder;

		Polygon output = new Polygon(xCoords, yCoords, 4);
		return output;
	}
	private void makeBlackKeys() {
		//the first black key is the second key overall
		int pitch = START_PITCH + 1;
		//the first black key starts with half of its width overlapping the width
		//of the first white key
		int xPos = WHITE_KEY_WIDTH - (BLACK_KEY_WIDTH / 2);

		//each octave has 5 black keys. The gaps between them follow this pattern
		int[] keyGap = new int[] {
				1,
				2,
				1,
				1,
				2
		};

		final int numBlackKeys = 5 * NUM_OCTAVES;//5 keys per octave * NUM_OCTAVES = total black keys
		for(int i = 0; i < numBlackKeys; i++) {
			Polygon p = blackKeyPolygon(0, xPos);
			Key k = new Key(p, false, pitch, this);
			_keys.add(k);

			//next black key is this many white-key-widths to the right
			xPos += keyGap[i%5] * WHITE_KEY_WIDTH;
			//if there is 1 white key between, next pitch is 2 higher. if 2 between then 3 higher
			pitch += keyGap[i%5] + 1;
		}
	}

	// TODO: implement this method. You should create and use several helper methods to do so.
	/**
	 * Instantiate all the Key objects with their correct polygons and pitches, and
	 * add them to the _keys array.
	 */
	private void makeKeys () {
		makeWhiteKeys();
		makeBlackKeys();
	}

	// DO NOT MODIFY THIS METHOD.
	@Override
	/**
	 * Paints the piano and all its constituent keys.
	 * @param g the Graphics object to use for painting.
	 */
	public void paint (Graphics g) {
		// Delegates to all the individual keys to draw themselves.
		for (Key key: _keys) {
			key.paint(g);
		}
	}
}
