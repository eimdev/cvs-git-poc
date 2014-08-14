/* $Header$ */

/*
 * @(#)MsgDefnDTO.java
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

import com.objectfrontier.rhs.meta.client.vo.TransactionPhaseValueObject;
import com.objectfrontier.rtgs.msg.defn.client.vo.MsgDefnValueObject;

/**
 * This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The MsgDefnDTO transfers message definition
 * data in the form of value objects.
 * 
 * @author Malar; Jul 23, 2004
 * @date   Jul 23, 2004
 * @since  RHS API 1.0; Jul 23, 2004
 * @see
 */
public class MsgDefnDTO
implements ClientInfo {

    public MsgDefnValueObject msgDefnVO;
    
    public RTGSDefnDTO rtgsDefnDTO;
    
    public TransactionPhaseValueObject transactionPhaseVO;
    
    
    public List blockDefns = new ArrayList();
    
    /**
     * Accessor method to get the list of message block definitions
     * 
     * @return blockDefns List of MsgBlockDefnDTO objects
     */
    public List getBlockDefns() {
        return blockDefns;
    }
    
    /**
     * Mutator method to set the list of message block Definitions
     * 
     * @param blockDefns
     */
    public void setBlockDefns(List blockDefns) {
        this.blockDefns = blockDefns;
    }
    
    /**
     * @return
     */
    public MsgDefnValueObject getMsgDefnVO() {
        
        if (msgDefnVO == null) msgDefnVO = new MsgDefnValueObject();
        return msgDefnVO;
    }

    /**
     * @param object
     */
    public void setMsgDefnVO(MsgDefnValueObject object) {
        msgDefnVO = object;
    }

}
