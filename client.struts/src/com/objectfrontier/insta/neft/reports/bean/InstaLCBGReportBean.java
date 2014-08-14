package com.objectfrontier.insta.neft.reports.bean;

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

        private static Category logger = Category.getInstance(InstaNEFTReportBean.class);

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
        @SuppressWarnings("unchecked")
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
        }


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

        /**
         * To generate NEFT Inward Summary reports
         * This method fully modified as like RTGS Br.summary report by Eswaripriyak for DC#02
         */
        public void generateNEFTInwSummaryReports(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                getReportDto().setStatusValue(getReportStatusValue(reportDto.getStatus(), statusList)); //To set the status value.
                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 202, 2, getReportDto());
                Message res = handle(sessionID, req);
                formatValueDate();

                reportMap = (Map<String, List<ReportDTO>>) res.info;

            } catch(Exception e) {

                logger.error("Exception ocurred while getting the NEFT Branch wise Summary Report : "
                             + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         * To generate NEFT Inward Txns Report
         */
        public void generateNEFTInwTxnsReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();
                //To change the format
                /*String inputDt = getReportDto().getValueDate(); //Commented by priyak for date format
                inputDt = InstaReportUtil.formatDateString(inputDt);
                String inputToDt = getReportDto().getToDate();
                inputToDt = InstaReportUtil.formatDateString(inputToDt);
                getReportDto().setValueDate(inputDt);
                getReportDto().setToDate(inputToDt);*/
                setCurrentReportPrintTime(InstaReportUtil.formatDate
                                                (new Date(System.currentTimeMillis())
                                                , currentReportPrintTimeFormat));
                getReportDto().setStatusValue(getReportStatusValue(reportDto.getStatus(), statusList)); //To set the status value.
                Message req = createMessage(sessionID, 202, 11, getReportDto());
                Message res = handle(sessionID, req);
                formatValueDate();  //Have done With date format dd-MMM-yyyy.
                inwardTxns  = (List<TransactionInfo>) res.info;
                getReportDto().setBranchCode(getBranchName(String.valueOf(getReportDto().getIfscId())));
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the NEFTInwTxns Report : " + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         * To generate NEFT Graduated Payment Reports
         */
        public void generateNEFTGraduatedPaymentReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();
                //To change the format
                String inputDt = getReportDto().getValueDate(); //Commented by priyak for date fromat.
                if(inputDt != null && inputDt.length() == 10) {

                    inputDt = InstaReportUtil.formatDateString(inputDt);
                }
                getReportDto().setValueDate(inputDt);
                setCurrentReportPrintTime(InstaReportUtil.formatDate
                                                (new Date(System.currentTimeMillis())
                                                , currentReportPrintTimeFormat));

                Message req = createMessage(sessionID, 202, 9, getReportDto());
                Message res = handle(sessionID, req);
                formatValueDate();  //Have done With date format dd-MMM-yyyy.
                graduadtedPayments = (List<ReportDTO>)res.info;
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the NEFT Graduated Payment Report : " + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         * To generate NEFT Outward Summary reports
         */
        public void generateNEFTOutSummaryReports(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();
                //To change the format
                /*String inputDt = getReportDto().getValueDate(); //Commented by priyak for date format.
                inputDt = InstaReportUtil.formatDateString(inputDt);
                getReportDto().setValueDate(inputDt);*/
//                getReportDto().setIfscCode(this.getUserIfscCode());
//                getReportDto().setIfscId(this.getUserIfscId());
                setCurrentReportPrintTime(InstaReportUtil.formatDate
                                                (new Date(System.currentTimeMillis())
                                                , currentReportPrintTimeFormat));

                Message req = createMessage(sessionID, 202, 3, getReportDto());
                Message res = handle(sessionID, req);
                formatValueDate();  //Have done With date format dd-MMM-yyyy.
                neftRepDTO = (NEFTReportDTO) res.info;
                LoginBean lb = (LoginBean)request.getSession().getAttribute("loginBean");
                neftRepDTO.reportRunBy = lb.getUserId();
                getReportDto().setBranchCode(getBranchIFSCCode(String.valueOf(getReportDto().getIfscId())));
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the NEFTReconciliation Report : " + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         * Method for generating Outward Summary Report VijayaBank Specific
         */
        public void generateNEFTOutwardTxnDetailsReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();
                //To change the format                  //Commented by priyak for date fromat
                /*String inputDt = getReportDto().getValueDate();
                inputDt = InstaReportUtil.formatDateString(inputDt);
                getReportDto().setValueDate(inputDt);

                inputDt = getReportDto().getToDate();
                inputDt = InstaReportUtil.formatDateString(inputDt);
                getReportDto().setToDate(inputDt);*/
//                getReportDto().setIfscCode(this.getUserIfscCode());
//                getReportDto().setIfscId(this.getUserIfscId());

                getReportDto().setStatusValue(getReportStatusValue(reportDto.getStatus(), statusList)); //To set the status value.
                setCurrentReportPrintTime(InstaReportUtil.formatDate
                                                (new Date(System.currentTimeMillis())
                                                , currentReportPrintTimeFormat));

                Message req = createMessage(sessionID, 202, 7, getReportDto());
                Message res = handle(sessionID, req);
                formatValueDate();  //Have done With date format dd-MMM-yyyy.
                neftRepDTO = (NEFTReportDTO) res.info;
                LoginBean lb = (LoginBean)request.getSession().getAttribute("loginBean");
                neftRepDTO.reportRunBy = lb.getUserId();
                //Since outward txn should be done only with detailed.
                getReportDto().setReportType("Detailed");
                //Done for a particular branch- by priyak
                if(!String.valueOf(getReportDto().getIfscId()).equals("0")) {
                    getReportDto().setBranchCode(getBranchIFSCCode(String.valueOf(getReportDto().getIfscId())));
                } else {
                    getReportDto().setBranchCode("ALL");
                }
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the NEFTReconciliation Report : " + e.getMessage());
                throw new ServerException(e.getMessage());
            }
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

        /**
         * Method to get Branchwisesubtype individual report
         */
        public void generatePaymentReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                /*
                 * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
                 * Assumption : FromDate and ToDate fileds will have values always.
                 * It will not be null or empty and the expected format dd-mm-yyyy.
                 */
                /*String inputFromDt = getReportDto().getValueDate();  //Commneted by priyak fro date fromat.
                String inputToDt = getReportDto().getToDate();
                inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
                inputToDt = InstaReportUtil.formatDateString(inputToDt);
                getReportDto().setValueDate(inputFromDt);
                getReportDto().setToDate(inputToDt);*/

                getReportDto().setStatusValue(getReportStatusValue(reportDto.getStatus(), statusList)); //To set the status value.
                setCurrentReportPrintTime(InstaReportUtil.formatDate(new Date(System.currentTimeMillis()),
                                                                    currentReportPrintTimeFormat));

                Message req = createMessage(sessionID, 202, 4, getReportDto());
                Message res = handle(sessionID, req);
                //formatValueDate();  //Have done With date format dd-MMM-yyyy.
                getReportDto().setStatus(getReportStatusValue(reportDto.getStatus(), statusList)); //To set the status value.
                List<NEFTDetailsReportDTO> detailsReportDTOList = (List) res.info;
                setDetailReportDTOs(detailsReportDTOList);
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the Payment Report "
                             + "Bank wise : " + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         * TODO
         */
        public void generateNeftBrInwReturnedReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                /*
                 * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
                 * Assumption : FromDate and ToDate fileds will have values always.
                 * It will not be null or empty and the expected format dd-mm-yyyy.
                 */
                /*String inputFromDt = getReportDto().getValueDate();       //Commented by priyak for date format.
                String inputToDt = getReportDto().getToDate();
                inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
                inputToDt = InstaReportUtil.formatDateString(inputToDt);
                getReportDto().setValueDate(inputFromDt);
                getReportDto().setToDate(inputToDt);*/

//                getReportDto().setIfscCode(this.getUserIfscCode());
//                getReportDto().setIfscId(this.getUserIfscId());
                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 202, 5, reportDto);
                Message res = handle(sessionID, req);
                getReportDto().setBranchCode(getBranchIFSCCode(String.valueOf(getReportDto().getIfscId())));
                reportMap = (Map<String, List<ReportDTO>>) res.info;

            } catch(Exception e) {

                logger.error("Exception ocurred while getting the Branch Inward Returned" +
                             " Report : " + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         * Method used to export the paymentreport in to Excel
         */
        public void paymentExportToExcel(ServletOutputStream out)
        throws Exception {

            try {
                List exportXLS = new ArrayList(1);
                long sno = 0;
                int rowCount = 0;
//                double totAmt = 0;
                BigDecimal totAmt = BigDecimal.ZERO;
                //Adding the items to a list
                for (Iterator i = getDetailReportDTOs().iterator(); i.hasNext();) {
                    NEFTDetailsReportDTO indentList = (NEFTDetailsReportDTO)i
                    .next();
                    exportXLS.add(indentList);

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

                    if (report.equalsIgnoreCase("submitted")) {
                        book.setSheetName(0,
                                      "Payments Submitted",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);
                    } else {
                        book.setSheetName(0,
                                          "Payments Received",
                                          HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);
                    }
                    caption_font = book.createFont();
                    caption_font.setFontHeightInPoints((short)10);
                    caption_font.setFontName("Verdana");
                    caption_style = book.createCellStyle();
                    caption_style.setFont(caption_font);
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    if (report.equalsIgnoreCase("submitted")) {
                        cell.setCellValue("Payment Submitted Report "+reportDto.getPaymentType()+"  From "+reportDto.getValueDate()+" to "+reportDto.getToDate()
                                          + " with status " + reportDto.getStatusValue());
                    } else {
                        cell.setCellValue("Payment Received Report "+reportDto.getPaymentType()+"  From "+reportDto.getValueDate()+" to "+reportDto.getToDate()
                                          + " with status " + reportDto.getStatusValue());
                    }
                    for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        for (short j = 0; j < 9; j++) {
                            cell = row.createCell(j);

                            // for header
                            if (roww == 0) {
                                if (j == 0) {
                                    cell.setCellValue("S.NO");
                                } else if (j == 1) {
                                    cell.setCellValue("VALUE DATE");
                                } else if (j == 2) {
                                    cell.setCellValue("SENDER ADDRESS");
                                } else if (j == 3) {
                                    cell.setCellValue("RECEIVER ADDRESS");
                                } else if (j == 4) {
                                    cell.setCellValue("UTR NUMBER");
                                } else if (j == 5) {
                                    cell.setCellValue("ACCOUNT NUMBER");
                                } else if (j == 6) {
                                    cell.setCellValue("BENIFICIARY DETAILS");
                                } else if (j == 7) {
                                    cell.setCellValue("AMOUNT(Rs)");
                                } else if (j == 8) {
                                    cell.setCellValue("STATUS");
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
                                    if (((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate() != null) {
                                        valueDate = ((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getValueDate();
                                    }
                                    cell.setCellValue(valueDate);
                                } else if (j == 2) {

                                    String sendAdd = null;

                                    if (((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getSenderAddress() != null) {
                                        sendAdd = ((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getSenderAddress();
                                    }
                                    cell.setCellValue(sendAdd);

                                } else if (j == 3) {

                                    String recAdd = null;

                                    if (((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getReceiverAddress() != null) {
                                        recAdd = ((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getReceiverAddress();
                                    }
                                    cell.setCellValue(recAdd);

                                }else if (j == 4) {

                                    String utrNo = null;

                                    if (((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo() != null) {
                                        utrNo = ((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getUtrNo();
                                    }
                                    cell.setCellValue(utrNo);

                                }else if (j == 5) {

                                    String accNo = null;

                                    if (((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getField6021() != null) {
                                        accNo = ((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getField6021();
                                    } else if (((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getField6061() != null) {
                                        accNo = ((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getField6061();
                                    }
                                    cell.setCellValue(accNo);

                                }else if (j == 6) {

                                    String beniDetails = null;

                                    if (((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getField5565() != null) {

                                        beniDetails = ((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getField5565();
                                        if(((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getField6081() != null) {

                                            beniDetails = ((NEFTDetailsReportDTO)exportXLS.get(roww - 1))
                                            .getField6081()+"-"+beniDetails;
                                        }
                                    } else if (((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getField6091() != null) {
                                        beniDetails = ((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getField6091();
                                    }
                                    cell.setCellValue(beniDetails);

                                }else if (j == 7) {

                                    String amt = null;

                                    if (new BigDecimal(((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0) {

//                                        totAmt += ((NEFTDetailsReportDTO)exportXLS
//                                        .get(roww - 1)).getAmount();
                                        totAmt = totAmt.add( new BigDecimal(((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getAmt()).setScale(2));
                                        amt = String.valueOf(((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getAmt());
                                    }
                                    cell.setCellValue(new BigDecimal(amt).setScale(2).toString());
                                } else if (j == 8) {                  //To add status column in the excel sheet.

                                    String status = null;
                                    if (((NEFTDetailsReportDTO)exportXLS
                                    .get(roww - 1)).getStatus() != null) {
                                        status = ((NEFTDetailsReportDTO)exportXLS
                                        .get(roww - 1)).getStatus();
                                    }
                                    cell.setCellValue(status);
                                }
                            }
                            cell.setCellStyle(caption_style);
                        }
                    }
                    row = sheet.createRow(rowCount);
                    cell = row.createCell((short)6);
                    cell.setCellValue("TOTAL AMOUNT");
                    cell.setCellStyle(caption_style);
                    cell = row.createCell((short)7);
                    cell.setCellValue(String.valueOf(totAmt));
                    cell.setCellStyle(caption_style);
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
         * Method used to export the graduated paymentreport in to Excel
         */
        public void graduatedPaymentExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                List <ReportDTO> exportXLS= new ArrayList<ReportDTO>(1);
                long sno = 0;
                int rowCount = 0;
                //double totAmt = 0;
                //Adding the items to a list
                for (Iterator i = getGraduadtedPayments().iterator(); i.hasNext();) {
                    ReportDTO dto = (ReportDTO)i
                    .next();
                    exportXLS.add(dto);

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
                                      "Graduated Payment",
                                      HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                    caption_font = book.createFont();
                    caption_font.setFontHeightInPoints((short)10);
                    caption_font.setFontName("Verdana");
                    caption_style = book.createCellStyle();
                    caption_style.setFont(caption_font);

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("");
                    cell = row.createCell((short)1);
                    cell.setCellValue("Datewise Graduated Payment Report");

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)10);
                    String dateForm = currentReportPrintTime.substring(0,11);
                    String time = currentReportPrintTime.substring(11);
                    cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                    rowCount += 1;
                    for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        for (short j = 0; j < 13; j++) {
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
                                    cell.setCellValue("Credit Amount(Rs)");
                                } else if (j == 7) {
                                    cell.setCellValue("Debit Amount(Rs)");
                                }  else if (j == 8) {
                                    cell.setCellValue("Batch Time");
                                }  else if (j == 9) {
                                    cell.setCellValue("Rescheduled Date");
                                }  else if (j == 10) {
                                    cell.setCellValue("Rescheduled Batch Time");
                                }  else if (j == 11) {
                                    cell.setCellValue("Rejected Date");
                                }  else if (j == 12) {
                                    cell.setCellValue("Rejected Batch Time");
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
                                    if ((exportXLS.get(roww - 1)).getUtrNo()!= null) {
                                        utrNo = (exportXLS.get(roww - 1)).getUtrNo();
                                    }
                                    cell.setCellValue(utrNo);
                                } else if (j == 2) {

                                    String msgType = null;

                                    if ((exportXLS.get(roww - 1)).getMsgType() != null) {
                                        msgType = (exportXLS.get(roww - 1)).getMsgType();
                                    }
                                    cell.setCellValue(msgType);

                                } else if (j == 3) {

                                    String tranType = null;

                                    if ((exportXLS.get(roww - 1)).getTranType() != null) {
                                        tranType = (exportXLS.get(roww - 1)).getTranType();
                                    }
                                    cell.setCellValue(tranType);

                                } else if (j == 4) {

                                    String sendAdd = null;

                                    if ((exportXLS.get(roww - 1)).getSenderAddress() != null) {
                                        sendAdd = (exportXLS.get(roww - 1)).getSenderAddress();
                                    }
                                    cell.setCellValue(sendAdd);

                                } else if (j == 5) {

                                    String recAdd = null;

                                    if ((exportXLS.get(roww - 1)).getReceiverAddress() != null) {
                                        recAdd = (exportXLS.get(roww - 1)).getReceiverAddress();
                                    }
                                    cell.setCellValue(recAdd);

                                }else if (j == 6) {         //Modified by priyak to maintain uniformity

                                    String crdDeb = null;
                                    String amount = null;
                                    if ((exportXLS.get(roww - 1)).getDebitCredit() != null) {
                                        crdDeb = (exportXLS.get(roww - 1)).getDebitCredit();
                                        if (crdDeb.equals("Credit")) {
                                            amount = (exportXLS.get(roww - 1)).getAmt();
                                        } else {
                                            amount = "0.00";
                                        }
                                    }
                                    cell.setCellValue(amount);

                                }else if (j == 7) {

                                    String crdDeb = null;
                                    String amount = null;
                                    if ((exportXLS.get(roww - 1)).getDebitCredit() != null) {
                                        crdDeb = (exportXLS.get(roww - 1)).getDebitCredit();
                                        if (crdDeb.equals("Debit")) {
                                            amount = (exportXLS.get(roww - 1)).getAmt();
                                        } else {
                                            amount = "0.00";
                                        }
                                    }
                                    cell.setCellValue(amount);
                                } else if (j == 8) {

                                    String batchTime = null;

                                    if ((exportXLS.get(roww - 1)).getBatchTime() != null) {
                                        batchTime = (exportXLS.get(roww - 1)).getBatchTime();
                                    }
                                    cell.setCellValue(batchTime);
                                } else if (j == 9) {

                                    String reshDate = null;

                                    if ((exportXLS.get(roww - 1)).getReshDate() != null) {
                                        reshDate = (exportXLS.get(roww - 1)).getReshDate();
                                        reshDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, reshDate);
                                    }
                                    cell.setCellValue(reshDate);
                                } else if (j == 10) {

                                    String reshBatchTime = null;

                                    if ((exportXLS.get(roww - 1)).getReshBatchTime() != null) {
                                        reshBatchTime = (exportXLS.get(roww - 1)).getReshBatchTime();
                                    }
                                    cell.setCellValue(reshBatchTime);
                                } else if (j == 11) {

                                    String rejDate = null;

                                    if ((exportXLS.get(roww - 1)).getRejDate() != null) {
                                        rejDate = (exportXLS.get(roww - 1)).getRejDate();
                                        rejDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, rejDate);
                                    }
                                    cell.setCellValue(rejDate);
                                } else if (j == 12) {

                                    String rejBatchTime = null;

                                    if ((exportXLS.get(roww - 1)).getRejBatchTime() != null) {
                                        rejBatchTime = (exportXLS.get(roww - 1)).getRejBatchTime();
                                    }
                                    cell.setCellValue(rejBatchTime);
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
         * Method used to export the Br inward returned report in to Excel
         */
        public void returnedInwardExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int roww = 0;
//                double totAmt = 0;
                BigDecimal totAmt = BigDecimal.ZERO;
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;
                //int roww = 0;
                Set keySet = getReportMap().keySet();
                Iterator it = keySet.iterator();

                book.setSheetName(0,
                                  "Inward Returned",
                                  HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);
                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch Report - Inward Returned from "+
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)5);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                roww += 1;
                while (it.hasNext()) {

                    List exportXLS = new ArrayList(1);
//                    double subTotal = 0;
                    BigDecimal subTotal = BigDecimal.ZERO;
                    String date = (String) it.next();
                    List listRep = (List) reportMap.get(date);
                    for (Iterator itr = listRep.iterator();itr.hasNext();) {
                        ReportDTO repDTO = (ReportDTO)itr.next();
                        exportXLS.add(repDTO);
                    }
                    row = sheet.createRow(roww);
                    roww += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("DATE :"+date+ " BATCH TIME :"+reportDto.getBatchTime());

                    //Only If the DTO is not empty
                    if (exportXLS.size() != 0) {

                        for (int i = exportXLS.size(),count = 0; count <= i; count++) {

                            row = sheet.createRow(roww);
                            roww += 1;
                            for (short j = 0; j < 8; j++) {

                                cell = row.createCell(j);

                                // for header
                                if (count == 0) {
                                    // for header
                                    if (j == 0) {
                                        cell.setCellValue("S.NO");
                                    } else if (j == 1) {
                                        cell.setCellValue("VALUE DATE");
                                    } else if (j == 2) {
                                        cell.setCellValue("MSG TYPE");
                                    } else if (j == 3) {
                                        cell.setCellValue("UTR NUMBER");
                                    } else if (j == 4) {
                                        cell.setCellValue("SENDER ADDRESS");
                                    } else if (j == 5) {
                                        cell.setCellValue("RECEIVER ADDRESS");
                                    } else if (j == 6) {
                                        cell.setCellValue("OUTWARD UTR NO");
                                    } else if (j == 7) {
                                        cell.setCellValue("AMOUNT(Rs)");
                                    }
                                } else {

                                    cell = row.createCell(j);
                                    // Setting values in cell for each and every row
                                    if (j == 0) {
                                        cell.setCellValue(count);
                                    } else if (j == 1) {

                                        String valueDate = null;
                                        if (date != null) {
                                            valueDate = date;
                                        }
                                        valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                        cell.setCellValue(valueDate);
                                    } else if (j == 2) {

                                        String msgType = null;

                                        if (((ReportDTO)exportXLS
                                        .get(count-1)).getMsgType() != null) {

                                            msgType = ((ReportDTO)exportXLS
                                            .get(count-1)).getMsgType();
                                        }
                                        cell.setCellValue(msgType);

                                    } else if (j == 3) {

                                        String utrNo = null;

                                        if (((ReportDTO)exportXLS
                                        .get(count-1)).getUtrNo() != null) {
                                            utrNo = ((ReportDTO)exportXLS
                                            .get(count-1)).getUtrNo();
                                        }
                                        cell.setCellValue(utrNo);

                                    } else if (j == 4) {

                                        String sendAdd = null;

                                        if (((ReportDTO)exportXLS
                                        .get(count-1)).getSenderAddress() != null) {
                                            sendAdd = ((ReportDTO)exportXLS
                                            .get(count-1)).getSenderAddress();
                                        }
                                        cell.setCellValue(sendAdd);

                                    } else if (j == 5) {

                                        String recAdd = null;

                                        if (((ReportDTO)exportXLS
                                        .get(count-1)).getReceiverAddress() != null) {
                                            recAdd = ((ReportDTO)exportXLS
                                            .get(count-1)).getReceiverAddress();
                                        }
                                        cell.setCellValue(recAdd);

                                    } else if (j == 6) {

                                        String outUtr = null;

                                        if (((ReportDTO)exportXLS
                                        .get(count-1)).getOutUTRNo() != null) {

                                            outUtr = ((ReportDTO)exportXLS
                                            .get(count-1)).getOutUTRNo();
                                        }
                                        cell.setCellValue(outUtr);
                                    } else if (j == 7) {

                                        String amt = null;

                                        //                                    if (((ReportDTO)exportXLS
//                                        .get(count-1)).getAmount() != 0) {
                                        if ( new BigDecimal(((ReportDTO)exportXLS.get(count-1)).getAmt()).compareTo(BigDecimal.ZERO) != 0) {
//                                            totAmt += ((ReportDTO)exportXLS
//                                            .get(count-1)).getAmount();
//                                            subTotal += ((ReportDTO)exportXLS
//                                            .get(count-1)).getAmount();
//                                            amt = String.valueOf(((ReportDTO)exportXLS
//                                            .get(count-1)).getAmount());
                                            totAmt = totAmt.add(new BigDecimal(((ReportDTO)exportXLS
                                            .get(count-1)).getAmt()));
                                            subTotal = subTotal.add(new BigDecimal(((ReportDTO)exportXLS
                                            .get(count-1)).getAmt()));
                                            amt = String.valueOf(((ReportDTO)exportXLS
                                            .get(count-1)).getAmt());
                                        }
                                        cell.setCellValue(new BigDecimal(amt).setScale(2).toString());
                                    }
                                    cell.setCellStyle(caption_style);
                                }
                            }
                       }
                    row = sheet.createRow(roww);
                    roww += 1;
                    cell = row.createCell((short)6);
                    cell.setCellValue("Sub Total(Date :"+date+")");
                    cell = row.createCell((short)7);
                    //cell.setCellValue(String.valueOf(subTotal));
                    cell.setCellValue(subTotal.setScale(2).toString());
                }
            }
                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)6);
                cell.setCellValue("Total Amount");
                cell = row.createCell((short)7);
               // cell.setCellValue(String.valueOf(totAmt));
                cell.setCellValue(totAmt.setScale(2).toString());

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
         * Method used to export the Batchwise Reconcilition report in to Excel
         */
        public void batchwiseReconcillationExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int roww = 0;
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;
                //int roww = 0;
                Set keySet = getReconcillationMap().keySet();
                Iterator it = keySet.iterator();

                book.setSheetName(0,
                                  "Batchwise Reconciliation",
                                  HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);

                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batchwise - Reconciliation Report ");

                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Report Date");
                cell = row.createCell((short)1);
                cell.setCellValue(InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getValueDate()));
               /* row = sheet.createRow(roww);    //Commented by priyak
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Transaction Type");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getTransactionType());*/
                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batch Time");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getBatchTime());
                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)9);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                roww += 1;

                while (it.hasNext()) {

                    String type = null;
                    List exportXLS = new ArrayList(1);
                    String key = (String) it.next();
                    List listRep =  reconcillationMap.get(key);
                    if (listRep.size() > 0) {

                        for (Iterator itr = listRep.iterator();itr.hasNext();) {
                            if (key.equals("N04")) {

                                type = "As Per N04";
                                NEFTN04DetailsDTO n04DTO = (NEFTN04DetailsDTO)itr.next();
                                exportXLS.add(n04DTO);
                            } else {

                                type = "As Per LMS";
                                BatchwiseReconcillationDTO n04DTO = (BatchwiseReconcillationDTO)itr.next();
                                exportXLS.add(n04DTO);
                            }
                        }
                        row = sheet.createRow(roww);
                        roww += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue(type);

                        row = sheet.createRow(roww);
                        roww += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("");
                        cell = row.createCell((short)1);
                        cell.setCellValue("");
                        cell = row.createCell((short)2);
                        cell.setCellValue("Outward Transactions");
                        cell = row.createCell((short)3);
                        cell.setCellValue("");
                        cell = row.createCell((short)4);
                        cell.setCellValue("");
                        cell = row.createCell((short)5);
                        cell.setCellValue("");
                        cell = row.createCell((short)6);
                        cell.setCellValue("");
                        cell = row.createCell((short)7);
                        cell.setCellValue("");
                        cell = row.createCell((short)8);
                        cell.setCellValue("Inward Transactions");

                        if (type.equalsIgnoreCase("As Per N04")) {

                            if (exportXLS.size() != 0) {

                                for (int i = exportXLS.size(),count = 0; count <= i; count++) {

                                    row = sheet.createRow(roww);
                                    roww += 1;
                                    for (short j = 0; j < 12; j++) {

                                        cell = row.createCell(j);

                                        // for header
                                        if (count == 0) {

                                            if (j == 0) {
                                                cell.setCellValue("S.No");
                                            } else if (j == 1) {
                                                cell.setCellValue("Batch Time");
                                            } else if (j == 2) {
                                                cell.setCellValue("Total no.of txns Sent");
                                            } else if (j == 3) {
                                                cell.setCellValue("Total amount Sent");
                                            } else if (j == 4) {
                                                cell.setCellValue("Total no.of txns Accepted");
                                            } else if (j == 5) {
                                                cell.setCellValue("Total amount Accepted");
                                            } else if (j == 6) {
                                                cell.setCellValue("Total no.of txns Rejected");
                                            } else if (j == 7) {
                                                cell.setCellValue("Total amount Rejected");
                                            } else if (j == 8) {
                                                cell.setCellValue("Total no.of txns Received");
                                            } else if (j == 9) {
                                                cell.setCellValue("Total amount Received");
                                            } else if (j == 10) {
                                                cell.setCellValue("Total no.of txns Returned");
                                            } else if (j == 11) {
                                                cell.setCellValue("Total amount Returned");
                                            }
                                        } else {

                                            cell = row.createCell(j);
                                            // Setting values in cell for each and every row
                                            if (j == 0) {
                                                cell.setCellValue(count);
                                            } else if (j == 1) {

                                                String batchTime = null;

                                                if (((NEFTN04DetailsDTO)exportXLS
                                                .get(count-1)).getField3535() != null) {

                                                    batchTime = ((NEFTN04DetailsDTO)exportXLS
                                                    .get(count-1)).getField3535();
                                                }
                                                cell.setCellValue(batchTime);
                                            } else if (j == 2) {

                                                String noSent = null;
                                                noSent = ((NEFTN04DetailsDTO)exportXLS
                                                .get(count-1)).getField5175();
                                                cell.setCellValue(noSent);

                                            } else if (j == 3) {

                                                String amtSent = "0.00";

                                                if (((NEFTN04DetailsDTO)exportXLS
                                                        .get(count-1)).getField4105() != null) {

                                                    amtSent = ((NEFTN04DetailsDTO)exportXLS
                                                    .get(count-1)).getField4105();
                                                }
                                                //cell.setCellValue(amtSent);
                                                cell.setCellValue(new BigDecimal(amtSent).setScale(2).toString());

                                            } else if (j == 4) {

                                                String noAccept = null;
                                                noAccept = ((NEFTN04DetailsDTO)exportXLS
                                                          .get(count-1)).getField5180();
                                                cell.setCellValue(noAccept);

                                            } else if (j == 5) {

                                                String amtAccept = "0.00";

                                                if (((NEFTN04DetailsDTO)exportXLS
                                                         .get(count-1)).getField4110() != null) {

                                                   amtAccept = ((NEFTN04DetailsDTO)exportXLS
                                                    .get(count-1)).getField4110();
                                                   if (amtAccept.indexOf(",") != -1) {
                                                       amtAccept = amtAccept.replace(",",".");
                                                   }
                                                }
                                                //cell.setCellValue(amtAccept);
                                                cell.setCellValue(new BigDecimal(amtAccept).setScale(2).toString());
                                            } else if (j == 6) {

                                                String noReject = null;
                                                noReject = ((NEFTN04DetailsDTO)exportXLS
                                                         .get(count-1)).getField5185();
                                                cell.setCellValue(noReject);
                                            } else if (j == 7) {

                                                String amtReject = "0.00";
                                                if (((NEFTN04DetailsDTO)exportXLS
                                                .get(count-1)).getField4115() != null) {

                                                    amtReject = ((NEFTN04DetailsDTO)exportXLS
                                                         .get(count-1)).getField4115();
                                                }
                                                //cell.setCellValue(amtReject);
                                                cell.setCellValue(new BigDecimal(amtReject).setScale(2).toString());
                                            } else if (j == 8) {

                                                String noReceive = null;
                                                noReceive = ((NEFTN04DetailsDTO)exportXLS
                                                         .get(count-1)).getField5267();
                                                cell.setCellValue(noReceive);
                                            } else if (j == 9) {

                                                String amtReceive = "0.00";
                                                if (((NEFTN04DetailsDTO)exportXLS
                                                .get(count-1)).getField4410() != null) {

                                                    amtReceive = ((NEFTN04DetailsDTO)exportXLS
                                                         .get(count-1)).getField4410();
                                                }
                                                //cell.setCellValue(amtReceive);
                                                cell.setCellValue(new BigDecimal(amtReceive).setScale(2).toString());
                                            } else if (j == 10) {

                                                String noReturn = null;
                                                noReturn = ((NEFTN04DetailsDTO)exportXLS
                                                         .get(count-1)).getField5047();
                                                cell.setCellValue(noReturn);
                                            } else if (j == 11) {

                                                String amtReturn = "0.00";
                                                if (((NEFTN04DetailsDTO)exportXLS
                                                .get(count-1)).getField4460() != null) {

                                                    amtReturn = ((NEFTN04DetailsDTO)exportXLS
                                                         .get(count-1)).getField4460();
                                                }
                                                //cell.setCellValue(amtReturn);
                                                cell.setCellValue(new BigDecimal(amtReturn).setScale(2).toString());
                                            }
                                            cell.setCellStyle(caption_style);
                                        }
                                    }
                               }
                          }
                    } else {
                        if (exportXLS.size() != 0) {
                            for (int i = exportXLS.size(),count = 0; count <= i; count++) {

                                row = sheet.createRow(roww);
                                roww += 1;
                                for (short j = 0; j < 12; j++) {

                                    cell = row.createCell(j);

                                    // for header
                                    if (count == 0) {

                                        if (j == 0) {
                                            cell.setCellValue("S.No");
                                        } else if (j == 1) {
                                            cell.setCellValue("Batch Time");
                                        } else if (j == 2) {
                                            cell.setCellValue("Outward total no.of txns Sent");
                                        } else if (j == 3) {
                                            cell.setCellValue("Outward Total Amount");
                                        } else if (j == 4) {
                                            cell.setCellValue("Total no.of txns Settled,Rescheduled");
                                        } else if (j == 5) {
                                            cell.setCellValue("Total amount Settled,Rescheduled");
                                        } else if (j == 6) {
                                            cell.setCellValue("Total no.of txns Unsuccessful");
                                        } else if (j == 7) {
                                            cell.setCellValue("Total Amount Unsuccessful");
                                        } else if (j == 8) {
                                            cell.setCellValue("Inward total no.of Txns Received");
                                        } else if (j == 9) {
                                            cell.setCellValue("Inward total amount Received");
                                        } else if (j == 10) {
                                            cell.setCellValue("Total no.of txns returned");
                                        } else if (j == 11) {
                                            cell.setCellValue("Total amount returned");
                                        }
                                    } else {

                                        cell = row.createCell(j);
                                        // Setting values in cell for each and every row
                                        if (j == 0) {
                                            cell.setCellValue(count);
                                        } else if (j == 1) {

                                            String batchTime = null;

                                            if (((BatchwiseReconcillationDTO)exportXLS
                                            .get(count-1)).getBatchTime() != null) {

                                                batchTime = ((BatchwiseReconcillationDTO)exportXLS
                                                .get(count-1)).getBatchTime();
                                            }
                                            cell.setCellValue(batchTime);
                                        } else if (j == 2) {

                                            long noSent = 0;
                                            noSent = ((BatchwiseReconcillationDTO)exportXLS
                                                .get(count-1)).getOwTxnAccepted() + ((BatchwiseReconcillationDTO)exportXLS
                                                .get(count-1)).getOwTxnRejected();
                                            cell.setCellValue(noSent);

                                        } else if (j == 3) {

//                                            double owTxnSentAmt = 0;
                                            BigDecimal owTxnSentAmt = BigDecimal.ZERO;
                                            BigDecimal owTxnSentAcceptedAmt = new BigDecimal((((BatchwiseReconcillationDTO)exportXLS
                                                        .get(count-1)).getOwTxnAmtAccepted()));
                                            BigDecimal owTxnSentRejctedAmt = new BigDecimal((((BatchwiseReconcillationDTO)exportXLS
                                                        .get(count-1)).getOwTxnAmtRejected()));

                                            owTxnSentAmt = owTxnSentAcceptedAmt.add(owTxnSentRejctedAmt);
                                            //cell.setCellValue(String.valueOf(owTxnSentAmt));
                                            cell.setCellValue(owTxnSentAmt.setScale(2).toString());
                                        } else if (j == 4) {

                                            long noAccept = 0;
                                            noAccept = ((BatchwiseReconcillationDTO)exportXLS
                                                      .get(count-1)).getOwTxnAccepted();
                                            //cell.setCellValue(noAccept);
                                            cell.setCellValue(new BigDecimal(noAccept).setScale(2).toString());

                                        } else if (j == 5) {

                                            String amtAccept = "0.00";

                                            if (((BatchwiseReconcillationDTO)exportXLS
                                                     .get(count-1)).getOwTxnAmtAccepted() != null) {

                                               amtAccept = ((BatchwiseReconcillationDTO)exportXLS
                                                .get(count-1)).getOwTxnAmtAccepted();
                                            }
                                            //cell.setCellValue(amtAccept);
                                            cell.setCellValue(new BigDecimal(amtAccept).setScale(2).toString());
                                        } else if (j == 6) {

                                            long noReject = 0;
                                            noReject = ((BatchwiseReconcillationDTO)exportXLS
                                                     .get(count-1)).getOwTxnRejected();
                                            cell.setCellValue(noReject);
                                        } else if (j == 7) {

                                            String amtReject = null;
                                            if (((BatchwiseReconcillationDTO)exportXLS
                                            .get(count-1)).getOwTxnAmtRejected() != null) {

                                                amtReject = ((BatchwiseReconcillationDTO)exportXLS
                                                     .get(count-1)).getOwTxnAmtRejected();
                                            }
                                            //cell.setCellValue(amtReject);
                                            cell.setCellValue(new BigDecimal(amtReject).setScale(2).toString());
                                        } else if (j == 8) {

                                            long noReceive = 0;
                                            noReceive = ((BatchwiseReconcillationDTO)exportXLS
                                                     .get(count-1)).getIwTxnReceived()+((BatchwiseReconcillationDTO)exportXLS
                                                     .get(count-1)).getIwTxnReturned();
                                            cell.setCellValue(noReceive);
                                        } else if (j == 9) {

                                            String amtReceive = "0.00";
                                            String amtReturn = "0.00";
                                            if (((BatchwiseReconcillationDTO)exportXLS
                                            .get(count-1)).getIwTxnAmtReceived() != null) {

                                                amtReceive = ((BatchwiseReconcillationDTO)exportXLS
                                                     .get(count-1)).getIwTxnAmtReceived();
                                            }
                                            if (((BatchwiseReconcillationDTO)exportXLS
                                            .get(count-1)).getIwTxnAmtReturned() != null) {

                                                amtReturn = ((BatchwiseReconcillationDTO)exportXLS
                                                     .get(count-1)).getIwTxnAmtReturned();
                                            }
                                            BigDecimal totInw = new BigDecimal(amtReceive).add(new BigDecimal(amtReturn));

                                            //cell.setCellValue(amtReceive);
                                            cell.setCellValue(totInw.setScale(2).toString());
                                        } else if (j == 10) {

                                            long noReturn = 0;
                                            noReturn = ((BatchwiseReconcillationDTO)exportXLS
                                                     .get(count-1)).getIwTxnReturned();
                                            cell.setCellValue(noReturn);
                                        } else if (j == 11) {

                                            String amtReturn = "0.00";
                                            if (((BatchwiseReconcillationDTO)exportXLS
                                            .get(count-1)).getIwTxnAmtReturned() != null) {

                                                amtReturn = ((BatchwiseReconcillationDTO)exportXLS
                                                     .get(count-1)).getIwTxnAmtReturned();
                                            }
                                            //cell.setCellValue(amtReturn);
                                            cell.setCellValue(new BigDecimal(amtReturn).setScale(2).toString());
                                        }
                                        cell.setCellStyle(caption_style);
                                    }
                                }
                            }
                        }
                    }
                } else {

                    if(key.equals("N04")) {
                        row = sheet.createRow(roww);
                        roww += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("As Per N04");
                        row = sheet.createRow(roww);
                        roww += 1;
                        for (short j = 0; j < 12; j++) {

                            cell = row.createCell(j);
                            if (j == 0) {
                                cell.setCellValue("S.No");
                            } else if (j == 1) {
                                cell.setCellValue("Batch Time");
                            } else if (j == 2) {
                                cell.setCellValue("Total no.of txns Sent");
                            } else if (j == 3) {
                                cell.setCellValue("Total amount Sent");
                            } else if (j == 4) {
                                cell.setCellValue("Total no.of txns Accepted");
                            } else if (j == 5) {
                                cell.setCellValue("Total amount Accepted");
                            } else if (j == 6) {
                                cell.setCellValue("Total no.of txns Rejected");
                            } else if (j == 7) {
                                cell.setCellValue("Total amount Rejected");
                            } else if (j == 8) {
                                cell.setCellValue("Total no.of txns Received");
                            } else if (j == 9) {
                                cell.setCellValue("Total amount Received");
                            } else if (j == 10) {
                                cell.setCellValue("Total no.of txns Returned");
                            } else if (j == 11) {
                                cell.setCellValue("Total amount Returned");
                            }
                        }
                        row = sheet.createRow(roww);
                        roww += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("No Records Found");
                    } else {

                            row = sheet.createRow(roww);
                            roww += 1;
                            cell = row.createCell((short)0);
                            cell.setCellValue("As Per LMS");
                            row = sheet.createRow(roww);
                            roww += 1;
                            for (short j = 0; j < 12; j++) {

                                cell = row.createCell(j);
                                if (j == 0) {
                                    cell.setCellValue("S.No");
                                } else if (j == 1) {
                                    cell.setCellValue("Batch Time");
                                } else if (j == 2) {
                                    cell.setCellValue("Outward total no.of txns Sent");
                                } else if (j == 3) {
                                    cell.setCellValue("Outward Total Amount");
                                } else if (j == 4) {
                                    cell.setCellValue("Total no.of txns Settled,Rescheduled");
                                } else if (j == 5) {
                                    cell.setCellValue("Total amount Settled,Rescheduled");
                                } else if (j == 6) {
                                    cell.setCellValue("Total no.of txns Unsuccessful");
                                } else if (j == 7) {
                                    cell.setCellValue("Total Amount Unsuccessful");
                                } else if (j == 8) {
                                    cell.setCellValue("Inward total no.of Txns Received");
                                } else if (j == 9) {
                                    cell.setCellValue("Inward total amount Received");
                                } else if (j == 10) {
                                    cell.setCellValue("Total no.of txns returned");
                                } else if (j == 11) {
                                    cell.setCellValue("Total amount returned");
                                }
                            }
                            row = sheet.createRow(roww);
                            roww += 1;
                            cell = row.createCell((short)0);
                            cell.setCellValue("No Records Found");
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
         * Method used to export the Batchwise aggregate report in to Excel
         */
        public void batchwiseAggregateExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int roww = 0;
                BigDecimal grandCredTotAmt = BigDecimal.ZERO;
                BigDecimal grandDebTotAmt = BigDecimal.ZERO;
                BigDecimal grandAggTotAmt = BigDecimal.ZERO;
//                double grandCredTotAmt = 0;
//                double grandDebTotAmt = 0;
//                double grandAggTotAmt = 0;
                long grandTotCredit = 0;
                long grandTotDebit = 0;
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;
                //int roww = 0;
                Set keySet = getAggregateMap().keySet();
                Iterator it = keySet.iterator();

                book.setSheetName(0,
                                  "Batchwise Aggregate Detailed",
                                  HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);

                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batchwise Aggregate Detailed Report for " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getValueDate()));
                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch");
                cell = row.createCell((short)1);
                String brName = getBranchName(String.valueOf(getReportDto().getIfscId()));
                cell.setCellValue(brName);
                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batch Time");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getBatchTime());
                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)4);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                roww += 1;
                while (it.hasNext()) {

                    List exportXLS = new ArrayList(1);
//                    double credTotAmt = 0;
//                    double debTotAmt = 0;
//                    double aggTotAmt = 0;
                    BigDecimal credTotAmt = BigDecimal.ZERO;
                    BigDecimal debTotAmt = BigDecimal.ZERO;
                    BigDecimal aggTotAmt = BigDecimal.ZERO;
                    long totCredit = 0;
                    long totDebit = 0;
                    String batchTime = (String) it.next();
                    List listRep = (List)aggregateMap.get(batchTime);
                    for (Iterator itr = listRep.iterator();itr.hasNext();) {
                        BatchwiseAggregateDTO aggDTO = (BatchwiseAggregateDTO)itr.next();
                        exportXLS.add(aggDTO);
                    }
                    row = sheet.createRow(roww);
                    roww += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("Batch :");
                    cell = row.createCell((short)1);
                    cell.setCellValue(batchTime);
                    //Only If the DTO is not empty
                    if (exportXLS.size() != 0) {

                        for (int i = exportXLS.size(),count = 0; count <= i; count++) {

                            row = sheet.createRow(roww);
                            roww += 1;
                            for (short j = 0; j < 7; j++) {

                                cell = row.createCell(j);

                                // for header
                                if (count == 0) {
                                    // for header
                                    if (j == 0) {
                                        cell.setCellValue("S.NO");
                                    } else if (j == 1) {
                                        cell.setCellValue("BRANCH IFSC CODE");
                                    } else if (j == 2) {
                                        cell.setCellValue("NO OF CREDITS");
                                    } else if (j == 3) {
                                        cell.setCellValue("CREDIT AMOUNT(Rs)");
                                    } else if (j == 4) {
                                        cell.setCellValue("NO OF DEBITS");
                                    } else if (j == 5) {
                                        cell.setCellValue("DEBIT AMOUNT(Rs)");
                                    } else if (j == 6) {
                                        cell.setCellValue("AGGREGATE AMOUNT (CREDIT-DEBIT)(Rs)");
                                    }
                                } else {

                                    cell = row.createCell(j);
                                    // Setting values in cell for each and every row
                                    if (j == 0) {

//                                        String no = null;
    //
//                                        sno += 1;
//                                        no = String.valueOf(sno);
                                        cell.setCellValue(count);
                                    } else if (j == 1) {

                                        String ifsc = null;

                                        if (((BatchwiseAggregateDTO)exportXLS
                                        .get(count-1)).getIfsc() != null) {

                                            ifsc = ((BatchwiseAggregateDTO)exportXLS
                                            .get(count-1)).getIfsc();
                                        }
                                        cell.setCellValue(ifsc);

                                    } else if (j == 2) {

                                        long noCredit = 0;

                                        noCredit = ((BatchwiseAggregateDTO)exportXLS
                                        .get(count-1)).getNoOfCredits();
                                        totCredit += noCredit;
                                        grandTotCredit += noCredit;
                                        cell.setCellValue(String.valueOf(noCredit));

                                    } else if (j == 3) {

                                        String credAmt = null;

                                        if (((BatchwiseAggregateDTO)exportXLS
                                        .get(count-1)).getCreditAmount() != null) {

                                            credTotAmt = credTotAmt.add( new BigDecimal(((BatchwiseAggregateDTO)exportXLS
                                            .get(count-1)).getCreditAmount()).setScale(2));
                                            credAmt = ((BatchwiseAggregateDTO)exportXLS
                                            .get(count-1)).getCreditAmount();
                                            grandCredTotAmt = grandCredTotAmt.add(new BigDecimal(((BatchwiseAggregateDTO)exportXLS
                                            .get(count-1)).getCreditAmount()).setScale(2));

                                        }
                                        cell.setCellValue(new BigDecimal(credAmt).setScale(2).toString());

                                    } else if (j == 4) {

                                        long noDebit = 0;
                                        noDebit = ((BatchwiseAggregateDTO)exportXLS
                                            .get(count-1)).getNoOfDebits();
                                        totDebit += noDebit;
                                        grandTotDebit += noDebit;
                                        cell.setCellValue(noDebit);

                                    } else if (j == 5) {

                                        String debitAmt = null;

                                        if (((BatchwiseAggregateDTO)exportXLS
                                        .get(count-1)).getDebitAmount() != null) {

                                            debTotAmt = debTotAmt.add( new BigDecimal(((BatchwiseAggregateDTO)exportXLS
                                            .get(count-1)).getDebitAmount()).setScale(2));
//                                            grandDebTotAmt += Double.valueOf(((BatchwiseAggregateDTO)exportXLS
//                                            .get(count-1)).getDebitAmount());
                                            grandDebTotAmt = grandDebTotAmt.add(new BigDecimal(((BatchwiseAggregateDTO)exportXLS
                                            .get(count-1)).getDebitAmount()).setScale(2));
                                            debitAmt = ((BatchwiseAggregateDTO)exportXLS
                                            .get(count-1)).getDebitAmount();
                                        }
                                        cell.setCellValue(new BigDecimal(debitAmt).setScale(2).toString());
                                    } else if (j == 6) {

//                                        double aggAmt = 0;
    //
//                                        double credit = Double.valueOf(((BatchwiseAggregateDTO)exportXLS.get(count-1)).getCreditAmount());
//                                        double debit  = Double.valueOf(((BatchwiseAggregateDTO)exportXLS.get(count-1)).getDebitAmount());
                                        BigDecimal aggAmt = BigDecimal.ZERO;
                                        BigDecimal credit = new BigDecimal(((BatchwiseAggregateDTO)exportXLS.get(count-1)).getCreditAmount());
                                        BigDecimal debit  = new BigDecimal(((BatchwiseAggregateDTO)exportXLS.get(count-1)).getDebitAmount());
                                        aggAmt = credit.subtract(debit).setScale(2);
                                        aggTotAmt = aggTotAmt.add(aggAmt).setScale(2);
                                        grandAggTotAmt = grandAggTotAmt.add(aggAmt).setScale(2);
                                        cell.setCellValue(String.valueOf(aggAmt));
                                    }
                                    cell.setCellStyle(caption_style);
                                }
                            }
                       }
                    row = sheet.createRow(roww);
                    roww += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("TOTAL");
                    cell = row.createCell((short)2);
                    cell.setCellValue(totCredit);
                    cell = row.createCell((short)3);
                    cell.setCellValue(String.valueOf(credTotAmt));
                    cell = row.createCell((short)4);
                    cell.setCellValue(totDebit);
                    cell = row.createCell((short)5);
                    cell.setCellValue(String.valueOf(debTotAmt));
                    cell = row.createCell((short)6);
                    cell.setCellValue(String.valueOf(aggTotAmt));
                }
            }

                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("");

                row = sheet.createRow(roww);
                roww += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("GRAND TOTAL");
                cell = row.createCell((short)2);
                cell.setCellValue(grandTotCredit);
                cell = row.createCell((short)3);
                cell.setCellValue(String.valueOf(grandCredTotAmt));
                cell = row.createCell((short)4);
                cell.setCellValue(grandTotDebit);
                cell = row.createCell((short)5);
                cell.setCellValue(String.valueOf(grandDebTotAmt));
                cell = row.createCell((short)6);
                cell.setCellValue(String.valueOf(grandAggTotAmt));

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
         * Method for Exporting the NEFT branchwise aggregate Report
         * @parameter ServletOutputStream
         * @return void
         */
        public void batchwiseAggregateSummaryExportToExcel(ServletOutputStream out)
        throws Exception {

            int roww = 0;
            BigDecimal grandAggTotAmt = BigDecimal.ZERO;
            String batchTime = "";
            BatchwiseAggregateDTO aggDTO = null;
            HSSFWorkbook book = new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet();
            HSSFRow row = null;
            HSSFCell cell = null;
            HSSFFont caption_font = null;
            HSSFCellStyle caption_style = null;
            //int roww = 0;
            Set keySet = getAggregateMap().keySet();
            Iterator it = keySet.iterator();

            book.setSheetName(0,
                              "Batchwise Aggregate Summary",
                              HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

            caption_font = book.createFont();
            caption_font.setFontHeightInPoints((short)10);
            caption_font.setFontName("Verdana");
            caption_style = book.createCellStyle();
            caption_style.setFont(caption_font);
            row = sheet.createRow(roww);
            roww += 1;
            cell = row.createCell((short)0);
            cell.setCellValue("Batchwise Aggregate Summary Report for "+
                              InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getValueDate()));
            row = sheet.createRow(roww);
            roww += 1;
            cell = row.createCell((short)0);
            cell.setCellValue("Branch");
            cell = row.createCell((short)1);
            String brName = getBranchName(String.valueOf(getReportDto().getIfscId()));
            cell.setCellValue(brName);
            row = sheet.createRow(roww);
            roww += 1;
            cell = row.createCell((short)0);
            cell.setCellValue("Batch Time");
            cell = row.createCell((short)1);
            cell.setCellValue(getReportDto().getBatchTime());
            row = sheet.createRow(roww);
            roww += 1;
            cell = row.createCell((short)3);
            String dateForm = currentReportPrintTime.substring(0,11);
            String time = currentReportPrintTime.substring(11);
            cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
            roww += 1;
            for (short j = 0; j < 6; j++) {

                cell = row.createCell(j);

                    // for header
                    if (j == 0) {
                        cell.setCellValue("Batch Time");
                    } else if (j == 1) {
                        cell.setCellValue("No. of credits");
                    } else if (j == 2) {
                        cell.setCellValue("Credit Amount (Rs.)");
                    } else if (j == 3) {
                        cell.setCellValue("No. of debits");
                    } else if (j == 4) {
                        cell.setCellValue("debit Amount (Rs.)");
                    } else if (j == 5) {
                        cell.setCellValue("Aggregate Amount (Credit-Debit)(Rs.)");
                    }
            }
            while (it.hasNext()) {

                batchTime = (String) it.next();
                aggDTO = (BatchwiseAggregateDTO)aggregateMap.get(batchTime);

                row = sheet.createRow(roww);
                roww += 1;
                for (short j = 0; j < 6; j++) {

                    cell = row.createCell(j);
                    // for header
                    if (j == 0) {
                        cell.setCellValue(aggDTO.getBatchTime());
                    } else if (j == 1) {
                        cell.setCellValue(aggDTO.getNoOfCredits());
                    } else if (j == 2) {
                        cell.setCellValue(new BigDecimal(aggDTO.getCreditAmount()).setScale(2).toString());
                    } else if (j == 3) {
                        cell.setCellValue(aggDTO.getNoOfDebits());
                    } else if (j == 4) {
                        cell.setCellValue(new BigDecimal(aggDTO.getDebitAmount()).setScale(2).toString());
                    } else if (j == 5) {
                        grandAggTotAmt = new BigDecimal(aggDTO.getCreditAmount()).subtract(new BigDecimal(aggDTO.getDebitAmount()));
                        cell.setCellValue(grandAggTotAmt.setScale(2).toString());
                    }
                }
            }
            sheet = book.createSheet();

            book.write(out);
            out.flush();
            out.close();
        }

        /**
         * Method used to export inwDetailreport to Excel
         */
        public void inwDetailExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int rowCount = 0;
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;
                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);

                book.setSheetName(0,"Inward Detail Report",
                                  HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("NEFT Inward Summary Report");

                //Adding the items to a list
                ITDetailReportDTO inwDto = (ITDetailReportDTO)getNeftRepDTO();
                Set keySet = inwDto.getReceivedTransactionInfo().keySet();
                Iterator it = keySet.iterator();
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch Code :");
                cell = row.createCell((short)1);
                cell.setCellValue(getBranchIFSCCode(String.valueOf(getReportDto().getIfscId())));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Report Date :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getValueDate());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batch Time :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getBatchTime());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Report Type :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getReportType());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Inward Type :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getInwardType());
                while (it.hasNext()) {

                    List exportXLS = new ArrayList(1);
                    String key = (String) it.next();
                    List listRep = (List) inwDto.getReceivedTransactionInfo().get(key);
                    for (Iterator itr = listRep.iterator();itr.hasNext();) {
                        TransactionInfo info = (TransactionInfo)itr.next();
                        exportXLS.add(info);
                    }
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue(key);
                    if (exportXLS.size() != 0) {

                        for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                            row = sheet.createRow(rowCount);
                            rowCount += 1;
                            for (short j = 0; j < 13; j++) {

                                cell = row.createCell(j);
                                // for header
                                if (roww == 0) {
                                    if (j == 0) {
                                        cell.setCellValue("S.No");
                                    } else if (j == 1) {
                                        cell.setCellValue("Batch Time");
                                    } else if (j == 2) {
                                        cell.setCellValue("Benificiary IFSC");
                                    } else if (j == 3) {
                                        cell.setCellValue("Sender IFSC");
                                    } else if (j == 4) {
                                        cell.setCellValue("Transaction Ref.No");
                                    } else if (j == 5) {
                                        cell.setCellValue("Amount(Rs)");
                                    } else if (j == 6) {
                                        cell.setCellValue("Benificiary A/c Name");
                                    } else if (j == 7) {
                                        cell.setCellValue("Benificiary A/c Type");
                                    } else if (j == 8) {
                                        cell.setCellValue("Benificiary A/c No");
                                    } else if (j == 9) {
                                        cell.setCellValue("Sender A/c Name");
                                    } else if (j == 10) {
                                        cell.setCellValue("Sender A/c Type");
                                    } else if (j == 11) {
                                        cell.setCellValue("Sender A/c No");
                                    } else if (j == 12) {
                                        cell.setCellValue("Transaction Status");
                                    }
                                } else {

                                    if (j == 0) {
                                        cell.setCellValue(roww);

                                    } else if (j == 1) {

                                        String batch = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getBatchTime() != null) {
                                            batch = ((TransactionInfo)exportXLS.get(roww-1)).getBatchTime();
                                        }
                                        cell.setCellValue(batch);
                                    } else if (j == 2) {

                                        String beneIfsc = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccIfsc() != null) {
                                            beneIfsc = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccIfsc();
                                        }
                                        cell.setCellValue(beneIfsc);
                                    } else if (j == 3) {

                                        String sendIfsc = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccIfsc() != null) {
                                            sendIfsc = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccIfsc();
                                        }
                                        cell.setCellValue(sendIfsc);
                                    } else if (j == 4) {

                                        String transRef = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getUtrNo() != null) {
                                            transRef = ((TransactionInfo)exportXLS.get(roww-1)).getUtrNo();
                                        }
                                        cell.setCellValue(transRef);
                                    } else if (j == 5) {

//                                        double amt = 0;
//                                        amt = ((TransactionInfo)exportXLS.get(roww-1)).getAmount();
                                        BigDecimal amt = BigDecimal.ZERO;
                                        amt = ((TransactionInfo)exportXLS.get(roww-1)).getAmount();
                                        //cell.setCellValue(FormatAmount.formatINRAmount(amt));
                                        cell.setCellValue(amt.setScale(2).toString());
                                    } else if (j == 6) {

                                        String bencAcName = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getBeneficiaryInfo().getAccName() != null) {
                                            bencAcName = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccName();
                                        }
                                        cell.setCellValue(bencAcName);
                                    } else if (j == 7) {

                                        String bencAcType = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getBeneficiaryInfo().getAccType() != null) {
                                            bencAcType = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccType();
                                        }
                                        cell.setCellValue(bencAcType);
                                    } else if (j == 8) {

                                        String bencAcNo = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getBeneficiaryInfo().getAccNo() != null) {
                                            bencAcNo = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccNo();
                                        }
                                        cell.setCellValue(bencAcNo);
                                    } else if (j == 9) {

                                        String sendAcName = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getSenderInfo().getAccName() != null) {
                                            sendAcName = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccName();
                                        }
                                        cell.setCellValue(sendAcName);
                                    } else if (j == 10) {

                                        String sendAcType = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getSenderInfo().getAccType() != null) {
                                            sendAcType = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccType();
                                        }
                                        cell.setCellValue(sendAcType);
                                    } else if (j == 11) {

                                        String sendAcNo = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getSenderInfo().getAccNo() != null) {
                                            sendAcNo = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccNo();
                                        }
                                        cell.setCellValue(sendAcNo);
                                    } else if (j == 12) {

                                        String status = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getStatusShortDesc() != null) {
                                            status = ((TransactionInfo)exportXLS.get(roww-1)).getStatusShortDesc();
                                        }
                                        cell.setCellValue(status);
                                    }
                                    cell.setCellStyle(caption_style);
                                }
                            }
                        }
                    } else {
                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("No records found");
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
         * Method used to export outTxnDetailreport to Excel
         */
        public void outTxnDetailExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int rowCount = 0;
                BigDecimal totAmt = BigDecimal.ZERO;
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;
                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);

                book.setSheetName(0,"Outward Txn Detail Report",
                                  HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);
                //Adding the items to a list
                OTDetailReportDTO outDto = (OTDetailReportDTO)getNeftRepDTO();
                Set keySet = outDto.getOutwardMap().keySet();
                Iterator it = keySet.iterator();
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch Code :");
                cell = row.createCell((short)1);
                cell.setCellValue(getBranchIFSCCode(String.valueOf(getReportDto().getIfscId())));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batch Date :");
                cell = row.createCell((short)1);
                cell.setCellValue(InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batch Time :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getBatchTime());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Report Type :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getReportType());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Generated by :");
                cell = row.createCell((short)1);
                cell.setCellValue(neftRepDTO.reportRunBy);
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Outward Txn Detailed Report from " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                  InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)13);
                String dateForm = currentReportPrintTime.substring(0,11);
                String time = currentReportPrintTime.substring(11);
                cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                rowCount += 1;
                while (it.hasNext()) {

                    List exportXLS = new ArrayList(1);
                    String key = (String) it.next();
                    List listRep = (List) outDto.getOutwardMap().get(key);
                    for (Iterator itr = listRep.iterator();itr.hasNext();) {
                        TransactionInfo info = (TransactionInfo)itr.next();
                        exportXLS.add(info);
                    }
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue(key);
                    if (exportXLS.size() != 0) {

                        for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                            row = sheet.createRow(rowCount);
                            rowCount += 1;
                            for (short j = 0; j < 16; j++) {

                                cell = row.createCell(j);
                                // for header
                                if (roww == 0) {

                                    if (j == 0) {
                                        cell.setCellValue("S.No");
                                    } else if (j == 1) {
                                        cell.setCellValue("Batch Time");
                                     } else if (j == 2) {
                                        cell.setCellValue("Value Date");
                                    } else if (j == 3) {
                                        cell.setCellValue("Transaction Ref.No");
                                    } else if (j == 4) {
                                        cell.setCellValue("Amount(Rs)");
                                    } else if (j == 5) {
                                        cell.setCellValue("Sender IFSC");
                                    } else if (j == 6) {
                                        cell.setCellValue("Sender A/c Type");
                                    } else if (j == 7) {
                                        cell.setCellValue("Sender A/c No");
                                    } else if (j == 8) {
                                        cell.setCellValue("Sender A/c Name");
                                    } else if (j == 9) {
                                        cell.setCellValue("Benificiary IFSC");
                                    }  else if (j == 10) {
                                        cell.setCellValue("Benificiary A/c Type");
                                    } else if (j == 11) {
                                        cell.setCellValue("Benificiary A/c No");
                                    } else if (j == 12) {
                                        cell.setCellValue("Benificiary A/c Name");
                                    } else if (j == 13) {
                                        cell.setCellValue("Rescheduled Date");
                                    } else if (j == 14) {
                                        cell.setCellValue("Rescheduled Time");
                                    } else if (j == 15) {
                                        cell.setCellValue("Message Status");
                                    }
                                } else {

                                    if (j == 0) {
                                        cell.setCellValue(roww);

                                    } else if (j == 1) {

                                        String batch = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getBatchTime() != null) {
                                            batch = ((TransactionInfo)exportXLS.get(roww-1)).getBatchTime();
                                        }
                                        cell.setCellValue(batch);
                                    } else if (j == 2) {

                                        String valueDate = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getValueDate() != null) {
                                            Date date = (Date)((TransactionInfo)exportXLS.get(roww-1)).getValueDate();
                                            valueDate = InstaReportUtil.formatDate(date);
                                            valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, InstaReportUtil.formatDateString(valueDate));
                                        }
                                        cell.setCellValue(valueDate);
                                    }  else if (j == 3) {

                                        String transRef = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getUtrNo() != null) {
                                            transRef = ((TransactionInfo)exportXLS.get(roww-1)).getUtrNo();
                                        }
                                        cell.setCellValue(transRef);
                                    } else if (j == 4) {

                                        //                                    double amt = 0;
                                        BigDecimal amt = BigDecimal.ZERO;
                                        amt = ((TransactionInfo)exportXLS.get(roww-1)).getAmount();
                                        cell.setCellValue(FormatAmount.formatINRAmount(amt));
                                        totAmt = totAmt.add(amt);
                                    }else if (j == 5) {

                                        String sendIfsc = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccIfsc() != null) {
                                            sendIfsc = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccIfsc();
                                        }
                                        cell.setCellValue(sendIfsc);
                                    } else if (j == 6) {

                                        String sendAcType = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getSenderInfo().getAccType() != null) {
                                            sendAcType = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccType();
                                        }
                                        cell.setCellValue(sendAcType);
                                    } else if (j == 7) {

                                        String sendAcNo = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getSenderInfo().getAccNo() != null) {
                                            sendAcNo = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccNo();
                                        }
                                        cell.setCellValue(sendAcNo);
                                    } else if (j == 8) {

                                        String sendAcName = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getSenderInfo().getAccName() != null) {
                                            sendAcName = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccName();
                                        }
                                        cell.setCellValue(sendAcName);
                                    } else if (j == 9) {

                                        String beneIfsc = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccIfsc() != null) {
                                            beneIfsc = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccIfsc();
                                        }
                                        cell.setCellValue(beneIfsc);
                                    } else if (j == 10) {

                                        String bencAcType = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getBeneficiaryInfo().getAccType() != null) {
                                            bencAcType = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccType();
                                        }
                                        cell.setCellValue(bencAcType);
                                    } else if (j == 11) {

                                        String bencAcNo = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getBeneficiaryInfo().getAccNo() != null) {
                                            bencAcNo = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccNo();
                                        }
                                        cell.setCellValue(bencAcNo);
                                    } else if (j == 12) {

                                        String bencAcName = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getBeneficiaryInfo().getAccName() != null) {
                                            bencAcName = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccName();
                                        }
                                        cell.setCellValue(bencAcName);
                                    } else if (j == 13) {

                                        String reschDate = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getRescheduleDate() != null) {
                                            Date date = (Date)((TransactionInfo)exportXLS.get(roww-1)).getRescheduleDate();
                                            reschDate = InstaReportUtil.formatDate(date);
                                            reschDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, InstaReportUtil.formatDateString(reschDate));
                                        }
                                        cell.setCellValue(reschDate);
                                    } else if (j == 14) {

                                        String reschBatch = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getRescheduleBatch() != null) {
                                            reschBatch = ((TransactionInfo)exportXLS.get(roww-1)).getRescheduleBatch();
                                        }
                                        cell.setCellValue(reschBatch);
                                    }else if (j == 15) {

                                        String status = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                            getCurrentStatus() != null) {
                                            status = ((TransactionInfo)exportXLS.get(roww-1)).getCurrentStatus();
                                        }
                                        cell.setCellValue(status);
                                    }
                                    cell.setCellStyle(caption_style);
                                }
                            }
                        }
                    } else {
                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("No records found");
                    }
                }
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)3);
                cell.setCellValue("Total");
                cell = row.createCell((short)4);
                cell.setCellValue(totAmt.setScale(2).toString());
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
         * Method used to export outDetailreport to Excel
         */
        public void outDetailExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int rowCount = 0;
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;
                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);

                book.setSheetName(0,"Outward Detail Report",
                                  HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);
                //Adding the items to a list
                OTDetailReportDTO outDto = (OTDetailReportDTO)getNeftRepDTO();
                Set keySet = outDto.getOutwardMap().keySet();
                Iterator it = keySet.iterator();
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch Code :");
                cell = row.createCell((short)1);
                cell.setCellValue(getBranchIFSCCode(String.valueOf(getReportDto().getIfscId())));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Report Date :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getValueDate());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batch Time :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getBatchTime());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Report Type :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getReportType());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Generated by :");
                cell = row.createCell((short)1);
                cell.setCellValue(neftRepDTO.reportRunBy);
                while (it.hasNext()) {

                    List exportXLS = new ArrayList(1);
                    String key = (String) it.next();
                    List listRep = (List) outDto.getOutwardMap().get(key);
                    for (Iterator itr = listRep.iterator();itr.hasNext();) {
                        TransactionInfo info = (TransactionInfo)itr.next();
                        exportXLS.add(info);
                    }
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue(key);
                    if (exportXLS.size() != 0) {

                        for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                            row = sheet.createRow(rowCount);
                            rowCount += 1;
                            for (short j = 0; j < 14; j++) {

                                cell = row.createCell(j);
                                // for header
                                if (roww == 0) {

                                    if (j == 0) {
                                        cell.setCellValue("S.No");
                                    } else if (j == 1) {
                                        cell.setCellValue("Batch Time");
                                     } else if (j == 2) {
                                        cell.setCellValue("Value Date");
                                    } else if (j == 3) {
                                        cell.setCellValue("Sender IFSC");
                                    } else if (j == 4) {
                                        cell.setCellValue("Benificiary IFSC");
                                    } else if (j == 5) {
                                        cell.setCellValue("Transaction Ref.No");
                                    } else if (j == 6) {
                                        cell.setCellValue("Amount(Rs)");
                                    } else if (j == 7) {
                                        cell.setCellValue("Sender A/c Type");
                                    } else if (j == 8) {
                                        cell.setCellValue("Sender A/c No");
                                    } else if (j == 9) {
                                        cell.setCellValue("Sender A/c Name");
                                    }  else if (j == 10) {
                                        cell.setCellValue("Benificiary A/c Type");
                                    } else if (j == 11) {
                                        cell.setCellValue("Benificiary A/c No");
                                    } else if (j == 12) {
                                        cell.setCellValue("Benificiary A/c Name");
                                    }else if (j == 13) {
                                        cell.setCellValue("Transaction Status");
                                    }
                                } else {

                                    if (j == 0) {
                                        cell.setCellValue(roww);

                                    } else if (j == 1) {

                                        String batch = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getBatchTime() != null) {
                                            batch = ((TransactionInfo)exportXLS.get(roww-1)).getBatchTime();
                                        }
                                        cell.setCellValue(batch);
                                    } else if (j == 2) {

                                        String valueDate = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getValueDate() != null) {
                                            Date date = (Date)((TransactionInfo)exportXLS.get(roww-1)).getValueDate();
                                            valueDate = InstaReportUtil.formatDate(date);

                                        }
                                        cell.setCellValue(valueDate);
                                    }  else if (j == 3) {

                                        String sendIfsc = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccIfsc() != null) {
                                            sendIfsc = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccIfsc();
                                        }
                                        cell.setCellValue(sendIfsc);
                                    } else if (j == 4) {

                                        String beneIfsc = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccIfsc() != null) {
                                            beneIfsc = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccIfsc();
                                        }
                                        cell.setCellValue(beneIfsc);
                                    }else if (j == 5) {

                                        String transRef = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).getUtrNo() != null) {
                                            transRef = ((TransactionInfo)exportXLS.get(roww-1)).getUtrNo();
                                        }
                                        cell.setCellValue(transRef);
                                    } else if (j == 6) {

//                                        double amt = 0;
                                        BigDecimal amt = BigDecimal.ZERO;
                                        amt = ((TransactionInfo)exportXLS.get(roww-1)).getAmount();
                                        //cell.setCellValue(FormatAmount.formatINRAmount(amt));
                                        cell.setCellValue(amt.setScale(2).toString());
                                    } else if (j == 7) {

                                        String sendAcType = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getSenderInfo().getAccType() != null) {
                                            sendAcType = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccType();
                                        }
                                        cell.setCellValue(sendAcType);
                                    } else if (j == 8) {

                                        String sendAcNo = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getSenderInfo().getAccNo() != null) {
                                            sendAcNo = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccNo();
                                        }
                                        cell.setCellValue(sendAcNo);
                                    } else if (j == 9) {

                                        String sendAcName = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getSenderInfo().getAccName() != null) {
                                            sendAcName = ((TransactionInfo)exportXLS.get(roww-1)).getSenderInfo().getAccName();
                                        }
                                        cell.setCellValue(sendAcName);
                                    } else if (j == 10) {

                                        String bencAcType = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getBeneficiaryInfo().getAccType() != null) {
                                            bencAcType = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccType();
                                        }
                                        cell.setCellValue(bencAcType);
                                    } else if (j == 11) {

                                        String bencAcNo = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getBeneficiaryInfo().getAccNo() != null) {
                                            bencAcNo = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccNo();
                                        }
                                        cell.setCellValue(bencAcNo);
                                    } else if (j == 12) {

                                        String bencAcName = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                                getBeneficiaryInfo().getAccName() != null) {
                                            bencAcName = ((TransactionInfo)exportXLS.get(roww-1)).getBeneficiaryInfo().getAccName();
                                        }
                                        cell.setCellValue(bencAcName);
                                    } else if (j == 13) {

                                        String status = null;
                                        if(((TransactionInfo)exportXLS.get(roww-1)).
                                            getCurrentStatus() != null) {
                                            status = ((TransactionInfo)exportXLS.get(roww-1)).getCurrentStatus();
                                        }
                                        cell.setCellValue(status);
                                    }
                                    cell.setCellStyle(caption_style);
                                }
                            }
                        }
                    } else {
                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        cell = row.createCell((short)0);
                        cell.setCellValue("No records found");
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
         * Method used to export outSummaryreport to Excel
         */
        public void outSummaryExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int rowCount = 0;
                int display = 0;
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;
                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);

                book.setSheetName(0,"Outward Summary Report",
                                  HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);
                //Adding the items to a list
                OTDetailReportDTO outDto = (OTDetailReportDTO)getNeftRepDTO();
                Set keySet = outDto.getOutwardMap().keySet();

                row = sheet.createRow(rowCount);   //Have done with Heading
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("");
                cell = row.createCell((short)1);
                cell.setCellValue("NEFT Outward Summary Report");

                Iterator it = keySet.iterator();
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch Code :");
                cell = row.createCell((short)1);
                cell.setCellValue(getBranchIFSCCode(String.valueOf(getReportDto().getIfscId())));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batch Date :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getValueDate());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batch Time :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getBatchTime());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Report Type :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getReportType());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Generated By:");
                cell = row.createCell((short)1);
                cell.setCellValue(neftRepDTO.reportRunBy);

                /*row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Status:");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getStatusValue());*/

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("");
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)1);
                cell.setCellValue("Summary Of the Transactions");
                while (it.hasNext()) {

                    List exportXLS = new ArrayList(1);
                    String key = (String) it.next();
                    List listRep = (List) outDto.getOutwardMap().get(key);
                    for (Iterator itr = listRep.iterator();itr.hasNext();) {
                        SummaryInfoElement info = (SummaryInfoElement)itr.next();
                        exportXLS.add(info);
                    }
                    if (exportXLS.size() != 0) {

                        for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                            row = sheet.createRow(rowCount);
                            rowCount += 1;
                            for (short j = 0; j < 3; j++) {
                                cell = row.createCell(j);

                                // for header
                                if (roww == 0 ) {

                                    if (j == 1 && display == 0) {
                                        cell.setCellValue("No of Transaction");
                                    } else if (j == 2 && display == 0) {
                                        cell.setCellValue("Amount (Rs.)");
                                    }
                                } else {

                                    SummaryInfoElement summary = ((SummaryInfoElement)exportXLS.get(roww - 1));
                                    if (j == 0) {

                                        String heading = null;
                                        if (summary != null) {
                                            heading = summary.getHeading();
                                        }
                                        cell.setCellValue(heading);
                                    } else if (j == 1) {

                                        int count = 0;
                                        if (summary != null) {
                                            count = summary.getCount();
                                        }
                                        cell.setCellValue(String.valueOf(count));
                                    } else if (j == 2) {

//                                        double amount = 0;
                                        BigDecimal amount = BigDecimal.ZERO;
                                        if (summary != null) {
                                            amount = new BigDecimal(summary.getAmount());
                                        }
                                        //cell.setCellValue(amount.toString()); //Have done amount format.
                                        cell.setCellValue(FormatAmount.formatINRAmount(amount.toString()));
                                    }
                               }

                                cell.setCellStyle(caption_style);
                           }
                      }
                      display = 1;
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
         * Method used to export inwSummaryreport to Excel
         *
         * This method completed modified as like RTGS Br.summary report by Eswaripriyak
         */
        public void inwSummaryExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int rowCount = 0;
//                double grandInwTotTxnAmt = 0;
//                double grandOwTotTxnAmt = 0;
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
                                          "NEFT Branchwise Summary Report",
                                          HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                    caption_font = book.createFont();
                    caption_font.setFontHeightInPoints((short)10);
                    caption_font.setFontName("Verdana");
                    caption_style = book.createCellStyle();
                    caption_style.setFont(caption_font);
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("NEFT Branch wise Summary Report from " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate())+" with status " +
                                      getReportDto().getStatusValue());
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)5);
                    String dateForm = currentReportPrintTime.substring(0,11);
                    String time = currentReportPrintTime.substring(11);
                    cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                    rowCount += 1;

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

