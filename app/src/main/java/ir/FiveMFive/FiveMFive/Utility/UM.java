package ir.FiveMFive.FiveMFive.Utility;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import ir.FiveMFive.FiveMFive.R;

public class UM {
    public static void giveFocus(ViewGroup layout, EditText edit) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.requestFocus();
                int position = edit.getText().toString().length();
                edit.setSelection(position);
            }
        });
    }
    public static void setEditTextLayoutFocus(Context c, ViewGroup layout, @Nullable TextView text, EditText edit, View divider) {
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    layout.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_field_focused));
                    if(text != null) {
                        text.setVisibility(View.INVISIBLE);
                    }
                    if(divider != null) {
                        divider.setBackgroundColor(c.getColor(R.color.navy_blue));
                    }
                } else {
                    layout.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_field_unfocused));
                    if(text != null) {
                        if (edit.getText().toString().isEmpty()) {
                            text.setVisibility(View.VISIBLE);
                        }
                    }
                    if(divider != null) {
                        divider.setBackgroundColor(c.getColor(R.color.gray_stroke));
                    }
                }
            }
        });
    }
    public static void setEditTextLayoutFocus(Context c, ViewGroup layout, TextView text, EditText edit) {
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    layout.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_field_focused));
                    if(text != null) {
                        text.setVisibility(View.INVISIBLE);
                    }
                } else {
                    layout.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_field_unfocused));
                    if(text != null) {
                        if (edit.getText().toString().isEmpty()) {
                            text.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    public static void setEditTextFocus(Context c, EditText edit, View otherView) {
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    edit.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_field_focused));
                    if(otherView != null) {
                        otherView.setBackgroundColor(c.getColor(R.color.navy_blue));
                    }
                } else {
                    edit.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_field_unfocused));
                    if(otherView != null) {
                        otherView.setBackgroundColor(c.getColor(R.color.gray_stroke));
                    }
                }
            }
        });
    }
    public static boolean checkEditNulls(EditText...edits) {
        for(EditText e : edits) {
            if(e.getText().toString().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    public static boolean checkEditNullWithResponse(Context c, View v, EditText[] editTexts, int[] errorIds) {

        for(int i = 0; i < editTexts.length; i++) {
            if(editTexts[i].getText().toString().equals("")) {
                String text = c.getString(errorIds[i]);
                SnackbarBuilder.showSnack(c, v, text, SnackbarBuilder.SnackType.ERROR);
                return true;
            }
        }
        return false;
    }
    public static int toDp(Context c, int dps) {
        final float scale = c.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }
    public static void showKeyboard(Context c, View v) {
        InputMethodManager manager = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(v, 0);
    }
    public static void hideKeyboard(Context c, View v) {
        InputMethodManager manager = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
