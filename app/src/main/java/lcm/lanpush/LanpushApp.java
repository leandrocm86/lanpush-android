package lcm.lanpush;

import android.app.ActivityManager;
import android.app.Application;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import android.content.ClipboardManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.core.app.JobIntentService;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class LanpushApp extends Application {

    private static WeakReference<Context> context;
    private static WeakReference<MainActivity> mainActivity;
    private static WeakReference<TextView> textView;

    @Override
    public void onCreate() {
        Log.i("Creating LanpushApp");
        super.onCreate();
        context = new WeakReference<>(getApplicationContext());
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
//        Notificador.getInstance().showNotification("Test");
    }

    @Override
    public void onTerminate() {
        Log.i("Terminating LanpushApp...");
        context.clear();
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        Log.i("OnLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.i("OnTrimMemory: Level " + level + " - " + descriptionForMemoryLevel(level));
        ActivityManager.MemoryInfo memoryInfo = getMemoryInfo();
        Log.i("LowMemory: " + memoryInfo.lowMemory + ", used " + (Math.round(100-memoryInfo.availMem*100/memoryInfo.totalMem)) + "%");
        if (level >= 40) {
            Notificador.clean();
            memoryInfo = getMemoryInfo();
            Log.i("Objects cleared. LowMemory: " + memoryInfo.lowMemory + ", used " + (Math.round(100-memoryInfo.availMem*100/memoryInfo.totalMem)) + "%");
        }
        super.onTrimMemory(level);
    }

    private ActivityManager.MemoryInfo getMemoryInfo() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    private String descriptionForMemoryLevel(int level) {
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                return "RUNNING MODERATE";
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                return "RUNNING LOW";
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                return "RUNNING CRITICAL";
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                return "UI HIDDEN";
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                return "BACKGROUND";
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                return "MODERATE";
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                return "COMPLETE";

            default: return "?";
        }
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
        return context.get();
    }

    public static void saveMainActivity(MainActivity activity) {
        mainActivity = new WeakReference<MainActivity>(activity);
    }

    public static MainActivity getMainActivity() {
        return mainActivity != null ? mainActivity.get() : null;
    }

    public static void saveTextView(TextView view) {
        textView = new WeakReference<TextView>(view);
    }

    public static TextView getTextView() {
        return textView != null ? textView.get() : null;
    }

    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    public static ClipboardManager getClipboard() {
        return (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
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
