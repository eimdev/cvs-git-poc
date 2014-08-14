/*
 * @(#)NEFTCPwiseReconcilliationReportAction.java
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
 * @date   Apr 22, 2009
 * @since  insta.reports; Apr 22, 2009
 */
public class NEFTCPwiseReconcilliationReportAction 
extends AbstractReportAction {
    
    private static Category logger = Category.getInstance(NEFTCPwiseReconcilliationReportAction
                                                          .class);

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.vijb.report.neftCPwiseReconcilliation";
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
        InstaNEFTReportBean reportBean = (InstaNEFTReportBean) form;

        if (reportBean==null) {
            reportBean = new InstaNEFTReportBean();
        }

        if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {

                reportBean.generateNeftCPwiseReconcilliation(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("exportExcel")) {
            
            try {
                
                String fileName = "NEFT Counter Party Wise Reconcilliation Report ";
                
                if (reportBean.getReportMap().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateReconcilliationReportCPwiseExportToExcel(response.getOutputStream());
                } else {
                    reportBean.setMessage("No records found for Exporting into Excel");
                }
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        }
        return mapping.findForward("input");
    }
}
