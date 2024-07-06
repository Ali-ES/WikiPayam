package ir.FiveMFive.FiveMFive.Fragment.SendMessage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.FiveMFive.FiveMFive.Java.ToolbarIcon;
import ir.FiveMFive.FiveMFive.Java.User;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.RetrofitClient;
import ir.FiveMFive.FiveMFive.RetrofitInterface;
import ir.FiveMFive.FiveMFive.Utility.ActivityContentResultHelper;
import ir.FiveMFive.FiveMFive.Utility.Checkers.ConnectivityChecker;
import ir.FiveMFive.FiveMFive.Utility.Checkers.MessageCharacterWatcher;
import ir.FiveMFive.FiveMFive.Utility.Constants;
import ir.FiveMFive.FiveMFive.Utility.CredentialCrypter;
import ir.FiveMFive.FiveMFive.Utility.Handlers.ExcelHandler;
import ir.FiveMFive.FiveMFive.Utility.Handlers.TextFileHandler;
import ir.FiveMFive.FiveMFive.Utility.PopupBuilder;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;
import ir.FiveMFive.FiveMFive.Utility.ToolbarHandler;
import ir.FiveMFive.FiveMFive.ViewCreators.NumberDialogBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static ir.FiveMFive.FiveMFive.Utility.UM.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.ViewsSetup.PriceDialogBuilder;


public class SingleMessageFragment extends Fragment {
    public static final String TAG = "SingleMessage";
    private static final String EXTRA_NUMBERS = "numbers";
    private Context c;
    private View v;
    private Toolbar toolbar;
    private LinearLayout senderLayout;
    private EditText senderEdit;
    private LinearLayout receiverLayout;
    private ConstraintLayout receiverEditLayout;
    private TextView receiverText;
    private ImageView add;
    private LinearLayout messageLayout;
    private NestedScrollView messageEditLayout;
    private ConstraintLayout messageBoxLayout;
    private EditText messageEdit;
    private View messageDivider;
    private TextView remainingCharsText;
    private Button sendMessage;
    private ActivityContentResultHelper contentResultHelper;
    private List<String> numbers;
    private FrameLayout progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResultHelper = new ActivityContentResultHelper(SingleMessageFragment.this);

