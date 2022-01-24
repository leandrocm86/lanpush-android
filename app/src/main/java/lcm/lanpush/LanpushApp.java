package lcm.lanpush;

import android.app.ActivityManager;
import android.app.Application;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.JobIntentService;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import lcm.lanpush.utils.CDI;

public class LanpushApp extends Application {

    private static WeakReference<Context> context;
//    private static final int RSS_JOB_ID = 1;

    @Override
    public void onCreate() {
        Log.i("Creating LanpushApp");
        super.onCreate();
        context = new WeakReference<>(getApplicationContext());
        CDI.set(this);
        CDI.set(getSystemService(Context.CLIPBOARD_SERVICE));
        Integer timeout = getIntPreference("timeout");
        if (timeout != null) {
            ClientListenning.getInstance().setTimeout(timeout);
        }

        PeriodicWorkRequest listener =
                new PeriodicWorkRequest.Builder(ListenningWorker.class,
                        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS,
                        PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        WorkManager.getInstance(getContext()).enqueueUniquePeriodicWork("Listener", ExistingPeriodicWorkPolicy.KEEP, listener);
//        PeriodicWorkRequest lazaro =
//                new PeriodicWorkRequest.Builder(LazaroWorker.class,
//                        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS,
//                        PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS)
//                        .build();
//        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("Lazaro", ExistingPeriodicWorkPolicy.KEEP, lazaro);
//        new Alarm().setAlarm(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        Log.i("Terminating LanpushApp...");
        super.onTerminate();
        context.clear();
    }

    public static void restartService() {
        Log.i("Order received to restart worker.");
        PeriodicWorkRequest listener =
                new PeriodicWorkRequest.Builder(ListenningWorker.class,
                        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS,
                        PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        WorkManager.getInstance(getContext()).enqueueUniquePeriodicWork("Listener", ExistingPeriodicWorkPolicy.REPLACE, listener);
//        OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(ListenningWorker.class)
//                .setInitialDelay(1, TimeUnit.SECONDS)
//                .build();
//        WorkManager.getInstance(getContext()).enqueue(mywork);
//        Intent serviceIntent = new Intent();
//        JobIntentService.enqueueWork(getContext(), ListenningService.class, RSS_JOB_ID, serviceIntent);
//        if (isServiceRunning(ListenningService.class))
//            Log.i("Servico ja estava em execucao! Ignorando ordem...");
    }

//    private static boolean isServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static Context getContext() {
        if (CDI.get(LanpushApp.class) != null)
            return CDI.get(LanpushApp.class).getApplicationContext();
        else {
            Log.i("LanpushApp instance not found! Using cached context...");
            return context.get();
        }
    }

    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    public static String getPreference(String key) {
        String value = null;
        try {
            value = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key, null);
            if (value == null) {
                Log.e("Preference not found: " + key);
            }
        }
        catch (ClassCastException e) {
            Log.e("Preference saved was not a String. Clearing the preferences...");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            editor.clear();
            editor.commit();
        }
        return value;
    }

    public static Integer getIntPreference(String key) {
        String value = getPreference(key);
        if (value == null)
            return null;
        else {
            return Integer.parseInt(value);
        }
    }

    public static void setIntPreference(String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putString(key, value + "");
        editor.commit();
    }
}
