package lcm.lanpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

// Parou de funcionar no Android 12.
// O navegador só abre quando a aplicação está em primeiro plano, se for chamado de dentro do Receiver.
// Manterei a classe aqui e sua declaração no manifest comentada, para testar novamente no futuro.
// Por enquanto, a ação para o navegador será a principal da notificação e não terá opção separada.
// Sendo a opção principal, a notificação é limpa depois de clicada. Sendo ação, precisaria ser limpa num Receiver.
public class BrowserAction extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String mensagem = intent.getStringExtra("mensagem");
            int notificationId = intent.getIntExtra("notificationId", 0);
            String url = mensagem.replaceAll(" ", "+");
            if (!url.startsWith("http") && !url.startsWith("www.")) {
                url = "https://www.google.com/search?q=" + url;
            }
            Uri uri = Uri.parse(url);
            Intent browserAction = new Intent(Intent.ACTION_VIEW, uri);
            browserAction.setData(uri);
            browserAction.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i("Calling browser for " + uri.toString());
            context.startActivity(browserAction);
            Notificador.inst.cancelNotification(notificationId);
        }
        catch (Throwable t) {
            Log.e("Error while trying to open browser", t);
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
