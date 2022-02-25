package lcm.lanpush.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;

import lcm.lanpush.LanpushApp;

public abstract class Alarm extends BroadcastReceiver {

    public void cancel() {
        getAlarmManager().cancel(getPendingIntent());
    }

    protected AlarmManager getAlarmManager() {
        return (AlarmManager) LanpushApp.getContext().getSystemService(Context.ALARM_SERVICE);
    }

    protected abstract PendingIntent getPendingIntent();
}
