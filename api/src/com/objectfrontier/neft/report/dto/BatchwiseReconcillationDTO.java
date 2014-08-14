/*
 * @(#)BatchwiseReconcillationDTO.java
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
package com.objectfrontier.neft.report.dto;

import java.io.Serializable;



/**
 * @author umeshs
 * @date   Jan 30, 2009
 * @since  insta.reports; Jan 30, 2009
 */
public class BatchwiseReconcillationDTO
implements Serializable {
    
//  This Holds the Batch Time
    private String batchTime;
    
    // This Holds the O/W debit Txn Accepted
    private long owTxnAccepted;
    
    // This Holds the O/W debit Txn Amt Accepted
    private String owTxnAmtAccepted;
    
    // This Holds the O/W debit Txn Rejected
    private long owTxnRejected;
    
    // This Holds the O/W debit Txn Amt Rejected
    private String owTxnAmtRejected;
    
    // This Holds the I/W Txn Received   
    private long iwTxnReceived;
    
    // This Holds the I/W Txn Amt Received
    private String iwTxnAmtReceived;
    
    // This Holds the I/W Txn Returned
    private long iwTxnReturned;
    
    // This Holds the I/W Txn Amt Returned
    private String iwTxnAmtReturned;

    
    public String getBatchTime() {
    
        return batchTime;
    }

    
    public void setBatchTime(String batchTime) {
    
        this.batchTime = batchTime;
    }

    
    public String getIwTxnAmtReceived() {
    
        return iwTxnAmtReceived;
    }

    
    public void setIwTxnAmtReceived(String iwTxnAmtReceived) {
    
        this.iwTxnAmtReceived = iwTxnAmtReceived;
    }

    
    public String getIwTxnAmtReturned() {
    
        return iwTxnAmtReturned;
    }

    
    public void setIwTxnAmtReturned(String iwTxnAmtReturned) {
    
        this.iwTxnAmtReturned = iwTxnAmtReturned;
    }

    
    public long getIwTxnReceived() {
    
        return iwTxnReceived;
    }

    
    public void setIwTxnReceived(long iwTxnReceived) {
    
        this.iwTxnReceived = iwTxnReceived;
    }

    
    public long getIwTxnReturned() {
    
        return iwTxnReturned;
    }

    
    public void setIwTxnReturned(long iwTxnReturned) {
    
        this.iwTxnReturned = iwTxnReturned;
    }

    
    public long getOwTxnAccepted() {
    
        return owTxnAccepted;
    }

    
    public void setOwTxnAccepted(long owTxnAccepted) {
    
        this.owTxnAccepted = owTxnAccepted;
    }

    
    public String getOwTxnAmtAccepted() {
    
        return owTxnAmtAccepted;
    }

    
    public void setOwTxnAmtAccepted(String owTxnAmtAccepted) {
    
        this.owTxnAmtAccepted = owTxnAmtAccepted;
    }

    
    public String getOwTxnAmtRejected() {
    
        return owTxnAmtRejected;
    }

    
    public void setOwTxnAmtRejected(String owTxnAmtRejected) {
    
        this.owTxnAmtRejected = owTxnAmtRejected;
    }

    
    public long getOwTxnRejected() {
    
        return owTxnRejected;
    }

    
    public void setOwTxnRejected(long owTxnRejected) {
    
        this.owTxnRejected = owTxnRejected;
    }
    
}
