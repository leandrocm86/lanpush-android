package lcm.lanpush.preferences;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.R;
import lcm.lanpush.Sender;

public class DebugPortPreference extends IntPreference {

    public static final DebugPortPreference inst = new DebugPortPreference();

    private DebugPortPreference() {
        super(LanpushApp.get(R.string.settings_debug_port_key),
                Integer.parseInt(LanpushApp.get(R.string.settings_debug_port_default)));
    }

    @Override
    public void apply(Integer value) {
        Sender.inst().setDebugPort(value);
    }
}
