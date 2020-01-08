package com.dim.ke.framework.core.util;


import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DigitFormatUtils {


    /**
     * 保留两位小数
     * @param number
     * @return
     */
    public static double formatDouble(double number){
        return Math.floor(number * 100) / 100;
    }

    /**
     * 小数位有值，则保留两位小数，否则不保留小数
     *
     * @param number
     * @return
     */
    public static String formatDecimal(double number) {
        String numStr = String.valueOf(number);
        int index = numStr.indexOf('.');
        if (index > 0) {
            numStr = numStr.substring(index + 1);
            if (Double.valueOf(numStr) > 0) {
                return formatDecimal(number, 2);
            }
            return formatDecimal(number, 0);
        } else {
            return formatDecimal(number, 0);
        }
    }

    /**
     * format a number properly with the given number of digits
     *
     * @param number the number to format
     * @param digits the number of digits
     * @return
     */
    public static String formatDecimal(double number, int digits) {

        StringBuffer a = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                a.append(".");
            a.append("0");
        }

//        DecimalFormat nf = new DecimalFormat("###,###,###,##0" + a.toString());
        DecimalFormat nf = new DecimalFormat("###########0" + a.toString());
        String formatted = nf.format(number);
        return formatted;
    }

    /**
     * Math.pow(...) is very expensive, so avoid calling it and create it
     * yourself.
     */
    private static final int POW_10[] =
            {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    /**
     * Formats the given number to the given number of decimals, and returns the
     * number as a string, maximum 35 characters.
     *
     * @param number
     * @param digitCount
     * @param separateThousands set this to true to separate thousands values
     * @return
     */
    public static String formatNumber(float number, int digitCount, boolean separateThousands) {

        char[] out = new char[35];

        boolean neg = false;
        if (number == 0) {
            return "0";
        }

        boolean zero = false;
        if (number < 1 && number > -1) {
            zero = true;
        }

        if (number < 0) {
            neg = true;
            number = -number;
        }

        if (digitCount > POW_10.length) {
            digitCount = POW_10.length - 1;
        }

        number *= POW_10[digitCount];
        long lval = Math.round(number);
        int ind = out.length - 1;
        int charCount = 0;
        boolean decimalPointAdded = false;

        while (lval != 0 || charCount < (digitCount + 1)) {
            int digit = (int) (lval % 10);
            lval = lval / 10;
            out[ind--] = (char) (digit + '0');
            charCount++;

            // add decimal point
            if (charCount == digitCount) {
                out[ind--] = '.';
                charCount++;
                decimalPointAdded = true;

                // add thousand separators
            } else if (separateThousands && lval != 0 && charCount > digitCount) {

                if (decimalPointAdded) {

                    if ((charCount - digitCount) % 4 == 0) {
                        out[ind--] = '.';
                        charCount++;
                    }

                } else {

                    if ((charCount - digitCount) % 4 == 3) {
                        out[ind--] = '.';
                        charCount++;
                    }
                }
            }
        }

        // if number around zero (between 1 and -1)
        if (zero)
            out[ind--] = '0';

        // if the number is negative
        if (neg)
            out[ind--] = '-';

        return new String(out);
    }

    /**
     * rounds the given number to the next significant number
     *
     * @param number
     * @return
     */
    public static float roundToNextSignificant(double number) {
        final float d = (float) Math.ceil((float) Math.log10(number < 0 ? -number : number));
        final int pw = 1 - (int) d;
        final float magnitude = (float) Math.pow(10, pw);
        final long shifted = Math.round(number * magnitude);
        return shifted / magnitude;
    }

    //    Android double类型保留到小数点两位，四舍五入scale就是需要传入保存小数点后几位的值。
    public static double round(Double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = null == v ? new BigDecimal("0.00") : new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    //两个Double数相加


    public static Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }


    //两个Double数相减
    public static Double sub(Double v1, Double v2) {

//        BigDecimal b1 = new BigDecimal(v1.toString());
//        BigDecimal b2 = new BigDecimal(v2.toString());

        BigDecimal b1 = new BigDecimal(formatDecimal(v1, 2));
        BigDecimal b2 = new BigDecimal(formatDecimal(v2, 2));

        return b1.subtract(b2).doubleValue();

    }


    // 两个Double数相乘

    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }


    //两个Double数相除
    public static Double div(Double v1, Double v2,int scale) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }





}

