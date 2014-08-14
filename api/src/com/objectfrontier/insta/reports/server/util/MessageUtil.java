/* $Header$ */

/*
 * @(#)MessageUtil.java
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
package com.objectfrontier.insta.reports.server.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.objectfrontier.arch.server.j2ee.env.J2EEServerEnvironment;
import com.objectfrontier.rhs.meta.client.vo.MsgFieldValueObject;
import com.objectfrontier.rtgs.dto.MsgBlockDefnDTO;
import com.objectfrontier.rtgs.dto.MsgCompoundFieldDTO;
import com.objectfrontier.rtgs.dto.MsgDTO;
import com.objectfrontier.rtgs.dto.MsgDefnDTO;
import com.objectfrontier.rtgs.dto.MsgFieldBlockDefnDTO;
import com.objectfrontier.rtgs.dto.MsgFieldDTO;
import com.objectfrontier.rtgs.dto.MsgFieldDefnDTO;
import com.objectfrontier.rtgs.dto.MsgFieldTypeDTO;
import com.objectfrontier.rtgs.dto.MsgRepetitiveFieldDTO;
import com.objectfrontier.rtgs.dto.TransactionPhaseDTO;


/**
 * Utility class that provides methods related to identify info from message 
 * and message definiton objects
 * 
 * @author Madhu
 * @date   Sep 7, 2004
 * @since  RHS App 1.0; Sep 7, 2004
 */
public class MessageUtil {
    
    /*
     * Field numbers
     */
    public static final String BANK_APPLICATION_IDENTIFIER_FIELD_NO = "F1";
    public static final String MESSAGE_IDENTIFIER_FIELD_NO          = "F2";
    public static final String INPUT_OUTPUT_IDENTIFIER_FIELD_NO     = "F3";
    public static final String MESSAGE_TYPE_FIELD_NO                = "F4";
    public static final String SUBMESSAGE_TYPE_FIELD_NO             = "F5";
    public static final String SENDER_ADDRESS_FIELD_NO              = "F6";
    public static final String RECEIVER_ADDRESS_FIELD_NO            = "F7";
    public static final String DELIVERY_NOTIFICATION_FLAG_FIELD_NO  = "F8";
    public static final String OPEN_NOTIFICATION_FLAG_FIELD_NO      = "F9";
    public static final String NON_DELIVERYWARNING_FLAG_FIELD_NO    = "F10";
    public static final String OBSOLESCENCEPERIOD_FIELD_NO          = "F11";
    public static final String MESSAGEUSER_REFERENCE_FIELD_NO       = "F12";
    public static final String POSSIBLE_DUPLICATEEMISSION_FLAG_FIELD_NO = "F13";
    public static final String SERVICE_IDENTIFIER_FIELD_NO          = "F14";
    public static final String ORIGINATING_DATE_FIELD_NO            = "F15";
    public static final String ORIGINATING_TIME_FIELD_NO            = "F16";
    public static final String TESTING_AND_TRAINING_FLAG_FIELD_NO   = "F17";
    public static final String SEQUENCE_NUMBER_FIELD_NO             = "F18";
    public static final String FILLER_FIELD_NO                      = "F19";
    public static final String UNIQUE_TRANSACTION_REFERENCE_FIELD_NO = "F20";
    public static final String PRIORITY_FIELD_NO                    = "F21";
    
    public static final String TRANSACTION_REFERENCE_NUMBER_FIELD_NO    = "2020";  
    public static final String RELATED_REFERENCE_FIELD_NO               = "2006";
    public static final String SENDER_TO_RECEIVER_FIELD_NO              = "7495";
    public static final String SENDER_TO_RECEIVER_INFO_ADDITIONAL_INFORMATION_FIELD_NO = "A7495";
    public static final String SENDER_TO_RECEIVER_INFO_CODE_FIELD_NO    = "C7495";
    public static final String SENDER_TO_RECEIVER_INFO_INFORMATION_FIELD_NO = "I7495";
   
    public static final String BENEFICIARY_INSTITUTION_FIELD_NO         = "6521";    
    public static final String ORDERING_CUSTOMER_FIELD_NO               = "5500";
    public static final String BENEFICIARY_CUSTOMER_ACCOUNT_NO = "A5561";
    public static final String BENEFICIARY_CUSTOMER_NAME_ADDRESS_FIELD_NO = "N5561";
    
    public static final String BENEFICIARY_INSTITUTION_COMPOUND_FIELD_NO         = "5556";
    public static final String BENEFICIARY_INSTITUTION_CODE_FIELD_NO         = "C5556"; 
    public static final String BENEFICIARY_INSTITUTION_INFORMATION_FIELD_NO  = "I5556";
    public static final String BENEFICIARY_INSTITUTION_ADDITIONAL_FIELD_NO   = "A5556";
    
