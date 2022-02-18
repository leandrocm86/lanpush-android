package lcm.lanpush.preferences;

import lcm.lanpush.Receiver;

public class TimeoutPreference extends LanpushPreference {

    public TimeoutPreference() {
        super("timeout", Receiver.DEFAULT_TIMEOUT + "");
    }

    @Override
    public void apply(Object value) {
        Receiver.inst.setTimeout(Integer.parseInt(value.toString()));
    }

}
