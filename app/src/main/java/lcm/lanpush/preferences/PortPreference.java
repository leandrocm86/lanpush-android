package lcm.lanpush.preferences;

import lcm.lanpush.LanpushApp;

public class PortPreference extends IntPreference {

    public static final PortPreference inst = new PortPreference();

    private PortPreference() {
        super("port", LanpushApp.DEFAULT_PORT);
    }

    @Override
    public void apply(Integer value) {
        LanpushApp.setPort(value);
    }
}
