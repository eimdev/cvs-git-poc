package com.objectfrontier.insta.lcbg.reports.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.crud.CrudException;
import com.objectfrontier.insta.lcbg.reports.bean.InstaLCBGReportBean;
import com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction;

/**
 *
 * @author joeam
 * @date   Sep 25, 2012
 * @since  insta.reports; Sep 25, 2012
 */

public class LCBGReportInputAction
extends AbstractReportAction {

    private static Category logger = Category.getInstance(LCBGReportInputAction.class.getName());
    public enum Operator {LcbgInwSummaryReport, LcbgOutwSummaryReport}

    public ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {

        String mode = request.getParameter("mode");
        String report = request.getParameter("report");
        String canReset = request.getParameter("canReset");

        boolean flag    = true;

        if(canReset != null) {
            flag = false;
        }

        if(report == null){
            report = "";
        }
        InstaLCBGReportBean reportBean = (InstaLCBGReportBean) form;
        if (reportBean==null) {
            reportBean = new InstaLCBGReportBean();
        }
        reportBean.setReport(report);
        try {

            if (mode!=null && mode.equalsIgnoreCase("input")) {

                if (report==null || report.trim().equalsIgnoreCase("")) {
                    throw new CrudException("Input Value is Empty or Null.");
                }

                /*
                 * To Enable the Branch Display.
                 */
                reportBean.disableBranchDisplay=0;

                /*
                 *   Based on the reports selected. Operator has to be created and load
                 * the initial values for the input page.
                 */
                Operator oper = Operator.valueOf(report);

                switch (oper) {

                    //For LCBG Reports
                    case LcbgInwSummaryReport :

                        reportBean.resetBean();
                        reportBean.setTranType("inward");
                        //Added to get the report name to check.
                        reportBean.reportName = report;
                        reportBean.setDateFormat("");
                        /*
                         *Set the Fields which are needed in the Input Page.
                         */
                        //setFieldValues1(reportBean, true, true, true, true, true);
                        setFieldValues(reportBean, true, false, true, true, true,
                                 true, true, false, true, false, false, false,
                                 false, false, false, false, false);
                        // for display the bank name in list box.
                        reportBean.setHaveBankListField(true);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.setHaveBranchField(true);
                        reportBean.loadInitial(request);
                        reportBean.setHaveBranchField(false);
                        reportBean.setPageForward("inwardLCBGReportInput");
                        return mapping.findForward("inwardLCBGReportInput");

                    case LcbgOutwSummaryReport:
                        reportBean.resetBean();
                        reportBean.setTranType("outward");
                        //Added to get the report name to check.
                        reportBean.reportName = report;
                        reportBean.setDateFormat("");
                        /*
                         *Set the Fields which are needed in the Input Page.
                         */
                        //setFieldValues1(reportBean, true, true, true, true, true);
                        setFieldValues(reportBean, true, false, true, true, true,
                                 true, true, false, true, false, false, false,
                                 false, false, false, false, false);
                        // for display the bank name in list box.
                        reportBean.setHaveBankListField(true);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.setHaveBranchField(true);
                        reportBean.loadInitial(request);
                        reportBean.setHaveBranchField(false);
                        reportBean.setPageForward("outwardLCBGReportInput");
                        return mapping.findForward("outwardLCBGReportInput");


                 }
             }
        }
        catch(Exception e) {

            logger.error("Exception Occurred while Loading the Input Value for NEFT Report. : " +
                         e.getMessage());
            //saveErrors(reportBean, e);
        }
        return mapping.findForward("input");
    }

    /**
     * This method is to set the fields whihc are need to be display on the input page.
     *
     */
    public void setFieldValues(InstaLCBGReportBean reportBean, boolean haveAmtFld, boolean haveBranchFld,
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
