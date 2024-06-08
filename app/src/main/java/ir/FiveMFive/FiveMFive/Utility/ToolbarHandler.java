package ir.FiveMFive.FiveMFive.Utility;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

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
            ImageView iconImage = icon.getIcon();
            iconsContainer.addView(iconImage);
            iconImage.setContentDescription(icon.getTitle());
            iconImage.setOnClickListener(icon.getListener());
        } else {
            /**
             * Put the icon in a new LinearLayout
             * add that icon to a popup window
             * add the popup window icon(vertical dots) to mainlayout
             * Tip: it's best if we could add icons based on screen's width
             */
        }
    }
}
