/* $Header$ */

/*
 * @(#)MsgDTO.java
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
package com.objectfrontier.rtgs.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.objectfrontier.arch.server.dto.ClientInfo;
import com.objectfrontier.arch.util.ListMap;
import com.objectfrontier.host.client.vo.HostSystemValueObject;
import com.objectfrontier.rhs.client.meta.vo.MessageValueObject;
import com.objectfrontier.user.dto.UserDTO;

/**
 * This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The MsgDTO transfers message data in the 
 * form of value objects.
 * 
 * @author Malar; Jul 23, 2004
 * @date   Jul 23, 2004
 * @since  RHS API 1.0; Jul 23, 2004
 * @see
 */
public class MsgDTO
implements ClientInfo {
    
    public static final int REGULAR_MESSAGE     = 0;
    public static final int RETURNED_MESSAGE    = 1;
     
    public MessageValueObject msgVO;
   
    /**
     * holds the assoiciated host system value object
     */
    public HostSystemValueObject hsVO;
    
    /**
     * The piResponseMsgDTO holds the PI Respo  nse Message
     */
    public ResponseMsgDTO piResponseMsgDTO;
    
    /**
     * Holds the SSN Message
     */
    public ResponseMsgDTO ssnResponseMsgDTO;
     
    /**
     * The ifscMasterDTO holds the mandatory IFSC master id for the message.
     */
    public IFSCMasterDTO ifscMasterDTO;
    
    /**
     * The defnDTO holds the mandatory definition id for the message.
     */
    public MsgDefnDTO defnDTO;
    
    /**
     * The msgSourceDTO is the source to which the message is associated.
     * It holds the unique message source name and its description.
     */
    public MsgSourceDTO msgSourceDTO;
    
    /**
     * The sourceMsgDTO is the associated message object for the message.
     */
    public MsgDTO sourceMsgDTO;
    
    /**
     * The userDTO object holds the user permission
     */
    public UserDTO userDTO;
    
    /**
     * Txn Details assocaited with message object  
     */
    public RHSClientTxnDetailsDTO txnDetailsDTO;
    
    /**
     * msgIFSCMasterHistoryDTO holds the operation code ,remarks and object history
     */
    public MsgIFSCMasterHistoryDTO msgIFSCMasterHistoryDTO;
        
    /**
     * The msgFields list holds the message fields.
     */
    public List msgFields = new ArrayList();
    
    /**
     * The msgHistories holds the list of histories associated with the message.
     */
    public List msgHistories = new ArrayList();
    
    /**
     * The ifscMasterHistories holds the list of histories associated with the message.
     */
    public List ifscMasterHistories = new ArrayList();
    
    /**
     * The msgErrors holds the list of validator errors associated with the errors
     */
    public List msgErrors;
    
    /**
     * Holds the reasonCode  
     */
    public String reasonCode;
    
    /**
     * Amount in display format
     */
    public String displayAmount;
    
    public String channel;
    
    public String hostName;

    /**
     * For certain specific operations, msg history has to be stored
     * for auditing purpose. In this case this holds the input data like 
     * remarks and OperationCode that has to be stored
     */
    public MsgHistoryDTO msgHistoryDTO;
    
    public int isReturned = REGULAR_MESSAGE;
    
    /**
     * RBC
     * 
     * This list contains the selected message ids for postiong 
     */
    public List msgIds = new ArrayList();
    
    /**
     * RBC
     * To hold MsgVO.msgId
     */
    public  String messageId = "";
    
    public List accountNos = new ArrayList();
    
    public List amounts = new ArrayList();

    /**
     * Accessor method to get the list of message fields
     * 
     * @return msgFields List of MsgFieldDTO objects
     */
    public List getMsgFields() {
        return msgFields;
    }
    
    /**
     * Mutator method to set the list of message fields
     * 
     * @param msgFields
     */
    public void setMsgFields(List msgFields) {
        this.msgFields = msgFields;
    }
    
    /**
     * Accessor method to get the list of message histories
     * 
     * @return msgHistories List of MsgHistoryDTO objects
     */
    public List getMsgHistories() {
        return msgHistories;
    }
    
    /**
     * Mutator method to set the list of message histories
     * 
     * @param msgHistories
     */
    public void setMsgHistories(List msgHistories) {
        this.msgHistories = msgHistories;
    }
    
    /**
     * Accessor method to get the list of responses
     * 
     * @return responses List of MsgDTO objects
     *
    public List getResponses() {
        return responses;
    }
    
    /**
     * Mutator method to set the list of responses
     * 
     * @param responses
     *
    public void setResponses(List responses) {
        this.responses = responses;
    }
    
    /**
     * Accessor method to get the source message
     * 
     * @return sourceMsgDTO
     */
    public MsgDTO getSourceMessage() {
        return sourceMsgDTO;
    }
    
    /**
     * Mutator method to set the source message
     * 
     * @param sourceMsgDTO
     */
    public void setSourceMessage(MsgDTO sourceMsgDTO) {
        this.sourceMsgDTO = sourceMsgDTO;
    }
    /**
     * Accessor method to get the list of ifscMasterHistories message histories
     * 
     * @return list of MsgIFSCMasterHistory object
     */
    public List getIfscMasterHistories() {
        return ifscMasterHistories;
    }

    /**
     * Mutator method to set the ifscMasterHistories
     * 
     * @param list of MsgIFSCMasterHistory object
     */
    public void setIfscMasterHistories(List ifscMasterHistories) {
        this.ifscMasterHistories = ifscMasterHistories;
    }

    /**
     * @return
     */
    public MsgDefnDTO getDefnDTO() {
        
        if (defnDTO == null) defnDTO = new MsgDefnDTO();
        return defnDTO;
    }

    /**
     * @return
     */
    public String getDisplayAmount() {
        return displayAmount;
    }

    /**
     * @return
     */
    public MessageValueObject getMsgVO() {
        
        if (msgVO == null) msgVO = new MessageValueObject(); 
        return msgVO;
    }

    /**
     * @return
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * @param defnDTO
     */
    public void setDefnDTO(MsgDefnDTO defnDTO) {
        this.defnDTO = defnDTO;
    }

    /**
     * @param string
     */
    public void setDisplayAmount(String string) {
        displayAmount = string;
    }

    /**
     * @param object
     */
    public void setMsgVO(MessageValueObject object) {
        msgVO = object;
    }

    /**
     * @param string
     */
    public void setReasonCode(String string) {
        reasonCode = string;
    }

    /**
     * @return
     */
    public MsgHistoryDTO getMsgHistoryDTO() {
        
        if (msgHistoryDTO == null) msgHistoryDTO = new MsgHistoryDTO();
        return msgHistoryDTO;
    }

    /**
     * @param historyDTO
     */
    public void setMsgHistoryDTO(MsgHistoryDTO historyDTO) {
        msgHistoryDTO = historyDTO;
    }

    public Object getField(String fieldID) {

        Object msgFieldDTO = msgFieldsMap.get(fieldID);   
        return msgFieldDTO;
    }

    public Map msgFieldsMap = new ListMap(0);

    public void setMessageFields(List msgFields) {

        for (Iterator i = msgFields.iterator(); i.hasNext(); ) {
            
            MsgFieldDTO field = (MsgFieldDTO)i.next();
            parseMsgFields(field, msgFieldsMap);
       }
    }

    @SuppressWarnings("unchecked")
    protected void parseMsgFields(MsgFieldDTO fieldDTO, Map fieldMap) {
        
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
    }

    /**
     * Method to returns the message field number
     * @param msgFieldDTO
     * @return
     */    
    protected String getFieldNO(MsgFieldDTO msgFieldDTO) {
        
        return msgFieldDTO.fieldTypeDTO.fieldTypeVO.no;
    }

    protected String getFieldValue(MsgFieldDTO msgFieldDTO) {
        
        return msgFieldDTO.msgFieldVO.getValue();
    }
    
    public boolean hasField(String fieldID) {

        return getField(fieldID) != null ? true : false;
    }
    /**
     * @return
     */
    public List getMsgErrors() {
        
        if (msgErrors == null) msgErrors = new ArrayList(1);
        return msgErrors;
    }

    /**
     * @param list
     */
    public void setMsgErrors(List msgErrors) {
        this.msgErrors = msgErrors;
    }

    /**
     * @return
     */
    public List getMsgIds() {
        return msgIds;
    }

    /**
     * @param list
     */
    public void setMsgIds(List list) {
        msgIds = list;
    }

    

    /**
     * @return
     */
    public String getMessageId() {
        
//        if (msgVO == null) msgVO = new MessageValueObject();
//        messageId = String.valueOf(msgVO.getMsgId()); 
        return messageId;
    }

    /**
     * @param string
     */
    public void setMessageId(String string) {
        messageId = string;
    }

    

    public List getAccountNos() {
        return accountNos;
    }

    public List getAmounts() {
        return amounts;
    }

    public void setAccountNos(List list) {
        accountNos = list;
    }

    public void setAmounts(List list) {
        amounts = list;
    }


  public String getChannel() {
    
        return channel;
    }

    
    public void setChannel(String channel) {
    
        this.channel = channel;
    }

    
    public String getHostName() {
    
        return hostName;
    }

    
    public void setHostName(String hostName) {
    
        this.hostName = hostName;
    }

}
