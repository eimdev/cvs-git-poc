/* $Header$ */
/*
 * @(#)IOBIFSCBean.java
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
package com.objectfrontier.insta.rtgs.reports.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.host.rhs.meta.client.vo.HostIFSCDetailsValueObject;
import com.objectfrontier.arch.client.jsp.bean.BeanException;
import com.objectfrontier.arch.resource.ResourceException;
import com.objectfrontier.arch.validator.Validator;
import com.objectfrontier.arch.validator.ValidatorConstants;
import com.objectfrontier.arch.validator.ValidatorFactory;
import com.objectfrontier.insta.reports.server.util.RHSConstants;
import com.objectfrontier.rhs.meta.client.vo.BankMasterValueObject;
import com.objectfrontier.rtgs.client.jsp.util.RHSJSPConstants;
import com.objectfrontier.rtgs.client.jsp.util.RHSMessageConstants;
import com.objectfrontier.rtgs.dto.BankMasterDTO;
import com.objectfrontier.rtgs.dto.HostIFSCDetailsDTO;
import com.objectfrontier.rtgs.dto.IFSCMasterDTO;
import com.objectfrontier.user.dto.UserDTO;


/**
 * This class holds the data of the BranchMaster and implements the create,
 * modify and delete methods.
 * @author niladrib;Aug 6, 2004
 * @date   Aug 6, 2004
 * @since  RHS App 1.0; Aug 6, 2004
 * @see
 */
public class HostIFSCBean extends IFSCBean {
    public static final String LogSourceClass = HostIFSCBean.class.getName();
    public static final String IfscCode = "IFSC Code";
    public static final String BranchName = "Branch Name";
    public static final String EmailId = "Email id";
    protected static Validator[] ifscCodeValidators;
    protected static Validator[] branchNameValidators;
    protected static Validator[] emailValidators;

    static {
        Map props = new HashMap(1);
        props.put(Validator.ValueType, Validator.ValueType_Single);
        props.put(ValidatorConstants.Key_MinLength, new Integer(3));
        props.put(ValidatorConstants.Key_MaxLength, new Integer(250));
        props.put(ValidatorConstants.Key_SpecialChar, "_ ");

        ifscCodeValidators = new Validator[3];
        ifscCodeValidators[0] = ValidatorFactory.getValidator(Validator.Validator_Name,
                props);
        ifscCodeValidators[1] = ValidatorFactory.getValidator(Validator.Validator_MinLength,
                props);
        ifscCodeValidators[2] = ValidatorFactory.getValidator(Validator.Validator_MaxLength,
                props);

        props = new HashMap(1);
        props.put(Validator.ValueType, Validator.ValueType_Single);
        props.put(ValidatorConstants.Key_MinLength, new Integer(3));
        props.put(ValidatorConstants.Key_MaxLength, new Integer(250));
        //  -,.() are included as the branch names come from PIDB may contain these spl. chars.
        props.put(ValidatorConstants.Key_SpecialChar, "_-,.() ");

        branchNameValidators = new Validator[3];
        branchNameValidators[0] = ValidatorFactory.getValidator(Validator.Validator_Name,
                props);
        branchNameValidators[1] = ValidatorFactory.getValidator(Validator.Validator_MinLength,
                props);
        branchNameValidators[2] = ValidatorFactory.getValidator(Validator.Validator_MaxLength,
                props);
                
        props = new HashMap(1);        
        props.put(Validator.ValueType, Validator.ValueType_Single);
        emailValidators = new Validator[1];
        emailValidators[0] = ValidatorFactory.getValidator(Validator.Validator_Email, props);
                
    }

    public long id;
    public String dbConnectionURL;
    public String dbDriverName;
    public String dbSchemaName;
    public String dbUserName;
    public String dbPassword;
    public String ipAddress;
    public String branchCode;
    public String branchType;
    public long port;
    public String emailId;
    public List iobIFSCDetailsList;
    public List bankNames = new ArrayList();
    public String displayBranchName;
    public long selectedBankID;
    public String queryParam;
    public String queryValue;
    public String[] branchTypeNameList = RHSConstants.BranchTypeNames; 
    // public List states;
       
    /**
     * @throws ResourceException
     */
    public HostIFSCBean() throws ResourceException {
        super();
    }

