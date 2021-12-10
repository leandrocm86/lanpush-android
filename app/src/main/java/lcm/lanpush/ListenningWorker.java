package lcm.lanpush;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class ListenningWorker extends Worker {
    private final Context context;

    public ListenningWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("Iniciando worker...");
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        ClientListenning.getInstance().run();
        OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(ListenningWorker.class)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(mywork);

//        Looper.loop();
        return Result.success();
    }
}
