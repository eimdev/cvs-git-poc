/*
 * @(#)InstaReportBean.java
 *
 * Copyright by ObjectFrontier Software Private Limited (OFS)
 * www.objectfrontier.com
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of OFS. You shall not disclose such confidential
 * information and shall use it only in accordance with the terms of
 * the license agreement you entered into with OFS.
 */
package com.objectfrontier.insta.rtgs.reports.bean;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import com.objectfrontier.arch.client.jsp.bean.BeanException;
import com.objectfrontier.arch.context.RequestContext;
import com.objectfrontier.arch.dto.Message;
import com.objectfrontier.arch.server.ServerException;
import com.objectfrontier.crud.CrudException;
import com.objectfrontier.insta.InstaDefaultConstants;
import com.objectfrontier.insta.client.InstaClientULC;
import com.objectfrontier.insta.client.env.InstaAppClientEnvironment;
import com.objectfrontier.insta.dto.DisplayValueReportDTO;
import com.objectfrontier.insta.message.client.dto.CMsgDTO;
import com.objectfrontier.insta.message.client.dto.HostIFSCMasterDTO;
import com.objectfrontier.insta.message.client.vo.HostIFSCMasterVO;
import com.objectfrontier.insta.reports.InstaReportUtil;
import com.objectfrontier.insta.reports.dto.ConsolidatedReconcileReportDTO;
import com.objectfrontier.insta.reports.dto.ReconcileReportDTO;
import com.objectfrontier.insta.reports.dto.ReportDTO;
import com.objectfrontier.insta.reports.dto.ReportInputDTO;
import com.objectfrontier.insta.workflow.util.ConversionUtil;
import com.objectfrontier.neft.report.dto.NEFT_RTGSNetSettlementDTO;
import com.objectfrontier.user.ws.UserManagement;
import com.objectfrontier.user.ws.dto.WSUserDTO;
import com.objectfrontier.user.ws.impl.UserManagementImpl;
import com.objectfrontier.user.ws.vo.WSUserValueObject;


/**
 * @author jinuj
 * @date   Jul 29, 2008
 * @since  insta.reports; Jul 29, 2008
 */
