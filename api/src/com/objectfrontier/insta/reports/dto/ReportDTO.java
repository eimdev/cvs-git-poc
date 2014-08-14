package com.objectfrontier.insta.reports.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author kannanm
 *
 */
public class ReportDTO implements Serializable {

	private static final long serialVersionUID  =200008L;

    private String utrNo;
//    private double amount;

    private String senderAddress;
    private String receiverAddress;
    private String valueDate;
    public Timestamp vDate;
    private int count;
    private String msgType;
    public String msgSubType;
//    public long fromAmount;
//    public long toAmount;
    public String fromAmount;
    public String toAmount;

    public String userId;
    public String tranType;
//    private double balance;
    private String balance;
    private String debitCredit;
    private String source;
    private String branch;
    private String bank;
    private String outUTRNo;
    private String status;
    public String toAddress;
	/**
     * Code added on 17-10-2008
     */
//    public double dateGrantTolal = 0.0;
    public String dateGrantTolal = "0.0";
    public String accNo;
    public String remitterName;
    public String beneficiaryName;
    public List branchwiseDTOs;
    public String ifsc;
    public String branchName;
    public String branchId;//RBC CMD 1.0
    public String branchCode;
    public Timestamp reportvalueDate;

//    public double creditSettlement;
//    public double debitSettlement;
    public String creditSettlement;
    public String debitSettlement;

//    public double dateCprInAmt = 0.0;
//    public double dateIprInAmt = 0.0;
//    public double dateCprOutAmt = 0.0;
//    public double dateIprOutAmt = 0.0;

    public String dateCprInAmt = "0.0";
    public String dateIprInAmt = "0.0";
    public String dateCprOutAmt = "0.0";
    public String dateIprOutAmt = "0.0";


    public String txnStatus;
    public String reportType;
    public String fieldA5561;
    public String fieldN5561;
    public String fieldA7495;
    public String fieldI7495;
    public String remarks;
    public String amt;
    public String batchTime;
    public String reshBatchTime;
    public String rejBatchTime;
    public String reshDate;
    public String rejDate;

    public String entryBy;
    public String passBy;
    public long msgId;
    public String channel;
    public String message;
    public String responseType;

    public String auditInfo;

    /** This is used to store completedTxnCount. */
    public int completedTxnCount;
    /**This is used to store completedTxnAmount. */
	public String completedTxnAmount;
	/**This is used to store rtnTxnCount. */
    public int rtnTxnCount;
    /**This is used to store rtnTxnAmount. */
    public String rtnTxnAmount;

    //LCBG - Starts, Joe.M
    /**
     * For LCBG
     *
     */
    public int inward;
    public int outward;
    public String lcbgDate;
    public int inwardInprocess;
    public int inwardCompleted;
    public int outwardInprocess;
    public int outwardForAcknowledge;
    public int outwardSettled;

    //LCBG - Ends, Joe.M

    //Beneficiary Account Number - Starts, Joe.M
    //RTGS
    public long rtgsAccountNumber;
    public String rtgsAccountName;
    public String rtgsAmount;
    public String rtgsUTR;
    public long rtgsCBSAccountNumber;
    public String rtgsCBSAccountName;

    //NEFT
    public long neftAccountNumber;
    public String neftAccountName;
    public String neftAmount;
    public String neftUTR;
    public long neftCBSAccountNumber;
    public String neftCBSAccountName;

