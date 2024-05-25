package ir.FiveMFive.FiveMFive.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import ir.FiveMFive.FiveMFive.Fragment.Login.LoginFragment;
import ir.FiveMFive.FiveMFive.ProgressIndicatorListener;
import ir.FiveMFive.FiveMFive.R;

public class LoginActivity extends AppCompatActivity implements ProgressIndicatorListener {
    private FrameLayout progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        LoginFragment fragment = new LoginFragment();
        fragment.setProgressIndicator(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();

        progressIndicator = findViewById(R.id.progress_indicator);
        progressIndicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }
    @Override
    public void showProgress() {
        progressIndicator.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideProgress() {
        progressIndicator.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finishAffinity();
        }
    }
}