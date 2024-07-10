package ir.FiveMFive.FiveMFive.ViewCreators;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.Interface.ListModifyListener;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.RecyclerViewAdapter;
import ir.FiveMFive.FiveMFive.Utility.Checkers.PhoneNumberFormatChecker;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;
import static ir.FiveMFive.FiveMFive.Utility.UM.*;

public class NumberDialogBuilder implements ListModifyListener {
    private static final String TAG = "NumberDialog";
    private Fragment fragment;
    private Context c;
    private View v;
    private View mainLayout;
    private RecyclerView numberRecycle;
    private RecyclerViewAdapter adapter;
    private List<String> numbers;
    private ChangeListener changeListener;

    public interface ChangeListener {
        void onRemove();
        void onAdd();
    }

    public NumberDialogBuilder(Fragment fragment, List<String> numbers, View v) {
        this.fragment = fragment;
        this.c = fragment.requireContext();
        this.numbers = numbers;
        this.v = v;

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainLayout = inflater.inflate(R.layout.dialog_phone_numbers, null);
        numberRecycle = mainLayout.findViewById(R.id.number_recycler);

        setUpRecyclerView();
        handleAddButton();
    }
    private void setUpRecyclerView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(c);
                numberRecycle.setLayoutManager(manager);

                adapter = new RecyclerViewAdapter(fragment, RecyclerViewAdapter.LayoutType.MOBILE, (ArrayList<String>) numbers);
                adapter.setListModifyListener(NumberDialogBuilder.this);
                numberRecycle.setAdapter(adapter);
            }
        }, 100);

    }

    public void showDialog() {
        new AlertDialog.Builder(c)
                .setView(mainLayout)
                .show();
    }

    public void setChangeListener(ChangeListener listener) {
        this.changeListener = listener;
    }

    private void handleAddButton() {
        ConstraintLayout mobileLayout = mainLayout.findViewById(R.id.mobile_layout);
        TextView mobileText = mobileLayout.findViewById(R.id.item_tv);
        EditText mobileEdit = mobileLayout.findViewById(R.id.mobile_et);
        setEditTextLayoutFocus(c, mobileLayout, mobileText, mobileEdit);

        Button add = mainLayout.findViewById(R.id.add_bt);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkEditNulls(mobileEdit)) {
                    String mobile = mobileEdit.getText().toString();
                    if(PhoneNumberFormatChecker.checkNumberFormat(mobile)) {
                        numbers.add(mobile);
                        int position = numbers.size() - 1;
                        adapter.notifyItemChanged(position);
                        numberRecycle.smoothScrollToPosition(position);

                        if(changeListener != null) {
                            changeListener.onAdd();
                        }
                        mobileEdit.setText("");
                        numberRecycle.requestFocus();

                        String successMessage = c.getString(R.string.success_adding_number);
                        SnackbarBuilder.showSnack(c, v, successMessage, SnackbarBuilder.SnackType.SUCCESS);
                    } else {
                        hideKeyboard(c, mobileEdit);
                        String errorMessage = c.getString(R.string.error_wrong_number_format);
                        SnackbarBuilder.showSnack(c, v, errorMessage, SnackbarBuilder.SnackType.ERROR);
                    }
                } else {
                    hideKeyboard(c, mobileEdit);
                    String warnMessage = c.getString(R.string.warn_empty_mobile);
                    SnackbarBuilder.showSnack(c, v, warnMessage, SnackbarBuilder.SnackType.WARNING);
                }
            }
        });

        mobileEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    if(!checkEditNulls(mobileEdit)) {
                        add.performClick();
                        mobileEdit.requestFocus();
                    } else {
                        hideKeyboard(c, mobileEdit);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onListRemove(int position) {
        numbers.remove(position);
        if(changeListener != null) {
            changeListener.onRemove();
        }
    }
}
