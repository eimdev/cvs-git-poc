/* $Header$ */

/*
 * @(#)ReportBean.java
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
package com.objectfrontier.insta.rtgs.reports.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.objectfrontier.arch.client.jsp.bean.BeanException;
import com.objectfrontier.arch.context.RequestContext;
import com.objectfrontier.arch.dto.Message;
import com.objectfrontier.arch.resource.ResourceException;
import com.objectfrontier.arch.server.ServerException;
import com.objectfrontier.crud.CrudException;
import com.objectfrontier.insta.InstaDefaultConstants;
import com.objectfrontier.insta.client.InstaClientULC;
import com.objectfrontier.rtgs.client.jsp.util.ConversionUtils;
import com.objectfrontier.rtgs.client.jsp.util.RHSJSPConstants;
import com.objectfrontier.rtgs.dto.HostIFSCDetailsDTO;
import com.objectfrontier.rtgs.dto.IFSCMasterDTO;
import com.objectfrontier.rtgs.report.dto.DetailedReportDTO;
import com.objectfrontier.rtgs.report.dto.ReportDTO;
import com.objectfrontier.user.ws.UserManagement;
import com.objectfrontier.user.ws.dto.WSUserDTO;
import com.objectfrontier.user.ws.impl.UserManagementImpl;
import com.objectfrontier.user.ws.vo.WSUserValueObject;

/**
 * @author Karthick GRP
 * @date   Dec 10, 2004
 * @since  RHS IOB 1.0; Dec 10, 2004
 */
