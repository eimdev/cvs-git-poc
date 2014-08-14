/* $Header$ */

/*
 * @(#)LocationTO.java
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

import org.w3c.dom.Element;

import com.objectfrontier.common.CommonDefaultConstants;
import com.objectfrontier.common.xml.parser.NodeUtil;
import com.objectfrontier.common.xml.parser.XMLUtil;
import com.objectfrontier.user.client.vo.LocationValueObject;

/**
 * Data structrue that will be passed to a client from server whenever a
 * Location is requested. Contains the Location data. Though this internally
 * may not contain any additional attributes other that VO it's a generic
 * way of dealing with Client Related data objects. It can be used to have
 * more info depending on the functionality.
 *
 * @author Karthick P
 * @date   Feb 5, 2004
 * @since  User; Feb 5, 2004
 */
public class LocationTO
extends AbstractUserDTO {

    public LocationValueObject locationVO;
    public Collection desiginations;
    public LocationTO reportingLocation;
    public Collection reportingLocations;
    public Collection users;
    public Collection subLocations;
    public Collection locationGroups;
    public Collection accesses;

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
        append("<id>").                 append(locationVO.id).                  append("</id>").
        append("<name>").               append(locationVO.name).                append("</name>").
        append("<type>").               append(locationVO.type).                append("</type>").
        append("<active>").             append(locationVO.active ? "1" : "0").  append("</active>").
        append("<failedAttempts>").     append(locationVO.failedAttempts).      append("</failedAttempts>").
        append("</AuditInfo>");

        return xmlData.toString();*/

        if(!getFlag()) {
            StringBuffer data = new StringBuffer();
            data.append(" ID : ")
            .append(locationVO.id)
            .append(", Name : ")
            .append(locationVO.name)
            .append(", Active")
            .append(locationVO.active ? "1" : "0")
            .append(", Failed Attempts : ")
            .append(locationVO.failedAttempts)
            .append(", Type : ")
            .append(locationVO.type);

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

            this.locationVO = new LocationValueObject();
            this.locationVO.id              = NodeUtil.getNonMandatoryNodeValue(rootElement,            "id");
            this.locationVO.name            = NodeUtil.getNonMandatoryNodeValue(rootElement,            "name");
            this.locationVO.type            = NodeUtil.getNonMandatoryNodeValue(rootElement,            "type");
            this.locationVO.active          = "1".equals(NodeUtil.getNonMandatoryNodeValue(rootElement, "active"));
            this.locationVO.failedAttempts  = NodeUtil.getNonMandatoryNodeLongValue(rootElement,        "failedAttempts");

        } catch (Exception e) {
            System.out.println("Unable to perform :\n" + e.getMessage());
        }
    }


    
    public LocationValueObject getLocationVO() {
    
        return locationVO;
    }


    
    public void setLocationVO(LocationValueObject locationVO) {
    
        this.locationVO = locationVO;
    }

}
