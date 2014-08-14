/*
 * @(#)BrSubtypeIndividualReportAction.java
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
package com.objectfrontier.insta.neft.reports.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.insta.InstaULC;
import com.objectfrontier.insta.neft.reports.bean.InstaNEFTReportBean;
import com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction;



/**
 * @author mohanadevis
 * @date   Dec 16, 2008
 * @since  insta.reports; Dec 16, 2008
 */
public class NEFTPaymentReportAction 
extends AbstractReportAction {
    
    private static Category logger = Category.getInstance(NEFTPaymentReportAction
                                                          .class);

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.vijb.report.paymentReport";
        if (mode!=null && mode.equalsIgnoreCase("viewreport")) {
            actionName += ".viewreport";
        }
        return actionName;
    }
    
    @Override
    public ActionForward exec(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request, HttpServletResponse response)
    throws Exception {

        String mode = request.getParameter("mode");
        String report = request.getParameter("report");
        InstaNEFTReportBean reportBean = (InstaNEFTReportBean) form;
        reportBean.setReport(report);
        
        if (reportBean==null) {
            reportBean = new InstaNEFTReportBean();
        }

        if (mode!=null && mode.equalsIgnoreCase("submitviewreport")) {

            try {

                reportBean.generatePaymentReport(request);
                return mapping.findForward("submitviewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode!=null && mode.equalsIgnoreCase("receiveviewreport")) {

            try {

                reportBean.generatePaymentReport(request);
                return mapping.findForward("receiveviewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("submitexportreport")) {
            
            try {
                String fileName = null;
                if (report.equalsIgnoreCase("submitted")) {
                    fileName = "Payment Submitted Report";
                } else {
                    fileName = "Payment Received Report";
                }
                if (reportBean.getDetailReportDTOs().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.paymentExportToExcel(response.getOutputStream());
                } else {
                    reportBean.setMessage("No records found for Exporting into Excel");
                }
                if (report.equalsIgnoreCase("submitted")) {
                    return mapping.findForward("submitviewreport");
                } 
                    return mapping.findForward("receiveviewreport");
                
            } catch (Exception e) {
                
                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } 
        return mapping.findForward("input");
    }

}
