/* $Header$ */

/*
 * @(#)MsgFieldDTO.java
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

import com.objectfrontier.arch.server.dto.ClientInfo;

/**
 * @author Bakiyaraj A
 * @date   Feb 9, 2004
 * @since  RHS IOB 1.0; Feb 9, 2004
 * @since  Insta App 1.0; Jun 8, 2005
 */
public class MsgFieldDTO 
implements ClientInfo {
    
    public String fieldName;
    public String fieldNo;
    public String value;
    public String displayValue;

    /**
     * Get the Message Field Name
     * 
     * @return String
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get the Message Field Number
     * 
     * @return String
     */
    public String getFieldNo() {
        return fieldNo;
    }

    /**
     * Set the Field Name 
     * 
     * @param string
     */
    public void setFieldName(String string) {
        fieldName = string;
    }

    /**
     * Set the Field Number
     * 
     * @param string
     */
    public void setFieldNo(String string) {
        fieldNo = string;
    }

    /**
     * Get the Field value 
     * 
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the Field Value
     * 
     * @param string 
     */
    public void setValue(String string) {
        value = string;
    }

    /**
     * Get the Display Value
     * 
     * @return String
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Set the Display Value
     * 
     * @param String
     */
    public void setDisplayValue(String string) {
        displayValue = string;
    }

}
