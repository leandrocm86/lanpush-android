package lcm.lanpush.preferences;

import lcm.lanpush.Log;

public class AutoStartPreference extends BooleanPreference {

    public static final AutoStartPreference inst = new AutoStartPreference();

    private AutoStartPreference() {
        super("autoStart", Boolean.TRUE);
    }

    @Override
    public void apply(Boolean value) { 
        Log.i("AutoStart preference changed to " + value);
    }
}
