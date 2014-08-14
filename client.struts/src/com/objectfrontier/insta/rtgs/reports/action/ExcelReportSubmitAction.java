/*
 * Created on Jul 7, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.objectfrontier.insta.rtgs.reports.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.arch.context.RequestContext;
import com.objectfrontier.insta.InstaULC;
import com.objectfrontier.insta.reports.InstaReportUtil;
import com.objectfrontier.insta.reports.dto.ReportDTO;
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;

/**
 *@author sasmitap
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExcelReportSubmitAction
extends AbstractReportAction{
    
    private static Category logger = Category.getInstance(ExcelReportSubmitAction.class.getName());
    
    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.rtgs.report.excel";
        if (mode!=null && mode.equalsIgnoreCase("controller")) {
            actionName += ".controller";
        } 
        return actionName;
    }
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
           
        
        String mode = request.getParameter("mode");
        String action = request.getParameter("action");
        RequestContext.setPageContext(request);

        InstaReportBean reportBean = (InstaReportBean) form;
        if (reportBean==null) {
            reportBean = new InstaReportBean();
        }
        ReportDTO newReportDto = new ReportDTO();
        if (("controller").equalsIgnoreCase(mode)
             && ("input").equalsIgnoreCase(action)) {
                     
         try {
            //reportBean.resetBean();
             reportBean.resetAll();
             reportBean.setListOfTranType(null);
             reportBean.subTypeMap.clear();
             reportBean.setListOfTranType(reportBean.getListOfTranType());
             reportBean.setTranTypeList(reportBean.getTransactionListForExcel());
             reportBean.setSubTypeMap(reportBean.getMsgSubTypeMapForExcel());
             reportBean.setNewReportDto(newReportDto);
             String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
             reportBean.getNewReportDto().setValueDate(appDate);
             return mapping.findForward("controllerInput");
   
         } catch (Exception e) {
             
             logger.error(e.getMessage());
             reportBean.setMessage(e.getMessage());
             reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
             return mapping.findForward("controllerFailure");
          }
        } else if (("controller").equalsIgnoreCase(mode)
        && ("submit").equalsIgnoreCase(action)) {
         try {
             reportBean.generateExcelSheetReport(request);
             String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
             reportBean.getNewReportDto().setValueDate(appDate); 
           
            return mapping.findForward("excelSubmit");
            }  catch (Exception e) {
            logger.error(e.getMessage());
            reportBean.setMessage(e.getMessage());
            reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            return mapping.findForward("controllerFailure");
            }
       } else if (("controller").equalsIgnoreCase(mode)
       && ("reset").equalsIgnoreCase(action)) {
        try {
           reportBean.resetAll();
           return mapping.findForward("controllerInput");
           } catch (Exception e) {
           logger.error(e.getMessage());
           reportBean.setMessage(e.getMessage());
           reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
           return mapping.findForward("controllerFailure");
           }
       }
        return mapping.findForward("controllerFailure");
   }
}