package com.objectfrontier.insta.rtgs.reports.server.bo;


/**
 *
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.objectfrontier.arch.dto.Message;
import com.objectfrontier.arch.server.bo.BOException;
import com.objectfrontier.insta.InstaDefaultConstants;
import com.objectfrontier.insta.message.client.dto.CMsgDTO;
import com.objectfrontier.insta.reports.InstaReportUtil;
import com.objectfrontier.insta.reports.constants.ReportConstants;
import com.objectfrontier.insta.reports.dto.ReportDTO;
import com.objectfrontier.insta.reports.dto.ReportInputDTO;
import com.objectfrontier.insta.reports.server.bo.InstaReportBO;
import com.objectfrontier.insta.server.workflow.bo.InstaRTGSMessageBO;
import com.objectfrontier.insta.workflow.util.ConversionUtil;
import com.objectfrontier.insta.workflow.util.FormatAmount;

/**
 *
 * @author jinuj
 * @date   Jul 29, 2008
 * @since  insta.reports; Jul 29, 2008
 */
public class InstaRTGSReportBO extends InstaReportBO {

    //This is used for logging purpose(Log4j).
    private static Category logger = Category.getInstance(InstaRTGSReportBO.class.getName());

    public List branchDto = new ArrayList();

    /**
     * TODO
     /
    static final Comparator reportDateComparator = new Comparator<String>() {
        public int compare ( String dt1, String dt2 ) {
            try {
                Date date1  = InstaReportUtil.convertToDate(dt1);
                Date date2  = InstaReportUtil.convertToDate(dt2);
                if (date1.after(date2)) {
                    return 1;
                } else if (date1.before(date2)) {
                    return -1;
                }
                return 0;
            } catch (Throwable e) {

                logger.error("Date Comparision problem. "
                             + e.getMessage());
                logger.error("Exception while comparing 2 date for Reports. "
                             + e.getMessage());
            }
            return 0;
        }
    };

    /**
     * TODO
     /
    static final Comparator reportStringComparator = new Comparator<String>() {
        public int compare ( String str1, String str2 ) {

            return str1.compareToIgnoreCase(str2);
        }
    };
**/

    /**
     *   Construt the Filter Condition based on the user type. User can be Central Office user or
     * Branch User. Branch User should only his branch transactions. Central Office User can see
     * all the Transactions in the bank.
     *
     * @return String - constructed filter condition.
     */
    public String getFilterCondition(ReportInputDTO dto, boolean isCOReport, long ifscId) {

        String filter = "";

        logger.debug("Param : " + dto + " " + isCOReport + " " + ifscId);

        if (isCOReport) {
            if (!(dto.getIfscId()==0)) {
                filter += " AND rm.ifsc_id = '" + dto.getIfscId() + "'";
            }
        } else {
            if (!(dto.getIfscId()==0)) {
                filter += " AND rm.ifsc_id = '" + dto.getIfscId() + "'";
            }
        }
        return filter;
    }

    /**
     * This Query is used to generate the Customer Payment Received Report Grouped by Receiver Address.
     * Later the query should be change to adapt the user branch code as a filter condition.
     *
     *  @param dto Holds the filter condition.
     *  @param isCOReport true if the user is COUser, else false.
     *  @param ifscId Holds the IfscId of the login users branch.
     *
     *  @return Constructed Query
     */
    public String getR41InwardReportQuery(ReportInputDTO dto, boolean isCOReport, long ifscId) {

        String filter = getFilterCondition(dto, isCOReport, ifscId);
        //Construct the R41 Inward Report Query
        String qryR41InwardReport    =  "SELECT DISTINCT rm.value_date AS value_date, rm.sender_address " +
                                        "AS sender_address, COUNT(*) AS COUNT, TO_CHAR(SUM ( TO_NUMBER(rm.amount))) AS totalAmount " +
                                        "FROM  v_r41inwardtxn rm " +
                                        "WHERE rm.business_date = ? and  rm.tran_type='inward' " + filter +
                                        " GROUP BY rm.value_date, rm.sender_address ";
        return qryR41InwardReport;
    }

    /**
     * This Query is used to generate the InterBank Payment Received Report Grouped by Receiver Address.
     * Later the query should be change to adapt the user branch code as a filter condition.
     *
     *  @param dto Holds the filter condition.
     *  @param isCOReport true if the user is COUser, else false.
     *  @param ifscId Holds the IfscId of the login users branch.
     *
     *  @return Constructed Query
     */
    public String getR42InwardReportQuery(ReportInputDTO dto, boolean isCOReport, long ifscId) {

        String filter = getFilterCondition(dto, isCOReport, ifscId);
        //Construct the R42 Inward Report Query
        String qryR42InwardReport    =  "SELECT DISTINCT rm.value_date AS value_date, rm.sender_address " +
                                        "AS sender_address, COUNT(*) AS COUNT, TO_CHAR(SUM ( TO_NUMBER(rm.amount))) AS totalAmount " +
                                        "FROM  v_r42inwardtxn rm " +
                                        "WHERE rm.business_date = ? and  rm.tran_type='inward' " + filter +
                                        " GROUP BY rm.value_date, rm.sender_address ";

        return qryR42InwardReport;
    }

    /**
     * This Query is used to generate the Customer Payment Submitted Report Grouped by Sender Address.
     * Later the query should be change to adapt the user branch code as a filter condition.
     *
     *  @param dto Holds the filter condition.
     *  @param isCOReport true if the user is COUser, else false.
     *  @param ifscId Holds the IfscId of the login users branch.
     *
     *  @return Constructed Query
     */
    public String getR41OutwardReportQuery(ReportInputDTO dto, boolean isCOReport, long ifscId) {

        String filter = getFilterCondition(dto, isCOReport, ifscId);
        logger.info("Filter :"+filter);
        //Construct the R41 Outward Report Query
        String qryR41Outwardreport   = "SELECT DISTINCT rm.value_date AS value_date, rm.receiver_address " +
                                       "AS receiver_address, COUNT(*) AS COUNT, TO_CHAR(SUM(TO_NUMBER(rm.amount))) AS totalAmount " +
                                       "FROM  v_r41outwardtxn rm " +
                                       "WHERE rm.business_date = ? and  rm.tran_type='outward' " + filter +
                                       " GROUP BY rm.value_date, rm.receiver_address ";
        return qryR41Outwardreport;
    }

    /**
     * This Query is used to generate the InterBank Payment Submitted Report Grouped by Sender Address.
     * Later the query should be change to adapt the user branch code as a filter condition.
     *
     *  @param dto Holds the filter condition.
     *  @param isCOReport true if the user is COUser, else false.
     *  @param ifscId Holds the IfscId of the login users branch.
     *
     *  @return Constructed Query
     */
    public String getR42OutwardReportQuery(ReportInputDTO dto, boolean isCOReport, long ifscId) {

        String filter = getFilterCondition(dto, isCOReport, ifscId);
        //Construct the R42 Outward Report Query
        String qryR42OutwardReport   =  "SELECT DISTINCT rm.value_date AS value_date, rm.receiver_address " +
                                        "AS receiver_address, COUNT(*) AS COUNT, TO_CHAR(SUM(TO_NUMBER(rm.amount))) AS totalAmount " +
                                        "FROM  v_r42outwardtxn rm " +
                                        "WHERE rm.business_date = ? and  rm.tran_type='outward' " + filter +
                                        " GROUP BY rm.value_date, rm.receiver_address ";
        return qryR42OutwardReport;
    }

    /**
     * Reconcillation Reoprt Query, only for the central office user.
     */
    public String getRTGSReconcillationReportQuery(ReportInputDTO dto) {

        logger.info("" + dto);

        String qryRTGSReconcillationReport  =
                        " SELECT   m.msg_type || '/' || m.msg_sub_type as msgType, rm.tran_type as tranType, " +
                        "   COUNT (*) as COUNT, TO_CHAR(SUM (TO_NUMBER(amount))) as TOTALAMOUNT                                  " +
                        "        FROM rtgs_message rm, MESSAGE m                                             " +
                        "       WHERE rm.business_date = ?  AND m.msg_sub_type IN ('R41', 'R42', 'R10', 'R43', 'R44') " +
                        "         AND rm.msg_id = m.msg_id                                                   " +
                        " GROUP BY m.msg_type || '/' || m.msg_sub_type, rm.tran_type                         ";

        return qryRTGSReconcillationReport;
    }

    /**
     * Construct the query to get the Inward Messages which are returned.
     *
     *  @param dto Holds the filter condition.
     *  @param isCOReport true if the user is COUser, else false.
     *  @param ifscId Holds the IfscId of the login users branch.
     *
     *  @return Constructed Query
     */
    public String getInwardReturnQuery(ReportInputDTO dto, boolean isCOReport, long ifscId) {

        String filter = getFilterCondition(dto, isCOReport, ifscId);
        //Construct the Inward Return Report Query
        String qryInwardReturn =
                        " SELECT DISTINCT (rm.msg_id), rm.utr_no, rm.amount, rm.value_date,             " +
                        "                  m.MSG_SUB_TYPE, OUT.msg_id, OUT.utr_no,                      " +
                        "                  rm.receiver_address AS receiver_address, " +
                        "                  rm.sender_address AS sender_address      " +
                        "           FROM rtgs_message rm, MESSAGE m,                                    " +
                        "                (SELECT rm.parent_id, rm.utr_no, rm.msg_id                     " +
                        "                   FROM rtgs_message rm, MESSAGE m                             " +
                        "                  WHERE rm.msg_id = m.msg_id                                   " +
                        "                    AND rm.tran_type = 'outward'                               " +
                        "                    AND rm.parent_id > 0                                       " +
                        "                    AND m.msg_sub_type IN ('R41', 'R42')                       " +
                        filter +
                        "                    AND rm.business_date = ? ) out                                   " +
                        "          WHERE out.parent_id = rm.msg_id                                      " +
                        "            AND rm.msg_id = m.msg_id                                           " +
                        "            AND rm.business_date = ?                                                 " +
                        "            AND rm.tran_type = 'inward'                                        " +
                        filter;

        return qryInwardReturn;
    }

    /**
     * TODO
     */
    public Message generateR41InwardReport(Message msg) throws Exception{

        Object []obj = (Object[]) msg.info;
        ReportInputDTO inputDto = (ReportInputDTO) obj[0];
        long userIFSCId = ((Long) obj[1]).longValue();
        String userIFSCCode = (String) obj[2];
        boolean isCOReport = false;
        Message res = new Message();
        String inputDate;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (inputDto==null ) {
            throw new BOException ("Invalid Criteria.");
        }

        try {

            if (InstaDefaultConstants.COIFSCCode.trim().equalsIgnoreCase(userIFSCCode)) {
                isCOReport = true;
            }
            //To get the Constructed Query, based on filter and logged in user
            String qryR41InwardReport = getR41InwardReportQuery(inputDto, isCOReport, userIFSCId);
            //To avoid printing queries in jboss console..by priyak.
            //logger.info("Query for generating the R41 Inward Report : " +qryR41InwardReport);
            inputDate = inputDto.getValueDate();
            logger.info("Query for getting R41 inward report for " + inputDate + ": " +qryR41InwardReport);
            ps = con.prepareStatement(qryR41InwardReport);
            ps.setString(1, inputDate);
            rs = ps.executeQuery();
            //To populate/construct the ReportDto list from the result set.
            res.info=generateInwardReportList(rs);
        } catch(Exception e) {

            logger.info("Exception ocurred while genearating R41 Inward Report : " + e.getMessage());
            throw new Exception("Exception while generating R41 Inward Report.");
        } finally {

            release(ps, rs);
        }
        return res;
    }

    /**
     * TODO
     */
    public Message generateR42InwardReport(Message msg) throws Exception{

        Object []obj = (Object[]) msg.info;
        ReportInputDTO inputDto = (ReportInputDTO) obj[0];
        long userIFSCId = ((Long) obj[1]).longValue();
        String userIFSCCode = (String) obj[2];
        boolean isCOReport = false;
        Message res = new Message();
        String inputDate;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (inputDto==null ) {
            throw new BOException ("Invalid Criteria.");
        }

        try {

            if (InstaDefaultConstants.COIFSCCode.trim().equalsIgnoreCase(userIFSCCode)) {
                isCOReport = true;
            }
            //To get the Constructed Query, based on filter and logged in user
            String qryR42InwardReport = getR42InwardReportQuery(inputDto, isCOReport, userIFSCId);
            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the R42 Inward Report : " + qryR42InwardReport);
            logger.info("Query for generating the R42 Inward Report : " + qryR42InwardReport);
            inputDate = inputDto.getValueDate();
            ps = con.prepareStatement(qryR42InwardReport);
            ps.setString(1, inputDate);
            rs = ps.executeQuery();
            //To populate/construct the ReportDto list from the result set.
            res.info=generateInwardReportList(rs);
        } catch(Exception e) {

            logger.info("Exception ocurred while genearating the R42 Inward Report : " + e.getMessage());
            throw new Exception("Exception while generating R42 Inward Report.");
        } finally {

            release(ps, rs);
        }
        return res;
    }

    /**
     * TODO
     */
    public Message generateR41OutwardReport(Message msg) throws Exception{

        Object []obj = (Object[]) msg.info;
        ReportInputDTO inputDto = (ReportInputDTO) obj[0];
        long userIFSCId = ((Long) obj[1]).longValue();
        String userIFSCCode = (String) obj[2];
        boolean isCOReport = false;
        Message res = new Message();
        String inputDate;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (inputDto==null ) {
            throw new BOException ("Invalid Criteria.");
        }

        try {

            if (InstaDefaultConstants.COIFSCCode.trim().equalsIgnoreCase(userIFSCCode)) {
                isCOReport = true;
            }

            //To get the Constructed Query, based on filter and logged in user
            String qryR41OutwardReport = getR41OutwardReportQuery(inputDto, isCOReport, userIFSCId);

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the R41 Outward Report : " +  qryR41OutwardReport);
            logger.info("Query for generating the R41 Outward Report : " +  qryR41OutwardReport);
            inputDate = inputDto.getValueDate();
            ps = con.prepareStatement(qryR41OutwardReport);
            ps.setString(1, inputDate);
            rs = ps.executeQuery();
            //To populate/construct the ReportDto list from the result set.
            res.info=generateOutwardReportList(rs);
        } catch(Exception e) {

            logger.info("Exception ocurred while genearating the R41 Outward Report : " + e.getMessage());
            throw new Exception("Exception while generating R41 Outward Report.");
        } finally {

            release(ps, rs);
        }
        return res;
    }

    /**
     * TODO
     */
    public Message generateR42OutwardReport(Message msg) throws Exception{

        Object []obj = (Object[]) msg.info;
        ReportInputDTO inputDto = (ReportInputDTO) obj[0];
        long userIFSCId = ((Long) obj[1]).longValue();
        String userIFSCCode = (String) obj[2];
        boolean isCOReport = false;
        Message res = new Message();
        String inputDate;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (inputDto==null ) {
            throw new BOException ("Invalid Criteria.");
        }

        try {

            if (InstaDefaultConstants.COIFSCCode.trim().equalsIgnoreCase(userIFSCCode)) {
                isCOReport = true;
            }
            //To get the Constructed Query, based on filter and logged in user
            String qryR42OutwardReport = getR42OutwardReportQuery(inputDto, isCOReport, userIFSCId);

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the R42 Outward Report : " + qryR42OutwardReport);
            logger.info("Query for generating the R42 Outward Report : " + qryR42OutwardReport);
            inputDate = inputDto.getValueDate();
            ps = con.prepareStatement(qryR42OutwardReport);
            ps.setString(1, inputDate);
            rs = ps.executeQuery();
            //To populate/construct the ReportDto list from the result set.
            res.info=generateOutwardReportList(rs);
        } catch(Exception e) {

            logger.info("Exception ocurred while genearating the R42 Outward Report : " + e.getMessage());
            throw new Exception("Exception while generating R42 Outward Report.");
        } finally {

            release(ps, rs);
        }
        return res;
    }

    /**
     * TODO
     */
    public List generateOutwardReportList(ResultSet rs)
    throws BOException {

        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        ReportDTO reportDTO;
        try {

            while (rs.next()) {

                reportDTO = new ReportDTO();
//                reportDTO.setAmount(rs.getDouble("totalamount"));
                reportDTO.setAmt(rs.getString("totalamount"));
                reportDTO.setCount(rs.getInt("count"));
                reportDTO.setReceiverAddress(rs.getString("receiver_address"));
                reportDTO.setValueDate(InstaReportUtil.formatDate(rs.getDate("value_date")));
                reportList.add(reportDTO);
            }
        } catch (SQLException e) {

            logger.info("Exception ocurred while generating the Report List from the ResultSet. " + e.getMessage());
            throw new BOException(e.getMessage());
        }
        return reportList;
    }

    public List generateInwardReportList(ResultSet rs)
    throws BOException {

        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        ReportDTO reportDTO;
        try {

            while (rs.next()) {

                reportDTO = new ReportDTO();
//                reportDTO.setAmount(rs.getDouble("totalamount"));
                reportDTO.setAmt(rs.getString("totalamount"));
                reportDTO.setCount(rs.getInt("count"));
                reportDTO.setSenderAddress(rs.getString("sender_address"));
                reportDTO.setValueDate(InstaReportUtil.formatDate(rs.getDate("value_date")));
                reportList.add(reportDTO);
            }
        } catch (SQLException e) {

            logger.info("Exception ocurred while generating the Report List from the ResultSet. " + e.getMessage());
            throw new BOException(e.getMessage());
        }
        return reportList;
    }

