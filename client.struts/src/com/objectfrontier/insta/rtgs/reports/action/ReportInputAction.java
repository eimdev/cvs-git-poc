package com.objectfrontier.insta.rtgs.reports.action;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.crud.CrudException;
import com.objectfrontier.insta.dto.DisplayValueReportDTO;
import com.objectfrontier.insta.rtgs.reports.bean.InstaReportBean;

/**
 * 
 * @author jinuj
 * @date   Jul 28, 2008
 * @since  insta.reports; Jul 28, 2008
 */
public class ReportInputAction extends AbstractReportAction {

    private static Category logger = Category.getInstance(ReportInputAction.class.getName());

    public enum Operator {BranchIndividualReport, BranchSummaryReport, InwardReturnedReport,
                         OutwardReturnedByReceiver, InwardBankwiseReport, UTRNumberwiseReport,
                         CPWiseReconcilliationReport, BrWiseReconcilliationReport, InwardRejectedByUser,
                         OutwardBankwiseReport, TransactionDetailsReport, BrSubTypeIndividual, OwReturnedReport,
                         futureDatedTxns, exceptionReport, returnPaymentRejectedReport, unsuccessfulPayment }

    @Override
    protected String getActionName(String mode) {

        String actionName = "insta.rtgs.report.inwardReturn";
        if (mode!=null && mode.equalsIgnoreCase("input")) {
            actionName += ".input";
        } else if (mode!=null && mode.equalsIgnoreCase("viewreport")) {
            actionName += ".viewreport";
        } else if (mode!=null && mode.equalsIgnoreCase("branchList")) {
            actionName += ".branchList";
        }
        return actionName;
    }

