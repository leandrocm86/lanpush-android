package lcm.lanpush.preferences;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;
import lcm.lanpush.R;

public class AutoStartPreference extends BooleanPreference {

    public static final AutoStartPreference inst = new AutoStartPreference();

    private AutoStartPreference() {
        super(LanpushApp.get(R.string.settings_autostart_key),
                Boolean.parseBoolean(LanpushApp.get(R.string.settings_autostart_default)));
    }

}
