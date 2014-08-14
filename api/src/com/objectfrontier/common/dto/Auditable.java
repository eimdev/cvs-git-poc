/*
 * @(#)Auditable.java
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
package com.objectfrontier.common.dto;


/**
 * @author gunaseelanv
 * @date   Aug 4, 2006
 * @since  AML 1.0; Aug 4, 2006
 */
public interface Auditable {

    /**
     * Method used to get the auditable Info that is to be stored in the data base
     *
     * This method takes care of converting the dto's data into
     * the xml, string or ..  format
     *
     * @return auditable info of the implementaion class's known format
     */
    Object getAuditableInfo();

    /**
     * Method used to set the auditable Info to the Dto.
     *
     * This method is responsbile for parsing the provided object data
     * in known format (xml, string or ...)  to the dto form
     *
     * Data provided in auditableInfo should be compactible with the data
     * that is obtained from getAuditableInfo()
     *
     * @param auditableInfo
     */
    void setAuditableInfo(Object auditableInfo);
}
