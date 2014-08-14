/* $Header$ */

/*
 * @(#)Status.java
 *
 * Copyright by ObjectFrontier, Inc.,
 * 12225 Broadleaf Lane, Alpharetta, GA 30005, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ObjectFrontier, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with ObjectFrontier.
 */
package com.objectfrontier.workflow.status;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

/**
 * @author anbus;Aug 4, 2005
 * @date   Aug 5, 2005
 * @since  RHS APP 2.0; Aug 5, 2005
 * @see    
 */
public class Status
implements Serializable {
    
    /**
     * Message is in initial status for outward message
     */
    public transient static final Status STATUS_INITIAL = new Status("I", "Initial");
    
    /**
     * Message is waiting for authorization
     */
    public transient static final Status STATUS_AUTHORIZATION = new Status("P", "Authorization" );
    
    /**
     * Message is waiting for manual authorization 
     */
    public transient static final Status STATUS_MANUAL_VERIFICATION = new Status("M", "Manual_Verification");
    
    /**
     * Message is waiting for auto authorization 
     */
    public transient static final Status STATUS_AUTOMATIC_VERIFICATION = new Status("A", "Automatic_Verification");
    
    /**
     * Message is waiting for transforming 
     */
    public transient static final Status STATUS_CLASSIFICATION = new Status("V", "Classification");

    /**
     * Message is waiting for verification
     */
    //public static final Status STATUS_VERIFICATION = new Status("verification");

    /**
     * Message is waiting for transforming 
     */
    public transient static final Status STATUS_TRANSFORMATION = new Status("F", "Transformation");
    
    /**
     * Message is wating for release
     */
    public transient static final Status STATUS_RELEASE = new Status("R", "Release");

    /**
     * MESSAGE is waiting in pending queue
     */
    public transient static final Status STATUS_PENDING_QUEUE = new Status("Q", "Pending_Queue");
    
    /**
     * Message is sucessfiully sent
     */
    public transient static final Status STATUS_SUCESSFULL = new Status("Y", "Sucessfull");
    
    public transient static final Status STATUS_SETTLED = new Status("S", "Settled");    

    /**
     * Error occured when sending the message
     */
    public transient static final Status STATUS_UNSUCESSFULL = new Status("U", "UnSucessfull");

    /**
     * Message is cancelled 
     */
    //public static final Status STATUS_CANCELLED = new Status("cancelled");
    
    /**
     * RTGS message lifecycle is completed 
     */
    public transient static final Status STATUS_COMPLETED = new Status("C", "Completed"); 
    
    /**
     * Message is rejected during authorization
     */
    public transient static final Status STATUS_REJECTED = new Status("L", "Rejected"); 

    /**
     * Message is WAITING FOR PI ACK RESPONSE
     */
    public transient static final Status STATUS_WAITING = new Status("W", "WAITING FOR HOST ACK"); 

    /**
     * Message is WAITING FOR PI ACK RESPONSE
     */
    public transient static final Status STATUS_ACK = new Status("K", "Acknowledged"); 

    /**
     * Message is waiting for registration into message Monitor
     */
    public transient static final Status STATUS_REGISTER = new Status("E", "Register");
    
    /**
     * Message is waiting for registration into message Monitor
     */
    public transient static final Status STATUS_UNREGISTER = new Status("D", "UnRegister");
    
    /**
     * Message is waiting for validation 
     */
    public transient static final Status STATUS_VALIDATION = new Status("V", "Validation");

    /**
     * Message is waiting for notification
     */
    public transient static final Status STATUS_NOTIFICATION = new Status("N", "Notification");

    /**
     * Message is waiting for notification
     */
    public transient static final Status STATUS_NOTIFICATION_ONWAIT = new Status("O", "NotificationOnWait");

    /**
     * Message is waiting for vouching
     */
//    public static final Status STATUS_VOUCHING = new Status("vouching");

    /**
     * Message is waiting for timeout
     */
    public transient static final Status STATUS_TIMEOUT = new Status("T", "TimeOut");

    /**
     * This status is used by the process defintions to specify that 
     * the next status is dependant on the task and task has to take care of 
     * changing the status not the workflow  
     */
    public transient static final Status STATUS_UNKNOWN = new Status("Unknown", "Unknown");

    /**
     * Message is in entry status for inward message
     */
    public transient static final Status STATUS_ENTRY = new Status("I", "Entry");
    
    public transient static final Status STATUS_FAILED_DB_CONNECTION = new Status("B", "BranchDBFailure");
    
    public transient static final Status STATUS_INWARD_VOUCHING_INITIATOR = new Status("H", "VouchingUpdate");
    
    public transient static final Status STATUS_COACCEPT = new Status("Y", "Accept");
    
    public transient static final Status STATUS_REDIRECT = new Status("G", "Redirect");
    
    public transient static final Status STATUS_ENTITY_IDENTIFIER = new Status("0E", "EntityIdentifier");
    
    public transient static final Status STATUS_CBS_INTERFACE = new Status("0G", "CBSInterface");   
    
    /**
     * Message is returned pending authoriazation
     */
    public transient static final Status STATUS_RETURNED_AUTHORIZATION = new Status("X", "ReturnedAuthorization"); 

    /**
     * Returned message status
     */
    public transient static final Status STATUS_RETURNED = new Status("Z", "Returned"); 

    /**
     * LM status
     */
    public transient static final Status LM_STATUS_INITIAL = new Status("0I", "Initial");
    
    public transient static final Status LM_STATUS_PROCESSES = new Status("0P", "Process"); 

    public transient static final Status LM_STATUS_FUNDS = new Status("0F", "Funds");

    public transient static final Status LM_STATUS_LIMITS = new Status("0L", "Limits");

    public transient static final Status LM_STATUS_RESERVED_FUNDS = new Status("0R", "ReservedFunds");

    public transient static final Status LM_STATUS_MATCH_MAKING = new Status("0K", "MatchMaking");
    
    public transient static final Status LM_STATUS_COMPLETED = new Status("0C", "MatchMaking");
    
    public transient static final Status LM_STATUS_UNSUCESSFULL = new Status("0U", "UnSucessfull");
    
    public transient static final Status LM_STATUS_MANUAL_VERIFICATION = new Status("0M", "ManualVerification");
   
    public transient static final Status ISO_STATUS_NOTIFICATION_WAIT = new Status("0W", "NotificationWaitTask");
    
    
    /**
     * For BOR, To set/identify Inward Messages in "For Posting" stage 
     */
    public transient static final Status STATUS_POST = new Status("PO", "FOR POSTING"); 

    /**
     * For BOR, Validating Outward Msg Status   
     * 12-Jan-06
     */
    public transient static final Status STATUS_VALIDATE_FAIL = new Status("VU", "VALIDATION FAILURE");    

    public transient static final Status STATUS_VALIDATE = new Status("OV", "FOR VALIDATION");
    
    //RBC CMD 1.0 
    public transient static final Status STATUS_TRSRY_AUTH = new Status("TA", "FOR TREASURY AUTHROIZATION");
    
    public transient static final Status STATUS_RTGS_CHRG = new Status("RC", "  WAITING TO APPLY RTGS CHARGES");
    
    public transient static final Status STATUS_RTGS_CHRG_ACKWAIT  = new Status("RW", "WAITING FOR RTGS CHARGES ACK FROM HOST");
        
    public transient static List supportedStatus = new ArrayList();
    
    /**
     * Workflow status name
     */
    public String name;
    
    public String shortDesc;
    
    static {
        loadSupportedStatus();
    }
    
    public Status(String status, String shortDesc) {
        
        name = status;
        this.shortDesc = shortDesc;
    }
    
    public static Status getStatusByName(String name) {
        
        for (int i = 0; i < supportedStatus.size(); i++) {
            Status status = (Status)supportedStatus.get(i);
            if (status.name.equalsIgnoreCase(name)) {
                return status;
            }
        }
        return null;
    }
    
    public static Status getStatusByShortDesc(String shortDesc) {
        
        for (int i = 0; i < supportedStatus.size(); i++) {
            Status status = (Status)supportedStatus.get(i);
            if (status.shortDesc.equalsIgnoreCase(shortDesc)) {
                return status;
            }
        }
        return null;
    }
    
    
    public static void loadSupportedStatus() {
        
        supportedStatus = new ArrayList();
        
        supportedStatus.add(Status.STATUS_INITIAL);
        supportedStatus.add(Status.STATUS_AUTHORIZATION);
        supportedStatus.add(Status.STATUS_MANUAL_VERIFICATION);
        supportedStatus.add(Status.STATUS_AUTOMATIC_VERIFICATION);
        supportedStatus.add(Status.STATUS_CLASSIFICATION);
        supportedStatus.add(Status.STATUS_TRANSFORMATION);
        supportedStatus.add(Status.STATUS_RELEASE);
        supportedStatus.add(Status.STATUS_PENDING_QUEUE);
        supportedStatus.add(Status.STATUS_SUCESSFULL);
        supportedStatus.add(Status.STATUS_SETTLED);
        supportedStatus.add(Status.STATUS_UNSUCESSFULL);
        supportedStatus.add(Status.STATUS_COMPLETED);
        supportedStatus.add(Status.STATUS_REJECTED);
        supportedStatus.add(Status.STATUS_WAITING);
        supportedStatus.add(Status.STATUS_ACK);
        supportedStatus.add(Status.STATUS_REGISTER);
        supportedStatus.add(Status.STATUS_UNREGISTER);
        supportedStatus.add(Status.STATUS_VALIDATION);
        supportedStatus.add(Status.STATUS_NOTIFICATION);
        supportedStatus.add(Status.STATUS_NOTIFICATION_ONWAIT);
        supportedStatus.add(Status.STATUS_TIMEOUT);
        supportedStatus.add(Status.STATUS_UNKNOWN);
        supportedStatus.add(Status.STATUS_FAILED_DB_CONNECTION);
        supportedStatus.add(STATUS_INWARD_VOUCHING_INITIATOR);
        supportedStatus.add(Status.STATUS_COACCEPT);
        supportedStatus.add(Status.STATUS_ENTRY);
        supportedStatus.add(Status.STATUS_REDIRECT);
        supportedStatus.add(Status.STATUS_RETURNED);
        supportedStatus.add(Status.STATUS_RETURNED_AUTHORIZATION);
        
        //for RBC
        supportedStatus.add(Status.STATUS_POST);
        supportedStatus.add(Status.STATUS_VALIDATE);
        supportedStatus.add(Status.STATUS_VALIDATE_FAIL);
        
        //RBC CMD 1.0
        supportedStatus.add(Status.STATUS_TRSRY_AUTH);
        supportedStatus.add(Status.STATUS_RTGS_CHRG);
        supportedStatus.add(Status.STATUS_RTGS_CHRG_ACKWAIT);
        
        supportedStatus.add(Status.LM_STATUS_INITIAL);
        supportedStatus.add(Status.LM_STATUS_PROCESSES);
        supportedStatus.add(Status.LM_STATUS_FUNDS);
        supportedStatus.add(Status.LM_STATUS_LIMITS);
        supportedStatus.add(Status.LM_STATUS_RESERVED_FUNDS);
        supportedStatus.add(Status.LM_STATUS_MATCH_MAKING);
        supportedStatus.add(Status.LM_STATUS_COMPLETED);
        supportedStatus.add(Status.LM_STATUS_UNSUCESSFULL);
        supportedStatus.add(Status.LM_STATUS_MANUAL_VERIFICATION);
        
        supportedStatus.add(Status.STATUS_ENTITY_IDENTIFIER);
        supportedStatus.add(Status.STATUS_CBS_INTERFACE);
        supportedStatus.add(Status.ISO_STATUS_NOTIFICATION_WAIT);
    }
}
