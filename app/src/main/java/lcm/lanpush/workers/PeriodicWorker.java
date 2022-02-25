package lcm.lanpush.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;
import lcm.lanpush.workers.LanpushWorker;

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

    public static void setPeriodicWorker() {
        PeriodicWorkRequest listener =
                new PeriodicWorkRequest.Builder(PeriodicWorker.class,
                        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS,
                        PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        WorkManager.getInstance(LanpushApp.getContext()).enqueueUniquePeriodicWork("Listener", ExistingPeriodicWorkPolicy.REPLACE, listener);
    }

}