    public static final String ACCOUNT_INSTITUTION_WITH_ADDITIONAL_FIELD_NO = "5551";
    public static final String ACCOUNT_INSTITUTION_WITH_ADDITIONAL_CODE_FIELD_NO = "C5551";
    public static final String ACCOUNT_INSTITUTION_WITH_ADDITIONAL_INFORMATION_FIELD_NO = "I5551";
    public static final String ACCOUNT_INSTITUTION_WITH_ADDITIONAL_ADDITIONALINFO_FIELD_NO = "A5551";
    
    public static final String ACCOUNT_INSTITUTION_WITH_BRANCH_FIELD_NO = "6719";
    public static final String ACCOUNT_INSTITUTION_WITH_BRANCH_CODE_FIELD_NO = "C6719";
    public static final String ACCOUNT_INSTITUTION_WITH_BRANCH_INFORMATION_FIELD_NO = "I6719";
    public static final String ACCOUNT_INSTITUTION_WITH_BRANCH_ADDITIONALINFO_FIELD_NO = "A6719";
    public static final String ACCOUNT_WITH__INSTITUTION_FIELD_NO         = "6516";
    
    public static final String INTERMEDIARY_FIELD_NO                = "6516";
     
    public static final String INTERMEDIARY_COMPOUND_FIELD_NO    = "5546";
    public static final String INTERMEDIARY_CODE_FIELD_NO         = "C5546";
    public static final String INTERMEDIARY_INFORMATION_FIELD_NO         = "I5546";
    public static final String INTERMEDIARY_ADDITIONAL_INFORMATION_FIELD_NO         = "A5546"; 
  
    public static final String RECEIVER_CORRESPONDENT_FIELD_NO = "6500";
   
    public static final String RECEIVER_CORRESPONDENT_BRANCH_FIELD_NO = "6718";
    public static final String RECEIVER_CORRESPONDENT_BRANCH_CODE_FIELD_NO = "C6718";
    public static final String RECEIVER_CORRESPONDENT_BRANCH_INFORMATION_FIELD_NO = "I6718";
    public static final String RECEIVER_CORRESPONDENT_BRANCH_INFO_FIELD_NO = "A6718";
    
    public static final String RECEIVER_CORRESPONDENT_ADDITIONAL_FIELD_NO = "5526";    
    public static final String RECEIVER_CORRESPONDENT_ADDITIONAL_CODE_FIELD_NO = "C5526";
    public static final String RECEIVER_CORRESPONDENT_ADDITIONAL_INFORMATION_FIELD_NO = "C5526";
    public static final String RECEIVER_CORRESPONDENT_ADDITIONAL_INFO_FIELD_NO = "C5526";
   
    public static final String SENDER_CORRESPONDENT_FIELD_NO = "5518";
    
    public static final String SENDER_CORRESPONDENT_BRANCH_FIELD_NO = "6717";
    public static final String SENDER_CORRESPONDENT_BRANCH_CODE_FIELD_NO = "C6717";
    public static final String SENDER_CORRESPONDENT_BRANCH_INFORMATION_FIELD_NO = "I6717";
    public static final String SENDER_CORRESPONDENT_BRANCH_INFO_FIELD_NO = "A6717";
    
    public static final String SENDER_CORRESPONDENT_COMPOUND_FIELD_NO = "5521";
    public static final String SENDER_CORRESPONDENT_ADDITIONAL_CODE_FIELD_NO = "C5521";
    public static final String SENDER_CORRESPONDENT_ADDITIONAL_INFORMATION_FIELD_NO = "C5521";
    public static final String SENDER_CORRESPONDENT_ADDITIONAL_INFO_FIELD_NO = "C5521";
   
    public static final String ORDERING_INSTITUTION_FIELD_NO = "5517";
  
    public static final String ORDERING_INSTITUTION_COMPOUND_FIELD_NO = "5516";    
    public static final String ORDERING_INSTITUTION_CODE_FIELD_NO = "C5516";
    public static final String ORDERING_INSTITUTION_INFORMATION_FIELD_NO = "I5516";
    public static final String ORDERING_INSTITUTION_ADDITIONAL_INFORMATION_FIELD_NO = "A5516";
  
    public static final String VALUE_DATE_FIELD_NO = "D4488";
    public static final String CURRENCY_FIELD_NO = "C4488";
    public static final String AMOUNT_FIELD_NO = "A4488";
    public static final String VALUE_DATE_COMPOUND_FIELD_NO = "4488";
    
    public static final String USER_TRANSACTION_ID = "txnId";
    
