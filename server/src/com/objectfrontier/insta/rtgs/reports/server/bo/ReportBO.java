/* $Header$ */

/*
 * @(#)ReportBO.java
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
package com.objectfrontier.insta.rtgs.reports.server.bo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.host.rhs.meta.client.vo.HostIFSCDetailsValueObject;
import com.objectfrontier.arch.dto.Message;
import com.objectfrontier.arch.server.bo.BOException;
import com.objectfrontier.arch.server.dao.DAOException;
import com.objectfrontier.arch.server.dto.ClientInfo;
import com.objectfrontier.arch.server.dto.DTOParserException;
import com.objectfrontier.insta.reports.constants.ReportConstants;
import com.objectfrontier.insta.reports.server.bo.InstaReportBO;
import com.objectfrontier.insta.reports.server.util.ConversionUtil;
import com.objectfrontier.insta.reports.server.util.MessageUtil;
import com.objectfrontier.rtgs.dto.HostIFSCDetailsDTO;
import com.objectfrontier.rtgs.dto.IFSCMasterDTO;
import com.objectfrontier.rtgs.dto.RTGSBalTxnDTO;
import com.objectfrontier.rtgs.dto.RTGSBalTxnValueObject;
import com.objectfrontier.rtgs.report.dto.AmountDTO;
import com.objectfrontier.rtgs.report.dto.BranchDTO;
import com.objectfrontier.rtgs.report.dto.BranchwiseDTO;
import com.objectfrontier.rtgs.report.dto.DetailedMsgDTO;
import com.objectfrontier.rtgs.report.dto.DetailedReportDTO;
import com.objectfrontier.rtgs.report.dto.MsgDTO;
import com.objectfrontier.rtgs.report.dto.MsgFieldDTO;
import com.objectfrontier.rtgs.report.dto.MsgTypeDTO;
import com.objectfrontier.rtgs.report.dto.MsgTypewiseDTO;
import com.objectfrontier.rtgs.report.dto.ReportDTO;
import com.objectfrontier.user.dto.UserDTO;


public class ReportBO extends InstaReportBO{
    
    static Logger logger = Logger.getLogger(ReportBO.class); 

    public List ifscList = new ArrayList();
    public Map dateWiseReportMap = new TreeMap();
    public Map branchIfscMap = new TreeMap();
    boolean ifscCheck = false;
    public String xls = "";
    public List branchDto = new ArrayList();
    MsgDTO messageDTO = null;
    /**
     * @see com.objectfrontier.arch.factory.FactoryObject#initializeObject(java.lang.Object)
     */
    
    protected Date getBusinessDate() {
        
        return getBusinessDate();
    }

    /**
     * @see com.iob.rtgs.server.dao.ReportDAO#getControllerViews()
     */
    public List getControllerViews() {
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        List rtgsTxns = new ArrayList(2);
        String query = "";
        
        try {

             query = "SELECT   rbt.ID AS txn_id, rbt.utr_no AS utr_no, rbt.txn_date AS ent_date, " +
                    "rbt.msg_type AS msg_type, rbt.tran_type AS tran_type, " +
                    "rbt.txn_amt AS txn_amt, rbt.balance AS balance, " +
                    "rbt.sender_address AS our_branch, " +
                    "rbt.receiver_address AS other_branch, rbt.dbt_crd AS dr_cr " +
                    "FROM RTGSBALTRAN rbt";

            if (getBusinessDate() != null){

                query = query + " where TRUNC(RBT.txn_date) = '"  + 
                    ConversionUtil.convertToString(getBusinessDate()) + "'";
            }
            query = query + " ORDER BY RBT.id DESC";

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            
            while (rs.next()) { 
                rtgsTxns.add(parseControllerView(rs));
            }
           
        } catch (Throwable t) {
            logger.error(" Unable to get Controller Views " + t.getMessage()+ "Query :"+ query);
            throw new RuntimeException(t);
        } 
        finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (Exception ignore) {
            }
        }
        return rtgsTxns;
    }

    /**
     * Method to parse the given lots resultset based on a predefined column
     * indices
     * 
     * @param resultset
     * @return object of LotsDTO
     * @throws DTOParserException
     */
    public ClientInfo parseControllerView(Object resultset) 
    throws DTOParserException {
        
        ResultSet rs = (ResultSet)resultset;
        RTGSBalTxnDTO rtgsBalTxnDTO = new RTGSBalTxnDTO();
        rtgsBalTxnDTO.rtgsBalTxnVO = new RTGSBalTxnValueObject();
        
        try {
            if (rs.next()) {
                rtgsBalTxnDTO.rtgsBalTxnVO.txnId = rs.getInt(ReportConstants.TXN_ID);
                rtgsBalTxnDTO.rtgsBalTxnVO.entDate = new Date(rs.getTimestamp(ReportConstants.ENT_DATE).getTime());
                rtgsBalTxnDTO.rtgsBalTxnVO.utrNo = trimWhileSpaces(rs.getString(ReportConstants.UTR_NUMBER));
                rtgsBalTxnDTO.rtgsBalTxnVO.msgType = rs.getString(ReportConstants.MSG_TYPE);
                rtgsBalTxnDTO.rtgsBalTxnVO.tranType = rs.getString(ReportConstants.TRAN_TYPE);
                rtgsBalTxnDTO.rtgsBalTxnVO.ourBranch = rs.getString(ReportConstants.OUR_BRANCH);
                rtgsBalTxnDTO.rtgsBalTxnVO.otherBank = rs.getString(ReportConstants.OTHER_BANK);
                rtgsBalTxnDTO.rtgsBalTxnVO.otherBranch = rs.getString(ReportConstants.OTHER_BRANCH);
//                rtgsBalTxnDTO.rtgsBalTxnVO.txnAmount = rs.getDouble(ReportConstants.TXN_AMT);
                rtgsBalTxnDTO.rtgsBalTxnVO.txnAmount = rs.getBigDecimal(ReportConstants.TXN_AMT);
                rtgsBalTxnDTO.rtgsBalTxnVO.balance = rs.getDouble(ReportConstants.BALANCE);
                rtgsBalTxnDTO.rtgsBalTxnVO.debitCredit = rs.getString(ReportConstants.DR_CR).toCharArray()[0];
            }
            
        } catch (SQLException se) {
            logger.error("Unable to parse the resultset " + se.getStackTrace());
            throw new RuntimeException("Unable to parse the resultset " + se.getStackTrace());
        }
        return rtgsBalTxnDTO;
    }    
    
    protected String trimWhileSpaces(String str) {
         if (str == null) return str;
         return str.trim();
    }

    /**
     * 
     * @see com.objectfrontier.rtgs.report.server.dao.ReportDAO#getReport(com.iob.rtgs.report.dto.ReportDTO)
     */
    public ReportDTO getDatewiseReport(ReportDTO reportDTO,Timestamp fromDate){
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        ReportDTO repDTO = null;
        StringBuilder query = new StringBuilder();
        
        try {
            query.append("SELECT    m.msg_id message_id, rm.utr_no utr_number, rm.tran_type txn_type,");
            query.append("    rm.value_date ent_date, rm.status txn_status, md.TYPE TYPE,");
            query.append("    md.sub_type sub_type, md.NAME type_name, rm.amount amount,");
            query.append("    rm.receiver_address receiver_address,");
            query.append("    rm.sender_address sender_address, i.NAME branch_name, i.ifsc ifsc,");
            query.append("    b.branch_code branch_code, rs.name statusName ");
            query.append("FROM RTGS_MESSAGE rm, MESSAGE m,MSGDEFN md,IFSCMASTER i,HOSTIFSCMASTER b, RTGS_STATUS rs ");
            
            if ((ReportConstants.REDIRECTED.equalsIgnoreCase(reportDTO.txnStatus))
                    || (ReportConstants.FOR_OTHER_BRANCHES.equalsIgnoreCase(reportDTO.txnStatus))) {
                query.append(", BANKMASTER ");
            }
            query.append(" WHERE m.msg_defn_id = md.ID "); 
            query.append("   AND rm.ifsc_master_id = b.ID ");
            query.append("   AND rs.id = rm.status ");
            query.append("   AND b.ID = i.ID  AND rm.MSG_ID = m.MSG_ID "); 
            query.append(" AND m.msg_sub_type not in ('R09', 'R90', 'R43', 'R44')");
            
            /**
             * RBC CMD 1.0
             * For UTR NO.Wise Report
             * UTR No. wise report should not go with Date
             */
                               
            if (reportDTO.txnStatus != null) {
                if ((ReportConstants.SUCCESSFUL.equalsIgnoreCase(reportDTO.txnStatus)) 
                        || (ReportConstants.SUCCESSFUL_TYPEWISE.equalsIgnoreCase(reportDTO.txnStatus)) ) {
                    
                    if (null != reportDTO.tranType ){
                        
                        if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(reportDTO.tranType))
                        {
                            query.append("AND rm.status = " + ReportConstants.INWARD_SUCCESSFUL_STATUS + " ");
                        }
                        else if( ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(reportDTO.tranType)){
                            
                            query.append("AND rm.status = " + ReportConstants.OUTWARD_SUCCESSFUL_STATUS + " ");
                        }
                        else{
                            query.append("AND rm.status in (" + ReportConstants.INWARD_SUCCESSFUL_STATUS + "," 
                                         + ReportConstants.OUTWARD_SUCCESSFUL_STATUS + ") ");
                        }
                    }
                } else if ((ReportConstants.UNSUCCESSFUL.equalsIgnoreCase(reportDTO.txnStatus))
                        || (ReportConstants.UNSUCCESSFUL_TYPEWISE.equalsIgnoreCase(reportDTO.txnStatus)) ) {
                    
                    if (null != reportDTO.tranType ){
                        
                        if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(reportDTO.tranType))
                        {
                            query.append("AND rm.status = " + ReportConstants.INWARD_UNSUCCESSFUL_STATUS + " ");
                        }
                        else if( ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(reportDTO.tranType)){
                            
                            query.append("AND rm.status = " + ReportConstants.OUTWARD_UNSUCCESSFUL_STATUS + " ");
                        }
                        else{
                            query.append("AND rm.status in (" + ReportConstants.INWARD_UNSUCCESSFUL_STATUS + "," 
                                         + ReportConstants.OUTWARD_UNSUCCESSFUL_STATUS + ") ");
                        }
                    }

                } else if (ReportConstants.RETURNED.equalsIgnoreCase(reportDTO.txnStatus)) {
                    
                    query.append("AND rm.status = '" + ReportConstants.INWARD_RETURNED_STATUS + "' ");
                    query.append("AND rm.tran_type = '" + ReportConstants.TXN_TYPE_INWARD + "' ");
                    
                }else if (ReportConstants.TIMED_OUT.equalsIgnoreCase(reportDTO.txnStatus)) {//RBC CMD 1.0

                    query.append("AND rm.status = '" + ReportConstants.OUTWARD_TIMED_OUT_STATUS + "' ");
                    
                } else if (ReportConstants.CANCELLED.equalsIgnoreCase(reportDTO.txnStatus)) {// RBC CMD 1.0

                    query.append("AND rm.status = '" + ReportConstants.OUTWARD_CANCELLED_STATUS+ "' ");
                }                  
            }

            if (reportDTO.branchCode != null 
                    && reportDTO.branchCode.trim().length() > 0) {
                
                if (!(ReportConstants.ALL_BRANCH.equalsIgnoreCase(reportDTO.branchCode))) {
                    query.append("   AND rm.ifsc_master_id = "); 
                    query.append("       (SELECT i.ID "); 
                    query.append("        FROM HOSTIFSCMASTER i, IFSCMASTER im "); 
                    query.append("        WHERE i.ID = im.ID AND i.branch_code = '"+ reportDTO.branchCode +"') ");                
                }
            }  

          // RBC
          /*
           *  Add  the condition  to the query when the fromAmount greater than or 
           *  equal to Zero and toAmount is greater than zero. 
           */
        
