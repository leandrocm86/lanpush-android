package lcm.lanpush.preferences;

import lcm.lanpush.Receiver;

public class TimeoutPreference extends LanpushPreference {

    public static final TimeoutPreference inst = new TimeoutPreference();

    private TimeoutPreference() {
        super("timeout", Receiver.DEFAULT_TIMEOUT + "");
    }

    @Override
    public void apply(String value) {
        Receiver.inst.setTimeout(Integer.parseInt(value));
    }

}
