/*
 * @(#)NEFTRTGSNetSettlementAction.java
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
import com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction;
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;



/**
 * @author kanagarajanm
 * @date   Mar 16, 2009
 * @since  insta.reports; Mar 16, 2009
 */
public class NEFTRTGSNetSettlementAction 
extends AbstractReportAction {
    
    private static Category logger = Category.getInstance("neftLogger");
    
    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.vijb.neft.report.neftRtgsNetSettlement";
        if (mode!=null && mode.equalsIgnoreCase("input")) {
            actionName += ".input";
        } else if (mode!=null && mode.equalsIgnoreCase("viewreport")) {
            actionName += ".view";
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
       if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

           try {

               reportBean.generateNeftRtgsNetSettlementReport(request);
               return mapping.findForward("view");
           } catch (Exception e) {

               logger.error(e.getMessage());
               reportBean.setMessage(e.getMessage());
               reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
           }
       } else if (mode != null && mode.equalsIgnoreCase("exportexcel")) {
           
           try {
               
               String fileName = "NEFT_RTGS_NetSettlement Report";
               
               if(reportBean.getNEFTRTGS_settlementList().size() > 0 || 
                  (reportBean.getNEFTRTGS_settlementMap().size() > 0)) {
               
                   response.setContentType("application/vnd.ms-excel");
                   response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                   reportBean.NEFTRTGS_NetSettlementExportToExcel(response.getOutputStream());
               } else {
                   reportBean.setMessage("No records found for Exporting into Excel");
               }
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
