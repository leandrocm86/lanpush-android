package lcm.lanpush;

import android.app.ActivityManager;
import android.app.Application;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;

import androidx.core.app.JobIntentService;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import lcm.lanpush.utils.CDI;

public class LanpushApp extends Application {

    private static WeakReference<Context> context;
    private static final int RSS_JOB_ID = 1;

    @Override
    public void onCreate() {
        Log.i("Criando LanpushApp");
        super.onCreate();
        context = new WeakReference<>(getApplicationContext());
        CDI.set(this);
        PeriodicWorkRequest mywork =
                new PeriodicWorkRequest.Builder(LazaroWorker.class, 15, TimeUnit.MINUTES)
                        .setInitialDelay(1, TimeUnit.MINUTES)
                        .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(mywork);
    }

    @Override
    public void onTerminate() {
        Log.i("Terminando LanpushApp...");
        super.onTerminate();
        context.clear();
    }

    public static void restartService() {
        Log.i("Ordem recebida parar reiniciar worker.");
        OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(ListenningWorker.class)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(getContext()).enqueue(mywork);
//        Intent serviceIntent = new Intent();
//        JobIntentService.enqueueWork(getContext(), ListenningService.class, RSS_JOB_ID, serviceIntent);
//        if (isServiceRunning(ListenningService.class))
//            Log.i("Servico ja estava em execucao! Ignorando ordem...");
    }

    private static boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Context getContext() {
        if (CDI.get(LanpushApp.class) != null)
            return CDI.get(LanpushApp.class).getApplicationContext();
        else {
            Log.i("Não foi encontrada instância de LanpushApp no CDI! Utilizando contexto cacheado...");
            return context.get();
        }
    }
}
