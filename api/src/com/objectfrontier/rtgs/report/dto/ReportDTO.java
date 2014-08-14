/* $Header$ */

/*
 * @(#)ReportDTO.java
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Karthick GRP
 * @date   Dec 9, 2004
 * @since  RHS IOB 1.0; Dec 9, 2004
 */
public class ReportDTO implements Serializable{
    
    private Collection userIds;
    public String branchName;
    public String branchAddress;
    public String branchCode;
    public Timestamp fromDate;
    public Timestamp toDate;
    public String txnStatus;
    public String reportType;
//    public long fromAmount;
//    public long toAmount;
    
    public BigDecimal fromAmount;
    public BigDecimal toAmount;
//    public BranchwiseDTO branchwiseDTO;
    public List branchwiseDTOs;
    public List typewiseDTOs;
    public AmountDTO branchesGrandTotalDTO;
    public MsgDTO msgDTO;
    public String userId;
    public String bank;
    public String msgSubType;
    public String allBranches;

    public String solId; //For RBC, Filtering by SOL(Service Out Line)
    public String tranType;
    public String utrNumber; //RBC CMD 1.0
    public String branchId;//RBC CMD 1.0
    public int txnCount;
    public String ifsc;
    public Timestamp valueDate;
    public Map dateWiseMap;
    public Map ifscMap;
//    public double dateCprInAmt = 0.0;
//    public double dateCprOutAmt = 0.0;
//    public double dateIprInAmt = 0.0;
//    public double dateIprOutAmt = 0.0;
//    public double dateGrantTolal = 0.0;
//    public double amount = 0.0;
    public BigDecimal dateCprInAmt = BigDecimal.ZERO;
    public BigDecimal dateCprOutAmt = BigDecimal.ZERO;
    public BigDecimal dateIprInAmt = BigDecimal.ZERO;
    public BigDecimal dateIprOutAmt = BigDecimal.ZERO;
    public BigDecimal dateGrantTolal = BigDecimal.ZERO;
    public BigDecimal amount = BigDecimal.ZERO;
    
    public List ifscList;
    
    public String accNo;
    public String remitterName;
    public String beneficiaryName;
    public String utrNo;
    
//    public double creditSettlement;
//    public double debitSettlement;
    public BigDecimal creditSettlement;
    public BigDecimal debitSettlement;
    public String fieldA5561; 
    public String fieldN5561;
    public String fieldA7495;
    public String fieldI7495;
    /**
     * @return
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * @return
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param string
     */
    public void setBranchCode(String string) {
        branchCode = string;
    }

    /**
     * @param string
     */
    public void setBranchName(String string) {
        branchName = string;
    }

    /**
     * @return
     */
    public String getReportType() {
        return reportType;
    }

    /**
     * @return
     */
    public String getTxnStatus() {
        return txnStatus;
    }

    /**
     * @param string
     */
    public void setReportType(String string) {
        reportType = string;
    }

    /**
     * @param string
     */
    public void setTxnStatus(String string) {
        txnStatus = string;
    }

    /**
     * @return
     */
    public Timestamp getFromDate() {
        return fromDate;
    }

    /**
     * @return
     */
    public Timestamp getToDate() {
        return toDate;
    }

    /**
     * @param timestamp
     */
    public void setFromDate(Timestamp timestamp) {
        fromDate = timestamp;
    }

    /**
     * @param timestamp
     */
    public void setToDate(Timestamp timestamp) {
        toDate = timestamp;
    }

    /**
     * @return
     */
    public List getBranchwiseDTOs() {
        
        if (branchwiseDTOs == null)
            branchwiseDTOs =  new ArrayList(0);
        return branchwiseDTOs;
    }

    /**
     * @param list
     */
    public void setBranchwiseDTOs(List list) {
        branchwiseDTOs = list;
    }

    /**
     * @return
     */
    public List getTypewiseDTOs() {
        
        if (typewiseDTOs == null)
            typewiseDTOs = new ArrayList(0);
        return typewiseDTOs;
    }

    /**
     * @param list
     */
    public void setTypewiseDTOs(List list) {
        typewiseDTOs = list;
    }

