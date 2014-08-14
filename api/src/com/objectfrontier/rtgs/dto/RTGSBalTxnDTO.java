/* $Header$ */

/*
 * @(#)RTGSBalTxnDTO.java
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

import com.objectfrontier.arch.server.dto.ClientInfo;

/**
 * This class holds the serializable data related to RTGSBalTxn table and its related
 * tables   
 * 
 * @author Madhu; Sep 15, 2004 
 * @date   Sep 15, 2004
 * @since  RHS IOB 1.0; Sep 15, 2004
 */
public class RTGSBalTxnDTO 
implements ClientInfo {

    public RTGSBalTxnValueObject rtgsBalTxnVO;
    
    public String txnType;
    public Timestamp time;
    /**
     * @return
     */
    public Timestamp getTime() {
        return time;
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
    public RTGSBalTxnValueObject getRtgsBalTxnVO() {
        
        if (rtgsBalTxnVO == null) 
        rtgsBalTxnVO = new RTGSBalTxnValueObject();
        return rtgsBalTxnVO;
    }

    /**
     * @param timestamp
     */
    public void setTime(Timestamp timestamp) {
        time = timestamp;
    }

    /**
     * @param string
     */
    public void setTxnType(String string) {
        txnType = string;
    }

    /**
     * @param object
     */
    public void setRtgsBalTxnVO(RTGSBalTxnValueObject object) {
        rtgsBalTxnVO = object;
    }

}
