/* $Header$ */

/*
 * @(#)MsgFieldBlockDefnDTO.java
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

import com.objectfrontier.rtgs.msg.defn.client.vo.MsgFieldBlockDefnValueObject;

/**
 *  This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The MsgFieldBlockDefnDTO transfers message 
 * field block definition data in the form of value objects.
 * 
 * @author Malar; Jul 24, 2004
 * @date   Jul 24, 2004
 * @since  RHS API 1.0; Jul 24, 2004
 * @see
 */
public class MsgFieldBlockDefnDTO
implements ClientInfo {
    
    public MsgFieldBlockDefnValueObject fieldBlockDefnVO;
    
    public List fieldDefns = new ArrayList();
    
    /**
     * Accessor method to get the list of message field definitions
     * 
     * @return fieldDefns List of MsgFieldDefn objects
     */
    public List getFieldDefns() {
        return fieldDefns;
    }
    
    /**
     * Mutator method to set the list of message field definitions
     * 
     * @param fieldDefns
     */
    public void setFieldDefns(List fieldDefns) {
        this.fieldDefns = fieldDefns;
    }
    
}