public class InstaReportBean
extends AbstractReportBean {

    private static Category logger = Category.getInstance(InstaReportBean.class);

    public ReportInputDTO reportDto;
    private List<ReportDTO> reportDTOs;
    public List<HostIFSCMasterDTO> hostBranchList;
    public Map reportMap;
    public int isCOUser = 0;
    private CMsgDTO messageDTO;
    //This holds the IFSC Code of the User who Logged In.
    private String userIfscCode;
    //This will hold the information of the pageForward.
    private String pageForward;
    private long userIfscId;
    public int isDateOnly;
    public int disableBranchDisplay = 0;
    public String report;

    public boolean haveValueDateField    = false;
    public boolean haveBatchTimeField    = false;
    public boolean haveReportTypeField   = false;
    public boolean haveInwardTypeField   = false;

    /*
     *   Below Variables are used to set the fields which are need to be displayed,
     * in the report input page. Based on the settings of the field value, input page
     * should be loaded.
     *
     * Start
     */
    public boolean haveDateField        = false;
    public boolean haveTranTypeField    = false;
    public boolean haveMsgSubTypeField  = false;
    public boolean haveHostTypeField    = false;
    public boolean haveBranchField      = false;
    public boolean isInwardSpecific     = false;
    public boolean isOutwardSpecific    = false;
    public boolean haveUTRNoField       = false;
    public boolean haveAmountField      = false;
    public boolean haveStatusField      = false;
    public boolean haveCounterPartyFld  = false;
    public boolean haveUserField        = false;
    public boolean haveChannelField     = false;
    //Added on 22-Sep-2009 by mohana for Future date txn status
    public boolean haveFutureDateTxnStatus = false;
    public boolean haveResponeType = false;

    public boolean haveBankListField = false;
    /*
     * Above Variables are used to set the fields which are need to be displayed.
     *
     * End
     */

    private List<DisplayValueReportDTO> subTypeList;
    private List hostList;
    private List<DisplayValueReportDTO> tranTypeList;
    private List<DisplayValueReportDTO> statusList;
    private List nonHostBranchList;
    private List bankList;
    public List userIdList;
    private List channelList;
    private List listOfTranType;

    //To Hold the current System Date and Time (DD-MMM-YYYY HH:MMa)
    public String currentReportPrintTime;
    public String currentReportPrintTimeFormat = "dd-MMM-yyyy HH:mmaa";
    private String ifscCode;
    /*
     * START
     * ADDED FOR BOR REPORT

     */
    public ReportDTO newReportDto;
    public List branchDTOs;
//    public double netAmount = 0.0;
//    public double creditAmt = 0.0;
//    public double debitAmt = 0.0;
//    public double inwCprTotal = 0.0;
//    public double inwIprTotal = 0.0;
//    public double outCprTotal = 0.0;
//    public double outIprTotal = 0.0;
//    public double liquidityamt = 0.0;
//    public double debitTotal = 0.0;
//    public double creditTotal = 0.0;
    public String netAmount = "0.00";
    public String creditAmt = "0.00";
    public String debitAmt = "0.00";
    public String inwCprTotal = "0.00";
    public String inwIprTotal = "0.00";
    public String outCprTotal = "0.00";
    public String outIprTotal = "0.00";
    public BigDecimal liquidityamt = BigDecimal.ZERO;
    public BigDecimal debitTotal = BigDecimal.ZERO;
    public BigDecimal creditTotal = BigDecimal.ZERO;
    public Map subTypeMap = new HashMap(1); //RBC CMD 1.0
    public String tranType;
    public String accNo;
    public List NEFTRTGS_settlementList;
    public Map NEFTRTGS_settlementMap;
    public String NEFTRTGS_NetSettleKeyword;

	/** To store inward detail report heading. */
    public String inwDetailReport = "NEFT Inward Detailed Report";
    public String outDetailReport = "NEFT Outward Detailed Report";
    public String inwSummaryReport = "NEFT Inward Summary Report";
    public String outwSummaryReport = "NEFT Outward Summary Report";
    public String reportTitle;

    //For LCBG - Starts, Joe.M
    /**
     * To set Report Title
     */
    public String lcbgSummaryReport = "LCBG Summary Report";
    public String lcbgReportTitle;
    //For LCBG - Ends, Joe.M

    public String reportEvent;
    //variable to hold date format
    public String dateFormat;

    public List inwardTypeList;
    public List outwardTypeList;
    public List inwardStatusList;
    public List outwardStatusList;

    public String inwardType;
    public String outwardType;
    public String inwardStatus;
    public String outwardStatus;

    public List<ReportDTO> returnedList;
    //Variable to hold channel
    public String channel;

    private String bifsc;


    public List<HostIFSCMasterDTO> getHostBranchList() {

        if (hostBranchList==null)
            hostBranchList = new ArrayList<HostIFSCMasterDTO>(0);
        return hostBranchList;
    }

    public void setHostBranchList(List<HostIFSCMasterDTO> hostBranchList) {

        this.hostBranchList = hostBranchList;
    }

    public int getIsCOUser() {

        return isCOUser;
    }

    public void setIsCOUser(int isCOUser) {

        this.isCOUser = isCOUser;
    }

    //Getting the Inward Detail Report Name, 20110802
    public String getInwDetailReport() {
        return inwDetailReport;
    }

    //Setting the Inward Detail Report Name, 20110802
    public void setInwDetailReport(String inwDetailReport) {
        this.inwDetailReport = inwDetailReport;
    }

    //Getting the Outward Detail Report Name, 20110802
    public String getOutDetailReport() {
        return outDetailReport;
    }

    //Setting the Outward Detail Report Name, 20110802
    public void setOutDetailReport(String outDetailReport) {
        this.outDetailReport = outDetailReport;
    }

    public String getUserIfscCode() {

        return userIfscCode;
    }

    public void setUserIfscCode(String userIfscCode) {

        this.userIfscCode = userIfscCode;
    }

    public long getUserIfscId() {

        return userIfscId;
    }

    public void setUserIfscId(long userIfscId) {

        this.userIfscId = userIfscId;
    }

    public int getIsDateOnly() {

        return isDateOnly;
    }

    public void setIsDateOnly(int isDateOnly) {

        this.isDateOnly = isDateOnly;
    }

    public ReportInputDTO getReportDto() {

        if (reportDto==null)
            reportDto = new ReportInputDTO();
        return reportDto;
    }

    public void setReportDto(ReportInputDTO reportDto) {

        this.reportDto = reportDto;
    }

    public List<ReportDTO> getReportDTOs() {

        if (reportDTOs==null) {
            reportDTOs = new ArrayList<ReportDTO>(0);
        }
        return reportDTOs;
    }

    public void setReportDTOs(List<ReportDTO> reportDTOs) {

        this.reportDTOs = reportDTOs;
    }


    public boolean isHaveAmountField() {

        return haveAmountField;
    }


    public void setHaveAmountField(boolean haveAmountField) {

        this.haveAmountField = haveAmountField;
    }


    public boolean isHaveBranchField() {

        return haveBranchField;
    }


    public void setHaveBranchField(boolean haveBranchField) {

        this.haveBranchField = haveBranchField;
    }


    public boolean isHaveDateField() {

        return haveDateField;
    }


    public void setHaveDateField(boolean haveDateField) {

        this.haveDateField = haveDateField;
    }


    public boolean isHaveHostTypeField() {

        return haveHostTypeField;
    }


    public void setHaveHostTypeField(boolean haveHostTypeField) {

        this.haveHostTypeField = haveHostTypeField;
    }


    public boolean isHaveMsgSubTypeField() {

        return haveMsgSubTypeField;
    }


    public void setHaveMsgSubTypeField(boolean haveMsgSubTypeField) {

        this.haveMsgSubTypeField = haveMsgSubTypeField;
    }


    public boolean isHaveStatusField() {

        return haveStatusField;
    }


    public void setHaveStatusField(boolean haveStatusField) {

        this.haveStatusField = haveStatusField;
    }


    public boolean isHaveTranTypeField() {

        return haveTranTypeField;
    }


    public void setHaveTranTypeField(boolean haveTranTypeField) {

        this.haveTranTypeField = haveTranTypeField;
    }


    public boolean isHaveUTRNoField() {

        return haveUTRNoField;
    }


    public void setHaveUTRNoField(boolean haveUTRNoField) {

        this.haveUTRNoField = haveUTRNoField;
    }


    public boolean getIsInwardSpecific() {

        return isInwardSpecific;
    }


    public void setIsInwardSpecific(boolean isInwardSpecific) {

        this.isInwardSpecific = isInwardSpecific;
    }


    public boolean getIsOutwardSpecific() {

        return isOutwardSpecific;
    }


    public void setIsOutwardSpecific(boolean isOutwardSpecific) {

        this.isOutwardSpecific = isOutwardSpecific;
    }


    public boolean isHaveCounterPartyFld() {

        return haveCounterPartyFld;
    }

    public void setHaveCounterPartyFld(boolean haveCounterPartyFld) {

        this.haveCounterPartyFld = haveCounterPartyFld;
    }

    public boolean isHaveUserField() {

        return haveUserField;
    }

    public void setHaveUserField(boolean haveUserField) {

        this.haveUserField = haveUserField;
    }

    public List getHostList() {

        if (hostList==null) {
            hostList = new ArrayList(0);
        }
        return hostList;
    }

    public void setHostList(List hostList) {

        this.hostList = hostList;
    }

    public List getStatusList() {

        if (statusList==null) {
            statusList = new ArrayList<DisplayValueReportDTO>(0);
        }
        return statusList;
    }

    public void setStatusList(List<DisplayValueReportDTO> statusList) {

        this.statusList = statusList;
    }

    public List getSubTypeList() {

        if (subTypeList==null) {
            subTypeList = new ArrayList<DisplayValueReportDTO>(0);
        }
        return subTypeList;
    }

    public void setSubTypeList(List<DisplayValueReportDTO> subTypeList) {

        this.subTypeList = subTypeList;
    }

    public List getTranTypeList() {

        if (tranTypeList==null) {
            tranTypeList = new ArrayList<DisplayValueReportDTO>(0);
        }
        return tranTypeList;
    }

    public void setTranTypeList(List<DisplayValueReportDTO> tranTypeList) {

        this.tranTypeList = tranTypeList;
    }

    public List getNonHostBranchList() {

        if (nonHostBranchList==null) {
            nonHostBranchList = new ArrayList(0);
        }
        return nonHostBranchList;
    }

    public void setNonHostBranchList(List nonHostBranchList) {

        this.nonHostBranchList = nonHostBranchList;
    }

    public Map<String, List<ReportDTO>> getReportMap() {

        if (reportMap==null) {
            reportMap = new HashMap<String, List<ReportDTO>>(0);
        }
        return reportMap;
    }

    public void setReportMap(Map<String, List<ReportDTO>> reportMap) {

        this.reportMap = reportMap;
    }

    public List getBankList() {

        if (bankList == null) {
            bankList = new ArrayList(0);
        }
        return bankList;
    }

    public void setBankList(List bankList) {

        this.bankList = bankList;
    }

    public CMsgDTO getMessageDTO() {

        return messageDTO;
    }

    public void setMessageDTO(CMsgDTO msgDTO) {

        this.messageDTO = msgDTO;
    }

    /**
     * TODO
     */
    @SuppressWarnings("unchecked")
    public void generateR41InwardReports(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();
            //To change the format
            /*String inputDt = getReportDto().getValueDate();
            inputDt = InstaReportUtil.formatDateString(inputDt);
            getReportDto().setValueDate(inputDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 200, 1, new Object[] {reportDto, userIfscId, userIfscCode});
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
            updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : "+e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    @SuppressWarnings("unchecked")
    public void generateR42InwardReports(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();
            //To change the format
            /*String inputDt = getReportDto().getValueDate();
            inputDt = InstaReportUtil.formatDateString(inputDt);
            getReportDto().setValueDate(inputDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 200, 2, new Object[] {reportDto, userIfscId, userIfscCode});
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy...Load initial
            reportDTOs = (List) res.info;
            updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : "+e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    @SuppressWarnings("unchecked")
    public void generateR41OutwardReports(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();
            //To change the format
           /* String inputDt = getReportDto().getValueDate();
            inputDt = InstaReportUtil.formatDateString(inputDt);
            getReportDto().setValueDate(inputDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 200, 3, new Object[] {reportDto, userIfscId, userIfscCode});
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;

            updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : "+e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    @SuppressWarnings("unchecked")
    public void generateR42OutwardReports(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();
            //To change the format
            /*String inputDt = getReportDto().getValueDate();
            inputDt = InstaReportUtil.formatDateString(inputDt);
            getReportDto().setValueDate(inputDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 200, 4, new Object[] {reportDto, userIfscId, userIfscCode});
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
            updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    @SuppressWarnings("unchecked")
    public void generateRTGSReconcillationReports(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();
            //To change the format
            /*String inputDt = getReportDto().getValueDate();
            inputDt = InstaReportUtil.formatDateString(inputDt);
            getReportDto().setValueDate(inputDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 200, 5, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
            //updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * To generate the report for the Inward Messages which are returned.
     */
    @SuppressWarnings("unchecked")
    public void generateInwardReturnReports(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();
            //To change the format
           /* String inputDt = getReportDto().getValueDate();
            inputDt = InstaReportUtil.formatDateString(inputDt);
            getReportDto().setValueDate(inputDt);*/


            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 200, 6, new Object[] {reportDto,
                                                            userIfscId, userIfscCode});
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
            updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    //Method added by Mohana on 22-Sep-2009 for Future Dated Txns report
    /**
     * Method to get Future Dated txns  Report
     */
    public void generateFutureDatedTxnsReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);


            long ifscId = ((Long)getRequest().getSession().getAttribute(InstaClientULC.IFSCID)).longValue();
            String ifscCode = (String) getRequest().getSession().getAttribute(InstaClientULC.IFSCCODE);
            reportDto.setIfscCode(ifscCode);
            reportDto.setIfscId(ifscId);

            Message req = createMessage(sessionID, 201, 15, reportDto);
            Message res = handle(sessionID, req);
            reportMap = (Map<String, List<ReportDTO>>) res.info;
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    //Method added by Mohana on 23-Sep-2009 for Exception Report
    /**
     * Method to get Exception Report
     */
    public void generateExceptionReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);
            Message req = createMessage(sessionID, 201, 16, reportDto);
            Message res = handle(sessionID, req);
            reportMap = (Map<String, List<ReportDTO>>) res.info;
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    //Method added on 20100111 to Display unsuccful txns Report
    /**
     * Method to Display unsuccful txns Report
     */
    public void generateUnsuccessfulPaymentReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);
            Message req = createMessage(sessionID, 201, 18, reportDto);
            Message res = handle(sessionID, req);
            reportDTOs = (List) res.info;
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     *     This method is to load the initial data which are required for loading the
     * report input page.
     *
     */
    @SuppressWarnings("unchecked")
    public void loadInitial(HttpServletRequest request) {

        try {

            inwardType = "";
            outwardType = "";
            inwardStatus = "";
            outwardStatus = "";
            String sessionID = request.getSession().getId();

            //To check the logged in user is COUser
            isCentralOffice(request);

            if(isCentralOffice(request) == 1) {
                if(!"UTRNumberwiseReport".equalsIgnoreCase(report)) {
                    userIfscCode = "All-All Branches";
                    userIfscId = 0;
                } else {
//                  Logged in user's Ifsc code
                    userIfscCode = (String) request.getSession()
                                        .getAttribute(InstaClientULC.IFSCCODE);
                    userIfscId = ((Long) request.getSession()
                                        .getAttribute(InstaClientULC.IFSCID)).longValue();
                }
            } else {

                //Logged in user's Ifsc code
                userIfscCode = (String) request.getSession()
                                    .getAttribute(InstaClientULC.IFSCCODE);
                userIfscId = ((Long) request.getSession()
                                    .getAttribute(InstaClientULC.IFSCID)).longValue();
            }

            String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());

            Message req = null;
            Message res = null;

            if (haveBranchField) {

                req = createMessage(sessionID, 190, 2, null);
                res = handle(sessionID, req);
                //formatValuDate();  //Have done With date format dd-MMM-yyyy.
                hostBranchList = (List) res.info;
                //updateDateForamt(); //After BO call

                int size = hostBranchList.size();
                HostIFSCMasterDTO _dto;

                for (int i = 0; i < size; i++) {

                    _dto = hostBranchList.get(i);

                    if(_dto.getHostIFSCMasterVO().getIfscCode().equalsIgnoreCase(getUserIfscCode())){

                        reportDto.setReceiverIfscId(Long.parseLong(_dto.getHostIFSCMasterVO().getIfscId()));
                        reportDto.setSenderIfscId(Long.parseLong(_dto.getHostIFSCMasterVO().getIfscId()));
                        reportDto.setCounterPartyIfscId(Long.parseLong(_dto.getHostIFSCMasterVO().getIfscId()));
                        reportDto.setIfscId(Long.parseLong(_dto.getHostIFSCMasterVO().getIfscId()));
                        break;
                     }
                }

              /*if (!(("InwardTxnsReport").equalsIgnoreCase(report))) {

                _dto = new HostIFSCMasterDTO();
                _dto.setHostIFSCMasterVO(new HostIFSCMasterVO());
                _dto.getHostIFSCMasterVO().setBranchName("All Branches");
                _dto.getHostIFSCMasterVO().setIfscCode("All");
                _dto.getHostIFSCMasterVO().setIfscId("0");
                hostBranchList.add(0,_dto); //Modified by priyak for requirement.
              }*/

               /* req = createMessage(sessionID, 190, 2, null);
                res = handle(sessionID, req);
                setHostBranchList((List) res.info);*/

                if (isInwardSpecific || isOutwardSpecific) {

                    req = createMessage(sessionID, 190, 3, null);
                    res = handle(sessionID, req);
                   formatValueDate();  //Have done With date format dd-MMM-yyyy.

                    bankList = (List)((Object[]) res.info)[0];
                    //updateDateForamt(); //After BO call
                    //nonHostBranchList = (List)((Object[]) res.info)[1];
                }
            }

            if (haveDateField) {

                getReportDto().setValueDate(ConversionUtil.getFormat(appDate));
                getReportDto().setToDate(ConversionUtil.getFormat(appDate));  //Have done with date format dd-MMM-yyyy
            }

            if (haveHostTypeField) {

                req = createMessage(sessionID, 190, 4, null);
                res = handle(sessionID, req);
                formatValueDate();  //Have done With date format dd-MMM-yyyy.
                hostList = (List) res.info;
                //updateDateForamt(); //After BO call
            }

            if (haveAmountField) {
                this.reportDto.setFromAmount("0.0");
                this.reportDto.setToAmount("0.0");
            }

            if (haveMsgSubTypeField) {

                DisplayValueReportDTO dto = new DisplayValueReportDTO();
                setSubTypeList(new ArrayList(0));
                setInwardTypeList(new ArrayList(0));
                setOutwardTypeList(new ArrayList(0));

                dto = new DisplayValueReportDTO();
                dto.setValue("ALL");
                dto.setDisplayValue("ALL Payments");
                getSubTypeList().add(dto);
                getInwardTypeList().add(dto);
                getOutwardTypeList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("R41");
                dto.setDisplayValue("Customer Payment");
                getSubTypeList().add(dto);
                getInwardTypeList().add(dto);
                getOutwardTypeList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("R42");
                dto.setDisplayValue("InterBank Payment");
                getSubTypeList().add(dto);
                getInwardTypeList().add(dto);
                getOutwardTypeList().add(dto);

                if (!(report.equalsIgnoreCase("BrSubTypeIndividual") || report.equalsIgnoreCase("unsuccessfulPayment"))) {

                    dto = new DisplayValueReportDTO();
                    dto.setValue("R10");
                    dto.setDisplayValue("OATR Payment");
                    getSubTypeList().add(dto);
                    getOutwardTypeList().add(dto);

                    dto = new DisplayValueReportDTO();
                    dto.setValue("R43");
                    dto.setDisplayValue("Debit Notification");
                    getSubTypeList().add(dto);
                    getInwardTypeList().add(dto);

                    dto = new DisplayValueReportDTO();
                    dto.setValue("R44");
                    dto.setDisplayValue("Credit Notification");
                    getSubTypeList().add(dto);
                    getInwardTypeList().add(dto);
                }
            }

            if (haveStatusField) {

                DisplayValueReportDTO dto = new DisplayValueReportDTO();
                setStatusList(new ArrayList(0));
                setInwardStatusList(new ArrayList(0));
                setOutwardStatusList(new ArrayList(0));

                /*
                 *   Here the value field holds 2 status values. It means first value is
                 * mentioning inward status and second value is mentioning outward status.
                 *
                 *   If the field dont have respective status for the transaction, then
                 * give the value as 0.
                 *
                 */
                dto.setValue("200, 0");
                dto.setDisplayValue("Entry");
                getStatusList().add(dto);
                getInwardStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("300, 2100");
                dto.setDisplayValue("ForAuthorization");
                getStatusList().add(dto);
                getInwardStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("500, 0");
                dto.setDisplayValue("ForReturnAuthorization");
                getStatusList().add(dto);
                getInwardStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("0, 2400");
                dto.setDisplayValue("ForTreasuryAuthorization");
                getStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("0, 2300");
                dto.setDisplayValue("ForRelease");
                getStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("0, 2500");
                dto.setDisplayValue("ForAcknowledge");
                getStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("0, 2600");
                dto.setDisplayValue("ForSettlement");
                getStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("900, 2800");
                dto.setDisplayValue("Returned/Cancelled");
                getStatusList().add(dto);
                getInwardStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("1000, 3000");
                dto.setDisplayValue("Completed/Settled");
                getStatusList().add(dto);
                getInwardStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("700, 2700");
                dto.setDisplayValue("Error");
                getStatusList().add(dto);
                getInwardStatusList().add(dto);
                getOutwardStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("0, 2900");
                dto.setDisplayValue("UnSuccessful");
                getStatusList().add(dto);
                getOutwardStatusList().add(dto);

            }

            if (haveTranTypeField) {

                DisplayValueReportDTO dto = new DisplayValueReportDTO();
                setTranTypeList(new ArrayList(0));

                dto = new DisplayValueReportDTO();
                dto.setValue("All");
                dto.setDisplayValue("Both");
                getTranTypeList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("inward");
                dto.setDisplayValue("Inward");
                getTranTypeList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("outward");
                dto.setDisplayValue("Outward");
                getTranTypeList().add(dto);
            }

            if (haveCounterPartyFld) {

//                nonHostBranchList = new ArrayList<HostIFSCMasterDTO>(0);
//
//                req = createMessage(sessionID, 190, 5, null);
//                res = handle(sessionID, req);
//
//                nonHostBranchList = (List) res.info;
                req = createMessage(sessionID, 190, 3, null);
                res = handle(sessionID, req);
                formatValueDate();  //Have done With date format dd-MMM-yyyy.

                bankList = (List)((Object[]) res.info)[0];
                //updateDateForamt() //After BO call
            }

            if(haveUserField) {
                if(userIdList != null)
                userIdList.clear();
                if (getReportDto().getIfscId() == 0) {
                    getUserIdByLocation();
                } else {

                    String brCode = getBranchIFSCCode(String.valueOf(getReportDto().getIfscId()));
                    getUserIdByLocation(brCode);
                    if (!brCode.equalsIgnoreCase(InstaDefaultConstants.COIFSCCode.substring(7, 11))) {
                        getUserIdByLocation(InstaDefaultConstants.COIFSCCode.substring(7, 11));
                    }
                }
            }
        } catch(Exception e) {

            logger.error("Exception ocurred while loading the input details for report :"
                         + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * This method is used to format the input date into dd-MMM-yyyy.
     */
    public void formatValueDate() {
        getReportDto().setValueDate(getReportDto().getValueDate()); //Modified for getting app date.
    }

    /**
     * This method is used to get the list of users
     *@throws BeanException
     */
    @SuppressWarnings("unchecked")
    public void getUserIdByLocation()
    throws  BeanException {

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

                    userIds.add(wsUserDTO.getUserVO().getLocation()+"-"+wsUserDTO.getUserVO().getId());
                }
            }

            List usrNames = new ArrayList(5);
            for (int i = 0; i < userIds.size(); i++) {

                String usrId = (String)userIds.get(i);

//                String branchCode = ((HostIFSCDetailsValueObject)
//                                    userDTO.masterDTO.ifscMasterVO).getBranchCode();

                String branchCode = getReportDto().getBranchCode();
                if (branchCode != null) {
                    defaultUser = branchCode + "A";
                }
//                String Id = usrId.substring(11, usrId.length());
                if (!(defaultUser.equalsIgnoreCase(usrId)))
                    usrNames.add(usrId);

            }
            this.setUserIdList(usrNames);

        } catch (Exception e) {
            logger.error("Exception while getting user by location", e);
            throw new BeanException(e.getMessage());
        }
    }

    /**
     * This method is used to get the list of users by locationId
     * @param branchId
     *@throws BeanException
     */
    public void getUserIdByLocation(String branchId)
    throws BeanException {

        try {

            List userIds = new ArrayList(1);

            List<WSUserDTO> users = getUsersByLocation(branchId);
            if (users != null) {
                for (WSUserDTO wsUserDTO : users) {
                    if (null != wsUserDTO && null != wsUserDTO.getUserVO()) {
                        userIds.add(wsUserDTO.getUserVO().getLocation()+"-"+wsUserDTO.getUserVO().getId());
                    }
                }
            }
            if (userIdList == null) {
                userIdList = new ArrayList();
                    this.setUserIdList(userIds);
            } else {
                userIdList.addAll(userIds);
            }
         } catch (Exception e) {
            logger.error("Exception while getting user by Location", e);
            throw new BeanException(e.getMessage());
        }

    }

    /**
     * Method to get users of a particular branch
     */
    public void getUserIdByBranch(String branchId)
    throws BeanException {

        try {

            List userIds = new ArrayList(1);

            List<WSUserDTO> users = getUsersByLocation(branchId);
            if (users != null) {
                for (WSUserDTO wsUserDTO : users) {
                    if (null != wsUserDTO && null != wsUserDTO.getUserVO()) {
                        userIds.add(wsUserDTO.getUserVO().getLocation()+"-"+wsUserDTO.getUserVO().getId());
                    }
                }
            }

            userIdList = new ArrayList();
            this.setUserIdList(userIds);

         } catch (Exception e) {
            logger.error("Exception while getting user by Location", e);
            throw new BeanException(e.getMessage());
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

    /**
     * This method is to get the Non Host Brnach List.
     *
     */
    public void getNonHostBranchList(HttpServletRequest request) {

        String bankCode = "";
        String sessionID = request.getSession().getId();
        nonHostBranchList = new ArrayList<HostIFSCMasterDTO>(0);

        if (reportDto.getReceiverBank()!=null && !reportDto.getReceiverBank().trim().equals("")
                                       && !reportDto.getReceiverBank().trim().equalsIgnoreCase("ALL")){

            bankCode = reportDto.getReceiverBank();
        } else if (reportDto.getSenderBank()!=null && !reportDto.getSenderBank().trim().equals("")
                                       && !reportDto.getSenderBank().trim().equalsIgnoreCase("ALL")){

            bankCode = reportDto.getSenderBank();
        } else if (reportDto.getCounterPartyBank()!=null && !reportDto.getCounterPartyBank().trim().equals("")
                                                && !reportDto.getCounterPartyBank().trim().equalsIgnoreCase("ALL")){

            bankCode = reportDto.getCounterPartyBank();
        }

        Message req = createMessage(sessionID, 190, 5, bankCode);
        Message res = handle(sessionID, req);
        formatValueDate();  //Have done With date format dd-MMM-yyyy.

        nonHostBranchList = (List) res.info;
        //updateDateForamt(); //After BO call
    }

    /**
     * TODO
     */
    public int isCentralOffice(HttpServletRequest request) {

        String ifscCode = (String) request.getSession().getAttribute(InstaClientULC.IFSCCODE);
        isCOUser = (ifscCode!=null && ifscCode.equalsIgnoreCase(InstaDefaultConstants.COIFSCCode)) ? 1 : 0;
        return isCOUser;
    }

    /**
     * Reset the values
     */
    public void resetBean() {

        this.reportDto = new ReportInputDTO();
        inwardType = "";
        outwardType = "";
        inwardStatus = "";
        outwardStatus = "";
    }

    /**
     * Method to generated Graduated Payment Report
     * @param HttpServletRequest
     */
    public void generateGraduatedPaymentReport(HttpServletRequest request, boolean datewise) {

        try {

            String sessionID = request.getSession().getId();

            if(datewise) {

                String inputDt = getReportDto().getValueDate();

                if(inputDt == null || inputDt.trim().length() == 0) {

                    throw new CrudException("Please input Value Date and then click Submit.");
                }

                if(inputDt != null && inputDt.length() == 10) {

                    inputDt = InstaReportUtil.formatDateString(inputDt);
                }

                getReportDto().setValueDate(inputDt);
                //getReportDto().setValueDate(InstaReportUtil.reportDisplayDateFormat(inputDt)); //For date formate
            } else {

                InstaAppClientEnvironment appEnv = (InstaAppClientEnvironment)env.getParentEnvironment();
                getReportDto().setValueDate(appEnv.appDate);
            }

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 200, 7, new Object[] {reportDto});
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
            updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : " + e.getMessage());
            throw new CrudException(e.getMessage());
        }
    }

    /**
     * Method to generated Inward Possible Return report
     * @param HttpServletRequest
     * @author Eswaripriyak
     */
    public void generateRTGSPossibleReturnReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();


            String inputDt = getReportDto().getValueDate();

            if(inputDt == null || inputDt.trim().length() == 0) {

                throw new CrudException("Please input Value Date and then click Submit.");
            }

            if(inputDt != null && inputDt.length() == 10) {

                inputDt = InstaReportUtil.formatDateString(inputDt);
            }

            getReportDto().setValueDate(inputDt);
            //getReportDto().setValueDate(InstaReportUtil.reportDisplayDateFormat(inputDt)); //For date formate

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 14, new Object[] {reportDto});
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
            updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the RTGS Inward Return Report report : " + e.getMessage());
            throw new CrudException(e.getMessage());
        }
    }
    /**
     * TODO
     */
    public void generateBrIndividualReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            //To convert the Input Date Format
            /*String inputFromDt = getReportDto().getValueDate();
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/
  			getReportDto().setStatusValue(getReportStatusValue(reportDto.getStatus(), statusList)); //To set the status value.
            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 1, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportMap = (Map<String, List<ReportDTO>>) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the BrIndividualReport : "
                         + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    public void generateBrSummaryReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            /*
             * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
             * Assumption : FromDate and ToDate fileds will have values always.
             * It will not be null or empty and the expected format dd-mm-yyyy.
             */
            /*String inputFromDt = getReportDto().getValueDate(); //Commented by priyak for date format dd-MMM-yyyy
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/
            getReportDto().setStatusValue(getReportStatusValue(reportDto.getStatus(), statusList)); //To set the status value.

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 2, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportMap = (Map<String, List<ReportDTO>>) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Branch Summary Report : "
                         + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * This method is used to get the displayValue of the selected status code.
     * @param  statusCode
     *            contains statusCode as a string value
     * @param statusList
     *            Having statusCode and statusValue as a list
     * @return String
     * @author eSwaripriyak
     *
     */
    public String getReportStatusValue(String statusCode, List statusList) {

        String displayValue = "All";

        if (!statusCode.equalsIgnoreCase("All")) {

            for(Iterator i = statusList.iterator();i.hasNext();) {

                DisplayValueReportDTO dto = (DisplayValueReportDTO)i.next();
                if (statusCode.equals(dto.getValue())) {
                    displayValue = dto.getDisplayValue();
                }
            }
        }
        return displayValue;
    }

    /**
     * TODO
     */
    public void generateBrInwReturnedReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            /*
             * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
             * Assumption : FromDate and ToDate fileds will have values always.
             * It will not be null or empty and the expected format dd-mm-yyyy.
             */
            /*String inputFromDt = getReportDto().getValueDate(); //Commented by priyak for date format.
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 3, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportMap = (Map<String, List<ReportDTO>>) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Branch Inward Returned" +
                         " Report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     *
     * Currently this report is not used.
     */
    /*public void generateBrOutReturnedByReceiverReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            /*
             * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
             * Assumption : FromDate and ToDate fileds will have values always.
             * It will not be null or empty and the expected format dd-mm-yyyy.
             /
            String inputFromDt = getReportDto().getValueDate();
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 4, reportDto);
            Message res = handle(sessionID, req);

            reportDTOs = (List<CMsgDTO>) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Branch Outward Returned"
                         + " By User Report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }*/

    /**
     * TODO
     */
    public void generateInwardBankwiseReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            /*
             * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
             * Assumption : FromDate and ToDate fileds will have values always.
             * It will not be null or empty and the expected format dd-mm-yyyy.
             */
            /*String inputFromDt = getReportDto().getValueDate(); //Commented by priyak for date format.
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 5, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportMap = (Map<String, List<ReportDTO>>) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Branch Inward Bankwise"
                         + " Report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    public void generateOutwardBankwiseReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            /*
             * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
             * Assumption : FromDate and ToDate fileds will have values always.
             * It will not be null or empty and the expected format dd-mm-yyyy.
             */
           /* String inputFromDt = getReportDto().getValueDate();     //Commented by priyak for date format.
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 6, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportMap = (Map<String, List<ReportDTO>>) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Branch Outward Bankwise"
                         + " Report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    public void generateUTRNowiseReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            //If the login user is CO User, then set the ifsc id as 0.
            if(isCOUser==1) {

                reportDto.setIfscId(0);
            }
            Message req = createMessage(sessionID, 201, 7, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            setMessageDTO((CMsgDTO) res.info);
            System.out.println("messageDTO : " + messageDTO);
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Branch UTR No wise"
                         + " Report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    public void generateReconcilliationReportCPwise(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            /*
             * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
             * Assumption : FromDate and ToDate fileds will have values always.
             * It will not be null or empty and the expected format dd-mm-yyyy.
             */
            /*String inputFromDt = getReportDto().getValueDate(); commented by priyak for date format
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 8, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportMap = (Map) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Reconcilliation Report "
                         + "CounterParty wise : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    public void generateReconcilliationReportBranchwise(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            /*
             * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
             * Assumption : FromDate and ToDate fileds will have values always.
             * It will not be null or empty and the expected format dd-mm-yyyy.
             */
            /*String inputFromDt = getReportDto().getValueDate(); //Commented by priyak for date format
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 9, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportMap = (Map) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Reconcilliation Report "
                         + "Bank wise : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     *
     * Not Yet Decided...need to get the UsersList via WebServices.
     */
    public void generateBrInwRejectedByUser(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            /*
             * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
             * Assumption : FromDate and ToDate fileds will have values always.
             * It will not be null or empty and the expected format dd-mm-yyyy.
             */
            /*String inputFromDt = getReportDto().getValueDate(); //Commented by priyak for date format
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 10, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportDTOs = (List) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Reconcillation Report "
                         + "Bank wise : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * Method to get Branchwisesubtype individual report
     */
    public void generateBrSubTypeIndividual(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            /*
             * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
             * Assumption : FromDate and ToDate fileds will have values always.
             * It will not be null or empty and the expected format dd-mm-yyyy.
             */
           /* String inputFromDt = getReportDto().getValueDate();
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/
            getReportDto().setStatusValue(getReportStatusValue(reportDto.getStatus(), statusList)); //To set the status value.
            currentReportPrintTime = InstaReportUtil.formatDate(new Date(System.currentTimeMillis()),
                                                                currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 12, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Branchwise subType Individual Report "
                         + "Bank wise : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    public void generateIndividualTXNDetailsReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            //To convert the Input Date Format
        /*    String inputFromDt = getReportDto().getValueDate();   //Commented by priya for date format dd-MMM-yyyy
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/
            getReportDto().setStatusValue(getReportStatusValue(reportDto.getStatus(), statusList)); //To set the status value.
            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 11, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportMap = (Map<String, List<ReportDTO>>) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the Individual Transaction Details Report : "
                         + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    public String getCurrentReportPrintTime() {

        return currentReportPrintTime;
    }

    public void setCurrentReportPrintTime(String currentReportPrintTime) {

        this.currentReportPrintTime = currentReportPrintTime;
    }

    public String getPageForward() {

        return pageForward;
    }

    public void setPageForward(String pageForward) {

        this.pageForward = pageForward;
    }


    public int getDisableBranchDisplay() {

        return disableBranchDisplay;
    }


    public void setDisableBranchDisplay(int disableBranchDisplay) {

        this.disableBranchDisplay = disableBranchDisplay;
    }

    public ReportDTO getNewReportDto() {

        if (newReportDto==null)
            newReportDto = new ReportDTO();
        return newReportDto;
    }

    public void setNewReportDto(ReportDTO newReportDto) {

        this.newReportDto = newReportDto;
    }

    public Map getSubTypeMap() {

        return subTypeMap;
    }


    public void setSubTypeMap(Map subTypeMap) {

        this.subTypeMap = subTypeMap;
    }


    public Map getMsgSubTypeMap() {

        subTypeMap.put("298/R42", "R42");
        subTypeMap.put("298/R41", "R41");
        subTypeMap.put("both","both");
        return subTypeMap;

     }



    public String getTranType() {

        return tranType;
    }


    public void setTranType(String tranType) {

        this.tranType = tranType;
    }


    public List getBranchDTOs() {

        return branchDTOs;
    }


    public void setBranchDTOs(List branchDTOs) {

        this.branchDTOs = branchDTOs;
    }


//    public double getNetAmount() {
//
//        return netAmount;
//    }
//
//
//    public void setNetAmount(double netAmount) {
//
//        this.netAmount = netAmount;
//    }
//
//
//    public double getInwIprTotal() {
//
//        return inwIprTotal;
//    }
//
//
//    public void setInwIprTotal(double inwIprTotal) {
//
//        this.inwIprTotal = inwIprTotal;
//    }
//
//
//    public double getInwCprTotal() {
//
//        return inwCprTotal;
//    }
//
//
//    public void setInwCprTotal(double inwCprTotal) {
//
//        this.inwCprTotal = inwCprTotal;
//    }
//
//
//    public double getOutCprTotal() {
//
//        return outCprTotal;
//    }
//
//
//    public void setOutCprTotal(double outCprTotal) {
//
//        this.outCprTotal = outCprTotal;
//    }
//
//
//    public double getOutIprTotal() {
//
//        return outIprTotal;
//    }
//
//
//    public void setOutIprTotal(double outIprTotal) {
//
//        this.outIprTotal = outIprTotal;
//    }
    public String getNetAmount() {

        return netAmount;
    }


    public void setNetAmount(String netAmount) {

        this.netAmount = netAmount;
    }


    public String getInwIprTotal() {

        return inwIprTotal;
    }


    public void setInwIprTotal(String inwIprTotal) {

        this.inwIprTotal = inwIprTotal;
    }


    public String getInwCprTotal() {

        return inwCprTotal;
    }


    public void setInwCprTotal(String inwCprTotal) {

        this.inwCprTotal = inwCprTotal;
    }


    public String getOutCprTotal() {

        return outCprTotal;
    }


    public void setOutCprTotal(String outCprTotal) {

        this.outCprTotal = outCprTotal;
    }


    public String getOutIprTotal() {

        return outIprTotal;
    }


    public void setOutIprTotal(String outIprTotal) {

        this.outIprTotal = outIprTotal;
    }

    public BigDecimal getLiquidityamt() {

        return liquidityamt;
    }


    public void setLiquidityamt(BigDecimal liquidityamt) {

        this.liquidityamt = liquidityamt;
    }


    public String getDebitAmt() {

        return debitAmt;
    }


    public void setDebitAmt(String debitAmt) {

        this.debitAmt = debitAmt;


    }


    public String getCreditAmt() {

        return creditAmt;
    }


    public void setCreditAmt(String creditAmt) {

        this.creditAmt = creditAmt;
    }

    public String getAccNo() {

        return accNo;
    }


    public void setAccNo(String accNo) {

        this.accNo = accNo;
    }


    public void resetAll() {

        this.getNewReportDto().tranType = null;
        this.getNewReportDto().msgSubType = null;
//        this.getNewReportDto().fromAmount = 0;
//        this.getNewReportDto().toAmount = 0;
        this.getNewReportDto().fromAmount = "0";
        this.getNewReportDto().toAmount = "0";
        this.getNewReportDto().userId = null;
        this.getNewReportDto().setUtrNo(null);

    }
    public void resetData(){

          this.newReportDto = new ReportDTO();

    }

    public List getTransactionListForExcel() {
        List tranTypeList = new ArrayList(1);
        tranTypeList.add("outward");
        tranTypeList.add("inward");

        return tranTypeList;
      }

    public Map getMsgSubTypeMapForExcel() {

        subTypeMap.put("298/R42","R42");
        subTypeMap.put("298/R41","R41");
        return subTypeMap;
     }

    /**
     * TODO
     */
    @SuppressWarnings("unchecked")
    public void generateInwardDayEndReport(HttpServletRequest request) {

    try {
        String sessionID = request.getSession().getId();
        String inputDt = getNewReportDto().getValueDate();
        inputDt = InstaReportUtil.formatDateString(inputDt);
        getNewReportDto().setValueDate(inputDt);
        Message req = createMessage(sessionID, 200, 8, newReportDto);
        Message res = handle(sessionID, req);
        formatValueDate();  //Have done With date format dd-MMM-yyyy.
        newReportDto = (ReportDTO) res.info;
    //    setAccNo(newReportDto.getAccNo());
        this.branchDTOs = newReportDto.branchwiseDTOs;
        this.netAmount = newReportDto.dateGrantTolal;
      } catch(Exception e) {

        logger.error("Exception ocurred while getting the report : "+e.getMessage());
        throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    @SuppressWarnings("unchecked")
    public void generateOutwardDayEndReport(HttpServletRequest request) {

    try {
        String sessionID = request.getSession().getId();
        String inputDt = getNewReportDto().getValueDate();
        inputDt = InstaReportUtil.formatDateString(inputDt);
        getNewReportDto().setValueDate(inputDt);
        Message req = createMessage(sessionID, 200, 9, newReportDto);
        Message res = handle(sessionID, req);
        formatValueDate();  //Have done With date format dd-MMM-yyyy.
        newReportDto = (ReportDTO) res.info;
        this.branchDTOs = newReportDto.branchwiseDTOs;
        this.netAmount = newReportDto.dateGrantTolal;
      } catch(Exception e) {

        logger.error("Exception ocurred while getting the report : "+e.getMessage());
        throw new ServerException(e.getMessage());
        }
    }

    /**
     * TODO
     */
    @SuppressWarnings("unchecked")
    public void generateLiquidityMgtReport(HttpServletRequest request) {

    try {
        String sessionID = request.getSession().getId();
        String inputDt = getNewReportDto().getValueDate();
        inputDt = InstaReportUtil.formatDateString(inputDt);
        getNewReportDto().setValueDate(inputDt);
        Message req = createMessage(sessionID, 200, 10, newReportDto);
        Message res = handle(sessionID, req);
        formatValueDate();  //Have done With date format dd-MMM-yyyy.
        newReportDto = (ReportDTO) res.info;

        creditAmt = newReportDto.creditSettlement;
        debitAmt =  newReportDto.debitSettlement;
        inwCprTotal = newReportDto.dateCprInAmt;
        inwIprTotal = newReportDto.dateIprInAmt;
        outCprTotal = newReportDto.dateCprOutAmt;
        outIprTotal = newReportDto.dateIprOutAmt;
        creditTotal = new BigDecimal(creditAmt).add(new BigDecimal(inwCprTotal)).add(new BigDecimal(inwIprTotal));
        debitTotal = new BigDecimal(outCprTotal).add(new BigDecimal(outIprTotal)).add(new BigDecimal(debitAmt));
        liquidityamt = creditTotal.subtract(debitTotal);
      } catch(Exception e) {

        logger.error("Exception ocurred while getting the report : "+e.getMessage());
        throw new ServerException(e.getMessage());
        }
    }

    /**
     * Method to get Excel Sheet Report
     *
     */
    public void generateExcelSheetReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            String inputDt = getNewReportDto().getValueDate();
            inputDt = InstaReportUtil.formatDateString(inputDt);
            getNewReportDto().setValueDate(inputDt);

            Message req = createMessage(sessionID, 200, 11, newReportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            newReportDto = (ReportDTO) res.info;
            this.branchDTOs = newReportDto .branchwiseDTOs;
            this.netAmount = newReportDto .dateGrantTolal;
            this.tranType = newReportDto .tranType;
        } catch (Throwable e) {

            logger.error("Exception ocurred while getting the report : " + e.getMessage());

            throw new ServerException(e.getMessage());
        }
    }

    /**
     * To generate NEFT Reconcilication reports
     */
    public void generateNEFTReconcillationReports(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();
            //To change the format
            /*String inputDt = getReportDto().getValueDate();
            inputDt = InstaReportUtil.formatDateString(inputDt);
            getReportDto().setValueDate(inputDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 202, 1, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            newReportDto = (ReportDTO) res.info;
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the NEFTReconcillation Report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * Method used to export the list in to Excel
     */
    public void reconcillationExportToExcel(ServletOutputStream out)
    throws Exception {

        try {
            List exportXLS = new ArrayList(1);
            List consolList = new ArrayList(1);
            int rowCount = 0;
            long sno = 0;
            //Adding the items to a list
            ReconcileReportDTO dto = (ReconcileReportDTO)getNewReportDto();
            for (Iterator i = dto.getReconcileReportDTOs().iterator(); i.hasNext();) {
                ReconcileReportDTO reconcileList = (ReconcileReportDTO)i.next();
                exportXLS.add(reconcileList);
            }

            ConsolidatedReconcileReportDTO consolDto = dto.getConsolidatedReportDTO();
            for(Iterator itr = consolDto.getConsolidatedReportDTOs().iterator(); itr.hasNext();) {
                ConsolidatedReconcileReportDTO conDto = (ConsolidatedReconcileReportDTO)itr.next();
                consolList.add(conDto);
            }

            if (exportXLS.size() != 0) {

                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                book.setSheetName(0,"NEFT Reconciliation Reports",
                                  HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("NEFT Reconciliation Report");

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue(" ");
                cell = row.createCell((short)1);
                cell.setCellValue(" ");
                cell = row.createCell((short)2);
                cell.setCellValue("Reconciliation Report on");
                cell = row.createCell((short)3);
                cell.setCellValue(InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getValueDate()));

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)6);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 9; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Batch Time");
                            } else if (j == 2) {
                                cell.setCellValue("Gross Outward N06,N07(a)");
                            } else if (j == 3) {
                                cell.setCellValue("Add rescheduled from previous batch(b)");
                            } else if (j == 4) {
                                cell.setCellValue("Less rescheduled to next batch (c)");
                            } else if (j == 5) {
                                cell.setCellValue("Less Rejected N03, N09 (d)");
                            } else if (j == 6) {
                                cell.setCellValue("Net outward (e)");
                            } else if (j == 7) {
                                cell.setCellValue("Inward N02 (f)");
                            } else if (j == 8) {
                                cell.setCellValue("Aggregate for the batch (g)");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;
                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String batchTime = null;
                                if (((ReconcileReportDTO)exportXLS
                                .get(roww - 1)).getBatchTime() != null) {

                                    batchTime = ((ReconcileReportDTO)exportXLS
                                    .get(roww - 1)).getBatchTime();
                                } else {
                                    batchTime = ((ReconcileReportDTO)exportXLS
                                    .get(roww - 1)).getHeading();
                                }
                                cell.setCellValue(batchTime);
                            } else if (j == 2) {

//                                double outAmt = 0;
                                BigDecimal outAmt = BigDecimal.ZERO;

                                    outAmt = ((ReconcileReportDTO)exportXLS
                                    .get(roww - 1)).getGrossOutwardAmount().setScale(2);

                                cell.setCellValue(outAmt.toString());
                            } else if (j == 3) {

//                                double reshPreAmt = 0;
                                BigDecimal reshPreAmt = BigDecimal.ZERO;
                                reshPreAmt = ((ReconcileReportDTO)exportXLS
                                    .get(roww - 1)).getRescheduledPrevBatchAmt().setScale(2);
                                cell.setCellValue(reshPreAmt.toString());
                            } else if (j == 4) {

//                                double reshNextAmt = 0;
                                BigDecimal reshNextAmt = BigDecimal.ZERO;

                                reshNextAmt = ((ReconcileReportDTO)exportXLS
                                .get(roww - 1)).getRescheduledNextBatchAmt().setScale(2);
                                cell.setCellValue(reshNextAmt.toString());
                            } else if (j == 5) {

//                                double rejectAmt = 0;
                                BigDecimal rejectAmt = BigDecimal.ZERO;

                                rejectAmt = ((ReconcileReportDTO)exportXLS
                                .get(roww - 1)).getRejectedAmt().setScale(2);
                                cell.setCellValue(rejectAmt.toString());
                            } else if (j == 6) {

//                                double netOutamt = 0;
                                BigDecimal netOutamt = BigDecimal.ZERO;
                                netOutamt = ((ReconcileReportDTO)exportXLS
                                .get(roww - 1)).getNetOutwardAmt().setScale(2);
                                cell.setCellValue(netOutamt.toString());
                            } else if (j == 7) {

//                                double netInamt = 0;
                                BigDecimal netInamt = BigDecimal.ZERO;

                                netInamt = ((ReconcileReportDTO)exportXLS
                                .get(roww - 1)).getNetInwardAmt().setScale(2);
                                cell.setCellValue(netInamt.toString());
                            } else if (j == 8) {

//                                double aggAmt = 0;
                                BigDecimal aggAmt = BigDecimal.ZERO;

                                aggAmt = ((ReconcileReportDTO)exportXLS
                                .get(roww - 1)).getAggregateAmt().setScale(2);
                                cell.setCellValue(aggAmt.toString());
                            }
                       }
                        cell.setCellStyle(caption_style);
                    }
                }

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("");
                cell = row.createCell((short)1);
                cell.setCellValue("");
                cell = row.createCell((short)2);
                cell.setCellValue("Consolidated Report for the Day");

                if (consolList.size() > 0) {

                    for (int size = consolList.size(), rows = 0; rows <= size; rows++) {

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        for (short j = 0; j < 3; j++) {
                            cell = row.createCell(j);
                            if (rows == 0) {

                                if (j == 1) {
                                cell.setCellValue("RBI Account");
                                } else if (j == 2) {
                                    cell.setCellValue("NEFT Account");
                                }
                            } else {

                                if (j == 0) {
                                    String heading = null;

                                    heading = ((ConsolidatedReconcileReportDTO)consolList
                                        .get(rows - 1)).getHeading();
                                    cell.setCellValue(heading);
                                } else if (j == 1) {
//                                    double rbiAcc = 0;
                                    String rbiAcc = "0.00";

                                    rbiAcc = ((ConsolidatedReconcileReportDTO)consolList
                                        .get(rows - 1)).getRBIAccountAmt();
                                    cell.setCellValue(rbiAcc);
                                } else if (j == 2) {
//                                    double neftAcc = 0;
                                    String neftAcc = "0.00";

                                    neftAcc = ((ConsolidatedReconcileReportDTO)consolList
                                        .get(rows - 1)).getNEFTAccountAmt();
                                    cell.setCellValue(neftAcc);
                                }
                            }
                        }
                    }
                }
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }
    /**
     * method used for get the Branch IFSC Code
     * @param ifscmasterId
     * @return IFSC Code
     */
    public String getBranchIFSCCode(String ifscId ){

        List<HostIFSCMasterDTO> hostBranches = this.hostBranchList;
        int size = hostBranches.size();
        HostIFSCMasterDTO dto;
        HostIFSCMasterVO data;
        for (int i = 0; i < size; i++) {
            dto = hostBranches.get(i);
            data = dto.getHostIFSCMasterVO();
            if (ifscId.equals(data.getIfscId())){
                return data.getBranchCode();
            }
        }
        return "ALL";
    }

    public String getBranchIFSC(String ifscId ){

        List<HostIFSCMasterDTO> hostBranches = this.hostBranchList;
        int size = hostBranches.size();
        HostIFSCMasterDTO dto;
        HostIFSCMasterVO data;
        for (int i = 0; i < size; i++) {
            dto = hostBranches.get(i);
            data = dto.getHostIFSCMasterVO();
            if (ifscId.equals(data.getIfscId())){
                return data.getIfscCode();
            }
        }
        return "ALL";
    }

    public List getListOfTranType() {

        if(listOfTranType == null) {

            listOfTranType = new ArrayList(2);
            listOfTranType.add("outward");
            listOfTranType.add("inward");
        }
        return listOfTranType;
    }


    public void setListOfTranType(List listOfTranType) {

        this.listOfTranType = listOfTranType;
    }


    public BigDecimal getCreditTotal() {

        return creditTotal;
    }


    public void setCreditTotal(BigDecimal creditTotal) {

        this.creditTotal = creditTotal;
    }


    public BigDecimal getDebitTotal() {

        return debitTotal;
    }


    public void setDebitTotal(BigDecimal debitTotal) {

        this.debitTotal = debitTotal;
    }

    /**
     * Method for reset the inputfields
     */
    public void resetInput(){

        haveDateField        = false;
        haveTranTypeField    = false;
        haveMsgSubTypeField  = false;
        haveHostTypeField    = false;
        haveBranchField      = false;
        isInwardSpecific     = false;
        isOutwardSpecific    = false;
        haveUTRNoField       = false;
        haveAmountField      = false;
        haveStatusField      = false;
        haveCounterPartyFld  = false;
        haveUserField        = false;
        haveValueDateField    = false;
        haveBatchTimeField    = false;
        haveReportTypeField   = false;
        haveInwardTypeField   = false;
        haveFutureDateTxnStatus = false;
    }

    public boolean isHaveBatchTimeField() {

        return haveBatchTimeField;
    }

    public void setHaveBatchTimeField(boolean haveBatchTimeField) {

        this.haveBatchTimeField = haveBatchTimeField;
    }

    public boolean isHaveValueDateField() {

        return haveValueDateField;
    }

    public void setHaveValueDateField(boolean haveValueDateField) {

        this.haveValueDateField = haveValueDateField;
    }


    public boolean isHaveInwardTypeField() {

        return haveInwardTypeField;
    }


    public void setHaveInwardTypeField(boolean haveInwardTypeField) {

        this.haveInwardTypeField = haveInwardTypeField;
    }


    public boolean isHaveReportTypeField() {

        return haveReportTypeField;
    }


    public void setHaveReportTypeField(boolean haveReportTypeField) {

        this.haveReportTypeField = haveReportTypeField;
    }

    public String getReport() {

        return report;
    }


    public void setReport(String report) {

        this.report = report;
    }


    public List getUserIdList() {

        return userIdList;
    }


    public void setUserIdList(List userIdList) {

        this.userIdList = userIdList;
    }

    /**
     * Method used to export the r41 inward report in to Excel
     */
    public void generateR41InwardExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;
            BigDecimal totAmt = BigDecimal.ZERO;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "R41(CPN) Received",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Customer Payments Received - Grouped By Sender Address on " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getValueDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)3);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 5; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Sender Address");
                            } else if (j == 2) {
                                cell.setCellValue("Value Date");
                            } else if (j == 3) {
                                cell.setCellValue("No. of Txns");
                            } else if (j == 4) {
                                cell.setCellValue("Total Txn Amount(Rs.)");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String sendAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);
                            } else if (j == 2) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                    valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                }
                                cell.setCellValue(valueDate);

                            } else if (j == 3) {

                                long txnCount = 0;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getCount() != 0) {
                                    txnCount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getCount();
                                }
                                cell.setCellValue(txnCount);

                            } else if (j == 4) {

//                                double txnAmount = 0.0;
                                String txnAmount = "0.00";

                                if ( new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    txnAmount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }
                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                                totAmt = totAmt.add(new BigDecimal(txnAmount).setScale(2));
                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }
                row = sheet.createRow(rowCount);
                cell = row.createCell((short)3);
                cell.setCellValue("Total : ");
                cell = row.createCell((short)4);
                cell.setCellValue(totAmt.toString());
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the r41 outward report in to Excel
     */
    public void generateR41OutwardExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;
            BigDecimal totAmt = BigDecimal.ZERO;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "R41(CPR) Submitted",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Customer Payments Submitted - Grouped By Receiver Address on " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)3);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 5; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Value Date");
                            } else if (j == 2) {
                                cell.setCellValue("Receiver Address");
                            } else if (j == 3) {
                                cell.setCellValue("No. of Txns");
                            } else if (j == 4) {
                                cell.setCellValue("Total Txn Amount(Rs.)");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                    valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                }
                                cell.setCellValue(valueDate);

                            } else if (j == 2) {

                                String recAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);
                            }  else if (j == 3) {

                                long txnCount = 0;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getCount() != 0) {
                                    txnCount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getCount();
                                }
                                cell.setCellValue(txnCount);

                            } else if (j == 4) {

//                                double txnAmount = 0.0;
                                String txnAmount = "0.00";

                                if ( new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    txnAmount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }
                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                                totAmt = totAmt.add(new BigDecimal(txnAmount).setScale(2));
                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }
                row = sheet.createRow(rowCount);
                cell = row.createCell((short)3);
                cell.setCellValue("Total : ");
                cell = row.createCell((short)4);
                cell.setCellValue(totAmt.toString());
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the r42 inward report in to Excel
     */
    public void generateR42InwardExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;
            BigDecimal totAmt = BigDecimal.ZERO;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "R42(IPN) Received",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Interbank Payments Received - Grouped By Sender Address on " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getValueDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)3);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 5; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Sender Address");
                            } else if (j == 2) {
                                cell.setCellValue("Value Date");
                            } else if (j == 3) {
                                cell.setCellValue("No. of Txns");
                            } else if (j == 4) {
                                cell.setCellValue("Total Txn Amount(Rs.)");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String sendAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);
                            } else if (j == 2) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                    valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                }
                                cell.setCellValue(valueDate);
                            } else if (j == 3) {

                                long txnCount = 0;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getCount() != 0) {
                                    txnCount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getCount();
                                }
                                cell.setCellValue(txnCount);

                            } else if (j == 4) {

//                                double txnAmount = 0.0;
                                String txnAmount = "0.00";
//                                if (((ReportDTO)exportXLS
//                                .get(roww - 1)).getAmount() != 0.0) {
                                if ( new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    txnAmount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }
                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                                totAmt = totAmt.add(new BigDecimal(txnAmount).setScale(2));
                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }
                row = sheet.createRow(rowCount);
                cell = row.createCell((short)3);
                cell.setCellValue("Total : ");
                cell = row.createCell((short)4);
                cell.setCellValue(totAmt.toString());
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the r42 outward report in to Excel
     */
    public void generateR42OutwardExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;
            BigDecimal totAmt = BigDecimal.ZERO;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "R42(IPR) Submitted",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Interbank Payments Submitted - Grouped By Receiver Address on " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)3);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 5; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Receiver Address");
                            } else if (j == 2) {
                                cell.setCellValue("VALUE DATE");
                            } else if (j == 3) {
                                cell.setCellValue("No. of Txns");
                            } else if (j == 4) {
                                cell.setCellValue("Total Txn Amount(Rs.)");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String recAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);
                            } else if (j == 2) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                    valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                }
                                cell.setCellValue(valueDate);

                            } else if (j == 3) {

                                long txnCount = 0;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getCount() != 0) {
                                    txnCount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getCount();
                                }
                                cell.setCellValue(txnCount);

                            } else if (j == 4) {

//                                double txnAmount = 0.0;
                                String txnAmount = "0.00";

                                if ((new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO)) != 0.0) {
                                    txnAmount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }
                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                                totAmt = totAmt.add(new BigDecimal(txnAmount).setScale(2));
                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }
                row = sheet.createRow(rowCount);
                cell = row.createCell((short)3);
                cell.setCellValue("Total : ");
                cell = row.createCell((short)4);
                cell.setCellValue(totAmt.toString());
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the Graduated Payment report in to Excel
     */
    public void generateGraduatedPaymentExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Graduated Payment Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Graduated Payment Report on "); //Heading modified
                cell = row.createCell((short)1);
                cell.setCellValue(InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)7);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 10; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("UTR No");
                            } else if (j == 2) {
                                cell.setCellValue("Msg Type");
                            } else if (j == 3) {
                                cell.setCellValue("Tran Type");
                            } else if (j == 4) {
                                cell.setCellValue("Sender Address");
                            } else if (j == 5) {
                                cell.setCellValue("Receiver Address");
                            } else if (j == 6) {
                                cell.setCellValue("Credit Amount (Rs.)");
                            } else if (j == 7) {
                                cell.setCellValue("Debit Amount (Rs.)");
                            } else if (j == 8) {
                                cell.setCellValue("Balance (Rs.)");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String utrNo = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getUtrNo() != null) {
                                    utrNo = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo();
                                }
                                cell.setCellValue(utrNo);
                            } else if (j == 2) {

                                String msgType = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getMsgType() != null) {
                                    msgType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getMsgType();
                                }
                                cell.setCellValue(msgType);
                            } else if (j == 3) {

                                String tranType = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getTranType() != null) {
                                    tranType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getTranType();
                                }
                                cell.setCellValue(tranType);
                            } else if (j == 4) {

                                String sendAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);

                            } else if (j == 5) {

                                String recAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);
                            } else if (j == 6) {

                                String debitCredit = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getDebitCredit() != null) {
                                    debitCredit = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getDebitCredit();
                                }
                                String txnAmount = "0.00";
                                if (new BigDecimal(((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    if(debitCredit != null && debitCredit.equalsIgnoreCase("credit")) {

                                        txnAmount = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getAmt();
                                    }
                                }

                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                            } else if (j == 7) {

                                String debitCredit = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getDebitCredit() != null) {
                                    debitCredit = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getDebitCredit();
                                }
                                String txnAmount = "0.00";
                                if (new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {

                                    if(debitCredit != null && debitCredit.equalsIgnoreCase("debit")) {

                                        txnAmount = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getAmt();
                                    }
                                }

                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                            } else if (j == 8) {

                                String balance = "0.00";

                                if ( new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getBalance()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    balance = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getBalance();
                                }
                                cell.setCellValue(new BigDecimal(balance).setScale(2).toString());
                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export RTGS Inward possible Return Report
     * @author Eswaripriyak
     */
    public void generateInwardPossibleReturnReportExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "RTGS Possible Return Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("RTGS Inward possible Return Report on "+InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate())); //Heading modified
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)4);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;

                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 10; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("UTR No");
                            } else if (j == 2) {
                                cell.setCellValue("Transaction Type");
                            } else if (j == 3) {
                                cell.setCellValue("Sender Address");
                            } else if (j == 4) {
                                cell.setCellValue("Receiver Address");
                            } else if (j == 5) {
                                cell.setCellValue("Amount (Rs.)");
                            } else if (j == 6) {
                                cell.setCellValue("Business date");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String utrNo = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getUtrNo() != null) {
                                    utrNo = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo();
                                }
                                cell.setCellValue(utrNo);
                            } else if (j == 2) {

                                String tranType = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getTranType() != null) {
                                    tranType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getTranType();
                                }
                                cell.setCellValue(tranType);
                            } else if (j == 3) {

                                String sendAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);

                            } else if (j == 4) {

                                String recAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);
                            } else if (j == 5) {

                                String amount = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt() != null) {
                                    amount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }

                                cell.setCellValue(new BigDecimal(amount).setScale(2).toString());
                            } else if (j == 6) {

                                String date = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    date = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                }

                                cell.setCellValue(date);
                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file for RTGS inward Possible Return Report"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export RTGS Inward possible Return Payment Rejected by user Report
     * @author MohanaDevis
     */
    public void returnPaymentRejectedReportExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "RTGS Return Payment Rejected",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("RTGS Inward Possible Return Payment Rejected By User Report between "
                                  +InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate())+ " and "+
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getToDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)4);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;

                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 10; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("UTR No");
                            } else if (j == 2) {
                                cell.setCellValue("Transaction Type");
                            } else if (j == 3) {
                                cell.setCellValue("Rejected By");
                            } else if (j == 4) {
                                cell.setCellValue("Sender Address");
                            } else if (j == 5) {
                                cell.setCellValue("Receiver Address");
                            } else if (j == 6) {
                                cell.setCellValue("Amount (Rs.)");
                            } else if (j == 7) {
                                cell.setCellValue("Business date");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String utrNo = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getUtrNo() != null) {
                                    utrNo = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo();
                                }
                                cell.setCellValue(utrNo);
                            } else if (j == 2) {

                                String tranType = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getTranType() != null) {
                                    tranType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getTranType();
                                }
                                cell.setCellValue(tranType);
                            } else if (j == 3) {

                                String userId = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getUserId()!= null) {
                                    userId = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getUserId();
                                }
                                cell.setCellValue(userId);

                            } else if (j == 4) {

                                String sendAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);

                            } else if (j == 5) {

                                String recAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);
                            } else if (j == 6) {

                                String amount = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt() != null) {
                                    amount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }

                                cell.setCellValue(new BigDecimal(amount).setScale(2).toString());
                            } else if (j == 7) {

                                String date = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    date = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                }

                                cell.setCellValue(date);
                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file for RTGS inward Possible Return Payment rejected Report"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }


    /**
     * Method used to export the Individual Txn Details report in to Excel
     */
    public void generateIndividualTXNDetailsExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;
