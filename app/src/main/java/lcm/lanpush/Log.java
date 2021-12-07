package lcm.lanpush;

import android.widget.TextView;

import java.util.ArrayList;

import lcm.lanpush.utils.CDI;
import lcm.lanpush.utils.Data;

public class Log {
    public static void i(String msg) {
        log(msg, "[INFO] ");
    }

    public static void e(String msg) {
        log(msg, "[ERRO] ");
    }

    public static void e(Throwable t) {
        e(resumeErro(t));
    }

    public static void e(String msg, Throwable t) {
        e(msg + ":\n" + resumeErro(t));
    }

    private static void log(String msg, String header) {
        log(msg, header, true);
    }

    private static void log(String msg, String header, boolean sendUDP) {
        String threadId = "" + Thread.currentThread().getId();
        if (threadId.length() > 6)
            threadId = threadId.substring(0, 3) + threadId.substring(threadId.length() - 3);
        String linha = "[" + threadId + "] " + Data.agora() + ": " + header + msg;
        TextView logView = (TextView) CDI.get("logView");
        if (logView != null) {
            ((MainActivity) CDI.get("main")).runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (!fila.isEmpty()) {
                       String mensagensEnfileiradas = "";
                       for (String mensagem : fila)
                           mensagensEnfileiradas += "[ENFILEIRADA]" + mensagem + "\n";
                       addMsg(mensagensEnfileiradas + linha + "\n", sendUDP);
                       fila.clear();
                   }
                   else
                       addMsg(linha + "\n", sendUDP);
               }
            });
        }
        else
            fila.add(linha);
    }

    private static void addMsg(String msg, boolean sendUDP) {
        TextView logView = (TextView) CDI.get("logView");
        logView.setText(logView.getText() + msg);
        if (sendUDP) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Sender.send(msg);
                    } catch (Throwable t) {
                        log(resumeErro(t), "[SENDER-ERROR] ", false);
                    }
                }
            }).start();
        }
    }

    private static String resumeErro(Throwable t) {
        String resumo = t.getClass().getName() + ": " + t.getMessage();
        if (t.getCause() != null) {
            resumo += ". Cause: " + t.getCause().getClass().getName() + ": " + t.getCause().getMessage();
        }
        resumo += stackTraceToStr(t, 10);
        return resumo;
    }

    private static String stackTraceToStr(Throwable t, int linhas) {
        String str = "";
        for (int i = 0; i < linhas && i < t.getStackTrace().length; i++) {
            str += "\n" + (t.getStackTrace()[i].toString());
        }
        return str;
    }

    private static ArrayList<String> fila = new ArrayList<>();
}
