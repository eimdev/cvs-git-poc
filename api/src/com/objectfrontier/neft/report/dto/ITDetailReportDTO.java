/* $Header$ */

/*
 * @(#)ITDetailReportDTO.java
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
public class ITDetailReportDTO 
extends NEFTReportDTO {


    public String batchTime;
    public String status;
    public List transactionInfo;
    public SummaryInfo summaryInfo;
    public Map receivedTransactionInfo;

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

    
    public Map getReceivedTransactionInfo() {
    
        return receivedTransactionInfo;
    }

    
    public void setReceivedTransactionInfo(Map receivedTransactionInfo) {
    
        this.receivedTransactionInfo = receivedTransactionInfo;
    }

}
