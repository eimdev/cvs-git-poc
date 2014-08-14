/*
 * @(#)ListRTGSTxnAction.java
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
package com.objectfrontier.insta.rtgs.reports.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;

import com.host.rhs.meta.client.vo.HostIFSCDetailsValueObject;
import com.objectfrontier.arch.context.RequestContext;
import com.objectfrontier.arch.resource.ResourceException;
import com.objectfrontier.insta.client.InstaClientULC;
import com.objectfrontier.insta.reports.server.util.DateUtil;
import com.objectfrontier.insta.rtgs.reports.bean.ReportBean;
import com.objectfrontier.rtgs.client.jsp.util.ConversionUtils;
import com.objectfrontier.rtgs.client.jsp.util.RHSJSPConstants;
import com.objectfrontier.rtgs.client.jsp.util.RHSUIJSPConstants;
import com.objectfrontier.rtgs.report.dto.ReportDTO;
import com.objectfrontier.user.dto.UserDTO;

/**
 *
 * @author Karthick GRP
 * @date   Nov 28, 2004
 * @since  RHS IOB 1.0; Nov 28, 2004
 */
public class ReportSubmitAction extends AbstractReportAction{

    /**
     * @throws ResourceException
     */
    public ReportSubmitAction()
    throws ResourceException {
        super();
    }
    final static Logger logger = Logger.getLogger(ReportSubmitAction.class);

