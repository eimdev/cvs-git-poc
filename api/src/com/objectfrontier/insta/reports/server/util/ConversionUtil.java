/*
 * @(#)ConversionUtil.java
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

package com.objectfrontier.insta.reports.server.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.StringTokenizer;

import com.objectfrontier.insta.reports.InstaReportUtil;
import com.objectfrontier.insta.workflow.util.NumberToTextConverter;

/**
 * @author johne;Nov 12, 2004
 * @date   Nov 12, 2004
 * @since  RHS App 1.0; Nov 12, 2004
 * @see    
 */

public class ConversionUtil {
    

    protected static String NullString = "";
    
    protected static String KEY_INTERVAL_TIME = "LISTENER_INTERVAL_TIME";
    
    protected static SimpleDateFormat userDateFmt =
        new SimpleDateFormat("dd-MM-yyyy");

    protected static SimpleDateFormat rtgsUserDateFmt =
        new SimpleDateFormat("yyyyMMdd");

    //RBC
    protected static SimpleDateFormat borDateFmt =
        new SimpleDateFormat("dd-MM-yyyy-hh_mm_ss");

    protected static DecimalFormat doubleStringFormat = new DecimalFormat("0.00");

    //RBC CMD 1.0
    protected static SimpleDateFormat isoDateFmt =
        new SimpleDateFormat("yyyyMMddhhmmss");


    protected static SimpleDateFormat onlyHHMM =
        new SimpleDateFormat("HHmm");

//    protected static DecimalFormat deciFormat = new DecimalFormat("#.##");

    protected static DecimalFormat deciFormat = new DecimalFormat("#.00");
    
	public ConversionUtil(){
        
    }
    
    /**
     * This method returns the string
     * 
     * @param java.util.Date date
     * @return String in dd-MMM-yy format
     * 
     * For converting Timestamp object into a String
     */
    public static String convertToString(java.util.Date date) {

        java.text.SimpleDateFormat out = new java.text.SimpleDateFormat("dd-MMM-yy");
        return out.format(date);
       
    }
    
