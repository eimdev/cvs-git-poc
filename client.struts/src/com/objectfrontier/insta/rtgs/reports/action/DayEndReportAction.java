/*
 * Created on Jul 8, 2008
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
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;

/**
 * 
 * @author sasmitap
 * @date   Oct 17, 2008
 * @since  insta.reports; Oct 17, 2008
 */
public class DayEndReportAction
extends AbstractReportAction {
    
    private static Category logger = Category.getInstance(DayEndReportAction.class.getName());
    
    @Override
    protected String getActionName(String mode) {
        String actionName = "insta.rtgs.report.DayEnd";
        if (mode!=null && mode.equalsIgnoreCase("inward")) {
            actionName += ".inward";
        } else if (mode!=null && mode.equalsIgnoreCase("outward")) {
            actionName += ".outward";
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
      if (("inward").equalsIgnoreCase(mode)
         && ("submit").equalsIgnoreCase(action)) {
         try {
                reportBean.resetData();
                String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
                reportBean.getNewReportDto().setValueDate(appDate);
                reportBean.generateInwardDayEndReport(request);
                return mapping.findForward("forwardInward");
          } catch (Exception e) {
            logger.error(e.getMessage());
            reportBean.setMessage(e.getMessage());
            reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            return mapping.findForward("failureInward");
          }
        }
        else if (("outward").equalsIgnoreCase(mode)
        && ("submit").equalsIgnoreCase(action)){
            try {
                reportBean.resetData();
                String appDate = InstaReportUtil.reportDisplayDateFormat(getBusinessDate());
                reportBean.getNewReportDto().setValueDate(appDate);
                reportBean.generateOutwardDayEndReport(request);
                return mapping.findForward("forwardOutward");
              } catch (Exception e) {
                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
                return mapping.findForward("failureOutward");
              }
         }
       return mapping.findForward("failure");
    }
 }

