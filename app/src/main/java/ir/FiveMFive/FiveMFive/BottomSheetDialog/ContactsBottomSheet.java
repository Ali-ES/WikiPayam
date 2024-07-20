package ir.FiveMFive.FiveMFive.BottomSheetDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.Java.Contact;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.RecyclerViewAdapter;
import ir.FiveMFive.FiveMFive.Utility.Handlers.ContactHandler;

public class ContactsBottomSheet extends BottomSheetDialogFragment {
    private Context c;
    private BottomSheetBehavior behavior;
    private ImageView close;
    private ImageView selectAll;
    private RecyclerView contactsRecycle;
    private FrameLayout progressIndicator;
    private ContactImportListener contactImportListener;

    public interface ContactImportListener {
        void onImport(List<Contact> contacts);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        c = requireContext();

        View v = View.inflate(requireContext(), R.layout.bottom_sheet_contacts, null);
        bottomSheetDialog.setContentView(v);

        behavior = BottomSheetBehavior.from((View) v.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        progressIndicator = v.findViewById(R.id.progress_indicator);
        progressIndicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        showProgress();

        ContactHandler contactHandler = new ContactHandler(requireContext());
        ArrayList<Contact> contacts = contactHandler.readContacts();


        contactsRecycle = v.findViewById(R.id.contacts_recycle);
        contactsRecycle.setLayoutManager(new LinearLayoutManager(requireContext()));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.LayoutType.CONTACT, contacts);
        contactsRecycle.setAdapter(adapter);


        close = v.findViewById(R.id.close_iv);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        selectAll = v.findViewById(R.id.select_all_iv);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < contacts.size(); i++) {
                    boolean currentState = contacts.get(i).isSelected();
                    if(!currentState) {
                        contacts.get(i).setSelected(true);
                        adapter.notifyItemChanged(i);
                    }
                }
            }
        });

        TextView importContacts = v.findViewById(R.id.import_contacts_tv);
        importContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Contact> selectedContacts = new ArrayList<>();
                for(Contact c : contacts) {
                    if(c.isSelected()) {
                        selectedContacts.add(c);
                    }
                }
                if(contactImportListener != null) {
                    contactImportListener.onImport(selectedContacts);
                }
                dismiss();
            }
        });


        hideProgress();


        return bottomSheetDialog;
    }


    public void showProgress() {
        progressIndicator.setVisibility(View.VISIBLE);
    }
    public void hideProgress() {
        progressIndicator.setVisibility(View.GONE);
    }

    public void setContactImportListener(ContactImportListener listener) {
        this.contactImportListener = listener;
    }

}