//            double inwTotTxnAmt = 0;
//            double owTotTxnAmt = 0;
            BigDecimal inwTotTxnAmt = BigDecimal.ZERO;
            BigDecimal owTotTxnAmt = BigDecimal.ZERO;

            Set set = getReportMap().entrySet();
            //Adding the items to a list
            for (Iterator i = set.iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                List dtoList = (List) entry.getValue();
                exportXLS.addAll(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Transaction Detailed Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Individual Transaction Detailed Report from " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate())+ " with status "+
                                  getReportDto().getStatusValue());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)8);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;

                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 11; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Value Date");
                            } else if (j == 2) {
                                cell.setCellValue("Host");
                            } else if (j == 3) {
                                cell.setCellValue("Trans Type");
                            }  else if (j == 4) {
                                cell.setCellValue("Msg Type");
                            } else if (j == 5) {
                                cell.setCellValue("UTR No");
                            } else if (j == 6) {
                                cell.setCellValue("Sender Address");
                            } else if (j == 7) {
                                cell.setCellValue("Receiver Address");
                            } else if (j == 8) {
                                cell.setCellValue("Status");
                            }  else if (j == 9) {
                                cell.setCellValue("Inward Amount (Rs.)");
                            } else if (j == 10) {
                                cell.setCellValue("Outward Amount (Rs.)");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                    valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                }
                                cell.setCellValue(valueDate);
                            } else if (j == 2) {

                                String host = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSource() != null) {
                                    host = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSource();
                                }
                                cell.setCellValue(host);
                            } else if (j == 3) {

                                String tranType = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getTranType() != null) {
                                    tranType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getTranType();
                                }
                                cell.setCellValue(tranType);
                            } else if (j == 4) {

                                String msgType = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getMsgType() != null) {
                                    msgType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getMsgType();
                                }
                                cell.setCellValue(msgType);
                            } else if (j == 5) {

                                String utrNo = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getUtrNo() != null) {
                                    utrNo = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo();
                                }
                                cell.setCellValue(utrNo);
                            } else if (j == 6) {

                                String sendAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);

                            } else if (j == 7) {

                                String recAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);
                            } else if (j == 8) {

                                String status = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getStatus() != null) {
                                    status = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getStatus();
                                }
                                cell.setCellValue(status);
                            }  else if (j == 9) {

//                                double inwTxnAmount = 0.00;
                                String inwTxnAmount = "0.00";

                                // R43 is Debit Notification it should be in �outward amount� field - 20110713
                                if((((ReportDTO)exportXLS.get(roww - 1)).getTranType().equalsIgnoreCase("inward"))
                                	&& !(((ReportDTO)exportXLS.get(roww - 1)).getMsgType().equalsIgnoreCase("R43"))
                                	) {

                                    if (new BigDecimal(((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                        inwTxnAmount = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getAmt();
                                    }
                                    inwTotTxnAmt = inwTotTxnAmt.add(new BigDecimal(inwTxnAmount).setScale(2));
                                    cell.setCellValue(new BigDecimal(inwTxnAmount).setScale(2).toString());
                                }
                            } else if (j == 10) {

//                                double owTxnAmount = 0.0;
                                String owTxnAmount = "0.00";

                                // R43 is Debit Notification it should be in �outward amount� field - 20110713
                                if((((ReportDTO)exportXLS.get(roww - 1)).getTranType().equalsIgnoreCase("outward"))
                                	|| (((ReportDTO)exportXLS.get(roww - 1)).getMsgType().equalsIgnoreCase("R43"))
                                	) {

//                                    if (((ReportDTO)exportXLS
//                                    .get(roww - 1)).getAmount() != 0.0) {
                                    if ( new BigDecimal(((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                        owTxnAmount = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getAmt();
                                    }
                                    owTotTxnAmt = owTotTxnAmt.add(new BigDecimal(owTxnAmount).setScale(2));
                                    cell.setCellValue(new BigDecimal(owTxnAmount).setScale(2).toString());
                                }
                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }

                row = sheet.createRow(rowCount);
                cell = row.createCell((short)8);
                cell.setCellValue("Total : ");
                cell = row.createCell((short)9);
                cell.setCellValue(inwTotTxnAmt.toString());
                cell = row.createCell((short)10);
                cell.setCellValue(owTotTxnAmt.toString());
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the Future Dated Txns report in to Excel
     */
    public void futureDatedTxnExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            int rowCount = 0;
//          double grandInwTotTxnAmt = 0;
//          double grandOwTotTxnAmt = 0;
          BigDecimal grandInwTotTxnAmt = BigDecimal.ZERO;
          BigDecimal grandOwTotTxnAmt = BigDecimal.ZERO;

          //Only If the list is not empty
          if (getReportMap().size() != 0) {

              // start to export excel
              HSSFWorkbook book = new HSSFWorkbook();
              HSSFSheet sheet = book.createSheet();
              HSSFRow row = null;
              HSSFCell cell = null;
              HSSFFont caption_font = null;
              HSSFCellStyle caption_style = null;

                  book.setSheetName(0,
                                    "Future Dated Txns Report",
                                    HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

              caption_font = book.createFont();
              caption_font.setFontHeightInPoints((short)10);
              caption_font.setFontName("Verdana");
              caption_style = book.createCellStyle();
              caption_style.setFont(caption_font);
              row = sheet.createRow(rowCount);
              rowCount += 1;
              cell = row.createCell((short)0);
              String statusName = "";
              if (getReportDto().getStatus().equalsIgnoreCase("2000")) {
                  statusName = "Active";
              } else if (getReportDto().getStatus().equalsIgnoreCase("2050")) {
                  statusName = "Cancelled";
              }
              cell.setCellValue("RTGS  Date wise Future Dated Txns Report from " +
                                InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()) + " with status " +
                                statusName);
              row = sheet.createRow(rowCount);
              rowCount += 1;
              cell = row.createCell((short)6);
              String dateForm = currentReportPrintTime.substring(0,11);
              String time = currentReportPrintTime.substring(11);
              cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
              rowCount += 1;
              Set set = getReportMap().entrySet();
              for (Iterator z = set.iterator(); z.hasNext();) {

                  Map.Entry<String, List<ReportDTO>> entry = (Map.Entry<String, List<ReportDTO>>)z.next();

                  List exportXLS = new ArrayList(1);
                  exportXLS.addAll(entry.getValue());

                  if(exportXLS.size() > 0) {

                      String date = entry.getKey();

                      row = sheet.createRow(rowCount);
                      rowCount += 1;
                      cell = row.createCell((short)0);
                      cell.setCellValue("Date : " + date);
                      BigDecimal inwTotTxnAmt = BigDecimal.ZERO;
                      long sno = 0;

                      for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                          row = sheet.createRow(rowCount);
                          rowCount += 1;
                          for (short j = 0; j < 11; j++) {
                              cell = row.createCell(j);

                              // for header
                              if (roww == 0) {
                                  if (j == 0) {
                                      cell.setCellValue("S.No");
                                  } else if (j == 1) {
                                      cell.setCellValue("Value Date");
                                  } else if (j == 2) {
                                      cell.setCellValue("Msg Type");
                                  } else if (j == 3) {
                                      cell.setCellValue("UTR Number");
                                  }  else if (j == 4) {
                                      cell.setCellValue("Sender Address");
                                  } else if (j == 5) {
                                      cell.setCellValue("Receiver Address");
                                  } else if (j == 6) {
                                      if(getReportDto().getPaymentType().equalsIgnoreCase("R41")) {
                                          cell.setCellValue("Account No");
                                      } else {
                                          cell.setCellValue("Info");
                                      }
                                  } else if (j == 7) {
                                      if(getReportDto().getPaymentType().equalsIgnoreCase("R41")) {
                                          cell.setCellValue("Beneficiary Details");
                                      } else {
                                          cell.setCellValue("Additional Info");
                                      }
                                  } else if (j == 8) {
                                      cell.setCellValue("Entry By");
                                  } else if (j == 9) {
                                      cell.setCellValue("Cancelled By");
                                  } else if (j == 10) {
                                      cell.setCellValue("Amount");
                                  }
                              } else {
                                  // Setting values in cell for each and every row
                                  if (j == 0) {

                                      String no = null;
                                      sno += 1;
                                      no = String.valueOf(sno);
                                      cell.setCellValue(no);
                                  } else if (j == 1) {

                                      String valueDate = null;
                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getValueDate() != null) {
                                          valueDate = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getValueDate();
                                          valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                      }
                                      cell.setCellValue(valueDate);
                                  } else if (j == 2) {

                                      String msgType = null;
                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getMsgSubType() != null) {
                                          msgType = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getMsgSubType();
                                      }
                                      cell.setCellValue(msgType);
                                  } else if (j == 3) {

                                      String utrNo = null;
                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getUtrNo() != null) {
                                          utrNo = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getUtrNo();
                                      }
                                      cell.setCellValue(utrNo);
                                  } else if (j == 4) {

                                      String sendAdd = null;

                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getSenderAddress() != null) {
                                          sendAdd = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getSenderAddress();
                                      }
                                      cell.setCellValue(sendAdd);

                                  } else if (j == 5) {

                                      String recAdd = null;

                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getReceiverAddress() != null) {
                                          recAdd = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getReceiverAddress();
                                      }
                                      cell.setCellValue(recAdd);
                                  } else if (j == 6) {

                                      if(getReportDto().getPaymentType().equalsIgnoreCase("R41")) {

                                          String a5561 = null;

                                          if (((ReportDTO)exportXLS
                                          .get(roww - 1)).getFieldA5561() != null) {
                                              a5561 = ((ReportDTO)exportXLS
                                              .get(roww - 1)).getFieldA5561();
                                          }
                                          cell.setCellValue(a5561);
                                      } else {

                                          String i7495 = null;

                                          if (((ReportDTO)exportXLS
                                          .get(roww - 1)).getFieldI7495() != null) {
                                              i7495 = ((ReportDTO)exportXLS
                                              .get(roww - 1)).getFieldI7495();
                                          }
                                          cell.setCellValue(i7495);
                                      }
                                  } else if (j == 7) {

                                      if(getReportDto().getPaymentType().equalsIgnoreCase("R41")) {

                                          String n5561 = null;

                                          if (((ReportDTO)exportXLS
                                          .get(roww - 1)).getFieldN5561() != null) {
                                              n5561 = ((ReportDTO)exportXLS
                                              .get(roww - 1)).getFieldN5561();
                                          }
                                          cell.setCellValue(n5561);
                                      } else {

                                          String a7495 = null;

                                          if (((ReportDTO)exportXLS
                                          .get(roww - 1)).getFieldA7495() != null) {
                                              a7495 = ((ReportDTO)exportXLS
                                              .get(roww - 1)).getFieldA7495();
                                          }
                                          cell.setCellValue(a7495);
                                      }
                                  } else if (j == 8) {

                                      String status = null;

                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getEntryBy() != null) {
                                          status = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getEntryBy();
                                      }
                                      cell.setCellValue(status);
                                  } else if (j == 9) {

                                      String status = null;

                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getPassBy() != null) {
                                          status = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getPassBy();
                                      }
                                      cell.setCellValue(status);
                                  } else if (j == 10) {

                                      String inwTxnAmount = "0.00";
                                      if ( new BigDecimal(((ReportDTO)exportXLS
                                          .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                              inwTxnAmount = ((ReportDTO)exportXLS
                                              .get(roww - 1)).getAmt();
                                          }
                                          inwTotTxnAmt = inwTotTxnAmt.add(new BigDecimal(inwTxnAmount).setScale(2));
                                          cell.setCellValue(new BigDecimal(inwTxnAmount).setScale(2).toString());

                                  }
                              }
                           cell.setCellStyle(caption_style);
                          }
                      }

                      row = sheet.createRow(rowCount);
                      rowCount += 1;
                      cell = row.createCell((short)9);
                      cell.setCellValue("Total ( Date : " + date + " ) ");
                      cell = row.createCell((short)10);
                      cell.setCellValue(inwTotTxnAmt.toString());
                      grandInwTotTxnAmt = grandInwTotTxnAmt.add(inwTotTxnAmt);
                      row = sheet.createRow(rowCount);
                      rowCount += 1;
                  }

              }
              row = sheet.createRow(rowCount);
              rowCount += 1;
              cell = row.createCell((short)9);
              cell.setCellValue("Total Amount : ");
              cell = row.createCell((short)10);
              cell.setCellValue(grandInwTotTxnAmt.toString());
              book.write(out);
              out.flush();
              out.close();
          }
        } catch (Exception e) {
            logger.error("Exception while exporting Future dtaed txns Report into Excel"+e.getMessage());
            throw new Exception("Exception while exporting Future dtaed txns Report into Excel"+e);
        }
    }

    /**
     * Method used to get return payemnt rejected by user report
     */
    public  void generateReturnPaymentRejectedReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);
            Message req = createMessage(sessionID, 201, 17, reportDto);
            Message res = handle(sessionID, req);
            reportDTOs = (List<ReportDTO>) res.info;
        } catch(Exception e) {

            logger.error("Exception ocurred while getting return payment rejected report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * Method to used to export the  Exception Report
     */
    public void exceptionReportExportToExcel(ServletOutputStream out,String channel)
    throws Exception {

        try {

            int rowCount = 0;
            BigDecimal grandInwTotTxnAmt = BigDecimal.ZERO;

            //Only If the list is not empty
            if (getReportMap().size() != 0) {

              // start to export excel
              HSSFWorkbook book = new HSSFWorkbook();
              HSSFSheet sheet = book.createSheet();
              HSSFRow row = null;
              HSSFCell cell = null;
              HSSFFont caption_font = null;
              HSSFCellStyle caption_style = null;

                  book.setSheetName(0,
                                    channel+" Exceptions Report",
                                    HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

              caption_font = book.createFont();
              caption_font.setFontHeightInPoints((short)10);
              caption_font.setFontName("Verdana");
              caption_style = book.createCellStyle();
              caption_style.setFont(caption_font);
              row = sheet.createRow(rowCount);
              rowCount += 1;
              cell = row.createCell((short)0);
              String statusName = "";

              cell.setCellValue(channel+" Exceptions Report from " +
                                InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
              row = sheet.createRow(rowCount);
              rowCount += 1;
              cell = row.createCell((short)5);
              String dateForm = currentReportPrintTime.substring(0,11);
              String time = currentReportPrintTime.substring(11);
              cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
              rowCount += 1;
              Set set = getReportMap().entrySet();
              for (Iterator z = set.iterator(); z.hasNext();) {

                  Map.Entry<String, List<ReportDTO>> entry = (Map.Entry<String, List<ReportDTO>>)z.next();

                  List exportXLS = new ArrayList(1);
                  exportXLS.addAll(entry.getValue());

                  if(exportXLS.size() > 0) {

                      statusName = entry.getKey();

                      row = sheet.createRow(rowCount);
                      rowCount += 1;
                      cell = row.createCell((short)0);
                      cell.setCellValue("Status : " + statusName);
                      BigDecimal inwTotTxnAmt = BigDecimal.ZERO;
                      long sno = 0;

                      for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                          row = sheet.createRow(rowCount);
                          rowCount += 1;
                          for (short j = 0; j < 11; j++) {
                              cell = row.createCell(j);

                              // for header
                              if (roww == 0) {
                                  if (j == 0) {
                                      cell.setCellValue("S.No");
                                  } else if (j == 1) {
                                      cell.setCellValue("Value Date");
                                  } else if (j == 2) {
                                      cell.setCellValue("Msg Type");
                                  } else if (j == 3) {
                                      cell.setCellValue("UTR Number");
                                  }  else if (j == 4) {
                                      cell.setCellValue("Sender Address");
                                  } else if (j == 5) {
                                      cell.setCellValue("Receiver Address");
                                  } else if (j == 6) {
                                      cell.setCellValue("Status");
                                  } else if (j == 7) {
                                      cell.setCellValue("Amount");
                                  }
                              } else {
                                  // Setting values in cell for each and every row
                                  if (j == 0) {

                                      String no = null;
                                      sno += 1;
                                      no = String.valueOf(sno);
                                      cell.setCellValue(no);
                                  } else if (j == 1) {

                                      String valueDate = null;
                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getValueDate() != null) {
                                          valueDate = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getValueDate();
                                          valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                      }
                                      cell.setCellValue(valueDate);
                                  } else if (j == 2) {

                                      String msgType = null;
                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getMsgSubType() != null) {
                                          msgType = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getMsgSubType();
                                      }
                                      cell.setCellValue(msgType);
                                  } else if (j == 3) {

                                      String utrNo = null;
                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getUtrNo() != null) {
                                          utrNo = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getUtrNo();
                                      }
                                      cell.setCellValue(utrNo);
                                  } else if (j == 4) {

                                      String sendAdd = null;

                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getSenderAddress() != null) {
                                          sendAdd = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getSenderAddress();
                                      }
                                      cell.setCellValue(sendAdd);

                                  } else if (j == 5) {

                                      String recAdd = null;

                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getReceiverAddress() != null) {
                                          recAdd = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getReceiverAddress();
                                      }
                                      cell.setCellValue(recAdd);
                                  }else if (j == 6) {

                                      String status = null;

                                      if (((ReportDTO)exportXLS
                                      .get(roww - 1)).getStatus() != null) {
                                          status = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getStatus();
                                      }
                                      cell.setCellValue(status);
                                  } else if (j == 7) {

                                      String inwTxnAmount = "0.00";
                                      if ( new BigDecimal(((ReportDTO)exportXLS
                                          .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                          inwTxnAmount = ((ReportDTO)exportXLS
                                          .get(roww - 1)).getAmt();
                                      }
                                      inwTotTxnAmt = inwTotTxnAmt.add(new BigDecimal(inwTxnAmount).setScale(2));
                                      cell.setCellValue(new BigDecimal(inwTxnAmount).setScale(2).toString());
                                  }
                              }
                           cell.setCellStyle(caption_style);
                          }
                      }

                      row = sheet.createRow(rowCount);
                      rowCount += 1;
                      cell = row.createCell((short)6);
                      cell.setCellValue("Total ( Status : " + statusName + " ) ");
                      cell = row.createCell((short)7);
                      cell.setCellValue(inwTotTxnAmt.toString());
                      grandInwTotTxnAmt = grandInwTotTxnAmt.add(inwTotTxnAmt);
                      row = sheet.createRow(rowCount);
                      rowCount += 1;
                  }

              }
              row = sheet.createRow(rowCount);
              rowCount += 1;
              cell = row.createCell((short)6);
              cell.setCellValue("Total Amount : ");
              cell = row.createCell((short)7);
              cell.setCellValue(grandInwTotTxnAmt.toString());
              book.write(out);
              out.flush();
              out.close();
          }
        } catch (Exception e) {
            logger.error("Exception while exporting Exception Report into Excel"+e.getMessage());
            throw new Exception("Exception while exporting Exception Report into Excel"+e);
        }
    }

    /**
     * Method used to export the Branchwise Individual Details report in to Excel
     */
    public void generateBrIndividualExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            int rowCount = 0;
