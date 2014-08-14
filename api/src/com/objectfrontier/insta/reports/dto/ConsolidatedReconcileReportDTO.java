/* $Header$ */

/*
 * @(#)ConsolidatedReportDTO.java
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
package com.objectfrontier.insta.reports.dto;

import java.util.List;

/**
 * This class contains the information which
 * provides the consolidated transaction details
 * for the particular day.  
 * 
 * @author SelvaganapathyT
 * @date   Apr 26, 2006
 * @since  Insta NEFT 1.0; Apr 26, 2006
 */
public class ConsolidatedReconcileReportDTO
extends ReportDTO {

    public static final String TOTAL_INWARD_AMOUNT  = "Total Inward";
    public static final String TOTAL_OUTWARD_AMOUNT  = "Less Outward";
    public static final String NET_AMOUNT  = "Net";

    /*
     * This variable holds the clientBank's total account balance 
     * amount in the RBI for both Inward and Outward transactions 
     * in clientBank.
     *  
     * RBIAccountAmt is calculated as follows.
     * 
     * for Inward  :
     *     RBIAccountAmt = net Inward Amount
     * 
     * for Outward : 
     *     RBIAccountAmt = gross outward amount + 
     *                     rescheduled previous batch amount  for the current days first batch -
     *                     rescheduled next batch amount for the current days last batch -
     *                     current days rejection amount   
     *                                   
     */
//    public double RBIAccountAmt;
    public String RBIAccountAmt;
    
    /*
     * This variable holds the clientBank's NEFT account balance 
     * amount in the RBI for both Inward and Outward transactions 
     * in clientBank.
     * 
     * NEFTAccountAmt is calculated as follows
     * 
     * for Inward  :
     *     NEFTAccountAmt = net Inward Amount + rejected amount for the current day
     * 
     * for Outward :
     *     NEFTAccountAmt = gross Outward Amount
     */
//    public double NEFTAccountAmt;
    public String NEFTAccountAmt;
    
    public List consolidatedReportDTOs;

    public String heading;

//    /**
//     * @return
//     */
//    public double getNEFTAccountAmt() {
//        return NEFTAccountAmt;
//    }
//
//    /**
//     * @return
//     */
//    public double getRBIAccountAmt() {
//        return RBIAccountAmt;
//    }
    
    /**
     * @return
     */
    public String getNEFTAccountAmt() {
        return NEFTAccountAmt;
    }

    /**
     * @return
     */
    public String getRBIAccountAmt() {
        return RBIAccountAmt;
    }

//    /**
//     * @param d
//     */
//    public void setNEFTAccountAmt(double d) {
//        NEFTAccountAmt = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setRBIAccountAmt(double d) {
//        RBIAccountAmt = d;
//    }
    
    /**
     * @param d
     */
    public void setNEFTAccountAmt(String d) {
        NEFTAccountAmt = d;
    }

    /**
     * @param d
     */
    public void setRBIAccountAmt(String d) {
        RBIAccountAmt = d;
    }

    /**
     * @return
     */
    public String getHeading() {
        return heading;
    }

    /**
     * @param string
     */
    public void setHeading(String string) {
        heading = string;
    }

    /**
     * @return
     */
    public List getConsolidatedReportDTOs() {
        return consolidatedReportDTOs;
    }

    /**
     * @param list
     */
    public void setConsolidatedReportDTOs(List list) {
        consolidatedReportDTOs = list;
    }

}
