package lcm.lanpush;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import android.text.InputType;
import android.widget.EditText;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import lcm.lanpush.preferences.IPsPreference;
import lcm.lanpush.preferences.LanpushPreference;
import lcm.lanpush.preferences.PortPreference;
import lcm.lanpush.preferences.TimeoutPreference;

public class SettingsActivity extends PreferenceActivity {

    private EditTextPreference timeoutPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreatePreferenceActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.settings, new SettingsFragment())
//                    .commit();
//        }
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            Log.i("onCreatePreferences");
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            loadPreference(IPsPreference.inst, InputType.TYPE_CLASS_TEXT);
            loadPreference(PortPreference.inst, InputType.TYPE_CLASS_NUMBER);
            loadPreference(TimeoutPreference.inst, InputType.TYPE_CLASS_NUMBER);
        }

        private void loadPreference(LanpushPreference preference, int type) {
            EditTextPreference textPreference = (EditTextPreference) findPreference(preference.getName());
            if (textPreference != null) {
                if (LanpushApp.getMainActivity() != null)
                    LanpushApp.getMainActivity().hideButton();
                textPreference.setText(preference.getValue());
                textPreference.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference androidPreference, Object newValue) {
                        Log.i(preference.getName() + " onPreferenceChange: " + newValue + (newValue.getClass().getName()));
                        preference.changeValue(newValue.toString());
                        return true;
                    }
                });
                textPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                    @Override
                    public void onBindEditText(@NonNull EditText editText) {
                        Log.i(preference.getName() + " onBindEditText " + editText.getText().toString());
                        editText.setInputType(type);
                    }
                });
            }
            else
                Log.e("Unable to show preference: " + preference.getName());
        }

        @Override
        public void onDestroyView() {
            if (LanpushApp.getMainActivity() != null)
                LanpushApp.getMainActivity().showButton();
            super.onDestroyView();
        }
    }
}