//            if (reportDTO.getFromAmount() >= 0 && reportDTO.getToAmount() > 0 ) {            
            if (reportDTO.getFromAmount().compareTo(BigDecimal.ZERO) >= 0 && reportDTO.getToAmount().compareTo(BigDecimal.ZERO) > 0 ) {
                query.append(" AND amount between ");
                query.append(reportDTO.getFromAmount());
                query.append(" AND ");
                query.append(reportDTO.getToAmount());
            }
            
            if ( reportDTO.getUserId() != null && reportDTO.getUserId().length() != 0) {
                query.append(" AND ");
                query.append(" rm.entry_by = '");
                //query.append(reportDTO.getUserId().substring(11, reportDTO.getUserId().length()));
                query.append(reportDTO.getUserId());
                query.append("'");
            }
            if (reportDTO.getTranType() != null) {

                if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(reportDTO.getTranType())) {
                    query.append(" AND ");
                    query.append(" rm.tran_type = '" + reportDTO.getTranType() + "'");
                }
                if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(reportDTO.getTranType())) {
                    query.append(" AND ");
                    query.append(" rm.tran_type = '" + reportDTO.getTranType() + "'");
                }
             }
            
            if (reportDTO.getMsgSubType() != null) { //RBC CMD 1.0
                if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(reportDTO.getMsgSubType()) || ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(reportDTO.getMsgSubType())) {
                    query.append(" AND ");
                    query.append(" md.type = '" + ReportConstants.MSGTYPE + "'");
                    query.append(" AND ");
                    query.append(" md.sub_type = '" + reportDTO.getMsgSubType() + "'");
                }
            }
            
            //For UTR No. Wiser Report
            if (reportDTO.getUtrNumber() != null) {//RBC CMD 1.0
                
                    query.append(" AND ");
                    query.append(" rm.utr_no LIKE '%" + reportDTO.getUtrNumber() + "%'");                    
            }  
            
            if (reportDTO.getUtrNumber() == null) {//RBC CMD 1.0
                query.append("   AND rm.business_date BETWEEN ? AND ? ");
            }
            query.append(" order by rm.utr_no desc");
                
            ps = con.prepareStatement(query.toString());
            if (reportDTO.fromDate != null && reportDTO.toDate != null) {
                
                //Date has to be taken for all report other than UTRNo.wise report    
                if (reportDTO.getUtrNumber() == null) {//RBC CMD 1.0
                                           
                        ps.setTimestamp(1, fromDate);
                        ps.setTimestamp(2, fromDate);
                }
            }
            
            rs = ps.executeQuery();
            
            repDTO = new ReportDTO();
            if (ReportConstants.UNSUCCESSFUL_TYPEWISE.equalsIgnoreCase(reportDTO.txnStatus) 
                    ||  ReportConstants.SUCCESSFUL_TYPEWISE.equalsIgnoreCase(reportDTO.txnStatus)) {

                repDTO = prepareTypewiseReportDTO(repDTO, rs);
            } else {

                repDTO = prepareBranchwiseReportDTO(repDTO, rs);
            }
        } catch (Throwable t) {
            
            logger.error("unable to generate Date wise report" + t.getMessage());
            logger.error("Query : " + query.toString());
            throw new RuntimeException(t);
        } finally {
            
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (Exception ignore) {
            }
        }
        return repDTO; 
    }
    
    protected ReportDTO prepareTypewiseReportDTO(ReportDTO reportDTO,  ResultSet rs){
        
        try {
            List types = new ArrayList(0);
            Map typesMap = new HashMap();
            
            while (rs.next()) { 

//                double amount = rs.getDouble(ReportConstants.AMOUNT);
                BigDecimal amount = rs.getBigDecimal(ReportConstants.AMOUNT);
                String receiverAddress = rs.getString(ReportConstants.RECEIVER_ADDRESS); //TODO TEMP CHANGE
                String senderAddress = rs.getString(ReportConstants.SENDER_ADDRESS); //TODO TEMP CHANGE
                String txnType  = rs.getString(ReportConstants.TXN_TYPE);
                String branchCode = rs.getString(ReportConstants.BRANCH_CODE);
                String branchName = rs.getString(ReportConstants.BRANCH_NAME);
                String ifsc = rs.getString(ReportConstants.IFSC);
                String type = rs.getString(ReportConstants.TYPE);
                String subType = rs.getString(ReportConstants.SUB_TYPE);
                String typeName = rs.getString(ReportConstants.TYPE_NAME);
                branchCode = branchCode +"-"+ifsc; //included for DateWise Sorting in Reports, to Display IFSC Code//included for DateWise Sorting in Reports, to Display IFSC Code

                if (reportDTO.branchCode != null) { 
                    reportDTO.branchCode = branchCode;
                    reportDTO.branchName = branchName;
                }

                MsgDTO msgDTO = prepareMsgDTO(rs, amount, senderAddress, receiverAddress,subType);
                if (typesMap.containsKey(subType)) {
                    
                    MsgTypewiseDTO typewiseDTO = (MsgTypewiseDTO) typesMap.get(subType);
                    typewiseDTO = prepareBranchMapForType(typewiseDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
                    typesMap.put(subType, typewiseDTO);
                } else {
                    
                    MsgTypewiseDTO typewiseDTO = new MsgTypewiseDTO();
                    typewiseDTO = prepareBranchMapForType(typewiseDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
                    typesMap.put(subType, typewiseDTO);                
                }
            }
            
            Set typeKeys = typesMap.keySet();
            Iterator t = typeKeys.iterator();
            
            // Set the Grand Total (in/out) value of all the Branches is 0.0;
//            reportDTO.getBranchesGrandTotalDTO().setInAmount(0.0);
//            reportDTO.getBranchesGrandTotalDTO().setOutAmount(0.0);
            reportDTO.getBranchesGrandTotalDTO().setInAmount(BigDecimal.ZERO);
            reportDTO.getBranchesGrandTotalDTO().setOutAmount(BigDecimal.ZERO);
            
            while (t.hasNext()) {
                String subType = (String) t.next();
                MsgTypewiseDTO typewiseDTO = (MsgTypewiseDTO) typesMap.get(subType);
                
                Set branchesKeys = typewiseDTO.getBranchesMap().keySet();
                Iterator b = branchesKeys.iterator();
                
                while (b.hasNext()) {
                    String code = (String) b.next();
                    typewiseDTO.getBranches().add(typewiseDTO.getBranchesMap().get(code));   
                }
                types.add(typewiseDTO);
                
                // Add all the Branch Grand Total value into Total branch grand total value.
                // This is applicable for only Controller Reports.
                reportDTO.getBranchesGrandTotalDTO().inAmount = reportDTO.getBranchesGrandTotalDTO().inAmount.add(typewiseDTO.grandTotalDTO.inAmount);
                reportDTO.getBranchesGrandTotalDTO().outAmount = reportDTO.getBranchesGrandTotalDTO().outAmount.add(typewiseDTO.grandTotalDTO.outAmount);
            }
            reportDTO.setTypewiseDTOs(types);
            
        } catch (Throwable t) {
            logger.error("unable to prepare Typewise ReportDTO " + t.getMessage());
            throw new RuntimeException("unable to prepare Typewise ReportDTO " + t.getMessage(), t);
        } 
        
        return reportDTO; 
        
    }
    
    public Map getConsolidatedReport(String ifsc,ReportDTO reportDTO){
                
        
        Timestamp fromDate = reportDTO.fromDate;
        Timestamp toDate = reportDTO.toDate;
        dateWiseReportMap = new TreeMap();
        try {
            while(fromDate.compareTo(toDate) <= 0) {
                
                Date date = new Date(fromDate.getTime());
                dateWiseReportMap.put(date,generateConsolidatedReport(reportDTO,fromDate,ifsc));
                fromDate = nextdate(fromDate);
            } 
        } catch (Throwable t) {
            logger.error("unable to get Consolidated Report " + t.getMessage());
            throw new RuntimeException("unable to get Consolidated Report " + t.getMessage(), t);
        }
           
        return dateWiseReportMap;
   }
    
    public ReportDTO generateConsolidatedReport(ReportDTO repDTO,Timestamp fromDate,String ifsc){
           
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement outCprPs =null;
        PreparedStatement outIprPs =null;
        PreparedStatement inCprPs =null;
        PreparedStatement inIprPs =null;
        ResultSet outCprRs = null;
        ResultSet outIprRs = null;
        ResultSet inCprRs = null;
        ResultSet inIprRs = null;
        
        long id = 0;
        ReportDTO reportDTO = new ReportDTO();
        StringBuffer query = new StringBuffer();
        StringBuffer queryOutCpr = new StringBuffer();
        StringBuffer queryOutIpr = new StringBuffer();
        StringBuffer queryInCpr = new StringBuffer();
        StringBuffer queryInIpr = new StringBuffer();
        
        try {
            
            query.append(" select id from IFSCMASTER WHERE ifsc =? ");
            ps = con.prepareStatement(query.toString());
            ps.setString(1,ifsc);
            rs = ps.executeQuery();
            
            while(rs.next()) {
              id = rs.getInt(1);   
            }
            queryOutCpr.append(" SELECT amount FROM MESSAGE m, RTGS_MESSAGE rm WHERE rm.ifsc_master_id = ? AND rm.business_date = ? AND rm.tran_type = 'outward' AND m.msg_defn_id = 14 AND m.msg_id = rm.msg_id ");
            outCprPs = con.prepareStatement(queryOutCpr.toString());
            outCprPs.setLong(1, id);
            outCprPs.setTimestamp(2, fromDate);
            outCprRs = outCprPs.executeQuery();
            
            while(outCprRs.next()) {
                
//                double amount = outCprRs.getDouble(1);
//                reportDTO.dateCprOutAmt += amount;
//                repDTO.amount += amount;
                BigDecimal amount = outCprRs.getBigDecimal(1);
                reportDTO.dateCprOutAmt = reportDTO.dateCprOutAmt.add(amount);
                repDTO.amount = repDTO.amount.add(amount);
            }
            queryOutIpr.append("    Select amount from message m, rtgs_message rm where rm.ifsc_master_id = ?  AND rm.business_date = ? AND rm.tran_type= 'outward' AND m.msg_defn_id = 13 and m.msg_id = rm.msg_id ");
            outIprPs = con.prepareStatement(queryOutIpr.toString());
            outIprPs.setLong(1, id);
            outIprPs.setTimestamp(2, fromDate);
            outIprRs = outIprPs.executeQuery();
            
            while(outIprRs.next()) {
                
//                double amount1 = outIprRs.getDouble(1);
//                reportDTO.dateIprOutAmt += amount1;
//                repDTO.amount += amount1;
                BigDecimal amount1 = outIprRs.getBigDecimal(1);
                reportDTO.dateIprOutAmt = reportDTO.dateIprOutAmt.add(amount1);
                repDTO.amount = repDTO.amount.add(amount1);
            }
            
            queryInCpr.append(" Select amount from message m, rtgs_message rm where rm.ifsc_master_id = ?  AND rm.business_date = ? AND rm.tran_type= 'inward' AND m.msg_defn_id = 14  and m.msg_id = rm.msg_id ");
            inCprPs = con.prepareStatement(queryInCpr.toString());
            inCprPs.setLong(1, id);
            inCprPs.setTimestamp(2, fromDate);
            inCprRs = inCprPs.executeQuery();
            
            while(inCprRs.next()) {
    
//                double amount2 = inCprRs.getDouble(1);
//                reportDTO.dateCprInAmt += amount2;
//                repDTO.amount += amount2;
                BigDecimal amount2 = inCprRs.getBigDecimal(1);
                reportDTO.dateCprInAmt = reportDTO.dateCprInAmt.add(amount2);
                repDTO.amount = repDTO.amount.add(amount2);
            }
            
            queryInIpr.append(" Select amount from message  m, rtgs_message rm where rm.ifsc_master_id = ?  AND rm.business_date = ? AND rm.tran_type= 'inward' AND m.msg_defn_id = 13  and m.msg_id = rm.msg_id ");
            inIprPs = con.prepareStatement(queryInIpr.toString());
            inIprPs.setLong(1, id);
            inIprPs.setTimestamp(2, fromDate);
            inIprRs = inIprPs.executeQuery();
            
            while(inIprRs.next()) {

//                double amount3 = inIprRs.getDouble(1);
//                reportDTO.dateIprInAmt += amount3;
//                repDTO.amount += amount3;
                BigDecimal amount3 = inIprRs.getBigDecimal(1);
                reportDTO.dateIprInAmt = reportDTO.dateIprInAmt.add(amount3);
                repDTO.amount = repDTO.amount.add(amount3);
            }
            reportDTO.ifsc = ifsc;
            reportDTO.valueDate = fromDate;
            reportDTO.dateGrantTolal = reportDTO.dateCprOutAmt.add(reportDTO.dateIprOutAmt).add(reportDTO.dateCprInAmt)
                                       .add(reportDTO.dateIprInAmt); 
        } catch (Throwable t) {
            
            logger.error("Query : " + query);
            logger.error("queryOutCpr : " + queryOutCpr);
            logger.error("queryOutIpr : " + queryOutIpr);
            logger.error("queryInCpr : " + queryInCpr);
            logger.error("queryInIpr : " + queryInIpr);
            logger.error("unable to generate Consolidated Report " + t.getMessage());
            throw new RuntimeException("unable to generate Consolidated Report " + t.getMessage(), t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (outCprRs != null) outCprRs.close();
                if (outCprPs != null) outCprPs.close();
                if (outIprRs != null) outIprRs.close();
                if (outIprPs != null) outIprPs.close();
                if (inCprRs != null) inCprRs.close();
                if (inCprPs != null) inCprPs.close();
                if (inIprRs != null) inIprRs.close();
                if (inIprPs != null) inIprPs.close();
            } catch (Exception ignore) {
          }
        }
           
        return reportDTO;
    }
    
    protected ReportDTO prepareConsolidatedReportDTO(ReportDTO reportDTO,  ResultSet rs){
        
        try { 
            while (rs.next()) {
                String ifsc = rs.getString(ReportConstants.IFSC);
                if (reportDTO.reportType.equals("Consolidated")) {
                    ifscCheck = ifscList.contains(ifsc);
                    if (!ifscCheck) {
                        ifscList.add(ifsc);
                        reportDTO.dateWiseMap = getConsolidatedReport(ifsc,reportDTO);
                        branchIfscMap.put(ifsc, reportDTO.getDateWiseMap());
                        reportDTO.ifscMap = branchIfscMap;
                    }
                }
                reportDTO.setIfscList(ifscList);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return reportDTO; 
    }     
        
    protected ReportDTO prepareBranchwiseReportDTO(ReportDTO reportDTO,  ResultSet rs) {
        
        try {
            List branches = new ArrayList(0);
            Map branchesMap = new HashMap();
            while (rs.next()) {

//                double amount = rs.getDouble(ReportConstants.AMOUNT);
                BigDecimal amount = rs.getBigDecimal(ReportConstants.AMOUNT);
                String receiverAddress = rs.getString(ReportConstants.RECEIVER_ADDRESS); //TODO TEMP CHANGE
                String senderAddress = rs.getString(ReportConstants.SENDER_ADDRESS); //TODO TEMP CHANGE
                //String utrNo = rs.getString(ReportConstants.UTR_NO);
                String txnType  = rs.getString(ReportConstants.TXN_TYPE);
                String branchCode = rs.getString(ReportConstants.BRANCH_CODE);
                String branchName = rs.getString(ReportConstants.BRANCH_NAME);
                String ifsc = rs.getString(ReportConstants.IFSC); //included for DateWise Sorting in Reports, to Display IFSC Code
                String type = rs.getString(ReportConstants.TYPE);
                String subType = rs.getString(ReportConstants.SUB_TYPE);
                String typeName = rs.getString(ReportConstants.TYPE_NAME);
                branchCode = branchCode +"-"+ifsc; //included for DateWise Sorting in Reports, to Display IFSC Code
                
                if (reportDTO.branchCode != null) { 
                    reportDTO.branchCode = branchCode;
                    reportDTO.branchName = branchName;
                }
                
                MsgDTO msgDTO = prepareMsgDTO(rs, amount, senderAddress, receiverAddress,subType);
                
                if (branchesMap.containsKey(branchCode)) {
                    
                    BranchwiseDTO branchwiseDTO = (BranchwiseDTO) branchesMap.get(branchCode);
                    branchwiseDTO = prepareTypesMapForBranch(branchwiseDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
                    branchesMap.put(branchCode, branchwiseDTO);
                } else {
                    
                    BranchwiseDTO branchwiseDTO = new BranchwiseDTO();
                    branchwiseDTO = prepareTypesMapForBranch(branchwiseDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
                    branchesMap.put(branchCode, branchwiseDTO);
                }
            }
            
            Set branchesKeys = branchesMap.keySet();
            Iterator b = branchesKeys.iterator();
//            reportDTO.getBranchesGrandTotalDTO().setInAmount(0.0);
//            reportDTO.getBranchesGrandTotalDTO().setOutAmount(0.0);
//            reportDTO.getBranchesGrandTotalDTO().setIprOutAmount(0.0);
//            reportDTO.getBranchesGrandTotalDTO().setCprOutAmount(0.0);   
//            reportDTO.getBranchesGrandTotalDTO().setGrandTotal(0.0);     
            
            reportDTO.getBranchesGrandTotalDTO().setInAmount(BigDecimal.ZERO);
            reportDTO.getBranchesGrandTotalDTO().setOutAmount(BigDecimal.ZERO);
            reportDTO.getBranchesGrandTotalDTO().setIprOutAmount(BigDecimal.ZERO);
            reportDTO.getBranchesGrandTotalDTO().setCprOutAmount(BigDecimal.ZERO);   
            reportDTO.getBranchesGrandTotalDTO().setGrandTotal(BigDecimal.ZERO); 
            
            while (b.hasNext()) {
                String bCode = (String) b.next();
                BranchwiseDTO branchwiseDTO = (BranchwiseDTO) branchesMap.get(bCode);
                
                Set typeKeys = branchwiseDTO.getMsgTypesMap().keySet();
                Iterator t = typeKeys.iterator();

                while (t.hasNext()) {
                    String subType = (String) t.next();
                    branchwiseDTO.getMsgTypes().add(branchwiseDTO.getMsgTypesMap().get(subType));
                }   
                branches.add(branchwiseDTO);
                
                // Add Branches grandTotal of both In and Out.
                // This is only applicable for Controller Reports
                if(branchwiseDTO.grandTotalDTO != null)//AL
                    reportDTO.getBranchesGrandTotalDTO().inAmount = reportDTO.getBranchesGrandTotalDTO().inAmount.add(branchwiseDTO.grandTotalDTO.inAmount);
                if(branchwiseDTO.grandTotalDTO != null)//AL    
                reportDTO.getBranchesGrandTotalDTO().outAmount = reportDTO.getBranchesGrandTotalDTO().outAmount.add(branchwiseDTO.grandTotalDTO.outAmount);
                
                reportDTO.getBranchesGrandTotalDTO().netAmount = reportDTO.getBranchesGrandTotalDTO().netAmount.add(branchwiseDTO.grandTotalDTO.netAmount);
                
                reportDTO.getBranchesGrandTotalDTO().iprOutAmount = reportDTO.getBranchesGrandTotalDTO().iprOutAmount.add(branchwiseDTO.grandTotalDTO.iprOutAmount);
                reportDTO.getBranchesGrandTotalDTO().cprOutAmount = reportDTO.getBranchesGrandTotalDTO().cprOutAmount.add(branchwiseDTO.grandTotalDTO.cprOutAmount);
                
                reportDTO.getBranchesGrandTotalDTO().grandTotal = reportDTO.getBranchesGrandTotalDTO().grandTotal.add(branchwiseDTO.grandTotalDTO.iprOutAmount)
                                                                  .add(branchwiseDTO.grandTotalDTO.cprOutAmount);
                reportDTO.getBranchesGrandTotalDTO().grantTotalInward = reportDTO.getBranchesGrandTotalDTO().grantTotalInward.add(branchwiseDTO.grandTotalDTO.iprInAmount)
                                                                        .add(branchwiseDTO.grandTotalDTO.cprInAmount);
            }
            reportDTO.setBranchwiseDTOs(branches);
            
        } catch (Throwable t) {
            logger.error("unable to prepare Branchwise ReportDTO" + t.getMessage());
            throw new RuntimeException("unable to prepare Branchwise ReportDTO " + t.getMessage(), t);
        } 
        return reportDTO; 
    }
    
    protected ReportDTO prepareBranchwiseReportDTOController(ReportDTO reportDTO,  ResultSet rs,Map prop) {
        
         logger.info("ReportDTO : " + reportDTO + ", rs : " + rs + ", prop : " + prop);
         
         try {
                List branches = new ArrayList(0);
                Map branchesMap = new HashMap();
                
                while (rs.next()) { 
                
//                    double amount = rs.getDouble(ReportConstants.AMOUNT);
                    BigDecimal amount = rs.getBigDecimal(ReportConstants.AMOUNT);
                    String receiverAddress = rs.getString(ReportConstants.RECEIVER_ADDRESS); //TODO TEMP CHANGE
                    String senderAddress = rs.getString(ReportConstants.SENDER_ADDRESS); //TODO TEMP CHANGE
                    //String utrNo = rs.getString(ReportConstants.UTR_NO);
                    String txnType  = rs.getString(ReportConstants.TXN_TYPE);
                    String branchCode = rs.getString(ReportConstants.BRANCH_CODE);
                    String branchName = rs.getString(ReportConstants.BRANCH_NAME);
                    String ifsc = rs.getString(ReportConstants.IFSC); //included for DateWise Sorting in Reports, to Display IFSC Code
                    String type = rs.getString(ReportConstants.TYPE);
                    String subType = rs.getString(ReportConstants.SUB_TYPE);
                    String typeName = rs.getString(ReportConstants.TYPE_NAME);
                    branchCode = branchCode +"-"+ifsc; //included for DateWise Sorting in Reports, to Display IFSC Code
                
                    if (reportDTO.branchCode != null) { 
                        reportDTO.branchCode = branchCode;
                        reportDTO.branchName = branchName;
                    }
                    MsgDTO msgDTO = prepareMsgDTO(rs, amount, senderAddress, receiverAddress,subType);
                    if (branchesMap.containsKey(branchCode)) {
                    
                        BranchwiseDTO branchwiseDTO = (BranchwiseDTO) branchesMap.get(branchCode);
                        branchwiseDTO = prepareTypesMapForBranch(branchwiseDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
                        branchesMap.put(branchCode, branchwiseDTO);
                    } else {
                    
                        BranchwiseDTO branchwiseDTO = new BranchwiseDTO();
                        branchwiseDTO = prepareTypesMapForBranch(branchwiseDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
                        branchesMap.put(branchCode, branchwiseDTO);
                    }
                }
            
                Set branchesKeys = branchesMap.keySet();
                Iterator b = branchesKeys.iterator();
                //Initialize All Branch Grand total value of both In and Out Amout
//                reportDTO.getBranchesGrandTotalDTO().setInAmount(0.0);
//                reportDTO.getBranchesGrandTotalDTO().setOutAmount(0.0);
//                reportDTO.getBranchesGrandTotalDTO().setIprOutAmount(0.0);
//                reportDTO.getBranchesGrandTotalDTO().setCprOutAmount(0.0);   
//                reportDTO.getBranchesGrandTotalDTO().setGrandTotal(0.0);
                reportDTO.getBranchesGrandTotalDTO().setInAmount(BigDecimal.ZERO);
                reportDTO.getBranchesGrandTotalDTO().setOutAmount(BigDecimal.ZERO);
                reportDTO.getBranchesGrandTotalDTO().setIprOutAmount(BigDecimal.ZERO);
                reportDTO.getBranchesGrandTotalDTO().setCprOutAmount(BigDecimal.ZERO);   
                reportDTO.getBranchesGrandTotalDTO().setGrandTotal(BigDecimal.ZERO);   
                
                while (b.hasNext()) {
                    String bCode = (String) b.next();
                    BranchwiseDTO branchwiseDTO = (BranchwiseDTO) branchesMap.get(bCode);
                
                    Set typeKeys = branchwiseDTO.getMsgTypesMap().keySet();
                    Iterator t = typeKeys.iterator();

                    while (t.hasNext()) {
                        String subType = (String) t.next();
                        branchwiseDTO.getMsgTypes().add(branchwiseDTO.getMsgTypesMap().get(subType));
                    }   
                
                    branches.add(branchwiseDTO);
                
                    // Add Branches grandTotal of both In and Out.
                    // This is only applicable for Controller Reports
                    if(branchwiseDTO.grandTotalDTO != null)//AL
                        reportDTO.getBranchesGrandTotalDTO().inAmount = reportDTO.getBranchesGrandTotalDTO().inAmount.add(branchwiseDTO.grandTotalDTO.inAmount);
                    if(branchwiseDTO.grandTotalDTO != null)//AL    
                    reportDTO.getBranchesGrandTotalDTO().outAmount = reportDTO.getBranchesGrandTotalDTO().outAmount.add(branchwiseDTO.grandTotalDTO.outAmount);
                
                    reportDTO.getBranchesGrandTotalDTO().netAmount = reportDTO.getBranchesGrandTotalDTO().netAmount.add(branchwiseDTO.grandTotalDTO.netAmount);
                
                    reportDTO.getBranchesGrandTotalDTO().iprOutAmount = reportDTO.getBranchesGrandTotalDTO().iprOutAmount.add(branchwiseDTO.grandTotalDTO.iprOutAmount);
                    reportDTO.getBranchesGrandTotalDTO().cprOutAmount = reportDTO.getBranchesGrandTotalDTO().cprOutAmount.add(branchwiseDTO.grandTotalDTO.cprOutAmount);
                
                    reportDTO.getBranchesGrandTotalDTO().grandTotal = reportDTO.getBranchesGrandTotalDTO().grandTotal.add(branchwiseDTO.grandTotalDTO.iprOutAmount)
                                                                      .add(branchwiseDTO.grandTotalDTO.cprOutAmount);
                    reportDTO.getBranchesGrandTotalDTO().grantTotalInward = reportDTO.getBranchesGrandTotalDTO().grantTotalInward.add(branchwiseDTO.grandTotalDTO.iprInAmount)
                                                                            .add(branchwiseDTO.grandTotalDTO.cprInAmount);
                }
                reportDTO.setBranchwiseDTOs(branches);
            
            } catch (Throwable t) {
                logger.error("unable to prepare BranchwiseReportDTO Controller" + t.getMessage());
                throw new RuntimeException("unable to prepare BranchwiseReportDTO Controller " + t.getMessage(), t);
            } 
            return reportDTO; 
        }
            
    public Message getReport(Message message){

        try {
            ReportDTO reportDTO = (ReportDTO)message.info;
            Timestamp fromDate = reportDTO.fromDate;
            Timestamp toDate = reportDTO.toDate;
            Map datesMap = new TreeMap();

            while (fromDate.compareTo(toDate) <= 0) {

                Date date = new Date(fromDate.getTime());
                ReportDTO reportDTO2 = getDatewiseReport(reportDTO, fromDate);
                datesMap.put(date, reportDTO2);
                fromDate = nextdate(fromDate);
            }
            message.info = datesMap;

        } catch (Throwable t) {
            logger.error("Unable to prepare the report " + t.getMessage());
            throw new RuntimeException("Unable to prepare the report " + t.getMessage(), t);
        }
        return message;
}
    
    /**
     * Method to get nextdate in timestamp
     * 
     * @param Timestamp
     * @return Timestamp
     */    
    protected Timestamp nextdate(Timestamp today) {
        
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(today.getTime());
        
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        int monthLastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        if (day == monthLastDay) {
            
            day = 1;
            month++;
            
            if(month > 12) {
                month =1;
                year++;
            }
        } else {
            day++; 
        }
        c.set(year,month-1,day);
        Timestamp nextdate = new Timestamp(c.getTimeInMillis());
        return nextdate;
    }
    
   
        
//    protected MsgDTO prepareMsgDTO(ResultSet rs, double amount,
//                                   String senderAddress, String receiverAddress,String subType){
//        
//        com.objectfrontier.rtgs.report.dto.MsgDTO msgDTO = new MsgDTO();
//        AmountDTO amountDTO = new AmountDTO();
//        try {            
//            String msgId = rs.getString(ReportConstants.MSG_ID);
//            msgDTO.utrNo    = rs.getString(ReportConstants.UTR_NO);
//            msgDTO.entDate  = rs.getTimestamp(ReportConstants.ENT_DATE);
//            String txnType  = rs.getString(ReportConstants.TXN_TYPE);
//            
//            Timestamp date = rs.getTimestamp(ReportConstants.ENT_DATE);// Value date and Entry date are same
//            msgDTO.setValueDate(date);
//            
//            if (msgDTO.getValueDate() != null) {
//            
//                if (date.equals(msgDTO.getValueDate())) {
//                    
//                    if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//                        if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                            amountDTO.dateCprInAmt += amount;
//                        }
//                        if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                            amountDTO.dateIprInAmt += amount;
//                        }
//                    }
//                    if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
//                        
//                        if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                            amountDTO.dateCprOutAmt += amount;
//                        }
//                        if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                            amountDTO.dateIprOutAmt += amount;
//                        }
//                    }
//                }
//            }
//            String txnStat = rs.getString("statusName");
//            
//            if (txnStat == null) 
//                    txnStat = "";
//            
//            msgDTO.status  = txnStat;
//            
//            if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//                msgDTO.txnType  = ReportConstants.IN;    
//            } else {
//                msgDTO.txnType  = ReportConstants.OUT;
//            }
//            
//            //added for getting 5500 field value
//            if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) { 
//               
//              if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//        
//                  String orderCustomer = getOrderingCustomerValue(msgId);
//                  msgDTO.setOrderingCustomer(orderCustomer);
//             }
//         }
//                
//            //added for getting accNo For R42
//            if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) { 
//                
//                if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//              
//                    String accNo = getMessagDetails(msgId);
//                    msgDTO.setAccNo(accNo);
//                }
//                if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//              
//                    String accountNo = getAccNo(msgId);
//                    msgDTO.setAccNo(accountNo);
//                }
//            }
//            
//            if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//                amountDTO.inAmount = amount;
//                if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                    amountDTO.iprInAmount = amount;
//                }
//                if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                    amountDTO.cprInAmount = amount;
//                }
//            } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
//                amountDTO.outAmount = amount;
//                if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                    amountDTO.iprOutAmount = amount;
//                }
//                if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                    amountDTO.cprOutAmount = amount;
//                }
//            }
//            amountDTO.grantTotalInward += amountDTO.cprInAmount + amountDTO.iprInAmount;
//            amountDTO.grandTotal += amountDTO.cprOutAmount + amountDTO.iprOutAmount;
//            msgDTO.amountDTO = amountDTO;
//    
//            if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//                msgDTO.otherBank = senderAddress;
//            } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
//                msgDTO.otherBank = receiverAddress;
//            }
//            
//        } catch (Throwable t) {
//                
//            logger.error("Unable to prepare MsgDTO " + t.getMessage());
//            throw new RuntimeException("Unable to prepare MsgDTO " + t.getMessage(), t);
//        }       
//
//        return msgDTO;
//    }
    
    protected MsgDTO prepareMsgDTO(ResultSet rs, BigDecimal amount,
                                   String senderAddress, String receiverAddress,String subType){
        
        com.objectfrontier.rtgs.report.dto.MsgDTO msgDTO = new MsgDTO();
        AmountDTO amountDTO = new AmountDTO();
        try {            
            String msgId = rs.getString(ReportConstants.MSG_ID);
            msgDTO.utrNo    = rs.getString(ReportConstants.UTR_NO);
            msgDTO.entDate  = rs.getTimestamp(ReportConstants.ENT_DATE);
            String txnType  = rs.getString(ReportConstants.TXN_TYPE);
            
            Timestamp date = rs.getTimestamp(ReportConstants.ENT_DATE);// Value date and Entry date are same
            msgDTO.setValueDate(date);
            
            if (msgDTO.getValueDate() != null) {
            
                if (date.equals(msgDTO.getValueDate())) {
                    
                    if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
                        if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                            amountDTO.dateCprInAmt += amount;
                            amountDTO.dateCprInAmt = amountDTO.dateCprInAmt.add(amount);
                        }
                        if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                            amountDTO.dateIprInAmt += amount;
                            amountDTO.dateIprInAmt = amountDTO.dateIprInAmt.add(amount);
                        }
                    }
                    if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
                        
                        if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                            amountDTO.dateCprOutAmt += amount;
                            amountDTO.dateCprOutAmt = amountDTO.dateCprOutAmt.add(amount);
                        }
                        if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                            amountDTO.dateIprOutAmt += amount;
                            amountDTO.dateIprOutAmt = amountDTO.dateIprOutAmt.add(amount);
                        }
                    }
                }
            }
            String txnStat = rs.getString("statusName");
            
            if (txnStat == null) 
                    txnStat = "";
            
            msgDTO.status  = txnStat;
            
            if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
                msgDTO.txnType  = ReportConstants.IN;    
            } else {
                msgDTO.txnType  = ReportConstants.OUT;
            }
            
            //added for getting 5500 field value
            if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) { 
               
              if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
        
                  String orderCustomer = getOrderingCustomerValue(msgId);
                  msgDTO.setOrderingCustomer(orderCustomer);
             }
         }
                
            //added for getting accNo For R42
            if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) { 
                
                if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
              
                    String accNo = getMessagDetails(msgId);
                    msgDTO.setAccNo(accNo);
                }
                if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
              
                    String accountNo = getAccNo(msgId);
                    msgDTO.setAccNo(accountNo);
                }
            }
            
            if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
                amountDTO.inAmount = amount;
                if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
                    amountDTO.iprInAmount = amount;
                }
                if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                    amountDTO.cprInAmount = amount;
                }
            } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
                amountDTO.outAmount = amount;
                if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
                    amountDTO.iprOutAmount = amount;
                }
                if(ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                    amountDTO.cprOutAmount = amount;
                }
            }
