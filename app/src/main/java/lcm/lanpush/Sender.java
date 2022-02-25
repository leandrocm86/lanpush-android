package lcm.lanpush;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import lcm.lanpush.preferences.IPsPreference;

public class Sender {
    public static final String[] DEFAULT_HOSTS = {"192.168.0.255"};
    private String[] hosts = DEFAULT_HOSTS;
    private String DEBUG_HOST = "192.168.0.66";
    private static final int DEBUG_PORT = 1051;

    private static Sender inst;

    public static Sender inst() {
        if (inst == null) {
            inst = new Sender();
            IPsPreference.inst.load();
            Log.i("Sender created.");
        }
        return inst;
    }

    private void send(String message, String host, int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Get the internet address of the specified host
                    InetAddress address = InetAddress.getByName(host);

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
                        Log.e("Error while sending message", e);
                }
            }}).start();
    }

    public void send(String message) {
        if ("[auto]".equals(message) || "[stop]".equals(message))
            send(message, "127.0.0.1", LanpushApp.getPort());
        else {
            for (String host : hosts)
                send(message, host, LanpushApp.getPort());
        }
    }
    public void sendDebug(String message) {
        send(message, DEBUG_HOST, DEBUG_PORT);
    }

    public void setHosts(String[] ips) {
        String hostsStr = ips[0];
        for (int i = 1; i < ips.length; i++)
            hostsStr += ", " + ips[i];
        Log.i("Hosts set: " + hostsStr);
        hosts = ips;
    }
}