    public static final String SWIFT_VALUE_DATE_FIELD_NO = "D32A";
    public static final String SWIFT_CURRENCY_FIELD_NO = "C32A";
    public static final String SWIFT_AMOUNT_FIELD_NO = "A32A";
    public static final String SWIFT_VALUE_DATE_COMPOUND_FIELD_NO = "32A";
    
    /*
     * Field values
     */
    public static final String INPUT_OUTPUT_IDENTIFIER_FIELD_OUTWARD_VALUE  = "O";
    
    public static final String BANK_APPLICATION_IDENTIFIER_FIELD_IPR_VALUE  = "IPR";
    
    public static final String BANK_APPLICATION_IDENTIFIER_FIELD_CPR_VALUE  = "CPR";
    
    public static final String RETURNED_MESSAGE_PRIORITY_VALUE              = "50";
    
    public static final String SENDER_TO_RECEIVER_INFO_CODE_FIELD_URGENT_VALUE = "URGENT/";
    
    public static final String MESSAGE_PRIORITY_VALUE = "20";

    /**
     * Method gets the field from msgDto that is associated to given field no
     * 
     * @param   MsgDTO object that holds field to be identified 
     * @param   Associated Field No
     *  
     * @return  Assocaited MsgField witrh fieldNo
     */
    public static MsgFieldDTO getField(MsgDTO msgDTO, String fieldNo) {
        
        List msgFields = msgDTO.getMsgFields();
        
        System.out.println("FIELD No :"+fieldNo);
        for (int i = 0; i < msgFields.size(); i++) {
            
            if (msgFields.get(i) instanceof MsgRepetitiveFieldDTO) {
                MsgRepetitiveFieldDTO rFieldDTO = (MsgRepetitiveFieldDTO)msgFields.get(i);
                List rFields = rFieldDTO.getMsgFields();
                for (int j = 0; j < rFields.size(); j++) {
                    MsgFieldDTO fDTO = getField((MsgFieldDTO)msgFields.get(i), fieldNo);
                    if (fDTO != null) return fDTO;
                }
            } else {
                MsgFieldDTO fDTO = getField((MsgFieldDTO)msgFields.get(i), fieldNo);
                if (fDTO != null) return fDTO;
            }
        }
        return null;
    }
    
    protected static MsgFieldDTO getField(MsgFieldDTO fDTO , String fieldNo) {
        
        if (fDTO.compoundFields != null && fDTO.compoundFields.size() > 0) {
            for (int i = 0; i < fDTO.compoundFields.size(); i++) {
                MsgFieldDTO cmpFieldDTO = getField((MsgFieldDTO)fDTO.compoundFields.get(i), fieldNo);
                if (cmpFieldDTO != null) return cmpFieldDTO;
            }
            return null;
        }
        
        if (fDTO.fieldTypeDTO.fieldTypeVO.no == null) return null;
        
        if (fieldNo == null) return null;
        if (fieldNo.equals(fDTO.fieldTypeDTO.fieldTypeVO.no)) {
            return fDTO;
        }
        return null;
    }

    /**
     * Method gets the field from msgDto that is associated to given field no
     * 
     * @param   MsgDTO object that holds field to be identified 
     * @param   Associated Field No
     *  
     * @return  Assocaited MsgField witrh fieldNo
     */
    public static MsgFieldDTO getFields(MsgDTO msgDTO, String fieldNo) {
        
        List msgFields = msgDTO.getMsgFields();
        
        for (int i = 0; i < msgFields.size(); i++) {
            
            if (msgFields.get(i) instanceof MsgRepetitiveFieldDTO) {
                MsgRepetitiveFieldDTO rFieldDTO = (MsgRepetitiveFieldDTO)msgFields.get(i);
                List rFields = rFieldDTO.getMsgFields();
                for (int j = 0; j < rFields.size(); j++) {
                    MsgFieldDTO fDTO = getFields((MsgFieldDTO)msgFields.get(i), fieldNo);
                    if (fDTO != null) return fDTO;
                }
            } else {
                MsgFieldDTO fDTO = getFields((MsgFieldDTO)msgFields.get(i), fieldNo);
                if (fDTO != null) return fDTO;
            }
        }
        return null;
    }
    
