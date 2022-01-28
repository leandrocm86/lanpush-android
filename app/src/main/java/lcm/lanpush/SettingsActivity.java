package lcm.lanpush;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import android.text.InputType;
import android.widget.EditText;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import lcm.lanpush.databinding.ActivityMainBinding;
import lcm.lanpush.utils.CDI;

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

            EditTextPreference timeoutPreferences = (EditTextPreference) findPreference("timeout");
            if (timeoutPreferences != null) {
                CDI.get(MainActivity.class, "main").hideButton();
                String timeoutText = LanpushApp.getPreference("timeout");
                timeoutPreferences.setText(timeoutText != null ? timeoutText : ClientListenning.DEFAULT_TIMEOUT + "");
                timeoutPreferences.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        Log.i("TIMEOUT onPreferenceChange: " + newValue + (newValue.getClass().getName()));
                        int novoTimeout = Integer.parseInt(newValue.toString());
                        LanpushApp.setIntPreference("timeout", novoTimeout);
                        ClientListenning.getInstance().setTimeout(novoTimeout);
                        return true;
                    }
                });
                timeoutPreferences.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                    @Override
                    public void onBindEditText(@NonNull EditText editText) {
                        Log.i("TIMEOUT onBindEditText " + editText.getText().toString());
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                });
            }
            else
                Log.e("Unable to show preferences!");
        }

        @Override
        public void onDestroyView() {
            CDI.get(MainActivity.class, "main").showButton();
            super.onDestroyView();
        }
    }
}