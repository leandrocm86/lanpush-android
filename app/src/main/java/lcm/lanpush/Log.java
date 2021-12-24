package lcm.lanpush;

import android.widget.TextView;

import java.util.ArrayList;

import lcm.lanpush.utils.CDI;
import lcm.lanpush.utils.Data;

public class Log {
    private static int id = 0;

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

    public static void log(String msg, String header, boolean sendDebug) {
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
                       addMsg(mensagensEnfileiradas + linha + "\n");
                       fila.clear();
                   }
                   else addMsg(linha + "\n");
                   sendDebug(linha, sendDebug);
               }
            });
        }
        else {
            addMsgFila(linha);
            sendDebug(linha, sendDebug);
        }
    }

    private static void addMsgFila(String linha) {
        if (fila.size() == 15) {
            fila.clear();
            fila.add("FILA COM MENSAGENS ANTIGAS FOI LIMPA");
        }
        fila.add(linha);
    }

    private static void addMsg(String msg) {
        TextView logView = (TextView) CDI.get("logView");
        logView.setText(logView.getText() + msg);
    }

    private static void sendDebug(String msg, boolean ligado) {
        if (ligado) {
            msg = "L" + ++id + msg + "\n";
            Sender.send(msg);
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