public class ReportBean
extends AbstractMsgDefnBean {

    public static final String LogSourceClass = ReportBean.class.getName();

    public static SimpleDateFormat timeFmt = new SimpleDateFormat("dd-MM-yyyy");

    public static final String FROMDATE = "FromDate";
    public static final String TODATE = "ToDate";

    public static String title;

    protected ReportDTO reportDTO;

    protected DetailedReportDTO detailedReportDTO;

    protected String fromDate;

    protected String toDate;

    protected String branchName;

    protected String bankId;

    protected String bankName;

    protected String txnType;

    protected String reportWise;

    protected String counterPartyTitle;
    
    protected String counterpartyReports;

    public List branches;//RBC CMD 1.0
    
    protected Map datesMap;
    
    protected Map ifscMap;

    protected List alertDTOList;

    public List userIdList;

    public List bankList;

    public List SOLList = new ArrayList(1);

    public String SOLCode;
//    protected String printString;

    public String tranType;
    
    public String branchType;
    
    public List tranTypeList = new ArrayList(0);
    
    public Map subTypeMap = new HashMap(1); //RBC CMD 1.0
    
    public String utrNumber;//RBC CMD 1.0
    
    public String utrWise;//RBC CMD 1.0
    
    public String currentMode;
    
    public List branchDTOs;
    
//    public double netAmount = 0.0;
    public BigDecimal netAmount = BigDecimal.ZERO;
    
    public int totalTxns;
    
    public Map prop = null;
    
//    public double creditAmt = 0.0;
//    public double debitAmt = 0.0;
//    public double inwCprTotal = 0.0;
//    public double inwIprTotal = 0.0;
//    public double outCprTotal = 0.0;
//    public double outIprTotal = 0.0;
//    public double liquidityamt = 0.0;
//    public double debitTotal = 0.0;
//    public double creditTotal = 0.0;
    
    public String creditAmt = "0";
    public String debitAmt = "0";
    public String inwCprTotal = "0";
    public String inwIprTotal = "0";
    public String outCprTotal = "0";
    public String outIprTotal = "0";
    public String liquidityamt = "0";
    public String debitTotal = "0";
    public String creditTotal = "0";
     
    
    /**
     * @throws ResourceException
     */
    @SuppressWarnings("unchecked")
    public ReportBean(){
        
//        this.tranTypeList = Arrays.asList(RHSJSPConstants.TRAN_TYPES);
        this.tranTypeList.add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
        this.tranTypeList.add(RHSJSPConstants.INWARD_TRANSACTION_TYPE);
        this.tranTypeList.add(RHSJSPConstants.BOTH_TRANSACTION_TYPE);
    }

    /**
         * @return Returns the codeWordList.
         */
        public List getUserIdList() {
        if (userIdList == null) userIdList = new ArrayList(0);
            return userIdList;
        }

        /**
         * The codeWordList to set.
         * @param codeWordList
         */
        public void setUserIdList(List userIdList) {
            this.userIdList   = userIdList;
        }

   /* public void loadHostIFSCMaster(){

        final String LogSourceMethod = "loadHostIFSCMaster()";
        
        try {
            
            List listIOBIFSCDetails = getMessageServer().listHostIFSCDetails();
            
            if (listIOBIFSCDetails != null) {
                branches = new ArrayList(listIOBIFSCDetails.size());

                for (Iterator iter = listIOBIFSCDetails.iterator(); iter.hasNext();) {
                    HostIFSCDetailsDTO ifscDetailsDTO = (HostIFSCDetailsDTO) iter.next();

                    if (RHSJSPConstants.ALL.equalsIgnoreCase(this.branchType)) {
                        branches.add(new HostIFSCBean(ifscDetailsDTO));                     
                        
                    } else if(RHSJSPConstants.BRANCH_CORE.equalsIgnoreCase(this.branchType)) {
                        
                        if(getMessageServer().isCoreIFSC(ifscDetailsDTO.ifscMasterVO.getIfsc())) {
                            branches.add(new HostIFSCBean(ifscDetailsDTO));                            
                        }
                    } else if(RHSJSPConstants.BRANCH_TREASUARY.equalsIgnoreCase(this.branchType)) {
                        
                        if(!(getMessageServer().isCoreIFSC(ifscDetailsDTO.ifscMasterVO.getIfsc()))) {
                            branches.add(new HostIFSCBean(ifscDetailsDTO));                            
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.severe(LogSourceClass, LogSourceMethod, e);
            throw new BeanException(e.getMessage());
        } finally {
           
                logger.finest(LogSourceClass, LogSourceMethod, ">>End");
        }
    }*/
    
    /**
     * RBC CMD 1.0
     * Method to load IFSC details
     * @throws BeanException
     */
    @SuppressWarnings("unchecked")
    public void loadIFSCMaster()
    throws BeanException {

        final String LogSourceMethod = "loadIFSCMaster()";
        
    
        try {
        
            List<IFSCMasterDTO> ifscMasterDTOlist = null;
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
            String sessionId = request.getSession().getId();
            
            Message req = createMessage(sessionId, 400, 1, null);
            Message res = handle(sessionId, req);
            ifscMasterDTOlist = (List) res.info;
            
            if (ifscMasterDTOlist != null) {
                
                branches = new ArrayList(ifscMasterDTOlist.size());

                for (Iterator iter = ifscMasterDTOlist.iterator(); iter.hasNext();) {
                    HostIFSCDetailsDTO ifscDetailsDTO = (HostIFSCDetailsDTO) iter.next();
                    branches.add(new HostIFSCBean(ifscDetailsDTO));                            
                }
            }
        } catch (Exception e) {
            logger.severe(LogSourceClass, LogSourceMethod, e);
            throw new BeanException(e.getMessage());
        } finally {
           
        }
    }

    public IFSCMasterDTO getIfscDetails(String ifsc)
    throws BeanException {
        
        IFSCMasterDTO ifscMasterDTO = null;
        HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
        String sessionId = request.getSession().getId();
        
        Message req = createMessage(sessionId, 400, 2, ifsc);
        Message res = handle(sessionId, req);
        ifscMasterDTO = (IFSCMasterDTO)res.info;
        
        return ifscMasterDTO;
    }

    /**
     * This method is used to inactive the list of expired users
     * @param currentBusinessDate
     *@throws CrudException
     */
    @SuppressWarnings("unchecked")
    public void getUserIdByLocation()
    throws  BeanException {

        final String LogSourceMethod = "getUserIdByLocation()";

       {
            logger.entering(LogSourceClass, LogSourceMethod);
        }
        try {

            List userIds = new ArrayList(1);
            String  defaultUser  = "";
            
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
            String ifsc = (String)request.getSession().getAttribute(InstaClientULC.IFSCCODE);
            
            String locationId = null;
            if (null != ifsc){
                //To get the IFSC ID
                locationId = ifsc.substring(ifsc.length()-4, ifsc.length());
                
            }
            
            
            List<WSUserDTO> users = getUsersByLocation(locationId);
            for (WSUserDTO wsUserDTO : users) {
                
                if (null != wsUserDTO && null != wsUserDTO.getUserVO()) {
                    
                    userIds.add(wsUserDTO.getUserVO().getId());
                }
            }

            List usrNames = new ArrayList(5);
            for (int i = 0; i < userIds.size(); i++) {

                String usrId = (String)userIds.get(i);

//                String branchCode = ((HostIFSCDetailsValueObject)
//                                    userDTO.masterDTO.ifscMasterVO).getBranchCode();
                
                String branchCode = getReportDTO().getBranchCode();
                if (branchCode != null) {
                    defaultUser = branchCode + "A";
                }
//                String Id = usrId.substring(11, usrId.length());
                if (!(defaultUser.equalsIgnoreCase(usrId)))
                    usrNames.add(usrId);

            }
            this.setUserIdList(usrNames);

        } catch (Exception e) {
            logger.severe(LogSourceClass, LogSourceMethod, e);
            throw new BeanException(e.getMessage());
        } finally {
        }
    }
    
    //RBC CMD 1.0
    @SuppressWarnings("unchecked")
    public void getUserIdByLocation(String branchId)
    throws BeanException {
        
        final String LogSourceMethod = "getUserIdByLocation(branchId)";

           {
                logger.entering(LogSourceClass, LogSourceMethod);
            }
            try {

                List userIds = new ArrayList(1);
                
                List<WSUserDTO> users = getUsersByLocation(branchId);
                for (WSUserDTO wsUserDTO : users) {
                    if (null != wsUserDTO && null != wsUserDTO.getUserVO()) {
                        userIds.add(wsUserDTO.getUserVO().getId());
                    }
                }
                this.setUserIdList(userIds);

            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new BeanException(e.getMessage());
            } finally {
            }
           
    }

    /**
     * Method to get Excel Sheet Report
     *
     *//*
    public void generateExcelSheetReport() {
        
        final String LogSourceMethod = "generateReport()";
        

        try {
            
            ReportDTO reportDto = getMessageServer().getExcelSheetReport(getReportDTO());
            this.branchDTOs = reportDto.branchwiseDTOs;
            this.netAmount = reportDto.dateGrantTolal;
            this.tranType = reportDto.tranType;
        } catch (Throwable throwable) {

            logger.severe(LogSourceClass, LogSourceMethod, throwable);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new RuntimeException(throwable.getMessage());
            }
        } finally {
        }
    }
    
    *//**
     * Method to generate Liquidity Mgt Report
     *
     *//*
    public void generateLiquidityMgtReport() {
        
        final String LogSourceMethod = "generateLiquidityMgtReport()";
        
    
        try {
            
            ReportDTO reportDto = getMessageServer().getLiquidityMgtReport(getReportDTO());
            creditAmt = reportDto.creditSettlement;
            debitAmt = reportDto.debitSettlement;
            inwCprTotal = reportDto.dateCprInAmt;
            inwIprTotal = reportDto.dateIprInAmt;
            outCprTotal = reportDto.dateCprOutAmt;
            outIprTotal = reportDto.dateIprOutAmt;
            creditTotal = creditAmt + inwCprTotal +inwIprTotal;
            debitTotal = outCprTotal + outIprTotal + debitAmt;
            liquidityamt = (creditTotal)-(debitTotal);
            
        } catch (Throwable throwable) {

            logger.severe(LogSourceClass, LogSourceMethod, throwable);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new RuntimeException(throwable.getMessage());
            }
        } finally {
        }
    }
    
    */
    
    /**
     * Method to get all rtgs definitions
     *
     */
    public void generateReportController(){

        final String LogSourceMethod = "generateReport()";
        

        try {
            
            try {
                if (fromDate != null && fromDate.length() > 0) {
                    Timestamp fromDateT = new Timestamp(timeFmt.parse(fromDate).getTime());
                    reportDTO.setFromDate(fromDateT);
                }
            } catch (ParseException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new BeanException("Enter a valid From Date :" + e.getMessage());
            }

            try {
                if (toDate != null && toDate.length() > 0) {
                    Timestamp todateT = new Timestamp(timeFmt.parse(toDate).getTime());
                    reportDTO.setToDate(todateT);
                }
            } catch (ParseException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new BeanException("Enter a valid To Date :" + e.getMessage());
            }
            
            
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
            String sessionId = request.getSession().getId();

            Message req = createMessage(sessionId, 400, 6, getReportDTO());
            req.addProperty("prop", prop);
            
            Message res = handle(sessionId, req);
            ReportDTO reportDto = (ReportDTO)res.info;
 
            if (reportDto.reportType != null) {
                
                if (reportDto.reportType.equalsIgnoreCase("Consolidated")) {

                    this.tranType = reportDto.tranType;
                    this.netAmount = reportDto.amount;
                    ifscMap = reportDto.ifscMap;
                    datesMap = reportDto.dateWiseMap;
                } else {
                    
                    this.netAmount = reportDto.branchesGrandTotalDTO.netAmount;
                    this.tranType = reportDto.tranType;
                    this.totalTxns = reportDto.getTxnCount();
                    this.branchDTOs = reportDto.branchwiseDTOs;
                }
            }
        } catch (Throwable throwable) {

            logger.severe(LogSourceClass, LogSourceMethod, throwable);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new RuntimeException(throwable.getMessage());
            }
        } finally {
        }
    }
    /*
    public void generateInwardDayEndReport() {

        final String LogSourceMethod = "generateInwardDayEndReport()";
        
    
        try { 
       
           ReportDTO reportDto = getMessageServer().getInwardDayEndReport(getReportDTO());
           this.branchDTOs = reportDto.branchwiseDTOs;
           this.netAmount = reportDto.dateGrantTolal;
       
        } catch (Throwable throwable) {
    
        logger.severe(LogSourceClass, LogSourceMethod, throwable);
        try {
           ServerException se = (ServerException)getExceptionHandler().getException(
                   RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
           throw se;
        } catch (Exception e) {
           logger.severe(LogSourceClass, LogSourceMethod, e);
           throw new RuntimeException(throwable.getMessage());
        }
        } finally {
        }
    }
    
    public void generateOutwardDayEndReport() {

        final String LogSourceMethod = "generateOutwardDayEndReport()";
        
    
        try { 
       
           ReportDTO reportDto = getMessageServer().getOutwardDayEndReport(getReportDTO());
           this.branchDTOs = reportDto.branchwiseDTOs;
           this.netAmount = reportDto.dateGrantTolal;
       
        } catch (Throwable throwable) {
    
        logger.severe(LogSourceClass, LogSourceMethod, throwable);
        try {
           ServerException se = (ServerException)getExceptionHandler().getException(
                   RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
           throw se;
        } catch (Exception e) {
           logger.severe(LogSourceClass, LogSourceMethod, e);
           throw new RuntimeException(throwable.getMessage());
        }
        } finally {
        }
    }
    
    *//**
         * Method to get all rtgs definitions
         *
         */
        public void generateReport(){

            final String LogSourceMethod = "generateReport()";
            logger.entering(LogSourceClass, LogSourceMethod);

            try {
                /*
                 * For Holidays time 'll be date
                 */
                try {
                    if (fromDate != null && fromDate.length() > 0) {
                        Timestamp fromDateT = new Timestamp(timeFmt.parse(fromDate).getTime());
                        reportDTO.setFromDate(fromDateT);
                    }
                } catch (ParseException e) {
                    logger.severe(LogSourceClass, LogSourceMethod, e);
                    throw new BeanException("Enter a valid From Date :" + e.getMessage());
                }

                try {
                    if (toDate != null && toDate.length() > 0) {
                        Timestamp todateT = new Timestamp(timeFmt.parse(toDate).getTime());
                        reportDTO.setToDate(todateT);
                    }
                } catch (ParseException e) {
                    logger.severe(LogSourceClass, LogSourceMethod, e);
                    throw new BeanException("Enter a valid To Date :" + e.getMessage());
                }

                Map dtMap ;
                
                HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
                String sessionId = request.getSession().getId();
                Message req = createMessage(sessionId, 400, 4, getReportDTO());
                Message res = handle(sessionId, req);
                dtMap = (Map) res.info;
                datesMap = dtMap;
                
                
                /*Iterator b = null;
                if (null != dtMap && null != dtMap.keySet()) {

                    while (dtMap.keySet().iterator().hasNext()) {
                        
                        java.sql.Date entdate = (java.sql.Date)b.next();
                        ReportDTO repDTO = (ReportDTO)dtMap.get(entdate);
                        this.reportDTO = repDTO;
                    }
                }*/
            } catch (Throwable throwable) {

                logger.severe(LogSourceClass, LogSourceMethod, throwable);
                try {
                    ServerException se = (ServerException)getExceptionHandler().getException(
                            RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                    throw se;
                } catch (Exception e) {
                    logger.severe(LogSourceClass, LogSourceMethod, e);
                    throw new RuntimeException(throwable.getMessage());
                }
            } finally {
                    logger.exiting(LogSourceClass, LogSourceMethod);
            }
        }
        /*
    public void generateMISCounterpartyAllBranches(){

        final String LogSourceMethod = "generateMISCounterpartyAllBranches()";
        

        try {
            
             * For Holidays time 'll be date
             
            try {
                if (fromDate != null && fromDate.length() > 0) {
                    Timestamp fromDateT = new Timestamp(timeFmt.parse(fromDate).getTime());
                    reportDTO.setFromDate(fromDateT);
                }
            } catch (ParseException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new BeanException("Enter a valid From Date :" + e.getMessage());
            }

            try {
                if (toDate != null && toDate.length() > 0) {
                    Timestamp todateT = new Timestamp(timeFmt.parse(toDate).getTime());
                    reportDTO.setToDate(todateT);
                }
            } catch (ParseException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new BeanException("Enter a valid To Date :" + e.getMessage());
            }

            reportDTO = getMessageServer().getMISCounterpartyAllBranches(getReportDTO());

        } catch (Throwable throwable) {

            logger.severe(LogSourceClass, LogSourceMethod, throwable);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new RuntimeException(throwable.getMessage());
            }
        } finally {
            if (isFinestLoggable)
                logger.exiting(LogSourceClass, LogSourceMethod);
        }
    }



    *//**
     * Method to generate MIS Report
     *
     *//*
    public void generateMISReport(){

        final String LogSourceMethod = "generateMISReport()";
        

        try {
            
             * For Holidays time 'll be date
             
            try {
                if (fromDate != null && fromDate.length() > 0) {
                    Timestamp fromDateT = new Timestamp(timeFmt.parse(fromDate).getTime());
                    reportDTO.setFromDate(fromDateT);
                }
            } catch (ParseException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new BeanException("Enter a valid From Date :" + e.getMessage());
            }

            try {
                if (toDate != null && toDate.length() > 0) {
                    Timestamp todateT = new Timestamp(timeFmt.parse(toDate).getTime());
                    reportDTO.setToDate(todateT);
                }
            } catch (ParseException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new BeanException("Enter a valid To Date :" + e.getMessage());
            }

            reportDTO = getMessageServer().getMISReport(getReportDTO());

        } catch (Throwable throwable) {

            logger.severe(LogSourceClass, LogSourceMethod, throwable);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new RuntimeException(throwable.getMessage());
            }
        } finally {
            if (isFinestLoggable)
                logger.exiting(LogSourceClass, LogSourceMethod);
        }
    }

    *//**
     * This is to generate MIS Counter party report
     *
     *//*
    public void generateMISCounterReport(){

       final String LogSourceMethod = "generateMISCounterReport()";
       

       try {
            
             * For Holidays time 'll be date
             
           try {
               if (fromDate != null && fromDate.length() > 0) {
                   Timestamp fromDateT = new Timestamp(timeFmt.parse(fromDate).getTime());
                   reportDTO.setFromDate(fromDateT);
               }
           } catch (ParseException e) {
               logger.severe(LogSourceClass, LogSourceMethod, e);
               throw new BeanException("Enter a valid From Date :" + e.getMessage());
           }

           try {
               if (toDate != null && toDate.length() > 0) {
                   Timestamp todateT = new Timestamp(timeFmt.parse(toDate).getTime());
                   reportDTO.setToDate(todateT);
               }
           } catch (ParseException e) {
               logger.severe(LogSourceClass, LogSourceMethod, e);
               throw new BeanException("Enter a valid To Date :" + e.getMessage());
           }

           reportDTO = getMessageServer().getMISCounterReport(getReportDTO());

       } catch (Throwable throwable) {

           logger.severe(LogSourceClass, LogSourceMethod, throwable);
           try {
               ServerException se = (ServerException)getExceptionHandler().getException(
                   RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
               throw se;
           } catch (Exception e) {
               logger.severe(LogSourceClass, LogSourceMethod, e);
               throw new RuntimeException(throwable.getMessage());
           }
       } finally {
           if (isFinestLoggable)
               logger.exiting(LogSourceClass, LogSourceMethod);
       }
    }

    public List getAllBanks() {

        List bankList = null;
        try {

            HttpServletRequest request = (HttpServletRequest)RequestContext.getPageContext();
            HttpSession session = request.getSession();

            com.objectfrontier.rtgs.service.MessageService messageService = getMessageServerName(session.getId(), true);

            List listBankMaster = messageService.listBankMasters();

            if (listBankMaster != null) {

                bankList = new ArrayList(listBankMaster.size());
                for (Iterator iter = listBankMaster.iterator(); iter.hasNext();) {

                    BankMasterDTO bankMasterDTO = (BankMasterDTO)iter.next();
                    String hostBankId = environment.getPropertyAsString(RHSJSPConstants.HOSTBANKID);
                    if (hostBankId.equalsIgnoreCase(bankMasterDTO.bankMasterVO.id+"")) continue;
                    BankBean bankBean = new BankBean(bankMasterDTO);
                    bankList.add(bankBean);
                }
               setBankList(bankList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bankList;
    }

    /**
     * Method to get all Detailed rtgs Messages definitions
     *
     */
    public void generateDetailedReport(){

        final String LogSourceMethod = "generateDetailedReport()";
        
        try {
            detailedReportDTO = new DetailedReportDTO();
            
            try {

                if (reportDTO.branchCode != null) {
                    detailedReportDTO.setBranchCode(reportDTO.branchCode);
                    detailedReportDTO.setBranchName(reportDTO.branchName);
                }
                detailedReportDTO.setTranType(reportDTO.tranType);
                detailedReportDTO.setTxnStatus(reportDTO.txnStatus);
                detailedReportDTO.setMsgSubType(reportDTO.msgSubType);//RBC CMD 1.0
                detailedReportDTO.setUtrNumber(reportDTO.utrNumber);//RBC CMD 1.0
                
                if (fromDate != null && fromDate.length() > 0) {

                    Timestamp fromDateT = new Timestamp(timeFmt.parse(fromDate).getTime());
                    detailedReportDTO.setFromDate(fromDateT);
                }
            } catch (Exception e) {

                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new BeanException("Enter a valid From Date :" + e.getMessage());
            }

            try {
                if (toDate != null && toDate.length() > 0) {
                    Timestamp todateT = new Timestamp(timeFmt.parse(toDate).getTime());
                    detailedReportDTO.setToDate(todateT);
                }
            } catch (Exception e) {

                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new BeanException("Enter a valid To Date :" + e.getMessage());
            }
//            detailedReportDTO = getMessageServer().getDetailedReport(getDetailedReportDTO());
            
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
            String sessionId = request.getSession().getId();
            Message req = createMessage(sessionId, 400, 3, getDetailedReportDTO());
            Message res = handle(sessionId, req);
            detailedReportDTO = (DetailedReportDTO) res.info;

        } catch (Throwable throwable) {

            logger.severe(LogSourceClass, LogSourceMethod, throwable);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                throw new RuntimeException(throwable.getMessage());
            }
        } finally {
                logger.exiting(LogSourceClass, LogSourceMethod);
        }
    }

    /**
     * Branch:Specific:Individual
     * Branch:All:Individual
     * ok
     * /
    public void prepareBranchIndividualPrint() {

        final String LogSourceMethod = "prepareBranchIndividualPrint()";
       {
            logger.entering(LogSourceClass, LogSourceMethod, ">>Start... ");
        }
        try {

            ReportDTO dto = this.getReportDTO();

            if (RHSJSPConstants.ALL.equalsIgnoreCase(dto.txnStatus) ) {

                // Preparetion of dto done here
                // Formatting done here
                BranchPrintJob pj = new AllBranchPrintJob(dto);
                pj.setHeading("Branch Individual Report");
                try {
                    this.setPrintString(pj.getText());
                } catch(Exception e) {
                    logger.severe(LogSourceClass, LogSourceMethod, e);
                }
            } else {

                // Preparetion of dto done here
                // Formatting done here
                BranchPrintJob pj = new BranchPrintJob(dto);
                pj.setHeading("Branch Individual Report");
                try {
                    this.setPrintString(pj.getText());
                } catch(Exception e) {
                    logger.severe(LogSourceClass, LogSourceMethod, e);
                }

            }

        } catch (Exception e) {

            logger.severe(LogSourceClass, LogSourceMethod, e);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception ex) {
                logger.severe(LogSourceClass, LogSourceMethod, ex);
                throw new RuntimeException(e.getMessage());
            }
        } finally {
           {
                logger.exiting(LogSourceClass, LogSourceMethod, "<<End");
            }
        }
    }

    /**
     * Branch: All:Consolidated:
     * Branch: Specific:Consolidated
     * ok
     * /
    public void prepareBranchConsolidatedPrint() {

        final String LogSourceMethod = "prepareBranchConsolidatedPrint()";
       {
            logger.entering(LogSourceClass, LogSourceMethod, ">>Start... ");
        }
        try {
            // Preparetion of dto done here
            // Formatting done here
            BranchPrintJob pj = new BranchConsolidatedPrintJob(this.getReportDTO());
            pj.setHeading("Branch Consolidated Report");
            try {
                this.setPrintString(pj.getText());
            } catch(Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
            }

        } catch (Exception e) {

            logger.severe(LogSourceClass, LogSourceMethod, e);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception ex) {
                logger.severe(LogSourceClass, LogSourceMethod, ex);
                throw new RuntimeException(e.getMessage());
            }
        } finally {
           {
                logger.exiting(LogSourceClass, LogSourceMethod, "<<End");
            }
        }
    }

    /**
     *  Controller:SuccessTx:All:Individual
     * /
    public void prepareControllerTypewiseIndividualPrint() {

        final String LogSourceMethod = "prepareControllerTypewiseIndividualPrint()";
       {
            logger.entering(LogSourceClass, LogSourceMethod, ">>Start... ");
        }
        try {
            // Preparetion of dto done here
            // Formatting done here
            TypePrintJob pj = new TypePrintJob(this.getReportDTO());
            pj.setHeading("Controller Typewise Individual Report");
            try {
                this.setPrintString(pj.getText());
            } catch(Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
            }

        } catch (Exception e) {

            logger.severe(LogSourceClass, LogSourceMethod, e);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception ex) {
                logger.severe(LogSourceClass, LogSourceMethod, ex);
                throw new RuntimeException(e.getMessage());
            }
        } finally {
           {
                logger.exiting(LogSourceClass, LogSourceMethod, "<<End");
            }
        }
    }

    /**
     * Controller:SuccTx:All:Consolidated
     * /
    public void prepareControllerTypewiseConsolidatedPrint() {

        final String LogSourceMethod = "prepareControllerTypewiseConsolidatedPrint()";
       {
            logger.entering(LogSourceClass, LogSourceMethod, ">>Start... ");
        }
        try {
            // Preparetion of dto done here
            // Formatting done here
            TypePrintJob pj = new TypeConsolidatedPrintJob(this.getReportDTO());
            pj.setHeading("Controller Typewise Consolidated Report");
            try {
                this.setPrintString(pj.getText());
            } catch(Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
            }

        } catch (Exception e) {

            logger.severe(LogSourceClass, LogSourceMethod, e);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception ex) {
                logger.severe(LogSourceClass, LogSourceMethod, ex);
                throw new RuntimeException(e.getMessage());
            }
        } finally {
           {
                logger.exiting(LogSourceClass, LogSourceMethod, "<<End");
            }
        }
    }

    /**
     *  Controler:All:All:Individual
     *  Controller:SuccessTx-BranchWise:Individual
     * /
    public void prepareControllerIndividualPrint() {

        final String LogSourceMethod = "prepareControllerIndividualPrint()";
       {
            logger.entering(LogSourceClass, LogSourceMethod, ">>Start... ");
        }
        try {

            ReportDTO dto = this.getReportDTO();

            if (RHSJSPConstants.ALL.equalsIgnoreCase(dto.txnStatus) ) {

                 BranchPrintJob pj = new AllBranchPrintJob(this.getReportDTO());
                 pj.setHeading("Controller Individual Report");
                 try {
                     this.setPrintString(pj.getText());
                 } catch(Exception e) {
                     logger.severe(LogSourceClass, LogSourceMethod, e);
                 }

            } else {

                BranchPrintJob pj = new BranchPrintJob(this.getReportDTO());
                pj.setHeading("Controller Individual Report");
                try {
                    this.setPrintString(pj.getText());
                } catch(Exception e) {
                    logger.severe(LogSourceClass, LogSourceMethod, e);
                }

            }
        } catch (Exception e) {

            logger.severe(LogSourceClass, LogSourceMethod, e);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception ex) {
                logger.severe(LogSourceClass, LogSourceMethod, ex);
                throw new RuntimeException(e.getMessage());
            }
        } finally {
           {
                logger.exiting(LogSourceClass, LogSourceMethod, "<<End");
            }
        }
    }

    /**
     * Controller:All:All:Consolidated
     * Controller:ST-branchwise:All:Consolidated
     * /
    public void prepareControllerConsolidatedPrint() {

        final String LogSourceMethod = "prepareControllerConsolidatedPrint()";
       {
            logger.entering(LogSourceClass, LogSourceMethod, ">>Start... ");
        }
        try {
            // Preparetion of dto done here
            // Formatting done here
            BranchPrintJob pj = new BranchConsolidatedPrintJob(this.getReportDTO());
            pj.setHeading("Controller Consolidated Report");
            try {
                this.setPrintString(pj.getText());
            } catch(Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
            }

        } catch (Exception e) {

            logger.severe(LogSourceClass, LogSourceMethod, e);
            try {
                ServerException se = (ServerException)getExceptionHandler().getException(
                        RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                throw se;
            } catch (Exception ex) {
                logger.severe(LogSourceClass, LogSourceMethod, ex);
                throw new RuntimeException(e.getMessage());
            }
        } finally {
           {
                logger.exiting(LogSourceClass, LogSourceMethod, "<<End");
            }
        }
    }

    //*/

    /**
     * @return
     */
    public ReportDTO getReportDTO() {

        if (reportDTO == null)
            reportDTO = new ReportDTO();
        return reportDTO;
    }

    /**
     * @param reportDTO
     */
    public void setReportDTO(ReportDTO reportDTO) {
        this.reportDTO = reportDTO;
    }

    public void reset() {

        //this.getReportDTO().branchCode = null;
    //    this.getReportDTO().txnStatus = null;
    //    this.getReportDTO().reportType = null;
        this.fromDate = null;
        this.toDate = null;
        this.alertDTOList = null;
        this.totalTxns = 0;
//        this.getReportDTO().tranType = null;
//        this.getReportDTO().msgSubType = null;
//        this.getReportDTO().fromAmount = 0;
//        this.getReportDTO().toAmount = 0;
//        this.getReportDTO().userId = null;
//        this.getReportDTO().utrNumber = "";
//        
    }
    
    public void resetAll() {

            //this.getReportDTO().branchCode = null;
        //    this.getReportDTO().txnStatus = null;
        //    this.getReportDTO().reportType = null;
//            this.fromDate = null;
//            this.toDate = null;
//            this.alertDTOList = null;
            this.getReportDTO().tranType = null;
            this.getReportDTO().msgSubType = null;
//            this.getReportDTO().fromAmount = 0;
//            this.getReportDTO().toAmount = 0;
            this.getReportDTO().fromAmount = BigDecimal.ZERO;
            this.getReportDTO().toAmount = BigDecimal.ZERO;
            this.getReportDTO().userId = null;
            this.getReportDTO().utrNumber = null;
            this.utrWise = "";
            this.reportWise = "";
        }

    /**
     * @param string
     */
    public void setFromDate(String string) {
        fromDate = string;
    }

    /**
     * @param string
     */
    public void setToDate(String string) {
        toDate = string;
    }

    /**
     * @return
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @return
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * @return
     */
    public List getBranches() {

        if (branches == null)
            branches = new ArrayList(0);
        return branches;
    }

    /**
     * @param list
     */
    public void setBranches(List list) {
        branches = list;
    }

   public ActionErrors validate() {

        final String LogSourceMethod = "validate(arg0, arg1)";
        ActionErrors errors = new ActionErrors(); 
        
        if (reportDTO.fromAmount.compareTo(reportDTO.toAmount) > 0) {
            
            try {
                errorMessage = "ToAmount ( " + String.valueOf(reportDTO.toAmount) + ") should be greater than FromAmount ( " + String.valueOf(reportDTO.fromAmount) + ")";
                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY, errorMessage));
            } catch (ResourceException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
            }
            
        }

        if (fromDate == null || fromDate.trim().length() == 0 ) {
            try {
               /* errorMessage =
                getMessageFactory().getMessage(RHSJSPConstants.MSG_RHS_EMPTY,
                FROMDATE).getShortDescription();*/
            } catch (ResourceException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
            }
            errors.add(fromDate,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY, errorMessage));

        } else if (toDate == null || toDate.trim().length() == 0){
            try {
                /*errorMessage =
                getMessageFactory().getMessage(RHSJSPConstants.MSG_RHS_EMPTY,
                    TODATE).getShortDescription();*/
            } catch (ResourceException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
            }
            errors.add(toDate,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY, errorMessage));
        }
        if (fromDate != null && toDate != null) {

            Date fDate = null;
            Date tDate = null;
            try {
                if (fromDate != null && fromDate.length() > 0) {
                    fDate = ConversionUtils.getDate(fromDate);
                }
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                try {
                   /* errorMessage =
                    getMessageFactory().getMessage(RHSJSPConstants.Msg_RHS_INVALID_DATE,
                        FROMDATE).getShortDescription();*/
                } catch (ResourceException er) {
                    logger.severe(LogSourceClass, LogSourceMethod, er);
                }
                errors.add(toDate,
                        new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY, errorMessage));
            }

            try {
                if (toDate != null && toDate.length() > 0) {
                    tDate = ConversionUtils.getDate(toDate);
                }
            } catch (Exception e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
                try {
                  /*  errorMessage =
                    getMessageFactory().getMessage(RHSJSPConstants.Msg_RHS_INVALID_DATE,
                        TODATE).getShortDescription();*/
                } catch (ResourceException er) {
                    logger.severe(LogSourceClass, LogSourceMethod, er);
                }
                errors.add(toDate,
                        new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY, errorMessage));
            }

            if (fDate != null && tDate != null
                    && fDate.after(tDate)) {

                try {
                   /* errorMessage =
                    getMessageFactory().getMessage(RHSJSPConstants.Msg_RHS_FROMDATEGTTODATE).getShortDescription();*/
                } catch (ResourceException er) {
                    logger.severe(LogSourceClass, LogSourceMethod, er);
                }
                errors.add(toDate,
                        new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY, errorMessage));
            }

        }

        return errors;
    }
    
    
    public ActionErrors validateUTR() {

        final String LogSourceMethod = "validateUTR()";
        ActionErrors errors = new ActionErrors(); 
        
        
        if (reportDTO.utrNumber == null || reportDTO.utrNumber.trim().length() == 0  ) {
            
            try {
                errorMessage = "Please enter the UTR Number";
                errors.add(ActionErrors.GLOBAL_MESSAGE,
                    new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY, errorMessage));
            } catch (ResourceException e) {
                logger.severe(LogSourceClass, LogSourceMethod, e);
            }

        } 

        return errors;
    }

    /**
     * Initialize the DTO, if it null
     * Get the Detailed Report view DTO
     *
     * @return IOBDetailedReportDTO
     */
    public DetailedReportDTO getDetailedReportDTO() {


        if (detailedReportDTO == null) {
            detailedReportDTO = new DetailedReportDTO();
        }
        return detailedReportDTO;
    }

    /**
     * Set the Detailed Reprot View DTO
     *
     * @param reportDTO
     */
    public void setDetailedReportDTO(DetailedReportDTO reportDTO) {
        this.detailedReportDTO = reportDTO;
    }
    /**
      * Method to get all alert info
      *
      *//*
     public void generateAlertReport(){

         final String LogSourceMethod = "generateAlertReport()";
         

         try {
             
              * For Holidays time 'll be date
              
             try {
                 if (fromDate != null && fromDate.length() > 0) {
                     Timestamp fromDateT = new Timestamp(timeFmt.parse(fromDate).getTime());
                     reportDTO.setFromDate(fromDateT);
                 }
             } catch (ParseException e) {
                 logger.severe(LogSourceClass, LogSourceMethod, e);
                 throw new BeanException("Enter a valid From Date :" + e.getMessage());
             }

             try {
                 if (toDate != null && toDate.length() > 0) {
                     Timestamp todateT = new Timestamp(timeFmt.parse(toDate).getTime());
                     reportDTO.setToDate(todateT);
                 }
             } catch (ParseException e) {
                 logger.severe(LogSourceClass, LogSourceMethod, e);
                 throw new BeanException("Enter a valid To Date :" + e.getMessage());
             }
             getReportDTO().setBranchName(branchName);
             title = RHSJSPConstants.EMAIL_ALERT;
             alertDTOList = getMessageServer().getAlertReport(getReportDTO());
         } catch (Throwable throwable) {

             logger.severe(LogSourceClass, LogSourceMethod, throwable);
             try {
                 ServerException se = (ServerException)getExceptionHandler().getException(
                         RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
                 throw se;
             } catch (Exception e) {
                 logger.severe(LogSourceClass, LogSourceMethod, e);
                 throw new RuntimeException(throwable.getMessage());
             }
         } finally {
             if (isFinestLoggable)
                 logger.exiting(LogSourceClass, LogSourceMethod);
         }
     }

    /**
      * Method to get all event alert info
      *
      *//*
     public void generateEventReport(){

         final String LogSourceMethod = "generateEventReport()";

         

         try {

             try {
                 if (fromDate != null && fromDate.length() > 0) {
                     Timestamp fromDateT = new Timestamp(timeFmt.parse(fromDate).getTime());
                     reportDTO.setFromDate(fromDateT);
                 }
             } catch (ParseException e) {
                 logger.severe(LogSourceClass, LogSourceMethod, e);
                 throw new BeanException("Enter a valid From Date :" + e.getMessage());
             }

             try {
                 if (toDate != null && toDate.length() > 0) {
                     Timestamp todateT = new Timestamp(timeFmt.parse(toDate).getTime());
                     reportDTO.setToDate(todateT);
                 }
             } catch (ParseException e) {
                 logger.severe(LogSourceClass, LogSourceMethod, e);
                 throw new BeanException("Enter a valid To Date :" + e.getMessage());
             }
             title = RHSJSPConstants.EVENT_ALERT;
             alertDTOList = getMessageServer().getEventReport(getReportDTO());
         } catch (Throwable throwable) {

             logger.severe(LogSourceClass, LogSourceMethod, throwable);
             try {
                 ServerException se = (ServerException)getExceptionHandler().getException(
                         RHSJSPConstants.MSG_RHS_LISTING_PROBLEM);
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

//    /**
//     * @return
//     */
//    public String getPrintString() {
//        return printString;
//    }
//
//    /**
//     * @param string
//     */
//    public void setPrintString(String string) {
//        printString = string;
//    }

    /**
     * @return
     */
    public List getAlertDTOList() {
        return alertDTOList;
    }

    /**
     * @param list
     */
    public void setAlertDTOList(List alertDTOList) {
        this.alertDTOList = alertDTOList;
    }

    /**
     * @return
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param string
     */
    public void setBranchName(String string) {
        branchName = string;
    }

    /**
     * @return
     */
    public static String getTitle() {
        return title;
    }

    /**
     * @param string
     */
    public static void setTitle(String string) {
        title = string;
    }
    /**
     * @return
     */
    public String getTxnType() {
        return txnType;
    }

    /**
     * @param string
     */
    public void setTxnType(String string) {
        txnType = string;
    }

    /**
     * @return
     */
    public String getReportWise() {
        return reportWise;
    }

    /**
     * @param string
     */
    public void setReportWise(String string) {
        reportWise = string;
    }

    /**
     * @return
     */
    public String getCounterpartyReports() {
        return counterpartyReports;
    }

    /**
     * @param string
     */
    public void setCounterpartyReports(String string) {
        counterpartyReports = string;
    }

    /**
     * @return
     */
    public List getBankList() {
        return bankList;
    }

    /**
     * @param list
     */
    public void setBankList(List list) {
        bankList = list;
    }

    /**
     * @return
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * @param string
     */
    public void setBankId(String string) {
        bankId = string;
    }

    /**
     * @return
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param string
     */
    public void setBankName(String string) {
        bankName = string;
    }

    /**
     * @return
     */
    public String getCounterPartyTitle() {
        return counterPartyTitle;
    }

    /**
     * @param string
     */
    public void setCounterPartyTitle(String string) {
        counterPartyTitle = string;
    }

    public String getBranchType() {
        return branchType;
    }

    public String getSOLCode() {
        return SOLCode;
    }

    public List getSOLList() {
        return SOLList;
    }

    public String getTranType() {
        return tranType;
    }
    
    public void setBranchType(String string) {
        branchType = string;
    }

    public void setSOLCode(String string) {
        SOLCode = string;
    }

    public void setSOLList(List list) {
        SOLList = list;
    }
    
    public void setTranType(String string) {
        tranType = string;
    }
    
    /**
     * Method to load the SOl  Details 
     */
    public void loadSOLDetails()
    throws BeanException {

        final String LogSourceMethod = "loadSOLDetails()";

        try {
            List ListSOLDetails;
            
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
            String sessionId = request.getSession().getId();
            Message req = createMessage(sessionId, 400, 5, null);
            Message res = handle(sessionId, req);
            ListSOLDetails = (List) res.info;

            if (ListSOLDetails != null) {
                
                for (Iterator iter = ListSOLDetails.iterator(); iter.hasNext(); ) {

                    ReportBean b = new ReportBean();
                    b.setSOLCode(String.valueOf(iter.next()));
                    SOLList.add(b);
                }
            }

        } catch (Exception e) {
            logger.severe(LogSourceClass, LogSourceMethod, e);
            throw new BeanException(e.getMessage());
        } finally {
           
                logger.finest(LogSourceClass, LogSourceMethod, ">>End");
        }
        
    }
    
    
    private List<WSUserDTO> getUsersByLocation(String location) throws Exception{
        
        String endpoint = InstaDefaultConstants.WEBSERVICE_ENDPOINT;
        if (endpoint==null) endpoint = env.getPropertyAsString("WebService_EndPoint");
        UserManagement user = new UserManagementImpl(endpoint);
        
        WSUserDTO userDTO = new WSUserDTO();
        WSUserValueObject vo = new WSUserValueObject();
        
        //Set the location 
        vo.setLocation(location);
        userDTO.setUserVO(vo);
        
        List<WSUserDTO> wsUserDTOList = user.getUsersByLocation(userDTO);
        
        return wsUserDTOList;
    }
    

    public Map getDatesMap() {
        return datesMap;
    }

    public void setDatesMap(Map map) {
        datesMap = map;
    }

    /**
     * @return
     */
    public List getTranTypeList() {
        return tranTypeList;
    }

    /**
     * @param list
     */
    public void setTranTypeList(List list) {
        tranTypeList = list;
    }

    public List getTransactionList() {

       List tranTypeList = new ArrayList(1);
       tranTypeList.add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
       tranTypeList.add(RHSJSPConstants.INWARD_TRANSACTION_TYPE);
       tranTypeList.add(RHSJSPConstants.BOTH_TRANSACTION_TYPE);
       return tranTypeList;
       
    }
   
    public List getTransactionListForExcel() {

      List tranTypeList = new ArrayList(1);
      tranTypeList.add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
      tranTypeList.add(RHSJSPConstants.INWARD_TRANSACTION_TYPE);
      return tranTypeList;
    }
      
    public List getTransactionListForConsolidated() {

      List tranTypeList = new ArrayList(1);
      tranTypeList.add(RHSJSPConstants.BOTH_TRANSACTION_TYPE);
      return tranTypeList;
   }
   /**
       * @return
       */
      public Map getSubTypeMap() {
          return subTypeMap;
      }

      /**
       * @param map
       */
      public void setSubTypeMap(Map map) {
          subTypeMap = map;
      }


       /**
        * RBC CMD 1.0
        * Method to get sub message type
        * @return
        */
       public Map getMsgSubTypeMap() {
          
           subTypeMap.put(RHSJSPConstants.IPR_MESSAGE_TYPE, RHSJSPConstants.IPR_MESSAGE_SUBTYPE);
           subTypeMap.put(RHSJSPConstants.CPR_MESSAGE_TYPE, RHSJSPConstants.CPR_MESSAGE_SUBTYPE);
           subTypeMap.put(RHSJSPConstants.BOTH,RHSJSPConstants.BOTH);
        return subTypeMap;
       
        }
   
       public Map getMsgSubTypeMapForExcel() {
          
          subTypeMap.put(RHSJSPConstants.IPR_MESSAGE_TYPE, RHSJSPConstants.IPR_MESSAGE_SUBTYPE);
          subTypeMap.put(RHSJSPConstants.CPR_MESSAGE_TYPE, RHSJSPConstants.CPR_MESSAGE_SUBTYPE);
          
        return subTypeMap;
       
      }
      
    public Map getMsgSubTypeMapForConsolidated() {
          
        subTypeMap.put(RHSJSPConstants.BOTH,RHSJSPConstants.BOTH);
        return subTypeMap;
    }
   
    /**
     * @return
     */
    public String getUtrNumber() {//RBC CMD 1.0
        return utrNumber;
    }

    /**
     * @param string
     */
    public void setUtrNumber(String string) {//RBC CMD 1.0
        utrNumber = string;
    }

    /**
     * @return
     */
    public String getUtrWise() {//RBC CMD 1.0
        return utrWise;
    }

    /**
     * @param string
     */
    public void setUtrWise(String string) {//RBC CMD 1.0
        utrWise = string;
    }

    /**
     * @return
     */
    public List getBranchDTOs() {
        return branchDTOs;
    }

    /**
     * @param list
     */
    public void setBranchDTOs(List list) {
        branchDTOs = list;
    }

