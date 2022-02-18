package lcm.lanpush;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

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
