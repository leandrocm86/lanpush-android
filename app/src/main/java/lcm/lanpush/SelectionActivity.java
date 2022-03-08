package lcm.lanpush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import lcm.lanpush.notification.Notificador;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Criando SelectionActivity");
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_selection);
        trataSelecao();
        finish();
    }

    private void trataSelecao() {
        Log.i("Tratando selecao...");
        try {
            CharSequence text = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            Log.i("Selecionado: " + text);
            if (text != null) {
                String msg = text.toString().trim();
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