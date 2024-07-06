package ir.FiveMFive.FiveMFive.ViewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ir.FiveMFive.FiveMFive.Interface.ListModifyListener;
import ir.FiveMFive.FiveMFive.Java.Group;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.RecyclerViewAdapter;

public class GroupHolder extends RecyclerView.ViewHolder {
    private TextView group;
    private ImageView delete;
    private RecyclerViewAdapter adapter;
    private ListModifyListener listModifyListener;
    public GroupHolder(LayoutInflater inflater, ViewGroup parent, RecyclerViewAdapter adapter) {
        super(inflater.inflate(R.layout.card_removable_item, parent, false));
        this.adapter = adapter;

        group = itemView.findViewById(R.id.item_tv);
        delete = itemView.findViewById(R.id.delete_iv);

        handleDelete();
    }
    private void handleDelete() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listModifyListener != null) {
                    listModifyListener.onListRemove(getAdapterPosition());
                }
                adapter.notifyItemRemoved(getAdapterPosition());
            }
        });
    }
    public void bind(Group g) {
        group.setText(g.getName());
    }

    public void setListModifyListener(ListModifyListener listener) {
        this.listModifyListener = listener;
    }
}
