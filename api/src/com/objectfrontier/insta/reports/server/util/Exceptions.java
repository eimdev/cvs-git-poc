/* $Header$ */

/*
 * @(#)Exceptions.java
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
package com.objectfrontier.insta.reports.server.util;

/**
 * This class holds all the exception id used in the RHS server layer.
 * This is mainly used to give proper identifiable naming for a given exception 
 * id
 * 
 * @author Madhu;Aug 23, 2004
 * @date   Aug 23, 2004
 * @since  RHS App 1.0; Aug 23, 2004
 * @see    
 */
public interface Exceptions {

    String RESOURCE_EXCEPTION = "resource";

    String WORKFLOW_EXCEPTION = "workflow";
    
    String SERVER_EXCEPTION = "serverException";
    
    String DAO_EXCEPTION = "dao";
    
    String DTO_PARSER_EXCEPTION = "dtoparserexception";

    String TASK_EXCEPTION = "taskexception";
    
    String MSG_PARSER_EXCEPTION = "msgparserexception";

    String VOUCHING_EXCEPTION = "vouchingexception";
    
    String INSUFFICIENT_BALANCE_EXCEPTION = "insufficientbalanceexception";

    String MSG_CLASSIFICATION_EXCEPTION = "classificationRuleException";
    
    String MSG_BRANCH_MASTER_EXCEPTION = "branchMasterException";
    
    String MSG_RTGS_DEFINITION_EXCEPTION = "rtgsDefinitionException";
    
    String MSG_RTGS_BANKMASTER_EXCEPTION = "bankMasterException";

    String MSG_RTGS_TRANSACTION_PHASE_EXCEPTION = "TransactionPhaseException";
    
    String MSG_RTGS_DBCONNECTION_EXCEPTION = "DBConnectionException";
}


