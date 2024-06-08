package ir.FiveMFive.FiveMFive.Utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import ir.FiveMFive.FiveMFive.Java.ToolbarIcon;
import ir.FiveMFive.FiveMFive.R;

public class ToolbarHandler {
    private Context c;
    private Toolbar toolbar;
    private ConstraintLayout mainLayout;
    private LinearLayout iconsContainer;
    private int iconCount = 0;
    public ToolbarHandler(Context c, Toolbar toolbar) {
        this.c = c;
        this.toolbar = toolbar;

        mainLayout = toolbar.findViewById(R.id.main_layout);
        iconsContainer = mainLayout.findViewById(R.id.icon_container);
    }
    public void setTitle(String title) {
        TextView titleText = mainLayout.findViewById(R.id.title_tv);
        titleText.setText(title);
    }
    public void addIcon(ToolbarIcon icon) {
        if(iconCount <= 3) {
            CharSequence iconText = c.getText(icon.getTitleResID());
            Drawable iconDrawable = ResourcesCompat.getDrawable(c.getResources(), icon.getIconResID(), null);

            ImageView iconImage = new ImageView(c);
            iconImage.setImageDrawable(iconDrawable);
            iconImage.setContentDescription(iconText);
            iconImage.setOnClickListener(icon.getListener());

            LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            iconImage.setLayoutParams(params);

            iconsContainer.addView(iconImage);
        } else {
            /**
             * Put the icon in a new LinearLayout
             * add that icon to a popup window
             * add the popup window icon(vertical dots) to mainlayout
             * Tip: it's best if we could add icons based on screen's width
             */
        }
    }
    public static void handleBackNav(Fragment fragment, Toolbar toolbar) {
        ImageView backNavIcon = toolbar.findViewById(R.id.back_nav_iv);
        backNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.getParentFragmentManager().popBackStack();
            }
        });
    }
}
