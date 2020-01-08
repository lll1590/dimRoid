package com.dim.ke.framework.core.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class RegexUtils {

    /**
     * 是否手机号.
     *
     * @param text
     * @return
     */
    public static boolean isCellPhone(String text) {
        return isMatch(text, "^(\\+?86)?(1[3456789]\\d{9})$");
    }

    /**
     * 判断文本是否全汉字.
     *
     * @param text
     * @return
     */
    public static boolean isChinese(String text) {
        return isMatch(text, "[\u4e00-\u9fa5]*");
    }

    public static boolean isEmail(String text) {
        return isMatch(text, "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
    }


    public static boolean isMatch(String text, String regularExpression) {
        if (!TextUtils.isEmpty(text)) {
            if (text.matches(regularExpression)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否座机号
     *
     * @param text
     * @return
     */
    public static boolean isPhone(String text) {
        return isMatch(text, "^0(10|2[012345789]|[3-9]\\d{2})-\\d{7,8}(-\\d{1,6})?$");
    }

    /**
     * 是否是Base64
     * @param str
     * @return
     */
    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    /**
     * 是否是颜色值
     * @param color
     * @return
     */
    public static boolean isColor(String color){
        String pattern = "^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$";
        return isMatch(color, pattern);
    }

    /**
     * 中文、英文、数字包括下划线
     * @param nick
     * @return
     */
    public static boolean isNick(String nick){
        String pattern = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
        return isMatch(nick, pattern);
    }

    /**
     * 是否是Upc码
     * @param digit
     * @return
     */
    public static boolean isUpccode(String digit){
        String pattern = "^\\d{8,13}$";
        String pattern2 = "^\\d{6}$";
        return isMatch(digit, pattern) || isMatch(digit, pattern2);
    }

    /**
     * T11 礼品卡规则
     * @param resultStr
     * @return
     */
    public static boolean isGiftCart(String resultStr) {
        if(!resultStr.contains("_")){
            return false;
        }
        String[] gifts = resultStr.split("_");
        String numPattern = "^\\d{18}$";
        boolean isMathcNum = isMatch(gifts[0], numPattern);
        boolean isMatchPwd = gifts[1].length() == 16;
        if(isMatchPwd && isMathcNum){
            return true;
        }
        return false;
    }
}
