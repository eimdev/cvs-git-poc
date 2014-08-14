/* $ Header $*/
/*
 * @(#)RHSJSPConstants.java
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
package com.objectfrontier.rtgs.client.jsp.util;

/**
 * 
 * @author Karthick GRP;Jul 22, 2004
 * @date   Jul 22, 2004
 * @since  rtgsapp; Jul 22, 2004
 * @see
 */
public interface RHSUIJSPConstants
extends com.objectfrontier.arch.client.jsp.util.JSPConstants,
        com.objectfrontier.crud.struts.StrutsConstants,  
        com.objectfrontier.rtgs.client.jsp.util.RHSJSPConstants {

    String THEME = "theme";
    String LANGUAGE = "language";

    //theme
    String GREY = "grey";
    String BLUE = "blue";
    String BROWN = "brown";

    String MODE = "mode";
    String ACTION = "action";
    String START = "start";
    String MSGDEFN_SUB_TYPE = "type";
    
    String SUBMIT = "Submit";
    String RESET = "Reset";

    String SELECT = "select";
    String SELECTED = "selected";
    String ENTRY = "entry";
    String CANCEL = "cancel";
    String ONERROR = "onerror";

    String TEXT = "text";
    String TEXT_FIELD = "TextField";

    String RADIO = "radio";

    String SIMPLE = "simple";
    String GROUP = "group";

    String DISPLAY_MSG_FIELD_NUMBER = "display-msg-fieldnumber";
    
    String IFSC = "ifsc ";
    String NAME = "name";
    String CITY = "city";
    String STATE = "state";
    String PINCODE = "pincode";

    String LIST_MSG_TYPE = "listmessagetypes";
    String LIST_RECD_MSG = "listrecdmsg";
    String VIEW_RECD_MSG = "viewrecdmsg";
    String VIEW_ACCEPT_MSG = "viewacceptmsg";
    String VIEW_REDIRECT_MSG =  "viewredirectmsg";
    String DISPLAY_MSG_DEFN = "displaymessagedefn";
    String GENERIC_DISPLAY = "genericdisplay";
    String FORWARD_CUSTOM_DISPLAY = "customdisplay";
    String FORWARD_MODIFY = "modify";
    String FORWARD_ERROR  = "onerror";
    
    String MODIFY_SAVE = "update";
    String LIST_MSGS = "listmessages";
    String VIEW_MSG = "viewmessage";
    String VIEW_FAILURE = "viewfailure";
    String APPROVE_MSG = "approvemessage";

    String LIST_VALIDATION_ENTRY = "listvalidationentry";
    String LIST_AWAITING_ENTRY = "listawaitingentry";
    String LIST_AWAITING_AUTHORIZATION = "listawaitingauthorization";
    String LIST_FOR_POSTING = "listforposting";
    String LIST_AWAITING_VERIFICATION = "listawaitingverification";
    String LIST_AWAITING_RELEASE = "listawaitingrelease";
    String LIST_AWAITING_PROCESSINGMESSAGE = "listawaitingprocessing";
    String LIST_AWAITING_ACKNOWLEDGEMENT = "listawaitingacknowledgement";
    String LIST_AWAITING_SETTLEMENT = "listawaitingsettlement";
    String LIST_SETTLED = "listsettled";
    String LIST_CANCELLED = "listcancelled";
    String LIST_UNSUCCESSFUL = "listunsuccessful";
    String LIST_RECEIVED = "listreceived";
    String LIST_AWAITING_NOTIFICATION = "listawaitingnotification";
    String LIST_FAILURE = "listfailure";
    String LIST_AWAITING_RET_AUTHORIZATION = "listawaitingretauthorization";    
    String LIST_RETURNED = "listreturned";
    
    String LIST_AWAITING_ACCEPT = "listawaitingaccept";
    String LIST_AWAITING_REDIRECT = "listawaitingredirect";

    String FORWARD_CONFIRMATION = "confirm";

    String VERIFY_VIEW = "verifyview";
    String VERIFY_VIEW_MSG = "verifyviewmsg";

    String DISPLAY_IPR_MSG = "iprmessage";

    String ADD = "add";
    String EDIT = "edit";
    String ADD_SAVE = "addsave";
    String REMOVE = "remove";
    String SAVE = "save";
    String SUCCESS = "success";
    String LIST = "list";
    String APPROVE = "approve";
    String REJECT = "reject";
    String VIEW = "view";
    String VERIFY = "verify";
    String APPROVE_VIEW = "approveview";
    String AWAITING_AUTHORIZATION_LIST = "authorization";
    String DOMAIN = "domain";
    String LISTACTION = "listaction";
    
    // RBC
    String ADDACCNO = "addaccno";
    String DELETEACCNO = "delaccno";    
    String ACCNO = "Account Number";   
    String AMT       = "Amount";
    String MULTIACCNO = "multiaccno";
    String MULTIVIEW = "multiView";
    String SINGLEVIEW = "singleView";
    String INWARDSETTLE = "inwardsettle";
    
    String REPETITIVE_FIELD_ADD = "add";
    String REPETITIVE_FIELD_EDIT = "edit";
    String REPETITIVE_FIELD_REMOVE = "remove";
    String OK = "ok";

    String FAILURE = "failure";

    String BANKMASTER = "bankmaster";
    String BRANCHMASTER = "branchmaster";
    String TRANSACTIONPHASE = "transactionphase";
    String CLASSIFICATION = "classificationrules";

    String DATATYPE_DATE = "date";
    String INWARDTRANSACTIONTYPE = "inward";
    String OUTWARDTRANSACTIONTYPE = "outward";
    String CREATE = "create";
    String MODIFY = "modify";
    String MODIFY_FAILURE = "modifyfailure";
    String DELETE = "delete";
    String ADD_CANCEL = "addcancel";
    String MODIFY_CANCEL = "modcancel";
    String RULES_RESET = "reset";

    String AUTHORIZATION = "authorization";
    String VERIFICATION = "verification";
    String RELEASE = "release";
    String ACKNOWLEDGEMENT = "acknowledgement";
    String NOTIFICATION = "notification";
    String ACCEPT   = "Accept";
    String REDIRECT   = "redirect";
    String REDIRECTSAVE = "redirectsave";
    String RECEIVED = "received";
    String SETTLEMENT = "settlement";
    String CANCELLED = "cancelled";
    String UNSUCCESSFUL = "unsuccessful";
    String POSTED = "posted";
    String SETTLED = "settled";
    String INWARD_ENTRY = "inwardentry";
    String INWARD_AUTHORIZATION = "inwardauthorization";
    String INWARD_AUTH = "inwardauth";
    String INWARD_RET_AUTH = "inwardretauth";
    String INWARD_RET_APPROVE = "inwardretapprove";    
    String INWARD_POST = "inwardpost";
    String INWARD_UNSCSS    =   "inwardunsuccess";
    String MSG_TYPE = "msgType";
    String SUB_MSG_TYPE = "subMsgType";
    String CODEWORD = "codeWord";
    String DESCRIPTION = "description";
    
    String FIELD_NO = "field no";
    String TAG = "tag";
    String SEQUENCE_NO = "sequence no";
    String ELEMENT = "element";
    String FORMAT = "format";
    String CHANNEL = "channel";
    String HOST = "host"; 
    String GATE = "gate";
    String CHANNEL_CONFIG = "channelconfig";
    String HOST_CONFIG = "hostconfig";
    String LIST_CHANNEL_CONFIG = "listchannelconfig";
    String LIST_HOST_CONFIG = "listhostconfig";
     
    String IPR_MESSAGE_TYPE = "298/R42";
    String CPR_MESSAGE_TYPE = "298/R41";
    String OATR_MESSAGE_TYPE = "298/R10";
    String NCR_MESSAGE_TYPE = "298/R12";

    String KEY_MESSAGE_TYPE = "298";

    String IPR_MESSAGE_SUBTYPE = "R42";
    String CPR_MESSAGE_SUBTYPE = "R41";
    String OATR_MESSAGE_SUBTYPE = "R10";
    String NCR_MESSAGE_SUBTYPE = "R12";

    String DIRECT = "direct";
    String DEFAULT = "default";
    String VALUEMAP = "valuemap";
    
    String OQLQUERY = "OQLQuery";    
    String CLASSMETHODS = "classmethods";
    
    String GETMSGTYPES     = "getMsgTypes";
    String GETMSGFIELDS    = "getMsgFields";
    
    String TRUE = "true";
    
    // RBC
    String PROVISION_FAILED = "provisionfailed";
    
    //RBC CMD 1.0
    String TRSRY_AUTHORIZATION = "trsryauthorization";
    String LIST_AWAITING_TRSRY_AUTHORIZATION = "listawaitingtrsryauthorization";
    String TIMED_OUT = "timedout";
    String LIST_TIMED_OUT = "listtimedout";

    String VIEW_TRSRY_ATUH_MSG = "viewtrsryauthmessage";
    String VIEW_TIMEDOUT_MSG = "viewtimedoutmessage";
    String TRSRY_AUTH_MODIFIY = "trsryauthmodify";    
//    String IPR_THRESHOLD = "ipr_threshold";
//    String CPR_THRESHOLD = "cpr_threshold";
//    String IDL = "idl";

      String IPR_THRESHOLD = "IPRTHRESHOLD";
      String CPR_THRESHOLD = "CPRTHRESHOLD";
      String IDL = "IDL";
      String INWARD_RTGS_CHRG_POST = "rtgschargespost";
      String RTG_CHRG_FOR_POSTING  = "rtgschargesforposting"; 
      String SAVEIPR = "saveipr";
      String SAVECPR = "savecpr";
      
      //23-Sep-06
      
      String TXNACKWAITING = "txnackwaiting";
      String LIST_TXNACKWAITING = "listtxnackwaiting";
      String VIEW_TXNACKWAITING = "viewtxnackwaiting";
      String FAIL_TXNACKWAITING = "failuretxnackwaiting";
      
      String CHRGSACKWAITING = "chrgsackwaiting";
      String LIST_CHRGSACKWAITING = "listchrgsackwaiting";
      String VIEW_CHRGSACKWAITING = "viewchrgsackwaiting";
      String FAIL_CHRGSACKWAITING = "failurechrgsackwaiting";
      
      //04-Oct-06
      String COMPLETE = "complete";
      
}
