/* $Header$ */

/*
 * @(#)RHSMessageConstants.java
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
 * @author sudharanip;Aug 24, 2004
 * @date   Aug 24, 2004
 * @since  RHS App 1.0; Aug 24, 2004
 * @see    
 */
public interface RHSMessageConstants 
extends com.objectfrontier.arch.client.jsp.util.JSPConstants,
com.objectfrontier.crud.struts.StrutsConstants {
    
    String MSG_DEFAULT_FIELD = "RHS_051";
    
    String MSG_AMOUNT = "RHS_052";
    
    String MSG_DATE = "RHS_053";
    
    String MSG_APPROVE = "RHS_054";
    
    String MSG_VIEW = "RHS_055";
    
    String MSG_FIELD_FIXED_LENGTH = "RHS_056";
    
    String BNK_CODE = "RHS_151";
    
    String BNK_NAME = "RHS_152";
    
    String BNK_MODIFY = "RHS_153";
    
    String BNK_DELETE = "RHS_154";
    
    String IFSC_CODE = "RHS_161";
    
    String IFSC_CODE_LENGTH = "RHS_166";

    String BANK_NAME = "RHS_162";
    
    String BRANCH_NAME = "RHS_163";
    
    String BRANCH_MODIFY = "RHS_164";
    
    String BRANCH_DELETE = "RHS_165";

    String STATE = "RHS_167";
    
    String PHASE_NAME = "RHS_168";
    
    String PHASE_ST_TIME  = "RHS_169";
    
    String PHASE_END_TIME = "RHS_170";
    
    String DB_DETAILS = "RHS_171";
    
    String MSG_DETAILS = "RHS_172";
    
    String MSG_CANNOT_MODIFY_OTHERS_ENTRY = "RHS_173";
    
    String BRANCH_CODE = "RHS_174";
    
    String INVALID_FIELD_VALUE = "RHS_175";
    
    String BRANCH_TYPE = "RHS_176";
    
    String CO_VOUCHING = "RHS_177";
    
    String CO_VOUCHING_APPROVE = "RHS_178";
    
    String CO_VOUCHING_REJECT = "RHS_179";
    
    String REDIRECT_VOUCHING = "RHS_180";
    
    String REDIRECT_VOUCHING_APPROVE = "RHS_181";
    
    String REDIRECT_VOUCHING_REJECT = "RHS_182";
    
    String MSG_REDIRECT_APPROVE_AUTHENTICATION = "RHS_183";
    
    String MSG_REDIRECT_REJECT_AUTHENTICATION = "RHS_184";
    
    String MSG_ACCEPT_APPROVE_AUTHENTICATION = "RHS_185";
    
    String MSG_ACCEPT_REJECT_AUTHENTICATION = "RHS_186";

    String MSG_CANNOT_APPROVE_OWN_ENTRY = "RHS_187";
    
    String MSG_REMARKS_MANDATORY = "RHS_188";
    
    String MSG_ALTFIELD_CODE_MANDATORY = "RHS_189";
    
    String MSG_RETURN_MODULE_ID = "RHS_190";
    
    String MSG_AMOUNT_NEGETIVE      = "RHS_191";
    String MSG_CURRENCY_INR         = "RHS_192";
    String MSG_INVAILD_VALUEDATE    = "RHS_193";
    
    // RBC
    String MSG_INVALID_TOTAL_AMOUNT = "RHS_194";
    String MSG_DUPLICATE_ACCNO = "RHS_195";
    String MSG_INVALID_AMOUNT = "RHS_196";
    String MSG_AMOUNT_LESS_THAN_EQUAL_TO_ZERO = "RHS_197";    
    String MSG_INVALID_ACCNO = "RHS_198";
    String MSG_ACCNO_LENGTH = "RHS_199";
    String MSG_TOAMT_LST_FROMAMT = "RHS_003";
    String MSG_SEL_POST = "RHS_200";
    String MSG_CUSTACCNO_LENGTH = "RHS_201";
    String MSG_INVALID_EXPRESSION = "RHS_203";
}

