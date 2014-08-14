/*
 * @(#)NEFTInwardPossibleReturnAction.java
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.insta.InstaULC;
import com.objectfrontier.insta.neft.reports.bean.InstaNEFTReportBean;
import com.objectfrontier.insta.reports.InstaReportUtil;
import com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction;



/**
 * @author Eswaripriyak
 * @date   Jun 02, 2009
 * @since  insta.reports; May 31, 2009
 */
public class NEFTInwardPossibleReturnAction 
extends AbstractReportAction {


    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.neft.report.rtgsinwardreturn";
        if (mode!=null && mode.equalsIgnoreCase("list")) {

            actionName += ".list";
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

        if (mode!=null && mode.equalsIgnoreCase("input")) {

            reportBean.resetBean();
            String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
            reportBean.getReportDto().setValueDate(appDate);
            return mapping.findForward("input");    
        } else if (mode!=null && mode.equalsIgnoreCase("list")) {

            try {
                
                reportBean.generateNEFTPossibleReturnReport(request);
                return mapping.findForward("list");    
            } catch (Exception e) {

                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
                return mapping.findForward("input");
            }
        }  else if (mode != null && mode.equalsIgnoreCase("exportExcel")) {
            
            try {
                
                String fileName = "NEFT Inward Possible Returned Report";
                
                if (reportBean.getReturnedList().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateInwardPossibleReturnReportExportToExcel(response.getOutputStream());
                } else {
                    reportBean.setMessage("No records found for Exporting into Excel");
                }
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        }
        return mapping.findForward("list");
    }
}
