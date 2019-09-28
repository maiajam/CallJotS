package com.maiajam.calljots.helper.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import com.maiajam.calljots.helper.Constant;


public class WindowsMangerHeperMethods {


    public static WindowManager.LayoutParams getWindoesMangerParam(int hint) {
        int TypesFLAG, Flags;
        if (Build.VERSION.SDK_INT >= 26) {
            TypesFLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            TypesFLAG = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            // ((Activity)context).setShowWhenLocked(true);
            Flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        } else {
            Flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                TypesFLAG,
                Flags,
                PixelFormat.TRANSPARENT);

        if (hint == Constant.AFTER_Call_HINT) {
            params.gravity = Gravity.TOP;
            params.x = 0;
            params.y = 5;
        } else {

            params.gravity = Gravity.CENTER_VERTICAL;
            params.x = 0;
            params.y = 20;
        }

        return params;
    }

}
