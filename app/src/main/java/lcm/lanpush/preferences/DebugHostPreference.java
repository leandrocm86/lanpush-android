package lcm.lanpush.preferences;

import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import java.util.regex.Pattern;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;
import lcm.lanpush.R;
import lcm.lanpush.Sender;
import lcm.lanpush.notification.Notificador;

public class DebugHostPreference extends StringPreference {

    public static final DebugHostPreference inst = new DebugHostPreference();

    private DebugHostPreference() {
       super(LanpushApp.get(R.string.settings_debug_ip_key),
               LanpushApp.get(R.string.settings_debug_ip_default));
    }

    @Override
    public void apply(String value) {
        Sender.inst().setDebugHost(value.trim());
    }

    @Override
    protected boolean validate(String value) {
        Pattern pattern = Pattern.compile("^[0-9,. ]++$");
        if (pattern.matcher(value).matches()) {
            return true;
        }
        else {
            Notificador.inst.showToast("Invalid value! IP must be numeric.");
            return false;
        }
    }

    @Override
    protected EditTextPreference.OnBindEditTextListener buildBindListener(int inputType) {
        return new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                Log.d(getName() + " onBindEditText for DEBUG IP " + editText.getText().toString());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
            }
        };
    }

    @Override
    public void prepareEditField(Preference androidPreference) {
        super.prepareEditField(androidPreference);
        ((EditTextPreference) androidPreference).setDialogTitle("IP of the host to receive debug messages.");
    }
}
