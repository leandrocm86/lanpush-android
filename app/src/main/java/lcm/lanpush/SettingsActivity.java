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

            loadPreference(new IPsPreference(), InputType.TYPE_CLASS_TEXT);
            loadPreference(new PortPreference(), InputType.TYPE_CLASS_NUMBER);
            loadPreference(new TimeoutPreference(), InputType.TYPE_CLASS_NUMBER);
        }

        private void loadPreference(LanpushPreference preference, int type) {
            EditTextPreference textPreference = (EditTextPreference) findPreference(preference.getName());
            if (textPreference != null) {
                if (LanpushApp.getMainActivity() != null)
                    LanpushApp.getMainActivity().hideButton();
                String savedPreference = preference.getValue();
                textPreference.setText(savedPreference != null ? savedPreference : preference.getDefaultValue() + "");
                textPreference.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference androidPreference, Object newValue) {
                        Log.i(preference.getName() + " onPreferenceChange: " + newValue + (newValue.getClass().getName()));
                        preference.saveValue(newValue.toString());
                        preference.apply(newValue.toString());
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

        private interface PreferenceChangeAction {
            public void run(String newValue);
        }

        @Override
        public void onDestroyView() {
            if (LanpushApp.getMainActivity() != null)
                LanpushApp.getMainActivity().showButton();
            super.onDestroyView();
        }
    }
}