/*
 * @(#)NEFTInwardSummaryReportAction.java
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
//import com.objectfrontier.neft.report.dto.ITDetailReportDTO;



/**
 * @author pandiarajann
 * @date   Jul 29, 2011
 * @since  insta.reports; Jul 29, 2011
 */
public class NEFTInwardBankWiseReportAction
extends AbstractReportAction {

    private static Category logger = Category.getInstance(NEFTInwardBankWiseReportAction.class.getName());

    /**
     * @see com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction#getActionName(java.lang.String)
     * @param mode String
     * @return actionName String
     *
     */
    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.neft.report.inwardBanksummary";
        if (mode!=null && mode.equalsIgnoreCase("inwardviewreport")) {
            actionName += ".viewreport";
        }
        return actionName;
    }

    /**
     * @see com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction#exec(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     *      This method is for executing according to the mode that comes in.
     * @return findForward ActionForward
     */
    @Override
    public ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {

        String mode = request.getParameter("mode");
        InstaNEFTReportBean reportBean = (InstaNEFTReportBean) form;
        if (reportBean==null) {
            reportBean = new InstaNEFTReportBean();
        }
        if (mode!=null && mode.equalsIgnoreCase("inwardSummaryReportView")) {

            try {

                reportBean.generateNEFTInwBankSummaryReport(request);
                reportBean.setReportTitle(reportBean.getInwSummaryReport());
                return mapping.findForward("inwardSummaryReportView");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("inwardSummaryExportExcel")) {

            try {

                String fileName = "NEFT Inward Bank wise Summary Report ";

                if (reportBean.getReturnedList().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateNEFTInwBankSummaryReportToExcel(response.getOutputStream());
                } else {
                    reportBean.setMessage("No records found for Exporting into Excel");
                }
                return mapping.findForward("inwardSummaryReportView");
            } catch (Exception e) {

                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("outwardSummaryExportExcel")) {

            try {

                String fileName = "NEFT Outward Bank wise Summary Report ";

                if (reportBean.getReturnedList().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateNEFTInwBankSummaryReportToExcel(response.getOutputStream());
                } else {
                    reportBean.setErrorMessage("No records found for Exporting into Excel");
                    reportBean.setMessage("No records found for Exporting into Excel");

                }
                return mapping.findForward("outwardSummaryviewreport");
            } catch (Exception e) {

                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        }else if (mode!=null && mode.equalsIgnoreCase("outwardDetailedViewReport")) {

            try {

            	reportBean.setDateFormat("");
                reportBean.generateNEFTOutwardBankDetailedReport(request);
                reportBean.setReportTitle(reportBean.getOutDetailReport());
                return mapping.findForward("outwardDetailedreportview");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode!=null && mode.equalsIgnoreCase("inwardDetailedviewreport")) {

            try {

            	reportBean.setDateFormat("dd-mmm-yyyy");
                reportBean.generateNEFTInwBankDetailedReport(request);
                reportBean.setReportTitle(reportBean.getInwDetailReport());
                return mapping.findForward("inwardDetailedreportview");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("inwardDetailedExportExcel")) {

            try {

                String fileName = "NEFT Bank wise Summary Report ";

                if (reportBean.getReportMap().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateNEFTInwBankDetailedReportToExcel(response.getOutputStream());
                } else {
                    reportBean.setMessage("No records found for Exporting into Excel");
                }
                return mapping.findForward("inwardDetailedreportview");
            } catch (Exception e) {

                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode!=null && mode.equalsIgnoreCase("outwardSummaryReportView")) {

            try {

                reportBean.setDateFormat("dd-mmm-yyyy");
                reportBean.generateNEFTOutwBankSummaryReport(request);
                reportBean.setReportTitle(reportBean.getOutwSummaryReport());
                return mapping.findForward("outwardSummaryviewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        }
        return mapping.findForward("input");
    }
}
