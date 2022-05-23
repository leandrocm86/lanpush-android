package lcm.lanpush.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;
import lcm.lanpush.Receiver;
import lcm.lanpush.preferences.SleepPreference;
import lcm.lanpush.utils.Data;

public class PeriodicCheckAlarm extends Alarm {

    public static final PeriodicCheckAlarm inst = new PeriodicCheckAlarm();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SleepPreference.inst.getValue() && Data.madrugada()) {
            Log.d("Periodic Alarm is resting.");
            return;
        }
        boolean running = Receiver.inst.isRunning();
        String diagnostic = running ? "Connection seems OK" : "Connection stopped. Restarting...";
        Log.d("Periodic Alarm being triggered... " + diagnostic);
        if (!running)
            LanpushApp.restartWorker();
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "lanpush:alarm");
//        wl.acquire();

//        LanpushApp.restartService();

//        wl.release();
    }

    public void setPeriodicAlarm() {
        Log.d("Setting periodic Alarm...");
        getAlarmManager().setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, getPendingIntent());
    }

    @Override
    protected PendingIntent getPendingIntent() {
        Intent i = new Intent(LanpushApp.getContext(), PeriodicCheckAlarm.class);
        return PendingIntent.getBroadcast(LanpushApp.getContext(), 0, i, PendingIntent.FLAG_IMMUTABLE);
    }
}
