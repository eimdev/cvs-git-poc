/* $Header$ */

/*
 * @(#)DetailedReportDTO.java
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
package com.objectfrontier.rtgs.report.dto;

import java.util.Map;
import java.util.TreeMap;

import com.objectfrontier.arch.server.dto.ClientInfo;

/**
 * @author Bakiyaraj A
 * @date   Feb 9, 2004
 * @since  RHS IOB 1.0; Feb 9, 2004
 * @since  Insta App 1.0; Jun 6, 2005
 */
public class DetailedReportDTO 
extends ReportDTO
implements ClientInfo {

    public Map detailedMessages; //<DetailedMsgDTO>

    /**
     * Get the Detailed Messages
     * @return map 
     */
    public Map getDetailedMessages() {
        
        if (detailedMessages == null)
            detailedMessages = new TreeMap();
        return detailedMessages;
    }

    /**
     * Set the Detailed Messages
     * @param map
     */
    public void setDetailedMessages(Map map) {
        detailedMessages = map;
    }

//    public List detailedMessages; //<DetailedMsgDTO>
//
//    /**
//     * Get the Detailed Messages
//     * @return List 
//     */
//    public List getDetailedMessages() {
//        
//        if (detailedMessages == null)
//            detailedMessages = new ArrayList();
//        return detailedMessages;
//    }
//
//    /**
//     * Set the Detailed Messages
//     * @param list
//     */
//    public void setDetailedMessages(List list) {
//        detailedMessages = list;
//    }
    
}
