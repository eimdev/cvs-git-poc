/*
 * @(#)NEFTUTRNoWiseReportAction.java
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
import com.objectfrontier.insta.rtgs.reports.action.UTRNoWiseReportAction;



/**
 * @author mohanadevis
 * @date   Sep 16, 2009
 * @since  insta.reports; Sep 16, 2009
 */
public class NEFTUTRNoWiseReportAction 
extends AbstractReportAction {
    
    private static Category logger = Category.getInstance(NEFTUTRNoWiseReportAction.class);

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.vijb.report.neftUtrNoWiseReport";
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
        InstaNEFTReportBean neftBean = (InstaNEFTReportBean) form;

        if (neftBean==null) {
            neftBean = new InstaNEFTReportBean();
        }

        if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {

                neftBean.generateNEFTUTRNowiseReport(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                neftBean.setMessage(e.getMessage());
                neftBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        }
        return mapping.findForward("input");
    }
}
