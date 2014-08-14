/*
 * @(#)NEFTReportDTO.java
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
import java.util.Date;
import java.util.Map;


/**
 * @author mohanadevis
 * @date   Dec 31, 2008
 * @since  insta.reports; Dec 31, 2008
 */
public class NEFTReportDTO 
implements Serializable {
        
    public Date reportGenerationDate;
    public String reportRunBy;
    public Map datas;

    /**
     * @return
     */
    public Date getReportGenerationDate() {
        return reportGenerationDate;
    }

    /**
     * @return
     */
    public String getReportRunBy() {
        return reportRunBy;
    }

    /**
     * @param date
     */
    public void setReportGenerationDate(Date date) {
        reportGenerationDate = date;
    }

    /**
     * @param string
     */
    public void setReportRunBy(String string) {
        reportRunBy = string;
    }

    /**
     * @return Map
     */
    public Map getDatas() {
    
        return datas;
    }

    /**
     * @param Map
     */
    public void setDatas(Map datas) {
    
        this.datas = datas;
    }
}
