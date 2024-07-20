package ir.FiveMFive.FiveMFive.ViewHolders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.FiveMFive.FiveMFive.Java.Contact;
import ir.FiveMFive.FiveMFive.R;

public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CheckBox.OnCheckedChangeListener {
    private static final String TAG = "ContactHolder";
    private TextView contactName;
    private TextView contactMobile;
    private CheckBox contactCheck;
    private List<Contact> contacts;
    public ContactHolder(LayoutInflater inflater, ViewGroup parent, List<Contact> contacts) {
        super(inflater.inflate(R.layout.card_contact_item, parent, false));

        this.contacts = contacts;

        contactName = itemView.findViewById(R.id.contact_name_tv);
        contactMobile = itemView.findViewById(R.id.contact_mobile_tv);
        contactCheck = itemView.findViewById(R.id.contact_check);

        itemView.setOnClickListener(this);
        contactCheck.setOnCheckedChangeListener(this);
    }

    public void bind() {
        int position = getAdapterPosition();
        contactName.setText(contacts.get(position).getName());
        contactMobile.setText(contacts.get(position).getMobile());
        contactCheck.setChecked(contacts.get(position).isSelected());
    }
    @Override
    public void onClick(View v) {
        contactCheck.setChecked(!contactCheck.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = getAdapterPosition();
        contacts.get(position).setSelected(isChecked);
    }
}
