package lcm.lanpush;

import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

import lcm.lanpush.utils.Data;

public class LazaroWorker {/*extends Worker {
    private final Context context;

    public LazaroWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        if (!Data.madrugada()) {
            Log.i("Executando Lázaro...");
            try {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                if (!ClientListenning.getInstance().isRunning()) {
                    Log.i("Lázaro ressuscitando Worker...");
                    LanpushApp.restartService();
                } else if (System.currentTimeMillis() - ClientListenning.getInstance().getUltimaConexao() > ClientListenning.TIMEOUT) {
                    Log.i("Client diz que está rodando, mas já era pra ter dado timeout. Forçando reconexão...");
                    ClientListenning.getInstance().fecharConexao();
                    LanpushApp.restartService();
                }
            } catch (Throwable t) {
                Log.e("Erro na execução de Worker Periódico", t);
                return Result.failure();
            }
        } else {
            Log.i("Lázaro vê que é madrugada e volta a dormir...");
        }
        return Result.success();
    }*/
}
