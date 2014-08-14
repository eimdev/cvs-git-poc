/*
 * @(#)UserDTO.java
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
package com.objectfrontier.user.dto;

import java.sql.Date;
import java.util.Collection;

import com.objectfrontier.rtgs.dto.IFSCMasterDTO;
import com.objectfrontier.user.client.vo.UserValueObject;


/**
 * Data structrue that will be passed to a client from server whenever a
 * User is requested. Contains the User data. Though this internally
 * may not contain any additional attributes other that VO it's a generic
 * way of dealing with Client Related data objects. It can be used to have
 * more info depending on the functionality.
 *
 * @author Karthick P
 * @date   Feb 5, 2004
 * @since  User 1.1; Feb 5, 2004
 */
public class UserDTO 
implements com.objectfrontier.arch.server.dto.ClientInfo {
    
    public UserValueObject userVO;
    public DesignationDTO designationDTO;
    public LocationTO branchDTO;
    public LocationTO departmentDTO;
    public Collection terminals;
    public Collection locations;
    public Collection userGroups;
    public Collection accesses;
    public Collection histories;
    public Collection reportingAuthorities;
    public AuthorityTypeDTO authorityTypeDTO;
    public Collection subordinates;
    public String userLevel;
    public IFSCMasterDTO masterDTO;
    public boolean centralOfficeUser = false;
    public Date businessDate;
    
    //RBC CMD 1.0
    public boolean defaultPassword = false;
    public boolean passwordExpired = false;
    
    public String getUserLevel() {
        return userLevel;
    }
    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
    /**
     * @return
     */
    public boolean getCentralOfficeUser() {
        return centralOfficeUser;
    }

    /**
     * @param b
     */
    public void setCentralOfficeUser(boolean b) {
        centralOfficeUser = b;
    }

    /**
     * @return
     */
    public Date getBusinessDate() {
        return businessDate;
    }

    /**
     * @param date
     */
    public void setBusinessDate(Date date) {
        businessDate =  date;
    }

    /**
     * @return
     */
    public boolean isDefaultPassword() {
        return defaultPassword;
    }

    /**
     * @return
     */
    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    /**
     * @param b
     */
    public void setDefaultPassword(boolean b) {
        defaultPassword = b;
    }

    /**
     * @param b
     */
    public void setPasswordExpired(boolean b) {
        passwordExpired = b;
    }

}
