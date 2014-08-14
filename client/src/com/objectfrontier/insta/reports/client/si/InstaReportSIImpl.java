package com.objectfrontier.insta.reports.client.si;

import com.objectfrontier.arch.env.ApplicationEnvironment;
import com.objectfrontier.insta.client.si.InstaSIImpl;
import com.objectfrontier.insta.reports.InstaReportULC;
import com.objectfrontier.insta.reports.server.j2ee.InstaReportSHome;


/**
 * 
 * @author jinuj
 * @date   Jul 28, 2008
 * @since  insta.reports; Jul 28, 2008
 */
public class InstaReportSIImpl
extends InstaSIImpl 
implements InstaReportSI {

    public static final String LogSourceClass = InstaReportSIImpl.class.getName();

    public InstaReportSIImpl() {
        super();
    }

    /**
     * @see com.objectfrontier.arch.server.ServiceServer#getServiceName()
     */
    public String getServiceName() {
        return InstaReportULC.MODULE_NAME;
    }

    /**
     * @see com.objectfrontier.ofmc.client.si.OFMCSIImpl#getEnvironment()
     */
    protected ApplicationEnvironment getEnvironment() {
        ApplicationEnvironment env = super.getEnvironment().getModule(InstaReportULC.MODULE_NAME);
        return (env == null) ? super.getEnvironment() : env;
    }

    /**
     * @see com.objectfrontier.arch.server.manager.DefaultServiceServer#getSubsystem()
     */
    protected String getSubsystem() {
        return InstaReportSI.LoggerSubsystem;
    }

    /**
     * @see com.objectfrontier.ofmc.client.si.OFMCSIImpl#getSHome()
     */
    protected Class getSHome() {
        return InstaReportSHome.class;
    }
}