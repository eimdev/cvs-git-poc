/*
 * @(#)NEFTOutwardSummaryReportAction.java
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
import com.objectfrontier.neft.report.dto.OTDetailReportDTO;



/**
 * @author mohanadevis
 * @date   Jan 10, 2009
 * @since  insta.reports; Jan 10, 2009
 */
public class NEFTOutwardSummaryReportAction 
extends AbstractReportAction {
    
private static Category logger = Category.getInstance(NEFTInwardSummaryReportAction.class.getName());
    
    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.neft.report.outwardsummary";
        if (mode!=null && mode.equalsIgnoreCase("viewreport")) {
            actionName += ".viewreport";
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

                reportBean.generateNEFTOutSummaryReports(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("exportexcel")) {
            
            try {
                
                String fileName = null;
                if (reportBean.getReportDto().getReportType().equalsIgnoreCase("Summary")) {
                    fileName = "Outward Summary Report";
                } else {
                    fileName = "Outward Detail Report";
                }
                OTDetailReportDTO outDto = (OTDetailReportDTO)reportBean.getNeftRepDTO();
                if (outDto.getOutwardMap().size() > 0) {
                    
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    if (reportBean.getReportDto().getReportType().equalsIgnoreCase("Summary")) {
                        reportBean.outSummaryExportToExcel(response.getOutputStream());      
                    } else {
                        reportBean.outDetailExportToExcel(response.getOutputStream());
                    }
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