//            double grandInwTotTxnAmt = 0;
//            double grandOwTotTxnAmt = 0;
            BigDecimal grandInwTotTxnAmt = BigDecimal.ZERO;
            BigDecimal grandOwTotTxnAmt = BigDecimal.ZERO;

            //Only If the list is not empty
            if (getReportMap().size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Branchwise Individual Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch wise Detailed Report from " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()) + "with status " +
                                  getReportDto().getStatusValue());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)8);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                Set set = getReportMap().entrySet();
                for (Iterator z = set.iterator(); z.hasNext();) {

                    Map.Entry<String, List<ReportDTO>> entry = (Map.Entry<String, List<ReportDTO>>)z.next();

                    List exportXLS = new ArrayList(1);
                    exportXLS.addAll(entry.getValue());

                    if(exportXLS.size() > 0) {

                        String branch = entry.getKey();

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("Branch : " + branch);

//                        double inwTotTxnAmt = 0;
//                        double owTotTxnAmt = 0;
                        BigDecimal inwTotTxnAmt = BigDecimal.ZERO;
                        BigDecimal owTotTxnAmt = BigDecimal.ZERO;
                        long sno = 0;

                        for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                            row = sheet.createRow(rowCount);
                            rowCount += 1;
                            for (short j = 0; j < 11; j++) {
                                cell = row.createCell(j);

                                // for header
                                if (roww == 0) {
                                    if (j == 0) {
                                        cell.setCellValue("S.No");
                                    } else if (j == 1) {
                                        cell.setCellValue("Value Date");
                                    } else if (j == 2) {
                                        cell.setCellValue("Host");
                                    } else if (j == 3) {
                                        cell.setCellValue("Msg Type");
                                    }  else if (j == 4) {
                                        cell.setCellValue("Tran Type");
                                    } else if (j == 5) {
                                        cell.setCellValue("UTR No");
                                    } else if (j == 6) {
                                        cell.setCellValue("Sender Address");
                                    } else if (j == 7) {
                                        cell.setCellValue("Receiver Address");
                                    } else if (j == 8) {
                                        cell.setCellValue("Status");
                                    }  else if (j == 9) {
                                        cell.setCellValue("Inward Amount (Rs.)");
                                    } else if (j == 10) {
                                        cell.setCellValue("Outward Amount (Rs.)");
                                    }
                                } else {
                                    // Setting values in cell for each and every row
                                    if (j == 0) {

                                        String no = null;

                                        sno += 1;
                                        no = String.valueOf(sno);
                                        cell.setCellValue(no);
                                    } else if (j == 1) {

                                        String valueDate = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getValueDate() != null) {
                                            valueDate = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getValueDate();
                                            valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                        }
                                        cell.setCellValue(valueDate);
                                    } else if (j == 2) {

                                        String host = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getSource() != null) {
                                            host = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getSource();
                                        }
                                        cell.setCellValue(host);
                                    } else if (j == 3) {

                                        String msgType = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getMsgType() != null) {
                                            msgType = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getMsgType();
                                        }
                                        cell.setCellValue(msgType);
                                    } else if (j == 4) {

                                        String tranType = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getTranType() != null) {
                                            tranType = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getTranType();
                                        }
                                        cell.setCellValue(tranType);
                                    } else if (j == 5) {

                                        String utrNo = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getUtrNo() != null) {
                                            utrNo = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getUtrNo();
                                        }
                                        cell.setCellValue(utrNo);
                                    } else if (j == 6) {

                                        String sendAdd = null;

                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getSenderAddress() != null) {
                                            sendAdd = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getSenderAddress();
                                        }
                                        cell.setCellValue(sendAdd);

                                    } else if (j == 7) {

                                        String recAdd = null;

                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getReceiverAddress() != null) {
                                            recAdd = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getReceiverAddress();
                                        }
                                        cell.setCellValue(recAdd);
                                    } else if (j == 8) {

                                        String status = null;

                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getStatus() != null) {
                                            status = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getStatus();
                                        }
                                        cell.setCellValue(status);
                                    }  else if (j == 9) {

//                                        double inwTxnAmount = 0.00;
                                        String inwTxnAmount = "0.00";

                                        if(((ReportDTO)exportXLS.get(roww - 1)).getTranType().equalsIgnoreCase("inward")) {
                                            if ( new BigDecimal(((ReportDTO)exportXLS
                                            .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                inwTxnAmount = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getAmt();
                                            }
                                            inwTotTxnAmt = inwTotTxnAmt.add(new BigDecimal(inwTxnAmount).setScale(2));
                                            cell.setCellValue(new BigDecimal(inwTxnAmount).setScale(2).toString());
                                        }
                                    } else if (j == 10) {

//                                        double owTxnAmount = 0.0;
                                        String owTxnAmount = "0.00";

                                        if(((ReportDTO)exportXLS.get(roww - 1)).getTranType().equalsIgnoreCase("outward")) {

//                                            if (((ReportDTO)exportXLS
//                                            .get(roww - 1)).getAmount() != 0.0) {
                                            if (new BigDecimal(((ReportDTO)exportXLS
                                            .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                owTxnAmount = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getAmt();
                                            }
                                            owTotTxnAmt = owTotTxnAmt.add(new BigDecimal(owTxnAmount).setScale(2));
                                            cell.setCellValue(new BigDecimal(owTxnAmount).setScale(2).toString());
                                        }
                                    }
                                }
                                cell.setCellStyle(caption_style);
                            }
                        }

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)8);
                        cell.setCellValue("Total ( Branch : " + branch + " ) ");
                        cell = row.createCell((short)9);
                        cell.setCellValue(inwTotTxnAmt.toString());
                        cell = row.createCell((short)10);
                        cell.setCellValue(owTotTxnAmt.toString());
                        grandInwTotTxnAmt = grandInwTotTxnAmt.add(inwTotTxnAmt);
                        grandOwTotTxnAmt = grandOwTotTxnAmt.add(owTotTxnAmt);
                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                    }

                }


                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)9);
                cell.setCellValue("Total Inward Amount : ");
                cell = row.createCell((short)10);
                cell.setCellValue(grandInwTotTxnAmt.toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)9);
                cell.setCellValue("Total Outward Amount : ");
                cell = row.createCell((short)10);
                cell.setCellValue(grandOwTotTxnAmt.toString());

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the Branchwise Summary Details report in to Excel
     */
    //Modified as like RTGS Br.summray Report
    public void generateBrSummaryExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            int rowCount = 0;
