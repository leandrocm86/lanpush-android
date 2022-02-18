package lcm.lanpush.preferences;

import lcm.lanpush.LanpushApp;

public class PortPreference extends LanpushPreference {

    public PortPreference() {
        super("port", LanpushApp.DEFAULT_PORT + "");
    }

    @Override
    public void apply(Object value) {
        LanpushApp.setPort(Integer.parseInt(value.toString()));
    }
}
