package com.objectfrontier.insta.rtgs.reports.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.insta.InstaULC;
import com.objectfrontier.insta.dto.DisplayValueReportDTO;
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;


/**
 * 
 * @author jinuj
 * @date   Oct 30, 2008
 * @since  insta.reports; Oct 30, 2008
 */
public class BrInwRejectedByUserlReportAction 
extends AbstractReportAction {

    private static Category logger = Category.getInstance(BrInwRejectedByUserlReportAction
                                                          .class);

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.vijb.report.brInwRejectedByUser";
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

        if (mode!=null && mode.equalsIgnoreCase("viewreport")) {

            try {

                reportBean.generateBrInwRejectedByUser(request);
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                logger.error(e.getMessage());
                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        } if (mode != null && mode.equalsIgnoreCase("input")) {
            /*
             *Set the Fields which are needed in the Input Page. 
             */
            setFieldValues(reportBean, true, true, true, false, true,
                           false, false, false, false, false, false, true);
            reportBean.loadInitial(request);
            setMsgSubType(reportBean, "ALL,R41,R42");
            reportBean.setPageForward("brInwRejectedByUser");
            return mapping.findForward("brInwRejectedByUser");
        } else if (mode != null && mode.equalsIgnoreCase("exportExcel")) {
            
            try {
                
                String fileName = "Branch Inward Rejected Report ";
                
                if (reportBean.getReportDTOs().size() > 0) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
                    reportBean.generateBrInwRejectedExportToExcel(response.getOutputStream());
                } else {
                    reportBean.setMessage("No records found for Exporting into Excel");
                }
                return mapping.findForward("viewreport");
            } catch (Exception e) {

                reportBean.setMessage(e.getMessage());
                reportBean.setMessageType(InstaULC.MSG_TYPE_INFO);
            }
        }
        return mapping.findForward("input");
    }
    
    /**
     * This method is to set the fields whihc are need to be display on the input page.
     * 
     */
    public void setFieldValues(InstaReportBean reportBean, boolean haveAmtFld, boolean haveBranchFld,
                                  boolean haveDateFld,boolean haveHostFld, boolean haveSubTypeFld,
                                  boolean haveStatusFld, boolean haveTranTypeFld, boolean haveUTRNoFld,
                                  boolean isInwardSpecific, boolean isOutwardSpecific,
                                  boolean haveCounterParty, boolean haveUserFld) {

        /*
         *Set the Fields which are needed in the Input Page. 
         */
        reportBean.setHaveAmountField(haveAmtFld);
        reportBean.setHaveBranchField(haveBranchFld);
        reportBean.setHaveDateField(haveDateFld);
        reportBean.setHaveHostTypeField(haveHostFld);
        reportBean.setHaveMsgSubTypeField(haveSubTypeFld);
        reportBean.setHaveStatusField(haveStatusFld);
        reportBean.setHaveTranTypeField(haveTranTypeFld);
        reportBean.setHaveUTRNoField(haveUTRNoFld);
        reportBean.setIsInwardSpecific(isInwardSpecific);
        reportBean.setIsOutwardSpecific(isOutwardSpecific);
        reportBean.setHaveCounterPartyFld(haveCounterParty);
        reportBean.setHaveUserField(haveUserFld);
    }
    
    /**
     * Set Message Sub Type Field.
     */

    public void setMsgSubType(InstaReportBean reportBean, String msgTypes) {
        DisplayValueReportDTO dto = new DisplayValueReportDTO();
        reportBean.setSubTypeList(new ArrayList<DisplayValueReportDTO>(0));

        if ( msgTypes.indexOf("ALL")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("ALL");
            dto.setDisplayValue("ALL Payments");
            reportBean.getSubTypeList().add(dto);
        }

        if ( msgTypes.indexOf("R41")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("R41");
            dto.setDisplayValue("Customer Payment");
            reportBean.getSubTypeList().add(dto);
        }

        if ( msgTypes.indexOf("R42")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("R42");
            dto.setDisplayValue("InterBank Payment");
            reportBean.getSubTypeList().add(dto);
        }

        if ( msgTypes.indexOf("R10")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("R10");
            dto.setDisplayValue("OATR Payment");
            reportBean.getSubTypeList().add(dto);
        }

        if ( msgTypes.indexOf("R43")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("R43");
            dto.setDisplayValue("Debit Notification");
            reportBean.getSubTypeList().add(dto);
        }

        if ( msgTypes.indexOf("R44")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("R44");
            dto.setDisplayValue("Credit Notification");
            reportBean.getSubTypeList().add(dto);
        }
    }

}
