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
public interface RHSJSPConstants
extends com.objectfrontier.arch.client.jsp.util.JSPConstants,
        com.objectfrontier.crud.struts.StrutsConstants,
        com.objectfrontier.rtgs.util.Constants {
           
    
    String THEME        = "theme";    
    String LANGUAGE     = "language";
    String DISPLAY_MODE = "displayMode";
        
    String PAGE_ID          = "pageID";
    String PAGE_HOLDER      = "pageHolder";
    
    String POP              = "pop";
    String PEEP             = "peep";
    String PUSH             = "push";
    String INDEX            = "index";
    
    String DISPLAY_MSG_FIELD_NUMBER = "display-msg-fieldnumber";
    
    String USER_ID          = "userId";
    String USER_NAME        = "username";
    String USER_PASSWORD    = "password";
    String USER_BRANCH_NAME  = "branchName";
    String USER_BRANCH_CODE  = "branchCode";
    
    //theme
    String GREY         = "grey";
    String BLUE         = "blue";
    String BROWN        = "brown";

    String FORWARD_LOGON    = "logon";
    String FORWARD_REJECT   = "reject";

    String VIEW = "view";

    String SUBMIT           = "submit";
    String RESET            = "reset";
    String ENTRY            = "entry";
    
    String ADD          = "add";
    String ADD_SAVE        = "addsave";
    String EDIT          = "edit";
    String REMOVE       = "remove";
    String LIST         = "list";
    String MODIFY       = "modify";
    String DELETE       = "delete";
    String SAVE         = "save";
    String SUCCESS      = "success";
    String UNSUCCESS    = "unsuccess";
    String SELECT       = "select";
    String VERIFY       = "verify";
    String CANCEL       = "cancel";
    String ACTION       = "action";
    String SEARCH       = "search";
    String GRAPH        = "graph";
    
    String ADD_CANCEL   = "addcancel";
    String MODIFY_CANCEL = "modcancel";

    String REPETITIVE_FIELD_ADD     = "add";
    String REPETITIVE_FIELD_EDIT    = "edit";
    String REPETITIVE_FIELD_REMOVE  = "remove";
    String APPROVE = "approve";
    String REJECT  = "reject";
    String ON = "on";
        
    String SELECTED = "selected";
    
    String LIST_MSG_TYPE        = "listmessagetypes";
    String DISPLAY_MSG_DEFN     = "displaymessagedefn";
    String CLASSIFY_MESSAGE     = "classifymessage";
    String CLASSIFICATION_RULES = "classificationrules";
    String LIST_CLASSIFICATION_RULES = "listclassificationrules";    
    String MODE                 = "mode";
    String OPERATION            = "operation";
    String CODEWORD             = "codeword";

    String CLASSIFICATION_RULES_CREATE   = "create";
    String CLASSIFICATION_RULES_ADD      = "add";
    String CLASSIFICATION_RULES_SAVE     = "save";
    String CLASSIFICATION_RULES_VIEW     = "view";
    String CLASSIFICATION_RULES_LIST     = "list";
    String CLASSIFICATION_RULES_ENTRY    = "entry";
    String CLASSIFICATION_RULES_SELECT   = "select";
    String CLASSIFICATION_RULES_MODIFY   = "modify";
    String CLASSIFICATION_RULES_DELETE   = "delete";
    String CLASSIFICATION_RULES_ADD_CANCEL   = "addcancel";
    String CLASSIFICATION_RULES_MODIFY_CANCEL   = "modcancel";
    String CLASSIFICATION_RULES_RESET    = "reset";

    
    String FORWARD_SUCCESS      = "success";
    String FORWARD_FAILURE      = "failure";
    String FORWARD_SELECT       = "select";
    String FORWARD_CREATE       = "create";
    String FORWARD_LIST         = "list";
    String FORWARD_ADD          = "add";
    String FORWARD_ENTRY        = "entry";
    String FORWARD_VIEW         = "view";
    String FORWARD_MODIFY       = "modify";
    String FORWARD_DELETE       = "delete";
    String FORWARD_SAVE         = "save";
    String FORWARD_CANCEL       = "cancel";
    String FORWARD_RESET        = "reset";
    String FORWARD_CREATE_FAIL  = "createfail";
    String FORWARD_MODIFY_FAIL  = "modifyfail";
    String FORWARD_GRAPH        = "graph";

    String FORWARD_EODCOMPLETED = "eodcompleted";
    String FORWARD_CURRENT_DAY_BOD_NOTDONE = "currentDayBODNotDone";
    String FORWARD_ENV_NOT_INITIALISED = "envNotInitialise";
    
    String AUTO                 = "auto";
    String MANUAL               = "manual";
    
    String MSG_RHS_EMPTY               = "RHS_001";
    String MSG_RHS_SELECTION           = "RHS_002";
    String MSG_RHS_NOTIFICATION_DELETE  = "RHS_004";
    String MSG_RHS_MULTIPLESELECTION = "RHS_005";
    String MSG_RHS_NOTIFICATION_SUCCESS = "RHS_006";
    String MSG_RHS_INVALID= "RHS_175";
    String MSG_TOAMT_LST_FROMAMT = "RHS_003";
    
    //RBC
    String MSG_INVALID_AMOUNT = "RHS_196";
    String MSG_AMOUNT_LESS_THAN_EQUAL_TO_ZERO = "RHS_197";
    String MSG_RHS_NOTIFICATION_UNDELETE  = "RHS_201";
       
    String MSG_GRAPH_INIT_FAILED            = "RHS_007";
    String MSG_GRAPH_CREATE_FAILED          = "RHS_008";
    String MSG_GRAPH_PNG_FAILED             = "RHS_009";
    String MSG_GRAPH_JPEG_FAILED            = "RHS_010";
    String MSG_GRAPH_PIE_FAILED             = "RHS_011";
    String MSG_GRAPH_BAR_FAILED             = "RHS_012";
    String MSG_GRAPH_XY_FAILED              = "RHS_013";
    String CURRENCY_FILEDNO                 = "C4488";
    String AMOUNT_FILEDNO                   = "A4488";
    String DATE_FILEDNO                     = "D4488";
    String SENDTORECV_FIELDNO               = "7495";
    String CURRENCY                         = "INR";
    String DATE_CURR_AMT_FIELDNO            = "4488";

    String Msg_RHS_INVALID_DATE = "MSG_210";
    String Msg_RHS_FROMDATEGTTODATE = "MSG_211";

    String TEXT = "text"; // To display the field as text box
    String TEXT_FIELD = "TextField"; // To display the field as text area
    // following constants added for supporting the message definition client 
    String JUSTIFY_LEFT = "left";
    String JUSTIFY_RIGHT = "right";
    String PADDING_CHAR = "*";
    String FIELD_DELIMITER = "\\n";
    boolean FIXEDLENGTH_TRUE = true ;
    boolean FIXEDLENGTH_FALSE = false;
    boolean CONSTANT_TRUE = true ;
    boolean CONSTANT_FALSE = false;
    boolean MANDATORY_TRUE = true ;
    boolean MANDATORY_FALSE = false;
    boolean MULTILINE_TRUE = true;
    boolean MULTILINE_FALSE = false;
    
    String CONST_TRUE = "true";
    String CONST_FALSE = "false";
    
    String BLOCK_SIMPLE = "simple";
    String BLOCK_GROUP = "group";
    String DATATYPE_STRING = "String";
    String DATATYPE_ALPHABETIC = "alphabetic";
    String DATATYPE_ALPHANUMERIC = "numeric";
    String DATATYPE_NUMERIC = "alphanumeric";
    String DATATYPE_CHAR = "char";
    
    String WORKFLOW         = "workflow";
    String OUTWARD_WORKFLOW = "outwardworkflow";
    String INWARD_WORKFLOW  ="inwardworkflow";
    
    String GO = "go";
    
    String MODIFY_MESSAGE = "modifymessage";    
    String BANKMASTER       = "bankmaster";
    String BRANCHMASTER     = "branchmaster";
    String DBCONFIG     = "dbconfig";
    String TRANSACTIONPHASE = "transactionphase";
    String CLASSIFICATIONRULES   = "classificationrules";
    String RTGSDEFINITIONS = "rtgsdefinitions";
    
    String RECEIVED             = "received";
    String AUTORIZATION         = "autorization";
    String VERIFICATION         = "verification";
    
    String  MSG_SetAttributes       = "MSG_001";
    String  MSG_ClassifySetAttributes   = "MSG_002";
    String  MSG_ClassifyAmountAttributes   = "MSG_003";
    
    String MSG_BRANCHMASTERAttributes  = "MSG_004";
    
    String MSG_RTGSDEFINITIONAttributes  = "MSG_005";
    
    String MSG_BANKMASTERAttributes  = "MSG_006";
    String MSG_BANKMASTERNameAttributes = "MSG_007";
    String MSG_BANKMASTERIdAttribute = "MSG_008";

    String MSG_TRANSACTIONPHASEAttributes = "MSG_009";
    String MSG_RELATED_MSGDEFINITIONS_EXIST = "MSG_010";    
    
    String MSG_DBCONNECTIONAttributes 		= "MSG_011";
    String MSG_GET_BODPARAMS_FAILED       	= "MSG_012";
    String MSG_SET_BODPARAMS_FAILED       	= "MSG_013";
    String MSG_GET_SODPARAMS_FAILED       	= "MSG_014";
    String MSG_SET_SODPARAMS_FAILED       	= "MSG_015";
    String MSG_WORKFLOWTASK_FAILED          = "MSG_016";
    String MSG_BODTASK_FAILED               = "MSG_017";
    String MSG_EODTASK_FAILED               = "MSG_018";    
    String MSG_DBBACKUPTASK_FAILED          = "MSG_019";
    String MSG_DBBACKUP_MANDATORY_FAILED    = "MSG_020";  
        
    String MSG_RHS_ENV_DEFN_INITIATION_ERROR = "MSG_198";
    
    String  MSG_RHS_LISTING_PROBLEM         = "MSG_200";
        
    String  ADD_FIELD_BLOCK         = "AddFieldBlock";    
    String  ADD_BLOCK               = "AddBlock";

    String  ADD_FIELD               = "AddField";    

    String  DELETE_BLOCK            = "DeleteBlock";
    String  DELETE_FIELD_BLOCK      = "DeleteFieldBlock";
    String  ADD_COMP0UND_FIELD      = "AddCompoundBlock";
    String  SHOW_FIELD_TAB          = "ShowFieldTab";
    String  ADD_ALTERNATE_FIELD     = "AddAlternateBlock";
    String FIELD_POP                = "FieldPop";
    String FIELD_PEEP               = "FieldPeep";

    String ALT_FIELD_POP            = "AltFieldPop";
    String ALT_FIELD_PEEP           = "AltFieldPeep";

    String COM_FIELD_POP            = "ComFieldPop";
    String COM_FIELD_PEEP           = "ComFieldPeep";
    

    String RTGSDEFN                     = "rtgsdefn";    
    String MSGDEFN                      = "msgdefn";    
    String PRE_LIST                 = "prelist";
    String MODIFY_VALUE              ="Modify";
    
    String IPR_MESSAGE_TYPE     = "298/R42";
    String CPR_MESSAGE_TYPE     = "298/R41";
    String OATR_MESSAGE_TYPE    = "298/R10";
    String NCR_MESSAGE_TYPE     = "298/R12";

    String KEY_MESSAGE_TYPE     = "298";

    String IPR_MESSAGE_SUBTYPE     = "R42";
    String CPR_MESSAGE_SUBTYPE     = "R41";
    String OATR_MESSAGE_SUBTYPE    = "R10";
    String NCR_MESSAGE_SUBTYPE     = "R12";

//    String CENTRAL_IFSC_CODE = "IOBA0000000";

    // RBC 
    String CENTRAL_IFSC_CODE = "ifsc_centraloffice";
    String SENDER_CORRESPONDENT_FIELD = "SenderCorrepondent";

    String RECEIVER_ADDRESS_FIELD_NO = "F7";
    String SENDER_ADDRESS_FIELD_NO = "F6";
    String IPR_AMOUNT_FIELD_NO = "A4488";
    int IFSC_CODDE_LENGTH = 11;
    
    //RTGSPARAMS
    String DB_BACKUP_STATUS = "DB_BACKUP_STATUS";
    
    String ISCENTRAL            = "isCentral";
    String IFSC_CENTRAL_OFFICE  = "ifsc_centraloffice";
    String IFSC_FUNDS_DEPT      = "ifsc_fundsdept";
    
//    String IOBBANKCODE      = "120"; //RBC Commented
      String HOSTBANKID      = "HOSTBANKID";
    String TRANSACTION_REFERENCE_DELIMITER = ""; //EMPTY STRING
    String RELATED_REFERENCE_DELIMITER = ""; //EMPTY STRING
    
    String RTGSTXN = "rtgstxn";

// FOR REPORT TITLE DISPLAY
      
    String BRANCH_REPORTS                   = "branch";
    String CONTROLLER_REPORT                = "controller";
    String ALL_TRANSACTION                  = "All";
    String SUCCESS_TRANSACTION              = "Success";
    String UN_SUCCESS_TRANSACTION           = "UnSuccess";
    String RETURN_TRANSACTION               = "Return";
    String SUCCESS_BRANCH_TRANSACTION       = "SuccessBranch";
    String UN_SUCCESS_BRANCH_TRANSACTION    = "UnSuccessBranch";
    String DEFERRED_TRANSACTION             = "Deferred";
    String TRANSACTION_BEHALF               =  "ForOtherBranch";    
    String UTR_REPORT                       = "utr";  
    String ALL_BRANCHES                     = "All Branches"; 

    String PRINT_BRANCH                     = "Branch Reports - ";
    String PRINT_CONTROLLER                 = "Controller Reports - ";
    String PRINT_All_TXNS                   = "All - ";
    String PRINT_SUCCESS_TXNS               = "Successful Transactions - ";
    String PRINT_SUCCESS_TYPEWISE_TXNS      = "Successful Transactions - Typewise - ";
    String PRINT_SUCCESS_BRANCH_TXNS        = "Successful Transactions - Branchwise - ";
    String PRINT_UNSUCCESS_TXNS             = "Un-Successful Transactions - ";
    String PRINT_UNSUCCESS_TYPEWISE_TXNS    = "Un-Successful Transactions - Typewise - ";
    String PRINT_UNSUCCESS_BRANCH_TXNS      = "Un-Successful Transactions - Branchwise - ";
    String PRINT_RETURN_TXNS                = "Returned Transaction - ";
    String PRINT_DEFFER_TXNS                = "Deffered Transaction - ";
    String PRINT_TXNS_BEHALF                = "Transactions Accepted on behalf of other Branches- ";
    String PRINT_REDIRECTED                 = "Redirected Transactions - ";
    String PRINT_UTR_REPORT                 = "UTR Report";
    
    String PRINT_TITLE_AS_ON                = "<BR> as on ";

    String DETAILED_VIEW  = "DetailedView";
    String PRINT_DETAILED_VIEW = "Detailed View";

// FOR REPORT MODULE
   String FORWARD_BRANCH_INDIVIDUAL = "branchIndividual";
   String FORWARD_BRANCH_CONSOLIDATED = "branchConsolidated";
   String FORWARD_CONTROLLER_INDIVIDUAL = "controllerIndividual";        
   String FORWARD_CONTROLLER_CONSOLIDATED = "controllerConsolidated";
   String FORWARD_BRANCH_INPUT = "branchInput";
   
   // added for UTR 
   String FORWARD_UTR_REPORT = "utr";
   String FORWARD_UTR_VIEW   = "utrDetailedView";
   String FORWARD_INVALID_UTR = "invalidUTR";  

   // RBC
   String FROM_AMOUNT = "fromAmount";
   String TO_AMOUNT   = "toAmount";
   
   //Newly added for report enhancement
   
   String FORWARD_BRANCH_INDIVIDUAL_ALL = "branchIndividualAll";
   String FORWARD_BRANCH_INDIVIDUAL_SUCCESSFUL = "branchIndividualSuccessful";
   String FORWARD_BRANCH_INDIVIDUAL_UNSUCCESSFUL = "branchIndividualUnsuccessful";
   String FORWARD_BRANCH_INDIVIDUAL_RETURNED = "branchIndividualReturned";
   
   String FORWARD_BRANCH_CONSOLIDATED_ALL = "branchConsolidatedAll";
   String FORWARD_BRANCH_CONSOLIDATED_SUCCESSFUL = "branchConsolidatedSuccessful";
   String FORWARD_BRANCH_CONSOLIDATED_UNSUCCESSFUL = "branchConsolidatedUnsuccessful";
   String FORWARD_BRANCH_CONSOLIDATED_RETURNED = "branchConsolidatedReturned";
   
   
   String FORWARD_CONTROLLER_INPUT = "controllerInput";
   String FORWARD_CONTROLLER_TYPEWISE_CONSOLIDATED = "controllerTypewiseConsolidated";
   String FORWARD_CONTROLLER_TYPEWISE_INDIVIDUAL = "controllerTypewiseIndividual";
   
   String FORWARD_BRANCH_FAILURE = "branchFailure";
   String FORWARD_CONTROLLER_FAILURE = "controllerFailure";
   String FORWARD_EMAIL_FAILURE = "emailFailure";
   String FORWARD_EVENT_FAILURE = "eventFailure";
   
   String ALL = "All";
   // RBC
   String BRANCH_CORE = "core";
   String BRANCH_TREASUARY = "treasury";//RBC CMD 1.0 Spell mistake corrected
   String INWARD_TRANSACTION_TYPE = "inward";
   String OUTWARD_TRANSACTION_TYPE = "outward";
   String BOTH_TRANSACTION_TYPE = "both";
   String BRANCH_DATACENTRE = "datacentre";//RBC CMD 1.0
   
   String[] TRAN_TYPES = {INWARD_TRANSACTION_TYPE, OUTWARD_TRANSACTION_TYPE, BOTH_TRANSACTION_TYPE};
   
   String SUCCESSFUL = "Successful";
   String UNSUCCESSFUL = "Unsuccessful";
   String RETURNED = "Returned";
   String SUCCESSFUL_TYPEWISE = "SuccessfulTypewise";
   String UNSUCCESSFUL_TYPEWISE = "UnsuccessfulTypewise";
   String DEFERRED = "Deferred";
   String FOR_OTHER_BRANCHES = "ForOtherBranches";
   String TYPEWISE = "Typewise";
   String REPORTWISE = "Reportwise";
   String REDIRECTED = "Redirected";
   
   String INDIVIDUAL = "Individual";
   String CONSOLIDATED = "Consolidated";
   
   String BRANCH = "branch";
   String CONTROLLER = "controller";

   String INPUT = "input";
   
   // Newly added for report segregation
   
   String BRANCH_INDIVIDUAL_ALL = "individualAll";
   String BRANCH_INDIVIDUAL_SUCCESSFUL = "individualSuccessful";
   String BRANCH_INDIVIDUAL_UNSUCESSFUL = "individualUnsucessful";
   String BRANCH_INDIVIDUAL_RETURNED = "individualReturned";
   
  
   String BRANCH_CONSOLIDATED_ALL = "consolidatedAll";
   String BRANCH_CONSOLIDATED_SUCCESSFUL = "consolidatedSuccessful";
   String BRANCH_CONSOLIDATED_UNSUCCESSFUL = "consolidatedUnsucessful";
   String BRANCH_CONSOLIDATED_RETURNED = "consolidatedReturned";
   
   // For MIS Reports
   
   String MIS = "mis";
   String CUSTOMER = "customer";
   String INTERBANK = "interbank";
   String COUNTERPARTY = "counterparty";
   String BRANCHWISE = "branchwise";
   String BOTH = "both";
   
   String CUSTOMER_INDIVIDUAL_CONTENT = "customerIndividualContent";
   String CUSTOMER_CONSOLIDATED_CONTENT = "customerConsolidatedContent";
   String INTERBANK_INDIVIDUAL_CONTENT = "interbankIndividualContent";
   String INTERBANK_CONSOLIDATED_CONTENT = "interbankConsolidatedContent";
   String COUNTERPARTY_BRANCHWISE_CONTENT = "counterpartyBranchwiseContent";
   String COUNTERPARTY_ALLBRANCHES_CONTENT = "counterpartyAllBranchesContent";
   
   
   String FORWARD_CUSTOMER_INDIVIDUAL = "customerIndividual";
   String FORWARD_CUSTOMER_CONSOLIDATED = "customerConsolidated";
   String FORWARD_INTERBANK_INDIVIDUAL = "interbankIndividual";
   String FORWARD_INTERBANK_CONSOLIDATED = "interbankConsolidated";
   String FORWARD_COUNTERPARTY_BRANCHWISE = "counterpartyBranchwise";
   String FORWARD_COUNTERPARTY_ALL_BRANCHES = "counterpartyAllBranches";
   
   
   
   //FOR PAGE ITERATOR
   String LIST_BANKS_KEY   = "listBanks";
   String LIST_POSTEDMSGS_KEY = "postedlist";
   String LIST_AWAITINGMSGS_KEY = "awaitinglist";
   String LIST_VERIFYMSGS_KEY = "verifylist";
   String LIST_RELEASEMSGS_KEY = "releaselist";
   String LIST_ACKMSGS_KEY = "acklist";
   String LIST_SETTLEMENTMSGS_KEY = "settlementlist";
   String LIST_CANCELMSGS_KEY = "cancellist";
   String LIST_OUTWARDUNSUCCESSMSGS_KEY = "outwardunsuccesslist";
   String LIST_SETTLEDMSGS_KEY = "settledlist"; 
   String LIST_RECEIVEDMSGS_KEY = "receivedmsglist";
   String LIST_NOTIFYMSGS_KEY = "notifylist";
   String LIST_INWARDENTRYMSGS_KEY = "inwardentrylist";
   String LIST_INWARDAUTHMSGS_KEY = "inwardauthlist";
   String LIST_INWARDSETTLEDMSGS_KEY = "inwardsettlelist";
   String LIST_INWARDCANCELMSGS_KEY = "inwardcancelledlist";
   String LIST_INWARDUNSUCCESSMSGS_KEY = "inwardunsuccesslist";
   String LIST_FIELD_DOMAIN = "listfielddomain";
   String LIST_FIELD_DOMAIN_SEARCH = "listfielddomain.listsearch"; //Ref PageHolder key format syntax
   String RECORDS_PER_PAGE = "records-per-page";
   String INWARDTRANSACTIONTYPE = "inward";
   
   // for Synchronize with PI Client Page 
   String ONERROR = "onerror";
   int TABLE_SEQ_NO_IFSCMASTER = 1;  
   String IFSCMASTER = "IFSCMASTER";
   int TABLE_SEQ_NO_REASONCODES = 2;   
   String REASONCODES = "REASONCODES";
   String PI_SYNC_UPDATE = "update";
   String PI_SYNC_INSERT = "insert";
   String PI_SYNC_BOTH = "both";
   
   
   String MODULE_ID = "Module ID";
   
   String ADMIN_BOD = "admin.bod";
   String ADMIN_EOD = "admin.eod";
   
   String BOD = "bod";
   String EOD = "eod";
   
   String BACKUP_MANDATORY = "backup-mandatory";
   String TAPE_BACKUP_MANDATORY = "tape-backup-mandatory";
   String TAPE_MEDIA_NAME = "tape-media-name";
   
   String SELECTBANK = "selectbank";
   String ALLBRANCHES  = "allbranches";

   String RETURN = "return";

   String LOGGER_BACKUP_MANDATORY = "logger-backup-mandatory";   

   String ERROR_PAGE = "errorpage";   
   String EMAIL = "email";
   String SCHEDULER = "scheduler";
   String ALERT = "alert";
   String EMAIL_ALERT = "Email Alert Report as on";
   String EVENT_LOG = "eventlog";
   String EVENT_ALERT = "Event Alert Report as on";

  //for gate module 
   String GATE = "gate";
   String ACK = "ack";
    
   //for graph generator
   String PIE = "pieChart";
   String BAR = "barGraph";
   String XY = "lineGraph";
   
   String GRAPH_PATH = "graph-path";
   String GRAPH_TYPE = "graph-type";
   String CONTROLLER_GRAPH_LEGEND = "Controller";
   String CONTROLLER_GRAPH_EXTENSION = ".png";
   String CONTROLLER_GRAPH_TITLE = "Controller View";   
   String CONTROLLER_GRAPH_X_LABLE = "Time (Hrs)";   
   String CONTROLLER_GRAPH_Y_LABLE = "Balance (Lacs)";   
   String PNG = "png";
   String JPEG = "jpeg";
   
   String PATH = "path";
   String NAME = "name";
   String TITLE = "title";
   String LEGEND = "legend";
   String X_LABLE = "xLable";   
   String Y_LABLE = "yLable";   
   String WIDTH = "width";   
   String HEIGHT = "height";   
   String X_DATA = "xData";   
   String Y_DATA = "yData";   
   
   String ACTIVITY = "activity"; 
   String ACT1 = "act1";
   String ACT2 = "act2";
   
   String VIEW_XSL = "viewxsl";
   String CONFIG = "config";
   
    // RBC CMD 1.0 - Constants for Reports
   String UTR_NO_WISE = "utrNumberwise";
   String TIMED_OUT = "timedout";
   String PRINT_UTR_NO_WISE = "UTR Numberwise Transactions -";
   String PRINT_TIMED_OUT = "Timed out Transactions -";
   String UTR_NUMBER = "UTR Number";
   String CANCEL_BRANCH_TRANSACTION = "cancelbranch";
   String PRINT_CANCEL_TXNS = "Cancelled Transactions -";
   String PRINT_CANCEL_BRANCH_TXNS = "Cancelled Transactions - Branchwise";
   String UNSUCCSRETURN = "unsuccsreturn";
   
   String FORWARD_EXCELSUBMIT = "excelSubmit";
   String EXCEL_FORMAT_REPORTS = "ExcelFormatReports-";
   String FORWARD_CONTROLLERSUBMIT = "controllerSubmit";
   String FORWARD_CONSOLIDATED = "forwardConsolidated";
   String FORWARD_CONSOLIDATED_INPUT = "consolidatedInput";
   String FORWARD_OUTWARD = "forwardOutward";
   String FORWARD_INWARD = "forwardInward";
   String FORWARD_OUTWARD_FAILURE ="failureOutward";
   String FORWARD_INWARD_FAILURE = "failureInward";
   String DAY_END_REPORTS = "DayEnd Reports-";
   String LIQUIDITY_MGT_REPORTS = "Liquidity Management Reports -";
   String FORWARD_LMREPORTS = "forwardlmreports";
   String FORWARD_LM_FAILURE = "lmfailure";
}
