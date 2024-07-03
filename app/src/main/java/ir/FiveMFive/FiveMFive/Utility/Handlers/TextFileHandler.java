package ir.FiveMFive.FiveMFive.Utility.Handlers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.Checkers.PhoneNumberFormatChecker;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;
import ir.FiveMFive.FiveMFive.Utility.UM;

public class TextFileHandler {
    private static final String TAG = "TextFileHandler";
    private Context c;
    private Uri data;
    private List<String> mobiles;
    private boolean isTextFileFormatWrong;
    public TextFileHandler(Context c, Uri uri) {
        this.c = c;
        this.data = uri;

        mobiles = new ArrayList<>();

        readTextFile();
    }
    private void readTextFile() {
        try {
            InputStream inputStream = c.getContentResolver().openInputStream(data);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            for(String line; (line = reader.readLine()) != null;){
                line = line.trim();
                if(PhoneNumberFormatChecker.checkNumberFormat(line)) {
                    mobiles.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isTextFileFormatWrong = true;
        }
    }
    public List<String> getMobiles() {
        return mobiles;
    }
    public void showResultSnack(View v) {
        if(mobiles.size() != 0) {
            String message = c.getString(R.string.success_importing_number);
            SnackbarBuilder.showSnack(c, v, message, SnackbarBuilder.SnackType.SUCCESS);
        } else if(isTextFileFormatWrong) {
            String message = c.getString(R.string.wrong_excel_format);
            SnackbarBuilder.showSnack(c, v, message, SnackbarBuilder.SnackType.ERROR);
        } else {
            String message = c.getString(R.string.empty_numbers_list);
            SnackbarBuilder.showSnack(c, v, message, SnackbarBuilder.SnackType.WARNING);
        }

    }
}
