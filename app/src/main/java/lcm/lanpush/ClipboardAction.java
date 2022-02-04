package lcm.lanpush;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;


public class ClipboardAction extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String mensagem = intent.getStringExtra("mensagem");
            ClipboardManager clipboard = LanpushApp.getClipboard();
            ClipData clip = ClipData.newPlainText(mensagem, mensagem);
            clipboard.setPrimaryClip(clip);
            Notificador.getInstance().showToast("Copiado: " + (mensagem.length() < 20 ? mensagem : mensagem.substring(0, 30) + "..."));
            NotificationManagerCompat.from(context.getApplicationContext()).cancelAll();
            collpasePanel(context);
        }
        catch (Throwable t) {
            Log.e("Error while trying to copy message to clipboard", t);
        }
    }

    // This is used to close the notification tray
    private static void collpasePanel(Context context) {
        if (Build.VERSION.SDK_INT < 31) {
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
        }
        // else use GLOBAL_ACTION_DISMISS_NOTIFICATION_SHADE action from Accessibility Services.
    }
}