    /**
     * TODO
     */
    public Message generateRTGSReconcillationReports(Message msg) throws Exception{

        ReportInputDTO inputDto = (ReportInputDTO) msg.info;
        Message res = new Message();
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        String inputDate;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (inputDto==null ) {
            throw new BOException ("Invalid Criteria.");
        }

        try {

            //To get the Constructed Query, based on filter and logged in user
            String qryRTGSReconcillationReport = getRTGSReconcillationReportQuery(inputDto);

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the RTGS Reconcillation Report : " + qryRTGSReconcillationReport);
            logger.info("Query for generating the RTGS Reconcillation Report : " + qryRTGSReconcillationReport);
            inputDate = inputDto.getValueDate();
            ps = con.prepareStatement(qryRTGSReconcillationReport);
            ps.setString(1, inputDate);
            rs = ps.executeQuery();

            ReportDTO reportDTO;
            while (rs.next()) {

                reportDTO = new ReportDTO();
//                reportDTO.setAmount(rs.getDouble("totalamount"));
                reportDTO.setAmt(rs.getString("totalamount"));
                reportDTO.setCount(rs.getInt("count"));
                reportDTO.setValueDate(inputDate);
                reportDTO.setMsgType(rs.getString("msgType"));
                reportDTO.setTranType(rs.getString("tranType"));
                reportList.add(reportDTO);
            }
            res.info=reportList;
        } catch(Exception e) {

            logger.info("Exception ocurred while genearating the RTGS Reconcillation Report : " + e.getMessage());
            throw new Exception("Exception while generating RTGS Reconcillation Report.");
        } finally {

            release(ps, rs);
        }
        return res;
    }

    /**
     * Generate a Report to view the Inward Messages which are returned.
     */
    public Message generateRTGSInwardReturnReports(Message msg) throws Exception{

        //Input Parameters
        Object []obj = (Object[]) msg.info;
        ReportInputDTO inputDto = (ReportInputDTO) obj[0];
        long userIFSCId = ((Long) obj[1]).longValue();
        String userIFSCCode = (String) obj[2];

        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        boolean isCOReport = false;
        Message res = new Message();
        String inputDate;

        PreparedStatement ps = null;
        ResultSet rs = null;

        if (inputDto==null ) {
            throw new BOException ("Invalid Criteria.");
        }

        try {

            if (InstaDefaultConstants.COIFSCCode.trim().equalsIgnoreCase(userIFSCCode)) {
                isCOReport = true;
            }

            //To get the Constructed Query, based on filter and logged in user
            String qryInwardReturnReport = getInwardReturnQuery(inputDto, isCOReport, userIFSCId);

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the Inward Return Report : " +qryInwardReturnReport);
            logger.info("Query for generating the Inward Return Report : " +qryInwardReturnReport);
            inputDate = inputDto.getValueDate();
            ps = con.prepareStatement(qryInwardReturnReport);
            ps.setString(1, inputDate);
            ps.setString(2, inputDate);
            rs = ps.executeQuery();

            ReportDTO reportDTO;
            while(rs.next()) {

                reportDTO = new ReportDTO();
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setMsgType(rs.getString("MSG_SUB_TYPE"));
                reportDTO.setValueDate(inputDate);
                reportDTO.setReceiverAddress(rs.getString("Receiver_Address"));
                reportDTO.setSenderAddress(rs.getString("Sender_Address"));
                reportList.add(reportDTO);
            }
            res.info = reportList;
        } catch(Exception e) {

            logger.info("Exception ocurred while genearating the Inward Return Report : " + e.getMessage());
            throw new Exception("Exception while generating Inward Return Report.");
        } finally {

            release(ps, rs);
        }
        return res;
    }

    /**
     * Generate a Report to view the Graduated Payment
     * @param Message
     * @return Message
     */
    public Message generateGraduatedPaymentReport(Message msg)
    throws Exception{

        //Input Parameters
        Object []obj = (Object[]) msg.info;
        ReportInputDTO inputDTO = (ReportInputDTO) obj[0];

        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Message res = new Message();

        if (inputDTO==null ) {
            throw new BOException ("Invalid Criteria.");
        }

        try {

            reportList = getGraduatedPaymentReport(inputDTO);
            res.info = reportList;
        } catch(Exception e) {

            logger.info("Exception ocurred while generating the Graduated Payment Report : " + e.getMessage());
            throw new Exception("Exception while generating Graduated Payment Report.");
        }
        return res;
    }

    /**
     * Method to Query for Graduated Payment Report
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public List<ReportDTO> getGraduatedPaymentReport(ReportInputDTO inputDTO)
    throws BOException {

        String inputDate;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);

        try {

            String qryGraduatedReport = "SELECT UTR_NO, MSG_TYPE, TRAN_TYPE, SENDER_ADDRESS, RECEIVER_ADDRESS, DBT_CRD, " +
                                        "TXN_AMT, BALANCE FROM RTGSBALTRAN WHERE TXN_DATE = ? ORDER BY ID DESC";

            //inputDate = inputDTO.getValueDate();
            inputDate = InstaReportUtil.reportDisplayDateFormat(inputDTO.getValueDate());
            //inputDTO.setValueDate(InstaReportUtil.reportDisplayDateFormat(inputDate)); //For date formate by priyak
            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Generating Graduated Payment Report : " + qryGraduatedReport+" " +"for Value Date :"+inputDate);
            logger.info("Generating Graduated Payment Report : " + qryGraduatedReport+" " +"for Value Date :"+inputDate);
            ps = con.prepareStatement(qryGraduatedReport);
            ps.setString(1, ConversionUtil.getFormat(inputDate));
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
//                reportDTO.setAmount(rs.getDouble("TXN_AMT"));
                reportDTO.setAmt(rs.getString("TXN_AMT"));
//                reportDTO.setBalance(rs.getDouble("BALANCE"));
                reportDTO.setBalance(rs.getString("BALANCE"));
                reportList.add(reportDTO);
            }
        } catch(Exception e) {

            logger.error("Exception while generating Graduated Payment report :"+e);
            throw new BOException("Exception while generating Graduated Payment Report :"+e);
        } finally {

            release(ps, rs);
        }
        return reportList;
    }

    /**
     * Method to Query for RTGS Inward Possible Return Report
     * @param ReportInputDTO
     * @return List<ReportDTO>
     * @author Eswaripriyak.
     */
    public Message generateRTGSInwardPossibleReturnReport(Message msg)
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

            String qryRTGSInwardReturnReport = "   SELECT UTR_NO,TRAN_TYPE,AMOUNT,SENDER_ADDRESS,RECEIVER_ADDRESS,VALUE_DATE,BUSINESS_DATE " +
                                               "             FROM RTGS_MESSAGE                                                             " +
                                               "                   WHERE BUSINESS_DATE = ? AND AUTORETURN = 1  AND STATUS = 900            ";

            inputDate = InstaReportUtil.reportDisplayDateFormat(inputDTO.getValueDate());
            logger.info("Generating RTGS Inward Possible Return Report : " + qryRTGSInwardReturnReport+" " +"for Value Date :"+inputDate);
            ps = con.prepareStatement(qryRTGSInwardReturnReport);
            ps.setString(1, ConversionUtil.getFormat(inputDate));
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

