/*
 * @(#)NEFTReconcilliationReportAction.java
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
import com.objectfrontier.insta.reports.dto.ReconcileReportDTO;
import com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction;
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;



/**
 * @author mohanadevis
 * @date   Dec 29, 2008
 * @since  insta.reports; Dec 29, 2008
 */
public class NEFTReconciliationReportAction 
extends AbstractReportAction {
    
    private static Category logger = Category.getInstance(NEFTReconciliationReportAction.class.getName());

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.neft.report.reconcillation";
        if (mode!=null && mode.equalsIgnoreCase("input")) {
            actionName += ".input";
        } else if (mode!=null && mode.equalsIgnoreCase("viewreport")) {
            actionName += ".viewreport";
        }
        return actionName;
    }

    @Override
    public ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {

        String mode = request.getParameter("mode");
         InstaReportBean reportBean = (InstaReportBean) form;
        if (reportBean==null) {
            reportBean = new InstaReportBean();
        }
        if (mode!=null && mode.equalsIgnoreCase("input")) {

            reportBean.resetBean();
            reportBean.isDateOnly=1;
            //String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate()); //Commented by priyak for date fromat
            reportBean.getReportDto().setValueDate(getBusinessDate());
            return mapping.findForward("input");    
        } else if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {

                reportBean.generateNEFTReconcillationReports(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("exportexcel")) {
            
            try {
                
                String fileName = "NEFT Reconciliation Report";
                ReconcileReportDTO dto = (ReconcileReportDTO)reportBean.getNewReportDto();
                if(dto.getReconcileReportDTOs().size() > 0) {
                
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.reconcillationExportToExcel(response.getOutputStream());
                } else {
                    reportBean.setMessage("No records found for Exporting into Excel");
                }
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        }
        return mapping.findForward("input");
    }

}
