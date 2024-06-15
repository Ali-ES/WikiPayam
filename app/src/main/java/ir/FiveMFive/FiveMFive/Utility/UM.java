package ir.FiveMFive.FiveMFive.Utility;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
    public static void setEditTextFocus(Context c, ViewGroup layout, TextView text, EditText edit) {
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    layout.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_field_focused));
                    text.setVisibility(View.INVISIBLE);
                } else {
                    layout.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_field_unfocused));
                    if(edit.getText().toString().isEmpty()) {
                        text.setVisibility(View.VISIBLE);
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
    public static int toDp(Context c, int dps) {
        final float scale = c.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }
    public static String addThousandSeparator(String in) {
        String input = in;
        if(input.length() > 1) {
            if(input.charAt(0) == '0') {
                input = input.substring(1);
            }
        }
        String output = "";
        int commas = input.length()/3;
        if((input.length()%3) == 0) {
            commas--;
        }
        if(input.contains("-") && (input.length()%3 == 1))  {
            commas--;
        }
        int counter = 0;


        for (int i = input.length()-1; i >= 0; i--) {
            output = input.charAt(i) + output;
            counter++;
            if((counter%3 == 0) && (commas > 0)) {
                output = "," + output;
                commas--;
            }
        }

        return output;
    }
}