//            double grandInwTotTxnAmt = 0;
//            double grandOwTotTxnAmt = 0;
            BigDecimal grandInwTotTxnAmt = BigDecimal.ZERO;
            BigDecimal grandOwTotTxnAmt = BigDecimal.ZERO;

            //Only If the list is not empty
            if (getReportMap().size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Branchwise Summary Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch wise Summary Report from " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()) + "with status " +
                                  getReportDto().getStatusValue());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                if (new BigDecimal(getReportDto().getToAmount()).compareTo(BigDecimal.ZERO) > 0){
                    row = sheet.createRow(rowCount);
                    cell = row.createCell((short)0);
                    cell.setCellValue("Amount Range from "+getReportDto().getFromAmount()+ " to " +getReportDto().getToAmount());
                    rowCount += 1;
                }
                cell = row.createCell((short)5);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                Set set = getReportMap().entrySet();
                for (Iterator z = set.iterator(); z.hasNext();) {

                    Map.Entry<String, List<ReportDTO>> entry = (Map.Entry<String, List<ReportDTO>>)z.next();

                    List exportXLS = new ArrayList(1);
                    exportXLS.addAll(entry.getValue());

                    if(exportXLS.size() > 0) {

                        String branch = entry.getKey();

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("Branch : " + branch);

//                        double inwTotTxnAmt = 0;
//                        double owTotTxnAmt = 0;
                        BigDecimal inwTotTxnAmt = BigDecimal.ZERO;
                        BigDecimal owTotTxnAmt = BigDecimal.ZERO;
                        long sno = 0;

                        for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                            row = sheet.createRow(rowCount);
                            rowCount += 1;
                            for (short j = 0; j < 8; j++) {
                                cell = row.createCell(j);

                                // for header
                                if (roww == 0) {
                                    if (j == 0) {
                                        cell.setCellValue("S.No");
                                    } else if (j == 1) {
                                        cell.setCellValue("Value Date");
                                    } else if (j == 2) {
                                        cell.setCellValue("Host");
                                    } else if (j == 3) {
                                        cell.setCellValue("Tran Type");
                                    } else if (j == 4) {
                                        cell.setCellValue("Status");
                                    } else if (j == 5) {
                                        cell.setCellValue("Count");
                                    }  else if (j == 6) {
                                        cell.setCellValue("Inward Amount (Rs.)");
                                    } else if (j == 7) {
                                        cell.setCellValue("Outward Amount (Rs.)");
                                    }
                                } else {
                                    // Setting values in cell for each and every row
                                    if (j == 0) {

                                        String no = null;

                                        sno += 1;
                                        no = String.valueOf(sno);
                                        cell.setCellValue(no);
                                    } else if (j == 1) {

                                        String valueDate = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getValueDate() != null) {
                                            valueDate = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getValueDate();
                                            valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                        }
                                        cell.setCellValue(valueDate);
                                    } else if (j == 2) {

                                        String host = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getSource() != null) {
                                            host = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getSource();
                                        }
                                        cell.setCellValue(host);
                                    } else if (j == 3) {

                                        String tranType = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getTranType() != null) {
                                            tranType = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getTranType();
                                        }
                                        cell.setCellValue(tranType);
                                    } else if (j == 4) {

                                        String status = null;

                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getStatus() != null) {
                                            status = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getStatus();
                                        }
                                        cell.setCellValue(status);
                                    } else if (j == 5) {

                                        long count = 0;

                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getCount() != 0) {
                                            count = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getCount();
                                        }
                                        cell.setCellValue(count);
                                    } else if (j == 6) {

//                                        double inwTxnAmount = 0.00;
                                        String inwTxnAmount = "0.00";

                                        if(((ReportDTO)exportXLS.get(roww - 1)).getTranType().equalsIgnoreCase("inward")) {
//                                            if (((ReportDTO)exportXLS
//                                            .get(roww - 1)).getAmount() != 0.0) {
                                            if (new BigDecimal(((ReportDTO)exportXLS
                                            .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                inwTxnAmount = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getAmt();
                                            }
                                            inwTotTxnAmt = inwTotTxnAmt.add(new BigDecimal( inwTxnAmount).setScale(2));
                                            cell.setCellValue(new BigDecimal(inwTxnAmount).setScale(2).toString());
                                        }
                                    } else if (j == 7) {

//                                        double owTxnAmount = 0.0;
                                        String owTxnAmount = "0.00";

                                        if(((ReportDTO)exportXLS.get(roww - 1)).getTranType().equalsIgnoreCase("outward")) {

//                                            if (((ReportDTO)exportXLS
//                                            .get(roww - 1)).getAmount() != 0.0) {
                                            if ( new BigDecimal(((ReportDTO)exportXLS
                                            .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                owTxnAmount = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getAmt();
                                            }
                                            owTotTxnAmt = owTotTxnAmt.add(new BigDecimal(owTxnAmount).setScale(2));
                                            cell.setCellValue(new BigDecimal(owTxnAmount).setScale(2).toString());
                                        }
                                    }
                                }
                                cell.setCellStyle(caption_style);
                            }
                        }

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)5);
                        cell.setCellValue("Total ( Branch : " + branch + " ) ");
                        cell = row.createCell((short)6);
                        cell.setCellValue(inwTotTxnAmt.setScale(2).toString());
                        cell = row.createCell((short)7);
                        cell.setCellValue(owTotTxnAmt.setScale(2).toString());
                        grandInwTotTxnAmt = grandInwTotTxnAmt.add(inwTotTxnAmt);
                        grandOwTotTxnAmt = grandOwTotTxnAmt.add(owTotTxnAmt);
                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                    }

                }


                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)6);
                cell.setCellValue("Total Inward Amount : ");
                cell = row.createCell((short)7);
                cell.setCellValue(grandInwTotTxnAmt.setScale(2).toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)6);
                cell.setCellValue("Total Outward Amount : ");
                cell = row.createCell((short)7);
                cell.setCellValue(grandOwTotTxnAmt.setScale(2).toString());

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the Bankwise report in to Excel
     */
    public void generateBankwiseExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            int rowCount = 0;

