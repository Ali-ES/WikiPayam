package ir.FiveMFive.FiveMFive.Fragment.SendMessage;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.FiveMFive.FiveMFive.Java.ToolbarIcon;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.ToolbarHandler;
import ir.FiveMFive.FiveMFive.ViewsSetup.PriceDialogBuilder;

public class SingleMessageFragment extends Fragment {
    private Context c;
    private View v;
    private Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_single_message, container, false);
        c = requireContext();
        toolbar = v.findViewById(R.id.toolbar);

        toolbarInit();


        return v;
    }


    private void toolbarInit() {

        ToolbarHandler.handleBackNav(this, toolbar);
        ToolbarHandler toolbarHandler = new ToolbarHandler(c, toolbar);
        toolbarHandler.setTitle((String) getText(R.string.send_single_message));

        ToolbarIcon priceIcon = new ToolbarIcon(R.string.message_send_price, R.drawable.ic_price, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PriceDialogBuilder builder = new PriceDialogBuilder(c);
                builder.showDialog();
            }
        });
        toolbarHandler.addIcon(priceIcon);

    }

}