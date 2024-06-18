package ir.FiveMFive.FiveMFive.ViewCreators;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.Checkers.PhoneNumberFormatChecker;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;
import static ir.FiveMFive.FiveMFive.Utility.UM.*;

public class NumberDialogBuilder {
    private Context c;
    private View v;
    private View mainLayout;
    private NestedScrollView numberScroll;
    private LinearLayout numberContainer;
    private List<String> numbers;
    private CancelListener cancelListener;
    private View lastDivider;
    private boolean hasChanged;

    public interface CancelListener {
        void onCancel(boolean hasChanged);
    }

    public NumberDialogBuilder(Context c, ArrayList<String> numbers, View v) {
        this.c = c;
        this.numbers = numbers;
        this.v = v;

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainLayout = inflater.inflate(R.layout.dialog_phone_numbers, null);
        numberScroll = mainLayout.findViewById(R.id.number_scroll);
        numberContainer = mainLayout.findViewById(R.id.number_container_layout);

        loadNumbers();
        handleAddButton();
    }

    private void loadNumbers() {
        for(String number : numbers) {
            createNumberView(number);
        }
    }
    private void createNumberView(String number) {
        RelativeLayout numberLayout = new RelativeLayout(c);

        int topMargin = (int) c.getResources().getDimension(R.dimen.mg_tb_dialog_items);

        TextView numberText = new TextView(c);
        numberText.setId(View.generateViewId());
        numberText.setText(number);
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        int textSize = (int) c.getResources().getDimension(R.dimen.ts_dialog_items);
        int textSizePX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, textSize, metrics);
        numberText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePX);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_VERTICAL);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        textParams.setMargins(0, topMargin, 0, 0);
        numberLayout.addView(numberText, textParams);


        ImageView delete = new ImageView(c);
        delete.setId(View.generateViewId());
        String deleteContentDesc = c.getString(R.string.cont_desc_delete);
        delete.setContentDescription(deleteContentDesc);
        Drawable deleteDrawable= AppCompatResources.getDrawable(c, R.drawable.ic_delete);
        delete.setImageDrawable(deleteDrawable);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberContainer.removeView(numberLayout);
                numbers.remove(number);
                hasChanged = true;
            }
        });

        RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        deleteParams.addRule(RelativeLayout.CENTER_VERTICAL);
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        numberLayout.addView(delete, deleteParams);

        View divider = new View(c);
        lastDivider = divider;
        divider.setBackgroundColor(c.getColor(R.color.gray_stroke));
        RelativeLayout.LayoutParams dividerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, toDp(c, 1));
        dividerParams.addRule(RelativeLayout.BELOW, numberText.getId());
        dividerParams.setMargins(0, topMargin, 0, 0);
        numberLayout.addView(divider, dividerParams);

        numberContainer.addView(numberLayout, LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void showDialog() {
        numberContainer.removeView(lastDivider);
        new AlertDialog.Builder(c)
                .setView(mainLayout)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(cancelListener != null) {
                            cancelListener.onCancel(hasChanged);
                        }
                    }
                })
                .show();
    }

    public void setCancelListener(CancelListener listener) {
        this.cancelListener = listener;
    }

    private void handleAddButton() {
        ConstraintLayout mobileLayout = mainLayout.findViewById(R.id.mobile_layout);
        TextView mobileText = mobileLayout.findViewById(R.id.mobile_tv);
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
                        hasChanged = true;
                        createNumberView(mobile);

                        mobileEdit.setText("");
                        numberScroll.requestFocus();

                        numberScroll.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int bottom = numberContainer.getBottom();
                                numberScroll.smoothScrollTo(0, bottom);
                            }
                        }, 100);

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
                    add.performClick();
                    mobileEdit.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

}