    /**
     *
     * @return
     */
    public HostIFSCBean(HostIFSCDetailsDTO iobIFSCMasterDTO)
        throws ResourceException {
        this.setAttribute(iobIFSCMasterDTO);
    }

    /**
     * @return Returns the dbSchemaName.
     */
    public String getDbSchemaName() {
        return dbSchemaName;
    }

    /**
     * @param dbSchemaName The dbSchemaName to set.
     */
    public void setDbSchemaName(String dbSchemaName) {
        this.dbSchemaName = dbSchemaName;
    }

    /**
     * @return Returns the dbUserName.
     */
    public String getDbUserName() {
        return dbUserName;
    }

    /**
     * @param dbUserName The dbUserName to set.
     */
    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Returns the ifsc.
     */
    public String getIfsc() {
        return ifsc;
    }

    /**
     * @param ifsc The ifsc to set.
     */
    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    /**
     * @return Returns the ipAddress.
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress The ipAddress to set.
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return Returns the dbConnectionURL.
     */
    public String getDbConnectionURL() {
        return dbConnectionURL;
    }

    /**
     * @param dbConnectionURL The dbConnectionURL to set.
     */
    public void setDbConnectionURL(String dbConnectionURL) {
        this.dbConnectionURL = dbConnectionURL;
    }

    /**
     * @return Returns the dbDriverName.
     */
    public String getDbDriverName() {
        return dbDriverName;
    }

    /**
     * @param dbDriverName The dbDriverName to set.
     */
    public void setDbDriverName(String dbDriverName) {
        this.dbDriverName = dbDriverName;
    }

    /**
     * @return Returns the dbPassword.
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * @param dbPassword The dbPassword to set.
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    /**
     * @return Returns the iobIFSCDetailsList.
     */
    public List getIobIFSCDetailsList() {
        return iobIFSCDetailsList;
    }

    /**
     * @param iobIFSCDetailsList The iobIFSCDetailsList to set.
     */
    public void setIobIFSCDetailsList(List iobIFSCDetailsList) {
        this.iobIFSCDetailsList = iobIFSCDetailsList;
    }

    /**
     * @return Returns the bankCode.
     */
    public List getBankNames() {
        return bankNames;
    }

    /**
     * @param bankCode The bankCode to set.
     */
    public void setBankNames(List bankNames) {
        this.bankNames = bankNames;
    }

    /**
     * @return Returns the email.
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * @param email The email to set.
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * @return Returns the port.
     */
    public long getPort() {
        return port;
    }

    /**
     * @param port The port to set.
     */
    public void setPort(long port) {
        this.port = port;
    }

    public void modify(HostIFSCBean bean) {
        this.setAttributes(bean);
    }

    public void setAttributes(HostIFSCBean ifscBean) {
        this.setId(ifscBean.getId());
        this.setIfsc(ifscBean.getIfsc());
        this.setName(ifscBean.getName());
        this.setBankCode(ifscBean.getBankCode());
        this.setBankName(ifscBean.getBankName());
        this.setBranchCode(ifscBean.getBranchCode());
        this.setBranchType(ifscBean.getBranchType());
        this.setAddress(ifscBean.getAddress());
        this.setCity(ifscBean.getCity());
        this.setState(ifscBean.getState());
        this.setPincode(ifscBean.getPincode());
        this.setDbConnectionURL(ifscBean.getDbConnectionURL());
        this.setDbDriverName(ifscBean.getDbDriverName());
        this.setDbSchemaName(ifscBean.getDbSchemaName());
        this.setDbUserName(ifscBean.getDbUserName());
        this.setDbPassword(ifscBean.getDbPassword());
        this.setIpAddress(ifscBean.getIpAddress());
        this.setEmailId(ifscBean.getEmailId());
    }

    /**
     * @throws ResourceException
     *
     */
    public void setAttribute(HostIFSCDetailsDTO iobIFSCDetailsDTO)
        throws ResourceException {
        
        super.setAttriute(iobIFSCDetailsDTO);

        HostIFSCDetailsValueObject iobIFSCDetailsVO = (HostIFSCDetailsValueObject) iobIFSCDetailsDTO.ifscMasterVO;

        this.setDisplayBranchName(iobIFSCDetailsVO.getIfsc() + "-" + iobIFSCDetailsVO.getName());
        this.setBranchCode(iobIFSCDetailsVO.getBranchCode());
        this.setBranchType(iobIFSCDetailsVO.getBranchType());
        this.setDbConnectionURL(iobIFSCDetailsVO.getDbConnectionURL());
        this.setDbDriverName(iobIFSCDetailsVO.getDbDriverName());
        this.setDbSchemaName(iobIFSCDetailsVO.getDbSchemaName());
        this.setDbUserName(iobIFSCDetailsVO.getDbUserName());
        this.setDbPassword(iobIFSCDetailsVO.getDbPassword());
        this.setIpAddress(iobIFSCDetailsVO.getIpAddress());
        this.setEmailId(iobIFSCDetailsVO.getEmailId());
    }