//            amountDTO.grantTotalInward += amountDTO.cprInAmount + amountDTO.iprInAmount;
            amountDTO.grantTotalInward = amountDTO.cprInAmount.add(amountDTO.iprInAmount);
//            amountDTO.grandTotal += amountDTO.cprOutAmount + amountDTO.iprOutAmount;
            amountDTO.grandTotal = amountDTO.cprOutAmount.add(amountDTO.iprOutAmount);
            msgDTO.amountDTO = amountDTO;
    
            if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
                msgDTO.otherBank = senderAddress;
            } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
                msgDTO.otherBank = receiverAddress;
            }
            
        } catch (Throwable t) {
                
            logger.error("Unable to prepare MsgDTO " + t.getMessage());
            throw new RuntimeException("Unable to prepare MsgDTO " + t.getMessage(), t);
        }       

        return msgDTO;
    }
    
    public String getMessagDetails(String msgId){
        
        PreparedStatement ps =null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        int accNoCount = 0;
        String accNo = "";
        
        List accNoList = new ArrayList();
        try {    
        
            StringBuffer query = new StringBuffer();
            query.append(" SELECT count(cbs.acc_number) from cbsaccountdetails cbs where msg_id = ? ");
            
            ps = con.prepareStatement(query.toString());
            ps.setString(1, msgId);
            rs = ps.executeQuery();
            while(rs.next()) {
                accNoCount = rs.getInt(1);
                logger.info("accNoCount : " + accNoCount);
            }
            
            StringBuffer query1 = new StringBuffer();
            query1.append(" SELECT cbs.acc_number from cbsaccountdetails cbs where msg_id = ? ");
            ps1 = con.prepareStatement(query1.toString());
            ps1.setString(1, msgId);
            rs1 = ps1.executeQuery();
            while(rs1.next()) {
                
                accNo = rs1.getString(1);
                accNoList.add(accNo); 
            }
            accNo = "";
            Iterator itr = accNoList.iterator();
            while(itr.hasNext()) {
                
                accNo += (String)itr.next();
                accNo += ",";
            }
            if(accNo != null && accNo.length() > 0) {
                
                int index =  accNo.lastIndexOf(",");
                accNo = accNo.substring(0,index);
            }
        } catch (Throwable t) {
            
            logger.error("Unable to get Message Details " + t.getMessage());
            throw new RuntimeException("Unable to get Message Details " + t.getMessage());
        } finally {
            try {
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
                if (rs1!=null) rs1.close();
                if (ps1!=null) ps1.close();
                
            } catch (Exception ignore) {
                // TODO: handle exception
            }
        }
        return accNo;
        }
        
    public String getOrderingCustomerValue(String msgId) {
        
        PreparedStatement ps =null;
        ResultSet rs = null;
        String orderingCust = "";
        StringBuffer query = null;
        try {
            query = new StringBuffer();
            query.append(" SELECT value FROM MSGFIELD_STAGE WHERE msg_field_type_id=( ");  
            query.append(" SELECT id FROM MSGFIELDTYPE WHERE id IN (");
            query.append(" SELECT default_field_type_id FROM MSGFIELDDEFN WHERE field_block_id IN ( ");
            query.append(" SELECT id FROM MSGFIELDBLOCKDEFN WHERE block_id = (SELECT MAX(id) FROM MSGBLOCKDEFN WHERE msg_defn_id = ( ");
            query.append(" SELECT MIN(id) FROM MSGDEFN WHERE TYPE='298' AND sub_type='R41')))) AND no='5500')AND msg_id = ? ");
            ps = con.prepareStatement(query.toString());
            ps.setString(1, msgId);
            rs = ps.executeQuery();
            while(rs.next()) {
                orderingCust = rs.getString(1);
            }
       } catch (Throwable t) {
           logger.error("Query : " + query);
           logger.error("Unable to get Ordering Customer Value " + t.getMessage());
           throw new RuntimeException("Unable to get Ordering Customer Value " + t.getMessage());
        } finally {
            try {
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
            } catch (Exception ignore) {
                // TODO: handle exception
            }
        }
    
        return orderingCust;
    }
        
    public String getBeneficiaryName(String msgId) {
        
        PreparedStatement ps =null;
        ResultSet rs = null;
        String beneficiaryName = "";
        StringBuffer query = null;
        
        try {
            query = new StringBuffer();
            query.append(" SELECT value FROM MSGFIELD_STAGE WHERE msg_field_type_id = ( ");       
            query.append(" SELECT mft.ID FROM MSGFIELDFORMAT mff, MSGFIELDTYPE mft, ");
            query.append(" MSGCOMPOUNDFIELDDEFN mcfd WHERE mff.id  = mft.field_format_id AND mft.id  = mcfd.cmp_msg_field_type_id ");
            query.append(" AND mcfd.msg_field_type_id = (SELECT id FROM MSGFIELDTYPE WHERE id IN ( ");
            query.append(" SELECT default_field_type_id FROM MSGFIELDDEFN WHERE field_block_id IN ( ");
            query.append(" SELECT id FROM MSGFIELDBLOCKDEFN WHERE block_id = (SELECT MAX(id) FROM MSGBLOCKDEFN WHERE msg_defn_id = ( ");
            query.append(" SELECT MIN(id) FROM MSGDEFN WHERE TYPE='298' AND sub_type='R41')))) AND no='5561') AND MFT.NO='N5561') AND msg_id = ? ");
            ps = con.prepareStatement(query.toString());
            ps.setString(1, msgId);
            rs = ps.executeQuery();
            while(rs.next()) {
                beneficiaryName = rs.getString(1);
            }
            
        }catch (Throwable t) {
            logger.error("Query : " + query);
            logger.error("Unable to get Beneficiary Name " + t.getMessage());
            throw new RuntimeException("Unable to get Beneficiary Name " + t.getMessage());
         } finally {
            try {
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
            } catch (Exception ignore) {
                // TODO: handle exception
            }
        }
    
        return beneficiaryName;
        
    }
    
    public String getAccNo(String msgId) {
        
            PreparedStatement ps =null;
            ResultSet rs = null;
            String accNo = "";
            StringBuffer query = null;
            try {
                
                query = new StringBuffer();
                
                query.append(" SELECT value FROM MSGFIELD_STAGE WHERE msg_field_type_id = ( ");       
                query.append(" SELECT mft.ID FROM MSGFIELDFORMAT mff, MSGFIELDTYPE mft, ");
                query.append(" MSGCOMPOUNDFIELDDEFN mcfd WHERE mff.id  = mft.field_format_id AND mft.id  = mcfd.cmp_msg_field_type_id ");
                query.append(" AND mcfd.msg_field_type_id = (SELECT id FROM MSGFIELDTYPE WHERE id IN (");
                query.append(" SELECT default_field_type_id FROM MSGFIELDDEFN WHERE field_block_id IN ( ");
                query.append(" SELECT id FROM MSGFIELDBLOCKDEFN WHERE block_id = (SELECT MAX(id) FROM MSGBLOCKDEFN WHERE msg_defn_id = ( ");
                query.append(" SELECT MIN(id) FROM MSGDEFN WHERE TYPE='298' AND sub_type='R41')))) AND no='5561') AND MFT.NO='A5561') AND msg_id = ? ");
                ps = con.prepareStatement(query.toString());
                ps.setString(1, msgId);
                rs = ps.executeQuery();
                while(rs.next()) {
                    accNo = rs.getString(1);
                }
            
            }catch (Throwable t) {
                logger.error("Query : " + query);
                logger.error("Unable to get Account Number " + t.getMessage());
                throw new RuntimeException("Unable to get Account Number " + t.getMessage());
             } finally {
                try {
                    if (rs!=null) rs.close();
                    if (ps!=null) ps.close();
                } catch (Exception ignore) {
                    // TODO: handle exception
                }
            }
            return accNo;
        
        }
        
    public void getSenderAndReceiverIfsc(String msgId,ReportDTO dto){
        
        PreparedStatement ps =null;
        ResultSet rs = null;
        String sendAdd = null;
        String recAdd = null;
        StringBuffer query = null;
        try {
            
            query = new StringBuffer();
            query.append("select sender_address,receiver_address from RTGS_MESSAGE where msg_id = ? ");
            ps = con.prepareStatement(query.toString());
            ps.setString(1, msgId);
            rs = ps.executeQuery();
            while(rs.next()) {
                
                sendAdd = rs.getString(1);
                recAdd = rs.getString(2);
            }
            dto.setBeneficiaryName(sendAdd);
            dto.setRemitterName(recAdd);
        } catch (Throwable t) {
            logger.error("Query : " + query);
            logger.error("Unable to get Sender And Receiver Ifsc " + t.getMessage());
            throw new RuntimeException("Unable to get Sender And Receiver Ifsc " + t.getMessage());
         } finally {
            try {
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
            } catch (Exception ignore) {
                // TODO: handle exception
            }
        }
    }
        
