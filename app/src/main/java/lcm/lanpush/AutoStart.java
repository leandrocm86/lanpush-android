package lcm.lanpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lcm.lanpush.Alarm;
import lcm.lanpush.utils.Data;

public class AutoStart extends BroadcastReceiver {
    Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AUTO START");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            alarm.setAlarm(Data.timestampProximaManha());
        }
    }
}
