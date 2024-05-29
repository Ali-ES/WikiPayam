package ir.FiveMFive.FiveMFive.Singleton;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.Java.DashboardItem;
import ir.FiveMFive.FiveMFive.R;

public class DashboardLab {
    private static DashboardLab dashboardLab;
    private List<DashboardItem> items;
    private Context c;
    private DashboardLab(Context c) {
        items = new ArrayList<>();
        this.c = c;

        items.add(new DashboardItem(R.drawable.ic_person, getStringRes(R.string.user_menu)));
        items.add(new DashboardItem(R.drawable.ic_send_message, getStringRes(R.string.send_message)));
        items.add(new DashboardItem(R.drawable.ic_finance, getStringRes(R.string.finance)));
        items.add(new DashboardItem(R.drawable.ic_receive_message, getStringRes(R.string.receive_message)));
        items.add(new DashboardItem(R.drawable.ic_contacts, getStringRes(R.string.contacts)));
        items.add(new DashboardItem(R.drawable.ic_stats, getStringRes(R.string.monthly_stats)));
    }

    public static DashboardLab get(Context c) {
        if(dashboardLab == null) {
            dashboardLab = new DashboardLab(c);
        }
        return dashboardLab;
    }

    public List<DashboardItem> getDashboards() {
        return items;
    }
    private String getStringRes(int resId) {
        return c.getResources().getString(resId);
    }
}
