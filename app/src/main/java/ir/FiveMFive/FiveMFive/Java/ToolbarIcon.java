package ir.FiveMFive.FiveMFive.Java;

import android.view.View;
import android.widget.ImageView;

public class ToolbarIcon {
    private int titleResID;
    private int iconResID;
    private View.OnClickListener listener;
    public ToolbarIcon() {

    }
    public ToolbarIcon(int title, int icon, View.OnClickListener listener) {
        this.titleResID = title;
        this.iconResID = icon;
        this.listener = listener;
    }

    public int getTitleResID() {
        return titleResID;
    }

    public void setTitleResID(int titleResID) {
        this.titleResID = titleResID;
    }

    public int getIconResID() {
        return iconResID;
    }

    public void setIconResID(int iconResID) {
        this.iconResID = iconResID;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
