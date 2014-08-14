/* $Header$ */

/*
 * @(#)RTGSMenuItem.java
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
package com.objectfrontier.arch.client.struts.tiles.bean;

/**
 * RTGS Menu item implemention for count display in links   
 * 
 * @author Karthick GRP
 * @date   Dec 31, 2004
 * @since  RHS UI Client 1.0; Dec 31, 2004
 */
public class RTGSMenuItem 
extends MenuItem {
    
    private String trantype;
    private String status;
    private String subtype;
    private String test;

    public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	/**
     * Return String representation.
     */
    public String toString() {
        
        StringBuffer buff = new StringBuffer();

        buff.append("RTGSMenuItem[");
        if (getValue() != null)
            buff.append("value=").append(getValue()).append(", ");
        if (getKey() != null)
            buff.append("key=").append(getKey()).append(", ");
        if (getLink() != null)
            buff.append("link=").append(getLink()).append(", ");
        if (getTooltip() != null)
            buff.append("tooltip=").append(getTooltip()).append(", ");
        if (getIcon() != null)
            buff.append("icon=").append(getIcon()).append(", ");
        if (getTrantype() != null)
                    buff.append("icon=").append(getTrantype()).append(", ");            
        if (getSubtype() != null)
                    buff.append("icon=").append(getSubtype()).append(", ");            
        if (getStatus() != null)
                    buff.append("icon=").append(getStatus()).append(", ");            
        buff.append("]");
        return buff.toString();
    }
    /**
     * String
     */
    public String getStatus() {
        return status;
    }

    /**
     * String
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * String
     */
    public String getTrantype() {
        return trantype;
    }

    /**
     * void
     */
    public void setStatus(String string) {
        status = string;
    }

    /**
     * void
     */
    public void setSubtype(String string) {
        subtype = string;
    }

    /**
     * void
     */
    public void setTrantype(String string) {
        trantype = string;
    }

}
