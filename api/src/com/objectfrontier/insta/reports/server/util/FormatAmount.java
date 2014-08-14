/* $Header$ */

/*
 * @(#)FormatAmount.java
 *
 * Copyright by ObjectFrontier, Inc.,
 * 2050 Marconi Drive, Alpharetta, GA 30005, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ObjectFrontier, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with ObjectFrontier.
 */
package com.objectfrontier.insta.reports.server.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.objectfrontier.insta.InstaDefaultConstants;

/**
 * @author pRasanna
 * @date   Sep 6, 2004
 * @since  Fetrem 1.0; Sep 6, 2004
 */
public class FormatAmount {

    private static final DecimalFormat rupeeFormat = new DecimalFormat("#,##");
//    private static final DecimalFormat rupeeFormat = new DecimalFormat("'Rs.'#,##");
    private static final DecimalFormat doubleStringFormat = new DecimalFormat("0.00");

    public static void main(String[] args) {
        

        log(formatINRAmount(12345678901L));
        log(formatINRAmount(12345678901.2));
        log(formatINRAmount(12345678901.2345));
        log(formatINRAmount(12345678901.2545));
        log(formatINRAmount(12345678901.2354));
    log("\n");
        log(formatINRAmount(1234567890L));
        log(formatINRAmount(123456789));
        log(formatINRAmount(12345678));
        log(formatINRAmount(123456));
        log(formatINRAmount(1234));

//        double amount = 12345678901.2345;
//        log(NumberFormat.getCurrencyInstance(new Locale("en","IN")).format(amount));
//        log(NumberFormat.getCurrencyInstance().format(amount));
    }

    public static String formatINRAmount(double amount) {

        String as = doubleStringFormat.format(amount);
        if (as.equalsIgnoreCase(".00")) as = "0.00";
        int i = as.length() - 3;

        long l = Long.parseLong(as.substring(0, i));
        String decimal = as.substring(i);

        String result = (rupeeFormat.format(l / 10) + l % 10 + decimal);
        String[] parts = result.split(",");
        if (parts.length > 4) {
            result = new StringBuffer(result).replace(parts[0].length(), parts[0].length() + 1, "").toString();
        }
        return result;
    }
    /**
     * Method for Formatting the Amount like Indian Amount Format
     * @param String
     * @return String
     */
    public static String formatINRAmount (String amount) {
        
        StringBuffer sb = new StringBuffer(40);
        BigDecimal txnAmount = new BigDecimal(amount);
        String value = InstaDefaultConstants.APPLICATION_AMOUNT_FORMAT;
        if(value.equals(InstaDefaultConstants.KEY_AMT_FORMAT_WESTERN)) {
            
            return formatWesternAmount(txnAmount);
        } else if(value.equals(InstaDefaultConstants.KEY_AMT_FORMAT_INDIAN)) {
            DecimalFormat formatter = new DecimalFormat();
            formatter.applyPattern("#,##");
            if( txnAmount.compareTo( new BigDecimal("1000")) > 0) {
                BigDecimal[] b = txnAmount.divideAndRemainder(new BigDecimal("1000"));
                sb.append(formatter.format(b[0])).append(",");
                formatter.applyPattern("#,000.00");
                sb.append(formatter.format(b[1]));
            } else {
                formatter.applyPattern("#,##0.00");
                sb.append(formatter.format(txnAmount));
            }
            
        }
        return sb.toString();
    }
    
    /**
     * Method for Formatting the Amount like Indian Amount Format
     * @param BigDecimal
     * @return String
     */
    public static String formatINRAmount (BigDecimal txnAmount) {
        
        StringBuffer sb = new StringBuffer(40);
        DecimalFormat formatter = new DecimalFormat();
        String value = InstaDefaultConstants.APPLICATION_AMOUNT_FORMAT;
        if(value.equals(InstaDefaultConstants.KEY_AMT_FORMAT_WESTERN)) {
            return formatWesternAmount(txnAmount);
        } else if (value.equals(InstaDefaultConstants.KEY_AMT_FORMAT_INDIAN)) {
            
            formatter.applyPattern("#,##");
            if( txnAmount.compareTo( new BigDecimal("1000")) > 0) {
                BigDecimal[] b = txnAmount.divideAndRemainder(new BigDecimal("1000"));
                sb.append(formatter.format(b[0])).append(",");
                formatter.applyPattern("#,000.00");
                sb.append(formatter.format(b[1]));
            } else {
                formatter.applyPattern("#,##0.00");
                sb.append(formatter.format(txnAmount));
            }
        }
        return sb.toString();
    }
    
    /**
     * Method to Format Amount in Western Pattern
     * @param BigDecimal
     * @return String
     */
    public static String formatWesternAmount(String amount) {

        DecimalFormat westernFormat = new DecimalFormat("#,000.00");
        String result = westernFormat.format(amount);

        return result;
    }
    
    /**
     * Method to Format Amount in Western Pattern
     * @param BigDecimal
     * @return String
     */
    public static String formatWesternAmount(BigDecimal amount) {

        DecimalFormat westernFormat = new DecimalFormat("#,000.00");
        westernFormat.setMinimumFractionDigits(2);
        String result = westernFormat.format(amount);

        return result;
    }
    
    private static void log(Object o) {
        if (o instanceof Throwable) {
            ((Throwable)o).printStackTrace(System.err);
        } else {
            System.out.println(o + "");
        }
    }
}