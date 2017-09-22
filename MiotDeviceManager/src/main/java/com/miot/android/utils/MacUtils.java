package com.miot.android.utils;

import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Created by xdf on 2016/11/21.
 */

public class MacUtils {

    static int m[] = { 237221, 23688, 234862, 123878, 233214, 1234123, 234883,
            188231, 28367, 18375, 287345, 283245, 98945, 84755, 892734, 99883 };

    public static boolean isMacAddress(String macAddress) {
        String reg = "^([0-9a-fA-F]){2}([:][0-9a-fA-F]{2}){6}";
        if (macAddress.equals("00.00.00.00.00")) {
            return false;
        }
        return Pattern.compile(reg).matcher(macAddress).find();
    }
    public static String mac2serial(String mac) {
        if (mac == null) {
            return null;
        }
        mac = mac.replace(":", "");
        if (mac.contains("_")) {
            mac = mac.split("_")[0];
        }
        if (mac.length() != 12) {
            return null;
        }
        char[] macChar = mac.toCharArray();
        int[] n = new int[12];
        for (int i = 0; i < macChar.length; i++) {
            n[i] = Integer.parseInt(("" + macChar[i]), 16);
        }

        BigInteger num1 = new BigInteger(m[n[0]] * 113 * m[n[7]] + "")
                .add(new BigInteger(m[n[2]] * 3131 * (m[n[5]] + 13) + ""))
                .add(new BigInteger("" + m[n[3]] * 932 * (m[n[8]] + 9)))
                .add(new BigInteger("" + m[n[1]] * (m[n[4]] + 3342)))
                .add(new BigInteger("" + m[n[6]] * 32 * m[n[9]]))
                .add(new BigInteger("" + m[n[10]] * m[n[11]] * 23));
        BigInteger num2 = new BigInteger(m[n[0]] + "")
                .multiply(new BigInteger("11213"))
                .multiply(new BigInteger(m[n[7]] + ""))
                .add(new BigInteger(m[n[2]] + "").multiply(
                        new BigInteger("3121231")).multiply(
                        new BigInteger((m[n[5]] + 13) + "")))
                .add(new BigInteger(m[n[3]] + "").multiply(
                        new BigInteger("9321237")).multiply(
                        new BigInteger((m[n[8]] + 9) + "")))
                .add(new BigInteger(m[n[1]] + "").multiply(new BigInteger(
                        (m[n[4]] + 33473232) + "")))
                .add(new BigInteger(m[n[6]] + "").multiply(
                        new BigInteger("32322")).multiply(
                        new BigInteger(m[n[9]] + "")))
                .add(new BigInteger(m[n[10]] + "").multiply(new BigInteger(
                        m[n[11]] + ""))).mod(new BigInteger("4294967296"));

        BigInteger first = num1.mod(new BigInteger("4294967296")).mod(
                new BigInteger("100"));
        StringBuilder firstStr = new StringBuilder(first.toString());

        StringBuilder secondStr = new StringBuilder(num2.toString());
        int firSize = firstStr.length();
        for (int i = 0; i < 2 - firSize; i++) {
            firstStr = new StringBuilder("0").append(firstStr);
        }
        int secSize = secondStr.length();
        if (secondStr.length() >= 10) {
            secondStr = new StringBuilder(secondStr.toString().substring(
                    secSize - 10, secSize));
        } else {
            for (int i = 0; i < (10 - secSize); i++) {
                secondStr = new StringBuilder("0").append(secondStr);
            }
        }
        return new StringBuilder(firstStr).append(secondStr).toString();
    }
}
