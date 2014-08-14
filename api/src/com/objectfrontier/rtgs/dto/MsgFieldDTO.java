/* $Header$ */

/*
 * @(#)MsgFieldDTO.java
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

import com.objectfrontier.rhs.meta.client.vo.MsgFieldValueObject;

/**
 * This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The MsgFieldDTO transfers message field 
 * data in the form of value objects.
 * 
 * @author Malar; Jul 24, 2004
 * @date   Jul 24, 2004
 * @since  RHS API 1.0; Jul 24, 2004
 * @see
 */
public class MsgFieldDTO
implements ClientInfo {
    
    public MsgFieldValueObject msgFieldVO;
    
    public MsgFieldTypeDTO fieldTypeDTO;
    
    public List compoundFields = new ArrayList();
    
    /**
     * @return Returns the compoundFields.
     */
    public List getCompoundFields() {
        return compoundFields;
    }
    
    /**
     * @param compoundFields The compoundFields to set.
     */
    public void setCompoundFields(List compoundFields) {
        this.compoundFields = compoundFields;
    }
}
