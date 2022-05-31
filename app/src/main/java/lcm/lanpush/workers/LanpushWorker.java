package lcm.lanpush.workers;

import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;
import lcm.lanpush.Receiver;
import lcm.lanpush.Sender;
import lcm.lanpush.alarms.CheckAlarm;
import lcm.lanpush.preferences.SleepPreference;
import lcm.lanpush.utils.Data;

public class LanpushWorker extends Worker {
    private final Context context;

    public LanpushWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            if (SleepPreference.inst.getValue() && Data.madrugada()) {
                Log.d("Time to sleep. Application will be shutdown and scheduled to wake by the morning.");
                LanpushApp.close();
                CheckAlarm.inst.setAlarm(Data.timestampProximaManha());
            }
            else {
                if (!Receiver.inst.isRunning()) {
                    listen();
                } else if (execucaoDemorada()) {
                    Log.d("Receiver seems to be listening, but timeout expired. Sending reconnect message...");
                    Sender.inst().send("[reconnect]");
                    Thread.sleep(1000);
                    if (!Receiver.inst.isRunning()) {
                        Log.d("Receiver seems to have stopped. Restarting it...");
                        listen();
                    } else if (execucaoDemorada()) {
                        // Sometimes timeout expires but the Receiver is still listening.
                        // That's why we send a 'reconnect' message to test it.
                        // Here is the case that it wasn't listening after all. I never saw it happen, but this is here just in case.
                        Log.d("The reconnect message seems to have been ignored by the receiver. Closing connection and restarting...");
                        Receiver.inst.fecharConexao();
                        listen();
                    }
                }
            }

            return Result.success();
        } catch (Throwable t) {
            Log.e("Error executing Worker", t);
            return Result.failure();
        }
    }

    private void listen() {
        if (Receiver.inst.run())
            enqueueNextWork();
    }

    private boolean execucaoDemorada() {
        long ultimaConexao = Receiver.inst.getUltimaConexao();
        return ultimaConexao != 0 && System.currentTimeMillis() - ultimaConexao > 2* Receiver.inst.getTimeout();
    }

    private void enqueueNextWork() {
        OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(LanpushWorker.class)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(mywork);
    }

    public static void cancel() {
        WorkManager.getInstance(LanpushApp.getContext()).cancelAllWork();
    }
}
