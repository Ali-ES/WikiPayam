package ir.FiveMFive.FiveMFive.ViewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import ir.FiveMFive.FiveMFive.Fragment.SendMessage.GroupMessageFragment;
import ir.FiveMFive.FiveMFive.Fragment.SendMessage.SingleMessageFragment;
import ir.FiveMFive.FiveMFive.Java.CategoryItem;
import ir.FiveMFive.FiveMFive.R;

public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView categoryTitle;
    private Fragment fragment;
    private CategoryItem categoryItem;
    public CategoryHolder(LayoutInflater inflater, ViewGroup parent, Fragment fragment) {
        super(inflater.inflate(R.layout.card_category_item, parent, false));

        categoryTitle = itemView.findViewById(R.id.category_title_tv);
        this.fragment = fragment;

        itemView.setOnClickListener(this);
    }
    public void bind(CategoryItem item) {
        this.categoryItem = item;
        categoryTitle.setText(item.getTitle());
    }
    @Override
    public void onClick(View v) {
        FragmentTransaction ft = fragment.getParentFragmentManager().beginTransaction();
        switch (categoryItem.getPosition()) {
            case 0:
                SingleMessageFragment singleMessageFragment = new SingleMessageFragment();
                ft.replace(R.id.fragment_container, singleMessageFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 1:
                GroupMessageFragment groupMessageFragment = new GroupMessageFragment();
                ft.replace(R.id.fragment_container, groupMessageFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }
}
