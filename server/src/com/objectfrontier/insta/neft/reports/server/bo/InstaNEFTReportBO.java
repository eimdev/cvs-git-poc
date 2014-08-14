/*
 * @(#)InstaNEFTReportBO.java
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
package com.objectfrontier.insta.neft.reports.server.bo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.objectfrontier.arch.dto.Message;
import com.objectfrontier.arch.server.ServerException;
import com.objectfrontier.arch.server.bo.BOException;
import com.objectfrontier.insta.InstaDefaultConstants;
import com.objectfrontier.insta.dto.DisplayValueReportDTO;
import com.objectfrontier.insta.message.client.dto.CMsgDTO;
import com.objectfrontier.insta.neft.server.workflow.bo.InstaNEFTMessageBO;
import com.objectfrontier.insta.reports.InstaReportUtil;
import com.objectfrontier.insta.reports.constants.ReportConstants;
import com.objectfrontier.insta.reports.dto.ConsolidatedReconcileReportDTO;
import com.objectfrontier.insta.reports.dto.ReconcileReportDTO;
import com.objectfrontier.insta.reports.dto.ReportDTO;
import com.objectfrontier.insta.reports.dto.ReportInputDTO;
import com.objectfrontier.insta.reports.server.bo.InstaReportBO;
import com.objectfrontier.insta.reports.server.util.ConversionUtil;
import com.objectfrontier.insta.reports.server.util.RHSConstants;
import com.objectfrontier.insta.worker.TaskException;
import com.objectfrontier.neft.report.dto.BatchwiseAggregateDTO;
import com.objectfrontier.neft.report.dto.BatchwiseReconcillationDTO;
import com.objectfrontier.neft.report.dto.CustomerInfo;
import com.objectfrontier.neft.report.dto.ITDetailReportDTO;
import com.objectfrontier.neft.report.dto.NEFTDetailsReportDTO;
import com.objectfrontier.neft.report.dto.NEFTN04DetailsDTO;
import com.objectfrontier.neft.report.dto.NEFT_RTGSNetSettlementDTO;
import com.objectfrontier.neft.report.dto.OTDetailReportDTO;
import com.objectfrontier.neft.report.dto.SummaryInfo;
import com.objectfrontier.neft.report.dto.TransactionInfo;
import com.objectfrontier.neft.report.dto.SummaryInfo.SummaryInfoElement;



/**
 * @author mohanadevis
 * @date   Dec 30, 2008
 * @since  insta.reports; Dec 30, 2008
 */
public class InstaNEFTReportBO
extends InstaReportBO {

    //NEFT Child logger should be logged using the name neftLogger
    //static Logger logger = Logger.getLogger(InstaNEFTReportBO.class);
    static Logger logger = Logger.getLogger("neftLogger");

    public static final String TOTAL = "Total";
    public static final String OUTWARD_TRANSACTION_TYPE = "outward";
    public static final String INWARD_TRANSACTION_TYPE = "inward";
    public static final String BANKNAME = InstaDefaultConstants.BANKNAME;

    /** Constants added for Levy Charge Reports */
    //Starts
    public static final String VALUE_DATE		= "VALUE_DATE";
    public static final String UTRNO 			= "UTRNO";
    public static final String AMOUNT			= "AMOUNT";
    public static final String RECVADDRIFSC		= "RECVADDRIFSC";
    public static final String BEN_ACC_NAME		= "BEN_ACC_NAME";
    public static final String BEN_ACC_TYPE		= "BEN_ACC_TYPE";
    public static final String BEN_ACC_NO		= "BEN_ACC_NO";
    public static final String SENDERIFSC		= "SENDERIFSC";
    public static final String SENDER_ACC_NAME	= "SENDER_ACC_NAME";
    public static final String SENDER_ACC_TYPE	= "SENDER_ACC_TYPE";
    public static final String SENDER_ACC_NO	= "SENDER_ACC_NO";
    public static final String STATUS_DESC		= "STATUS_DESC";

    public static final String SENDERBANK			= "SENDERBANK";
    public static final String TOTTXNCOUNT			= "TOTTXNCOUNT";
    public static final String TOTAMOUNT			= "TOTAMOUNT";
    public static final String COMPLETEDTXNCOUNT 	= "COMPLETEDTXNCOUNT";
    public static final String COMPLETEDTXNAMT		= "COMPLETEDTXNAMT";
    public static final String RTNTXNCOUNT			= "RTNTXNCOUNT";
    public static final String RTNTXNAMT 			= "RTNTXNAMT";
    public static final String TOTTXNAMT            = "TOTTXNAMT";
    //Ends

    //Added for batch time ordering.
    /**
     * This class is used to display the rpeort content in batch time order.
     */
    public static final Comparator CASE_INSENSITIVE_ORDER = new CaseInsensitiveComparator1();

    private static class CaseInsensitiveComparator1
    implements Comparator , java.io.Serializable {

        private static final long serialVersionUID = 8575799808933029326L;

        public int compare(Object str1, Object str2) {

            String s1 = ((NEFTN04DetailsDTO)str1).getField3535();
            String s2 = ((NEFTN04DetailsDTO)str2).getField3535();

            int n1=s1.length();
            int n2=s2.length();
            for (int i1=0, i2=0; i1<n1 && i2<n2; i1++, i2++) {

                char c1 = s1.charAt(i1);
                char c2 = s2.charAt(i2);

                if (c1 != c2) {

                    c1 = Character.toUpperCase(c1);
                    c2 = Character.toUpperCase(c2);

                    if (c1 != c2) {

                        c1 = Character.toLowerCase(c1);
                        c2 = Character.toLowerCase(c2);

                        if (c1 != c2) {
                            return c1 - c2;
                        }
                    }
                }
            }
            return n1 - n2;
        }
    }

//  Query for Reconciliation Report
    String reconciliationQuery =

        " SELECT z.batch_time BATCH ," +
        " (CASE WHEN x.outward IS NULL THEN 0 ELSE x.outward END ) GROSS_OUTWARD," +
        " (CASE WHEN x.inward IS NULL THEN 0 ELSE x.inward END ) INWARD," +
        " (CASE WHEN x.next_batch IS NULL THEN 0 ELSE x.next_batch END ) RESCHEDULED_TO_NEXT_BATCH," +
        " (CASE WHEN u.prev_batch IS NULL THEN 0 ELSE u.prev_batch END ) RESCHEDULED_IN_PREV_BATCH," +
        " (CASE WHEN y.rejected IS NULL THEN 0 ELSE y.rejected END ) REJECTED, " +
        " ((CASE WHEN x.outward IS NULL THEN 0 ELSE x.outward END ) + " +
        " (CASE WHEN u.prev_batch IS NULL THEN 0 ELSE u.prev_batch END ) -" +
        " (CASE WHEN x.next_batch IS NULL THEN 0 ELSE x.next_batch END )  -" +
        " (CASE WHEN y.rejected IS NULL THEN 0 ELSE y.rejected END )) NET_OUTWARD," +
        " (((CASE WHEN x.outward IS NULL THEN 0 ELSE x.outward END ) + " +
        " (CASE WHEN u.prev_batch IS NULL THEN 0 ELSE u.prev_batch END ) -" +
        " (CASE WHEN x.next_batch IS NULL THEN 0 ELSE x.next_batch END )  -" +
        " (CASE WHEN y.rejected IS NULL THEN 0 ELSE y.rejected END )) -" +
        " (CASE WHEN x.inward IS NULL THEN 0 ELSE x.inward END )) AGGREGATE " +
        " FROM " +
        " (SELECT  a.batch_time, " +
        " SUM( (CASE WHEN a.tran_type = 'O' THEN TO_NUMBER(a.txn_amt) ELSE 0 END) ) outward," +
        " SUM( (CASE WHEN a.tran_type = 'I' AND a.is_rejected IS NULL THEN TO_NUMBER(a.txn_amt) ELSE 0 END) ) inward,       " +
        " SUM( (CASE WHEN a.tran_type = 'O'  AND " +
        " ((a.resh_date = a.txn_date  AND TO_NUMBER(a.resh_batch_time)   > TO_NUMBER(a.batch_time)  ) OR        " +   " a.resh_date >  a.txn_date  )  " +
        " THEN TO_NUMBER(a.txn_amt) ELSE 0 END) ) next_batch " +
        " FROM  NEFTBALTRAN a " +
        " WHERE a.txn_date = @txndate" +
        " GROUP BY a.batch_time " +
        " Order BY a.batch_time " +
        " ) x," +
        " (" +
        " SELECT  a.rej_batch_time batch_time, " +
        "  SUM( (CASE WHEN a.is_rejected ='Y' THEN TO_NUMBER(a.txn_amt) ELSE 0 END) ) rejected "+
        " FROM  NEFTBALTRAN a "+
        "  WHERE a.rej_date = @txndate AND "+
        "  a.tran_type = 'O'" +
        "  GROUP BY a.rej_batch_time "+
        " ) y," +
        " (" +
        " SELECT  a.resh_batch_time batch_time," +
        " SUM( (CASE WHEN a.tran_type = 'O'  AND " +
        "((a.resh_date = a.txn_date  AND TO_NUMBER(a.resh_batch_time)   > TO_NUMBER(a.batch_time)  OR a.resh_date > a.txn_date ) ) " +
        " THEN TO_NUMBER(a.txn_amt) ELSE 0 END) ) prev_batch "+
        " FROM  NEFTBALTRAN a " +
        " WHERE a.resh_date = @txndate  " +
        " GROUP BY a.resh_batch_time " +
        " ) u, "+
        " (SELECT REPLACE(end_time,':', '')  batch_time FROM BATCHTIMING b WHERE b.active = 1 ) z" +
        " WHERE z.batch_time = x.batch_time(+) AND" +
        " z.batch_time= y.batch_time (+) AND" +
        " z.batch_time = u.batch_time (+)" +
        " ORDER BY z.batch_time ";

    /*
     *  This Query is used to get the InwardTransactions(DetailedReport) and
     *  in this query NEFTREPORTINFO is a VIEW not a TABLE
     */
    final String AllDetailedInwardQuery =

        " SELECT " +
            " business_date, " +
            " value_date, " +
            " batchtime, " +
            " utrno, " +
            " senderAddr, " +
            " SUBSTR(recvAddr,1,11) recvAddr, " +
            " sender_acc_name, " +
            " sender_acc_no, " +
            " sender_acc_type, " +
            " ben_acc_name, " +
            " ben_acc_no, " +
            " ben_acc_type, " +
            " amount, " +
            " status, " +
            " status_desc, " +
            " resCode, " +
            " flag, " +
            " remarks " +
        " FROM NEFTREPORTINFO" +
        " WHERE " +
        " tran_type = ?  AND " +
        " subtype = 'N02' ";

//  Query for getting OutwardSummaryReport
    protected static String outwardBaseQuery =

        " SELECT info.batchtime, " +
        " info.value_date, " +
        " info.senderAddr, " +
        " SUBSTR(info.recvAddr, 1 , 11), " +
        " info.utrno, " +
        " info.amount, " +
        " info.sender_acc_name, " +
        " info.sender_acc_no, " +
        " info.sender_acc_type, " +
        " info.ben_acc_name, " +
        " info.ben_acc_no, " +
        " info.ben_acc_type, " +
        " info.status_desc, " +
        " info.ifsc_master_id " +
        " FROM NEFTREPORTINFO info";

    /*
     *  This Query is used to get the InwardTransactions (SummaryReport) and
     *  in this query NEFTREPORTINFO is a VIEW not a TABLE
     */
    final String AllInwardSummaryReportQuery =

        " SELECT COUNT(*), TO_CHAR( SUM (TO_NUMBER(amount))) FROM NEFTREPORTINFO " +
        " WHERE " +
        " tran_type = ? AND " +
        " subtype = 'N02' ";

    /**
     * To generate NEFT Reconcilication reports
     */
    public Message generateNEFTReconciliationReport(Message msg)
    throws Exception {

        ReportInputDTO reportInpDTO = (ReportInputDTO) msg.info;
        Message res = new Message();
        try {

            String reconcileQuery = reconciliationQuery.replaceAll("@txndate","'"+reportInpDTO.getValueDate()+"'");
            PreparedStatement ps = con.prepareStatement(reconcileQuery);
            ResultSet rs = ps.executeQuery();
            ReportDTO  reportDTO = constructReconcilationReportInfo(rs);
            res.info = reportDTO;
            return res;
        } catch(Exception e){

            logger.error("Exception while generating NEFTReconciliationReport :"+e);
            throw new BOException("Exception while generating NEFTReconciliationReport."+e);
        }
    }

    /**
     * This method is used to prepare the list
     * of reconcileReportDTos
     *
     * @param rs
     * @return
     */
    private ReportDTO constructReconcilationReportInfo(ResultSet rs)
    throws SQLException {

        ReconcileReportDTO reconcileReportDTO = new ReconcileReportDTO();
        List reconcileReportDTOs = new ArrayList();
//        double totalGrossOutAmt = 0; //a
//        double rescheduledPrevBatchAmt = 0; //b1
//        double rescheduledNextBatchAmt = 0; //c3
//        double rejectedAmt = 0; //d
//        double netOutwardAmt = 0; //e
//        double netInwardAmt = 0; //f
//        double aggregateAmt = 0; //g
//        double rescheduledPrevBatchTotalAmt = 0; //b
//        double rescheduledNextBatchTotalAmt = 0; //c

        BigDecimal totalGrossOutAmt = BigDecimal.ZERO; //a
        BigDecimal rescheduledPrevBatchAmt = BigDecimal.ZERO; //b1
        BigDecimal rescheduledNextBatchAmt = BigDecimal.ZERO; //c3
        BigDecimal rejectedAmt = BigDecimal.ZERO; //d
        BigDecimal netOutwardAmt = BigDecimal.ZERO; //e
        BigDecimal netInwardAmt = BigDecimal.ZERO; //f
        BigDecimal aggregateAmt = BigDecimal.ZERO; //g
        BigDecimal rescheduledPrevBatchTotalAmt = BigDecimal.ZERO; //b
        BigDecimal rescheduledNextBatchTotalAmt = BigDecimal.ZERO; //c

        int count = 0;
        while (rs.next()) {

            count ++;
            ReconcileReportDTO reportDTO = new ReconcileReportDTO();

            reportDTO.batchTime = rs.getString("BATCH");

//            reportDTO.grossOutwardAmount = rs.getDouble("GROSS_OUTWARD");
            reportDTO.grossOutwardAmount = rs.getBigDecimal("GROSS_OUTWARD");
//            totalGrossOutAmt += reportDTO.grossOutwardAmount;
            totalGrossOutAmt = totalGrossOutAmt.add(reportDTO.grossOutwardAmount);

//            reportDTO.rescheduledPrevBatchAmt = rs.getDouble("RESCHEDULED_IN_PREV_BATCH");
            reportDTO.rescheduledPrevBatchAmt = rs.getBigDecimal("RESCHEDULED_IN_PREV_BATCH");

//            rescheduledPrevBatchTotalAmt += reportDTO.rescheduledPrevBatchAmt; // calculate b
            rescheduledPrevBatchTotalAmt = rescheduledPrevBatchTotalAmt.add(reportDTO.rescheduledPrevBatchAmt);

            if (count == 1) { // check for first batch
//                rescheduledPrevBatchAmt += reportDTO.rescheduledPrevBatchAmt;
                rescheduledPrevBatchAmt  = rescheduledPrevBatchAmt.add(reportDTO.rescheduledPrevBatchAmt);
            }

//            reportDTO.rescheduledNextBatchAmt = rs.getDouble("RESCHEDULED_TO_NEXT_BATCH");
            reportDTO.rescheduledNextBatchAmt = rs.getBigDecimal("RESCHEDULED_TO_NEXT_BATCH");

//            rescheduledNextBatchTotalAmt += reportDTO.rescheduledNextBatchAmt; // calulate c
            rescheduledNextBatchTotalAmt = rescheduledNextBatchTotalAmt.add(reportDTO.rescheduledNextBatchAmt);

            rescheduledNextBatchAmt = reportDTO.rescheduledNextBatchAmt; // always take the last batch values

//            reportDTO.rejectedAmt = rs.getDouble("REJECTED");
            reportDTO.rejectedAmt = rs.getBigDecimal("REJECTED");
//            rejectedAmt += reportDTO.rejectedAmt;
            rejectedAmt = rejectedAmt.add(reportDTO.rejectedAmt);

            reportDTO.netOutwardAmt = rs.getBigDecimal("NET_OUTWARD");
//            netOutwardAmt += reportDTO.netOutwardAmt;
            netOutwardAmt = netOutwardAmt.add(reportDTO.netOutwardAmt);

//            reportDTO.netInwardAmt = rs.getDouble("INWARD");
            reportDTO.netInwardAmt = rs.getBigDecimal("INWARD");
//            netInwardAmt += reportDTO.netInwardAmt;
            netInwardAmt =  netInwardAmt.add(reportDTO.netInwardAmt);
            reportDTO.netInwardAmt = rs.getBigDecimal("INWARD");
            netInwardAmt = netInwardAmt.add(reportDTO.netInwardAmt);

            String aggAmt = Double.toString(rs.getDouble("AGGREGATE"));
            if(aggAmt.indexOf("-") != -1) {
                aggAmt = aggAmt.replace("-", "");
            }
            reportDTO.aggregateAmt = new BigDecimal(aggAmt);
            aggregateAmt = aggregateAmt.add(reportDTO.aggregateAmt);
//            reportDTO.aggregateAmt = rs.getDouble("AGGREGATE");
            reportDTO.aggregateAmt = rs.getBigDecimal("AGGREGATE");
//            aggregateAmt += reportDTO.aggregateAmt;
            aggregateAmt =  aggregateAmt.add(reportDTO.aggregateAmt);

            reconcileReportDTOs.add(reportDTO);
        }

        /*
         * add the total amount calculated  to the dto
         */
        ReconcileReportDTO reportTotalDTO = new ReconcileReportDTO();
        reportTotalDTO.heading = TOTAL;

//        reportTotalDTO.grossOutwardAmount      = formatAmount(totalGrossOutAmt);
//        reportTotalDTO.rescheduledPrevBatchAmt = formatAmount(rescheduledPrevBatchTotalAmt);
//        reportTotalDTO.rescheduledNextBatchAmt = formatAmount(rescheduledNextBatchTotalAmt);
//        reportTotalDTO.rejectedAmt             = formatAmount(rejectedAmt);
//        reportTotalDTO.netOutwardAmt           = formatAmount(netOutwardAmt);
//        reportTotalDTO.netInwardAmt            = formatAmount(netInwardAmt);
//        reportTotalDTO.aggregateAmt            = formatAmount(aggregateAmt);

        reportTotalDTO.grossOutwardAmount      = formatAmountAsBigDecimal(totalGrossOutAmt);
        reportTotalDTO.rescheduledPrevBatchAmt = formatAmountAsBigDecimal(rescheduledPrevBatchTotalAmt);
        reportTotalDTO.rescheduledNextBatchAmt = formatAmountAsBigDecimal(rescheduledNextBatchTotalAmt);
        reportTotalDTO.rejectedAmt             = formatAmountAsBigDecimal(rejectedAmt);
        reportTotalDTO.netOutwardAmt           = formatAmountAsBigDecimal(netOutwardAmt);
        reportTotalDTO.netInwardAmt            = formatAmountAsBigDecimal(netInwardAmt);
        reportTotalDTO.aggregateAmt            = formatAmountAsBigDecimal(aggregateAmt);


        reconcileReportDTOs.add(reportTotalDTO);
        reconcileReportDTO.setReconcileReportDTOs(reconcileReportDTOs);

        // set the overall amount to the reconcileConsolidatedReportDTO
        ConsolidatedReconcileReportDTO consolidatedReportDTO = new ConsolidatedReconcileReportDTO();
        consolidatedReportDTO.consolidatedReportDTOs = new ArrayList();

        ConsolidatedReconcileReportDTO consolidatedReportDTO1 = new ConsolidatedReconcileReportDTO();

        //   f
        consolidatedReportDTO1.RBIAccountAmt = formatAmount(netInwardAmt);

        //   f + d + c3          @@@@ 06-08-2006
//        consolidatedReportDTO1.NEFTAccountAmt = formatAmount(netInwardAmt + rejectedAmt + rescheduledNextBatchAmt);
        consolidatedReportDTO1.NEFTAccountAmt = formatAmount(netInwardAmt.add(rejectedAmt).add(rescheduledNextBatchAmt));

        consolidatedReportDTO1.heading = ConsolidatedReconcileReportDTO.TOTAL_INWARD_AMOUNT+"\n"+
                                            "for RBI Account (f), NEFT Account (f) + (d) + (c"+count+")";

        consolidatedReportDTO.consolidatedReportDTOs.add(consolidatedReportDTO1);

        ConsolidatedReconcileReportDTO consolidatedReportDTO2 = new ConsolidatedReconcileReportDTO();

        //      a + b1 - c3 - d
//        consolidatedReportDTO2.RBIAccountAmt = formatAmount(
//                                                   (totalGrossOutAmt + rescheduledPrevBatchAmt -
//                                                   rescheduledNextBatchAmt - rejectedAmt));
        consolidatedReportDTO2.RBIAccountAmt = formatAmount(totalGrossOutAmt.add(rescheduledPrevBatchAmt).subtract(rescheduledNextBatchAmt)
                                                            .subtract(rejectedAmt));
        // a + b1   @@@@@
//        consolidatedReportDTO2.NEFTAccountAmt = formatAmount(totalGrossOutAmt + rescheduledPrevBatchAmt);
        consolidatedReportDTO2.NEFTAccountAmt = formatAmount(totalGrossOutAmt.add(rescheduledPrevBatchAmt));

        consolidatedReportDTO2.heading = ConsolidatedReconcileReportDTO.TOTAL_OUTWARD_AMOUNT+"\n"+
                                            "for RBI Account (a)+(b1)-(c"+count+")-(d), NEFT Account (a) + (b1)";

        consolidatedReportDTO.consolidatedReportDTOs.add(consolidatedReportDTO2);

        ConsolidatedReconcileReportDTO consolidatedReportDTO3 = new ConsolidatedReconcileReportDTO();

//        consolidatedReportDTO3.RBIAccountAmt = formatAmount(consolidatedReportDTO1.RBIAccountAmt  -
//                                               consolidatedReportDTO2.RBIAccountAmt);
        consolidatedReportDTO3.RBIAccountAmt = formatAmount( new BigDecimal(consolidatedReportDTO1.RBIAccountAmt).
                                                             subtract(new BigDecimal(consolidatedReportDTO2.RBIAccountAmt)));


//        consolidatedReportDTO3.NEFTAccountAmt = formatAmount(consolidatedReportDTO1.NEFTAccountAmt -
//                                                consolidatedReportDTO2.NEFTAccountAmt);
        consolidatedReportDTO3.NEFTAccountAmt = formatAmount(new BigDecimal(consolidatedReportDTO1.NEFTAccountAmt).
                                                             subtract(new BigDecimal(consolidatedReportDTO2.NEFTAccountAmt)));

        consolidatedReportDTO3.heading = ConsolidatedReconcileReportDTO.NET_AMOUNT;
        consolidatedReportDTO.consolidatedReportDTOs.add(consolidatedReportDTO3);

        reconcileReportDTO.consolidatedReportDTO = consolidatedReportDTO;
        return reconcileReportDTO;
    }

//    private double formatAmount(double d) {
//
//        String amount = ConversionUtil.formatDouble(d);
//        return  Double.parseDouble(amount);
//    }

    /**
     * Method Overloaded for double to BigDecimal Conversion
     * @param BigDecimal
     * @return String
     */
    private String formatAmount(BigDecimal d) {
        return  ConversionUtil.formatBigDecimal(d);
    }

    /**
     * Method Overloaded for double to BigDecimal Conversion
     * @param BigDecimal
     * @return BigDecimal
     */
    private BigDecimal formatAmountAsBigDecimal(BigDecimal d) {
        return  new BigDecimal(ConversionUtil.formatBigDecimal(d));
    }

    /**
     * Method to genertae Inward Summary Report
     */
    /*public Message generateNEFTInwSummaryReport(Message msg)
    throws Exception {

        ReportInputDTO reportInpDTO = (ReportInputDTO) msg.info;
        Message res = new Message();
        ITDetailReportDTO detailsDTO;
        try {

            String query;
//          Setting the Query based on the Report Type selected by the user
            if (reportInpDTO.getReportType().equalsIgnoreCase(ReportConstants.DETAILED_INWARD_REPORT) ){
                query = AllDetailedInwardQuery;
            } else {
                query = AllInwardSummaryReportQuery;
            }

            String query1 = query + " AND TO_NUMBER(flag) = 1 AND value_date = '" +
                                      reportInpDTO.getValueDate() + "'";//RBI-Branch
            String query2 = query + " AND TO_NUMBER(flag) > 1 AND value_date = '" +
                                      reportInpDTO.getValueDate() + "'";//RBI-Sundry
            String query3 = query + " AND status NOT IN ('550') AND TO_NUMBER(flag) > 1 AND business_date = '" +
                                      reportInpDTO.getValueDate()+ "'";//Sundry-Branch
            String query4 = query + " AND status IN ('550') AND TO_NUMBER(flag) > 2 AND value_date = '" +
                                      reportInpDTO.getValueDate() + "'";//Sundry-Sundry

            ResultSet rs1 = setFiltersForInwardTransactionReport(reportInpDTO, query1).executeQuery();//RBI-Branch
            ResultSet rs2 = setFiltersForInwardTransactionReport(reportInpDTO, query2).executeQuery();//RBI-Sundry
            ResultSet rs3 = setFiltersForInwardTransactionReport(reportInpDTO, query3).executeQuery();//Sundry-Branch
            ResultSet rs4 = setFiltersForInwardTransactionReport(reportInpDTO, query4).executeQuery();//Sundry-Sundry

            //Based on the report type, construct the DTO to return
            if (reportInpDTO.getReportType().equalsIgnoreCase(ReportConstants.DETAILED_INWARD_REPORT)){

                detailsDTO = constructDetailedInwardReport(rs1, rs2, rs3, rs4,reportInpDTO.getValueDate());
            } else {

                detailsDTO = constructSummaryInwardReport(rs1, rs2, rs3, rs4, reportInpDTO.getValueDate());
            }
            res.info = detailsDTO;
            return res;
        } catch(Exception e){

            logger.error("Exception while generating NEFTInwSummaryReport :"+e);
            throw new BOException("Exception while generating NEFTInwSummaryReport "+e);
        }
    }*/

    /**
     * Method to genertae Inward Summary Report
     *
     * This method fully modified as like RTGS Br.summary report by Eswaripriyak for DC#02
     */
    public Message generateNEFTInwSummaryReport(Message msg)
    throws Exception {


        ReportInputDTO inputDTO = (ReportInputDTO) msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;

        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);

        Map<String, List<ReportDTO>> reportMap =new LinkedHashMap<String, List<ReportDTO>>();

        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());

        try {

            //NEFT Branchwise Report Query.
            String qryBrSummaryReport =
                " SELECT  im.ifsc || '-' || im.NAME as branch, value_date, " +
                "         msg_source, tran_type, COUNT(*) as count, " +
                "         TO_CHAR(SUM (TO_NUMBER(amount))) as TotAmount, rs.name as status, rm.business_date " +
                "   FROM neft_message rm, MESSAGE m, ifscmaster im, neft_status rs ";

            String whereBlock =
                " WHERE m.msg_id = rm.msg_id " +
                "       AND im.ID = rm.ifsc_master_id " +
                "       AND m.msg_sub_type IN ('N02', 'N04', 'N06', 'N07')" +
                "       AND rs.id = rm.status ";

            String groupBy =
                " GROUP BY im.ifsc || '-' || im.NAME, business_date, " +
                "           msg_source, " +
                "           value_date, " +
                "           msg_source, " +
                "           tran_type, " +
                "           rs.name ";

            String orderBY =
                " ORDER BY im.ifsc || '-' || im.NAME, " +
                "           business_date ASC, " +
                "           tran_type asc, " +
                "           msg_source asc, " +
                "           status asc ";

            /*
             *  Build the filter condition based on the input given by the user.
             */

            /*
             *  Related to Transaction Date.
             *  Assumption : ToDate should be greater than FromDate.
             */
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                whereBlock += " and rm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
            }
            /**
             * Done on on 08-Jan-2011.
             *
             * Separated database calls to generate reports based on data available
             * in local/archive schema.
             *
             * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we no need to look for data available in ARCHIVE schema.
             *
             * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we need to look both local and archive schema to generate reports.
             */

            if (inputDTO.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               qryBrSummaryReport =
                " SELECT  im.ifsc || '-' || im.NAME as branch, value_date, " +
                "         msg_source, tran_type, COUNT(*) as count, " +
                "         TO_CHAR(SUM (TO_NUMBER(amount))) as TotAmount, rs.name as status, rm.business_date " +
                "   FROM neft_message_vw rm, MESSAGE_vw m, ifscmaster im, neft_status rs ";
                }
            }


            //Related to Transaction Type.
            if (inputDTO.getTransactionType() != null && !inputDTO.getTransactionType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and rm.TRAN_TYPE = '" + inputDTO.getTransactionType() + "'" ;
            }

            //Related to Message Sub Type.
            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and m.MSG_SUB_TYPE = '" + inputDTO.getPaymentType() +"'" ;
            }

            //Related to Source Type.
            if (inputDTO.getHostType() != null && !inputDTO.getHostType()
                                                            .equalsIgnoreCase("ALL")) {

                whereBlock += " and rm.MSG_SOURCE = '" + inputDTO.getHostType() +"'" ;
            }

            //Related to Branch/IFSC Specific.
            if (inputDTO.getIfscId() > 0 ) {

                whereBlock += " and rm.IFSC_Master_ID = " + inputDTO.getIfscId();
            }

            /*
             *     Related to Amount given by the user. If the user fills only the
             * FromAmount field then system fetch messages having amount greater than
             * or equalto given FromAmount. If the user fills only the ToAmount field
             * then system fetch the messages having amount less than or equalto given
             * ToAmount. If the user give both the amount then the system looks for
             * messages having anmount between the fromAmount and toAmount.
             */
            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {

                whereBlock += " and rm.AMOUNT BETWEEN " + inputDTO.getFromAmount() +
                                                " and " + inputDTO.getToAmount();
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and rm.AMOUNT >= " + inputDTO.getFromAmount();
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and rm.AMOUNT <= " + inputDTO.getToAmount();
            }

            //Related to Message Status Specific.
            if (inputDTO.getStatus() != null && !inputDTO.getStatus()
                                                        .equalsIgnoreCase("ALL")) {

                whereBlock += " and rm.STATUS in (" + inputDTO.getStatus() +") " ;
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryBrSummaryReport += whereBlock + groupBy + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            logger.debug("Query for generating the NEFT Branch Summary Report : " + qryBrSummaryReport);

            ps = con.prepareStatement(qryBrSummaryReport);
            rs = ps.executeQuery();

            String prevBranch = "";

            while(rs.next()) {

                String currBranch = rs.getString("BRANCH");

                /*
                 * This is to group the records based on the Branches.
                 */
                if (prevBranch != null && prevBranch!= "" &&
                                        !prevBranch.equalsIgnoreCase(currBranch)) {

                    reportMap.put(prevBranch, reportList);
                    reportList = new ArrayList<ReportDTO>(0);
                }

                reportDTO = new ReportDTO();

                //Populate ReportDTO.
                reportDTO.setBranch(currBranch);
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("Value_date")));
                reportDTO.setSource(rs.getString("MSG_SOURCE"));

                String tranType = "Outward";

                if(rs.getString("TRAN_TYPE").equalsIgnoreCase("Inward")) {
                    tranType = "Inward";
                }
                reportDTO.setTranType(tranType);
                reportDTO.setAmt(rs.getString("TotAMOUNT"));
                reportDTO.setCount(rs.getInt("COUNT"));
                reportDTO.setStatus(rs.getString("status"));

                reportList.add(reportDTO);

                //Set the Current Branch to the Previous Branch.
                prevBranch = currBranch;
            }

            /*
             * This is to set the Last set of List.
             */
            if (reportList.size()>0){
                reportMap.put(prevBranch, reportList);
            }

        } catch(Throwable th) {

            logger.error("Exception while generating NEFT Branchwise Summary Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating NEFT Branchwise Summary Report."
                                , th);
        } finally {

            release(ps, rs);
        }

        msg.info = reportMap;
        return msg;
    }

