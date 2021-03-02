package com.hunglv.office.ss.sheetbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SheetButton extends LinearLayout {
    private static final int FONT_SIZE = 14;
    //
    private static final int SHEET_BUTTON_MIN_WIDTH = 120;

    /**
     * @param context
     */
    public SheetButton(Context context, String sheetName, int sheetIndex) {
        super(context);
        this.context = context;
        setOrientation(HORIZONTAL);
        this.sheetIndex = sheetIndex;

        init(context, sheetName);
    }

    /**
     *
     */
    private void init(Context context, String sheetName) {
        //Left icon
        left = new View(context);
        addView(left);

        // 
        textView = new TextView(context);
        textView.setBackgroundColor(Color.parseColor("#F0F4F7"));
        textView.setText(sheetName);
        textView.setTextSize(FONT_SIZE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setPadding(5, 0, 5, 0);

        int w = correctWidth(sheetName);
        w = Math.max(w, SHEET_BUTTON_MIN_WIDTH);
        LayoutParams params = new LayoutParams(w, LayoutParams.MATCH_PARENT);
        params.setMargins(0, 1, 1, 0);
        addView(textView, params);

        // right icon
        right = new View(context);
        right.setBackgroundColor(Color.BLUE);
        addView(right);
    }

    private int correctWidth(String text) {
        Rect bounds = new Rect();
        textView.getPaint().getTextBounds(text, 0, text.length(), bounds);
        return bounds.width() + 24;
    }

    /**
     *
     */
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
//                if(!active)
//                {
//                    left.setBackground(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_PUSH_LEFT));
//                    textView.setBackground(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_PUSH_MIDDLE));
//                    right.setBackground(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_PUSH_RIGHT));
//                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                if(!active)
//                {
//                    left.setBackground(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_LEFT));
//                    textView.setBackground(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_MIDDLE));
//                    right.setBackground(sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_RIGHT));
//                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 选中或取消选中用到
     */
    public void changeFocus(boolean gainFocus) {
        active = gainFocus;
        textView.setBackgroundColor(Color.parseColor(gainFocus ? "#FFFFFF" : "#F0F4F7"));
        textView.setTextColor(Color.parseColor(gainFocus ? "#297841" : "#000000"));
        //        left.setBackground(gainFocus ? sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_FOCUS_LEFT) :
//            sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_LEFT));
//        textView.setBackground(gainFocus ? sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_FOCUS_MIDDLE) :
//            sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_MIDDLE));
//        right.setBackground(gainFocus ? sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_FOCUS_RIGHT) :
//            sheetbarResManager.getDrawable(SheetbarResConstant.RESID_SHEETBUTTON_NORMAL_RIGHT));
    }

    /**
     *
     */
    public int getSheetIndex() {
        return this.sheetIndex;
    }

    /**
     *
     */
    public void dispose() {
        left = null;
        textView = null;
        right = null;
    }

    //
    private int sheetIndex;
    // Left icon
    private View left;
    // Text center
    private TextView textView;
    // Right icon
    private View right;
    private Context context;
    private boolean active;
}
