/* $Header$ */

/*
 * @(#)ReconcileReportDTO.java
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

import java.math.BigDecimal;
import java.util.List;

/**
 * This class contains the information which holds the
 * total outward amount, total inward amount , total 
 * amount rescheduled from previous day, total amount 
 * rescheduled for the next batch , the net outward amount,
 * net inward amount and  the aggregate amout  for a given
 * day.
 * 
 * @author SelvaganapathyT
 * @date   Apr 26, 2006
 * @since  Insta NEFT 1.0; Apr 26, 2006
 */
public class ReconcileReportDTO
extends ReportDTO {

    public static final String TOTAL_AMOUNT = "Total"; 

    /*
     * This variable holds the respective batchTime for which 
     * all the transctions has been done
     */
    public String batchTime;
    
    /*
     * This variable holds the total outward amount 
     * that has been sent for a particular batch.
     * 
     * grossOutwardAmount = NormalOutwardAmount +
     *                      rescheduledPreviousBatchAmount + 
     *                      RescheduledNextBatchAmount +
     *                      RejectedAmount
     */
//    public double grossOutwardAmount;
    public BigDecimal grossOutwardAmount;
    
    /*
     * This variable holds the amount
     * that is rescheduled from the previous batch
     * by the SFMS Server
     */
    public BigDecimal rescheduledPrevBatchAmt;
//    public double rescheduledPrevBatchAmt;
    
    /*
     * This variable holds the amount
     * that is rescheduled to the next batch
     * by the SFMS Server
     */
//    public double rescheduledNextBatchAmt;
    public BigDecimal rescheduledNextBatchAmt;
    
    /*
     * This variable holds the amount 
     * that is rejected by SFMS Sever for 
     * the current batch
     * 
     */
//    public double rejectedAmt;
    public BigDecimal rejectedAmt;
    
    /*
     * This variable holds the netOutward amount.
     * NetOutwardAmount is calculated as follows.
     * 
     * NetOutwardAmount = grossOutwardAmount + 
     *                    rescheduledPrevBatchAmt - 
     *                    rescheduledNextBatchAmt 
     */
//    public double netOutwardAmt;
    public BigDecimal netOutwardAmt;
    
    /*
     * This variable holds the total inward amount 
     * that is received for the particular batch
     * from SFMS Server 
     */
    public BigDecimal netInwardAmt;
//    public double netInwardAmt;
    
    /*
     * This variable holds the aggerate amount for the 
     * particular batch.
     * 
     * aggregateAmt is calculated as follows
     * 
     * aggregateAmt = netOutwardAmt - netInwardAmt;
     */
//    public double aggregateAmt;
    public BigDecimal aggregateAmt;
    
    public List ReconcileReportDTOs;
    
    public ConsolidatedReconcileReportDTO consolidatedReportDTO;
    
    public String heading;

//    /**
//     * @return
//     */
//    public double getGrossOutwardAmount() {
//        return grossOutwardAmount;
//    }
//
//    /**
//     * @return
//     */
//    public double getNetInwardAmt() {
//        return netInwardAmt;
//    }
//
//    /**
//     * @return
//     */
//    public double getNetOutwardAmt() {
//        return netOutwardAmt;
//    }
    
    /**
     * @return
     */
    public BigDecimal getGrossOutwardAmount() {
        return grossOutwardAmount;
    }

    /**
     * @return
     */
    public BigDecimal getNetInwardAmt() {
        return netInwardAmt;
    }

    /**
     * @return
     */
    public BigDecimal getNetOutwardAmt() {
        return netOutwardAmt;
    }

    /**
     * @return
     */
    public List getReconcileReportDTOs() {
        return ReconcileReportDTOs;
    }

//    /**
//     * @return
//     */
//    public double getRejectedAmt() {
//        return rejectedAmt;
//    }
//
//    /**
//     * @return
//     */
//    public double getRescheduledNextBatchAmt() {
//        return rescheduledNextBatchAmt;
//    }
//
//    /**
//     * @return
//     */
//    public double getRescheduledPrevBatchAmt() {
//        return rescheduledPrevBatchAmt;
//    }
    
    /**
     * @return
     */
    public BigDecimal getRejectedAmt() {
        return rejectedAmt;
    }

    /**
     * @return
     */
    public BigDecimal getRescheduledNextBatchAmt() {
        return rescheduledNextBatchAmt;
    }

    /**
     * @return
     */
    public BigDecimal getRescheduledPrevBatchAmt() {
        return rescheduledPrevBatchAmt;
    }
    
    

//    /**
//     * @param d
//     */
//    public void setGrossOutwardAmount(double d) {
//        grossOutwardAmount = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setNetInwardAmt(double d) {
//        netInwardAmt = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setNetOutwardAmt(double d) {
//        netOutwardAmt = d;
//    }
    
    /**
     * @param d
     */
    public void setGrossOutwardAmount(BigDecimal d) {
        grossOutwardAmount = d;
    }

    /**
     * @param d
     */
    public void setNetInwardAmt(BigDecimal d) {
        netInwardAmt = d;
    }

    /**
     * @param d
     */
    public void setNetOutwardAmt(BigDecimal d) {
        netOutwardAmt = d;
    }

//    /**
//     * @param list
//     */
//    public void setReconcileReportDTOs(List list) {
//        ReconcileReportDTOs = list;
//    }
//
//    /**
//     * @param d
//     */
//    public void setRejectedAmt(double d) {
//        rejectedAmt = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setRescheduledNextBatchAmt(double d) {
//        rescheduledNextBatchAmt = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setRescheduledPrevBatchAmt(double d) {
//        rescheduledPrevBatchAmt = d;
//    }
//
//    /**
//     * @return
//     */
//    public double getAggregateAmt() {
//        return aggregateAmt;
//    }
    
    /**
     * @param list
     */
    public void setReconcileReportDTOs(List list) {
        ReconcileReportDTOs = list;
    }

    /**
     * @param d
     */
    public void setRejectedAmt(BigDecimal d) {
        rejectedAmt = d;
    }

    /**
     * @param d
     */
    public void setRescheduledNextBatchAmt(BigDecimal d) {
        rescheduledNextBatchAmt = d;
    }

    /**
     * @param d
     */
    public void setRescheduledPrevBatchAmt(BigDecimal d) {
        rescheduledPrevBatchAmt = d;
    }

    /**
     * @return
     */
    public BigDecimal getAggregateAmt() {
        return aggregateAmt;
    }

    /**
     * @return
     */
    public String getBatchTime() {
        return batchTime;
    }

    /**
     * @param d
     */
    public void setAggregateAmt(BigDecimal d) {
        aggregateAmt = d;
    }

    /**
     * @param string
     */
    public void setBatchTime(String string) {
        batchTime = string;
    }

    /**
     * @return
     */
    public ConsolidatedReconcileReportDTO getConsolidatedReportDTO() {
        return consolidatedReportDTO;
    }

    /**
     * @param reportDTO
     */
    public void setConsolidatedReportDTO(ConsolidatedReconcileReportDTO reportDTO) {
        consolidatedReportDTO = reportDTO;
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

}