//    /**
//     * @return
//     */
//    public double getNetAmount() {
//        return netAmount;
//    }
//
//    /**
//     * @param d
//     */
//    public void setNetAmount(double d) {
//        netAmount = d;
//    }
    
    /**
     * @return
     */
    public BigDecimal getNetAmount() {
        return netAmount;
    }

    /**
     * @param d
     */
    public void setNetAmount(BigDecimal d) {
        netAmount = d;
    }

    /**
     * @return
     */
    public int getTotalTxns() {
        return totalTxns;
    }

    /**
     * @param i
     */
    public void setTotalTxns(int i) {
        totalTxns = i;
    }

    /**
     * @return
     */
    public Map getIfscMap() {
        return ifscMap;
    }

    /**
     * @param map
     */
    public void setIfscMap(Map map) {
        ifscMap = map;
    }
//    /**
//     * @return
//     */
//    public double getCreditAmt() {
//        return creditAmt;
//    }
//
//    /**
//     * @return
//     */
//    public double getDebitAmt() {
//        return debitAmt;
//    }
//
//    /**
//     * @return
//     */
//    public double getInwCprTotal() {
//        return inwCprTotal;
//    }
//
//    /**
//     * @return
//     */
//    public double getInwIprTotal() {
//        return inwIprTotal;
//    }
//
//    /**
//     * @return
//     */
//    public double getLiquidityamt() {
//        return liquidityamt;
//    }
//
//    /**
//     * @return
//     */
//    public double getOutCprTotal() {
//        return outCprTotal;
//    }
//
//    /**
//     * @return
//     */
//    public double getOutIprTotal() {
//        return outIprTotal;
//    }
//
//    /**
//     * @param d
//     */
//    public void setCreditAmt(double d) {
//        creditAmt = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setDebitAmt(double d) {
//        debitAmt = d;
//    }
//
//    /**
//     * @param d
//     */
//    public void setInwCprTotal(double d) {
//        inwCprTotal = d;
//    }
    
    /**
     * @return
     */
    public String getCreditAmt() {
        return creditAmt;
    }

    /**
     * @return
     */
    public String getDebitAmt() {
        return debitAmt;
    }

    /**
     * @return
     */
    public String getInwCprTotal() {
        return inwCprTotal;
    }

    /**
     * @return
     */
    public String getInwIprTotal() {
        return inwIprTotal;
    }

    /**
     * @return
     */
    public String getLiquidityamt() {
        return liquidityamt;
    }

    /**
     * @return
     */
    public String getOutCprTotal() {
        return outCprTotal;
    }

    /**
     * @return
     */
    public String getOutIprTotal() {
        return outIprTotal;
    }

    /**
     * @param d
     */
    public void setCreditAmt(String d) {
        creditAmt = d;
    }

    /**
     * @param d
     */
    public void setDebitAmt(String d) {
        debitAmt = d;
    }

    /**
     * @param d
     */
    public void setInwCprTotal(String d) {
        inwCprTotal = d;
    }

    /**
     * @param d
     */
    public void setInwIprTotal(String d) {
        inwIprTotal = d;
    }

    /**
     * @param d
     */
    public void setLiquidityamt(String d) {
        liquidityamt = d;
    }

    /**
     * @param d
     */
    public void setOutCprTotal(String d) {
        outCprTotal = d;
    }

    /**
     * @param d
     */
    public void setOutIprTotal(String d) {
        outIprTotal = d;
    }

    /**
     * @return
     */
    public String getCreditTotal() {
        return creditTotal;
    }

    /**
     * @return
     */
    public String getDebitTotal() {
        return debitTotal;
    }

    /**
     * @param d
     */
    public void setCreditTotal(String d) {
        creditTotal = d;
    }

    /**
     * @param d
     */
    public void setDebitTotal(String d) {
        debitTotal = d;
    }

    
    public String getCurrentMode() {
    
        return currentMode;
    }

    
    public void setCurrentMode(String currentMode) {
    
        this.currentMode = currentMode;
    }

}
