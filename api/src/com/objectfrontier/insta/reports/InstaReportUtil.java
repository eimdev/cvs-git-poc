package com.objectfrontier.insta.reports;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author jinuj
 * @date   Jul 29, 2008
 * @since  insta.reports; Jul 29, 2008
 */
public class InstaReportUtil {

    /**
     * To convert the given Date to String Format (dd-MM-yyyy)
     */
    public static String formatDate(Date input) {

        if (input==null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return  sdf.format(input);
    }

    /**
     * To convert the given Date to String Format.
     */
    public static String formatDate(Date input, String format) throws Exception {

        if (input==null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return  sdf.format(input);
    }


    /**
     * To convert the given String from format (dd-MM-yyyy) to String Format (dd-MMM-yyyy)
     */
    public static String formatDateString(String inputDate) {

        if (inputDate==null) return null;
        String []getMonth = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String dd  = inputDate.substring(0,2);
        String mon = inputDate.substring(3,5);
        String yr  = inputDate.substring(6,10);
        int mm =Integer.parseInt(mon);
        String returnDate = dd+"-"+getMonth[mm-1]+"-"+yr; 
        return returnDate;
    }

    /**
     * To convert the given String from format (dd-MMM-yyyy) to String Format (dd-MM-yyyy)
     */
    public static String reportDisplayDateFormat(String inputDate) {

        if (inputDate==null) return null;

        String []months = {"Jan","Feb","Mar","Apr","May","Jun",
                            "Jul","Aug","Sep","Oct","Nov","Dec"};
        String dd  = inputDate.substring(0,2);
        String mon = inputDate.substring(3,6);
        String yr  = inputDate.substring(7,11);

        for (int ind=0; ind<months.length; ind++){

            if (months[ind].equalsIgnoreCase(mon)) {

                if (ind>=9) {
                    mon = (ind+1) + "";
                } else {
                    mon= "0" + (ind+1) + "";
                }
                break;
            }
        }
        String returnDate = dd+"-"+mon+"-"+yr; 
        return returnDate.toUpperCase();
    }

    /**
     * To convert the given Date to String Format (dd-MMM-yyyy)
     */
    public static String indianFormatDate(Date input) {

        if (input==null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return  sdf.format(input);
    }

    /**
     * Convert To date from String format dd-MMM-yyyy
     */
    public static Date convertToDate(String date) throws Exception {

        if (date==null || date.trim().length()==0) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return  sdf.parse(date);
    }

    /**
     * For Testing
     */
    public static void main(String [] args){
        System.out.println(formatDateString("07-04-1948"));
    }
    

    /**
     * To convert date from yyyy-mm-dd format to dd-mm-yyyy format
     * And this method added for changing transactioninfo date into fomat.
     * @author Eswaripriyak
     */
    public static String convertFormat(String date) throws Exception {

        if (date==null || date.trim().length()==0) return null;
        
        String day = date.substring(8);
        String month = date.substring(5,7);
        String year = date.substring(0,4);
        String inputDate = day + "-" + month + "-"+ year;
        
        return inputDate;
    }
  public static String getDateInSpecificFormat(String format,String input) {
        
        if (format.equalsIgnoreCase("dd-mmm-yy")) {
            
            String str =input.substring(0,input.lastIndexOf("-")+1);
            String str1 =input.substring(input.lastIndexOf("-")+3);
            input = str+str1;
        } else if (format.equalsIgnoreCase("dd-mm-yyyy")) {
            input = reportDisplayDateFormat(input);
        } else if (format.equalsIgnoreCase("mm/dd/yy")) {
            input = getDateFormat(input);
        } else if (format.equalsIgnoreCase("mm/dd/yyyy")) {
            input = getFormatedDate(input);
        } else if (format.equalsIgnoreCase("")) {
            return input;
        }
        return input;
    }
  
    public static String getDateFormat(String inputDate) {
      
        String formatDate = reportDisplayDateFormat(inputDate);
        String dd  = formatDate.substring(0,2);
        String mon = formatDate.substring(3,5);
        String yr  = formatDate.substring(8,10);
        String retDate = mon+"/"+dd+"/"+yr;
        return retDate;
    }
  
    public static String getFormatedDate(String inputDate) {
      
        String formatDate = reportDisplayDateFormat(inputDate);
        String dd  = formatDate.substring(0,2);
        String mon = formatDate.substring(3,5);
        String yr  = formatDate.substring(6,10);
        String retDate = mon+"/"+dd+"/"+yr;
        return retDate;
    }
    
    public static String convertToAppFormat(java.util.Date date) {

        java.text.SimpleDateFormat out = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        return out.format(date);
       
    }
}
