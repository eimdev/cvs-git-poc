/*
 * @(#)NEFTReturnPaymentRejectedReportAction.java
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
 * @date   Sep 29, 2009
 * @since  insta.reports; Sep 29, 2009
 */
public class NEFTReturnPaymentRejectedReportAction 
extends AbstractReportAction {
    
    private static Category logger = Category.getInstance(NEFTReturnPaymentRejectedReportAction.class.getName());
    
    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.vijb.neft.report.exceptions";
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
    
                reportBean.generateNEFTReturnPaymentRejectedReport(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {
    
                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("input")) {
            
            reportBean.resetInput();
            setFieldValues(reportBean, true,true,true,false,false,false,false,false,
                           false,false,false,true,false,false,false,false,false);
            reportBean.reportName = "neftReturnPaymentRejectedReport";
            reportBean.loadInitial(request);
            reportBean.setPageForward("neftReturnPaymentRejectedReport");
            return mapping.findForward("neftReturnPaymentRejectedReport");
        } else if (mode != null && mode.equalsIgnoreCase("exportexcel")) {
            
            try {
                
                String fileName = "NEFT Return Payment Rejected By User Report";
                
                if (reportBean.getReportDTOs().size() > 0) {
                    
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.neftReturnPaymentRejectedReportExportToExcel(response.getOutputStream());      
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

    public void setFieldValues(InstaNEFTReportBean reportBean, boolean haveAmtFld, boolean haveBranchFld,
                               boolean haveDateFld,boolean haveHostFld, boolean haveSubTypeFld,
                               boolean haveStatusFld, boolean haveTranTypeFld, boolean haveUTRNoFld,
                               boolean isInwardSpecific, boolean isOutwardSpecific,
                               boolean haveCounterParty, boolean haveUserFld,boolean haveBatchTime,
                               boolean haveRepTypeFld, boolean haveRepdateFld, boolean haveInwtype,boolean haveFutureDateTxnStatus) {

         /*
          *Set the Fields which are needed in the Input Page
          */
         
         reportBean.setHaveAmountField(haveAmtFld);
         reportBean.setHaveBranchField(haveBranchFld);
         reportBean.setHaveDateField(haveDateFld);
         reportBean.setHaveHostTypeField(haveHostFld);
         reportBean.setHaveMsgSubTypeField(haveSubTypeFld);
         reportBean.setHaveStatusField(haveStatusFld);
         reportBean.setHaveTranTypeField(haveTranTypeFld);
         reportBean.setHaveUTRNoField(haveUTRNoFld);
         reportBean.setIsInwardSpecific(isInwardSpecific);
         reportBean.setIsOutwardSpecific(isOutwardSpecific);
         reportBean.setHaveCounterPartyFld(haveCounterParty);
         reportBean.setHaveUserField(haveUserFld);
         reportBean.setHaveBatchTimeField(haveBatchTime);
         reportBean.setHaveReportTypeField(haveRepTypeFld);
         reportBean.setHaveValueDateField(haveRepdateFld);
         reportBean.setHaveInwardTypeField(haveInwtype);
         reportBean.setHaveFutureDateTxnStatus(haveFutureDateTxnStatus);
    }
}
