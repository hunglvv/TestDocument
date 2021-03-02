package com.hunglv.office.ss.sheetbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Insets;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.hunglv.office.constant.EventConstant;
import com.hunglv.office.system.IControl;

import java.util.Vector;

public class SheetBar extends HorizontalScrollView implements OnClickListener {
    /**
     * @param context
     */
    public SheetBar(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param control
     */
    public SheetBar(Context context, IControl control, int minimumWidth) {
        super(context);
        this.control = control;
        this.setVerticalFadingEdgeEnabled(false);
        this.setFadingEdgeLength(0);
        if (minimumWidth == getResources().getDisplayMetrics().widthPixels) {
            this.minimumWidth = -1;
        } else {
            this.minimumWidth = minimumWidth;
        }
        init();
    }

    /**
     *
     */
    public void onConfigurationChanged(Configuration newConfig) {
        sheetbarFrame.setMinimumWidth(minimumWidth == -1 ? getResources().getDisplayMetrics().widthPixels
                : minimumWidth);
    }

    /**
     *
     */
    private void init() {
        Context context = this.getContext();
        sheetbarFrame = new LinearLayout(context);
        sheetbarFrame.setGravity(Gravity.BOTTOM);

        sheetbarFrame.setBackgroundColor(Color.parseColor("#E2E6E9"));
        sheetbarFrame.setOrientation(LinearLayout.HORIZONTAL);
        sheetbarFrame.setMinimumWidth(minimumWidth == -1 ? getResources().getDisplayMetrics().widthPixels
                : minimumWidth);
        float dp = 48;
        sheetbarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, sheetbarHeight);
        // left shadow
        this.sheetbarFrame.addView(new View(context), params);
        // sheetButton
        @SuppressWarnings("unchecked")
        Vector<String> vec = (Vector<String>) control.getActionValue(EventConstant.SS_GET_ALL_SHEET_NAME, null);
        LayoutParams paramsButton = new LayoutParams(LayoutParams.WRAP_CONTENT, sheetbarHeight);
        int count = vec.size();
        for (int i = 0; i < count; i++) {
            SheetButton sb = new SheetButton(context, vec.get(i), i);
            if (currentSheet == null) {
                currentSheet = sb;
                currentSheet.changeFocus(true);
            }
            sb.setOnClickListener(this);
            sheetbarFrame.addView(sb, paramsButton);

            if (i < count - 1) {
                View view = new View(context);
                sheetbarFrame.addView(view, paramsButton);
            }
        }

        // 右边shadow
        View right = new View(context);
        sheetbarFrame.addView(right, params);

        //
        addView(sheetbarFrame, new LayoutParams(LayoutParams.WRAP_CONTENT, sheetbarHeight));
    }

    /**
     *
     */
    public void onClick(View v) {
        currentSheet.changeFocus(false);

        SheetButton sb = (SheetButton) v;
        sb.changeFocus(true);
        currentSheet = sb;

        control.actionEvent(EventConstant.SS_SHOW_SHEET, currentSheet.getSheetIndex());
    }

    /**
     * set focus sheet button(called when clicked document hyperlink)
     *
     * @param index
     */
    public void setFocusSheetButton(int index) {
        if (currentSheet.getSheetIndex() == index) {
            return;
        }

        int count = sheetbarFrame.getChildCount();
        View view = null;
        for (int i = 0; i < count; i++) {
            view = sheetbarFrame.getChildAt(i);
            if (view instanceof SheetButton && ((SheetButton) view).getSheetIndex() == index) {
                currentSheet.changeFocus(false);

                currentSheet = (SheetButton) view;
                currentSheet.changeFocus(true);
                break;
            }
        }

        //sheet bar scrolled
        int screenWidth;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = control.getActivity().getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            screenWidth = windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            control.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;
        }
        int barWidth = sheetbarFrame.getWidth();
        if (barWidth > screenWidth && view != null) {
            int left = view.getLeft();
            int right = view.getRight();
            int off = (screenWidth - (right - left)) / 2;

            off = left - off;
            if (off < 0) {
                off = 0;
            } else if (off + screenWidth > barWidth) {
                off = barWidth - screenWidth;
            }

            scrollTo(off, 0);
        }
    }

    /**
     * @return Returns the sheetbarHeight.
     */
    public int getSheetbarHeight() {
        return sheetbarHeight;
    }

    /**
     *
     */
    public void dispose() {
        currentSheet = null;
        if (sheetbarFrame != null) {
            int count = sheetbarFrame.getChildCount();
            View v;
            for (int i = 0; i < count; i++) {
                v = sheetbarFrame.getChildAt(i);
                if (v instanceof SheetButton) {
                    ((SheetButton) v).dispose();
                }
            }
            sheetbarFrame = null;
        }
    }

    //
    private int minimumWidth;
    //
//    private SheetbarResManager sheetbarResManager;
    //
    private int sheetbarHeight;
    //
    private SheetButton currentSheet;
    //
    private IControl control;
    //
    private LinearLayout sheetbarFrame;

}
