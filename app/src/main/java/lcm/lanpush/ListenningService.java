package lcm.lanpush;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.app.job.JobParameters;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import lcm.lanpush.utils.Data;

public class ListenningService extends Service {
    Alarm alarm = new Alarm();
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.setAlarm(Data.timestampProximaManha());
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        alarm.setAlarm(Data.timestampProximaManha());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}