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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.objectfrontier.arch.server.dto.ClientInfo;

/**
 * @author Karthick GRP
 * @date   Dec 9, 2004
 * @since  RHS IOB 1.0; Dec 9, 2004
 */
public class BranchwiseDTO 
implements ClientInfo {
    
    public String branchName;
    public String branchCode;
    public String branchAddress;
    public List msgTypes; //MsgTypeDTOs
    public AmountDTO grandTotalDTO;
    public int inCount = 0;
    public int outCount = 0;
    public int msgCount = 0;
    
    public MsgDTO msgDTO;
    
    public Map msgTypesMap; //Only at the time of parsing  
    
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
    public List getMsgTypes() {
        
        if (msgTypes == null)
            msgTypes = new ArrayList();
        return msgTypes;
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
    public void setMsgTypes(List list) {
        msgTypes = list;
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
    public Map getMsgTypesMap() {
        
        if (msgTypesMap == null)
            msgTypesMap = new HashMap(); 
        return msgTypesMap;
    }

    /**
     * @param map
     */
    public void setMsgTypesMap(Map map) {
        msgTypesMap = map;
    }

    /**
     * @return
     */
    public int getMsgCount() {
        return msgCount;
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
    public void setMsgCount(int i) {
        msgCount = i;
    }

    /**
     * @param i
     */
    public void setOutCount(int i) {
        outCount = i;
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
     * @return
     */
    public String getBranchAddress() {
        return branchAddress;
    }

    /**
     * @param string
     */
    public void setBranchAddress(String string) {
        branchAddress = string;
    }

    /**
     * @return
     */
     public MsgDTO getMsgDTO() {
         if (msgDTO == null)
            msgDTO = new MsgDTO();
            return msgDTO;
        }

    /**
     * @param msgDTO
     */
    public void setMsgDTO(MsgDTO msgDTO) {
        this.msgDTO = msgDTO;
    }

}
