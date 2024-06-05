package ir.FiveMFive.FiveMFive.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.FiveMFive.FiveMFive.R;

public class CategoryFragment extends Fragment {
    private static final String KEY_CATEGORY_TYPE = "categoryType";
    private View v;
    private Context c;
    private RecyclerView categoryRecycle;
    private CategoryType categoryType;

    public enum CategoryType {
        SEND_MESSAGE
    }

    public CategoryFragment newInstance(CategoryType type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_CATEGORY_TYPE, type);

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null) {
            categoryType = (CategoryType) arguments.getSerializable(KEY_CATEGORY_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        this.v = v;
        c = requireContext();

        categoryRecycle = v.findViewById(R.id.category_recycle);

        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        categoryRecycle.setLayoutManager(layoutManager);

        assert categoryType != null;
        switch (categoryType) {
            case SEND_MESSAGE:
                String[] sendMessageItems = getResources().getStringArray(R.array.array_send_message);

                break;
        }

        getResources().getStringArray(R.array.array_send_message);


        return v;
    }
}