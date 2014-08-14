/*
 * @(#)EventsAlertsReportAction.java
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
package com.objectfrontier.insta.rtgs.reports.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.insta.InstaULC;
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;



/**
 * @author mohanadevis
 * @date   Jun 1, 2009
 * @since  insta.reports; Jun 1, 2009
 */
public class EventsAlertsReportAction
extends AbstractReportAction {
    
    private static Category logger = Category.getInstance(EventsAlertsReportAction
                                                          .class);

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.vijb.report.eventalert";
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
        InstaReportBean reportBean = (InstaReportBean) form;

        if (reportBean==null) {
            reportBean = new InstaReportBean();
        }
        
        if (mode!=null && mode.equalsIgnoreCase("select")) {

            try {
                
                reportBean.getReportDto().setIfscId(0);
                return mapping.findForward("select");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode!=null && mode.equalsIgnoreCase("input")) {

            try {
                
                reportBean.resetBean();
                if (reportBean.reportEvent.equalsIgnoreCase("AuditLog")) {
                    
                    setInputParams(reportBean,true,true,false,true,false,false);
                } else if (reportBean.reportEvent.equalsIgnoreCase("Email")) {
                    setInputParams(reportBean,true,true,true,false,true,true);
                } else if (reportBean.reportEvent.equalsIgnoreCase("IDL")) {
                    setInputParams(reportBean,true,false,false,false,false,false);
                } else if (reportBean.reportEvent.equalsIgnoreCase("SystemEvent")) {
                    setInputParams(reportBean, true, false, false, false, false, false); //Code added for System level events by Mohana on 25-Sep-2009
                }
                reportBean.loadInputParameters(request);
                return mapping.findForward("input");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } else if (mode!=null && mode.equalsIgnoreCase("refresh")) {

            try {
                
                if (reportBean.reportEvent.equalsIgnoreCase("AuditLog")) {
                    
                    setInputParams(reportBean,true,true,false,true,false,false);
                } else if (reportBean.reportEvent.equalsIgnoreCase("Email")) {
                    setInputParams(reportBean,true,true,true,false,true,true);
                } else if (reportBean.reportEvent.equalsIgnoreCase("IDL")) {
                    setInputParams(reportBean,true,false,false,false,false,false);
                } else if (reportBean.reportEvent.equalsIgnoreCase("SystemEvent")) {
                    setInputParams(reportBean, true, false, false, false, false, false); //Code added for System level events by Mohana on 25-Sep-2009
                }
                reportBean.loadInputParameters(request);
                return mapping.findForward("input");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            } 
        } else if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {
                
                if (reportBean.reportEvent.equalsIgnoreCase("AuditLog")) {
                    reportBean.generateUserLevelEventReport(request);
                    return mapping.findForward("viewauditreport");
                } else if (reportBean.reportEvent.equalsIgnoreCase("Email")) {
                    reportBean.validateInput();
                    reportBean.generateEmailInfoReport(request);
                    return mapping.findForward("viewemailreport");
                } else if (reportBean.reportEvent.equalsIgnoreCase("IDL")) {
                    reportBean.generateIDLUtilizationReport(request);
                    return mapping.findForward("viewidlreport");
                } else if (reportBean.reportEvent.equalsIgnoreCase("SystemEvent")) { //Code added for System level events by Mohana on 25-Sep-2009
                    reportBean.generateSystemLevelEventsReport(request);
                    return mapping.findForward("viewSystemEventreport");
                }
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        }  else if (mode!=null && mode.equalsIgnoreCase("setValue")) {

            try {
                
                reportBean.setMsgTypeField();
                return mapping.findForward("input");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } 
        return mapping.findForward("input");
    }
    
    /**
     *Method to set input params for report page  
     */
    public void setInputParams(InstaReportBean reportBean,boolean date,boolean branch,boolean tranType,boolean user,boolean channel,boolean msgType) {
        
        
        reportBean.setHaveBranchField(branch);
        reportBean.setHaveDateField(date);
        reportBean.setHaveTranTypeField(tranType);
        reportBean.setHaveUserField(user);
        reportBean.setHaveMsgSubTypeField(msgType);
        reportBean.setHaveChannelField(channel);
    }

    
}


