/* $Header$ */

/*
 * @(#)AbstractMessageSBean.java
 *
 * Copyright by ObjectFrontier, Inc.,
 * 12225 Broadleaf Lane, Alpharetta, GA 30005, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ObjectFrontier, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with ObjectFrontier.
 */

package com.objectfrontier.insta.rtgs.reports.bean;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.objectfrontier.arch.client.jsp.bean.BeanException;
import com.objectfrontier.arch.context.RequestContext;
import com.objectfrontier.arch.resource.ResourceException;
import com.objectfrontier.arch.server.StatefulServer;
import com.objectfrontier.arch.service.acl.AccessControl;
import com.objectfrontier.arch.service.exception.ExceptionHandler;
import com.objectfrontier.arch.service.logging.Level;
import com.objectfrontier.rtgs.client.jsp.util.RHSJSPConstants;
import com.objectfrontier.user.client.vo.UserValueObject;
import com.objectfrontier.user.dto.UserDTO;
import com.objectfrontier.user.ws.dto.WSUserDTO;

/**
 * 
 * @author sudharanip
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class AbstractMsgDefnBean 
extends AbstractReportBean {
    private static final long serialVersionUID = -685163976325493302L;

    /**
     * This method transfer the errormessages from the list
     * to that of ActionErrors for the property name specified
     *
     * @param errMessages list of errormessages
     * @param errors ActionErrors
     * @param property which has the error
     * @param displayName displayname of the property
     */
    protected void prepareErrors(List errMessages, ActionErrors errors,
        String property, String displayName) {

        for (int i = 0; i < errMessages.size(); i++) {
            errors.add(property,
                new ActionMessage(RHSJSPConstants.ERROR_MESSAGE_KEY,
                    displayName + " - " + errMessages.get(i)));
        }
    }

    /**
     * This methos return the current loggged user.
     * @return userDTO if user loggged in else null
     * @throws BeanException
     */
    public UserDTO getCurrentUser() throws BeanException {
        
        final String LogSourceMethod = "getCurrentUser()";
        
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest(LogSourceClass, LogSourceMethod, ">>Start");            
        }
        
        UserDTO userDTO = null;
        try {
        
            HttpServletRequest request = (HttpServletRequest) RequestContext.getPageContext();
                    
            StatefulServer server = getJSPClientEnvironment()
                                    .getStatefulServer(request.getSession().getId(), true);
    
            if (server != null) {
                
                AccessControl accessControl = server.getAccessControl();
                if (accessControl != null) {
                        
                     WSUserDTO wsUserDTO = (WSUserDTO)accessControl.getUser();
                     
                     userDTO  = new UserDTO();
                     userDTO.userVO = new UserValueObject();
                     userDTO.accesses = wsUserDTO.getAccesses();
                     userDTO.userVO.id = wsUserDTO.getUserVO().getId();
                     userDTO.userVO.name = wsUserDTO.getUserVO().getName();
                     userDTO.userVO.description = wsUserDTO.getUserVO().getDescription();
                     userDTO.userVO.password = wsUserDTO.getUserVO().getPassword();
                     userDTO.userVO.active  = wsUserDTO.getUserVO().getActive();
                     userDTO.userVO.failedAttempts = wsUserDTO.getUserVO().getFailedAttempts();
                }
            }
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest(LogSourceClass, LogSourceMethod, 
                    "<<End UserDTO : " + userDTO);
            }
        } catch (Exception e) {
            logger.severe(LogSourceClass, LogSourceMethod, ">>" + e);
            throw new BeanException("BeanException:" + e.getMessage());
        }
        return userDTO;
    }

    /**
     * Method to get the exception handler using environment
     * @return
     * @throws ResourceException
     */
    public ExceptionHandler getExceptionHandler() 
    throws ResourceException {
        
        return getJSPClientEnvironment().getExceptionHandler();
    }
}

