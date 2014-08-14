/*
 * @(#)DetailedMsgDTO.java
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

import java.util.ArrayList;
import java.util.List;

import com.objectfrontier.arch.server.dto.ClientInfo;

/**
 * @author Bakiyaraj A
 * @date   Feb 9, 2004
 * @since  RHS IOB 1.0; Feb 9, 2004
 * @since  Insta App 1.0; Jun 6, 2005
 */
public class DetailedMsgDTO 
implements ClientInfo {
    
    public String msgType;
    public String msgSubType;
    public String creditTxnId;
    public String lotId;
    public String typeName;
    public String tranType;
    public String branchName;
    public String entDate;
    public String utrNumber;   
    public String txnStatus;
        
    public List msgfields;
    public DetailedMsgDTO piMessage;
    public DetailedMsgDTO ssnMessage;

    /**
     * Get the Credit Transaction id
     * 
     * @return String
     */
    public String getCreditTxnId() {
        return creditTxnId;
    }

    /**
     * Get the Lot Id
     * 
     * @return String
     */
    public String getLotId() {
        return lotId;
    }

    /**
     * Get the Message Fields
     *  
     * @return List
     */
    public List getMsgfields() {
        
        if (msgfields == null) msgfields = new ArrayList();
        return msgfields;
    }

    /**
     * Get the Message sub Type
     * 
     * @return String
     */
    public String getMsgSubType() {
        return msgSubType;
    }

    /**
     * Get the Message Type
     * 
     * @return String
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * Get the Pi Message DTO
     * 
     * @return DetailedMsgDTO
     */
    public DetailedMsgDTO getPiMessage() {
        return piMessage;
    }

    /**
     * Get the SSN Message DTO
     * 
     * @return DetailedMsgDTO
     */
    public DetailedMsgDTO getSsnMessage() {
        return ssnMessage;
    }

    /**
     * Set the Credit Transaction Id
     * 
     * @param string
     */
    public void setCreditTxnId(String string) {
        creditTxnId = string;
    }

    /**
     * Set the Lot Id
     * 
     * @param string
     */
    public void setLotId(String string) {
        lotId = string;
    }

    /**
     * Set the Message Fields
     * 
     * @param list
     */
    public void setMsgfields(List list) {
        msgfields = list;
    }

    /**
     * Set the Message sub type
     * @param string
     */
    public void setMsgSubType(String string) {
        msgSubType = string;
    }

    /**
     * Set the Message Type
     * 
     * @param string
     */
    public void setMsgType(String string) {
        msgType = string;
    }

    /**
     * Set the PI message DTO
     * 
     * @param msgDTO
     */
    public void setPiMessage(DetailedMsgDTO msgDTO) {
        piMessage = msgDTO;
    }

    /**
     * Set the SSN Message DTO
     *  
     * @param msgDTO
     */
    public void setSsnMessage(DetailedMsgDTO msgDTO) {
        ssnMessage = msgDTO;
    }

    /**
     * Get thet Transaction Type (inward / Outward)
     * 
     * @return String
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * Set the Transaction Type (inward / Outward)
     * 
     * @param string
     */
    public void setTranType(String string) {
        tranType = string;
    }

    /**
     * Get the Type Name
     * 
     * @return String 
     * 
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Set the Type Name
     * 
     * @param string String
     * 
     */
    public void setTypeName(String string) {
        typeName = string;
    }
    
    /**
     * Get the Branch Name
     * 
     * @return String
     */
    public String getBranchName() {
        return branchName;
    }
    
    /**
     * Set the Branch Name
     * 
     * @param string  String
     */
    public void setBranchName(String string) {
        branchName = string;
    }

    /**
     * Get the Date when message entered
     * 
     * @return String
     */
    public String getEntDate() {
        return entDate;
    }

    /**
     * Set the Date when message entered
     * 
     * @param String 
     */
    public void setEntDate(String string) {
        entDate = string;
    }

    /**
     * Get the UTR Number
     * 
     * @return String
     */
    public String getUtrNumber() {
        return utrNumber;
    }

    /**
     * Set the UTR Number
     * 
     * @param String 
     */
    public void setUtrNumber(String string) {
        utrNumber = string;
    }

    /**
     * Get the Transaction Status
     * 
     * @return String
     */
    public String getTxnStatus() {
        return txnStatus;
    }

    /**
     * Set the Transaction Status
     * 
     * @param String
     */
    public void setTxnStatus(String string) {
        txnStatus = string;
    }

}
