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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.objectfrontier.arch.dto.Message;
import com.objectfrontier.arch.server.ServerException;
import com.objectfrontier.insta.InstaDefaultConstants;
import com.objectfrontier.insta.reports.dto.ReportDTO;
import com.objectfrontier.insta.reports.dto.ReportInputDTO;
import com.objectfrontier.insta.reports.server.bo.InstaReportBO;



/**
 *
 * @author joeam
 * @date   Dec 1, 2012
 * @since  insta.reports; Dec 1, 2012
 */

public class InstaLCBGReportBO
extends InstaReportBO {

    //NEFT Child logger should be logged using the name neftLogger
    //static Logger logger = Logger.getLogger(InstaNEFTReportBO.class);
    static Logger logger = Logger.getLogger("neftLogger");

    public static final String TOTAL = "Total";
    public static final String OUTWARD_TRANSACTION_TYPE = "outward";
    public static final String INWARD_TRANSACTION_TYPE = "inward";
    public static final String BANKNAME = InstaDefaultConstants.BANKNAME;
    public static final String BANKNAMETEST = "Test";

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
    SimpleDateFormat userDateFmt = new SimpleDateFormat("dd-MM-yyyy");

    //LCBG - Inward Report - Starts, Joe.M
    /**
     * LCBG Report BO call
     *
     */
    @SuppressWarnings("unchecked")
    public Message generateLCBGInwardReport(Message msg)
    throws Exception {

        ReportInputDTO inputDTO = (ReportInputDTO) msg.info;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReportDTO reportDTO;

        List inwList = new ArrayList<ReportDTO>();
        try {

            ps = con.prepareStatement(generateLCBGInwardReportQuery());
            ps.setString(1, inputDTO.getValueDate());
            ps.setString(2, inputDTO.getToDate());
            ps.setString(3, inputDTO.getValueDate());
            ps.setString(4, inputDTO.getToDate());

            rs = ps.executeQuery();

            while(rs.next()) {

                reportDTO = new ReportDTO();

                reportDTO.setLcbgDate(rs.getString("VALUE_DATE").substring(0, 11));
                reportDTO.setInwardInprocess(rs.getInt("INWINPROCESS"));
                reportDTO.setInwardCompleted(rs.getInt("INWCOMPLETED"));
                reportDTO.setOutwardInprocess(rs.getInt("OUTWINPROCESS"));
                reportDTO.setOutwardForAcknowledge(rs.getInt("OUTWFORACK"));
                reportDTO.setOutwardSettled(rs.getInt("OUTWSETTLED"));

               inwList.add(reportDTO);
            }

        } catch(Throwable th) {

            logger.error("Exception while generating LCBG Summary Report : "
                         + th.getMessage());
            throw new Exception("Exception while generating LCBG Summary Report."
                                , th);
        } finally {

            release(ps, rs);
        }

        msg.info = inwList;
        return msg;
    }

    /**
     * LCBG Query to fetch the counts
     */
    public String generateLCBGInwardReportQuery() {

        StringBuffer queryString = new StringBuffer();

        queryString.append("select value_date, ");
        queryString.append("  Sum(StIn1TranIn) + Sum(StOut1TranIn) inwInprocess,  ");
        queryString.append("  Sum(St0TranIn) inwCompleted,  ");
        queryString.append("  Sum(StIn1TranOut) + Sum(StOut1TranOut) outwInprocess,  ");
        queryString.append("  Sum(St2TranOut) outwForAck,   ");
        queryString.append("  Sum(St0TranOut) outwSettled  ");
        queryString.append("from (  ");
        queryString.append("     select inw.VALUE_DATE  ");
        queryString.append("            , Sum(Case When inw.status = 1 and inw.tran_type = 'inward' then 1 else 0 end) StIn1TranIn   ");
        queryString.append("            , 0 StOut1TranIn   ");
        queryString.append("            , Sum(Case When inw.status = 1 and inw.tran_type = 'outward' then 1 else 0 end) StIn1TranOut  ");
        queryString.append("            , 0 StOut1TranOut  ");
        queryString.append("            , 0 St0TranIn , 0 St2TranOut , 0 St0TranOut  ");
        queryString.append("       from  inward_lcbgmsgstage inw   ");
        queryString.append("       where inw.value_date between ? and ?  ");
        queryString.append("       Group by inw.value_date     ");
        queryString.append("     UNION ALL  ");
        queryString.append("      select  ");
        queryString.append("          outw.VALUE_DATE  ");
        queryString.append("          , 0 StIn1TranIn  ");
        queryString.append("          , Sum(Case When outw.status = 1 and outw.tran_type = 'inward' then 1 else 0 end) StOut1TranIn  ");
        queryString.append("          , 0 StIn1TranOut  ");
        queryString.append("          , Sum(Case When outw.status = 1 and outw.tran_type = 'outward' then 1 else 0 end) StOut1TranOut  ");
        queryString.append("          , Sum(Case When outw.status = 0 and outw.tran_type = 'inward' then 1 else 0 end) St0TranIn   ");
        queryString.append("          , Sum(Case When outw.status = 2 and outw.tran_type = 'outward' then 1 else 0 end) St2TranOut  ");
        queryString.append("          , Sum(Case When outw.status = 0 and outw.tran_type = 'outward' then 1 else 0 end) St0TranOut  ");
        queryString.append("        from outward_lcbgmsgstage outw  ");
        queryString.append("        where outw.value_date between ? and ?  ");
        queryString.append("        Group by outw.value_date   ");
        queryString.append(") TabData group by value_date order by value_date asc");

        return queryString.toString();
    }
    //LCBG - Inward Report - Ends, Joe.M


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
