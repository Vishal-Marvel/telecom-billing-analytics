package com.telecom.utils;

public class TaxUtils {

    public static double applyGST(double amount) {
        return amount * Constants.GST_RATE;
    }
}


