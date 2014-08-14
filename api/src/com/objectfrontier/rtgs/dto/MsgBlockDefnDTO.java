/* $Header$ */

/*
 * @(#)MsgBlockDefnDTO.java
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

import com.objectfrontier.rtgs.msg.defn.client.vo.MsgBlockDefnValueObject;
import com.objectfrontier.rtgs.msg.defn.client.vo.MsgBlockFormatValueObject;

/**
 * This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The MsgBlockDefnDTO transfers message block
 * definition data in the form of value objects.
 * 
 * @author Malar; Jul 23, 2004
 * @date   Jul 23, 2004
 * @since  RHS API 1.0; Jul 23, 2004
 * @see
 */

public class MsgBlockDefnDTO
implements ClientInfo {
    
    public MsgBlockDefnValueObject blockDefnVO;
    
    public MsgBlockFormatValueObject blockFormatVO;
    
    public List fieldBlockDefns = new ArrayList();
    
    /**
     * Accessor method to get the list of message field block definitions
     * 
     * @return fieldBlockDefns List of MsgFieldBlockdefnDTO objects
     */
    public List getFieldBlockDefns() {
        return fieldBlockDefns;
    }
    
    /**
     * Mutator method to set the list of message field block definitions
     * 
     * @param fieldBlockDefns
     */
    public void setFieldBlockDefns(List fieldBlockDefns) {
        this.fieldBlockDefns = fieldBlockDefns;
    }
}