//            double grandTotTxnAmt = 0;
            BigDecimal grandTotTxnAmt = BigDecimal.ZERO;

            //Only If the list is not empty
            if (getReportMap().size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;
                String title = "";
                if (report != null && report.equalsIgnoreCase("inward")) {
                    title = "Inward Bank Wise Report";
                } else if (report != null && report.equalsIgnoreCase("outward")) {
                    title = "Outward Bank Wise Report";
                }
                book.setSheetName(0,title,HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                if (report != null && report.equalsIgnoreCase("inward")) {
                    cell.setCellValue("Branch Report - Inward - Bank Wise from " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                } else if (report != null && report.equalsIgnoreCase("outward")) {
                    cell.setCellValue("Branch Report - Outward - Bank Wise from " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                }
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)4);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                Set set = getReportMap().entrySet();
                for (Iterator z = set.iterator(); z.hasNext();) {

                    Map.Entry<String, List<ReportDTO>> entry = (Map.Entry<String, List<ReportDTO>>)z.next();

                    List exportXLS = new ArrayList(1);
                    exportXLS.addAll(entry.getValue());

                    if(exportXLS.size() > 0) {

                        String bank = entry.getKey();

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("Bank : " + bank);

//                        double totTxnAmt = 0;
                        BigDecimal totTxnAmt = BigDecimal.ZERO;
                        long sno = 0;

                        for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                            row = sheet.createRow(rowCount);
                            rowCount += 1;
                            for (short j = 0; j < 7; j++) {
                                cell = row.createCell(j);

                                // for header
                                if (roww == 0) {
                                    if (j == 0) {
                                        cell.setCellValue("S.No");
                                    } else if (j == 1) {
                                        cell.setCellValue("Value Date");
                                    } else if (j == 2) {
                                        cell.setCellValue("Msg Type");
                                    } else if (j == 3) {
                                        cell.setCellValue("UTR No");
                                    } else if (j == 4) {
                                        cell.setCellValue("Sender Address");
                                    } else if (j == 5) {
                                        cell.setCellValue("Receiver Address");
                                    } else if (j == 6) {
                                        cell.setCellValue("Amount (Rs.)");
                                    }
                                } else {
                                    // Setting values in cell for each and every row
                                    if (j == 0) {

                                        String no = null;

                                        sno += 1;
                                        no = String.valueOf(sno);
                                        cell.setCellValue(no);
                                    } else if (j == 1) {

                                        String valueDate = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getValueDate() != null) {
                                            valueDate = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getValueDate();
                                            valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                        }
                                        cell.setCellValue(valueDate);
                                    } else if (j == 2) {

                                        String msgType = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getMsgType() != null) {
                                            msgType = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getMsgType();
                                        }
                                        cell.setCellValue(msgType);
                                    } else if (j == 3) {

                                        String utrNo = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getUtrNo() != null) {
                                            utrNo = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getUtrNo();
                                        }
                                        cell.setCellValue(utrNo);
                                    } else if (j == 4) {

                                        String sendAdd = null;

                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getSenderAddress() != null) {
                                            sendAdd = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getSenderAddress();
                                        }
                                        cell.setCellValue(sendAdd);

                                    } else if (j == 5) {

                                        String recAdd = null;

                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getReceiverAddress() != null) {
                                            recAdd = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getReceiverAddress();
                                        }
                                        cell.setCellValue(recAdd);
                                    } else if (j == 6) {

//                                        double txnAmount = 0.0;
                                        String txnAmount = "0.00";

                                        if (new BigDecimal(((ReportDTO)exportXLS
                                            .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                txnAmount = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getAmt();
                                            }
                                            totTxnAmt = totTxnAmt.add( new BigDecimal(txnAmount).setScale(2));
                                            cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                                    }
                                }
                                cell.setCellStyle(caption_style);
                            }
                        }

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)5);
                        cell.setCellValue("Total ( Bank : " + bank + " ) ");
                        cell = row.createCell((short)6);
                        cell.setCellValue(totTxnAmt.toString());
                        grandTotTxnAmt = grandTotTxnAmt.add(totTxnAmt);
                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                    }

                }


                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)5);
                cell.setCellValue("Total Amount : ");
                cell = row.createCell((short)6);
                cell.setCellValue(grandTotTxnAmt.toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the Branch Inward Returned report in to Excel
     */
    public void generateBrInwReturnedExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            int rowCount = 0;

//            double grandTotTxnAmt = 0;
            BigDecimal grandTotTxnAmt = BigDecimal.ZERO;

            //Only If the list is not empty
            if (getReportMap().size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Branch Inward Returned Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch Inward Returned Report from " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)5);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                Set set = getReportMap().entrySet();
                for (Iterator z = set.iterator(); z.hasNext();) {

                    Map.Entry<String, List<ReportDTO>> entry = (Map.Entry<String, List<ReportDTO>>)z.next();

                    List exportXLS = new ArrayList(1);
                    exportXLS.addAll(entry.getValue());

                    if(exportXLS.size() > 0) {

                        String date = entry.getKey();

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("Date : " + date);

//                        double totTxnAmt = 0;
                        BigDecimal totTxnAmt = BigDecimal.ZERO;
                        long sno = 0;

                        for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                            row = sheet.createRow(rowCount);
                            rowCount += 1;
                            for (short j = 0; j < 8; j++) {
                                cell = row.createCell(j);

                                // for header
                                if (roww == 0) {
                                    if (j == 0) {
                                        cell.setCellValue("S.No");
                                    } else if (j == 1) {
                                        cell.setCellValue("Value Date");
                                    } else if (j == 2) {
                                        cell.setCellValue("Msg Type");
                                    } else if (j == 3) {
                                        cell.setCellValue("UTR Number");
                                    } else if (j == 4) {
                                        cell.setCellValue("Sender Address");
                                    } else if (j == 5) {
                                        cell.setCellValue("Receiver Address");
                                    } else if (j == 6) {
                                        cell.setCellValue("Outward Utr No");
                                    } else if (j == 7) {
                                        cell.setCellValue("Amount (Rs.)");
                                    }
                                } else {
                                    // Setting values in cell for each and every row
                                    if (j == 0) {

                                        String no = null;

                                        sno += 1;
                                        no = String.valueOf(sno);
                                        cell.setCellValue(no);
                                    } else if (j == 1) {

                                        String valueDate = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getValueDate() != null) {
                                            valueDate = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getValueDate();
                                            valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                        }
                                        cell.setCellValue(valueDate);
                                    } else if (j == 2) {

                                        String msgType = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getMsgType() != null) {
                                            msgType = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getMsgType();
                                        }
                                        cell.setCellValue(msgType);
                                    } else if (j == 3) {

                                        String utrNo = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getUtrNo() != null) {
                                            utrNo = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getUtrNo();
                                        }
                                        cell.setCellValue(utrNo);
                                    } else if (j == 4) {

                                        String sendAdd = null;

                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getSenderAddress() != null) {
                                            sendAdd = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getSenderAddress();
                                        }
                                        cell.setCellValue(sendAdd);

                                    } else if (j == 5) {

                                        String recAdd = null;

                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getReceiverAddress() != null) {
                                            recAdd = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getReceiverAddress();
                                        }
                                        cell.setCellValue(recAdd);
                                    } else if (j == 6) {

                                        String owUtrNo = null;
                                        if (((ReportDTO)exportXLS
                                        .get(roww - 1)).getOutUTRNo() != null) {
                                            owUtrNo = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getOutUTRNo();
                                        }
                                        cell.setCellValue(owUtrNo);
                                    } else if (j == 7) {

//                                        double txnAmount = 0.0;
                                        String txnAmount = "0.00";

//                                        if (((ReportDTO)exportXLS
//                                            .get(roww - 1)).getAmount() != 0.0) {
                                        if (new BigDecimal(((ReportDTO)exportXLS
                                        .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                            txnAmount = ((ReportDTO)exportXLS
                                            .get(roww - 1)).getAmt();
                                        }
                                            totTxnAmt = totTxnAmt.add( new BigDecimal(txnAmount).setScale(2));
                                            cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                                    }
                                }
                                cell.setCellStyle(caption_style);
                            }
                        }

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)6);
                        cell.setCellValue("Total ( Date : " + date + " ) ");
                        cell = row.createCell((short)7);
                        cell.setCellValue(totTxnAmt.toString());
                        grandTotTxnAmt = grandTotTxnAmt.add(totTxnAmt);
                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                    }

                }


                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)6);
                cell.setCellValue("Total Amount : ");
                cell = row.createCell((short)7);
                cell.setCellValue(grandTotTxnAmt.toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the unsuccessful payments report in to Excel
     */
    public void unsuccessfulPaymentReportExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;
//            double totTxnAmt = 0.0;
            BigDecimal totTxnAmt = BigDecimal.ZERO;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Unsuccessful Payments Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                String responeType = "";
                if (getReportDto().getResponse().equalsIgnoreCase("R90")) {
                    responeType = "PI";
                }else {
                    responeType = "SSN";
                }
                cell.setCellValue("RTGS Unsuccessful Payments Report from " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()) +
                                      "  with Negative "+responeType+" Response");
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)5);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 9; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {

                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Value Date");
                            } else if (j == 2) {
                                cell.setCellValue("Msg Type");
                            } else if (j == 3) {
                                cell.setCellValue("UTR Number");
                            }  else if (j == 4) {
                                cell.setCellValue("Sender Address");
                            } else if (j == 5) {
                                cell.setCellValue("Receiver Address");
                            } else if (j == 6) {
                                cell.setCellValue("Response Type");
                            } else if (j == 7) {
                                cell.setCellValue("Remarks");
                            }else if (j == 8) {
                                cell.setCellValue("Amount)");
                                }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                }
                                valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                cell.setCellValue(valueDate);

                            } else if (j == 2) {

                                String msgType = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getMsgSubType() != null) {
                                    msgType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getMsgSubType();
                                }
                                cell.setCellValue(msgType);
                            } else if (j == 3) {

                                String utrNo = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getUtrNo() != null) {
                                    utrNo = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo();
                                }
                                cell.setCellValue(utrNo);
                            } else if (j == 4) {

                                String sendAdd = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);
                            } else if (j == 5) {

                                String recAdd = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);

                            } else if (j == 6) {

                                String resType = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getResponseType() != null) {
                                    resType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getResponseType();
                                }
                                cell.setCellValue(resType);

                            } else if (j == 7) {

                                String remarks = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getRemarks() != null) {
                                    remarks = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getRemarks();
                                }
                                cell.setCellValue(remarks);
                            } else if (j == 8) {

                                String txnAmount = "0";
                                if ( new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    txnAmount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }
                                totTxnAmt = totTxnAmt.add( new BigDecimal(txnAmount).setScale(2));
                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                            }
                          }
                          cell.setCellStyle(caption_style);
                        }
                     }
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)7);
                cell.setCellValue("Total Amount : ");
                cell = row.createCell((short)8);
                cell.setCellValue(totTxnAmt.toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file for unsuccessful payment report"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file for unsuccessful payment report"+e);
        }
    }

    /**
     * Method used to export the branch subtype individual report in to Excel
     */
    public void generateBrSubTypeIndividualExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;
//            double totTxnAmt = 0.0;
            BigDecimal totTxnAmt = BigDecimal.ZERO;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Br. Subtype Individual Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                String tranType = "";
                if(getReportDto().getTransactionType().equalsIgnoreCase("inward")) {

                    tranType = "Inward";
                } else {

                    tranType = "Outward";
                }
                if(getReportDto().getPaymentType().equalsIgnoreCase("R42")) {

                    cell.setCellValue("Branch SubType Individual Report - " + tranType + " Interbank payment from " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()) + "with status " +
                                      getReportDto().getStatusValue());
                } else {
                    cell.setCellValue("Branch SubType Individual Report - " + tranType + " Customer payment from " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()) + "with status " +
                                      getReportDto().getStatusValue());
                }
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)8);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 9; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {

                                if (j == 0) {
                                    cell.setCellValue("S.No");
                                } else if (j == 1) {
                                    cell.setCellValue("Value Date");
                                } else if (j == 2) {
                                    cell.setCellValue("Sender Address");
                                } else if (j == 3) {
                                    cell.setCellValue("Receiver Address");
                                }  else if (j == 4) {
                                    cell.setCellValue("UTR No");
                                } else if (j == 5) {
                                    if(getReportDto().getPaymentType().equalsIgnoreCase("R41")) {
                                        cell.setCellValue("Account Number");
                                    } else {
                                        cell.setCellValue("Info");
                                    }
                                } else if (j == 6) {
                                    if(getReportDto().getPaymentType().equalsIgnoreCase("R41")) {
                                        cell.setCellValue("Beneficiary Details");
                                    } else {
                                        cell.setCellValue("Additional Info");
                                    }
                                } else if (j == 7) {
                                    cell.setCellValue("Amount (Rs.)");
                                }  else if (j == 8) {
                                    cell.setCellValue("Status");
                                }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                }
                                valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                cell.setCellValue(valueDate);

                            } else if (j == 2) {

                                String sendAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);
                            } else if (j == 3) {

                                String recAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);
                            } else if (j == 4) {

                                String utrNo = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getUtrNo() != null) {
                                    utrNo = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo();
                                }
                                cell.setCellValue(utrNo);
                            } else if (j == 5) {

                                if(getReportDto().getPaymentType().equalsIgnoreCase("R41")) {

                                    String a5561 = null;

                                    if (((ReportDTO)exportXLS
                                    .get(roww - 1)).getFieldA5561() != null) {
                                        a5561 = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getFieldA5561();
                                    }
                                    cell.setCellValue(a5561);
                                } else {

                                    String i7495 = null;

                                    if (((ReportDTO)exportXLS
                                    .get(roww - 1)).getFieldI7495() != null) {
                                        i7495 = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getFieldI7495();
                                    }
                                    cell.setCellValue(i7495);
                                }
                            } else if (j == 6) {

                                if(getReportDto().getPaymentType().equalsIgnoreCase("R41")) {

                                    String n5561 = null;

                                    if (((ReportDTO)exportXLS
                                    .get(roww - 1)).getFieldN5561() != null) {
                                        n5561 = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getFieldN5561();
                                    }
                                    cell.setCellValue(n5561);
                                } else {

                                    String a7495 = null;

                                    if (((ReportDTO)exportXLS
                                    .get(roww - 1)).getFieldA7495() != null) {
                                        a7495 = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getFieldA7495();
                                    }
                                    cell.setCellValue(a7495);
                                }
                            } else if (j == 7) {

                                String txnAmount = "0";

                                if ( new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    txnAmount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }
                                totTxnAmt = totTxnAmt.add( new BigDecimal(txnAmount).setScale(2));
                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                            } else if ( j == 8) {

                                String status = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getStatus() != null) {
                                    status = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getStatus();
                                }
                                cell.setCellValue(status);
                            }
                          }
                          cell.setCellStyle(caption_style);
                        }
                     }
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)6);
                cell.setCellValue("Total Amount : ");
                cell = row.createCell((short)7);
                cell.setCellValue(totTxnAmt.toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the branch Inward Rejected report in to Excel
     */
    public void generateBrInwRejectedExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;
//            double totTxnAmt = 0.0;
            BigDecimal  totTxnAmt = BigDecimal.ZERO;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Br. Inward Rejected Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);

                if(getReportDto().getPaymentType().equalsIgnoreCase("R42")) {

                    cell.setCellValue("Branch Inward Rejected Report - Inward Interbank payment from " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                } else {
                    cell.setCellValue("Branch Inward Rejected Report - Inward Customer payment from " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                }
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)7);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;

                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 10; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {

                                if (j == 0) {
                                    cell.setCellValue("S.No");
                                } else if (j == 1) {
                                    cell.setCellValue("Value Date");
                                } else if (j == 2) {
                                    cell.setCellValue("Sub Msg Type");
                                } else if (j == 3) {
                                    cell.setCellValue("UTR No");
                                } else if (j == 4) {
                                    cell.setCellValue("Sender Address");
                                }  else if (j == 5) {
                                    cell.setCellValue("Receiver Address");
                                }   else if (j == 6) {
                                    cell.setCellValue("Rejected By");
                                } else if (j == 7) {
                                    cell.setCellValue("Rejection Approved By");
                                } else if (j == 8) {
                                    cell.setCellValue("Remarks");
                                } else if (j == 9) {
                                    cell.setCellValue("Amount (Rs.)");
                                }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                }
                                valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                cell.setCellValue(valueDate);

                            } else if (j == 2) {

                                String msgType = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getMsgType() != null) {
                                    msgType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getMsgType();
                                }
                                cell.setCellValue(msgType);
                            } else if (j == 3) {

                                String utrNo = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getUtrNo() != null) {
                                    utrNo = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo();
                                }
                                cell.setCellValue(utrNo);
                            } else if (j == 4) {

                                String sendAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);
                            } else if (j == 5) {

                                String recAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);
                            } else if (j == 6) {

                                String entryBy = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getEntryBy() != null) {
                                    entryBy = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getEntryBy();
                                }
                                cell.setCellValue(entryBy);
                            } else if (j == 7) {

                                String passBy = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getPassBy() != null) {
                                    passBy = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getPassBy();
                                }
                                cell.setCellValue(passBy);
                            } else if (j == 8) {

                                String remarks = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getRemarks() != null) {
                                    remarks = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getRemarks();
                                }
                                cell.setCellValue(remarks);
                            } else if (j == 9) {

//                                double txnAmount = 0.0;
                                String txnAmount = "0";

                                if ( new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    txnAmount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }
                                totTxnAmt = totTxnAmt.add(new BigDecimal(txnAmount));
                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)8);
                cell.setCellValue("Total Amount : ");
                cell = row.createCell((short)9);
                cell.setCellValue(totTxnAmt.setScale(2).toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the Reconcillation report in to Excel
     */
    public void generateRTGSReconcillationExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Reconciliation Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Reconciliation Report on "+InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)3);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 6; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            }  else if (j == 1) {
                                cell.setCellValue("Value Date");
                            } else if (j == 2) {
                                cell.setCellValue("Message Type");
                            } else if (j == 3) {
                                cell.setCellValue("Transaction Type");
                            } else if (j == 4) {
                                cell.setCellValue("Total Txn");
                            } else if (j == 5) {
                                cell.setCellValue("Total Txn Amount(Rs.)");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                    valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                }
                                cell.setCellValue(valueDate);

                            } else if (j == 2) {

                                String msgType = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getMsgType() != null) {
                                    msgType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getMsgType();
                                }
                                cell.setCellValue(msgType);
                            } else if (j == 3) {

                                String tranType = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getTranType() != null) {
                                    tranType = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getTranType();
                                }
                                cell.setCellValue(tranType);
                            } else if (j == 4) {

                                long txnCount = 0;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getCount() != 0) {
                                    txnCount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getCount();
                                }
                                cell.setCellValue(txnCount);

                            } else if (j == 5) {

//                                double txnAmount = 0.0;
                                String txnAmount = "0.00";

                                if (new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    txnAmount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }
                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());

                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }

                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the Generate Counter Party Wise Reconcillation report in to Excel
     */
    public void generateReconcilliationReportCPwiseExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            int rowCount = 0;

