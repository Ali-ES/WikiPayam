package ir.FiveMFive.FiveMFive.Fragment.Login;

import static ir.FiveMFive.FiveMFive.Utility.UM.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import ir.FiveMFive.FiveMFive.Activity.MainActivity;
import ir.FiveMFive.FiveMFive.ProgressIndicatorListener;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.RetrofitClient;
import ir.FiveMFive.FiveMFive.RetrofitInterface;
import ir.FiveMFive.FiveMFive.Utility.ConnectivityChecker;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {
    public static final String TAG = "LoginFragment";
    private Context c;
    private View v;
    private ProgressIndicatorListener progressListener;
    private ConstraintLayout userLayout;
    private TextView userText;
    private EditText userEdit;
    private ConstraintLayout passLayout;
    private TextView passText;
    private EditText passEdit;
    private Button submit;
    private TextView signupText;
    private TextView forgotPassText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);
        c = getContext();
        userLayout = v.findViewById(R.id.user_layout);
        userText = v.findViewById(R.id.user_tv);
        userEdit = v.findViewById(R.id.user_et);
        passLayout = v.findViewById(R.id.pass_layout);
        passText = v.findViewById(R.id.pass_tv);
        passEdit = v.findViewById(R.id.pass_et);
        submit = v.findViewById(R.id.submit_bt);
        signupText = v.findViewById(R.id.signup_tv);
        forgotPassText = v.findViewById(R.id.forgot_pass_tv);

        giveFocus(userLayout, userEdit);
        giveFocus(passLayout, passEdit);

        setEditTextFocus(getContext(), userLayout, userText, userEdit);
        setEditTextFocus(getContext(), passLayout, passText, passEdit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                if(!checkEditNulls(userEdit, passEdit)) {

                    ConnectivityChecker connectivityChecker = new ConnectivityChecker(new ConnectivityChecker.ConnectionListener() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()) {
                                try {
                                    String r = response.body().string();
                                    if(r.contains("error")) {
                                        SnackbarBuilder.showSnack(c, v, getString(R.string.error_incorrect_user_pass), SnackbarBuilder.SnackType.ERROR);
                                    } else {
                                        SnackbarBuilder.showSnack(c, v, getString(R.string.note_login_success), SnackbarBuilder.SnackType.SUCCESS);
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                        public void isConnected(boolean status) {
                            if(status) {
                                login();
                            } else {
                                ConnectivityChecker.showConnectionFailSnack(getContext(), v);
                                hideProgress();
                            }
                        }
                    });
                    connectivityChecker.checkConnection(requireActivity());



                } else {
                    SnackbarBuilder.showSnack(c, v, getString(R.string.warn_empty_user_pass), SnackbarBuilder.SnackType.WARNING);
                    hideProgress();
                }
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingupFragment fragment = new SingupFragment();
                fragment.setProgressIndicator(progressListener);
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        forgotPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordFragment fragment = new ForgotPasswordFragment();
                fragment.setProgressIndicator(progressListener);
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

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

    private void login() {
        String user = userEdit.getText().toString();
        String pass = passEdit.getText().toString();


        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitInterface apiInterface = retrofit.create(RetrofitInterface.class);
        Call<ResponseBody> call = apiInterface.login(user, pass);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        String r = response.body().string();
                        if(r.contains("error")) {
                            SnackbarBuilder.showSnack(c, v, getString(R.string.error_incorrect_user_pass), SnackbarBuilder.SnackType.ERROR);
                        } else {
                            SnackbarBuilder.showSnack(c, v, getString(R.string.note_login_success), SnackbarBuilder.SnackType.SUCCESS);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v(TAG, "failed", t);
                hideProgress();
                ConnectivityChecker.showConnectionFailSnack(getContext(), v);
            }
        });
    }

}