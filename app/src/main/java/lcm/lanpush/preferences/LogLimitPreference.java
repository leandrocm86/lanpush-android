package lcm.lanpush.preferences;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import lcm.lanpush.Log;

public class LogLimitPreference extends IntPreference {

    public static final LogLimitPreference inst = new LogLimitPreference();

    private LogLimitPreference() {
        super("logLimit", 100);
    }

    @Override
    public void apply(Integer value) {
        Log.setMessageLimit(value);
    }

    @Override
    public void prepareEditField(Preference androidPreference) {
        super.prepareEditField(androidPreference);
        ((EditTextPreference) androidPreference).setDialogTitle("Maximum number of last messages to display");
    }
}