    public String getFormat(String inputDate) {
        
        String formatedDate = "";
        String[] months = {"","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        String date  = inputDate.substring(0,2);
        String month = inputDate.substring(3,5);
        int monthh   = new Integer(month);
        String year  = inputDate.substring(6,10);
        
        formatedDate = date + "-" + months[monthh] + "-" + year;
        
        return formatedDate;
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
     * This method convert Object to long
     */   
    public static long convertObjectToLong(Map rtgsConfigInfo) {
   
        String s =  rtgsConfigInfo.get(KEY_INTERVAL_TIME).toString();
        return Long.parseLong(s);
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
     * Method to append the current time into a date
     * 
     * @param java.util.Date date
     *  
     * @return Timestamp - holds the date with current time
     */
    public static java.sql.Date appendToDate(java.util.Date date) {
    
//    	Calendar gc = new GregorianCalendar();
//    	long todaysMills = gc.get(Calendar.MILLISECOND) + gc.get(Calendar.SECOND)*1000 + gc.get(Calendar.MINUTE) * 60 * 1000 + gc.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000 ;
//      Timestamp tmpTimestamp = new Timestamp(date.getTime() + todaysMills );
//      return new java.sql.Date(tmpTimestamp.getTime());
        Timestamp tmpTimestamp = new Timestamp(date.getTime());
        return new java.sql.Date(tmpTimestamp.getTime());
    }
    
    public static Timestamp appendCurrentTimeToDate(java.util.Date date) {
    
      Calendar gc = new GregorianCalendar();
      long todaysMills = gc.get(Calendar.MILLISECOND) + gc.get(Calendar.SECOND)*1000 + gc.get(Calendar.MINUTE) * 60 * 1000 + gc.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000 ;
      Timestamp tmpTimestamp = new Timestamp(date.getTime() + todaysMills );
      return tmpTimestamp;
   }
   
    /**
     * This method will take userID in IOB format ie. IFSC + userID, 
     * returns original UserID  
     * 
     */
    public static String getUserId(String iobUserId) {
       
        if (iobUserId != null && iobUserId.trim().length() > 11) 
            return (iobUserId.substring(11));
        return iobUserId; 
    }
   
    public static Date roundDate(Date date) {

        try {
            date = new Date(date.getYear(), date.getMonth(), date.getDate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }                   
        return date;        
    }
    
    /**
     * Format amount from ##.00 to Rs ####,##,###.00 and in words
     */
    public static String formatAmount(String amount) {

        if (amount == null || amount.trim().length() == 0 ) return "";
        
        String formatedAmount = FormatAmount.formatINRAmount(Double.parseDouble(amount)); 
   
        String amountWords = NumberToTextConverter.convertIntoRupeeString(amount); 
        String displayAmount = formatedAmount + "  - ( Rupees " + amountWords + ")";
        return displayAmount;
    }
    
    public static int getRTGSDate(String date) {
        
           try {
               java.sql.Date dat = ConversionUtil.getDate(date);
               return getRTGSDate(dat);
           } catch (Exception e) {
               throw new RuntimeException(e);
           }                   
       }
        
    public static int getRTGSDate(java.sql.Date date) {
        
           String formatDate = "";
           try {
               int year    = date.getYear() + 1900;
               int month   = date.getMonth() + 1;
               int day     = date.getDate();
        
               String mZero = (month <= 9) ? "0" : "";
               String dZero = (day <= 9) ? "0" : "";

               formatDate = String.valueOf(year) + mZero + String.valueOf(month) 
                                   + dZero + String.valueOf(day);
           } catch (Exception e) {
               throw new RuntimeException(e);
           }                   
           return Integer.parseInt(formatDate); 
       }

       public static String getRTGSDate(int date) {
        
           String formatDate = "";
           try {
               String sDate = String.valueOf(date);
               int year    = Integer.parseInt(sDate.substring(0, 4));
               int month   = Integer.parseInt(sDate.substring(4, 6));
               int day     = Integer.parseInt(sDate.substring(6, 8));

               Date dDate = new Date(year-1900, month-1, day);
               
               formatDate = ConversionUtil.formatDate(dDate);        
           } catch (Exception e) {
               throw new RuntimeException(e);
           }                   
           return formatDate; 
       }

    public static Date getDateFromRTGSDateString(String iobDate) {

        Date date = null;
        try {
            int year    = Integer.parseInt(iobDate.substring(0, 4));
            int month   = Integer.parseInt(iobDate.substring(4, 6));
            int day     = Integer.parseInt(iobDate.substring(6, 8));
            date = new Date(year-1900, month-1, day);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }                   
        return date;        
    }
    
    public static Date getDateFromISODateString(String isoDate) {
        
        String year = getCurrentYear();
        
        return getDateFromRTGSDateString(year + isoDate);
    }
    
    /**
     * This method should return the current year 
     *  
     * @return string
     */
    public static String getCurrentYear() {
        
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        
        java.text.SimpleDateFormat out = new java.text.SimpleDateFormat("yyyymmdd");
        
        return out.format(date).substring(0, 4);
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

    public static String getRTGSEntDate(String date) {
       String formatDate = "";
       try {
           int year    = Integer.parseInt(date.substring(0, 4));
           int month   = Integer.parseInt(date.substring(5, 7));
           int day     = Integer.parseInt(date.substring(8, 10));

           Date dDate = new Date(year-1900, month-1, day);
   
           formatDate = ConversionUtil.formatDate(dDate);        
       } catch (Exception e) {
           throw new RuntimeException(e);
       }                   
        return formatDate;   
    }

    //RBC
    /**
     * This method is used to Convert Timestamp 
     * to UserDefined Format
     * @param timeStamp
     * @return String
     */
    public static String convertTS2String(Timestamp timeStamp) {
        
        return borDateFmt.format(timeStamp);
    }
    
    /**
     * 
     * @param time
     * @return
     */
    public static Timestamp convertTime2TS(long time){
        
        return new Timestamp(time);
    }

    /**
     * 
     * @param time
     * @return
     */
    public static String convertTime2String(long time){
        
        return convertTS2String(new Timestamp(time));
    }

    /**
     * This method is used to format the value to 2 decimal places
     * if val is 800.0 then this method will return 800.00
     * if val is 800.136 then this method will return 800.14
     * @param val
     * @return
     */    
    public static String formatDoube(double val){
            return doubleStringFormat.format(String.valueOf(val));
    }

    /**
     * This method converts a JDBC format date into a <code>String</code>
     *
     * @param   date JDBC format date 
     * @param   formatString format e.g. "dd-MMM-yyyy"
     * @return  date in specified format
     */
    public static String formatDate(java.util.Date date, String format) {
    
        if (date == null) return null;
            SimpleDateFormat userDateFmt = null;
            try {
                userDateFmt = new SimpleDateFormat(format);
            } catch (Exception e) {
                userDateFmt = new SimpleDateFormat("dd-MMM-yyyy");
                // TODO: handle exception
            }
            return userDateFmt.format(date);
       }
       
    
    /**
     * RBC CMD 1.0
     * This method will return the date  if x no. of days is 
     * added to the passed date
     * passed date 
     * @param date
     * @param days
     * @return
     */
    public static java.sql.Date manipulateDate(java.sql.Date date, int days) {

        int yr = date.getYear();
        int month = date.getMonth();
        int day = date.getDate();
          
        GregorianCalendar passedDt = new GregorianCalendar(1900+yr, month, day);
        passedDt.add(GregorianCalendar.DATE, days);
        return new java.sql.Date(passedDt.getTimeInMillis());
    
    }
       
    /**
     * RBC CMD 1.0
     * This method is used to Convert Timestamp 
     * to ISO Date & time Format(YYYYDDMMhhmmss)
     * @param timeStamp
     * @return String
     */
    public static String convertTS2ISOFormat(Timestamp timeStamp) {
        
        return isoDateFmt.format(timeStamp);
    }

    /**
     * RBC CMD 1.0
     * This method is used to Convert Timestamp
     * to HHMM(HoursMinutes)
     * @param timeStamp
     * @return
     */
    public static String convertTS2HHMM(Timestamp timeStamp) {
        
        return onlyHHMM.format(timeStamp);
    }



    /**
     * RBC
     * This method is used to format
     * double value to String
     * @param val
     * @return
     */
    public static String formatDouble2String(double val) {
        
        return deciFormat.format(val);
    }
    
    /**
     * This method converts date provided as a String into JDBC format.
     *
     * @param   date date in "dd-MON-yyyy" format
     *
     * @return  JDBC formatted instance of the day
     *
     * throws   ParseException will be thrown if the date format of the is invalid
     */
     public static java.sql.Date getFormatedDate(String date)
     throws Exception {
        
         try {
            if ((date == null) || (date.trim().length() == 0)) return null;
        
              return new java.sql.Date(userDateFmt.parse(InstaReportUtil.reportDisplayDateFormat(date)).getTime());
            } catch (ParseException e) {
                    //RBC    
                    try{
                        if ((date == null) || (date.trim().length() == 0)) return null;
                        return new java.sql.Date(rtgsUserDateFmt.parse(InstaReportUtil.reportDisplayDateFormat(date)).getTime());
                    }catch(ParseException pe) {
                        throw new Exception("Invalid Date Format(Expected:DD-MM-YYYY or yyyyMMdd). Check Date, Month & Year.");
                    }
             }
        }
        
    public static void main(String args[]){
        try {
//         System.out.println(getFormatedDate("21-JAN_2009"));
         System.out.println(formatDouble(123456789.89));
        }catch(Exception e){
            log(e);
        }
    }
    
    private static void log(Object o) {

        if (o instanceof Throwable) {
            ((Throwable)o).printStackTrace(System.err);
        } else {
            System.out.println(o + "");
        }
    }
    
    /**
     * Method for adding trailing zeroes for a BigDecimal value
     * @param BigDecimal
     * @return String 
     */
    public static String formatBigDecimal(BigDecimal d){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        return df.format(d);
    }
    
}
