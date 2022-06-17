package lcm.lanpush;

import android.content.SharedPreferences;
import android.os.health.SystemHealthManager;

import androidx.preference.PreferenceManager;

import java.util.LinkedList;
import java.util.List;

import lcm.lanpush.preferences.DebugHostPreference;
import lcm.lanpush.preferences.DebugPortPreference;
import lcm.lanpush.preferences.EnableDebugPreference;
import lcm.lanpush.preferences.LogLimitPreference;
import lcm.lanpush.utils.Data;

public class Log {
//    private static int id = 0;
    private static LinkedList<String> messages = new LinkedList<>();
    private static int messageLimit = LogLimitPreference.inst.getValue();

    public static void d(String msg) {
        if (EnableDebugPreference.inst.getValue())
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

    /*public static String getThreadId() {
        String threadId = "" + Thread.currentThread().getId();
        if (threadId.length() > 3)
            threadId = threadId.substring(threadId.length() - 3);
        return threadId;
    }*/

    public static void log(String msg, String header) {
        String linha = Data.agora() + ": " + header + msg;
        if (EnableDebugPreference.inst.getValue())
            sendDebug(linha);
        addMsg(linha);
        LogFragment logView = (LogFragment) LanpushApp.getLogView();
        if (logView != null) {
            logView.addMsg(linha);
        }
    }

    public static List<String> getMessages() {
        return messages;
    }

    private static void addMsg(String msg) {
        if (messages.size() >= messageLimit && messages.size() > 0) {
            messages.removeFirst();
        }
        messages.add(msg);
    }

    private static void sendDebug(String msg) {
        if (!msg.contains("[DEBUG-ERROR]")) {
//            msg = "L" + ++id + msg + "\n";
            Sender.inst().sendDebug(msg + "\n");
        }
    }

    public static String resumeErro(Throwable t) {
        String resumo = t.getClass().getName() + ": " + t.getMessage();
        if (t.getCause() != null) {
            resumo += ". Cause: " + t.getCause().getClass().getName() + ": " + t.getCause().getMessage();
        }
        resumo += stackTraceToStr(t, 5);
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
        if (!messages.isEmpty()) {
            d("Saving logs...");
            if (messages.size() > messageLimit) {
                while (messages.size() > messageLimit)
                    messages.removeFirst();
            }
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).edit();
            StringBuilder log = new StringBuilder();
            for (String msg : messages)
                log.append(msg + '\n');
            editor.putString("lanpush-logs", log.toString());
    //        editor.apply();
            editor.commit();
        }
    }

    public static void loadMessages() {
        LinkedList<String> restoredMessages = new LinkedList<>();
        String savedLog = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).getString("lanpush-logs", "");
        if (!savedLog.isEmpty()) {
            for (String msg : savedLog.split("\n")) {
                restoredMessages.add(msg);
            }
            messages.addAll(0, restoredMessages);
            d("Restored " + restoredMessages.size() + " log messages.");
        }
    }

    public static void setMessageLimit(int limit) {
        messageLimit = limit;
        Log.d("Message limit set: " + messageLimit);
    }
}
