/*
 * @(#)NEFTReportInputAction.java
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
package com.objectfrontier.insta.neft.reports.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.objectfrontier.crud.CrudException;
import com.objectfrontier.insta.dto.DisplayValueReportDTO;
import com.objectfrontier.insta.neft.reports.bean.InstaNEFTReportBean;
import com.objectfrontier.insta.rtgs.reports.action.AbstractReportAction;



/**
 * @author mohanadevis
 * @date   Dec 30, 2008
 * @since  insta.reports; Dec 30, 2008
 */
public class NEFTReportInputAction
extends AbstractReportAction {

    private static Category logger = Category.getInstance(NEFTReportInputAction.class.getName());
    public enum Operator {NeftInwardSummaryReport, PaymentSubmittedReport, PaymentReceivedReport, NeftOutwardSummaryReport,
                          NeftBrInwReturnedReport, BatchwiseAggregateReport, BatchwiseReconcillationReport, NeftOutwardTxnDetailsReport,
                          DatewiseGrduatedPaymentReport, OwReturnedReport,InwardTxnsReport, NeftRtgsNetSettlementReport, CPWiseReconcilliationReport,
                          NeftUTRNumberwiseReport, neftFutureDatedTxns, neftExceptionReport, neftReturnPaymentRejectedReport,
                          InwardSummaryBankWiseReport, InwardBankDetailedReport,OutwardSummaryBankWiseReport, OutwardBankDetailedReport}


    @Override
    public ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {

        String mode = request.getParameter("mode");
        String report = request.getParameter("report");
        String canReset = request.getParameter("canReset");

        boolean flag    = true;

        if(canReset != null) {
            flag = false;
        }

        if(report == null){
            report = "";
        }
        InstaNEFTReportBean reportBean = (InstaNEFTReportBean) form;
        if (reportBean==null) {
            reportBean = new InstaNEFTReportBean();
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

                    case NeftInwardSummaryReport :

                        if(flag) {

                            reportBean.resetBean();
                            reportBean.resetInput();
                        }
                        //Added to get the report name to check.
                        reportBean.reportName = report;
                        /*
                         *Set the Fields which are needed in the Input Page.
                         */
                        //setFieldValues1(reportBean, true, true, true, true, true);
                        setFieldValues(reportBean, true,true,true,true,true,true,true,false,false,false,false,false,false,false,false,false,false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
                        reportBean.setPageForward("neftinwardSummaryReport");
                        return mapping.findForward("neftinwardSummaryReport");

                    case PaymentSubmittedReport :

                        reportBean.resetBean();
                        reportBean.resetInput();
                        /*
                         *Set the Fields which are needed in the Input Page.
                         */
                        setFieldValues(reportBean, true, true, true, true, true,
                                       true, true, false, false, false, false, false, false, false, false, false,false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
//                      This is to list the Message Types.
                        setMsgSubType(reportBean, "ALL, N06, N07");
                        setTranType(reportBean, "Outward");
                        reportBean.setPageForward("neftPaymentSubmittedReport");
                        return mapping.findForward("neftPaymentSubmittedReport");

                    case PaymentReceivedReport :

                        reportBean.resetBean();
                        reportBean.resetInput();
                        /*
                         *Set the Fields which are needed in the Input Page.
                         */
                        setFieldValues(reportBean, true, true, true, true, true,
                                       true, true, false, false, false, false, false, false, false, false, false,false);

                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                        reportBean.loadInitial(request);
//                      This is to list the Message Types.
                        setMsgSubType(reportBean, "N02");
                        setTranType(reportBean, "Inward");
                        reportBean.setPageForward("neftPaymentReceivedReport");
                        return mapping.findForward("neftPaymentReceivedReport");

                    case NeftOutwardSummaryReport:

                        if(flag){

                            reportBean.resetBean();
                            reportBean.resetInput();
                        }
                        /*
                         *Set the Fields which are needed in the Input Page.
                         */
                        setFieldValues1(reportBean, true, true, true, true, false);
                        /*
                         *     This method is to load the initial data which are required
                         * for loading the report input page.
                         */
                       reportBean.loadInitial(request);
                       reportBean.setPageForward("neftoutwardSummaryReport");
                       return mapping.findForward("neftoutwardSummaryReport");

                    case NeftBrInwReturnedReport:

                       reportBean.resetBean();
                       reportBean.resetInput();
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       setFieldValues(reportBean, false, true, true, false, false,
                                       false, false, false, false, false, false, false, true, false, false, false,false);
                       /*
                       *     This method is to load the initial data which are required
                       * for loading the report input page.
                       */
                       reportBean.loadInitial(request);
                       reportBean.setPageForward("neftBrInwReturnedReport");
                       return mapping.findForward("neftBrInwReturnedReport");

                    case BatchwiseAggregateReport :

                        if(flag) {

                            reportBean.resetBean();
                            reportBean.resetInput();
                        }
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       setFieldValues1(reportBean, true, true, true, true, false);
                        /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.loadInitial(request);
                       reportBean.setPageForward("neftBatchwiseAggregateReport");
                       return mapping.findForward("neftBatchwiseAggregateReport");

                    case BatchwiseReconcillationReport :

                       reportBean.resetBean();
                       reportBean.resetInput();
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       setBatchReconcillationFieldValues(reportBean, false, true, true); //To delete the transaction type.
                       /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.loadInitial(request);
                       setTranType(reportBean, "Both, Inward, Outward");
                       reportBean.setPageForward("neftBatchwiseReconcillationReport");
                       return mapping.findForward("neftBatchwiseReconcillationReport");

                   case NeftOutwardTxnDetailsReport:

                       if(flag){
                           reportBean.resetBean();
                           reportBean.resetInput();
                       }
                       reportBean.reportName = report;
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       //Commented by priyak for user requirement-Report type not needed in the display and host type need to be dispalyed
                       //setFieldValues(reportBean, true,true,true,false,true,false,false,false,false,false,false,false,true,true,false,false);
                       setFieldValues(reportBean, true,true,true,true,true,false,false,false,false,false,false,false,true,false,false,false,false);
                       reportBean.loadOutwardSpecificStatus();
                       /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.loadInitial(request);
                       reportBean.setHaveStatusField(true);
                       reportBean.setPageForward("NeftOutwardTxnDetailsReport");
                       return mapping.findForward("NeftOutwardTxnDetailsReport");

                   case DatewiseGrduatedPaymentReport:

                       reportBean.resetBean();
                       reportBean.resetInput();
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       setFieldValues1(reportBean, false, true, false, false, false);
                       /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.loadInitial(request);
                       reportBean.setPageForward("neftGraduatedPaymentReport");
                       return mapping.findForward("neftGraduatedPaymentReport");

                   case OwReturnedReport :

                       reportBean.resetBean();
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       setFieldValues(reportBean, true, true, true, false, false,
                                      false, false, false, true, false, false, false, true, false, false, false,false);

                       /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.loadInitial(request);
                       reportBean.setPageForward("neftOwReturnedReport");
                       return mapping.findForward("neftOwReturnedReport");

                   case InwardTxnsReport :

                       reportBean.resetBean();
                       reportBean.resetInput();

                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       //setFieldValues1(reportBean, true, false, true, true, true);
                       //setFieldValues(reportBean, true,true,true,false,false,false,false,false,false,false,false,false,true,false,false,true);
                       //Modified by priyak for user requirement
                       setFieldValues(reportBean, true,true,true,true,false,false,false,false,false,false,false,false,true,false,false,false,false);
                       reportBean.loadInwardSpecificStatus();
                       /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.loadInitial(request);
                       reportBean.setHaveStatusField(true);
                       reportBean.setPageForward("neftInwardTxnsReport");
                       return mapping.findForward("neftInwardTxnsReport");

                   case NeftRtgsNetSettlementReport:
                       reportBean.resetBean();
                       reportBean.resetInput();
                       setFieldValues(reportBean, false, false, false, false, false, false, false, false,
                                      false, false, false, false, false, false, true, false,false);
                       reportBean.loadInitial(request);
                       reportBean.setPageForward("neftRtgsNetSettlementReport");
                       return mapping.findForward("neftRtgsNetSettlementReport");

                   case CPWiseReconcilliationReport:

                       reportBean.resetBean();
                       reportBean.resetInput();
                       reportBean.reportName = "CPWiseReconcilliationReport";
                       setFieldValues(reportBean, true, false, true, false, true, false, true, false,
                                      false, false, true, false, false, false, false, false,false);
                       reportBean.loadInitial(request);
                       reportBean.setPageForward("neftCPWiseReconcilliationReport");
                       return mapping.findForward("neftCPWiseReconcilliationReport");
                       //Added on 16-Sep-2009 by Mohana for NEFT UtrNowise report
                   case NeftUTRNumberwiseReport:

                       reportBean.resetBean();
                       reportBean.resetInput();
                       setFieldValues(reportBean, false,true, false, false, false, false, false, true, false,
                                      false, false, false, false, false, false, false,false);
                       reportBean.loadInitial(request);
                       /*
                        * To Disable the Branch Display.
                        */
                       reportBean.disableBranchDisplay=1;

                       reportBean.setPageForward("neftUtrNoWise");
                       return mapping.findForward("neftUtrNoWise");
                       //Added on 22-Sep-2009 by Mohana for NEFT Future Dated Txns report
                   case neftFutureDatedTxns:

                       reportBean.resetBean();
                       reportBean.resetInput();
                       setFieldValues(reportBean, false,false, true, false, true, false, false, false,
                                      false, false, false, false, false, false, false, false,true);
                       reportBean.loadInitial(request);
                       setMsgSubType(reportBean,"N06");
                       reportBean.setPageForward("neftFutureDatedTxnsReport");
                       return mapping.findForward("neftFutureDatedTxnsReport");
//                     Added on 24-Sep-2009 by Mohana for NEFT Future Dated Txns report
                   case neftExceptionReport:

                       reportBean.resetBean();
                       reportBean.resetInput();
                       setFieldValues(reportBean, true,true,true,true,true,false,true,false,
                                      false,false,false,false,false,false,false,false,false);
                       reportBean.reportName = report;
                       reportBean.loadInitial(request);
                       setTranType(reportBean, "Inward,Outward");
                       reportBean.setPageForward("neftExceptionsReport");
                       return mapping.findForward("neftExceptionsReport");
                   case neftReturnPaymentRejectedReport:

                       reportBean.resetBean();
                       reportBean.resetInput();
                       setFieldValues(reportBean, true,true,true,false,false,false,false,false,
                                      false,false,false,true,false,false,false,false,false);
                       reportBean.reportName = report;
                       reportBean.loadInitial(request);
                       reportBean.setPageForward("neftReturnPaymentRejectedReport");
                       return mapping.findForward("neftReturnPaymentRejectedReport");

                       // for NEFT - Levy charges report.
                   case InwardSummaryBankWiseReport:

                	   reportBean.resetBean();

                       //Added to get the report name to check.
                       reportBean.reportName = report;
                       reportBean.setDateFormat("");
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       //setFieldValues1(reportBean, true, true, true, true, true);
                       setFieldValues(reportBean, true, false, true, true, true,
                    		   	true, true, false, true, false, false, false,
                    		   	false, false, false, false, false);
                       // for display the bank name in list box.
                       reportBean.setHaveBankListField(true);

                       /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.setHaveBranchField(true);
                       reportBean.loadInitial(request);
                       reportBean.setHaveBranchField(false);

                	   reportBean.setPageForward("InwardBankWiseSummaryReport");
                       return mapping.findForward("InwardBankWiseSummaryReport");

                       // for NEFT - Levy charges report.
                   case InwardBankDetailedReport:

                	   reportBean.resetBean();
                	   reportBean.setTranType("inward");

                       //Added to get the report name to check.
                       reportBean.reportName = report;
                       reportBean.setDateFormat("");
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       //setFieldValues1(reportBean, true, true, true, true, true);
                       setFieldValues(reportBean, true, false, true, true, true,
                    		   	true, true, false, true, false, false, false,
                    		   	false, false, false, false, false);
                       // for display the bank name in list box.
                       reportBean.setHaveBankListField(true);

                       /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.setHaveBranchField(true);
                       reportBean.loadInitial(request);
                       reportBean.setHaveBranchField(false);
                	   reportBean.setPageForward("InwardBankDetailReport");
                       return mapping.findForward("InwardBankDetailReport");

                       // for NEFT - Levy charges report.
                   case OutwardSummaryBankWiseReport:

                       reportBean.resetBean();

                       //Added to get the report name to check.
                       reportBean.reportName = report;
                       reportBean.setDateFormat("");
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       //setFieldValues1(reportBean, true, true, true, true, true);
                       setFieldValues(reportBean, true, false, true, true, true,
                                true, true, false, true, false, false, false,
                                false, false, false, false, false);
                       // for display the bank name in list box.
                       reportBean.setHaveBankListField(true);

                       /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.setHaveBranchField(true);
                       reportBean.loadInitial(request);
                       reportBean.setHaveBranchField(false);

                	   reportBean.setPageForward("OutwardSenderSummaryReport");
                       return mapping.findForward("OutwardSenderSummaryReport");

                       // for NEFT - Levy charges report.
                   case OutwardBankDetailedReport:

                	   reportBean.resetBean();
                	   reportBean.setTranType("outward");
                       //Added to get the report name to check.
                       reportBean.reportName = report;
                       reportBean.setDateFormat("");
                       /*
                        *Set the Fields which are needed in the Input Page.
                        */
                       //setFieldValues1(reportBean, true, true, true, true, true);
                       setFieldValues(reportBean, true, false, true, true, true,
                    		   	true, true, false, true, false, false, false,
                    		   	false, false, false, false, false);
                       // for display the bank name in list box.
                       reportBean.setHaveBankListField(true);

                       /*
                        *     This method is to load the initial data which are required
                        * for loading the report input page.
                        */
                       reportBean.setHaveBranchField(true);
                       reportBean.loadInitial(request);
                       reportBean.setHaveBranchField(false);
                	   reportBean.setPageForward("OutwardBankDetailReport");
                       return mapping.findForward("OutwardBankDetailReport");
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

            logger.error("Exception Occurred while Loading the Input Value for NEFT Report. : " +
                         e.getMessage());
            saveErrors(reportBean, e);
        }
        return mapping.findForward("input");
    }

    /**
     * This method is to set the fields whihc are need to be display on the input page.
     *
     */
    public void setFieldValues1(InstaNEFTReportBean neftReportBean, boolean haveBranchFld,
                                  boolean haveValueDate,boolean haveBatchTime,boolean haveRepTypeFld,boolean haveInwtype) {

        /*
         *Set the Fields which are needed in the Input Page.
         */
        neftReportBean.setHaveBranchField(haveBranchFld);
        neftReportBean.setHaveValueDateField(haveValueDate);
        neftReportBean.setHaveBatchTimeField(haveBatchTime);
        neftReportBean.setHaveReportTypeField(haveRepTypeFld);
        neftReportBean.setHaveInwardTypeField(haveInwtype);
    }

    /**
     * This method is to set the fields whihc are need to be display on the input page.
     *
     */
    public void setFieldValues(InstaNEFTReportBean reportBean, boolean haveAmtFld, boolean haveBranchFld,
                                  boolean haveDateFld,boolean haveHostFld, boolean haveSubTypeFld,
                                  boolean haveStatusFld, boolean haveTranTypeFld, boolean haveUTRNoFld,
                                  boolean isInwardSpecific, boolean isOutwardSpecific,
                                  boolean haveCounterParty, boolean haveUserFld,boolean haveBatchTime,
                                  boolean haveRepTypeFld, boolean haveRepdateFld, boolean haveInwtype,boolean haveFutureDateTxnStatus) {

        /*
         *Set the Fields which are needed in the Input Page
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
        reportBean.setHaveBatchTimeField(haveBatchTime);
        reportBean.setHaveReportTypeField(haveRepTypeFld);
        reportBean.setHaveValueDateField(haveRepdateFld);
        reportBean.setHaveInwardTypeField(haveInwtype);
        reportBean.setHaveFutureDateTxnStatus(haveFutureDateTxnStatus);
}
    /**
     * Set Message Sub Type Field.
     */
    public void setMsgSubType(InstaNEFTReportBean reportBean, String msgTypes) {

        DisplayValueReportDTO dto = new DisplayValueReportDTO();
        reportBean.setSubTypeList(new ArrayList<DisplayValueReportDTO>(0));

        if ( msgTypes.indexOf("ALL")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("ALL");
            dto.setDisplayValue("ALL Payments");
            reportBean.getSubTypeList().add(dto);
        }

        if ( msgTypes.indexOf("N02")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("N02");
            dto.setDisplayValue("N02");
            reportBean.getSubTypeList().add(dto);
        }

        if ( msgTypes.indexOf("N06")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("N06");
            dto.setDisplayValue("N06");
            reportBean.getSubTypeList().add(dto);
        }

        if ( msgTypes.indexOf("N07")>=0 ) {

            dto = new DisplayValueReportDTO();
            dto.setValue("N07");
            dto.setDisplayValue("N07");
            reportBean.getSubTypeList().add(dto);
        }

    }

    /**
     * Set tran Type field
     */

    public void setTranType(InstaNEFTReportBean reportBean,String tranType) {
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
     * This method is to set the fields whihc are need to be display on the input page.
     *
     */
    public void setBatchReconcillationFieldValues(InstaNEFTReportBean neftReportBean, boolean haveTranTypeFld,
                                  boolean haveValueDate,boolean haveBatchTime) {

        /*
         *Set the Fields which are needed in the Input Page.
         */
        neftReportBean.setHaveTranTypeField(haveTranTypeFld);
        neftReportBean.setHaveValueDateField(haveValueDate);
        neftReportBean.setHaveBatchTimeField(haveBatchTime);
    }
}
