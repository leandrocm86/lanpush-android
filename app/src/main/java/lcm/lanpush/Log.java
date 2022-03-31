package lcm.lanpush;

import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import java.util.LinkedList;

import lcm.lanpush.preferences.LogLimitPreference;
import lcm.lanpush.utils.Data;

public class Log {
    private static int id = 0;
    private static LinkedList<String> messages = new LinkedList<>();
    private static int messageLimit = LogLimitPreference.inst.getValue();

    private static boolean enableDebug = false;

    public static void d(String msg) {
        if (enableDebug)
            log(msg, "[D] ");
    }

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
        sendDebug(linha);
        messages.add(linha);
        TextView logView = (TextView) LanpushApp.getTextView();
        if (logView != null) {
            LanpushApp.getMainActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (logView.getText().length() == 0) {
                       fillMessages(logView);
                   }
                   else logView.setText(logView.getText() + linha + "\n");
               }
            });
        }
    }

    public static void fillMessages(TextView logView) {
        String output = "";
        for (String msg : messages)
            output += msg + "\n";
        logView.setText(output);
    }

    private static void addMsg(String msg) {
        if (messages.size() >= messageLimit) {
            messages.removeFirst();
        }
        messages.add(msg);
//        TextView logView = LanpushApp.getTextView();
//        logView.setText(logView.getText() + msg);
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

    public static void saveMessages() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).edit();
        StringBuilder log = new StringBuilder("");
        messages.forEach(msg -> log.append(msg + '\n'));
        editor.putString("lanpush-logs", log.toString());
        editor.commit();
        d("Log saved.");
    }

    public static void loadMessages() {
        LinkedList<String> restoredMessages = new LinkedList<>();
        String savedLog = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).getString("lanpush-logs", "");
        for (String msg : savedLog.split("\n")) {
            restoredMessages.add(msg);
        }
        messages.addAll(0, restoredMessages);
        d("Restored " + messages.size() + " log messages.");
    }

    public static void enableDebug(boolean enable) {
        enableDebug = enable;
        d("Debug enabled: " + enableDebug);
    }

    public static void setMessageLimit(int limit) {
        messageLimit = limit;
        Log.d("Message limit set: " + messageLimit);
    }
}
