/* $Header$ */

/*
 * @(#)RTGSBalTxnValueObject.java
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
package com.objectfrontier.rtgs.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import java.sql.Date;

/**
 * This class holds the serializable data related to RTGSBalTran table 
 * 
 * @author Madhu; Sep 15, 2004 
 * @date   Sep 15, 2004
 * @since  RHS IOB 1.0; Sep 15, 2004
 */
public class RTGSBalTxnValueObject  
implements Serializable {
    
    public int txnId;    

    public String utrNo;
    
    /**
     * posted date
     */
    public Date entDate;

    /**
     * Holds the concatenated String of msgType + "/" + msgSubType
     */
    public String msgType;
    
    /**
     * Transaction Type, either [I]nward or [O]utward
     */
    public String tranType;

//    /**
//     *   Transaction amount          
//     */
//    public double txnAmount;
    
    /**
     *   Transaction amount          
     */
    public BigDecimal txnAmount;
    
    /**
     * Balance in the bal master when the transaction is made
     */
    public double balance;
    
    /**
     * Holds the sender to receivers information 
     */
    public String particulars;
    
    /**
     * Implementation bank's branch code
     */
    public String ourBranch;
    
    /**
     * Participant branch code
     */
    public String otherBranch;
    
    /**
     * Participant bank code
     */
    public String otherBank;
    
    /**
     * Debit (D) / Credit (C) 
     */
    public char debitCredit;
    
    
    /**
     * Accessor method to get balance
     * @return Returns the balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Accessor method to get Debit/Credit
     * @return Returns the debitCredit
     */
    public char getDebitCredit() {
        return debitCredit;
    }

    /**
     * Accessor method to get EntryDate
     * @return Returns the endDate
     */
    public Date getEntDate() {
        return entDate;
    }

    /**
     * Accessor method to get MsgType
     * @return Returns the msgType
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * Accessor method to get OtherBank
     * @return Returns the otherBank
     */
    public String getOtherBank() {
        return otherBank;
    }

    /**
     * Accessor method to get OtherBank
     * @return Returns the otherBank
     */
    public String getOtherBranch() {
        return otherBranch;
    }

    /**
     * Accessor method to get OtherBank
     * @return Returns the otherBank
     */
    public String getOurBranch() {
        return ourBranch;
    }

    /**
     * Accessor method to get particulars
     * @return Returns the particulars
     */
    public String getParticulars() {
        return particulars;
    }

    /**
     * Accessor method to get tranType
     * @return Returns the tranType
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * Accessor method to get txnAmount
     * @return Returns the txnAmount
     */
    public BigDecimal getTxnAmount() {
        return txnAmount;
    }

    /**
     * Accessor method to get txnId
     * @return Returns the txnId
     */
    public int getTxnId() {
        return txnId;
    }

    /**
     * Accessor method to get UTRNo
     * @return Returns the utrno
     */
    public String getUtrNo() {
        return utrNo;
    }

    /**
     * Mutator method to set the value
     * @param d  - Balance
     */
    public void setBalance(double d) {
        balance = d;
    }

    /**
     * Mutator method to set the value
     * @param c - Debit/Credit
     */
    public void setDebitCredit(char c) {
        debitCredit = c;
    }

    /**
     * Mutator method to set the value
     * @param date - Entry Date
     */
    public void setEntDate(Date date) {
        entDate = date;
    }

    /**
     * Mutator method to set the value
     * @param string - Message Type
     */
    public void setMsgType(String string) {
        msgType = string;
    }

    /**
     * Mutator method to set the value
     * @param string - Other Bank
     */
    public void setOtherBank(String string) {
        otherBank = string;
    }

    /**
     * Mutator method to set the value
     * @param string - Other Branch
     */
    public void setOtherBranch(String string) {
        otherBranch = string;
    }

    /**
     * Mutator method to set the value
     * @param string - Our Branch
     */
    public void setOurBranch(String string) {
        ourBranch = string;
    }

    /**
     * Mutator method to set the value
     * @param string - Particulars
     */
    public void setParticulars(String string) {
        particulars = string;
    }

    /**
     * Mutator method to set the value
     * @param string - Transaction type
     */
    public void setTranType(String string) {
        tranType = string;
    }

    /**
     * Mutator method to set the value
     * @param d - Txn Amount
     */
    public void setTxnAmount(BigDecimal d) {
        txnAmount = d;
    }

    /**
     * Mutator method to set the value
     * @param i - Txn Id
     */
    public void setTxnId(int i) {
        txnId = i;
    }

    /**
     * Mutator method to set the value
     * @param string  - UTR No.
     */
    public void setUtrNo(String string) {
        utrNo = string;
    }

}
