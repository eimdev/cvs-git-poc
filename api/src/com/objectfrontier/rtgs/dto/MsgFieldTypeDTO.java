/* $Header$ */

/*
 * @(#)MsgFieldTypeDTO.java
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

import com.objectfrontier.rtgs.msg.defn.client.vo.MsgFieldTypeValueObject;
import com.objectfrontier.rtgs.msg.defn.client.vo.MsgFieldValueDomainValueObject;

/**
 * This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The MsgFieldTypeDTO transfers message field
 * type data in the form of value objects.
 * 
 * @author Malar; Jul 24, 2004
 * @date   Jul 24, 2004
 * @since  RHS API 1.0; Jul 24, 2004
 * @see
 */
public class MsgFieldTypeDTO
implements ClientInfo {
    
    public MsgFieldTypeValueObject fieldTypeVO;
    
    public MsgFieldFormatDTO msgFieldFormatDTO;
           
    public MsgFieldValueDomainValueObject valueDomainVO;
    
    public List compoundFields = new ArrayList();
    
    public String queryString;
    
    /**
     * Accessor method to get the list of message compound fields
     * 
     * @return compoundFields List of MsgCompoundFieldDTO objects
     */
    public List getCompoundFields() {
        return compoundFields;
    }
    
    /**
     * Mutator method to set the list of message compound fields
     * 
     * @param compoundFields
     */
    public void setCompoundFields(List compoundFields) {
        this.compoundFields = compoundFields;
    }
    
    /**
     * @return
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * @param string
     */
    public void setQueryString(String string) {
        queryString = string;
    }

    /**
     * @return
     */
    public MsgFieldTypeValueObject getFieldTypeVO() {
        
        if (fieldTypeVO == null) fieldTypeVO = new MsgFieldTypeValueObject();
        return fieldTypeVO;
    }

    /**
     * @param object
     */
    public void setFieldTypeVO(MsgFieldTypeValueObject object) {
        fieldTypeVO = object;
    }
}
