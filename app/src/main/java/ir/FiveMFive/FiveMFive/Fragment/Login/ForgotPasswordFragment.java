package ir.FiveMFive.FiveMFive.Fragment.Login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.FiveMFive.FiveMFive.ProgressIndicatorListener;
import ir.FiveMFive.FiveMFive.R;


public class ForgotPasswordFragment extends Fragment {
    public static final String TAG = "ForgotPasswordFragment";
    private ProgressIndicatorListener progressListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);



        return v;
    }
    public void setProgressIndicator(ProgressIndicatorListener listener) {
        this.progressListener = listener;
    }
    private void showProgress() {
        if(progressListener != null) {
            progressListener.showProgress();
        }
    }
    private void hideProgress() {
        if(progressListener != null) {
            progressListener.hideProgress();
        }
    }
}