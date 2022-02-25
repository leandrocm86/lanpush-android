package lcm.lanpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lcm.lanpush.alarms.CheckAlarm;
import lcm.lanpush.utils.Data;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AUTO START");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            CheckAlarm.inst.setAlarm(Data.timestampProximaManha());
        }
    }
}
