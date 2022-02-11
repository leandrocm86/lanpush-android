package lcm.lanpush;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;

import lcm.lanpush.utils.Data;

public class Alarm extends BroadcastReceiver {

    private static int id = Math.round(System.currentTimeMillis() / 60000);

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean running = ClientListenning.getInstance().isRunning();
        String diagnostic = running ? "Connection seems OK" : "Connection stopped. Restarting...";
        Log.i("Check Alarm being triggered... " + diagnostic);
        if (!running)
            LanpushApp.restartWorker();
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "lanpush:alarm");
//        wl.acquire();

//        LanpushApp.restartService();

//        wl.release();
    }

    public static void setAlarm(long triggerAtMillis) {
        AlarmManager alarmMgr = getAlarmManager();
        if (Build.VERSION.SDK_INT < 31 || alarmMgr.canScheduleExactAlarms()) {
            Log.i("Configuring wake up in " + Data.formataTempo(triggerAtMillis - System.currentTimeMillis()));
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, getPendingIntent(false));
        } else {
            Log.i("There's no permission to set alarms. The application may be killed and don't restart.");
        }
    }

    public static void setPeriodicAlarm() {
        Log.i("Setting periodic Check Alarm...");
        getAlarmManager().setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_HALF_HOUR, getPendingIntent(true));
    }

    private static AlarmManager getAlarmManager() {
        return (AlarmManager) LanpushApp.getContext().getSystemService(Context.ALARM_SERVICE);
    }

    private static PendingIntent getPendingIntent(boolean periodic) {
        Intent i = new Intent(LanpushApp.getContext(), Alarm.class);
        return PendingIntent.getBroadcast(LanpushApp.getContext(), periodic ? 0 : ++id, i, PendingIntent.FLAG_IMMUTABLE);
    }

//    public void cancelAlarm(Context context) {
//        Intent intent = new Intent(context, Alarm.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//    }
}