    /**
     * Collect the all the Branches Grand Total
     * and returns AmountDTO, which has all the In amout and Out amount
     * 
     * @return AmountDTO
     */
    public AmountDTO getBranchesGrandTotalDTO() {
        if (branchesGrandTotalDTO == null) {
            branchesGrandTotalDTO = new AmountDTO();
        }
        return branchesGrandTotalDTO;
    }


    /**
     * Set the In amount and Out amount of all the Branches Grand Total
     * 
     * @param amountDTO
     */
    public void setBranchesGrandTotalDTO(AmountDTO amountDTO) {
        branchesGrandTotalDTO = amountDTO;
    }

//    public long getFromAmount() {
//        return fromAmount;
//    }
//
//    public long getToAmount() {
//        return toAmount;
//    }
//
//    public void setFromAmount(long l) {
//        fromAmount = l;
//    }
//
//    public void setToAmount(long l) {
//        toAmount = l;
//    }
    
    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setFromAmount(BigDecimal l) {
        fromAmount = l;
    }

    public void setToAmount(BigDecimal l) {
        toAmount = l;
    }

    public Collection getUserIds() {
        return userIds;
    }

    public void setUserIds(Collection collection) {
        userIds = collection;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String string) {
        userId = string;
    }
   
    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String string) {
        branchAddress = string;
    }

    /**
     * @return
     */
    public String getBank() {
        return bank;
    }

    /**
     * @param string
     */
    public void setBank(String string) {
        bank = string;
    }

    /**
     * @return
     */
    public String getMsgSubType() {
        return msgSubType;
    }

    /**
     * @param string
     */
    public void setMsgSubType(String string) {
        msgSubType = string;
    }

    /**
     * @return
     */
    public String getAllBranches() {
        return allBranches;
    }

    /**
     * @param string
     */
    public void setAllBranches(String string) {
        allBranches = string;
    }

    public String getSolId() {
        return solId;
    }

    public String getTranType() {
        return tranType;
    }

    public void setSolId(String string) {
        solId = string;
    }

    public void setTranType(String string) {
        tranType = string;
    }

    /**
     * @return
     */
    public String getUtrNumber() {//RBC CMD 1.0
        return utrNumber;
    }

    /**
     * @param string
     */
    public void setUtrNumber(String string) {//RBC CMD 1.0
        utrNumber = string;
    }

    /**
     * @return
     */
    public String getBranchId() {//RBC CMD 1.0
        return branchId;
    }

    /**
     * @param string
     */
    public void setBranchId(String string) {//RBC CMD 1.0
        branchId = string;
    }

    /**
     * @return
     */
    public MsgDTO getMsgDTO() {
       if (msgDTO == null) {
               msgDTO = new MsgDTO();
           }
           return msgDTO;
       }

    /**
     * @param msgDTO
     */
    public void setMsgDTO(MsgDTO msgDTO) {
        this.msgDTO = msgDTO;
    }

    /**
     * @return
     */
    public int getTxnCount() {
        return txnCount;
    }

    /**
     * @param i
     */
    public void setTxnCount(int i) {
        txnCount = i;
    }

    /**
     * @return
     */
    public Map getDateWiseMap() {
        return dateWiseMap;
    }

    /**
     * @param map
     */
    public void setDateWiseMap(Map map) {
        dateWiseMap = map;
    }

    /**
     * @return
     */
    public String getIfsc() {
        return ifsc;
    }

    /**
     * @param string
     */
    public void setIfsc(String string) {
        ifsc = string;
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
    public Map getIfscMap() {
        return ifscMap;
    }

    /**
     * @param map
     */
    public void setIfscMap(Map map) {
        ifscMap = map;
    }

//    /**
//     * @return
//     */
//    public double getDateCprInAmt() {
//        return dateCprInAmt;
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
//     * @return
//     */
//    public double getDateGrantTolal() {
//        return dateGrantTolal;
//    }
//
//    /**
//     * @return
//     */
//    public double getDateIprInAmt() {
//        return dateIprInAmt;
//    }
//
//    /**
//     * @return
//     */
//    public double getDateIprOutAmt() {
//        return dateIprOutAmt;
//    }
//
//    /**
//     * @param d
//     */
//    public void setDateCprInAmt(double d) {
//        dateCprInAmt = d;
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
//     * @param d
//     */
//    public void setDateGrantTolal(double d) {
//        dateGrantTolal = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setDateIprInAmt(double d) {
//        dateIprInAmt = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setDateIprOutAmt(double d) {
//        dateIprOutAmt = d;
//    }
    /**
     * @return
     */
    public BigDecimal getDateCprInAmt() {
        return dateCprInAmt;
    }

    /**
     * @return
     */
    public BigDecimal getDateCprOutAmt() {
        return dateCprOutAmt;
    }

    /**
     * @return
     */
    public BigDecimal getDateGrantTolal() {
        return dateGrantTolal;
    }

    /**
     * @return
     */
    public BigDecimal getDateIprInAmt() {
        return dateIprInAmt;
    }

    /**
     * @return
     */
    public BigDecimal getDateIprOutAmt() {
        return dateIprOutAmt;
    }

    /**
     * @param d
     */
    public void setDateCprInAmt(BigDecimal d) {
        dateCprInAmt = d;
    }

    /**
     * @param d
     */
    public void setDateCprOutAmt(BigDecimal d) {
        dateCprOutAmt = d;
    }

    /**
     * @param d
     */
    public void setDateGrantTolal(BigDecimal d) {
        dateGrantTolal = d;
    }

    /**
     * @param d
     */
    public void setDateIprInAmt(BigDecimal d) {
        dateIprInAmt = d;
    }

    /**
     * @param d
     */
    public void setDateIprOutAmt(BigDecimal d) {
        dateIprOutAmt = d;
    }
    /**
     * @return
     */
    public List getIfscList() {
        return ifscList;
    }

    /**
     * @param list
     */
    public void setIfscList(List list) {
        ifscList = list;
    }

    /**
     * @return
     */
    public String getUtrNo() {
        return utrNo;
    }

    /**
     * @param string
     */
    public void setUtrNo(String string) {
        utrNo = string;
    }

//    /**
//     * @return
//     */
//    public double getAmount() {
//        return amount;
//    }
//
//    /**
//     * @param d
//     */
//    public void setAmount(double d) {
//        amount = d;
//    }
    
    /**
     * @return
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param d
     */
    public void setAmount(BigDecimal d) {
        amount = d;
    }

    /**
     * @return
     */
    public String getAccNo() {
        return accNo;
    }

    /**
     * @return
     */
    public String getRemitterName() {
        return remitterName;
    }

    /**
     * @param string
     */
    public void setAccNo(String string) {
        accNo = string;
    }

    /**
     * @param string
     */
    public void setRemitterName(String string) {
        remitterName = string;
    }

    /**
     * @return
     */
    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    /**
     * @param string
     */
    public void setBeneficiaryName(String string) {
        beneficiaryName = string;
    }

//    /**
//     * @return
//     */
//    public double getCreditSettlement() {
//        return creditSettlement;
//    }
//
//    /**
//     * @return
//     */
//    public double getDebitSettlement() {
//        return debitSettlement;
//    }
//
//    /**
//     * @param d
//     */
//    public void setCreditSettlement(double d) {
//        creditSettlement = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setDebitSettlement(double d) {
//        debitSettlement = d;
//    }
    
    /**
     * @return
     */
    public BigDecimal getCreditSettlement() {
        return creditSettlement;
    }

    /**
     * @return
     */
    public BigDecimal getDebitSettlement() {
        return debitSettlement;
    }

    /**
     * @param d
     */
    public void setCreditSettlement(BigDecimal d) {
        creditSettlement = d;
    }

    /**
     * @param d
     */
    public void setDebitSettlement(BigDecimal d) {
        debitSettlement = d;
    }

    
    public String getFieldA5561() {
    
        return fieldA5561;
    }

    
    public void setFieldA5561(String fieldA5561) {
    
        this.fieldA5561 = fieldA5561;
    }

    
    public String getFieldA7495() {
    
        return fieldA7495;
    }

    
    public void setFieldA7495(String fieldA7495) {
    
        this.fieldA7495 = fieldA7495;
    }

    
    public String getFieldI7495() {
    
        return fieldI7495;
    }

    
    public void setFieldI7495(String fieldI7495) {
    
        this.fieldI7495 = fieldI7495;
    }

    
    public String getFieldN5561() {
    
        return fieldN5561;
    }

    
    public void setFieldN5561(String fieldN5561) {
    
        this.fieldN5561 = fieldN5561;
    }

}
