package lcm.lanpush.preferences;

import lcm.lanpush.Sender;

public class IPsPreference extends LanpushPreference {

    public static final IPsPreference inst = new IPsPreference();

    private IPsPreference() {
        super("ips", Sender.DEFAULT_HOSTS[0]);
    }

    @Override
    public void apply(String value) {
        Sender.inst().setHosts(value.trim().split(","));
    }
}
