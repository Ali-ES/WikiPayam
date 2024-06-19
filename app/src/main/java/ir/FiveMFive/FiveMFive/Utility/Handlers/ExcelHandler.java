package ir.FiveMFive.FiveMFive.Utility.Handlers;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.Checkers.PhoneNumberFormatChecker;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;

public class ExcelHandler {
    private static final String TAG = "ExcelHandler";
    private Context c;
    private Uri data;
    private List<String> numbers;
    private boolean isExcelFormatWrong;
    public ExcelHandler(Context c, Uri uri) {
        this.c = c;
        this.data = uri;

        numbers = new ArrayList<>();

        readExcel();
    }
    private void readExcel() {
        try {
            InputStream inputStream = c.getContentResolver().openInputStream(data);

            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for(Row row : sheet) {
                if(row.getRowNum() > 0) {
                    Cell cell = row.getCell(0);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String number = cell.getStringCellValue();
                    if(PhoneNumberFormatChecker.checkNumberFormat(number)) {
                        numbers.add(number);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isExcelFormatWrong = true;
        }

    }
    public List<String> getNumbers() {
        return numbers;
    }
    public void showResultSnack(View v) {
        if(numbers.size() != 0) {
            String message = c.getString(R.string.success_importing_number);
            SnackbarBuilder.showSnack(c, v, message, SnackbarBuilder.SnackType.SUCCESS);
        } else if(isExcelFormatWrong) {
            String message = c.getString(R.string.wrong_excel_format);
            SnackbarBuilder.showSnack(c, v, message, SnackbarBuilder.SnackType.ERROR);
        } else {
            String message = c.getString(R.string.empty_numbers_list);
            SnackbarBuilder.showSnack(c, v, message, SnackbarBuilder.SnackType.WARNING);
        }
    }
}
