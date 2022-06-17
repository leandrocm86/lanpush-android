package lcm.lanpush.preferences;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;
import lcm.lanpush.R;

public class LogLimitPreference extends IntPreference {

    public static final LogLimitPreference inst = new LogLimitPreference();

    private LogLimitPreference() {

        super(LanpushApp.get(R.string.settings_log_limit_key),
                Integer.parseInt(LanpushApp.get(R.string.settings_log_limit_default)));
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
