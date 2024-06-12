package ir.FiveMFive.FiveMFive.Utility.Checkers;

import static ir.FiveMFive.FiveMFive.Utility.Constants.DEFAULT_MESSAGE_CHARS;
import static ir.FiveMFive.FiveMFive.Utility.Constants.DEFAULT_MESSAGE_END;

import android.content.Context;

import ir.FiveMFive.FiveMFive.R;

public class MessageCharacterController {
    private Context c;
    private String text;
    public MessageCharacterController(Context c, String input) {
        this.c = c;
        this.text = input;

        if(!text.endsWith(DEFAULT_MESSAGE_END)) {
            text += "\n" + DEFAULT_MESSAGE_END;
        }
        System.out.println(text);
    }
    public String getCharactersCount() {
        int length = text.length();

        int messageCount = length / DEFAULT_MESSAGE_CHARS + 1;
        int remainingChars = DEFAULT_MESSAGE_CHARS - (length % DEFAULT_MESSAGE_CHARS);

        String charCountText = c.getString(R.string.remaining_chars_);

        return String.format(charCountText, remainingChars, messageCount, length);
    }
    public String getText() {
        return this.text;
    }

    public static String getCharacterCountDefault(Context c) {
        return String.format(c.getString(R.string.remaining_chars_), DEFAULT_MESSAGE_CHARS, 1, 0);
    }

}
