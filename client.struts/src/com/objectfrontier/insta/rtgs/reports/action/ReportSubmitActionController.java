/*
 * Created on Jul 11, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.objectfrontier.insta.rtgs.reports.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
 * @author mohanadevis
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReportSubmitActionController 
extends AbstractReportAction {
    
    public ReportSubmitActionController()
    throws ResourceException {
        super();
    }

    final static Logger logger = Logger.getLogger(ReportSubmitActionController.class);
    
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
   public ActionForward exec(ActionMapping mapping, ActionForm form,
               HttpServletRequest request, HttpServletResponse response)
   throws Exception {
   
        RequestContext.setPageContext(request);
      /*  if (!isAuthenticated(request.getSession().getId())) {
            return mapping.findForward(RHSJSPConstants.FORWARD_LOGON);
        }*/
        
        ReportDTO reportDTO = new ReportDTO();
        ReportBean reportBean = (ReportBean) form;
        if (reportBean == null) reportBean = new ReportBean();
    
        String mode = request.getParameter(RHSUIJSPConstants.MODE);
        String action = request.getParameter(RHSUIJSPConstants.ACTION);
        String reportwise = request.getParameter(RHSUIJSPConstants.REPORTWISE);
   
        if (mode != null) reportBean.setCurrentMode(mode);
        ActionErrors errors = new ActionErrors();
        String printTitle = null;
        String forward = null;
        
        if ((RHSJSPConstants.CONTROLLER).equalsIgnoreCase(mode) && (RHSJSPConstants.INPUT).equalsIgnoreCase(action)) {
            reportBean.reset();
            reportBean.resetAll();

        }
        
       if ((RHSJSPConstants.CONTROLLER).equalsIgnoreCase(mode) && (RHSJSPConstants.INPUT).equalsIgnoreCase(action)) {
           
           try {
                reportBean.getReportDTO().tranType = null;
                reportBean.getReportDTO().msgSubType = null;
                reportBean.getTranTypeList().clear();
                reportBean.subTypeMap.clear();
                reportBean.reset();
                reportBean.setReportWise(reportwise);

                if ((RHSJSPConstants.CONSOLIDATED).equalsIgnoreCase(reportwise)) {

                    reportBean.setTranTypeList(reportBean.getTransactionList());
                    reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());
                } else {

                    reportBean.setTranTypeList(reportBean.getTransactionList());
                    reportBean.setSubTypeMap(reportBean.getMsgSubTypeMap());
                }

                reportBean.setReportDTO(reportDTO);
                reportBean.setBranchType(RHSJSPConstants.ALL);
                reportBean.loadIFSCMaster();
                reportBean.loadSOLDetails();

                UserDTO userDTO = reportBean.getCurrentUser();
                String id = (String)request.getSession().getAttribute(InstaClientULC.IFSCCODE);
                logger.info("IFSC code = " + id);
                userDTO.masterDTO = reportBean.getIfscDetails(id);
                // set the Default RTGS Business date to the From date and
                // todate field

                String date = DateUtil.convert((getBusinessDate()));
                reportBean.setFromDate(date);
                reportBean.setToDate(date);

                // RBC CMD 1.0
                String branchType = reportBean.getBranchType();
                String branchId = reportBean.getReportDTO().getBranchId();
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
                if ((RHSJSPConstants.CONSOLIDATED).equalsIgnoreCase(reportwise)) {

                    forward = RHSJSPConstants.FORWARD_CONSOLIDATED_INPUT;
                } else {
                    forward = RHSJSPConstants.FORWARD_CONTROLLER_INPUT;
                }

                return mapping.findForward(forward);
            } catch (Exception e) {
                logger.error("Cannot generate the report for Controller" + e.getMessage() );
               errors.add(ActionErrors.GLOBAL_ERROR,
                          new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                          ConversionUtils.getRootExceptionMessage(e)));
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
              if ((RHSJSPConstants.CONSOLIDATED).equalsIgnoreCase(reportwise)) {
                  reportDTO.setReportType(RHSJSPConstants.CONSOLIDATED);
                  printTitle = RHSJSPConstants.PRINT_CONTROLLER;
                  ReportBean.title = printTitle+RHSJSPConstants.PRINT_All_TXNS +RHSJSPConstants.CONSOLIDATED+RHSJSPConstants.PRINT_TITLE_AS_ON;
                  forward = RHSJSPConstants.FORWARD_CONSOLIDATED;
                  reportBean.getReportDTO().setReportType(RHSJSPConstants.CONSOLIDATED);
              } else {
                 forward = RHSJSPConstants.FORWARD_CONTROLLERSUBMIT;
                 reportDTO.setReportType(RHSJSPConstants.INDIVIDUAL);
                 printTitle = RHSJSPConstants.PRINT_CONTROLLER;
                 ReportBean.title = printTitle+RHSJSPConstants.PRINT_All_TXNS +RHSJSPConstants.INDIVIDUAL+RHSJSPConstants.PRINT_TITLE_AS_ON;
                 reportBean.getReportDTO().setReportType(RHSJSPConstants.INDIVIDUAL);
              }
//              reportForm.setReportDTO(reportDTO);
              //String tranType = reportForm.getReportDTO().getTranType();
              reportBean.generateReportController();
           
           return mapping.findForward(forward);

       } catch (Exception e) {
           logger.error("Cannot generate the report for Controller" + e.getMessage() );
           errors.add(ActionErrors.GLOBAL_ERROR,
                      new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                      ConversionUtils.getRootExceptionMessage(e)));
           saveErrors(reportBean, errors);
           return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_FAILURE);
       }
   } else if ((RHSJSPConstants.CONTROLLER).equalsIgnoreCase(mode)
   && (RHSJSPConstants.RESET).equalsIgnoreCase(action)) {

            try {
                reportBean.resetAll();
                return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_INPUT);
            } catch (Exception e) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError(RHSJSPConstants.ERROR_MESSAGE_KEY,
                                           ConversionUtils
                                           .getRootExceptionMessage(e)));
                saveErrors(reportBean, errors);
                return mapping.findForward(RHSJSPConstants.FORWARD_CONTROLLER_FAILURE);
            }
        }
    return mapping.findForward(RHSJSPConstants.FORWARD_FAILURE);
   } 
}
