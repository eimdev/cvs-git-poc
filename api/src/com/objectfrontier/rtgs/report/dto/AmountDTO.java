/* $Header$ */

/*
 * @(#)TotalAmountDTO.java
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
package com.objectfrontier.rtgs.report.dto;

import java.math.BigDecimal;

import com.objectfrontier.arch.server.dto.ClientInfo;

/**
 * @author Karthick GRP
 * @date   Dec 9, 2004
 * @since  RHS IOB 1.0; Dec 9, 2004
 */
public class AmountDTO 
implements ClientInfo {
    
    public BigDecimal inAmount = BigDecimal.ZERO;
    public BigDecimal outAmount = BigDecimal.ZERO;
    public BigDecimal netAmount = BigDecimal.ZERO;
    public BigDecimal cprOutAmount = BigDecimal.ZERO;
    public BigDecimal iprOutAmount = BigDecimal.ZERO;
    public BigDecimal grandTotal = BigDecimal.ZERO;
    public BigDecimal cprInAmount = BigDecimal.ZERO;
    public BigDecimal iprInAmount = BigDecimal.ZERO;
    public BigDecimal grantTotalInward = BigDecimal.ZERO;
    
    public BigDecimal dateCprInAmt = BigDecimal.ZERO;
    public BigDecimal dateCprOutAmt = BigDecimal.ZERO;
    public BigDecimal dateIprInAmt = BigDecimal.ZERO;
    public BigDecimal dateIprOutAmt = BigDecimal.ZERO;
    public BigDecimal dateGrantTolal = BigDecimal.ZERO;
    /**
     * @return
     */
    public BigDecimal getInAmount() {
        return inAmount;
    }

    /**
     * @return
     */
    public BigDecimal getOutAmount() {
        return outAmount;
    }

    /**
     * @param d
     */
    public void setInAmount(BigDecimal d) {
        inAmount = d;
    }

    /**
     * @param d
     */
    public void setOutAmount(BigDecimal d) {
        outAmount = d;
    }

    /**
     * @return
     */
    public BigDecimal getNetAmount() {
        return netAmount;
    }

    /**
     * @param d
     */
    public void setNetAmount(BigDecimal d) {
        netAmount = d;
    }

    /**
     * @return
     */
    public BigDecimal getIprOutAmount() {
        return iprOutAmount;
    }

 
    /**
     * @param d
     */
    public void setIprOutAmount(BigDecimal d) {
        iprOutAmount = d;
    }

    /**
     * @return
     */
    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    /**
     * @param d
     */
    public void setGrandTotal(BigDecimal d) {
        grandTotal = d;
    }

    /**
     * @return
     */
    public BigDecimal getCprInAmount() {
        return cprInAmount;
    }

    /**
     * @return
     */
    public BigDecimal getIprInAmount() {
        return iprInAmount;
    }

    /**
     * @param d
     */
    public void setCprInAmount(BigDecimal d) {
        cprInAmount = d;
    }

    /**
     * @param d
     */
    public void setIprInAmount(BigDecimal d) {
        iprInAmount = d;
    }

    /**
     * @return
     */
    public BigDecimal getGrantTotalInward() {
        return grantTotalInward;
    }

    /**
     * @param d
     */
    public void setGrantTotalInward(BigDecimal d) {
        grantTotalInward = d;
    }

    /**
     * @return
     */
    public BigDecimal getDateCprOutAmt() {
        return dateCprOutAmt;
    }

    /**
     * @param d
     */
    public void setDateCprOutAmt(BigDecimal d) {
        dateCprOutAmt = d;
    }

    /**
     * @return
     */
    public BigDecimal getCprOutAmount() {
        return cprOutAmount;
    }

    /**
     * @param d
     */
    public void setCprOutAmount(BigDecimal d) {
        cprOutAmount = d;
    }

    /**
     * @return
     */
    public BigDecimal getDateGrantTolal() {
        return dateGrantTolal;
    }

    /**
     * @param d
     */
    public void setDateGrantTolal(BigDecimal d) {
        dateGrantTolal = d;
    }

//    public double inAmount = 0.0;
//    public double outAmount = 0.0;
//    public double netAmount = 0.0;
//    public double cprOutAmount = 0.0;
//    public double iprOutAmount = 0.0;
//    public double grandTotal = 0.0;
//    public double cprInAmount = 0.0;
//    public double iprInAmount = 0.0;
//    public double grantTotalInward = 0.0;
//    
//    public double dateCprInAmt = 0.0;
//    public double dateCprOutAmt = 0.0;
//    public double dateIprInAmt = 0.0;
//    public double dateIprOutAmt = 0.0;
//    public double dateGrantTolal = 0.0;
//    /**
//     * @return
//     */
//    public double getInAmount() {
//        return inAmount;
//    }
//
//    /**
//     * @return
//     */
//    public double getOutAmount() {
//        return outAmount;
//    }
//
//    /**
//     * @param d
//     */
//    public void setInAmount(double d) {
//        inAmount = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setOutAmount(double d) {
//        outAmount = d;
//    }
//
//    /**
//     * @return
//     */
//    public double getNetAmount() {
//        return netAmount;
//    }
//
//    /**
//     * @param d
//     */
//    public void setNetAmount(double d) {
//        netAmount = d;
//    }
//
//    /**
//     * @return
//     */
//    public double getIprOutAmount() {
//        return iprOutAmount;
//    }
//
// 
//    /**
//     * @param d
//     */
//    public void setIprOutAmount(double d) {
//        iprOutAmount = d;
//    }
//
//    /**
//     * @return
//     */
//    public double getGrandTotal() {
//        return grandTotal;
//    }
//
//    /**
//     * @param d
//     */
//    public void setGrandTotal(double d) {
//        grandTotal = d;
//    }
//
//    /**
//     * @return
//     */
//    public double getCprInAmount() {
//        return cprInAmount;
//    }
//
//    /**
//     * @return
//     */
//    public double getIprInAmount() {
//        return iprInAmount;
//    }
//
//    /**
//     * @param d
//     */
//    public void setCprInAmount(double d) {
//        cprInAmount = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setIprInAmount(double d) {
//        iprInAmount = d;
//    }
//
//    /**
//     * @return
//     */
//    public double getGrantTotalInward() {
//        return grantTotalInward;
//    }
//
//    /**
//     * @param d
//     */
//    public void setGrantTotalInward(double d) {
//        grantTotalInward = d;
//    }
//
//    /**
//     * @return
//     */
//    public double getDateCprOutAmt() {
//        return dateCprOutAmt;
//    }
//
//    /**
//     * @param d
//     */
//    public void setDateCprOutAmt(double d) {
//        dateCprOutAmt = d;
//    }
//
//    /**
//     * @return
//     */
//    public double getCprOutAmount() {
//        return cprOutAmount;
//    }
//
//    /**
//     * @param d
//     */
//    public void setCprOutAmount(double d) {
//        cprOutAmount = d;
//    }
//
//    /**
//     * @return
//     */
//    public double getDateGrantTolal() {
//        return dateGrantTolal;
//    }
//
//    /**
//     * @param d
//     */
//    public void setDateGrantTolal(double d) {
//        dateGrantTolal = d;
//    }

}
