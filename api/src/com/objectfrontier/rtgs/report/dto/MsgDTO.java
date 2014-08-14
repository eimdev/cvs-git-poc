/* $Header$ */

/*
 * @(#)TransactionDTO.java
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

import java.sql.Timestamp;

import com.objectfrontier.arch.server.dto.ClientInfo;

/**
 * @author Karthick GRP
 * @date   Dec 9, 2004
 * @since  RHS IOB 1.0; Dec 9, 2004
 */
public class MsgDTO 
implements ClientInfo {
    
    public String utrNo;
    
    public String otherBank;
    
    public String txnType;
    
    public AmountDTO amountDTO;
    
    public String status;
    
    public Timestamp entDate;
    
    public Timestamp valueDate;
    
    public String accNo;
    
    public String orderingCustomer;
    
    /**
     * @return
     */
    public AmountDTO getAmountDTO() {
        
        if (amountDTO == null)
            amountDTO = new AmountDTO();
        return amountDTO;
    }

    /**
     * @return
     */
    public Timestamp getEntDate() {
        return entDate;
    }

    /**
     * @return
     */
    public String getOtherBank() {
        return otherBank;
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
    public String getTxnType() {
        return txnType;
    }

    /**
     * @return
     */
    public String getUtrNo() {
        return utrNo;
    }

    /**
     * @param amountDTO
     */
    public void setAmountDTO(AmountDTO amountDTO) {
        this.amountDTO = amountDTO;
    }

    /**
     * @param date
     */
    public void setEntDate(Timestamp date) {
        entDate = date;
    }

    /**
     * @param string
     */
    public void setOtherBank(String string) {
        otherBank = string;
    }

    /**
     * @param string
     */
    public void setStatus(String string) {
        status = string;
    }

    /**
     * @param string
     */
    public void setTxnType(String string) {
        txnType = string;
    }

    /**
     * @param string
     */
    public void setUtrNo(String string) {
        utrNo = string;
    }

    /**
     * @return
     */
    public Timestamp getValueDate() {
        return valueDate;
    }

    /**
     * @param timestamp
     */
    public void setValueDate(Timestamp timestamp) {
        valueDate = timestamp;
    }

    /**
     * @return
     */
    public String getAccNo() {
        return accNo;
    }

    /**
     * @param string
     */
    public void setAccNo(String string) {
        accNo = string;
    }

    /**
     * @return
     */
    public String getOrderingCustomer() {
        return orderingCustomer;
    }

    /**
     * @param string
     */
    public void setOrderingCustomer(String string) {
        orderingCustomer = string;
    }
}