    /**
     *
     */
    public IFSCMasterDTO getAttribute() {
        
        UserDTO userDTO = null;
        try {
            userDTO = getCurrentUser();
        } catch (BeanException e) {
            e.printStackTrace(System.err);
        }
        
        HostIFSCDetailsDTO iobIFSCDetailsDTO = new HostIFSCDetailsDTO();
        HostIFSCDetailsValueObject iobIFSCDetailsVO = new HostIFSCDetailsValueObject();

        iobIFSCDetailsVO = (HostIFSCDetailsValueObject) super.getAttribute(iobIFSCDetailsVO);

        iobIFSCDetailsVO.setBranchCode(this.branchCode);
        iobIFSCDetailsVO.setBranchType(this.branchType);
        iobIFSCDetailsVO.setDbConnectionURL(this.dbConnectionURL);
        iobIFSCDetailsVO.setDbDriverName(this.dbDriverName);
        iobIFSCDetailsVO.setDbSchemaName(this.dbSchemaName);
        iobIFSCDetailsVO.setDbUserName(this.dbUserName);
        iobIFSCDetailsVO.setDbPassword(this.dbPassword);
        iobIFSCDetailsVO.setIpAddress(this.ipAddress);
        iobIFSCDetailsVO.setEmailId(this.emailId);

        iobIFSCDetailsDTO.ifscMasterVO = iobIFSCDetailsVO;

        BankMasterDTO bankDTO = new BankMasterDTO();
        bankDTO.bankMasterVO = new BankMasterValueObject();
        if(getBankCode() != null){
            bankDTO.bankMasterVO.setId(Long.parseLong(getBankCode()));
            bankDTO.bankMasterVO.setName(this.getBankName());
        }
        
        //bankDTO.bankMasterVO.setCode(getBankCode());
        iobIFSCDetailsDTO.bankMasterDTO = bankDTO;
        iobIFSCDetailsDTO.bankMasterDTO.userDTO = userDTO;
        
        return iobIFSCDetailsDTO;
    }
/*
    *//**
     * This method is used to getthe list of the Branch Master
     * @see
     *
     *//*
    public List getBranchMasterList() throws CrudException {
        try {
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
            HttpSession session = request.getSession();

            MessageService messageService = getMessageServerName(session.getId(),
                    true);

            List listHostIFSCDetails = messageService.listHostIFSCDetails();

            if (listHostIFSCDetails != null) {
                ifscList = new ArrayList(listHostIFSCDetails.size());

                for (Iterator iter = listHostIFSCDetails.iterator();
                        iter.hasNext();) {
                    HostIFSCDetailsDTO ifscDetailsDTO = (HostIFSCDetailsDTO) iter.next();
                    HostIFSCBean iobIfscBean = new HostIFSCBean(ifscDetailsDTO);
                    ifscList.add(iobIfscBean);
                }

                setIobIFSCDetailsList(ifscList);
            }
        } catch (Exception e) {
            throw new CrudException(e);
        }

        return ifscList;
    }
    
    *//**
         * This method is used to getthe list of the Branch Master
         * @see
         *
         *//*
        public List getSelectedBranches() throws CrudException {
            try {
                HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
                HttpSession session = request.getSession();

                MessageService messageService = getMessageServerName(session.getId(),
                        true);

                List listHostIFSCDetails = messageService.listHostIFSCDetails(queryParam, queryValue);

                if (listHostIFSCDetails != null) {
                    ifscList = new ArrayList(listHostIFSCDetails.size());

                    for (Iterator iter = listHostIFSCDetails.iterator();
                            iter.hasNext();) {
                        HostIFSCDetailsDTO ifscDetailsDTO = (HostIFSCDetailsDTO) iter.next();
                        HostIFSCBean iobIfscBean = new HostIFSCBean(ifscDetailsDTO);
                        ifscList.add(iobIfscBean);
                    }

                    setIobIFSCDetailsList(ifscList);
                }
            } catch (Exception e) {
                throw new CrudException(e);
            }

            return ifscList;
        }
    *//**
         * This method is used to getthe list of the Branch Master
         * @see
         *
         *//*
        public List getBranchMasterList(String queryParam,String queryValue) throws CrudException {
            try {
                HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
                HttpSession session = request.getSession();

                MessageService messageService = getMessageServerName(session.getId(),
                        true);

                List listHostIFSCDetails = messageService.listHostIFSCDetails(queryParam,queryValue);

                if (listHostIFSCDetails != null) {
                    ifscList = new ArrayList(listHostIFSCDetails.size());

                    for (Iterator iter = listHostIFSCDetails.iterator();
                            iter.hasNext();) {
                        HostIFSCDetailsDTO ifscDetailsDTO = (HostIFSCDetailsDTO) iter.next();
                        HostIFSCBean iobIfscBean = new HostIFSCBean(ifscDetailsDTO);
                        ifscList.add(iobIfscBean);
                    }

                    setIobIFSCDetailsList(ifscList);
                }
            } catch (Exception e) {
                throw new CrudException(e);
            }

            return ifscList;
        }


    *//**
     * This method is for to store the value in the DataBase
     * @param request
     * @param HostIFSCDetailsDTO
     *//*
    public void create(HttpServletRequest request,
        HostIFSCDetailsDTO iobIFSCDetailsDTO) {
        String LogSourceMethod = "create(request, iobIFSCDetailsDTO)";

        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(LogSourceClass, LogSourceMethod);
        }

        try {
            iobIFSCDetailsDTO = (HostIFSCDetailsDTO) getAttribute();
            getMessageServer().createHostIFSCDetails(iobIFSCDetailsDTO);
        } finally {
            if (logger.isLoggable(Level.FINEST)) {
                logger.exiting(LogSourceClass, LogSourceMethod);
            }
        }
    }

    public void modify() {
        String LogSourceMethod = "modify()";

        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(LogSourceClass, LogSourceMethod);
        }

        try {
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
            HttpSession session = request.getSession();

            HostIFSCDetailsDTO ifscDetailsDTO = (HostIFSCDetailsDTO) getAttribute();

            if (logger.isLoggable(Level.FINEST)) {
                logger.finest(LogSourceClass, LogSourceMethod,
                    "ifscDetailsDTO.ifscMasterVO.getId()" +
                    ifscDetailsDTO.ifscMasterVO.getId());
            }

            MessageService service = getMessageServerName(session.getId(), true);
            service.modifyHostIFSCDetails(ifscDetailsDTO);
        } catch (Exception e) {
            logger.severe(LogSourceClass, LogSourceMethod, e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (logger.isLoggable(Level.FINEST)) {
                logger.exiting(LogSourceClass, LogSourceMethod);
            }
        }
    }

    public void delete() {
        String LogSourceMethod = "delete()";

        try {
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
            HttpSession session = request.getSession();

            HostIFSCDetailsDTO ifscDetailsDTO = (HostIFSCDetailsDTO) getAttribute();
            MessageService service = getMessageServerName(session.getId(), true);
            service.deleteHostIFSCDetails(ifscDetailsDTO);
        } catch (Exception e) {
            logger.severe(LogSourceClass, LogSourceMethod, e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (logger.isLoggable(Level.FINEST)) {
                logger.exiting(LogSourceClass, LogSourceMethod);
            }
        }
    }

    *//**
     * Method to get all branch master details
     *
     *//*
    public void load(HostIFSCDetailsDTO iobIFSCDetailsDTO) {
        final String LogSourceMethod = "load(iobIFSCDetailsDTO)";

        if (isFinestLoggable) {
            logger.entering(LogSourceClass, LogSourceMethod);
        }

        try {
            iobIFSCDetailsDTO = getMessageServer().getHostIFSCDetails(iobIFSCDetailsDTO);
            setAttribute(iobIFSCDetailsDTO);
        } catch (Throwable throwable) {
            logger.severe(LogSourceClass, LogSourceMethod, throwable);

            try {
                ServerException se = (ServerException) getExceptionHandler()
                                                           .getException(Invariants.MSG_RHS_UNABLETOCREATE);
                throw se;
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new RuntimeException(throwable.getMessage());
            }
        } finally {
            if (isFinestLoggable) {
                logger.exiting(LogSourceClass, LogSourceMethod);
            }
        }
    }

    public void populate() throws ResourceException {
        this.setBankNames((new BankBean()).getOtherBankMasterList());
    }

    public void getIFSCListForBank() {
        
        final String LogSourceMethod = "getIFSCListForBank()";
        if (isFinestLoggable) {
            logger.entering(LogSourceClass, LogSourceMethod);
        }
                
        try {
            List branches = getMessageServer().getIFSCMasters( this.selectedBankID, queryParam, queryValue);

            if (branches != null) {
                ifscList = new ArrayList(branches.size());

                for (Iterator iter = branches.iterator(); iter.hasNext();) {
                    
                    IFSCMasterDTO masterDTO = (IFSCMasterDTO) iter.next();
                    IFSCBean bean = new IFSCBean(masterDTO);
                    ifscList.add(bean);
                }

                setIfscList(ifscList);
            }
            
            
        } catch (Throwable throwable) {
            logger.severe(LogSourceClass, LogSourceMethod, throwable);

            try {
                ServerException se = (ServerException) getExceptionHandler()
                                                           .getException(Invariants.MSG_RHS_UNABLETOFIND);
                throw se;
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new RuntimeException(throwable.getMessage());
            }
        } finally {
            if (isFinestLoggable)
                logger.exiting(LogSourceClass, LogSourceMethod);
        }
    }*/

