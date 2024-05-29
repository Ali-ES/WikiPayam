package ir.FiveMFive.FiveMFive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.Java.DashboardItem;
import ir.FiveMFive.FiveMFive.ViewHolders.DashboardHolder;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private Fragment fragment;
    private LayoutType layoutType;
    private List<?> items;

    public enum LayoutType {
        DASHBOARD
    }
    public RecyclerViewAdapter(Fragment fragment, LayoutType layoutType, ArrayList<?> items) {
        this.fragment = fragment;
        this.layoutType = layoutType;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (layoutType) {
            case DASHBOARD:
            return new DashboardHolder(inflater, parent, fragment);



            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(layoutType) {
            case DASHBOARD:
                DashboardHolder dashboard = (DashboardHolder) holder;
                dashboard.bind((DashboardItem) items.get(position), fragment.requireContext());
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
