package lcm.lanpush.preferences;

import lcm.lanpush.Sender;

public class DebugPortPreference extends IntPreference {

    public static final DebugPortPreference inst = new DebugPortPreference();

    private DebugPortPreference() {
        super("debugPort", 1051);
    }

    @Override
    public void apply(Integer value) {
        Sender.inst().setDebugPort(value);
    }
}
