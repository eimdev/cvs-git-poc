package com.objectfrontier.insta.lcbg.reports.bean;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import com.objectfrontier.arch.dto.Message;
import com.objectfrontier.arch.server.ServerException;
import com.objectfrontier.crud.CrudException;
import com.objectfrontier.insta.InstaDefaultConstants;
import com.objectfrontier.insta.client.InstaClientULC;
import com.objectfrontier.insta.client.env.InstaAppClientEnvironment;
import com.objectfrontier.insta.client.struts.bean.LoginBean;
import com.objectfrontier.insta.dto.DisplayValueReportDTO;
import com.objectfrontier.insta.message.client.dto.CMsgDTO;
import com.objectfrontier.insta.message.client.dto.HostIFSCMasterDTO;
import com.objectfrontier.insta.message.client.vo.HostIFSCMasterVO;
import com.objectfrontier.insta.neft.reports.bean.InstaNEFTReportBean;
import com.objectfrontier.insta.reports.InstaReportUtil;
import com.objectfrontier.insta.reports.dto.ReportDTO;
import com.objectfrontier.insta.reports.server.util.FormatAmount;
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;
import com.objectfrontier.insta.workflow.util.ConversionUtil;
import com.objectfrontier.neft.report.dto.BatchwiseAggregateDTO;
import com.objectfrontier.neft.report.dto.BatchwiseReconcillationDTO;
import com.objectfrontier.neft.report.dto.ITDetailReportDTO;
import com.objectfrontier.neft.report.dto.NEFTDetailsReportDTO;
import com.objectfrontier.neft.report.dto.NEFTN04DetailsDTO;
import com.objectfrontier.neft.report.dto.NEFTReportDTO;
import com.objectfrontier.neft.report.dto.OTDetailReportDTO;
import com.objectfrontier.neft.report.dto.TransactionInfo;
import com.objectfrontier.neft.report.dto.SummaryInfo.SummaryInfoElement;

/**
 *
 * @author joeam
 * @date   Sep 25, 2012
 * @since  insta.reports; Sep 25, 2012
 */


