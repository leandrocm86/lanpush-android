package lcm.lanpush;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class Notificador {
    private static int notificationId = 0;
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
            channel.setDescription("notificacoes lanpush");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
            Log.i("canal de notificacao criado");
        }
    }

    public static Notificador getInstance() {
        if (instance == null)
            instance = new Notificador(LanpushApp.getContext());
        return instance;
    }

    public void showNotification(String msg) {
        Log.i("Preparando para exibir notificação '" + msg + "'");
        Intent clipboardAction = new Intent(context, ActionReceiver.class);
        clipboardAction.putExtra("action","clipboard");
        clipboardAction.putExtra("mensagem", msg);
        PendingIntent pIntentClipboard = PendingIntent.getBroadcast(context, notificationId+1, clipboardAction, PendingIntent.FLAG_IMMUTABLE);

        Intent browserAction = new Intent(context, ActionReceiver.class);
        browserAction.putExtra("action","browser");
        browserAction.putExtra("mensagem", msg);
        PendingIntent pIntentBrowser = PendingIntent.getBroadcast(context, notificationId+2, browserAction, PendingIntent.FLAG_IMMUTABLE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "lanpush")
                .setSmallIcon(R.drawable.lanpush_small)
                .setContentTitle("LANPUSH")
                .setContentText(msg)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_foreground, "Copiar", pIntentClipboard)
                .addAction(R.drawable.ic_launcher_foreground, "Navegar", pIntentBrowser);
        // notificationId is a unique int for each notification that you must define
        notificationId += 3;
        notificationManager.notify(notificationId, builder.build());
    }

    public void showToast(String msg) {
        Log.i("[TOAST] " + msg);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

//    public void showSnackbar(String msg) {
//        Snackbar snackbar = Snackbar.make((View) CDI.get("rootView"), msg, Snackbar.LENGTH_LONG);
//        snackbar.show();
//    }

}

