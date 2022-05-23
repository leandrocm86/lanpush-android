package lcm.lanpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lcm.lanpush.alarms.CheckAlarm;
import lcm.lanpush.preferences.AutoStartPreference;
import lcm.lanpush.utils.Data;

public class AutoStart extends BroadcastReceiver {

    private static boolean okToStart = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!AutoStartPreference.inst.getValue()) {
            okToStart = false;
            Log.d("Application auto started but autostart configuration is disabled. Shutting down...");
            LanpushApp.close();
            LanpushApp.shutdown();
            return;
        }
        Log.d("AUTO START");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            CheckAlarm.inst.setAlarm(Data.timestampProximaManha());
        }
    }

    public static boolean isOkToStart() {
        return okToStart;
    }
}
