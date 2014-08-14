/* $Header$ */

/*
 * @(#)MsgRepetitiveFieldDTO.java
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

/**
 * This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The MsgRepetitiveFieldDTO transfers repetitive
 * filed data in the form of value objects. It extends the MsgFieldDTO.
 * 
 * @author Malar; Jul 26, 2004
 * @date   Jul 26, 2004
 * @since  RHS API 1.0; Jul 26, 2004
 * @see
 */
public class MsgRepetitiveFieldDTO
extends MsgFieldDTO
implements ClientInfo {
    
    public List msgFields = new ArrayList();
    
    public MsgFieldBlockDefnDTO fieldBlockDefnDTO;
    
    /**
     * Accessor method to get the list of message fields
     * 
     * @return msgFields List of MsgFieldDTO objects
     */
    public List getMsgFields() {
        return msgFields;
    }
    
    /**
     * Mutator method to set the list of message fields
     * 
     * @param msgField
     */
    public void setMsgFields(List msgFields) {
        this.msgFields = msgFields;
    }

}