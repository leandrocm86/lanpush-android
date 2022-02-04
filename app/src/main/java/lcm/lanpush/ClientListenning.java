package lcm.lanpush;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientListenning {

    public static final int DEFAULT_TIMEOUT = 60000; // 1min
    private int timeout = DEFAULT_TIMEOUT;
    private int erros = 0;
    private DatagramSocket udpSocket;
    private long ultimaMensagem = 0;
    private boolean running = false;
    private long ultimaConexao = 0;
    private String penultimaThread = "";
    private String threadAtual = "";

    private static ClientListenning instance;

    private ClientListenning() {}

    public static ClientListenning getInstance() {
        if (instance == null)
            instance = new ClientListenning();
        return instance;
    }

    public boolean isRunning() {
        return running;
    }
    public long getUltimaConexao() { return ultimaConexao; }

    public void run() {
//        if (!Data.madrugada())
        escutar();
        if (erros == 3) {
            Notificador.getInstance().showNotification("Since there were 3 errors, the app is getting closed.");
            System.exit(1);
        }
    }

    private synchronized void escutar() {
        if (running == true) {
            Log.i("Tried to execute listenner that was already running. Aborting...");
            return;
        }
        running = true;
//        Notificador.getInstance().showNotification("Teste");
        try {
            DatagramPacket packet = reconectar();
            udpSocket.setSoTimeout(timeout);
            ultimaConexao = System.currentTimeMillis();
            Log.i("UDP: about to wait (" + erros + " errors, timeout " + timeout + ", thread " + atualizaThread() + ")");
            udpSocket.receive(packet);
            String text = new String(packet.getData(), 0, packet.getLength()).trim();
            Log.i("Received: " + text);
            synchronized(this) {
                // Espera um tempo pra ouvir de novo, evitando mensagens duplicadas.
                if (System.currentTimeMillis() - ultimaMensagem > 3000 && !text.contains("[auto]"))
                    Notificador.getInstance().showNotification(text);
                ultimaMensagem = System.currentTimeMillis();
            }
        } catch (SocketTimeoutException e) {
            Log.i("TIMEOUT!");
        } catch (Throwable t) {
            erros++;
            Log.e("Error while listenning!", t);
            ultimaMensagem = System.currentTimeMillis();
        } finally {
            fecharConexao();
            running = false;
        }
    }

    private String atualizaThread() {
        penultimaThread = threadAtual;
        threadAtual = Log.getThreadId();
        return penultimaThread + "->" + threadAtual;
    }

    private synchronized DatagramPacket reconectar() throws SocketException {
        if (udpSocket != null && !udpSocket.isClosed()) {
            Log.i("Socket was already active while trying to reconnect. Closing it...");
            fecharConexao();
        }
        udpSocket = new DatagramSocket(1050);
        byte[] message = new byte[8000];
        return new DatagramPacket(message, message.length);
    }

    public synchronized void fecharConexao() {
        if (udpSocket != null) {
            try {
                if (udpSocket.isClosed())
                    Log.i("Connection was already closed.");
                else {
                    Log.i("Closing connection...");
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
            Log.i("Null connection. Doesn't need to be closed.");
        }
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
        Log.i("Timeout set: " + timeout);
    }
    public int getTimeout() {
        return timeout;
    }
}