/* $Header$ */

/*
 * @(#)SummaryInfo.java
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
package com.objectfrontier.neft.report.dto;

import java.util.List;

import com.objectfrontier.arch.server.dto.ClientInfo;

/**
 * Holds the Summary information
 * 
 * @author pRasanna
 * @date   Mar 13, 2006
 * @since  Fetrem 1.0; Mar 13, 2006
 */
public class SummaryInfo 
implements ClientInfo{

    public static class SummaryInfoElement 
    implements ClientInfo{
        
        public String heading;
        public int    count;
//        public double amount;
//        public BigDecimal amount;
        public String amount;
        public SummaryInfoElement(){}
//        public SummaryInfoElement(String n, int c, double a){
//            this.heading =n;
//            this.count = c;
//            this.amount = a;
//        }
        
        public SummaryInfoElement(String n, int c, String a){
            this.heading =n;
            this.count = c;
            this.amount = a;
        }
//        /**
//         * @return
//         */
//        public double getAmount() {
//            return amount;
//        }
        
        /**
         * @return
         */
        public String getAmount() {
            return amount;
        }

        /**
         * @return
         */
        public int getCount() {
            return count;
        }

        /**
         * @return
         */
        public String getHeading() {
            return heading;
        }

//        /**
//         * @param d
//         */
//        public void setAmount(double d) {
//            amount = d;
//        }
        
        /**
         * @param d
         */
        public void setAmount(String d) {
            amount = d;
        }

        /**
         * @param i
         */
        public void setCount(int i) {
            count = i;
        }

        /**
         * @param string
         */
        public void setHeading(String string) {
            heading = string;
        }

    }
    
    public List summaryElements;
    /**
     * @return
     */
    public List getSummaryElements() {
        return summaryElements;
    }

    /**
     * @param list
     */
    public void setSummaryElements(List list) {
        summaryElements = list;
    }

}
