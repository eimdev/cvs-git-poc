/* $Header$ */

/*
 * @(#)IFSCMaster.java
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
import java.util.List;

import com.objectfrontier.arch.service.logging.Level;

import com.objectfrontier.arch.resource.ResourceException;

import com.objectfrontier.rhs.meta.client.vo.IFSCMasterValueObject;
import com.objectfrontier.rtgs.dto.HostIFSCDetailsDTO;
import com.objectfrontier.rtgs.dto.IFSCMasterDTO;

/**
 * @author niladrib;Aug 6, 2004
 * @date   Aug 6, 2004
 * @since  RHS App 1.0; Aug 6, 2004
 * @see    
 */
public class IFSCBean 
extends AbstractMsgDefnBean {
    
    public static final String LogSourceClass = IFSCBean.class.getName();

    public long id;
    
    public String ifsc;
    
    public String name;
    
    public String city;
    
    public String state;
    
    public String address;
    
    public String pincode;
    
    public String bankCode;
    
    public String bankName;
    
    public List ifscList = new ArrayList(5);
    
    public String mode;
    
    /**
     * The Constructor of the IFSCBean
     * @throws ResourceException
     */
    public IFSCBean()
    throws ResourceException {
        super();
       
    }
    
    /**
     * This Consrtictor with parameter as DTO  
     * @return
     */
    public IFSCBean(IFSCMasterDTO ifscMasterDTO)
    throws ResourceException{
        setAttriute(ifscMasterDTO);
    }
    
    public IFSCBean(HostIFSCDetailsDTO iobIfscDetailsDTO)
    throws ResourceException{
        setAttriute(iobIfscDetailsDTO);
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
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return Returns the pincode.
     */
    public String getPincode() {
        return pincode;
    }
    
    /**
     * @param pincode The pincode to set.
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
    
    /**
     * @return Returns the state.
     */
    public String getState() {
        return state;
    }
    
    /**
     * @param state The state to set.
     */
    public void setState(String state) {
        this.state = state;
    }
    
    /**
     * @return Returns the address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return Returns the city.
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return Returns the bankCode.
     */
    public String getBankCode() {
        return bankCode;
    }
    
    /**
     * @param bankCode The bankCode to set.
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    /**
     * @return Returns the ifscList.
     */
    public List getIfscList() {
        return ifscList;
    }
    
    /**
     * @param ifscList The ifscList to set.
     */
    public void setIfscList(List ifscList) {
        this.ifscList = ifscList;
    }
    
    /**
     * @return
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param string
     */
    public void setMode(String string) {
        mode = string;
    }
    
    /**
     * @return Returns the bankName.
     */
    public String getBankName() {
        return bankName;
    }
    
    /**
     * @param bankName The bankName to set.
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    
    /**
     * The setAttribute method which is used to set the value 
     * @throws ResourceException
     *
     */
    public void setAttriute(IFSCMasterDTO ifscMasterDTO) 
    throws ResourceException{
        final String LogSourceMethod = "setAttriute(ifscMasterDTO)";
        
        this.setId(ifscMasterDTO.ifscMasterVO.getId());
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest(LogSourceClass, LogSourceMethod, "Param>>>>>>>>" +ifscMasterDTO.ifscMasterVO.getId());
        }
        this.setIfsc(ifscMasterDTO.ifscMasterVO.getIfsc());
        this.setName(ifscMasterDTO.ifscMasterVO.getName());
        this.setCity(ifscMasterDTO.ifscMasterVO.getCity());
        this.setState(ifscMasterDTO.ifscMasterVO.getState());
        this.setAddress(ifscMasterDTO.ifscMasterVO.getAddress());
        this.setPincode(ifscMasterDTO.ifscMasterVO.getPincode());
        
        if (ifscMasterDTO.bankMasterDTO != null)
            this.setBankCode(String.valueOf(ifscMasterDTO.bankMasterDTO.bankMasterVO.getId()));
            this.setBankName(ifscMasterDTO.bankMasterDTO.bankMasterVO.getName());
            //this.setBankCode(ifscMasterDTO.bankMasterDTO.bankMasterVO.getName());
        
    }
    
    
    public void setAttriute(HostIFSCDetailsDTO iobIfscDetailsDTO) 
    throws ResourceException{
        final String LogSourceMethod = "setAttriute(iobIfscDetailsDTO)";
    
        this.setId(iobIfscDetailsDTO.ifscMasterVO.getId());
        if (logger.isLoggable(Level.FINEST)) {
           logger.finest(LogSourceClass, LogSourceMethod, "Param>>>>>>>>" +iobIfscDetailsDTO.ifscMasterVO.getId());
        }
        this.setIfsc(iobIfscDetailsDTO.ifscMasterVO.getIfsc());
        this.setName(iobIfscDetailsDTO.ifscMasterVO.getName());
        this.setCity(iobIfscDetailsDTO.ifscMasterVO.getCity());
        this.setState(iobIfscDetailsDTO.ifscMasterVO.getState());
        this.setAddress(iobIfscDetailsDTO.ifscMasterVO.getAddress());
        this.setPincode(iobIfscDetailsDTO.ifscMasterVO.getPincode());
    
        if (iobIfscDetailsDTO.bankMasterDTO != null) {
            this.setBankCode(String.valueOf(iobIfscDetailsDTO.bankMasterDTO.bankMasterVO.getId()));
            this.setBankName(iobIfscDetailsDTO.bankMasterDTO.bankMasterVO.getName());
            //this.setBankCode(ifscMasterDTO.bankMasterDTO.bankMasterVO.getName());
        }
    }
    
    /**
     *The getAttribute method which is used to get the value 
     *
     */
    public IFSCMasterDTO getAttribute() {
        
        IFSCMasterDTO ifscMasterDTO = new IFSCMasterDTO();
        ifscMasterDTO.ifscMasterVO = getAttribute(new IFSCMasterValueObject());
        
        
        return ifscMasterDTO;
    }
    
    public IFSCMasterValueObject getAttribute(IFSCMasterValueObject ifscMasterVO) {
        
        final String LogSourceMethod = "getAttribute(IFSCMasterValueObject)";
        
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest(LogSourceClass, LogSourceMethod, "id<<<<<<<<<" +getId());
            logger.finest(LogSourceClass, LogSourceMethod, "ifsc<<<<<" +getIfsc());
        }
        
        ifscMasterVO.setId(this.getId());
        ifscMasterVO.setIfsc(this.getIfsc());
        ifscMasterVO.setName(this.getName());
        ifscMasterVO.setCity(this.getCity());
        ifscMasterVO.setState(this.getState());
        ifscMasterVO.setAddress(this.getAddress());
        ifscMasterVO.setPincode(this.getPincode());
        
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest(LogSourceClass, LogSourceMethod, "id>>>>>>>>" +ifscMasterVO.getId());
            logger.finest(LogSourceClass, LogSourceMethod, "ifsc>>>>>>>>" +ifscMasterVO.getIfsc());
        }
       
        
        return ifscMasterVO;
    }
    
    public void reset() {
        
        this.ifsc = "";
        this.name  = "";
        this.bankCode = "" ;
        this.address = "";
        this.city = "";
        this.state = "";
        this.pincode = "";
    }
    
    /**
     * This method is used to getthe list of the Branch Master
     * @see
     *
     *
    
    public void getBranchMasterList() 
    throws CrudException {

        try {
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
            HttpSession session = request.getSession();
            
            MessageService messageService = getMessageServerName(session.getId(), true);
            
            List listIFSCMasters = messageService.listIFSCMasters();
            
            if (listIFSCMasters != null) {
                ifscList = new ArrayList(listIFSCMasters.size());
                for (Iterator iter = listIFSCMasters.iterator(); iter.hasNext();) {
                    IFSCMasterDTO ifscMasterDTO = (IFSCMasterDTO) iter.next();
                    ifscList.add(new IFSCBean(ifscMasterDTO));
                }
               this.setIfscList(ifscList);
            }
        } catch (Exception e) {
            throw new CrudException(e);
        }
    } */


}

