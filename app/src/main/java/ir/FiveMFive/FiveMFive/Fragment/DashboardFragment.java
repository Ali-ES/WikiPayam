package ir.FiveMFive.FiveMFive.Fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.FiveMFive.FiveMFive.Java.DashboardItem;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.RecyclerViewAdapter;
import ir.FiveMFive.FiveMFive.Singleton.DashboardLab;
import ir.FiveMFive.FiveMFive.Utility.PopupBuilder;

public class DashboardFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.dashboard_recycle);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<DashboardItem> dashboardItems = (ArrayList<DashboardItem>) DashboardLab.get(getContext()).getDashboards();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.LayoutType.DASHBOARD, dashboardItems);
        recyclerView.setAdapter(adapter);



        ConstraintLayout actionBar = v.findViewById(R.id.action_bar);
        ImageView more = v.findViewById(R.id.more_iv);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupBuilder popupBuilder = new PopupBuilder(getContext());
                popupBuilder.addItem(R.drawable.ic_exit, R.string.exit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //handle click
                    }
                });
                popupBuilder.showPopup(actionBar);
            }
        });

        return v;
    }
}