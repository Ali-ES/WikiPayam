package ir.FiveMFive.FiveMFive.Utility;

import android.app.Activity;
import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class ActivityContentResultHelper {
    public interface ContentResultListener {
        void gotResult(Uri uri);
    }
    private ActivityResultLauncher launcher;
    private ContentResultListener contentResultListener;
    public ActivityContentResultHelper(Fragment fragment) {
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
}
