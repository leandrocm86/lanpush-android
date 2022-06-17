package lcm.lanpush.preferences;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;
import lcm.lanpush.R;

public class EnableDebugPreference extends BooleanPreference {

    public static final EnableDebugPreference inst = new EnableDebugPreference();

    private EnableDebugPreference() {
        super(LanpushApp.get(R.string.settings_enable_debug_key),
                Boolean.parseBoolean(LanpushApp.get(R.string.settings_enable_debug_default)));
    }

    @Override
    public void apply(Boolean value) {
        if (value == true) {
            DebugHostPreference.inst.load();
            DebugPortPreference.inst.load();
        }
    }
}
