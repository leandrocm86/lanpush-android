package lcm.lanpush.preferences;

import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;

import java.util.regex.Pattern;

import lcm.lanpush.Log;
import lcm.lanpush.Sender;
import lcm.lanpush.notification.Notificador;

public class IPsPreference extends StringPreference {

    public static final IPsPreference inst = new IPsPreference();

    private IPsPreference() {
        super("ips", Sender.DEFAULT_HOSTS[0]);
    }

    @Override
    public void apply(String value) {
        Sender.inst().setHosts(value.trim().split(","));
    }

    @Override
    protected boolean validate(String value) {
        Pattern pattern = Pattern.compile("^[0-9,. ]++$");
        if (pattern.matcher(value).matches()) {
            return true;
        }
        else {
            Notificador.inst.showToast("Invalid value! IPs must be numeric and (if more than one) separated by comma.");
            return false;
        }
    }

    @Override
    protected EditTextPreference.OnBindEditTextListener buildBindListener(int inputType) {
        return new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                Log.i(getName() + " onBindEditText for IPs " + editText.getText().toString());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
            }
        };
    }
}
