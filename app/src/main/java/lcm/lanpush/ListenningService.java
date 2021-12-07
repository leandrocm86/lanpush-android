package lcm.lanpush;

import android.app.Activity;
import android.app.IntentService;
import android.app.job.JobParameters;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ListenningService extends JobIntentService {
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("Enfileirando listener via servico");
        Looper.prepare();
        new ClientListenning().run();
    }

    @Override
    public void onCreate() {
        Log.i("Criando servico...");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i("Destruindo servico...");
        super.onDestroy();
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.i("Parando servico...");
        return super.onStopCurrentWork();
    }
}