package lcm.lanpush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import lcm.lanpush.notification.Notificador;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Creating SelectionActivity");
        super.onCreate(savedInstanceState);
        handleSelectedText();
        finish();
    }

    private void handleSelectedText() {
        Log.d("Handling text selection...");
        try {
            String text = getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT);
            if (text != null)
                Log.i("Selected text to send: " + text);
            else {
                text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
                if (text != null)
                    Log.i("Shared link to send: " + text);
            }
            if (text != null) {
                String msg = text.trim();
                if (!msg.isEmpty()) {
                    Sender.inst().send(msg);
                    Notificador.inst.showToast("Text Sent!");
                }
            }
        } catch (Throwable t) {
            Log.e(t);
        }
    }
}