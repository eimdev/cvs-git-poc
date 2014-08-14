/*
 * @(#)ConversionUtils.java
 *
 * Copyright by ObjectFrontier, Inc.,
 * 12225 Broadleaf Lane, Alpharetta, GA 30005, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ObjectFrontier, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with ObjectFrontier.
 */
package com.objectfrontier.rtgs.client.jsp.util;

import java.lang.reflect.Method;

import java.sql.Timestamp;

import java.util.StringTokenizer;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Karthick GRP;Jul 22, 2004
 * @author NSenthil Kumar
 * @date   Jul 22, 2004
 * @since  rtgsapp; Jul 22, 2004
 */
public class ConversionUtils {
    
    protected static SimpleDateFormat userDateFmt =
        new SimpleDateFormat("dd-MM-yyyy");
    
    protected static SimpleDateFormat userTimeFmt =
        new SimpleDateFormat("dd-MM-yyyy'-'HH:mm");
    
    protected static SimpleDateFormat userTimeOnlyFmt =
        new SimpleDateFormat("HH:mm");
        
    protected static SimpleDateFormat rtgsUserDateFmt =
            new SimpleDateFormat("yyyyMMdd");    
    
    /**
     * This method converts date provided as a String into JDBC format.
     *
     * @param   date date in "dd-MM-yyyy" format
     *
     * @return  JDBC formatted instance of the day
     *
     * throws   ParseException will be thrown if the date format of the is invalid
     */
    public static java.sql.Date getDate(String date)
    throws Exception {
        
        try {
            if ((date == null) || (date.trim().length() == 0)) return null;
        
            return new java.sql.Date(userDateFmt.parse(date).getTime());
        } catch (ParseException e) {
            //RBC    
            try{
                if ((date == null) || (date.trim().length() == 0)) return null;
                return new java.sql.Date(rtgsUserDateFmt.parse(date).getTime());
            }catch(ParseException pe) {
                throw new Exception("Invalid Date Format(Expected:DD-MM-YYYY or yyyyMMdd). Check Date, Month & Year.");
            }
        }
    }
    
    /**
     * This method converts time provided as a String into JDBC format.
     *
     * @param   time time in "dd-MMM-yyyy-HH:mm" format
     * @param   onlyTime controls whether date will be included or not
     * @return  JDBC formatted instance of the time
     *
     * throws   ParseException will be thrown if the date format of the is invalid
     */
    public static Timestamp getTime(String time, boolean onlyTime)
    throws ParseException {
        
        if ((time == null) || (time.trim().length() == 0)) return null;

        return (onlyTime)
               ? new Timestamp(userTimeFmt.parse(ConversionUtils.formatDate(new java.util.Date()) +
                                                 "-" + time).getTime())
               : new Timestamp(userTimeFmt.parse(time).getTime());
    }
    
    /**
     * This method provide the current date and current time 
     * 
     * @param 
     * @return TimeStamp
     */

    public static String getCurrentDateTime()
        throws ParseException {
            java.util.Date today = new java.util.Date();
            String currentDateTime = userTimeFmt.format(today);
            return  currentDateTime;
    }
        
    /**
     * This method converts a JDBC format date into a <code>String</code>
     *
     * @param   date JDBC format date
     *
     * @return  date in "dd-MMM-yyyy" format
     */
    public static String formatDate(java.util.Date date) {
        
        if (date == null) return null;

        return userDateFmt.format(date);
    }
    
    /**
     * This method converts a JDBC format date into a <code>String</code>
     *
     * @param   date JDBC format date
     * @param   dateFormat 
     *
     * @return  date in user defined format
     */
    public static String formatDate(java.util.Date date, String simpleDateFormat) {
        
        if (date == null) return null;
        
        return new SimpleDateFormat(simpleDateFormat).format(date);
    }
    
    /**
     * This method converts a JDBC format date into a <code>String</code>
     *
     * @param   date JDBC format time
     * @param   onlyTime controls whether date will be included or not
     *
     * @return  date in "dd-MMM-yyyy" format
     */
    public static String formatDate(Timestamp time) {
        
        if (time == null) return null;

        return userDateFmt.format(time);
    }
    
    /**
     * This method converts a JDBC format date into a <code>String</code>
     *
     * @param   date JDBC format date
     *
     * @return  date in "yyyyMMdd" format
     */
    public static String formatRTGSDate(java.util.Date date) {
        
        if (date == null) return null;

        return rtgsUserDateFmt.format(date);
    }

