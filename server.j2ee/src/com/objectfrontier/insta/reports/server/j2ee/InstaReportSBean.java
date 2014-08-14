/*
 * @(#)SUMASBean.java
 *
 * Copyright by ObjectFrontier, Inc.,
 * 2050 Marconi Drive, Alpharetta, GA 30005, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ObjectFrontier, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with ObjectFrontier.
 */
package com.objectfrontier.insta.reports.server.j2ee;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.objectfrontier.arch.dto.Message;
import com.objectfrontier.arch.server.j2ee.env.J2EEServerEnvironment;
import com.objectfrontier.insta.reports.InstaReportULC;
import com.objectfrontier.insta.reports.server.bo.InstaReportBODelegator;
import com.objectfrontier.insta.server.j2ee.InstaSBean;

/**
 * @author vEeRa. C
 * @date   Jan 16, 2006
 * @since  suma.app 1.0; Jan 16, 2006
 */
public class InstaReportSBean
extends InstaSBean {

	private static final long serialVersionUID  =200004L;
	private static Category logger = Category.getInstance(InstaReportSBean.class.getName());

    public InstaReportSBean()
    throws com.objectfrontier.arch.resource.ResourceException {
        super();
    }

    /**
     * create method according to the spec.
     * @throws RemoteException
     * @throws CreateException
     */
    public void ejbCreate()
    throws RemoteException, CreateException {
    }

    /**
     * @see com.objectfrontier.arch.server.j2ee.bean.AbstractSessionBean#getJ2EEServerEnvironment()
     */
    protected J2EEServerEnvironment getJ2EEServerEnvironment() {

        J2EEServerEnvironment env = (J2EEServerEnvironment)super.getJ2EEServerEnvironment().getModule(InstaReportULC.MODULE_NAME);
        return (env == null) ? super.getJ2EEServerEnvironment() : env;
    }

    protected InstaReportBODelegator getBODelegator() {
        return (InstaReportBODelegator)getBO(InstaReportBODelegator.class);
    }

    /**
     * @see com.objectfrontier.aml.server.j2ee.InstaReportSRemote#handle(com.objectfrontier.arch.dto.Message)
     */
    public Message handle(Message message)
    throws RemoteException, EJBException {

    	InstaReportBODelegator bo = null;
        Message result;
        try {

            bo = getBODelegator();
            result = bo.handle(message);
        } catch (Exception e) {
            logger.info("Exception Occurred in the Handle Method. " + e.getMessage());
            sessionContext.setRollbackOnly();
            throw new java.rmi.ServerException(e.getMessage(), e);
         } finally {
            if (bo != null) bo.cleanup();
        }
        return result;
    }
}