public class InstaLCBGReportBean
extends InstaReportBean {

        private static Category logger = Category.getInstance(InstaLCBGReportBean.class);

        public List batchTimings;
        public List inwardTypeList;
        public List reportTypeList;

        private List<NEFTDetailsReportDTO> detailReportDTOs;

        public static String title;
        public String reportName;
        public NEFTReportDTO neftRepDTO;

        public Map<String, Object> aggregateMap;

        public Map<String, List> reconcillationMap;

        public List<ReportDTO> graduadtedPayments;
        public List<ReportDTO> returnedList;
        private List<DisplayValueReportDTO> statusList;
        private List<HostIFSCMasterDTO> hostBranchList;

        public List<TransactionInfo> inwardTxns;

        public int isDateWiseGraduated;
        public int ifnSeries;


        String reportDate = null;
        String appDate = null;
        public static InstaAppClientEnvironment clientEnv;

        /** This variable is used to store selected bank. */
        public String[] selectedBank;


        static {

            if(clientEnv == null) {
                clientEnv = InstaAppClientEnvironment.getSingletonEnvironment();
            }
        }
        /**
         *     This method is to load the initial data which are required for loading the
         * report input page.
         *
         */
       /* @SuppressWarnings("unchecked")
        public void loadInitial(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                //To check the logged in user is COUser
                isCentralOffice(request);

                if(isCentralOffice(request) == 1) {
                    //code change done by mohana on 16-Sep-2009 for neft utrnowise report
                    if(!"NeftUTRNumberwiseReport".equalsIgnoreCase(report)) {
                        setUserIfscCode("All-All Branches");
                        this.setUserIfscId(0);
                    } else {

                    //Logged in user's Ifsc code
                    setUserIfscCode((String) request.getSession()
                                        .getAttribute(InstaClientULC.IFSCCODE));
                    this.setUserIfscId(((Long) request.getSession()
                                        .getAttribute(InstaClientULC.IFSCID)).longValue());
                    }
                } else {

                    //Logged in user's Ifsc code
                    setUserIfscCode((String) request.getSession()
                                        .getAttribute(InstaClientULC.IFSCCODE));
                    this.setUserIfscId(((Long) request.getSession()
                                        .getAttribute(InstaClientULC.IFSCID)).longValue());
                }
                //String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
                String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
                getReportDto().setValueDate(ConversionUtil.getFormat(appDate));

                Message req = null;
                Message res = null;

                //To get branches based on the login name-priyak
                if (haveBranchField) {

                    req = createMessage(sessionID, 190, 2, null);
                    res = handle(sessionID, req);
                    setHostBranchList((List) res.info);

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

                    if (isInwardSpecific || isOutwardSpecific) {

                        req = createMessage(sessionID, 190, 3, null);
                        res = handle(sessionID, req);

                        setBankList((List)((Object[]) res.info)[0]);
                        //nonHostBranchList = (List)((Object[]) res.info)[1];
                    }
                }

                if (haveDateField) {

                    getReportDto().setValueDate(ConversionUtil.getFormat(appDate));
                    getReportDto().setToDate(ConversionUtil.getFormat(appDate));  //Have done with date format dd-MMM-yyyy
                }

                if (haveHostTypeField) {

                    req = createMessage(sessionID, 1900, 1, null);
                    res = handle(sessionID, req);
                    formatValueDate();
                    setHostList((List) res.info);
                    if (report.equalsIgnoreCase("InwardTxnsReport")) {

                        for (Iterator itr = getHostList().iterator();itr.hasNext();) {

                            DisplayValueReportDTO dto = (DisplayValueReportDTO)itr.next();
                            if(dto.getValue().equalsIgnoreCase("CBS")) {
                                getHostList().remove(dto);
                                break;
                            }
                        }
                    }
                }

                if (haveAmountField) {
                    getReportDto().setFromAmount("0.0");
                    getReportDto().setToAmount("0.0");
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


                    if(reportName != null && !reportName.equalsIgnoreCase("NeftOutwardTxnDetailsReport")){

                        dto = new DisplayValueReportDTO();
                        dto.setValue("N02");
                        dto.setDisplayValue("N02");
                        getSubTypeList().add(dto);
                        getInwardTypeList().add(dto);
                    }
                    //To add N04 in the list.
                    if (reportName != null && (reportName.equalsIgnoreCase("NeftInwardSummaryReport") ||
                                                reportName.equalsIgnoreCase("neftExceptionReport"))){

                        dto = new DisplayValueReportDTO();
                        dto.setValue("N04");
                        dto.setDisplayValue("N04");
                        getSubTypeList().add(dto);
                        getInwardTypeList().add(dto);
                    }

                    dto = new DisplayValueReportDTO();
                    dto.setValue("N06");
                    dto.setDisplayValue("N06");
                    getSubTypeList().add(dto);
                    getOutwardTypeList().add(dto);

                    dto = new DisplayValueReportDTO();
                    dto.setValue("N07");
                    dto.setDisplayValue("N07");
                    getSubTypeList().add(dto);
                    getOutwardTypeList().add(dto);

                }

                if (haveStatusField) {

                    DisplayValueReportDTO dto = new DisplayValueReportDTO();
                    setStatusList(new ArrayList(0));
                    setInwardStatusList(new ArrayList(0));
                    setOutwardStatusList(new ArrayList(0));


                     *   Here the value field holds 2 status values. It means first value is
                     * mentioning inward status and second value is mentioning outward status.
                     *
                     *   If the field dont have respective status for the transaction, then
                     * give the value as 0.
                     *

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
                    dto.setValue("0, 2400");
                    dto.setDisplayValue("ForTreasuryAuthorization");
                    getStatusList().add(dto);
                    getOutwardStatusList().add(dto);

                    dto = new DisplayValueReportDTO();
                    dto.setValue("500, 0");
                    dto.setDisplayValue("ForReturnAuthorization");
                    getStatusList().add(dto);
                    getInwardStatusList().add(dto);

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
                    dto.setValue("1200, 3200");
                    dto.setDisplayValue("Credited");
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

//                    nonHostBranchList = new ArrayList<HostIFSCMasterDTO>(0);
    //
//                    req = createMessage(sessionID, 190, 5, null);
//                    res = handle(sessionID, req);
    //
//                    nonHostBranchList = (List) res.info;
                    req = createMessage(sessionID, 190, 3, null);
                    res = handle(sessionID, req);
                    formatValueDate();  //Have done With date format dd-MMM-yyyy.

                    setBankList((List)((Object[]) res.info)[0]);
                    // updateDateForamt(); //After BO call
                }

                if (haveBatchTimeField) {

                    req = createMessage(sessionID, 190, 7, null);
                    res = handle(sessionID, req);
                    batchTimings = (List) res.info;
                }

                if (haveValueDateField) {
                    //getReportDto().setValueDate(appDate);     //Commented for date format.
                    getReportDto().setValueDate(ConversionUtil.getFormat(appDate));
                }
                if(haveInwardTypeField) {

                    DisplayValueReportDTO dto = new DisplayValueReportDTO();
                    setInwardTypeList(new ArrayList(0));

                    dto = new DisplayValueReportDTO();
                    dto.setValue("All");
                    dto.setDisplayValue("All");
                    getInwardTypeList().add(dto);

                    dto = new DisplayValueReportDTO();
                    dto.setValue("Completed");
                    dto.setDisplayValue("Completed");
                    getInwardTypeList().add(dto);

                    dto = new DisplayValueReportDTO();
                    dto.setValue("Returned");
                    dto.setDisplayValue("Returned");
                    getInwardTypeList().add(dto);
                }
                if(haveReportTypeField) {

                    DisplayValueReportDTO dto = new DisplayValueReportDTO();
                    setReportTypeList(new ArrayList(0));

                    dto = new DisplayValueReportDTO();
                    dto.setValue("Summary");
                    dto.setDisplayValue("Summary");
                    getReportTypeList().add(dto);

                    dto = new DisplayValueReportDTO();
                    dto.setValue("Detailed");
                    dto.setDisplayValue("Detailed");
                    getReportTypeList().add(dto);
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

                logger.error("Exception ocurred while loading the input details for NEFT report :"
                             + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }*/


        public List getInwardTypeList() {

            return inwardTypeList;
        }


        public void setInwardTypeList(List inwardTypeList) {

            this.inwardTypeList = inwardTypeList;
        }


        public List getReportTypeList() {

            return reportTypeList;
        }


        public void setReportTypeList(List reportTypeList) {

            this.reportTypeList = reportTypeList;
        }


        public List getBatchTimings() {

            return batchTimings;
        }


        public void setBatchTimings(List batchTimings) {

            this.batchTimings = batchTimings;
        }

        public NEFTReportDTO getNeftRepDTO() {

            return neftRepDTO;
        }


        public void setNeftRepDTO(NEFTReportDTO neftRepDTO) {

            this.neftRepDTO = neftRepDTO;
        }



        public static String getTitle() {

            return title;
        }


        public static void setTitle(String title) {

            InstaNEFTReportBean.title = title;
        }

        public List<NEFTDetailsReportDTO> getDetailReportDTOs() {

            if(detailReportDTOs == null) {
                detailReportDTOs = new ArrayList<NEFTDetailsReportDTO>();
            }
            return detailReportDTOs;
        }


        public void setDetailReportDTOs(List<NEFTDetailsReportDTO> detailReportDTOs) {

            this.detailReportDTOs = detailReportDTOs;
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

        public Map<String, Object> getAggregateMap() {

            if(aggregateMap == null) {

                aggregateMap = new LinkedHashMap<String, Object>();
            }
            return aggregateMap;
        }



        public void setAggregateMap(Map<String, Object> aggregateMap) {

            this.aggregateMap = aggregateMap;
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

        /**
         * method used for get the Branch IFSC Code and branchname
         * @param ifscmasterId
         * @return IFSC Code
         */
        public String getBranchName(String ifscID) {


            for(Iterator iter = hostBranchList.iterator(); iter.hasNext();) {

                HostIFSCMasterDTO _dto = (HostIFSCMasterDTO)iter.next();
                String ifscId = _dto.getHostIFSCMasterVO().getIfscId();
                if(ifscID.equals(ifscId)) {
                    String branchName = _dto.getHostIFSCMasterVO().getIfscCode() + "-"+ _dto.getHostIFSCMasterVO().getBranchName();
                    return branchName;
                }
            }
            return "ALL Branches";
        }

        public Map<String, List> getReconcillationMap() {

            if(reconcillationMap == null) {
                reconcillationMap = new HashMap<String, List>(0);
            }
            return reconcillationMap;
        }



        public void setReconcillationMap(Map<String, List> reconcillationMap) {

            this.reconcillationMap = reconcillationMap;
        }



        public List<ReportDTO> getGraduadtedPayments() {

            return graduadtedPayments;
        }



        public void setGraduadtedPayments(List<ReportDTO> graduadtedPayments) {

            this.graduadtedPayments = graduadtedPayments;
        }



        public String getAppDate() {

            return appDate;
        }



        public void setAppDate(String appDate) {

            this.appDate = appDate;
        }



        public String getReportDate() {

            return reportDate;
        }



        public void setReportDate(String reportDate) {

            this.reportDate = reportDate;
        }

        public List<TransactionInfo> getInwardTxns() {

            return inwardTxns;
        }



        public void setInwardTxns(List<TransactionInfo> inwardTxns) {

            this.inwardTxns = inwardTxns;
        }



        public List getHostBranchList() {

            return hostBranchList;
        }



        public void setHostBranchList(List hostBranchList) {

            this.hostBranchList = hostBranchList;
        }



        public int getIsDateWiseGraduated() {

            return isDateWiseGraduated;
        }



        public void setIsDateWiseGraduated(int isDateWiseGraduated) {

            this.isDateWiseGraduated = isDateWiseGraduated;
        }


        public List<ReportDTO> getReturnedList() {

            if (returnedList==null) {
                returnedList = new ArrayList<ReportDTO>(0);
            }
            return returnedList;
        }

        public void setReturnedList(List<ReportDTO> reportDTOs) {

            this.returnedList = reportDTOs;
        }

        /**
         * To generate LCBG Inward Summary reports
         *
         * @param request HttpServletRequest
         *
         */
        public void generateLCBGInwardReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 500500, 1, getReportDto());
                Message res = handle(sessionID, req);
                formatValueDate();

                returnedList = (List<ReportDTO>) res.info;

            } catch(Exception e) {

                logger.error("Exception ocurred while getting the LCBG Report : "
                             + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         *  To generate NEFT Outward Bank Summary Report
         *
         *  @param request HttpServletRequest
         *
         */
        public void generateNEFTOutwBankSummaryReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 202, 19, getReportDto());
                Message res = handle(sessionID, req);
                formatValueDate();

                returnedList = (List<ReportDTO>) res.info;

            } catch(Exception e) {

                logger.error("Exception ocurred while getting the NEFT Bank wise Summary Report : "
                             + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         * To get selected bank.
         *
         * @return selectedBank String.
         *
         */
        public String[] getSelectedBank() {
            return selectedBank;
        }

        /**
         * To set selected bank.
         *
         * @param selectedBank String
         *
         */
        public void setSelectedBank(String[] selectedBank) {
            this.selectedBank = selectedBank;
        }


        public int getIfnSeries() {

            return ifnSeries;
        }



        public void setIfnSeries(int ifnSeries) {

            this.ifnSeries = ifnSeries;
        }

    }

