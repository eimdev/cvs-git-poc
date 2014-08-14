/* $Header$ */

/*
 * @(#)DesignationDTO.java
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
package com.objectfrontier.user.dto;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Element;

import com.objectfrontier.common.CommonDefaultConstants;
import com.objectfrontier.common.xml.parser.NodeUtil;
import com.objectfrontier.common.xml.parser.XMLUtil;
import com.objectfrontier.user.client.vo.DesignationValueObject;

/**
 * Data structrue that will be passed to a client from server whenever a
 * Desigination is requested. Contains the Desigination data. Though this internally
 * may not contain any additional attributes other that VO it's a generic
 * way of dealing with Client Related data objects. It can be used to have
 * more info depending on the functionality.
 *
 * @author Karthick P
 * @date   Feb 5, 2004
 * @since  User; Feb 5, 2004
 */
public class DesignationDTO
extends AbstractUserDTO {

    public DesignationValueObject designationVO;
    public DesignationDTO reportingDesignation;
    public Collection reportingDesignations;
    public Collection locations;
    public Collection users;
    public boolean flag;
    public String auditData;



    public String getAuditData() {

        return auditData;
    }



    public void setAuditData(String auditData) {

        this.auditData = auditData;
    }



    public boolean getFlag() {

        return flag;
    }



    public void setFlag(boolean flag) {

        this.flag = flag;
    }


    /**
     * @see com.objectfrontier.aml.audit.Auditable#getAuditableInfo()
     */
    public Object getAuditableInfo() {

        /*StringBuffer xmlData = new StringBuffer();
        xmlData.append("<AuditInfo>").
        append("<id>").                 append(designationVO.id).                      append("</id>").
        append("<name>").               append(designationVO.name).                    append("</name>").
        append("<active>").             append(designationVO.active ? "1" : "0").      append("</active>").
        append("<description>").        append(designationVO.description).             append("</description>").
        append("</AuditInfo>");*/

        if(!getFlag()) {
            StringBuffer data = new StringBuffer();
            data.append(" ID : ")
            .append(designationVO.id)
            .append(", Name : ")
            .append(designationVO.name)
            .append(", Active : ")
            .append(designationVO.active ? "1" : "0")
            .append(" Description : ")
            .append(designationVO.description)
            .append(" Location : ")
            .append(getCollectionAsString(locations));

            String xmlData = data.toString();
            if (xmlData.length() > CommonDefaultConstants.MAX_SIZE) {
                return xmlData.substring(0, CommonDefaultConstants.MAX_SIZE);
            }
            return xmlData;
        }

        String data = getAuditData();
        if (data.length() > CommonDefaultConstants.MAX_SIZE) {
             return data.substring(0, CommonDefaultConstants.MAX_SIZE);
        }
        return data;
    }


    /**
     * @see com.objectfrontier.aml.audit.Auditable#setAuditableInfo(java.lang.Object)
     */
    public void setAuditableInfo(Object auditableInfo) {

        String xmlData = (String)auditableInfo;

        try {
            byte[] byteData = new byte[xmlData.length()];
            byteData = xmlData.getBytes();
            Element rootElement = XMLUtil.loadXML(new ByteArrayInputStream(byteData));

            this.designationVO = new DesignationValueObject();
            this.designationVO.id              = NodeUtil.getNonMandatoryNodeValue(rootElement,            "id");
            this.designationVO.name            = NodeUtil.getNonMandatoryNodeValue(rootElement,            "name");
            this.designationVO.active          = "1".equals(NodeUtil.getNonMandatoryNodeValue(rootElement, "active"));
            this.designationVO.description     = NodeUtil.getNonMandatoryNodeValue(rootElement,            "description");

        } catch (Exception e) {
            System.out.println("Unable to perform :\n" + e.getMessage());
        }
    }

    public String getCollectionAsString(Collection location) {

        String operationSum = "";
        if(location == null) {
            return "";
        }

        if(location != null && location.size() > 0) {
            for (Iterator i = location.iterator(); i.hasNext(); ) {
                LocationTO dto = (LocationTO)i.next();
                if(operationSum != null && operationSum.length() == 0) {
                    operationSum = dto.locationVO.getId(); //for Audit
                } else {
                    operationSum += ", " + dto.locationVO.getId(); //for Audit
                }

            }
        }
        return operationSum;
    }



    
    public DesignationValueObject getDesignationVO() {
    
        return designationVO;
    }



    
    public void setDesignationVO(DesignationValueObject designationVO) {
    
        this.designationVO = designationVO;
    }
}
