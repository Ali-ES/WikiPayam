package ir.FiveMFive.FiveMFive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.Interface.ListModifyListener;
import ir.FiveMFive.FiveMFive.Java.CategoryItem;
import ir.FiveMFive.FiveMFive.Java.DashboardItem;
import ir.FiveMFive.FiveMFive.ViewHolders.CategoryHolder;
import ir.FiveMFive.FiveMFive.ViewHolders.DashboardHolder;
import ir.FiveMFive.FiveMFive.ViewHolders.MobileHolder;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private Fragment fragment;
    private LayoutType layoutType;
    private List<?> items;
    private ListModifyListener listModifyListener;

    public enum LayoutType {
        DASHBOARD,
        CATEGORY,
        MOBILE
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

            case CATEGORY:
                return new CategoryHolder(inflater, parent, fragment);
            case MOBILE:
                return new MobileHolder(inflater, parent, this);
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

            case CATEGORY:
                CategoryHolder category = (CategoryHolder) holder;
                category.bind((CategoryItem) items.get(position));
                break;
            case MOBILE:
                MobileHolder mobile = (MobileHolder) holder;
                if(listModifyListener != null) {
                    mobile.setListModifyListener(listModifyListener);
                }
                mobile.bind((String) items.get(position));
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
    public void setListModifyListener(ListModifyListener listener) {
        this.listModifyListener = listener;
    }
}
