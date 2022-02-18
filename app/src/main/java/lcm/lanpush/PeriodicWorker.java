package lcm.lanpush;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

public class PeriodicWorker extends LanpushWorker {
    private final Context context;

    public PeriodicWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("Starting periodic worker...");
        return super.doWork();
    }
}