    /**
     * This method converts a JDBC format date into a <code>String</code>
     *
     * @param   date JDBC format time
     * @param   onlyTime controls whether date will be included or not
     *
     * @return  date in "dd-MMM-yyyy-HH:mm" or "HH:mm" format
     */
    public static String formatTime(Timestamp time, boolean onlyTime) {
        
        if (time == null) return null;

        return (onlyTime) ? userTimeOnlyFmt.format(time)
                          : userTimeFmt.format(time);
    }
    
    /**
     * This method gets the method name as string and converts
     * it into an object and invokes it and finally returns
     * the result in the form of string.
     *
     * @param   object
     * @param   methodName
     * @return
     */
    public static String convert(Object object, String methodName) {
        
        if (object == null) return null;

        try {
            Class clazz   = object.getClass();
            Method method = clazz.getMethod(methodName, new Class[0]);
            Object result = method.invoke(object, new Object[0]);

            return result.toString();
        } catch (Throwable throwable) {
            return null;
        }
    }
    
    /**
     * This method takes double value as a parameter and rounds it to
     * two decimal points and returns the rounded value.
     *
     * @param   value value to be rounded
     * @return  double roundedvalue
     */
    public static double roundDouble(double value) {
        
        int decimalPlace    = 2;
        double power_of_ten = 1;

        while (decimalPlace-- > 0) {
            power_of_ten *= 10.0;
        }

        return Math.round(value * power_of_ten) / power_of_ten;
    }
    
    /**
     * This method takes double value as a parameter and formats it to
     * two decimal points and returns the formatted value.
     *
     * @param   value value to be formatted
     * @return  double formattedvalue
     */
    public static String formatDouble(double value) {
        
        double valueHundred = roundDouble(value) * 100;
        Double temp         = new Double(roundDouble(valueHundred));
        String cost         = String.valueOf(temp.longValue());

        if (cost.equals("0")) {
            cost = "0.00";
        } else {
            cost =
                cost.substring(0, cost.length() - 2) + "." +
                cost.substring(cost.length() - 2);
        }

        return cost;
    }
    
    /**
     * This method parses the string based on the delimiter
     * and returns the token array
     *
     * @param   fieldName
     * @param   delimiter
     * @return  String[] tokens
     */
    public static String[] parseString(String fieldName, String delimiter) {
        
        StringTokenizer tokenizer = new StringTokenizer(fieldName, delimiter);
        String[] tokens           = new String[tokenizer.countTokens()];

        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokenizer.nextToken();
        }

        return tokens;
    }
    
    /**
     * This method transforms the given input string into browser understandable format
     *
     * @param   preValue
     * @param   searchString
     * @param   replaceString
     * @return  String
     */
    public static String transformString(String preValue, String searchString, String replaceString) {
        
        String postValue = "";
        if (preValue == null) return postValue;        
        int position = 0;
        
        if(preValue.indexOf(searchString, position) == -1) return preValue; 

        while(preValue.indexOf(searchString, position) > 0){

            postValue += preValue.substring(position, preValue.indexOf(searchString, position));
            postValue += replaceString;
            position = preValue.indexOf(searchString, position)+ searchString.length();
        }
        postValue += preValue.substring(position);

        return postValue;
    }

    /**
     * This method parses the string passed based on the delimiter
     * and returns in the form of long array
     *
     * @param   fieldName
     * @param   delimiter
     * @return  long[]
     */
    public static long[] parseLong(String fieldName, String delimiter) {
        
        String[] tokens = parseString(fieldName, delimiter);
        long[] values   = new long[tokens.length];

        for (int i = 0; i < tokens.length; i++) {
            values[i] = Long.valueOf(tokens[i]).longValue();
        }

        return values;
    }
    
    /**
     * This method removes the multiple sequential blank spaces in the
     * String parameter passed to it and returns the trimmed string.
     *
     * @param   name
     * @return  String trimmedString
     */
    public static String removeBlankSpaces(String name) {
        
        String returnString = "";

        if (name != null) {
            String trimmedName = name.trim();

            if (trimmedName.length() > 0) {
                for (int i = 0; i < (trimmedName.length() - 1); i++) {
                    if ((trimmedName.charAt(i) == ' ') &&
                            (trimmedName.charAt(i + 1) == ' ')) {
                        continue;
                    } 
                    returnString += trimmedName.charAt(i);
                }
                returnString += trimmedName.charAt(trimmedName.length() - 1);
            }
        }

        return returnString;
    }

    /**
     * This method returns the root cause of the exception
     *
     * @param exception
     * @return
     */
    public static String getRootExceptionMessage(Throwable exception) {
        
        Throwable th = exception.getCause();

        if (th == null) return (exception.getMessage() == null) ? exception.toString() : exception.getMessage();

        return getRootExceptionMessage(th);
    }
    
    public static String display() {
    	return "Test";
    }
}
