package ir.FiveMFive.FiveMFive.ViewsSetup;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.UM;
import static ir.FiveMFive.FiveMFive.Utility.UM.addThousandSeparator;

import java.util.ArrayList;
import java.util.List;

public class PriceDialogBuilder {
    private Context c;
    private LinearLayout main;
    private ScrollView itemsScroll;
    private LinearLayout itemsLayout;
    private List<LinePrice> linePrices;
    public PriceDialogBuilder(Context c) {
        this.c = c;
        main = new LinearLayout(c);
        itemsScroll = new ScrollView(c);
        itemsLayout = new LinearLayout(c);
        linePrices = new ArrayList<>();


        int startEndPad = (int) c.getResources().getDimension(R.dimen.pad_se_dialog_main);
        int topBottomPad = (int) c.getResources().getDimension(R.dimen.pad_tb_dialog_main);

        main.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(startEndPad, topBottomPad, startEndPad, topBottomPad);

        itemsScroll.setVerticalScrollBarEnabled(false);

        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scrollParams.gravity = Gravity.CENTER_HORIZONTAL;
        main.addView(itemsScroll, scrollParams);

        ScrollView.LayoutParams params = new ScrollView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        itemsLayout.setOrientation(LinearLayout.VERTICAL);
        itemsScroll.addView(itemsLayout, params);

        addTitle();
        populateList();
    }
    private void addTitle() {
        TextView title = new TextView(c);
        title.setText(c.getString(R.string.price_dialog_title));
        title.setTextSize(c.getResources().getDimension(R.dimen.ts_dialog_title)/2);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int bottomMargin = (int) c.getResources().getDimension(R.dimen.mg_b_dialog_title);
        params.bottomMargin = UM.toDp(c, bottomMargin);

        itemsLayout.addView(title, params);
    }

    private void populateList() {
        linePrices.add(new LinePrice("3000", "1439", "2800", "1520", "3024"));
        linePrices.add(new LinePrice("2000", "1495", "2800", "1221", "2330"));
        linePrices.add(new LinePrice("1000", "1400", "2800", "1182", "2341"));
        linePrices.add(new LinePrice("50002", "1422", "2783", "1159", "2268"));
        linePrices.add(new LinePrice("9999", "1680", "2800", "1680", "2800"));
        linePrices.add(new LinePrice("ثابت", "1103", "2632", "1170", "2783"));
        linePrices.add(new LinePrice("50004", "1215", "2800", "1288", "3024"));

        for(LinePrice linePrice : linePrices) {
            addItem(linePrice);
        }
    }

    private void addItem(LinePrice linePrice) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_price, null);

        int bottomMargin = (int) c.getResources().getDimension(R.dimen.mg_b_dialog_items);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = bottomMargin;
        itemsLayout.addView(view, params);


        TextView subtitleView = view.findViewById(R.id.price_subtitle_tv);
        String subtitle = c.getString(R.string.price_dialog_subtitle);
        String lineNumber = linePrice.getLine();
        String subtitleFormatted = String.format(subtitle, lineNumber);

        subtitleView.setText(addSpannableBackground(subtitleFormatted, lineNumber));


        TextView textView = view.findViewById(R.id.price_text_tv);
        String text = c.getString(R.string.price_dialog_default);
        String per = linePrice.getPer();
        String eng = linePrice.getEng();
        String irancellPer = linePrice.getIrancellPer();
        String irancellEng = linePrice.getIrancellEng();

        String textFormatted = String.format(text, per, eng, irancellPer, irancellEng);

        SpannableString output = addSpannableBackground(textFormatted, per, eng, irancellPer, irancellEng);
        textView.setText(output);


    }
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setView(main);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private SpannableString addSpannableBackground(String text, String highlight) {
        final int blueHighlight = c.getResources().getColor(R.color.blue_highlight);

        int start = text.indexOf(highlight);
        SpannableString output = new SpannableString(text);
        output.setSpan(new BackgroundColorSpan(blueHighlight), start, start + highlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return output;
    }
    private SpannableString addSpannableBackground(String text, String...highlights) {
        final int blueHighlight = c.getResources().getColor(R.color.blue_highlight);

        SpannableString output = new SpannableString(text);
        for(String highlight : highlights) {
            int start = text.indexOf(highlight);
            output.setSpan(new BackgroundColorSpan(blueHighlight), start, start + highlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return output;
    }


    private class LinePrice {
        private String line;
        private String per;
        private String eng;
        private String irancellPer;
        private String irancellEng;
        public LinePrice(String line, String per, String eng, String irancellPer, String irancellEng) {
            this.line = line;
            this.per = per;
            this.eng = eng;
            this.irancellPer = irancellPer;
            this.irancellEng = irancellEng;
        }

        public String getLine() {
            return line;
        }

        public String getPer() {
            return addThousandSeparator(per);
        }

        public String getEng() {
            return addThousandSeparator(eng);
        }

        public String getIrancellPer() {
            return addThousandSeparator(irancellPer);
        }

        public String getIrancellEng() {
            return addThousandSeparator(irancellEng);
        }
    }
}
