package com.objectfrontier.insta.rtgs.reports.bean;

import com.objectfrontier.arch.client.jsp.env.JSPClientEnvironment;
import com.objectfrontier.insta.client.si.InstaSI;
import com.objectfrontier.insta.client.struts.bean.InstaBean;
import com.objectfrontier.insta.reports.InstaReportULC;


/**
 * 
 * @author jinuj
 * @date   Jul 29, 2008
 * @since  insta.reports; Jul 29, 2008
 */
public class AbstractReportBean
extends InstaBean {

    /**
     * 
     * @see com.objectfrontier.insta.client.struts.bean.InstaBean#getServiceName()
     */
    protected String getServiceName() {
        return InstaReportULC.MODULE_NAME;
    }

    /**
     * 
     * @see com.objectfrontier.insta.client.struts.bean.InstaBean#getSI(java.lang.Object, boolean)
     */
    protected InstaSI getSI(Object id, boolean create) {

        return (InstaSI)getStatefulServer(id, create).
                       getServiceServer(getServiceName(), create);
    }

    /**
     * 
     * @see com.objectfrontier.arch.client.struts.bean.DefaultFormBean#getJSPClientEnvironment()
     */
    public JSPClientEnvironment getJSPClientEnvironment() {

        JSPClientEnvironment env = (JSPClientEnvironment)super.getJSPClientEnvironment().getModule(InstaReportULC.MODULE_NAME);
        return (env == null) ? super.getJSPClientEnvironment() : env;
    }

    /**
     * 
     */
    
}
