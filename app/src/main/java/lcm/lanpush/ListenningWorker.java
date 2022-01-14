package lcm.lanpush;

import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

import lcm.lanpush.utils.Data;

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
        try {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            if (!Data.madrugada()) {
                if (!ClientListenning.getInstance().isRunning()) {
                    ClientListenning.getInstance().run();
                    enqueueNextWork();
                } else if (execucaoDemorada()) {
                    Log.i("Client diz que está rodando, mas já era pra ter dado timeout. Enviando auto-mensagem...");
                    Sender.send("[auto]", 1050);
                    Thread.sleep(1000);
                    if (!ClientListenning.getInstance().isRunning()) {
                        Log.i("Client parece ter parado. Religando...");
                        ClientListenning.getInstance().run();
                        enqueueNextWork();
                    } else if (execucaoDemorada()) {
                        Log.i("Auto-mensagem não surtiu efeito. Fechando a conexão e reiniciando...");
                        ClientListenning.getInstance().fecharConexao();
                        ClientListenning.getInstance().run();
                        enqueueNextWork();
                    }
                }
            }

            return Result.success();
        } catch (Throwable t) {
            Log.e("Erro na execução de ListenningWorker", t);
            return Result.failure();
        }
    }

    private boolean execucaoDemorada() {
        long ultimaConexao = ClientListenning.getInstance().getUltimaConexao();
        return ultimaConexao != 0 && System.currentTimeMillis() - ultimaConexao > 2*ClientListenning.getInstance().getTimeout();
    }

    private void enqueueNextWork() {
        OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(ListenningWorker.class)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(mywork);
    }
}
