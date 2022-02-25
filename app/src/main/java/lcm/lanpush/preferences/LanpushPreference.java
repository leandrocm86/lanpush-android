package lcm.lanpush.preferences;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;

public abstract class LanpushPreference {
    private String name;
    private String defaultValue;

    protected LanpushPreference(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public abstract void apply(String value);

    public void load() {
        apply(getValue());
    }

    public void changeValue(String value) {
        saveValue(value);
        apply(value);
    }

    public String getValue() {
        String value = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).getString(name, null);
        if (value == null) {
            Log.e("Preference not found, using default value for " + name);
            saveValue(defaultValue);
            Log.i("Saved default value " + defaultValue);
            value = defaultValue;
        }
        return value;
    }

    private void saveValue(String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).edit();
        editor.putString(name, value);
        editor.commit();
    }

    public String getName() {
        return name;
    }
}
