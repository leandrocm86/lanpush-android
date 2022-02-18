package lcm.lanpush.preferences;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;

public abstract class LanpushPreference {
    private String name;
    private String defaultValue;
    public abstract void apply(Object value);

    protected LanpushPreference(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public void load() {
        Object value = getPreference(name);
        if (value != null) {
            apply(value);
        } else {
            Log.i("Saving default value " + defaultValue);
            saveValue(defaultValue);
        }
    }

    public void saveValue(String value) {
        savePreference(name, value);
    }

    public String getValue() {
        return getPreference(name);
    }

    public static String getPreference(String key) {
        String value = null;
        try {
            value = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).getString(key, null);
            if (value == null) {
                Log.e("Preference not found: " + key);
            }
        }
        catch (ClassCastException e) {
            Log.e("Preference saved was not a String. Clearing the preferences...");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).edit();
            editor.clear();
            editor.commit();
        }
        return value;
    }

    public static Integer getIntPreference(String key) {
        String value = getPreference(key);
        if (value == null)
            return null;
        return Integer.parseInt(value);
    }

    public static String[] getListPreference(String key) {
        String value = getPreference(key);
        if (value == null)
            return null;
        return value.split(",");
    }

    public static void savePreference(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LanpushApp.getContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveIntPreference(String key, int value) {
        savePreference(key, value + "");
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
