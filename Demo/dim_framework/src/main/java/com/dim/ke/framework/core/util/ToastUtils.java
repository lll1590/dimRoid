package com.dim.ke.framework.core.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.dim.ke.framework.R;


public class ToastUtils {

    public static void showToast(Context context, String msg)
    {
        if(context != null && !TextUtils.isEmpty(msg)) {
//            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            showCustomToast(context, msg);
        }
    }

    public static void showToast(Context context, int resId)
    {
        if(context != null && resId != -1) {
//           Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
            showCustomToast(context, context.getString(resId));
        }
    }

    private static Toast mToast = null;

    private static void showCustomToast(Context context, CharSequence msg)
    {
        if (null != mToast) {
            mToast.cancel();
        }
        Toast toast = new Toast(context.getApplicationContext());
        TextView textView = (TextView) LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.custom_toast_layout, null, false);
        textView.setText(msg);
        toast.setView(textView);
//        toast.setGravity(Gravity.CENTER, 0, ToolHelper.dip2px(context, 20));
        toast.setGravity(Gravity.TOP, 0, ToolHelper.dip2px(context, 265));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        mToast = toast;
    }
}
