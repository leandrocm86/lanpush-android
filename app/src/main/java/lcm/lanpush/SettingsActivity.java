package lcm.lanpush;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.preference.PreferenceFragmentCompat;

import lcm.lanpush.preferences.AutoStartPreference;
import lcm.lanpush.preferences.DebugHostPreference;
import lcm.lanpush.preferences.DebugPortPreference;
import lcm.lanpush.preferences.EnableDebugPreference;
import lcm.lanpush.preferences.IPsPreference;
import lcm.lanpush.preferences.LogLimitPreference;
import lcm.lanpush.preferences.PortPreference;
import lcm.lanpush.preferences.SleepPreference;
import lcm.lanpush.preferences.TimeoutPreference;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreatePreferenceActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            Log.d("onCreatePreferences");
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            if (LanpushApp.getMainActivity() != null)
                LanpushApp.getMainActivity().hideButton();

            IPsPreference.inst.prepareEditField(findPreference(IPsPreference.inst.getName()));
            PortPreference.inst.prepareEditField(findPreference(PortPreference.inst.getName()));
//            TimeoutPreference.inst.prepareEditField(findPreference(TimeoutPreference.inst.getName()));
            AutoStartPreference.inst.prepareEditField(findPreference(AutoStartPreference.inst.getName()));
            SleepPreference.inst.prepareEditField(findPreference(SleepPreference.inst.getName()));
            LogLimitPreference.inst.prepareEditField(findPreference(LogLimitPreference.inst.getName()));
            EnableDebugPreference.inst.prepareEditField(findPreference(EnableDebugPreference.inst.getName()));
            DebugHostPreference.inst.prepareEditField(findPreference((DebugHostPreference.inst.getName())));
            DebugPortPreference.inst.prepareEditField(findPreference(DebugPortPreference.inst.getName()));
        }

        @Override
        public void onDestroyView() {
            if (LanpushApp.getMainActivity() != null)
                LanpushApp.getMainActivity().showButton();
            super.onDestroyView();
        }
    }
}