//    /**
//     * Method for Outward Summary Report
//     */
//    public Message generateNEFTOutSummaryReport(Message msg)
//    throws Exception {
//
//        ReportInputDTO input = (ReportInputDTO) msg.info;
//        Message res = new Message();
//
//
//        /*
//         *      < Summary Report Details >
//         * TODDMMYYYYPS1  - Sent from Branch Received by RBI
//         * TODDMMYYYYPRJ  - Sent From Branch Rejected by RBI
//         * TODDMMYYYYPSF1 - Sent From Branch Rejscheduled by RBI
//         * TODDMMYYYYPS2  - Sent From Sundry Received by RBI
//         * TODDMMYYYYPSF2 - Sent From Sundry Rejected by RBI
//         */
//
//        // To hold Total amount of Txns Sent from branch & received by RBI
//        double PS1amount = 0;
//        // To hold Total amount of Txns sent from branch and rejected by RBI
//        double PRJamount = 0;
//        // To hold Total amount of Txns Sent from branch & rescheduled by RBI
//        double PSF1amount = 0;
//        // To hold Total amount of Txns sent from branch and rejceived by RBI
//        double PS2amount = 0;
//        // To hold Total amount of Txns Sent from Sundry and rejected by RBI
//        double PSF2amount = 0;
//
//        /*double rescheduledAmount = 0;
//        int rescheduledCount = 0;*/
//
//        try {
//
//             OTDetailReportDTO report = new OTDetailReportDTO();
//             String heading = OTDetailReportDTO.TO+InstaReportUtil.formatDate(InstaReportUtil.convertToDate(input.getValueDate()),"ddMMyyyy");
//
//             /*
//              * holds the report type requested . The possible values
//              * include
//              *
//              *     @ Summary
//              *     @ Detailed
//              */
//             String reportType = input.getReportType();
//
//             List transactionPS1Info = new ArrayList(1);
//             List transactionPRJInfo = new ArrayList(1);
//             List transactionPSF1Info = new ArrayList(1);
//             List transactionPS2Info = new ArrayList(1);
//             List transactionPSF2Info = new ArrayList(1);
//
//             // For Getting Sent from Branch and Received by RBI Txn Details
//             PS1amount = getPS1Transactions(input, transactionPS1Info);
//
//             // For Getting Sent From Branch and Rejected by RBI Txn Details
//             PRJamount = getPRJTransactions(input, transactionPRJInfo);
//
//             // For Getting Sent From Branch and Rejscheduled by RBI Txn Details
//             PSF1amount = getPSF1Transactions(input, transactionPSF1Info);
//
//             // For Getting From Sundry and Received by RBI Txn Details
//             PS2amount = getPS2Transactions(input, transactionPS2Info);
//
//             // For Getting Sent From Sundry Rejected by RBI Txn Details
//             PSF2amount = getPSF2Transactions(input, transactionPSF2Info);
//
////             double totalAmount = PS1amount + PRJamount + PSF1amount + PS2amount + PSF2amount;
////             int    totalCount  = transactionPS1Info.size() + transactionPRJInfo.size() + transactionPSF1Info.size();
////                    totalCount +=  transactionPS2Info.size() + transactionPSF2Info.size();
//
//             // generate the output report
//             Map elementMap = new TreeMap();
//
//             if (reportType.equalsIgnoreCase(OTDetailReportDTO.REPORT_TYPE_DETAIL)) {
//
//                elementMap.put(ReportConstants.REPORT_TYPE1 +
//                               input.getValueDate() + OTDetailReportDTO.HEAD_PS1, transactionPS1Info);
//                elementMap.put(ReportConstants.REPORT_TYPE1 +
//                               input.getValueDate()+ OTDetailReportDTO.HEAD_PRJ, transactionPRJInfo);
//                elementMap.put(ReportConstants.REPORT_TYPE1 +
//                               input.getValueDate()+ OTDetailReportDTO.HEAD_PSF1, transactionPSF1Info);
//                elementMap.put(ReportConstants.REPORT_TYPE2 +
//                               input.getValueDate() + OTDetailReportDTO.HEAD_PS2, transactionPS2Info);
//                elementMap.put(ReportConstants.REPORT_TYPE2 +
//                               input.getValueDate()+ OTDetailReportDTO.HEAD_PSF2, transactionPSF2Info);
//            }
//
//            if ((reportType.equalsIgnoreCase(OTDetailReportDTO.REPORT_TYPE_SUMMARY)) ||
//                 (reportType.equalsIgnoreCase("ALL"))) {
//
//                OTDetailReportDTO element = new OTDetailReportDTO();
//
//                element.summaryInfo = new SummaryInfo();
//                element.summaryInfo.summaryElements = new ArrayList();
//
//                SummaryInfoElement sm = new SummaryInfoElement();
//                // for PS1 transaction
//                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PS1 ,
//                                         transactionPS1Info.size(), PS1amount );
//                element.summaryInfo.summaryElements.add(sm);
//
//                // for PRJ transaction
//                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PRJ ,
//                                         transactionPRJInfo.size(), PRJamount );
//
//                element.summaryInfo.summaryElements.add(sm);
//
//                // for PSF1 transaction
//                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PSF1 ,
//                                         transactionPSF1Info.size(), PSF1amount );
//                element.summaryInfo.summaryElements.add(sm);
//
//                // for PS2 transaction
//                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PS2 ,
//                                             transactionPS2Info.size(), PS2amount );
//
//                element.summaryInfo.summaryElements.add(sm);
//
//                // for PSF2 transaction
//                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PSF2 ,
//                                             transactionPSF2Info.size(), PSF2amount );
//                element.summaryInfo.summaryElements.add(sm);
//
//                // for total transactions
////                 sm = new SummaryInfoElement(OTReportDTO.TOTAL_TRANACTIONS,  totalCount, totalAmount);
////                 element.summaryInfo.summaryElements.add(sm);
//
//                // set the Summary Details to the Map
//                elementMap.put(OTDetailReportDTO.SUMMARY_INFO, element.summaryInfo.summaryElements);
//         }
//         //set the Map
//         report.setOutwardMap(elementMap);
//         res.info = report;
//         return res;
//         } catch(Exception e) {
//
//             logger.error("Exception while generating NEFTInwSummaryReport :"+e);
//             throw new BOException("Exception while generating NEFTInwSummaryReport "+e);
//         }
//    }

    /**
     * Method for Outward Summary Report
     */
    public Message generateNEFTOutSummaryReport(Message msg)
    throws Exception {

        ReportInputDTO input = (ReportInputDTO) msg.info;
        Message res = new Message();


        /*
         *      < Summary Report Details >
         * TODDMMYYYYPS1  - Sent from Branch Received by RBI
         * TODDMMYYYYPRJ  - Sent From Branch Rejected by RBI
         * TODDMMYYYYPSF1 - Sent From Branch Rejscheduled by RBI
         * TODDMMYYYYPS2  - Sent From Sundry Received by RBI
         * TODDMMYYYYPSF2 - Sent From Sundry Rejected by RBI
         */

        // To hold Total amount of Txns Sent from branch & received by RBI
        BigDecimal PS1amount = BigDecimal.ZERO;
        // To hold Total amount of Txns sent from branch and rejected by RBI
        BigDecimal PRJamount = BigDecimal.ZERO;
        // To hold Total amount of Txns Sent from branch & rescheduled by RBI
        BigDecimal PSF1amount = BigDecimal.ZERO;
        // To hold Total amount of Txns sent from branch and rejceived by RBI
        BigDecimal PS2amount = BigDecimal.ZERO;
        // To hold Total amount of Txns Sent from Sundry and rejected by RBI
        BigDecimal PSF2amount = BigDecimal.ZERO;

        /*double rescheduledAmount = 0;
        int rescheduledCount = 0;*/

        try {

             OTDetailReportDTO report = new OTDetailReportDTO();
             String heading = OTDetailReportDTO.TO + " " + input.getValueDate();

             /*
              * holds the report type requested . The possible values
              * include
              *
              *     @ Summary
              *     @ Detailed
              */
             String reportType = input.getReportType();

             List transactionPS1Info = new ArrayList(1);
             List transactionPRJInfo = new ArrayList(1);
             List transactionPSF1Info = new ArrayList(1);
             List transactionPS2Info = new ArrayList(1);
             List transactionPSF2Info = new ArrayList(1);

             // For Getting Sent from Branch and Received by RBI Txn Details
             PS1amount = getPS1Transactions(input, transactionPS1Info);

             // For Getting Sent From Branch and Rejected by RBI Txn Details
             PRJamount = getPRJTransactions(input, transactionPRJInfo);

             // For Getting Sent From Branch and Rejscheduled by RBI Txn Details
             PSF1amount = getPSF1Transactions(input, transactionPSF1Info);

             // For Getting From Sundry and Received by RBI Txn Details
             PS2amount = getPS2Transactions(input, transactionPS2Info);

             // For Getting Sent From Sundry Rejected by RBI Txn Details
             PSF2amount = getPSF2Transactions(input, transactionPSF2Info);

//             double totalAmount = PS1amount + PRJamount + PSF1amount + PS2amount + PSF2amount;
//             int    totalCount  = transactionPS1Info.size() + transactionPRJInfo.size() + transactionPSF1Info.size();
//                    totalCount +=  transactionPS2Info.size() + transactionPSF2Info.size();

             // generate the output report
             Map elementMap = new TreeMap();

             if (reportType.equalsIgnoreCase(OTDetailReportDTO.REPORT_TYPE_DETAIL)) {

                elementMap.put(ReportConstants.REPORT_TYPE1 + " " +
                               input.getValueDate() + " " + OTDetailReportDTO.HEAD_PS1, transactionPS1Info);
                elementMap.put(ReportConstants.REPORT_TYPE1 + " " +
                               input.getValueDate()+ " " + OTDetailReportDTO.HEAD_PRJ, transactionPRJInfo);
                elementMap.put(ReportConstants.REPORT_TYPE1 + " " +
                               input.getValueDate()+ " " + OTDetailReportDTO.HEAD_PSF1, transactionPSF1Info);
                elementMap.put(ReportConstants.REPORT_TYPE2 + " " +
                               input.getValueDate() + " " + OTDetailReportDTO.HEAD_PS2, transactionPS2Info);
                elementMap.put(ReportConstants.REPORT_TYPE2 + " " +
                               input.getValueDate()+ " " + OTDetailReportDTO.HEAD_PSF2, transactionPSF2Info);
            }

            if ((reportType.equalsIgnoreCase(OTDetailReportDTO.REPORT_TYPE_SUMMARY)) ||
                 (reportType.equalsIgnoreCase("ALL"))) {

                OTDetailReportDTO element = new OTDetailReportDTO();

                element.summaryInfo = new SummaryInfo();
                element.summaryInfo.summaryElements = new ArrayList();

                SummaryInfoElement sm = new SummaryInfoElement();
                // for PS1 transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PS1 ,
                                         transactionPS1Info.size(), PS1amount.toString() );
                element.summaryInfo.summaryElements.add(sm);

                // for PRJ transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PRJ ,
                                         transactionPRJInfo.size(), PRJamount.toString() );

                element.summaryInfo.summaryElements.add(sm);

                // for PSF1 transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PSF1 ,
                                         transactionPSF1Info.size(), PSF1amount.toString() );
                element.summaryInfo.summaryElements.add(sm);

                // for PS2 transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PS2 ,
                                             transactionPS2Info.size(), PS2amount.toString() );

                element.summaryInfo.summaryElements.add(sm);

                // for PSF2 transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PSF2 ,
                                             transactionPSF2Info.size(), PSF2amount.toString() );
                element.summaryInfo.summaryElements.add(sm);

                // for total transactions
//                 sm = new SummaryInfoElement(OTReportDTO.TOTAL_TRANACTIONS,  totalCount, totalAmount);
//                 element.summaryInfo.summaryElements.add(sm);

                // set the Summary Details to the Map
                elementMap.put(OTDetailReportDTO.SUMMARY_INFO, element.summaryInfo.summaryElements);
         }
         //set the Map
         report.setOutwardMap(elementMap);
         res.info = report;
         return res;
         } catch(Exception e) {

             logger.error("Exception while generating NEFTInwSummaryReport :"+e);
             throw new BOException("Exception while generating NEFTInwSummaryReport "+e);
         }
    }

//    /**
//     * method to get all PSF1 transactions. A PSF1 Transaction is
//     * considered as a transaction initiated by a branch. and Rescheduled by RBI
//     *
//     * @param input     - ReportInputDTO which holds the all the input paramter
//     *                    choosed by the user
//     *
//     * @param con       - Connection
//     *
//     * @param batchTime - String which holds the respective batchTiming for which
//     *                    the debit transactions must be produced
//     *
//     * @param PSF1TxnsList - An empty list which must contain the result
//     *
//     * @return double - total amount of the PSF1 transactions for the respective batch
//     */
//    protected double getPSF1Transactions(ReportInputDTO input, List PSF1TxnsList) {
//
//         double totalAmt = 0;
//
//         boolean flag = false;
//         String query = outwardBaseQuery;
//         String additionalQuery = " WHERE info.status = ? " +
//                                  " AND info.FLAG = 1 " +
//                                  " AND info.BUSINESS_DATE = ? ";
//
//         query = query + additionalQuery;
//
//         if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
//             query += " and info.BATCHTIME = ? ";
//         }
//
////       filtering based on branch or Central office view
//         if ( input.getIfscId() > 0 ) {
//             query += " and info.IFSC_MASTER_ID = ? ";
//         }
//
//         try {
//             PreparedStatement ps = con.prepareStatement(query);
//
//             ps.setInt(1, RHSConstants.RESCHEDULED_BY_RBI_STATUS);
////           ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
//             ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date
//
//
//             if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
//                 ps.setString(3, input.getBatchTime()); // batchTime
//                 flag = true;
//             }
//             if ( input.getIfscId() > 0 ) {
//                 if (flag) {
//                     ps.setLong(4, input.getIfscId()); // branch Code
//                 } else {
//                     ps.setLong(3, input.getIfscId());
//                 }
//             }
//
//            ResultSet rs = ps.executeQuery();
//            totalAmt = constructOutwardDetailedTransactionInfo(rs, PSF1TxnsList);
//
//            rs.close();
//        } catch (Throwable th) {
//             logger.error("Exception while getting txns :" +th.getMessage());
//             throw new BOException("Exception while getting txns :"+th.getMessage());
//        }
//        return totalAmt;
//    }

    /**
     * method to get all PSF1 transactions. A PSF1 Transaction is
     * considered as a transaction initiated by a branch. and Rescheduled by RBI
     *
     * @param input     - ReportInputDTO which holds the all the input paramter
     *                    choosed by the user
     *
     * @param con       - Connection
     *
     * @param batchTime - String which holds the respective batchTiming for which
     *                    the debit transactions must be produced
     *
     * @param PSF1TxnsList - An empty list which must contain the result
     *
     * @return double - total amount of the PSF1 transactions for the respective batch
     */
    protected BigDecimal getPSF1Transactions(ReportInputDTO input, List PSF1TxnsList) {

        BigDecimal totalAmt = BigDecimal.ZERO;

         boolean flag = false;
         String query = outwardBaseQuery;
         String additionalQuery = " WHERE info.status = ? " +
                                  " AND info.FLAG = 1 " +
                                  " AND info.BUSINESS_DATE = ? ";

         query = query + additionalQuery;

         if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
             query += " and info.BATCHTIME = ? ";
         }

//       filtering based on branch or Central office view
         if ( input.getIfscId() > 0 ) {
             query += " and info.IFSC_MASTER_ID = ? ";
         }

         try {
             PreparedStatement ps = con.prepareStatement(query);

             ps.setInt(1, RHSConstants.RESCHEDULED_BY_RBI_STATUS);
//           ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
             ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date


             if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
                 ps.setString(3, input.getBatchTime()); // batchTime
                 flag = true;
             }
             if ( input.getIfscId() > 0 ) {
                 if (flag) {
                     ps.setLong(4, input.getIfscId()); // branch Code
                 } else {
                     ps.setLong(3, input.getIfscId());
                 }
             }

            ResultSet rs = ps.executeQuery();
            totalAmt = constructOutwardDetailedTransactionInfo(rs, PSF1TxnsList);

            rs.close();
        } catch (Throwable th) {
             logger.error("Exception while getting txns :" +th.getMessage());
             throw new BOException("Exception while getting txns :"+th.getMessage());
        }
        return totalAmt;
    }

//    /**
//     * This method is used to Generate the Transaction Informations such as
//     *
//     *  @ amount
//     *  @ value Date
//     *  @ batch time
//     *  @ sender account Number
//     *  @ sender account Type
//     *  @ sender account Name
//     *  @ sender ifsc
//     *  @ Beneficiary account Number
//     *  @ Beneficiary account Type
//     *  @ Beneficiary account Name
//     *  @ beneficiary ifsc
//     *  @ current status of message
//     *
//     * @param rs      - ResultSet
//     * @param list    - an Empty list which must contain the result
//     * @param con     - Connection
//     * @return amount - total amount of the transactions for the respective batch
//     * @throws SQLException
//     */
//
//    private double constructOutwardDetailedTransactionInfo(ResultSet rs, List outwardTxnInfoList)
//    throws SQLException {
//
//        // Total Amount
//        double totalAmount = 0;
//
//        while(rs.next()) {
//
//            TransactionInfo ti = new TransactionInfo();
//
//            ti.batchTime = rs.getString(1);
//            ti.valueDate = rs.getDate(2);
//            ti.senderInfo      = new CustomerInfo();
//            ti.beneficiaryInfo = new CustomerInfo();
//
//            ti.senderInfo.accIfsc = rs.getString(3);
//            ti.beneficiaryInfo.accIfsc = rs.getString(4);
//
//            ti.utrNo = rs.getString(5);
//            ti.amount = rs.getDouble(6);
//
//            ti.senderInfo.accName = rs.getString(7);
//            ti.senderInfo.accNo = rs.getString(8);
//            ti.senderInfo.accType = rs.getString(9);
//            ti.beneficiaryInfo.accName = rs.getString(10);
//            ti.beneficiaryInfo.accNo = rs.getString(11);
//            ti.beneficiaryInfo.accType = rs.getString(12);
//            ti.currentStatus = rs.getString(13);
//
//            totalAmount = totalAmount + ti.amount;
//
//            // Sets the DTO Object in to the List
//            outwardTxnInfoList.add(ti);
//        }
//        return totalAmount;
//    }

    /**
     * This method is used to Generate the Transaction Informations such as
     *
     *  @ amount
     *  @ value Date
     *  @ batch time
     *  @ sender account Number
     *  @ sender account Type
     *  @ sender account Name
     *  @ sender ifsc
     *  @ Beneficiary account Number
     *  @ Beneficiary account Type
     *  @ Beneficiary account Name
     *  @ beneficiary ifsc
     *  @ current status of message
     *
     * @param rs      - ResultSet
     * @param list    - an Empty list which must contain the result
     * @param con     - Connection
     * @return amount - total amount of the transactions for the respective batch
     * @throws SQLException
     */

    private BigDecimal constructOutwardDetailedTransactionInfo(ResultSet rs, List outwardTxnInfoList)
    throws SQLException {

        // Total Amount
//        double totalAmount = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        while(rs.next()) {

            TransactionInfo ti = new TransactionInfo();

            ti.batchTime = rs.getString(1);
            ti.valueDate = rs.getDate(2);
            ti.senderInfo      = new CustomerInfo();
            ti.beneficiaryInfo = new CustomerInfo();

            ti.senderInfo.accIfsc = rs.getString(3);
            ti.beneficiaryInfo.accIfsc = rs.getString(4);

            ti.utrNo = rs.getString(5);
//            ti.amount = rs.getDouble(6);
            ti.amount = rs.getBigDecimal(6);

            ti.senderInfo.accName = rs.getString(7);
            ti.senderInfo.accNo = rs.getString(8);
            ti.senderInfo.accType = rs.getString(9);
            ti.beneficiaryInfo.accName = rs.getString(10);
            ti.beneficiaryInfo.accNo = rs.getString(11);
            ti.beneficiaryInfo.accType = rs.getString(12);
            ti.currentStatus = rs.getString(13);

//            totalAmount = totalAmount + ti.amount;
            totalAmount = totalAmount.add(ti.amount);

            // Sets the DTO Object in to the List
            outwardTxnInfoList.add(ti);
        }
        return totalAmount;
    }

//    /**
//     * method to get all PRJ transactions. A PRJ Transaction is
//     * considered as a transaction initiated by a branch. and Rejected by RBI
//     *
//     * @param input     - ReportInputDTO which holds the all the input paramter
//     *                    choosed by the user
//     *
//     * @param con       - Connection
//     *
//     * @param batchTime - String which holds the respective batchTiming for which
//     *                    the debit transactions must be produced
//     *
//     * @param PRJTxnsList - An empty list which must contain the result
//     *
//     * @return double - total amount of the PRJ transactions for the respective batch
//     */
//    protected double getPRJTransactions(ReportInputDTO input, List PRJTxnsList) {
//
//          double totalAmt = 0;
//
//          boolean flag = false;
//          String query = outwardBaseQuery;
//          String additionalPRJQuery = " WHERE info.status = ? " +
//                                      " AND info.FLAG = 1 " +
//                                      " AND info.BUSINESS_DATE = ?  ";
//
//          query = query + additionalPRJQuery;
//
//          if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
//              query += " and info.BATCHTIME = ? ";
//          }
//
////        filtering based on branch or Central office view
//          if ( input.getIfscId() > 0 ) {
//              query += " and info.IFSC_MASTER_ID = ? ";
//          }
//
//          try {
//              PreparedStatement ps = con.prepareStatement(query);
//
//              ps.setInt(1, RHSConstants.REJECTED_BY_RBI_STATUS);
////            ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
//              ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date
//
//              if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
//                  ps.setString(3, input.getBatchTime()); // batchTime
//                  flag = true;
//              }
//              if ( input.getIfscId() > 0 ) {
//                  if (flag) {
//                      ps.setLong(4, input.getIfscId()); // branch Code
//                  } else {
//                      ps.setLong(3, input.getIfscId());
//                  }
//              }
//
//             ResultSet rs = ps.executeQuery();
//             totalAmt = constructOutwardDetailedTransactionInfo(rs, PRJTxnsList);
//
//            rs.close();
//        } catch (Throwable th) {
//            logger.error("Exception while getting txn details :"+ th.getMessage());
//            throw new BOException("Exception while getting txn details :"+th.getMessage());
//        }
//        return totalAmt;
//    }

    /**
     * method to get all PRJ transactions. A PRJ Transaction is
     * considered as a transaction initiated by a branch. and Rejected by RBI
     *
     * @param input     - ReportInputDTO which holds the all the input paramter
     *                    choosed by the user
     *
     * @param con       - Connection
     *
     * @param batchTime - String which holds the respective batchTiming for which
     *                    the debit transactions must be produced
     *
     * @param PRJTxnsList - An empty list which must contain the result
     *
     * @return double - total amount of the PRJ transactions for the respective batch
     */
    protected BigDecimal getPRJTransactions(ReportInputDTO input, List PRJTxnsList) {

        BigDecimal totalAmt = BigDecimal.ZERO;

        boolean flag = false;
        String query = outwardBaseQuery;
        String additionalPRJQuery = " WHERE info.status = ? " +
                                    " AND info.FLAG = 1 " +
                                    " AND info.BUSINESS_DATE = ?  ";

        query = query + additionalPRJQuery;

        if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
            query += " and info.BATCHTIME = ? ";
        }

//        filtering based on branch or Central office view
        if ( input.getIfscId() > 0 ) {
              query += " and info.IFSC_MASTER_ID = ? ";
        }

        try {
          PreparedStatement ps = con.prepareStatement(query);

          ps.setInt(1, RHSConstants.REJECTED_BY_RBI_STATUS);
//            ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
          ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date

          if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
              ps.setString(3, input.getBatchTime()); // batchTime
              flag = true;
          }
          if ( input.getIfscId() > 0 ) {
              if (flag) {
                  ps.setLong(4, input.getIfscId()); // branch Code
              } else {
                  ps.setLong(3, input.getIfscId());
              }
          }

         ResultSet rs = ps.executeQuery();
         totalAmt = constructOutwardDetailedTransactionInfo(rs, PRJTxnsList);

        rs.close();
    } catch (Throwable th) {
        logger.error("Exception while getting txn details :"+ th.getMessage());
        throw new BOException("Exception while getting txn details :"+th.getMessage());
    }
    return totalAmt;
}

//    /**
//     * method to get all PS1 transactions. A PS1 Transaction is
//     * considered as a transaction initiated by a branch. and Received by RBI
//     *
//     * @param input     - ReportInputDTO which holds the all the input paramter
//     *                    choosed by the user
//     *
//     * @param con       - Connection
//     *
//     * @param batchTime - String which holds the respective batchTiming for which
//     *                    the debit transactions must be produced
//     *
//     * @param PS1TxnsList - An empty list which must contain the result
//     *
//     * @return double - total amount of the PS1 transactions for the respective batch
//     */
//    protected double getPS1Transactions(ReportInputDTO input, List PS1TxnsList) {
//
//         double totalAmt = 0;
//
//         String query = outwardBaseQuery;
//         boolean flag = false;
//         String additionalPS1Query = " WHERE info.status = ? " +
//                                     " AND info.FLAG = 1 " +
//                                     " AND info.BUSINESS_DATE = ? ";
//
//
//         query = query + additionalPS1Query;
//
//        if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
//            query += " and info.BATCHTIME = ? ";
//        }
//
////      filtering based on branch or Central office view
//        if ( input.getIfscId() > 0 ) {
//            query += " and info.IFSC_MASTER_ID = ? ";
//        }
//
//        try {
//
//            PreparedStatement ps = con.prepareStatement(query);
////            ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
//            ps.setInt(1, RHSConstants.OUTWARD_COMPLETE_STATUS);
//            ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date
//
//            if ( !input.getBatchTime().equalsIgnoreCase("ALL") ) {
//                ps.setString(3, input.getBatchTime()); // batchTime
//                flag = true;
//            }
//
//            if ( input.getIfscId() > 0 ) {
//                if (flag) {
//                    ps.setLong(4, input.getIfscId()); // branch Code
//                } else {
//                    ps.setLong(3, input.getIfscId());
//                }
//            }
//
//            ResultSet rs = ps.executeQuery();
//            totalAmt = constructOutwardDetailedTransactionInfo(rs, PS1TxnsList);
//
//            rs.close();
//        } catch (Throwable th) {
//
//            logger.error("Exception while getting txn details :"+ th.getMessage());
//            throw new BOException("Exception while getting txn details :"+th.getMessage());
//        }
//        return totalAmt;
//    }

    /**
     * method to get all PS1 transactions. A PS1 Transaction is
     * considered as a transaction initiated by a branch. and Received by RBI
     *
     * @param input     - ReportInputDTO which holds the all the input paramter
     *                    choosed by the user
     *
     * @param con       - Connection
     *
     * @param batchTime - String which holds the respective batchTiming for which
     *                    the debit transactions must be produced
     *
     * @param PS1TxnsList - An empty list which must contain the result
     *
     * @return double - total amount of the PS1 transactions for the respective batch
     */
    protected BigDecimal getPS1Transactions(ReportInputDTO input, List PS1TxnsList) {

//         double totalAmt = 0;

        BigDecimal totalAmt = BigDecimal.ZERO;
        String query = outwardBaseQuery;
        boolean flag = false;
        String additionalPS1Query = " WHERE info.status = ? " +
                                     " AND info.FLAG = 1 " +
                                     " AND info.BUSINESS_DATE = ? ";


        query = query + additionalPS1Query;

        if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
            query += " and info.BATCHTIME = ? ";
        }

    //      filtering based on branch or Central office view
        if ( input.getIfscId() > 0 ) {
            query += " and info.IFSC_MASTER_ID = ? ";
        }

        try {

            PreparedStatement ps = con.prepareStatement(query);
    //            ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
            ps.setInt(1, RHSConstants.OUTWARD_COMPLETE_STATUS);
            ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date

            if ( !input.getBatchTime().equalsIgnoreCase("ALL") ) {
                ps.setString(3, input.getBatchTime()); // batchTime
                flag = true;
            }

            if ( input.getIfscId() > 0 ) {
                if (flag) {
                    ps.setLong(4, input.getIfscId()); // branch Code
                } else {
                    ps.setLong(3, input.getIfscId());
                }
            }

            ResultSet rs = ps.executeQuery();
            totalAmt = constructOutwardDetailedTransactionInfo(rs, PS1TxnsList);

            rs.close();
        } catch (Throwable th) {

            logger.error("Exception while getting txn details :"+ th.getMessage());
            throw new BOException("Exception while getting txn details :"+th.getMessage());
        }
        return totalAmt;
    }

//    /**
//     * method to get all PS2 transactions. A PS2 Transaction is
//     * considered as a transaction initiated from Sundry and Received by RBI
//     *
//     * @param input     - ReportInputDTO which holds the all the input paramter
//     *                    choosed by the user
//     *
//     * @param con       - Connection
//     *
//     * @param batchTime - String which holds the respective batchTiming for which
//     *                    the debit transactions must be produced
//     *
//     * @param PSFTxnsList - An empty list which must contain the result
//     *
//     * @return double - total amount of the PS2 transactions for the respective batch
//     */
//    protected double getPS2Transactions(ReportInputDTO input, List PS2TxnsList) {
//
//         double totalAmt = 0;
//         boolean flag = false;
//         String query = outwardBaseQuery;
//         String additionalQuery = " WHERE info.STATUS = ? " +
//                                  " AND info.FLAG = 2 " +
//                                  " AND info.BUSINESS_DATE = ? ";
//
//
////         String additionalQuery = " , NEFTSCRS ns " +
////                                 " WHERE info.value_date = ns.org_txn_dt " +
////                                 " AND info.utrno= ns.utr_no " +
////                                 " AND ns.msg_type IN ('298N03', '298N09') " +
////                                 " AND ns.org_rev = 'R' AND ns.org_txn_dt = ? " +
////                                 " AND info.senderaddr IS NOT NULL ";
//
//         query = query + additionalQuery;
//
//         if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
//             query += " and info.BATCHTIME = ? ";
//         }
//
////       filtering based on branch or Central office view
//         if ( input.getIfscId() > 0 ) {
//             query += " and info.IFSC_MASTER_ID = ? ";
//         }
//
//         try {
//             PreparedStatement ps = con.prepareStatement(query);
//
//             ps.setInt(1, RHSConstants.OUTWARD_COMPLETE_STATUS);
////           ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
//             ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date
//
//
//             if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
//                 ps.setString(3, input.getBatchTime()); // batchTime
//                 flag = true;
//             }
//             if ( input.getIfscId() > 0 ) {
//                 if (flag) {
//                     ps.setLong(4, input.getIfscId()); // branch Code
//                 } else {
//                     ps.setLong(3, input.getIfscId());
//                 }
//             }
//
//            ResultSet rs = ps.executeQuery();
//            totalAmt = constructOutwardDetailedTransactionInfo(rs, PS2TxnsList);
//
//            rs.close();
//        } catch (Throwable th) {
//
//            logger.error("Exception while getting txn details :"+ th.getMessage());
//            throw new BOException("Exception while getting txn details :"+th.getMessage());
//        }
//        return totalAmt;
//    }

    /**
     * method to get all PS2 transactions. A PS2 Transaction is
     * considered as a transaction initiated from Sundry and Received by RBI
     *
     * @param input     - ReportInputDTO which holds the all the input paramter
     *                    choosed by the user
     *
     * @param con       - Connection
     *
     * @param batchTime - String which holds the respective batchTiming for which
     *                    the debit transactions must be produced
     *
     * @param PSFTxnsList - An empty list which must contain the result
     *
     * @return double - total amount of the PS2 transactions for the respective batch
     */
    protected BigDecimal getPS2Transactions(ReportInputDTO input, List PS2TxnsList) {

        BigDecimal totalAmt = BigDecimal.ZERO;
         boolean flag = false;
         String query = outwardBaseQuery;
         String additionalQuery = " WHERE info.STATUS = ? " +
                                  " AND info.FLAG = 2 " +
                                  " AND info.BUSINESS_DATE = ? ";


//         String additionalQuery = " , NEFTSCRS ns " +
//                                 " WHERE info.value_date = ns.org_txn_dt " +
//                                 " AND info.utrno= ns.utr_no " +
//                                 " AND ns.msg_type IN ('298N03', '298N09') " +
//                                 " AND ns.org_rev = 'R' AND ns.org_txn_dt = ? " +
//                                 " AND info.senderaddr IS NOT NULL ";

         query = query + additionalQuery;

         if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
             query += " and info.BATCHTIME = ? ";
         }

