/*
 * @(#)DateUtil.java
 *
 * Copyright by ObjectFrontier Software Private Limited (OFS)
 * www.objectfrontier.com
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of OFS. You shall not disclose such confidential
 * information and shall use it only in accordance with the terms of
 * the license agreement you entered into with OFS.
 */
package com.objectfrontier.insta.reports.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;



/**
 * @author mohanr
 * @date   Nov 12, 2008
 * @since  BoR Insta App 2.0; Nov 12, 2008
 */
public class DateUtil {
    
    static Logger logger = Logger.getLogger(DateUtil.class);
    
    /**
     * To convert the given String from format (dd-MMM-yyyy) to String Format (dd-MM-yyyy)
     */
    public static String convert(String inputDate) {

        if (inputDate == null) return null;
        String[] getMonth = {"jan", "feb", "mar", "apr", "may", "jun", "jul",
                        "aug", "sep", "oct", "nov", "dec"};
        Map<String, Integer> month = new HashMap<String, Integer>();

        for (int i = 0; i < getMonth.length; i++) {
            month.put(getMonth[i], i);
        }
        String dd = inputDate.substring(0, 2);
        String mon = inputDate.substring(3, 6);
        String yr = inputDate.substring(7, 11);

        String returnDate = dd + "-";
        Integer intDate = month.get(mon.toLowerCase()) + 1;
        returnDate += (intDate < 10) ? "0" + intDate : intDate;
        return returnDate += "-" + yr;
    }
    
    /**
     * To convert the given String from format (dd-MMM-yyyy) to Date Format (dd-MM-yy)
     */
    public static Date StringToDate(String inputDate) {

        
        if (inputDate==null) return null;
        String []getMonth = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
        Map<String, Integer> month = new HashMap<String, Integer>();
        
        for(int i=0; i<getMonth.length-1;i++)
            month.put(getMonth[i], i);
        
        String dd  = inputDate.substring(0,2);
        String mon = inputDate.substring(3,6);
        String yr  = inputDate.substring(7,11);
        
        String returnDate = dd+"-"+month.get(mon.toLowerCase())+"-"+yr;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try{
            date = sdf.parse(returnDate);
        }catch(ParseException pe){
            
            logger.error("Unable to parse the String to Date");
            throw new RuntimeException("Unable to parse the String to Date " + pe.getMessage());
        }
        return date;
    }
    

}
