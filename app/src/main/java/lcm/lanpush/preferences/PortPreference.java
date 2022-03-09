package lcm.lanpush.preferences;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

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

    @Override
    public void prepareEditField(Preference androidPreference) {
        super.prepareEditField(androidPreference);
        ((EditTextPreference) androidPreference).setDialogTitle("UDP Port");
    }
}
