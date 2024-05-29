package ir.FiveMFive.FiveMFive.ViewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import ir.FiveMFive.FiveMFive.Java.DashboardItem;
import ir.FiveMFive.FiveMFive.R;

public class DashboardViewHolder extends RecyclerView.ViewHolder {
    private DashboardItem dashboardItem;
    private ImageView icon;
    private TextView title;
    public DashboardViewHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.card_dashboard_item, parent, false));

        icon = itemView.findViewById(R.id.icon);
        title = itemView.findViewById(R.id.title);
    }
    public void bind(DashboardItem item, Context c) {
        dashboardItem = item;

        int imageResID = item.getImageResID();
        icon.setImageDrawable(ResourcesCompat.getDrawable(c.getResources(), imageResID, null));

        String titleText = item.getTitle();
        title.setText(titleText);
    }
}
