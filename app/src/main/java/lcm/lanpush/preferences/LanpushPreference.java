package lcm.lanpush.preferences;

import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;

public abstract class LanpushPreference<T> {
    private String name;
    private T defaultValue;

    protected LanpushPreference(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public abstract void apply(T value);

    public void load() {
        apply(getValue());
    }

    public boolean changeValue(T value) {
        if (validate(value)) {
            saveValue(value);
            apply(value);
            return true;
        }
        return false;
    }

    public T getValue() {
        T value = null;
        try {
            value = getValueFromPreference(PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()));
        } catch (ClassCastException e) {
            Log.e("Saved preference type different than expected. Preferences will be cleared.");
            PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).edit().clear().commit();
        }
        if (value == null) {
            Log.e("Preference not found, using default value for " + name);
            saveValue(defaultValue);
            Log.i("Saved default value " + defaultValue);
            value = defaultValue;
        }
        return value;
    }

    private void saveValue(T value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).edit();
        persistValueInPreference(editor, value);
        editor.commit();
    }

    protected Preference.OnPreferenceChangeListener buildChangeListener() {
        return new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference androidPreference, Object newValue) {
                Log.i(name + " onPreferenceChange: " + newValue + (newValue.getClass().getName()));
                changeValue((T) newValue);
                return true;
            }
        };
    }

    protected EditTextPreference.OnBindEditTextListener buildBindListener(int inputType) {
        return new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                Log.i(getName() + " onBindEditText " + editText.getText().toString());
                editText.setInputType(inputType);
            }
        };
    }

    public String getName() {
        return name;
    }
    protected boolean validate(T value) {return true;}

    public abstract void fillPreferenceField(Preference androidPreference);
    protected abstract T getValueFromPreference(SharedPreferences preferences);
    protected abstract void persistValueInPreference(SharedPreferences.Editor editor, T value);
}