//    protected MsgTypewiseDTO prepareBranchMapForType(MsgTypewiseDTO typewiseDTO, MsgDTO msgDTO, 
//            String type, String subType, 
//            String typeName, String branchCode, String branchName,   
//            String txnType, double amount) {
//                
//         
//        
//        Map branchesMap = typewiseDTO.getBranchesMap();
//        if (branchesMap.containsKey(branchCode)) {
//        
//            BranchDTO branchDTO = (BranchDTO) branchesMap.get(branchCode);
//            branchDTO = prepareBranchDTO(branchDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
//            branchesMap.put(branchCode, branchDTO);
//        } else {
//        
//            BranchDTO branchDTO = new BranchDTO();
//            branchDTO = prepareBranchDTO(branchDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
//            branchesMap.put(branchCode, branchDTO);
//        }
//        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//            typewiseDTO.getGrandTotalDTO().inAmount  += amount; 
//        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
//            typewiseDTO.getGrandTotalDTO().outAmount  += amount;
//        }
//        typewiseDTO.setMsgType(type);
//        typewiseDTO.setMsgSubType(subType);
//        typewiseDTO.setMsgTypeName(typeName);
//        
//        return typewiseDTO;
//    }
    protected MsgTypewiseDTO prepareBranchMapForType(MsgTypewiseDTO typewiseDTO, MsgDTO msgDTO, 
                                                     String type, String subType, 
                                                     String typeName, String branchCode, String branchName,   
                                                     String txnType, BigDecimal amount) {
                                                 
         Map branchesMap = typewiseDTO.getBranchesMap();
         if (branchesMap.containsKey(branchCode)) {
         
             BranchDTO branchDTO = (BranchDTO) branchesMap.get(branchCode);
             branchDTO = prepareBranchDTO(branchDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
             branchesMap.put(branchCode, branchDTO);
         } else {
         
             BranchDTO branchDTO = new BranchDTO();
             branchDTO = prepareBranchDTO(branchDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
             branchesMap.put(branchCode, branchDTO);
         }
         if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
    //                                                     typewiseDTO.getGrandTotalDTO().inAmount  += amount; 
             typewiseDTO.getGrandTotalDTO().inAmount  = typewiseDTO.getGrandTotalDTO().inAmount.add(amount);
         } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
             typewiseDTO.getGrandTotalDTO().outAmount  = typewiseDTO.getGrandTotalDTO().outAmount.add(amount);
         }
         typewiseDTO.setMsgType(type);
         typewiseDTO.setMsgSubType(subType);
         typewiseDTO.setMsgTypeName(typeName);
         
         return typewiseDTO;
     }    
    
    
//    protected BranchwiseDTO prepareTypesMapForBranch(BranchwiseDTO branchwiseDTO, MsgDTO msgDTO, 
//            String type, String subType, 
//            String typeName, String branchCode, String branchName, 
//            String txnType, double amount) {
//
//          branchwiseDTO.setBranchCode(branchCode);
//          // included for DateWise Sorting, to Display IFSC Code
//          String ifsc = ""; 
//          if(branchCode.indexOf("-") != -1){
//              ifsc = branchCode.substring(branchCode.indexOf("-")+1);
//          }
//          
//          branchwiseDTO.setBranchName(branchName+" - "+ifsc); // included for Datewise Sorting, to Display IFSC Code
//
//        Map typesMap = branchwiseDTO.getMsgTypesMap();
//        if (typesMap.containsKey(subType)) {
//            
//            MsgTypeDTO typeDTO = (MsgTypeDTO) typesMap.get(subType);
//            typeDTO = prepareTypeDTO(typeDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
//            typesMap.put(subType, typeDTO);
//        } else {
//            
//            MsgTypeDTO typeDTO = new MsgTypeDTO();
//            typeDTO = prepareTypeDTO(typeDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
//            typesMap.put(subType, typeDTO);
//        }
//        branchwiseDTO.getMsgDTO().setValueDate(msgDTO.getValueDate());
////        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType) 
//            && 
//            !(msgDTO.getStatus().equalsIgnoreCase(ReportConstants.RETURNED) || 
//              subType.equalsIgnoreCase(ReportConstants.CN_SUBTYPE) ||
//              subType.equalsIgnoreCase(ReportConstants.DN_SUBTYPE) ) ) { //AL
//               
//              if (ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                  branchwiseDTO.getGrandTotalDTO().iprInAmount += amount; 
//              } else if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                  branchwiseDTO.getGrandTotalDTO().cprInAmount += amount; 
//              }     
//                branchwiseDTO.getGrandTotalDTO().inAmount  += amount; 
//            
//        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
//            
//            if (ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                branchwiseDTO.getGrandTotalDTO().iprOutAmount += amount; 
//            } else if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                 branchwiseDTO.getGrandTotalDTO().cprOutAmount += amount; 
//            }
//                    
//            branchwiseDTO.getGrandTotalDTO().outAmount  += amount;
//        }
//        branchwiseDTO.getGrandTotalDTO().grandTotal += branchwiseDTO.getGrandTotalDTO().cprOutAmount + branchwiseDTO.getGrandTotalDTO().iprOutAmount;
//        branchwiseDTO.getGrandTotalDTO().grantTotalInward += branchwiseDTO.getGrandTotalDTO().cprInAmount + branchwiseDTO.getGrandTotalDTO().iprInAmount;
//        branchwiseDTO.getGrandTotalDTO().netAmount += amount;
//        return branchwiseDTO;
//
//    }
    
    protected BranchwiseDTO prepareTypesMapForBranch(BranchwiseDTO branchwiseDTO, MsgDTO msgDTO, 
                                                     String type, String subType, 
                                                     String typeName, String branchCode, String branchName, 
                                                     String txnType, BigDecimal amount) {

        branchwiseDTO.setBranchCode(branchCode);
        // included for DateWise Sorting, to Display IFSC Code
        String ifsc = ""; 
        if(branchCode.indexOf("-") != -1){
            ifsc = branchCode.substring(branchCode.indexOf("-")+1);
        }
       
        branchwiseDTO.setBranchName(branchName+" - "+ifsc); // included for Datewise Sorting, to Display IFSC Code

        Map typesMap = branchwiseDTO.getMsgTypesMap();
        if (typesMap.containsKey(subType)) {
         
            MsgTypeDTO typeDTO = (MsgTypeDTO) typesMap.get(subType);
            typeDTO = prepareTypeDTO(typeDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
            typesMap.put(subType, typeDTO);
        } else {
         
            MsgTypeDTO typeDTO = new MsgTypeDTO();
            typeDTO = prepareTypeDTO(typeDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
            typesMap.put(subType, typeDTO);
        }
        branchwiseDTO.getMsgDTO().setValueDate(msgDTO.getValueDate());
//   if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType) 
            && 
            !(msgDTO.getStatus().equalsIgnoreCase(ReportConstants.RETURNED) || 
            subType.equalsIgnoreCase(ReportConstants.CN_SUBTYPE) ||
            subType.equalsIgnoreCase(ReportConstants.DN_SUBTYPE) ) ) { //AL
            
            if (ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
                branchwiseDTO.getGrandTotalDTO().iprInAmount = branchwiseDTO.getGrandTotalDTO().iprInAmount.add(amount); 
            } else if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                branchwiseDTO.getGrandTotalDTO().cprInAmount = branchwiseDTO.getGrandTotalDTO().cprInAmount.add(amount); 
            }     
            branchwiseDTO.getGrandTotalDTO().inAmount = branchwiseDTO.getGrandTotalDTO().inAmount.add(amount); 
         
        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
         
            if (ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
                branchwiseDTO.getGrandTotalDTO().iprOutAmount = branchwiseDTO.getGrandTotalDTO().iprOutAmount.add(amount); 
            } else if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                branchwiseDTO.getGrandTotalDTO().cprOutAmount = branchwiseDTO.getGrandTotalDTO().cprOutAmount.add(amount); 
            }
                 
            branchwiseDTO.getGrandTotalDTO().outAmount  = branchwiseDTO.getGrandTotalDTO().outAmount.add(amount);
        }
        branchwiseDTO.getGrandTotalDTO().grandTotal = branchwiseDTO.getGrandTotalDTO().grandTotal.add(branchwiseDTO.getGrandTotalDTO().cprOutAmount)
                                                      .add(branchwiseDTO.getGrandTotalDTO().iprOutAmount);
        branchwiseDTO.getGrandTotalDTO().grantTotalInward = branchwiseDTO.getGrandTotalDTO().grantTotalInward.add(branchwiseDTO.getGrandTotalDTO().cprInAmount)
                                                      .add(branchwiseDTO.getGrandTotalDTO().iprInAmount);
        branchwiseDTO.getGrandTotalDTO().netAmount = branchwiseDTO.getGrandTotalDTO().netAmount.add(amount);
        return branchwiseDTO;
    }
    
//    protected BranchDTO prepareBranchDTO(BranchDTO branchDTO, MsgDTO msgDTO, 
//            String type, String subType, 
//            String typeName, String branchCode, String branchName, 
//            String txnType, double amount) {
//                
//        logger.info("BrachDTO : " + branchDTO + ", MsgDTO : " + msgDTO + ", type : " + type + ", subType : " + subType + ", amount :" + amount);
//        logger.info("typeName : " + typeName + ", branchCode : " + branchCode + ", branchName : " + branchName + ", txnType : " + txnType);
//        branchDTO.getMessages().add(msgDTO);      
//        branchDTO.setMsgSubType(subType);
//        branchDTO.setBranchCode(branchCode);
//        branchDTO.setBranchName(branchName);
//        
//         // included for DateWise Sorting, to Display IFSC Code
//         String ifsc = ""; 
//         if(branchCode.indexOf("-") != -1){
//             ifsc = branchCode.substring(branchCode.indexOf("-")+1);
//         }
//  
//         branchDTO.setBranchName(branchName+" - "+ifsc); // included for Datewise Sorting, to Display IFSC Code
//               
//        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//            
//            branchDTO.getGrandTotalDTO().inAmount  += amount; 
//        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
//            
//            branchDTO.getGrandTotalDTO().outAmount  += amount;
//        }
//                
//        return branchDTO;
//    }
    
    protected BranchDTO prepareBranchDTO(BranchDTO branchDTO, MsgDTO msgDTO, 
                                         String type, String subType, 
                                         String typeName, String branchCode, String branchName, 
                                         String txnType, BigDecimal amount) {
                                             
        logger.info("BrachDTO : " + branchDTO + ", MsgDTO : " + msgDTO + ", type : " + type + ", subType : " + subType + ", amount :" + amount);
        logger.info("typeName : " + typeName + ", branchCode : " + branchCode + ", branchName : " + branchName + ", txnType : " + txnType);
        branchDTO.getMessages().add(msgDTO);      
        branchDTO.setMsgSubType(subType);
        branchDTO.setBranchCode(branchCode);
        branchDTO.setBranchName(branchName);
         
        // included for DateWise Sorting, to Display IFSC Code
        String ifsc = ""; 
        if(branchCode.indexOf("-") != -1){
         ifsc = branchCode.substring(branchCode.indexOf("-")+1);
        }
           
                 branchDTO.setBranchName(branchName+" - "+ifsc); // included for Datewise Sorting, to Display IFSC Code
                    
            if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
                 
//                branchDTO.getGrandTotalDTO().inAmount  += amount; 
                branchDTO.getGrandTotalDTO().inAmount  = branchDTO.getGrandTotalDTO().inAmount.add(amount);
            } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
                