    /**
     * This methos to search with comp parents
     *  
     * @param fDTO
     * @param fieldNo
     * @return
     */
    protected static MsgFieldDTO getFields(MsgFieldDTO fDTO , String fieldNo) {

        if (fDTO.fieldTypeDTO.fieldTypeVO.no == null) return null;
        
        if (fieldNo.equals(fDTO.fieldTypeDTO.fieldTypeVO.no)) {
            return fDTO;
        }
        
        if (fDTO.compoundFields != null && fDTO.compoundFields.size() > 0) {
            for (int i = 0; i < fDTO.compoundFields.size(); i++) {
                MsgFieldDTO cmpFieldDTO = getField((MsgFieldDTO)fDTO.compoundFields.get(i), fieldNo);
                if (cmpFieldDTO != null) return cmpFieldDTO;
            }
            return null;
        }
        
        return null;
    }
    
    
    /**
     * Methods returns the field numbers of all the fieldtypes in the field block  
     *  
     * @param fbDTO
     * @return
     */
    public static List getFieldNumbers(MsgFieldBlockDefnDTO fbDTO) {
        
        List fNoList = new ArrayList();
        
        for (int i = 0; i < fbDTO.getFieldDefns().size(); i++) {
            MsgFieldDefnDTO defnDTO = (MsgFieldDefnDTO)fbDTO.getFieldDefns().get(i);
            
            fNoList.add(defnDTO.defaultFieldTypeDTO.fieldTypeVO.getNo());
            List altFieldTypes = defnDTO.getAlternativeFieldTypes();
            for (int j = 0; j < altFieldTypes.size(); j++) {
                MsgFieldTypeDTO altDefnDTO = (MsgFieldTypeDTO)altFieldTypes.get(j);
                fNoList.add(altDefnDTO.fieldTypeVO.getNo());
            }
        }
        return fNoList;
    }
    
    /**
     * Method to parse the message fields and store in the hashmap
     * 
     * @param msgDTO - source message object
     * 
     * @return map (key is assign on fieldno, value is assigned on msgFieldValue)
     */
    public static Map parseMsgFields(MsgDTO msgDTO) {
        
        List fields = msgDTO.getMsgFields();
        Map fieldMap = new HashMap(1);
        
        for (Iterator it = fields.iterator(); it.hasNext();) {
            MsgFieldDTO fieldDTO = (MsgFieldDTO) it.next();
            parseMsgFields(fieldDTO, fieldMap);
        }
        return fieldMap;
    }

    public static Map parseMsgFields(MsgFieldDTO fieldDTO, Map fieldMap) {
        
        if (fieldDTO instanceof MsgRepetitiveFieldDTO) {
            
            MsgRepetitiveFieldDTO rFieldDTO = (MsgRepetitiveFieldDTO) fieldDTO;
            if (rFieldDTO.getMsgFields().size() > 0 ) {
        
                List fieldDTOList =  rFieldDTO.getMsgFields();
                Map rFieldMap = new HashMap(2);
                for (Iterator i = fieldDTOList.iterator(); i.hasNext();) {
                    MsgFieldDTO reFieldDTO = (MsgFieldDTO)i.next();
                    
                    parseMsgFields(reFieldDTO, rFieldMap);
                }
                //Adding repetative filed into fieldMap  
                fieldMap.put((new Long(fieldDTO.msgFieldVO.id)).toString(), rFieldMap);       
            }
        } else {
            //if compound fields then this condition occur
            if (fieldDTO.getCompoundFields().size() > 0) {
            
                List cmpFields = fieldDTO.getCompoundFields();
                for (Iterator iter = cmpFields.iterator(); iter.hasNext();) {
                    MsgFieldDTO cmpFieldDTO = (MsgFieldDTO)iter.next();
                    parseMsgFields(cmpFieldDTO, fieldMap);
                }
            } else {
                //if msgfield is simple field 
                fieldMap.put(getFieldNO(fieldDTO), getFieldValue(fieldDTO));       
            }
        }
        return fieldMap;
    }

    /**
     * Method to returns the message field number
     * @param msgFieldDTO
     * @return
     */    
    public static String getFieldNO(MsgFieldDTO msgFieldDTO) {
        
        return msgFieldDTO.fieldTypeDTO.fieldTypeVO.no;
    }

    public static String getFieldValue(MsgFieldDTO msgFieldDTO) {
        
        return msgFieldDTO.msgFieldVO.getValue();
    }
    
    /**
     * Method to get the business date
     * 
     * @return current date
     */
    public static Date getBusinessDate() {
        return new Date();
    }
    
    /**
     * Method to get UTR service tag
     * 
     * @return 'H' for Host delivered
     */
    public static String getUTRServiceTag() {
        return "H";
    }
    
    /**
     * Method to get the sequence number format for UTR field
     * 
     * @return 6!n
     */
    public static String getUTRSeqNoFormat() {
        return "000000";
    }
    
    /**
     * Method to get the date format for UTR field
     * 
     * @return yyDDD
     */
    public static String getUTRDateFormat() {
        return "yyDDD";
    }
    
    /**
     * Method to get the date format for OriginatingDate field
     * 
     * @return yyyyMMdd
     */
    public static String getOriginatingDateFieldFormat() {
        return "yyyyMMdd";
    }

    /**
     * Method to get the time format for OriginatingTime field
     * 
     * @return hhmm
     */
    public static String getOriginatingTimeFieldFormat() {
        return "HHmm";
    }

