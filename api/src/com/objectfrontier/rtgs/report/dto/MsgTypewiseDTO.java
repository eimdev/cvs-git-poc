/* $Header$ */

/*
 * @(#)MsgTypeDTO.java
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
public class MsgTypewiseDTO 
implements ClientInfo {

    public int inCount = 0;
    public String msgType;
    public String msgSubType;
    public String msgTypeName;
   
    public int outCount = 0;
    public List branches; //BranchDTO
    public AmountDTO grandTotalDTO;
    public Map branchesMap;

    /**
     * @return
     */
    public List getBranches() {
        
        if (branches == null)
            branches = new ArrayList();
        return branches;
    }

    /**
     * @return
     */
    public AmountDTO getGrandTotalDTO() {
        if (grandTotalDTO == null)
            grandTotalDTO = new AmountDTO();
        return grandTotalDTO;
    }

    /**
     * @param list
     */
    public void setBranches(List list) {
        branches = list;
    }

    /**
     * @param amountDTO
     */
    public void setGrandTotalDTO(AmountDTO amountDTO) {
        grandTotalDTO = amountDTO;
    }

    /**
     * @return
     */
    public String getMsgSubType() {
        return msgSubType;
    }

    /**
     * @return
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * @return
     */
    public String getMsgTypeName() {
        return msgTypeName;
    }

    /**
     * @param string
     */
    public void setMsgSubType(String string) {
        msgSubType = string;
    }

    /**
     * @param string
     */
    public void setMsgType(String string) {
        msgType = string;
    }

    /**
     * @param string
     */
    public void setMsgTypeName(String string) {
        msgTypeName = string;
    }

    /**
     * @return
     */
    public Map getBranchesMap() {
        
        if (branchesMap == null)
            branchesMap = new HashMap(); 
        return branchesMap;
    }

    /**
     * @param map
     */
    public void setBranchesMap(Map map) {
        branchesMap = map;
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
