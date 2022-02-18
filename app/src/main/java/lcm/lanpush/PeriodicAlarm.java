package lcm.lanpush;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import lcm.lanpush.utils.Data;

public class PeriodicAlarm extends Alarm {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Data.madrugada()) {
            Log.i("Periodic Alarm is resting.");
            return;
        }
        boolean running = Receiver.inst.isRunning();
        String diagnostic = running ? "Connection seems OK" : "Connection stopped. Restarting...";
        Log.i("Periodic Alarm being triggered... " + diagnostic);
        if (!running)
            LanpushApp.restartWorker();
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "lanpush:alarm");
//        wl.acquire();

//        LanpushApp.restartService();

//        wl.release();
    }

    public static void setPeriodicAlarm() {
        Log.i("Setting periodic Alarm...");
        getAlarmManager().setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, getPeriodicPendingIntent());
    }

    private static PendingIntent getPeriodicPendingIntent() {
        Intent i = new Intent(LanpushApp.getContext(), PeriodicAlarm.class);
        return PendingIntent.getBroadcast(LanpushApp.getContext(), 0, i, PendingIntent.FLAG_IMMUTABLE);
    }

//    public void cancelAlarm(Context context) {
//        Intent intent = new Intent(context, Alarm.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//    }
}