//                            double inwTotTxnAmt = 0;
//                            double owTotTxnAmt = 0;
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
                                            cell.setCellValue("Transaction Type");
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
                                            }
                                            valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
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

//                                            double inwTxnAmount = 0.00;
                                            String inwTxnAmount = "0.00";

                                            if(((ReportDTO)exportXLS.get(roww - 1)).getTranType().equalsIgnoreCase("inward")) {
//                                                if (((ReportDTO)exportXLS
//                                                .get(roww - 1)).getAmount() != 0.0) {
                                                if (new BigDecimal(((ReportDTO)exportXLS
                                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                    inwTxnAmount = ((ReportDTO)exportXLS
                                                    .get(roww - 1)).getAmt();
                                                }
                                                inwTotTxnAmt = inwTotTxnAmt.add(new BigDecimal( inwTxnAmount).setScale(2));
                                                cell.setCellValue(new BigDecimal(inwTxnAmount).setScale(2).toString());
                                            }
                                        } else if (j == 7) {

//                                            double owTxnAmount = 0.0;
                                            String owTxnAmount = "0.00";

                                            if(((ReportDTO)exportXLS.get(roww - 1)).getTranType().equalsIgnoreCase("outward")) {

//                                                if (((ReportDTO)exportXLS
//                                                .get(roww - 1)).getAmount() != 0.0) {
                                                if ( new BigDecimal(((ReportDTO)exportXLS
                                                .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                    owTxnAmount = ((ReportDTO)exportXLS
                                                    .get(roww - 1)).getAmt();
                                                }
                                                owTotTxnAmt = owTotTxnAmt.add( new BigDecimal(owTxnAmount).setScale(2));
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
                            cell.setCellValue(inwTotTxnAmt.toString());
                            cell = row.createCell((short)7);
                            cell.setCellValue(owTotTxnAmt.toString());
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
                    cell.setCellValue(grandInwTotTxnAmt.toString());
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)6);
                    cell.setCellValue("Total Outward Amount : ");
                    cell = row.createCell((short)7);
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
         * Method used to export inwSummaryreport to Excel
         */
       /* public void inwSummaryExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int rowCount = 0;
                int display = 0;
                HSSFWorkbook book = new HSSFWorkbook();
                HSSFSheet sheet = book.createSheet();
                HSSFRow row = null;
                HSSFCell cell = null;
                HSSFFont caption_font = null;
                HSSFCellStyle caption_style = null;
                caption_font = book.createFont();
                caption_font.setFontHeightInPoints((short)10);
                caption_font.setFontName("Verdana");
                caption_style = book.createCellStyle();
                caption_style.setFont(caption_font);

                book.setSheetName(0,"Inward Summary Report",
                                  HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);
                //Adding the items to a list
                ITDetailReportDTO inwDto = (ITDetailReportDTO)getNeftRepDTO();
                Set keySet = inwDto.getReceivedTransactionInfo().keySet();
                Iterator it = keySet.iterator();

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("NEFT Inward Summary Report");

                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Branch Code :");
                cell = row.createCell((short)1);
                cell.setCellValue(getBranchIFSCCode(String.valueOf(getReportDto().getIfscId())));
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Report Date :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getValueDate());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Batch Time :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getBatchTime());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Report Type :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getReportType());
                row = sheet.createRow(rowCount);
                rowCount += 1;
                cell = row.createCell((short)0);
                cell.setCellValue("Inward Type :");
                cell = row.createCell((short)1);
                cell.setCellValue(getReportDto().getInwardType());

                while (it.hasNext()) {

                    List exportXLS = new ArrayList(1);
                    String key = (String) it.next();
                    List listRep = (List) inwDto.getReceivedTransactionInfo().get(key);
                    for (Iterator itr = listRep.iterator();itr.hasNext();) {
                        SummaryInfoElement info = (SummaryInfoElement)itr.next();
                        exportXLS.add(info);
                    }
                    if (exportXLS.size() != 0) {

                        for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                            row = sheet.createRow(rowCount);
                            rowCount += 1;
                            for (short j = 0; j < 3; j++) {
                                cell = row.createCell(j);

                                // for header
                                if (roww == 0 ) {

                                    if (j == 1 && display == 0) {
                                        cell.setCellValue("Total Txns");
                                    } else if (j == 2 && display == 0) {
                                        cell.setCellValue("Total Amount(Rs.)");
                                    }
                                } else {

                                    SummaryInfoElement summary = ((SummaryInfoElement)exportXLS.get(roww - 1));
                                    if (j == 0) {

                                        String heading = null;
                                        heading = key;
                                        cell.setCellValue(heading);
                                    } else if (j == 1) {

                                        int count = 0;
                                        if (summary != null) {
                                            count = summary.getCount();
                                        }
                                        cell.setCellValue(String.valueOf(count));
                                    } else if (j == 2) {

//                                        double amount = 0;
                                        BigDecimal amount = BigDecimal.ZERO;
                                        if (summary != null) {
                                            amount = new BigDecimal(summary.getAmount());
                                        }
                                        //cell.setCellValue(amount.toString());     //Have dont amount format.
                                        cell.setCellValue(FormatAmount.formatINRAmount(amount.toString()));
                                    }
                               }
                                cell.setCellStyle(caption_style);
                           }
                      }
                      display = 1;
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
        }*/

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
         * Method to get Batchwise Aggregate report
         */
        public void generateBatchwiseAggregate(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                /*
                 * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
                 * Assumption : FromDate and ToDate fileds will have values always.
                 * It will not be null or empty and the expected format dd-mm-yyyy.
                 */
                /*String inputFromDt = getReportDto().getValueDate();
                inputFromDt = InstaReportUtil.formatDateString(inputFromDt);

                getReportDto().setValueDate(inputFromDt)*/

                setCurrentReportPrintTime(InstaReportUtil.formatDate(new Date(System.currentTimeMillis()),
                                                                    currentReportPrintTimeFormat));

                Message req = createMessage(sessionID, 202, 6, getReportDto());
                Message res = handle(sessionID, req);
                aggregateMap = (Map<String, Object>) res.info;
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the Batchwise Aggregate "
                             + " : " + e.getMessage());
                throw new ServerException(e.getMessage());
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

        public void loadInwardSpecificStatus() {

            DisplayValueReportDTO dto = new DisplayValueReportDTO();
            setStatusList(new ArrayList(0));

            dto = new DisplayValueReportDTO();
            dto.setValue("200");
            dto.setDisplayValue("Entry");
            getStatusList().add(dto);

            dto = new DisplayValueReportDTO();
            dto.setValue("300");
            dto.setDisplayValue("ForAuthorization");
            getStatusList().add(dto);

            dto = new DisplayValueReportDTO();
            dto.setValue("500");
            dto.setDisplayValue("ForReturnAuthorization");
            getStatusList().add(dto);

            dto = new DisplayValueReportDTO();
            dto.setValue("700");
            dto.setDisplayValue("Error");
            getStatusList().add(dto);

            dto = new DisplayValueReportDTO();
            dto.setValue("900");
            dto.setDisplayValue("Returned");
            getStatusList().add(dto);

            dto = new DisplayValueReportDTO();
            dto.setValue("1000");
            dto.setDisplayValue("Completed");
            getStatusList().add(dto);

            dto = new DisplayValueReportDTO();
            dto.setValue("1200");
            dto.setDisplayValue("Credited");
            getStatusList().add(dto);
        }

        /**
         * method for loading outward specific status for Outward Summary Report
         */
        public void loadOutwardSpecificStatus(){

            DisplayValueReportDTO dto = new DisplayValueReportDTO();
            setStatusList(new ArrayList(0));

            if(getReport().equals("NeftOutwardTxnDetailsReport"))//else if(getReport().equals(InwardTxnsReport)

                dto = new DisplayValueReportDTO();
                dto.setValue("2100");
                dto.setDisplayValue("ForAuthorization");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("2400");
                dto.setDisplayValue("ForTreasuryAuthorization");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("2300");
                dto.setDisplayValue("ForRelease");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("2550");
                dto.setDisplayValue("Re-Scheduled");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("2500");
                dto.setDisplayValue("ForAcknowledge");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("2600");
                dto.setDisplayValue("ForSettlement");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("2800");
                dto.setDisplayValue("Cancelled");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("2700");
                dto.setDisplayValue("Error");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("2900");
                dto.setDisplayValue("UnSuccessful");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("3000");
                dto.setDisplayValue("Settled");
                getStatusList().add(dto);

                dto = new DisplayValueReportDTO();
                dto.setValue("3200");
                dto.setDisplayValue("Credited");
                getStatusList().add(dto);
        }

        /**
         * Method to get Batchwise Aggregate report
         */
        public void generateBatchwiseReconcillation(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                /*
                 * To convert the Input Date Format from dd-mm-yyyy to dd-MMM-yyyy.
                 * Assumption : FromDate and ToDate fileds will have values always.
                 * It will not be null or empty and the expected format dd-mm-yyyy.
                 */
                /*String inputFromDt = getReportDto().getValueDate();
                inputFromDt = InstaReportUtil.formatDateString(inputFromDt);

                getReportDto().setValueDate(inputFromDt);*/

                setCurrentReportPrintTime(InstaReportUtil.formatDate(new Date(System.currentTimeMillis()),
                                                                    currentReportPrintTimeFormat));
                Object[] obj = new Object[2];
                obj[0] = getReportDto();
                obj[1] = batchTimings;
                Message req = createMessage(sessionID, 202, 8, obj);
                Message res = handle(sessionID, req);
                formatValueDate();    //Done with date format.
                reconcillationMap = (Map<String, List>) res.info;
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the Batchwise Aggregate "
                             + " : " + e.getMessage());
                throw new ServerException(e.getMessage());
            }
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

        /**
         * TODO
         */
        public void generateNEFTOwReturnedReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                //To convert the Input Date Format
                /*String inputFromDt = getReportDto().getValueDate();   //Commented by priyak for date format.
                String inputToDt = getReportDto().getToDate();
                inputFromDt = InstaReportUtil.formatDateString(inputFromDt);
                inputToDt = InstaReportUtil.formatDateString(inputToDt);
                getReportDto().setValueDate(inputFromDt);
                getReportDto().setToDate(inputToDt);*/

                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 202, 10, reportDto);
                Message res = handle(sessionID, req);

                setReportDTOs((List<ReportDTO>) res.info);

            } catch(Exception e) {

                logger.error("Exception ocurred while getting the OwReturnedReport : "
                             + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         * Method used to export the outward returned report in to Excel
         */
        public void generateNEFTOwReturnedExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                List exportXLS = new ArrayList(1);
                long sno = 0;
                int rowCount = 0;
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

                    cell.setCellValue("NEFT Outward Returned Report from " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));

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
                                        cell.setCellValue("Batch Time");
                                    } else if (j == 2) {
                                        cell.setCellValue("Value Date");
                                    } else if (j == 3) {
                                        cell.setCellValue("Sender Address");
                                    } else if (j == 4) {
                                        cell.setCellValue("Receiver Address");
                                    }  else if (j == 5) {
                                        cell.setCellValue("UTR No");
                                    } else if (j == 6) {
                                        cell.setCellValue("Original UTR No");
                                    } else if (j == 7) {
                                        cell.setCellValue("Info");
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

                                    String batchtime = null;
                                    if (((ReportDTO)exportXLS
                                    .get(roww - 1)).getBatchTime() != null) {
                                        batchtime = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getBatchTime();
                                    }

                                    cell.setCellValue(batchtime);
                                } else if (j == 2) {

                                    String valueDate = null;
                                    if (((ReportDTO)exportXLS
                                    .get(roww - 1)).getValueDate() != null) {
                                        valueDate = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getValueDate();
                                    }

                                    valueDate = InstaReportUtil.getDateInSpecificFormat(dateFormat, valueDate);
                                    cell.setCellValue(valueDate);

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

                                    String utrNo = null;

                                    if (((ReportDTO)exportXLS
                                    .get(roww - 1)).getUtrNo() != null) {
                                        utrNo = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getUtrNo();
                                    }
                                    cell.setCellValue(utrNo);
                                } else if (j == 6) {

                                    String orgUtrNo = null;

                                    if (((ReportDTO)exportXLS
                                    .get(roww - 1)).getOutUTRNo() != null) {
                                        orgUtrNo = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getOutUTRNo();
                                    }
                                    cell.setCellValue(orgUtrNo);
                                } else if (j == 7) {


                                    String a7495 = null;

                                    if (((ReportDTO)exportXLS.get(roww - 1))
                                    .getFieldA7495() != null) {
                                        a7495 = ((ReportDTO)exportXLS.get(roww - 1))
                                        .getFieldA7495();
                                    }
                                    cell.setCellValue(a7495);
                                } else if (j == 8) {

                                    String txnAmount = "0.00";

                                    if (((ReportDTO)exportXLS
                                    .get(roww - 1)).getAmt() != null) {
                                        txnAmount = ((ReportDTO)exportXLS
                                        .get(roww - 1)).getAmt();
                                    }

                                    BigDecimal dec = new BigDecimal(txnAmount);
                                    dec.setScale(2);
                                    totTxnAmt = totTxnAmt.add(dec);
                                    cell.setCellValue(txnAmount);

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
                logger.error("Exception while creating Excel sheet file"+e.getMessage());
                throw new Exception("Exception while creating Excel sheet file"+e);
            }
        }

        public void inwTxnsDetailExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                List exportXLS = new ArrayList(1);
                long sno = 0;
                int rowCount = 0;
