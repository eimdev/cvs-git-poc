/* $Header$ */

/*
 * @(#)ReportConstants.java
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
package com.objectfrontier.insta.reports.constants;

/**
 * @author Karthick GRP
 * @date   Dec 10, 2004
 * @since  RHS IOB 1.0; Dec 10, 2004
 */
public interface ReportConstants {

    final static String TXN_TYPE_INWARD = "inward";
    final static String TXN_TYPE_OUTWARD = "outward";
    
    final static String VALUE_DATE = "VALUE_DATE";
    final static String MSG_ID = "MESSAGE_ID";
    final static String UTR_NO = "UTR_NUMBER";
    final static String TXN_TYPE = "TXN_TYPE"; 
    final static String ENT_DATE = "ENT_DATE";
    final static String TXN_STATUS = "TXN_STATUS";
    final static String TYPE = "TYPE"; 
    final static String SUB_TYPE = "SUB_TYPE";
    final static String TYPE_NAME = "TYPE_NAME";
    final static String AMOUNT = "AMOUNT";
    final static String SENDER_ADDRESS = "SENDER_ADDRESS";
    final static String RECEIVER_ADDRESS = "RECEIVER_ADDRESS";
    final static String BRANCH_CODE = "BRANCH_CODE";
    final static String BRANCH_NAME = "BRANCH_NAME";
    final static String BRANCH_ADDRESS = "ADDRESS";
    
    final static String IFSC = "IFSC"; //included for DateWise Sorting in Reports
    final static String COREIFSC = "COREIFSC";
        
    final static String PI_ID  = "PI_ID";
    final static String SSN_ID = "SSN_ID";
    final static String LOT_ID = "LOT_ID";

    final static String FIELD_NAME  = "FIELD_NAME";
    final static String FIELD_NO    = "FIELD_NO";
    final static String FIELD_VALUE = "FIELD_VALUE";
    final static String DISP_VALUE  = "DISP_VALUE";
    final static String FIELD_ID    = "FIELD_ID";
        
    final static String TYPEWISE   = "Typewise";
    final static String BRANCHWISE = "Branchwise";


    final static String ALL = "All";
    final static String NORMAL = "NORMAL";
    final static String SUCCESSFUL = "Successful";
    final static String UNSUCCESSFUL = "Unsuccessful";
    final static String RETURNED = "Returned";
    final static String SUCCESSFUL_TYPEWISE = "SuccessfulTypewise";
    final static String UNSUCCESSFUL_TYPEWISE = "UnsuccessfulTypewise";
    final static String DEFERRED = "Deferred";
    final static String FOR_OTHER_BRANCHES = "ForOtherBranches";
    final static String REDIRECTED = "Redirected";
    
    final static String BRANCH = "branch";
    final static String CONTROLLER = "controller";
  
    final static String INDIVIDUAL = "Individual";
    final static String CONSOLIDATED = "Consolidated";
    final static String DETAILED_VIEW  = "DetailedView";
    
    final static String SSN_TIME_FORMAT = "(HH:MM:SS)";
    final static String SSN_TIME_FORMAT_SEPERATOR = ":";

    final static String IN = "In";
    final static String OUT = "Out";
    
    // Constants utilized for RTGSBALTRAN
    final static String TXN_ID = "TXN_ID";
    final static String TXN_DATE = "TXN_DATE";
    final static String UTR_NUMBER = "UTR_NO";
    final static String MSG_TYPE = "MSG_TYPE";
    final static String TRAN_TYPE = "TRAN_TYPE";
    final static String OUR_BRANCH = "OUR_BRANCH";
    final static String OTHER_BANK = "OTHER_BANK";
    final static String OTHER_BRANCH = "OTHER_BRANCH";
    final static String TXN_AMT = "TXN_AMT";
    final static String BALANCE = "BALANCE";
    final static String DR_CR = "DR_CR";
    final static String PARTICULARS = "PARTICULARS";
    
    final static String REASONCODE_REDIRECTION = "REDIRECTION";
    final static String REASONCODE_CO_VOUCHING = "CO-VOUCHING";
    
    final static String TO_ADDRESS = "TO_ADDRESS";
    final static String SUBJECT = "SUBJECT";
    final static String MESSAGE = "MESSAGE";
    final static String DATE_1 = "DATE_1";
    final static String ALL_BRANCH = "All Branches";
    final static String ERROR_LONG_DESCRIPTION = "Error_Long_Description";
    final static String ERROR_SHORT_DESCRIPTION = "Error_Short_Description";
    
    final static String TXN_TYPE_BOTH = "both";
    final static String IPR_SUB_TYPE = "298R41/IPR";
    final static String CPR_SUB_TYPE = "298R42/CPR";

    final static String MSG_COUNT = "msg_count";
    final static String CUSTOMER = "customer";
    final static String INTERBANK = "interbank";
    
    //  RBC CMD 1.0 -- added for Reports
    final static String IPRSUB_TYPE = "R41";
    final static String CPRSUB_TYPE = "R42";
    final static String MSGTYPE = "298";
    
    final static String TIMED_OUT = "Timedout";
    
    final static String UTR_NO_WISE = "utrNumberwise";
    final static String CANCELLED = "cancelled";
    
    final static String CN_SUBTYPE = "R44";
    final static String DN_SUBTYPE = "R43";
    
    
    final static String SUCCESSFUL_STATUS = "C";
    final static String UNSUCCESSFUL_STATUS = "U"; 
    final static String RETURNED_STATUS = "Z"; 
    final static String TIMED_OUT_STATUS = "T";
    final static String CANCELLED_STATUS = "L";
    
    final static Integer INWARD_SUCCESSFUL_STATUS = 1000;
    final static Integer OUTWARD_SUCCESSFUL_STATUS = 3000;
    
    final static Integer INWARD_UNSUCCESSFUL_STATUS  = 930;
    final static Integer OUTWARD_UNSUCCESSFUL_STATUS = 2900;
    
    final static Integer INWARD_RETURNED_STATUS = 900;
    
    final static Integer OUTWARD_TIMED_OUT_STATUS = 2950;
    final static Integer OUTWARD_CANCELLED_STATUS = 2800;
    
    final static String SUMMARY_INWARD_REPORT = "Summary";
    final static String DETAILED_INWARD_REPORT = "Detailed";
    
    final static String REPORT_TYPE1 = "T0";
    final static String REPORT_TYPE2 = "T1";
    
    final static String RBI_TO_BRANCH = "R1 - RBI To Branch";
    final static String RBI_TO_SUNDRY = "RF1 - RBI To Sundry";
    final static String SUNDRY_To_BRANCH = "R2 - Sundry To Branch";
    final static String SUNDRY_To_SUNDRY = "RF2 - Sundry To Sundry";
    
    public static String COMPLETED = "Completed";
    public static String STATUS_NOTIFICATION = "H";
    public static String STATUS_SCHEDULED  = "SC";
    public static String YES = "Y";
    public static String NO = "N";
    public static String SUNDRY_VOUCHING  = "SundryVouching";
    public static String generatedOn = "Generated On ";
    public static String title = "Outward Summary Report - ";
    public static String TxnDetailstitle = "Outward Txn Detailed Report from ";   
    public static String MAILSUBJECT = "IDL";
}