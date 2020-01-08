package com.dim.ke.framework.core.util;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String HtmlBlack = "#000000";
    public static final String HtmlGray = "#808080";
    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static Spanned stringToSpan(String src) {
        return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
    }

    public static boolean IsNullOrEmptyString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    public static String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("<font color=").append(color).append(">").append(src).append("</font>");
        return strBuf.toString();
    }

    public static String makeHtmlNewLine() {
        return "<br />";
    }

    public static String makeHtmlSpace(int number) {
        final String space = "&nbsp;";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append(space);
        }
        return result.toString();
    }

    /**
     * 判断是不是在有效范围内
     */
    public static boolean IsOverTime(String lastTime, int intervalTime) {
        if (lastTime == null || lastTime == "") {
            return true;
        }
        Date beforeTime = null;
        try {
            beforeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastTime);
        } catch (ParseException e) {
            return true;
        }
        if (beforeTime != null) {
            int minute = (int) (new Date().getTime() - beforeTime.getTime()) / (1000 * 60);
            Log.i("mytest", "已过：" + minute + "分");
            if (minute < intervalTime) {
                return false;
            }
        }

        return true;
    }

    /**
     * MD5加密字符串
     */
    public static String md5(String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(source.getBytes());
            byte[] mess = digest.digest();
            return toHexString(mess);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return source;
    }

    public static String get32MD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    public static String toHexString(byte[] b) { // byte to String
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 判断是否是JSON 数据
     *
     * @param jsonData
     * @return
     */
    public static boolean isJson(String jsonData) {
        boolean isJson = false;
        if ("".equals(jsonData) || null == jsonData) {
            return isJson;
        }
        try {
            new JSONObject(jsonData);
            isJson = true;
        } catch (Exception e) {

            try {
                new JSONArray(jsonData);
                isJson = true;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return isJson;
    }

    /**
     * 判断是否是json数组数据
     *
     * @param jsonData
     * @return
     */
    public static boolean isJsonArray(String jsonData) {
        boolean isJson = false;
        if ("".equals(jsonData) || null == jsonData) {
            return isJson;
        }

        try {
            new JSONArray(jsonData);
            isJson = true;
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return isJson;
    }

    /**
     * 判断是否是json类型数据
     *
     * @param jsonData
     * @return
     */
    public static boolean isJsonObject(String jsonData) {
        boolean isJson = false;
        if ("".equals(jsonData) || null == jsonData) {
            return isJson;
        }
        try {
            new JSONObject(jsonData);
            isJson = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isJson;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    /**
     * 隐藏银行卡号
     * 隐藏60%信息
     * 如果小于3个字符，则全部隐藏
     * 大于3个字符，中间的60%隐藏
     *
     * @param s
     * @return
     */
    public static String hideInfo(String s) {
        if (TextUtils.isEmpty(s)) {
            return "*";
        }
        StringBuffer sb = new StringBuffer();
        int length = s.length();
        if (length < 4) {

            return "***";

        }

        int hideLength = (int) Math.floor(length * 0.6);

        int start = (length - hideLength) / 2;
        int end = start + hideLength;

        for (int i = 0; i < length; i++) {
            if (i > start && i <= end) {
                sb.append("*");
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     *
     * @Title: getPostParams
     * @Description: 将实体类clazz的属性转换为url参数
     * @param clazz	参数实体类
     * @return
     * String
     */
    public static String getPostParams(Object clazz) {
        if(clazz == null){
            return "";
        }
        // 遍历属性类、属性值
        Field[] fields = clazz.getClass().getDeclaredFields();

        StringBuilder requestURL = new StringBuilder();
        try {
            boolean flag = true;
            String property, value;
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                // 允许访问私有变量
                field.setAccessible(true);

                // 属性名
                property = field.getName();
                // 属性值
                value = field.get(clazz).toString();

                String params = property + "=" + value;
                if (flag) {
                    requestURL.append(params);
                    flag = false;
                } else {
                    requestURL.append("&" + params);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestURL.toString();
    }
}
