package ir.FiveMFive.FiveMFive.BottomSheetDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ir.FiveMFive.FiveMFive.R;

public class BlacklistBottomSheet extends BottomSheetDialogFragment {
    private BlacklistSendListener blacklistSendListener;

    public interface BlacklistSendListener {
        void onClick(boolean sendToBlacklist);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_blacklist_send, container, false);

        Button blacklistSend = v.findViewById(R.id.send_blacklist_btn);
        blacklistSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blacklistSendListener != null) {
                    blacklistSendListener.onClick(true);
                }
            }
        });

        Button regularSend = v.findViewById(R.id.send_regular_btn);
        regularSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blacklistSendListener != null) {
                    blacklistSendListener.onClick(false);
                }
            }
        });

        return v;
    }
    public void setBlacklistSendListener(BlacklistSendListener listener) {
        this.blacklistSendListener = listener;
    }
}
