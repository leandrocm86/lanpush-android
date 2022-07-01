package lcm.lanpush.preferences;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.R;

public class SleepPreference extends BooleanPreference {

    public static final SleepPreference inst = new SleepPreference();

    private SleepPreference() {
        super(LanpushApp.get(R.string.settings_sleep_key),
                Boolean.parseBoolean(LanpushApp.get(R.string.settings_sleep_default)));
    }

}
