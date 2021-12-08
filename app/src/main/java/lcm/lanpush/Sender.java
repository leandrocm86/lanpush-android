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
    private static final int DEBUG_PORT = 1051;

    public static void send(String message, int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Get the internet address of the specified host
                    InetAddress address = InetAddress.getByName(HOST);

                    // Initialize a datagram packet with data and address
                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length,
                            address, port);

                    // Create a datagram socket, send the packet through it, close it.
                    DatagramSocket dsocket = new DatagramSocket();
                    dsocket.send(packet);
                    dsocket.close();
                }
                catch (IOException e) {
                    if (port == DEBUG_PORT)
                        Log.log(e.getClass().getName() + ": " + e.getMessage(), "[SENDER-ERROR] ", false);
                    else
                        Log.e("Erro ao enviar mensagem", e);
                }
            }}).start();
    }

    public static void send(String message) {
        send(message, DEBUG_PORT);
    }
}