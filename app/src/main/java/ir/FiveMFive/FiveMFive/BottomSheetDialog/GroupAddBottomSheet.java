package ir.FiveMFive.FiveMFive.BottomSheetDialog;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.APIHelper.GroupManager;
import ir.FiveMFive.FiveMFive.Java.Group;
import ir.FiveMFive.FiveMFive.Java.User;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.RetrofitClient;
import ir.FiveMFive.FiveMFive.RetrofitInterface;
import ir.FiveMFive.FiveMFive.Utility.Checkers.ConnectivityChecker;
import ir.FiveMFive.FiveMFive.Utility.CredentialCrypter;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GroupAddBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "GroupAddBottomSheet";
    private Context c;
    private View root;
    private List<String> mobiles;
    private List<Group> groups;
    private Spinner selectGroupSpin;
    private ConstraintLayout mainLayout;
    private FrameLayout progressIndicator;
    private boolean gotGroups = false;
    private static final String EXTRA_MOBILES = "mobilesToAdd";
    private int currentGroupSend = 0;
    private GroupAddFinishListener groupAddFinishListener;
    public static GroupAddBottomSheet newInstance(List<String> mobiles) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRA_MOBILES, (ArrayList<String>) mobiles);

        GroupAddBottomSheet bottomSheet = new GroupAddBottomSheet();
        bottomSheet.setArguments(bundle);
        return bottomSheet;
    }

    public interface GroupAddFinishListener {
        void onFinish(boolean isSuccessful);
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
        c = requireContext();
        root = getDialog().getWindow().getDecorView();
        mainLayout = v.findViewById(R.id.main_layout);
        selectGroupSpin = mainLayout.findViewById(R.id.select_group_spin);
        progressIndicator = v.findViewById(R.id.progress_indicator);

        selectGroupSpin.setAdapter(new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.select_group)));


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
                    showProgress();

                    CredentialCrypter crypter = new CredentialCrypter(requireContext());
                    User user = crypter.decrypt();

                    Group selectedGroup = groups.get(selectGroupSpin.getSelectedItemPosition());

                    addMobilesToGroup(user, selectedGroup);



                } else {
                    String errorMessage = getString(R.string.error_wrong_selected_group);
                    SnackbarBuilder.showSnack(c, root, errorMessage, SnackbarBuilder.SnackType.ERROR);
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
                if(status) {
                    showProgress();
                    GroupManager groupManager = new GroupManager(c, root, new GroupManager.GroupManagerListener() {
                        @Override
                        public void gotGroups(List<Group> groups) {
                            if(groups != null) {
                                GroupAddBottomSheet.this.groups = groups;
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

    private void addMobilesToGroup(User user, Group selectedGroup) {
        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ResponseBody> call = retrofitInterface.addMobileToGroup(user.getUsername(),
                user.getPassword(),
                selectedGroup.getId(),
                selectedGroup.getName(),
                mobiles.get(currentGroupSend));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        String output = response.body().string();
                        Log.v(TAG, output);
                        if (output.equals("success")) {
                            if(currentGroupSend == (mobiles.size()-1)) {
                                hideProgress();
                                dismiss();
                                currentGroupSend = 0;
                                if(groupAddFinishListener != null) {
                                    groupAddFinishListener.onFinish(true);
                                }
                            } else {
                                currentGroupSend++;
                                addMobilesToGroup(user, selectedGroup);
                            }
                        } else {
                            hideProgress();
                            dismiss();
                            currentGroupSend = 0;
                            if(groupAddFinishListener != null) {
                                groupAddFinishListener.onFinish(false);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ConnectivityChecker.showServerFailSnack(c, root);
                    dismiss();
                    hideProgress();
                    if(groupAddFinishListener != null) {
                        groupAddFinishListener.onFinish(false);
                    }
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ConnectivityChecker.showConnectionFailSnack(c, root, t);
                dismiss();
                hideProgress();
                if(groupAddFinishListener != null) {
                    groupAddFinishListener.onFinish(false);
                }
            }
        });
    }

    public String getCommaSeparatedMobiles() {
        String mobilesComma = "";
        for(String mobile : mobiles) {
            mobilesComma += (mobile + ", ");
        }
        if(mobilesComma.length() > 0) {
            int limit = mobilesComma.lastIndexOf(", ");
            mobilesComma = mobilesComma.substring(0, limit);
        }
        return mobilesComma;
    }

    private void showProgress() {
        progressIndicator.setVisibility(View.VISIBLE);
    }
    private void hideProgress() {
        progressIndicator.setVisibility(View.GONE);
    }
    public void setGroupAddFinishListener(GroupAddFinishListener listener) {
        this.groupAddFinishListener = listener;
    }
}