//            double outerGrandTotTxnAmt = 0.0;
            BigDecimal outerGrandTotTxnAmt = BigDecimal.ZERO;

            //Only If the list is not empty
            if (getReportMap().size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "CP Wise Reconciliation Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Counter Party wise Reconciliation Report from " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)4);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                Set set = getReportMap().entrySet();

                for (Iterator z = set.iterator(); z.hasNext();) {

                    Map.Entry<String, Map<String, List<ReportDTO>>> entry = (Map.Entry<String, Map<String, List<ReportDTO>>>)z.next();

//                    double grandTotTxnAmt = 0.0;
                    BigDecimal grandTotTxnAmt = BigDecimal.ZERO;
                    String cp = entry.getKey();

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("Counter Party : " + cp);
                    row = sheet.createRow(rowCount);
                    rowCount += 1;

                    Map map = entry.getValue();

                    if(map != null && map.size() > 0) {

                        Set innerSet = map.entrySet();

                        for (short j = 0; j < 7; j++) {

                            cell = row.createCell(j);

                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Value Date");
                            } else if (j == 2) {
                                cell.setCellValue("Tran Type");
                            } else if (j == 3) {
                                cell.setCellValue("Msg Type");
                            } else if (j == 4) {
                                cell.setCellValue("Sender Address");
                            } else if (j == 5) {
                                cell.setCellValue("Receiver Address");
                            } else if (j == 6) {
                                cell.setCellValue("Amount (Rs.)");
                            }
                        }

                        for (Iterator e = innerSet.iterator(); e.hasNext();) {

                            Map.Entry<String, List<ReportDTO>> innerEntry = (Map.Entry<String, List<ReportDTO>>)e.next();

                            String msgType = innerEntry.getKey();

                            List exportXLS = new ArrayList(1);
                            exportXLS.addAll(innerEntry.getValue());

                            if(exportXLS.size() > 0) {

//                                double totTxnAmt = 0;
                                BigDecimal totTxnAmt = BigDecimal.ZERO;
                                long sno = 0;

                                row = sheet.createRow(rowCount);
                                rowCount += 1;
                                cell = row.createCell((short)0);
                                cell.setCellValue(msgType);

                                for (int i = exportXLS.size(), roww = 1; roww <= i; roww++) {

                                    row = sheet.createRow(rowCount);
                                    rowCount += 1;
                                    for (short j = 0; j < 7; j++) {

                                        cell = row.createCell(j);
                                        // Setting values in cell for each and
                                        // every row
                                        if (j == 0) {

                                            String no = null;

                                            sno += 1;
                                            no = String.valueOf(sno);
                                            cell.setCellValue(no);
                                        } else if (j == 1) {

                                            String valueDate = null;
                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1)).getValueDate() != null) {
                                                valueDate = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getValueDate();
                                                valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                            }
                                            cell.setCellValue(valueDate);
                                        } else if (j == 2) {

                                            String tranType = null;
                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1)).getTranType() != null) {
                                                tranType = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getTranType();
                                            }
                                            cell.setCellValue(tranType);
                                        } else if (j == 3) {

                                            String type = null;
                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1)).getMsgType() != null) {
                                                type = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getMsgType();
                                            }
                                            cell.setCellValue(type);
                                        }  else if (j == 4) {

                                            String sendAdd = null;

                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1)).getSenderAddress() != null) {
                                                sendAdd = ((ReportDTO)exportXLS
                                                .get(roww - 1))
                                                .getSenderAddress();
                                            }
                                            cell.setCellValue(sendAdd);

                                        } else if (j == 5) {

                                            String recAdd = null;

                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1))
                                            .getReceiverAddress() != null) {
                                                recAdd = ((ReportDTO)exportXLS
                                                .get(roww - 1))
                                                .getReceiverAddress();
                                            }
                                            cell.setCellValue(recAdd);
                                        } else if (j == 6) {

//                                            double txnAmount = 0.0;
                                            String txnAmount = "0.00";

                                            if ( new BigDecimal(((ReportDTO)exportXLS
                                            .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                txnAmount = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getAmt();
                                            }
                                            totTxnAmt = totTxnAmt.add(new BigDecimal(txnAmount).setScale(2));
                                            cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                                        }
                                    }
                                    cell.setCellStyle(caption_style);
                                }

                                row = sheet.createRow(rowCount);
                                rowCount += 1;
                                cell = row.createCell((short)5);
                                cell.setCellValue("Sub Total Amount (Msg Type : " + msgType + " )  : ");
                                cell = row.createCell((short)6);
                                cell.setCellValue(totTxnAmt.toString());
                                grandTotTxnAmt = grandTotTxnAmt.add(totTxnAmt);
                                row = sheet.createRow(rowCount);
                                rowCount += 1;
                            }
                        }

                    }
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)5);
                    cell.setCellValue("TOTAL Amount (Counter Partywise : " + cp + " ) : ");
                    cell = row.createCell((short)6);
                    cell.setCellValue(grandTotTxnAmt.setScale(2).toString());
                    outerGrandTotTxnAmt = outerGrandTotTxnAmt.add(grandTotTxnAmt);
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                }

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)5);
                cell.setCellValue("TOTAL Amount : ");
                cell = row.createCell((short)6);
                cell.setCellValue(outerGrandTotTxnAmt.setScale(2).toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method used to export the Generate Counter Party Wise Reconcillation report in to Excel
     */
    public void generateReconcilliationReportBranchwiseExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            int rowCount = 0;

//            double outerGrandTotTxnAmt = 0.0;
            BigDecimal outerGrandTotTxnAmt = BigDecimal.ZERO;

            //Only If the list is not empty
            if (getReportMap().size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Br. Wise Reconciliation Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch Wise Reconcillation Report from " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)4);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                Set set = getReportMap().entrySet();

                for (Iterator z = set.iterator(); z.hasNext();) {

                    Map.Entry<String, Map<String, List<ReportDTO>>> entry = (Map.Entry<String, Map<String, List<ReportDTO>>>)z.next();

//                    double grandTotTxnAmt = 0.0;
                    BigDecimal grandTotTxnAmt = BigDecimal.ZERO;
                    String branch = entry.getKey();

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("Branch : " + branch);
                    row = sheet.createRow(rowCount);
                    rowCount += 1;

                    Map map = entry.getValue();

                    if(map != null && map.size() > 0) {

                        Set innerSet = map.entrySet();

                        for (short j = 0; j < 7; j++) {

                            cell = row.createCell(j);

                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Value Date");
                            } else if (j == 2) {
                                cell.setCellValue("Tran Type");
                            } else if (j == 3) {
                                cell.setCellValue("Msg Type");
                            } else if (j == 4) {
                                cell.setCellValue("Sender Address");
                            } else if (j == 5) {
                                cell.setCellValue("Receiver Address");
                            } else if (j == 6) {
                                cell.setCellValue("Amount (Rs.)");
                            }
                        }

                        for (Iterator e = innerSet.iterator(); e.hasNext();) {

                            Map.Entry<String, List<ReportDTO>> innerEntry = (Map.Entry<String, List<ReportDTO>>)e.next();

                            String msgType = innerEntry.getKey();

                            List exportXLS = new ArrayList(1);
                            exportXLS.addAll(innerEntry.getValue());

                            if(exportXLS.size() > 0) {

//                                double totTxnAmt = 0;
                                BigDecimal totTxnAmt = BigDecimal.ZERO;
                                long sno = 0;

                                row = sheet.createRow(rowCount);
                                rowCount += 1;
                                cell = row.createCell((short)0);
                                cell.setCellValue(msgType);

                                for (int i = exportXLS.size(), roww = 1; roww <= i; roww++) {

                                    row = sheet.createRow(rowCount);
                                    rowCount += 1;
                                    for (short j = 0; j < 7; j++) {

                                        cell = row.createCell(j);
                                        // Setting values in cell for each and
                                        // every row
                                        if (j == 0) {

                                            String no = null;

                                            sno += 1;
                                            no = String.valueOf(sno);
                                            cell.setCellValue(no);
                                        } else if (j == 1) {

                                            String valueDate = null;
                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1)).getValueDate() != null) {
                                                valueDate = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getValueDate();
                                                valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                            }
                                            cell.setCellValue(valueDate);
                                        } else if (j == 2) {

                                            String tranType = null;
                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1)).getTranType() != null) {
                                                tranType = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getTranType();
                                            }
                                            cell.setCellValue(tranType);
                                        } else if (j == 3) {

                                            String type = null;
                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1)).getMsgType() != null) {
                                                type = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getMsgType();
                                            }
                                            cell.setCellValue(type);
                                        }  else if (j == 4) {

                                            String sendAdd = null;

                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1)).getSenderAddress() != null) {
                                                sendAdd = ((ReportDTO)exportXLS
                                                .get(roww - 1))
                                                .getSenderAddress();
                                            }
                                            cell.setCellValue(sendAdd);

                                        } else if (j == 5) {

                                            String recAdd = null;

                                            if (((ReportDTO)exportXLS
                                            .get(roww - 1))
                                            .getReceiverAddress() != null) {
                                                recAdd = ((ReportDTO)exportXLS
                                                .get(roww - 1))
                                                .getReceiverAddress();
                                            }
                                            cell.setCellValue(recAdd);
                                        } else if (j == 6) {

//                                            double txnAmount = 0.0;
                                            String txnAmount = "0.00";

                                            if (new BigDecimal(((ReportDTO)exportXLS
                                            .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                txnAmount = ((ReportDTO)exportXLS
                                                .get(roww - 1)).getAmt();
                                            }
                                            totTxnAmt = totTxnAmt.add(new BigDecimal(txnAmount));
                                            cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());
                                        }
                                    }
                                    cell.setCellStyle(caption_style);
                                }

                                row = sheet.createRow(rowCount);
                                rowCount += 1;
                                cell = row.createCell((short)5);
                                cell.setCellValue("Sub Total Amount (Msg Type : " + msgType + " )  : ");
                                cell = row.createCell((short)6);
                                cell.setCellValue(totTxnAmt.setScale(2).toString());
                                grandTotTxnAmt = grandTotTxnAmt.add(totTxnAmt);
                                row = sheet.createRow(rowCount);
                                rowCount += 1;
                            }
                        }

                    }
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)5);
                    cell.setCellValue("TOTAL Amount (Branch : " + branch + " ) : ");
                    cell = row.createCell((short)6);
                    cell.setCellValue(grandTotTxnAmt.setScale(2).toString());
                    outerGrandTotTxnAmt = outerGrandTotTxnAmt.add(grandTotTxnAmt);
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                }

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)5);
                cell.setCellValue("TOTAL Amount : ");
                cell = row.createCell((short)6);
                cell.setCellValue(outerGrandTotTxnAmt.toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * TODO
     */
    public void generateOwReturnedReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            //To convert the Input Date Format      //Commented by priyak for date format.
            /*String inputFromDt = getReportDto().getValueDate();
            String inputToDt = getReportDto().getToDate();
            inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
            inputToDt = InstaReportUtil.formatDateString(inputToDt);
            getReportDto().setValueDate(inputFromDt);
            getReportDto().setToDate(inputToDt);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 201, 13, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.

            reportDTOs = (List<ReportDTO>) res.info;

        } catch(Exception e) {

            logger.error("Exception ocurred while getting the OwReturnedReport : "
                         + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * Method used to export the outward returned report in to Excel
     */
    public void generateOwReturnedExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;
            int rowCount = 0;
//            double totTxnAmt = 0.0;
            BigDecimal totTxnAmt = BigDecimal.ZERO;

            //Adding the items to a list
            for (Iterator i = getReportDTOs().iterator(); i.hasNext();) {
                ReportDTO dtoList = (ReportDTO)i
                .next();
                exportXLS.add(dtoList);

            }

            //Only If the list is not empty
            if (exportXLS.size() != 0) {

                // start to export excel
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;

                    book.setSheetName(0,
                                      "Outward Returned Report",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);

                cell.setCellValue("Outward Returned Report from " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)6);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 9; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {

                                if (j == 0) {
                                    cell.setCellValue("S.No");
                                } else if (j == 1) {
                                    cell.setCellValue("Value Date");
                                } else if (j == 2) {
                                    cell.setCellValue("Sender Address");
                                } else if (j == 3) {
                                    cell.setCellValue("Receiver Address");
                                }  else if (j == 4) {
                                    cell.setCellValue("UTR No");
                                } else if (j == 5) {
                                    cell.setCellValue("Original UTR No");
                                } else if (j == 6) {
                                    cell.setCellValue("Info");
                                } else if (j == 7) {
                                    cell.setCellValue("Additional Info");
                                } else if (j == 8) {
                                    cell.setCellValue("Amount (Rs.)");
                                }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String no = null;

                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {

                                String valueDate = null;
                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getValueDate() != null) {
                                    valueDate = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate();
                                }
                                valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                cell.setCellValue(valueDate);

                            } else if (j == 2) {

                                String sendAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getSenderAddress() != null) {
                                    sendAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress();
                                }
                                cell.setCellValue(sendAdd);
                            } else if (j == 3) {

                                String recAdd = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getReceiverAddress() != null) {
                                    recAdd = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress();
                                }
                                cell.setCellValue(recAdd);
                            } else if (j == 4) {

                                String utrNo = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getUtrNo() != null) {
                                    utrNo = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo();
                                }
                                cell.setCellValue(utrNo);
                            } else if (j == 5) {

                                String orgUtrNo = null;

                                if (((ReportDTO)exportXLS
                                .get(roww - 1)).getOutUTRNo() != null) {
                                    orgUtrNo = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getOutUTRNo();
                                }
                                cell.setCellValue(orgUtrNo);
                            } else if (j == 6) {

                                String i7495 = null;

                                if (((ReportDTO)exportXLS.get(roww - 1))
                                .getFieldI7495() != null) {
                                    i7495 = ((ReportDTO)exportXLS.get(roww - 1))
                                    .getFieldI7495();
                                }
                                cell.setCellValue(i7495);
                            } else if (j == 7) {


                                String a7495 = null;

                                if (((ReportDTO)exportXLS.get(roww - 1))
                                .getFieldA7495() != null) {
                                    a7495 = ((ReportDTO)exportXLS.get(roww - 1))
                                    .getFieldA7495();
                                }
                                cell.setCellValue(a7495);
                            } else if (j == 8) {

                                //double txnAmount = 0.0;
                                String txnAmount = "0.00";

                                if (new BigDecimal(((ReportDTO)exportXLS
                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                    txnAmount = ((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt();
                                }
                                totTxnAmt = totTxnAmt.add( new BigDecimal(txnAmount));
                                cell.setCellValue(new BigDecimal(txnAmount).setScale(2).toString());

                            }
                        }
                        cell.setCellStyle(caption_style);
                    }
                }

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)7);
                cell.setCellValue("Total Amount : ");
                cell = row.createCell((short)8);
                cell.setCellValue(totTxnAmt.setScale(2).toString());
                row = sheet.createRow(rowCount);
                rowCount += 1;

                book.write(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * method for generate NEFT-RTGS Net Settlement Report
     */
    public void generateNeftRtgsNetSettlementReport(HttpServletRequest request){

        try {

            String sessionId = request.getSession().getId();
            /*String inputDate = getReportDto().getValueDate();
            inputDate = InstaReportUtil.formatDateString(inputDate);
            getReportDto().setValueDate(inputDate);*/

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);
            Message req = createMessage(sessionId, 202, 12, reportDto);
            Message res = handle(sessionId, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            Map resultMap = (HashMap) res.info;
            NEFTRTGS_settlementList = (List)resultMap.get("list");
            NEFTRTGS_settlementMap = (HashMap)resultMap.get("map");
            NEFTRTGS_NetSettleKeyword = InstaDefaultConstants.NEFT_RTGS_NET_SETTLE_SEARCH_KEYWORD;
        } catch(Throwable e) {

            logger.error("Exception ocurred while getting the NEFT RTGS Net Settlement Report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }


    public Map getNEFTRTGS_settlementMap() {

        return NEFTRTGS_settlementMap;
    }


    public void setNEFTRTGS_settlementMap(Map NEFTRTGS_settlementMap) {

        this.NEFTRTGS_settlementMap = NEFTRTGS_settlementMap;
    }


    public List getNEFTRTGS_settlementList() {

        if(NEFTRTGS_settlementList == null)
            return new ArrayList(0);
        return NEFTRTGS_settlementList;
    }


    public void setNEFTRTGS_settlementList(List list) {

        NEFTRTGS_settlementList = list;
    }


    public String getNEFTRTGS_NetSettleKeyword() {

        return NEFTRTGS_NetSettleKeyword;
    }


    public void setNEFTRTGS_NetSettleKeyword(String netSettleKeyword) {

        NEFTRTGS_NetSettleKeyword = netSettleKeyword;
    }

    /**
     * Method for Cretaing Excel for NEFT RTGS Net Settlement Report
     */
    public void NEFTRTGS_NetSettlementExportToExcel(ServletOutputStream out)
    throws Exception {

        try {

            List exportXLS = new ArrayList(1);
            long sno = 0;            //To add serial number by priyak
            Map LMSaggregateMap = new HashMap();
            int rowCount = 0;
            exportXLS = this.getNEFTRTGS_settlementList();
            LMSaggregateMap = this.getNEFTRTGS_settlementMap();
            rowCount = exportXLS.size();

            HSSFWorkbook book = new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet();
            HSSFRow row = null;
            HSSFCell cell = null;
            HSSFFont caption_font = null;
            HSSFCellStyle caption_style = null;

            book.setSheetName(0,"NEFT-RTGS Net Settlement Report",
                              HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

            caption_font = book.createFont();
            caption_font.setFontHeightInPoints((short)10);
            caption_font.setFontName("Verdana");
            caption_style = book.createCellStyle();
            caption_style.setFont(caption_font);

            row = sheet.createRow(rowCount);
            rowCount += 1;
            cell = row.createCell((short)0);
            cell.setCellValue("Net Settlements received from RTGS");

            row = sheet.createRow(rowCount);
            rowCount += 1;
            cell = row.createCell((short)0);
            cell.setCellValue("Report Date :");
            cell = row.createCell((short)1);
            cell.setCellValue(InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()));
            row = sheet.createRow(rowCount);
            rowCount += 1;
            cell = row.createCell((short)3);
            String dateForm = currentReportPrintTime.substring(0,11);
            String time = currentReportPrintTime.substring(11);
            cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
            rowCount += 1;
            if (exportXLS.size() != 0) {

                rowCount += 1;
                for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    for (short j = 0; j < 6; j++) {
                        cell = row.createCell(j);

                        // for header
                        if (roww == 0) {
                            if (j == 0) {
                                cell.setCellValue("Msg Type");
                            } else if (j == 1) {
                                cell.setCellValue("Ordering Institution");
                            } else if (j == 2) {
                                cell.setCellValue("Code");
                            } else if (j == 3) {
                                cell.setCellValue("Info");
                            } else if (j == 4) {
                                cell.setCellValue("Additional Info");
                            } else if (j == 5) {
                                cell.setCellValue("Amount");
                            }
                        } else {
                            // Setting values in cell for each and every row
                            if (j == 0) {

                                String msgType = "";
                                    msgType = ((NEFT_RTGSNetSettlementDTO)exportXLS
                                                .get(roww - 1)).getMsgType()+((NEFT_RTGSNetSettlementDTO)exportXLS
                                                .get(roww - 1)).getMsgSubType();

                                cell.setCellValue(msgType);
                            } else if (j == 1) {

                                String orderingInstitution = "";
                                orderingInstitution = ((NEFT_RTGSNetSettlementDTO)exportXLS
                                                       .get(roww - 1)).getOrderingInstitution();
                                cell.setCellValue((orderingInstitution == null)? "":orderingInstitution);
                            } else if (j == 2) {


                                String code = "";
                                code = this.getNEFTRTGS_NetSettleKeyword();
                                cell.setCellValue((code == null)? "":code);
                            } else if (j == 3) {

                                String info = "";

                                info = ((NEFT_RTGSNetSettlementDTO)exportXLS
                                                .get(roww - 1)).getInfo();
                                cell.setCellValue((info == null)?"":info);
                            } else if (j == 4) {

                                String additionalInfo = "";

                                additionalInfo = ((NEFT_RTGSNetSettlementDTO)exportXLS
                                                .get(roww - 1)).getAdditionalInfo();
                                cell.setCellValue((additionalInfo == null)?"":additionalInfo);
                            } else if (j == 5) {

                                String amount = "";
                                amount = ((NEFT_RTGSNetSettlementDTO)exportXLS
                                .get(roww - 1)).getAmount();
                                cell.setCellValue((amount == null)?"0.00":amount);
                            }
                       }
                        cell.setCellStyle(caption_style);
                    }
                }
            }
                row = sheet.createRow(rowCount);
                rowCount += 1;
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("LMS NEFT Aggregate");
                int rows = 0;
                short j;
                if (LMSaggregateMap.size() > 0) {

                   Set<Map.Entry<String, String>> entrySet = LMSaggregateMap.entrySet();

                   row = sheet.createRow(rowCount);
                   rowCount += 1;
                   for (j = 0; j < 3; j++) {
                       cell = row.createCell(j);
                       if (rows == 0) {

                           if (j == 0) {
                               cell.setCellValue("S.No");
                           } else if (j == 1) {
                               cell.setCellValue("Batch Time");
                           } else if (j == 2) {
                               cell.setCellValue("Aggregate Amount\r\n(Total Credit-Total Debit)");
                           }
                       }
                   }

                   for (Iterator<Map.Entry<String, String>> i = entrySet.iterator(); i.hasNext();rows++) {
                       Map.Entry<String, String> entry = i.next();
                       // String key = entry.getKey();

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        for (j = 0; j < 3; j++) {
                            cell = row.createCell(j);
                            if (j == 0) {
                                String no = null;
                                sno += 1;
                                no = String.valueOf(sno);
                                cell.setCellValue(no);
                            } else if (j == 1) {
                                String batchTime = "";
                                batchTime = entry.getKey();
                                cell.setCellValue(batchTime);
                            } else if (j == 2) {
                                String totAmount = "0.00";
                                totAmount = entry.getValue();
                                //cell.setCellValue(FormatAmount.formatINRAmount(totAmount));
                                cell.setCellValue(new BigDecimal(totAmount).setScale(2).toString());
                            }
                        }
                    }
                }
                sheet = book.createSheet();

                book.write(out);
                out.flush();
                out.close();

        } catch (Exception e) {
            logger.error("Exception while creating Excel sheet file"+e.getMessage());
            throw new Exception("Exception while creating Excel sheet file"+e);
        }
    }

    /**
     * Method to load input params for report
     */
    public void loadInputParameters(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();
            Message req = null;
            Message res = null;

            String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
            if (haveBranchField) {

                req = createMessage(sessionID, 190, 2, null);
                res = handle(sessionID, req);
                //formatValuDate();  //Have done With date format dd-MMM-yyyy.
                hostBranchList = (List) res.info;
                //updateDateForamt(); //After BO call

                int size = hostBranchList.size();
                HostIFSCMasterDTO _dto;

                for (int i = 0; i < size; i++) {

                    _dto = hostBranchList.get(i);

                    if(_dto.getHostIFSCMasterVO().getIfscCode().equalsIgnoreCase(getUserIfscCode())){

                        reportDto.setReceiverIfscId(Long.parseLong(_dto.getHostIFSCMasterVO().getIfscId()));
                        reportDto.setSenderIfscId(Long.parseLong(_dto.getHostIFSCMasterVO().getIfscId()));
                        reportDto.setCounterPartyIfscId(Long.parseLong(_dto.getHostIFSCMasterVO().getIfscId()));
                        reportDto.setIfscId(Long.parseLong(_dto.getHostIFSCMasterVO().getIfscId()));
                        break;
                     }
                }
            }
            if (haveDateField) {

                getReportDto().setValueDate(ConversionUtil.getFormat(appDate));
                getReportDto().setToDate(ConversionUtil.getFormat(appDate));  //Have done with date format dd-MMM-yyyy
            }
            if (haveChannelField) {

                channelList = new ArrayList(0);
                channelList.add("RTGS");
                channelList.add("NEFT");
            }

            if (haveTranTypeField) {

                DisplayValueReportDTO dto = new DisplayValueReportDTO();
                setTranTypeList(new ArrayList(0));

                dto = new DisplayValueReportDTO();
                dto.setValue("inward");
                dto.setDisplayValue("Inward");
                getTranTypeList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("outward");
                dto.setDisplayValue("Outward");
                getTranTypeList().add(dto);
            }



            if(haveUserField) {
                if(userIdList != null)
                userIdList.clear();
                if (getReportDto().getIfscId() == 0) {
                    getUserIdByLocation();
                } else {

                    String brCode = getBranchIFSCCode(String.valueOf(getReportDto().getIfscId()));
                    getUserIdByBranch(brCode);
//                    if (!brCode.equalsIgnoreCase(InstaDefaultConstants.COIFSCCode.substring(7, 11))) {
//                        getUserIdByLocation(InstaDefaultConstants.COIFSCCode.substring(7, 11));
//                    }
               }
            }
        } catch (Exception e) {
            logger.error("Exception ocurred while loading the input params for report :"
                         + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * Method to set msgType value
     */
    public void setMsgTypeField() {

        if (haveMsgSubTypeField) {

            if (getReportDto().getChannel().equalsIgnoreCase("RTGS")) {

                DisplayValueReportDTO dto = new DisplayValueReportDTO();
                setSubTypeList(new ArrayList(0));
                dto = new DisplayValueReportDTO();
                dto.setValue("ALL");
                dto.setDisplayValue("ALL Payments");
                getSubTypeList().add(dto);
                dto = new DisplayValueReportDTO();
                dto.setValue("R41");
                dto.setDisplayValue("R41");
                getSubTypeList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("R42");
                dto.setDisplayValue("R42");
                getSubTypeList().add(dto);
                if (getReportDto().getTransactionType().equalsIgnoreCase("inward")) {

                    dto = new DisplayValueReportDTO();
                    dto.setValue("R43");
                    dto.setDisplayValue("R43");
                    getSubTypeList().add(dto);

                    dto = new DisplayValueReportDTO();
                    dto.setValue("R44");
                    dto.setDisplayValue("R44");
                    getSubTypeList().add(dto);
                } else if (getReportDto().getTransactionType().equalsIgnoreCase("outward")) {

                    dto = new DisplayValueReportDTO();
                    dto.setValue("R10");
                    dto.setDisplayValue("R10");
                    getSubTypeList().add(dto);
                }
            } else if (getReportDto().getChannel().equalsIgnoreCase("NEFT")) {

                DisplayValueReportDTO dtoNeft = new DisplayValueReportDTO();
                setSubTypeList(new ArrayList(0));
                dtoNeft = new DisplayValueReportDTO();
                dtoNeft.setValue("ALL");
                dtoNeft.setDisplayValue("ALL Payments");
                getSubTypeList().add(dtoNeft);
                if (getReportDto().getTransactionType().equalsIgnoreCase("outward")) {

                    dtoNeft = new DisplayValueReportDTO();
                    dtoNeft.setValue("N06");
                    dtoNeft.setDisplayValue("N06");
                    getSubTypeList().add(dtoNeft);

                    dtoNeft = new DisplayValueReportDTO();
                    dtoNeft.setValue("N07");
                    dtoNeft.setDisplayValue("N07");
                    getSubTypeList().add(dtoNeft);
                } else if (getReportDto().getTransactionType().equalsIgnoreCase("inward")) {

                    dtoNeft = new DisplayValueReportDTO();
                    dtoNeft.setValue("N02");
                    dtoNeft.setDisplayValue("N02");
                    getSubTypeList().add(dtoNeft);

                    dtoNeft = new DisplayValueReportDTO();
                    dtoNeft.setValue("N04");
                    dtoNeft.setDisplayValue("N04");
                    getSubTypeList().add(dtoNeft);
                }
            }
        }
    }
    /**
     * Method to generate user level event report
     */
    public void generateUserLevelEventReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 202, 21, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
            ifscCode = getBranchIFSC(String.valueOf(reportDto.getIfscId()));
            //updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : "+e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * Method to generate email Info report
     */
    public void generateEmailInfoReport(HttpServletRequest request){

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 202, 22, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
            //getBranchIFSC(String.valueOf(reportDto.getIfscId()));
            //updateDateForamt(); //After BO call
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : "+e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * Method to validate the Email Info Report Input
     */
    public void validateInput() {

        boolean notANumber = false;

        try {

            // Validating Start Time check if empty
            if ((getReportDto().getChannel() == null) ||
                    (getReportDto().getChannel().trim().length() == 0)) {
                throw new Exception("Please Select any one channel");
            }

            // Validating End Time check if empty
            if ((getReportDto().getPaymentType() == null) ||
                    (getReportDto().getPaymentType().trim().length() == 0)) {
                throw new Exception("Please Select the Payment Type");
            }

        } catch(Throwable e) {

            logger.error("Exception while validating the Transaction Phase Detail. " + e);
            throw new CrudException(e);
        }
    }
    /**
     * Method to generate IDL utilization Info report
     */
    public void generateIDLUtilizationReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 202, 23, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the report : "+e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * Method to generate System Level Events info Report
     */
    public void generateSystemLevelEventsReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                            (new Date(System.currentTimeMillis())
                                            , currentReportPrintTimeFormat);

            Message req = createMessage(sessionID, 202, 24, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();  //Have done With date format dd-MMM-yyyy.
            reportDTOs = (List) res.info;
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the System Level Events Report : "+e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }



    /*
     * This method is used to update the date format dd-MMM-yyyy after BO call
     */
    public void updateDateForamt() {

        Iterator itr = reportDTOs.iterator();
        while(itr.hasNext()) {

            ReportDTO obj = (ReportDTO)itr.next();
            obj.setValueDate(ConversionUtil.getFormat((obj.getValueDate())));
        }
    }


    public String getReportEvent() {

        return reportEvent;
    }


    public void setReportEvent(String reportEvent) {

        this.reportEvent = reportEvent;
    }


    public boolean isHaveChannelField() {

        return haveChannelField;
    }


    public void setHaveChannelField(boolean haveChannelField) {

        this.haveChannelField = haveChannelField;
    }

    public List getChannelList() {
        if (channelList==null) {
            channelList = new ArrayList(0);
        }
        return channelList;
    }


    public void setChannelList(List channelList) {

        this.channelList = channelList;
    }


    public String getIfscCode() {

        return ifscCode;
    }


    public void setIfscCode(String ifscCode) {

        this.ifscCode = ifscCode;
    }


    public String getDateFormat() {

        return dateFormat;
    }


    public void setDateFormat(String dateFormat) {

        this.dateFormat = dateFormat;
    }


    public List getInwardStatusList() {

        if(inwardStatusList == null) {

            inwardStatusList = new ArrayList(0);
        }
        return inwardStatusList;
    }


    public void setInwardStatusList(List inwardStatusList) {

        this.inwardStatusList = inwardStatusList;
    }


    public List getInwardTypeList() {

        if(inwardTypeList == null) {

            inwardTypeList = new ArrayList(0);
        }
        return inwardTypeList;
    }


    public void setInwardTypeList(List inwardTypeList) {

        this.inwardTypeList = inwardTypeList;
    }


    public List getOutwardStatusList() {

        if(outwardStatusList == null) {

            outwardStatusList = new ArrayList(0);
        }
        return outwardStatusList;
    }


    public void setOutwardStatusList(List outwardStatusList) {

        this.outwardStatusList = outwardStatusList;
    }


    public List getOutwardTypeList() {

        if(outwardTypeList == null) {

            outwardTypeList = new ArrayList(0);
        }
        return outwardTypeList;
    }


    public void setOutwardTypeList(List outwardTypeList) {

        this.outwardTypeList = outwardTypeList;
    }


    public String getInwardStatus() {

        return inwardStatus;
    }


    public void setInwardStatus(String inwardStatus) {

        this.inwardStatus = inwardStatus;
    }


    public String getInwardType() {

        return inwardType;
    }


    public void setInwardType(String inwardType) {

        this.inwardType = inwardType;
    }


    public String getOutwardStatus() {

        return outwardStatus;
    }


    public void setOutwardStatus(String outwardStatus) {

        this.outwardStatus = outwardStatus;
    }


    public String getOutwardType() {

        return outwardType;
    }


    public void setOutwardType(String outwardType) {

        this.outwardType = outwardType;
    }


    public boolean isHaveFutureDateTxnStatus() {

        return haveFutureDateTxnStatus;
    }


    public void setHaveFutureDateTxnStatus(boolean haveFutureDateTxnStatus) {

        this.haveFutureDateTxnStatus = haveFutureDateTxnStatus;
    }


    public String getChannel() {

        return channel;
    }


    public void setChannel(String channel) {

        this.channel = channel;
    }


    public boolean isHaveResponeType() {

        return haveResponeType;
    }


    public void setHaveResponeType(boolean haveResponeType) {

        this.haveResponeType = haveResponeType;
    }

    /**
     * To get haveBankListField.
     *
     * @return
     */
    public boolean isHaveBankListField() {

        return haveBankListField;
    }


    /**
     * To set haveBankListField.
     *
     * @param haveBankListField
     */
    public void setHaveBankListField(boolean haveBankListField) {

        this.haveBankListField = haveBankListField;
    }

    /** Getting the report title, 20110802
     *
     * @return reportTitle String
     *
     */
    public String getReportTitle() {

        return reportTitle;
    }

    /** Setting the report title, 20110802
     *
     * @param reportTitle String
     *
     */
    public void setReportTitle(String reportTitle) {

        this.reportTitle = reportTitle;
    }

    /** Getting Inward Summary Report, 20110802
    *
    * @return inwSummaryReport String
    *
    */
    public String getInwSummaryReport() {

        return inwSummaryReport;
    }

    /** Setting Inward Summary Report, 20110802
    *
    * @param inwSummaryReport String
    *
    */
    public void setInwSummaryReport(String inwSummaryReport) {

        this.inwSummaryReport = inwSummaryReport;
    }

    /** Getting Outward Summary Report, 20110802
    *
    * @return outwSummaryReport String
    *
    */
    public String getOutwSummaryReport() {

        return outwSummaryReport;
    }

    /** Setting Outward Summary Report, 20110802
    *
    * @param outwSummaryReport String
    *
    */
    public void setOutwSummaryReport(String outwSummaryReport) {

        this.outwSummaryReport = outwSummaryReport;
    }


    public String getLcbgSummaryReport() {

        return lcbgSummaryReport;
    }


    public void setLcbgSummaryReport(String lcbgSummaryReport) {

        this.lcbgSummaryReport = lcbgSummaryReport;
    }


    public String getLcbgReportTitle() {

        return lcbgReportTitle;
    }


    public void setLcbgReportTitle(String lcbgReportTitle) {

        this.lcbgReportTitle = lcbgReportTitle;
    }

    //Joe.M, 20130604
    /**
     *
     * RTGS Beneficiary Account Report List
     */
    public List<ReportDTO> getReturnedList() {

        if (returnedList==null) {
            returnedList = new ArrayList<ReportDTO>(0);
        }
        return returnedList;
    }

    public void setReturnedList(List<ReportDTO> reportDTOs) {

        this.returnedList = reportDTOs;
    }

    public String getBifsc() {

        return bifsc;
    }


    public void setBifsc(String ifsc) {

        bifsc = ifsc;
    }
    /**
     *
     * RTGS Beneficiary Account Report
     */
    public void generateRTGSBeneficiaryAccountReport(HttpServletRequest request) {

        try {

            String sessionID = request.getSession().getId();

            currentReportPrintTime = InstaReportUtil.formatDate
                                                    (new Date(System.currentTimeMillis())
                                                     , currentReportPrintTimeFormat);

            //If the login user is CO User, then set the ifsc id as 0.
//            if(isCOUser==1) {
//
//                reportDto.setIfscId(0);
//            }
            //this.reportDto.setBifsc(this.getBifsc());
            Message req = createMessage(sessionID, 900900, 1, reportDto);
            Message res = handle(sessionID, req);
            formatValueDate();
            returnedList = (List<ReportDTO>) res.info;
//            setMessageDTO((CMsgDTO) res.info);
//            System.out.println("messageDTO : " + messageDTO);
        } catch(Exception e) {

            logger.error("Exception ocurred while getting the RTGS Beneficiary Account Report"
                         + " Report : " + e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }




}
