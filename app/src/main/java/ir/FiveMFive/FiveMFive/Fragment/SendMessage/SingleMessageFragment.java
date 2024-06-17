package ir.FiveMFive.FiveMFive.Fragment.SendMessage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.FiveMFive.FiveMFive.Java.ToolbarIcon;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.ActivityContentResultHelper;
import ir.FiveMFive.FiveMFive.Utility.Checkers.MessageCharacterController;
import ir.FiveMFive.FiveMFive.Utility.Checkers.PhoneNumberFormatChecker;
import ir.FiveMFive.FiveMFive.Utility.PopupBuilder;
import ir.FiveMFive.FiveMFive.Utility.ToolbarHandler;

import static ir.FiveMFive.FiveMFive.Utility.UM.*;

import java.io.InputStream;

public class SingleMessageFragment extends Fragment {
    public static final String TAG = "SingleMessage";
    private Context c;
    private View v;
    private Toolbar toolbar;
    private LinearLayout senderLayout;
    private EditText senderEdit;
    private LinearLayout receiverLayout;
    private ConstraintLayout receiverEditLayout;
    private EditText receiverEdit;
    private View receiverDivider;
    private ImageView add;
    private LinearLayout messageLayout;
    private NestedScrollView messageEditLayout;
    private ConstraintLayout messageBoxLayout;
    private EditText messageEdit;
    private View messageDivider;
    private TextView remainingCharsText;
    private Button sendMessage;
    private ActivityContentResultHelper contentResultHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResultHelper = new ActivityContentResultHelper(SingleMessageFragment.this);
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
        receiverEdit = v.findViewById(R.id.receiver_et);
        receiverDivider = v.findViewById(R.id.receiver_divider);
        add = v.findViewById(R.id.add_iv);
        messageLayout = v.findViewById(R.id.message_layout);
        messageEditLayout = v.findViewById(R.id.message_et_layout);
        messageBoxLayout = v.findViewById(R.id.message_box_layout);
        messageEdit = v.findViewById(R.id.message_et);
        messageDivider = v.findViewById(R.id.message_divider);
        remainingCharsText = v.findViewById(R.id.remaining_chars_tv);
        sendMessage = v.findViewById(R.id.send_message_bt);

        giveFocus(senderLayout, senderEdit);
        giveFocus(receiverLayout, receiverEdit);
        giveFocus(messageLayout, messageEdit);

        setEditTextFocus(c, senderEdit, null);
        setEditTextLayoutFocus(c, receiverEditLayout, null, receiverEdit, receiverDivider);
        setEditTextLayoutFocus(c, messageBoxLayout, null, messageEdit, messageDivider);

        messageEditLayout.setOnTouchListener((v, event) -> {
            messageEdit.requestFocus();
            return false;
        });

        toolbarInit();

        handleAdd();

        handleRemainingChars();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        return v;
    }


    private void toolbarInit() {

        ToolbarHandler.handleBackNav(this, toolbar);
        ToolbarHandler toolbarHandler = new ToolbarHandler(c, toolbar);
        toolbarHandler.setTitle((String) getText(R.string.send_single_message));

        ToolbarIcon priceIcon = new ToolbarIcon(R.string.message_send_price, R.drawable.ic_price, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        toolbarHandler.addIcon(priceIcon);
        ToolbarHandler.handleBackNav(this, toolbar);
    }

    private void sendMessage() {
        String numbers = receiverEdit.getText().toString();
        String faultyNumber = PhoneNumberFormatChecker.checkFaultyNumber(numbers);
        if(faultyNumber != null) {
            Toast.makeText(requireContext(), faultyNumber, Toast.LENGTH_SHORT).show();
        }

    }
    private void handleRemainingChars() {
        remainingCharsText.setText(MessageCharacterController.getCharacterCountDefault(c));
        messageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                messageEdit.removeTextChangedListener(this);

                String text = messageEdit.getText().toString();
                MessageCharacterController characterController = new MessageCharacterController(c, text);
                String output = characterController.getText();
                messageEdit.setText(output);
                remainingCharsText.setText(characterController.getCharactersCount());
                try {
                    messageEdit.setSelection(output.length() - 6);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                messageEdit.addTextChangedListener(this);
            }
        });
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
                                    Log.v(TAG, "uri is not null");
                                } else {
                                    Log.v(TAG, "uri IS null");
                                }
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
                                    Log.v(TAG, "uri is not null");
                                } else {
                                    Log.v(TAG, "uri IS null");
                                }
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

}