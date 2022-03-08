package lcm.lanpush;

import android.widget.TextView;

import java.util.ArrayList;

import lcm.lanpush.utils.Data;

public class Log {
    private static int id = 0;

    private static boolean enableDebug = false;

    public static void i(String msg) {
        log(msg, "[I] ");
    }

    public static void e(String msg) {
        log(msg, "[E] ");
    }

    public static void e(Throwable t) {
        e(resumeErro(t));
    }

    public static void e(String msg, Throwable t) {
        e(msg + ":\n" + resumeErro(t));
    }

    public static String getThreadId() {
        String threadId = "" + Thread.currentThread().getId();
        if (threadId.length() > 3)
            threadId = threadId.substring(threadId.length() - 3);
        return threadId;
    }

    public static void log(String msg, String header) {
        String linha = "[" + getThreadId() + "] " + Data.agora() + ": " + header + msg;
        android.util.Log.i("INFO", linha);
        TextView logView = (TextView) LanpushApp.getTextView();
        if (logView != null) {
            LanpushApp.getMainActivity().runOnUiThread(new Runnable() {
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
                   sendDebug(linha);
               }
            });
        }
        else {
            addMsgFila(linha);
            sendDebug(linha);
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
        TextView logView = LanpushApp.getTextView();
        logView.setText(logView.getText() + msg);
    }

    private static void sendDebug(String msg) {
        if (enableDebug && !msg.contains("[DEBUG-ERROR]")) {
            msg = "L" + ++id + msg + "\n";
            Sender.inst().sendDebug(msg);
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

    public static void enableDebug(boolean enable) {
        enableDebug = enable;
        i("Debug enabled: " + enableDebug);
    }
}
