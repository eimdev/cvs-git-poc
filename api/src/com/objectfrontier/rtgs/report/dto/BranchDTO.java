/* $Header$ */

/*
 * @(#)BranchDTO.java
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
 * @author Karthick GRP
 * @date   Dec 9, 2004
 * @since  RHS IOB 1.0; Dec 9, 2004
 */
public class BranchDTO 
implements ClientInfo {
    
    public String branchName;
    public String branchCode;
    public String msgSubType;
    public List messages;
    public int inCount;
    public int outCount;
    public AmountDTO grandTotalDTO;
    /**
     * @return
     */
    public AmountDTO getGrandTotalDTO() {
        if (grandTotalDTO == null)
            grandTotalDTO = new AmountDTO();
        return grandTotalDTO;
    }

    /**
     * @return
     */
    public List getMessages() {
        
        if (messages == null)
            messages = new ArrayList(); 
        return messages;
    }

    /**
     * @param amountDTO
     */
    public void setGrandTotalDTO(AmountDTO amountDTO) {
        grandTotalDTO = amountDTO;
    }

    /**
     * @param list
     */
    public void setMessages(List list) {
        messages = list;
    }

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
    public int getInCount() {
        return inCount;
    }

    /**
     * @return
     */
    public int getOutCount() {
        return outCount;
    }

    /**
     * @param i
     */
    public void setInCount(int i) {
        inCount = i;
    }

    /**
     * @param i
     */
    public void setOutCount(int i) {
        outCount = i;
    }

}
