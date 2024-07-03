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

        switch (categoryItem.getPosition()) {
            case 0:
                SingleMessageFragment singleMessageFragment = new SingleMessageFragment();
                openFragment(singleMessageFragment);
                break;
            case 1:
                GroupMessageFragment groupMessageFragment = new GroupMessageFragment();
                openFragment(groupMessageFragment);
                break;
        }
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction ft = this.fragment.getParentFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