//       filtering based on branch or Central office view
         if ( input.getIfscId() > 0 ) {
             query += " and info.IFSC_MASTER_ID = ? ";
         }

         try {
             PreparedStatement ps = con.prepareStatement(query);

             ps.setInt(1, RHSConstants.OUTWARD_COMPLETE_STATUS);
//           ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
             ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date


             if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
                 ps.setString(3, input.getBatchTime()); // batchTime
                 flag = true;
             }
             if ( input.getIfscId() > 0 ) {
                 if (flag) {
                     ps.setLong(4, input.getIfscId()); // branch Code
                 } else {
                     ps.setLong(3, input.getIfscId());
                 }
             }

            ResultSet rs = ps.executeQuery();
            totalAmt = constructOutwardDetailedTransactionInfo(rs, PS2TxnsList);

            rs.close();
        } catch (Throwable th) {

            logger.error("Exception while getting txn details :"+ th.getMessage());
            throw new BOException("Exception while getting txn details :"+th.getMessage());
        }
        return totalAmt;
    }

//    /**
//     * method to get all PSF2 transactions. A PSF2 Transaction is
//     * considered as a transaction initiated from Sundry, and Rejected by RBI
//     *
//     * @param input     - ReportInputDTO which holds the all the input paramter
//     *                    choosed by the user
//     *
//     * @param con       - Connection
//     *
//     * @param batchTime - String which holds the respective batchTiming for which
//     *                    the debit transactions must be produced
//     *
//     * @param PSF2TxnsList - An empty list which must contain the result
//     *
//     * @return double - total amount of the PSF1 transactions for the respective batch
//     */
//    protected double getPSF2Transactions(ReportInputDTO input, List PSF2TxnsList) {
//
//         double totalAmt = 0;
//
//         boolean flag = false;
//         String query = outwardBaseQuery;
//         String additionalQuery = " WHERE info.STATUS = ? " +
//                                  " AND info.FLAG = 2 " +
//                                  " AND info.BUSINESS_DATE = ? ";
//
////         String additionalQuery = " WHERE info.msgid IN ( " +
////                                  " SELECT srcmsgid " +
////                                  " FROM neftreportinfo " +
////                                  " WHERE tran_type = 'inward' " +
////                                  " AND SUBTYPE IN ('N03','N09') "+
////                                  " AND rescode IN ( " +
////                                  " SELECT code " +
////                                  " FROM REASONCODE " +
////                                  " WHERE TYPE LIKE '%N03rej%' OR TYPE LIKE '%N09rej%')" +
////                                  " AND TO_NUMBER(flag)= 2 ) " +
////                                  " AND info.VALUE_DATE = ? ";
//
//         query = query + additionalQuery;
//
//         if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
//             query += " and info.BATCHTIME = ? ";
//         }
//
//         // filtering based on branch or Central office view
//         if ( input.getIfscId() > 0 ) {
//
////             query += " and info.RECVADDR LIKE ? ";
//             query += " and info.IFSC_MASTER_ID = ? ";
//         }
//
//         try {
//             PreparedStatement ps = con.prepareStatement(query);
//
//             ps.setInt(1, RHSConstants.REJECTED_BY_RBI_STATUS);
////             ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
//              ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date
//
//             if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
//                 ps.setString(3, input.getBatchTime()); // batchTime
//                 flag = true;
//             }
////             if ( !(input.getBranchCode().equalsIgnoreCase("ALL"))) {
////                 if (flag) {
////                     String ifsc = "%" + input.getBranchCode() + "%";
////                     ps.setString(4, ifsc);
////                 } else {
////                     String ifsc = "%" + input.getBranchCode() + "%";
////                     ps.setString(3, ifsc);
////                 }
////             }
//
//             if ( input.getIfscId() > 0 ) {
//                 if (flag) {
//                     ps.setLong(4, input.getIfscId()); // branch Code
//                 } else {
//                     ps.setLong(3, input.getIfscId());
//                 }
//             }
//
//            ResultSet rs = ps.executeQuery();
//            totalAmt = constructOutwardDetailedTransactionInfo(rs, PSF2TxnsList);
//
//            rs.close();
//        } catch (Throwable th) {
//
//            logger.error("Exception while getting txn details :"+ th.getMessage());
//            throw new BOException("Exception while getting txn details :"+th.getMessage());
//        }
//        return totalAmt;
//    }

    /**
     * method to get all PSF2 transactions. A PSF2 Transaction is
     * considered as a transaction initiated from Sundry, and Rejected by RBI
     *
     * @param input     - ReportInputDTO which holds the all the input paramter
     *                    choosed by the user
     *
     * @param con       - Connection
     *
     * @param batchTime - String which holds the respective batchTiming for which
     *                    the debit transactions must be produced
     *
     * @param PSF2TxnsList - An empty list which must contain the result
     *
     * @return double - total amount of the PSF1 transactions for the respective batch
     */
    protected BigDecimal getPSF2Transactions(ReportInputDTO input, List PSF2TxnsList) {

        BigDecimal totalAmt = BigDecimal.ZERO;

         boolean flag = false;
         String query = outwardBaseQuery;
         String additionalQuery = " WHERE info.STATUS = ? " +
                                  " AND info.FLAG = 2 " +
                                  " AND info.BUSINESS_DATE = ? ";

//         String additionalQuery = " WHERE info.msgid IN ( " +
//                                  " SELECT srcmsgid " +
//                                  " FROM neftreportinfo " +
//                                  " WHERE tran_type = 'inward' " +
//                                  " AND SUBTYPE IN ('N03','N09') "+
//                                  " AND rescode IN ( " +
//                                  " SELECT code " +
//                                  " FROM REASONCODE " +
//                                  " WHERE TYPE LIKE '%N03rej%' OR TYPE LIKE '%N09rej%')" +
//                                  " AND TO_NUMBER(flag)= 2 ) " +
//                                  " AND info.VALUE_DATE = ? ";

         query = query + additionalQuery;

         if (!input.getBatchTime().equalsIgnoreCase("ALL")) {
             query += " and info.BATCHTIME = ? ";
         }

         // filtering based on branch or Central office view
         if ( input.getIfscId() > 0 ) {

//             query += " and info.RECVADDR LIKE ? ";
             query += " and info.IFSC_MASTER_ID = ? ";
         }

         try {
             PreparedStatement ps = con.prepareStatement(query);

             ps.setInt(1, RHSConstants.REJECTED_BY_RBI_STATUS);
//             ps.setDate(1, ConversionUtil.getDate(input.getValueDate())); // report Date
              ps.setDate(2, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date

             if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
                 ps.setString(3, input.getBatchTime()); // batchTime
                 flag = true;
             }
//             if ( !(input.getBranchCode().equalsIgnoreCase("ALL"))) {
//                 if (flag) {
//                     String ifsc = "%" + input.getBranchCode() + "%";
//                     ps.setString(4, ifsc);
//                 } else {
//                     String ifsc = "%" + input.getBranchCode() + "%";
//                     ps.setString(3, ifsc);
//                 }
//             }

             if ( input.getIfscId() > 0 ) {
                 if (flag) {
                     ps.setLong(4, input.getIfscId()); // branch Code
                 } else {
                     ps.setLong(3, input.getIfscId());
                 }
             }

            ResultSet rs = ps.executeQuery();
            totalAmt = constructOutwardDetailedTransactionInfo(rs, PSF2TxnsList);

            rs.close();
        } catch (Throwable th) {

            logger.error("Exception while getting txn details :"+ th.getMessage());
            throw new BOException("Exception while getting txn details :"+th.getMessage());
        }
        return totalAmt;
    }

    /*
     * This Method is used for generating the SummaryInwardReport
     * its Accepts the ResultSets as inputs and convert it in to lists then put the
     * converted lists in to the map then finally set that map in to ITDetailReportDTO
     *
     * Returns ITDetailReportDTO
     */
    public ITDetailReportDTO constructSummaryInwardReport(ResultSet rs, ResultSet rs1, ResultSet rs2, ResultSet rs3, String date) throws SQLException {

        // ITDetailReportDTO object only will hold the Map which will have the report details
        ITDetailReportDTO report = new ITDetailReportDTO();

        // Temporary Map which will hold the Lists which contains SummaryInwardReport
        Map receivedInfo = new TreeMap();

        // Add all the prepared Lists in to the temporary Map with appropriate Key's
        receivedInfo.put(ReportConstants.REPORT_TYPE1 + " " + date + " " + ReportConstants.RBI_TO_BRANCH,
                         constructSummaryInwardTransactionList(rs));
        receivedInfo.put(ReportConstants.REPORT_TYPE1 + " " + date + " " + ReportConstants.RBI_TO_SUNDRY,
                         constructSummaryInwardTransactionList(rs1));
        receivedInfo.put(ReportConstants.REPORT_TYPE2 + " " + date + " " + ReportConstants.SUNDRY_To_BRANCH,
                         constructSummaryInwardTransactionList(rs2));
        receivedInfo.put(ReportConstants.REPORT_TYPE2 + " " + date + " " + ReportConstants.SUNDRY_To_SUNDRY,
                         constructSummaryInwardTransactionList(rs3));

        // Set the temporary Map in to the ITDetailReportDTO Map
        report.setReceivedTransactionInfo(receivedInfo);

        return report;
    }

    /*
     * This Method is used for generating the DetailedInwardReport
     * its Accepts the ResultSets as inputs and convert in to lists then put the
     * converted lists in to the map then finally set that map in to ITDetailReportDTO
     *
     * Returns ITDetailReportDTO
     */
    public ITDetailReportDTO constructDetailedInwardReport(ResultSet rs, ResultSet rs1, ResultSet rs2, ResultSet rs3, String date) throws SQLException {

        // ITDetailReportDTO object only will hold the Map which will have the report details
        ITDetailReportDTO report = new ITDetailReportDTO();

        // Temporary Map for hold the List which will hold the report details
        Map receivedInfo = new TreeMap();

        // Add all the prepared Lists in to the temporary Map with appropriate Key's
        receivedInfo.put(ReportConstants.REPORT_TYPE1 + " " + date + " " + ReportConstants.RBI_TO_BRANCH,
                         constructDetailedInwardTransactionList(rs));
        receivedInfo.put(ReportConstants.REPORT_TYPE1 + " " + date + " " + ReportConstants.RBI_TO_SUNDRY ,
                         constructDetailedInwardTransactionList(rs1));
        receivedInfo.put(ReportConstants.REPORT_TYPE2 + " " + date + " " + ReportConstants.SUNDRY_To_BRANCH,
                         constructDetailedInwardTransactionList(rs2));
        receivedInfo.put(ReportConstants.REPORT_TYPE2 + " " + date + " " + ReportConstants.SUNDRY_To_SUNDRY,
                         constructDetailedInwardTransactionList(rs3));

        // Set the temporary Map in to the ITDetailReportDTO Map
        report.setReceivedTransactionInfo(receivedInfo);

        return report;
    }

    /*
     * This Method is used for set All the common PreparedStatement's condition parameter's
     * The Possible condition parameters are
     *
     *  !   1.Branch Code
     *  !   2.Report date
     *  !   3.Transaction Type (Normal or Return)
     *  !   4.batch Time
     *
     * reurns PreparedStatement
     */
    public PreparedStatement setFiltersForInwardTransactionReport(ReportInputDTO input,
                                                                   String query) throws Exception {

        /*
         * Here v just check whether the branch code is selected by the user
         * if yes then v update the query with the specific branch code filteration.
         */
//      Related to Branch/IFSC Specific.
        if (input.getIfscId() > 0 ) {

            query = query + " AND ifsc_master_id = "+input.getIfscId();
        }
        /*
         * Here v just check whether the Batch Time is selected by the user
         * if yes then v update the query with the specific Batch Time filteration
         */
        /*if (!input.getBatchTime().equalsIgnoreCase("All")) {
            query = query + " AND BatchTime = '"+input.getBatchTime()+"'";
        }*/

        /*
         * Here v just check whether the InwardType choosed by the user
         * and then v update the query as per the selection made.
         */
        /*if (input.getInwardType().equalsIgnoreCase(ReportConstants.COMPLETED)) {
            query = query +  " AND resCode IS NULL ";
        } else if (input.getInwardType().equalsIgnoreCase(ReportConstants.RETURNED)) {
            query = query +  " AND resCode IS not NULL ";
        }*/

        // Finally update the query with ORDER BY Batch Time and Receiver Address
        query = query + " ORDER BY batchtime,recvAddr";

        //Prepare the PreparedStatement
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, INWARD_TRANSACTION_TYPE);
        return ps;
    }

    private PreparedStatement setFiltersForInwardTxnReport(ReportInputDTO input,
                                                           String query) throws Exception {

        if (input.getValueDate() != null && input.getToDate() != null ) {

            query += " and BUSINESS_DATE BETWEEN '" + input.getValueDate()
                                         + "' and '" + input.getToDate() + "' ";
        }
        if (input.getIfscId() > 0 ) {

            query = query + " AND ifsc_master_id = "+input.getIfscId();
        }

        if (!input.getBatchTime().equalsIgnoreCase("All")) {
            query = query + " AND BatchTime = '"+input.getBatchTime()+"'";
        }

        if ( new BigDecimal(input.getFromAmount()).compareTo(BigDecimal.ZERO) > 0 && new BigDecimal(input.getToAmount()).compareTo(BigDecimal.ZERO) > 0 ) {

            query += " and AMOUNT BETWEEN " + input.getFromAmount() + " and "
                                                + input.getToAmount();
        } else if (new BigDecimal(input.getFromAmount()).compareTo(BigDecimal.ZERO) > 0) {

            query += " and AMOUNT >= " + input.getFromAmount();
        } else if (new BigDecimal(input.getToAmount()).compareTo(BigDecimal.ZERO) > 0) {

            query += " and AMOUNT <= " + input.getToAmount();
        }
        //Added for msgsource on 20100105

        if (input.getHostType() != null && !input.getHostType()
                                                        .equalsIgnoreCase("ALL")) {

            query += " and SOURCE = '" + input.getHostType() +"'" ;
        }

//        //Have done with inwardTxns-NEFT issue by priyak
//        if (input.getInwardType().equalsIgnoreCase(ReportConstants.COMPLETED)) {
//            query = query +  " AND resCode IS NULL " + " AND status_desc = '" +input.getInwardType() +"'";
//        } else if (input.getInwardType().equalsIgnoreCase(ReportConstants.RETURNED)) {
//
//            //query = query +  " AND resCode IS not NULL ";
//            query = query + " AND status_desc =" +"'" + "RetunAuthorization"+ "'";
//        }

       if(!input.getStatus().equalsIgnoreCase("ALL")) {
           query += " and STATUS in (" + input.getStatus() +")" ;
       }

        query = query + " ORDER BY batchtime,recvAddr";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, INWARD_TRANSACTION_TYPE);
        return ps;
    }

    /*
     * This method gets a ResultSet and put it in to SummaryInfoElement DTO then
     * Convert that in to a list
     *
     * Returns the List
     */
    private List constructSummaryInwardTransactionList(ResultSet rs) throws SQLException{

        List inwardTransactionList = new ArrayList(0);

        while(rs.next()) {

            SummaryInfo si = new SummaryInfo();

            // Here we just add Key, Total No.of Txn's and Total Amount, in to SummaryInfoElement Object
//            SummaryInfoElement sie = new SummaryInfoElement("", rs.getInt(1), rs.getDouble(2));
            SummaryInfoElement sie = new SummaryInfoElement("", rs.getInt(1), (rs.getString(2) == null)? "0":rs.getString(2));
            si.summaryElements = new ArrayList(0);
            si.summaryElements.add(sie);
            inwardTransactionList.add(sie);
        }
        return inwardTransactionList;
    }

    /*
     * This method just accepts a ResultSet as a parameter and put it in to
     * TransactionInfo DTO and convert taht into a list
     *
     * Returns the List
     */
    private List constructDetailedInwardTransactionList(ResultSet rs) throws SQLException {

        // List which will hold the DetailedInwardTransactionReport
        List inwardTranInfoList = new ArrayList(0);

        while(rs.next()) {

            TransactionInfo ti = new TransactionInfo();

            ti.txnDate = rs.getDate(1);
            ti.valueDate = rs.getDate(2);
            ti.batchTime = rs.getString(3);
            ti.utrNo = rs.getString(4);

            ti.senderInfo      = new CustomerInfo();
            ti.beneficiaryInfo = new CustomerInfo();

            ti.senderInfo.accIfsc = rs.getString(5);
            ti.beneficiaryInfo.accIfsc = rs.getString(6);
            ti.senderInfo.accName = rs.getString(7);
            ti.senderInfo.accNo = rs.getString(8);
            ti.senderInfo.accType = rs.getString(9);
            ti.beneficiaryInfo.accName = rs.getString(10);
            ti.beneficiaryInfo.accNo = rs.getString(11);
            ti.beneficiaryInfo.accType = rs.getString(12);
//            ti.amount = rs.getDouble(13);
            ti.amount = rs.getBigDecimal(13);
            ti.currentStatus = String.valueOf(rs.getLong(14));
            ti.statusShortDesc = rs.getString(15);
            ti.flag  = rs.getString(16);

            // Sets the DTO Object in to the List
            inwardTranInfoList.add(ti);
        }
        return inwardTranInfoList;
    }

    /**
     * Method to get Branchwisesubtype individual report
     */
    public Message generatePaymentReport(Message req)
    throws BOException {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        NEFTDetailsReportDTO reportDTO;
        List<NEFTDetailsReportDTO> reportList = new ArrayList<NEFTDetailsReportDTO>(0);
        //Map<String, List<ReportDTO>> reportMap = new LinkedHashMap<String, List<ReportDTO>>();

        try {

            //Report Query.
            String brSubQry = " select ifsc_master_id, im.ifsc ||'-'|| im.NAME as Branch,nm.msg_id,nm.utr_no, " +
                              " nm.value_date,nm.sender_address,nm.receiver_address,nm.amount, ns.NAME as STATUS, " +
                              " nm.remarks,nm.error_remarks,nm.status, m.msg_sub_type from neft_message nm,message m,ifscmaster im, neft_status ns" +
                              " where m.msg_id = nm.msg_id and im.ID = nm.ifsc_master_id  and ns.ID = nm.STATUS";

            String orderBY = " ORDER BY ifsc asc,value_date asc, msg_id asc ";

          /*  nm.value_date,nm.sender_address,nm.receiver_address,nm.amount, ns.NAME as STATUS,
            nm.remarks,nm.error_remarks,nm.status, m.msg_sub_type from neft_message nm,message m,ifscmaster im, neft_status ns
            where m.msg_id = nm.msg_id and im.ID = nm.ifsc_master_id and ns.ID = nm.STATUS*/
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                brSubQry += " and nm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
            }

            //Related to Transaction Type.
            if (inputDTO.getTransactionType() != null && !inputDTO.getTransactionType()
                                                             .equalsIgnoreCase("ALL")) {

                brSubQry += " and nm.TRAN_TYPE = '" + inputDTO.getTransactionType() +"'" ;
            }

            //Related to Message Sub Type.
            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
                                                             .equalsIgnoreCase("ALL")) {

                brSubQry += " and m.MSG_SUB_TYPE = '" + inputDTO.getPaymentType() +"'" ;
            } else if(inputDTO.getPaymentType().equalsIgnoreCase("ALL")) {

                brSubQry += " and m.MSG_SUB_TYPE IN ( 'N06', 'N07') ";
            }

            //Related to Source Type.
            if (inputDTO.getHostType() != null && !inputDTO.getHostType()
                                                            .equalsIgnoreCase("ALL")) {

                brSubQry += " and nm.MSG_SOURCE = '" + inputDTO.getHostType() +"'" ;
            }

            //Related to Branch/IFSC Specific.
            if (inputDTO.getIfscId() > 0 ) {

                brSubQry += " and nm.IFSC_Master_ID = " + inputDTO.getIfscId();
            }

            /*
             *     Related to Amount given by the user. If the user fills only the
             * FromAmount field then system fetch messages having amount greater than
             * or equalto given FromAmount. If the user fills only the ToAmount field
             * then system fetch the messages having amount less than or equalto given
             * ToAmount. If the user give both the amount then the system looks for
             * messages having anmount between the fromAmount and toAmount.
             */
            BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
            BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());

