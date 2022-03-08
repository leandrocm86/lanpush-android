package lcm.lanpush;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import lcm.lanpush.alarms.CheckAlarm;
import lcm.lanpush.alarms.PeriodicCheckAlarm;
import lcm.lanpush.databinding.ActivityMainBinding;
import lcm.lanpush.notification.Notificador;
import lcm.lanpush.workers.LanpushWorker;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.i("Creating MainActivity");
            super.onCreate(savedInstanceState);
            LanpushApp.saveMainActivity(this);

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            setSupportActionBar(binding.toolbar);

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            binding.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.input.setVisibility(View.VISIBLE);
                    if (binding.input.requestFocus()){
                        getInputManager().showSoftInput(binding.input, InputMethodManager.SHOW_IMPLICIT);
                    }
                    hideButton();
                }
            });

            binding.input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        getInputManager().hideSoftInputFromWindow(binding.input.getWindowToken(), 0);
                        showButton();
                        binding.input.setVisibility(View.GONE);
                        Sender.inst().send(binding.input.getText().toString());
                        Notificador.inst.showToast("Sent!");
                        binding.input.setText("");
                        return true;
                    }
                    else return false;
                }
            });
        }
        catch(Throwable t) {
            Log.e(t);
        }
    }

    public void hideButton() {
        binding.fab.setVisibility(View.GONE);
    }

    public void showButton() {
        binding.fab.setVisibility(View.VISIBLE);
    }

    public InputMethodManager getInputManager() {
        return (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public NotificationManager getNotificationManager() {
        return getSystemService(NotificationManager.class);
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
        if (id == R.id.action_reconnection) {
            try {
                Log.i("Reconectando...");
                Notificador.inst.showToast("Reconectando...");
                LanpushApp.restartWorker();
            } catch (Throwable t) {
                Log.e(t);
            }
        }
        else if (id == R.id.action_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//            NavigationUI.navigateUp(navController, appBarConfiguration);
            navController.navigate(R.id.settings);
        }
        else if (id == R.id.action_about) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.about)
                    .setPositiveButton(R.string.about_git, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton(R.string.about_close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .setIcon(R.drawable.lanpush_small);
            // Create the AlertDialog object and return it
            builder.create().show();
        }
        else if (id == R.id.action_close) {
            Log.i("Closing the application...");
            LanpushWorker.cancel();
            CheckAlarm.inst.cancel();
            PeriodicCheckAlarm.inst.cancel();
            Sender.inst().send("[stop]");
            finishAffinity();
            finishAndRemoveTask();
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
        Log.i("Destroying MainActivity");
        super.onDestroy();
//        CDI.clear();
    }

    @Override
    protected void onStop() {
        Log.i("Stopping MainActivity");
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