import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.sound.midi.*;
import java.util.*;

// DO NOT MODIFY THIS CLASS
/**
 * Implements a "band" application with a piano keyboard that
 * can play different musical instruments, either locally or
 * over the network.
 */
public class Band {
	public static void main (String[] args) {
		// Create the local MIDI receiver
		Receiver localReceiver;
		try {
			localReceiver = MidiSystem.getReceiver();
		} catch (MidiUnavailableException mue) {
			mue.printStackTrace();
			return;
		}

		// Create the JFrame that constitutes the Band program
		JFrame frame = new JFrame("Band");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ControlPanel controlPanel = new ControlPanel(localReceiver);
		Piano piano = new Piano(localReceiver);
		frame.setLayout(new BorderLayout());
		frame.add(piano, BorderLayout.CENTER);
		frame.add(controlPanel, BorderLayout.SOUTH);
		// Add a listener to radio button that selects the local vs. remote receiver.
		controlPanel.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (controlPanel.isRemote()) {
					try {
						// Construct a new receiver given the currently entered IP.
						NetworkReceiver networkReceiver = new NetworkReceiver();
						networkReceiver.setAddress(controlPanel.getIPAddress());
						controlPanel.setReceiver(networkReceiver);
						piano.setReceiver(networkReceiver);
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				} else {
					// Use the local MIDI receiver instead.
					controlPanel.setReceiver(localReceiver);
					piano.setReceiver(localReceiver);
				}
			}
		});
		frame.pack();
		frame.setVisible(true);
		piano.requestFocus();
	}
}