//                branchDTO.getGrandTotalDTO().outAmount  += amount;
                branchDTO.getGrandTotalDTO().outAmount  = branchDTO.getGrandTotalDTO().outAmount.add(amount);
            }
                    
            return branchDTO;
        }
    
//    protected MsgTypeDTO prepareTypeDTO(MsgTypeDTO typeDTO, MsgDTO msgDTO, 
//            String type, String subType, 
//            String typeName, String branchCode, String branchName,  
//            String txnType, double amount) {
//                
//        logger.info("MsgTypeDTO : " + typeDTO + ", MsgDTO : " + msgDTO + ", type : " + type + ", subType : " + subType + ", amount :" + amount);
//        logger.info("typeName : " + typeName + ", branchCode : " + branchCode + ", branchName : " + branchName + ", txnType : " + txnType);
//        
//        typeDTO.getMessages().add(msgDTO);      
//        typeDTO.setMsgType(type);
//        typeDTO.setMsgSubType(subType);
//        typeDTO.setMsgTypeName(typeName);
//        typeDTO.setBranchCode(branchCode);
//        
//        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//            
//            typeDTO.getGrandTotalDTO().inAmount  += amount; 
//            if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//               typeDTO.getGrandTotalDTO().iprInAmount  += amount;
//           } else if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//               typeDTO.getGrandTotalDTO().cprInAmount  += amount;
//           }
//        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
//            
//            typeDTO.getGrandTotalDTO().outAmount  += amount;
//            if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                typeDTO.getGrandTotalDTO().iprOutAmount  += amount;
//            } else if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
//                typeDTO.getGrandTotalDTO().cprOutAmount  += amount;
//            }
//        }
//        typeDTO.getGrandTotalDTO().grandTotal += typeDTO.getGrandTotalDTO().cprOutAmount + typeDTO.getGrandTotalDTO().iprOutAmount; 
//        typeDTO.getGrandTotalDTO().grantTotalInward += typeDTO.getGrandTotalDTO().cprInAmount + typeDTO.getGrandTotalDTO().iprInAmount;      
//        return typeDTO;
//    }
    
    protected MsgTypeDTO prepareTypeDTO(MsgTypeDTO typeDTO, MsgDTO msgDTO, 
                                        String type, String subType, 
                                        String typeName, String branchCode, String branchName,  
                                        String txnType, BigDecimal amount) {
                                            
        logger.info("MsgTypeDTO : " + typeDTO + ", MsgDTO : " + msgDTO + ", type : " + type + ", subType : " + subType + ", amount :" + amount);
        logger.info("typeName : " + typeName + ", branchCode : " + branchCode + ", branchName : " + branchName + ", txnType : " + txnType);
        
        typeDTO.getMessages().add(msgDTO);      
        typeDTO.setMsgType(type);
        typeDTO.setMsgSubType(subType);
        typeDTO.setMsgTypeName(typeName);
        typeDTO.setBranchCode(branchCode);
        
        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
            
            typeDTO.getGrandTotalDTO().inAmount.add(amount); 
            if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
               typeDTO.getGrandTotalDTO().iprInAmount.add(amount);
           } else if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
               typeDTO.getGrandTotalDTO().cprInAmount.add(amount);
           }
        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
            
            typeDTO.getGrandTotalDTO().outAmount.add(amount);
            if(ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
                typeDTO.getGrandTotalDTO().iprOutAmount.add(amount);
            } else if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                typeDTO.getGrandTotalDTO().cprOutAmount.add(amount);
            }
        }
        typeDTO.getGrandTotalDTO().grandTotal = typeDTO.getGrandTotalDTO().grandTotal.add(typeDTO.getGrandTotalDTO().cprOutAmount)
                                                .add(typeDTO.getGrandTotalDTO().iprOutAmount); 
        typeDTO.getGrandTotalDTO().grantTotalInward = typeDTO.getGrandTotalDTO().grantTotalInward.add(typeDTO.getGrandTotalDTO().cprInAmount)
                                                        .add(typeDTO.getGrandTotalDTO().iprInAmount);      
        return typeDTO;
    }    

    
    /**
     * Get the Detailed Report from the Database view
     * 
     * @param reportDTO IOBDetailedReportDTO
     * @return reportDTO IOBDetailedReportDTO
     */
    public Message getDetailedReport(Message message)  {
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        DetailedReportDTO detailedReportDTO = (DetailedReportDTO)message.info;
        StringBuffer query = new StringBuffer();    
        try {

                       
            query.append("SELECT  rm.msg_id MESSAGE_ID, rm.utr_no, rm.pi_response_id PI_ID, rm.ssn_response_id SSN_ID, ");
            query.append("rm.value_date, rm.tran_type, m.MSG_DEFN_ID, rm.STATUS, rm.amount, " );
            query.append("rm.receiver_address, rm.sender_address, md.ID, md.TYPE, md.sub_type, md.NAME, iim.branch_code, rs.name statusName ");
            query.append("FROM MESSAGE m, RTGS_MESSAGE rm,  MSGDEFN md, HOSTIFSCMASTER iim ,IFSCMASTER im, RTGS_STATUS rs ");
            query.append(" WHERE md.ID = m.msg_defn_id AND rm.msg_id = m.msg_id AND rm.ifsc_master_id = iim.id ") ;
            query.append(" AND iim.id = im.id AND rs.id = rm.status AND m.msg_sub_type NOT IN ('R43', 'R44') ");
            
                        
        //  Date has to be taken for all report other than UTRNo.wise report    
            if (detailedReportDTO.getUtrNumber() == null) {//RBC CMD 1.0
                query.append(" and rm.business_date BETWEEN ? AND ? ");
            } 
            if (detailedReportDTO.txnStatus != null) {
                
                if ((ReportConstants.SUCCESSFUL.equalsIgnoreCase(detailedReportDTO.txnStatus)) 
                        || (ReportConstants.SUCCESSFUL_TYPEWISE.equalsIgnoreCase(detailedReportDTO.txnStatus)) ) {
                            
//                    query.append(" AND rm.STATUS = '" + ReportConstants.OUTWARD_SUCCESSFUL_STATUS + "' ");
                    
                    if (null != detailedReportDTO.tranType ){
                        
                        if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(detailedReportDTO.tranType))
                        {
                            query.append("AND rm.status = " + ReportConstants.INWARD_SUCCESSFUL_STATUS + " ");
                        }
                        else if( ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(detailedReportDTO.tranType)){
                            
                            query.append("AND rm.status = " + ReportConstants.OUTWARD_SUCCESSFUL_STATUS + " ");
                        }
                        else{
                            query.append("AND rm.status in (" + ReportConstants.INWARD_SUCCESSFUL_STATUS + "," 
                                         + ReportConstants.OUTWARD_SUCCESSFUL_STATUS + ") ");
                        }
                    }
                } else if ((ReportConstants.UNSUCCESSFUL.equalsIgnoreCase(detailedReportDTO.txnStatus))
                        || (ReportConstants.UNSUCCESSFUL_TYPEWISE.equalsIgnoreCase(detailedReportDTO.txnStatus)) ) {
                            
                    if (null != detailedReportDTO.tranType ){
                        
                        if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(detailedReportDTO.tranType))
                        {
                            query.append("AND rm.status = " + ReportConstants.INWARD_UNSUCCESSFUL_STATUS + " ");
                        }
                        else if( ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(detailedReportDTO.tranType)){
                            
                            query.append("AND rm.status = " + ReportConstants.OUTWARD_UNSUCCESSFUL_STATUS + " ");
                        }
                        else{
                            query.append("AND rm.status in (" + ReportConstants.INWARD_UNSUCCESSFUL_STATUS + "," 
                                         + ReportConstants.OUTWARD_UNSUCCESSFUL_STATUS + ") ");
                        }
                    }
                    
                } else if (ReportConstants.RETURNED.equalsIgnoreCase(detailedReportDTO.txnStatus)) {
                    
                    query.append(" AND rm.STATUS = '" + ReportConstants.INWARD_RETURNED_STATUS + "' ");
                    query.append("AND rm.TRAN_TYPE = '" + ReportConstants.TXN_TYPE_INWARD + "' ");

                }   else if (ReportConstants.TIMED_OUT.equalsIgnoreCase(detailedReportDTO.txnStatus)) {//RBC CMD 1.0
                    query.append(" and");
                    query.append(" rm.STATUS = '" + ReportConstants.OUTWARD_TIMED_OUT_STATUS + "' ");
                    
                }              
                //Default condition : ALL transactions
            }

            if (!(ReportConstants.ALL_BRANCH.equalsIgnoreCase(detailedReportDTO.branchCode))) {
                if (detailedReportDTO.branchCode != null && detailedReportDTO.branchCode.trim().length() > 0) {

                    query.append(" and");
                    query.append(" iim.BRANCH_CODE = '" +detailedReportDTO.branchCode+"' "); // IOBIFSCMaster Code = Branch Code
                }
                
            }
            // RBC

            if (detailedReportDTO.getFromAmount().compareTo(BigDecimal.ZERO) >= 0 && detailedReportDTO.getToAmount().compareTo(BigDecimal.ZERO) > 0 ) {
                query.append(" and");            
                query.append(" amount between ");
                query.append(detailedReportDTO.getFromAmount());
                query.append(" AND ");
                query.append(detailedReportDTO.getToAmount());
            }
            
            if ( detailedReportDTO.getUserId() != null && detailedReportDTO.getUserId().length() != 0) {
                query.append(" and");              
                query.append(" rm.entry_by = '");
                query.append(detailedReportDTO.getUserId());
                query.append("'");
            }

            if (detailedReportDTO.getTranType() != null) {

                if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(detailedReportDTO.getTranType())) {
                    query.append(" and");
                    query.append(" rm.tran_type = '" + detailedReportDTO.getTranType() + "'");
                }
                if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(detailedReportDTO.getTranType())) {
                    query.append(" and");
                    query.append(" rm.tran_type = '" + detailedReportDTO.getTranType() + "'");
                }
                
            }
            if (detailedReportDTO.getMsgSubType() != null) {//RBC CMD 1.0
                if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(detailedReportDTO.getMsgSubType()) || ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(detailedReportDTO.getMsgSubType())) {
                    query.append(" and");
                    query.append(" md.type = '" + ReportConstants.MSGTYPE + "'");
                    query.append(" AND ");
                    query.append(" md.sub_type = '" + detailedReportDTO.getMsgSubType() + "'");
                }
            }

            if (detailedReportDTO.getUtrNumber() != null) {//RBC CMD 1.0
    
                    query.append(" and");
                    query.append(" rm.utr_no = '" + detailedReportDTO.getUtrNumber() + "'"); 
            }  
            
            //Added By Umesh As requested by kanagarajan on 16-Dec-2008
            query.append(" ORDER BY rm.utr_no DESC ");
            
            ps = con.prepareStatement(query.toString());
            if (detailedReportDTO.fromDate != null && detailedReportDTO.toDate != null) {

                  if (detailedReportDTO.getUtrNumber() == null) {//RBC CMD 1.0
                               
                          ps.setTimestamp(1, detailedReportDTO.fromDate);
                          ps.setTimestamp(2, detailedReportDTO.toDate);
                  }   
            }
            rs = ps.executeQuery();
            String status;
            
            if (ReportConstants.UNSUCCESSFUL_TYPEWISE.equalsIgnoreCase(detailedReportDTO.txnStatus) || 
                ReportConstants.SUCCESSFUL_TYPEWISE.equalsIgnoreCase(detailedReportDTO.txnStatus)) {

                status = ReportConstants.TYPEWISE;
            } else {
                status = ReportConstants.BRANCHWISE;
            }
            
            List messages = new ArrayList();

            while (rs.next()) {
                
                List messagesIDs = new ArrayList();    
                messagesIDs.add(rs.getString(ReportConstants.MSG_ID));
                messagesIDs.add(rs.getString(ReportConstants.PI_ID));
                messagesIDs.add(rs.getString(ReportConstants.SSN_ID));
                messages.add(messagesIDs);
            }
            rs.close();
            ps.close();
            for (Iterator i = messages.iterator(); i.hasNext();){ 
                List message1 = (ArrayList) i.next();                  
                for (Iterator itr = message1.iterator(); itr.hasNext();) {
                    String msgID   = (String) itr.next();
                    String piID    = (String) itr.next();
                    String ssnID   = (String) itr.next();

                    DetailedMsgDTO detailedMsgDTO = new DetailedMsgDTO();

                    // Collect the Common Message Fields
                    detailedMsgDTO = getDetailedMessage(msgID); 

                   // Collect the PI Respone Message Fields (if any)
                    if (piID != null && piID.trim().length() > 0) { 

                        detailedMsgDTO.piMessage = getDetailedMessage(piID);
                    }
                   // Collect the SSN Response Message fields (if any)
                    if (ssnID != null && ssnID.trim().length() > 0) {

                        detailedMsgDTO.ssnMessage = getDetailedMessage(ssnID);
                    }
                   // Order the Detailed messages in Branchwise || Typewise
                    String typeKey = detailedMsgDTO.msgType +" / "+ detailedMsgDTO.msgSubType + " - " + detailedMsgDTO.typeName;
                    
                    if (status.equalsIgnoreCase(ReportConstants.BRANCHWISE)) {

                       prepareBranchwiseDetailedReport(detailedMsgDTO, detailedReportDTO, typeKey);
                    } else if (status.equalsIgnoreCase(ReportConstants.TYPEWISE)) {

                        prepareTypewiseDetailedReport(detailedMsgDTO, detailedReportDTO, typeKey);
                    }
                } 
            }

        } catch (Throwable t) {
            logger.error("Query : " + query);
            logger.error("Unable to get Detailed eport " + t.getMessage());
            throw new RuntimeException("Unable to get Detailed Report " + t.getMessage());
         }  finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        message.info = detailedReportDTO;
        return message;     
    }

    /**
     * Prepare the Branchwise Detailed Report
     * 
     * @param detailedMsgDTO DetailedMsgDTO , reportDTO IOBDetailedReportDTO, String typeKey
     */
    private void prepareBranchwiseDetailedReport(DetailedMsgDTO detailedMsgDTO, DetailedReportDTO detailedReportDTO, String typeKey) {
        
        Map detailedMessages = detailedReportDTO.getDetailedMessages();
        
        if (detailedMessages.containsKey(detailedMsgDTO.entDate)) {
                        
            Map branchWiseMsgsMap = (Map)detailedReportDTO.getDetailedMessages().get(detailedMsgDTO.entDate);
            if (branchWiseMsgsMap.containsKey(detailedMsgDTO.branchName)) {
                            
                Map typeWiseMsgsMap = (Map)branchWiseMsgsMap.get(detailedMsgDTO.branchName);
                if (typeWiseMsgsMap.containsKey(typeKey)) {
                                
                    ((List)typeWiseMsgsMap.get(typeKey)).add(detailedMsgDTO);
                } else {
                                
                    List msgs = new ArrayList();
                    msgs.add(detailedMsgDTO);
                    typeWiseMsgsMap.put(typeKey, msgs);
                }
                branchWiseMsgsMap.put(detailedMsgDTO.branchName, typeWiseMsgsMap);
            } else {
                            
                Map typeWiseMsgsMap = new HashMap();
                if (typeWiseMsgsMap.containsKey(typeKey)) {
                                
                    ((List)typeWiseMsgsMap.get(typeKey)).add(detailedMsgDTO);
                } else {
                                
                    List msgs = new ArrayList();
                    msgs.add(detailedMsgDTO);
                    typeWiseMsgsMap.put(typeKey, msgs);
                }
                branchWiseMsgsMap.put(detailedMsgDTO.branchName, typeWiseMsgsMap);
            }
            detailedReportDTO.getDetailedMessages().put(detailedMsgDTO.entDate, branchWiseMsgsMap);
        } else {
                        
            Map branchWiseMsgsMap = new HashMap();
            if (branchWiseMsgsMap.containsKey(detailedMsgDTO.branchName)) {
                            
                Map typeWiseMsgsMap = (Map)branchWiseMsgsMap.get(detailedMsgDTO.branchName);
                if (typeWiseMsgsMap.containsKey(typeKey)) {
                                
                    ((List)typeWiseMsgsMap.get(typeKey)).add(detailedMsgDTO);
                } else {
                                
                    List msgs = new ArrayList();
                    msgs.add(detailedMsgDTO);
                    typeWiseMsgsMap.put(typeKey, msgs);
                }
                branchWiseMsgsMap.put(detailedMsgDTO.branchName, typeWiseMsgsMap);
            } else {
                            
                Map typeWiseMsgsMap = new HashMap();
                if (typeWiseMsgsMap.containsKey(typeKey)) {
                                
                    ((List)typeWiseMsgsMap.get(typeKey)).add(detailedMsgDTO);
                } else {
                                
                    List msgs = new ArrayList();
                    msgs.add(detailedMsgDTO);
                    typeWiseMsgsMap.put(typeKey, msgs);
                }
                branchWiseMsgsMap.put(detailedMsgDTO.branchName, typeWiseMsgsMap);
            }
            detailedReportDTO.getDetailedMessages().put(detailedMsgDTO.entDate, branchWiseMsgsMap);
        }

    }

    /**
     * Prepare the Typewise Detailed Report
     * 
     * @param detailedMsgDTO DetailedMsgDTO , reportDTO DetailedReportDTO

     */
    private void prepareTypewiseDetailedReport(DetailedMsgDTO detailedMsgDTO, DetailedReportDTO reportDTO, String typeKey) {
        if (reportDTO.getDetailedMessages().containsKey(detailedMsgDTO.entDate)) {
                        
            Map typeWiseMsgsMap = (Map)reportDTO.getDetailedMessages().get(detailedMsgDTO.entDate);
            if (typeWiseMsgsMap.containsKey(typeKey)) {
                            
                Map branchWiseMsgsMap = (Map)typeWiseMsgsMap.get(typeKey);
                if (branchWiseMsgsMap.containsKey(detailedMsgDTO.branchName)) {
                                
                    ((List)branchWiseMsgsMap.get(detailedMsgDTO.branchName)).add(detailedMsgDTO);
                } else {
                                
                    List msgs = new ArrayList();
                    msgs.add(detailedMsgDTO);
                    branchWiseMsgsMap.put(detailedMsgDTO.branchName, msgs);
                }
                typeWiseMsgsMap.put(typeKey, branchWiseMsgsMap);
            } else {
                            
                Map branchWiseMsgsMap = new HashMap();
                if (branchWiseMsgsMap.containsKey(detailedMsgDTO.branchName)) {
                                
                    ((List)branchWiseMsgsMap.get(detailedMsgDTO.branchName)).add(detailedMsgDTO);
                } else {
                                
                    List msgs = new ArrayList();
                    msgs.add(detailedMsgDTO);
                    branchWiseMsgsMap.put(detailedMsgDTO.branchName, msgs);
                }
                typeWiseMsgsMap.put(typeKey, branchWiseMsgsMap);
            }
            reportDTO.getDetailedMessages().put(detailedMsgDTO.entDate, typeWiseMsgsMap);
        } else {
                        
            Map typeWiseMsgsMap = new HashMap();
            if (typeWiseMsgsMap.containsKey(typeKey)) {
                            
                Map branchWiseMsgsMap = (Map)typeWiseMsgsMap.get(typeKey);
                if (branchWiseMsgsMap.containsKey(detailedMsgDTO.branchName)) {
                                
                    ((List)branchWiseMsgsMap.get(detailedMsgDTO.branchName)).add(detailedMsgDTO);
                } else {
                                
                    List msgs = new ArrayList();
                    msgs.add(detailedMsgDTO);
                    branchWiseMsgsMap.put(detailedMsgDTO.branchName, msgs);
                }
                typeWiseMsgsMap.put(detailedMsgDTO.branchName, typeWiseMsgsMap);
            } else {
                            
                Map branchWiseMsgsMap = new HashMap();
                if (branchWiseMsgsMap.containsKey(detailedMsgDTO.branchName)) {
                                
                    ((List)branchWiseMsgsMap.get(detailedMsgDTO.branchName)).add(detailedMsgDTO);
                } else {
                                
                    List msgs = new ArrayList();
                    msgs.add(detailedMsgDTO);
                    branchWiseMsgsMap.put(detailedMsgDTO.branchName, msgs);
                }
                typeWiseMsgsMap.put(typeKey, branchWiseMsgsMap);
            }
            reportDTO.getDetailedMessages().put(detailedMsgDTO.entDate, typeWiseMsgsMap);
        }
    }

    /**
     * Get the Detailed Message by passing Message Id 
     * (both Simple and Compound Attribute)
     * 
     * @param String
     * @return DetailedMsgDTO
     */
    protected DetailedMsgDTO getDetailedMessage(String msgID){
        
        final String commonFieldQuery =
                "SELECT DMV.MD_TYPE TYPE, DMV.M_UTRNO UTR_NO, DMV.MD_SUB_TYPE SUB_TYPE,"+
                "   DMV.MD_NAME TYPE_NAME,  "+
                "   DMV.M_TRAN_TYPE TXN_TYPE, DMV.M_CURRENT_STATUS TXN_STATUS, " + 
                "   DMV.IM_BRANCH_NAME BRANCH_NAME, " +
                "   DMV.M_VALUE_DATE ENT_DATE, RS.NAME STATUSNAME "+
                "FROM DETAILEDMSGVIEW DMV, RTGS_STATUS RS  "+
                "WHERE DMV.M_MSGID = ? AND RS.ID = DMV.M_CURRENT_STATUS ORDER BY DMV.M_UTRNO DESC ";                
        
        final String simpleCompoundFieldQuery =
                "SELECT MFTNAME FIELD_NAME, MFTNO FIELD_NO, MFVALUE FIELD_VALUE, MFDISPVALUE DISP_VALUE, MFTID FIELD_ID " + 
                "FROM DETAILEDSIMPLEFIELDVIEW  " +
                "WHERE " +
                    "MFMSGID = ? AND MFVALUE IS NOT NULL " +
                "UNION "+
                "SELECT MFTNAME FIELD_NAME, MFTNO FIELD_NO, MFVALUE FIELD_VALUE, MFDISPVALUE DISP_VALUE, MFTID FIELD_ID " +
                "FROM DETAILEDCOMPFIELDVIEW " +
                "WHERE "+
                   "MFCMPID IN (SELECT MFID " +
                               "FROM DETAILEDCOMPFIELDVIEW " +
                               "WHERE MFMSGID = ?) AND MFVALUE IS NOT NULL " +
                "ORDER BY FIELD_ID";  
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            // Fetch Common message Details from DB to DetailedMsgDTO 
            ps = con.prepareStatement(commonFieldQuery);  
            ps.setString(1, msgID);
            rs = ps.executeQuery();
            
            DetailedMsgDTO detailedMsgDTO = new DetailedMsgDTO();            
            while (rs.next()) {
                
                detailedMsgDTO.msgType     = rs.getString(ReportConstants.TYPE);
                detailedMsgDTO.utrNumber   = rs.getString(ReportConstants.UTR_NUMBER);
                detailedMsgDTO.msgSubType  = rs.getString(ReportConstants.SUB_TYPE);
                detailedMsgDTO.typeName    = rs.getString(ReportConstants.TYPE_NAME);
                //detailedMsgDTO.creditTxnId = rs.getString(ReportConstants.TXN_ID);
                detailedMsgDTO.tranType    = rs.getString(ReportConstants.TXN_TYPE);
                
                String txnStat = rs.getString("STATUSNAME");
                 
                if (txnStat == null)
                    txnStat = "";                
                    
                detailedMsgDTO.txnStatus   = txnStat;
                //detailedMsgDTO.lotId       = rs.getString(ReportConstants.LOT_ID);
                detailedMsgDTO.branchName  = rs.getString(ReportConstants.BRANCH_NAME);
                detailedMsgDTO.entDate     = rs.getString(ReportConstants.ENT_DATE);
                detailedMsgDTO.entDate     = (detailedMsgDTO.entDate != null )
                                             ? ConversionUtil.getRTGSEntDate(detailedMsgDTO.entDate)
                                             : detailedMsgDTO.entDate;
            }    
            rs.close();
            ps.close();
                        
            // Fetch Message fields Value (Both Simple and Compound) to MsgFieldDTO 
            ps = con.prepareStatement(simpleCompoundFieldQuery);
            ps.setString(1, msgID);
            ps.setString(2, msgID);
            rs= ps.executeQuery();
            
            while (rs.next()) {
                
                MsgFieldDTO msgFieldDTO = new MsgFieldDTO();    
                msgFieldDTO.fieldName   = rs.getString(ReportConstants.FIELD_NAME);
                msgFieldDTO.fieldNo     = rs.getString(ReportConstants.FIELD_NO);
                msgFieldDTO.value       = rs.getString(ReportConstants.FIELD_VALUE);
                // if the value is Date value then make it to dd-mm-yyyy format                
                msgFieldDTO.value       = (msgFieldDTO.value != null && msgFieldDTO.fieldNo != null) 
                                           ? formatDate(msgFieldDTO.fieldNo, msgFieldDTO.value)
                                           : msgFieldDTO.value;
                                           
                msgFieldDTO.displayValue = rs.getString(ReportConstants.DISP_VALUE);
                detailedMsgDTO.getMsgfields().add(msgFieldDTO);
            }
            return detailedMsgDTO;
            
        } catch (Throwable t) {
            logger.error("commonFieldQuery : " + commonFieldQuery);
            logger.error("simpleCompoundFieldQuery : " + simpleCompoundFieldQuery);
            logger.error("Unable to get Detailed Message " + t.getMessage());
            throw new RuntimeException("Unable to get Detailed Message " + t.getMessage());
            
         } finally {
            try {
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            } catch (SQLException ignore) {

            }
        }   
    }
            
    /**
     * Format the Value (if date value) into dd-mm-yyyy format and hh : mm : ss
     * 
     * @param fno String, fvalue String
     */
    private String formatDate(String fNo, String fValue) {
      
        if (fNo.equalsIgnoreCase(MessageUtil.getValueDateFieldNo())) {
            
            try {
                fValue = ConversionUtil.getRTGSDate(Integer.parseInt(fValue));
            } catch (Exception e) {
                //Ignore the exception
                return fValue;   
            }
            
        } else if (fNo.equalsIgnoreCase(MessageUtil.getSSNSettlementTime())) {
            
            fValue = fValue.substring(0, 2) + ReportConstants.SSN_TIME_FORMAT_SEPERATOR + 
                     fValue.substring(2, 4) + ReportConstants.SSN_TIME_FORMAT_SEPERATOR + 
                     fValue.substring(4, 6) + " " +ReportConstants.SSN_TIME_FORMAT;
        }
        return fValue;
    }

    
    
   
    
   /**
    * This method is to get the RTGS Business Date from the RTGS Parameters 
    * 
    * @throws DAOException
    * @return Date
    * 
    * @see com.objectfrontier.rtgs.server.dao.MessageDAO#getRTGSBusinessDate()
    */
   public Date getRTGSBusinessDate() {

        Date rtgsBusinessDate = null;
        try {
            rtgsBusinessDate = getBusinessDate();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getMessage());
        } finally {
        }
        return rtgsBusinessDate;

    }


   /**
        * This method get the count of messages list for the each status.
        * This count will be displayed in the browser link.
        * Links for those count has to be displayed is idenfied in tiles-rhs-defs.xml
        * The count will be fecthed form the map return by this method wiht name given the tiles-rhs-defs.xml.
        * Name will be avalable in countkey property of item.
        * 
        *
        * @return map HashMap contains status and count pair
        * @throws DAOException
        */
       public Map getMessageCount(UserDTO userDTO){
        
           int TRANTYPE = 1;
           int STATUS = 2;
           int SUBTYPE = 3;
           int COUNT = 4;

           ResultSet rs = null;
           PreparedStatement ps = null;
           StringBuffer msgCountQueryBuf = new StringBuffer();
           
           try {
               msgCountQueryBuf.append("SELECT m.tran_type AS tran_type, m.current_status AS status, d.sub_type AS sub_type, count(*) ");
               msgCountQueryBuf.append("FROM message m, msgdefn d ");
               msgCountQueryBuf.append("WHERE m.object_history_ent_date = ? ");
               msgCountQueryBuf.append("AND class_identifier = 548 AND m.msg_defn_id = d.ID AND d.TYPE = '298' ");
            
               if (!userDTO.centralOfficeUser) 
                   msgCountQueryBuf.append("AND ifsc_master_id = ? ");
               msgCountQueryBuf.append("GROUP BY tran_type, current_status, sub_type");
                        
               ps = con.prepareStatement(msgCountQueryBuf.toString());
               ps.setString(1, ConversionUtil.formatDate(getBusinessDate(), "dd-MMM-yyyy"));
               if (!userDTO.centralOfficeUser) {
                   ps.setLong(2, userDTO.masterDTO.ifscMasterVO.id);    
               }
               rs= ps.executeQuery();
            
               HashMap countMap = new HashMap(); 
               while (rs.next()) {
                
                   String tranType = rs.getString(TRANTYPE);
                   String status   = rs.getString(STATUS);
                   String subType  = rs.getString(SUBTYPE);
                   int count       = rs.getInt(COUNT);

                   if (countMap.containsKey(tranType)) {
                    
                       Map subTypeMap = (Map)countMap.get(tranType);
                       if (subTypeMap.containsKey(subType)) {
                           Map statusMap = (Map)subTypeMap.get(subType);
                           statusMap.put(status, "" + count);
                       } else {
                           Map statusMap = new HashMap();
                           statusMap.put(status, "" + count);
                           subTypeMap.put(subType, statusMap);
                       }
                   } else {
                            
                       Map subTypeMap = new HashMap();
                       if (subTypeMap.containsKey(subType)) {
                           Map statusMap = (Map)subTypeMap.get(subType);
                           statusMap.put(status, "" + count);
                       } else {
                           Map statusMap = new HashMap();
                           statusMap.put(status, "" + count);
                           subTypeMap.put(subType, statusMap);
                       }
                       countMap.put(tranType, subTypeMap);
                   }
               }

               return countMap;
           } catch (Throwable t) {
               logger.error("msgCountQueryBuf : " + msgCountQueryBuf);
               logger.error("Unable to get Message count" + t.getMessage());
               throw new RuntimeException("Unable to get Message count" + t.getMessage());
               
            } finally {
               try {
                   if (ps != null) ps.close();
                   if (rs != null) rs.close();
               } catch (SQLException e) {
                   //ignore
               }
            
           }
       }

    /**
     * @see com.objectfrontier.rtgs.report.server.dao.ReportDAO#getSOLDetails()
     */
    public Message getSOLDetails(Message message){

        List SOLDetails = new ArrayList(0);
        String CODE = "code";       
        final String QueryString  = " SELECT code from bankmaster " ; 
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {

            ps = con.prepareStatement(QueryString);
            rs = ps.executeQuery();

            while (rs.next()) {
                SOLDetails.add(rs.getString(CODE));
            }
            rs.close();
            ps.close();

        }catch (Throwable t) {
            logger.error("QueryString : " + QueryString);
            logger.error("Unable to get SOL Details" + t.getMessage());
            throw new RuntimeException("Unable to get SOL Details" + t.getMessage());
            
         }finally {
            try {
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            } catch (SQLException sqlExp) {

            }
        }   
        message = new Message();
        message.info = SOLDetails;
        return message;
    }

    

    /**
     * This is to produce Counterparty report for branchwise
     * @param reportDTO
     * @param rs
     * @return ReportDTO
     * @throws DAOException
     */
    protected ReportDTO prepareBranchwiseCounterReportDTO(ReportDTO reportDTO,  ResultSet rs){


        try {
            List branches = new ArrayList(0);
            Map branchesMap = new HashMap();

            while (rs.next()) {

//                double amount = rs.getDouble(ReportConstants.AMOUNT);
                BigDecimal amount = rs.getBigDecimal(ReportConstants.AMOUNT);
                String receiverAddress = rs.getString(ReportConstants.RECEIVER_ADDRESS); //TODO TEMP CHANGE
                String senderAddress = rs.getString(ReportConstants.SENDER_ADDRESS); //TODO TEMP CHANGE
                String txnType  = rs.getString(ReportConstants.TXN_TYPE);
                String branchCode = rs.getString(ReportConstants.BRANCH_CODE);
                String branchName = rs.getString(ReportConstants.BRANCH_NAME);
                String type = rs.getString(ReportConstants.TYPE);
                String subType = rs.getString(ReportConstants.SUB_TYPE);
                String typeName = rs.getString(ReportConstants.TYPE_NAME);
                String branchAddress = rs.getString(ReportConstants.BRANCH_ADDRESS);


                if (reportDTO.branchCode != null) {
                    reportDTO.branchCode = branchCode;
                    reportDTO.branchName = branchName;
                }

                MsgDTO msgDTO = prepareMsgDTO(rs, amount, senderAddress, receiverAddress,subType);

                if (branchesMap.containsKey(branchCode)) {

                    BranchwiseDTO branchwiseDTO = (BranchwiseDTO) branchesMap.get(branchCode);
                    branchwiseDTO = prepareTypesMapForBranch(branchwiseDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
                    branchesMap.put(branchCode, branchwiseDTO);
                } else {

                    BranchwiseDTO branchwiseDTO = new BranchwiseDTO();
                    branchwiseDTO = prepareTypesMapForBranch(branchwiseDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
                    branchwiseDTO.setBranchAddress(branchAddress);
                    branchesMap.put(branchCode, branchwiseDTO);
                }
            }

            Set branchesKeys = branchesMap.keySet();
            Iterator b = branchesKeys.iterator();

            //Initialize All Branch Grand total value of both In and Out Amout
//            reportDTO.getBranchesGrandTotalDTO().setInAmount(0.0);
//            reportDTO.getBranchesGrandTotalDTO().setOutAmount(0.0);
            reportDTO.getBranchesGrandTotalDTO().setInAmount(BigDecimal.ZERO);
            reportDTO.getBranchesGrandTotalDTO().setOutAmount(BigDecimal.ZERO);

            while (b.hasNext()) {
                String bCode = (String) b.next();
                BranchwiseDTO branchwiseDTO = (BranchwiseDTO) branchesMap.get(bCode);

                Set typeKeys = branchwiseDTO.getMsgTypesMap().keySet();
                Iterator t = typeKeys.iterator();

                while (t.hasNext()) {
                    String subType = (String) t.next();
                    branchwiseDTO.getMsgTypes().add(branchwiseDTO.getMsgTypesMap().get(subType));
                }
                branches.add(branchwiseDTO);

                // Add Branches grandTotal of both In and Out.
                // This is only applicable for Controller Reports
                if(branchwiseDTO.grandTotalDTO != null)//AL
                    reportDTO.getBranchesGrandTotalDTO().inAmount = reportDTO.getBranchesGrandTotalDTO().inAmount
                                                                    .add(branchwiseDTO.grandTotalDTO.inAmount);
                if(branchwiseDTO.grandTotalDTO != null) //AL 
                    reportDTO.getBranchesGrandTotalDTO().outAmount = reportDTO.getBranchesGrandTotalDTO().outAmount
                                                                     .add(branchwiseDTO.grandTotalDTO.outAmount);

            }
            reportDTO.setBranchwiseDTOs(branches);

        }catch (Throwable t) {
            logger.error("Unable to prepare Branchwise Counter Report DTO" + t.getMessage());
            throw new RuntimeException("Unable to prepare Branchwise CounterReport DTO" + t.getMessage());
        }
        return reportDTO;

    }


   
    

//    /**
//     * This is to prepare Counterparty branch for type
//     * @param typewiseDTO
//     * @param msgDTO
//     * @param type
//     * @param subType
//     * @param typeName
//     * @param branchCode
//     * @param branchName
//     * @param txnType
//     * @param amount
//     * @return MsgTypewiseDTO
//     */
//    protected MsgTypewiseDTO prepareCounterpartyBranchForType(MsgTypewiseDTO typewiseDTO, MsgDTO msgDTO,
//    String type, String subType, String typeName, String branchCode, String branchName,
//    String txnType, double amount) {
//
//        Map branchesMap = typewiseDTO.getBranchesMap();
//        if (branchesMap.containsKey(branchCode)) {
//
//            BranchDTO branchDTO = (BranchDTO) branchesMap.get(branchCode);
//            branchDTO = prepareCounterpartyAllBranchDTO(branchDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
//            branchesMap.put(branchCode, branchDTO);
//        } else {
//
//            BranchDTO branchDTO = new BranchDTO();
//            branchDTO = prepareCounterpartyAllBranchDTO(branchDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
//            branchesMap.put(branchCode, branchDTO);
//        }
//
//        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//            typewiseDTO.getGrandTotalDTO().inAmount  += amount;
//            typewiseDTO.inCount += 1;
//        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
//            typewiseDTO.getGrandTotalDTO().outAmount  += amount;
//            typewiseDTO.outCount +=1;
//        }
//        typewiseDTO.setMsgType(type);
//        typewiseDTO.setMsgSubType(subType);
//        typewiseDTO.setMsgTypeName(typeName);
//
//        return typewiseDTO;
//    }
    
    /**
     * This is to prepare Counterparty branch for type
     * @param typewiseDTO
     * @param msgDTO
     * @param type
     * @param subType
     * @param typeName
     * @param branchCode
     * @param branchName
     * @param txnType
     * @param amount
     * @return MsgTypewiseDTO
     */
    protected MsgTypewiseDTO prepareCounterpartyBranchForType(MsgTypewiseDTO typewiseDTO, MsgDTO msgDTO,
    String type, String subType, String typeName, String branchCode, String branchName,
    String txnType, BigDecimal amount) {

        Map branchesMap = typewiseDTO.getBranchesMap();
        if (branchesMap.containsKey(branchCode)) {

            BranchDTO branchDTO = (BranchDTO) branchesMap.get(branchCode);
            branchDTO = prepareCounterpartyAllBranchDTO(branchDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
            branchesMap.put(branchCode, branchDTO);
        } else {

            BranchDTO branchDTO = new BranchDTO();
            branchDTO = prepareCounterpartyAllBranchDTO(branchDTO, msgDTO, type, subType, typeName, branchCode, branchName, txnType, amount);
            branchesMap.put(branchCode, branchDTO);
        }

        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
            typewiseDTO.getGrandTotalDTO().inAmount  = typewiseDTO.getGrandTotalDTO().inAmount.add(amount);
            typewiseDTO.inCount += 1;
        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
            typewiseDTO.getGrandTotalDTO().outAmount  = typewiseDTO.getGrandTotalDTO().outAmount.add(amount);
            typewiseDTO.outCount +=1;
        }
        typewiseDTO.setMsgType(type);
        typewiseDTO.setMsgSubType(subType);
        typewiseDTO.setMsgTypeName(typeName);

        return typewiseDTO;
    }    

//    /**
//     * This is to prepare Counter party for all branches
//     * @param branchDTO
//     * @param msgDTO
//     * @param type
//     * @param subType
//     * @param typeName
//     * @param branchCode
//     * @param branchName
//     * @param txnType
//     * @param amount
//     * @return BranchDTO
//     */
//    protected BranchDTO prepareCounterpartyAllBranchDTO(BranchDTO branchDTO, MsgDTO msgDTO,
//    String type, String subType,
//    String typeName, String branchCode, String branchName,
//    String txnType, double amount) {
//
//        logger.info("branchDTO : " + branchDTO + ", MsgDTO : " + msgDTO + ", type : " + type + ", subType : " + subType + ", amount :" + amount);
//        logger.info("typeName : " + typeName + ", branchCode : " + branchCode + ", branchName : " + branchName + ", txnType : " + txnType);
//        
//        List messages = branchDTO.getMessages();
//
//            if (messages != null && !messages.isEmpty()) {
//                for (int i=0; i < messages.size(); i++ ) {
//                    MsgDTO _msgDTO = (MsgDTO)messages.get(i);
//
//                    if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(msgDTO.txnType)) {
//                        _msgDTO.getAmountDTO().setInAmount(amount);
//
//                    } else {
//                        _msgDTO.getAmountDTO().setOutAmount(amount);
//                    }
//                 }
//            } else {
//                branchDTO.getMessages().add(msgDTO);
//            }
//
//
//        branchDTO.setMsgSubType(subType);
//        branchDTO.setBranchCode(branchCode);
//        branchDTO.setBranchName(branchName);
//        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//            branchDTO.getGrandTotalDTO().inAmount  += amount;
//        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
//            branchDTO.getGrandTotalDTO().outAmount  += amount;
//        }
//
//        return branchDTO;
//    }
    
    /**
     * This is to prepare Counter party for all branches
     * @param branchDTO
     * @param msgDTO
     * @param type
     * @param subType
     * @param typeName
     * @param branchCode
     * @param branchName
     * @param txnType
     * @param amount
     * @return BranchDTO
     */
    protected BranchDTO prepareCounterpartyAllBranchDTO(BranchDTO branchDTO, MsgDTO msgDTO,
    String type, String subType,
    String typeName, String branchCode, String branchName,
    String txnType, BigDecimal amount) {

        logger.info("branchDTO : " + branchDTO + ", MsgDTO : " + msgDTO + ", type : " + type + ", subType : " + subType + ", amount :" + amount);
        logger.info("typeName : " + typeName + ", branchCode : " + branchCode + ", branchName : " + branchName + ", txnType : " + txnType);
        
        List messages = branchDTO.getMessages();

            if (messages != null && !messages.isEmpty()) {
                for (int i=0; i < messages.size(); i++ ) {
                    MsgDTO _msgDTO = (MsgDTO)messages.get(i);

                    if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(msgDTO.txnType)) {
                        _msgDTO.getAmountDTO().setInAmount(amount);

                    } else {
                        _msgDTO.getAmountDTO().setOutAmount(amount);
                    }
                 }
            } else {
                branchDTO.getMessages().add(msgDTO);
            }


        branchDTO.setMsgSubType(subType);
        branchDTO.setBranchCode(branchCode);
        branchDTO.setBranchName(branchName);
        if(ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
            branchDTO.getGrandTotalDTO().inAmount  = branchDTO.getGrandTotalDTO().inAmount.add(amount);
        } else if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
            branchDTO.getGrandTotalDTO().outAmount = branchDTO.getGrandTotalDTO().outAmount.add(amount);
        }

        return branchDTO;
    }


