package lcm.lanpush;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notificador {
    private static int notificationId = Math.round(System.currentTimeMillis() / 60000);
    private Context context;
    private NotificationManager notificationManager;
    private static Notificador instance;

    private Notificador(Context context) {
        this.context = context;
        this.notificationManager = context.getSystemService(NotificationManager.class);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("lanpush", "lanpush", importance);
            channel.setDescription("Lanpush notifications");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
            Log.i("Created notification channel");
        }
    }

    public static Notificador getInstance() {
        if (instance == null)
            instance = new Notificador(LanpushApp.getContext());
        return instance;
    }

    public void cancelNotification(int notificationId) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context.getApplicationContext());
        manager.cancel(notificationId);
        manager.cancel(notificationId+1); // ClipboardAction
        manager.cancel(notificationId+2); // BrowserAction
        manager.cancel(notificationId+3); // Notification
    }

    public static void clean() {
        instance = null;
    }

    public void showNotification(String msg) {
        Log.i("Preparing to show notification '" + msg + "'");

        // Intent que traz a aplicação para primeiro plano.
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "lanpush")
                .setSmallIcon(R.drawable.lanpush_small)
                .setContentTitle("LANPUSH")
                .setContentText(msg)
//                .setContentIntent(contentIntent)
                .setContentIntent(getBrowserIntent(msg))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_foreground, "Copy", getClipboardIntent(msg));
//                .addAction(R.drawable.ic_launcher_foreground, "Browse", getBrowserIntent(msg));
        // notificationId is a unique int for each notification that you must define
        notificationId += 3;
        notificationManager.notify(notificationId, builder.build());
    }

    private PendingIntent getClipboardIntent(String mensagem) {
        Intent clipboardIntent = new Intent(context, ClipboardAction.class);
        clipboardIntent.putExtra("mensagem", mensagem);
        clipboardIntent.putExtra("notificationId", notificationId);
        return PendingIntent.getBroadcast(context, notificationId+1, clipboardIntent, PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getBrowserIntent(String mensagem) {
        String url = mensagem.replaceAll(" ", "+");
        if (!url.startsWith("http") && !url.startsWith("www.")) {
            url = "https://www.google.com/search?q=" + url;
        }
        Uri uri = Uri.parse(url);
        Intent browserAction = new Intent(Intent.ACTION_VIEW, uri);
        browserAction.setData(uri);
        browserAction.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Log.i("Calling browser for " + uri.toString());
        return PendingIntent.getActivity(context, notificationId+2, browserAction, PendingIntent.FLAG_IMMUTABLE);
//        Intent browserIntent = new Intent(context, BrowserAction.class);
//        browserIntent.putExtra("mensagem", mensagem);
//        browserIntent.putExtra("notificationId", notificationId);
//        return PendingIntent.getBroadcast(context, notificationId+2, browserIntent, PendingIntent.FLAG_IMMUTABLE);
    }

    public void showToast(String msg) {
        Log.i("[TOAST] " + msg);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}