    public ActionErrors validate() {
        final String LogSourceMethod = "validate()";
        ActionErrors errors = new ActionErrors();

        try {
            if ((getIfsc() == null) || (getIfsc().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.IFSC_CODE,
                        this.getIfsc()).getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            } else if (getIfsc().trim().length() != 11) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.IFSC_CODE_LENGTH,
                        this.getIfsc()).getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            } else {
                if (ifsc != null) {
                    for (int i = 0; i < ifscCodeValidators.length; i++) {
                        Validator validator = ifscCodeValidators[i];

                        if (!validator.validate(ifsc)) {
                            prepareErrors(validator.getErrorMessages(), errors,
                                ifsc, IfscCode);
                        }
                    }
                }
            }

            // Commented because only IOB bank related are to be entered here
            //            if (getBankCode() == null || getBankCode().trim().length() == 0) {
            //                
            //                 errorMessage = getMessageFactory().getMessage(
            //                 RHSMessageConstants.BANK_NAME, this.getBankCode()).getShortDescription();
            //     
            //                 errors.add(ActionErrors.GLOBAL_ERROR,
            //                 new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY, errorMessage));
            //            }
            if ((getName() == null) || (getName().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.BRANCH_NAME,
                        this.getName()).getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            } else {
                if (name != null) {
                    for (int i = 0; i < branchNameValidators.length; i++) {
                        Validator validator = branchNameValidators[i];

                        if (!validator.validate(name)) {
                            prepareErrors(validator.getErrorMessages(), errors,
                                name, BranchName);
                        }
                    }
                }
            }
            
