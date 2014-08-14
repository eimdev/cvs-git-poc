package com.objectfrontier.insta.rtgs.reports.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.insta.InstaULC;
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;

/**
 * 
 * @author jinuj
 * @date   Jul 28, 2008
 * @since  insta.reports; Jul 28, 2008
 */
public class RTGSReconcillationReportAction extends AbstractReportAction {

    private static Category logger = Category.getInstance(RTGSReconcillationReportAction.class.getName());

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.rtgs.report.reconcillation";
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
            //String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate()); //Commented by priyak for date format.
            reportBean.getReportDto().setValueDate(getBusinessDate());
            return mapping.findForward("input");    
        } else if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {

                reportBean.generateRTGSReconcillationReports(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("exportExcel")) {
            
            try {
                
                String fileName = "Reconciliation Report ";
                
                if (reportBean.getReportDTOs().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateRTGSReconcillationExportToExcel(response.getOutputStream());
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