    @Override
    public ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {

        String mode = request.getParameter("mode");
        String report = request.getParameter("report");
        InstaReportBean reportBean = (InstaReportBean) form;
        if (reportBean==null) {
            reportBean = new InstaReportBean();
        }
        reportBean.setReport(report);
        try {

            if (mode!=null && mode.equalsIgnoreCase("input")) {

                if (report==null || report.trim().equalsIgnoreCase("")) { 
                    throw new CrudException("Input Value is Empty or Null.");
                }

                /*
                 * To Enable the Branch Display.
                 */
                reportBean.disableBranchDisplay=0;

                /*
                 *   Based on the reports selected. Operator has to be created and load
                 * the initial values for the input page. 
                 */
                Operator oper = Operator.valueOf(report);

                switch (oper) {

                    case TransactionDetailsReport :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, true, true,
                                       true, true, false, false, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        reportBean.setPageForward("tranDetailsReport");
                        return mapping.findForward("tranDetailsReport");

                    case BranchIndividualReport :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, true, true,
                                       true, true, false, false, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        reportBean.setPageForward("brIndReport");
                        return mapping.findForward("brIndReport");

                    case BranchSummaryReport :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, true, true,
                                       true, true, false, false, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        reportBean.setPageForward("brSummReport");
                        return mapping.findForward("brSummReport");


                    case InwardReturnedReport :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, false, true, true, false, true,
                                       false, false, false, false, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        setMsgSubType(reportBean, "ALL,R41,R42");
                        reportBean.setPageForward("brInwardReturned");
                        return mapping.findForward("brInwardReturned");


                    case OutwardReturnedByReceiver :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, false, true,
                                       false, false, false, false, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        reportBean.setPageForward("brOutwardReturned");
                        return mapping.findForward("brOutwardReturned");


                    case InwardBankwiseReport :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, false, true,
                                       false, false, false, true, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        //This is to list the Message Types.
                        setMsgSubType(reportBean, "ALL,R41,R42,R43,R44");
                        reportBean.setPageForward("brInwardBankwise");
                        return mapping.findForward("brInwardBankwise");

                        
                    case OutwardBankwiseReport :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, false, true,
                                       false, false, false, false, true, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        //This is to list the Message Types.
                        setMsgSubType(reportBean, "ALL,R41,R42,R10");
                        reportBean.setPageForward("brOutwardBankwise");
                        return mapping.findForward("brOutwardBankwise");


                    case UTRNumberwiseReport :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, false, true, false, false, false,
                                       false, false, true, false, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);

                        /*
                         * To Disable the Branch Display.
                         */
                        reportBean.disableBranchDisplay=1;

                        reportBean.setPageForward("utrNoWise");
                        return mapping.findForward("utrNoWise");


                    case CPWiseReconcilliationReport :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, false, true, false, true,
                                       false, true, false, false, false, true, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        //This is to list the Message Types.
                        setMsgSubType(reportBean, "ALL,R41,R42,R10,R43,R44");
                        reportBean.setPageForward("cpWiseReconcilliation");
                        return mapping.findForward("cpWiseReconcilliation");


                    case BrWiseReconcilliationReport :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, false, true,
                                       false, true, false, false, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        //This is to list the Message Types.
                        setMsgSubType(reportBean, "ALL,R41,R42,R10,R43,R44");
                        reportBean.setPageForward("brWiseReconcilliation");
                        return mapping.findForward("brWiseReconcilliation");


                    case InwardRejectedByUser :

                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, false, true,
                                       false, false, false, false, false, false, true, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        setMsgSubType(reportBean, "ALL,R41,R42");
                        reportBean.setPageForward("brInwRejectedByUser");
                        return mapping.findForward("brInwRejectedByUser");
                        
                    case BrSubTypeIndividual :
                        
                        reportBean.resetBean();
                        reportBean.setReport("BrSubTypeIndividual");
                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, true, true,
                                       true, true, false, false, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
//                      This is to list the Message Types.
                        //setMsgSubType(reportBean, "R41,R42");
                        setTranType(reportBean, "Inward,Outward");
                        reportBean.setPageForward("brSubTypeIndividual");
                        return mapping.findForward("brSubTypeIndividual");
                        
                    case OwReturnedReport :
                        
                        reportBean.resetBean();
                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, false, false,
                                       false, false, false, true, false, false, false, false, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        reportBean.setPageForward("owReturnedReport");
                        return mapping.findForward("owReturnedReport");     
                    
                    case futureDatedTxns :
                        
                        reportBean.resetBean();
                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, false, false, true, false, true,
                                       false, false, false, false, false, false, false,true, false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        setMsgSubType(reportBean,"R41,R42");
                        reportBean.setPageForward("futureDatedTxnsReport");
                        return mapping.findForward("futureDatedTxnsReport"); 
                        
                    case exceptionReport:
                        
                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean, true, true, true, true, true,
                                       false, true, false, false, false, false, false, false, false);
                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        setTranType(reportBean, "Inward,Outward");
                        reportBean.setPageForward("exceptionReport");
                        return mapping.findForward("exceptionReport");
                    case returnPaymentRejectedReport:
                        
                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean,true, true, true,false, false, false, false,
                                       false, false, false, false,true,false, false);
                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        reportBean.setPageForward("returnPaymentRejectedReport");
                        return mapping.findForward("returnPaymentRejectedReport");
                        
                    case unsuccessfulPayment:
                        
                        reportBean.resetBean();

                        /*
                         *Set the Fields which are needed in the Input Page. 
                         */
                        setFieldValues(reportBean,true, true, true,true, true, false, false,
                                       false, false, false, false,false,false,true);
                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        reportBean.setPageForward("unsuccessfulPaymentReport");
                        return mapping.findForward("unsuccessfulPaymentReport");
                } 
            } else if (mode!=null && mode.equalsIgnoreCase("branchList")) {

                /**
                 * To get the nonHost Branches based on the bank code.
                 * 
                 * Should not reset the bean.
                 */
                reportBean.getNonHostBranchList(request);
                return mapping.findForward(reportBean.getPageForward());
            }
        } catch(Exception e) {

            logger.error("Exception Occurred while Loading the Input Value. : " +
                         e.getMessage());
            saveErrors(reportBean, e);
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
                                  boolean haveCounterParty, boolean haveUserFld,boolean haveFutureDateTxnStatus,boolean haveResponeType) {

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
        reportBean.setHaveFutureDateTxnStatus(haveFutureDateTxnStatus);
        reportBean.setHaveResponeType(haveResponeType);
    }
    
    /**
     * Set tran Type field
     */
    public void setTranType(InstaReportBean reportBean,String tranType) {
        
        DisplayValueReportDTO dto = new DisplayValueReportDTO();
        reportBean.setTranTypeList(new ArrayList<DisplayValueReportDTO>(0));
        if ( tranType.indexOf("Both")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("All");
            dto.setDisplayValue("Both");
            reportBean.getTranTypeList().add(dto);
        }
        
        if ( tranType.indexOf("Inward")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("inward");
            dto.setDisplayValue("Inward");
            reportBean.getTranTypeList().add(dto);
        }
        
        if ( tranType.indexOf("Outward")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("outward");
            dto.setDisplayValue("Outward");
            reportBean.getTranTypeList().add(dto);
        }
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
