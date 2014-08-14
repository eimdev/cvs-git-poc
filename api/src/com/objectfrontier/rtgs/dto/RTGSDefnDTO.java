/* $Header$ */

/*
 * @(#)RTGSDefnDTO.java
 *
 * Copyright by ObjectFrontier, Inc.,
 * 12225 Broadleaf Lane, Alpharetta, GA 30005, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ObjectFrontier, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with ObjectFrontier.
 */
package com.objectfrontier.rtgs.dto;

import java.util.ArrayList;
import java.util.List;

import com.objectfrontier.arch.server.dto.ClientInfo;

import com.objectfrontier.rtgs.msg.defn.client.vo.ChannelDefnValueObject;

/**
 * DTO object that holds the RTGS defn detauls. It also holds the all the 
 * messages definitions supported in RTGS
 * 
 * @author Madhu;Aug 15, 2004
 * @date   Aug 15, 2004
 * @since  RHS API 1.0; Aug 15, 2004
 * @see    ClientInfo
 */
public class RTGSDefnDTO 
implements ClientInfo {

    public ChannelDefnValueObject rtgsDefnVO;
    
    /**
     * Holds all the messages defns supported by this version in form of 
     * MsgDefnDTO objects
     */
    public List msgDefns = new ArrayList();
    
    public List inputMediums = new ArrayList(); 
    
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
    /**
     * @return
     */
    public List getInputMediums() {
        return inputMediums;
    }

    /**
     * @param list
     */
    public void setInputMediums(List list) {
        inputMediums = list;
    }

}