    //public String bIfsc;
    //Beneficiary Account Number - Ends, Joe.M

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

//    public double getAmount() {
//		return amount;
//	}
//
//	public void setAmount(double amount) {
//		this.amount = amount;
//	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getUtrNo() {
		return utrNo;
	}

	public void setUtrNo(String utrNo) {
		this.utrNo = utrNo;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

//    public double getBalance() {
//        return balance;
//    }
//
//    public void setBalance(double balance) {
//        this.balance = balance;
//    }

	public String getDebitCredit() {
        return debitCredit;
    }

    public void setDebitCredit(String debitCredit) {
        this.debitCredit = debitCredit;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getOutUTRNo() {
        return outUTRNo;
    }

    public void setOutUTRNo(String outUTRNo) {
        this.outUTRNo = outUTRNo;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }


    public String getAccNo() {

        return accNo;
    }


    public void setAccNo(String accNo) {

        this.accNo = accNo;
    }


    public String getBeneficiaryName() {

        return beneficiaryName;
    }


    public void setBeneficiaryName(String beneficiaryName) {

        this.beneficiaryName = beneficiaryName;
    }


    public String getBranchCode() {

        return branchCode;
    }


    public void setBranchCode(String branchCode) {

        this.branchCode = branchCode;
    }


    public String getBranchId() {

        return branchId;
    }


    public void setBranchId(String branchId) {

        this.branchId = branchId;
    }


    public String getBranchName() {

        return branchName;
    }


    public void setBranchName(String branchName) {

        this.branchName = branchName;
    }


    public List getBranchwiseDTOs() {

        return branchwiseDTOs;
    }


    public void setBranchwiseDTOs(List branchwiseDTOs) {

        this.branchwiseDTOs = branchwiseDTOs;
    }


//    public double getCreditSettlement() {
//
//        return creditSettlement;
//    }
//
//
//    public void setCreditSettlement(double creditSettlement) {
//
//        this.creditSettlement = creditSettlement;
//    }

    public String getCreditSettlement() {

        return creditSettlement;
    }


    public void setCreditSettlement(String creditSettlement) {

        this.creditSettlement = creditSettlement;
    }


//    public double getDateCprInAmt() {
//
//        return dateCprInAmt;
//    }
//
//
//    public void setDateCprInAmt(double dateCprInAmt) {
//
//        this.dateCprInAmt = dateCprInAmt;
//    }

    public String getDateCprInAmt() {

        return dateCprInAmt;
    }


    public void setDateCprInAmt(String dateCprInAmt) {

        this.dateCprInAmt = dateCprInAmt;
    }


//    public double getDateCprOutAmt() {
//
//        return dateCprOutAmt;
//    }
//
//
//    public void setDateCprOutAmt(double dateCprOutAmt) {
//
//        this.dateCprOutAmt = dateCprOutAmt;
//    }

    public String getDateCprOutAmt() {

        return dateCprOutAmt;
    }


    public void setDateCprOutAmt(String dateCprOutAmt) {

        this.dateCprOutAmt = dateCprOutAmt;
    }


//    public double getDateGrantTolal() {
//
//        return dateGrantTolal;
//    }
//
//
//    public void setDateGrantTolal(double dateGrantTolal) {
//
//        this.dateGrantTolal = dateGrantTolal;
//    }

    public String getDateGrantTolal() {

        return dateGrantTolal;
    }


    public void setDateGrantTolal(String dateGrantTolal) {

        this.dateGrantTolal = dateGrantTolal;
    }


//    public double getDateIprInAmt() {
//
//        return dateIprInAmt;
//    }
//
//
//    public void setDateIprInAmt(double dateIprInAmt) {
//
//        this.dateIprInAmt = dateIprInAmt;
//    }

    public String getDateIprInAmt() {

        return dateIprInAmt;
    }


    public void setDateIprInAmt(String dateIprInAmt) {

        this.dateIprInAmt = dateIprInAmt;
    }


    public String getDateIprOutAmt() {

        return dateIprOutAmt;
    }


    public void setDateIprOutAmt(String dateIprOutAmt) {

        this.dateIprOutAmt = dateIprOutAmt;
    }


//    public double getDebitSettlement() {
//
//        return debitSettlement;
//    }
//
//
//    public void setDebitSettlement(double debitSettlement) {
//
//        this.debitSettlement = debitSettlement;
//    }

    public String getDebitSettlement() {

        return debitSettlement;
    }


    public void setDebitSettlement(String debitSettlement) {

        this.debitSettlement = debitSettlement;
    }


//    public long getFromAmount() {
//
//        return fromAmount;
//    }
//
//
//    public void setFromAmount(long fromAmount) {
//
//        this.fromAmount = fromAmount;
//    }

    public String getFromAmount() {

        return fromAmount;
    }


    public void setFromAmount(String fromAmount) {

        this.fromAmount = fromAmount;
    }


    public String getIfsc() {

        return ifsc;
    }


    public void setIfsc(String ifsc) {

        this.ifsc = ifsc;
    }


    public String getMsgSubType() {

        return msgSubType;
    }


    public void setMsgSubType(String msgSubType) {

        this.msgSubType = msgSubType;
    }


    public String getRemitterName() {

        return remitterName;
    }


    public void setRemitterName(String remitterName) {

        this.remitterName = remitterName;
    }


    public String getReportType() {

        return reportType;
    }


    public void setReportType(String reportType) {

        this.reportType = reportType;
    }


    public Timestamp getReportvalueDate() {

        return reportvalueDate;
    }


    public void setReportvalueDate(Timestamp reportvalueDate) {

        this.reportvalueDate = reportvalueDate;
    }


//    public long getToAmount() {
//
//        return toAmount;
//    }
//
//
//    public void setToAmount(long toAmount) {
//
//        this.toAmount = toAmount;
//    }

    public String getToAmount() {

        return toAmount;
    }


    public void setToAmount(String toAmount) {

        this.toAmount = toAmount;
    }


    public String getTxnStatus() {

        return txnStatus;
    }


    public void setTxnStatus(String txnStatus) {

        this.txnStatus = txnStatus;
    }


    public String getUserId() {

        return userId;
    }


    public void setUserId(String userId) {

        this.userId = userId;
    }


    public Timestamp getVDate() {

        return vDate;
    }


    public void setVDate(Timestamp date) {

        vDate = date;
    }


    public String getFieldA5561() {

        return fieldA5561;
    }


    public void setFieldA5561(String fieldA5561) {

        this.fieldA5561 = fieldA5561;
    }


    public String getFieldA7495() {

        return fieldA7495;
    }


    public void setFieldA7495(String fieldA7495) {

        this.fieldA7495 = fieldA7495;
    }


    public String getFieldI7495() {

        return fieldI7495;
    }


    public void setFieldI7495(String fieldI7495) {

        this.fieldI7495 = fieldI7495;
    }


    public String getFieldN5561() {

        return fieldN5561;
    }


    public void setFieldN5561(String fieldN5561) {

        this.fieldN5561 = fieldN5561;
    }


    public String getRemarks() {

        return remarks;
    }


    public void setRemarks(String remarks) {

        this.remarks = remarks;
    }


    public String getEntryBy() {

        return entryBy;
    }


    public void setEntryBy(String entryBy) {

        this.entryBy = entryBy;
    }


    public String getPassBy() {

        return passBy;
    }


    public void setPassBy(String passBy) {

        this.passBy = passBy;
    }


    public String getAmt() {

        return amt;
    }


    public void setAmt(String amt) {

        this.amt = amt;
    }


    public String getBatchTime() {

        return batchTime;
    }


    public void setBatchTime(String batchTime) {

        this.batchTime = batchTime;
    }


    public String getReshBatchTime() {

        return reshBatchTime;
    }


    public void setReshBatchTime(String reshBatchTime) {

        this.reshBatchTime = reshBatchTime;
    }


    public String getRejDate() {

        return rejDate;
    }


    public void setRejDate(String rejDate) {

        this.rejDate = rejDate;
    }


    public String getReshDate() {

        return reshDate;
    }


    public void setReshDate(String reshDate) {

        this.reshDate = reshDate;
    }


    public String getRejBatchTime() {

        return rejBatchTime;
    }


    public void setRejBatchTime(String rejBatchTime) {

        this.rejBatchTime = rejBatchTime;
    }


    public String getAuditInfo() {

        return auditInfo;
    }


    public void setAuditInfo(String auditInfo) {

        this.auditInfo = auditInfo;
    }


    public long getMsgId() {

        return msgId;
    }


    public void setMsgId(long msgId) {

        this.msgId = msgId;
    }


    public String getChannel() {

        return channel;
    }


    public void setChannel(String channel) {

        this.channel = channel;
    }

    public String getToAddress() {

        return toAddress;
    }


    public void setToAddress(String toAddress) {

        this.toAddress = toAddress;
    }


    public String getMessage() {

        return message;
    }


    public void setMessage(String message) {

        this.message = message;
    }


    public String getResponseType() {

        return responseType;
    }


    public void setResponseType(String responseType) {

        this.responseType = responseType;
    }

    /**
     * To get completedTxnCount.
     *
     * @return completedTxnCount.
     *
     */
    public int getCompletedTxnCount() {
		return completedTxnCount;
	}

	/**
	 * To set completedTxnCount.
	 *
	 * @param completedTxnCount
	 *
	 */
	public void setCompletedTxnCount(int completedTxnCount) {
		this.completedTxnCount = completedTxnCount;
	}

	/**
	 * To get completedTxnCount
	 *
	 * @return completedTxnAmount
	 *
	 */
	public String getCompletedTxnAmount() {
		return completedTxnAmount;
	}

	/**
	 * To set completedTxnCount.
	 *
	 * @param completedTxnAmount
	 *
	 */
	public void setCompletedTxnAmount(String completedTxnAmount) {
		this.completedTxnAmount = completedTxnAmount;
	}

	/**
	 * To get rtnTxnCount.
	 *
	 * @return rtnTxnCount
	 *
	 */
	public int getRtnTxnCount() {
		return rtnTxnCount;
	}

	/**
	 * To set rtnTxnCount.
	 *
	 * @param rtnTxnCount
	 */
	public void setRtnTxnCount(int rtnTxnCount) {
		this.rtnTxnCount = rtnTxnCount;
	}

	/**
	 * To get rtnTxnAmount.
	 *
	 * @return rtnTxnAmount String
	 */
	public String getRtnTxnAmount() {
		return rtnTxnAmount;
	}

	/**
	 * To set rtnTxnAmount.
	 *
	 * @param rtnTxnAmount String
	 */
	public void setRtnTxnAmount(String rtnTxnAmount) {
		this.rtnTxnAmount = rtnTxnAmount;
	}

	/**
     * To get LCBG Inward Count
     */
    public int getInward() {

        return inward;
    }

    /**
     * To set LCBG Inward Count
     */
    public void setInward(int inward) {

        this.inward = inward;
    }

    /**
     * To get LCBG Outward Count
     */
    public int getOutward() {

        return outward;
    }

    /**
     * To set LCBG Outward Count
     */
    public void setOutward(int outward) {

        this.outward = outward;
    }

    /**
     * To get LCBG Value Date
     */
    public String getLcbgDate() {

        return lcbgDate;
    }

    /**
     * To set LCBG Value Date
     */
    public void setLcbgDate(String lcbgDate) {

        this.lcbgDate = lcbgDate;
    }


    public int getInwardInprocess() {

        return inwardInprocess;
    }


    public void setInwardInprocess(int inwardInprocess) {

        this.inwardInprocess = inwardInprocess;
    }


    public int getInwardCompleted() {

        return inwardCompleted;
    }


    public void setInwardCompleted(int inwardCompleted) {

        this.inwardCompleted = inwardCompleted;
    }


    public int getOutwardInprocess() {

        return outwardInprocess;
    }


    public void setOutwardInprocess(int outwardInprocess) {

        this.outwardInprocess = outwardInprocess;
    }


    public int getOutwardForAcknowledge() {

        return outwardForAcknowledge;
    }


    public void setOutwardForAcknowledge(int outwardForAcknowledge) {

        this.outwardForAcknowledge = outwardForAcknowledge;
    }


    public int getOutwardSettled() {

        return outwardSettled;
    }


    public void setOutwardSettled(int outwardSettled) {

        this.outwardSettled = outwardSettled;
    }

    //For Beneficiary Account Details Reports Getter - Setter Methods - Starts, Joe.M
    public long getRtgsAccountNumber() {

        return rtgsAccountNumber;
    }


    public void setRtgsAccountNumber(long rtgsAccountNumber) {

        this.rtgsAccountNumber = rtgsAccountNumber;
    }


    public String getRtgsAccountName() {

        return rtgsAccountName;
    }


    public void setRtgsAccountName(String rtgsAccountName) {

        this.rtgsAccountName = rtgsAccountName;
    }


    public String getRtgsAmount() {

        return rtgsAmount;
    }


    public void setRtgsAmount(String rtgsAmount) {

        this.rtgsAmount = rtgsAmount;
    }


    public String getRtgsUTR() {

        return rtgsUTR;
    }


    public void setRtgsUTR(String rtgsUTR) {

        this.rtgsUTR = rtgsUTR;
    }

    public long getRtgsCBSAccountNumber() {

        return rtgsCBSAccountNumber;
    }


    public void setRtgsCBSAccountNumber(long rtgsCBSAccountNumber) {

        this.rtgsCBSAccountNumber = rtgsCBSAccountNumber;
    }


    public String getRtgsCBSAccountName() {

        return rtgsCBSAccountName;
    }


    public void setRtgsCBSAccountName(String rtgsCBSAccountName) {

        this.rtgsCBSAccountName = rtgsCBSAccountName;
    }


    public long getNeftAccountNumber() {

        return neftAccountNumber;
    }


    public void setNeftAccountNumber(long neftAccountNumber) {

        this.neftAccountNumber = neftAccountNumber;
    }


    public String getNeftAccountName() {

        return neftAccountName;
    }


    public void setNeftAccountName(String neftAccountName) {

        this.neftAccountName = neftAccountName;
    }


    public String getNeftAmount() {

        return neftAmount;
    }


    public void setNeftAmount(String neftAmount) {

        this.neftAmount = neftAmount;
    }


    public String getNeftUTR() {

        return neftUTR;
    }


    public void setNeftUTR(String neftUTR) {

        this.neftUTR = neftUTR;
    }


    public long getNeftCBSAccountNumber() {

        return neftCBSAccountNumber;
    }


    public void setNeftCBSAccountNumber(long neftCBSAccountNumber) {

        this.neftCBSAccountNumber = neftCBSAccountNumber;
    }


    public String getNeftCBSAccountName() {

        return neftCBSAccountName;
    }


    public void setNeftCBSAccountName(String neftCBSAccountName) {

        this.neftCBSAccountName = neftCBSAccountName;
    }


    /*public String getBIfsc() {

        return bIfsc;
    }


    public void setBIfsc(String bIfsc) {

        this.bIfsc = bIfsc;
    }
*/
    //For Beneficiary Account Details Reports Getter - Setter Methods - Ends, Joe.M
}
