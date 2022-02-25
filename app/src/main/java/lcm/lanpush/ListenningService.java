package lcm.lanpush;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import lcm.lanpush.alarms.CheckAlarm;
import lcm.lanpush.utils.Data;

public class ListenningService extends Service {
    public void onCreate() {
        Log.i("LISTENNING SERVICE!");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LISTENNING SERVICE onStartCommand");
        CheckAlarm.inst.setAlarm(Data.timestampProximaManha());
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i("LISTENNING SERVICE onStart");
        CheckAlarm.inst.setAlarm(Data.timestampProximaManha());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}