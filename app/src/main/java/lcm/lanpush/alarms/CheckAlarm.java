package lcm.lanpush.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;
import lcm.lanpush.Receiver;
import lcm.lanpush.utils.Data;

public class CheckAlarm extends Alarm {

//    private static int id = Math.round(System.currentTimeMillis() / 60000);
    public static final CheckAlarm inst = new CheckAlarm();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Check Alarm being triggered... ");
        if (Receiver.inst.isRunning())
            Log.d("Connection seems OK");
        else {
            Log.d("Connection stopped. Restarting...");
            LanpushApp.restartWorker();
        }
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "lanpush:alarm");
//        wl.acquire();

//        LanpushApp.restartService();

//        wl.release();
    }

    public void setAlarm(long triggerAtMillis) {
        AlarmManager alarmMgr = super.getAlarmManager();
        if (Build.VERSION.SDK_INT < 31 || alarmMgr.canScheduleExactAlarms()) {
            Log.d("Configuring preventive wake up in " + Data.formataTempo(triggerAtMillis - System.currentTimeMillis()));
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, getPendingIntent());
        } else {
            Log.d("There's no permission to set alarms. The application may be killed and don't restart.");
        }
    }

    @Override
    protected PendingIntent getPendingIntent() {
        Intent i = new Intent(LanpushApp.getContext(), CheckAlarm.class);
        return PendingIntent.getBroadcast(LanpushApp.getContext(), 1, i, PendingIntent.FLAG_IMMUTABLE);
    }

}
