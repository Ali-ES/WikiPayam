package ir.FiveMFive.FiveMFive.Fragment.Login;

import static ir.FiveMFive.FiveMFive.Utility.UM.*;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import ir.FiveMFive.FiveMFive.ProgressIndicatorListener;
import ir.FiveMFive.FiveMFive.R;


public class SingupFragment extends Fragment {
    private static final String TAG = "SingupFragment";
    private Context c;
    private ConstraintLayout userLayout;
    private TextView userText;
    private EditText userEdit;
    private ConstraintLayout passLayout;
    private TextView passText;
    private EditText passEdit;
    private ConstraintLayout passRepeatLayout;
    private TextView passRepeatText;
    private EditText passRepeatEdit;
    private ConstraintLayout emailLayout;
    private TextView emailText;
    private EditText emailEdit;
    private ConstraintLayout nameLayout;
    private TextView nameText;
    private EditText nameEdit;
    private ConstraintLayout mobileLayout;
    private TextView mobileText;
    private EditText mobileEdit;
    private TextView rulesText;
    private CheckBox rulesCheck;
    private Button signup;

    private ProgressIndicatorListener progressListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_singup, container, false);
        init(v);



        return v;
    }
    private void init(View v) {
        c = getContext();
        userLayout = v.findViewById(R.id.user_layout);
        userText = v.findViewById(R.id.user_tv);
        userEdit = v.findViewById(R.id.user_et);
        passLayout = v.findViewById(R.id.pass_layout);
        passText = v.findViewById(R.id.pass_tv);
        passEdit = v.findViewById(R.id.pass_et);
        passRepeatLayout = v.findViewById(R.id.pass_repeat_layout);
        passRepeatText = v.findViewById(R.id.pass_repeat_tv);
        passRepeatEdit = v.findViewById(R.id.pass_repeat_et);
        emailLayout = v.findViewById(R.id.email_layout);
        emailText = v.findViewById(R.id.email_tv);
        emailEdit = v.findViewById(R.id.email_et);
        nameLayout = v.findViewById(R.id.name_layout);
        nameText = v.findViewById(R.id.name_tv);
        nameEdit = v.findViewById(R.id.name_et);
        mobileLayout = v.findViewById(R.id.mobile_layout);
        mobileText = v.findViewById(R.id.mobile_tv);
        mobileEdit = v.findViewById(R.id.mobile_et);
        rulesText = v.findViewById(R.id.rules_tv);
        rulesCheck = v.findViewById(R.id.rules_check);
        signup = v.findViewById(R.id.singup_bt);

        giveFocus(userLayout, userEdit);
        giveFocus(passLayout, passEdit);
        giveFocus(passRepeatLayout, passRepeatEdit);
        giveFocus(emailLayout, emailEdit);
        giveFocus(nameLayout, nameEdit);
        giveFocus(mobileLayout, mobileEdit);

        setEditTextFocus(c, userLayout, userText, userEdit);
        setEditTextFocus(c, passLayout, passText, passEdit);
        setEditTextFocus(c, passRepeatLayout, passRepeatText, passRepeatEdit);
        setEditTextFocus(c, emailLayout, emailText, emailEdit);
        setEditTextFocus(c, nameLayout, nameText, nameEdit);
        setEditTextFocus(c, mobileLayout, mobileText, mobileEdit);

        rulesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulesCheck.setChecked(!rulesCheck.isChecked());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();

                //handle networking...
            }
        });
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