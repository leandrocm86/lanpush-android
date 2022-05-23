package lcm.lanpush.preferences;

public class SleepPreference extends BooleanPreference {

    public static final SleepPreference inst = new SleepPreference();

    private SleepPreference() {
        super("sleep", Boolean.FALSE);
    }

    @Override
    public void apply(Boolean value) {}
}
