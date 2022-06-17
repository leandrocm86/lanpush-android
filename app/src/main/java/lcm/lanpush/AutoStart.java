package lcm.lanpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lcm.lanpush.preferences.AutoStartPreference;

public class AutoStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) { // This is only called on system boot.
        if (!AutoStartPreference.inst.getValue()) {
            Log.d("Application auto started but autostart configuration is disabled. Shutting down...");
            LanpushApp.close(true);
            return;
        }
        Log.d("AUTO START");
    }

}
