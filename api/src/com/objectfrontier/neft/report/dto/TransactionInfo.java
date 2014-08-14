/* $Header$ */

/*
 * @(#)TransactionInfo.java
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

import java.math.BigDecimal;
import java.util.Date;

import com.objectfrontier.arch.server.dto.ClientInfo;


/**
 * TransactionInfo object to hold the details of transactions for reporting
 * 
 * @author johne
 * @date   Feb 27, 2006
 * @since  neft 1.0; Feb 27, 2006
 */
public class TransactionInfo
implements ClientInfo {

    public String tranRefNo;
//    public double amount;
    public BigDecimal amount;
    public Date valueDate;
    public String batchTime;
    
    public CustomerInfo senderInfo;
    public CustomerInfo beneficiaryInfo;
    public String currentStatus;
    public String statusShortDesc;
    public String batchTimeAll;
    
    public Date txnDate;
    public Date rescheduleDate;
    public String rescheduleBatch; 
    public Date batchDate;
    public String flag;
    public String msgType;
    public String remarks;
    
    //  for displaying the Status of the Message
    public String messageStatus;
    
    // holds the result rescheduled Amount
//    public double rescheduleAmount;    
    public BigDecimal rescheduleAmount;
       
    /*
     * This variable holds the information about 
     * the UTR number
     */
    public String utrNo;
  
    /*
     * This variable holds the information about
     * the date of when the reversal record for the particular txn
     * created
     */
    public Date contraDate;
    /*
     * This variable holds the information about the
     * current transaction balance
     * 
     * if the transaction is credit then
     * balance = prevbalance + creditAmount
     * 
     * else if transaction is debit then
     * balance = prevbalance - debitAmount
     * 
     */ 
//    public double balance;
    public BigDecimal balance;

    /*
     * This Variable holds the information about the TransactionType 
     * Holds Value 'C' - for credit Transaction
     *       Value 'D' - for debit Transaction.
     */
    public String txnType;

    /*
     * This Variable is for Distinct information for Credit Transaction Amount
     */
//    public double creditAmount;
    public BigDecimal creditAmount;
    /*
     * This Variable is for Distinct information for Debit Transaction Amount
     */
//    public double debitAmount;
    public BigDecimal debitAmount;

    /*
     * This Variable is for Total Credit Transaction Amount
     */
//    public double totalCreditAmount;
    public BigDecimal totalCreditAmount;
    /*
     * This Variable is for Total Debit Transaction Amount
     */
//    public double totalDebitAmount;
    public BigDecimal totalDebitAmount;
    
    /*
     * This variable is used to indicate the Sundry transaction as 
     * Reversal or Original Txn.
     */
    public String orgOrRevTransaction;  
  
//    /**
//     * @return
//     */
//    public double getAmount() {
//        return amount;
//    }
    /**
     * @return
     */
    public BigDecimal getAmount() {
        return amount;
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
    public CustomerInfo getBeneficiaryInfo() {
        return beneficiaryInfo;
    }

    /**
     * @return
     */
    public CustomerInfo getSenderInfo() {
        return senderInfo;
    }

    /**
     * @return
     */
    public String getTranRefNo() {
        return tranRefNo;
    }

    /**
     * @return
     */
    public Date getValueDate() {
        return valueDate;
    }

//    /**
//     * @param d
//     */
//    public void setAmount(double d) {
//        amount = d;
//    }
    
    /**
     * @param d
     */
    public void setAmount(BigDecimal d) {
        amount = d;
    }

    /**
     * @param string
     */
    public void setBatchTime(String string) {
        batchTime = string;
    }

    /**
     * @param info
     */
    public void setBeneficiaryInfo(CustomerInfo info) {
        beneficiaryInfo = info;
    }

    /**
     * @param info
     */
    public void setSenderInfo(CustomerInfo info) {
        senderInfo = info;
    }

    /**
     * @param string
     */
    public void setTranRefNo(String string) {
        tranRefNo = string;
    }

    /**
     * @param date
     */
    public void setValueDate(Date date) {
        valueDate = date;
    }

    /**
     * @return
     */
    public String getCurrentStatus() {
        return currentStatus;
    }

    /**
     * @param string
     */
    public void setCurrentStatus(String string) {
        currentStatus = string;
    }

    /**
     * @return
     */
    public String getStatusShortDesc() {
        return statusShortDesc;
    }

    /**
     * @param string
     */
    public void setStatusShortDesc(String string) {
        statusShortDesc = string;
    }   
      
    /**
     * @param string
     */
    public void setBatchTimeAll(String string) {
        batchTimeAll = string;
    }

    /**
     * @return
     */
    public String getBatchTimeAll() {
        return batchTimeAll;
    }

    /**
     * @return
     */
    public Date getBatchDate() {
        return batchDate;
    }

    /**
     * @return
     */
    public String getRescheduleBatch() {
        return rescheduleBatch;
    }

    /**
     * @return
     */
    public Date getRescheduleDate() {
        return rescheduleDate;
    }

    /**
     * @return
     */
    public Date getTxnDate() {
        return txnDate;
    }

    /**
     * @param date
     */
    public void setBatchDate(Date date) {
        batchDate = date;
    }

    /**
     * @param string
     */
    public void setRescheduleBatch(String string) {
        rescheduleBatch = string;
    }

    /**
     * @param date
     */
    public void setRescheduleDate(Date date) {
        rescheduleDate = date;
    }

    /**
     * @param date
     */
    public void setTxnDate(Date date) {
        txnDate = date;
    }

//    /**
//     * @return
//     */
//    public double getBalance() {
//        return balance;
//    }
    
    /**
     * @return
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @return
     */
    public Date getContraDate() {
        return contraDate;
    }

//    /**
//     * @return
//     */
//    public double getCreditAmount() {
//        return creditAmount;
//    }
    
    /**
     * @return
     */
    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

//    /**
//     * @return
//     */
//    public double getDebitAmount() {
//        return debitAmount;
//    }
    
    /**
     * @return
     */
    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

//    /**
//     * @return
//     */
//    public String getMessageStatus() {
//        return messageStatus;
//    }
//
//    /**
//     * @return
//     */
//    public double getRescheduleAmount() {
//        return rescheduleAmount;
//    }
//
//    /**
//     * @return
//     */
//    public double getTotalCreditAmount() {
//        return totalCreditAmount;
//    }

    /**
     * @return
     */
    public BigDecimal getTotalDebitAmount() {
        return totalDebitAmount;
    }
    
    /**
     * @return
     */
    public BigDecimal getRescheduleAmount() {
        return rescheduleAmount;
    }

    /**
     * @return
     */
    public BigDecimal getTotalCreditAmount() {
        return totalCreditAmount;
    }

//    /**
//     * @return
//     */
//    public double getTotalDebitAmount() {
//        return totalDebitAmount;
//    }

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

//    /**
//     * @param d
//     */
//    public void setBalance(double d) {
//        balance = d;
//    }
    
    /**
     * @param d
     */
    public void setBalance(BigDecimal d) {
        balance = d;
    }

    /**
     * @param date
     */
    public void setContraDate(Date date) {
        contraDate = date;
    }

//    /**
//     * @param d
//     */
//    public void setCreditAmount(double d) {
//        creditAmount = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setDebitAmount(double d) {
//        debitAmount = d;
//    }
    /**
     * @param d
     */
    public void setCreditAmount(BigDecimal d) {
        creditAmount = d;
    }

    /**
     * @param d
     */
    public void setDebitAmount(BigDecimal d) {
        debitAmount = d;
    }

    /**
     * @param string
     */
    public void setMessageStatus(String string) {
        messageStatus = string;
    }

//    /**
//     * @param d
//     */
//    public void setRescheduleAmount(double d) {
//        rescheduleAmount = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setTotalCreditAmount(double d) {
//        totalCreditAmount = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setTotalDebitAmount(double d) {
//        totalDebitAmount = d;
//    }
    
    /**
     * @param d
     */
    public void setRescheduleAmount(BigDecimal d) {
        rescheduleAmount = d;
    }

    /**
     * @param d
     */
    public void setTotalCreditAmount(BigDecimal d) {
        totalCreditAmount = d;
    }

    /**
     * @param d
     */
    public void setTotalDebitAmount(BigDecimal d) {
        totalDebitAmount = d;
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
    public String getOrgOrRevTransaction() {
        return orgOrRevTransaction;
    }

    /**
     * @param string
     */
    public void setOrgOrRevTransaction(String string) {
        orgOrRevTransaction = string;
    }
    
    public String getFlag() {
    
        return flag;
    }

    
    public void setFlag(String flag) {
    
        this.flag = flag;
    }

    
    public String getMsgType() {
    
        return msgType;
    }

    
    public void setMsgType(String msgType) {
    
        this.msgType = msgType;
    }

    
    public String getRemarks() {
    
        return remarks;
    }

    
    public void setRemarks(String remarks) {
    
        this.remarks = remarks;
    }

}
