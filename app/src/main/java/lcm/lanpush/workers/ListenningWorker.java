package lcm.lanpush.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import lcm.lanpush.Log;
import lcm.lanpush.workers.LanpushWorker;

public class ListenningWorker extends LanpushWorker {
    private final Context context;

    public ListenningWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("Starting worker...");
        return super.doWork();
    }
}
