package com.objectfrontier.insta.lcbg.reports.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.insta.InstaULC;
import com.objectfrontier.insta.lcbg.reports.bean.InstaLCBGReportBean;
import com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction;

/**
 *
 * @author joeam
 * @date   Sep 25, 2012
 * @since  insta.reports; Sep 25, 2012
 */

public class LCBGOutwardSummaryReportAction
extends AbstractReportAction {

        private static Category logger = Category.getInstance(LCBGOutwardSummaryReportAction.class.getName());

        @Override
        protected String getActionName(String mode) {

            String actionName = "insta.neft.report.inwardsummary";
            if (mode!=null && mode.equalsIgnoreCase("viewreport")) {
                actionName += ".viewreport";
            }
            return actionName;
        }

        @Override
        public ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

            String mode = request.getParameter("mode");
            InstaLCBGReportBean reportBean = (InstaLCBGReportBean) form;
            if (reportBean==null) {
                reportBean = new InstaLCBGReportBean();
            }
            if (mode!=null && mode.equalsIgnoreCase("outwardSummaryReportView")) {

                try {

                    reportBean.generateLCBGInwardReport(request);
                    reportBean.setReportTitle(reportBean.getInwSummaryReport());
                    return mapping.findForward("outwardSummaryReportView");
                } catch (Exception e) {

                    logger.error(e.getMessage());
                    reportBean.setMessage(e.getMessage());
                    reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
                }
            }
            return mapping.findForward("input");
        }

}