            if ((getBranchCode() == null) ||
                    (getBranchCode().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.BRANCH_CODE,
                        this.getBranchCode()).getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            }
            
            if ((getBranchType() == null) ||
                    (getBranchType().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.BRANCH_TYPE,
                        this.getBranchType()).getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            }
            
            if ((getDbConnectionURL() == null) ||
                    (getDbConnectionURL().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.DB_DETAILS,
                        "DB Connection URL").getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            }

            if ((getDbDriverName() == null) ||
                    (getDbDriverName().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.DB_DETAILS,
                        "DB Driver Name").getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            }

            if ((getDbSchemaName() == null) ||
                    (getDbSchemaName().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.DB_DETAILS,
                        "DB Schema Name").getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            }

            if ((getDbUserName() == null) ||
                    (getDbUserName().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.DB_DETAILS,
                        "DB User Name").getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            }

            if ((getDbPassword() == null) ||
                    (getDbPassword().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.DB_DETAILS,
                        "DB Password").getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            }

            if ((getIpAddress() == null) ||
                    (getIpAddress().trim().length() == 0)) {
                errorMessage = getMessageFactory()
                                   .getMessage(RHSMessageConstants.DB_DETAILS,
                        "IP Address").getShortDescription();

                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                        errorMessage));
            }
              
            if ((getEmailId() == null) ||
                    (getEmailId().trim().length() == 0)) {
                 errorMessage = getMessageFactory().getMessage(RHSMessageConstants.DB_DETAILS,
                          "Email Id").getShortDescription();
                  errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                            errorMessage));               
            } else {
                if (emailId != null) {
                    for (int i = 0; i < emailValidators.length; i++) {
                        Validator validator = emailValidators[i];
                        if (!validator.validate(emailId)) {
                            prepareErrors(validator.getErrorMessages(), errors,
                            emailId, EmailId);
                        }
                    }
                }    
            }
        } catch (ResourceException e) {
            logger.severe(LogSourceClass, LogSourceMethod, e);
            throw new RuntimeException(e);
        }

        return errors;
    }

   /* public void initializationBean() throws ResourceException {
        //Bank Code is defaultly set to host bankcode "120", which is IOB in this case. 
//        this.setBankCode(RHSJSPConstants.IOBBANKCODE);
//        this.setBranchTypeNameList(RHSConstants.BranchTypeNames);
       // this.setStates(new StateBean().getAllStates());

       //Bank Code is defaultly set to host bankcode "120", which is IOB in this case.
       //RBC
       String bankCode = "";
       if(this.ifsc.trim().length() >= 4 )
           bankCode = this.ifsc.substring(0,4);
       BankMasterDTO bankMasterDTO = getMessageServer().getBankMaster(bankCode);

       if(bankMasterDTO != null){
           this.setBankCode(String.valueOf(bankMasterDTO.bankMasterVO.getId()));
       }else {
           //Get the HOSTBANK ID from the JSP Env Properties
           String hostBankId = environment.getPropertyAsString(RHSJSPConstants.HOSTBANKID);
           if(hostBankId != null && hostBankId.trim().length() > 0 )
               this.setBankCode(hostBankId);
       }

       this.setBranchTypeNameList(RHSConstants.BranchTypeNames);
      // this.setStates(new StateBean().getAllStates());

    }*/

    public void reset() {
        super.reset();
        this.branchCode = "";
        this.branchType = "";
        this.dbConnectionURL = "";
        this.dbDriverName = "";
        this.dbPassword = "";
        this.dbSchemaName = "";
        this.dbUserName = "";
        this.ipAddress = "";
        this.emailId = "";
        this.branchType = "";
        this.bankNames = null;
        this.ifscList = null;
        this.selectedBankID = 0;
        this.queryParam = "";
        this.queryValue = "";
    }

    /**
     * @return
     */
    public String getDisplayBranchName() {
        return displayBranchName;
    }

    /**
     * @param string
     */
    public void setDisplayBranchName(String string) {
        displayBranchName = string;
    }

    /**
     * Accessor method to get branchCode
     * @return branchCode : String
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Mutator method to set branchCode
     * @param value The value to set.
     */
    public void setBranchCode(String string) {
        branchCode = string;
    }
    /**
     * @return
     */
    public String getBranchType() {
        return branchType;
    }

    /**
     * @param string
     */
    public void setBranchType(String branchType) {
        this.branchType = branchType;
    }
    /**
     * @return
     */
    public String[] getBranchTypeNameList() {
        
        return RHSConstants.BranchTypeNames;
    }
    /**
     * @return
     */
    public long getSelectedBankID() {
        return selectedBankID;
    }

    /**
     * @param l
     */
    public void setSelectedBankID(long l) {
        selectedBankID = l;
    }

    /**
     * @param strings
     */
    public void setBranchTypeNameList(String[] strings) {
        branchTypeNameList = strings;
    }

    /**
     * @return
   
    public List getStates() {
        
        if (states == null) states = new ArrayList();
        return states;
    }
    */
    
    /**
     * @param list
     
    public void setStates(List list) {
        states = list;
    }
*/
    /**
     * @return
     */
    public String getQueryParam() {
        return queryParam;
    }

    /**
     * @return
     */
    public String getQueryValue() {
        return queryValue;
    }

    /**
     * @param string
     */
    public void setQueryParam(String string) {
        queryParam = string;
    }

    /**
     * @param string
     */
    public void setQueryValue(String string) {
        queryValue = string;
    }

} 
