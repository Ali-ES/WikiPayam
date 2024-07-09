package ir.FiveMFive.FiveMFive.Fragment.SendMessage;

import static ir.FiveMFive.FiveMFive.Utility.UM.checkEditNullWithResponse;
import static ir.FiveMFive.FiveMFive.Utility.UM.giveFocus;
import static ir.FiveMFive.FiveMFive.Utility.UM.setEditTextFocus;
import static ir.FiveMFive.FiveMFive.Utility.UM.setEditTextLayoutFocus;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.BottomSheetDialog.BlacklistBottomSheet;
import ir.FiveMFive.FiveMFive.Java.Group;
import ir.FiveMFive.FiveMFive.Java.User;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.RetrofitClient;
import ir.FiveMFive.FiveMFive.RetrofitInterface;
import ir.FiveMFive.FiveMFive.Utility.ActivityContentResultHelper;
import ir.FiveMFive.FiveMFive.Utility.Checkers.ConnectivityChecker;
import ir.FiveMFive.FiveMFive.Utility.Checkers.MessageCharacterWatcher;
import ir.FiveMFive.FiveMFive.Utility.Constants;
import ir.FiveMFive.FiveMFive.Utility.CredentialCrypter;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;
import ir.FiveMFive.FiveMFive.Utility.ToolbarHandler;
import ir.FiveMFive.FiveMFive.ViewCreators.GroupDialogBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GroupMessageFragment extends Fragment {
    private static final String TAG = "GroupMessage";
    private static final String EXTRA_GROUPS = "groups";
    private static final String EXTRA_SELECTED_GROUPS = "selectedGroups";
    private Context c;
    private View v;
    private Toolbar toolbar;
    private LinearLayout senderLayout;
    private EditText senderEdit;
    private LinearLayout receiverLayout;
    private TextView receiverText;
    private LinearLayout messageLayout;
    private NestedScrollView messageEditLayout;
    private ConstraintLayout messageBoxLayout;
    private EditText messageEdit;
    private View messageDivider;
    private TextView addAllGroupsText;
    private TextView remainingCharsText;
    private Button sendMessage;
    private ActivityContentResultHelper contentResultHelper;
    private List<Group> groups;
    private List<Group> selectedGroups;
    private boolean noGroupsExist = false;
    private boolean shouldSendToBlackList = false;
    private FrameLayout progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groups = new ArrayList<>();
        selectedGroups = new ArrayList<>();

        getGroupsList();

        contentResultHelper = new ActivityContentResultHelper(GroupMessageFragment.this);
        if(savedInstanceState != null) {
            // code...
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_group_message, container, false);
        c = requireContext();
        toolbar = v.findViewById(R.id.toolbar);
        senderLayout = v.findViewById(R.id.sender_layout);
        senderEdit = v.findViewById(R.id.sender_et);
        receiverLayout = v.findViewById(R.id.receiver_layout);
        receiverText = v.findViewById(R.id.receiver_tv);
        messageLayout = v.findViewById(R.id.message_layout);
        messageEditLayout = v.findViewById(R.id.message_et_layout);
        messageBoxLayout = v.findViewById(R.id.message_box_layout);
        messageEdit = v.findViewById(R.id.message_et);
        messageDivider = v.findViewById(R.id.message_divider);
        addAllGroupsText = v.findViewById(R.id.add_all_groups_tv);
        remainingCharsText = v.findViewById(R.id.remaining_chars_tv);
        sendMessage = v.findViewById(R.id.send_message_bt);
        progressIndicator = v.findViewById(R.id.progress_indicator);

        giveFocus(senderLayout, senderEdit);
        giveFocus(messageLayout, messageEdit);

        setEditTextFocus(c, senderEdit, null);
        setEditTextLayoutFocus(c, messageBoxLayout, null, messageEdit, messageDivider);

        getGroupsList();

        messageEditLayout.setOnTouchListener((v, event) -> {
            messageEdit.requestFocus();
            return false;
        });
        progressIndicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        receiverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GroupDialogBuilder groupDialog = new GroupDialogBuilder(GroupMessageFragment.this, groups, selectedGroups, getView());
                groupDialog.setChangeListener(new GroupDialogBuilder.ChangeListener() {
                    @Override
                    public void onRemove() {
                        updateReceiversView();
                    }

                    @Override
                    public void onAdd() {
                        updateReceiversView();
                    }
                });
                if(groups.size() == 0) {
                    getGroupsList();
                } else if(noGroupsExist) {
                    String warnMessage = getString(R.string.warn_receiver_groups_not_exits);
                    SnackbarBuilder.showSnack(c, v, warnMessage, SnackbarBuilder.SnackType.WARNING);
                } else {
                    groupDialog.showDialog();
                }
            }
        });

        toolbarInit();
        handleAddAllGroups();
        handleRemainingChars();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendMessage();
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void toolbarInit() {

        ToolbarHandler.handleBackNav(this, toolbar);
        ToolbarHandler toolbarHandler = new ToolbarHandler(c, toolbar);
        toolbarHandler.setTitle((String) getText(R.string.send_group_message));

    }

    private void getGroupsList() {
        ConnectivityChecker connectivityChecker = new ConnectivityChecker(new ConnectivityChecker.ConnectionListener() {
            @Override
            public void isConnected(boolean status) {
                if(status) {
                    showProgress();
                    CredentialCrypter crypter = new CredentialCrypter(c);
                    User user = crypter.decrypt();
                    String username = user.getUsername();
                    String password = user.getPassword();

                    Retrofit retrofit = RetrofitClient.getClient();
                    RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
                    Call<List<Group>> call = retrofitInterface.getGroupsList(username, password);
                    call.enqueue(new Callback<List<Group>>() {
                        @Override
                        public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                            if(response.isSuccessful()) {
                                groups = response.body();
                                if(response.body().size() == 0) {
                                    noGroupsExist = true;
                                }
                            } else {
                                ConnectivityChecker.showServerFailSnack(c, v);
                            }
                            hideProgress();
                        }

                        @Override
                        public void onFailure(Call<List<Group>> call, Throwable t) {
                            ConnectivityChecker.showConnectionFailSnack(c, v, t);
                            hideProgress();
                        }
                    });
                } else {
                    ConnectivityChecker.showNoConnectionSnack(c, v);
                }
            }
        });
        connectivityChecker.checkConnection(requireActivity());
    }

    private void handleSendMessage() {
        ConnectivityChecker checker = new ConnectivityChecker(new ConnectivityChecker.ConnectionListener() {
            @Override
            public void isConnected(boolean status) {
                if(status) {
                    if(!checkEditNullWithResponse(c, v, new EditText[] {senderEdit, messageEdit},
                            new int[] {R.string.warn_empty_sender, R.string.warn_empty_message})) {
                        if (selectedGroups.size() != 0) {
                            BlacklistBottomSheet blacklistBottomSheet = new BlacklistBottomSheet();
                            blacklistBottomSheet.setBlacklistSendListener(new BlacklistBottomSheet.BlacklistSendListener() {
                                @Override
                                public void onClick(boolean sendToBlacklist) {
                                    shouldSendToBlackList = sendToBlacklist;
                                    sendMessage();
                                }
                            });
                            blacklistBottomSheet.show(getParentFragmentManager(), null);

                        } else {
                            String errorMsg = getString(R.string.warn_empty_receiver);
                            SnackbarBuilder.showSnack(c, getView(), errorMsg, SnackbarBuilder.SnackType.WARNING);
                        }
                    }
                } else {
                    ConnectivityChecker.showNoConnectionSnack(c, getView());
                }
            }
        });
        checker.checkConnection(requireActivity());
    }
    private void sendMessage() {
        showProgress();

        String sender = senderEdit.getText().toString();

        List<String> receiversId = new ArrayList<>();
        for(Group group : selectedGroups) {
            receiversId.add(group.getId());
        }

        String message = messageEdit.getText().toString();
        message = message.replace(Constants.DEFAULT_MESSAGE_END, "");

        CredentialCrypter crypter = new CredentialCrypter(c);
        User user = crypter.decrypt();

        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ResponseBody> call = retrofitInterface.sendGroupMessage(user.getUsername(), user.getPassword(), sender, message, receiversId, shouldSendToBlackList);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        try {
                            long code = Long.parseLong(result.split("_")[0]);
                            SnackbarBuilder.showSnack(c, getView(), getString(R.string.success_sending_single_sms), SnackbarBuilder.SnackType.SUCCESS);
                            getParentFragmentManager().popBackStack();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            SnackbarBuilder.showSnack(c, getView(), result, SnackbarBuilder.SnackType.ERROR);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ConnectivityChecker.showServerFailSnack(c, v);
                }
                hideProgress();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ConnectivityChecker.showConnectionFailSnack(c, v, t);
                hideProgress();
            }
        });
    }
    private void handleRemainingChars() {
        MessageCharacterWatcher messageCharacterWatcher = new MessageCharacterWatcher(c, messageEdit, remainingCharsText);
        messageEdit.addTextChangedListener(messageCharacterWatcher);
    }
    private void updateReceiversView() {

        StringBuilder stringBuilder = new StringBuilder();
        for(Group group : selectedGroups) {
            stringBuilder.append(group.getName());
            stringBuilder.append(", ");
        }
        if(stringBuilder.length() > 0) {
            int lastComma = stringBuilder.lastIndexOf(", ");
            stringBuilder.delete(lastComma, lastComma + 2);
        }
        String result = stringBuilder.toString();
        receiverText.setText(result);
    }
    private void handleAddAllGroups() {
        addAllGroupsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groups.size() != selectedGroups.size()) {
                    for(Group group : groups) {
                        if(!selectedGroups.contains(group)) {
                            selectedGroups.add(group);
                        }
                    }
                    updateReceiversView();
                }
                String successMessage = getString(R.string.success_adding_all_groups);
                SnackbarBuilder.showSnack(c, v, successMessage, SnackbarBuilder.SnackType.SUCCESS);
            }
        });
    }
    private void showProgress() {
        progressIndicator.setVisibility(View.VISIBLE);
    }
    private void hideProgress() {
        progressIndicator.setVisibility(View.GONE);
    }


}