//            if (inputDTO.getFromAmount() > 0 && inputDTO.getToAmount() > 0 ) {
            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {
                brSubQry += " and nm.AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                brSubQry += " and nm.AMOUNT >= " + inputDTO.getFromAmount();
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                brSubQry += " and nm.AMOUNT <= " + inputDTO.getToAmount();
            }

            // Related to Message Status Specific.
            if (inputDTO.getStatus() != null && !inputDTO.getStatus()
                                                        .equalsIgnoreCase("ALL")) {

                brSubQry += " and nm.STATUS in (" + inputDTO.getStatus() +")" ;
            }


            brSubQry = brSubQry + orderBY;

            ps = con.prepareStatement(brSubQry);
            rs = ps.executeQuery();
            String actualField6061 = "6061";
            String actualField6081 = "6081";
            String actualField6021 = "6021";
            String actualField6091 = "6091";
            String actualField5565 = "5565";

            //String prevBranch = "";
            while(rs.next()) {

//                String currBranch = rs.getString("BRANCH");
//                /*
//                 * This is to group the records based on the Branches.
//                 */
//                if (prevBranch != null && prevBranch!= "" &&
//                                        !prevBranch.equalsIgnoreCase(currBranch)) {
//
//                    reportMap.put(prevBranch, reportList);
//                    reportList = new ArrayList<ReportDTO>(0);
//                }

                reportDTO = new NEFTDetailsReportDTO();
                String msgSubType = rs.getString("MSG_SUB_TYPE");
                reportDTO.setMsgSubType(msgSubType);
                Long msgId = rs.getLong("MSG_ID");
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("Value_date")));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
//                reportDTO.setAmount(Double.parseDouble(rs.getString("AMOUNT")));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setStatus(rs.getString("STATUS")); //For status
                if(inputDTO.getStatus().equalsIgnoreCase("900, 2800")) {
                    reportDTO.setRemarks(rs.getString("REMARKS"));
                }
                if(inputDTO.getStatus().equalsIgnoreCase("700, 2700") || inputDTO.getStatus().equalsIgnoreCase("0, 2900")) {
                    reportDTO.setRemarks(rs.getString("ERROR_REMARKS"));
                }
                if(inputDTO.getStatus().equalsIgnoreCase("ALL")) {

                    String remarks = rs.getString("REMARKS");
                    String errRemarks = rs.getString("ERROR_REMARKS");
                    if (remarks != null && remarks.trim().length() > 0) {
                        reportDTO.setRemarks(remarks);
                    }
                    if(errRemarks != null && errRemarks.trim().length() > 0) {
                        reportDTO.setRemarks(errRemarks);
                    }

                    if (remarks == null && errRemarks == null) {
                        reportDTO.setRemarks("");
                    }
                }
                if(inputDTO.getPaymentType().equalsIgnoreCase("ALL")) {

                    if(msgSubType.equalsIgnoreCase("N06") || msgSubType.equalsIgnoreCase("N07")) {

                        String field6061 = getFieldValues(msgId,actualField6061);
                        String field6081 = getFieldValues(msgId,actualField6081);
                        String field5565 = getFieldValues(msgId,actualField5565);

                        reportDTO.setField6061(field6061);
                        reportDTO.setField6081(field6081);
                        reportDTO.setField5565(field5565);
                    }

                } else if(msgSubType.equalsIgnoreCase("N02")) {

                    String field6021 = getFieldValues(msgId,actualField6021);
                    String field6091 = getFieldValues(msgId,actualField6091);

                    reportDTO.setField6021(field6021);
                    reportDTO.setField6091(field6091);

                } else if(msgSubType.equalsIgnoreCase("N06") || msgSubType.equalsIgnoreCase("N07")) {

                    String field6061 = getFieldValues(msgId,actualField6061);
                    String field6081 = getFieldValues(msgId,actualField6081);
                    String field5565 = getFieldValues(msgId,actualField5565);

                    reportDTO.setField6061(field6061);
                    reportDTO.setField6081(field6081);
                    reportDTO.setField5565(field5565);
                }
                reportList.add(reportDTO);
            }

        } catch(Throwable th) {

            logger.error("Exception while generating Branch SubType Individual Report :" + th.getMessage());
            throw new BOException("Exception while generating Branch SubType Individual Report." + th.getMessage());
        } finally {

            release(ps, rs);
        }
        req.info = reportList;
        return req;
    }

    public String getFieldValues(Long msgId,String actualField) {

        PreparedStatement ps =null;
        ResultSet rs = null;
        String fieldValue = "";
        try {

            StringBuffer query = new StringBuffer();
            query.append(" SELECT value  ");
            query.append("   FROM MSGFIELD_STAGE mfs, MSGFIELDTYPE mft ");
            query.append("   WHERE mfs.MSG_FIELD_TYPE_ID = mft.ID ");
            query.append("     AND mft.NO = ? ");
            query.append("     AND mfs.MSG_ID = ? ");

            ps = con.prepareStatement(query.toString());
            ps.setString(1, actualField);
            ps.setLong(2, msgId);
            rs = ps.executeQuery();
            while(rs.next()) {
                fieldValue = rs.getString(1);
            }

        } catch (Exception e1) {
            // TODO: handle exception
        } finally {
            try {

                release(ps, rs);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return fieldValue;
    }

    public String getFieldValuesFromArchival(Long msgId,String actualField) {

        PreparedStatement ps =null;
        ResultSet rs = null;
        String fieldValue = "";
        try {

            StringBuffer query = new StringBuffer();
            query.append(" SELECT value  ");
            query.append("   FROM MSGFIELD_STAGE_VW mfs, MSGFIELDTYPE mft ");
            query.append("   WHERE mfs.MSG_FIELD_TYPE_ID = mft.ID ");
            query.append("     AND mft.NO = ? ");
            query.append("     AND mfs.MSG_ID = ? ");

            ps = con.prepareStatement(query.toString());
            ps.setString(1, actualField);
            ps.setLong(2, msgId);
            rs = ps.executeQuery();
            while(rs.next()) {
                fieldValue = rs.getString(1);
            }

        } catch (Exception e1) {
            // TODO: handle exception
        } finally {
            try {

                release(ps, rs);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return fieldValue;
    }


    /**
     * Method to Generate Branch Inward Returned Reports.
     *
     * @param ReportInputDTO
     * @return Map<String, List<ReportDTO>>
     */
    public Message generateNeftBrInwReturnedReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Map<String, List<ReportDTO>> reportMap =
            new LinkedHashMap<String, List<ReportDTO>>();
        boolean flag = false;
        try {

            //Report Query.
            String qryBrInwardReturnedReport =
                "  SELECT r.value_date, r.msg_sub_type as MSG_Type, r.amount, " +
                "         r.utr_no as Inw_UTR_No, r.sender_address, r.receiver_address" +
                "         , r1.utr_no as Out_UTR_No, r.BATCH_TIME, r.IFSC_MASTER_ID  " +
                "      FROM (SELECT value_date, msg_sub_type, status, rm.msg_id, ifsc_master_id,  " +
                "                   sender_address, receiver_address, utr_no, amount, BATCH_TIME " +
                "               FROM neft_message rm, MESSAGE m, ifscmaster im " +
                "              WHERE m.msg_id = rm.msg_id " +
                "                    AND im.ID = rm.ifsc_master_id " +
                "                    AND m.msg_sub_type IN ('N02') " +
                //Below 2 conditions ment for the Inward Return - Status 900 is Inward Returned.
                "                    AND rm.status = 900 " +
                "                    AND rm.tran_type = 'inward' " +
                "            ORDER BY value_date ASC) r, " +
                "           (SELECT rm1.parent_id, rm1.utr_no, m1.msg_id as outward_id " +
                "               FROM neft_message rm1, MESSAGE m1 " +
                "              WHERE rm1.parent_id IS NOT NULL " +
                "                    AND m1.msg_sub_type IN ('N07') " +
                "                    AND rm1.tran_type = 'outward' " +
                "                    AND m1.msg_id = rm1.msg_id) r1 ";


            /**
             * Done on on 19-Jan-2011.
             *
             * Separated database calls to generate reports based on data available
             * in local/archive schema.
             *
             * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we no need to look for data available in ARCHIVE schema.
             *
             * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we need to look both local and archive schema to generate reports.
             */

            if (inputDTO.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                    qryBrInwardReturnedReport =
                        "  SELECT r.value_date, r.msg_sub_type as MSG_Type, r.amount, " +
                        "         r.utr_no as Inw_UTR_No, r.sender_address, r.receiver_address" +
                        "         , r1.utr_no as Out_UTR_No, r.BATCH_TIME, r.IFSC_MASTER_ID  " +
                        "      FROM (SELECT value_date, msg_sub_type, status, rm.msg_id, ifsc_master_id,  " +
                        "                   sender_address, receiver_address, utr_no, amount, BATCH_TIME " +
                        "               FROM neft_message_vw rm, MESSAGE_vw m, ifscmaster im " +
                        "              WHERE m.msg_id = rm.msg_id " +
                        "                    AND im.ID = rm.ifsc_master_id " +
                        "                    AND m.msg_sub_type IN ('N02') " +
                        //Below 2 conditions ment for the Inward Return - Status 900 is Inward Returned.
                        "                    AND rm.status = 900 " +
                        "                    AND rm.tran_type = 'inward' " +
                        "            ORDER BY value_date ASC) r, " +
                        "           (SELECT rm1.parent_id, rm1.utr_no, m1.msg_id as outward_id " +
                        "               FROM neft_message_vw rm1, MESSAGE_vw m1 " +
                        "              WHERE rm1.parent_id IS NOT NULL " +
                        "                    AND m1.msg_sub_type IN ('N07') " +
                        "                    AND rm1.tran_type = 'outward' " +
                        "                    AND m1.msg_id = rm1.msg_id) r1 ";

                    logger.info("Data fetching from archive database schema..");

                } else {

                    logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                    logger.info("Data fetching from Local Database schema...");
                }

            String whereBlock =
                " WHERE msg_id = parent_id ";

            String orderBY =
                " ORDER BY r.value_date asc," +
                "          r.msg_id asc ";

            /*
             *  Build the filter condition based on the input given by the user.
             */

            /*
             *  Related to Transaction Date.
             *  Assumption : ToDate should be greater than FromDate.
             */
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                whereBlock += " and r.VALUE_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
            }

            if ( !inputDTO.getBatchTime().equalsIgnoreCase("ALL")) {
                whereBlock += " and r.BATCH_TIME = ? ";
            }

//          filtering based on branch or Central office view
            if ( inputDTO.getIfscId() > 0) {
                whereBlock += " and r.IFSC_MASTER_ID = ? ";
            }

//            //Related to Message Sub Type.
//            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
//                                                             .equalsIgnoreCase("ALL")) {
//
//                whereBlock += " and r.MSG_SUB_TYPE = '" + inputDTO.getPaymentType() +"'" ;
//            }

//            //Related to Branch/IFSC Specific.
//            if (inputDTO.getIfscId() > 0 ) {
//
//                whereBlock += " and IFSC_Master_ID = " + inputDTO.getIfscId();
//            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryBrInwardReturnedReport += whereBlock + orderBY;

            //To avoid printing queries in the jboss console..by priyak
            //logger.info("Query for generating the Branch Inward Returned Report : " + qryBrInwardReturnedReport);
            logger.debug("Query for generating the Branch Inward Returned Report : " + qryBrInwardReturnedReport);

            ps = con.prepareStatement(qryBrInwardReturnedReport);

            if ( !inputDTO.getBatchTime().equalsIgnoreCase("ALL") ) {
                ps.setString(1, inputDTO.getBatchTime()); // batchTime
                flag = true;
            }
            if ( inputDTO.getIfscId() > 0) {
                if (flag) {
                    ps.setLong(2, inputDTO.getIfscId()); // branch Code
                } else {
                    ps.setLong(1, inputDTO.getIfscId());
                }
            }

            rs = ps.executeQuery();

            String prevDate = "";

            while(rs.next()) {

                String currDate = InstaReportUtil.
                                    indianFormatDate(rs.getDate("Value_date"));

                /*
                 *  If the Previous Date the Current Date are same then ignore.
                 *  If not same then add the current list to the map and create
                 * a new List. This is to group the record based on the date.
                 */
                if (prevDate != null && prevDate!= "" &&
                                        !prevDate.equalsIgnoreCase(currDate)) {

                    reportMap.put(prevDate, reportList);
                    reportList = new ArrayList<ReportDTO>(0);
                }

                reportDTO = new ReportDTO();

                reportDTO.setValueDate(currDate);
                reportDTO.setMsgType(rs.getString("MSG_Type"));
                reportDTO.setUtrNo(rs.getString("INW_UTR_NO"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setOutUTRNo(rs.getString("OUT_UTR_NO"));
                //Here Batch time added by priyak for user requirement.
                reportDTO.setBatchTime(rs.getString("BATCH_TIME"));

                reportList.add(reportDTO);

                //Set the Current Date to the PreviousDate.
                prevDate = currDate;
            }

            /*
             * This is to set the Last set of List.
             */
            if (reportList.size()>0){
                reportMap.put(prevDate, reportList);
            }

        } catch(Throwable th) {

            logger.error("Exception while generating Branch Summary Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating Branch Summary Report."
                                , th);
        } finally {

            release(ps, rs);
        }
        req.info = reportMap;
        return req;
    }

    /**
     * Method to Generate Batch wise Aggregate Reports.
     *
     * @param ReportInputDTO
     * @return Map<String, List<ReportDTO>>
     */
    public Message generateNeftBatchwiseAggregateReport(Message req)
    throws Exception {

        StringBuffer query = new StringBuffer(1024);
        Message res = new Message();
        query.append("SELECT   ifsc, COUNT (ifsc) count, SUM (amount) amt, batchtime, trantype                        ")
             .append("  FROM (SELECT m.msg_id,                                                                        ")
             .append("             DECODE (m.tran_type,                                                               ")
             .append("                     'inward', SUBSTR (m.receiver_address, 0, 11),                              ")
             .append("                     'outward', SUBSTR (m.sender_address, 0, 11)                                ")
             .append("                    ) ifsc, m.IFSC_MASTER_ID ifscid,                                            ")
             .append("             m.amount, m.value_date, m.business_date, m.tran_type trantype,                     ")
             .append("             getfieldvalue (m.msg_id, '3535', msg.msg_sub_type) batchtime                       ")
             .append("        FROM NEFT_MESSAGE m, MESSAGE msg                                                        ")
             .append("       WHERE msg.msg_id = m.msg_id                                                              ")
             .append("         AND m.status IN ('1000', '900', '3000') AND msg.MSG_SUB_TYPE in ('N02', 'N06', 'N07')) ");


        /**
         * Done on on 19-Jan-2011.
         *
         * Separated database calls to generate reports based on data available
         * in local/archive schema.
         *
         * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we no need to look for data available in ARCHIVE schema.
         *
         * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we need to look both local and archive schema to generate reports.
         */
        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        if (inputDTO.getValueDate() != null) {

            java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

            if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                //archive database schema
                query.append("SELECT   ifsc, COUNT (ifsc) count, SUM (amount) amt, batchtime, trantype                   ")
                .append("  FROM (SELECT m.msg_id,                                                                        ")
                .append("             DECODE (m.tran_type,                                                               ")
                .append("                     'inward', SUBSTR (m.receiver_address, 0, 11),                              ")
                .append("                     'outward', SUBSTR (m.sender_address, 0, 11)                                ")
                .append("                    ) ifsc, m.IFSC_MASTER_ID ifscid,                                            ")
                .append("             m.amount, m.value_date, m.business_date, m.tran_type trantype,                     ")
                .append("             getfieldvalue (m.msg_id, '3535', msg.msg_sub_type) batchtime                       ")
                .append("        FROM NEFT_MESSAGE_VW m, MESSAGE_VW msg                                                  ")
                .append("       WHERE msg.msg_id = m.msg_id                                                              ")
                .append("         AND m.status IN ('1000', '900', '3000') AND msg.MSG_SUB_TYPE in ('N02', 'N06', 'N07')) ");

                    logger.info("Data fetching from archive database schema..");

            } else {

                logger.info("Data fetching from Local Database schema...");
                }
            } else {

                logger.info("Data fetching from Local Database schema...");
            }

        StringBuffer groupByQuery = new StringBuffer(1024);

        groupByQuery.append("        GROUP BY ifsc, batchtime, trantype                                               ")
                    .append("ORDER BY batchtime, ifsc ASC                                                             ");

        StringBuffer whereCondition = new StringBuffer(1024);
        StringBuffer finalQuery     = new StringBuffer(4028);
        boolean whereConditionAdded = false;

        PreparedStatement ps = null;
        ResultSet         rs = null;

        Map<String, Object> result = new LinkedHashMap<String, Object>();

        try {

            //ReportInputDTO inputDTO = (ReportInputDTO) req.info;

            if(inputDTO.getBatchTime() != null && inputDTO.getBatchTime().length() > 0) {

                if(!inputDTO.getBatchTime().equalsIgnoreCase("ALL")) {

                    if(!whereConditionAdded) {

                        whereCondition.append("WHERE");
                        whereConditionAdded = true;
                    }

                    whereCondition.append(" batchtime like '").append(inputDTO.getBatchTime()).append("' ");
                }
            }

            if(inputDTO.getIfscId() != 0) {

                if(!whereConditionAdded) {

                    whereCondition.append("WHERE");
                    whereConditionAdded = true;
                } else {

                    whereCondition.append("AND");
                }

                whereCondition.append(" ifscid = ").append(inputDTO.getIfscId()).append(" ");

            }

            if(inputDTO.getValueDate() != null && inputDTO.getValueDate().length() > 0) {

                if(!whereConditionAdded) {

                    whereCondition.append("WHERE");
                    whereConditionAdded = true;
                } else {

                    whereCondition.append("AND");
                }

                whereCondition.append(" business_date = '").append(inputDTO.getValueDate()).append("' ");
            }

            finalQuery.append(query).append(whereCondition).append(groupByQuery);

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Batch Wise Aggregate Report Query : "  + finalQuery.toString());
            logger.debug("Batch Wise Aggregate Report Query : "  + finalQuery.toString());

            ps = con.prepareStatement(finalQuery.toString());
            rs = ps.executeQuery();

            if(inputDTO.getReportType().equalsIgnoreCase(ReportConstants.DETAILED_INWARD_REPORT)){
                String previousBatchTime = "";
                String previousIfsc = "";
                BatchwiseAggregateDTO dto = null;
                List<BatchwiseAggregateDTO> resultList = new ArrayList<BatchwiseAggregateDTO>(0);

                while(rs.next()) {

                    String batchTime = rs.getString("batchtime");

                    if(batchTime != null && batchTime.length() > 0) {

                        String ifsc = rs.getString("ifsc");

                        if(previousBatchTime.equals(batchTime) || previousBatchTime.equals("")) {

                            if(!previousIfsc.equals(ifsc) || previousIfsc.equals("")) {

                                if(previousIfsc.equals("")) {
                                    // Do Nothing
                                } else {

                                    if(dto.getDebitAmount() == null) {
                                        dto.setDebitAmount("0.00");
                                    }
                                    if(dto.getCreditAmount() == null) {
                                        dto.setCreditAmount("0.00");
                                    }
                                    resultList.add(dto);
                                }
                                dto = new BatchwiseAggregateDTO();
                                previousIfsc = ifsc;
                                dto.setIfsc(ifsc);
                            } else {

                                previousIfsc = ifsc;
                            }

                            String tranType = rs.getString("trantype");
                            if(tranType.equalsIgnoreCase("Outward")) {

                                dto.setNoOfDebits(rs.getLong("count"));
                                dto.setDebitAmount(rs.getString("amt") == null ? "0.00" : rs.getString("amt"));
                            } else {

                                dto.setNoOfCredits(rs.getLong("count"));
                                dto.setCreditAmount(rs.getString("amt") == null ? "0.00" : rs.getString("amt"));
                            }

                            previousBatchTime = batchTime;
                        } else {
                            previousIfsc = "";
                            if(!previousIfsc.equals(ifsc) || previousIfsc.equals("")) {

                                if(!previousIfsc.equals("")) {
                                    // Do Nothing
                                } else {

                                    if(dto.getDebitAmount() == null) {
                                        dto.setDebitAmount("0.00");
                                    }
                                    if(dto.getCreditAmount() == null) {
                                        dto.setCreditAmount("0.00");
                                    }
                                    resultList.add(dto);
                                    result.put(previousBatchTime, resultList);
                                    resultList = new ArrayList<BatchwiseAggregateDTO>(0);
                                }
                                dto = new BatchwiseAggregateDTO();
                                previousIfsc = ifsc;
                                dto.setIfsc(ifsc);
                            } else {

                                previousIfsc = ifsc;
                            }

                            String tranType = rs.getString("trantype");
                            if(tranType.equalsIgnoreCase("Outward")) {

                                dto.setNoOfDebits(rs.getLong("count"));
                                dto.setDebitAmount(rs.getString("amt") == null ? "0.00" : rs.getString("amt"));
                            } else {

                                dto.setNoOfCredits(rs.getLong("count"));
                                dto.setCreditAmount(rs.getString("amt") == null ? "0.00" : rs.getString("amt"));
                            }
                            // resultList.add(dto);
                            previousBatchTime = batchTime;
                        }
                    }
                }
                if(!previousIfsc.equals("")) {

                    if(dto.getDebitAmount() == null) {
                        dto.setDebitAmount("0.00");
                    }
                    if(dto.getCreditAmount() == null) {
                        dto.setCreditAmount("0.00");
                    }
                    resultList.add(dto);
                }
                result.put(previousBatchTime, resultList);

                res.info = result;
            } else {
                //List summaryResultList = new ArrayList();
                BatchwiseAggregateDTO summaryDTO = null;
                BatchwiseAggregateDTO tempDTO = null;
                Map<String, Object> summaryMap = new TreeMap<String, Object>();
                BigDecimal amount = BigDecimal.ZERO;
                long tempCount = 0;
                while(rs.next()) {
                    summaryDTO = new BatchwiseAggregateDTO();
                    summaryDTO.setBatchTime(rs.getString("batchtime"));

                    if(rs.getString("trantype").equalsIgnoreCase(ReportConstants.TXN_TYPE_INWARD)){
                        summaryDTO.setCreditAmount(rs.getString("amt"));
                        summaryDTO.setNoOfCredits(rs.getLong("count"));
                        summaryDTO.setDebitAmount("0.00");
                        summaryDTO.setNoOfDebits(0);
                    } else {
                        summaryDTO.setCreditAmount("0.00");
                        summaryDTO.setNoOfCredits(0);
                        summaryDTO.setDebitAmount(rs.getString("amt"));
                        summaryDTO.setNoOfDebits(rs.getLong("count"));
                    }
                    if(summaryMap.containsKey(summaryDTO.getBatchTime())){
                        tempDTO = (BatchwiseAggregateDTO)summaryMap.get(summaryDTO.getBatchTime());
                        if(rs.getString("trantype").equalsIgnoreCase(ReportConstants.TXN_TYPE_INWARD)){
                            amount = new BigDecimal(tempDTO.getCreditAmount()).add(new BigDecimal(summaryDTO.getCreditAmount()));
                            tempCount = tempDTO.getNoOfCredits() + summaryDTO.getNoOfCredits();
                            tempDTO.setCreditAmount(amount.toString());
                            tempDTO.setNoOfCredits(tempCount);
//                            tempDTO.setNoOfDebits(tempDTO.getNoOfDebits());
//                            tempDTO.setDebitAmount(tempDTO.getDebitAmount());
                        } else {
                            amount = new BigDecimal(tempDTO.getDebitAmount()).add(new BigDecimal(summaryDTO.getDebitAmount()));
                            tempCount = tempDTO.getNoOfDebits() + summaryDTO.getNoOfDebits();
                            tempDTO.setDebitAmount(amount.toString());
                            tempDTO.setNoOfDebits(tempCount);
//                            tempDTO.setNoOfCredits(tempDTO.getNoOfCredits());
//                            tempDTO.setCreditAmount(tempDTO.getCreditAmount());
                        }
                        summaryMap.remove(summaryDTO.getBatchTime());
                        summaryMap.put(summaryDTO.getBatchTime(), tempDTO);
                        tempCount = 0;
                        amount = BigDecimal.ZERO;
                        tempDTO = null;
                    } else {
                        summaryMap.put(summaryDTO.getBatchTime(), summaryDTO);
                    }
                }
                res.info = summaryMap;
            }
            return res;
        } catch(Throwable th) {

            logger.error("Exception while generating Batch Wise Aggregate Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating Batch Wise Aggregate Report."
                                , th);
        } finally {

            release(ps, rs);
        }
    }

    /**
     * To override compareTo for comparator.
     */
    public int compareTo(String batchTime1, String batchTime2) {

        logger.info("Param1 : " + batchTime1 + " Param2 : " + batchTime2);

        NEFTN04DetailsDTO dto = new NEFTN04DetailsDTO();
        int result = dto.getField3535().compareTo(dto.getField3535());
        //System.out.println(result);
        return result;

    }

    /**
     * Method to Generate Batch wise Reconcillation Reports.
     *
     * @param ReportInputDTO
     * @return Map<String, List<ReportDTO>>
     */
    public Message generateNeftBatchwiseReconcillationReport(Message req)
    throws Exception {

        StringBuffer N04_query = new StringBuffer(1024);

        N04_query.append("SELECT  getfieldvalue (m.msg_id, '3535', msg.msg_sub_type) batchtime,             ")
                 .append("        getfieldvalue (m.msg_id, '5175', msg.msg_sub_type) owdebittxn,            ")
                 .append("        getfieldvalue (m.msg_id, '4105', msg.msg_sub_type) owdebittxnamt,         ")
                 .append("        getfieldvalue (m.msg_id, '5180', msg.msg_sub_type) owdebittxnAccepted,    ")
                 .append("        getfieldvalue (m.msg_id, '4110', msg.msg_sub_type) owdebittxnAmtAccepted, ")
                 .append("        getfieldvalue (m.msg_id, '5185', msg.msg_sub_type) owdebittxnRejected,    ")
                 .append("        getfieldvalue (m.msg_id, '4115', msg.msg_sub_type) owdebittxnAmtRejected, ")
                 .append("        getfieldvalue (m.msg_id, '5267', msg.msg_sub_type) iwtxnReceived,         ")
                 .append("        getfieldvalue (m.msg_id, '4410', msg.msg_sub_type) iwtxnAmtReceived,      ")
                 .append("        getfieldvalue (m.msg_id, '5047', msg.msg_sub_type) iwtxnReturned,         ")
                 .append("        getfieldvalue (m.msg_id, '4460', msg.msg_sub_type) iwtxnAmtReturned ,m.msg_id      ")
                 .append(" FROM NEFT_MESSAGE m, MESSAGE msg                                                 ")
                 .append("WHERE m.msg_id = msg.msg_id AND msg.msg_sub_type IN ('N04') AND m.STATUS = 1000  ");

        /**
         * Done on on 19-Jan-2011.
         *
         * Separated database calls to generate reports based on data available in local/archive schema.
         *
         * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we no need to look for data available in ARCHIVE schema.
         *
         * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we need to look both local and archive schema to generate reports.
         */
//        ReportInputDTO repInputDTO = (ReportInputDTO) req.info;
        Object[] obj1 = (Object[]) req.info;
        ReportInputDTO repInputDTO = (ReportInputDTO)obj1[0];
        if (repInputDTO.getValueDate() != null) {

            java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

            if (new Date(repInputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                //archive database schema
                N04_query.append("SELECT  getfieldvalue (m.msg_id, '3535', msg.msg_sub_type) batchtime,             ")
                .append("        getfieldvalue (m.msg_id, '5175', msg.msg_sub_type) owdebittxn,            ")
                .append("        getfieldvalue (m.msg_id, '4105', msg.msg_sub_type) owdebittxnamt,         ")
                .append("        getfieldvalue (m.msg_id, '5180', msg.msg_sub_type) owdebittxnAccepted,    ")
                .append("        getfieldvalue (m.msg_id, '4110', msg.msg_sub_type) owdebittxnAmtAccepted, ")
                .append("        getfieldvalue (m.msg_id, '5185', msg.msg_sub_type) owdebittxnRejected,    ")
                .append("        getfieldvalue (m.msg_id, '4115', msg.msg_sub_type) owdebittxnAmtRejected, ")
                .append("        getfieldvalue (m.msg_id, '5267', msg.msg_sub_type) iwtxnReceived,         ")
                .append("        getfieldvalue (m.msg_id, '4410', msg.msg_sub_type) iwtxnAmtReceived,      ")
                .append("        getfieldvalue (m.msg_id, '5047', msg.msg_sub_type) iwtxnReturned,         ")
                .append("        getfieldvalue (m.msg_id, '4460', msg.msg_sub_type) iwtxnAmtReturned ,m.msg_id      ")
                .append(" FROM NEFT_MESSAGE_VW m, MESSAGE_VW msg                                             ")
                .append("WHERE m.msg_id = msg.msg_id AND msg.msg_sub_type IN ('N04') AND m.STATUS = 1000  ");

                    logger.info("Data fetching from archive database schema..");

            } else {

                    logger.info("Data fetching from Local Database schema...");
                }
            } else {

                    logger.info("Data fetching from Local Database schema...");
            }


        StringBuffer N04_Order_Query = new StringBuffer(1024);
        N04_Order_Query.append("ORDER BY m.msg_id, batchtime ASC ");

        StringBuffer N04_whereCondition = new StringBuffer(1024);
        StringBuffer N04_finalQuery     = new StringBuffer(4028);


        StringBuffer LMS_Query = new StringBuffer(4028);
        LMS_Query.append("SELECT   batchtime, SUM (amount) amount, trantype, status, count(status) count                   ")
                 .append("  FROM (SELECT m.amount amount, m.tran_type trantype,                                            ")
                 .append("  DECODE(m.status, '200', '1000', '300', '1000', '500', '1000', '550', '1000', m.status) status, ")
                 .append("               getfieldvalue (m.msg_id, '3535', msg.msg_sub_type) batchtime,                     ")
                 .append("               m.business_date                                                                   ")
                 .append("          FROM NEFT_MESSAGE m, MESSAGE msg                                                       ")
                 .append("         WHERE m.msg_id = msg.msg_id                                                             ")
                 .append("           AND m.status IN (200, 300, 500, 550, 900, 1000, 2900, 3000)                           ")
                 .append("           AND msg.msg_sub_type IN ('N02', 'N06', 'N07'))                                        ");

        /**
         * Done on on 19-Jan-2011.
         *
         * Separated database calls to generate reports based on data available in local/archive schema.
         *
         * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we no need to look for data available in ARCHIVE schema.
         *
         * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we need to look both local and archive schema to generate reports.
         */

        if (repInputDTO.getValueDate() != null) {

            java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

            if (new Date(repInputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                //archive database schema
                LMS_Query.append("SELECT   batchtime, SUM (amount) amount, trantype, status, count(status) count                   ")
                .append("  FROM (SELECT m.amount amount, m.tran_type trantype,                                            ")
                .append("  DECODE(m.status, '200', '1000', '300', '1000', '500', '1000', '550', '1000', m.status) status, ")
                .append("               getfieldvalue (m.msg_id, '3535', msg.msg_sub_type) batchtime,                     ")
                .append("               m.business_date                                                                   ")
                .append("          FROM NEFT_MESSAGE_VW m, MESSAGE_VW msg                                                 ")
                .append("         WHERE m.msg_id = msg.msg_id                                                             ")
                .append("           AND m.status IN (200, 300, 500, 550, 900, 1000, 2900, 3000)                           ")
                .append("           AND msg.msg_sub_type IN ('N02', 'N06', 'N07'))                                        ");

                    logger.info("Data fetching from archive database schema..");

            } else {

                    logger.info("Data fetching from Local Database schema...");
                }
            } else {

                    logger.info("Data fetching from Local Database schema...");
            }

        StringBuffer groupByQuery = new StringBuffer(1024);

        groupByQuery.append("GROUP BY batchtime, trantype, status                                                          ")
                    .append("ORDER BY batchtime ASC                                                                        ");

        StringBuffer whereCondition = new StringBuffer(1024);
        StringBuffer finalQuery     = new StringBuffer(4028);
        boolean whereConditionAdded = false;

        PreparedStatement ps = null;
        ResultSet         rs = null;
        Map<String, List> resultMap = new HashMap<String, List>(0);

        try {

            Object[] obj = (Object[]) req.info;
            ReportInputDTO inputDTO = (ReportInputDTO)obj[0];
            List batchTimings = (ArrayList)obj[1];

            if(inputDTO.getBatchTime() != null && inputDTO.getBatchTime().length() > 0) {

                if(!inputDTO.getBatchTime().equalsIgnoreCase("ALL")) {

                    if(!whereConditionAdded) {

                        whereCondition.append("WHERE");
                        whereConditionAdded = true;
                    }

                    N04_whereCondition.append(" AND ");
                    N04_whereCondition.append(" getfieldvalue (m.msg_id, '3535', msg.msg_sub_type) like '").append(inputDTO.getBatchTime()).append("' ");
                    whereCondition.append(" batchtime like '").append(inputDTO.getBatchTime()).append("' ");
                }
            }

            if(inputDTO.getValueDate() != null && inputDTO.getValueDate().length() > 0) {

                if(!whereConditionAdded) {

                    whereCondition.append("WHERE");
                    whereConditionAdded = true;
                } else {

                    whereCondition.append("AND");
                }

                N04_whereCondition.append("AND");
                N04_whereCondition.append(" business_date = '").append(inputDTO.getValueDate()).append("' ");
                whereCondition.append(" business_date = '").append(inputDTO.getValueDate()).append("' ");
            }

            if(inputDTO.getTransactionType() != null && inputDTO.getTransactionType().length() > 0
                && !(inputDTO.getTransactionType().equalsIgnoreCase("all"))) {

                if(!whereConditionAdded) {

                    whereCondition.append("WHERE");
                    whereConditionAdded = true;
                } else {

                    whereCondition.append("AND");
                }

                whereCondition.append(" trantype = '").append(inputDTO.getTransactionType()).append("' ");
            }

            N04_finalQuery.append(N04_query).append(N04_whereCondition).append(N04_Order_Query);

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Batch Wise Reconcillation Report Query : " + N04_finalQuery.toString());
            logger.debug("Batch Wise Reconcillation Report Query : " + N04_finalQuery.toString());

            ps = con.prepareStatement(N04_finalQuery.toString());
            rs = ps.executeQuery();

            List N04_list = new ArrayList(0);
            long outDebitTxn = 0;
            BigDecimal owDebitTxnAmt = BigDecimal.ZERO;
            long owdebittxnAccepted = 0;
            BigDecimal owdebittxnAmtAccepted = BigDecimal.ZERO;
            long owdebittxnRejected = 0;
            BigDecimal owdebittxnAmtRejected = BigDecimal.ZERO;
            long iwtxnReceived = 0;
            BigDecimal iwtxnAmtReceived = BigDecimal.ZERO;
            long iwtxnReturned = 0;
            BigDecimal iwtxnAmtReturned = BigDecimal.ZERO;
            while(rs.next()) {
                NEFTN04DetailsDTO no4DTO = new NEFTN04DetailsDTO();
                String batchTime = rs.getString("batchtime");
                if(batchTime == null || "".equalsIgnoreCase(batchTime.trim())){
                    no4DTO.setField3535(((DisplayValueReportDTO)batchTimings.get(batchTimings.size()-1)).getValue());
                } else if (batchTime.trim() != "" && batchTime.trim().length() > 0) {
//                  Modified by priyak for null or empty ("") batch time
                    no4DTO.setField3535(rs.getString("batchtime"));
                }
                if (batchTime.trim() != "" && batchTime.trim().length() > 0) {

                    no4DTO.setField5175(rs.getString("owdebittxn"));
                    outDebitTxn += Long.parseLong(no4DTO.getField5175());
                    no4DTO.setField4105(rs.getString("owdebittxnamt"));
                    owDebitTxnAmt.add(new BigDecimal(no4DTO.getField4105()));
                    no4DTO.setField5180(rs.getString("owdebittxnAccepted"));
                    owdebittxnAccepted += Long.parseLong(no4DTO.getField5180());
                    String debAmt = rs.getString("owdebittxnAmtAccepted");
                    if (debAmt.indexOf(",") != -1) {
                        debAmt = debAmt.replace(",", ".");
                    }
                    no4DTO.setField4110(debAmt);
                    owdebittxnAmtAccepted.add(new BigDecimal(no4DTO.getField4110()));
                    no4DTO.setField5185(rs.getString("owdebittxnRejected"));
                    owdebittxnRejected += Long.parseLong(no4DTO.getField5185());
                    no4DTO.setField4115(rs.getString("owdebittxnAmtRejected"));
                    owdebittxnAmtRejected.add(new BigDecimal(no4DTO.getField4115()));
                    no4DTO.setField5267(rs.getString("iwtxnReceived"));
                    iwtxnReceived += Long.parseLong(no4DTO.getField5267());
                    no4DTO.setField4410(rs.getString("iwtxnAmtReceived"));
                    iwtxnAmtReceived.add(new BigDecimal(no4DTO.getField4410()));
                    no4DTO.setField5047(rs.getString("iwtxnReturned"));
                    iwtxnReturned += Long.parseLong(no4DTO.getField5047());
                    no4DTO.setField4460(rs.getString("iwtxnAmtReturned"));
                    iwtxnAmtReturned.add(new BigDecimal(no4DTO.getField4460()));
                    N04_list.add(no4DTO);
                } else {

                    String debitTxn = String.valueOf(Long.parseLong(rs.getString("owdebittxn")) - outDebitTxn);
                    no4DTO.setField5175(debitTxn);
                    String debitAmt = String.valueOf(new BigDecimal(rs.getString("owdebittxnamt")).subtract(owDebitTxnAmt));
                    no4DTO.setField4105(debitAmt);
                    String debitTxnAcc = String.valueOf(Long.parseLong(rs.getString("owdebittxnAccepted")) - owdebittxnAccepted);
                    no4DTO.setField5180(debitTxnAcc);
                    String debtxnAmt = rs.getString("owdebittxnAmtAccepted");
                    if (debtxnAmt.indexOf(",") != -1) {
                        debtxnAmt = debtxnAmt.replace(",", ".");
                    }
                    String debitTxnAmtAcc = String.valueOf(new BigDecimal(debtxnAmt).subtract(owdebittxnAmtAccepted));
                    no4DTO.setField4110(debitTxnAmtAcc);
                    String debitTxnRej = String.valueOf(Long.parseLong(rs.getString("owdebittxnRejected")) - owdebittxnRejected);
                    no4DTO.setField5185(debitTxnRej);
                    String debitTxnAmtRej = String.valueOf(new BigDecimal(rs.getString("owdebittxnAmtRejected")).subtract(owdebittxnAmtRejected));
                    no4DTO.setField4115(debitTxnAmtRej);
                    String inwTxn = String.valueOf(Long.parseLong(rs.getString("iwtxnReceived")) - iwtxnReceived);
                    no4DTO.setField5267(inwTxn);
                    String inwAmt = String.valueOf(new BigDecimal(rs.getString("iwtxnAmtReceived")).subtract(iwtxnAmtReceived));
                    no4DTO.setField4410(inwAmt);
                    String inwTxnRet = String.valueOf(Long.parseLong(rs.getString("iwtxnReturned")) - iwtxnReturned);
                    no4DTO.setField5047(inwTxnRet);
                    String iwTxnAmtRet = String.valueOf(new BigDecimal(rs.getString("iwtxnAmtReturned")).subtract(iwtxnAmtReturned));
                    no4DTO.setField4460(iwTxnAmtRet);
                    N04_list.add(no4DTO);
                }
                Collections.sort(N04_list, CASE_INSENSITIVE_ORDER);
            }

            resultMap.put("N04", N04_list);
            release(ps, rs);

            finalQuery.append(LMS_Query).append(whereCondition).append(groupByQuery);

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Batch Wise Reconcillation Report Query : " + finalQuery.toString());
            logger.debug("Batch Wise Reconcillation Report Query : " + finalQuery.toString());

            ps = con.prepareStatement(finalQuery.toString());
            rs = ps.executeQuery();

            List resultList = new ArrayList(0);
            BatchwiseReconcillationDTO dto = new BatchwiseReconcillationDTO();
            String previousBatchTime = "";
            int count = 0;
            while(rs.next()) {

                String batchTime = rs.getString("batchtime");

                if(batchTime != null && batchTime.length() > 0) {

                    if(previousBatchTime.equals(batchTime) || previousBatchTime.equals("")) {

                        if(count == 0) {
                            dto.setBatchTime(batchTime);
                            count = 1;
                        }
                        String tranType = rs.getString("trantype");
                        long status = rs.getLong("Status");

                        if(tranType.equalsIgnoreCase("inward") && status == 1000) {

                            dto.setIwTxnReceived(rs.getLong("count"));
                            String amount = rs.getString("amount");
                            dto.setIwTxnAmtReceived(amount == null ?  "0.00" : amount.length() == 0 ? "0.00" : amount);
                        } else if(tranType.equalsIgnoreCase("inward") && status == 900) {

                            dto.setIwTxnReturned(rs.getLong("count"));
                            String amount = rs.getString("amount");
                            dto.setIwTxnAmtReturned(amount == null ?  "0.00" : amount.length() == 0 ? "0.00" : amount);
                        } else if(tranType.equalsIgnoreCase("outward") && status == 3000) {

                            dto.setOwTxnAccepted(rs.getLong("count"));
                            String amount = rs.getString("amount");
                            dto.setOwTxnAmtAccepted(amount == null ?  "0.00" : amount.length() == 0 ? "0.00" : amount);
                        } else if(tranType.equalsIgnoreCase("outward") && status == 2900) {

                            dto.setOwTxnRejected(rs.getLong("count"));
                            String amount = rs.getString("amount");
                            dto.setOwTxnAmtRejected(amount == null ?  "0.00" : amount.length() == 0 ? "0.00" : amount);
                        }

                        previousBatchTime = batchTime;
                    } else {

                        if(dto.getIwTxnAmtReceived() == null) {
                            dto.setIwTxnAmtReceived("0.00");
                        }
                        if(dto.getIwTxnAmtReturned() == null) {
                            dto.setIwTxnAmtReturned("0.00");
                        }
                        if(dto.getOwTxnAmtAccepted() == null) {
                            dto.setOwTxnAmtAccepted("0.00");
                        }
                        if(dto.getOwTxnAmtRejected() == null) {
                            dto.setOwTxnAmtRejected("0.00");
                        }
                        resultList.add(dto);
                        count = 0;
                        dto = new BatchwiseReconcillationDTO();

                        String tranType = rs.getString("trantype");
                        long status = rs.getLong("Status");

                        if(count == 0) {

                            dto.setBatchTime(batchTime);
                            count = 1;
                        }
                        if(tranType.equalsIgnoreCase("inward") && status == 1000) {

                            dto.setIwTxnReceived(rs.getLong("count"));
                            String amount = rs.getString("amount");
                            dto.setIwTxnAmtReceived(amount == null ?  "0.00" : amount.length() == 0 ? "0.00" : amount);
                        } else if(tranType.equalsIgnoreCase("inward") && status == 900) {

                            dto.setIwTxnReturned(rs.getLong("count"));
                            String amount = rs.getString("amount");
                            dto.setIwTxnAmtReturned(amount == null ?  "0.00" : amount.length() == 0 ? "0.00" : amount);
                        } else if(tranType.equalsIgnoreCase("outward") && status == 3000) {

                            dto.setOwTxnAccepted(rs.getLong("count"));
                            String amount = rs.getString("amount");
                            dto.setOwTxnAmtAccepted(amount == null ?  "0.00" : amount.length() == 0 ? "0.00" : amount);
                        } else if(tranType.equalsIgnoreCase("outward") && status == 2900) {

                            dto.setOwTxnRejected(rs.getLong("count"));
                            String amount = rs.getString("amount");
                            dto.setOwTxnAmtRejected(amount == null ?  "0.00" : amount.length() == 0 ? "0.00" : amount);
                        }

                        previousBatchTime = batchTime;
                    }
                }
            }

            if(!previousBatchTime.equals("")) {

                if(dto.getIwTxnAmtReceived() == null) {
                    dto.setIwTxnAmtReceived("0.00");
                }
                if(dto.getIwTxnAmtReturned() == null) {
                    dto.setIwTxnAmtReturned("0.00");
                }
                if(dto.getOwTxnAmtAccepted() == null) {
                    dto.setOwTxnAmtAccepted("0.00");
                }
                if(dto.getOwTxnAmtRejected() == null) {
                    dto.setOwTxnAmtRejected("0.00");
                }
                resultList.add(dto);
            }

            resultMap.put("SUBTYPE", resultList);

            req.info = resultMap;
            return req;

        } catch(Throwable th) {

            logger.error("Exception while generating Batch Wise Reconcillation Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating Batch Wise Reconcillation Report."
                                , th);
        } finally {

            release(ps, rs);
        }
    }

    /**
     * Method for Outward Txn Details Report ( VIjayaBank )
     */
    public Message generateNEFTOutwardTxnDetailsReport(Message msg)
    throws Exception {

        ReportInputDTO input = (ReportInputDTO) msg.info;
        Message res = new Message();


        /*
         *      < Summary Report Details >
         * TODDMMYYYYPS1  - Sent from Branch Received by RBI
         * TODDMMYYYYPRJ  - Sent From Branch Rejected by RBI
         * TODDMMYYYYPSF1 - Sent From Branch Rejscheduled by RBI
         * TODDMMYYYYPS2  - Sent From Sundry Received by RBI
         * TODDMMYYYYPSF2 - Sent From Sundry Rejected by RBI
         */

        // To hold Total amount of Txns Sent from branch & received by RBI
//        double PS1amount = 0;
        BigDecimal PS1amount = BigDecimal.ZERO;
        // To hold Total amount of Txns sent from branch and rejected by RBI
//        double PRJamount = 0;
        BigDecimal PRJamount = BigDecimal.ZERO;
        // To hold Total amount of Txns Sent from branch & rescheduled by RBI
//        double PSF1amount = 0;
        BigDecimal PSF1amount = BigDecimal.ZERO;
        // To hold Total amount of Txns sent from branch and rejceived by RBI
//        double PS2amount = 0;
        BigDecimal PS2amount = BigDecimal.ZERO;
        // To hold Total amount of Txns Sent from Sundry and rejected by RBI
//        double PSF2amount = 0;
        BigDecimal PSF2amount = BigDecimal.ZERO;
        /*double rescheduledAmount = 0;
        int rescheduledCount = 0;*/

        try {

             OTDetailReportDTO report = new OTDetailReportDTO();
             String heading = OTDetailReportDTO.TO+InstaReportUtil.formatDate(InstaReportUtil.convertToDate(input.getValueDate()),"ddMMyyyy");

             /*
              * holds the report type requested . The possible values
              * include
              *
              *     @ Summary
              *     @ Detailed
              */
             //Commented by priyak
             input.setReportType(OTDetailReportDTO.REPORT_TYPE_DETAIL);
             String reportType = input.getReportType(); //Since outward txn detailed should be for Detailed report.

             List transactionPS1Info = new ArrayList(1);
             List transactionPRJInfo = new ArrayList(1);
             List transactionPSF1Info = new ArrayList(1);
             List transactionPS2Info = new ArrayList(1);
             List transactionPSF2Info = new ArrayList(1);
             // generate the output report
             Map elementMap = new TreeMap();

             if (reportType.equalsIgnoreCase(OTDetailReportDTO.REPORT_TYPE_DETAIL)) {

                 //transactionPS1Info.clear();
                 PS1amount = getOutwardTransactions(input, transactionPS1Info);

                 elementMap.put(OTDetailReportDTO.HEADING, transactionPS1Info);
             }

            if ((reportType.equalsIgnoreCase(OTDetailReportDTO.REPORT_TYPE_SUMMARY)) ||
                 (reportType.equalsIgnoreCase("ALL"))) {

                // For Getting Sent from Branch and Received by RBI Txn Details
                PS1amount = getPS1Transactions(input, transactionPS1Info);

                // For Getting Sent From Branch and Rejected by RBI Txn Details
                PRJamount = getPRJTransactions(input, transactionPRJInfo);

                // For Getting Sent From Branch and Rejscheduled by RBI Txn Details
                PSF1amount = getPSF1Transactions(input, transactionPSF1Info);

                // For Getting From Sundry and Received by RBI Txn Details
                PS2amount = getPS2Transactions(input, transactionPS2Info);

                // For Getting Sent From Sundry Rejected by RBI Txn Details
                PSF2amount = getPSF2Transactions(input, transactionPSF2Info);

//                double totalAmount = PS1amount + PRJamount + PSF1amount + PS2amount + PSF2amount;
//                int    totalCount  = transactionPS1Info.size() + transactionPRJInfo.size() + transactionPSF1Info.size();
//                       totalCount +=  transactionPS2Info.size() + transactionPSF2Info.size();

                OTDetailReportDTO element = new OTDetailReportDTO();

                element.summaryInfo = new SummaryInfo();
                element.summaryInfo.summaryElements = new ArrayList();

                SummaryInfoElement sm = new SummaryInfoElement();
                // for PS1 transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PS1 ,
                                         transactionPS1Info.size(), PS1amount.toString() );
                element.summaryInfo.summaryElements.add(sm);

                // for PRJ transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PRJ ,
                                         transactionPRJInfo.size(), PRJamount.toString() );

                element.summaryInfo.summaryElements.add(sm);

                // for PSF1 transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PSF1 ,
                                         transactionPSF1Info.size(), PSF1amount.toString() );
                element.summaryInfo.summaryElements.add(sm);

                // for PS2 transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PS2 ,
                                             transactionPS2Info.size(), PS2amount.toString() );

                element.summaryInfo.summaryElements.add(sm);

                // for PSF2 transaction
                sm = new SummaryInfoElement(heading + OTDetailReportDTO.HEAD_PSF2 ,
                                             transactionPSF2Info.size(), PSF2amount.toString());
                element.summaryInfo.summaryElements.add(sm);

                // for total transactions
//                 sm = new SummaryInfoElement(OTReportDTO.TOTAL_TRANACTIONS,  totalCount, totalAmount);
//                 element.summaryInfo.summaryElements.add(sm);

                // set the Summary Details to the Map
                elementMap.put(OTDetailReportDTO.SUMMARY_INFO, element.summaryInfo.summaryElements);
         }
         //set the Map
         report.setOutwardMap(elementMap);
         res.info = report;
         return res;
         } catch(Exception e) {

             logger.error("Exception while generating NEFTInwSummaryReport :"+e);
             throw new BOException("Exception while generating NEFTInwSummaryReport "+e);
         }
    }

//    /**
//     * method to get all Outward transactions.
//     *
//     * @param input     - ReportInputDTO which holds the all the input paramter
//     *                    choosed by the user
//     *
//     * @param con       - Connection
//     *
//     * @param batchTime - String which holds the respective batchTiming for which
//     *                    the debit transactions must be produced
//     *
//     * @param PS1TxnsList - An empty list which will contain the result
//     *
//     * @return double - total amount of the PS1 transactions for the respective batch
//     */
//    protected double getOutwardTransactions(ReportInputDTO input, List PS1TxnsList) {
//
//        double totalAmt = 0;
////      Query for getting OutwardTxnDetails
//        String query =  " SELECT distinct info.batchtime, " +
//                        " info.value_date, " +
//                        " info.senderAddr, " +
//                        " info.recvAddr, " +
//                        " info.utrno, " +
//                        " info.amount, " +
//                        " info.sender_acc_name, " +
//                        " info.sender_acc_no, " +
//                        " info.sender_acc_type, " +
//                        " info.ben_acc_name, " +
//                        " info.ben_acc_no, " +
//                        " info.ben_acc_type, " +
//                        " info.status_desc, " +
//                        " info.ifsc_master_id, " +
//                        " nbt.RESH_BATCH_TIME, " +
//                        " nbt.RESH_DATE, "+
//                        " nbt.REJ_BATCH_TIME, " +
//                        " nbt.REJ_DATE " +
//                        " FROM NEFTREPORTINFO info, NEFTBALTRAN nbt " +
//                        " WHERE info.UTRNO = nbt.UTR_NO AND nbt.TRAN_TYPE LIKE 'O' "+
//                        " AND info.BUSINESS_DATE BETWEEN ? AND ? ";
//
//
////        if (input.equalsIgnoreCase("ALL")){
////            query += " AND info.";
////        }
//        if (input.getToAmount() > 0){
//            query += " AND info.amount BETWEEN ? AND ? ";
//        }
//
//        if (!input.getStatus().equalsIgnoreCase("ALL")){
//            query += " AND info.STATUS = ? ";
//        }
//
//        if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
//            query += " and info.BATCHTIME = ? ";
//        }
//
////      filtering based on branch or Central office view
//        if ( input.getIfscId() > 0 ) {
//            query += " and info.IFSC_MASTER_ID = ? ";
//        }
//
////      filtering based on paymentType N06 or N07
//        if ( !input.getPaymentType().equalsIgnoreCase("All")  ) {
//            query += " and info.SUBTYPE = ? ";
//        }
//
//        try {
//            int i =0;
//            PreparedStatement ps = con.prepareStatement(query);
//
////            ps.setDouble(++i, input.getFromAmount()); // report Amount
////            ps.setDouble(++i, input.getToAmount());
//
//            ps.setDate(++i, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date
//            ps.setDate(++i, ConversionUtil.getFormatedDate(input.getToDate()));
//
//            if (input.getToAmount() > 0){
//                ps.setDouble(++i, input.getFromAmount()); // report Amount
//                ps.setDouble(++i, input.getToAmount());
//            }
//
//            if ( !input.getStatus().equalsIgnoreCase("ALL")){
//                ps.setString(++i, input.getStatus());
//            }
//
//            if ( !input.getBatchTime().equalsIgnoreCase("ALL") ) {
//                ps.setString(++i, input.getBatchTime());
//            }
//
//            if ( input.getIfscId() > 0 ) {
//                ps.setLong(++i, input.getIfscId()); // branch Code
//            }
//
//            if ( !input.getPaymentType().equalsIgnoreCase("All")){
//                ps.setString(++i, input.getPaymentType()); // payment Type ( N06 | N07
//            }
//            i = 0;
//            query += " ORDER BY info.UTRNO, info.ifsc_master_id ";
//            ResultSet rs = ps.executeQuery();
//            totalAmt = constructOutwardTxnDetailedTxnInfo(rs, PS1TxnsList);
//
//            rs.close();
//            ps.close();
//        } catch (Throwable th) {
//
//            logger.error("Exception while getting txn details :"+ th.getMessage());
//            throw new BOException("Exception while getting txn details :"+th.getMessage());
//        }
//        return totalAmt;
//    }

    /**
     * method to get all Outward transactions.
     *
     * @param input     - ReportInputDTO which holds the all the input paramter
     *                    choosed by the user
     *
     * @param con       - Connection
     *
     * @param batchTime - String which holds the respective batchTiming for which
     *                    the debit transactions must be produced
     *
     * @param PS1TxnsList - An empty list which will contain the result
     *
     * @return double - total amount of the PS1 transactions for the respective batch
     */
    protected BigDecimal getOutwardTransactions(ReportInputDTO input, List PS1TxnsList) {

//      double totalAmt = 0;
      BigDecimal totalAmt = BigDecimal.ZERO;
//    Query for getting OutwardTxnDetails
      String query =  " SELECT distinct info.batchtime, " +
                      " info.value_date, " +
                      " info.senderAddr, " +
                      " info.recvAddr, " +
                      " info.utrno, " +
                      " info.amount, " +
                      " info.sender_acc_name, " +
                      " info.sender_acc_no, " +
                      " info.sender_acc_type, " +
                      " info.ben_acc_name, " +
                      " info.ben_acc_no, " +
                      " info.ben_acc_type, " +
                      " info.status_desc, " +
                      " info.ifsc_master_id, " +
                      " info.subtype, " +
                      " nbt.RESH_BATCH_TIME,nbt.RESH_DATE " +//Added for Finetuning the Query, 20101008
                      " FROM NEFTREPORTINFO info, NEFTBALTRAN nbt  " +
                      " WHERE info.BUSINESS_DATE BETWEEN ? AND ? "+
                      " AND info.tran_type = 'outward'"+
                      " AND nbt.UTR_NO(+) = info.UTRNO ";//Added for Finetuning the Query, 20101008, Include OuterJoin on 20101125
                      //Changed to view the rescheduled report at any day, 20101006
                      if("2550".equalsIgnoreCase(input.getStatus())) {
                          //query += "AND info.flag = 2";
                          query += " AND (info.flag = 2 OR (info.status = 2550 AND info.FLAG = 1)) ";
                      }
      //Commented the following query, for finetuning the outward reports query. 20101008
      //String queryResch = " SELECT  nbt.RESH_BATCH_TIME,nbt.RESH_DATE FROM NEFTBALTRAN nbt,NEFTREPORTINFO info " +
      //                    " WHERE nbt.UTR_NO = info.UTRNO and "+
      //                    " nbt.UTR_NO = ? and nbt.TRAN_TYPE LIKE 'O'";

//      if (input.equalsIgnoreCase("ALL")){
//          query += " AND info.";
//      }
      //BigDecimal fromAmount = new BigDecimal(input.getFromAmount());
      BigDecimal toAmount = new BigDecimal(input.getToAmount());

//      if (input.getToAmount() > 0){
      if (toAmount.compareTo(BigDecimal.ZERO) > 0){
          query += " AND info.amount BETWEEN ? AND ? ";
      }
      // Changed to view the rescheduled report at any day, 20101006
      if(!("2550".equalsIgnoreCase(input.getStatus()) ||
           input.getStatus().equalsIgnoreCase("ALL"))) {
          query += " AND info.STATUS = ? ";
      }

//      if (!input.getStatus().equalsIgnoreCase("ALL")){
//          query += " AND info.STATUS = ? ";
//      }

      if ( !input.getBatchTime().equalsIgnoreCase("ALL")) {
          query += " and info.BATCHTIME = ? ";
      }

//    filtering based on branch or Central office view
      if ( input.getIfscId() > 0 ) {
          query += " and info.IFSC_MASTER_ID = ? ";
      }

//    filtering based on paymentType N06 or N07
      if ( !input.getPaymentType().equalsIgnoreCase("All")  ) {
          query += " and info.SUBTYPE = ? ";
      }

      if (input.getHostType() != null && !input.getHostType()
      .equalsIgnoreCase("ALL")) {

          query += " and info.SOURCE = '" + input.getHostType() +"'" ;
      }
      PreparedStatement ps =  null;
      PreparedStatement psResh  = null;
      ResultSet rsResch = null;
      ResultSet rs = null;
      try {
          int i =0;
//          ps = con.prepareStatement(query);
          //psResh = con.prepareStatement(queryResch); //Commented for Finetuning the Query, 20101008

//          ps.setDouble(++i, input.getFromAmount()); // report Amount
//          ps.setDouble(++i, input.getToAmount());

          query += " ORDER BY info.batchtime,info.UTRNO, info.ifsc_master_id, nbt.resh_batch_time, nbt.resh_date "; //20101008
          ps = con.prepareStatement(query);

          ps.setDate(++i, ConversionUtil.getFormatedDate(input.getValueDate())); // report Date
          ps.setDate(++i, ConversionUtil.getFormatedDate(input.getToDate()));


//          if (input.getToAmunt() > 0){
          if (toAmount.compareTo(BigDecimal.ZERO) > 0){
//              ps.setDouble(++i, input.getFromAmount()); // report Amount
//              ps.setDouble(++i, input.getToAmount());
              ps.setBigDecimal(++i, new BigDecimal(input.getFromAmount())); // report Amount
              ps.setBigDecimal(++i, new BigDecimal(input.getToAmount()));
          }

          if ( !("2550".equalsIgnoreCase(input.getStatus()) ||
                  input.getStatus().equalsIgnoreCase("ALL"))) {
              ps.setString(++i, input.getStatus());
          }

          if ( !input.getBatchTime().equalsIgnoreCase("ALL") ) {
              ps.setString(++i, input.getBatchTime());
          }

          if ( input.getIfscId() > 0 ) {
              ps.setLong(++i, input.getIfscId()); // branch Code
          }

          if ( !input.getPaymentType().equalsIgnoreCase("All")){
              ps.setString(++i, input.getPaymentType()); // payment Type ( N06 | N07
          }
          i = 0;
          //query += " ORDER BY info.batchtime,info.UTRNO, info.ifsc_master_id "; //Modified by priyak
          rs = ps.executeQuery();
          //totalAmt = constructOutwardTxnDetailedTxnInfo(rs, PS1TxnsList);
          //code modified for cancelled txns not coming in report
          //BigDecimal totalAmount = BigDecimal.ZERO;

          while(rs.next()) {

              TransactionInfo ti = new TransactionInfo();

              //ti.msgType   = rs.getString("MSG_TYPE");

              //System.out.println(rs.getString("MSG_TYPE"));
              ti.batchTime = rs.getString(1);
              ti.valueDate = rs.getDate(2);
              ti.senderInfo      = new CustomerInfo();
              ti.beneficiaryInfo = new CustomerInfo();


              ti.senderInfo.accIfsc = rs.getString(3);
              ti.beneficiaryInfo.accIfsc = rs.getString(4);

              ti.utrNo = rs.getString(5);
//              ti.amount = rs.getDouble(6);
              ti.amount = rs.getBigDecimal(6);

              ti.senderInfo.accName = rs.getString(7);
              ti.senderInfo.accNo = rs.getString(8);
              ti.senderInfo.accType = rs.getString(9);
              ti.beneficiaryInfo.accName = rs.getString(10);
              ti.beneficiaryInfo.accNo = rs.getString(11);
              ti.beneficiaryInfo.accType = rs.getString(12);
              ti.currentStatus = rs.getString(13);

              //Added for Outward Report Finetuned Query, 20101008
              ti.rescheduleBatch = rs.getString("resh_batch_time");
              ti.rescheduleDate = rs.getDate("RESH_DATE");
              ti.msgType = rs.getString("SUBTYPE");
              //Commented the following code for Outward Report Finetuned Query. 20101008
              //psResh.setString(1, rs.getString(5));
              //rsResch = psResh.executeQuery();
//              if (rsResch.next()) {
//                  ti.rescheduleBatch = rs.getString(1);
//                  ti.rescheduleDate = rs.getDate(2);
//              } else {
//                  ti.rescheduleBatch = "";
//                  ti.rescheduleDate = null;
//              }

              //System.out.println( rs.getString(17));


//              totalAmount = totalAmount + ti.amount;
              totalAmt = totalAmt.add(ti.amount);
              // Sets the DTO Object in to the List
              PS1TxnsList.add(ti);
          }

      } catch (Throwable th) {

          logger.error("Exception while getting txn details :"+ th.getMessage());
          throw new BOException("Exception while getting txn details :"+th.getMessage());
      } finally {
          release(ps,rs);
          release(psResh,rsResch);
      }
      return totalAmt;
  }

//    /**
//     * This method is used to Generate the Transaction Informations such as
//     *
//     *  @ amount
//     *  @ value Date
//     *  @ batch time
//     *  @ sender account Number
//     *  @ sender account Type
//     *  @ sender account Name
//     *  @ sender ifsc
//     *  @ Beneficiary account Number
//     *  @ Beneficiary account Type
//     *  @ Beneficiary account Name
//     *  @ beneficiary ifsc
//     *  @ current status of message
//     *
//     * @param rs      - ResultSet
//     * @param list    - an Empty list which must contain the result
//     * @param con     - Connection
//     * @return amount - total amount of the transactions for the respective batch
//     * @throws SQLException
//     */
//
//    private double constructOutwardTxnDetailedTxnInfo(ResultSet rs, List outwardTxnInfoList)
//    throws SQLException {
//
//        // Total Amount
//        double totalAmount = 0;
//
//        while(rs.next()) {
//
//            TransactionInfo ti = new TransactionInfo();
//
//            ti.batchTime = rs.getString(1);
//            ti.valueDate = rs.getDate(2);
//            ti.senderInfo      = new CustomerInfo();
//            ti.beneficiaryInfo = new CustomerInfo();
//
//            ti.senderInfo.accIfsc = rs.getString(3);
//            ti.beneficiaryInfo.accIfsc = rs.getString(4);
//
//            ti.utrNo = rs.getString(5);
//            ti.amount = rs.getDouble(6);
//
//            ti.senderInfo.accName = rs.getString(7);
//            ti.senderInfo.accNo = rs.getString(8);
//            ti.senderInfo.accType = rs.getString(9);
//            ti.beneficiaryInfo.accName = rs.getString(10);
//            ti.beneficiaryInfo.accNo = rs.getString(11);
//            ti.beneficiaryInfo.accType = rs.getString(12);
//            ti.currentStatus = rs.getString(13);
//            ti.rescheduleBatch = rs.getString(15);
//            ti.rescheduleDate = rs.getDate(16);
//
//            totalAmount = totalAmount + ti.amount;
//
//            // Sets the DTO Object in to the List
//            outwardTxnInfoList.add(ti);
//        }
//        return totalAmount;
//    }

    /**
     * This method is used to Generate the Transaction Informations such as
     *
     *  @ amount
     *  @ value Date
     *  @ batch time
     *  @ sender account Number
     *  @ sender account Type
     *  @ sender account Name
     *  @ sender ifsc
     *  @ Beneficiary account Number
     *  @ Beneficiary account Type
     *  @ Beneficiary account Name
     *  @ beneficiary ifsc
     *  @ current status of message
     *
     * @param rs      - ResultSet
     * @param list    - an Empty list which must contain the result
     * @param con     - Connection
     * @return amount - total amount of the transactions for the respective batch
     * @throws SQLException
     */

    private BigDecimal constructOutwardTxnDetailedTxnInfo(ResultSet rs, List outwardTxnInfoList)
    throws SQLException {

        // Total Amount
//        double totalAmount = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        while(rs.next()) {

            TransactionInfo ti = new TransactionInfo();

            //ti.msgType   = rs.getString("MSG_TYPE");

            //System.out.println(rs.getString("MSG_TYPE"));
            ti.batchTime = rs.getString(1);
            ti.valueDate = rs.getDate(2);
            ti.senderInfo      = new CustomerInfo();
            ti.beneficiaryInfo = new CustomerInfo();


            ti.senderInfo.accIfsc = rs.getString(3);
            ti.beneficiaryInfo.accIfsc = rs.getString(4);

            ti.utrNo = rs.getString(5);
//            ti.amount = rs.getDouble(6);
            ti.amount = rs.getBigDecimal(6);

            ti.senderInfo.accName = rs.getString(7);
            ti.senderInfo.accNo = rs.getString(8);
            ti.senderInfo.accType = rs.getString(9);
            ti.beneficiaryInfo.accName = rs.getString(10);
            ti.beneficiaryInfo.accNo = rs.getString(11);
            ti.beneficiaryInfo.accType = rs.getString(12);
            ti.currentStatus = rs.getString(13);
            ti.rescheduleBatch = rs.getString(15);
            ti.rescheduleDate = rs.getDate(16);
            //System.out.println( rs.getString(17));
            ti.msgType = rs.getString("SUBTYPE");

//            totalAmount = totalAmount + ti.amount;
            totalAmount = totalAmount.add(ti.amount);
            // Sets the DTO Object in to the List
            outwardTxnInfoList.add(ti);
        }
        return totalAmount;
    }

    /**
     * To generate NEFT Graduated Payment Reports
     */
    public Message generateNeftGraduatedPaymentReport(Message msg)
    throws Exception {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        Message res = new Message();
        String inputDate;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);

        try {

            String qryGraduatedReport = "SELECT UTR_NO, MSG_TYPE, TRAN_TYPE, SENDER_ADDRESS, RECEIVER_ADDRESS, DBT_CRD, " +
                                        "TXN_AMT, BATCH_TIME, RESH_DATE ,RESH_BATCH_TIME ,REJ_DATE ,REJ_BATCH_TIME " +
                                        " FROM NEFTBALTRAN WHERE TXN_DATE = ? ORDER BY ID DESC";

            /**
             * Done on on 19-Jan-2011.
             *
             * Separated database calls to generate reports based on data available in local/archive schema.
             *
             * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we no need to look for data available in ARCHIVE schema.
             *
             * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we need to look both local and archive schema to generate reports.
             */

            if (input.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

                if (new Date(input.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                    qryGraduatedReport = "SELECT UTR_NO, MSG_TYPE, TRAN_TYPE, SENDER_ADDRESS, RECEIVER_ADDRESS, DBT_CRD, " +
                    "TXN_AMT, BATCH_TIME, RESH_DATE ,RESH_BATCH_TIME ,REJ_DATE ,REJ_BATCH_TIME " +
                    " FROM NEFTBALTRAN_VW WHERE TXN_DATE = ? ORDER BY ID DESC";

                        logger.info("Data fetching from archive database schema..");

                } else {

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                        logger.info("Data fetching from Local Database schema...");
                }
            inputDate = input.getValueDate();
            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Generating NEFT Graduated Payment Report : " + qryGraduatedReport+" " +"for Value Date :"+inputDate);
            logger.debug("Generating NEFT Graduated Payment Report : " + qryGraduatedReport+" " +"for Value Date :"+inputDate);
            ps = con.prepareStatement(qryGraduatedReport);
            ps.setString(1, inputDate);
            rs = ps.executeQuery();
            while(rs.next()) {

                reportDTO = new ReportDTO();
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setMsgType(rs.getString("MSG_TYPE"));
                String tranType = "Outward";
                if(rs.getString("TRAN_TYPE").equals("I")) {

                    tranType = "Inward";
                }
                reportDTO.setTranType(tranType);
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                if(rs.getString("DBT_CRD").equals("C")) {

                    reportDTO.setDebitCredit("Credit");
                }else if(rs.getString("DBT_CRD").equals("D")){

                    reportDTO.setDebitCredit("Debit");
                }
                reportDTO.setValueDate(inputDate);
                reportDTO.setAmt(rs.getString("TXN_AMT"));
                reportDTO.setBatchTime(rs.getString("BATCH_TIME"));
                reportDTO.setReshDate(InstaReportUtil
                                      .indianFormatDate(rs.getDate("RESH_DATE")));
                reportDTO.setReshBatchTime(rs.getString("RESH_BATCH_TIME"));
                reportDTO.setRejDate(InstaReportUtil
                                     .indianFormatDate(rs.getDate("REJ_DATE")));
                reportDTO.setRejBatchTime(rs.getString("REJ_BATCH_TIME"));
                reportList.add(reportDTO);
            }
            res.info = reportList;
            return res;
        } catch(Exception e) {

            logger.error("Exception while generating Neft Graduated Payment report :"+e);
            throw new BOException("Exception while generating Neft Graduated Payment Report :"+e);
        } finally {

            release(ps, rs);
        }
    }

    /**
     * Method to generate Outward returned report
     */
    public Message generateNEFTOutwardReturnedReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        List<String> addedUtr = new ArrayList<String>();
        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getFromAmount());

        try {

            String neft_ow_retn_search_field = InstaDefaultConstants.NEFT_OW_RETN_SEARCH_FIELD;

            String[] neft_ow_retn_search_field_arr = null;

            if(neft_ow_retn_search_field != null && neft_ow_retn_search_field.length() > 0) {

                neft_ow_retn_search_field_arr = neft_ow_retn_search_field.split(",");
            }

            StringBuffer sb = new StringBuffer(4028);

            sb.append("SELECT DISTINCT utr_no, sender_address, receiver_address,")
            .append(" A7495, BUSINESS_DATE, ")
            .append(" amount, value_date, batch_time ");

            if(neft_ow_retn_search_field_arr != null  && neft_ow_retn_search_field_arr.length > 0) {

                for (int i = 0; i < neft_ow_retn_search_field_arr.length; i++) {

                    sb.append(", ");
                    String result = appendFinalString(neft_ow_retn_search_field_arr[i], "NEFT");
                    sb.append(result);
                }
            }

            sb.append(" FROM ( SELECT DISTINCT v.utr_no, v.sender_address, v.receiver_address,")
              .append(" v.A7495, v.BUSINESS_DATE, ")
              .append(" v.amount, v.value_date, v.batch_time ");

            if(neft_ow_retn_search_field_arr != null  && neft_ow_retn_search_field_arr.length > 0) {

                for (int i = 0; i < neft_ow_retn_search_field_arr.length; i++) {

                    sb.append(", ");
                    String result = appendFieldString(neft_ow_retn_search_field_arr[i], "NEFT", 0, 1);
                    sb.append(result);
                }
            }


            sb.append(" FROM ( SELECT DISTINCT nm.utr_no, nm.sender_address, nm.receiver_address,")
            .append( appendSelectString("7495").toString() + " A7495, nm.BUSINESS_DATE, ")
            .append(" nm.amount, nm.value_date, nm.batch_time");

            if(neft_ow_retn_search_field_arr != null  && neft_ow_retn_search_field_arr.length > 0) {

                for (int i = 0; i < neft_ow_retn_search_field_arr.length; i++) {

                    sb.append(", ");
                    String result = appendSelectString(neft_ow_retn_search_field_arr[i]);
                    sb.append(result + " NNN_" + neft_ow_retn_search_field_arr[i]);
                }
            }

            /**
             * Done on on 19-Jan-2011.
             *
             * Separated database calls to generate reports based on data available in local/archive schema.
             *
             * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we no need to look for data available in ARCHIVE schema.
             *
             * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we need to look both local and archive schema to generate reports.
             */

            if (inputDTO.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                    sb.append(" FROM MESSAGE_VW m, NEFT_MESSAGE_VW nm   ");
                    logger.info("Data fetching from archive database schema..");

                } else {
                        sb.append(" FROM MESSAGE m, NEFT_MESSAGE nm   ");
                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {
                        sb.append(" FROM MESSAGE m, NEFT_MESSAGE nm   ");
                        logger.info("Data fetching from Local Database schema...");
                }


              //Related to Inward Sender Bank Specific.
              if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                            .equalsIgnoreCase("ALL") ) {

                  sb.append(" , IFSCMASTER im, BANKMASTER bm ");
              }

              sb.append(" WHERE  nm.msg_id = m.msg_id AND nm.tran_type = 'inward' AND m.msg_sub_type = 'N02'");

              // Related to Inward Sender Bank Specific.
              if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                            .equalsIgnoreCase("ALL") ) {

                  sb.append(" AND bm.ID = im.bank_master_id ");
              }


            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                sb.append(" and nm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ");
            }

            /*
             * Related to Amount given by the user. If the user fills only the
             * FromAmount field then system fetch messages having amount greater than
             * or equalto given FromAmount. If the user fills only the ToAmount field
             * then system fetch the messages having amount less than or equalto given
             * ToAmount. If the user give both the amount then the system looks for
             * messages having anmount between the fromAmount and toAmount.
             */
