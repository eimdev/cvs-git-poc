/* $Header$ */

/*
 * @(#)OTDetailReportDTO.java
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
import java.util.Map;

/**
 * @author pRasanna
 * @date   Mar 13, 2006
 * @since  Fetrem 1.0; Mar 13, 2006
 */
public class OTDetailReportDTO 
extends NEFTReportDTO {

    public static final String TOTAL_TRANACTIONS  = "Total Transaction";
    
    public static final String REPORT_TYPE_SUMMARY = "Summary";
    public static final String REPORT_TYPE_DETAIL = "Detailed";
    
    // For Heading in Outward Summary Reports
    public static final String HEAD_PS1  = "PS1 - Sent From Branch Received by RBI";
    public static final String HEAD_PRJ  = "PRJ - Sent From Branch Rejected by RBI";
    public static final String HEAD_PSF1 = "PSF1 - Sent From Branch Rescheduled by RBI";
    public static final String HEAD_PS2  = "PS2 - Sent From Sundry Received by RBI";
    public static final String HEAD_PSF2 = "PSF2 - Sent From Sundry Rejected by RBI";
    public static final String HEADING = " NEFT Outward Txn Details ";
    public static final String SUMMARY_INFO = "Summary";
    public static final String TO = "TO";
    
    public String bankCode;
    public String bankName;
    public String batchTime;
    public String status;
    public String branchIfsc;
    public String reportDate;
    
    public List   transactionInfo;
    public SummaryInfo summaryInfo;
    
    // Map that holds all type of transaction lists
    public Map outwardMap;
    
    /**
     * @return
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @return
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @return
     */
    public String getBatchTime() {
        return batchTime;
    }

    /**
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return
     */
    public SummaryInfo getSummaryInfo() {
        return summaryInfo;
    }

    /**
     * @return
     */
    public List getTransactionInfo() {
        return transactionInfo;
    }

    /**
     * @param string
     */
    public void setBankCode(String string) {
        bankCode = string;
    }

    /**
     * @param string
     */
    public void setBankName(String string) {
        bankName = string;
    }

    /**
     * @param string
     */
    public void setBatchTime(String string) {
        batchTime = string;
    }

    /**
     * @param string
     */
    public void setStatus(String string) {
        status = string;
    }

    /**
     * @param info
     */
    public void setSummaryInfo(SummaryInfo info) {
        summaryInfo = info;
    }

    /**
     * @param list
     */
    public void setTransactionInfo(List list) {
        transactionInfo = list;
    }

    
    public String getBranchIfsc() {
    
        return branchIfsc;
}
    
    public void setBranchIfsc(String branchIfsc) {
    
        this.branchIfsc = branchIfsc;
    }

    
    public Map getOutwardMap() {
    
        return outwardMap;
    }

    
    public void setOutwardMap(Map outwardMap) {
    
        this.outwardMap = outwardMap;
    }

    
    public String getReportDate() {
    
        return reportDate;
    }

    
    public void setReportDate(String reportDate) {
    
        this.reportDate = reportDate;
    }

}
