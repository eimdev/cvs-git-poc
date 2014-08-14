package com.objectfrontier.insta.rtgs.reports.action;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.arch.client.jsp.env.JSPClientEnvironment;
import com.objectfrontier.insta.InstaDefaultConstants;
import com.objectfrontier.insta.client.struts.action.InstaAction;
import com.objectfrontier.insta.client.struts.bean.InstaWorkFlowBean;
import com.objectfrontier.insta.neft.client.struts.bean.InstaNEFTMessageBean;
import com.objectfrontier.insta.reports.InstaReportULC;

/**
 * 
 * @author jinuj
 * @date   Jul 28, 2008
 * @since  insta.reports; Jul 28, 2008
 */

public class AbstractReportAction
extends InstaAction {

    String module = "reports";

    /**
     * 
     * @see com.objectfrontier.insta.client.struts.action.InstaAction#exec(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward exec(ActionMapping mapping,
                              ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response)
    throws Exception {
        
        //To load the Message Count of RTGS Messages.
        InstaWorkFlowBean bean = new InstaWorkFlowBean();
        request.setAttribute("MESSAGECOUNT", bean.getMessagesCount());
        //To load the Message Count of NEFT Messages.
        if(InstaDefaultConstants.BANKNAME.equalsIgnoreCase("VIJB") && 
            InstaDefaultConstants.NEFT_ENABLED.equalsIgnoreCase("1")) {
            
            InstaNEFTMessageBean neftBean = new InstaNEFTMessageBean();
            request.setAttribute("NEFTMESSAGECOUNT", neftBean.getMessagesCount());
        }
        return mapping.findForward("success");
    }

    @Override
    protected String getActionName(String mode) {
        return "insta.reports.welcome";
    }

    /**
     * @see com.objectfrontier.insta.client.struts.action.InstaAction#getJSPClientEnvironment()
     */
    @Override
    protected JSPClientEnvironment getJSPClientEnvironment() {

        JSPClientEnvironment env = (JSPClientEnvironment)super.getJSPClientEnvironment().getModule(InstaReportULC.MODULE_NAME);
        return (env == null) ? super.getJSPClientEnvironment() : env;
    }
}
