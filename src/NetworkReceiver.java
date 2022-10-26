import javax.sound.midi.*;
import java.io.*;
import java.nio.*;
import java.net.*;

// DO NOT MODIFY THIS CLASS.
/**
 * Implements a network-based MIDI receiver that
 * can be accessed remotely.
 */
public class NetworkReceiver implements Receiver {

	private DatagramSocket _socket;
	private InetAddress _address;

	/**
	 * Sets the remote adress to the specified string.
	 * @param addressStr the remote adress 
	 */
	public void setAddress (String addressStr) throws UnknownHostException {
		_address = InetAddress.getByName(addressStr);
	}

	public NetworkReceiver () throws SocketException {
		_socket = new DatagramSocket();
	}

	@Override
	/**
	 * Closes the receiver and its associated network socket.
	 */
	public void close () {
		_socket.close();
	}

	@Override
	/**
	 * Sends the MIDI message to a remote receiver over the network.
	 * @param midiMessage the message to send.
	 * @param the associated timestamp of the message.
	 */
	public void send (MidiMessage midiMessage, long timeStamp) {
		ShortMessage message = (ShortMessage) midiMessage;
		try {
			int capacity = 4 * Integer.BYTES;
			ByteBuffer b = ByteBuffer.allocate(capacity);
			b.putInt(message.getCommand());
			b.putInt(message.getChannel());
			b.putInt(message.getData1());
			b.putInt(message.getData2());
			DatagramPacket packet = new DatagramPacket(b.array(), capacity, _address, Server.PORT);
			_socket.send(packet);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
