package lcm.lanpush.preferences;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.R;

public class PortPreference extends IntPreference {

    public static final PortPreference inst = new PortPreference();

    private PortPreference() {
        super(LanpushApp.get(R.string.settings_UDP_port_key),
                Integer.parseInt(LanpushApp.get(R.string.settings_UDP_port_default)));
    }

    @Override
    public void prepareEditField(Preference androidPreference) {
        super.prepareEditField(androidPreference);
        ((EditTextPreference) androidPreference).setDialogTitle("UDP Port");
    }
}
