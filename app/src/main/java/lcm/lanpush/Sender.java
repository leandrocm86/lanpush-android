package lcm.lanpush;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import lcm.lanpush.preferences.DebugHostPreference;
import lcm.lanpush.preferences.DebugPortPreference;
import lcm.lanpush.preferences.IPsPreference;
import lcm.lanpush.preferences.PortPreference;

public class Sender {
    private String[] hosts = {IPsPreference.inst.getDefaultValue()};
    private String debugHost = DebugHostPreference.inst.getDefaultValue();
    private int debugPort = DebugPortPreference.inst.getDefaultValue();

    private static Sender inst;

    private long lastSent = 0;

    public static Sender inst() {
        if (inst == null) {
            inst = new Sender();
            IPsPreference.inst.load();
            Log.d("Sender created.");
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
                    lastSent = System.currentTimeMillis();
                }
                catch (IOException e) {
                    if (port == DebugPortPreference.inst.getValue())
                        Log.log(e.getClass().getName() + ": " + e.getMessage(), "[DEBUG-ERROR] ");
                    else
                        Log.e("Error while sending message", e);
                }
            }}).start();
    }

    public void send(String message) {
        if ("[reconnect]".equals(message) || "[stop]".equals(message))
            send(message, "127.0.0.1", PortPreference.inst.getValue());
        else {
            for (String host : hosts)
                send(message, host, PortPreference.inst.getValue());
        }
    }

    public void sendDebug(String message) {
        send(message, debugHost, debugPort);
    }

    public void setHosts(String[] ips) {
        String hostsStr = ips[0];
        for (int i = 1; i < ips.length; i++)
            hostsStr += ", " + ips[i];
        Log.d("Hosts set: " + hostsStr);
        hosts = ips;
    }

    public void setDebugPort(int debugPort) {
        this.debugPort = debugPort;
        Log.d("Debug port set: " + debugPort);
    }

    public void setDebugHost(String debugHost) {
        this.debugHost = debugHost;
        Log.d("Debug host set: " + debugHost);
    }

    public long getLastSent() {
        return lastSent;
    }
}