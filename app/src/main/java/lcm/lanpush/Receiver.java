package lcm.lanpush;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import lcm.lanpush.notification.Notificador;
import lcm.lanpush.preferences.PortPreference;
import lcm.lanpush.preferences.TimeoutPreference;

public class Receiver {

    public static final int DEFAULT_TIMEOUT = 300000; // 5min
    private int timeout = DEFAULT_TIMEOUT;
    private int erros = 0;
    private DatagramSocket udpSocket;
    private boolean running = false;
    private long lastConnection = 0;
//    private String penultimaThread = "";
//    private String threadAtual = "";

    public static final Receiver inst = new Receiver();

    private Receiver() {
        Log.d("Creating Receiver...");
        setTimeout(TimeoutPreference.inst.getValue());
    }

    public boolean isRunning() {
        return running;
    }
    public long getLastConnection() { return lastConnection; }

    public boolean run() {
        if (erros == 3) {
            Notificador.inst.showNotification("Since there were 3 errors, the app is getting closed.");
            LanpushApp.close(false);
            System.exit(1);
        }
        return listen();
    }

    private synchronized boolean listen() {
        if (running == true) {
            Log.d("Tried to execute listenner that was already running. Aborting...");
            return false;
        }
        running = true;
//        Notificador.getInstance().showNotification("Teste");
        try {
            DatagramPacket packet = reconnect();
            udpSocket.setSoTimeout(timeout);
            lastConnection = System.currentTimeMillis();
            Log.d("Listener reconnecting on UDP " + PortPreference.inst.getValue());
            udpSocket.receive(packet);
            String text = new String(packet.getData(), 0, packet.getLength()).trim();
            if (autoMsg(text))
                Log.d("Received: " + text);
            else {
                Log.i("Received: " + text);
                Notificador.inst.showNotification(text);
            }
            return !text.contains("[stop]");
        } catch (SocketTimeoutException e) {
            Log.d("Listener timeout!");
        } catch (Throwable t) {
            erros++;
            Log.e("Error while listenning!", t);
        } finally {
            closeConnection();
            running = false;
        }
        return true;
    }

    // Evaluates if a new message was sent by the app itself.
    private boolean autoMsg(String msg) {
        return System.currentTimeMillis() - Sender.inst().getLastSent() < 1000 || msg.contains("[reconnect]") || msg.contains("[stop]");
    }

    public void stop() {
        Sender.inst().send("[stop]");
    }

    /*private String atualizaThread() {
        penultimaThread = threadAtual;
        threadAtual = Log.getThreadId();
        return penultimaThread + "->" + threadAtual;
    }*/

    private synchronized DatagramPacket reconnect() throws SocketException {
        if (udpSocket != null && !udpSocket.isClosed()) {
            Log.d("Socket was already active while trying to reconnect. Closing it...");
            closeConnection();
        }
        udpSocket = new DatagramSocket(PortPreference.inst.getValue());
        byte[] message = new byte[8000];
        return new DatagramPacket(message, message.length);
    }

    public synchronized void closeConnection() {
        if (udpSocket != null) {
            try {
                if (udpSocket.isClosed())
                    Log.d("Connection was already closed.");
                else {
//                    Log.i("Closing connection...");
                    udpSocket.disconnect();
                    udpSocket.close();
                    udpSocket = null;
                }
                running = false;
            } catch (Throwable t) {
                erros++;
                Log.e("Error while trying to close connection", t);
            }
        } else {
            Log.d("Null connection. Doesn't need to be closed.");
        }
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
        Log.d("Timeout set: " + timeout);
    }

    public int getTimeout() {
        return timeout;
    }
}