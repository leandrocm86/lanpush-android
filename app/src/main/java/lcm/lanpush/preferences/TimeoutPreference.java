package lcm.lanpush.preferences;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

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

    @Override
    public void prepareEditField(Preference androidPreference) {
        super.prepareEditField(androidPreference);
        ((EditTextPreference) androidPreference).setDialogTitle("Miliseconds between reconnections.");
    }

}
