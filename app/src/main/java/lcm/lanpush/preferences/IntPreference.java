package lcm.lanpush.preferences;

import android.content.SharedPreferences;
import android.text.InputType;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import lcm.lanpush.Log;
import lcm.lanpush.notification.Notificador;

public abstract class IntPreference extends LanpushPreference<Integer> {

    protected IntPreference(String name, Integer defaultValue) {super(name, defaultValue);}

    @Override
    public void fillPreferenceField(Preference androidPreference) {
        EditTextPreference inputText = (EditTextPreference) androidPreference;
//        inputText.setText(super.getValue() + "");
        inputText.setOnPreferenceChangeListener(this.buildChangeListener());
        inputText.setOnBindEditTextListener(super.buildBindListener(InputType.TYPE_CLASS_NUMBER));
    }

    // Integer preferences are actually being saved as text...

    @Override
    protected Integer getValueFromPreference(SharedPreferences preferences) {
        String value = preferences.getString(getName(), null);
        return value != null ? Integer.parseInt(value) : null;
    }

    @Override
    protected void persistValueInPreference(SharedPreferences.Editor editor, Integer value) {
        editor.putString(getName(), value.toString());
    }

    @Override
    protected Preference.OnPreferenceChangeListener buildChangeListener() {
        return new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference androidPreference, Object newValue) {
                Log.i(getName() + " onPreferenceChange (int): " + newValue + (newValue.getClass().getName()));
                try {
                    return changeValue(Integer.parseInt(newValue.toString()));
                }
                catch (NumberFormatException e) {
                    Log.e("Attempt to set invalid value for integer preference: " + newValue.toString());
                    Notificador.inst.showToast("Invalid value! Must be an integer.");
                    return false;
                }
            }
        };
    }
}
