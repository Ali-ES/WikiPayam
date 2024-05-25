package ir.FiveMFive.FiveMFive.Utility;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import ir.FiveMFive.FiveMFive.R;

public class SnackbarBuilder {
    public static enum SnackType {
        SUCCESS,
        WARNING,
        ERROR
    }
    public static void showSnack(Context c, View v, String msg, SnackType type) {
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.snack_inform, null);

        TextView title = myView.findViewById(R.id.title);
        title.setText(msg);
        ImageView icon = myView.findViewById(R.id.icon);
        View side = myView.findViewById(R.id.side);


        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);


        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(myView, ViewGroup.LayoutParams.MATCH_PARENT, toDp(c, (int)c.getResources().getDimension(R.dimen.snack_h)));

        if(type == SnackType.WARNING) {
            side.setBackgroundColor(ContextCompat.getColor(c, R.color.orange));
            icon.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.ic_warning));
        } else if(type == SnackType.ERROR) {
            side.setBackgroundColor(ContextCompat.getColor(c, R.color.red));
            icon.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.ic_error));
        }

        snackbar.show();
    }


    private static int toDp(Context c, int dps) {
        final float scale = c.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }
}
