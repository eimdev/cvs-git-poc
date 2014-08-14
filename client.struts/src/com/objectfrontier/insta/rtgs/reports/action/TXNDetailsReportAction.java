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
 * @date   Oct 30, 2008
 * @since  insta.reports; Oct 30, 2008
 */
public class TXNDetailsReportAction 
extends AbstractReportAction {

    private static Category logger = Category.getInstance(TXNDetailsReportAction.class);

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.vijb.report.tranDetailsReport";
        if (mode!=null && mode.equalsIgnoreCase("viewreport")) {
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

        if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {

                reportBean.generateIndividualTXNDetailsReport(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("exportExcel")) {
            
            try {
                
                String fileName = "Individual Transaction Detailed Report ";
                
                if (reportBean.getReportMap().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateIndividualTXNDetailsExportToExcel(response.getOutputStream());
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
