import javax.sound.midi.*;
import java.util.*;

// DO NOT MODIFY THIS CLASS.
/**
 * Utility class to support <tt>PianoTester.java</tt>
 * that allows the tester to query different properties
 * of the keys in the virtual Piano keyboard.
 */
public class TestReceiver implements Receiver {
	private Map<Integer, Boolean> _keyIsOnMap = new HashMap<>();
	private Map<Integer, Integer> _keyCountOnMap = new HashMap<>();
	private Map<Integer, Integer> _keyCountOffMap = new HashMap<>();

	/**
	 * Returns whether the key of the specified pitch is currently on.
	 * @param pitch the pitch to query
	 * @return whether the key is on
	 */
	public boolean isKeyOn (int pitch) {
		return _keyIsOnMap.containsKey(pitch) ? _keyIsOnMap.get(pitch) : false;
	}

	/**
	 * Returns the count of how many times the key with the specified pitch has been turned on.
	 * @param pitch the pitch to query
	 * @return the count.
	 */
	public int getKeyOnCount (int pitch) {
		return _keyCountOnMap.containsKey(pitch) ? _keyCountOnMap.get(pitch) : 0;
	}

	/**
	 * Returns the count of how many times the key with the specified pitch has been turned off.
	 * @param pitch the pitch to query
	 * @return the count.
	 */
	public int getKeyOffCount (int pitch) {
		return _keyCountOffMap.containsKey(pitch) ? _keyCountOffMap.get(pitch) : 0;
	}

	public TestReceiver () {
	}

	@Override
	public void close () {
	}

	@Override
	public void send (MidiMessage midiMessage, long timeStamp) {
		ShortMessage message = (ShortMessage) midiMessage;
		int pitch = message.getData1();
		if (message.getCommand() == ShortMessage.NOTE_ON) {
			_keyIsOnMap.put(message.getData1(), true);
			_keyCountOnMap.put(pitch, _keyCountOnMap.getOrDefault(pitch, 0) + 1);
		} else if (message.getCommand() == ShortMessage.NOTE_OFF) {
			_keyIsOnMap.put(message.getData1(), false);
			_keyCountOffMap.put(pitch, _keyCountOffMap.getOrDefault(pitch, 0) + 1);
		}
	}
}