//    /**
//     * This is to prepare Counter party Message DTO
//     * @param rs
//     * @param amount
//     * @return MsgDTO
//     * @throws DAOException
//     */
//    protected MsgDTO prepareCounterpartyMsgDTO(ResultSet rs, double amount){
//
//        MsgDTO msgDTO = new MsgDTO();
//        try {
//            AmountDTO amountDTO = new AmountDTO();
//            String txnType  = rs.getString(ReportConstants.TXN_TYPE);
//
//            if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
//                msgDTO.txnType  = ReportConstants.IN;
//                amountDTO.inAmount = amount;
//            } else {
//                msgDTO.txnType  = ReportConstants.OUT;
//                amountDTO.outAmount = amount;
//            }
//
//            msgDTO.amountDTO = amountDTO;
//
//        }catch (Throwable t) {
//            logger.error("Unable to prepare Counterparty MsgDTO" + t.getMessage());
//            throw new RuntimeException("Unable to prepare Counterparty MsgDTO" + t.getMessage());
//            
//         }finally {
//        }
//
//        return msgDTO;
//    }

    /**
     * This is to prepare Counter party Message DTO
     * @param rs
     * @param amount
     * @return MsgDTO
     * @throws DAOException
     */
    protected MsgDTO prepareCounterpartyMsgDTO(ResultSet rs, BigDecimal amount){

        MsgDTO msgDTO = new MsgDTO();
        try {
            AmountDTO amountDTO = new AmountDTO();
            String txnType  = rs.getString(ReportConstants.TXN_TYPE);

            if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
                msgDTO.txnType  = ReportConstants.IN;
                amountDTO.inAmount = amount;
            } else {
                msgDTO.txnType  = ReportConstants.OUT;
                amountDTO.outAmount = amount;
            }

            msgDTO.amountDTO = amountDTO;

        }catch (Throwable t) {
            logger.error("Unable to prepare Counterparty MsgDTO" + t.getMessage());
            throw new RuntimeException("Unable to prepare Counterparty MsgDTO" + t.getMessage());
            
         }finally {
        }

        return msgDTO;
    }
    
    /**
     * To get the host ifsc list.
     */
    

    public Message getHostIFSCList(Message req) {
        
        final String QUERY_GETIFSCCODE_LIST = "SELECT * FROM IFSCMASTER MASTER, HOSTIFSCMASTER HOST " +
                                              "WHERE MASTER.ID = HOST.ID ORDER BY IFSC";

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<HostIFSCDetailsDTO> hostIFSCDetailsDTOList = new ArrayList<HostIFSCDetailsDTO>();
        Message newMessage = new Message();
        logger.info("req : " + req);
        try {

            ps = con.prepareStatement(QUERY_GETIFSCCODE_LIST);
            rs = ps.executeQuery();
            HostIFSCDetailsDTO hostIFSCDetailsDTO;
            
            while (rs.next()) {

                hostIFSCDetailsDTO = new HostIFSCDetailsDTO();
                
                HostIFSCDetailsValueObject hostIFSCDetailsValueObject = new HostIFSCDetailsValueObject();
                hostIFSCDetailsValueObject.setDbConnectionURL(rs.getString("DB_CONNECTION_URL"));
                hostIFSCDetailsValueObject.setDbDriverName(rs.getString("DB_DRIVER_NAME"));
                hostIFSCDetailsValueObject.setDbSchemaName(rs.getString("DB_SCHEMA_NAME"));
                hostIFSCDetailsValueObject.setDbUserName(rs.getString("DB_USER_NAME"));
                hostIFSCDetailsValueObject.setDbPassword(rs.getString("DB_PASSWORD"));
                hostIFSCDetailsValueObject.setIpAddress(rs.getString("IP_ADDRESS"));
                hostIFSCDetailsValueObject.setPort(rs.getString("port"));
                hostIFSCDetailsValueObject.setEmailId(rs.getString("email_id"));
                hostIFSCDetailsValueObject.setBranchCode(rs.getString("branch_code"));
                hostIFSCDetailsValueObject.setBranchType(rs.getString("branch_type"));
                hostIFSCDetailsDTO.ifscMasterVO = hostIFSCDetailsValueObject;
                
                if (rs.getInt("ACTIVE") != 0 ){
                
                    hostIFSCDetailsDTO.ifscMasterVO.setActive(false);
                }else {
                    hostIFSCDetailsDTO.ifscMasterVO.setActive(true);
                }
                
                hostIFSCDetailsDTO.ifscMasterVO.setAddress(rs.getString("ADDRESS"));
                hostIFSCDetailsDTO.ifscMasterVO.setCity(rs.getString("CITY"));
                hostIFSCDetailsDTO.ifscMasterVO.setId(rs.getLong("ID"));
                hostIFSCDetailsDTO.ifscMasterVO.setIfsc(rs.getString("IFSC"));
                hostIFSCDetailsDTO.ifscMasterVO.setName(rs.getString("NAME"));
                
                hostIFSCDetailsDTO.ifscMasterVO.setPincode(rs.getString("PINCODE"));
                hostIFSCDetailsDTO.ifscMasterVO.setState(rs.getString("STATE"));
                
               /* ObjectHistory objectHistory = new ObjectHistory();
                
                objectHistory.setEntBr(rs.getString("OBJECT_HISTORY_ENT_BR"));
                objectHistory.setEntBy(rs.getString("OBJECT_HISTORY_ENT_BY"));
                objectHistory.setEntDate(rs.getDate("OBJECT_HISTORY_ENT_DATE"));
                objectHistory.setPassBr(rs.getString("OBJECT_HISTORY_PASS_BR"));
                objectHistory.setPassBy(rs.getString("OBJECT_HISTORY_PASS_BY"));
                objectHistory.setPassDate(rs.getDate("OBJECT_HISTORY_PASS_DATE"));
                objectHistory.setTimeStamp(rs.getTimestamp("OBJECT_HISTORY_TIME_STAMP"));
               
                
                ifscMasterDTO.ifscMasterVO.setObjectHistory(objectHistory);*/
                
                hostIFSCDetailsDTOList.add(hostIFSCDetailsDTO);
            }
            newMessage.info = hostIFSCDetailsDTOList;
        } catch (Throwable th) {
            logger.error("QUERY  : " + QUERY_GETIFSCCODE_LIST);
            logger.error("Exception Occurred while getting the HostIFSC Code List : " + th.getMessage());
            throw new BOException("Exception Occurred while getting the HostIFSC Code List : ", th);
        }
        finally {
            
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();

            } catch (Exception ignore) {

            }
        }
        return newMessage;
    }
    
    
    /**
     * To get the host ifsc list.
     */
   

    public Message getIfscDetails(Message req) {
        
        final String QUERY_GETIFSCCODE_LIST    = 
            "SELECT * FROM IFSCMASTER MASTER, HOSTIFSCMASTER HOST " +
                        "WHERE IFSC = ? AND MASTER.ID = HOST.ID ORDER BY IFSC";
        
        String ifsc = (String) req.info;

        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {

            ps = con.prepareStatement(QUERY_GETIFSCCODE_LIST);
            ps.setString(1, ifsc);
            rs = ps.executeQuery();
            IFSCMasterDTO ifscMasterDTO;
            ifscMasterDTO = new IFSCMasterDTO();
            
            if (rs.next()) {
                
                HostIFSCDetailsValueObject hostIFSCDetailsValueObject = new HostIFSCDetailsValueObject();
                
                hostIFSCDetailsValueObject.setBranchCode(rs.getString("branch_code"));
                hostIFSCDetailsValueObject.setName(rs.getString("name"));
                hostIFSCDetailsValueObject.setBranchType(rs.getString("branch_type"));
                hostIFSCDetailsValueObject.setDbConnectionURL(rs.getString("DB_CONNECTION_URL"));
                hostIFSCDetailsValueObject.setDbDriverName(rs.getString("DB_DRIVER_NAME"));
                hostIFSCDetailsValueObject.setDbPassword(rs.getString("DB_PASSWORD"));
                hostIFSCDetailsValueObject.setDbSchemaName(rs.getString("DB_SCHEMA_NAME"));
                hostIFSCDetailsValueObject.setDbUserName(rs.getString("DB_USER_NAME"));
                hostIFSCDetailsValueObject.setEmailId(rs.getString("email_id"));
                hostIFSCDetailsValueObject.setIfsc(rs.getString("ifsc"));
                hostIFSCDetailsValueObject.setIpAddress(rs.getString("IP_ADDRESS"));
                hostIFSCDetailsValueObject.setPort(rs.getString("port"));
                
                ifscMasterDTO.ifscMasterVO = hostIFSCDetailsValueObject;
                
            }
            req.info = ifscMasterDTO;
            
        }catch (Throwable th) {
            logger.error("QUERY : " + QUERY_GETIFSCCODE_LIST);
            logger.error("Exception Occurred while getting the HostIFSC Code List : " + th.getMessage());
            throw new BOException("Exception Occurred while getting the HostIFSC Code List : ", th);
        }finally {
            
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();

            } catch (Exception ignore) {

            }
        }
        return req;
    }
    
    public Message getReportController(Message message){
         
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        String branch = null;
        
        ReportDTO reportDTO = (ReportDTO)message.info;
        Map prop = (Map)message.getProperty("prop");
        StringBuffer query = new StringBuffer();
        
        try {
            
           query.append(" SELECT ifsc.IFSC ifsc, md.sub_type sub_type, rm.utr_no utr_number, msg.msg_id message_id, ");
           query.append(" rm.value_date ent_date, rm.value_date value_date, rm.sender_address sender_address, rm.receiver_address receiver_address, ");
           query.append(" md.TYPE TYPE, md.NAME type_name,rm.amount amount, rm.status txn_status, ");
           query.append(" rm.tran_type txn_type, ifsc.NAME branch_name, hostifsc.branch_code branch_code, rs.name statusName ");
           query.append(" FROM MESSAGE msg, RTGS_MESSAGE rm, MSGDEFN md, IFSCMASTER ifsc, HOSTIFSCMASTER hostifsc, RTGS_STATUS rs " );
           
           
           if ((ReportConstants.REDIRECTED.equalsIgnoreCase(reportDTO.txnStatus))
                               || (ReportConstants.FOR_OTHER_BRANCHES.equalsIgnoreCase(reportDTO.txnStatus))) {
                     
                 query.append(", MSGIFSCMASTERHISTORY h, BANKMASTER ");
           }
           query.append(" WHERE rm.msg_id = msg.msg_id AND msg.msg_defn_id = md.ID "); 
           query.append(" AND rm.ifsc_master_id = hostifsc.ID "); 
           query.append(" AND  hostifsc.ID = ifsc.ID AND rs.id = rm.status"); 
           query.append(" AND msg.msg_sub_type not in ('R09', 'R90', 'R43', 'R44')");
           
           if (reportDTO.txnStatus != null) {
               
               if ((ReportConstants.SUCCESSFUL.equalsIgnoreCase(reportDTO.txnStatus)) 
                       || (ReportConstants.SUCCESSFUL_TYPEWISE.equalsIgnoreCase(reportDTO.txnStatus)) ) {
         
                   if (null != reportDTO.tranType ){
                       
                       if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(reportDTO.tranType)){
                           query.append("AND rm.status = " + ReportConstants.INWARD_SUCCESSFUL_STATUS + " ");
                       }
                       else if( ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(reportDTO.tranType)){
                           
                           query.append("AND rm.status = " + ReportConstants.OUTWARD_SUCCESSFUL_STATUS + " ");
                       }
                       else{
                           query.append("AND rm.status in (" + ReportConstants.INWARD_SUCCESSFUL_STATUS + "," 
                                        + ReportConstants.OUTWARD_SUCCESSFUL_STATUS + ") ");
                       }
                   }
         
               } else if ((ReportConstants.UNSUCCESSFUL.equalsIgnoreCase(reportDTO.txnStatus))
                       || (ReportConstants.UNSUCCESSFUL_TYPEWISE.equalsIgnoreCase(reportDTO.txnStatus)) ) {
         
                   
                   if (null != reportDTO.tranType ){
                       
                       if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(reportDTO.tranType))
                       {
                           query.append("AND rm.status = " + ReportConstants.INWARD_UNSUCCESSFUL_STATUS + " ");
                       }
                       else if( ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(reportDTO.tranType)){
                           
                           query.append("AND rm.status = " + ReportConstants.OUTWARD_UNSUCCESSFUL_STATUS + " ");
                       }
                       else{
                           query.append("AND rm.status in (" + ReportConstants.INWARD_UNSUCCESSFUL_STATUS + "," 
                                        + ReportConstants.OUTWARD_UNSUCCESSFUL_STATUS + ") ");
                       }
                   }
         
               } else if (ReportConstants.RETURNED.equalsIgnoreCase(reportDTO.txnStatus)) {
         
                   query.append("AND rm.status = '" + ReportConstants.INWARD_RETURNED_STATUS + "' ");
                   query.append("AND rm.tran_type = '" + ReportConstants.TXN_TYPE_INWARD + "' ");

               } /*else if (ReportConstants.REDIRECTED.equalsIgnoreCase(reportDTO.txnStatus)) {
         
                   query.append("AND h.operation_code = '" + ReportConstants.REASONCODE_REDIRECTION + "' ");
                   query.append("AND h.msg_id = msg.msg_id ");

               } else if (ReportConstants.FOR_OTHER_BRANCHES.equalsIgnoreCase(reportDTO.txnStatus)) {
         
                   query.append("AND h.operation_code = '" + ReportConstants.REASONCODE_CO_VOUCHING + "' ");
                   query.append("AND h.msg_id = msg.msg_id ");
         
               } */else if (ReportConstants.TIMED_OUT.equalsIgnoreCase(reportDTO.txnStatus)) {//RBC CMD 1.0

         
                   query.append("AND rm.status = '" + ReportConstants.OUTWARD_TIMED_OUT_STATUS + "' ");
         
               } else if (ReportConstants.CANCELLED.equalsIgnoreCase(reportDTO.txnStatus)) {//RBC CMD 1.0

         
               query.append("AND rm.status = '" + ReportConstants.OUTWARD_CANCELLED_STATUS + "' ");
         
           }                  
           }
           
           if (reportDTO.branchCode != null 
                   && reportDTO.branchCode.trim().length() > 0) {

               branch =  reportDTO.branchCode;
               if (!(ReportConstants.ALL_BRANCH.equalsIgnoreCase(reportDTO.branchCode))) {
                   query.append("   AND rm.ifsc_master_id = "); 
                   query.append("            (SELECT i.ID "); 
                   query.append("               FROM HOSTIFSCMASTER i, IFSCMASTER im "); 
                   query.append("              WHERE i.ID = im.ID AND i.branch_code = '"+ reportDTO.branchCode +"') ");                
         
               }
           }  

           if (reportDTO.getFromAmount().compareTo(BigDecimal.ZERO) >= 0 && reportDTO.getToAmount().compareTo(BigDecimal.ZERO) > 0 ) {            
               query.append(" AND amount between ");
               query.append(reportDTO.getFromAmount());
               query.append(" AND ");
               query.append(reportDTO.getToAmount());

           }
             
           if ( reportDTO.getUserId() != null && reportDTO.getUserId().length() != 0) {
               query.append(" AND ");
               query.append(" rm.entry_by = '");
               query.append(reportDTO.getUserId());
               query.append("'");
                 
           }
           if (reportDTO.getTranType() != null) {

               if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(reportDTO.getTranType())) {
                   query.append(" AND ");
                   query.append(" rm.tran_type = '" + reportDTO.getTranType() + "'");
               }

               if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(reportDTO.getTranType())) {
                   query.append(" AND ");
                   query.append(" rm.tran_type = '" + reportDTO.getTranType() + "'");
               }
     
           }
           
           if (reportDTO.getMsgSubType() != null) { //RBC CMD 1.0
               if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(reportDTO.getMsgSubType()) || ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(reportDTO.getMsgSubType())) {
                   query.append(" AND ");
                   query.append(" md.type = '" + ReportConstants.MSGTYPE + "'");
                   query.append(" AND ");
                   query.append(" md.sub_type = '" + reportDTO.getMsgSubType() + "'");
               }
           }
           
