package lcm.lanpush;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
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
        Log.i("Instanciando Listener via serviço");
        Looper.prepare();
        new ClientListenning().run();
    }
}