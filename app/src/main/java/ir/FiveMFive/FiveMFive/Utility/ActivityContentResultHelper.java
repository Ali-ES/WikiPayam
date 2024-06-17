package ir.FiveMFive.FiveMFive.Utility;

import android.net.Uri;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import ir.FiveMFive.FiveMFive.R;

public class ActivityContentResultHelper {
    public interface ContentResultListener {
        void gotResult(Uri uri);
    }
    private Fragment fragment;
    private ActivityResultLauncher launcher;
    private ContentResultListener contentResultListener;
    public ActivityContentResultHelper(Fragment fragment) {
        this.fragment = fragment;
        this.contentResultListener = contentResultListener;
        this.launcher = fragment.registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if(contentResultListener != null) {
                    contentResultListener.gotResult(uri);
                }
            }
        });
    }
    public void setContentResultListener(ContentResultListener listener) {
        this.contentResultListener = listener;
    }
    public void getExcel() {
        launcher.launch("application/vnd.ms-excel");
    }
    public void getTextFile() {
        launcher.launch("text/plain");
    }

    public void showNoFileSelectedSnack(View v) {
        String message = fragment.getString(R.string.warn_no_file_selected);
        SnackbarBuilder.showSnack(fragment.requireContext(), v, message, SnackbarBuilder.SnackType.WARNING);
    }
}
