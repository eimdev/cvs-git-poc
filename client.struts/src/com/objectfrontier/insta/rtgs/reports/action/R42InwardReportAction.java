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
public class R42InwardReportAction extends AbstractReportAction {

    private static Category logger = Category.getInstance(R42InwardReportAction.class.getName());

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.rtgs.report.R42Inward";
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
        String msgSubType = request.getParameter("msgSubType");
        String tranType = request.getParameter("tranType");
        String groupBy = request.getParameter("groupby");
        InstaReportBean reportBean = (InstaReportBean) form;
        if (reportBean==null) {
            reportBean = new InstaReportBean();
        }

        if (mode!=null && mode.equalsIgnoreCase("input")) {

            reportBean.resetBean();
            /*
             *Set the Fields which are needed in the Input Page. 
             */
            reportBean.setHaveAmountField(false);
            reportBean.setHaveBranchField(true);
            reportBean.setHaveDateField(false);
            reportBean.setHaveHostTypeField(false);
            reportBean.setHaveMsgSubTypeField(false);
            reportBean.setHaveStatusField(false);
            reportBean.setHaveTranTypeField(false);
            reportBean.setHaveUTRNoField(false);
            reportBean.setIsInwardSpecific(false);
            reportBean.setIsOutwardSpecific(false);

            /*
             *     This method is to load the initial data which are required
             * for loading the report input page.
             */
            reportBean.loadInitial(request);
            //Setting the input values based on the link they clicked
            reportBean.isDateOnly=0;
            reportBean.getReportDto().setPaymentType(msgSubType);
            reportBean.getReportDto().setTransactionType(tranType);
            reportBean.getReportDto().setGroupBy(groupBy);
            //String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
            reportBean.getReportDto().setValueDate(getBusinessDate());
            return mapping.findForward("input");    
        } else if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {

                reportBean.generateR42InwardReports(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode != null && mode.equalsIgnoreCase("exportExcel")) {
            
            try {
                
                String fileName = "R42(IPN) Received ";
                
                if (reportBean.getReportDTOs().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateR42InwardExportToExcel(response.getOutputStream());
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
