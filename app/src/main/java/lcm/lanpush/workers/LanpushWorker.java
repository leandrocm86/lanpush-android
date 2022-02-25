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

            if (!Data.madrugada()) {
                if (!Receiver.inst.isRunning()) {
                    listen();
                } else if (execucaoDemorada()) {
                    Log.i("Client diz que está rodando, mas já era pra ter dado timeout. Enviando auto-mensagem...");
                    Sender.inst().send("[auto]");
                    Thread.sleep(1000);
                    if (!Receiver.inst.isRunning()) {
                        Log.i("Client parece ter parado. Religando...");
                        listen();
                    } else if (execucaoDemorada()) {
                        Log.i("Auto-mensagem não surtiu efeito. Fechando a conexão e reiniciando...");
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
