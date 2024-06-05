package ir.FiveMFive.FiveMFive.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import ir.FiveMFive.FiveMFive.R;

public class PopupBuilder {
    private Context c;
    private LinearLayout main;
    private View lastDivider;
    private PopupWindow window;

    public PopupBuilder(Context c) {
        this.c = c;
        main = new LinearLayout(c);
        main.setOrientation(LinearLayout.VERTICAL);
    }

    public void addItem(int imageResID, int titleId, View.OnClickListener clickListener) throws Resources.NotFoundException {
        String title = c.getResources().getString(titleId);

        ImageView icon = new ImageView(c);
        icon.setImageDrawable(ResourcesCompat.getDrawable(c.getResources(), imageResID, null));
        icon.setContentDescription(title);
        int iconDimen = (int) c.getResources().getDimension(R.dimen.wh_popup_icon);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(toDp(iconDimen), toDp(iconDimen));

        TextView text = new TextView(c);
        text.setText(title);
        text.setTextColor(c.getResources().getColor(R.color.navy_blue));

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int textMargin = (int) c.getResources().getDimension(R.dimen.mg_s_popup_title);
        textParams.rightMargin = toDp(textMargin);

        LinearLayout layout = new LinearLayout(c);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        int layoutSEPadding = (int) c.getResources().getDimension(R.dimen.pad_se_popup_item);
        int layoutTBPadding = (int) c.getResources().getDimension(R.dimen.pad_tb_popup_item);
        layout.setPadding(toDp(layoutSEPadding), layoutTBPadding, toDp(layoutSEPadding), layoutTBPadding);
        layout.addView(icon, iconParams);
        layout.addView(text, textParams);

        main.addView(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        main.addView(getDivider());
        layout.setOnClickListener(clickListener);

    }

    public View getDivider() {
        View divider = new View(c);
        divider.setId(View.generateViewId());
        lastDivider = divider;
        divider.setBackgroundColor(c.getResources().getColor(R.color.gray_popup_divider));
        ViewGroup.LayoutParams dividerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, toDp(1));
        divider.setLayoutParams(dividerParams);
        return divider;
    }
    public void showPopup(View anchorView) {
        if(lastDivider != null) {
            main.removeView(lastDivider);
        }
        CardView cardView = new CardView(c);
        cardView.addView(main);
        cardView.setCardElevation(30);
        cardView.setRadius(toDp(5));
        cardView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        int popupWidth = (int) c.getResources().getDimension(R.dimen.w_popup);
        window = new PopupWindow(cardView, toDp(popupWidth), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.showAsDropDown(anchorView, 0, 0);
    }

    public void dismiss() {
        if(window != null) {
            window.dismiss();
        }
    }

    private int toDp(int dps) {
        final float scale = c.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

}
