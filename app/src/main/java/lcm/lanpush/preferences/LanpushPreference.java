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
    private T value;
    private T defaultValue;

    protected LanpushPreference(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    // Optional method to override. Does something when the preference value is changed.
    public void apply(T value) {}

    public void load() {
        apply(getValue());
    }

    public boolean changeValue(T value) {
        if (validate(value)) {
            Log.i("Changing setting '" + name + "' from " + this.value + " to " + value);
            this.value = value;
            saveValue(value);
            apply(value);
            return true;
        }
        return false;
    }

    public T getValue() {
        if (this.value == null) {
            try {
                this.value = getValueFromPreference(PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()));
            } catch (ClassCastException e) {
                PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).edit().clear().commit();
                Log.e("Saved setting type different than expected. Saved preferences are now restored.");
            }
            if (this.value == null) {
                this.value = defaultValue;
                saveValue(defaultValue);
                Log.d("Setting '" + name + "' not found. Using default value " + defaultValue);
            }
        }
        return this.value;
    }

    public T getDefaultValue() {
        return defaultValue;
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
                Log.d(name + " onPreferenceChange: " + newValue + (newValue.getClass().getName()));
                changeValue((T) newValue);
                return true;
            }
        };
    }

    protected EditTextPreference.OnBindEditTextListener buildBindListener(int inputType) {
        return new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                Log.d(getName() + " onBindEditText " + editText.getText().toString());
                editText.setInputType(inputType);
            }
        };
    }

    public String getName() {
        return name;
    }
    protected boolean validate(T value) {return true;}

    public abstract void prepareEditField(Preference androidPreference);
    protected abstract T getValueFromPreference(SharedPreferences preferences);
    protected abstract void persistValueInPreference(SharedPreferences.Editor editor, T value);
}
