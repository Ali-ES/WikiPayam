package ir.FiveMFive.FiveMFive.Utility;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ir.FiveMFive.FiveMFive.R;

public class PermissionManager {
    private Context c;
    private Fragment fragment;
    private ActivityResultLauncher<String> launcher;
    private PermissionListener permissionListener;

    public interface PermissionListener {
        void setPermission(boolean isGranted);
    }
    public PermissionManager(Fragment fragment,  int errorResId) {
        this.c = fragment.requireContext();
        this.fragment = fragment;


        launcher = fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean isGranted) {
                if(permissionListener != null) {
                    permissionListener.setPermission(isGranted);
                }
            }
        });
    }
    public boolean checkPermission(String permission) {
        if(ContextCompat.checkSelfPermission(c, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            launcher.launch(permission);
            return false;
        }
    }
    public void setPermissionListener(PermissionListener listener) {
        this.permissionListener = listener;
    }
}
