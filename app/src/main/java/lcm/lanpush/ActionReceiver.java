package lcm.lanpush;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationManagerCompat;

import lcm.lanpush.utils.CDI;

public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getStringExtra("action");
            String mensagem = intent.getStringExtra("mensagem");
            if (action.equals("clipboard")) {
                ClipboardManager clipboard = CDI.get(ClipboardManager.class);
                ClipData clip = ClipData.newPlainText(mensagem, mensagem);
                clipboard.setPrimaryClip(clip);
                Notificador.getInstance().showToast("Copiado: " + mensagem);
            }
            else {
                String url = mensagem;
                if (!url.startsWith("http") && !url.startsWith("www.")) {
                    url = "https://www.google.com/search?q=" + url;
                }
                Uri uri = Uri.parse(url);
                Intent browserAction = new Intent(Intent.ACTION_VIEW, uri);
                browserAction.setData(uri);
                browserAction.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserAction);
                //PendingIntent pIntentBrowser = PendingIntent.getActivity(context,notificationId+2, browserAction, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            //This is used to close the notification tray
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
            NotificationManagerCompat.from(context.getApplicationContext()).cancelAll();
        }
        catch (Throwable t) {
            Log.e(t);
        }
    }
}