//            if (inputDTO.getFromAmount() > 0 && inputDTO.getToAmount() > 0 ) {
            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {

                sb.append(" and nm.AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount());
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {
//            } else if (inputDTO.getFromAmount() > 0) {

                sb.append(" and nm.AMOUNT >= " + inputDTO.getFromAmount());
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(" and nm.AMOUNT <= " + inputDTO.getToAmount());
            }

            //Related to Inward Receiver Branch/IFSC Specific.
            if (inputDTO.getReceiverIfscId() > 0 ) {

                sb.append(" and nm.IFSC_Master_ID = " + inputDTO.getReceiverIfscId());
            }

//          Related to Inward Receiver Branch/IFSC Specific.
            if (!inputDTO.getBatchTime().equalsIgnoreCase("ALL")) {

                sb.append(" and nm.BATCH_TIME = " + inputDTO.getBatchTime());
            }

            //Related to Inward Sender Bank Specific.
            if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                            .equalsIgnoreCase("ALL") ) {

                sb.append(" and bm.CODE = '" + inputDTO.getSenderBank() + "'");
            }

            //Related to Inward Sender Branch/IFSC Specific.
            if (inputDTO.getSenderIfscId() > 0 ) {

                sb.append(" and SUBSTR(nm.sender_address, 0, 11) = ( SELECT IFSC FROM IFSCMASTER WHERE ID = " + inputDTO.getSenderIfscId() + " ) ");
            }

            sb.append("  AND nm.status <> 700 ) v ");

            if(neft_ow_retn_search_field_arr != null  && neft_ow_retn_search_field_arr.length > 0) {

                sb.append(" WHERE ( ");
                int count = 1;

                for (int i = 0; i < neft_ow_retn_search_field_arr.length; i++) {
                    //to be changed,20100930
                    String result = appendWhereString(neft_ow_retn_search_field_arr[i], "NEFT", 0, 1);
                    sb.append(" " + result);
                    if(count != neft_ow_retn_search_field_arr.length) {

                        sb.append(" OR ");
                        count++;
                    }
                }
                //Added on 20100929, For Duplicate Inward displayed in reports.
                sb.append(" OR ");
                sb.append("(UPPER (nnn_7495) LIKE '%NO SUCH%'");
                sb.append("OR UPPER (nnn_7495) LIKE '%INVALID%'");
                sb.append("OR UPPER (nnn_7495) LIKE '%NOT %'");
                sb.append("OR UPPER (nnn_7495) LIKE '%DIFFER%')");
                sb.append(") ");
            }

            sb.append("  ) ORDER BY BUSINESS_DATE ASC, utr_no ASC ");

            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();

            while(rs.next()) {

                ReportDTO reportDTO = new ReportDTO();

                //Populate the ReportDTO.
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                if(neft_ow_retn_search_field_arr != null && neft_ow_retn_search_field_arr.length > 0) {

                    for (int i = 0; i < neft_ow_retn_search_field_arr.length; i++) {

                        String owResult = rs.getString("NN_" + neft_ow_retn_search_field_arr[i]);
                        if(owResult != null && (owResult.length() > 0 && owResult.length() == 16)) {

                            //Added on 20100929, For \r\n in UTR field in report.
                            if(owResult.indexOf("\r\n") != -1) {
                                owResult = owResult.replaceAll("\r\n", "");
                            } else if(owResult.indexOf("\n") != -1) {
                                owResult = owResult.replaceAll("\n", "");
                            }
                            reportDTO.setOutUTRNo(owResult);
                            break;
                        } else if(owResult == null || owResult == "") {
                            reportDTO.setOutUTRNo("Not Referred");
                        }
                    }

                }
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("VALUE_DATE")));
                reportDTO.setFieldA7495(rs.getString("A7495"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setBatchTime(rs.getString("BATCH_TIME"));
                reportList.add(reportDTO);
                addedUtr.add(reportDTO.getUtrNo());

            }

            release(ps, rs);

            //Commented on 20100929, For Duplicate Inward displayed in reports.
//            StringBuffer keyWordQuery = new StringBuffer(4028);
//
//            keyWordQuery.append("SELECT V.* ")
//            .append("FROM (   ");
//
//            keyWordQuery.append("SELECT DISTINCT nm.utr_no, nm.sender_address, nm.receiver_address,")
//              .append( appendSelectString("7495") + " N7495, nm.BUSINESS_DATE, ")
//              .append(" nm.amount, nm.value_date ")
//              .append("FROM MESSAGE m, NEFT_MESSAGE nm   ");
//
//              //Related to Inward Sender Bank Specific.
//              if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
//                                                            .equalsIgnoreCase("ALL") ) {
//
//                  keyWordQuery.append(" , IFSCMASTER im, BANKMASTER bm ");
//              }
//              keyWordQuery.append(" WHERE  nm.msg_id = m.msg_id AND nm.tran_type = 'inward' AND m.msg_sub_type = 'N02' ");
//
//              // Related to Inward Sender Bank Specific.
//              if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
//                                                            .equalsIgnoreCase("ALL") ) {
//
//                  keyWordQuery.append(" AND bm.ID = im.bank_master_id ");
//              }
//
//
//            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {
//
//                keyWordQuery.append(" and nm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
//                                             + "' and '" + inputDTO.getToDate() + "' ");
//            }
//
//            /*
//             *     Related to Amount given by the user. If the user fills only the
//             * FromAmount field then system fetch messages having amount greater than
//             * or equalto given FromAmount. If the user fills only the ToAmount field
//             * then system fetch the messages having amount less than or equal to given
//             * ToAmount. If the user give both the amount then the system looks for
//             * messages having anmount between the fromAmount and toAmount.
//             */
////            if (inputDTO.getFromAmount() > 0 && inputDTO.getToAmount() > 0 ) {
//            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {
//                keyWordQuery.append(" and nm.AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
//                                                    + inputDTO.getToAmount());
////            } else if (inputDTO.getFromAmount() > 0) {
//            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {
//
//                keyWordQuery.append(" and nm.AMOUNT >= " + inputDTO.getFromAmount());
////            } else if (inputDTO.getToAmount() > 0) {
//            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {
//                keyWordQuery.append(" and nm.AMOUNT <= " + inputDTO.getToAmount());
//            }
//
//            //Related to Inward Receiver Branch/IFSC Specific.
//            if (inputDTO.getReceiverIfscId() > 0 ) {
//
//                keyWordQuery.append(" and nm.IFSC_Master_ID = " + inputDTO.getReceiverIfscId());
//            }
//
//            //Related to Inward Sender Bank Specific.
//            if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
//                                                            .equalsIgnoreCase("ALL") ) {
//
//                keyWordQuery.append(" and bm.CODE = '" + inputDTO.getSenderBank() + "'");
//            }
//
//            //Related to Inward Sender Branch/IFSC Specific.
//            if (inputDTO.getSenderIfscId() > 0 ) {
//
//                keyWordQuery.append(" and SUBSTR(nm.sender_address, 0, 11) = ( SELECT IFSC FROM IFSCMASTER WHERE ID = " + inputDTO.getSenderIfscId() + " ) ");
//            }
//
//            keyWordQuery.append(" AND nm.status <> 700 ) v ");
//
//            String[] key = InstaDefaultConstants.OW_RETN_SEARCH_KEYWORDS.split(",");
//
//            if(key.length > 0) {
//
//                keyWordQuery.append(" WHERE ( " );
//            }
//            for (int i = 0; i < key.length; i++ ) {
//
//                keyWordQuery.append(" UPPER(v.N7495) LIKE '%" + key[i] + "%' ");
//
//                if(i < (key.length - 1)) {
//
//                    keyWordQuery.append(" OR ");
//                }
//            }
//
//            if(key.length > 0) {
//
//                keyWordQuery.append(" ) " );
//            }
//
//            keyWordQuery.append(" ORDER BY BUSINESS_DATE ASC, utr_no ASC ");
//
//            ps = con.prepareStatement(keyWordQuery.toString());
//            rs = ps.executeQuery();
//
//            while(rs.next()) {
//
//                ReportDTO reportDTO = new ReportDTO();
//
//                //Populate the ReportDTO.
//                reportDTO.setUtrNo(rs.getString("UTR_NO"));
//                reportDTO.setOutUTRNo("Not Referred");
//                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
//                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
//                reportDTO.setValueDate(InstaReportUtil
//                                       .indianFormatDate(rs.getDate("VALUE_DATE")));
//                reportDTO.setFieldA7495(rs.getString("N7495"));
//                reportDTO.setAmt(rs.getString("AMOUNT"));
//
//                reportList.add(reportDTO);
//            }

        } catch(Throwable th) {

            logger.error("Exception while generating Outward Returned Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating Outward Returned Report."
                                , th);
        } finally {

            release(ps, rs);
        }
        req.info = reportList;
        return req;

    }

    /**
     * To generate NEFT Inward Txns Report
     */
    public Message generateNEFTInwTxnsReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        //PreparedStatement ps = null;
        //ResultSet rs = null;
        Message res = new Message();
        //ITDetailReportDTO detailsDTO;
        List inwList = new ArrayList<TransactionInfo>();
        try {

            String query1 = AllDetailedInwardQuery + " AND TO_NUMBER(flag) = 1 ";
            ResultSet rs1 = setFiltersForInwardTxnReport(inputDTO, query1).executeQuery();
            inwList = constructDetailedInwardTxnReport(rs1);

            res.info = inwList;
            return res;
        } catch(Exception e){

            logger.error("Exception while generating NEFTInwTxnDetailReport :"+e);
            throw new BOException("Exception while generating NEFTInwTxnDetailReport "+e);
        }
    }

    private List constructDetailedInwardTxnReport(ResultSet rs) throws SQLException {

        // List which will hold the DetailedInwardTransactionReport
        List inwardTranInfoList = new ArrayList(0);

        while(rs.next()) {

            TransactionInfo ti = new TransactionInfo();

            ti.txnDate = rs.getDate(1);
            ti.valueDate = rs.getDate(2);
            ti.batchTime = rs.getString(3);
            ti.utrNo = rs.getString(4);

            ti.senderInfo      = new CustomerInfo();
            ti.beneficiaryInfo = new CustomerInfo();

            ti.senderInfo.accIfsc = rs.getString(5);
            ti.beneficiaryInfo.accIfsc = rs.getString(6);
            ti.senderInfo.accName = rs.getString(7);
            ti.senderInfo.accNo = rs.getString(8);
            ti.senderInfo.accType = rs.getString(9);
            ti.beneficiaryInfo.accName = rs.getString(10);
            ti.beneficiaryInfo.accNo = rs.getString(11);
            ti.beneficiaryInfo.accType = rs.getString(12);
            ti.amount = rs.getBigDecimal(13);
            ti.currentStatus = String.valueOf(rs.getLong(14));
            ti.statusShortDesc = rs.getString(15);
            ti.flag  = rs.getString(16);
            ti.remarks = rs.getString(18) == null ? "" : rs.getString(18);

            // Sets the DTO Object in to the List
            inwardTranInfoList.add(ti);
        }
        return inwardTranInfoList;
    }

    /**
     * Method For Generate the NEFT RTGS Net Settlement Report
     * @param Message
     * @return Message
     */
    public Message generateNEFTRTGSNetSettlementReport(Message req){

        Message res = new Message();

        StringBuffer reConcileQuery = new StringBuffer();
        String filter = "AND BUSINESS_DATE = TO_DATE(?) ";
        String code = InstaDefaultConstants.NEFT_RTGS_NET_SETTLE_SEARCH_KEYWORD;
        StringBuffer aggregateQuery = new StringBuffer();
        ReportInputDTO reportDTO = (ReportInputDTO)req.info;

        /**
         * Done on on 19-Jan-2011.
         *
         * Separated database calls to generate reports based on data available in local/archive schema.
         *
         * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we no need to look for data available in ARCHIVE schema.
         *
         * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we need to look both local and archive schema to generate reports.
         */

        if (reportDTO.getValueDate() != null) {

            java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

            if (new Date(reportDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                //archive database schema
                reConcileQuery.append(" SELECT msg.MSG_TYPE, msg.MSG_SUB_TYPE, rtgs.SENDER_ADDRESS, " )
                .append("        GetCompoundFieldValue (rtgs.msg_id, 7495,'I7495', msg.msg_sub_type) INFO, ")
                .append("        GetCompoundFieldValue (rtgs.msg_id, 7495,'A7495', msg.msg_sub_type) ADDTIONAL_INFO, rtgs.amount")
                .append(" FROM INWARD_MSGSTAGE i, RTGS_MESSAGE_VW rtgs, MESSAGE_VW msg ")
                .append(" WHERE i.ID = rtgs.msg_stage_id ")
                .append(" AND rtgs.msg_id = msg.msg_id ")
                .append(" AND (   i.MESSAGE LIKE '%298R43%:4488:%:7495:/"+code+"/%' ")
                .append(" OR i.MESSAGE LIKE '%298R44%:4488:%:7495:/"+code+"/%' ) ")
                .append("  AND rtgs.STATUS IN (1000)")
                .append(filter);

                aggregateQuery.append(" SELECT   TO_CHAR ( SUM (TO_NUMBER (amount))),neft.batch_time, neft.TRAN_TYPE ")
                .append(" FROM NEFT_MESSAGE_VW neft, MESSAGE_VW msg ")
                .append(" WHERE msg.msg_id = neft.msg_id ")
                .append(filter)
                .append(" AND msg.msg_sub_type IN ('N02', 'N06', 'N07') ")
                .append(" GROUP BY neft.batch_time, neft.TRAN_TYPE ");

                    logger.info("Data fetching from archive database schema..");

            } else {
                reConcileQuery.append(" SELECT msg.MSG_TYPE, msg.MSG_SUB_TYPE, rtgs.SENDER_ADDRESS, " )
                .append("        GetCompoundFieldValue (rtgs.msg_id, 7495,'I7495', msg.msg_sub_type) INFO, ")
                .append("        GetCompoundFieldValue (rtgs.msg_id, 7495,'A7495', msg.msg_sub_type) ADDTIONAL_INFO, rtgs.amount")
                .append(" FROM INWARD_MSGSTAGE i, RTGS_MESSAGE rtgs, MESSAGE msg ")
                .append(" WHERE i.ID = rtgs.msg_stage_id ")
                .append(" AND rtgs.msg_id = msg.msg_id ")
                .append(" AND (   i.MESSAGE LIKE '%298R43%:4488:%:7495:/"+code+"/%' ")
                .append(" OR i.MESSAGE LIKE '%298R44%:4488:%:7495:/"+code+"/%' ) ")
                .append("  AND rtgs.STATUS IN (1000)")
                .append(filter);

                aggregateQuery.append(" SELECT   TO_CHAR ( SUM (TO_NUMBER (amount))),neft.batch_time, neft.TRAN_TYPE ")
                .append(" FROM NEFT_MESSAGE neft, MESSAGE msg ")
                .append(" WHERE msg.msg_id = neft.msg_id ")
                .append(filter)
                .append(" AND msg.msg_sub_type IN ('N02', 'N06', 'N07') ")
                .append(" GROUP BY neft.batch_time, neft.TRAN_TYPE ");

                logger.info("Data fetching from Local Database schema...");
                }
            } else {
                reConcileQuery.append(" SELECT msg.MSG_TYPE, msg.MSG_SUB_TYPE, rtgs.SENDER_ADDRESS, " )
                .append("        GetCompoundFieldValue (rtgs.msg_id, 7495,'I7495', msg.msg_sub_type) INFO, ")
                .append("        GetCompoundFieldValue (rtgs.msg_id, 7495,'A7495', msg.msg_sub_type) ADDTIONAL_INFO, rtgs.amount")
                .append(" FROM INWARD_MSGSTAGE i, RTGS_MESSAGE rtgs, MESSAGE msg ")
                .append(" WHERE i.ID = rtgs.msg_stage_id ")
                .append(" AND rtgs.msg_id = msg.msg_id ")
                .append(" AND (   i.MESSAGE LIKE '%298R43%:4488:%:7495:/"+code+"/%' ")
                .append(" OR i.MESSAGE LIKE '%298R44%:4488:%:7495:/"+code+"/%' ) ")
                .append("  AND rtgs.STATUS IN (1000)")
                .append(filter);

                aggregateQuery.append(" SELECT   TO_CHAR ( SUM (TO_NUMBER (amount))),neft.batch_time, neft.TRAN_TYPE ")
                .append(" FROM NEFT_MESSAGE neft, MESSAGE msg ")
                .append(" WHERE msg.msg_id = neft.msg_id ")
                .append(filter)
                .append(" AND msg.msg_sub_type IN ('N02', 'N06', 'N07') ")
                .append(" GROUP BY neft.batch_time, neft.TRAN_TYPE ");

                    logger.info("Data fetching from Local Database schema...");
            }
        PreparedStatement ps = null;
        ResultSet rs = null;
        List txnList = new ArrayList();
        Map reportMap = new HashMap<String, String>();
        try{
            ps = con.prepareStatement(reConcileQuery.toString());
            ps.setString(1, reportDTO.getValueDate());
            rs = ps.executeQuery();
            NEFT_RTGSNetSettlementDTO dto = null;
            while(rs.next()){
                dto = new NEFT_RTGSNetSettlementDTO();
                dto.setMsgType(rs.getString(1));
                dto.setMsgSubType(rs.getString(2));
                dto.setOrderingInstitution(rs.getString(3));
                dto.setInfo((rs.getString(4) == null)?"":rs.getString(4));
                dto.setAdditionalInfo((rs.getString(5) == null)?"":rs.getString(5));
                dto.setAmount(rs.getString(6));
                txnList.add(dto);
            }

            ps = con.prepareStatement(aggregateQuery.toString());
            ps.setString(1, reportDTO.getValueDate());
            rs = ps.executeQuery();
            String key = "";
            String value = "";
            String tran_type = "";
            BigDecimal amount = BigDecimal.ZERO;
            while(rs.next()){
                key = rs.getString(2);
                value = rs.getString(1);
                tran_type = rs.getString(3);
                if(!reportMap.containsKey(key)){
                    if(tran_type.equalsIgnoreCase("inward")){
                        value = (new BigDecimal(value).multiply(new BigDecimal("1"))).toString();
                    }else{
                        value = (new BigDecimal(value).multiply(new BigDecimal("-1"))).toString();
                    }
                    reportMap.put(key, value);
                }else{
                    amount = new BigDecimal(reportMap.get(key).toString());
                    if(tran_type.equalsIgnoreCase("inward")){
                        amount = amount.add(new BigDecimal(value));
                    }else{
                        amount = amount.subtract(new BigDecimal(value));
                    }
                    reportMap.remove(key);
                    reportMap.put(key, amount.toString());
                }
            }
            Map resultMap = new HashMap();
            resultMap.put("list", txnList);
            resultMap.put("map", reportMap);
            res.info = resultMap;
            return res;
        }catch(Exception e){

            logger.error("Exception while generating NEFTRTGSNetSettlementReport :"+e);
            throw new BOException("Exception while generating NEFTRTGSNetSettlementReport "+e);
        } finally {
            try{
                rs.close();
                ps.close();
            }catch(Exception e){
                logger.error("Exception while generating NEFTRTGSNetSettlementReport :"+e);
                throw new BOException("Exception while generating NEFTRTGSNetSettlementReport "+e);
            }
        }
    }

    /**
     * Method to Generate Neft Reconcilliation CounterPatry wise Reports.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateNEFTReconcilliationReportCPwise(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Map<String, Map<String, List<ReportDTO>>> reportMap =
            new LinkedHashMap<String, Map<String, List<ReportDTO>>>();
        Map<String, List<ReportDTO>> localMap = new LinkedHashMap<String, List<ReportDTO>>();

        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());
        try {

            //Report Query.
            /**
             * Done on on 19-Jan-2011.
             *
             * Separated database calls to generate reports based on data available in local/archive schema.
             *
             * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we no need to look for data available in ARCHIVE schema.
             *
             * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we need to look both local and archive schema to generate reports.
             */
            String qryReconcilliationCPWiseReport = null;
            if (inputDTO.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                     qryReconcilliationCPWiseReport =
                        " SELECT im.ifsc||'-'||im.NAME as BRANCH, value_date, tran_type," +
                        "        msg_type||'-'||msg_sub_type as MSG_TYPE, amount, utr_no," +
                        "        receiver_address, nm.sender_address, bm.CODE " +
                        "    FROM neft_message_vw nm, MESSAGE_vw m, ifscmaster im, bankmaster bm ";
                        logger.info("Data fetching from archive database schema..");

                } else {
                     //local database schema
                     qryReconcilliationCPWiseReport =
                        " SELECT im.ifsc||'-'||im.NAME as BRANCH, value_date, tran_type," +
                        "        msg_type||'-'||msg_sub_type as MSG_TYPE, amount, utr_no," +
                        "        receiver_address, nm.sender_address, bm.CODE " +
                        "    FROM neft_message nm, MESSAGE m, ifscmaster im, bankmaster bm ";
                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {
                     //local database schema
                     qryReconcilliationCPWiseReport =
                        " SELECT im.ifsc||'-'||im.NAME as BRANCH, value_date, tran_type," +
                        "        msg_type||'-'||msg_sub_type as MSG_TYPE, amount, utr_no," +
                        "        receiver_address, nm.sender_address, bm.CODE " +
                        "    FROM neft_message nm, MESSAGE m, ifscmaster im, bankmaster bm ";
                        logger.info("Data fetching from Local Database schema...");
                }

            String whereBlock =
                "   WHERE m.msg_id = nm.msg_id " +
                "         AND ( nm.SENDER_ADDRESS like im.IFSC || '%' or " +
                "               nm.RECEIVER_ADDRESS like im.IFSC || '%') " +
                "         AND m.msg_sub_type in ('N06', 'N07', 'N02') " +
                "         AND im.IFSC not like '%VIJB%'  " +
                "         AND bm.ID=im.BANK_MASTER_ID ";

            String orderBY =
                " ORDER BY im.ifsc asc, msg_sub_type asc, " +
                "           business_date asc, m.msg_id asc ";

            /*
             *  Build the filter condition based on the input given by the user.
             */

            /*
             *  Related to Transaction Date.
             *  Assumption : ToDate should be greater than FromDate.
             */
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                whereBlock += " and nm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
            }

            //Related to Transaction Type.
            if (inputDTO.getTransactionType() != null && !inputDTO.getTransactionType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and TRAN_TYPE = '" + inputDTO.getTransactionType() +"'" ;
            }

            //Related to Message Sub Type.
            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and MSG_SUB_TYPE = '" + inputDTO.getPaymentType() +"'" ;
            }

            //Related to CounterParty BANK.
            if (inputDTO.getCounterPartyBank()!=null && !inputDTO.getCounterPartyBank()
                                                                .equalsIgnoreCase("ALL") ) {

                whereBlock += " and bm.CODE = '" + inputDTO.getCounterPartyBank() +"'";
            }

            //Related to CounterParty IFSC.
            if (inputDTO.getCounterPartyIfscId() > 0 ) {

                whereBlock += " and im.ID = " + inputDTO.getCounterPartyIfscId();
            }

            /*
             *     Related to Amount given by the user. If the user fills only the
             * FromAmount field then system fetch messages having amount greater than
             * or equalto given FromAmount. If the user fills only the ToAmount field
             * then system fetch the messages having amount less than or equalto given
             * ToAmount. If the user give both the amount then the system looks for
             * messages having anmount between the fromAmount and toAmount.
             */
