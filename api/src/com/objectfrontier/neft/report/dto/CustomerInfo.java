/* $Header$ */

/*
 * @(#)CustomerInfoDTO.java
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
package com.objectfrontier.neft.report.dto;

import com.objectfrontier.arch.server.dto.ClientInfo;



/**
 * @author pRasanna
 * @date   Mar 13, 2006
 * @since  Fetrem 1.0; Mar 13, 2006
 */
public class CustomerInfo 
implements ClientInfo {

    public String accIfsc;
    public String accName;
    public String accType;
    public String accNo;
    
    /*
     * for the Address Details
     */
    public String accAddress;    
    
    /**
     * @return
     */
    public String getAccName() {
        return accName;
    }

    /**
     * @return
     */
    public String getAccNo() {
        return accNo;
    }

    /**
     * @return
     */
    public String getAccType() {
        return accType;
    }

    /**
     * @param string
     */
    public void setAccName(String string) {
        accName = string;
    }

    /**
     * @param string
     */
    public void setAccNo(String string) {
        accNo = string;
    }

    /**
     * @param string
     */
    public void setAccType(String string) {
        accType = string;
    }

    /**
     * @return
     */
    public String getAccIfsc() {
        return accIfsc;
    }

    /**
     * @param string
     */
    public void setAccIfsc(String string) {
        accIfsc = string;
    }
    /**
     * @return
     */
    public String getAccAddress() {
        return accAddress;
    }

    /**
     * @param string
     */
    public void setAccAddress(String string) {
        accAddress = string;
    }
}
