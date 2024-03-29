package lcm.lanpush;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import lcm.lanpush.databinding.FragmentLogBinding;

public class LogFragment extends Fragment {

    private FragmentLogBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLogBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("Creating log view...");
        super.onViewCreated(view, savedInstanceState);
        LanpushApp.saveLogView(this);
        //this.fillMessages();
    }

    @Override
    public void onViewStateRestored(Bundle savedBundle) {
        Log.d("Restoring log view...");
        super.onViewStateRestored(savedBundle);
        this.fillMessages();
    }

    public void addMsg(String msg) {
        LanpushApp.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (binding != null && binding.logview != null) {
                    binding.logview.setText(binding.logview.getText() + msg + "\n");
                    scrollDown();
                }
            }
        });
    }

    public void fillMessages() {
        Log.d("Loading messages... First message is: ");
        String output = "";
        for (String msg : Log.getMessages())
            output += msg + "\n";
        if (output.length() > 0)
            binding.logview.setText(LanpushApp.get(R.string.first_message) + output);
        scrollDown();
    }

    private void scrollDown() {
        // We need to wait 1s for the view to update its size before scrolling down.
        binding.scrollView.postDelayed(() -> {
                        if (binding != null && binding.scrollView != null)
                            binding.scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                , 1000);
    }

    @Override
    public void onDestroyView() {
        Log.d("Destroying LogView...");
        super.onDestroyView();
        LanpushApp.clearLogView();
        binding = null;
    }
}