    /**
     * Method to get the default value for SeqNo field
     * 
     * @return 9!n
     */
    public static String getSeqNoFieldValue() {
        return "999999999";
    }
    
    /**
     * Method to get the default value for ObsolescencePeriod field
     * 
     * @return 3!n
     */
    public static String getObsolescencePeriodFieldValue() {
        return "000";
    }
    
    /**
     * Method to get MUR field format
     * 
     * @return 16!x
     */
    public static String getMURFieldFormat() {
        return "0000000000000000";
    }
    
    public static String getR90AckIndicatorFieldNo() {
        
        return "1076";
    }

    public static String getR40SettledIndicatorFieldNo() {
        
        return "6450";
    }

    public static String getR09SettledIndicatorFieldNo() {
        
        return "6450";
    }

    public static String getUTRFieldNo() {
        
        return "F20";
    }
    
    public static String getAmountFieldNo() {
        
        return "A4488";
    }

    public static String getCurrencyFieldNo() {
        
        return "C4488";
    }

    public static String getValueDateFieldNo() {
        
        return "D4488";
    }

    public static String getReceiverAddressFieldNo() {
        
        return "F7";
    }
    
    public static String getSenderAddressFieldNo() {
        
        return "F6";
    }

    public static String getSenderToReceiverFieldNo() {
        
        return "7495";
    }

    public static String getOrderingIFSCFieldNo() {
        
        return "5517";
    }
    
    public static String getOrderingCustomerFieldNo() {
        
        return "5500";
    }

    public static String getChargesFieldNo() {
        
        return "7028";
    }

    public static String getReasonCodeFieldNo() {
        
        return "6346";
    }
    
    public static String getRTGSMsgType() {
        return "298";
    }
    
    public static String getCPRSubType() {
        return "R41";
    }

    public static String getIPRSubType() {
        return "R42";
    }

    public static String getCNSubType() {
        return "R44";
    }

    public static String getDNSubType() {
        return "R43";
    }

    /*public static String getOATRSubType() {
        return "R10";
    }*/
    
    public static String getOATRSubType() {
        return "R10";
    }
    
    public static String trimNewLineString(String str) {
        
        if ("\\r\\n".equalsIgnoreCase(str)) {
            return "\r\n";
        } else if ("\\n".equalsIgnoreCase(str)) {
            return "\n";
        } else if ("\\r".equalsIgnoreCase(str)) {
            return "\r";
        }
        return str;
    }
    
    public static String trimEmptyString(String str) {
        
        if (str == null) return "";
        return str;
    }

    public static String getSSNSettlementTime() {
        return "3525";
    }    

    /**
     * Trim subSequent delimit string and return string, which is not starts
     * with these delimit. 
     * 
     * @param String original string
     * @param String delimit
     * 
     * @return String 
     */
    public static String trimSubSequentDelimiter(String str, String delimit) {
        
        try {
            
            // trim RightHand side if delimit occurs first in these string
            str = rTrim(str, delimit);

            while (str.startsWith(delimit)) {
            
                str = str.substring(delimit.length(), str.length());
                str = rTrim(str, delimit);
            }
            return str;
        } catch (Throwable th) {
            
            // ignore exception
            return str;    
        } 
    }
    
    /**
     * Right Trim the empty space before the delimiter presents in the string
     * 
     * @param String orgStr
     * @param String delimiter
     * 
     * @return String trimStr 
     *   
     */
    public static String rTrim(String orgStr, String delimiter) {
        
        try {
            
            int index = orgStr.indexOf(delimiter);
            orgStr = ((orgStr.substring(0, index)).trim().length() > 0) 
                  ? orgStr 
                  : orgStr.substring(index);
              
            return orgStr;
        } catch (Throwable th) {
            
            // ignore exception
            return orgStr;
        }
    }
    
    public static void createMsgField(MsgDTO msgDTO, String fieldNo) {
       
        MsgDefnDTO mDefn = msgDTO.defnDTO;
        List mbDefns = mDefn.getBlockDefns();
         
        boolean sucess = false;    
        for (Iterator j = mbDefns.iterator(); j.hasNext(); ) {
            MsgBlockDefnDTO mbDefn = (MsgBlockDefnDTO)j.next();
            
            //first get the field type for the given field no
            sucess = generateMsgFields(msgDTO, mbDefn, fieldNo);
            
            if (sucess) break;
        }
    }
    
