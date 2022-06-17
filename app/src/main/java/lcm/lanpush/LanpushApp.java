package lcm.lanpush;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import lcm.lanpush.alarms.CheckAlarm;
import lcm.lanpush.alarms.PeriodicCheckAlarm;
import lcm.lanpush.preferences.EnableDebugPreference;
import lcm.lanpush.preferences.PortPreference;
import lcm.lanpush.utils.Data;
import lcm.lanpush.workers.LanpushWorker;
import lcm.lanpush.workers.ListenningWorker;
import lcm.lanpush.workers.PeriodicWorker;

public class LanpushApp extends Application {

    private static long lastConnectionCheck;

    private static WeakReference<Context> context;
    private static WeakReference<MainActivity> mainActivity;
    private static WeakReference<LogFragment> logView;

    @Override
    public void onCreate() {
        try {
            super.onCreate();
            context = new WeakReference<>(getApplicationContext());
            EnableDebugPreference.inst.load();
            Log.loadMessages();
            start();
        }
        catch(Throwable t) {
            Log.e(t);
        }
    }

    public static void start() {
        Log.d("---------- Starting LanpushApp ----------");
        PortPreference.inst.load();
        restartWorker();
        PeriodicWorker.setPeriodicWorker();
        PeriodicCheckAlarm.inst.setPeriodicAlarm();
    }

    public static void close(boolean ignoreLogs) {
        if (!ignoreLogs)
            Log.i("Closing the application...");
        LanpushWorker.cancel();
        CheckAlarm.inst.cancel();
        PeriodicCheckAlarm.inst.cancel();
        Sender.inst().send("[stop]");
        if (!ignoreLogs)
            Log.saveMessages();
    }

    @Override
    public void onTerminate() {
        Log.d("Terminating LanpushApp...");
        context.clear();
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        Log.d("OnLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d("OnTrimMemory: Level " + level);
        if (level >= 40) {
            if (System.currentTimeMillis() - lastConnectionCheck > 60000) {
                ActivityManager.MemoryInfo memoryInfo = getMemoryInfo();
                Log.d("LowMemory: " + memoryInfo.lowMemory + ", used " + (Math.round(100-memoryInfo.availMem*100/memoryInfo.totalMem)) + "%");
                if (!Data.isSleepTime())
                    CheckAlarm.inst.setAlarm(System.currentTimeMillis() + 60000);
                Log.saveMessages();
                lastConnectionCheck = System.currentTimeMillis();
            }
//            Log.i("Shutting down...");
//            shutdown();
        }
    }

    private ActivityManager.MemoryInfo getMemoryInfo() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    /*private String descriptionForMemoryLevel(int level) {
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
    }*/

    public static void restartWorker() {
        Log.d("Restarting worker...");
        OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(ListenningWorker.class)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(getContext()).enqueue(mywork);
    }

    public static Context getContext() {
        return context.get();
    }

    public static String get(int key) {
        return getContext().getString(key);
    }

    public static void saveMainActivity(MainActivity activity) {
        mainActivity = new WeakReference<MainActivity>(activity);
    }

    public static MainActivity getMainActivity() {
        return mainActivity != null ? mainActivity.get() : null;
    }

    public static void saveLogView(LogFragment view) {
        logView = new WeakReference<LogFragment>(view);
    }

    public static void clearLogView() {
        logView = null;
    }

    public static LogFragment getLogView() {
        return logView != null ? logView.get() : null;
    }

    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    public static ClipboardManager getClipboard() {
        return (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    }

}
