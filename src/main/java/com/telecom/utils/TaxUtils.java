package com.telecom.utils;

public class TaxUtils {
    private static final double GST_RATE = 0.18; // 18% GST

    public static double applyGST(double amount) {
        return amount * GST_RATE;
        }
}


