package ir.FiveMFive.FiveMFive.BottomSheetDialog;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.APIHelper.GroupManager;
import ir.FiveMFive.FiveMFive.Java.Group;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.Checkers.ConnectivityChecker;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;

public class GroupAddBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "GroupAddBottomSheet";
    private List<String> mobiles;
    private Spinner selectGroupSpin;
    private ConstraintLayout mainLayout;
    private FrameLayout progressIndicator;
    private boolean gotGroups = false;
    private static final String EXTRA_MOBILES = "mobilesToAdd";
    public static GroupAddBottomSheet newInstance(List<String> mobiles) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRA_MOBILES, (ArrayList<String>) mobiles);

        GroupAddBottomSheet bottomSheet = new GroupAddBottomSheet();
        bottomSheet.setArguments(bundle);
        return bottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null) {
            mobiles = arguments.getStringArrayList(EXTRA_MOBILES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_group_save, container, false);
        mainLayout = v.findViewById(R.id.main_layout);
        selectGroupSpin = mainLayout.findViewById(R.id.select_group_spin);
        progressIndicator = v.findViewById(R.id.progress_indicator);

        selectGroupSpin.setAdapter(new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.select_group)));


        setCancelable(false);

        progressIndicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Button groupAdd = mainLayout.findViewById(R.id.add_to_group_btn);
        groupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gotGroups) {

                } else {
                    String errorMessage = getString(R.string.error_wrong_selected_group);
                    View root = getDialog().getWindow().getDecorView();
                    SnackbarBuilder.showSnack(requireContext(), root, errorMessage, SnackbarBuilder.SnackType.ERROR);
                }
            }
        });

        Button cancel = mainLayout.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ImageView refresh = mainLayout.findViewById(R.id.refresh_iv);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGroupsList();
            }
        });


        getGroupsList();

        return v;
    }

    private void getGroupsList() {
        ConnectivityChecker connectivityChecker = new ConnectivityChecker(new ConnectivityChecker.ConnectionListener() {
            @Override
            public void isConnected(boolean status) {
                Context c = requireContext();
                View root = getDialog().getWindow().getDecorView();
                if(status) {
                    showProgress();
                    GroupManager groupManager = new GroupManager(c, root, new GroupManager.GroupManagerListener() {
                        @Override
                        public void gotGroups(List<Group> groups) {
                            if(groups != null) {
                                if (groups.size() == 0) {
                                    dismiss();
                                } else {
                                    ArrayList<String> items = new ArrayList<>();
                                    for (Group group : groups) {
                                        items.add(group.getName());
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, items);
                                    selectGroupSpin.setAdapter(adapter);
                                }
                            }

                            gotGroups = true;
                            hideProgress();
                        }
                    });
                    groupManager.getGroupsList();


                } else {
                    ConnectivityChecker.showNoConnectionSnack(c, root);
                    Log.v(TAG, "No connection");
                    gotGroups = false;
                }
            }
        });
        connectivityChecker.checkConnection(requireActivity());
    }


    private void showProgress() {
        progressIndicator.setVisibility(View.VISIBLE);
    }
    private void hideProgress() {
        progressIndicator.setVisibility(View.GONE);
    }

}