        if(savedInstanceState != null) {
            numbers = savedInstanceState.getStringArrayList(EXTRA_NUMBERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_single_message, container, false);
        c = requireContext();
        toolbar = v.findViewById(R.id.toolbar);
        senderLayout = v.findViewById(R.id.sender_layout);
        senderEdit = v.findViewById(R.id.sender_et);
        receiverLayout = v.findViewById(R.id.receiver_layout);
        receiverEditLayout = v.findViewById(R.id.receiver_et_layout);
        receiverText = v.findViewById(R.id.receiver_tv);
        add = v.findViewById(R.id.add_iv);
        messageLayout = v.findViewById(R.id.message_layout);
        messageEditLayout = v.findViewById(R.id.message_et_layout);
        messageBoxLayout = v.findViewById(R.id.message_box_layout);
        messageEdit = v.findViewById(R.id.message_et);
        messageDivider = v.findViewById(R.id.message_divider);
        remainingCharsText = v.findViewById(R.id.remaining_chars_tv);
        sendMessage = v.findViewById(R.id.send_message_bt);
        progressIndicator = v.findViewById(R.id.progress_indicator);

        giveFocus(senderLayout, senderEdit);
        giveFocus(messageLayout, messageEdit);

        setEditTextFocus(c, senderEdit, null);
        setEditTextLayoutFocus(c, messageBoxLayout, null, messageEdit, messageDivider);

        numbers = new ArrayList<>();

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
                NumberDialogBuilder numberDialog = new NumberDialogBuilder(SingleMessageFragment.this, numbers, getView());
                numberDialog.setChangeListener(new NumberDialogBuilder.ChangeListener() {
                    @Override
                    public void onRemove() {
                        updateNumberView();
                    }

                    @Override
                    public void onAdd() {
                        updateNumberView();
                    }
                });
                numberDialog.showDialog();
            }
        });

        toolbarInit();

        handleAdd();

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
        outState.putStringArrayList(EXTRA_NUMBERS, (ArrayList<String>) numbers);
    }

    private void toolbarInit() {

        ToolbarHandler.handleBackNav(this, toolbar);
        ToolbarHandler toolbarHandler = new ToolbarHandler(c, toolbar);
        toolbarHandler.setTitle((String) getText(R.string.send_single_message));

        ToolbarIcon priceIcon = new ToolbarIcon(R.string.message_send_price, R.drawable.ic_price, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PriceDialogBuilder builder = new PriceDialogBuilder(c);
                builder.showDialog();
            }
        });
        toolbarHandler.addIcon(priceIcon);
    }

    private void handleSendMessage() {
        showProgress();
        ConnectivityChecker checker = new ConnectivityChecker(new ConnectivityChecker.ConnectionListener() {
            @Override
            public void isConnected(boolean status) {
                if(status) {
                    if(!checkEditNullWithResponse(c, v, new EditText[] {senderEdit, messageEdit},
                            new int[] {R.string.warn_empty_sender, R.string.warn_empty_message})) {
                        if (numbers.size() != 0) {
                            sendMessage();
                        } else {
                            String errorMsg = getString(R.string.warn_empty_receiver);
                            SnackbarBuilder.showSnack(c, getView(), errorMsg, SnackbarBuilder.SnackType.WARNING);
                            hideProgress();
                        }
                    } else {
                        hideProgress();
                    }
                } else {
                    ConnectivityChecker.showNoConnectionSnack(c, getView());
                    hideProgress();
                }
            }
        });
        checker.checkConnection(requireActivity());
    }
    private void sendMessage() {
        String sender = senderEdit.getText().toString();
        StringBuilder receiverComma = new StringBuilder();
        for(String number : numbers) {
            receiverComma.append(number);
            receiverComma.append(",");
        }
        if(receiverComma.toString().endsWith(",")) {
            receiverComma.deleteCharAt(receiverComma.length()-1);
        }
        String message = messageEdit.getText().toString();
        message = message.replace(Constants.DEFAULT_MESSAGE_END, "");


        CredentialCrypter crypter = new CredentialCrypter(c);
        User user = crypter.decrypt();

        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ResponseBody> call = retrofitInterface.sendSingleMessage(user.getUsername(),
                user.getPassword(), sender, receiverComma.toString(), message);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    try {
                        long code = Long.parseLong(result);
                        SnackbarBuilder.showSnack(c, getView(), getString(R.string.success_sending_single_sms), SnackbarBuilder.SnackType.SUCCESS);
                        getParentFragmentManager().popBackStack();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        SnackbarBuilder.showSnack(c, getView(), getString(R.string.error_sending_single_sms), SnackbarBuilder.SnackType.ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hideProgress();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgress();
            }
        });
    }
    private void handleRemainingChars() {
        MessageCharacterWatcher messageCharacterWatcher = new MessageCharacterWatcher(c, messageEdit, remainingCharsText);
        messageEdit.addTextChangedListener(messageCharacterWatcher);
    }
    private void handleAdd() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupBuilder builder = new PopupBuilder(c);
                View.OnClickListener contactImportListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
                builder.addItem(R.drawable.ic_contacts, R.string.import_via_contacts, contactImportListener);

                View.OnClickListener excelImportListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contentResultHelper.setContentResultListener(new ActivityContentResultHelper.ContentResultListener() {
                            @Override
                            public void gotResult(Uri uri) {
                                if(uri != null) {
                                    showProgress();
                                    ExcelHandler excelHandler = new ExcelHandler(c, uri);
                                    List<String> importedNumbers = excelHandler.getNumbers();
                                    numbers.addAll(importedNumbers);
                                    updateNumberView();
                                    hideProgress();
                                    excelHandler.showResultSnack(getView());
                                } else {
                                    contentResultHelper.showNoFileSelectedSnack();
                                }
                                builder.dismiss();
                            }
                        });
                        contentResultHelper.getExcel();
                    }
                };
                builder.addItem(R.drawable.ic_excel, R.string.import_via_excel, excelImportListener);

                View.OnClickListener textFileListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contentResultHelper.setContentResultListener(new ActivityContentResultHelper.ContentResultListener() {
                            @Override
                            public void gotResult(Uri uri) {
                                if(uri != null) {
                                    showProgress();
                                    TextFileHandler textFileHandler = new TextFileHandler(c, uri);
                                    List<String> importedMobiles = textFileHandler.getMobiles();
                                    numbers.addAll(importedMobiles);
                                    updateNumberView();
                                    hideProgress();
                                    textFileHandler.showResultSnack(getView());

                                } else {
                                    contentResultHelper.showNoFileSelectedSnack();
                                }
                                builder.dismiss();
                            }
                        });
                        contentResultHelper.getTextFile();
                    }
                };
                builder.addItem(R.drawable.ic_text_file, R.string.import_via_text_file, textFileListener);


                builder.showPopup(add);
            }
        });
    }
    private void updateNumberView() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 10 && i < numbers.size(); i++) {
            stringBuilder.append(numbers.get(i));
            stringBuilder.append(", ");
        }
        if(stringBuilder.length() > 0) {
            int lastComma = stringBuilder.lastIndexOf(", ");
            stringBuilder.delete(lastComma, lastComma + 2);
        }
        String result = stringBuilder.toString();
        receiverText.setText(result);
    }
    private void showProgress() {
        progressIndicator.setVisibility(View.VISIBLE);
    }
    private void hideProgress() {
        progressIndicator.setVisibility(View.GONE);
    }

}