    /**
     * Method to get the message field type of given field no from the given 
     * message block definition
     * 
     * @param msg
     * @param mbDefn
     * @return
     */
    protected static boolean generateMsgFields(MsgDTO msgDTO, MsgBlockDefnDTO mbDefn, String fieldNo) {
       
        List fbDefns = mbDefn.getFieldBlockDefns();
        
        boolean sucess = false;
        
        for (Iterator i = fbDefns.iterator(); i.hasNext(); ) {

            MsgFieldBlockDefnDTO fbDefn = (MsgFieldBlockDefnDTO)i.next();
            sucess = generateMsgFields(fbDefn, msgDTO, fieldNo);
            
            if (sucess) return true;
        }
        return false;
    }

    /**
     * Methods to get the message field definition for the given message field block definition
     * @param fbDefn
     * @param msgDTO
     */ 
    protected static boolean generateMsgFields(MsgFieldBlockDefnDTO fbDefn, MsgDTO msgDTO, String fieldNo) {
                 
         List fbDefnList = fbDefn.getFieldDefns();
         
         for (Iterator i = fbDefnList.iterator(); i.hasNext(); ) {
             
             MsgFieldDefnDTO fieldDefnDTO = (MsgFieldDefnDTO) i.next();
             
             MsgFieldTypeDTO fieldTypeDTO = fieldDefnDTO.defaultFieldTypeDTO;
             
             if (fieldTypeDTO.fieldTypeVO.no.equalsIgnoreCase(fieldNo)) {
                
                MsgFieldDTO mFieldDTO = createMsgField(fieldTypeDTO);
                msgDTO.getMsgFields().add(mFieldDTO);
                return true;   
             }       
        }
        return false;
    }
    
    /**
     * Method to create message field for the given field value (simple fields)
     * @param fType
     * @return mField MsgFieldDTO
     */
    protected static MsgFieldDTO createMsgField(MsgFieldTypeDTO fType) {
     
        MsgFieldDTO mField = new MsgFieldDTO();
        MsgFieldValueObject mFieldVO = new MsgFieldValueObject();
        mFieldVO.setValue("");
        mField.msgFieldVO = mFieldVO;
        mField.fieldTypeDTO = fType;
        return mField;
    }


    /**
     * 15-Jun-06
     * This method is used to Check allowed
     * Message Poll time for CBS IPR/CPR outward
     * messages.
     * Allowed Poll Time for IPR is 5:30 PM and
     * for CPR is 3:00 PM 
     * Poll times are configured in config file
     * @param msg
     * @return
     */
    public static boolean chkPollingTime(String msg, J2EEServerEnvironment env){
        
        boolean crossedTime = false;
        String tranType = "";
        if(msg.length() > 60 ){
            tranType = msg.substring(43,49);
        }
        
        String iprPollTime = env.getPropertyAsString(RHSConstants.IPRPOLLTIME);
        String cprPollTime = env.getPropertyAsString(RHSConstants.CPRPOLLTIME);

        Calendar rightNow = Calendar.getInstance();
        Calendar setPolTime = Calendar.getInstance();

        if(rightNow.get(7) ==  Calendar.SATURDAY){ //rightNow.get(7) return DAY_OF_WEEK (1-Sunday,2-Monday..7-Saturday)
            iprPollTime = env.getPropertyAsString(RHSConstants.SAT_IPRPOLLTIME);
            cprPollTime = env.getPropertyAsString(RHSConstants.SAT_CPRPOLLTIME);
        }

        int hr = 0;
        int min = 0;
        
        //For 298R42
        if(tranType.equalsIgnoreCase(RHSConstants.KEY_MESSAGE_TYPE+RHSConstants.IPR_MESSAGE_SUB_TYPE)){
            hr = Integer.parseInt(iprPollTime.substring(0,iprPollTime.indexOf(":")));
            min = Integer.parseInt(iprPollTime.substring(iprPollTime.indexOf(":")+1,iprPollTime.length()));
        }

        //For 298R41
        if(tranType.equalsIgnoreCase(RHSConstants.KEY_MESSAGE_TYPE+RHSConstants.CPR_MESSAGE_SUB_TYPE)){
            hr = Integer.parseInt(cprPollTime.substring(0,cprPollTime.indexOf(":")));
            min = Integer.parseInt(cprPollTime.substring(cprPollTime.indexOf(":")+1,cprPollTime.length()));
        }

        setPolTime.set(11,hr);
        setPolTime.set(12,min); 
        if(rightNow.after(setPolTime))
            crossedTime = true;
        
        return crossedTime;
    }

