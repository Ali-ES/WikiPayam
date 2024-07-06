package ir.FiveMFive.FiveMFive.Utility.Checkers;

import static ir.FiveMFive.FiveMFive.Utility.Constants.DEFAULT_ALLOWED_MESSAGE_CHARS;
import static ir.FiveMFive.FiveMFive.Utility.Constants.DEFAULT_MESSAGE_CHARS;
import static ir.FiveMFive.FiveMFive.Utility.Constants.DEFAULT_MESSAGE_END;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import ir.FiveMFive.FiveMFive.R;

public class MessageCharacterWatcher implements TextWatcher {
    private final long DELAY = 300;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private Context c;
    private EditText messageEdit;
    private TextView remainingChars;
    private String outputText = "";
    public MessageCharacterWatcher(Context c, EditText messageEdit, TextView remainingChars) {
        this.c = c;
        this.messageEdit = messageEdit;
        this.remainingChars = remainingChars;

        remainingChars.setText(getCharacterCountDefault());
    }
    public String getCharactersCountText() {
        int messageLength = outputText.length() - DEFAULT_MESSAGE_END.length() ;
        int messageCount = Math.abs(messageLength / DEFAULT_ALLOWED_MESSAGE_CHARS) + 1;


        int remainingChars = DEFAULT_ALLOWED_MESSAGE_CHARS - (messageLength % DEFAULT_ALLOWED_MESSAGE_CHARS);

        int characterCount = messageLength + (messageCount * 6);

        String charCountText = c.getString(R.string.remaining_chars_);
        return String.format(charCountText, remainingChars, messageCount, characterCount);
    }

    public String getCharacterCountDefault() {
        return String.format(c.getString(R.string.remaining_chars_), DEFAULT_MESSAGE_CHARS, 1, 0);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                messageEdit.removeTextChangedListener(MessageCharacterWatcher.this);

                int selection = messageEdit.getSelectionEnd();
                String message = messageEdit.getText().toString();
                if(!message.endsWith(DEFAULT_MESSAGE_END)) {
                    message += DEFAULT_MESSAGE_END;
                    messageEdit.setText(message);
                }
                outputText = message;

                try {
                    messageEdit.setSelection(selection);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                remainingChars.setText(getCharactersCountText());

                messageEdit.addTextChangedListener(MessageCharacterWatcher.this);
            }
        };
        handler.postDelayed(runnable, DELAY);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