    /**
     * Method to Generate Individual Transaction Details Reports.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateIndividualTXNDetailsReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Map<String, List<ReportDTO>> reportMap =
                    new LinkedHashMap<String, List<ReportDTO>>();
        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());
        try {

            //Report Query.
            String qryBrIndividualReport =
                " SELECT ifsc_master_id, im.ifsc ||'-'|| im.NAME as Branch, utr_no, " +
                "        msg_sub_type, tran_type, rm.sender_address, msg_source, m.msg_id, " +
                "        receiver_address, business_date, amount, rs.name as status " +
                "   FROM rtgs_message rm, MESSAGE m, ifscmaster im, rtgs_status rs " ;

            String whereBlock =
                "  WHERE m.msg_id = rm.msg_id " +
                "       AND im.ID = rm.ifsc_master_id " +
                "       AND m.msg_sub_type in ('R42','R41', 'R43', 'R44', 'R10') " +
                "       AND rs.ID = rm.STATUS   ";

            String orderBY =
                " ORDER BY business_date asc, " +
                "          m.msg_id asc ";

            /*
             *  Build the filter condition based on the input given by the user.
             */
            /**
             * Done on on 07-Jan-2011.
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               qryBrIndividualReport =
                " SELECT ifsc_master_id, im.ifsc ||'-'|| im.NAME as Branch, utr_no, " +
                "        msg_sub_type, tran_type, rm.sender_address, msg_source, m.msg_id, " +
                "        receiver_address, business_date, amount, rs.name as status " +
                "   FROM rtgs_message_vw rm, MESSAGE_vw m, ifscmaster im, rtgs_status rs " ;

               logger.info("Data fetching from archive database schema..");

                } else {

                    logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                    logger.info("Data fetching from Local Database schema...");
                }


            /*
             *  Related to Transaction Date.
             *  Assumption : ToDate should be greater than FromDate.
             */
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                whereBlock += " and rm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
            }

            //Related to Transaction Type.
            if (inputDTO.getTransactionType() != null && !inputDTO.getTransactionType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and rm.TRAN_TYPE = '" + inputDTO.getTransactionType() +"'" ;
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
//            if (inputDTO.getFromAmount() > 0 && inputDTO.getToAmount() > 0 ) {
            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {

                whereBlock += " and rm.AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and rm.AMOUNT >= " + inputDTO.getFromAmount();
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and rm.AMOUNT <= " + inputDTO.getToAmount();
            }

            //Related to Message Status Specific.
            if (inputDTO.getStatus() != null && !inputDTO.getStatus()
                                                        .equalsIgnoreCase("ALL")) {

                whereBlock += " and rm.STATUS in (" + inputDTO.getStatus() +")" ;
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryBrIndividualReport += whereBlock + orderBY;


            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the Individual TXN Details Report : "+ qryBrIndividualReport);
            logger.info("Query for generating the Individual TXN Details Report : "+ qryBrIndividualReport);
            ps = con.prepareStatement(qryBrIndividualReport);
            rs = ps.executeQuery();

            String key = "txnDetails";

            while(rs.next()) {

                reportDTO = new ReportDTO();

                //Populate ReportDTO
                reportDTO.setBranch(rs.getString("BRANCH"));
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("BUSINESS_DATE")));
                reportDTO.setSource(rs.getString("MSG_SOURCE"));
                reportDTO.setMsgType(rs.getString("MSG_SUB_TYPE"));
                String tranType = "Outward";
                if(rs.getString("TRAN_TYPE").equalsIgnoreCase("Inward")) {
                    tranType = "Inward";
                }
                reportDTO.setTranType(tranType);
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setStatus(rs.getString("STATUS"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));

                reportList.add(reportDTO);
            }

            //Add the List in the Map. Since it is generic for the report page..
            reportMap.put(key, reportList);

        } catch(Throwable th) {

            logger.error("Exception while generating Individual TXN Details Report :"
                         + th.getMessage());
            throw new Exception("Exception while generating Individual TXN Details Report."
                                + th);
        } finally {

            release(ps, rs);
        }
        req.info = reportMap;
        return req;
    }

    /**
     * Method to Generate Branch Individual Reports.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateBrIndividualReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Map<String, List<ReportDTO>> reportMap =
                    new LinkedHashMap<String, List<ReportDTO>>();
        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());
        try {

            //Report Query.
            String qryBrIndividualReport =
                " SELECT ifsc_master_id, im.ifsc ||'-'|| im.NAME as Branch, utr_no, " +
                "        msg_sub_type, tran_type, rm.sender_address, msg_source, m.msg_id, " +
                "        receiver_address, value_date, amount, rs.name as status " +
                "   FROM rtgs_message rm, MESSAGE m, ifscmaster im, rtgs_status rs " ;

            String whereBlock =
                "  WHERE m.msg_id = rm.msg_id " +
                "       AND im.ID = rm.ifsc_master_id " +
                "       AND m.msg_sub_type in ('R42','R41', 'R43', 'R44', 'R10') " +
                "       AND rs.ID = rm.STATUS   ";

            String orderBY =
                " ORDER BY ifsc asc, " +
                "          business_date asc, " +
                "          m.msg_id asc ";

            /**
             * Done on on 07-Jan-2011.
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               qryBrIndividualReport =
                " SELECT ifsc_master_id, im.ifsc ||'-'|| im.NAME as Branch, utr_no, " +
                "        msg_sub_type, tran_type, rm.sender_address, msg_source, m.msg_id, " +
                "        receiver_address, value_date, amount, rs.name as status " +
                "   FROM rtgs_message_vw rm, MESSAGE_vw m, ifscmaster im, rtgs_status rs " ;

                    logger.info("Data fetching from archive database schema..");

                } else {

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                    logger.info("Data fetching from Local Database schema...");
                }

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

            //Related to Transaction Type.
            if (inputDTO.getTransactionType() != null && !inputDTO.getTransactionType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and rm.TRAN_TYPE = '" + inputDTO.getTransactionType() +"'" ;
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
//            if (inputDTO.getFromAmount() > 0 && inputDTO.getToAmount() > 0 ) {
            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {

                whereBlock += " and rm.AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {
                whereBlock += " and rm.AMOUNT >= " + inputDTO.getFromAmount();
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {
//            } else if (inputDTO.getToAmount() > 0) {
                whereBlock += " and rm.AMOUNT <= " + inputDTO.getToAmount();
            }

            //Related to Message Status Specific.
            if (inputDTO.getStatus() != null && !inputDTO.getStatus()
                                                        .equalsIgnoreCase("ALL")) {

                whereBlock += " and rm.STATUS in (" + inputDTO.getStatus() +")" ;
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryBrIndividualReport += whereBlock + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the Branch Individual Report : "+ qryBrIndividualReport);
            logger.info("Query for generating the Branch Individual Report : "+ qryBrIndividualReport);
            ps = con.prepareStatement(qryBrIndividualReport);
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

                //Populate ReportDTO
                reportDTO.setBranch(currBranch);
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("Value_date")));
                reportDTO.setSource(rs.getString("MSG_SOURCE"));
                reportDTO.setMsgType(rs.getString("MSG_SUB_TYPE"));
                String tranType = "Outward";
                if(rs.getString("TRAN_TYPE").equalsIgnoreCase("Inward")) {
                    tranType = "Inward";
                }
                reportDTO.setTranType(tranType);
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setStatus(rs.getString("STATUS"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));
                reportList.add(reportDTO);

                //Set the Current Branch to the Previous Branch.
                prevBranch = currBranch;
            }

            /*
             * This is to set the set of List.
             */
            if (reportList.size()>0){
                reportMap.put(prevBranch, reportList);
            }
        } catch(Throwable th) {

            logger.error("Exception while generating Branch Individual Report :"
                         + th.getMessage());
            throw new Exception("Exception while generating Branch Individual Report."
                                + th);
        } finally {

            release(ps, rs);
        }
        req.info = reportMap;
        return req;
    }

    /**
     * Method to Generate Branch Summary Reports.
     *
     * @param ReportInputDTO
     * @return Map<String, List<ReportDTO>>
     */
    public Message generateBrSummaryReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Map<String, List<ReportDTO>> reportMap =
                    new LinkedHashMap<String, List<ReportDTO>>();

        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());

        try {

            //Report Query.
            String qryBrSummaryReport =
                " SELECT  im.ifsc || '-' || im.NAME as branch, value_date, " +
                "         msg_source, tran_type, COUNT(*) as count, " +
                "         TO_CHAR(SUM (TO_NUMBER(amount))) as TotAmount, rs.name as status, rm.business_date " +
                "   FROM rtgs_message rm, MESSAGE m, ifscmaster im, rtgs_status rs ";

            String whereBlock =
                " WHERE m.msg_id = rm.msg_id " +
                "       AND im.ID = rm.ifsc_master_id " +
                "       AND m.msg_sub_type IN ('R42', 'R41', 'R43', 'R44', 'R10')" +
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



            /**
             * Done on on 07-Jan-2011.
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               qryBrSummaryReport =
                " SELECT  im.ifsc || '-' || im.NAME as branch, value_date, " +
                "         msg_source, tran_type, COUNT(*) as count, " +
                "         TO_CHAR(SUM (TO_NUMBER(amount))) as TotAmount, rs.name as status, rm.business_date " +
                "   FROM rtgs_message_vw rm, MESSAGE_vw m, ifscmaster im, rtgs_status rs ";

                  logger.info("Data fetching from archive database schema..");

                } else {

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                    logger.info("Data fetching from Local Database schema...");
                }

            /*
             *  Related to Transaction Date.
             *  Assumption : ToDate should be greater than FromDate.
             */
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                whereBlock += " and rm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
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
//                if (inputDTO.getFromAmount() > 0 && inputDTO.getToAmount() > 0 ) {

                whereBlock += " and rm.AMOUNT BETWEEN " + inputDTO.getFromAmount() +
                                                " and " + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {
                whereBlock += " and rm.AMOUNT >= " + inputDTO.getFromAmount();
//            } else if (inputDTO.getToAmount() > 0) {
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
            //logger.info("Query for generating the Branch Summary Report : " + qryBrSummaryReport);
            logger.info("Query for generating the Branch Summary Report : " + qryBrSummaryReport);
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
//                reportDTO.setAmount(rs.getDouble("TotAMOUNT"));
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
     * Method to Generate Branch Inward Returned Reports.
     *
     * @param ReportInputDTO
     * @return Map<String, List<ReportDTO>>
     */
    public Message generateBrInwReturnedReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Map<String, List<ReportDTO>> reportMap =
            new LinkedHashMap<String, List<ReportDTO>>();

        try {

            //Report Query.
            String qryBrInwardReturnedReport =
                "  SELECT r.value_date, r.msg_sub_type as MSG_Type, r.amount, " +
                "         r.utr_no as Inw_UTR_No, r.sender_address, r.receiver_address" +
                "         , r1.utr_no as Out_UTR_No, r.business_date  " +
                "      FROM (SELECT value_date, msg_sub_type, status, rm.msg_id, ifsc_master_id,  " +
                "                   sender_address, receiver_address, utr_no, amount, business_date " +
                "               FROM rtgs_message rm, MESSAGE m, ifscmaster im " +
                "              WHERE m.msg_id = rm.msg_id " +
                "                    AND im.ID = rm.ifsc_master_id " +
                "                    AND m.msg_sub_type IN ('R42', 'R41') " +
                //Below 2 conditions ment for the Inward Return - Status 900 is Inward Returned.
                "                    AND rm.status = 900 " +
                "                    AND rm.tran_type = 'inward' " +
                "            ORDER BY business_date ASC) r, " +
                "           (SELECT rm1.parent_id, rm1.utr_no, m1.msg_id as outward_id " +
                "               FROM rtgs_message rm1, MESSAGE m1 " +
                "              WHERE rm1.parent_id IS NOT NULL " +
                "                    AND m1.msg_sub_type IN ('R42', 'R41') " +
                "                    AND rm1.tran_type = 'outward' " +
                "                    AND m1.msg_id = rm1.msg_id) r1 ";

            String whereBlock =
                " WHERE msg_id = parent_id ";

            String orderBY =
                " ORDER BY r.business_date asc," +
                "          r.msg_id asc ";

            /*
             *  Build the filter condition based on the input given by the user.
             */

            /*
             *  Related to Transaction Date.
             *  Assumption : ToDate should be greater than FromDate.
             */
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                whereBlock += " and r.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               qryBrInwardReturnedReport =
                "  SELECT r.value_date, r.msg_sub_type as MSG_Type, r.amount, " +
                "         r.utr_no as Inw_UTR_No, r.sender_address, r.receiver_address" +
                "         , r1.utr_no as Out_UTR_No, r.business_date  " +
                "      FROM (SELECT value_date, msg_sub_type, status, rm.msg_id, ifsc_master_id,  " +
                "                   sender_address, receiver_address, utr_no, amount, business_date " +
                "               FROM rtgs_message_vw rm, MESSAGE_vw m, ifscmaster im " +
                "              WHERE m.msg_id = rm.msg_id " +
                "                    AND im.ID = rm.ifsc_master_id " +
                "                    AND m.msg_sub_type IN ('R42', 'R41') " +
                //Below 2 conditions ment for the Inward Return - Status 900 is Inward Returned.
                "                    AND rm.status = 900 " +
                "                    AND rm.tran_type = 'inward' " +
                "            ORDER BY business_date ASC) r, " +
                "           (SELECT rm1.parent_id, rm1.utr_no, m1.msg_id as outward_id " +
                "               FROM rtgs_message_vw rm1, MESSAGE_vw m1 " +
                "              WHERE rm1.parent_id IS NOT NULL " +
                "                    AND m1.msg_sub_type IN ('R42', 'R41') " +
                "                    AND rm1.tran_type = 'outward' " +
                "                    AND m1.msg_id = rm1.msg_id) r1 ";
                }
            }

            //Related to Message Sub Type.
            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and r.MSG_SUB_TYPE = '" + inputDTO.getPaymentType() +"'" ;
            }

            //Related to Branch/IFSC Specific.
            if (inputDTO.getIfscId() > 0 ) {

                whereBlock += " and IFSC_Master_ID = " + inputDTO.getIfscId();
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryBrInwardReturnedReport += whereBlock + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the Branch Inward Returned Report : "+ qryBrInwardReturnedReport);
            logger.info("Query for generating the Branch Inward Returned Report : "+ qryBrInwardReturnedReport);
            ps = con.prepareStatement(qryBrInwardReturnedReport);
            rs = ps.executeQuery();

            String prevDate = "";

            while(rs.next()) {

                String currDate = InstaReportUtil.
                                    indianFormatDate(rs.getDate("Business_date"));

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
     * Method to Generate Branch Outward Returned by Receiver Reports.
     *
     * report not needed. as of now it is droped. this report will not work.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateBrOutReturnedByReceiverReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);

        try {

            //Report Query.
//TODO QUERY
            String qryBrOutwardReturnedByReceiverReport =
                "SELECT UTR_NO, MSG_TYPE, TRAN_TYPE, SENDER_ADDRESS," +
                "       RECEIVER_ADDRESS, TXN_AMT, BALANCE " +
                "   FROM RTGSBALTRAN ";
            String whereBlock = " WHERE TXN_DATE between ?  and  ? ";
            String orderBY = " ORDER BY ID DESC";

            /*
             *  Build the filter condition based on the input given by the user.
             */

            /*
             *  Related to Transaction Date.
             *  Assumption : ToDate should be greater than FromDate.
             */
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                whereBlock += " and BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
            }

            //Related to Message Sub Type.
            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and MSG_SUB_TYPE = '" + inputDTO.getPaymentType() +"'" ;
            }

            //Related to Branch/IFSC Specific.
            if (inputDTO.getIfscId() > 0 ) {

                whereBlock += " and IFSC_Master_ID = " + inputDTO.getIfscId();
            }

            //Related to Message Status Specific.
            if (inputDTO.getStatus() != null && !inputDTO.getStatus()
                                                        .equalsIgnoreCase("ALL")) {

                whereBlock += " and rm.STATUS in (" + inputDTO.getStatus() +")" ;
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryBrOutwardReturnedByReceiverReport += whereBlock + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the Branch Individual Report : "+ qryBrOutwardReturnedByReceiverReport);
            logger.info("Query for generating the Branch Individual Report : "+ qryBrOutwardReturnedByReceiverReport);
            ps = con.prepareStatement(qryBrOutwardReturnedByReceiverReport);
//TODO set the values.
            ps.setString(1, "");
            rs = ps.executeQuery();

            while(rs.next()) {
//TODO Fetch and set values based on query
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
                reportDTO.setValueDate("");
//                reportDTO.setAmount(rs.getDouble("TXN_AMT"));
                reportDTO.setAmt(rs.getString("TXN_AMT"));
//                reportDTO.setBalance(rs.getDouble("BALANCE"));
                reportDTO.setBalance(rs.getString("BALANCE"));
                reportList.add(reportDTO);
            }
        } catch(Throwable th) {

            logger.error("Exception while generating Branch Summary Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating Branch Summary Report."
                                , th);
        } finally {

            release(ps, rs);
        }
        req.info = reportList;
        return req;
    }

    /**
     * Method to Generate Inward Bank wise Reports.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateInwardBankwiseReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Map<String, List<ReportDTO>> reportMap =
                    new LinkedHashMap<String, List<ReportDTO>>();
        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());
        try {

            //Report Query.
            String qryInwardBankwiseReport =
                " SELECT value_date, msg_sub_type, utr_no, sender_address, amount, r.msg_id, " +
                "        receiver_address, code||'-'||name as code, sender_ifsc_id " +
                "    FROM (SELECT   value_date, msg_sub_type, utr_no, sender_address, rm.business_date, " +
                "                   receiver_address, amount, rm.msg_id, ifsc_master_id " +
                "             FROM rtgs_message rm, MESSAGE m, ifscmaster im " +
                "            WHERE m.msg_id = rm.msg_id " +
                "                   AND im.ID = rm.ifsc_master_id " +
                "                   AND rm.tran_type = 'inward' " +
                "                   AND m.msg_sub_type IN ('R41', 'R42', 'R43', 'R44') " +
                "           ORDER BY business_date ASC, rm.msg_id) r, " +
                "         (SELECT m.msg_id, bm.code, bm.name, im.ID as sender_ifsc_id " +
                "             FROM rtgs_message rm, ifscmaster im, MESSAGE m, bankmaster bm " +
                "            WHERE m.msg_id = rm.msg_id " +
                "                  AND rm.sender_address LIKE im.ifsc || '%' " +
                "                  AND rm.tran_type = 'inward' " +
                "                  AND m.msg_sub_type IN ('R41', 'R42', 'R43', 'R44') " +
                "                  AND bm.ID = im.bank_master_id) r1 ";

            String whereBlock =
                " WHERE r.msg_id = r1.msg_id ";

            String orderBY =
                " ORDER BY code asc, " +
                "          business_date asc, " +
                "          sender_address asc, " +
                "          r.msg_id asc ";
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                       qryInwardBankwiseReport =
                        " SELECT value_date, msg_sub_type, utr_no, sender_address, amount, r.msg_id, " +
                        "        receiver_address, code||'-'||name as code, sender_ifsc_id " +
                        "    FROM (SELECT   value_date, msg_sub_type, utr_no, sender_address, rm.business_date, " +
                        "                   receiver_address, amount, rm.msg_id, ifsc_master_id " +
                        "             FROM rtgs_message_vw rm, MESSAGE_vw m, ifscmaster im " +
                        "            WHERE m.msg_id = rm.msg_id " +
                        "                   AND im.ID = rm.ifsc_master_id " +
                        "                   AND rm.tran_type = 'inward' " +
                        "                   AND m.msg_sub_type IN ('R41', 'R42', 'R43', 'R44') " +
                        "           ORDER BY business_date ASC, rm.msg_id) r, " +
                        "         (SELECT m.msg_id, bm.code, bm.name, im.ID as sender_ifsc_id " +
                        "             FROM rtgs_message_vw rm, ifscmaster im, MESSAGE_vw m, bankmaster bm " +
                        "            WHERE m.msg_id = rm.msg_id " +
                        "                  AND rm.sender_address LIKE im.ifsc || '%' " +
                        "                  AND rm.tran_type = 'inward' " +
                        "                  AND m.msg_sub_type IN ('R41', 'R42', 'R43', 'R44') " +
                        "                  AND bm.ID = im.bank_master_id) r1 ";

                       logger.info("Data fetching from Archive Database schema...");

                } else {

                    logger.info("Data fetching from Local Database schema...");
                }
            } else {

                logger.info("Data fetching from Local Database schema...");
            }



            /*
             *  Build the filter condition based on the input given by the user.
             */

            /*
             *  Related to Transaction Date.
             *  Assumption : ToDate should be greater than FromDate.
             */
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                whereBlock += " and r.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
            }

            //Related to Message Sub Type.
            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and MSG_SUB_TYPE = '" + inputDTO.getPaymentType() + "'";
            }

            //Related to Inward Receiver Branch/IFSC Specific.
            if (inputDTO.getReceiverIfscId() > 0 ) {

                whereBlock += " and IFSC_Master_ID = " + inputDTO.getReceiverIfscId();
            }

            //Related to Inward Sender Bank Specific.
            if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                            .equalsIgnoreCase("ALL") ) {

                whereBlock += " and CODE = '" + inputDTO.getSenderBank() + "'";
            }

            //Related to Inward Sender Branch/IFSC Specific.
            if (inputDTO.getSenderIfscId() > 0 ) {

                whereBlock += " and sender_ifsc_id = " + inputDTO.getSenderIfscId();
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
//            if (inputDTO.getFromAmount() > 0 && inputDTO.getToAmount() > 0 ) {
                whereBlock += " and AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and AMOUNT >= " + inputDTO.getFromAmount();
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and AMOUNT <= " + inputDTO.getToAmount();
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryInwardBankwiseReport += whereBlock + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the Inward Bank wise Report : "+ qryInwardBankwiseReport);
            logger.info("Query for generating the Inward Bank wise Report : "+ qryInwardBankwiseReport);
            ps = con.prepareStatement(qryInwardBankwiseReport);
            rs = ps.executeQuery();

            String prevBank = "";

            while(rs.next()) {

                String currBank = rs.getString("Code");

                /*
                 * This is to group the record based on the CP Bank Wise.
                 */
                if (prevBank != "" && !prevBank.equalsIgnoreCase(currBank)) {

                    reportMap.put(prevBank, reportList);
                    reportList = new ArrayList<ReportDTO>(0);
                }

                reportDTO = new ReportDTO();

                //Populate the ReportDTO.
                reportDTO.setBank(currBank);
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("VALUE_DATE")));
                reportDTO.setMsgType(rs.getString("MSG_SUB_TYPE"));
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));
                reportList.add(reportDTO);

                //Set the Current Bank to the Previous Bank
                prevBank = currBank;
            }

            /*
             * This is to set the Last set of List.
             */
            if (reportList.size()>0){
                reportMap.put(prevBank, reportList);
            }
        } catch(Throwable th) {

            logger.error("Exception while generating Inward Bankwise Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating Inward Bankwise Report."
                                , th);
        } finally {

            release(ps, rs);
        }
        req.info = reportMap;
        return req;
    }

    /**
     * Method to Generate Outward Bank wise Reports.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateOutwardBankwiseReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        Map<String, List<ReportDTO>> reportMap =
                    new LinkedHashMap<String, List<ReportDTO>>();

        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());
        try {

            //Report Query.
            String qryOutwardBankwiseReport =
                " SELECT   value_date, msg_sub_type, utr_no, sender_address, amount, r.msg_id, " +
                "          receiver_address, code||'-'||name as code, receiver_ifsc_id " +
                "    FROM (SELECT   value_date, msg_sub_type, utr_no, sender_address, rm.business_date, " +
                "                   receiver_address, amount, rm.msg_id, ifsc_master_id " +
                "             FROM rtgs_message rm, MESSAGE m, ifscmaster im " +
                "            WHERE m.msg_id = rm.msg_id " +
                "                   AND im.ID = rm.ifsc_master_id " +
                "                   AND rm.tran_type = 'outward' " +
                "                   AND m.msg_sub_type IN ('R41', 'R42', 'R10') " +
                "           ORDER BY business_date ASC, rm.msg_id) r, " +
                "         (SELECT m.msg_id, bm.code, bm.name, im.ID as receiver_ifsc_id " +
                "             FROM rtgs_message rm, ifscmaster im, MESSAGE m, bankmaster bm " +
                "            WHERE m.msg_id = rm.msg_id " +
                "                  AND rm.receiver_address LIKE im.ifsc || '%' " +
                "                  AND rm.tran_type = 'outward' " +
                "                  AND m.msg_sub_type IN ('R41', 'R42', 'R10') " +
                "                  AND bm.ID = im.bank_master_id) r1 ";

            String whereBlock =
                " WHERE r.msg_id = r1.msg_id ";

            String orderBY =
                " ORDER BY code asc,  " +
                "          business_date asc, " +
                "          receiver_address asc, " +
                "          r.msg_id asc ";


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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               qryOutwardBankwiseReport =
                " SELECT   value_date, msg_sub_type, utr_no, sender_address, amount, r.msg_id, " +
                "          receiver_address, code||'-'||name as code, receiver_ifsc_id " +
                "    FROM (SELECT   value_date, msg_sub_type, utr_no, sender_address, rm.business_date, " +
                "                   receiver_address, amount, rm.msg_id, ifsc_master_id " +
                "             FROM rtgs_message_vw rm, MESSAGE_vw m, ifscmaster im " +
                "            WHERE m.msg_id = rm.msg_id " +
                "                   AND im.ID = rm.ifsc_master_id " +
                "                   AND rm.tran_type = 'outward' " +
                "                   AND m.msg_sub_type IN ('R41', 'R42', 'R10') " +
                "           ORDER BY business_date ASC, rm.msg_id) r, " +
                "         (SELECT m.msg_id, bm.code, bm.name, im.ID as receiver_ifsc_id " +
                "             FROM rtgs_message_vw rm, ifscmaster im, MESSAGE_vw m, bankmaster bm " +
                "            WHERE m.msg_id = rm.msg_id " +
                "                  AND rm.receiver_address LIKE im.ifsc || '%' " +
                "                  AND rm.tran_type = 'outward' " +
                "                  AND m.msg_sub_type IN ('R41', 'R42', 'R10') " +
                "                  AND bm.ID = im.bank_master_id) r1 ";

                        logger.info("Data fetching from archive database schema..");

                } else {

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                    logger.info("Data fetching from Local Database schema...");
                }

            /*
             *  Build the filter condition based on the input given by the user.
             */

            /*
             *  Related to Transaction Date.
             *  Assumption : ToDate should be greater than FromDate.
             */
            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                whereBlock += " and r.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
            }

            //Related to Message Sub Type.
            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and MSG_SUB_TYPE = '" + inputDTO.getPaymentType() +"'" ;
            }

            //Related to Outward Sender Branch/IFSC Specific.
            if (inputDTO.getSenderIfscId() > 0 ) {

                whereBlock += " and IFSC_Master_ID = " + inputDTO.getSenderIfscId();
            }

            //Related to Outward Receiver Bank Specific.
            if (inputDTO.getReceiverBank() !=null && !inputDTO.getReceiverBank()
                                                            .equalsIgnoreCase("ALL") ) {

                whereBlock += " and CODE = '" + inputDTO.getReceiverBank()+"'";
            }

            //Related to Outward Receiver Branch/IFSC Specific.
            if (inputDTO.getReceiverIfscId() > 0 ) {

                whereBlock += " and receiver_ifsc_id = " + inputDTO.getReceiverIfscId();
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

                whereBlock += " and AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and AMOUNT >= " + inputDTO.getFromAmount();
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and AMOUNT <= " + inputDTO.getToAmount();
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryOutwardBankwiseReport += whereBlock + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the Outward Bank wise Report : "+ qryOutwardBankwiseReport);
            logger.info("Query for generating the Outward Bank wise Report : "+ qryOutwardBankwiseReport);
            ps = con.prepareStatement(qryOutwardBankwiseReport);
            rs = ps.executeQuery();


            String prevBank = "";

            while(rs.next()) {

                String currBank = rs.getString("Code");

                /*
                 * This is to group the record based on the CP Bank Wise.
                 */
                if (prevBank != "" && !prevBank.equalsIgnoreCase(currBank)) {

                    reportMap.put(prevBank, reportList);
                    reportList = new ArrayList<ReportDTO>(0);
                }

                reportDTO = new ReportDTO();

                //Populate the ReportDTO.
                reportDTO.setBank(currBank);
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("VALUE_DATE")));
                reportDTO.setMsgType(rs.getString("MSG_SUB_TYPE"));
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportList.add(reportDTO);

                //Set the Current Bank to the Previous Bank
                prevBank = currBank;
            }

            /*
             * This is to set the Last set of List.
             */
            if (reportList.size()>0){
                reportMap.put(prevBank, reportList);
            }
        } catch(Throwable th) {

            logger.error("Exception while generating Outward Bankwise Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating Outward Bankwise Report."
                                , th);
        } finally {

            release(ps, rs);
        }
        req.info = reportMap;
        return req;
    }

    /**
     * Method to Generate UTR No wise Reports.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateUTRNowiseReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        CMsgDTO msgDTO = new CMsgDTO();

        try {

            //Report Query.
            /**
             * Changed for archival data is not displaying in the RTGS Report, 20121211, By Joe. M
             */
            String qryUTRNoWiseReport;
            if (inputDTO.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    qryUTRNoWiseReport = "SELECT rm.*, MSG_TYPE, MSG_SUB_TYPE " +
                                            "   FROM RTGS_MESSAGE_VW rm, Message_VW m ";

                    logger.info("Data fetching from archive database schema-FirstBlock..");
                } else {

                    qryUTRNoWiseReport = "SELECT rm.*, MSG_TYPE, MSG_SUB_TYPE " +
                                            "   FROM RTGS_MESSAGE rm, Message m ";

                    logger.info("Data fetching from Local Database schema-SecondBlock...");
                }
            } else {

                qryUTRNoWiseReport =  " SELECT rm.*, MSG_TYPE, MSG_SUB_TYPE " +
                                        " FROM RTGS_MESSAGE rm, Message m ";

                logger.info("Data fetching from Local Database schema-ThirdBlock...");
            }

            String whereBlock =
                " WHERE rm.MSG_ID = m.MSG_ID AND m.MSG_SUB_TYPE NOT IN ('R09','R90')" +
                "       AND UTR_NO = ? ";

            String orderBy =
                " ORDER BY rm.MSG_ID DESC ";

            /**
             * Done on on 08-Jan-2011.
             *
             * Separated database calls to generate reports based on data available
             * in local/archive schema.
             *
             * We need to Check UTR is avaiable in Local Database schema . If its avaiable then we can genrate and Display .
             * If its not there in Local Database schema then we have to look for Archive Database so called VIEW
             *
             */
            String qryUTRNoWiseReportFromView = "SELECT rm.*, MSG_TYPE, MSG_SUB_TYPE " +
                                                "FROM RTGS_MESSAGE_VW rm, Message_VW m ";

            //Related to Branch/IFSC Specific.
            if (inputDTO.getIfscId() > 0 ) {

                whereBlock += " and IFSC_Master_ID = " + inputDTO.getIfscId();
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryUTRNoWiseReport += whereBlock + orderBy;
            qryUTRNoWiseReportFromView += whereBlock + orderBy;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the UTRNo wise Report : "+ qryUTRNoWiseReport);
            logger.info("Query for generating the UTRNo wise Report from local database schema : "+ qryUTRNoWiseReport);
            logger.info("Query for generating the UTRNo wise Report from Archive database schema : "+ qryUTRNoWiseReportFromView);
            ps = con.prepareStatement(qryUTRNoWiseReport);
            //18-JAN-2011 The given UTR number should be compared asit is . upper case conversion is not needed.
            //ps.setString(1, inputDTO.getUtrNo().toUpperCase());
            ps.setString(1, inputDTO.getUtrNo());
            rs = ps.executeQuery();

            //Checking whether user input UTR NO is available in local database schema..
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
//                msgDTO.amount           = rs.getDouble("AMOUNT");
                msgDTO.amt              = rs.getString("AMOUNT");
                msgDTO.tranType         = rs.getString("TRAN_TYPE");
                msgDTO.entryBy          = rs.getString("ENTRY_BY");
                msgDTO.passBy           = rs.getString("PASS_BY");
                msgDTO.remarks          = rs.getString("REMARKS") == null ? "" : rs.getString("REMARKS");
                msgDTO.errorRemarks     = rs.getString("ERROR_REMARKS") == null ? "" : rs.getString("ERROR_REMARKS");

                InstaRTGSMessageBO bo = new InstaRTGSMessageBO();
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

                    InstaRTGSMessageBO bo = new InstaRTGSMessageBO();
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

            logger.error("Exception while generating UTRNo wise Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating UTRNo wise Report."
                                , th);
        } finally {

            release(ps1,rs1);
        }
        req.info = msgDTO;
        return req;
    }


    /**
     * Method to Generate Reconcilliation CounterPatry wise Reports.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateReconcilliationReportCPwise(Message req)
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
            String qryReconcilliationCPWiseReport =
                " SELECT im.ifsc||'-'||im.NAME as BRANCH, value_date, tran_type," +
                "        msg_type||'-'||msg_sub_type as MSG_TYPE, amount, utr_no," +
                "        receiver_address, rm.sender_address, bm.CODE " +
                "    FROM rtgs_message rm, MESSAGE m, ifscmaster im, bankmaster bm ";

            String whereBlock =
                "   WHERE m.msg_id = rm.msg_id " +
                "         AND ( rm.SENDER_ADDRESS like im.IFSC || '%' or " +
                "               rm.RECEIVER_ADDRESS like im.IFSC || '%') " +
                "         AND m.msg_sub_type in ('R42', 'R41', 'R10', 'R43', 'R44') " +
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               qryReconcilliationCPWiseReport =
                " SELECT im.ifsc||'-'||im.NAME as BRANCH, value_date, tran_type," +
                "        msg_type||'-'||msg_sub_type as MSG_TYPE, amount, utr_no," +
                "        receiver_address, rm.sender_address, bm.CODE " +
                "    FROM rtgs_message_vw rm, MESSAGE_vw m, ifscmaster im, bankmaster bm ";
                }
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
                whereBlock += " and rm.AMOUNT BETWEEN " + inputDTO.getFromAmount()
                                              + " and " + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and rm.AMOUNT >= " + inputDTO.getFromAmount();
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {
                whereBlock += " and rm.AMOUNT <= " + inputDTO.getToAmount();
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryReconcilliationCPWiseReport += whereBlock + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the CPwise Reconcilliation Report : "+ qryReconcilliationCPWiseReport);
            logger.info("Query for generating the CPwise Reconcilliation Report : "+ qryReconcilliationCPWiseReport);
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

            logger.error("Exception while generating CPwise Reconcilliation Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating CPwise Reconcilliation Report."
                                , th);
        } finally {

            release(ps, rs);
        }

        req.info = reportMap;
        return req;
    }

    /**
     * Method to Generate Reconcilliation Host Branch wise Reports.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateReconcilliationReportBranchwise(Message req)
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
            String qryReconcilliationBranchWiseReport =
                " SELECT id, im.ifsc ||'-'|| im.name as BRANCH, value_date, utr_no," +
                "        msg_type||'-'||msg_sub_type as MSG_TYPE, tran_type, " +
                "        rm.sender_address, receiver_address, amount " +
                "   FROM rtgs_message rm, MESSAGE m, ifscmaster im ";
            String whereBlock =
                " WHERE m.msg_id = rm.msg_id    " +
                "       AND im.ID = rm.IFSC_MASTER_ID " +
                "       AND m.msg_sub_type in ('R42' ,'R41', 'R10', 'R43', 'R44') ";
            String orderBY =
                " ORDER BY im.ifsc asc, msg_sub_type asc, " +
                "          business_date asc, m.msg_id asc ";

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

            //Related to Branch IFSC.
            if (inputDTO.getIfscId() > 0 ) {

                whereBlock += " and IFSC_Master_ID = " + inputDTO.getIfscId();
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

                whereBlock += " and AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {
                whereBlock += " and AMOUNT >= " + inputDTO.getFromAmount();
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {
                whereBlock += " and AMOUNT <= " + inputDTO.getToAmount();
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryReconcilliationBranchWiseReport += whereBlock + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the Host Branch wise Reconcilliation " +" Report : " + qryReconcilliationBranchWiseReport);
            logger.info("Query for generating the Host Branch wise Reconcilliation " +" Report : " + qryReconcilliationBranchWiseReport);
            ps = con.prepareStatement(qryReconcilliationBranchWiseReport);
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
     * Method to get Branchwisesubtype individual report
     */
    public Message generateBrSubTypeIndividualReport(Message req)
    throws BOException {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        //Map<String, List<ReportDTO>> reportMap = new LinkedHashMap<String, List<ReportDTO>>();

        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());

        try {

            //Report Query.
            String brSubQry = " select ifsc_master_id, im.ifsc ||'-'|| im.NAME as Branch,rm.msg_id,rm.utr_no, " +
                              " rm.value_date,rm.sender_address,rm.receiver_address,rm.amount,rs.name as status , " +
                              " rm.remarks,rm.error_remarks,rm.STATUS from rtgs_message rm,message m,ifscmaster im,rtgs_status rs  " +
                              " where m.msg_id = rm.msg_id and im.ID = rm.ifsc_master_id AND rs.ID = rm.STATUS ";

            String orderBY = " ORDER BY business_date asc,ifsc asc ";

            /**
             * Done on on 07-Jan-2011.
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                    brSubQry = " select ifsc_master_id, im.ifsc ||'-'|| im.NAME as Branch,rm.msg_id,rm.utr_no, " +
                                " rm.value_date,rm.sender_address,rm.receiver_address,rm.amount,rs.name as status , " +
                                " rm.remarks,rm.error_remarks,rm.STATUS from rtgs_message_vw rm,message_vw m, " +
                                " ifscmaster im,rtgs_status rs  where m.msg_id = rm.msg_id and " +
                                " im.ID = rm.ifsc_master_id AND rs.ID = rm.STATUS ";

                    logger.info("Data fetching from archive database schema..");

            } else {

                    logger.info("Data fetching from Local Database schema...");
                }
            } else {

                logger.info("Data fetching from Local Database schema...");
            }


            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                brSubQry += " and rm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ";
            }

            //Related to Transaction Type.
            if (inputDTO.getTransactionType() != null && !inputDTO.getTransactionType()
                                                             .equalsIgnoreCase("ALL")) {

                brSubQry += " and rm.TRAN_TYPE = '" + inputDTO.getTransactionType() +"'" ;
            }

            //Related to Message Sub Type.
            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
                                                             .equalsIgnoreCase("ALL")) {

                brSubQry += " and m.MSG_SUB_TYPE = '" + inputDTO.getPaymentType() +"'" ;
            }

            //Related to Source Type.
            if (inputDTO.getHostType() != null && !inputDTO.getHostType()
                                                            .equalsIgnoreCase("ALL")) {

                brSubQry += " and rm.MSG_SOURCE = '" + inputDTO.getHostType() +"'" ;
            }

            //Related to Branch/IFSC Specific.
            if (inputDTO.getIfscId() > 0 ) {

                brSubQry += " and rm.IFSC_Master_ID = " + inputDTO.getIfscId();
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
                brSubQry += " and rm.AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                brSubQry += " and rm.AMOUNT >= " + inputDTO.getFromAmount();
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                brSubQry += " and rm.AMOUNT <= " + inputDTO.getToAmount();
            }

            // Related to Message Status Specific.
            if (inputDTO.getStatus() != null && !inputDTO.getStatus()
                                                        .equalsIgnoreCase("ALL")) {

                brSubQry += " and rm.STATUS in (" + inputDTO.getStatus() +")" ;
            }

            brSubQry = brSubQry + "and m.msg_sub_type not in ('R90','R09','R40','R43','R44','R10')";
            brSubQry = brSubQry + orderBY;
            logger.info("Query to generate Br.SubType Individual Report : "+brSubQry);
            ps = con.prepareStatement(brSubQry);
            rs = ps.executeQuery();
            String parentfield5561 = "5561";
            String actualFieldA5561 = "A5561";
            String actualFieldN5561 = "N5561";
            String parentfield7495 = "7495";
            String actualFieldI7495 = "I7495";
            String actualFieldA7495 = "A7495";

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

                reportDTO = new ReportDTO();
                Long msgId = rs.getLong("MSG_ID");
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("Value_date")));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setStatus(rs.getString("STATUS"));
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
                if (inputDTO.getPaymentType() != null && inputDTO.getPaymentType().equalsIgnoreCase("R41")) {

                    String A5561 = getFieldValues(msgId,inputDTO.getPaymentType(),parentfield5561,actualFieldA5561);
                    String N5561 = getFieldValues(msgId,inputDTO.getPaymentType(),parentfield5561,actualFieldN5561);
                    if(A5561 != null && A5561.indexOf("/") != -1) {

                        A5561 = A5561.replace("/","");
                    } else if(A5561 == null) {

                        A5561 = "";
                    }
                    reportDTO.setFieldA5561(A5561);
                    reportDTO.setFieldN5561(N5561);
                } else {

                    String I7495 = getFieldValues(msgId,inputDTO.getPaymentType(),parentfield7495,actualFieldI7495);
                    String A7495 = getFieldValues(msgId,inputDTO.getPaymentType(),parentfield7495,actualFieldA7495);
                    reportDTO.setFieldI7495(I7495);
                    reportDTO.setFieldA7495(A7495);
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

    public String getFieldValues(Long msgId,String subType,String parentfield,String actualField) {

        PreparedStatement ps =null;
        ResultSet rs = null;
        String fieldValue = "";
        try {

            StringBuffer query = new StringBuffer();
            query.append(" SELECT value FROM MSGFIELD_STAGE WHERE msg_field_type_id = ( ");
            query.append(" SELECT mft.ID FROM MSGFIELDFORMAT mff, MSGFIELDTYPE mft, ");
            query.append(" MSGCOMPOUNDFIELDDEFN mcfd WHERE mff.id  = mft.field_format_id AND mft.id  = mcfd.cmp_msg_field_type_id ");
            query.append(" AND mcfd.msg_field_type_id = (SELECT id FROM MSGFIELDTYPE WHERE id IN ( ");
            query.append(" SELECT default_field_type_id FROM MSGFIELDDEFN WHERE field_block_id IN ( ");
            query.append(" SELECT id FROM MSGFIELDBLOCKDEFN WHERE block_id = (SELECT MAX(id) FROM MSGBLOCKDEFN WHERE msg_defn_id = ( ");
            query.append(" SELECT MAX(id) FROM MSGDEFN WHERE sub_type= ?)))) AND no= ?) AND MFT.NO= ?) AND msg_id = ? ");
            ps = con.prepareStatement(query.toString());
            ps.setString(1, subType);
            ps.setString(2, parentfield);
            ps.setString(3, actualField);
            ps.setLong(4, msgId);
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

    public String getFieldValuesArchive(Long msgId,String subType,String parentfield,String actualField) {

        PreparedStatement ps =null;
        ResultSet rs = null;
        String fieldValue = "";
        try {

            StringBuffer query = new StringBuffer();
            query.append(" SELECT value FROM MSGFIELD_STAGE_VW WHERE msg_field_type_id = ( ");
            query.append(" SELECT mft.ID FROM MSGFIELDFORMAT mff, MSGFIELDTYPE mft, ");
            query.append(" MSGCOMPOUNDFIELDDEFN mcfd WHERE mff.id  = mft.field_format_id AND mft.id  = mcfd.cmp_msg_field_type_id ");
            query.append(" AND mcfd.msg_field_type_id = (SELECT id FROM MSGFIELDTYPE WHERE id IN ( ");
            query.append(" SELECT default_field_type_id FROM MSGFIELDDEFN WHERE field_block_id IN ( ");
            query.append(" SELECT id FROM MSGFIELDBLOCKDEFN WHERE block_id = (SELECT MAX(id) FROM MSGBLOCKDEFN WHERE msg_defn_id = ( ");
            query.append(" SELECT MAX(id) FROM MSGDEFN WHERE sub_type= ?)))) AND no= ?) AND MFT.NO= ?) AND msg_id = ? ");
            ps = con.prepareStatement(query.toString());
            ps.setString(1, subType);
            ps.setString(2, parentfield);
            ps.setString(3, actualField);
            ps.setLong(4, msgId);
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

    public String getFieldValueByNo(long msgId,String fieldNo,String msgSubType) {

        String getValue = " SELECT GETFIELDVALUE("+msgId+",'"+msgSubType+"','"+fieldNo+"') FROM DUAL ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String value = "";
        try {

            ps = con.prepareStatement(getValue);
            rs = ps.executeQuery();
            if (rs.next()) {
                value = rs.getString(1);
            }
        } catch (Exception e1) {
            logger.error("Exception while getting the fieldvalue"+e1.getMessage());
        } finally {
            release(ps, rs);
        }
        return value;
    }

    /**
     * Method to Generate Inward Payment Rejected By User Reports.
     *
     * Need TODO - get the users list from the user module.
     *
     * @param ReportInputDTO
     * @return List<ReportDTO>
     */
    public Message generateBrInwRejectedByUser(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);

        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());

        try {

            String qryInwardRejectedByUserReport =
                " SELECT rm.VALUE_DATE,rm.UTR_NO,rm.SENDER_ADDRESS,rm.RECEIVER_ADDRESS,"+
                " rm.AMOUNT, rm.ENTRY_BY,rm.PASS_BY,rm.REMARKS,m.MSG_SUB_TYPE FROM MESSAGE m,RTGS_MESSAGE rm";
            String whereBlock = " WHERE m.MSG_ID = rm.MSG_ID AND rm.STATUS = 900 ";
            String orderBY = " ORDER BY m.MSG_ID DESC";


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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               qryInwardRejectedByUserReport =
                " SELECT rm.VALUE_DATE,rm.UTR_NO,rm.SENDER_ADDRESS,rm.RECEIVER_ADDRESS,"+
                " rm.AMOUNT, rm.ENTRY_BY,rm.PASS_BY,rm.REMARKS,m.MSG_SUB_TYPE FROM MESSAGE_vw m,RTGS_MESSAGE_vw rm";
             whereBlock = " WHERE m.MSG_ID = rm.MSG_ID AND rm.STATUS = 900 ";
             orderBY = " ORDER BY m.MSG_ID DESC";

                        logger.info("Data fetching from archive database schema..");

                } else {

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                    logger.info("Data fetching from Local Database schema...");
                }


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

            //Related to Message Sub Type.
            if (inputDTO.getPaymentType() != null && !inputDTO.getPaymentType()
                                                             .equalsIgnoreCase("ALL")) {

                whereBlock += " and MSG_SUB_TYPE = '" + inputDTO.getPaymentType() +"'" ;
            }

            //Related to Branch IFSC.
            if (inputDTO.getIfscId() > 0 ) {

                whereBlock += " and IFSC_Master_ID = " + inputDTO.getIfscId();
            }

            //Related to USER.
            if (inputDTO.getUserId() != null && !inputDTO.getUserId()
                 .equalsIgnoreCase("ALL") && inputDTO.getUserId().length() > 0) {
                String userId = inputDTO.getUserId().substring(inputDTO.getUserId().indexOf("-")+1);
                whereBlock += " and ENTRY_BY = '" + userId+"'";
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
                whereBlock += " and AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount();
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and AMOUNT >= " + inputDTO.getFromAmount();
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                whereBlock += " and AMOUNT <= " + inputDTO.getToAmount();
            }

            //Append the WhereBlock(filter conditions) and OrderBy.
            qryInwardRejectedByUserReport += whereBlock + orderBY;

            //To avoid printing queries in jboss console ..by priyak
            //logger.info("Query for generating the Branch inward rejected By user Report : "+ qryInwardRejectedByUserReport);
            logger.info("Query for generating the Branch inward rejected By user Report : "+ qryInwardRejectedByUserReport);
            ps = con.prepareStatement(qryInwardRejectedByUserReport);
            rs = ps.executeQuery();

            while(rs.next()) {

                reportDTO = new ReportDTO();
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                reportDTO.setMsgType(rs.getString("MSG_SUB_TYPE"));
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("VALUE_DATE")));
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));
                reportDTO.setAmt(rs.getString("AMOUNT"));
                reportDTO.setEntryBy(rs.getString("ENTRY_BY"));
                reportDTO.setPassBy(rs.getString("PASS_BY"));
                reportDTO.setRemarks(rs.getString("REMARKS"));
                reportList.add(reportDTO);
            }
        } catch(Throwable th) {

            logger.error("Exception while generating Branch inward rejected By user Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating Branch inward rejected By user Report."
                                , th);
        } finally {

            release(ps, rs);
        }
        req.info = reportList;
        return req;
    }

    /**
     * Specifically for Reports.
     *
     * Since we are calling from child to parent. we need to send the parent environment.
     *
     * @see com.objectfrontier.insta.server.bo.InstaBO#getProperties()
     */
    public Map getProperties(){

        Map<String, Object> props = new HashMap<String, Object>(0);
        props.put(KEY_ENVIRONMENT, this.env.getParentEnvironment());
        props.put(KEY_CONNECTION, this.con);

        return props;
    }

    public String getMessagDetails(String msgId)
    throws Exception {


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
            }

            logger.info("accNoCount : " + accNoCount);

            StringBuffer query1 = new StringBuffer();
            query1.append(" SELECT cbs.acc_number from cbsaccountdetails cbs where msg_id = ? ");
            ps1 = con.prepareStatement(query1.toString());
            ps1.setString(1, msgId);
            rs1 = ps1.executeQuery();
            while(rs1.next()) {

                accNo = rs1.getString(1) == null ? "" : rs1.getString(1);
                accNoList.add(accNo);
            }
            accNo = "";
            Iterator itr = accNoList.iterator();
            while(itr.hasNext()) {

                accNo += (String)itr.next();
                accNo += ",";
            }
            int index =  accNo.lastIndexOf(",");
            accNo = accNo.substring(0,index);

        } catch (Exception e1) {
            // TODO: handle exception

        } finally {
            try {

                release(ps, rs);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return accNo;
    }

    public String getAccNo(String msgId)
    throws Exception {

        logger.info("getAccNo(msgId)");

        PreparedStatement ps =null;
        ResultSet rs = null;
        String accNo = "";
        try {

            StringBuffer query = new StringBuffer();

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
                accNo = rs.getString(1) == null ? "" : rs.getString(1);
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

        return accNo;

    }
    /**
     * @author sasmitap
     * To get OrderingCustomer Value
     */
    public String getOrderingCustomerValue(String msgId)
    throws Exception {

        logger.info("getOrderingCustomerValue(msgId)");

        PreparedStatement ps =null;
        ResultSet rs = null;
        String orderingCust = "";
        try {

            StringBuffer query = new StringBuffer();
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

       } catch (Exception e1) {
            //TODO: handle exception
        } finally {
            try {

                release(ps, rs);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

        return orderingCust;
    }
    /*
     * @author sasmitaP
     * To get beneficiaryName
     */
    public String getBeneficiaryName(String msgId)
    throws Exception {

        logger.info("getBeneficiaryName(msgId)");

        PreparedStatement ps =null;
        ResultSet rs = null;
        String beneficiaryName = "";
        try {

            StringBuffer query = new StringBuffer();
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

        } catch (Exception e1) {
            // TODO: handle exception
        } finally {
            try {

                release(ps, rs);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return beneficiaryName;
    }

    public void getSenderAndReceiverIfsc(String msgId,ReportDTO dto)
    throws Exception {

        logger.info("getAccNo(msgId)");


            PreparedStatement ps =null;
            ResultSet rs = null;
            String sendAdd = null;
            String recAdd = null;
            try {

                StringBuffer query = new StringBuffer();
                query.append("select sender_address,receiver_address from rtgs_message where msg_id = ? ");
                ps = con.prepareStatement(query.toString());
                ps.setString(1, msgId);
                rs = ps.executeQuery();
                while(rs.next()) {

                    sendAdd = rs.getString(1);
                    recAdd = rs.getString(2);
                }
                dto.setBeneficiaryName(recAdd);
                dto.setRemitterName(sendAdd);
            } catch (Exception e1) {
                //TODO: handle exception
            } finally {
                try {

                    release(ps, rs);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
    }

    /**
     * This method is to get Excel sheet Report for current date
     * @param msg
     * @return Message
     */

    public Message generateExcelSheetReport(Message msg) throws Exception{

        ReportDTO reportDTO = (ReportDTO) msg.info;
        Message res = new Message();
        String currentDate = reportDTO.getValueDate();
        PreparedStatement ps;
        ResultSet rs;
        PreparedStatement ps1;
        ResultSet rs1;
        if (reportDTO==null ) {
            throw new BOException ("Invalid Criteria.");
        }

        try{

            StringBuffer query =  new StringBuffer();
            query.append(" SELECT ifsc.IFSC ifsc, ");
            query.append(" msg.msg_id message_id,");
            query.append(" md.sub_type sub_type, ");
            query.append(" rtgs_msg.utr_no utr_number, ");
            query.append(" md.sub_type sub_type, ");
            query.append(" rtgs_msg.tran_type txn_type, ");
            query.append(" rtgs_msg.receiver_address receiver_address, ");
            query.append(" rtgs_msg.value_date value_date,");
            query.append(" rtgs_msg.amount  amount, rs.name statusname ");
            query.append(" from message msg , rtgs_message  rtgs_msg, rtgs_status rs, msgdefn md, ifscmaster ifsc, hostifscmaster hostifsc ");
            query.append(" WHERE rtgs_msg.msg_id = msg.msg_id AND msg.msg_defn_id = md.ID AND rtgs_msg.ifsc_master_id  = hostifsc.ID ");
            query.append(" AND hostifsc.ID = ifsc.ID  AND rtgs_msg.status = rs.id AND ");
            query.append(" rtgs_msg.business_date = ' " + currentDate + " ' ");

            if ( reportDTO.getTranType() != null) {

            if (InstaDefaultConstants.TXN_TYPE_INWARD.equalsIgnoreCase( reportDTO.getTranType())) {
                query.append(" AND ");
                query.append(" rtgs_msg.tran_type = '" +  reportDTO .getTranType() + "'");
             }

            if (InstaDefaultConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase( reportDTO.getTranType())) {
                query.append(" AND ");
                query.append(" rtgs_msg.tran_type = '" +  reportDTO.getTranType() + "'");
              }
            }

            if ( reportDTO.getMsgSubType() != null) {
            if (InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase( reportDTO.getMsgSubType()) || InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase( reportDTO .getMsgSubType())) {
                query.append(" AND ");
                query.append(" md.type = '" + InstaDefaultConstants.MSGTYPE + "'");
                query.append(" AND ");
                query.append(" md.sub_type = '" +  reportDTO.getMsgSubType() + "'");
            }
        }
        query.append(" ORDER BY rtgs_msg.utr_no DESC ");
        ps = con.prepareStatement(query.toString());
        rs = ps.executeQuery();

        String rtgsQuery = "select parameter_value from rtgsparameters where parameter_name = 'excel_file_path' ";

        String filePath = null;
        String fileName = null;
        String txnType = null;
        ps1 = con.prepareStatement(rtgsQuery);
        rs1 = ps1.executeQuery();
        while (rs1.next()) {
            filePath = rs1.getString(1);
        }
        if (InstaDefaultConstants.TXN_TYPE_INWARD.equalsIgnoreCase( reportDTO.getTranType())) {

            if (InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase( reportDTO.getMsgSubType())) {
                fileName = filePath+currentDate+"R42Inward.xls";
            }
            if (InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase( reportDTO.getMsgSubType())) {
                fileName = filePath+currentDate+"R41Inward.xls";
            }
        }
        if (InstaDefaultConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase( reportDTO.getTranType())) {

            if (InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase( reportDTO.getMsgSubType())) {
               fileName = filePath+currentDate+"R42Outward.xls";
            }
            if (InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase( reportDTO.getMsgSubType())) {
               fileName = filePath+currentDate+"R41Outward.xls";
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        int i = 0;
        if (InstaDefaultConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase( reportDTO.getTranType())) {

            writer.write("S.NO");
            writer.write('\t');
            writer.write("BRANCH NAME");
            writer.write('\t');
            writer.write("RECEIVER IFSC");
            writer.write('\t');
            writer.write("AMOUNT");
            writer.write('\t');
            writer.write("UTR NO");
            writer.write('\t');
            writer.write("STATUS");
            writer.write('\t');
            writer.flush();
            writer.write('\n');
        }
        if (InstaDefaultConstants.TXN_TYPE_INWARD.equalsIgnoreCase( reportDTO.getTranType())) {

            writer.write("S.NO");
            writer.write('\t');
            writer.write("BENEFICIARY NAME");
            writer.write('\t');
            writer.write("AMOUNT");
            writer.write('\t');
            writer.write("ACCOUNT NUMBER");
            writer.write('\t');
            writer.write("UTR NO");
            writer.write('\t');
            writer.write("NAME OF REMITTER");
            writer.write('\t');
            writer.write("STATUS");
            writer.write('\t');
            writer.flush();
            writer.write('\n');
       }

        while(rs.next()) {

            ReportDTO dto = new ReportDTO();
            String accNo = "";
            String orderCustomer = "";
            String beneficiaryName = "";
            String msgId = rs.getString(InstaDefaultConstants.MSG_ID);
            txnType  = rs.getString(InstaDefaultConstants.TXN_TYPE);
            String subType = rs.getString(InstaDefaultConstants.SUB_TYPE);

            if (InstaDefaultConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {

                 if(InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {

                     accNo = getMessagDetails(msgId);
                     dto.setAccNo(accNo);
                 }
                 if(InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
                   accNo =  getAccNo(msgId);
                   if(accNo.indexOf("/") != -1) {
                       accNo = accNo.replace("/", "");
                   }
                  dto.setAccNo(accNo);
                 }
            }

            if (InstaDefaultConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {

                  if(InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {

                      orderCustomer = getOrderingCustomerValue(msgId);
                      int newLineIndex = orderCustomer.indexOf("\r\n");  //NEW_LINE_STRING \r\n
                      int firstCtrlIndx = orderCustomer.indexOf("\n"); //
                      if(newLineIndex != -1) {

                          orderCustomer = orderCustomer.replace("\r\n"," ");
                      } else if(firstCtrlIndx != -1) {

                          orderCustomer = orderCustomer.replace("\n"," ");
                      }
                      dto.setRemitterName(orderCustomer);
                      beneficiaryName = getBeneficiaryName(msgId);
                      int newLine = beneficiaryName.indexOf("\r\n");
                      int  ctrlstr = beneficiaryName.indexOf("\n");
                      if(newLine != -1 ) {

                          beneficiaryName = beneficiaryName.replace("\r\n"," ");
                      } else if(ctrlstr != -1) {

                          beneficiaryName = beneficiaryName.replace("\n"," ");
                      }
                      dto.setBeneficiaryName(beneficiaryName);
                 }
                 if(InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                     getSenderAndReceiverIfsc(msgId,dto);
                 }
            }
            getOrderingCustomerValue(msgId);
            String ifsc = rs.getString(InstaDefaultConstants.IFSC);
            dto.ifsc = ifsc;
            String recAdd = rs.getString(InstaDefaultConstants.RECEIVER_ADDRESS);
            dto.branchName = recAdd;
//            double amount = rs.getDouble(InstaDefaultConstants.AMOUNT);
//            dto.setAmount(amount);
            BigDecimal amount = rs.getBigDecimal(InstaDefaultConstants.AMOUNT);
            dto.setAmt(amount.toString());
            String formatAmount = FormatAmount.formatINRAmount(amount);
            amount = amount.add(new BigDecimal(reportDTO.dateGrantTolal));
            reportDTO.dateGrantTolal = amount.toString();
            String utrNo = rs.getString(InstaDefaultConstants.UTR_NO);
            dto.setUtrNo(utrNo);
            Timestamp date = rs.getTimestamp(InstaDefaultConstants.VALUE_DATE);
            dto.reportvalueDate = date;
            String statusName = rs.getString("statusname");
            dto.setStatus(statusName);
            branchDto.add(dto);
            reportDTO.branchwiseDTOs = branchDto;
            if (InstaDefaultConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {

                i += 1;
                writer.write(String.valueOf(i));
                writer.write('\t');
                if(InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
                   writer.write(beneficiaryName.trim());
                   writer.write('\t');
                }
                if(InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                   writer.write(dto.beneficiaryName.trim());
                   writer.write('\t');
                }
                writer.write(formatAmount);
                writer.write('\t');
                if(InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                   writer.write(accNo);
                }
                if(InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
                   writer.write(dto.accNo);
                }
                writer.write('\t');
                writer.write(utrNo);
                writer.write('\t');
                if(InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {
                   writer.write(orderCustomer);
                }
                if(InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                    writer.write(dto.remitterName);
                }
                writer.write('\t');
                writer.write(statusName);
                writer.write('\t');
                writer.flush();
                writer.write('\n');
            }
            if (InstaDefaultConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {

               i += 1;
               writer.write(String.valueOf(i));
               writer.write('\t');
               writer.write(ifsc);
               writer.write('\t');
               writer.write(recAdd);
               writer.write('\t');
               writer.write(formatAmount);
               writer.write('\t');
               writer.write(utrNo);
               writer.write('\t');
               writer.write(statusName);
               writer.write('\t');
               writer.flush();
               writer.write('\n');
           }
        }
        if (InstaDefaultConstants.TXN_TYPE_OUTWARD.equalsIgnoreCase(txnType)) {
            writer.write('\t');
            writer.write('\t');
            writer.write("Total");
        }
        if (InstaDefaultConstants.TXN_TYPE_INWARD.equalsIgnoreCase(txnType)) {
            writer.write('\t');
            writer.write("Total");
        }
        writer.write('\t');
        String totalAmt = FormatAmount.formatINRAmount(reportDTO.dateGrantTolal);
        writer.write(totalAmt);
        writer.write('\t');
        writer.flush();
        writer.write('\n');

        res.info= reportDTO;

        }
        catch(Exception e){

            logger.error("Exception while generating Graduated Payment report :"+e);
            throw new Exception("Exception while generating Graduated Payment Report."+e);
           }
        return res;
    }
    /**
     * This method is to get DayEnd Inward Report for current date
     * @param msg
     * @return message
     */

    public Message generateInwardDayEndReport(Message msg)
    throws Exception{

        ReportDTO reportDTO = (ReportDTO) msg.info;
        Message res = new Message();
        String currentDate = reportDTO.getValueDate();
        ResultSet rs = null;
        PreparedStatement ps = null;
       try {

          StringBuffer query =  new StringBuffer();
          query.append(" SELECT ifsc.IFSC ifsc, ");
          query.append(" msg.msg_id message_id,");
          query.append(" md.sub_type sub_type, ");
          query.append(" rtgsmsg.utr_no  utr_number, ");
          query.append(" md.sub_type sub_type, ");
          query.append(" rtgsmsg.tran_type txn_type, ");
          query.append(" rtgsmsg.receiver_address receiver_address, ");
          query.append(" rtgsmsg.value_date value_date,");
          query.append(" rtgsmsg.amount amount ");
          query.append(" from message msg, RTGS_MESSAGE rtgsmsg, msgdefn md, ifscmaster ifsc, hostifscmaster hostifsc ");
          query.append(" WHERE rtgsmsg.msg_id = msg.msg_id AND msg.msg_defn_id = md.ID AND rtgsmsg.ifsc_master_id = hostifsc.ID ");
          query.append(" AND hostifsc.ID = ifsc.ID  AND ");
          query.append(" rtgsmsg.business_date='" + currentDate + "' AND ");
          query.append(" rtgsmsg.tran_type = 'inward' ");
          query.append(" AND msg.msg_sub_type not in ('R09', 'R90', 'R43') ");
          query.append(" ORDER BY rtgsmsg.utr_no DESC ");
          ps = con.prepareStatement(query.toString());
          rs = ps.executeQuery();

          while(rs.next()) {

              ReportDTO dto = new ReportDTO();
              String accNo = "";
              String orderCustomer = "";
              String beneficiaryName = "";
              String msgId = rs.getString(InstaDefaultConstants.MSG_ID);
              String subType = rs.getString(InstaDefaultConstants.SUB_TYPE);
//              double amount= rs.getDouble(InstaDefaultConstants.AMOUNT);
//              dto.setAmount(amount);
              BigDecimal amount= rs.getBigDecimal(InstaDefaultConstants.AMOUNT);
              dto.setAmt(amount.toString());
              /*if(subType.equalsIgnoreCase("R43")) {
                  reportDTO.dateGrantTolal -= amount;
              } else {

              }*/
              amount = amount.add(new BigDecimal(reportDTO.dateGrantTolal));
              reportDTO.dateGrantTolal = amount.toString();

              String utrNo = rs.getString(InstaDefaultConstants.UTR_NO);
              dto.setUtrNo(utrNo);
              if(InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {

                  accNo = getMessagDetails(msgId);
                  dto.setAccNo(accNo);
              }
              if(InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {

                  accNo =  getAccNo(msgId);
                  if(accNo.indexOf("/") != -1) {
                      accNo = accNo.replace("/", "");
                  }
                  dto.setAccNo(accNo);
              }
              if(InstaDefaultConstants.IPRSUB_TYPE.equalsIgnoreCase(subType)) {

                  orderCustomer = getOrderingCustomerValue(msgId);
                  dto.setRemitterName(orderCustomer);
                  beneficiaryName = getBeneficiaryName(msgId);
                  dto.setBeneficiaryName(beneficiaryName);
              }
              if(InstaDefaultConstants.CPRSUB_TYPE.equalsIgnoreCase(subType)) {
                  getSenderAndReceiverIfsc(msgId,dto);
              }
              branchDto.add(dto);
              reportDTO.branchwiseDTOs = branchDto;
         }
          res.info= reportDTO;

          return res;

      }catch(Exception e){

          logger.error("Exception while generating Graduated Payment report :"+e);
          throw new Exception("Exception while generating Graduated Payment Report."+e);
         }
    }

    /**
     * This method is to get Report for Day End Outward
     * @param msg
     * @return message
     */
    public Message generateOutwardDayEndReport(Message msg)
    throws Exception {
        ReportDTO reportDTO = (ReportDTO) msg.info;
        Message res = new Message();
        String currentDate = reportDTO.getValueDate();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            StringBuffer query =  new StringBuffer();
            query.append(" SELECT ifsc.IFSC ifsc, ");
            query.append(" msg.msg_id message_id,");
            query.append(" md.sub_type sub_type, ");
            query.append(" rtgs_msg.utr_no utr_number, ");
            query.append(" md.sub_type sub_type, ");
            query.append(" rtgs_msg.tran_type txn_type, ");
            query.append(" rtgs_msg.receiver_address receiver_address, ");
            query.append(" rtgs_msg.value_date value_date,");
            query.append(" rtgs_msg.amount amount ");
            query.append(" from message msg, RTGS_MESSAGE rtgs_msg, msgdefn md, ifscmaster ifsc, hostifscmaster hostifsc ");
            query.append(" WHERE rtgs_msg.msg_id   = msg.msg_id AND msg.msg_defn_id = md.ID AND rtgs_msg.ifsc_master_id = hostifsc.ID ");
            query.append(" AND hostifsc.ID = ifsc.ID  AND ");
            query.append(" rtgs_msg.business_date ='" + currentDate + "' AND ");
            query.append(" rtgs_msg.tran_type = 'outward' ");
            query.append(" ORDER BY rtgs_msg.utr_no DESC ");
            ps = con.prepareStatement(query.toString());
            rs = ps.executeQuery();

            while(rs.next()) {

               ReportDTO dto = new ReportDTO();
               String ifsc = rs.getString(InstaDefaultConstants.IFSC);
               dto.ifsc = ifsc;
               String recAdd = rs.getString(InstaDefaultConstants.RECEIVER_ADDRESS);
               dto.branchName = recAdd;
//               double amount = rs.getDouble(InstaDefaultConstants.AMOUNT);
//               dto.setAmount(amount);
               BigDecimal amount = rs.getBigDecimal(InstaDefaultConstants.AMOUNT);
               dto.setAmt(amount.toString());
               amount = amount.add(new BigDecimal(reportDTO.dateGrantTolal));
               reportDTO.dateGrantTolal = amount.toString();
               String utrNo = rs.getString(InstaDefaultConstants.UTR_NO);
               dto.setUtrNo(utrNo);
               Timestamp date = rs.getTimestamp(InstaDefaultConstants.VALUE_DATE);
               dto.vDate = date;
               branchDto.add(dto);
               reportDTO.branchwiseDTOs = branchDto;
          }

          if(rs != null) {
              rs.close();
          }
          if(ps != null) {
              ps.close();
          }

          StringBuffer inwardQuery =  new StringBuffer();
          inwardQuery.append(" SELECT ifsc.IFSC ifsc, ");
          inwardQuery.append(" msg.msg_id message_id,");
          inwardQuery.append(" md.sub_type sub_type, ");
          inwardQuery.append(" rtgsmsg.utr_no  utr_number, ");
          inwardQuery.append(" md.sub_type sub_type, ");
          inwardQuery.append(" rtgsmsg.tran_type txn_type, ");
          inwardQuery.append(" rtgsmsg.receiver_address receiver_address, ");
          inwardQuery.append(" rtgsmsg.value_date value_date,");
          inwardQuery.append(" rtgsmsg.amount amount ");
          inwardQuery.append(" from message msg, RTGS_MESSAGE rtgsmsg, msgdefn md, ifscmaster ifsc, hostifscmaster hostifsc ");
          inwardQuery.append(" WHERE rtgsmsg.msg_id = msg.msg_id AND msg.msg_defn_id = md.ID AND rtgsmsg.ifsc_master_id = hostifsc.ID ");
          inwardQuery.append(" AND hostifsc.ID = ifsc.ID  AND ");
          inwardQuery.append(" rtgsmsg.business_date='" + currentDate + "' AND ");
          inwardQuery.append(" rtgsmsg.tran_type = 'inward' ");
          inwardQuery.append(" AND msg.msg_sub_type in ('R43') ");
          inwardQuery.append(" ORDER BY rtgsmsg.utr_no DESC ");
          ps = con.prepareStatement(inwardQuery.toString());
          rs = ps.executeQuery();

          while(rs.next()) {

              ReportDTO dto = new ReportDTO();
              String ifsc = rs.getString(InstaDefaultConstants.IFSC);
              dto.ifsc = ifsc;
              String recAdd = rs.getString(InstaDefaultConstants.RECEIVER_ADDRESS);
              dto.branchName = recAdd;
//              double amount = rs.getDouble(InstaDefaultConstants.AMOUNT);
//              dto.setAmount(amount);
              BigDecimal amount = rs.getBigDecimal(InstaDefaultConstants.AMOUNT);
              amount.add(new BigDecimal(reportDTO.dateGrantTolal));
              reportDTO.dateGrantTolal = amount.toString();
              String utrNo = rs.getString(InstaDefaultConstants.UTR_NO);
              dto.setUtrNo(utrNo);
              Timestamp date = rs.getTimestamp(InstaDefaultConstants.VALUE_DATE);
              dto.vDate = date;
              branchDto.add(dto);
              reportDTO.branchwiseDTOs = branchDto;
         }

          res.info= reportDTO;

            return res;
        } catch(Exception e){

            logger.error("Exception while generating Graduated Payment report :"+e);
            throw new Exception("Exception while generating Graduated Payment Report."+e);
        }
     }

    /**
     * Method to generate Liquidity Mgt Report
     * @param msg
     * @return message
     */

    public Message generateLiquidityMgtReport(Message msg)
    throws Exception {

       ReportDTO reportDTO = (ReportDTO) msg.info;
       Message res = new Message();
       String currentDate = reportDTO.getValueDate();
       ResultSet rsR44 = null;
       PreparedStatement psR44 = null;
       ResultSet rsR41Inward = null;
       PreparedStatement psR41Inward = null;
       ResultSet rsR42Inward = null;
       PreparedStatement psR42Inward = null;
       ResultSet rsR41Outward = null;
       PreparedStatement psR41Outward = null;
       ResultSet rsR42Outward = null;
       PreparedStatement psR42Outward = null;
       ResultSet rsR43 = null;
       PreparedStatement psR43 = null;
       try {

           String query = " SELECT TO_CHAR(SUM ( TO_NUMBER( rtgs.amount))) FROM RTGS_MESSAGE  rtgs WHERE rtgs.msg_id IN " +
                          "( SELECT m.msg_id FROM MESSAGE  m WHERE  m.msg_defn_id = 20  ) AND  rtgs.tran_type LIKE 'inward'  AND "+
                          " TO_DATE(rtgs.business_date) = ? ";
           psR44 = con.prepareStatement(query);
           psR44.setString(1, currentDate);
           rsR44 = psR44.executeQuery();
//           while (rsR44.next()) {
           if(rsR44.next()) {
//               double amount = rsR44.getDouble(1);
//               reportDTO.creditSettlement = amount;
               reportDTO.creditSettlement = rsR44.getString(1);
           }

          String inCprQuery = " SELECT TO_CHAR( SUM ( TO_NUMBER( rtgs.amount))) FROM RTGS_MESSAGE  rtgs WHERE rtgs.msg_id IN " +
                              "( SELECT m.msg_id FROM MESSAGE  m WHERE  m.msg_defn_id = 14 ) AND  rtgs.tran_type LIKE 'inward'  AND "+
                              " TO_DATE(rtgs.business_date) = ? ";
           psR41Inward =  con.prepareStatement(inCprQuery);
           psR41Inward.setString(1,currentDate);
           rsR41Inward = psR41Inward.executeQuery();
//           while (rsR41Inward.next()) {
//
//               double inwCprAmt = rsR41Inward.getDouble(1);
//               reportDTO.dateCprInAmt = inwCprAmt;
//           }
           if(rsR41Inward.next()){
               reportDTO.dateCprInAmt = rsR41Inward.getString(1);
           }

           String inIprQuery = " SELECT TO_CHAR( SUM ( TO_NUMBER( rtgs.amount))) FROM RTGS_MESSAGE  rtgs WHERE rtgs.msg_id IN " +
                               "( SELECT m.msg_id FROM MESSAGE  m WHERE  m.msg_defn_id = 13 ) AND  rtgs.tran_type LIKE 'inward'  AND "+
                               " TO_DATE(rtgs.business_date) = ? ";
           psR42Inward =  con.prepareStatement(inIprQuery);
           psR42Inward.setString(1,currentDate);
           rsR42Inward = psR42Inward.executeQuery();
//           while (rsR42Inward.next()) {
//
//                double inwIprAmt = rsR42Inward.getDouble(1);
//                reportDTO.dateIprInAmt = inwIprAmt;
//           }
           if(rsR42Inward.next()){
               reportDTO.dateIprInAmt = rsR42Inward.getString(1);
           }
           String outCprQuery = " SELECT TO_CHAR( SUM ( TO_NUMBER( rtgs.amount))) FROM RTGS_MESSAGE  rtgs WHERE rtgs.msg_id IN " +
                                "( SELECT m.msg_id FROM MESSAGE  m WHERE  m.msg_defn_id = 14 ) AND  rtgs.tran_type LIKE 'outward'  AND "+
                                " TO_DATE(rtgs.business_date) = ? and status in(2500,2600,2350,3000)";

           psR41Outward =  con.prepareStatement(outCprQuery);
           psR41Outward.setString(1,currentDate);
           rsR41Outward = psR41Outward.executeQuery();
//           while (rsR41Outward.next()) {
//
//               double outCprAmt = rsR41Outward.getDouble(1);
//               reportDTO.dateCprOutAmt = outCprAmt;
//           }
           if(rsR41Outward.next()){
               reportDTO.dateCprOutAmt = rsR41Outward.getString(1);
           }

           String outIprQuery = " SELECT TO_CHAR( SUM ( TO_NUMBER( rtgs.amount))) FROM RTGS_MESSAGE  rtgs WHERE rtgs.msg_id IN " +
                                "( SELECT m.msg_id FROM MESSAGE  m WHERE  m.msg_defn_id = 13 ) AND  rtgs.tran_type LIKE 'outward'  AND "+
                                " TO_DATE(rtgs.business_date) = ? and status in(2500,2600,2350,3000)";

           psR42Outward =  con.prepareStatement(outIprQuery);
           psR42Outward.setString(1,currentDate);
           rsR42Outward = psR42Outward.executeQuery();
//           while (rsR42Outward.next()) {
//
//               double outIprAmt = rsR42Outward.getDouble(1);
//               reportDTO.dateIprOutAmt = outIprAmt;
//           }
           if(rsR42Outward.next()){
               reportDTO.dateIprOutAmt = rsR42Outward.getString(1);
           }
           String queryR43 = " SELECT TO_CHAR( SUM ( TO_NUMBER( rtgs.amount))) FROM RTGS_MESSAGE  rtgs WHERE rtgs.msg_id IN " +
                                   "( SELECT m.msg_id FROM MESSAGE  m WHERE  m.msg_defn_id = 23 ) AND  rtgs.tran_type LIKE 'inward'  AND "+
                                   " TO_DATE(rtgs.business_date) = ? ";

           psR43 =  con.prepareStatement(queryR43);
           psR43.setString(1,currentDate);
           rsR43 = psR43.executeQuery();
//           while (rsR43.next()) {
//
//               double debitAmt = rsR43.getDouble(1);
//               reportDTO.debitSettlement = debitAmt;
//           }
           if(rsR43.next()){
               reportDTO.debitSettlement = rsR43.getString(1);
           }
           res.info= reportDTO;
           return res;
        } catch(Exception e){

            logger.error("Exception while generating Graduated Payment report :"+e);
            throw new Exception("Exception while generating Graduated Payment Report."+e);
        } finally {

            release(psR44, rsR44);
            release(psR41Inward, rsR41Inward);
            release(psR42Inward, rsR42Inward);
            release(psR41Outward, rsR41Outward);
            release(psR42Outward, rsR42Outward);
            release(psR43, rsR43);
        }
    }

    /**
     * Method to generate Outward returned report
     */
    public Message generateOutwardReturnedReport(Message req)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) req.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        List<String> addedUtr = new ArrayList<String>();

        BigDecimal fromAmount = new BigDecimal(inputDTO.getFromAmount());
        BigDecimal toAmount = new BigDecimal(inputDTO.getToAmount());
        try {

            Map<String, String> rtgsOwReturned = new LinkedHashMap<String, String>(0);

            String rtgs_ow_retn_search_field = InstaDefaultConstants.RTGS_OW_RETN_SEARCH_FIELD;

            String[] rtgs_ow_retn_search_field_arr = null;

            if(rtgs_ow_retn_search_field != null && rtgs_ow_retn_search_field.length() > 0) {

                rtgs_ow_retn_search_field_arr = rtgs_ow_retn_search_field.split(",");
            }


            final String msgQuery_alt =  "  SELECT id FROM MSGFIELDTYPE WHERE field_defn_id IN ( " +
                                         "  SELECT id FROM MSGFIELDDEFN WHERE field_block_id IN (  " +
                                         " SELECT id FROM MSGFIELDBLOCKDEFN WHERE block_id = ( " +
                                         " SELECT MAX(id) FROM MSGBLOCKDEFN WHERE msg_defn_id = ( " +
                                         " SELECT MAX(id) FROM MSGDEFN WHERE TYPE='298' AND sub_type='R42'))) AND no=?)     ";

            final String msgQuery_type = "  SELECT cmp_msg_field_type_id FROM MSGCOMPOUNDFIELDDEFN WHERE msg_field_type_id = ( " +
                                         "  SELECT id FROM MSGFIELDTYPE WHERE id IN ( " +
                                         "  SELECT DEFAULT_FIELD_TYPE_ID FROM MSGFIELDDEFN WHERE field_block_id IN (  " +
                                         " SELECT id FROM MSGFIELDBLOCKDEFN WHERE block_id = ( " +
                                         " SELECT MAX(id) FROM MSGBLOCKDEFN WHERE msg_defn_id = ( " +
                                         " SELECT MAX(id) FROM MSGDEFN WHERE TYPE='298' AND sub_type='R42')))) AND no=?)     ";


            final String msgQuery_altType = "  SELECT cmp_msg_field_type_id FROM MSGCOMPOUNDFIELDDEFN WHERE msg_field_type_id = ( " +
                                            "  SELECT id FROM MSGFIELDTYPE WHERE field_defn_id IN ( " +
                                            "  SELECT id FROM MSGFIELDDEFN WHERE field_block_id IN (  " +
                                            " SELECT id FROM MSGFIELDBLOCKDEFN WHERE block_id = ( " +
                                            " SELECT MAX(id) FROM MSGBLOCKDEFN WHERE msg_defn_id = ( " +
                                            " SELECT MAX(id) FROM MSGDEFN WHERE TYPE='298' AND sub_type='R42')))) AND no=?)     ";

            for (int i = 0; i < rtgs_ow_retn_search_field_arr.length; i++) {

                ps = con.prepareStatement(msgQuery_alt);
                ps.setString(1, rtgs_ow_retn_search_field_arr[i]);
                rs = ps.executeQuery();

                boolean result = false;
                if(rs.next()) {

                    result = true;
                }

                if(rs != null) {
                    rs.close();
                }
                if(ps != null) {
                    ps.close();
                }

                String isComAlt = "";

                if(result) {

                    isComAlt = "1";
                    ps = con.prepareStatement(msgQuery_altType);
                } else {

                    isComAlt = "0";
                    ps = con.prepareStatement(msgQuery_type);
                }

                ps.setString(1, rtgs_ow_retn_search_field_arr[i]);
                rs = ps.executeQuery();

                if(rs.next()) {

                    isComAlt += ",1";
                    rtgsOwReturned.put(rtgs_ow_retn_search_field_arr[i], isComAlt);
                } else {

                    isComAlt += ",0";
                    rtgsOwReturned.put(rtgs_ow_retn_search_field_arr[i], isComAlt);
                }

                if(rs != null) {
                    rs.close();
                }
                if(ps != null) {
                    ps.close();
                }
            }


            StringBuffer sb = new StringBuffer(4028);

            sb.append("SELECT DISTINCT utr_no, sender_address, receiver_address,")
            .append(" i7495, BUSINESS_DATE, ")
            .append(" a7495, amount, value_date ");

              if(rtgsOwReturned != null  && rtgsOwReturned.size() > 0) {

                  int size = rtgsOwReturned.size();
                  sb.append(", ");
                  int count = 1;
                  for(Iterator i = rtgsOwReturned.entrySet().iterator(); i.hasNext(); ) {
                      Map.Entry entry = (Map.Entry)i.next();
                      String result = appendFinalString((String)entry.getKey(), "RTGS");
                      sb.append(" " + result);
                      if(count != size) {
                          sb.append(", ");
                          count++;
                      }
                  }
              }

            sb.append(" FROM ( SELECT DISTINCT rm.utr_no, rm.sender_address, rm.receiver_address,")
              .append("Getcompoundfieldvalue (rm.msg_id, '7495', 'I7495', m.msg_sub_type) i7495, rm.BUSINESS_DATE, ")
              .append("Getcompoundfieldvalue (rm.msg_id, '7495', 'A7495', m.msg_sub_type) a7495, rm.amount, rm.value_date ");

            if(rtgsOwReturned != null  && rtgsOwReturned.size() > 0) {

                int size = rtgsOwReturned.size();
                sb.append(", ");
                int count = 1;
                for(Iterator i = rtgsOwReturned.entrySet().iterator(); i.hasNext(); ) {
                    Map.Entry entry = (Map.Entry)i.next();
                    String value = (String)entry.getValue();
                    String[] isComAlt = value.split(",");
                    String result = appendFieldString((String)entry.getKey(), "RTGS", Integer.parseInt(isComAlt[0]), Integer.parseInt(isComAlt[1]));
                    sb.append(" " + result);
                    if(count != size) {
                        sb.append(", ");
                        count++;
                    }
                }
            }

            if (inputDTO.getValueDate() != null) {

                SimpleDateFormat appFormat = new SimpleDateFormat("dd-MMM-yyyy");
                Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                    sb.append(" FROM MESSAGE_VW m, RTGS_MESSAGE_VW rm   ");
                } else {

                    sb.append(" FROM MESSAGE m, RTGS_MESSAGE rm   ");
                }
            } else {

                sb.append(" FROM MESSAGE m, RTGS_MESSAGE rm   ");
            }

            // Related to Inward Sender Bank Specific.
            if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                            .equalsIgnoreCase("ALL") ) {

                sb.append(" , IFSCMASTER im, BANKMASTER bm ");
            }

            sb.append(" WHERE  rm.msg_id = m.msg_id AND rm.tran_type = 'inward' AND m.msg_sub_type = 'R42'");

              // Related to Inward Sender Bank Specific.
              if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                              .equalsIgnoreCase("ALL") ) {

                  sb.append(" AND bm.ID = im.bank_master_id ");
              }

            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                sb.append(" and rm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ");
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
            if(fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0){
                sb.append(" and rm.AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount());
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                sb.append(" and rm.AMOUNT >= " + inputDTO.getFromAmount());
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                sb.append(" and rm.AMOUNT <= " + inputDTO.getToAmount());
            }

            //Related to Inward Receiver Branch/IFSC Specific.
            if (inputDTO.getReceiverIfscId() > 0 ) {

                sb.append(" and rm.IFSC_Master_ID = " + inputDTO.getReceiverIfscId());
            }

            //Related to Inward Sender Bank Specific.
            if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                            .equalsIgnoreCase("ALL") ) {

                sb.append(" and bm.CODE = '" + inputDTO.getSenderBank() + "'");
            }

            //Related to Inward Sender Branch/IFSC Specific.
            if (inputDTO.getSenderIfscId() > 0 ) {

                sb.append(" and SUBSTR(rm.sender_address,0 , 11) = ( SELECT IFSC FROM IFSCMASTER WHERE ID = " + inputDTO.getSenderIfscId() + " )" );
            }

            if(rtgsOwReturned != null  && rtgsOwReturned.size() > 0) {

                int size = rtgsOwReturned.size();
                sb.append("AND ( ");
                int count = 1;
                for(Iterator i = rtgsOwReturned.entrySet().iterator(); i.hasNext(); ) {
                    Map.Entry entry = (Map.Entry)i.next();
                    String value = (String)entry.getValue();
                    String[] isComAlt = value.split(",");
                    String result = appendWhereString((String)entry.getKey(), "RTGS", Integer.parseInt(isComAlt[0]), Integer.parseInt(isComAlt[1]));
                    sb.append(" " + result);
                    if(count != size) {
                        sb.append(" OR ");
                        count++;
                    }
                }
                sb.append(" ) ");
            }

            sb.append(" AND rm.STATUS <> 700 ) ORDER BY BUSINESS_DATE ASC, utr_no ASC ");
            logger.info("Query for getting the outward Returned : " + sb.toString());
            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();

            while(rs.next()) {

                ReportDTO reportDTO = new ReportDTO();

                //Populate the ReportDTO.
                reportDTO.setUtrNo(rs.getString("UTR_NO"));
                if(rtgs_ow_retn_search_field_arr != null && rtgs_ow_retn_search_field_arr.length > 0) {

                    for (int i = 0; i < rtgs_ow_retn_search_field_arr.length; i++) {

                        String owResult = rs.getString("RR_" + rtgs_ow_retn_search_field_arr[i]);
                        if(owResult != null && (owResult.length() > 0 && owResult.length() == 16)) {

                            reportDTO.setOutUTRNo(owResult);
                            break;
                        }
                    }

                }
                reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                reportDTO.setValueDate(InstaReportUtil
                                       .indianFormatDate(rs.getDate("VALUE_DATE")));
                reportDTO.setFieldA7495(rs.getString("A7495"));
                reportDTO.setFieldI7495(rs.getString("I7495"));
//                reportDTO.setAmount(rs.getDouble("AMOUNT"));
                reportDTO.setAmt(rs.getString("AMOUNT"));

                reportList.add(reportDTO);
                addedUtr.add(reportDTO.getUtrNo());
            }

            release(ps, rs);

            StringBuffer keyWordsQuery = new StringBuffer(4028);

            if (inputDTO.getValueDate() != null) {

                SimpleDateFormat appFormat = new SimpleDateFormat("dd-MMM-yyyy");
                Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(inputDTO.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
                    keyWordsQuery.append("SELECT DISTINCT rm.utr_no, rm.sender_address, rm.receiver_address,")
                    .append("Getcompoundfieldvalue (rm.msg_id, '7495', 'I7495', m.msg_sub_type) i7495, rm.BUSINESS_DATE, ")
                    .append("Getcompoundfieldvalue (rm.msg_id, '7495', 'A7495', m.msg_sub_type) a7495, rm.amount, rm.value_date ")
                    .append("FROM MESSAGE_VW m, RTGS_MESSAGE_VW rm   ");
                } else {

                    keyWordsQuery.append("SELECT DISTINCT rm.utr_no, rm.sender_address, rm.receiver_address,")
                    .append("Getcompoundfieldvalue (rm.msg_id, '7495', 'I7495', m.msg_sub_type) i7495, rm.BUSINESS_DATE, ")
                    .append("Getcompoundfieldvalue (rm.msg_id, '7495', 'A7495', m.msg_sub_type) a7495, rm.amount, rm.value_date ")
                    .append("FROM MESSAGE m, RTGS_MESSAGE rm   ");
                }
            } else {

                keyWordsQuery.append("SELECT DISTINCT rm.utr_no, rm.sender_address, rm.receiver_address,")
                .append("Getcompoundfieldvalue (rm.msg_id, '7495', 'I7495', m.msg_sub_type) i7495, rm.BUSINESS_DATE, ")
                .append("Getcompoundfieldvalue (rm.msg_id, '7495', 'A7495', m.msg_sub_type) a7495, rm.amount, rm.value_date ")
                .append("FROM MESSAGE m, RTGS_MESSAGE rm   ");
            }


            // Related to Inward Sender Bank Specific.
            if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                            .equalsIgnoreCase("ALL") ) {

                keyWordsQuery.append(" ,IFSCMASTER im, BANKMASTER bm ");
            }

            keyWordsQuery.append(" WHERE  rm.msg_id = m.msg_id AND rm.tran_type = 'inward' AND m.msg_sub_type = 'R42' ");

              // Related to Inward Sender Bank Specific.
              if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                              .equalsIgnoreCase("ALL") ) {

                  keyWordsQuery.append(" AND bm.ID = im.bank_master_id ");
              }

            if (inputDTO.getValueDate() != null && inputDTO.getToDate() != null ) {

                keyWordsQuery.append(" and rm.BUSINESS_DATE BETWEEN '" + inputDTO.getValueDate()
                                             + "' and '" + inputDTO.getToDate() + "' ");
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

                keyWordsQuery.append(" and rm.AMOUNT BETWEEN " + inputDTO.getFromAmount() + " and "
                                                    + inputDTO.getToAmount());
//            } else if (inputDTO.getFromAmount() > 0) {
            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {

                keyWordsQuery.append(" and rm.AMOUNT >= " + inputDTO.getFromAmount());
//            } else if (inputDTO.getToAmount() > 0) {
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {
                keyWordsQuery.append(" and rm.AMOUNT <= " + inputDTO.getToAmount());
            }

            //Related to Inward Receiver Branch/IFSC Specific.
            if (inputDTO.getReceiverIfscId() > 0 ) {

                keyWordsQuery.append(" and rm.IFSC_Master_ID = " + inputDTO.getReceiverIfscId());
            }

            //Related to Inward Sender Bank Specific.
            if (inputDTO.getSenderBank() !=null && !inputDTO.getSenderBank()
                                                            .equalsIgnoreCase("ALL") ) {

                keyWordsQuery.append(" and bm.CODE = '" + inputDTO.getSenderBank() + "'");
            }

            //Related to Inward Sender Branch/IFSC Specific.
            if (inputDTO.getSenderIfscId() > 0 ) {

                keyWordsQuery.append(" and SUBSTR(rm.sender_address, 0, 11) = (SELECT IFSC FROM IFSCMASTER WHERE ID = " + inputDTO.getSenderIfscId() + " )");
            }

            String[] key = InstaDefaultConstants.OW_RETN_SEARCH_KEYWORDS.split(",");

            if(key.length > 0) {

                keyWordsQuery.append(" AND ( " );
            }
            for (int i = 0; i < key.length; i++ ) {

                keyWordsQuery.append(" UPPER(Concat7495field (rm.msg_id, '7495', m.msg_sub_type)) LIKE '%" + key[i] + "%' ");

                if(i < (key.length - 1)) {

                    keyWordsQuery.append(" OR ");
                }
            }

            keyWordsQuery.append(" ) AND rm.STATUS <> 700 ORDER BY rm.BUSINESS_DATE ASC, rm.utr_no ASC ");

            ps = con.prepareStatement(keyWordsQuery.toString());
            rs = ps.executeQuery();

            while(rs.next()) {

                String utr = rs.getString("UTR_NO");
                if(!addedUtr.contains(utr)) {

                    ReportDTO reportDTO = new ReportDTO();

                    //Populate the ReportDTO.
                    reportDTO.setUtrNo(rs.getString("UTR_NO"));
                    reportDTO.setOutUTRNo("Not Referred");
                    reportDTO.setSenderAddress(rs.getString("SENDER_ADDRESS"));
                    reportDTO.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
                    reportDTO.setValueDate(InstaReportUtil
                                           .indianFormatDate(rs.getDate("VALUE_DATE")));
                    reportDTO.setFieldA7495(rs.getString("A7495"));
                    reportDTO.setFieldI7495(rs.getString("I7495"));
//                    reportDTO.setAmount(rs.getDouble("AMOUNT"));
                    reportDTO.setAmt(rs.getString("AMOUNT"));

                    reportList.add(reportDTO);
                    addedUtr.add(reportDTO.getUtrNo());
                }
            }


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
     * Method to generate user level events report
     */
    public Message generateUserLevelEventReport(Message msg)
    throws BOException {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        final String selUserEvents = "SELECT actor,datetime,info FROM AUDITLOG WHERE actor = ? and to_date(datetime) between  TO_DATE(?) and TO_DATE(?) ";
        List<ReportDTO> userEventList = new ArrayList<ReportDTO>();
        Message res = new Message();
        try {

            ps = con.prepareStatement(selUserEvents);
            ps.setString(1, input.getUserId().substring(input.getUserId().indexOf("-")+1));
            ps.setString(2, input.getValueDate());
            ps.setString(3, input.getToDate());
            rs = ps.executeQuery();
            while (rs.next()) {

                ReportDTO dto = new ReportDTO();
                dto.setUserId(rs.getString(1));
                dto.setValueDate(rs.getString(2));
                dto.setAuditInfo(rs.getString(3));
                userEventList.add(dto);
            }
            res.info = userEventList;
            return res;
        } catch(Throwable th) {
            logger.error("Exception while generating UserLevelEventReport : "
                        + th.getMessage());
             throw new BOException("Exception while generating UserLevelEventReport."
                             , th);
        }finally {

            release(ps, rs);
        }
    }

    /**
     * Method to genertae email Info Report
     */
    public Message generateEmailInfoReport(Message msg)
    throws BOException {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement selPs = null;
        ResultSet selRs = null;
        String tableName = "";
        if(input.getChannel().equalsIgnoreCase("RTGS")) {
            tableName = "rtgs_message";
        } else if (input.getChannel().equalsIgnoreCase("NEFT")) {
            tableName = "neft_message";
        }
        String selUserEvents = "select date_time,remarks,channel,to_address from email_info where msg_id = ?";
        String selUtr = " SELECT rm.utr_no,m.msg_id,rm.sender_address,rm.receiver_address,rm.tran_type FROM "+tableName+" rm,MESSAGE m WHERE m.msg_id = rm.msg_id ";

        if (input.getChannel() != null && input.getChannel().trim().length() > 0) {
            selUtr += " AND m.msg_channel_name= '"+input.getChannel()+"'";
        }
        if (input.getTransactionType() != null) {
            selUtr  += " AND rm.tran_type= '"+input.getTransactionType()+"'";
        }
        if (input.getValueDate() != null && input.getToDate() != null ) {

            selUtr += " AND rm.business_date between '" + input.getValueDate()
                                         + "' and '" + input.getToDate() + "' ";
        }
        if (input.getPaymentType() != null && !input.getPaymentType()
                                                        .equalsIgnoreCase("ALL")) {
            selUtr += " AND m.msg_sub_type = '"+input.getPaymentType()+"'";
        }
        List<ReportDTO> userEventList = new ArrayList<ReportDTO>();
        Message res = new Message();
        try {

            if (input.getIfscId() > 0) {
                selUtr += " AND rm.ifsc_master_id="+ input.getIfscId();
            }
            ps = con.prepareStatement(selUtr);
            rs = ps.executeQuery();
            while (rs.next()) {

                ReportDTO dto = new ReportDTO();
                dto.setUtrNo(rs.getString(1));
                long msgId = rs.getLong(2);
                dto.setSenderAddress(rs.getString(3));
                dto.setReceiverAddress(rs.getString(4));
                dto.setTranType(rs.getString(5));
                dto.setMsgId(msgId);
                selPs = con.prepareStatement(selUserEvents);
                selPs.setLong(1,msgId);
                selRs = selPs.executeQuery();
                while (selRs.next()) {

                    //Below line commented on 15-Sep-2009 byto get date_time from db
                    //dto.setValueDate(InstaReportUtil.indianFormatDate(selRs.getDate(1)));
                    dto.setValueDate(selRs.getString(1));
                    dto.setRemarks(selRs.getString(2));
                    dto.setChannel(selRs.getString(3));
                    dto.setToAddress(selRs.getString(4));
                    userEventList.add(dto);
                }
            }
            res.info = userEventList;
            return res;
        } catch(Throwable th) {
            logger.error("Exception while generating Email EventReport : "
                        + th.getMessage());
             throw new BOException("Exception while generating Email EventReport."
                             , th);
        }finally {

            release(ps, rs);
        }
    }

    /**
     * Method to generate IDL utilizatuion report
     */
    public Message generateIDLUtilizationReport(Message msg)
    throws BOException {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //PreparedStatement selPs = null;
        //ResultSet selRs = null;
        String tableName = null;
        final String selIDLEvents = "select msg_id,date_time,remarks,channel,message from email_info where log_date between ? and ? " +
                                    "and mail_subject like '%"+ReportConstants.MAILSUBJECT+"%'";
        //final String selAmt = "select amount,utr_no from "+tableName+" where msg_id = ?";
        List<ReportDTO> userEventList = new ArrayList<ReportDTO>();
        Message res = new Message();
        try {

            ps = con.prepareStatement(selIDLEvents);
            ps.setString(1, input.getValueDate());
            ps.setString(2, input.getToDate());
            rs = ps.executeQuery();
            while (rs.next()) {

                ReportDTO dto = new ReportDTO();
                dto.setMsgId(rs.getLong(1));
                dto.setValueDate(rs.getString(2));
                dto.setRemarks(rs.getString(3));
                String channel = rs.getString(4);

                Blob blob = rs.getBlob(5);
                byte[] data = blob.getBytes(1, (int) blob.length());
                String message = new String(data);
                int start = message.indexOf("Business date");
                int end = message.indexOf("**");
                message = message.substring(start,end).trim();
                dto.setMessage(message);
//                selPs = con.prepareStatement(selAmt);
//                selPs.setLong(1, dto.getMsgId());
//                selRs = selPs.executeQuery();
//                while (selRs.next()) {
//                    dto.setAmt(selRs.getString(1));
//                    dto.setUtrNo(selRs.getString(2));
//                    dto.setMessage(message);
//                }

                userEventList.add(dto);
            }
            res.info = userEventList;
            return res;
        } catch(Throwable th) {
            logger.error("Exception while generating Email EventReport : "
                        + th.getMessage());
             throw new BOException("Exception while generating Email EventReport."
                             , th);
        } finally {

            release(ps, rs);
        }
    }

    /**
     * Method to get Future Dated txns  Report
     * @param msg
     * @return message
     * @throws BOException
     */
    public Message generateFutureDatedTxnsReport(Message msg)
    throws BOException {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>();
        Map<String, List<ReportDTO>> reportMap = new LinkedHashMap<String, List<ReportDTO>>();
        Message res = new Message();
        String  msgListSQL =
                        " SELECT RM.MSG_ID, RM.UTR_NO, RM.STATUS, RM.SENDER_ADDRESS, RM.RECEIVER_ADDRESS,     " +
                        " RM.AMOUNT, MD.TYPE, MD.SUB_TYPE, RM.TRAN_TYPE, RM.ENTRY_BY, RM.PASS_BY, " +
                        " RM.REMARKS, RM.ERROR_REMARKS, RM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, RM.BUSINESS_DATE, RM.VALUE_DATE " +
                        " FROM RTGS_MESSAGE RM, MSGDEFN MD, MESSAGE M, RTGS_STATUS RS                " +
                        " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID    ";
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(input.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               msgListSQL =
                        " SELECT RM.MSG_ID, RM.UTR_NO, RM.STATUS, RM.SENDER_ADDRESS, RM.RECEIVER_ADDRESS,     " +
                        " RM.AMOUNT, MD.TYPE, MD.SUB_TYPE, RM.TRAN_TYPE, RM.ENTRY_BY, RM.PASS_BY, " +
                        " RM.REMARKS, RM.ERROR_REMARKS, RM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, RM.BUSINESS_DATE, RM.VALUE_DATE " +
                        " FROM RTGS_MESSAGE_vw RM, MSGDEFN MD, MESSAGE_vw M, RTGS_STATUS RS                " +
                        " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID    ";
                }
            }
            if (input.getPaymentType() != null){

                msgListSQL = msgListSQL + " AND MD.SUB_TYPE ='"  + input.getPaymentType()  + "'";
            }
            if (input.getStatus() != null) {

                msgListSQL = msgListSQL + " AND RM.STATUS =  " + input.getStatus();
            }
            if(!InstaDefaultConstants.COIFSCCode.equalsIgnoreCase(input.getIfscCode())) {

                msgListSQL = msgListSQL + " AND RM.IFSC_MASTER_ID = " + input.getIfscId();
            }

            msgListSQL = msgListSQL + " AND RM.IS_FUTURE_DATE_TXN = 1 AND  " +
                         "RM.BUSINESS_DATE BETWEEN '"+input.getValueDate()+ "' AND '" +input.getToDate()+"'";
            msgListSQL = msgListSQL + " ORDER BY RM.BUSINESS_DATE ASC ";

            logger.info("Query for getting the Future date Message List : " + msgListSQL);
            ps = con.prepareStatement(msgListSQL);
            rs = ps.executeQuery();
            String prevDate = "";
            String parentfield5561 = "5561";
            String actualFieldA5561 = "A5561";
            String actualFieldN5561 = "N5561";
            String parentfield7495 = "7495";
            String actualFieldI7495 = "I7495";
            String actualFieldA7495 = "A7495";
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

                if (input.getPaymentType() != null && input.getPaymentType().equalsIgnoreCase("R41")){

                    String A5561 = getFieldValues(msgId,input.getPaymentType(),parentfield5561,actualFieldA5561);
                    String N5561 = getFieldValues(msgId,input.getPaymentType(),parentfield5561,actualFieldN5561);
                    if(A5561 != null && A5561.indexOf("/") != -1) {

                        A5561 = A5561.replace("/","");
                    } else if(A5561 == null) {

                        A5561 = "";
                    }
                    reportDTO.setFieldA5561(A5561);
                    reportDTO.setFieldN5561(N5561);
                } else if (input.getPaymentType() != null && input.getPaymentType().equalsIgnoreCase("R42")) {

                    String I7495 = getFieldValues(msgId,input.getPaymentType(),parentfield7495,actualFieldI7495);
                    String A7495 = getFieldValues(msgId,input.getPaymentType(),parentfield7495,actualFieldA7495);
                    reportDTO.setFieldI7495(I7495);
                    reportDTO.setFieldA7495(A7495);
                }
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
     * Method to get Exception Report
     * @param msg
     * @return message
     * @throws BOException
     */
    public Message generateExceptionReport(Message msg)
    throws BOException {

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
                        " FROM RTGS_MESSAGE RM, MSGDEFN MD, MESSAGE M, RTGS_STATUS RS                " +
                        " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID    ";
        ReportDTO reportDTO;
        BigDecimal fromAmount = new BigDecimal(input.getFromAmount());
        BigDecimal toAmount = new BigDecimal(input.getToAmount());
        try {

            if (input.getValueDate() != null && input.getToDate() != null ) {

                msgListSQL += " and rm.BUSINESS_DATE BETWEEN '" + input.getValueDate()
                                             + "' and '" + input.getToDate() + "' ";
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

            if (input.getValueDate() != null) {

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(input.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               msgListSQL =
                        " SELECT RM.MSG_ID, RM.UTR_NO,RM.STATUS,RS.NAME, RM.SENDER_ADDRESS, RM.RECEIVER_ADDRESS,     " +
                        " RM.AMOUNT, MD.TYPE, MD.SUB_TYPE, RM.TRAN_TYPE, RM.ENTRY_BY, RM.PASS_BY, " +
                        " RM.REMARKS, RM.ERROR_REMARKS, RM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, RM.BUSINESS_DATE, RM.VALUE_DATE " +
                        " FROM RTGS_MESSAGE_vw RM, MSGDEFN MD, MESSAGE_vw M, RTGS_STATUS RS                " +
                        " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID    ";

                         logger.info("Data fetching from archive database schema..");

                } else {

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                    logger.info("Data fetching from Local Database schema...");
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

            logger.info("Query for getting the Future date Message List : " + msgListSQL);
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

            logger.error("Exception while generating Future Dated Txns Report :"
                         + th.getMessage());
            throw new BOException("Exception while generating Future Dated Txns Report"
                                + th);
        } finally {
            release(ps, rs);
        }
    }

    //Code Added on 25-Sep-2009 by Mohana for System Level Events report
    /**
     * Method to generate System Level Events Report
     * @param msg
     * @return message
     * @throws BOException
     */
    public Message generateSystemLevelEventsReport(Message msg)
    throws BOException {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        String selSystemEvents = " SELECT actor,datetime,info FROM AUDITLOG WHERE " +
                                 " TYPE ='lms.system.events' and to_date(datetime) between  TO_DATE(?) and TO_DATE(?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReportDTO> userEventList = new ArrayList<ReportDTO>();
        Message res = new Message();
        try {

            ps = con.prepareStatement(selSystemEvents);
            ps.setString(1, input.getValueDate());
            ps.setString(2, input.getToDate());
            rs = ps.executeQuery();
            while (rs.next()) {

                ReportDTO dto = new ReportDTO();
                dto.setUserId(rs.getString(1));
                dto.setValueDate(rs.getString(2));
                dto.setAuditInfo(rs.getString(3));
                userEventList.add(dto);
            }
            res.info = userEventList;
        } catch(Throwable th) {

            logger.error("Exception while generating System Level Events Report :"
                         + th.getMessage());
            throw new BOException("Exception while generating System Level Events Report"
                                + th);
        } finally {
            release(ps, rs);
        }
        return res;
    }

    //  Code Added on 28-Sep-2009 by Mohana for ReturnPaymentRejectedReport
    /**
     * Method to get inward possible return payment rejected by user report
     * @param msg
     * @return message
     * @throws BOException
     *
     */
    public Message generateReturnPaymentRejectedReport(Message msg)
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
                                " FROM RTGS_MESSAGE RM, MSGDEFN MD, MESSAGE M, RTGS_STATUS RS                " +
                                " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID " +
                                " AND RM.AUTORETURN = 1 AND RM.STATUS NOT IN (900,500)";
        BigDecimal fromAmount = new BigDecimal(input.getFromAmount());
        BigDecimal toAmount = new BigDecimal(input.getToAmount());
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(input.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               selReturnPayment = " SELECT RM.MSG_ID, RM.UTR_NO,RM.STATUS,RS.NAME, RM.SENDER_ADDRESS, RM.RECEIVER_ADDRESS,     " +
                                " RM.AMOUNT, MD.TYPE, MD.SUB_TYPE, RM.TRAN_TYPE, RM.ENTRY_BY, RM.PASS_BY, " +
                                " RM.REMARKS, RM.ERROR_REMARKS, RM.LAST_MODIFIED_TIME, RS.NAME STATUSNAME, RM.BUSINESS_DATE, RM.VALUE_DATE " +
                                " FROM RTGS_MESSAGE_vw RM, MSGDEFN MD, MESSAGE_vw M, RTGS_STATUS RS                " +
                                " WHERE M.MSG_DEFN_ID = MD.ID AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID " +
                                " AND RM.AUTORETURN = 1 AND RM.STATUS NOT IN (900,500)";

                        logger.info("Data fetching from archive database schema..");

                } else {

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                    logger.info("Data fetching from Local Database schema...");
                }
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

            logger.error("Exception while generating RTGS Inward Possible Return Payment Rejected By User report :"+e);
            throw new BOException("Exception while generating RTGS Inward Possible Return Payment Rejected By User report:"+e);
        } finally {

            release(ps, rs);
        }
        return res;
    }

    /**
     * Method to get unsuccessful payment report action
     */
    public Message generateUnsuccessfulPaymentReport(Message msg)
    throws BOException {

        ReportInputDTO input = (ReportInputDTO)msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;
        List<ReportDTO> reportList = new ArrayList<ReportDTO>(0);
        List<ReportDTO> piList = new ArrayList<ReportDTO>(0);
        List<ReportDTO> ssnList = new ArrayList<ReportDTO>(0);
        Message res = new Message();
        String selUnsuccessMsg = " SELECT * FROM RTGS_MESSAGE RM,MESSAGE M, "+
                                 " MSGDEFN MD, RTGS_STATUS RS WHERE M.MSG_DEFN_ID = MD.ID " +
                                 " AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID AND RM.STATUS = 2900 ";
        String selSsnResponse  = " SELECT M.MSG_ID FROM RTGS_MESSAGE RM, MESSAGE M, MSGDEFN MD WHERE RM.MSG_ID = M.MSG_ID " +
                                 " AND M.MSG_DEFN_ID = MD.ID AND RM.UTR_NO = ? AND M.MSG_SUB_TYPE = 'R09'";
        String selPiResponse  = " SELECT M.MSG_ID FROM RTGS_MESSAGE RM, MESSAGE M, MSGDEFN MD WHERE RM.MSG_ID = M.MSG_ID " +
                                " AND M.MSG_DEFN_ID = MD.ID AND RM.UTR_NO = ? AND M.MSG_SUB_TYPE = 'R90'";
        PreparedStatement ssnPs = null;
        ResultSet ssnRs = null;
        PreparedStatement piPs = null;
        ResultSet piRs = null;

        BigDecimal fromAmount = new BigDecimal(input.getFromAmount());
        BigDecimal toAmount = new BigDecimal(input.getToAmount());
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

                java.sql.Date availableDataStartDate = getAvailableDataStartDate();

                if (new Date(input.getValueDate()).compareTo(availableDataStartDate) < 0) {

                    //archive database schema
               selUnsuccessMsg = " SELECT * FROM RTGS_MESSAGE_vw RM,MESSAGE_vw M, "+
                                 " MSGDEFN MD, RTGS_STATUS RS WHERE M.MSG_DEFN_ID = MD.ID " +
                                 " AND RM.MSG_ID = M.MSG_ID AND RM.STATUS = RS.ID AND RM.STATUS = 2900 ";
               selSsnResponse  = " SELECT M.MSG_ID FROM RTGS_MESSAGE_vw RM, MESSAGE_vw M, MSGDEFN MD WHERE RM.MSG_ID = M.MSG_ID " +
                                 " AND M.MSG_DEFN_ID = MD.ID AND RM.UTR_NO = ? AND M.MSG_SUB_TYPE = 'R09'";
                selPiResponse  = " SELECT M.MSG_ID FROM RTGS_MESSAGE_vw RM, MESSAGE_vw M, MSGDEFN MD WHERE RM.MSG_ID = M.MSG_ID " +
                                 " AND M.MSG_DEFN_ID = MD.ID AND RM.UTR_NO = ? AND M.MSG_SUB_TYPE = 'R90'";


                        logger.info("Data fetching from archive database schema..");

                } else {

                        logger.info("Data fetching from Local Database schema...");
                    }
                } else {

                    logger.info("Data fetching from Local Database schema...");
                }
            if (input.getValueDate() != null && input.getToDate() != null ) {

                selUnsuccessMsg += " and RM.BUSINESS_DATE BETWEEN '" + input.getValueDate()
                                             + "' and '" + input.getToDate() + "' ";
            }
            if (input.getPaymentType() != null && !input.getPaymentType()
                                                            .equalsIgnoreCase("ALL")){

                selUnsuccessMsg += " and MD.SUB_TYPE ='"  + input.getPaymentType()  + "'";
            }
            if (input.getHostType() != null && !input.getHostType()
                .equalsIgnoreCase("ALL")) {

                selUnsuccessMsg += " and rm.MSG_SOURCE = '" + input.getHostType() +"'" ;
            }

            if (fromAmount.compareTo(BigDecimal.ZERO) > 0 && toAmount.compareTo(BigDecimal.ZERO) > 0 ) {

                selUnsuccessMsg += " and rm.AMOUNT BETWEEN " + input.getFromAmount() + " and "
                + input.getToAmount();

            } else if (fromAmount.compareTo(BigDecimal.ZERO) > 0) {
                selUnsuccessMsg += " and rm.AMOUNT >= " + input.getFromAmount();
            } else if (toAmount.compareTo(BigDecimal.ZERO) > 0) {

                selUnsuccessMsg += " and rm.AMOUNT <= " + input.getToAmount();
            }
            //Related to Branch/IFSC Specific.
            if (input.getIfscId() > 0 ) {

                selUnsuccessMsg += " and rm.IFSC_Master_ID = " + input.getIfscId();
            }
            selUnsuccessMsg = selUnsuccessMsg + " ORDER BY RM.BUSINESS_DATE ASC ";

            ps = con.prepareStatement(selUnsuccessMsg);
            rs = ps.executeQuery();
            long msgId = 0;
            String statusName = "";
            while(rs.next()) {

                long currStatus = rs.getLong("STATUS");
                /*
                 * This is to group the records based on the Branches.
                 */
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
                reportDTO.setRemarks(rs.getString("ERROR_REMARKS") == null ? "":rs.getString("ERROR_REMARKS"));
                ssnPs = con.prepareStatement(selSsnResponse);
                ssnPs.setString(1, reportDTO.getUtrNo());
                ssnRs = ssnPs.executeQuery();
                if (ssnRs.next()) {

                    long ssnMsgId = ssnRs.getLong(1);
                    reportDTO.setResponseType(getFieldValueByNo(ssnMsgId,"R09","6450"));
                    ssnList.add(reportDTO);
                } else {

                    piPs = con.prepareStatement(selPiResponse);
                    piPs.setString(1, reportDTO.getUtrNo());
                    piRs = piPs.executeQuery();
                    if (piRs.next()) {

                        long piMsgId = piRs.getLong(1);
                        reportDTO.setResponseType(getFieldValueByNo(piMsgId,"R90","1076"));
                        piList.add(reportDTO);
                    }
                }
            }
            if (input.getResponse().equalsIgnoreCase("R90")) {
                reportList = piList;
            } else {
                reportList = ssnList;
            }
            res.info = reportList;
            return res;
       } catch(Throwable th) {

            logger.error("Exception while generating Unsuccessful Payment Report :"
                         + th.getMessage());
            throw new BOException("Exception while generating Unsuccessful Payment Report"
                                + th);
       } finally {
            release(ps, rs);
            release(ssnPs,ssnRs);

       }
    }

    /**
     * Method to get specific field values using field no
     */
    public String getStatusName(long status)
    throws Exception {

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
            throw new Exception("Getting Exception While Fetching the status description :",
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
    public java.sql.Date getAvailableDataStartDate(){

        final String getBusinessDateSQL = " SELECT parameter_value FROM rtgsparameters " +
                                          " WHERE parameter_name = 'AVAILABLE_DATA_START_DATE' ";

        String availableDataStartDate = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat userDateFmt = new SimpleDateFormat("dd-MM-yyyy");

        try {

            logger.info("Query to Fetch AVAILABLE_DATA_START_DATE from RTGSPARAMETERS : "+getBusinessDateSQL);
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

            release(ps, rs);
        }
    }

    /**
     * Method for generating the RTGS Beneficiary Account Details Report
     *
     */
    //Beneficiary Account Details, Joe.M, 20130605
    //Starts
    @SuppressWarnings("unchecked")
    public Message generateRTGSBeneficiaryAccountReport(Message msg)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO =null;

        List rtgsBenefList = new ArrayList<ReportDTO>();
        try {

            StringBuffer beneficiaryQueryString = new StringBuffer();

            beneficiaryQueryString.append(" SELECT   rtg.utr_no, b.foracid cbs_accno, b.acct_name cbs_account_name,  ");
            beneficiaryQueryString.append("         rtg.account_name lms_account_name,rtg.amount  ");
            beneficiaryQueryString.append("    FROM (SELECT   b.ifsc, a.msg_id,  ");
            beneficiaryQueryString.append("                   MAX (DECODE (d.NO, 'N5561', c.VALUE)) account_name,  ");
            beneficiaryQueryString.append("                   MAX (DECODE (d.NO, 'F20', c.VALUE)) utr_no,  ");
            beneficiaryQueryString.append("                   MAX (DECODE (d.NO, 'A5561', c.VALUE)) account_no,  ");
            beneficiaryQueryString.append("                   MAX (DECODE (d.NO, 'A4488', c.VALUE)) amount  ");
            beneficiaryQueryString.append("              FROM rtgs_message a JOIN ifscmaster b ON a.ifsc_master_id = b.ID  ");
            beneficiaryQueryString.append("                   JOIN MESSAGE m ON a.msg_id = m.msg_id  ");
            beneficiaryQueryString.append("                   JOIN msgfield_stage c ON a.msg_id = c.msg_id  ");
            beneficiaryQueryString.append("                   JOIN msgfieldtype d ON c.msg_field_type_id = d.ID  ");
            if(inputDTO.getBifsc().equalsIgnoreCase("ALL")){
                beneficiaryQueryString.append("               WHERE a.value_date between ? and ?  ");
            } else {
                beneficiaryQueryString.append("               WHERE a.value_date between ? and ?  ");
                beneficiaryQueryString.append("               AND ifsc = ?  ");
            }
                beneficiaryQueryString.append("               AND m.msg_type = 298  ");
                beneficiaryQueryString.append("               AND m.msg_sub_type = 'R41'  ");
                beneficiaryQueryString.append("               AND d.NO IN ('A5561', 'N5561', 'A4488', 'F20')  ");
                beneficiaryQueryString.append("          GROUP BY ifsc, a.msg_id) rtg LEFT OUTER JOIN cbs_acc b ON rtg.account_no = b.foracid  ");
                beneficiaryQueryString.append(" ORDER BY rtg.msg_id  ");

            ps = con.prepareStatement(beneficiaryQueryString.toString());
            if(inputDTO.getBifsc().equalsIgnoreCase("ALL")){
                ps.setString(1, inputDTO.getValueDate());
                ps.setString(2, inputDTO.getToDate());
            } else {
                ps.setString(1, inputDTO.getValueDate());
                ps.setString(2, inputDTO.getToDate());
                ps.setString(3, inputDTO.getBifsc());
            }

            rs = ps.executeQuery();

            while(rs.next()) {

                reportDTO = new ReportDTO();

                reportDTO.setRtgsUTR(rs.getString("UTR_NO"));
                reportDTO.setRtgsCBSAccountNumber(rs.getLong("CBS_ACCNO"));
                reportDTO.setRtgsCBSAccountName(rs.getString("CBS_ACCOUNT_NAME"));
                reportDTO.setRtgsAccountName(rs.getString("LMS_ACCOUNT_NAME"));
                reportDTO.setRtgsAmount(rs.getString("AMOUNT"));

                rtgsBenefList.add(reportDTO);
            }

        } catch(Throwable th) {

            logger.error("Exception while generating RTGS Beneficiary Account Details Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating RTGS Beneficiary Account Details Report : "
                                , th);
        } finally {

            release(ps, rs);
        }

        msg.info = rtgsBenefList;
        return msg;
    }
    //Beneficiary Account Details - Ends
}
