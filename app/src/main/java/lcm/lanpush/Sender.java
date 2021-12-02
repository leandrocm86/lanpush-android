package lcm.lanpush;

import android.app.Activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Sender {
    private static final String HOST = "192.168.0.255";
    private static final int PORT = 1051;

    public static void send(String message) throws IOException {

        // Get the internet address of the specified host
        InetAddress address = InetAddress.getByName(HOST);

        // Initialize a datagram packet with data and address
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length,
                address, PORT);

        // Create a datagram socket, send the packet through it, close it.
        DatagramSocket dsocket = new DatagramSocket();
        dsocket.send(packet);
        dsocket.close();
    }
}