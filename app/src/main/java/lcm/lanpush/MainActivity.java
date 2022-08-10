package lcm.lanpush;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import lcm.lanpush.alarms.GoodMorningAlarm;
import lcm.lanpush.databinding.ActivityMainBinding;
import lcm.lanpush.notification.Notificador;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d("Creating MainActivity");
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
                        String textToSend = binding.input.getText().toString();
                        if (!textToSend.isEmpty()) {
                            Log.i("Sending text: " + textToSend);
                            Sender.inst().send(textToSend);
                        }
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

//        if (id == R.id.action_reconnection) {
//            try {
//                Log.d("Reconectando...");
//                Notificador.inst.showToast("Reconectando...");
//                LanpushApp.restartWorker();
//            } catch (Throwable t) {
//                Log.e(t);
//            }
//        }
        if (id == R.id.action_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.settings);
        }
        else if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.about)
                .setPositiveButton(R.string.about_git, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/leandrocm86/lanpush-android")));
                    }
                })
                .setNegativeButton(R.string.about_close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .setIcon(R.drawable.lanpush_small);
            builder.create().show();// Create the AlertDialog object and return it
        }
        else if (id == R.id.action_close) {
            LanpushApp.close(false);
            GoodMorningAlarm.inst.cancel();
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
        Log.d("Destroying MainActivity");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d("Stopping MainActivity");
        Log.saveMessages();
        super.onStop();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        Log.d("PostCreate MainActivity");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        Log.d("PostResume MainActivity");
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        Log.d("Start MainActivity");
        super.onStart();
    }
}