//                double totAmt = 0;
                BigDecimal totAmt = BigDecimal.ZERO;
                //Adding the items to a list
                for (Iterator i = getInwardTxns().iterator(); i.hasNext();) {
                    TransactionInfo info = (TransactionInfo)i.next();
                    exportXLS.add(info);
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
                    //added newly on 29-Jan-2010 for printing whole in one page
//                    HSSFPrintSetup ps = sheet.getPrintSetup();
//                    sheet.setAutobreaks(true);
//                    ps.setFitHeight((short)1);
//                    ps.setFitWidth((short)1);
                    //Ends here
                    book.setSheetName(0,
                                          "Inward Txns -Detailed",
                                          HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                    caption_font = book.createFont();
                    caption_font.setFontHeightInPoints((short)10);
                    caption_font.setFontName("Verdana");
                    caption_style = book.createCellStyle();
                    caption_style.setFont(caption_font);

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("NEFT Inward Txns Report");

                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("NEFT Inward Transactions - Detailed - from "+
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                      InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("Status: "+reportDto.getStatusValue()+" Batch Time: "+reportDto.getBatchTime());
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("Branch: "+reportDto.getBranchCode());
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)10);
                    String dateForm = currentReportPrintTime.substring(0,11);
                    String time = currentReportPrintTime.substring(11);
                    cell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm)+time);
                    rowCount += 1;

                    for (int i = exportXLS.size(), roww = 0; roww <= i; roww++) {

                        row = sheet.createRow(rowCount);
                        rowCount += 1;
                        for (short j = 0; j < 13; j++) {
                            cell = row.createCell(j);

                            // for header
                            if (roww == 0) {

                                if (j == 0) {
                                    cell.setCellValue("S.No");
                                } else if (j == 1) {
                                    cell.setCellValue("Batch Time");
                                } else if (j == 2) {
                                    cell.setCellValue("Transaction Ref.No");
                                } else if (j == 3) {
                                    cell.setCellValue("Amount(Rs) ");
                                }  else if (j == 4) {
                                    cell.setCellValue("Benificiary IFSC");
                                } else if (j == 5) {
                                    cell.setCellValue("Benificiary A/c Name");
                                } else if (j == 6) {
                                    cell.setCellValue("Benificiary A/c Type");
                                } else if (j == 7) {
                                    cell.setCellValue("Benificiary A/c No");
                                } else if (j == 8) {
                                    cell.setCellValue("Sender IFSC");
                                } else if (j == 9) {
                                    cell.setCellValue("Sender A/c Name");
                                } else if (j == 10) {
                                    cell.setCellValue("Sender A/c Type");
                                } else if (j == 11) {
                                    cell.setCellValue("Sender A/c No");
                                } else if (j == 12) {
                                    cell.setCellValue("Transaction Status");
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
                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getBatchTime() != null) {
                                        batchTime = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getBatchTime();
                                    }
                                    cell.setCellValue(batchTime);
                                } else if (j == 2) {

                                    String refNo = null;

                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getUtrNo() != null) {
                                        refNo = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getUtrNo();
                                    }
                                    cell.setCellValue(refNo);

                                } else if (j == 3) {

                                    BigDecimal amt = BigDecimal.ZERO;
                                    amt =  ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getAmount();
                                    cell.setCellValue(amt.setScale(2).toString());
                                    totAmt = totAmt.add(amt);
                                } else if (j == 4) {

                                    String benIfsc = null;
                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getBeneficiaryInfo().getAccIfsc() != null) {

                                        benIfsc = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getBeneficiaryInfo().getAccIfsc();
                                    }
                                    cell.setCellValue(benIfsc);
                                } else if (j == 5) {

                                    String benAccName = null;
                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getBeneficiaryInfo().getAccName() != null) {

                                        benAccName = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getBeneficiaryInfo().getAccName();
                                    }
                                    cell.setCellValue(benAccName);
                                } else if (j == 6) {

                                    String benAccType = null;
                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getBeneficiaryInfo().getAccType() != null) {

                                        benAccType = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getBeneficiaryInfo().getAccType();
                                    }
                                    cell.setCellValue(benAccType);
                                } else if (j == 7) {

                                    String benAccNo = null;
                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getBeneficiaryInfo().getAccNo() != null) {

                                        benAccNo = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getBeneficiaryInfo().getAccNo();
                                    }
                                    cell.setCellValue(benAccNo);
                                } else if (j == 8) {

                                    String senderIfsc = null;

                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getSenderInfo().getAccIfsc() != null) {
                                        senderIfsc = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getSenderInfo().getAccIfsc();
                                    }
                                    cell.setCellValue(senderIfsc);

                                } else if (j == 9) {

                                    String accName = null;

                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getSenderInfo().getAccName() != null) {
                                        accName = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getSenderInfo().getAccName();
                                    }
                                   cell.setCellValue(accName);
                                } else if (j == 10) {

                                    String accType = null;

                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getSenderInfo().getAccType() != null) {

                                        accType = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getSenderInfo().getAccType();
                                    }
                                    cell.setCellValue(accType);

                                } else if (j == 11) {

                                    String accNo = null;

                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getSenderInfo().getAccNo() != null) {

                                        accNo = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getSenderInfo().getAccNo();
                                    }
                                    cell.setCellValue(accNo);
                                } else if (j == 12) {

                                    String status = null;
                                    if (((TransactionInfo)exportXLS
                                    .get(roww - 1)).getStatusShortDesc() != null) {

                                        status = ((TransactionInfo)exportXLS
                                        .get(roww - 1)).getStatusShortDesc();
                                    }
                                    cell.setCellValue(status);
                                }
                            cell.setCellStyle(caption_style);
                        }
                    }
                }
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)2);
                    cell.setCellValue("Total");
                    cell = row.createCell((short)3);
                    cell.setCellValue(totAmt.toString());

                    sheet = book.createSheet();
                    book.write(out);
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                logger.error("Exception while creating Excel sheet file for Inward txns"+e.getMessage());
                throw new Exception("Exception while creating Excel sheet file for Inward txns"+e);
            }
        }

        public void generateNeftCPwiseReconcilliation(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 202, 13, reportDto);
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
         * Method to generated Inward Possible Return report
         * @param HttpServletRequest
         * @author Eswaripriyak
         */
        public void generateNEFTPossibleReturnReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                //getReportDto().setValueDate(inputDt);
                //getReportDto().setValueDate(InstaReportUtil.reportDisplayDateFormat(inputDt)); //For date formate
                String inputDt = getReportDto().getValueDate();

                if(inputDt == null || inputDt.trim().length() == 0) {

                    throw new CrudException("Please input Value Date and then click Submit.");
                }

                if(inputDt != null && inputDt.length() == 10) {

                    inputDt = InstaReportUtil.formatDateString(inputDt);
                }

                getReportDto().setValueDate(inputDt);
                currentReportPrintTime = InstaReportUtil.formatDate
                                                (new Date(System.currentTimeMillis())
                                                , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 202, 14, new Object[] {reportDto});
                Message res = handle(sessionID, req);
                formatValueDate();  //Have done With date format dd-MMM-yyyy.
                returnedList =  (List<ReportDTO>)res.info;
                //updateDateForamt(); //After BO call
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the RTGS Inward Return Report report : " + e.getMessage());
                throw new CrudException(e.getMessage());
            }
        }

        //Method added on 22-Sep-2009 by Mohana
        /**
         * Method used to get Future dated txns report
         */
        public void generateNEFTFutureDatedTxnsReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                (new Date(System.currentTimeMillis())
                                                , currentReportPrintTimeFormat);


                long ifscId = ((Long)getRequest().getSession().getAttribute(InstaClientULC.IFSCID)).longValue();
                String ifscCode = (String) getRequest().getSession().getAttribute(InstaClientULC.IFSCCODE);
                reportDto.setIfscCode(ifscCode);
                reportDto.setIfscId(ifscId);

                Message req = createMessage(sessionID, 202, 15, reportDto);
                Message res = handle(sessionID, req);
                reportMap = (Map<String, List<ReportDTO>>) res.info;
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the report : " + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        //Method added on 23-Sep-2009 by Mohana
        /**
         * Method used to get Exceptions report
         */
        public void generateNEFTExceptionsReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                (new Date(System.currentTimeMillis())
                                                , currentReportPrintTimeFormat);
                Message req = createMessage(sessionID, 205, 12, reportDto);
                Message res = handle(sessionID, req);
                reportMap = (Map<String, List<ReportDTO>>) res.info;
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the report : " + e.getMessage());
                throw new ServerException(e.getMessage());
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
                                          "NEFT Possible Return Report",
                                          HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                    caption_font = book.createFont();
                    caption_font.setFontHeightInPoints((short)10);
                    caption_font.setFontName("Verdana");
                    caption_style = book.createCellStyle();
                    caption_style.setFont(caption_font);
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("NEFT Inward possible Return Report on "); //Heading modified
                    cell = row.createCell((short)1);
                    cell.setCellValue(InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getValueDate()));
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
                                        date = InstaReportUtil.getDateInSpecificFormat(dateFormat, date);
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
                logger.error("Exception while creating Excel sheet file for NEFT inward Possible Return Report"+e.getMessage());
                throw new Exception("Exception while creating Excel sheet file"+e);
            }
        }

        /**
         * Method to used to export the Neft Exception Report
         */
        public void neftExceptionReportExportToExcel(ServletOutputStream out)
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
                                        "NEFT Exceptions Report",
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

                  cell.setCellValue("NEFT Exceptions Report from " +
                                    InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                    InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
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
                logger.error("Exception while exporting NEFT Exception Report into Excel"+e.getMessage());
                throw new Exception("Exception while exporting NEFT Exception Report into Excel"+e);
            }
        }

        /**
         * Method used to export the Future Dated Txns report in to Excel
         */
        public void neftFutureDatedTxnExportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                int rowCount = 0;
