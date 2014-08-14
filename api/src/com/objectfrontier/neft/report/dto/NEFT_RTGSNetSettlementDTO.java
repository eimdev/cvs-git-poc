/*
 * @(#)NEFT_RTGSNetSettlementDTO.java
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
import java.util.Map;



/**
 * @author kanagarajanm
 * @date   Mar 16, 2009
 * @since  insta.reports; Mar 16, 2009
 */
public class NEFT_RTGSNetSettlementDTO 
implements Serializable {

    protected String msgType;
    protected String msgSubType;
    // Sender IFSC
    protected String orderingInstitution;
    protected String info;
    protected String additionalInfo;
    protected String amount;
    
    // for NEFT_RTGS Net Settlement aggregate Report
    protected Map aggregateMap;

    
    public String getAdditionalInfo() {
    
        return additionalInfo;
    }

    
    public void setAdditionalInfo(String additionalInfo) {
    
        this.additionalInfo = additionalInfo;
    }

    
    public String getAmount() {
    
        return amount;
    }

    
    public void setAmount(String amount) {
    
        this.amount = amount;
    }
   
    public String getInfo() {
    
        return info;
    }

    
    public void setInfo(String info) {
    
        this.info = info;
    }

    
    public String getMsgSubType() {
    
        return msgSubType;
    }

    
    public void setMsgSubType(String msgSubType) {
    
        this.msgSubType = msgSubType;
    }

    
    public String getMsgType() {
    
        return msgType;
    }

    
    public void setMsgType(String msgType) {
    
        this.msgType = msgType;
    }

    
    public String getOrderingInstitution() {
    
        return orderingInstitution;
    }

    
    public void setOrderingInstitution(String orderingInstitution) {
    
        this.orderingInstitution = orderingInstitution;
    }


    
    public Map getAggregateMap() {
    
        return aggregateMap;
    }


    
    public void setAggregateMap(Map aggregateMap) {
    
        this.aggregateMap = aggregateMap;
    }
    
}
