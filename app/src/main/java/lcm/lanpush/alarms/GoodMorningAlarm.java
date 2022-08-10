package lcm.lanpush.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;
import lcm.lanpush.utils.Dates;

public class GoodMorningAlarm extends Alarm {

    public static final GoodMorningAlarm inst = new GoodMorningAlarm();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Good Morning Alarm being triggered... ");
        LanpushApp.restartWorker();
    }

    public void setAlarm() {
        long timestampNextMorning = Dates.timestampNextMorning();
        AlarmManager alarmMgr = super.getAlarmManager();
        if (Build.VERSION.SDK_INT < 31 || alarmMgr.canScheduleExactAlarms()) {
            Log.d("Configuring morning wake up in " + Dates.formatTime(timestampNextMorning - System.currentTimeMillis()));
            alarmMgr.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timestampNextMorning, getPendingIntent());
        } else {
            Log.d("There's no permission to set alarms. The application can't restart by itself.");
        }
    }

    @Override
    protected PendingIntent getPendingIntent() {
        Intent i = new Intent(LanpushApp.getContext(), GoodMorningAlarm.class);
        return PendingIntent.getBroadcast(LanpushApp.getContext(), 2, i, PendingIntent.FLAG_IMMUTABLE);
    }

}