    /**
     * Process the requested form and forwards accordingly
     *
     * @param   mapping         ActionMapping to select this instance
     * @param   form            ActionForm bean for this request
     * @param   request         HttpServletRequest object
     * @param   response        HttpServletResponse object
     * @return  ActionForward   instance describing where and how the control
     *      should be forwarded or null if the response has already been completed
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public ActionForward exec(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        
        RequestContext.setPageContext(request);
//        if (!isAuthenticated(request.getSession().getId())) {
//            return mapping.findForward(RHSJSPConstants.FORWARD_LOGON);
//        }

        ReportDTO reportDTO = new ReportDTO();
        ReportBean reportBean = (ReportBean) form;
        if (reportBean == null) reportBean = new ReportBean();

        String mode = request.getParameter(RHSUIJSPConstants.MODE);
        String action = request.getParameter(RHSUIJSPConstants.ACTION);
        String typewise = request.getParameter(RHSUIJSPConstants.TYPEWISE);
        String reportwise = request.getParameter(RHSUIJSPConstants.REPORTWISE);
        String operation = request.getParameter(RHSJSPConstants.OPERATION);
        
        
        if ((RHSJSPConstants.BRANCH).equalsIgnoreCase(mode) && 
                            (RHSJSPConstants.INPUT).equalsIgnoreCase(action)) {
            reportBean.reset();
            reportBean.resetAll();
        }
        
        
        reportBean.setCurrentMode(mode);
        if (mode != null) reportBean.setCurrentMode(mode);
        ActionErrors errors = new ActionErrors();
        //Corresponding Transaction Report Title is generated
         String title = null;
         String printTitle = null;
         reportDTO.setReportType(
                                 ((RHSJSPConstants.INDIVIDUAL).equalsIgnoreCase(reportwise)
                                     ? RHSJSPConstants.INDIVIDUAL:(RHSJSPConstants.CONSOLIDATED).equalsIgnoreCase(reportwise)
                                     ? RHSJSPConstants.CONSOLIDATED:RHSJSPConstants.DETAILED_VIEW));

         if ((RHSJSPConstants.BRANCH).equalsIgnoreCase(mode)) {

             if ((RHSJSPConstants.ALL).equalsIgnoreCase(typewise)) {

                reportDTO.setTxnStatus(RHSJSPConstants.ALL);
                reportBean.setTranTypeList(reportBean.getTransactionList());
                reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());
                reportBean.setUtrWise(null);
                reportBean.setReportDTO(reportDTO);
            } else if ((RHSJSPConstants.SUCCESSFUL).equalsIgnoreCase(typewise)) {

                reportDTO.setTxnStatus(RHSJSPConstants.SUCCESSFUL);
                reportBean.setTranTypeList(reportBean.getTransactionList());
                reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());
                reportBean.setUtrWise(null);
                reportBean.setReportDTO(reportDTO);
            } else if ((RHSJSPConstants.UNSUCCESSFUL_TYPEWISE)
            .equalsIgnoreCase(typewise)) {

                reportDTO.setTxnStatus(RHSJSPConstants.UNSUCCESSFUL_TYPEWISE);
                reportBean.getTranTypeList().clear();
                reportBean.getTranTypeList().add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
                reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());
                reportBean.setUtrWise(null); 
                reportBean.setReportDTO(reportDTO);

            } else if ((RHSJSPConstants.UNSUCCESSFUL)
            .equalsIgnoreCase(typewise)) {

                reportDTO.setTxnStatus(RHSJSPConstants.UNSUCCESSFUL);
                // RBC
                reportBean.getTranTypeList().clear();
                reportBean.getTranTypeList().add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
                reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());
                reportBean.setUtrWise(null); 
                reportBean.setReportDTO(reportDTO);
            } else if ((RHSUIJSPConstants.CANCELLED).equalsIgnoreCase(typewise)) {

                reportDTO.setTxnStatus(RHSUIJSPConstants.CANCELLED);
                // RBC
                reportBean.getTranTypeList().clear();
                reportBean.getTranTypeList().add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
                reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());
                reportBean.setUtrWise(null); 
                reportBean.setReportDTO(reportDTO);
            } else if ((RHSJSPConstants.RETURNED).equalsIgnoreCase(typewise)) {

                reportDTO.setTxnStatus(RHSJSPConstants.RETURNED);
                reportBean.getTranTypeList().clear();
                reportBean.getTranTypeList().add(RHSJSPConstants.INWARD_TRANSACTION_TYPE);
                reportBean.setUtrWise(null);
                reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());
                reportBean.setReportDTO(reportDTO);
            } else if ((RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(typewise)) {
                
                reportDTO.setTxnStatus(RHSJSPConstants.UTR_NO_WISE);
                reportBean.setUtrWise(RHSJSPConstants.UTR_NO_WISE);
                reportDTO.setTxnStatus(RHSJSPConstants.UTR_NO_WISE);
                reportBean.setReportDTO(reportDTO);
            } else if ((RHSJSPConstants.TIMED_OUT).equalsIgnoreCase(typewise)) {
                
                reportDTO.setTxnStatus(RHSJSPConstants.TIMED_OUT);
                reportBean.getTranTypeList().clear();
                reportBean.getTranTypeList().add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
                reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());
                reportBean.setUtrWise(null);
                reportDTO.setTxnStatus(RHSJSPConstants.TIMED_OUT);
                reportBean.setReportDTO(reportDTO);
            }

              title = RHSJSPConstants.BRANCH_REPORTS;
              printTitle = RHSJSPConstants.PRINT_BRANCH;

         } else if((RHSJSPConstants.CONTROLLER).equalsIgnoreCase(mode)) {
             
             if ((RHSJSPConstants.ALL).equalsIgnoreCase(typewise)) {
                 
                 reportDTO.setTxnStatus(RHSJSPConstants.ALL);
                 reportBean.setBranchType(RHSJSPConstants.ALL);
                 reportBean.setTranTypeList(reportBean.getTransactionList());
                 reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());//RBC CMD 1.0 for setting message sub type
                 reportBean.setUtrWise(null);//RBC CMD 1.0  for setting utr number 
                 reportBean.setReportDTO(reportDTO);
              } else if ((RHSJSPConstants.SUCCESS_TRANSACTION).equalsIgnoreCase(typewise)) {
                  
                 reportDTO.setTxnStatus(RHSJSPConstants.SUCCESSFUL_TYPEWISE);
                 reportBean.setBranchType(RHSJSPConstants.ALL);   
                 reportBean.setTranTypeList(reportBean.getTransactionList());
                 reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());//RBC CMD 1.0 for setting message sub type
                 reportBean.setUtrWise(null);//RBC CMD 1.0  for setting utr number
                 reportBean.setReportDTO(reportDTO);
              } else if ((RHSJSPConstants.UN_SUCCESS_TRANSACTION).equalsIgnoreCase(typewise)) {
                  
                 reportDTO.setTxnStatus(RHSJSPConstants.UNSUCCESSFUL_TYPEWISE);
                 reportBean.setBranchType(RHSJSPConstants.ALL);
                 reportBean.getTranTypeList().clear();
                 // reportBean.getTranTypeList().add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
                 // reportBean.getTranTypeList().add(RHSJSPConstants.INWARD_TRANSACTION_TYPE);
                 reportBean.setTranTypeList(reportBean.getTransactionList());
                 reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());//RBC CMD 1.0 for setting message sub type
                 reportBean.setUtrWise(null); //RBC CMD 1.0  for setting utr number                
                 reportBean.setReportDTO(reportDTO);
             } else if ((RHSJSPConstants.SUCCESSFUL).equalsIgnoreCase(typewise)) {
                 
                 reportDTO.setTxnStatus(RHSJSPConstants. SUCCESSFUL );
                 reportBean.setBranchType(RHSJSPConstants.ALL);
                 reportBean.setTranTypeList(reportBean.getTransactionList());
                 reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());//RBC CMD 1.0 for setting message sub type
                 reportBean.setUtrWise(null);//RBC CMD 1.0  for setting utr number
                 reportBean.setReportDTO(reportDTO);
              } else if ((RHSJSPConstants.UNSUCCESSFUL).equalsIgnoreCase(typewise)) {
                  
                 reportDTO.setTxnStatus(RHSJSPConstants.UNSUCCESSFUL);
                 reportBean.setBranchType(RHSJSPConstants.ALL);
                 reportBean.getTranTypeList().clear();
                 // reportBean.getTranTypeList().add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
                 // reportBean.getTranTypeList().add(RHSJSPConstants.INWARD_TRANSACTION_TYPE);
                 reportBean.setTranTypeList(reportBean.getTransactionList());
                 reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());//RBC CMD 1.0 for setting message sub type
                 reportBean.setUtrWise(null);  //RBC CMD 1.0  for setting utr number               
                 reportBean.setReportDTO(reportDTO);
              } else if ((RHSUIJSPConstants.CANCELLED).equalsIgnoreCase(typewise)) {
                  
                 reportDTO.setTxnStatus(RHSUIJSPConstants.CANCELLED);
                 reportBean.setBranchType(RHSJSPConstants.ALL);
                 reportBean.getTranTypeList().clear();
                 reportBean.getTranTypeList().add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
                 reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());//RBC CMD 1.0 for setting message sub type
                 reportBean.setUtrWise(null);  //RBC CMD 1.0  for setting utr number               
                 reportBean.setReportDTO(reportDTO);
             } else if ((RHSJSPConstants.REDIRECTED).equalsIgnoreCase(typewise)) {
                 
                 reportDTO.setTxnStatus(RHSJSPConstants.REDIRECTED);
                 reportBean.setBranchType(RHSJSPConstants.ALL);
                 reportBean.getTranTypeList().clear();
                 reportBean.getTranTypeList().add(RHSJSPConstants.INWARD_TRANSACTION_TYPE);                 
                 reportBean.setReportDTO(reportDTO);
              } else if ((RHSJSPConstants.FOR_OTHER_BRANCHES).equalsIgnoreCase(typewise)) {
                  
                  reportDTO.setTxnStatus(RHSJSPConstants.FOR_OTHER_BRANCHES);
                 reportBean.setBranchType(RHSJSPConstants.ALL);
                 reportBean.setTranTypeList(reportBean.getTransactionList());
                 reportBean.setReportDTO(reportDTO);
              } else if ((RHSJSPConstants.RETURNED).equalsIgnoreCase(typewise)) {//RBC CMD 1.0 for Returned
                    
                  reportDTO.setTxnStatus(RHSJSPConstants.RETURNED);
                    reportBean.setBranchType(RHSJSPConstants.ALL);
                    reportBean.getTranTypeList().clear();
                    reportBean.getTranTypeList().add(RHSJSPConstants.INWARD_TRANSACTION_TYPE);
                    reportBean.setUtrWise(null);//RBC CMD 1.0  for setting utr number   
                    reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());//RBC CMD 1.0 for setting message sub type              
                    reportBean.setReportDTO(reportDTO);
             } else if ((RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(typewise)) {//RBC CMD 1.0 for UTR number wise report
                 
                 reportDTO.setTxnStatus(RHSJSPConstants.UTR_NO_WISE);
                 reportBean.setBranchType(RHSJSPConstants.ALL);
                 reportBean.setUtrWise(RHSJSPConstants.UTR_NO_WISE);
                 reportDTO.setTxnStatus(RHSJSPConstants.UTR_NO_WISE);              
                 reportBean.setReportDTO(reportDTO);
            } else if ((RHSJSPConstants.TIMED_OUT).equalsIgnoreCase(typewise)) {

                reportDTO.setTxnStatus(RHSJSPConstants.TIMED_OUT);
                reportBean.setBranchType(RHSJSPConstants.ALL);
                reportBean.setUtrWise(null);
                reportDTO.setTxnStatus(RHSJSPConstants.TIMED_OUT);
                reportBean.getTranTypeList().clear();
                reportBean.getTranTypeList().add(RHSJSPConstants.OUTWARD_TRANSACTION_TYPE);
                reportBean.setReportDTO(reportDTO);
            }
              title = RHSJSPConstants.CONTROLLER_REPORT;
              printTitle = RHSJSPConstants.PRINT_CONTROLLER;
         }

         String status = reportBean.getReportDTO().getTxnStatus();

         if (status != null) {

            if (status.equalsIgnoreCase(RHSJSPConstants.ALL)) {

                title += RHSJSPConstants.ALL_TRANSACTION;
                printTitle += RHSJSPConstants.PRINT_All_TXNS;
            } else if (status.equalsIgnoreCase(RHSJSPConstants.SUCCESSFUL)) {

                title += RHSJSPConstants.BRANCH.equalsIgnoreCase(mode) ? 
                         RHSJSPConstants.SUCCESS_TRANSACTION
                         : RHSJSPConstants.SUCCESS_BRANCH_TRANSACTION;

                printTitle += RHSJSPConstants.BRANCH.equalsIgnoreCase(mode) ? 
                              RHSJSPConstants.PRINT_SUCCESS_TXNS
                              : RHSJSPConstants.PRINT_SUCCESS_BRANCH_TXNS;
            } else if (status.equalsIgnoreCase(RHSJSPConstants.UNSUCCESSFUL)) {

                title += (RHSJSPConstants.BRANCH).equalsIgnoreCase(mode) ? 
                          RHSJSPConstants.UN_SUCCESS_TRANSACTION
                          : RHSJSPConstants.UN_SUCCESS_BRANCH_TRANSACTION;

                printTitle += (RHSJSPConstants.BRANCH).equalsIgnoreCase(mode) ? 
                               RHSJSPConstants.PRINT_UNSUCCESS_TXNS
                               : RHSJSPConstants.PRINT_UNSUCCESS_BRANCH_TXNS;
            } else if (status.equalsIgnoreCase(RHSUIJSPConstants.CANCELLED)) {

                title += RHSJSPConstants.CANCEL;
                printTitle += RHSJSPConstants.PRINT_CANCEL_TXNS;
            } else if (status.equalsIgnoreCase(RHSJSPConstants.RETURNED)) {

                title += RHSJSPConstants.RETURN_TRANSACTION;
                printTitle += RHSJSPConstants.PRINT_RETURN_TXNS;
            } else if (status.equalsIgnoreCase(RHSJSPConstants.SUCCESSFUL_TYPEWISE)) {

                title += RHSJSPConstants.SUCCESS_TRANSACTION;
                printTitle += RHSJSPConstants.PRINT_SUCCESS_TYPEWISE_TXNS;
            } else if (status.equalsIgnoreCase(RHSJSPConstants.UNSUCCESSFUL_TYPEWISE)) {

                title += RHSJSPConstants.UN_SUCCESS_TRANSACTION;
                printTitle += RHSJSPConstants.PRINT_UNSUCCESS_TYPEWISE_TXNS;
            } else if (status.equalsIgnoreCase(RHSJSPConstants.DEFERRED)) {

                title += RHSJSPConstants.DEFERRED_TRANSACTION;
                printTitle += RHSJSPConstants.PRINT_DEFFER_TXNS;

            } else if (status.equalsIgnoreCase(RHSJSPConstants.FOR_OTHER_BRANCHES)) {

                title += RHSJSPConstants.TRANSACTION_BEHALF;
                printTitle += RHSJSPConstants.PRINT_TXNS_BEHALF;
            } else if (status.equalsIgnoreCase(RHSJSPConstants.REDIRECTED)) {

                title += RHSJSPConstants.REDIRECTED;
                printTitle += RHSJSPConstants.PRINT_REDIRECTED;
            } else if (status.equalsIgnoreCase(RHSJSPConstants.UTR_NO_WISE)) {

                title += RHSJSPConstants.UTR_NO_WISE;
                printTitle += RHSJSPConstants.PRINT_UTR_NO_WISE;
            } else if (status.equalsIgnoreCase(RHSJSPConstants.TIMED_OUT)) {

                title += RHSJSPConstants.TIMED_OUT;
                printTitle += RHSJSPConstants.PRINT_TIMED_OUT;
            }
        }

      String reportType = reportBean.getReportDTO().getReportType();

      if (reportType != null) {

          if (reportType.equalsIgnoreCase(RHSJSPConstants.INDIVIDUAL)) {

              title += RHSJSPConstants.INDIVIDUAL;
              printTitle += RHSJSPConstants.INDIVIDUAL;
          } else if (reportType.equalsIgnoreCase(RHSJSPConstants.CONSOLIDATED)) {

              title += RHSJSPConstants.CONSOLIDATED;
              printTitle += RHSJSPConstants.CONSOLIDATED;
          } else if (reportType.equalsIgnoreCase(RHSJSPConstants.DETAILED_VIEW)) {

              title += RHSJSPConstants.DETAILED_VIEW;
              printTitle += RHSJSPConstants.PRINT_DETAILED_VIEW;
          }
      }

        // Set title value in ReportBean static title variable
      ReportBean.title = printTitle + RHSJSPConstants.PRINT_TITLE_AS_ON; // Set the title variable into the ReportBean static title variable

          if ((RHSJSPConstants.BRANCH).equalsIgnoreCase(mode)
                  && (RHSJSPConstants.INPUT).equalsIgnoreCase(action)) {

              try {
//                  reportBean.reset();
//                  reportBean.resetAll();
                  reportBean.loadIFSCMaster(); //RBC CMD 1.0
                  logger.info("IFSCMaster loaded ");
                  UserDTO userDTO = reportBean.getCurrentUser();
                  
                  if (null == userDTO) {
                      logger.error("UserDTO is null ");
                      throw new RuntimeException("UserDTO is null");
                  }
                  
                  String branchCode = null;
                  String branchName = null;
                  String id = (String)request.getSession().getAttribute(InstaClientULC.IFSCCODE);
                  logger.info("IFSC Code = " + id);
                  userDTO.masterDTO = reportBean.getIfscDetails(id);

                  if (userDTO.masterDTO != null) {
                      branchCode = ((HostIFSCDetailsValueObject)userDTO.masterDTO.ifscMasterVO).getBranchCode();
                      branchName = ((HostIFSCDetailsValueObject)userDTO.masterDTO.ifscMasterVO).getName();
                  }
                  reportBean.getReportDTO().setBranchCode(branchCode);
                  reportBean.getReportDTO().setBranchName(branchName);
                  logger.info("BranchCode & BranchName = " + branchCode + branchName);
                  
                 /* Set the RTGS Business Date to the From date and To date
                  * Convert the date format from DD-MMM-YYYY to DD-MM-YYYY
                  */
                 
