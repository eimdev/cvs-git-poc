/*
 * @(#)NEFTDetailsReportDTO.java
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

import com.objectfrontier.insta.reports.dto.ReportDTO;


/**
 * @author umeshs
 * @date   Jan 22, 2009
 * @since  insta.reports; Jan 22, 2009
 */
public class NEFTDetailsReportDTO
extends ReportDTO {
    
    // This field Holds the value of 6021 Field
    private String field6021;
    
    // This field Holds the value of 6091 Field
    private String field6091;
    
    //  This field Holds the value of 6061 Field
    private String  field6061;
    
    //  This field Holds the value of 6081 Field
    private String  field6081;
    
    //  This field Holds the value of 5565 Field
    private String  field5565;

    
    public String getField5565() {
    
        return field5565;
    }

    
    public void setField5565(String field5565) {
    
        this.field5565 = field5565;
    }

    
    public String getField6021() {
    
        return field6021;
    }

    
    public void setField6021(String field6021) {
    
        this.field6021 = field6021;
    }

    
    public String getField6061() {
    
        return field6061;
    }

    
    public void setField6061(String field6061) {
    
        this.field6061 = field6061;
    }

    
    public String getField6081() {
    
        return field6081;
    }

    
    public void setField6081(String field6081) {
    
        this.field6081 = field6081;
    }

    
    public String getField6091() {
    
        return field6091;
    }

    
    public void setField6091(String field6091) {
    
        this.field6091 = field6091;
    }
    
    
}
