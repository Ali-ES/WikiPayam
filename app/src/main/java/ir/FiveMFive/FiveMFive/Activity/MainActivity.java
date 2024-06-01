package ir.FiveMFive.FiveMFive.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ir.FiveMFive.FiveMFive.Fragment.DashboardFragment;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.PopupBuilder;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Fragment fragment = new DashboardFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();


        ConstraintLayout actionBar = findViewById(R.id.action_bar);
        ImageView more = findViewById(R.id.more_iv);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupBuilder popupBuilder = new PopupBuilder(MainActivity.this);
                popupBuilder.addItem(R.drawable.ic_exit, R.string.exit);
                popupBuilder.showPopup(actionBar);
            }
        });


    }

    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() < 0) {
            finishAffinity();
        }
    }
}