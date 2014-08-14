/*
 * @(#)BatchwiseAggregateDTO.java
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
 * @date   Jan 29, 2009
 * @since  insta.reports; Jan 29, 2009
 */
public class BatchwiseAggregateDTO
implements Serializable {
    
    //Holds the IFSC Code
    private String ifsc;
    
    //Hold total no. of Credits(i.e outward)
    private long noOfCredits;
    
    //  Hold total of Credits Amount(i.e outward)
    private String creditAmount;
    
    //  Hold total no. of debits(i.e inward)
    private long noOfDebits;
    
    //  Hold total of debits Amount(i.e inward)
    private String debitAmount;
    
    // For Aggreagate Summary Report
    private String batchTime;
    private String aggregateAmount;
    
    public String getCreditAmount() {
        
        if(creditAmount == null || "".equals(creditAmount)){
            creditAmount = "0.00";
        }
        return creditAmount;
    }

    
    public void setCreditAmount(String creditAmount) {
    
        this.creditAmount = creditAmount;
    }

    
    public String getDebitAmount() {
    
        if(debitAmount == null || "".equals(debitAmount)){
            debitAmount = "0.00";
        }
        return debitAmount;
    }

    
    public void setDebitAmount(String debitAmount) {
    
        this.debitAmount = debitAmount;
    }

    
    public String getIfsc() {
    
        return ifsc;
    }

    
    public void setIfsc(String ifsc) {
    
        this.ifsc = ifsc;
    }

    
    public long getNoOfCredits() {
    
        return noOfCredits;
    }

    
    public void setNoOfCredits(long noOfCredits) {
    
        this.noOfCredits = noOfCredits;
    }

    
    public long getNoOfDebits() {
    
        return noOfDebits;
    }

    
    public void setNoOfDebits(long noOfDebits) {
    
        this.noOfDebits = noOfDebits;
    }


    
    public String getAggregateAmount() {
        
        if(aggregateAmount == null || "".equals(aggregateAmount)){
            aggregateAmount = "0.00";
        }
        return aggregateAmount;
    }


    
    public void setAggregateAmount(String aggregateAmount) {
    
        this.aggregateAmount = aggregateAmount;
    }


    
    public String getBatchTime() {
    
        return batchTime;
    }


    
    public void setBatchTime(String batchTime) {
    
        this.batchTime = batchTime;
    }
    
}
