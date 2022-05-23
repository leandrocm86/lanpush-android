package lcm.lanpush.preferences;

import android.content.SharedPreferences;
import android.text.InputType;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

public abstract class StringPreference extends LanpushPreference<String> {

    protected StringPreference(String name, String defaultValue) {super(name, defaultValue);}

    @Override
    public void prepareEditField(Preference androidPreference) {
        EditTextPreference inputText = (EditTextPreference) androidPreference;
        inputText.setDialogTitle("title");
        inputText.setOnPreferenceChangeListener(super.buildChangeListener());
        inputText.setOnBindEditTextListener(buildBindListener(InputType.TYPE_CLASS_TEXT));
    }

    @Override
    protected String getValueFromPreference(SharedPreferences preferences) {
        return preferences.getString(getName(), null);
    }

    @Override
    protected void persistValueInPreference(SharedPreferences.Editor editor, String value) {
        editor.putString(getName(), value);
    }
}
