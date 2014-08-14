/*
 * @(#)NEFTGraduatedPaymentReportAction.java
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

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.insta.InstaULC;
import com.objectfrontier.insta.neft.reports.bean.InstaNEFTReportBean;
import com.objectfrontier.insta.reports.InstaReportUtil;
import com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction;



/**
 * @author mohanadevis
 * @date   Feb 6, 2009
 * @since  insta.reports; Feb 6, 2009
 */
public class NEFTGraduatedPaymentReportAction 
extends AbstractReportAction {
    
private static Category logger = Category.getInstance(NEFTInwardSummaryReportAction.class.getName());
    
    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.neft.report.gpr";
        if (mode!=null && mode.equalsIgnoreCase("viewreport")) {
            actionName += ".viewreport";
        }else if (mode!=null && mode.equalsIgnoreCase("view")) {
            actionName += ".view";
        }
        return actionName;
    }

    @Override
    public ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {

        String mode = request.getParameter("mode");
        InstaNEFTReportBean reportBean = (InstaNEFTReportBean) form;
        if (reportBean==null) {
            reportBean = new InstaNEFTReportBean();
        }
        if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {
                
                reportBean.isDateWiseGraduated = 1;
                reportBean.generateNEFTGraduatedPaymentReport(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("exportexcel")) {
            
            try {
                
                String fileName = "Graduated Payment Report";
                
                if (reportBean.getGraduadtedPayments().size() > 0) {
                    
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.graduatedPaymentExportToExcel(response.getOutputStream());     
                 } else {
                    reportBean.setMessage("No records found for Exporting into Excel");
                }
                if (reportBean.isDateWiseGraduated == 1) {
                    return mapping.findForward("viewreport");
                } 
                return mapping.findForward("view");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode!=null && mode.equalsIgnoreCase("view")) {

            try {
                
                reportBean.isDateWiseGraduated = 0;
                reportBean.setDateFormat("");
                reportBean.getReportDto().setValueDate(InstaReportUtil.formatDate(new Date(System.currentTimeMillis()), "dd-MM-yyyy"));
                reportBean.generateNEFTGraduatedPaymentReport(request);
                return mapping.findForward("view");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        }
        return mapping.findForward("input");
    }
}
