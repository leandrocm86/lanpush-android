package lcm.lanpush.preferences;

import lcm.lanpush.Sender;

public class IPsPreference extends LanpushPreference {

    public IPsPreference() {
        super("ips", Sender.DEFAULT_HOSTS[0]);
    }

    @Override
    public void apply(Object value) {
        Sender.setHosts(value.toString().trim().split(","));
    }
}
