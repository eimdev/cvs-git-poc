/*
 * @(#)DefaultConstants.java
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
package com.objectfrontier.common;

import java.text.SimpleDateFormat;

import com.objectfrontier.arch.env.Environment;
import com.objectfrontier.arch.server.bo.BO;

/**
 * @author vEeRa. C
 * @date   Apr 20, 2006
 * @since  aml.app 1.0; Apr 20, 2006
 */
public class CommonDefaultConstants {

    public static final String ENTITY_HISTORY_ID
                                                = "history.id";
    public static final String HISTORY_SEQ      = "HISTORY_SEQ";

    
    //Constants used for Bean Look up.
    public static String Variable_NameService = "NameService";
    public static String Variable_ProviderURL = "ProviderURL";
    public static String Variable_ENVIRONMENT = "ENVIRONMENT";
    public static String Variable_Scheduler   = "Scheduler";
    
    public static String PropertyEnvironment = "aml.resources";

    public static String Property_Config_Environment = "env.config.file";

    public static String UserEnable = "enable";

    public static String NameService;
    public static String ProviderURL;
    public static String ENVIRONMENT;
    public static String MANUALSTRHOST;
    public static String WatchlistSearch;

    public static String PropertyResource;

    // Constants Used for Connection to Oracle Database

    public static String Variable_DSN_NAME          = "DSN_NAME";
    
    public static String Scenario_Run_Time = "";
    
    public static final int MAX_SIZE    = 3999;

    public static String DSN_NAME;

    // DATABASE SERVER

    public static final String ORACLE                   = "Oracle";
    public static final String SQLSERVER                = "SqlServer";
    public static final String MYSQL                    = "MySQL"; 

    // Schema Name    
    public static String SCHEMA_NAME;
    
    public static final String Variable_Schema_Name         = "Schema_Name";

    // Super admin User
    public static String SUPER_ADMIN_USER_GROUP;
    public static String Variable_Super_Admin_User_Group        = "superadminuserGroup";
    
    // Central Office
    public static String AML_CENTRAL_OFFICE;
    public static String Variable_Central_Office        = "centraloffice";
    public static String Variable_Is_Central_Office        = "isCentral";
    
    // for Email Notification
    public static String AML_EMAIL_NOTIFICATION;
    public static String Variable_EMAIL_NOTIFICATION        = "Email_Notification";
    
    //For Date Format
    public static final String DateFormat = "MMM.dd.yyyy";      // Modify by Mariselvam
    
    public static final String VIEW_FIELD_RUNID             = "RUN_ID";
    public static final String VIEW_FIELD_HISTORYID         = "History_Id";
    public static final String VIEW_FIELD_RUNTIME           = "RUN_TIME";
    public static final String VIEW_FIELD_RUNUSER           = "RUN_USER";
    public static final String VIEW_FIELD_SCE_TYPE          = "TYPE";
    public static final String VIEW_FIELD_SCENARIO_PREFIX   = "S_";
    public static final String HISTORY_TABLE_FIELD_NAME     = "AML_HISTORY_COLUMN";

    public static final String KEY_CONNECTION                   = BO.KEY_CONNECTION;
    public static final String KEY_ENVIRONMENT                  = Environment.KEY_ENVIRONMENT;

    // Related to the Databaase Adapters
    public static final String ORACLE_ADAPTER              = "Oracle";
    public static final String SQL_SERVER_ADAPTER          = "SQLServer";

    public static String DATABASE_SERVER             = "";
    public static String Variable_DATABASE_SERVER    = "DatabaseServer";

    public static final String USER_ENTITY_NAME  = "entityName";

    public static final SimpleDateFormat APPLICATION_DATE_FORMATTER = new SimpleDateFormat(DateFormat);
    
    // for Task Type
    public static final String DB_UPDATE                         = "DBUpdate"; 
    
    public static final String StorageKey                        = "storage";
    
    public static final String MONTHLY                              = "Monthly";
    public static final String YEARLY                               = "Yearly";
    public static final String QUARTERLY                            = "Quarterly";
    public static final String HALFYEARLY                           = "HalfYearly";
    
    public static final String PreviousQuarter                   = "Previous Quarter";
    public static final String PreviousHalfYear                  = "Previous HalfYear";
    public static final String PreviousYear                      = "Previous Year";
    public static final String PreviousMonth                     = "Previous Month";
    
    public static final String PreviousYearSameMonth             = "Previous Year Same Month";
    public static final String PreviousHalfYearSameMonth         = "Previous HalfYear Same Month";
    public static final String PreviousQuarterSameMonth          = "Previous Quarter Same Month";
    public static final String PreviousYearSameQuarter           = "Previous Year Same Quarter";
    public static final String PreviousHalfYearSameQuarter       = "Previous HalfYear Same Quarter";
    public static final String PreviousYearSameHalf              = "Previous Year Same HalfYear";
    
    //Added for taking branchcode of currently logged-in user from session
    public static final String USER_BRANCH_CODE  = "branchCode";
    public static final String COMPARISION_DATA_SAME = "SAME";
    
        
    public static final String SELECT                 = "select";
    
}
