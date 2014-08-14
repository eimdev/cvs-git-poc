/* $Header$ */

/*
 * @(#)SwiftStatus.java
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
package com.objectfrontier.workflow.status;

import com.objectfrontier.workflow.status.Status;

/**
 * @author Anbu
 * @date   Jan 09, 2005
 * @since  Insta Swift 1.1; Jan 09, 2005
 */
public class BACSStatus
extends Status {

    /**
     * Swift status
     */
    public transient static final BACSStatus SWIFT_STATUS_NEW = new BACSStatus("N", "New"); 

    public transient static final BACSStatus SWIFT_STATUS_CANCELLED = new BACSStatus("C", "Cancelled");

    public transient static final BACSStatus SWIFT_STATUS_SENT = new BACSStatus("S", "Sent");

    public transient static final BACSStatus SWIFT_STATUS_SETTLED = new BACSStatus("T", "Settled");

    public transient static final BACSStatus SWIFT_STATUS_UNSUCESSFULL = new BACSStatus("U", "UnSucessfull");

    public transient static final BACSStatus SWIFT_STATUS_ERRORS = new BACSStatus("E", "Error");

    public transient static final BACSStatus SWIFT_STATUS_PROCESSED = new BACSStatus("P", "Processed");

    public transient static final BACSStatus SWIFT_STATUS_COMPLETED = new BACSStatus("C", "Completed");

    public BACSStatus(String status, String shortDesc) {
        super(status, shortDesc);        
    }
}
