/* $Header$ */

/*
 * @(#)BankMasterDTO.java
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

import com.objectfrontier.rhs.meta.client.vo.BankMasterValueObject;
import com.objectfrontier.user.dto.UserDTO;

/**
 * This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The BankMasterDTO transfers bank master data 
 * in the form of value object.
 * 
 * @author Malar; Aug 2, 2004
 * @date   Aug 2, 2004
 * @since  RHS API 1.0; Aug 2, 2004
 * @see    
 */
public class BankMasterDTO
implements ClientInfo {
    
    public BankMasterValueObject bankMasterVO;
    
    public UserDTO userDTO;
    
    //This varaiable keeping intraday liquidity exposure amount 
    public double idlExposure;
    
    public List ifscMasters = new ArrayList();
    
    /**
     * Accessor method to get the list of IFSC masters
     * 
     * @return ifscMasters List of IFSCMasterDTO objects
     */
    public List getIFSCMasters() {
        return ifscMasters;
    }
    
    /**
     * Mutator method to set the list of IFSC masters
     * 
     * @param ifscMasters
     */
    public void setIFSCMasters(List ifscMasters) {
        this.ifscMasters = ifscMasters;
    }
    /**
     * @return
     */
    public double getIdlExposure() {
        return idlExposure;
    }

    /**
     * @param d
     */
    public void setIdlExposure(double d) {
        idlExposure = d;
    }

}