    /**
     * 30-Apr-07
     * This method is used to Check allowed
     * Message Poll time for CBS IPR/CPR outward
     * messages.
     * Allowed Poll Time for IPR is 5:30 PM and
     * for CPR is 3:00 PM  in week days
     * and IPR- 2:00 PM, CPR-12:00 PM in saturday
     * Poll times will be available in TransactionPhase table
     * @param msg
     * @param env
     * @param phaseInfo
     * @return
     */
    public static boolean chkPollingTime(String msg, J2EEServerEnvironment env, List phaseInfo){
        
        boolean crossedTime = false;
        String tranType = "";
        if(msg.length() > 60 ){
            tranType = msg.substring(43,49);
        }
        
        Calendar rightNow = Calendar.getInstance();
        Calendar setPolTime = Calendar.getInstance();
        
        String iprPollTime = env.getPropertyAsString(RHSConstants.IPRPOLLTIME);
        String cprPollTime = env.getPropertyAsString(RHSConstants.CPRPOLLTIME);

        if(rightNow.get(7) ==  Calendar.SATURDAY){ //rightNow.get(7) return DAY_OF_WEEK (1-Sunday,2-Monday..7-Saturday)
            iprPollTime = env.getPropertyAsString(RHSConstants.SAT_IPRPOLLTIME);
            cprPollTime = env.getPropertyAsString(RHSConstants.SAT_CPRPOLLTIME);
        }

        for (int i = 0; i < phaseInfo.size(); i++) {
            TransactionPhaseDTO tpDTO = (TransactionPhaseDTO)phaseInfo.get(i);
            if(tpDTO != null){
                
                if(rightNow.get(7) ==  Calendar.SATURDAY){
                    if(tpDTO.tPhaseVO.getName().startsWith(RHSConstants.SAT_IPRPOLLTIME))
                        iprPollTime = String.valueOf(tpDTO.tPhaseVO.getEndTime().getHours())+":"+String.valueOf(tpDTO.tPhaseVO.getEndTime().getMinutes());
                    if(tpDTO.tPhaseVO.getName().startsWith(RHSConstants.SAT_CPRPOLLTIME))    
                        cprPollTime = String.valueOf(tpDTO.tPhaseVO.getEndTime().getHours())+":"+String.valueOf(tpDTO.tPhaseVO.getEndTime().getMinutes());
                         
                }else{
                    if(tpDTO.tPhaseVO.getName().startsWith(RHSConstants.IPRPOLLTIME))
                        iprPollTime = String.valueOf(tpDTO.tPhaseVO.getEndTime().getHours())+":"+String.valueOf(tpDTO.tPhaseVO.getEndTime().getMinutes());
                    if(tpDTO.tPhaseVO.getName().startsWith(RHSConstants.CPRPOLLTIME))    
                        cprPollTime = String.valueOf(tpDTO.tPhaseVO.getEndTime().getHours())+":"+String.valueOf(tpDTO.tPhaseVO.getEndTime().getMinutes()); 
                    
                }
            }
                
        }



        int hr = 0;
        int min = 0;
        
        //For 298R42
        if(tranType.equalsIgnoreCase(RHSConstants.KEY_MESSAGE_TYPE+RHSConstants.IPR_MESSAGE_SUB_TYPE)){
            hr = Integer.parseInt(iprPollTime.substring(0,iprPollTime.indexOf(":")));
            min = Integer.parseInt(iprPollTime.substring(iprPollTime.indexOf(":")+1,iprPollTime.length()));
        }

        //For 298R41
        if(tranType.equalsIgnoreCase(RHSConstants.KEY_MESSAGE_TYPE+RHSConstants.CPR_MESSAGE_SUB_TYPE)){
            hr = Integer.parseInt(cprPollTime.substring(0,cprPollTime.indexOf(":")));
            min = Integer.parseInt(cprPollTime.substring(cprPollTime.indexOf(":")+1,cprPollTime.length()));
        }

        setPolTime.set(11,hr);
        setPolTime.set(12,min); 
        if(rightNow.after(setPolTime))
            crossedTime = true;
        
        return crossedTime;
    }

    //RBC CMD 1.0 for ISO
    public static String paddTrailSpaces(String fValue, int len){
        while(fValue.length() < len)
            fValue = fValue+" ";
        
        return fValue;    
    }

    //  RBC CMD 1.0 for ISO
    public static String paddLeadingSpaces(String fValue, int len){
        while(fValue.length() < len)
            fValue = " "+fValue;
        
        return fValue;    
    }


    //RBC CMD 1.0 for ISO
    public static String paddLeadingZeros(String fValue, int len){
        while(fValue.length() < len)
            fValue = "0"+fValue;

        return fValue;
    }



    //RBC CMD 1.0 for ISO
    public static String paddTrailZeros(String fValue, int len){
        while(fValue.length() < len)
            fValue = fValue+"0";

        return fValue;
    }


    //RBC CMD 1.0 for ISO
    public static String paddTrailWith(String fValue, int len,String str){
        while(fValue.length() < len)
            fValue = fValue+str;
        
        return fValue;    
    }