//              double grandInwTotTxnAmt = 0;
//              double grandOwTotTxnAmt = 0;
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
                                        "NEFT Future Dated Txns Report",
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
                  cell.setCellValue("NEFT  Date wise Future Dated Txns Report from " +
                                    InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate()) + " to " +
                                    InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()) + "with status " +
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
                                          cell.setCellValue("Msg Type");
                                      } else if (j == 3) {
                                          cell.setCellValue("UTR Number");
                                      }  else if (j == 4) {
                                          cell.setCellValue("Sender Address");
                                      } else if (j == 5) {
                                          cell.setCellValue("Receiver Address");
                                      } else if (j == 6) {
                                          cell.setCellValue("Account Number");
                                      } else if (j == 7) {
                                          cell.setCellValue("Beneficiary Details");
                                      }  else if (j == 8) {
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

                                          String accNo = null;

                                          if (((ReportDTO)exportXLS
                                          .get(roww - 1)).getAccNo() != null) {
                                              accNo = ((ReportDTO)exportXLS
                                              .get(roww - 1)).getAccNo();
                                          }
                                          cell.setCellValue(accNo);
                                      } else if (j == 7) {

                                          String beneName = null;

                                          if (((ReportDTO)exportXLS
                                          .get(roww - 1)).getBeneficiaryName() != null) {
                                              beneName = ((ReportDTO)exportXLS
                                              .get(roww - 1)).getBeneficiaryName();
                                          }
                                          cell.setCellValue(beneName);
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

                                          if(((ReportDTO)exportXLS.get(roww - 1)).getTranType().equalsIgnoreCase("inward")) {
                                              if ( new BigDecimal(((ReportDTO)exportXLS
                                              .get(roww - 1)).getAmt()).compareTo(BigDecimal.ZERO) != 0.0) {
                                                  inwTxnAmount = ((ReportDTO)exportXLS
                                                  .get(roww - 1)).getAmt();
                                              }
                                              inwTotTxnAmt = inwTotTxnAmt.add(new BigDecimal(inwTxnAmount).setScale(2));
                                              cell.setCellValue(new BigDecimal(inwTxnAmount).setScale(2).toString());
                                          }
                                      }
                                  }
                               cell.setCellStyle(caption_style);
                              }
                          }

                          row = sheet.createRow(rowCount);
                          rowCount += 1;
                          cell = row.createCell((short)8);
                          cell.setCellValue("Total ( Date : " + date + " ) ");
                          cell = row.createCell((short)9);
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
                logger.error("Exception while exporting NEFT Future dated txns Report into Excel"+e.getMessage());
                throw new Exception("Exception while exporting NEFT Future dated txns Report into Excel"+e);
            }
        }

        /**
         * Method used to export NEFT Inward possible Return Payment Rejected by user Report
         * @author MohanaDevis
         */
        public void neftReturnPaymentRejectedReportExportToExcel(ServletOutputStream out)
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
                                          "NEFT Return Payment Rejected",
                                          HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);

                    caption_font = book.createFont();
                    caption_font.setFontHeightInPoints((short)10);
                    caption_font.setFontName("Verdana");
                    caption_style = book.createCellStyle();
                    caption_style.setFont(caption_font);
                    row = sheet.createRow(rowCount);
                    rowCount += 1;
                    cell = row.createCell((short)0);
                    cell.setCellValue("NEFT Inward Possible Return Payment Rejected By User Report between "
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
                logger.error("Exception while creating Excel sheet file for NEFT inward Possible Return Payment rejected Report"+e.getMessage());
                throw new Exception("Exception while creating Excel sheet file"+e);
            }
        }

        //Method added by mohana on 16-Sep-2009 for Utr numberwise report
        /**
         * Method to get NEFT UTRNumberwise Report
         */
        public void generateNEFTUTRNowiseReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                //If the login user is CO User, then set the ifsc id as 0.
                if(isCOUser==1) {

                    reportDto.setIfscId(0);
                }
                Message req = createMessage(sessionID, 205, 11, reportDto);
                Message res = handle(sessionID, req);
                setMessageDTO((CMsgDTO) res.info);
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the NEFT UTR No wise "
                             + " Report : " + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         * Method to generate neft inward possible return payment rejected by user
         */
        public void generateNEFTReturnPaymentRejectedReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                (new Date(System.currentTimeMillis())
                                                , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 205, 13, reportDto);
                Message res = handle(sessionID, req);
                setReportDTOs((List) res.info);
            } catch(Exception e) {

                logger.error("Exception ocurred while getting the System Level Events Report : "+e.getMessage());
                throw new ServerException(e.getMessage());
            }
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
         * To generate NEFT Inward Bank Summary reports
         *
         * @param request HttpServletRequest
         *
         */
        public void generateNEFTInwBankSummaryReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 202, 16, getReportDto());
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
         *  To generate NEFT Inward Detailed reports - Bank wise
         *
         *  @param request HttpServletRequest
         *
         */
        public void generateNEFTInwBankDetailedReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 202, 17, getReportDto());
                Message res = handle(sessionID, req);
                formatValueDate();

                reportMap = (Map<String, List<ReportDTO>>) res.info;
                int reportMapValue = reportMap.size();
                //reportMapValue

            } catch(Exception e) {

                logger.error("Exception ocurred while getting the NEFT Bank wise Summary Report : "
                             + e.getMessage());
                throw new ServerException(e.getMessage());
            }
        }

        /**
         *  To generate NEFT Outward detailed reports - Bank wise
         *
         *  @param request HttpServletRequest
         *
         */
        public void generateNEFTOutwardBankDetailedReport(HttpServletRequest request) {

            try {

                String sessionID = request.getSession().getId();

                currentReportPrintTime = InstaReportUtil.formatDate
                                                        (new Date(System.currentTimeMillis())
                                                         , currentReportPrintTimeFormat);

                Message req = createMessage(sessionID, 202, 18, getReportDto());
                Message res = handle(sessionID, req);
                formatValueDate();

                reportMap = (Map<String, List<ReportDTO>>) res.info;

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

        /**
         * Method used to export the bank wise summary report in to Excel for both Inward and Outward.
         *
         * @param out ServletOutputStream
         *
         */
        public void generateNEFTInwBankSummaryReportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                if(returnedList.contains(" ")) {
                    throw new Exception("No Data Found! Cannot Export as Excel Sheet!!");
                }

                HSSFWorkbook wb = new HSSFWorkbook();

                HSSFSheet sheet = wb.createSheet(reportTitle);

                HSSFCellStyle titleStyle = wb.createCellStyle();
                titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle fontStyle = wb.createCellStyle();
                HSSFFont fontSize = wb.createFont();
                fontSize.setFontHeightInPoints((short) 10);
                fontSize.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                fontStyle.setFont(fontSize);
                fontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                sheet.setColumnWidth((short)0,(short)2000);
                sheet.setColumnWidth((short)1,(short)4000);
                sheet.setColumnWidth((short)2,(short)6000);
                sheet.setColumnWidth((short)3,(short)5000);
                sheet.setColumnWidth((short)4,(short)7000);
                sheet.setColumnWidth((short)5,(short)7000);
                sheet.setColumnWidth((short)6,(short)7000);
                sheet.setColumnWidth((short)7,(short)7000);

                HSSFCellStyle contentStyle = wb.createCellStyle();
                contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                HSSFCellStyle stringStyle = wb.createCellStyle();
                stringStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                stringStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                stringStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                stringStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                stringStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                HSSFCellStyle stringRightStyle = wb.createCellStyle();
                stringRightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                stringRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                stringRightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                stringRightStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                stringRightStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                HSSFFont headingFont = wb.createFont();
                headingFont.setFontHeightInPoints((short) 9);
                headingFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFCellStyle headingStyle = wb.createCellStyle();

                headingStyle.setFont(headingFont);
                headingStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                headingStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
                headingStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);
                headingStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                headingStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                headingStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                headingStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                headingStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                headingStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                HSSFCellStyle titleRightStyle = wb.createCellStyle();
                titleRightStyle.setFont(headingFont);
                titleRightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                titleRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                titleRightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                titleRightStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                titleRightStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                HSSFCellStyle titleLeftStyle = wb.createCellStyle();
                titleLeftStyle.setFont(headingFont);
                titleLeftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                titleLeftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                titleLeftStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                titleLeftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                titleLeftStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                int rowCount = 2;

                String dateForm = currentReportPrintTime.substring(0,11);
                String time              = currentReportPrintTime.substring(11);
                HSSFRow reportTimeRow    = sheet.createRow((short) rowCount);
                HSSFCell reportTimeRowCell   = reportTimeRow.createCell((short) 0);
                reportTimeRowCell.setCellStyle(titleRightStyle);
                reportTimeRowCell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm) + time);
                sheet.addMergedRegion(new Region(rowCount, (short) 0, rowCount, (short) 7));

                rowCount++;
                HSSFRow stTitle = sheet.createRow((short)rowCount);
                HSSFCell title  = stTitle.createCell((short) 0);
                title.setCellStyle(fontStyle);
                title.setCellValue(reportTitle);
                sheet.addMergedRegion(new Region(rowCount, (short) 0, rowCount, (short) 7));

                rowCount++;
                HSSFRow stTitleTwo = sheet.createRow((short)rowCount);
                HSSFCell stTitleCellOne  = stTitleTwo.createCell((short) 0);
                stTitleCellOne.setCellStyle(fontStyle);
                stTitleCellOne.setCellValue("from "
                    + InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate())
                    + " to " + InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                sheet.addMergedRegion(new Region(rowCount, (short) 0, rowCount, (short) 7));

                rowCount = rowCount + 2;

                HSSFRow rowhead      = sheet.createRow((short) rowCount);

                HSSFCell snoHead     = rowhead.createCell((short) 0);
                snoHead.setCellStyle(headingStyle);
                snoHead.setCellValue("S. NO");

                HSSFCell ifscHead     = rowhead.createCell((short) 1);
                ifscHead.setCellStyle(headingStyle);
                ifscHead.setCellValue("Bank IFSC");

                HSSFCell txnCountHead     = rowhead.createCell((short) 2);
                txnCountHead.setCellStyle(headingStyle);
                txnCountHead.setCellValue("Txn Count");

                HSSFCell amountHead     = rowhead.createCell((short) 3);
                amountHead.setCellStyle(headingStyle);
                amountHead.setCellValue("Sum of Txn AMT (Rs)");

                if (reportTitle.equals(inwSummaryReport)) {

                    HSSFCell txnCompletedCountHead     = rowhead.createCell((short) 4);
                    txnCompletedCountHead.setCellStyle(headingStyle);
                    txnCompletedCountHead.setCellValue("TXN count Completed/Credited ");

                    HSSFCell txnCompletedCountAmtHead     = rowhead.createCell((short) 5);
                    txnCompletedCountAmtHead.setCellStyle(headingStyle);
                    txnCompletedCountAmtHead.setCellValue("Sum of Completed/Credited AMT");

                    HSSFCell txnRtnCountHead     = rowhead.createCell((short) 6);
                    txnRtnCountHead.setCellStyle(headingStyle);
                    txnRtnCountHead.setCellValue("TXN count Inward Returned");

                    HSSFCell txnRtnCountAmtHead     = rowhead.createCell((short) 7);
                    txnRtnCountAmtHead.setCellStyle(headingStyle);
                    txnRtnCountAmtHead.setCellValue("Sum of Inward Returned AMT");
                } else {

                    HSSFCell txnCompletedCountHead     = rowhead.createCell((short) 4);
                    txnCompletedCountHead.setCellStyle(headingStyle);
                    txnCompletedCountHead.setCellValue("TXN count Settled/Credited ");

                    HSSFCell txnCompletedCountAmtHead     = rowhead.createCell((short) 5);
                    txnCompletedCountAmtHead.setCellStyle(headingStyle);
                    txnCompletedCountAmtHead.setCellValue("Sum of Settled/Credited AMT");

                    HSSFCell txnRtnCountHead     = rowhead.createCell((short) 6);
                    txnRtnCountHead.setCellStyle(headingStyle);
                    txnRtnCountHead.setCellValue("TXN count Outward Returned");

                    HSSFCell txnRtnCountAmtHead     = rowhead.createCell((short) 7);
                    txnRtnCountAmtHead.setCellStyle(headingStyle);
                    txnRtnCountAmtHead.setCellValue("Sum of Outward Returned AMT");
                }
                int rowIndex = 1;
                for (Iterator itr = returnedList.iterator(); itr.hasNext();) {

                     ReportDTO reportDTO = (ReportDTO) itr.next();
                     HSSFRow row = null;

                         rowCount++;

                         row = sheet.createRow(rowCount);

                         HSSFCell snoCell    = row.createCell((short) 0);
                         snoCell.setCellStyle(contentStyle);
                         snoCell.setCellValue(rowIndex);
                         rowIndex++;

                         HSSFCell ifscCell    = row.createCell((short) 1);
                         ifscCell.setCellStyle(stringStyle);
                         if (reportDTO.getSenderAddress() != null) {
                             ifscCell.setCellValue(reportDTO.getSenderAddress());
                         } else {
                             ifscCell.setCellValue("");
                         }

                         HSSFCell txnCountCell    = row.createCell((short) 2);
                         txnCountCell.setCellStyle(stringRightStyle);
                         if (reportDTO.getCount() != 0) {
                             txnCountCell.setCellValue(reportDTO.getCount());
                         } else {
                             txnCountCell.setCellValue("");
                         }

                         HSSFCell txnAmountCell    = row.createCell((short) 3);
                         txnAmountCell.setCellStyle(stringRightStyle);
                         if (reportDTO.getAmt() != null && !(reportDTO.getAmt().equals("0"))) {
                             txnAmountCell.setCellValue(reportDTO.getAmt());
                         } else {
                             txnAmountCell.setCellValue("");
                         }

                         HSSFCell cmpTxnCountCell    = row.createCell((short) 4);
                         cmpTxnCountCell.setCellStyle(stringRightStyle);
                         if (reportDTO.getCompletedTxnCount() != 0) {
                             cmpTxnCountCell.setCellValue(reportDTO.getCompletedTxnCount());
                         } else {
                             cmpTxnCountCell.setCellValue("");
                         }

                         HSSFCell cmpTxnAmtCell    = row.createCell((short) 5);
                         cmpTxnAmtCell.setCellStyle(stringRightStyle);
                         if (reportDTO.getCompletedTxnAmount() != null && !(reportDTO.getCompletedTxnAmount().equals("0"))) {
                             cmpTxnAmtCell.setCellValue(reportDTO.getCompletedTxnAmount());
                         } else {
                             cmpTxnAmtCell.setCellValue("");
                         }

                         HSSFCell rtnTxnCountCell    = row.createCell((short) 6);
                         rtnTxnCountCell.setCellStyle(stringRightStyle);
                         if (reportDTO.getRtnTxnCount() != 0) {
                             rtnTxnCountCell.setCellValue(reportDTO.getRtnTxnCount());
                         } else {
                             rtnTxnCountCell.setCellValue("");
                         }

                         HSSFCell rtnTxnAmtCell    = row.createCell((short) 7);
                         rtnTxnAmtCell.setCellStyle(stringRightStyle);
                         if (reportDTO.getRtnTxnAmount() != null && !(reportDTO.getRtnTxnAmount().equals("0"))) {
                             rtnTxnAmtCell.setCellValue(reportDTO.getRtnTxnAmount());
                         } else {
                             rtnTxnAmtCell.setCellValue("");
                         }
                     }

                wb.write(out);
                out.flush();
                out.close();


            } catch (Exception e) {
                logger.error("Exception while creating Excel sheet file"+e.getMessage());
                throw new Exception("Exception while creating Excel sheet file"+e);
            }
        }


        /**
         * Method used to export the bank wise detailed report in to Excel for both Inward and Outward.
         *
         * @param out ServletOutputStream
         *
         */
        public void generateNEFTInwBankDetailedReportToExcel(ServletOutputStream out)
        throws Exception {

            try {

                HSSFWorkbook wb = new HSSFWorkbook();

                HSSFSheet sheet = wb.createSheet(reportTitle);

                HSSFCellStyle titleStyle = wb.createCellStyle();
                titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle fontStyle = wb.createCellStyle();
                HSSFFont fontSize = wb.createFont();
                fontSize.setFontHeightInPoints((short) 10);
                fontSize.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                fontStyle.setFont(fontSize);
                fontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                sheet.setColumnWidth((short)0,(short)2000);
                sheet.setColumnWidth((short)1,(short)4000);
                sheet.setColumnWidth((short)2,(short)6000);
                sheet.setColumnWidth((short)3,(short)5000);
                sheet.setColumnWidth((short)4,(short)3000);
                sheet.setColumnWidth((short)5,(short)6000);
                sheet.setColumnWidth((short)6,(short)3000);
                sheet.setColumnWidth((short)7,(short)6000);
                sheet.setColumnWidth((short)8,(short)2500);
                sheet.setColumnWidth((short)9,(short)6000);
                sheet.setColumnWidth((short)10,(short)3000);
                sheet.setColumnWidth((short)11,(short)6000);
                sheet.setColumnWidth((short)12,(short)4500);

                HSSFCellStyle contentStyle = wb.createCellStyle();
                contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                HSSFCellStyle stringStyle = wb.createCellStyle();
                stringStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                stringStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                stringStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                stringStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                stringStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                HSSFFont headingFont = wb.createFont();
                headingFont.setFontHeightInPoints((short) 9);
                headingFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFCellStyle headingStyle = wb.createCellStyle();

                headingStyle.setFont(headingFont);
                headingStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                headingStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
                headingStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);
                headingStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                headingStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                headingStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                headingStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                headingStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                headingStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                HSSFCellStyle titleRightStyle = wb.createCellStyle();
                titleRightStyle.setFont(headingFont);
                titleRightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                titleRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                titleRightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                titleRightStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                titleRightStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                HSSFCellStyle titleLeftStyle = wb.createCellStyle();
                titleLeftStyle.setFont(headingFont);
                titleLeftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                titleLeftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                titleLeftStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                titleLeftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                titleLeftStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                int rowCount = 2;

                String dateForm = currentReportPrintTime.substring(0,11);
                String time              = currentReportPrintTime.substring(11);
                HSSFRow reportTimeRow    = sheet.createRow((short) rowCount);
                HSSFCell reportTimeRowCell   = reportTimeRow.createCell((short) 0);
                reportTimeRowCell.setCellStyle(titleRightStyle);
                reportTimeRowCell.setCellValue("Report Printed on "+InstaReportUtil.getDateInSpecificFormat(dateFormat,dateForm) + time);
                sheet.addMergedRegion(new Region(rowCount, (short) 0, rowCount, (short) 12));

                rowCount++;
                HSSFRow stTitle = sheet.createRow((short)rowCount);
                HSSFCell title  = stTitle.createCell((short) 0);
                title.setCellStyle(fontStyle);
                title.setCellValue(reportTitle);
                sheet.addMergedRegion(new Region(rowCount, (short) 0, rowCount, (short) 12));

                rowCount++;
                HSSFRow stTitleTwo = sheet.createRow((short)rowCount);
                HSSFCell stTitleCellOne  = stTitleTwo.createCell((short) 0);
                stTitleCellOne.setCellStyle(fontStyle);
                stTitleCellOne.setCellValue("from "
                    + InstaReportUtil.getDateInSpecificFormat(dateFormat, getReportDto().getValueDate())
                    + " to " + InstaReportUtil.getDateInSpecificFormat(dateFormat,getReportDto().getToDate()));
                sheet.addMergedRegion(new Region(rowCount, (short) 0, rowCount, (short) 12));

                rowCount = rowCount + 2;
                if (getReportMap().size() != 0) {

                    Set set = getReportMap().entrySet();
                     for (Iterator z = set.iterator(); z.hasNext();) {

                         Map.Entry<String, List<TransactionInfo>> entry = (Map.Entry<String, List<TransactionInfo>>)z.next();
                         List exportXLS = new ArrayList(1);
                         exportXLS.addAll(entry.getValue());

                         String bankName = entry.getKey().substring(0,4);
                         String sumTxnAmount = entry.getKey().substring(5);

                         HSSFRow titleRowOne     = sheet.createRow((short) rowCount);

                         HSSFCell bankNameCell  = titleRowOne.createCell((short) 0);
                         bankNameCell.setCellStyle(titleRightStyle);
                         if(getTranType().equals("inward")) {
                             bankNameCell.setCellValue("Sender Bank : ");
                         } else {
                             bankNameCell.setCellValue(" Receiver Bank : ");
                         }
                         sheet.addMergedRegion(new Region(rowCount, (short) 0, rowCount,
                                 (short) 6));

                         HSSFCell bankNameValCell  = titleRowOne.createCell((short) 7);
                         bankNameValCell.setCellStyle(titleLeftStyle);
                         bankNameValCell.setCellValue(bankName);
                         sheet.addMergedRegion(new Region(rowCount, (short) 7, rowCount,
                                 (short) 12));

                         rowCount++;
                         HSSFRow titleRowTwo     = sheet.createRow((short) rowCount);
                         HSSFCell txnCountCell  = titleRowTwo.createCell((short) 0);
                         txnCountCell.setCellStyle(titleRightStyle);
                         txnCountCell.setCellValue("Txn Count : ");
                         sheet.addMergedRegion(new Region(rowCount, (short) 0, rowCount,
                                 (short) 6));

                         HSSFCell txnCountValCell  = titleRowTwo.createCell((short) 7);
                         txnCountValCell.setCellStyle(titleLeftStyle);
                         txnCountValCell.setCellValue(exportXLS.size());
                         sheet.addMergedRegion(new Region(rowCount, (short) 7, rowCount,
                                 (short) 12));

                         rowCount++;
                         HSSFRow titleRowThree     = sheet.createRow((short) rowCount);
                         HSSFCell sumTxnAmountCell  = titleRowThree.createCell((short) 0);
                         sumTxnAmountCell.setCellStyle(titleRightStyle);
                         sumTxnAmountCell.setCellValue("Sum of Txn Amount : ");
                         sheet.addMergedRegion(new Region(rowCount, (short) 0, rowCount,
                                 (short) 6));

                         HSSFCell sumTxnAmountValCell  = titleRowThree.createCell((short) 7);
                         sumTxnAmountValCell.setCellStyle(titleLeftStyle);
                         sumTxnAmountValCell.setCellValue(sumTxnAmount);
                         sheet.addMergedRegion(new Region(rowCount, (short) 7, rowCount,
                                 (short) 12));

                         rowCount++;

                         HSSFRow titleRow     = sheet.createRow((short) rowCount);
                         HSSFCell beneficiaryTitle  = titleRow.createCell((short) 4);
                         beneficiaryTitle.setCellStyle(headingStyle);
                         beneficiaryTitle.setCellValue("Beneficiary Details");

                         sheet.addMergedRegion(new Region(rowCount, (short) 4, rowCount,
                                 (short) 7));

                         HSSFCell senderTitle  = titleRow.createCell((short) 8);
                         senderTitle.setCellStyle(headingStyle);
                         senderTitle.setCellValue("Sender's Details");

                         sheet.addMergedRegion(new Region(rowCount, (short) 8, rowCount,
                                                          (short) 11));

                         rowCount++;
                         HSSFRow rowhead      = sheet.createRow((short) rowCount);

                         HSSFCell snoHead     = rowhead.createCell((short) 0);
                         snoHead.setCellStyle(headingStyle);
                         snoHead.setCellValue("S .NO");

                         HSSFCell valueDateHead     = rowhead.createCell((short) 1);
                         valueDateHead.setCellStyle(headingStyle);
                         valueDateHead.setCellValue("Value Date");

                         HSSFCell tranRefHead     = rowhead.createCell((short) 2);
                         tranRefHead.setCellStyle(headingStyle);
                         tranRefHead.setCellValue("Transaction Ref. No");

                         HSSFCell amountHead     = rowhead.createCell((short) 3);
                         amountHead.setCellStyle(headingStyle);
                         amountHead.setCellValue("Amount(Rs)");

                         HSSFCell benIfscHead     = rowhead.createCell((short) 4);
                         benIfscHead.setCellStyle(headingStyle);
                         benIfscHead.setCellValue("IFSC");

                         HSSFCell benACNameHead     = rowhead.createCell((short) 5);
                         benACNameHead.setCellStyle(headingStyle);
                         benACNameHead.setCellValue("A/c Name");

                         HSSFCell benACTypeHead     = rowhead.createCell((short) 6);
                         benACTypeHead.setCellStyle(headingStyle);
                         benACTypeHead.setCellValue("A/c Type");

                         HSSFCell benACNoHead     = rowhead.createCell((short) 7);
                         benACNoHead.setCellStyle(headingStyle);
                         benACNoHead.setCellValue("A/c No");

                         HSSFCell senIfscHead     = rowhead.createCell((short) 8);
                         senIfscHead.setCellStyle(headingStyle);
                         senIfscHead.setCellValue("IFSC");

                         HSSFCell senACNameHead     = rowhead.createCell((short) 9);
                         senACNameHead.setCellStyle(headingStyle);
                         senACNameHead.setCellValue("A/c Name");

                         HSSFCell senACTypeHead     = rowhead.createCell((short) 10);
                         senACTypeHead.setCellStyle(headingStyle);
                         senACTypeHead.setCellValue("A/c Type");

                         HSSFCell senACNoHead     = rowhead.createCell((short) 11);
                         senACNoHead.setCellStyle(headingStyle);
                         senACNoHead.setCellValue("A/c No");

                         HSSFCell tranStatusHead     = rowhead.createCell((short) 12);
                         tranStatusHead.setCellStyle(headingStyle);
                         tranStatusHead.setCellValue("Transaction Status");

                         HSSFRow row = null;

                         for (int i = exportXLS.size(), rowIndex = 0; rowIndex < i; rowIndex++) {

                             TransactionInfo ti = (TransactionInfo) exportXLS.get(rowIndex);

                             rowCount++;

                             row = sheet.createRow(rowCount);

                             HSSFCell snoCell    = row.createCell((short) 0);
                             snoCell.setCellStyle(contentStyle);
                             snoCell.setCellValue(rowIndex + 1);

                             HSSFCell valueDateCell    = row.createCell((short) 1);
                             valueDateCell.setCellStyle(stringStyle);
                             if (ti.getValueDate() != null) {
                                 valueDateCell.setCellValue(InstaReportUtil.getDateInSpecificFormat(dateFormat, ti.getValueDate().toString()));
                             } else {
                                 valueDateCell.setCellValue("");
                             }

                             HSSFCell tranRefCell    = row.createCell((short) 2);
                             tranRefCell.setCellStyle(stringStyle);
                             if (ti.getUtrNo() != null) {
                                 tranRefCell.setCellValue(ti.getUtrNo());
                             } else {
                                 tranRefCell.setCellValue("");
                             }

                             HSSFCell amountCell    = row.createCell((short) 3);
                             amountCell.setCellStyle(stringStyle);
                             if (ti.getAmount() != null) {
                                 amountCell.setCellValue(ti.getAmount().toString());
                             } else {
                                 amountCell.setCellValue("");
                             }

                             HSSFCell benIfscCell    = row.createCell((short) 4);
                             benIfscCell.setCellStyle(stringStyle);
                             if (ti.getBeneficiaryInfo().getAccIfsc() != null) {
                                 benIfscCell.setCellValue(ti.getBeneficiaryInfo().getAccIfsc());
                             } else {
                                 benIfscCell.setCellValue("");
                             }

                             HSSFCell benACNameCell    = row.createCell((short) 5);
                             benACNameCell.setCellStyle(stringStyle);
                             if (ti.getBeneficiaryInfo().getAccName() != null) {
                                 benACNameCell.setCellValue(ti.getBeneficiaryInfo().getAccName());
                             } else {
                                 benACNameCell.setCellValue("");
                             }

                             HSSFCell benACTypeCell    = row.createCell((short) 6);
                             benACTypeCell.setCellStyle(stringStyle);
                             if (ti.getBeneficiaryInfo().getAccType() != null) {
                                 benACTypeCell.setCellValue(ti.getBeneficiaryInfo().getAccType());
                             } else {
                                 benACTypeCell.setCellValue("");
                             }

                             HSSFCell benACNoCell    = row.createCell((short) 7);
                             benACNoCell.setCellStyle(stringStyle);
                             if (ti.getBeneficiaryInfo().getAccNo() != null) {
                                 benACNoCell.setCellValue(ti.getBeneficiaryInfo().getAccNo());
                             } else {
                                 benACNoCell.setCellValue("");
                             }



                             HSSFCell senIfscCell    = row.createCell((short) 8);
                             senIfscCell.setCellStyle(stringStyle);
                             if (ti.getBeneficiaryInfo().getAccIfsc() != null) {
                                 senIfscCell.setCellValue(ti.getSenderInfo().getAccIfsc());
                             } else {
                                 senIfscCell.setCellValue("");
                             }

                             HSSFCell senACNameCell    = row.createCell((short) 9);
                             senACNameCell.setCellStyle(stringStyle);
                             if (ti.getBeneficiaryInfo().getAccName() != null) {
                                 senACNameCell.setCellValue(ti.getSenderInfo().getAccName());
                             } else {
                                 senACNameCell.setCellValue("");
                             }

                             HSSFCell senACTypeCell    = row.createCell((short) 10);
                             senACTypeCell.setCellStyle(stringStyle);
                             if (ti.getBeneficiaryInfo().getAccType() != null) {
                                 senACTypeCell.setCellValue(ti.getSenderInfo().getAccType());
                             } else {
                                 senACTypeCell.setCellValue("");
                             }

                             HSSFCell senACNoCell    = row.createCell((short) 11);
                             senACNoCell.setCellStyle(stringStyle);
                             if (ti.getBeneficiaryInfo().getAccNo() != null) {
                                 senACNoCell.setCellValue(ti.getSenderInfo().getAccNo());
                             } else {
                                 senACNoCell.setCellValue("");
                             }

                             HSSFCell tranStatusCell    = row.createCell((short) 12);
                             tranStatusCell.setCellStyle(stringStyle);
                             if (ti != null) {
                                 tranStatusCell.setCellValue(ti.getStatusShortDesc());
                             } else {
                                 tranStatusCell.setCellValue("");
                             }

                         }

                         rowCount = rowCount +3;
                     }
                }
                wb.write(out);
                out.flush();
                out.close();

            } catch (Exception e) {
                logger.error("Exception while creating Excel sheet file"+e.getMessage());
                throw new Exception("Exception while creating Excel sheet file"+e);
            }
        }



        public int getIfnSeries() {

            return ifnSeries;
        }



        public void setIfnSeries(int ifnSeries) {

            this.ifnSeries = ifnSeries;
        }

    }

