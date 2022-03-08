package lcm.lanpush;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.preference.PreferenceFragmentCompat;

import lcm.lanpush.preferences.DebugPortPreference;
import lcm.lanpush.preferences.EnableDebugPreference;
import lcm.lanpush.preferences.IPsPreference;
import lcm.lanpush.preferences.PortPreference;
import lcm.lanpush.preferences.TimeoutPreference;

public class SettingsActivity extends PreferenceActivity {

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

            if (LanpushApp.getMainActivity() != null)
                LanpushApp.getMainActivity().hideButton();

            IPsPreference.inst.fillPreferenceField(findPreference(IPsPreference.inst.getName()));
            PortPreference.inst.fillPreferenceField(findPreference(PortPreference.inst.getName()));
            TimeoutPreference.inst.fillPreferenceField(findPreference(TimeoutPreference.inst.getName()));
            EnableDebugPreference.inst.fillPreferenceField(findPreference(EnableDebugPreference.inst.getName()));
            DebugPortPreference.inst.fillPreferenceField(findPreference(DebugPortPreference.inst.getName()));
        }

        @Override
        public void onDestroyView() {
            if (LanpushApp.getMainActivity() != null)
                LanpushApp.getMainActivity().showButton();
            super.onDestroyView();
        }
    }
}