//         For UTR No. Wiser Report
          if (reportDTO.getUtrNumber() != null) {//RBC CMD 1.0
     
             query.append(" AND ");
             query.append(" rm.utr_no LIKE '%" + reportDTO.getUtrNumber() + "%'");                    

          }  

//        Date has to be taken for all report other than UTRNo.wise report    
          if (reportDTO.getUtrNumber() == null) {//RBC CMD 1.0
              query.append(" AND rm.business_date BETWEEN ? AND ? ");
          }
          
          //query.append(" ORDER BY ifsc.IFSC, md.sub_type, rm.value_date, rm.utr_no DESC ");
          query.append(" ORDER BY rm.utr_no DESC ");
          
          ps = con.prepareStatement(query.toString());
          if (reportDTO.fromDate != null && reportDTO.toDate != null) {
     
              //Date has to be taken for all report other than UTRNo.wise report    
              if (reportDTO.getUtrNumber() == null) {//RBC CMD 1.0
                                
                      ps.setTimestamp(1, reportDTO.fromDate);
                      ps.setTimestamp(2, reportDTO.toDate);
              }
         }
         
         rs = ps.executeQuery();
         String txnType = reportDTO.getTranType();
         
         if (ReportConstants.UNSUCCESSFUL_TYPEWISE.equalsIgnoreCase(reportDTO.txnStatus) 
                             ||  ReportConstants.SUCCESSFUL_TYPEWISE.equalsIgnoreCase(reportDTO.txnStatus)) {

             reportDTO = prepareTypewiseReportDTO(reportDTO, rs);
             reportDTO.setTranType(txnType);
         } else if (reportDTO.reportType.equalsIgnoreCase("Consolidated")) { 
             
             reportDTO = prepareConsolidatedReportDTO(reportDTO, rs);
             reportDTO.setTranType(txnType);
         } else {

             reportDTO = prepareBranchwiseReportDTOController(reportDTO, rs,prop); 
             reportDTO.setTranType(txnType);
             StringBuffer countQuery = new StringBuffer();
             countQuery.append(" SELECT COUNT(msg.msg_id) FROM MESSAGE msg, RTGS_MESSAGE rm, ");
             countQuery.append(" MSGDEFN md, IFSCMASTER ifsc, HOSTIFSCMASTER hostifsc ");
             countQuery.append(" WHERE msg.msg_defn_id = md.ID AND rm.msg_id = msg.msg_id ");   
             countQuery.append(" AND rm.ifsc_master_id = hostifsc.ID AND hostifsc.ID = ifsc.ID ");
             countQuery.append(" AND msg.msg_sub_type NOT IN ( 'R09', 'R90', 'R43', 'R44' ) ");
             
             if (reportDTO.getTranType() != null) {

                 if (ReportConstants.TXN_TYPE_INWARD.equalsIgnoreCase(reportDTO.getTranType())) {
                     countQuery.append(" AND ");
                     countQuery.append(" rm.tran_type = '" + reportDTO.getTranType() + "'");
                 }

                 if (ReportConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(reportDTO.getTranType())) {
                     countQuery.append(" AND ");
                     countQuery.append(" rm.tran_type = '" + reportDTO.getTranType() + "'");
                 }
            }
            if (reportDTO.getMsgSubType() != null) { //RBC CMD 1.0
                if (ReportConstants.CPRSUB_TYPE.equalsIgnoreCase(reportDTO.getMsgSubType()) || ReportConstants.IPRSUB_TYPE.equalsIgnoreCase(reportDTO.getMsgSubType())) {
                     countQuery.append(" AND ");
                     countQuery.append(" md.type = '" + ReportConstants.MSGTYPE + "'");
                     countQuery.append(" AND ");
                     countQuery.append(" md.sub_type = '" + reportDTO.getMsgSubType() + "'");
                }
            }
            if (reportDTO.getFromAmount().compareTo(BigDecimal.ZERO) >= 0 && reportDTO.getToAmount().compareTo(BigDecimal.ZERO) > 0 ) {            
                countQuery.append(" AND amount between ");
                countQuery.append(reportDTO.getFromAmount());
                countQuery.append(" AND ");
                countQuery.append(reportDTO.getToAmount());

            }
            if ( reportDTO.getUserId() != null && reportDTO.getUserId().length() != 0) {
                countQuery.append(" AND ");
                countQuery.append(" rm.entry_by = '");
                countQuery.append(reportDTO.getUserId());
                countQuery.append("'");

            }
            
            if(branch != null) {
                if (!(ReportConstants.ALL_BRANCH.equalsIgnoreCase(branch))) {
                    countQuery.append("   AND rm.ifsc_master_id = "); 
                    countQuery.append("            (SELECT i.ID "); 
                    countQuery.append("               FROM HOSTIFSCMASTER i, IFSCMASTER im "); 
                    countQuery.append("              WHERE i.ID = im.ID AND i.branch_code = '"+ branch +"') ");                

                }
            }
            countQuery.append(" AND rm.business_date BETWEEN ? AND ? ");
            
            ps1 =  con.prepareStatement(countQuery.toString());
            
            if (reportDTO.fromDate != null && reportDTO.toDate != null) {
     
                ps1.setTimestamp(1, reportDTO.fromDate);
                ps1.setTimestamp(2, reportDTO.toDate);    
            }
             
            rs1 = ps1.executeQuery();
            int totalTxn = 0;
            while (rs1.next()) {
                
                totalTxn = rs1.getInt(1);
                reportDTO.setTxnCount(totalTxn);
            }
        } 
        } catch (Throwable th) {
            
            logger.error("QUERY : " + query);
            logger.error("Unable to get Report Controller : " + th.getMessage());
            throw new RuntimeException("Unable to get Report Controller : ", th);
        } finally {
            try {
                rs.close();
                ps.close();
                rs1.close();
                ps1.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        message.info = reportDTO;
        return message;
     }
    
}