    //  RBC CMD 1.0 for ISO
    public static String paddLeadingWith(String fValue, int len,String str){
        while(fValue.length() < len)
            fValue = str+fValue;
        
        return fValue;    
    }

    /**
     * RBC CMD 1.0
     * Method to get Field Value
     * @param msgDTO
     * @return
     */ 
    public static String getFieldValue(MsgDTO msgDTO, String fieldNo) {
        
        String fieldVal = "";
        if (fieldNo == null) return fieldVal; 
        MsgFieldDTO fieldDTO = MessageUtil.getField(msgDTO, fieldNo);
        if (fieldDTO == null ) return fieldVal;
            else if(fieldDTO.msgFieldVO.getValue() == null ) return fieldVal; //For RBC     
        fieldVal = fieldDTO.msgFieldVO.getValue().trim();
        return fieldVal;     
    }

    /**
     * RBC CMD 1.0
     * Method to set Field Value
     * @param msgDTO
     * @param fieldNo
     * @param fieldValue
     */
    public static void setFieldValue(MsgDTO msgDTO, String fieldNo, String fieldValue){
     
        for(Iterator itr = msgDTO.getMsgFields().iterator();itr.hasNext();){
            MsgFieldDTO msgFieldDTO = (MsgFieldDTO) itr.next();

            if(msgFieldDTO.fieldTypeDTO.fieldTypeVO.getNo().equalsIgnoreCase(fieldNo)){
                msgFieldDTO.msgFieldVO.value = fieldValue;
            }
                 
        }   
    }
    
    /**
     * For BoR Requirement-JUL 07
     * CPN Inward Return as IPR 

     * To return the Message fields for the given MsgDefnDTO 
     * 
     * @param msgDefnDTO 
     * @return msgFields
     */
    public static List getMsgFields(MsgDefnDTO msgDefnDTO) {
        
        List msgFields = new ArrayList();
        
        for(int i = 0; i < msgDefnDTO.blockDefns.size(); i++ ) {
            
            MsgBlockDefnDTO blockDefnDTO = (MsgBlockDefnDTO)msgDefnDTO.blockDefns.get(i);
            for(int j = 0; j < blockDefnDTO.fieldBlockDefns.size(); j++) {
                
                MsgFieldBlockDefnDTO fieldBlockDefnDTO = (MsgFieldBlockDefnDTO)blockDefnDTO.fieldBlockDefns.get(j);
                for(int k = 0; k < fieldBlockDefnDTO.fieldDefns.size(); k++) {
                    
                    MsgFieldDefnDTO fieldDefnDTO = (MsgFieldDefnDTO)fieldBlockDefnDTO.fieldDefns.get(k);

                    MsgFieldDTO fieldDTO = new MsgFieldDTO();
                    fieldDTO.msgFieldVO = new MsgFieldValueObject();
                    fieldDTO.fieldTypeDTO = fieldDefnDTO.defaultFieldTypeDTO;
                    if(fieldDefnDTO.defaultFieldTypeDTO.compoundFields != null) {
                        for(int l = 0; l < fieldDefnDTO.defaultFieldTypeDTO.compoundFields.size(); l++) {
                                                    
                            MsgCompoundFieldDTO compFieldDTO = (MsgCompoundFieldDTO)fieldDefnDTO.defaultFieldTypeDTO.compoundFields.get(l);
                            MsgFieldDTO compMsgFieldDTO = new MsgFieldDTO();
                            compMsgFieldDTO.msgFieldVO = new MsgFieldValueObject();
                            compMsgFieldDTO.fieldTypeDTO = compFieldDTO.fieldTypeDTO;
                            if(compMsgFieldDTO.fieldTypeDTO.fieldTypeVO.constant) {
                                compMsgFieldDTO.msgFieldVO.value = compFieldDTO.fieldTypeDTO.fieldTypeVO.defaultValue;                                  
                            }
                            fieldDTO.compoundFields.add(compMsgFieldDTO);
                                
                        }                                                
                    }
                    
                    if(fieldDefnDTO.defaultFieldTypeDTO.fieldTypeVO.constant) {
                        
                        fieldDTO.msgFieldVO.value = fieldDefnDTO.defaultFieldTypeDTO.fieldTypeVO.defaultValue;
                    }
                    msgFields.add(fieldDTO);
                }
            }
        }
        
        return msgFields;
    }    

    /**
     * To return the priority field value 
     */
    public static String getPriorityFieldValue() {
        return "00";
    }

    /**
     * To return the currency field value 
     */
    public static String getCurrencyFieldValue() {
        return "INR";
    }
    
    
    
    /**
     * To return the value for field I7495 in 
     * case of 298R41 Inward Return   
     */
    public static String getDefaultCPNRetValue() {
        return "CPN RETURN";
    }
    
    
}
