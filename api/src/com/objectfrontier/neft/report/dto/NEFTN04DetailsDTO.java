/*
 * @(#)NEFTN04DetailsDTO.java
 *
 * Copyright by ObjectFrontier Software Private Limited (OFS)
 * www.objectfrontier.com
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of OFS. You shall not disclose such confidential
 * information and shall use it only in accordance with the terms of
 * the license agreement you entered into with OFS.
 */
package com.objectfrontier.neft.report.dto;

import java.io.Serializable;



/**
 * @author umeshs
 * @date   Jan 30, 2009
 * @since  insta.reports; Jan 30, 2009
 */
public class NEFTN04DetailsDTO 
implements Serializable {
    
    // This Holds the Batch Time
    private String field3535;
    
    // This Holds the O/W debit Txn
    private String field5175;
    
    // This Holds the O/W debit Txn Amt
    private String field4105;
    
    // This Holds the O/W debit Txn Accepted
    private String field5180;
    
    // This Holds the O/W debit Txn Amt Accepted
    private String field4110;
    
    // This Holds the O/W debit Txn Rejected
    private String field5185;
    
    // This Holds the O/W debit Txn Amt Rejected
    private String field4115;
    
    // This Holds the I/W Txn Received   
    private String field5267;
    
    // This Holds the I/W Txn Amt Received
    private String field4410;
    
    // This Holds the I/W Txn Returned
    private String field5047;
    
    // This Holds the I/W Txn Amt Returned
    private String field4460;

    
    public String getField3535() {
    
        return field3535;
    }

    
    public void setField3535(String field3535) {
    
        this.field3535 = field3535;
    }

    
    public String getField4105() {
    
        return field4105;
    }

    
    public void setField4105(String field4105) {
    
        this.field4105 = field4105;
    }

    
    public String getField4110() {
    
        return field4110;
    }

    
    public void setField4110(String field4110) {
    
        this.field4110 = field4110;
    }

    
    public String getField4115() {
    
        return field4115;
    }

    
    public void setField4115(String field4115) {
    
        this.field4115 = field4115;
    }

    
    public String getField4410() {
    
        return field4410;
    }

    
    public void setField4410(String field4410) {
    
        this.field4410 = field4410;
    }

    
    public String getField4460() {
    
        return field4460;
    }

    
    public void setField4460(String field4460) {
    
        this.field4460 = field4460;
    }

    
    public String getField5047() {
    
        return field5047;
    }

    
    public void setField5047(String field5047) {
    
        this.field5047 = field5047;
    }

    
    public String getField5175() {
    
        return field5175;
    }

    
    public void setField5175(String field5175) {
    
        this.field5175 = field5175;
    }

    
    public String getField5180() {
    
        return field5180;
    }

    
    public void setField5180(String field5180) {
    
        this.field5180 = field5180;
    }

    
    public String getField5185() {
    
        return field5185;
    }

    
    public void setField5185(String field5185) {
    
        this.field5185 = field5185;
    }

    
    public String getField5267() {
    
        return field5267;
    }

    
    public void setField5267(String field5267) {
    
        this.field5267 = field5267;
    }

}
