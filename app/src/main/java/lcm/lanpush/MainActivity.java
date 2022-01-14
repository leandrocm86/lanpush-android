package lcm.lanpush;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;

import androidx.core.app.JobIntentService;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import lcm.lanpush.databinding.ActivityMainBinding;
import lcm.lanpush.utils.CDI;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.i("Criando MainActivity");
            super.onCreate(savedInstanceState);
            CDI.clear();
            CDI.set(this, "main");
            CDI.set(getSystemService(Context.CLIPBOARD_SERVICE));

            SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
            int timeout = preferences.getInt("timeout", ClientListenning.DEFAULT_TIMEOUT);
            ClientListenning.getInstance().setTimeout(timeout);

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
                        Sender.send(binding.input.getText().toString(), 1050);
                        Notificador.getInstance().showToast("Sent!");
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
                Notificador.getInstance().showToast("Reconectando...");
                LanpushApp.restartService();
            } catch (Throwable t) {
                Log.e(t);
            }
        }
        else if (id == R.id.action_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//            NavigationUI.navigateUp(navController, appBarConfiguration);
            navController.navigate(R.id.settings);
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
//        CDI.clear();
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