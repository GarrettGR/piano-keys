import java.io.*;
import java.util.*;
import java.nio.*;
import java.net.*;
import javax.sound.midi.*;

// DO NOT MODIFY THIS CLASS.
/**
 * Implements a server that can receive and produce audio
 * for MIDI events received over the computer network.
 */
public class Server {
	public static final int PORT = 4567;
	private static final int MAX_CAPACITY = 256;

	public static void main(String[] args)
			throws InvalidMidiDataException, MidiUnavailableException, IOException {
		Receiver _receiver = MidiSystem.getReceiver();
		HashMap<String, Integer> channelMapping = new HashMap<>();

		DatagramSocket socket = new DatagramSocket(PORT);
		System.out.println("Receiving messages...");
		while (true) {
			// Receive message
			byte[] buffer = new byte[MAX_CAPACITY];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String addressAndPort = "" + packet.getAddress() + ":" + packet.getPort();

			// Extract request
			ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
			int command = byteBuffer.getInt();
			int channelIgnored = byteBuffer.getInt();  // Throw this away since we'll overwrite it anyhow
			int data1 = byteBuffer.getInt();
			int data2 = byteBuffer.getInt();

			// Determine unique channel for this (address,port) tuple
			int channel;
			if (channelMapping.containsKey(addressAndPort)) {
				channel = channelMapping.get(addressAndPort);
			} else {
				channel = channelMapping.size();  // Assign an unused channel
				channelMapping.put(addressAndPort, channel);
			}

			// Execute MIDI event
			ShortMessage message = new ShortMessage(command, channel, data1, data2);
			_receiver.send(message, -1);
			System.out.println("src=" + addressAndPort + " channel=" + channel);
		}
	}
}
