package ir.FiveMFive.FiveMFive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.Java.DashboardItem;
import ir.FiveMFive.FiveMFive.ViewHolders.DashboardViewHolder;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private Context c;
    private LayoutType layoutType;
    private List<?> items;

    public enum LayoutType {
        DASHBOARD
    }
    public RecyclerViewAdapter(Context c, LayoutType layoutType, ArrayList<?> items) {
        this.c = c;
        this.layoutType = layoutType;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (layoutType) {
            case DASHBOARD:
            return new DashboardViewHolder(inflater, parent);



            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(layoutType) {
            case DASHBOARD:
                DashboardViewHolder dashboard = (DashboardViewHolder) holder;
                dashboard.bind((DashboardItem) items.get(position), c);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
