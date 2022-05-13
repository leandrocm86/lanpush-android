package lcm.lanpush;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        super.onViewCreated(view, savedInstanceState);
        Log.d("Loading messages...");

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });

        LanpushApp.saveLogView(this);
        this.fillMessages();
    }

    public void addMsg(String msg) {
        LanpushApp.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (binding.logview.getText().length() == 0) {
//                    fillMessages();
//                }
                binding.logview.setText(binding.logview.getText() + msg + "\n");
                scrollDown();
            }
        });
    }

    public void fillMessages() {
        String output = "";
        for (String msg : Log.getMessages())
            output += msg + "\n";
        binding.logview.setText(output);
        scrollDown();
    }

    private void scrollDown() {
        // We need to wait some 500ms for the view to update its size before scrolling down.
        binding.scrollView.postDelayed(() -> {
                        if (binding != null && binding.scrollView != null)
                            binding.scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                , 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LanpushApp.clearLogView();
        binding = null;
        Log.d("LogView destroyed");
    }
}