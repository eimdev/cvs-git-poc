/* $Header$ */

/*
 * @(#)TransactionPhaseDTO.java
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

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

import com.objectfrontier.arch.server.dto.ClientInfo;

import com.objectfrontier.rhs.meta.client.vo.TransactionPhaseValueObject;

/**
 * @author anbus
 * @date   Aug 31, 2004
 * @since  RHS API 1.0; Aug 31, 2004
 */
public class TransactionPhaseDTO
implements ClientInfo {

    public TransactionPhaseValueObject tPhaseVO;
    
    public Timestamp time;
    
    public List msgDefns = new ArrayList(2);
    
    /**
     * @return Returns the msgDefns.
     */
    public List getMsgDefns() {
        return msgDefns;
    }
    /**
     * @param msgDefns The msgDefns to set.
     */
    public void setMsgDefns(List msgDefns) {
        this.msgDefns = msgDefns;
    }
}
