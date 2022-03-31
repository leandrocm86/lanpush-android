package lcm.lanpush.notification;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import lcm.lanpush.LanpushApp;
import lcm.lanpush.Log;


public class ClipboardAction extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String mensagem = intent.getStringExtra("mensagem");
            int notificationId = intent.getIntExtra("notificationId", 0);
            ClipboardManager clipboard = LanpushApp.getClipboard();
            ClipData clip = ClipData.newPlainText(mensagem, mensagem);
            clipboard.setPrimaryClip(clip);
            Notificador.inst.showToast("Copiado: " + (mensagem.length() < 20 ? mensagem : mensagem.substring(0, 40) + "..."));
//            NotificationManagerCompat.from(context.getApplicationContext()).cancelAll();
            Log.d("Cancelando notificacoes " + notificationId);
            Notificador.inst.cancelNotification(notificationId);
//            collpasePanel(context);
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
