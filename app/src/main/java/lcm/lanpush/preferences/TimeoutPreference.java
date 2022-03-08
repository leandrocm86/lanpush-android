package lcm.lanpush.preferences;

import lcm.lanpush.Receiver;

public class TimeoutPreference extends IntPreference {

    public static final TimeoutPreference inst = new TimeoutPreference();

    private TimeoutPreference() {
        super("timeout", Receiver.DEFAULT_TIMEOUT);
    }

    @Override
    public void apply(Integer value) {
        Receiver.inst.setTimeout(value);
    }

}
