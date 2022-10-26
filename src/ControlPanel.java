import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.midi.*;

// DO NOT MODIFY THIS CLASS
/**
 * Implements a control panel with which the user can
 * select a musical instrument as well as the MIDI receiver
 * to use.
 */
public class ControlPanel extends JPanel {
	private JTextField _ipTextField = null;
	private JRadioButton _remoteButton = null;
	Receiver _receiver;

	/**
	 * Returns the IP address currently entered in the text box.
	 * @return the IP address currently entered in the text box.
	 */
	public String getIPAddress () {
		return _ipTextField.getText();
	}

	/**
	 * Adds the specified ActionListener to the radio button.
	 * @param listener the ActionListener to add.
	 */
	public void addActionListener (ActionListener listener) {
		_remoteButton.addActionListener(listener);
	}

	/**
	 * Returns whether or not the radio button indicates the
	 * receiver should be remote (i.e., network).
	 * @return whether or not the receiver should be remote.
	 */
	public boolean isRemote () {
		return _remoteButton.isSelected();
	}

	/**
	 * Sets the local receiver to the specified value.
	 * @param the local receiver 
	 */
	public void setReceiver (Receiver receiver) {
		_receiver = receiver;
	}

	/**
	 * @param the local receiver.
	 */
	public ControlPanel (Receiver receiver) {
		_receiver = receiver;

		Instrument[] instruments;
		Map<String, Patch> patches = new HashMap<>();
		try {
			instruments = MidiSystem.getSynthesizer().getAvailableInstruments();
		} catch (MidiUnavailableException mue) {
			return;
		}
		String[] strings = new String[instruments.length];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = instruments[i].getName();
			patches.put(strings[i], instruments[i].getPatch());
		}
		JComboBox comboBox = new JComboBox(strings);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				Patch patch = patches.get((String) comboBox.getSelectedItem());
				try {
					ShortMessage myMsg = new ShortMessage();
					myMsg.setMessage(ShortMessage.PROGRAM_CHANGE, patch.getBank(), patch.getProgram(), 0);
					_receiver.send(myMsg, -1);
				} catch (InvalidMidiDataException imde) {
					System.out.println("Could not change instrument!");
				}
			}
		});
		ButtonGroup buttonGroup = new ButtonGroup();
		JRadioButton localButton = new JRadioButton("Local");
		localButton.setSelected(true);
		_remoteButton = new JRadioButton("Remote: ");
		_ipTextField = new JTextField("255.255.255.255");
		buttonGroup.add(localButton);
		buttonGroup.add(_remoteButton);
		add(comboBox);
		add(localButton);
		add(_remoteButton);
		add(_ipTextField);
	}
}
