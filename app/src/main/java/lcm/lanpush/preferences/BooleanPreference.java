package lcm.lanpush.preferences;

import android.content.SharedPreferences;

import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

public abstract class BooleanPreference extends LanpushPreference<Boolean> {

    protected BooleanPreference(String name, Boolean defaultValue) {super(name, defaultValue);}

    @Override
    public void fillPreferenceField(Preference androidPreference) {
        SwitchPreferenceCompat checkBox = (SwitchPreferenceCompat) androidPreference;
//        checkBox.setChecked(super.getValue());
        checkBox.setOnPreferenceChangeListener(super.buildChangeListener());
    }

    @Override
    protected Boolean getValueFromPreference(SharedPreferences preferences) {
        return preferences.getBoolean(getName(), false);
    }

    @Override
    protected void persistValueInPreference(SharedPreferences.Editor editor, Boolean value) {
        editor.putBoolean(getName(), value);
    }
}
