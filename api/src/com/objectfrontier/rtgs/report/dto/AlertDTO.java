/* $Header$ */

/*
 * @(#)AlertDTO.java
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

import java.sql.Timestamp;

import com.objectfrontier.arch.server.dto.ClientInfo;

/**
 * @author anbus
 * @date   Mar 16, 2005
 * @since  RTGS 1.0; Mar 16, 2005
 */
public class AlertDTO
implements ClientInfo {

    public String toAddress;
    
    public String subject;
    
    public String message;
    
    public String Description;
    
    public String ccAddress;
    
    public String bccAddress;
    
    public String branchName;
    
    public Timestamp entDate;
    
    public String errorLongDesc;
    
    public String errorShortDesc;
    
    /**
     * @return
     */
    public String getBccAddress() {
        return bccAddress;
    }

    /**
     * @return
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @return
     */
    public String getCcAddress() {
        return ccAddress;
    }

    /**
     * @return
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @return
     */
    public Timestamp getEntDate() {
        return entDate;
    }

    /**
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * @param string
     */
    public void setBccAddress(String string) {
        bccAddress = string;
    }

    /**
     * @param string
     */
    public void setBranchName(String string) {
        branchName = string;
    }

    /**
     * @param string
     */
    public void setCcAddress(String string) {
        ccAddress = string;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        Description = string;
    }

    /**
     * @param timestamp
     */
    public void setEntDate(Timestamp timestamp) {
        entDate = timestamp;
    }

    /**
     * @param string
     */
    public void setMessage(String string) {
        message = string;
    }

    /**
     * @param string
     */
    public void setSubject(String string) {
        subject = string;
    }

    /**
     * @param string
     */
    public void setToAddress(String string) {
        toAddress = string;
    }
    /**
     * @return
     */
    public String getErrorLongDesc() {
        return errorLongDesc;
    }

    /**
     * @return
     */
    public String getErrorShortDesc() {
        return errorShortDesc;
    }

    /**
     * @param string
     */
    public void setErrorLongDesc(String string) {
        errorLongDesc = string;
    }

    /**
     * @param string
     */
    public void setErrorShortDesc(String string) {
        errorShortDesc = string;
    }

}
