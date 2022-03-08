package lcm.lanpush.preferences;

import lcm.lanpush.Log;

public class EnableDebugPreference extends BooleanPreference {

    public static final EnableDebugPreference inst = new EnableDebugPreference();

    private EnableDebugPreference() {
        super("enableDebug", Boolean.FALSE);
    }

    @Override
    public void apply(Boolean value) {
        Log.enableDebug(value);
    }
}
