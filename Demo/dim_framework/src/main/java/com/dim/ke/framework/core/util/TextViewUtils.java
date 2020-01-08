package com.dim.ke.framework.core.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class TextViewUtils
{
    public static SpannableString setSpannableColorText(String data, String destDate, int color)
    {
        return setSpannableText(data, destDate, color, 0);
    }

    public static SpannableString setSpannableSizeText(String data, String destDate, int size)
    {
        return setSpannableText(data, destDate, 0, size);
    }



    /**
     * 
     * @Title: setTextView
     * @Description: 给textView 设置不同颜色、大小
     * @param data
     *            修改颜色与大小字段
     * @param color
     *            修改的颜色值
     * @param size
     *            修改的字体大小
     * @return
     * @throws
     */
    public static SpannableString setSpannableText(String data, String destDate, int color, int size)
    {
        if(TextUtils.isEmpty(data)){
            return SpannableString.valueOf("");
        }
        int index = data.indexOf(destDate);
        if(index == -1){
            return SpannableString.valueOf(data);
        }
        SpannableString msp = new SpannableString(data);
        if(size > 0) {
            msp.setSpan(new AbsoluteSizeSpan(size, true), index, index + destDate.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(color != 0) {
            msp.setSpan(new ForegroundColorSpan(color), index, index + destDate.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return msp;
    }


    /**
     * 设置字体
     * @param data
     * @param destDate
     * @param font android.graphics.Typeface.BOLD
     * @return
     */
    public static SpannableString setSpannableFontStyleText(String data, String destDate, int font,int size){
        if(TextUtils.isEmpty(data)){
            return SpannableString.valueOf("");
        }
        int index = data.indexOf(destDate);
        if(index == -1){
            return SpannableString.valueOf(data);
        }
        SpannableString msp = new SpannableString(data);

        if(font != 0) {
            msp.setSpan(new StyleSpan(font), index, index + destDate.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(size > 0) {
            msp.setSpan(new AbsoluteSizeSpan(size, true), index, index + destDate.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return msp;

    }

    /**
     * 格式化字符串
     * @param content
     * @param step
     * @return
     */
    public static String formatText(String content, int step, String divideStr){
        if(content.length() < step){
            return content;
        }
        String result = "";
        int startIndex = 0;
        do{
            int endIndex;
            if(startIndex + step < content.length()){
                endIndex = startIndex + step;
            }else{
                endIndex = content.length();
            }
            if(endIndex - startIndex < step || endIndex == content.length()){
                result += content.substring(startIndex, endIndex);
            }else {
                result += (content.substring(startIndex, endIndex) + divideStr);
            }
            startIndex += step;
        }while (startIndex < content.length());

        return result;
    }

    public static void main(String[] aa){
        System.out.println(formatText("12", 4, "-"));
    }
}
