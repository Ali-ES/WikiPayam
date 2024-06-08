package ir.FiveMFive.FiveMFive.Java;

import android.view.View;
import android.widget.ImageView;

public class ToolbarIcon {
    private String title;
    private ImageView icon;
    private View.OnClickListener listener;
    public ToolbarIcon() {

    }
    public ToolbarIcon(String title, ImageView icon, View.OnClickListener listener) {
        this.title = title;
        this.icon = icon;
        this.listener = listener;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
