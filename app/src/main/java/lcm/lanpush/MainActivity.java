package lcm.lanpush;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.view.View;

import androidx.core.app.JobIntentService;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import lcm.lanpush.databinding.ActivityMainBinding;
import lcm.lanpush.utils.CDI;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.i("Criando MainActivity");
            super.onCreate(savedInstanceState);
            CDI.set(this, "main");

            Notificador.init(this, getSystemService(NotificationManager.class));
            CDI.set(getSystemService(Context.CLIPBOARD_SERVICE));

            Intent servico = new Intent(this, ListenningService.class);

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            //        new Thread(new ClientListenning()).start();
            Intent serviceIntent = new Intent();
            //        this.startService(servico);
            final int RSS_JOB_ID = 1000;
            JobIntentService.enqueueWork(this, ListenningService.class, RSS_JOB_ID, serviceIntent);

            setSupportActionBar(binding.toolbar);

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            binding.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Snackbar.make(view, "Reconectando...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.i("Reconectando...");
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                Log.i("Instanciando Listener por nova Thread.");
                                new ClientListenning().run();
                            }
                        });
                        thread.start();
//                        Notificador.getInstance().showNotification("Notificação de click!");
                    } catch (Throwable t) {
                        Log.e(t);
                    }
                }
            });
        }
        catch(Throwable t) {
            Log.e(t);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        Log.i("Destruindo MainActivity");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i("Parando MainActivity");
        super.onStop();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        Log.i("PostCreate MainActivity");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        Log.i("PostResume MainActivity");
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        Log.i("Start MainActivity");
        super.onStart();
    }
}