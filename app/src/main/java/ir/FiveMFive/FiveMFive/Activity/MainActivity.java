package ir.FiveMFive.FiveMFive.Activity;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.concurrent.Executor;

import ir.FiveMFive.FiveMFive.Fragment.DashboardFragment;
import ir.FiveMFive.FiveMFive.ProgressIndicatorListener;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.CredentialCrypter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FrameLayout progressIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if(CredentialCrypter.hasLoggedIn(this)) {
            if(getIntent() != null) {
                BiometricManager biometricManager = BiometricManager.from(this);
                if (biometricManager.canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
                    biometricAuth();
                } else {
                    openDashboardFragment();
                }
            } else {
                openDashboardFragment();
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void biometricAuth() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt prompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // error handle for biometric auth
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                openDashboardFragment();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //biometric auth failed
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.user_authentication))
                .setSubtitle(getString(R.string.biometric_auth))
                .setNegativeButtonText(getString(R.string.passcode_login))
                .build();

        prompt.authenticate(promptInfo);
    }
    private void openDashboardFragment() {
        Fragment fragment = new DashboardFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() < 0) {
            finishAffinity();
        }
    }
}