                  String date = DateUtil.convert((getBusinessDate()));
                  
                  reportBean.setFromDate(date);
                  reportBean.setToDate(date);
                  
                  if (userDTO.masterDTO != null) reportBean.getUserIdByLocation();
                  logger.info("User details populated");
                  return mapping.findForward(RHSJSPConstants.FORWARD_BRANCH_INPUT);
                  
              } catch (Exception e) {
                  logger.error("Cannot generate the report for Branch" + e.getMessage() );
                  errors.add(ActionErrors.GLOBAL_ERROR,
                         new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                         ConversionUtils.getRootExceptionMessage(e)));
                  saveErrors(reportBean, errors);
                  return mapping.findForward(RHSJSPConstants.FORWARD_BRANCH_INPUT);
              }
          }  else if ((RHSJSPConstants.BRANCH).equalsIgnoreCase(mode)
                      && (RHSJSPConstants.SUBMIT).equalsIgnoreCase(action)) {
              try {
                  if (!(RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(reportBean.getUtrWise())) {
                  errors = reportBean.validate();
                      if(!errors.isEmpty()) {
                          saveErrors(reportBean, errors);
                          return mapping.findForward(RHSJSPConstants.FORWARD_BRANCH_INPUT);
                      }
                  } else { 
                      
                    errors = reportBean.validateUTR();
                    if(!errors.isEmpty()) {
                        saveErrors(reportBean, errors);
                        return mapping.findForward(RHSJSPConstants.FORWARD_BRANCH_INPUT);
                    }
                      
                  }
                  // Calling DetailedReport method if reportType is Detailed_view
                  if (reportType != null &&  reportType.trim().length() > 0
                  && reportType.equalsIgnoreCase(RHSJSPConstants.DETAILED_VIEW) ) {

                      reportBean.generateDetailedReport();
                  } else {

                      reportBean.generateReport();
                  }
                  return mapping.findForward(title);

              } catch (Exception e) {
                  logger.error("Cannot generate the report for Branch" + e.getMessage() );
                  errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                           ConversionUtils.getRootExceptionMessage(e)));
                  saveErrors(reportBean, errors);
                  return mapping.findForward(RHSJSPConstants.FORWARD_BRANCH_FAILURE);
              }
          } else if ((RHSJSPConstants.BRANCH).equalsIgnoreCase(mode)
              && (RHSJSPConstants.SUBMIT)
                   .equalsIgnoreCase(action)
                   && (RHSJSPConstants.ALL).equalsIgnoreCase(typewise)) {

            try {
                errors = reportBean.validate();
                if (!errors.isEmpty()) {

                    saveErrors(reportBean, errors);
                    return mapping.findForward(RHSJSPConstants.FORWARD_BRANCH_INPUT);
                }

                // Calling DetailedReport method if reportType is Detailed_view
                if (reportType != null && reportType.trim().length() > 0
                    && reportType.equalsIgnoreCase(RHSJSPConstants.DETAILED_VIEW)) {

                    reportBean.generateDetailedReport();
                } else {

                    reportBean.generateReport();
                }
                return mapping.findForward(title);

            } catch (Exception e) {
                logger.error("Cannot generate the report for Branch" + e.getMessage() );
                errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                             ConversionUtils
                                             .getRootExceptionMessage(e)));
                saveErrors(reportBean, errors);
                return mapping.findForward(RHSJSPConstants.FORWARD_BRANCH_FAILURE);
            }
        } else if ((RHSJSPConstants.BRANCH).equalsIgnoreCase(mode)
              && (RHSUIJSPConstants.RESET).equalsIgnoreCase(action)) {

              try {
                  reportBean.resetAll();

                  UserDTO userDTO = reportBean.getCurrentUser();
                  String branchCode = null;
                  String branchName = null;
                  String id = (String)request.getSession().getAttribute(InstaClientULC.IFSCCODE);
                  logger.info("IFSC Code = " + id);
                  userDTO.masterDTO = reportBean.getIfscDetails(id);

                  if (userDTO.masterDTO != null) {
                      branchCode = ((HostIFSCDetailsValueObject)userDTO.masterDTO.ifscMasterVO).getBranchCode();
                      branchName = ((HostIFSCDetailsValueObject)userDTO.masterDTO.ifscMasterVO).getName();
                  }
                  
                  reportBean.getReportDTO().setBranchCode(branchCode);
                  reportBean.getReportDTO().setBranchName(branchName);
                  // Set the RTGS Business Date to the From date and To date text fields
                  String date = DateUtil.convert((getBusinessDate()));
                  reportBean.setFromDate(date);
                  reportBean.setToDate(date);

                  return mapping.findForward(RHSJSPConstants.FORWARD_BRANCH_INPUT);
              } catch (Exception e) {
                  logger.error("Cannot generate the report for Branch" + e.getMessage() );
                  errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                           ConversionUtils.getRootExceptionMessage(e)));
                  saveErrors(reportBean, errors);
                  return mapping.findForward(RHSJSPConstants.FORWARD_BRANCH_FAILURE);
            }
        } else if ((RHSJSPConstants.CONTROLLER).equalsIgnoreCase(mode)
                    && (RHSJSPConstants.INPUT).equalsIgnoreCase(action)) {
            try {

                if ((RHSUIJSPConstants.RESET).equalsIgnoreCase(operation)) {
                    reportBean.resetAll();
                }
                // reportBean.reset();
                reportBean.loadIFSCMaster();
                reportBean.loadSOLDetails();
                UserDTO userDTO = reportBean.getCurrentUser();
                String id = (String)request.getSession().getAttribute(InstaClientULC.IFSCCODE);
                logger.info("IFSC Code = " + id);
                
                userDTO.masterDTO = reportBean.getIfscDetails(id);
                // set the Default RTGS Business date to the From date and todate field
                String date = DateUtil.convert((getBusinessDate()));
                reportBean.setFromDate(date);
                reportBean.setToDate(date);
                String branchType = reportBean.getBranchType();
                String branchId = reportBean.getReportDTO().getBranchId();
                logger.info("Branch Id and Type = " + branchType + "  " + branchId);
                
                if (userDTO.masterDTO != null) {
                    if (branchType != null) {
                        if (branchType.equalsIgnoreCase(RHSJSPConstants.ALL)) {
                            reportBean.getUserIdByLocation();
                        } else {
                            if (branchId != null) {
                                reportBean.getUserIdByLocation(branchId);
                            }
                        }
                    }
                }
                return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_INPUT);

            } catch (Exception e) {
                logger.error("Cannot generate the report for Controller" + e.getMessage() );
                errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                             ConversionUtils
                                             .getRootExceptionMessage(e)));
                saveErrors(reportBean, errors);
                return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_FAILURE);
            }
        } else if ((RHSJSPConstants.CONTROLLER).equalsIgnoreCase(mode)
                && (RHSJSPConstants.SUBMIT).equalsIgnoreCase(action)) {

            try {
                
                if (!(RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(reportBean.getUtrWise())) {
                  errors = reportBean.validate();
                  if(!errors.isEmpty()) {
                      saveErrors(reportBean, errors);
                      return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_INPUT);
                  }
                  } else { 
      
                    errors = reportBean.validateUTR();
                    if(!errors.isEmpty()) {
                        saveErrors(reportBean, errors);
                        return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_INPUT);
                    }
                  }
                
                if (reportType != null && reportType.trim().length() > 0
                    && reportType.equalsIgnoreCase(RHSJSPConstants.DETAILED_VIEW)) {

                        reportBean.generateDetailedReport();
                } else {

                    reportBean.generateReport();
                }
                return mapping.findForward(title);


            } catch (Exception e) {
                logger.error("Cannot generate the report for Controller" + e.getMessage() );
                errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                           ConversionUtils.getRootExceptionMessage(e)));
                saveErrors(reportBean, errors);
                return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_FAILURE);
            }
        } else if ((RHSJSPConstants.CONTROLLER).equalsIgnoreCase(mode)
                && (RHSUIJSPConstants.RESET).equalsIgnoreCase(action)) {

            try {
                reportBean.resetAll();
                String date = DateUtil.convert((getBusinessDate()));
                reportBean.setFromDate(date);
                reportBean.setToDate(date);
                return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_INPUT);
            } catch (Exception e) {
                logger.error("Cannot generate the report for Controller" + e.getMessage() );
                errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                           ConversionUtils.getRootExceptionMessage(e)));
                saveErrors(reportBean, errors);
                return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_FAILURE);
            }
       }
        return mapping.findForward(RHSJSPConstants.FORWARD_FAILURE);
    }
}
