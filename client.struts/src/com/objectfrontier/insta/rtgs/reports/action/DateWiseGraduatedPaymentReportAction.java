package com.objectfrontier.insta.rtgs.reports.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.insta.InstaULC;
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;

/**
 * @author Gnanasekarang
 * @date   Sep 19, 2008
 * @since  insta.reports; Sep 19, 2008
 */
public class DateWiseGraduatedPaymentReportAction 
extends AbstractReportAction {

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.rtgs.report.datewisegpr";
        if (mode!=null && mode.equalsIgnoreCase("list")) {

            actionName += ".list";
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
            //String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
            reportBean.getReportDto().setValueDate(getBusinessDate());
            return mapping.findForward("input");    
        } else if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {
                
                reportBean.generateGraduatedPaymentReport(request, true);
                return mapping.findForward("list");    
            } catch (Exception e) {

                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
                return mapping.findForward("input");
            }
        }  else if (mode != null && mode.equalsIgnoreCase("exportExcel")) {
            
            try {
                
                String fileName = "Graduated Payment Report";
                
                if (reportBean.getReportDTOs().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateGraduatedPaymentExportToExcel(response.getOutputStream());
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
