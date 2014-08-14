/* $Header$ */

/*
 * @(#)MsgFieldFormatDTO.java
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

import java.util.List;

import com.objectfrontier.arch.server.dto.ClientInfo;

import com.objectfrontier.rtgs.msg.defn.client.vo.MsgFieldFormatValueObject;

/**
 * This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The MsgFieldFormatDTO contains the 
 * MsgFieldFormatObject
 * 
 * @author anbus
 * @date   Jan 24, 2005
 * @since  RTGS 1.0; Jan 24, 2005
 */
public class MsgFieldFormatDTO 
implements ClientInfo {
    
    public MsgFieldFormatValueObject fieldFormatVO;
    
    /**
     * Holds the alternative delimiter values
     */
    public List startDelimiterList;
    public List endDelimiterList;
    public List multiLineStartDelimiterList;
    public List multiLineEndDelimiterList;
    
    /**
     * Holds the defaultDelimiter Values
     */
    public String startDelimiter;
    public String endDelimiter;
    public String multiLineStartDelimiter;
    public String multiLineEndDelimiter;

    /**
     * @return
     */
    public String getEndDelimiter() {
        return endDelimiter;
    }

    /**
     * @return
     */
    public List getEndDelimiterList() {
        return endDelimiterList;
    }

    /**
     * @return
     */
    public String getMultiLineEndDelimiter() {
        return multiLineEndDelimiter;
    }

    /**
     * @return
     */
    public List getMultiLineEndDelimiterList() {
        return multiLineEndDelimiterList;
    }

    /**
     * @return
     */
    public String getMultiLineStartDelimiter() {
        return multiLineStartDelimiter;
    }

    /**
     * @return
     */
    public List getMultiLineStartDelimiterList() {
        return multiLineStartDelimiterList;
    }

    /**
     * @return
     */
    public String getStartDelimiter() {
        return startDelimiter;
    }

    /**
     * @return
     */
    public List getStartDelimiterList() {
        return startDelimiterList;
    }

    /**
     * @param string
     */
    public void setEndDelimiter(String string) {
        endDelimiter = string;
    }

    /**
     * @param list
     */
    public void setEndDelimiterList(List list) {
        endDelimiterList = list;
    }

    /**
     * @param string
     */
    public void setMultiLineEndDelimiter(String string) {
        multiLineEndDelimiter = string;
    }

    /**
     * @param list
     */
    public void setMultiLineEndDelimiterList(List list) {
        multiLineEndDelimiterList = list;
    }

    /**
     * @param string
     */
    public void setMultiLineStartDelimiter(String string) {
        multiLineStartDelimiter = string;
    }

    /**
     * @param list
     */
    public void setMultiLineStartDelimiterList(List list) {
        multiLineStartDelimiterList = list;
    }

    /**
     * @param string
     */
    public void setStartDelimiter(String string) {
        startDelimiter = string;
    }

    /**
     * @param list
     */
    public void setStartDelimiterList(List list) {
        startDelimiterList = list;
    }
}
