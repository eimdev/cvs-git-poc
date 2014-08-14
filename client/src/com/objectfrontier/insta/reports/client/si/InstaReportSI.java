package com.objectfrontier.insta.reports.client.si;

import com.objectfrontier.arch.dto.Message;
import com.objectfrontier.arch.server.ServiceServer;
import com.objectfrontier.insta.reports.InstaReportULC;

/**
 * 
 * @author jinuj
 * @date   Jul 14, 2008
 * @since  insta.workflow; Jul 14, 2008
 */
public interface InstaReportSI
extends ServiceServer {

    String ServiceName = InstaReportULC.MODULE_NAME;

    Message handle(Message msg);
}