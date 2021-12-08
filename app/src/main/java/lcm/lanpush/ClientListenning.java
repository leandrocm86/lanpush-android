package lcm.lanpush;

import android.app.Activity;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.DatagramChannel;

import lcm.lanpush.utils.Data;

public class ClientListenning extends Activity implements Runnable {

    private final int TIMEOUT = 300000; // 5min
    private int erros = 0;
    private DatagramSocket udpSocket;
    private long ultimaMensagem = 0;
    private boolean running = false;

    private static ClientListenning instance;

    private ClientListenning() {}

    public static ClientListenning getInstance() {
        if (instance == null)
            instance = new ClientListenning();
        return instance;
    }

    @Override
    public void run() {
        escutar();
        if (erros == 3) {
            Notificador.getInstance().showNotification("Como houveram 3 erros, o programa será fechado.");
            System.exit(1);
        }
        else LanpushApp.restartService(); // Parou por timeout.
    }

    private synchronized void escutar() {
        if (running == true) {
            Log.i("Tentativa de executar escutador que ja estava rodando. Abortando...");
            return;
        }
        running = true;
//        Notificador.getInstance().showNotification("Teste");
        while (erros < 3) {
            try {
                Log.i("Iniciando conexão com " + erros + " erros.");
                DatagramPacket packet = reconectar();
                udpSocket.setSoTimeout(TIMEOUT);
                Log.i("UDP client: about to wait to receive");
                udpSocket.receive(packet);
                String text = new String(packet.getData(), 0, packet.getLength()).trim();
                Log.i("Received: " + text);
                if (System.currentTimeMillis() - ultimaMensagem > 3000) // Espera um tempo pra ouvir de novo, evitando mensagens duplicadas.
                    Notificador.getInstance().showNotification(text);
                ultimaMensagem = System.currentTimeMillis();
            }
            catch (SocketTimeoutException e) {
                Log.i("TIMEOUT!");
                break;
            }
            catch (Throwable t) {
                erros++;
                Log.e("Erro ao tentar ouvir porta", t);
            } finally {
                fecharConexao();
            }
        }
        running = false;
    }

    private DatagramPacket reconectar() throws SocketException {
        if (udpSocket != null) {
            Log.i("Socket já estava instanciado ao começar a ouvir. Será fechado...");
            fecharConexao();
        }
        udpSocket = new DatagramSocket(1050);
        byte[] message = new byte[8000];
        return new DatagramPacket(message, message.length);
    }

    private void fecharConexao() {
        if (udpSocket != null) {
            try {
                if (udpSocket.isClosed())
                    Log.i("Conexão já se encontra fechada.");
                else {
                    Log.i("Fechando conexão...");
                    udpSocket.disconnect();
                    udpSocket.close();
                }
                udpSocket = null;
            } catch (Throwable t) {
                erros++;
                Log.e("Erro ao tentar fechar conexão", t);
            }
        } else {
            Log.i("Conexão nula não precisa ser fechada.");
        }
    }
}