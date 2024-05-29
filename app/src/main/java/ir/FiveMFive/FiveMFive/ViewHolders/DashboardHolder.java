package ir.FiveMFive.FiveMFive.ViewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import ir.FiveMFive.FiveMFive.Fragment.SendMessage.SendMessageFragment;
import ir.FiveMFive.FiveMFive.Java.DashboardItem;
import ir.FiveMFive.FiveMFive.R;

public class DashboardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private DashboardItem dashboardItem;
    private ImageView icon;
    private TextView title;
    private Fragment fragment;
    public DashboardHolder(LayoutInflater inflater, ViewGroup parent, Fragment fragment) {
        super(inflater.inflate(R.layout.card_dashboard_item, parent, false));

        icon = itemView.findViewById(R.id.icon);
        title = itemView.findViewById(R.id.title);
        itemView.setOnClickListener(this);
    }
    public void bind(DashboardItem item, Context c) {
        dashboardItem = item;

        int imageResID = item.getImageResID();
        icon.setImageDrawable(ResourcesCompat.getDrawable(c.getResources(), imageResID, null));

        String titleText = item.getTitle();
        title.setText(titleText);
    }
    @Override
    public void onClick(View view) {
        Fragment f = new SendMessageFragment();
        FragmentTransaction ft = fragment.getParentFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, f);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
}