//            if (inputDTO.getFromAmount() > 0 && inputDTO.getToAmount() > 0 ) {
            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {
                whereBlock += " and nm.AMOUNT BETWEEN " + inputDTO.getFromAmount()
                                              + " and " + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and nm.AMOUNT >= " + inputDTO.getFromAmount();
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {
                whereBlock += " and nm.AMOUNT <= " + inputDTO.getToAmount();
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryReconcilliationCPWiseReport += whereBlock + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the CPwise Reconcilliation Report : "+ qryReconcilliationCPWiseReport);

            ps = con.prepareStatement(qryReconcilliationCPWiseReport);
            rs = ps.executeQuery();

            String prevBranch = "";
            String prevMsgType = "";

            while(rs.next()) {

                String currBranch = rs.getString("BRANCH");
                String currMsgType = rs.getString("MSG_TYPE");

                /*
                 * This is to group the records based on the Branches.
                 */
                if (prevBranch != null && prevBranch!= "" &&
                                        !prevBranch.equalsIgnoreCase(currBranch)) {

                    localMap.put(prevMsgType, reportList);
                    reportMap.put(prevBranch, localMap);
                    reportList = new ArrayList<ReportDTO>(0);
                    localMap = new LinkedHashMap<String, List<ReportDTO>>();
                    prevMsgType= "";
                }

                /*
                 * This is to group the records based on the MessageType.
                 */
                if (prevMsgType != null && prevMsgType!= "" &&
                                        !prevMsgType.equalsIgnoreCase(currMsgType)) {

                    localMap.put(prevMsgType, reportList);
                    reportList = new ArrayList<ReportDTO>(0);
                }

                reportDTO = new ReportDTO();

                //Populate ReportDTO.
                reportDTO.setBranch(currBranch);
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("Value_date")));
                String tranType = "Outward";
                if(rs.getString("TRAN_TYPE").equalsIgnoreCase("Inward")) {
                    tranType = "Inward";
                }
                reportDTO.setTranType(tranType);
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setMsgType(rs.getString("MSG_TYPE"));

                reportList.add(reportDTO);

                //Set the Current Branch to the Previous Branch.
                prevBranch = currBranch;

                //Set the Current Message Type to the Previous Message Type.
                prevMsgType = currMsgType;
            }

            /*
             * This is to set the Last set of Records..
             */
            if (reportList.size()>0){

                localMap.put(prevMsgType, reportList);
                reportMap.put(prevBranch, localMap);
            }
        } catch(Throwable th) {

            logger.error("Exception while generating NEFT CPwise Reconciliation Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating NEFT CPwise Reconcilliation Report."
                                , th);
        } finally {

            release(ps, rs);
        }

        req.info = reportMap;
        return req;
    }

    /**
     * Method to Query for NEFT Inward Possible Return Report
     * @param ReportInputDTO
     * @return List<ReportDTO>
     * @author Eswaripriyak.
     */
    public Message generateNEFTInwardPossibleReturnReport(Message msg)
    throws Exception {

        String inputDate;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Message res = new Message();

        try {

            Object[] obj = (Object[]) msg.info;
            ReportInputDTO inputDTO = (ReportInputDTO)obj[0];

            String qryRTGSInwardReturnReport;
            /**
             * Done on on 19-Jan-2011.
             *
             * Separated database calls to generate reports based on data available in local/archive schema.
             *
             * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we no need to look for data available in ARCHIVE schema.
             *
             * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we need to look both local and archive schema to generate reports.
             */

            if (inputDTO.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                    qryRTGSInwardReturnReport = " SELECT UTR_NO,TRAN_TYPE,AMOUNT,SENDER_ADDRESS,RECEIVER_ADDRESS,VALUE_DATE,BUSINESS_DATE " +
                        " FROM NEFT_MESSAGE_VW                                                    " +
                        " WHERE BUSINESS_DATE = ? AND AUTORETURN = 1  AND STATUS = 900            ";

                        logger.info("Data fetching from archive database schema..");

                } else {
                     qryRTGSInwardReturnReport = "   SELECT UTR_NO,TRAN_TYPE,AMOUNT,SENDER_ADDRESS,RECEIVER_ADDRESS,VALUE_DATE,BUSINESS_DATE " +
                         " FROM NEFT_MESSAGE                                                       " +
                         " WHERE BUSINESS_DATE = ? AND AUTORETURN = 1  AND STATUS = 900            ";

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {
                     qryRTGSInwardReturnReport = "   SELECT UTR_NO,TRAN_TYPE,AMOUNT,SENDER_ADDRESS,RECEIVER_ADDRESS,VALUE_DATE,BUSINESS_DATE " +
                         " FROM NEFT_MESSAGE                                                       " +
                         " WHERE BUSINESS_DATE = ? AND AUTORETURN = 1  AND STATUS = 900            ";

                        logger.info("Data fetching from Local Database schema...");
                }

            inputDate = inputDTO.getValueDate();
            logger.debug("Generating RTGS Inward Possible Return Report : " + qryRTGSInwardReturnReport+" " +"for Value Date :"+inputDate);
            ps = con.prepareStatement(qryRTGSInwardReturnReport);
            ps.setString(1, inputDate);
            rs = ps.executeQuery();

            while(rs.next()) {

                reportDTO = new ReportDTO();
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setTranType(rs.getString("TRAN_TYPE"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setValueDate(inputDate);
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportList.add(reportDTO);
            }

            res.info = reportList;
        } catch(Exception e) {

            logger.error("Exception while generating RTGS Inward Possible Return Report Payment report :"+e);
            throw new BOException("Exception while generating Graduated Payment Report :"+e);
        } finally {

            release(ps, rs);
        }
        return res;
    }

    //Method added by mohana on 16-Sep-2009 for Utr numberwise report
    /**
     * Method to Generate NEFT UTR No wise Reports.
     *
     * @param ReportInputDTO
     * @return CMsgDTO
     */
    public Message generateNEFTUTRNowiseReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        CMsgDTO msgDTO = new CMsgDTO();
        Message res = new Message();
        try {

            //Report Query.
            String qryUTRNoWiseReport;
            /**
             * Done on on 19-Jan-2011.
             *
             * Separated database calls to generate reports based on data available in local/archive schema.
             *
             * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we no need to look for data available in ARCHIVE schema.
             *
             * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we need to look both local and archive schema to generate reports.
             */

            if (inputDTO.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                     qryUTRNoWiseReport =  " SELECT nm.*, MSG_TYPE, MSG_SUB_TYPE " +
                    " FROM NEFT_MESSAGE_VW nm, Message_VW m ";

                        logger.info("Data fetching from archive database schema..");

                } else {
                     qryUTRNoWiseReport =  " SELECT nm.*, MSG_TYPE, MSG_SUB_TYPE " +
                    " FROM NEFT_MESSAGE nm, Message m ";

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {
                     qryUTRNoWiseReport =  " SELECT nm.*, MSG_TYPE, MSG_SUB_TYPE " +
                    " FROM NEFT_MESSAGE nm, Message m ";
                        logger.info("Data fetching from Local Database schema...");
                }


            String whereBlock = " WHERE nm.MSG_ID = m.MSG_ID AND m.MSG_SUB_TYPE NOT IN ('N03','N09')AND UPPER(UTR_NO) = ? ";
            String orderBy = " ORDER BY nm.MSG_ID DESC ";

            /**
             * Done on on 24-Feb-2011.
             *
             * Separated database calls to generate reports based on data available
             * in local/archive schema.
             *
             * We need to Check UTR is avaiable in Local Database schema . If its avaiable then we can genrate and Display .
             * If its not there in Local Database schema then we have to look for Archive Database so called VIEW
             *
             */
            String qryUTRNoWiseReportFromView = "SELECT nm.*, MSG_TYPE, MSG_SUB_TYPE " +
                                                "FROM NEFT_MESSAGE_VW nm, Message_VW m ";

            //Related to Branch/IFSC Specific.
            if (inputDTO.getIfscId() > 0 ) {

                whereBlock += " and IFSC_Master_ID = " + inputDTO.getIfscId();
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryUTRNoWiseReport += whereBlock + orderBy;
            qryUTRNoWiseReportFromView += whereBlock + orderBy;

            //To avoid printing queries in jboss console ..by priyak
            logger.debug("Query for generating the NEFT UTRNo wise Report : "+ qryUTRNoWiseReport);

            ps = con.prepareStatement(qryUTRNoWiseReport);
            //03-feb-2011//Below line has been commented to allow lowercase while searching UTR
             ps.setString(1, inputDTO.getUtrNo().toUpperCase());
            //ps.setString(1, inputDTO.getUtrNo());
            rs = ps.executeQuery();

            if(rs.next()) {

                msgDTO.msgId            = rs.getLong("MSG_ID");
                msgDTO.utrNo            = rs.getString("UTR_NO");
                //Added for LMS, In order to display the Status and its description in UI. 20100806
                long status             = rs.getLong("STATUS");
                String stName = getStatusName(status);
                msgDTO.statusName = stName;
                // LMS, 20100806 Ends
                msgDTO.status           = rs.getLong("STATUS");
                msgDTO.msgType          = rs.getString("MSG_TYPE");
                msgDTO.msgSubType       = rs.getString("MSG_SUB_TYPE");
                msgDTO.receiverAddress  = rs.getString("RECEIVER_ADDRESS");
                msgDTO.senderAddress    = rs.getString("SENDER_ADDRESS");
                msgDTO.amt              = rs.getString("AMOUNT");
                msgDTO.tranType         = rs.getString("TRAN_TYPE");
                msgDTO.entryBy          = rs.getString("ENTRY_BY");
                msgDTO.passBy           = rs.getString("PASS_BY");
                msgDTO.remarks          = rs.getString("REMARKS") == null ? "" : rs.getString("REMARKS");
                msgDTO.errorRemarks     = rs.getString("ERROR_REMARKS") == null ? "" : rs.getString("ERROR_REMARKS");

                InstaNEFTMessageBO bo = new InstaNEFTMessageBO();
                bo.initialize(getProperties());
                msgDTO.setArchivalData(false); // data is available in local db - no need to search in archival db
                //Pass the current CMsgDTO as filter condition to fetch the details.
                req.info = msgDTO;
                msgDTO = (CMsgDTO)(bo.viewFieldInfoForMsg(req).info);
            }
            release(ps, rs);
            if (msgDTO.msgId == 0) { //User Input UTR number is not avaiable in Local database.

                ps1 = con.prepareStatement(qryUTRNoWiseReportFromView);
                ps1.setString(1, inputDTO.getUtrNo());
                rs1 = ps1.executeQuery();
                if(rs1.next()) {

                    msgDTO.msgId            = rs1.getLong("MSG_ID");
                    msgDTO.utrNo            = rs1.getString("UTR_NO");
                    long status             = rs1.getLong("STATUS");
                    String stName = getStatusName(status);
                    msgDTO.statusName = stName;
                    msgDTO.status           = rs1.getLong("STATUS");
                    msgDTO.msgType          = rs1.getString("MSG_TYPE");
                    msgDTO.msgSubType       = rs1.getString("MSG_SUB_TYPE");
                    msgDTO.receiverAddress  = rs1.getString("RECEIVER_ADDRESS");
                    msgDTO.senderAddress    = rs1.getString("SENDER_ADDRESS");
                    msgDTO.amt              = rs1.getString("AMOUNT");
                    msgDTO.tranType         = rs1.getString("TRAN_TYPE");
                    msgDTO.entryBy          = rs1.getString("ENTRY_BY");
                    msgDTO.passBy           = rs1.getString("PASS_BY");
                    msgDTO.remarks          = rs1.getString("REMARKS") == null ? "" : rs1.getString("REMARKS");
                    msgDTO.errorRemarks     = rs1.getString("ERROR_REMARKS") == null ? "" : rs1.getString("ERROR_REMARKS");

                    InstaNEFTMessageBO bo = new InstaNEFTMessageBO();
                    bo.initialize(getProperties());

                    //Added to identify data present in local database or archival database
                    msgDTO.setArchivalData(true);
                    //Pass the current CMsgDTO as filter condition to fetch the details.
                    req.info = msgDTO;
                    msgDTO = (CMsgDTO)(bo.viewFieldInfoForMsg(req).info);
                } else {

                    //If user input UTR No is not available in both local and archive database, then we return NULL object
                    //to display zero records in UI.
                    msgDTO = null;
                }
            }
        } catch(Throwable th) {
            logger.error("Exception while generating NEFT UTRNo wise Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating NEFT UTRNo wise Report."
                                , th);
        } finally {

            release(ps, rs);
        }
        res.info = msgDTO;
        return res;
    }

    /**
     * Method to get specific field values using field no
     */
    public String getFieldValue(long msgId,String fieldNumber)
    throws TaskException {

        final String query = " SELECT mfs.value " + " FROM msgfield_stage mfs, msgfieldtype mft, MESSAGE m "
                             + " WHERE mfs.msg_field_type_id = mft.ID "
                             + " and mfs.MSG_ID = m.MSG_ID "
                             + " AND mfs.msg_id = ? "
                             + " AND mft.NO = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        String value = null;

        try {

            ps = con.prepareStatement(query);
            ps.setLong(1, msgId);
            ps.setString(2, fieldNumber);
            rs = ps.executeQuery();
            while (rs.next()) {

                value = rs.getString("VALUE");
            }
        } catch (Throwable e) {

            logger
            .error("Getting Exception While Fetching the Field Value :" + e);
            throw new TaskException("Getting Exception While Fetching the Field Value :",
                                    e);
        } finally {

            release(ps, rs);
        }
        return value;
    }

    /**
     * Method to geerate Neft Future Dated Txns Report
     * @param msg
     * @return message
     */
    public Message generateNEFTFutureDatedTxnsReport(Message msg)
    throws Exception {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>();
        Map<String, List<ReportDTO>> reportMap = new LinkedHashMap<String, List<ReportDTO>>();
        Message res = new Message();
        String  msgListSQL =
                        " SELECT NM.MSG_ID, NM.UTR_NO, NM.STATUS, NM.SENDER_ADDRESS, NM.RECEIVER_ADDRESS,     " +
                        " NM.AMOUNT, MD.TYPE, MD.SUB_TYPE, NM.TRAN_TYPE, NM.ENTRY_BY, NM.PASS_BY, " +
                        " NM.REMARKS, NM.ERROR_REMARKS, NM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, NM.BUSINESS_DATE, NM.VALUE_DATE " +
                        " FROM NEFT_MESSAGE NM, MSGDEFN MD, MESSAGE M, NEFT_STATUS RS                " +
                        " WHERE M.MSG_DEFN_ID = MD.ID AND NM.MSG_ID = M.MSG_ID AND NM.STATUS = RS.ID    ";
        ReportDTO reportDTO;
        try {
            /**
             * Done on on 08-Jan-2011.
             *
             * Separated database calls to generate reports based on data available
             * in local/archive schema.
             *
             * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we no need to look for data available in ARCHIVE schema.
             *
             * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
             * we need to look both local and archive schema to generate reports.
             */

            if (input.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

                if (new Date(input.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               msgListSQL =
                        " SELECT NM.MSG_ID, NM.UTR_NO, NM.STATUS, NM.SENDER_ADDRESS, NM.RECEIVER_ADDRESS,     " +
                        " NM.AMOUNT, MD.TYPE, MD.SUB_TYPE, NM.TRAN_TYPE, NM.ENTRY_BY, NM.PASS_BY, " +
                        " NM.REMARKS, NM.ERROR_REMARKS, NM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, NM.BUSINESS_DATE, NM.VALUE_DATE " +
                        " FROM NEFT_MESSAGE_vw NM, MSGDEFN MD, MESSAGE_vw M, NEFT_STATUS RS                " +
                        " WHERE M.MSG_DEFN_ID = MD.ID AND NM.MSG_ID = M.MSG_ID AND NM.STATUS = RS.ID    ";
                }
            }
            if (input.getPaymentType() != null){

                msgListSQL = msgListSQL + " AND MD.SUB_TYPE ='"  + input.getPaymentType()  + "'";
            }
            if (input.getStatus() != null) {

                msgListSQL = msgListSQL + " AND NM.STATUS =  " + input.getStatus();
            }
            if(!InstaDefaultConstants.COIFSCCode.equalsIgnoreCase(input.getIfscCode())) {

                msgListSQL = msgListSQL + " AND NM.IFSC_MASTER_ID = " + input.getIfscId();
            }

            msgListSQL = msgListSQL + " AND NM.IS_FUTURE_DATE_TXN = 1 AND  " +
                         "NM.BUSINESS_DATE BETWEEN '"+input.getValueDate()+ "' AND '" +input.getToDate()+"'";
            msgListSQL = msgListSQL + " ORDER BY NM.BUSINESS_DATE ASC ";

            logger.debug("Query for getting the Future date Message List : " + msgListSQL);
            ps = con.prepareStatement(msgListSQL);
            rs = ps.executeQuery();
            String prevDate = "";
            long msgId = 0;
            while(rs.next()) {

                String currDate = "";
                Date appDate = rs.getDate("BUSINESS_DATE");
                if(appDate != null) {
                    currDate = InstaReportUtil.convertToAppFormat(appDate);
                }
                /*
                 * This is to group the records based on the Branches.
                 */
                if (prevDate != null && prevDate!= "" &&
                                        !prevDate.equalsIgnoreCase(currDate)) {

                    reportMap.put(prevDate, reportList);
                    reportList = new ArrayList<ReportDTO>(0);
                }

                reportDTO= new ReportDTO();
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                msgId = rs.getLong("MSG_ID");
                reportDTO.setMsgSubType(rs.getString("SUB_TYPE"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setEntryBy(rs.getString("ENTRY_BY") == null ? "":rs.getString("ENTRY_BY"));
                reportDTO.setPassBy(rs.getString("PASS_BY") == null ? "":rs.getString("PASS_BY"));
                reportDTO.setValueDate(currDate);

                String beneAccNo = getFieldValue(msgId,"6061");
                String beneAccDetail = getFieldValue(msgId,"6081");
                if(beneAccNo != null && beneAccNo.indexOf("/") != -1) {

                    beneAccNo = beneAccNo.replace("/","");
                } else if(beneAccNo == null) {

                    beneAccNo = "";
                }
                reportDTO.setAccNo(beneAccNo);
                reportDTO.setBeneficiaryName(beneAccDetail);

                reportList.add(reportDTO);

//              Set the Current Date to the Previous Date.
                prevDate = currDate;
            }

            /*
             * This is to set the Last set of List.
             */
            if (reportList.size()>0){
                reportMap.put(prevDate, reportList);
            }
            res.info = reportMap;
            return res;
        } catch(Throwable th) {

            logger.error("Exception while generating Future Dated Txns Report :"
                         + th.getMessage());
            throw new BOException("Exception while generating Future Dated Txns Report"
                                + th);
        } finally {
            release(ps, rs);
        }
    }

    /**
     * Method used to get Exceptions report
     * @param msg
     * @return message
     */
    public Message generateNEFTExceptionReport(Message msg)
    throws Exception {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>();
        Map<String, List<ReportDTO>> reportMap = new LinkedHashMap<String, List<ReportDTO>>();
        Message res = new Message();
        String  msgListSQL =
                        " SELECT RM.MSG_ID, RM.UTR_NO,RM.STATUS,RS.NAME, RM.SENDER_ADDRESS, RM.RECEIVER_ADDRESS,     " +
                        " RM.AMOUNT, MD.TYPE, MD.SUB_TYPE, RM.TRAN_TYPE, RM.ENTRY_BY, RM.PASS_BY, " +
                        " RM.REMARKS, RM.ERROR_REMARKS, RM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, RM.BUSINESS_DATE, RM.VALUE_DATE " +
                        " FROM NEFT_MESSAGE RM, MSGDEFN MD, MESSAGE M, RTGS_STATUS RS                " +
                        " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID    ";

        /**
         * Done on on 19-Jan-2011.
         *
         * Separated database calls to generate reports based on data available in local/archive schema.
         *
         * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we no need to look for data available in ARCHIVE schema.
         *
         * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we need to look both local and archive schema to generate reports.
         */

        if (input.getValueDate() != null) {

            java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

            if (new Date(input.getValueDate()).compareTo(availableDataStartDate) < 0) {

                //archive database schema
                  msgListSQL =
                    " SELECT RM.MSG_ID, RM.UTR_NO,RM.STATUS,RS.NAME, RM.SENDER_ADDRESS, RM.RECEIVER_ADDRESS,     " +
                    " RM.AMOUNT, MD.TYPE, MD.SUB_TYPE, RM.TRAN_TYPE, RM.ENTRY_BY, RM.PASS_BY, " +
                    " RM.REMARKS, RM.ERROR_REMARKS, RM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, RM.BUSINESS_DATE, RM.VALUE_DATE " +
                    " FROM NEFT_MESSAGE_VW RM, MSGDEFN MD, MESSAGE_VW M, RTGS_STATUS RS                " +
                    " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID    ";

                    logger.info("Data fetching from archive database schema..");

            } else {

                    logger.info("Data fetching from Local Database schema...");
                }
            } else {

                    logger.info("Data fetching from Local Database schema...");
            }

        ReportDTO reportDTO;
        BigDecimal fromAmount = new BigDecimal(input.getFromAmount());
        BigDecimal toAmount = new BigDecimal(input.getToAmount());
        try {

            if (input.getValueDate() != null && input.getToDate() != null ) {

                msgListSQL += " and rm.BUSINESS_DATE BETWEEN '" + input.getValueDate()
                                             + "' and '" + input.getToDate() + "' ";
            }

//          Related to Transaction Type.
            if (input.getTransactionType() != null && !input.getTransactionType()
                                                             .equalsIgnoreCase("ALL")) {

                msgListSQL += " and rm.TRAN_TYPE = '" + input.getTransactionType() +"'" ;
            }
            if (input.getPaymentType() != null && !input.getPaymentType()
                                                            .equalsIgnoreCase("ALL")){

                msgListSQL += " and MD.SUB_TYPE ='"  + input.getPaymentType()  + "'";
            }

            msgListSQL += " and rm.STATUS in (2700,2800,2900,700) ";

//          Related to Source Type.
            if (input.getHostType() != null && !input.getHostType()
                                                            .equalsIgnoreCase("ALL")) {

                msgListSQL += " and rm.MSG_SOURCE = '" + input.getHostType() +"'" ;
            }

            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {

                msgListSQL += " and rm.AMOUNT BETWEEN " + input.getFromAmount() + " and "
                                                    + input.getToAmount();

            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {
                msgListSQL += " and rm.AMOUNT >= " + input.getFromAmount();
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                msgListSQL += " and rm.AMOUNT <= " + input.getToAmount();
            }
//          Related to Branch/IFSC Specific.
            if (input.getIfscId() > 0 ) {

                msgListSQL += " and rm.IFSC_Master_ID = " + input.getIfscId();
            }
            msgListSQL = msgListSQL + " ORDER BY RM.STATUS ASC,RM.BUSINESS_DATE ASC ";

            logger.debug("Query for getting the Future date Message List : " + msgListSQL);
            ps = con.prepareStatement(msgListSQL);
            rs = ps.executeQuery();
            long prevStatus = 0;
            long msgId = 0;
            String statusName = "";
            while(rs.next()) {

                long currStatus = rs.getLong("STATUS");
                /*
                 * This is to group the records based on the Branches.
                 */
                if (prevStatus != 0 && prevStatus != currStatus) {


                    if(prevStatus == 2700) {
                        statusName = "Error";
                    }else if(prevStatus == 2800) {
                        statusName = "Cancelled";
                    } else if(prevStatus == 2900) {
                        statusName = "Unsuccessful";
                    } else if (prevStatus == 700) {
                        statusName = "Error";
                    }
                    reportMap.put(statusName, reportList);
                    reportList = new ArrayList<ReportDTO>(0);
                }

                reportDTO= new ReportDTO();
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                msgId = rs.getLong("MSG_ID");
                reportDTO.setMsgSubType(rs.getString("SUB_TYPE"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setEntryBy(rs.getString("ENTRY_BY") == null ? "":rs.getString("ENTRY_BY"));
                reportDTO.setPassBy(rs.getString("PASS_BY") == null ? "":rs.getString("PASS_BY"));
                Date appDate = rs.getDate("BUSINESS_DATE");
                if(appDate != null) {
                    reportDTO.setValueDate(InstaReportUtil.convertToAppFormat(appDate));
                }
                reportDTO.setStatus(rs.getString("NAME"));
                reportList.add(reportDTO);

//              Set the Current Date to the Previous Date.
                prevStatus = currStatus;
            }

            /*
             * This is to set the Last set of List.
             */
            if(prevStatus == 2700) {
                statusName = "Error";
            }else if(prevStatus == 2800) {
                statusName = "Cancelled";
            } else if(prevStatus == 2900) {
                statusName = "Unsuccessful";
            } else if (prevStatus == 700) {
                statusName = "Error";
            }
            if (reportList.size()>0){
                reportMap.put(statusName, reportList);
            }
            res.info = reportMap;
            return res;
        } catch(Throwable th) {

            logger.error("Exception while generating NEFT Future Dated Txns Report :"
                         + th.getMessage());
            throw new BOException("Exception while generating NEFT Future Dated Txns Report"
                                + th);
        } finally {
            release(ps, rs);
        }
    }

//  Code Added on 29-Sep-2009 by Mohana for ReturnPaymentRejectedReport
    /**
     * Method to get inward possible return payment rejected by user report
     * @param msg
     * @return message
     * @throws BOException
     *
     */
    public Message generateNEFTReturnPaymentRejectedReport(Message msg)
    throws BOException {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Message res = new Message();
        String selReturnPayment = " SELECT RM.MSG_ID, RM.UTR_NO,RM.STATUS,RS.NAME, RM.SENDER_ADDRESS, RM.RECEIVER_ADDRESS,     " +
                                " RM.AMOUNT, MD.TYPE, MD.SUB_TYPE, RM.TRAN_TYPE, RM.ENTRY_BY, RM.PASS_BY, " +
                                " RM.REMARKS, RM.ERROR_REMARKS, RM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, RM.BUSINESS_DATE, RM.VALUE_DATE " +
                                " FROM NEFT_MESSAGE RM, MSGDEFN MD, MESSAGE M, NEFT_STATUS RS                " +
                                " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID " +
                                " AND RM.AUTORETURN = 1 AND RM.STATUS NOT IN (900,500)";
        /**
         * Done on on 19-Jan-2011.
         *
         * Separated database calls to generate reports based on data available in local/archive schema.
         *
         * If user input FROM DATE is less than or equal to RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we no need to look for data available in ARCHIVE schema.
         *
         * If user input FROM DATE is earlier than RTGSPARAMETERS.AVAILABLE_DATA_START_TIME value, then
         * we need to look both local and archive schema to generate reports.
         */

        if (input.getValueDate() != null) {

            java.sql.Date availableDataStartDate = getAvailableDataStartDate(con);

            if (new Date(input.getValueDate()).compareTo(availableDataStartDate) < 0) {

                //archive database schema
                selReturnPayment = " SELECT RM.MSG_ID, RM.UTR_NO,RM.STATUS,RS.NAME, RM.SENDER_ADDRESS, RM.RECEIVER_ADDRESS,     " +
                " RM.AMOUNT, MD.TYPE, MD.SUB_TYPE, RM.TRAN_TYPE, RM.ENTRY_BY, RM.PASS_BY, " +
                " RM.REMARKS, RM.ERROR_REMARKS, RM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, RM.BUSINESS_DATE, RM.VALUE_DATE " +
                " FROM NEFT_MESSAGE_VW RM, MSGDEFN MD, MESSAGE_VW M, NEFT_STATUS RS                " +
                " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID " +
                " AND RM.AUTORETURN = 1 AND RM.STATUS NOT IN (900,500)";

                    logger.info("Data fetching from archive database schema..");

            } else {

                    logger.info("Data fetching from Local Database schema...");
                }
            } else {

                    logger.info("Data fetching from Local Database schema...");
            }

        BigDecimal fromAmount = new BigDecimal(input.getFromAmount());
        BigDecimal toAmount = new BigDecimal(input.getToAmount());
        try {

            if(input.getIfscId() > 0) {

                selReturnPayment = selReturnPayment + " AND RM.IFSC_MASTER_ID = " + input.getIfscId();
            }
            if (input.getUserId() != null && input.getUserId().trim().length() > 0) {
                String user =input.getUserId().substring(input.getUserId().indexOf("-")+1).trim();
                selReturnPayment = selReturnPayment + " AND RM.PASS_BY = '" + user+"'";
            }
            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {

                selReturnPayment += " and rm.AMOUNT BETWEEN " + input.getFromAmount() + " and "
                                                    + input.getToAmount();

            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {
                selReturnPayment += " and rm.AMOUNT >= " + input.getFromAmount();
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                selReturnPayment += " and rm.AMOUNT <= " + input.getToAmount();
            }
            selReturnPayment = selReturnPayment + " AND RM.TRAN_TYPE = 'inward' AND RM.BUSINESS_DATE " +
                                 " BETWEEN '"+input.getValueDate()+ "' AND '" +input.getToDate()+"'";
            selReturnPayment = selReturnPayment + " ORDER BY RM.BUSINESS_DATE ASC ";
            ps = con.prepareStatement(selReturnPayment);
            rs = ps.executeQuery();
            while(rs.next()) {

                reportDTO = new ReportDTO();
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setTranType(rs.getString("TRAN_TYPE"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setUserId(rs.getString("PASS_BY"));
                Date appDate = rs.getDate("BUSINESS_DATE");
                if(appDate != null) {
                    reportDTO.setValueDate(InstaReportUtil.convertToAppFormat(appDate));
                }
                reportList.add(reportDTO);
            }
            res.info = reportList;
        } catch(Exception e) {

            logger.error("Exception while generating NEFT Inward Possible Return Payment Rejected By User report :"+e);
            throw new BOException("Exception while generating NEFT Inward Possible Return Payment Rejected By User report:"+e);
        } finally {

            release(ps, rs);
        }
        return res;
    }

    /**
     * Method to get specific field values using field no
     */
    public String getStatusName(long status)
    throws TaskException {

        final String query = " SELECT NAME FROM NEFT_STATUS WHERE ID = "+ status;

        PreparedStatement ps = null;
        ResultSet rs = null;
        String value = null;

        try {

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {

                value = rs.getString("NAME");
            }
        } catch (Throwable e) {

            logger
            .error("Getting Exception While Fetching the status description :" + e);
            throw new TaskException("Getting Exception While Fetching the status description :",
                                    e);
        } finally {

            release(ps, rs);
        }
        return value;
    }

    /**
     * Method to fetch AVAILABLE_DATA_START_DATE from RTGSPARAMETERS table
     * @return Date
     *
     */
    public java.sql.Date getAvailableDataStartDate(Connection con){

        final String getBusinessDateSQL = " SELECT parameter_value FROM rtgsparameters " +
                                          " WHERE parameter_name = 'AVAILABLE_DATA_START_DATE' ";

        String availableDataStartDate = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat userDateFmt = new SimpleDateFormat("dd-MM-yyyy");

        try {

            ps = con.prepareStatement(getBusinessDateSQL);
            rs = ps.executeQuery();
            while (rs.next()) {

                availableDataStartDate = rs.getString(1);
            }
            return new java.sql.Date(userDateFmt.parse(availableDataStartDate).getTime());
        } catch (Exception e) {

            logger.error("Exception While trying to get Available Data Start Date :" + e.getMessage());
            throw new RuntimeException("Exception While trying to get Available Data Start Date : " + e.getMessage());
        } finally {

            try {

                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Method to generate  Inward Bank wise Summary Report : NEFT
     *
     * @param msg Message
     *        msg contains parameters of sessionId,class,method and DTO
     * @return msg Message
     *        msg contains inwList List
     * @throws Exception when Inward Bankwise Summary is not generated.
     *
     */
    public Message generateNEFTInwBankSummaryReport(Message msg)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;

        List inwList = new ArrayList<ReportDTO>();
        try {

            ps = con.prepareStatement(getInwardBankSummaryReportQuery(inputDTO));
            ps.setString(1, inputDTO.getValueDate());
            ps.setString(2, inputDTO.getToDate());
            ps.setString(3, inputDTO.getValueDate());
            ps.setString(4, inputDTO.getToDate());

            rs = ps.executeQuery();

            while(rs.next()) {

                reportDTO = new ReportDTO();

                reportDTO.setSenderAddress(rs.getString(SENDERBANK));
                reportDTO.setCount(rs.getInt(TOTTXNCOUNT));
                reportDTO.setAmt(rs.getString(TOTAMOUNT));
                reportDTO.setCompletedTxnCount(rs.getInt(COMPLETEDTXNCOUNT));
                reportDTO.setCompletedTxnAmount(rs.getString(COMPLETEDTXNAMT));
                reportDTO.setRtnTxnCount(rs.getInt(RTNTXNCOUNT));
                reportDTO.setRtnTxnAmount(rs.getString(RTNTXNAMT));

                inwList.add(reportDTO);
            }


        } catch(Throwable th) {

            logger.error("Exception while generating NEFT Bank wise Summary Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating NEFT Bank wise Summary Report."
                                , th);
        } finally {

            release(ps, rs);
        }

        msg.info = inwList;
        return msg;
    }

    /**
     * Method to generate Outward Bank wise Summary Report : NEFT
     *
     * @param msg Message
     *      msg contains parameters of sessionId,class,method and DTO
     * @return msg Message
     *      msg contains inwList List
     * @throws Exception when outward bankwise summary report is not generated
     *
     */
    public Message generateNEFTOutwBankSummaryReport(Message msg)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        String bankIds = "";

        List inwList = new ArrayList<ReportDTO>();
        try {

            ps = con.prepareStatement(getOutwardBankSummaryReportQuery(inputDTO));
            ps.setString(1, inputDTO.getValueDate());
            ps.setString(2, inputDTO.getToDate());
            ps.setString(3, inputDTO.getValueDate());
            ps.setString(4, inputDTO.getToDate());

            rs = ps.executeQuery();

            while(rs.next()) {

                reportDTO = new ReportDTO();

                reportDTO.setSenderAddress(rs.getString("receiverbank"));
                reportDTO.setCount(rs.getInt(TOTTXNCOUNT));
                reportDTO.setAmt(rs.getString(TOTAMOUNT));
                reportDTO.setCompletedTxnCount(rs.getInt(COMPLETEDTXNCOUNT));
                reportDTO.setCompletedTxnAmount(rs.getString(COMPLETEDTXNAMT));
                reportDTO.setRtnTxnCount(rs.getInt(RTNTXNCOUNT));
                reportDTO.setRtnTxnAmount(rs.getString(RTNTXNAMT));

                inwList.add(reportDTO);
            }


        } catch(Throwable th) {

            logger.error("Exception while generating NEFT Bank wise Summary Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating NEFT Bank wise Summary Report."
                                , th);
        } finally {

            release(ps, rs);
        }

        msg.info = inwList;
        return msg;
    }

    /**
     * To get Outward summary report query.
     *
     * @param inputDTO ReportInputDTO
     *      inputDTO contains the method called getSelectedBank
     * @return queryString String
     *      queryString contains the query of the Inward Bank Summary Report
     */
    private String getInwardBankSummaryReportQuery(ReportInputDTO inputDTO) {

        StringBuffer queryString = new StringBuffer();
        String bankIds = "";

        if (inputDTO.getSelectedBank() != null) {

            String[] bankArray = inputDTO.getSelectedBank();
            for (int i = 0, len  = bankArray.length; i < len; i++) {
                if (bankIds.length() > 0) {
                    bankIds = bankIds + ",";
                }
                bankIds = bankIds + bankArray[i];
            }
        }

        queryString.append(" SELECT   a.senderbranch AS senderbank, SUM (a.COUNT) AS tottxncount, ");
        queryString.append("         SUM (a.totamount) AS totamount, ");
        queryString.append("         SUM (CASE ");
        queryString.append("                 WHEN a.status IN ('1000', '1200') ");
        queryString.append("                    THEN COUNT ");
        queryString.append("              END ");
        queryString.append("             ) AS completedtxncount, ");
        queryString.append("         SUM (CASE ");
        queryString.append("                 WHEN a.status IN ('1000', '1200') ");
        queryString.append("                    THEN totamount ");
        queryString.append("              END ");
        queryString.append("             ) AS completedtxnamt, ");
        queryString.append("         SUM (CASE ");
        queryString.append("                 WHEN a.status IN ('900') ");
        queryString.append("                    THEN COUNT ");
        queryString.append("              END) AS rtntxncount, ");
        queryString.append("         SUM (CASE ");
        queryString.append("                 WHEN a.status IN ('900') ");
        queryString.append("                    THEN totamount ");
        queryString.append("              END) AS rtntxnamt ");
        queryString.append("    FROM (SELECT   SUBSTR (nm.sender_address, 0, 4) AS senderbranch, ");
        queryString.append("                   nm.status, COUNT (*) AS COUNT, ");
        queryString.append("                   TO_CHAR (SUM (TO_NUMBER (nm.amount))) AS totamount ");
        queryString.append("              FROM NEFT_MESSAGE nm, MESSAGE m ");
        queryString.append("             WHERE m.msg_id = nm.msg_id ");
        queryString.append("               AND m.msg_sub_type = 'N02' ");
        queryString.append("               AND nm.value_date BETWEEN ? AND ? ");
        queryString.append("               AND nm.utr_no NOT IN ( ");
        queryString.append("                      SELECT utr_no ");
        queryString.append("                        FROM (SELECT DISTINCT nm.utr_no, nm.sender_address, ");
        queryString.append("                                              nm.receiver_address, ");
        queryString.append("                                              (SELECT REPLACE ");
        queryString.append("                                                         (REPLACE (mfs.VALUE, ");
        queryString.append("                                                                   CHR (10), ");
        queryString.append("                                                                   '' ");
        queryString.append("                                                                  ), ");
        queryString.append("                                                          CHR (13), ");
        queryString.append("                                                          '' ");
        queryString.append("                                                         ) ");
        queryString.append("                                                 FROM MSGDEFN md, ");
        queryString.append("                                                      MSGBLOCKDEFN mbd, ");
        queryString.append("                                                      MSGFIELDBLOCKDEFN mfb, ");
        queryString.append("                                                      MSGFIELDDEFN mfd, ");
        queryString.append("                                                      MSGFIELDTYPE mft, ");
        queryString.append("                                                      MSGFIELD_STAGE mfs ");
        queryString.append("                                                WHERE md.ID = mbd.msg_defn_id ");
        queryString.append("                                                  AND mbd.ID = mfb.block_id ");
        queryString.append("                                                  AND mfb.ID = ");
        queryString.append("                                                            mfd.field_block_id ");
        queryString.append("                                                  AND mfd.default_field_type_id = ");
        queryString.append("                                                                        mft.ID ");
        queryString.append("                                                  AND mft.ID = ");
        queryString.append("                                                         mfs.msg_field_type_id ");
        queryString.append("                                                  AND mft.NO = '7495' ");
        queryString.append("                                                  AND mfs.msg_id = m.msg_id ");
        queryString.append("                                                  AND md.ID = ");
        queryString.append("                                                         (SELECT MAX (ID) ");
        queryString.append("                                                            FROM MSGDEFN ");
        queryString.append("                                                           WHERE sub_type = ");
        queryString.append("                                                                    m.msg_sub_type)) ");
        queryString.append("                                                                        a7495, ");
        queryString.append("                                              nm.business_date, nm.amount, ");
        queryString.append("                                              nm.value_date, nm.batch_time, ");
        queryString.append("                                              (SELECT REPLACE ");
        queryString.append("                                                         (REPLACE (mfs.VALUE, ");
        queryString.append("                                                                   CHR (10), ");
        queryString.append("                                                                   '' ");
        queryString.append("                                                                  ), ");
        queryString.append("                                                          CHR (13), ");
        queryString.append("                                                          '' ");
        queryString.append("                                                         ) ");
        queryString.append("                                                 FROM MSGDEFN md, ");
        queryString.append("                                                      MSGBLOCKDEFN mbd, ");
        queryString.append("                                                      MSGFIELDBLOCKDEFN mfb, ");
        queryString.append("                                                      MSGFIELDDEFN mfd, ");
        queryString.append("                                                      MSGFIELDTYPE mft, ");
        queryString.append("                                                      MSGFIELD_STAGE mfs ");
        queryString.append("                                                WHERE md.ID = mbd.msg_defn_id ");
        queryString.append("                                                  AND mbd.ID = mfb.block_id ");
        queryString.append("                                                  AND mfb.ID = ");
        queryString.append("                                                            mfd.field_block_id ");
        queryString.append("                                                  AND mfd.default_field_type_id = ");
        queryString.append("                                                                        mft.ID ");
        queryString.append("                                                  AND mft.ID = ");
        queryString.append("                                                         mfs.msg_field_type_id ");
        queryString.append("                                                  AND mft.NO = '2006' ");
        queryString.append("                                                  AND mfs.msg_id = m.msg_id ");
        queryString.append("                                                  AND md.ID = ");
        queryString.append("                                                         (SELECT MAX (ID) ");
        queryString.append("                                                            FROM MSGDEFN ");
        queryString.append("                                                           WHERE sub_type = ");
        queryString.append("                                                                    m.msg_sub_type)) ");
        queryString.append("                                                                     nnn_2006, ");
        queryString.append("                                              (SELECT REPLACE ");
        queryString.append("                                                         (REPLACE (mfs.VALUE, ");
        queryString.append("                                                                   CHR (10), ");
        queryString.append("                                                                   '' ");
        queryString.append("                                                                  ), ");
        queryString.append("                                                          CHR (13), ");
        queryString.append("                                                          '' ");
        queryString.append("                                                         ) ");
        queryString.append("                                                 FROM MSGDEFN md, ");
        queryString.append("                                                      MSGBLOCKDEFN mbd, ");
        queryString.append("                                                      MSGFIELDBLOCKDEFN mfb, ");
        queryString.append("                                                      MSGFIELDDEFN mfd, ");
        queryString.append("                                                      MSGFIELDTYPE mft, ");
        queryString.append("                                                      MSGFIELD_STAGE mfs ");
        queryString.append("                                                WHERE md.ID = mbd.msg_defn_id ");
        queryString.append("                                                  AND mbd.ID = mfb.block_id ");
        queryString.append("                                                  AND mfb.ID = ");
        queryString.append("                                                            mfd.field_block_id ");
        queryString.append("                                                  AND mfd.default_field_type_id = ");
        queryString.append("                                                                        mft.ID ");
        queryString.append("                                                  AND mft.ID = ");
        queryString.append("                                                         mfs.msg_field_type_id ");
        queryString.append("                                                  AND mft.NO = '7495' ");
        queryString.append("                                                  AND mfs.msg_id = m.msg_id ");
        queryString.append("                                                  AND md.ID = ");
        queryString.append("                                                         (SELECT MAX (ID) ");
        queryString.append("                                                            FROM MSGDEFN ");
        queryString.append("                                                           WHERE sub_type = ");
        queryString.append("                                                                    m.msg_sub_type)) ");
        queryString.append("                                                                     nnn_7495 ");
        queryString.append("                                         FROM MESSAGE m, NEFT_MESSAGE nm ");
        queryString.append("                                        WHERE nm.msg_id = m.msg_id ");
        queryString.append("                                          AND nm.tran_type = 'inward' ");
        queryString.append("                                          AND m.msg_sub_type = 'N02' ");
        queryString.append("                                          AND nm.value_date ");
        queryString.append("                                                 BETWEEN ? ");
        queryString.append("                                                     AND ? ");

        if (inputDTO.getSelectedBank() != null && inputDTO.getSelectedBank().length > 0) {

            queryString.append("                                          AND SUBSTR (nm.sender_address, 0, ");
            queryString.append("                                                      11) IN ( ");
            queryString.append("                                                 SELECT ifsc ");
            queryString.append("                                                   FROM IFSCMASTER ");
            queryString.append("                                                  WHERE bank_master_id IN ");
            queryString.append("                                                                     (" + bankIds + ")) ");
        }
        queryString.append("                                          AND nm.status <> 700) v ");
        queryString.append("                       WHERE (   (    UPPER (nnn_2006) LIKE '%VIJB%' ");
        queryString.append("                                  AND LENGTH (SUBSTR (UPPER (nnn_2006), ");
        queryString.append("                                                      INSTR (UPPER (nnn_2006), ");
        queryString.append("                                                             'VIJB' ");
        queryString.append("                                                            ), ");
        queryString.append("                                                      16 ");
        queryString.append("                                                     ) ");
        queryString.append("                                             ) = 16 ");
        queryString.append("                                 ) ");
        queryString.append("                              OR (    UPPER (nnn_7495) LIKE '%VIJB%' ");
        queryString.append("                                  AND LENGTH (SUBSTR (UPPER (nnn_7495), ");
        queryString.append("                                                      INSTR (UPPER (nnn_7495), ");
        queryString.append("                                                             'VIJB' ");
        queryString.append("                                                            ), ");
        queryString.append("                                                      16 ");
        queryString.append("                                                     ) ");
        queryString.append("                                             ) = 16 ");
        queryString.append("                                 ) ");
        queryString.append("                              OR (   UPPER (nnn_7495) LIKE '%NO SUCH%' ");
        queryString.append("                                  OR UPPER (nnn_7495) LIKE '%INVALID%' ");
        queryString.append("                                  OR UPPER (nnn_7495) LIKE '%NOT %' ");
        queryString.append("                                  OR UPPER (nnn_7495) LIKE '%DIFFER%' ");
        queryString.append("                                 ) ");
        queryString.append("                             )) ");
        if (inputDTO.getSelectedBank() != null && inputDTO.getSelectedBank().length > 0) {

            queryString.append("               AND SUBSTR (nm.sender_address, 0, 11) IN ( ");
            queryString.append("                                              SELECT ifsc ");
            queryString.append("                                                FROM IFSCMASTER ");
            queryString.append("                                               WHERE bank_master_id IN ");
            queryString.append("                                                                     (" + bankIds + ")) ");
        }
        queryString.append("          GROUP BY SUBSTR (nm.sender_address, 0, 4), nm.status ");
        queryString.append("          ORDER BY SUBSTR (nm.sender_address, 0, 4)) a ");
        queryString.append(" GROUP BY a.senderbranch ");
        queryString.append(" ORDER BY a.senderbranch ");

        return queryString.toString();
    }


    /**
     * To get Outward summary report query.
     *
     * @param inputDTO ReportInputDTO
     *      inputDTO contains the method called getSelectedBank
     * @return queryString String
     *      queryString contains the query of Outward Bank Summary Report
     *
     */
    private String getOutwardBankSummaryReportQuery(ReportInputDTO inputDTO) {

        StringBuffer queryString = new StringBuffer();
        String bankIds = "";

        if (inputDTO.getSelectedBank() != null) {

            String[] bankArray = inputDTO.getSelectedBank();
            for (int i = 0, len  = bankArray.length; i < len; i++) {
                if (bankIds.length() > 0) {
                    bankIds = bankIds + ",";
                }
                bankIds = bankIds + bankArray[i];
            }
        }

        queryString.append(" SELECT   receiverbank, SUM (tottxncount) tottxncount, ");
        queryString.append("         SUM (totamount) totamount, SUM (completedtxncount) completedtxncount, ");
        queryString.append("         SUM (completedtxnamt) completedtxnamt, SUM (rtntxncount) rtntxncount, ");
        queryString.append("         SUM (rtntxnamt) rtntxnamt ");
        queryString.append("    FROM ((SELECT   a.receiverbranch AS receiverbank, ");
        queryString.append("                    SUM (a.COUNT) AS tottxncount, ");
        queryString.append("                    SUM (a.totamount) AS totamount, ");
        queryString.append("                    SUM ");
        queryString.append("                       (CASE ");
        queryString.append("                           WHEN a.status IN ('3000', '3200', '2550') ");
        queryString.append("                              THEN COUNT ");
        queryString.append("                        END ");
        queryString.append("                       ) AS completedtxncount, ");
        queryString.append("                    SUM ");
        queryString.append("                       (CASE ");
        queryString.append("                           WHEN a.status IN ('3000', '3200', '2550') ");
        queryString.append("                              THEN totamount ");
        queryString.append("                        END ");
        queryString.append("                       ) AS completedtxnamt, ");
        queryString.append("                    0 AS rtntxncount, 0 AS rtntxnamt ");
        queryString.append("               FROM (SELECT   SUBSTR (nm.receiver_address, ");
        queryString.append("                                      0, ");
        queryString.append("                                      4 ");
        queryString.append("                                     ) AS receiverbranch, ");
        queryString.append("                              nm.status, COUNT (*) AS COUNT, ");
        queryString.append("                              TO_CHAR (SUM (TO_NUMBER (nm.amount)) ");
        queryString.append("                                      ) AS totamount ");
        queryString.append("                         FROM neft_message nm, MESSAGE m ");
        queryString.append("                        WHERE m.msg_id = nm.msg_id ");
        queryString.append("                          AND m.msg_sub_type = 'N06' ");
        queryString.append("                          AND nm.value_date BETWEEN ? ");
        queryString.append("                                                   AND ? ");

        if (inputDTO.getSelectedBank() != null && inputDTO.getSelectedBank().length > 0) {

            queryString.append("                     AND SUBSTR (nm.receiver_address, 0, 11) IN ( ");
            queryString.append("                            SELECT ifsc FROM IFSCMASTER ");
            queryString.append("                             WHERE bank_master_id IN ");
            queryString.append("                              ("+ bankIds +")) ");
        }
        queryString.append("                     GROUP BY SUBSTR (nm.receiver_address, 0, 4), nm.status ");
        queryString.append("                     ORDER BY SUBSTR (nm.receiver_address, 0, 4)) a ");
        queryString.append("           GROUP BY a.receiverbranch) ");
        queryString.append("          UNION ");
        queryString.append("          (SELECT   SUBSTR (v.sender_address, 0, 4) AS receiverbank, ");
        queryString.append("                    0 AS tottxncount, 0 AS totamount, 0 AS completedtxncount, ");
        queryString.append("                    0 AS completedtxnamt, COUNT (*) rtntxncount, ");
        queryString.append("                    SUM (v.amount) AS rtntxnamt ");
        queryString.append("               FROM (SELECT DISTINCT nm.utr_no, nm.sender_address, ");
        queryString.append("                                     nm.receiver_address, ");
        queryString.append("                                     (SELECT REPLACE ");
        queryString.append("                                                  (REPLACE (mfs.VALUE, ");
        queryString.append("                                                            CHR (10), ");
        queryString.append("                                                            '' ");
        queryString.append("                                                           ), ");
        queryString.append("                                                   CHR (13), ");
        queryString.append("                                                   '' ");
        queryString.append("                                                  ) ");
        queryString.append("                                        FROM msgdefn md, ");
        queryString.append("                                             msgblockdefn mbd, ");
        queryString.append("                                             msgfieldblockdefn mfb, ");
        queryString.append("                                             msgfielddefn mfd, ");
        queryString.append("                                             msgfieldtype mft, ");
        queryString.append("                                             msgfield_stage mfs ");
        queryString.append("                                       WHERE md.ID = mbd.msg_defn_id ");
        queryString.append("                                         AND mbd.ID = mfb.block_id ");
        queryString.append("                                         AND mfb.ID = mfd.field_block_id ");
        queryString.append("                                         AND mfd.default_field_type_id = ");
        queryString.append("                                                                        mft.ID ");
        queryString.append("                                         AND mft.ID = mfs.msg_field_type_id ");
        queryString.append("                                         AND mft.NO = '7495' ");
        queryString.append("                                         AND mfs.msg_id = m.msg_id ");
        queryString.append("                                         AND md.ID = ");
        queryString.append("                                                (SELECT MAX (ID) ");
        queryString.append("                                                   FROM msgdefn ");
        queryString.append("                                                  WHERE sub_type = ");
        queryString.append("                                                                m.msg_sub_type)) ");
        queryString.append("                                                                        a7495, ");
        queryString.append("                                     nm.business_date, nm.amount, ");
        queryString.append("                                     nm.value_date, nm.batch_time, ");
        queryString.append("                                     (SELECT REPLACE ");
        queryString.append("                                                (REPLACE (mfs.VALUE, ");
        queryString.append("                                                          CHR (10), ");
        queryString.append("                                                          '' ");
        queryString.append("                                                         ), ");
        queryString.append("                                                 CHR (13), ");
        queryString.append("                                                 '' ");
        queryString.append("                                                ) ");
        queryString.append("                                        FROM msgdefn md, ");
        queryString.append("                                             msgblockdefn mbd, ");
        queryString.append("                                             msgfieldblockdefn mfb, ");
        queryString.append("                                             msgfielddefn mfd, ");
        queryString.append("                                             msgfieldtype mft, ");
        queryString.append("                                             msgfield_stage mfs ");
        queryString.append("                                       WHERE md.ID = mbd.msg_defn_id ");
        queryString.append("                                         AND mbd.ID = mfb.block_id ");
        queryString.append("                                         AND mfb.ID = mfd.field_block_id ");
        queryString.append("                                         AND mfd.default_field_type_id = ");
        queryString.append("                                                                        mft.ID ");
        queryString.append("                                         AND mft.ID = mfs.msg_field_type_id ");
        queryString.append("                                         AND mft.NO = '2006' ");
        queryString.append("                                         AND mfs.msg_id = m.msg_id ");
        queryString.append("                                         AND md.ID = ");
        queryString.append("                                                (SELECT MAX (ID) ");
        queryString.append("                                                   FROM msgdefn ");
        queryString.append("                                                  WHERE sub_type = ");
        queryString.append("                                                                m.msg_sub_type)) ");
        queryString.append("                                                                     nnn_2006, ");
        queryString.append("                                     (SELECT REPLACE ");
        queryString.append("                                                (REPLACE (mfs.VALUE, ");
        queryString.append("                                                          CHR (10), ");
        queryString.append("                                                          '' ");
        queryString.append("                                                         ), ");
        queryString.append("                                                 CHR (13), ");
        queryString.append("                                                 '' ");
        queryString.append("                                                ) ");
        queryString.append("                                        FROM msgdefn md, ");
        queryString.append("                                             msgblockdefn mbd, ");
        queryString.append("                                             msgfieldblockdefn mfb, ");
        queryString.append("                                             msgfielddefn mfd, ");
        queryString.append("                                             msgfieldtype mft, ");
        queryString.append("                                             msgfield_stage mfs ");
        queryString.append("                                       WHERE md.ID = mbd.msg_defn_id ");
        queryString.append("                                         AND mbd.ID = mfb.block_id ");
        queryString.append("                                         AND mfb.ID = mfd.field_block_id ");
        queryString.append("                                         AND mfd.default_field_type_id = ");
        queryString.append("                                                                        mft.ID ");
        queryString.append("                                         AND mft.ID = mfs.msg_field_type_id ");
        queryString.append("                                         AND mft.NO = '7495' ");
        queryString.append("                                         AND mfs.msg_id = m.msg_id ");
        queryString.append("                                         AND md.ID = ");
        queryString.append("                                                (SELECT MAX (ID) ");
        queryString.append("                                                   FROM msgdefn ");
        queryString.append("                                                  WHERE sub_type = ");
        queryString.append("                                                                m.msg_sub_type)) ");
        queryString.append("                                                                     nnn_7495 ");
        queryString.append("                                FROM MESSAGE m, neft_message nm ");
        queryString.append("                               WHERE nm.msg_id = m.msg_id ");
        queryString.append("                                 AND nm.tran_type = 'inward' ");
        queryString.append("                                 AND m.msg_sub_type = 'N02' ");
        queryString.append("                                 AND nm.value_date BETWEEN ? ");
        queryString.append("                                                          AND ? ");

        if (inputDTO.getSelectedBank() != null && inputDTO.getSelectedBank().length > 0) {
            queryString.append("                                 AND SUBSTR (nm.sender_address, 0, 11) IN ( ");
            queryString.append("                                   SELECT ifsc FROM IFSCMASTER ");
            queryString.append("                                   WHERE bank_master_id IN ");
            queryString.append("                                    ("+ bankIds +")) ");
        }

        queryString.append("                                 AND nm.status <> 700) v ");
        queryString.append("              WHERE (   (    UPPER (nnn_2006) LIKE '%VIJB%' ");
        queryString.append("                         AND LENGTH (SUBSTR (UPPER (nnn_2006), ");
        queryString.append("                                             INSTR (UPPER (nnn_2006), 'VIJB'), ");
        queryString.append("                                             16 ");
        queryString.append("                                            ) ");
        queryString.append("                                    ) = 16 ");
        queryString.append("                        ) ");
        queryString.append("                     OR (    UPPER (nnn_7495) LIKE '%VIJB%' ");
        queryString.append("                         AND LENGTH (SUBSTR (UPPER (nnn_7495), ");
        queryString.append("                                             INSTR (UPPER (nnn_7495), 'VIJB'), ");
        queryString.append("                                             16 ");
        queryString.append("                                            ) ");
        queryString.append("                                    ) = 16 ");
        queryString.append("                        ) ");
        queryString.append("                     OR (   UPPER (nnn_7495) LIKE '%NO SUCH%' ");
        queryString.append("                         OR UPPER (nnn_7495) LIKE '%INVALID%' ");
        queryString.append("                         OR UPPER (nnn_7495) LIKE '%NOT %' ");
        queryString.append("                         OR UPPER (nnn_7495) LIKE '%DIFFER%' ");
        queryString.append("                        ) ");
        queryString.append("                    ) ");
        queryString.append("           GROUP BY sender_address)) ");
        queryString.append("GROUP BY receiverbank ");
        queryString.append("ORDER BY receiverbank  ");


        return queryString.toString();

    }

    /**
     * Method to generate  Inward Bank wise Detailed Report : NEFT
     *
     * @param msg Message
     *      msg contains parameters of sessionId,class,method and DTO
     * @return msg Message
     *      msg contains reportMap Map
     * @throws Exception when inward bankwise report not generated.
     *
     */
    public Message generateNEFTInwBankDetailedReport(Message msg)
    throws Exception {

        ReportInputDTO reportInputDTO = (ReportInputDTO) msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        String bankIds = "";

        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);

        Map<String, List<ReportDTO>> reportMap =new LinkedHashMap<String, List<ReportDTO>>();

        try {

            //NEFT Bank wise Report Query.
        	StringBuffer queryString = new StringBuffer();
        	queryString.append("SELECT a.value_date, b.senderifsc, a.utrno, a.amount, ");
        	queryString.append("   a.recvAddrifsc, a.ben_acc_name, a.ben_acc_type, a.ben_acc_no, ");
        	queryString.append("   a.sender_acc_name, a.SENDER_ACC_TYPE, ");
        	queryString.append("   a.sender_acc_no, a.status_desc, b.totamt as tottxnamt FROM ");
        	queryString.append(" (SELECT  value_date,  utrno, amount, ");
        	queryString.append("       recvAddr recvAddrIFSC, ben_acc_name,  ben_acc_type, ben_acc_no, ");
        	queryString.append("   SUBSTR(senderAddr, 0, 4) senderIFSC, sender_acc_name, SENDER_ACC_TYPE, sender_acc_no, status_desc ");
        	queryString.append("   FROM NEFTREPORTINFO ");
        	queryString.append("   WHERE  tran_type = 'inward'  AND  SUBTYPE IN ('N02') AND value_date ");
        	queryString.append("   BETWEEN ? AND ? ");

        	if (reportInputDTO.getSelectedBank() != null) {

            	String[] bankArray = reportInputDTO.getSelectedBank();
                for (int i = 0, len  = bankArray.length; i < len; i++) {
                    if (bankIds.length() > 0) {
                        bankIds = bankIds + ",";
                    }
                    bankIds = bankIds + bankArray[i];
                }
        	}

        	if (reportInputDTO.getSelectedBank() != null && reportInputDTO.getSelectedBank().length > 0) {

        	    queryString.append("       AND SUBSTR (senderaddr, 0, 11) IN ");
                queryString.append(" (SELECT ifsc FROM IFSCMASTER WHERE bank_master_id IN ("+ bankIds +")) ");
        	}
        	queryString.append("       AND source IN ('SFMS')) a, ");
        	queryString.append("  (SELECT  SUBSTR(senderaddr, 0, 4) senderIFSC, SUM(amount) totamt ");
        	queryString.append("   FROM NEFTREPORTINFO ");
        	queryString.append("   WHERE  tran_type = 'inward'  AND  SUBTYPE IN ('N02') ");
        	queryString.append("AND value_date BETWEEN ? AND ? ");
        	if (reportInputDTO.getSelectedBank() != null && reportInputDTO.getSelectedBank().length > 0) {
        	    queryString.append("AND SUBSTR (senderaddr, 0, 11) IN (SELECT ifsc FROM IFSCMASTER WHERE bank_master_id IN ("+ bankIds +")) ");
        	}
        	queryString.append("AND source IN ('SFMS') ");
        	queryString.append("   GROUP BY SUBSTR(senderaddr, 0, 4)) b ");
        	queryString.append("   WHERE a.senderifsc = b.senderifsc ");
        	queryString.append("   ORDER BY a.senderifsc ASC, ");
        	queryString.append("   a.value_date ASC , ");
        	queryString.append("   a.status_desc ASC ");

            ps = con.prepareStatement(queryString.toString());
            ps.setString(1, reportInputDTO.getValueDate());
            ps.setString(2, reportInputDTO.getToDate());
            ps.setString(3, reportInputDTO.getValueDate());
            ps.setString(4, reportInputDTO.getToDate());

            rs = ps.executeQuery();
            String prevBank = "";
            String prevBankTxnSumAmount = "";

            List inwardTranInfoList = new ArrayList<TransactionInfo>(0);

            while(rs.next()) {

            	String senderBank = rs.getString("SENDERIFSC");

                 // This is to group the records based on the Bank.

                if (prevBank != null && prevBank!= "" &&
                                        !prevBank.equalsIgnoreCase(senderBank)) {

                    reportMap.put(prevBank + "_" + prevBankTxnSumAmount, inwardTranInfoList);
                    inwardTranInfoList = new ArrayList<TransactionInfo>(0);
                }

                TransactionInfo ti = new TransactionInfo();

                ti.valueDate = rs.getDate(VALUE_DATE);
                ti.utrNo = rs.getString(UTRNO);
                ti.amount = rs.getBigDecimal(AMOUNT);

                ti.beneficiaryInfo = new CustomerInfo();
                ti.beneficiaryInfo.accIfsc = rs.getString(RECVADDRIFSC);
                ti.beneficiaryInfo.accName = rs.getString(BEN_ACC_NAME);
                ti.beneficiaryInfo.accNo = rs.getString(BEN_ACC_NO);
                ti.beneficiaryInfo.accType = rs.getString(BEN_ACC_TYPE);

                ti.senderInfo      = new CustomerInfo();
                ti.senderInfo.accIfsc = rs.getString(SENDERIFSC);
                ti.senderInfo.accName = rs.getString(SENDER_ACC_NAME);
                ti.senderInfo.accNo = rs.getString(SENDER_ACC_NO);
                ti.senderInfo.accType = rs.getString(SENDER_ACC_TYPE);

                ti.statusShortDesc = rs.getString(STATUS_DESC);
                prevBankTxnSumAmount = rs.getString(TOTTXNAMT);
                // Sets the DTO Object in to the List
                inwardTranInfoList.add(ti);

                //Set the Current Branch to the Previous Branch.
                prevBank = senderBank;

            }

            /*
             * This is to set the Last set of List.
             */
            if (inwardTranInfoList.size()>0){
                reportMap.put(prevBank + "_" + prevBankTxnSumAmount, inwardTranInfoList);
            }


        } catch(Throwable th) {

            logger.error("Exception while generating NEFT Bank wise Summary Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating NEFT Bank wise Summary Report."
                                , th);
        } finally {

            release(ps, rs);
        }

        msg.info = reportMap;
        return msg;
    }

    /**
     * Method to generate Outward bank wise detailed Report : NEFT
     *
     * @param msg Message
     *      msg contains parameters of sessionId,class,method and DTO
     * @return msg Message
     *      msg contains reportMap Map
     * @throws Exception when outward bankwise reprot not generated.
     *
     */
    public Message generateNEFTOutwardBankDetailedReport(Message msg)
    throws Exception {

        ReportInputDTO reportInputDTO = (ReportInputDTO) msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        String bankIds = "";

        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);

        Map<String, List<ReportDTO>> reportMap =new LinkedHashMap<String, List<ReportDTO>>();

        try {

            //NEFT Bank wise Report Query.
        	StringBuffer queryString = new StringBuffer();
        	queryString.append("SELECT a.value_date, a.senderIFSC, a.utrno, a.amount, ");
        	queryString.append("       a.recvAddrifsc, a.ben_acc_name, a.ben_acc_type, a.ben_acc_no, ");
            queryString.append("       a.sender_acc_name, a.SENDER_ACC_TYPE, ");
            queryString.append("       a.sender_acc_no, a.status_desc, b.totamt as TOTTXNAMT  FROM ");
            queryString.append("(SELECT  value_date,  utrno, amount,        ");
            queryString.append("        SUBSTR(recvAddr, 0, 4) recvAddrIFSC, ben_acc_name,  ben_acc_type, ben_acc_no, status_desc,");
            queryString.append("        senderAddr AS senderIFSC, sender_acc_name, SENDER_ACC_TYPE, sender_acc_no ");
            queryString.append("    FROM NEFTREPORTINFO ");
            queryString.append("    WHERE  tran_type = 'outward'  AND  (SUBTYPE IN ('N06', 'N07')) ");
            queryString.append( "AND value_date BETWEEN ? AND ? ");

            if (reportInputDTO.getSelectedBank() != null) {

                String[] bankArray = reportInputDTO.getSelectedBank();
                for (int i = 0, len  = bankArray.length; i < len; i++) {
                    if (bankIds.length() > 0) {
                        bankIds = bankIds + ",";
                    }
                    bankIds = bankIds + bankArray[i];
                }
            }
            if (reportInputDTO.getSelectedBank() != null && reportInputDTO.getSelectedBank().length > 0) {

                queryString.append("    AND SUBSTR (recvaddr, 0, 11) IN ");
                queryString.append("(SELECT ifsc FROM IFSCMASTER WHERE bank_master_id IN ("+ bankIds +")) ");
            }
            queryString.append("    AND source IN ('LMS', 'CBS', 'NEFT_UPLOAD'))a, ");
            queryString.append("(SELECT  SUBSTR(recvaddr, 0, 4) receiverIFSC, SUM(amount) totamt ");
            queryString.append("    FROM NEFTREPORTINFO  ");
            queryString.append("    WHERE  tran_type = 'outward'  AND  (SUBTYPE IN ('N06', 'N07')) AND value_date BETWEEN ? AND ? ");
            if (reportInputDTO.getSelectedBank() != null && reportInputDTO.getSelectedBank().length > 0) {
                queryString.append("    AND SUBSTR (recvaddr, 0, 11) IN (SELECT ifsc FROM IFSCMASTER WHERE bank_master_id IN ("+ bankIds +")) ");
            }
            queryString.append("    AND source IN ('LMS', 'CBS', 'NEFT_UPLOAD') ");
            queryString.append("    GROUP BY SUBSTR(recvaddr, 0, 4)) b ");
            queryString.append("WHERE a.recvAddrIFSC = b.receiverIFSC ");
            queryString.append("    ORDER BY a.senderifsc ASC, ");
            queryString.append("    a.value_date ASC , ");
            queryString.append("    a.status_desc ASC ");

            ps = con.prepareStatement(queryString.toString());
            ps.setString(1, reportInputDTO.getValueDate());
            ps.setString(2, reportInputDTO.getToDate());
            ps.setString(3, reportInputDTO.getValueDate());
            ps.setString(4, reportInputDTO.getToDate());

            rs = ps.executeQuery();
            String prevBank = "";
            String prevBankTxnSumAmount = "";

            List inwardTranInfoList = new ArrayList<TransactionInfo>(0);

            while(rs.next()) {

            	String senderBank = rs.getString("RECVADDRIFSC");

            	/*
                 * This is to group the records based on the Bank.
                 */
                if (prevBank != null && prevBank!= "" &&
                                        !prevBank.equalsIgnoreCase(senderBank)) {

                    reportMap.put(prevBank + "_" + prevBankTxnSumAmount, inwardTranInfoList);
                    inwardTranInfoList = new ArrayList<TransactionInfo>(0);
                }

                TransactionInfo ti = new TransactionInfo();

                ti.valueDate = rs.getDate(VALUE_DATE);
                ti.utrNo = rs.getString(UTRNO);
                ti.amount = rs.getBigDecimal(AMOUNT);

                ti.beneficiaryInfo = new CustomerInfo();
                ti.beneficiaryInfo.accIfsc = rs.getString(RECVADDRIFSC);
                ti.beneficiaryInfo.accName = rs.getString(BEN_ACC_NAME);
                ti.beneficiaryInfo.accNo = rs.getString(BEN_ACC_NO);
                ti.beneficiaryInfo.accType = rs.getString(BEN_ACC_TYPE);

                ti.senderInfo      = new CustomerInfo();
                ti.senderInfo.accIfsc = rs.getString(SENDERIFSC);
                ti.senderInfo.accName = rs.getString(SENDER_ACC_NAME);
                ti.senderInfo.accNo = rs.getString(SENDER_ACC_NO);
                ti.senderInfo.accType = rs.getString(SENDER_ACC_TYPE);

                ti.statusShortDesc = rs.getString(STATUS_DESC);
                prevBankTxnSumAmount = rs.getString(TOTTXNAMT);

                // Sets the DTO Object in to the List
                inwardTranInfoList.add(ti);

                //Set the Current Branch to the Previous Branch.
                prevBank = senderBank;

            }

            /*
             * This is to set the Last set of List.
             */
            if (inwardTranInfoList.size()>0){
                reportMap.put(prevBank + "_" + prevBankTxnSumAmount, inwardTranInfoList);
            }

        } catch(Throwable th) {

            logger.error("Exception while generating NEFT Outward Bank wise Detailed Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating NEFT Outward Bank wise Detailed Report."
                                , th);
        } finally {

            release(ps, rs);
        }

        msg.info = reportMap;
        return msg;
    }

    /**
     * This method is used to initialize the database connection
     * using the datasource.
     * @param proMap Properties
     * @return Connection this.jdbcConnection
     * @throws Exception - throw exception when connection initalize.
     */
    public Connection initConnection(Properties proMap)
    throws Exception {

        try {

            this.con = (Connection) proMap.get("CONNECTION");
            return this.con;
        } catch (Throwable t) {
            throw new ServerException(t.getMessage());
        }
    }

}
