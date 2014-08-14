package com.objectfrontier.insta.reports.dto;

import java.io.Serializable;


/**
 *
 * @author jinuj
 * @date   Jul 28, 2008
 * @since  insta.reports; Jul 28, 2008
 */
public class ReportInputDTO
implements Serializable {

    private String branchCode;
    private long ifscId;
    private String ifscCode;
    private String paymentType;
    private String transactionType;
    private String groupBy;
    private String valueDate;
    private String toDate;
//    private Double fromAmount;
//    private Double toAmount;
    private String fromAmount;
    private String toAmount;
    private String hostType;
    private long receiverIfscId;
    private String receiverIfscCode;
    private String receiverBank;
    private long senderIfscId;
    private String senderIfscCode;
    private String senderBank;
    private long counterPartyIfscId;
    private String counterPartyBank;
    private String status;
    private String utrNo;
    private String userId;
    private String batchTime;
    private String reportType;
    private String inwardType;
    private String statusValue;
    private String channel;
    private String response;

    /** This variable is used to store selected bank. */
    public String[] selectedBank;

    //Getter and Setter Methods

    public String getUtrNo() {

        return utrNo;
    }

    public void setUtrNo(String utrNo) {

        this.utrNo = utrNo;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public long getCounterPartyIfscId() {

        return counterPartyIfscId;
    }

    public void setCounterPartyIfscId(long counterPartyIfscId) {

        this.counterPartyIfscId = counterPartyIfscId;
    }

//    public Double getFromAmount() {
//
//        return fromAmount;
//    }
//
//    public void setFromAmount(Double fromAmount) {
//
//        this.fromAmount = fromAmount;
//    }

    public String getFromAmount() {

        return fromAmount;
    }

    public void setFromAmount(String fromAmount) {

        this.fromAmount = fromAmount;
    }

    public String getHostType() {

        return hostType;
    }

    public void setHostType(String hostType) {

        this.hostType = hostType;
    }

    public String getReceiverBank() {

        return receiverBank;
    }

    public void setReceiverBank(String receiverBank) {

        this.receiverBank = receiverBank;
    }

    public String getReceiverIfscCode() {

        return receiverIfscCode;
    }

    public void setReceiverIfscCode(String receiverIfscCode) {

        this.receiverIfscCode = receiverIfscCode;
    }

    public long getReceiverIfscId() {

        return receiverIfscId;
    }

    public void setReceiverIfscId(long receiverIfscId) {

        this.receiverIfscId = receiverIfscId;
    }

    public String getSenderBank() {

        return senderBank;
    }

    public void setSenderBank(String senderBank) {

        this.senderBank = senderBank;
    }

    public String getSenderIfscCode() {

        return senderIfscCode;
    }

    public void setSenderIfscCode(String senderIfscCode) {

        this.senderIfscCode = senderIfscCode;
    }

    public long getSenderIfscId() {

        return senderIfscId;
    }

    public void setSenderIfscId(long senderIfscId) {

        this.senderIfscId = senderIfscId;
    }

    public String getToAmount() {

        return toAmount;
    }

    public void setToAmount(String toAmount) {

        this.toAmount = toAmount;
    }

    public String getBranchCode() {

        return branchCode;
    }

    public void setBranchCode(String branchCode) {

        this.branchCode = branchCode;
    }

    public String getGroupBy() {

        return groupBy;
    }

    public void setGroupBy(String groupBy) {

        this.groupBy = groupBy;
    }

    public String getPaymentType() {

        return paymentType;
    }

    public void setPaymentType(String paymentType) {

        this.paymentType = paymentType;
    }

    public String getTransactionType() {

        return transactionType;
    }

    public void setTransactionType(String transactionType) {

        this.transactionType = transactionType;
    }

    public String getValueDate() {

        return valueDate;
    }

    public void setValueDate(String valueDate) {

        this.valueDate = valueDate;
    }

    public String getIfscCode() {

        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {

        this.ifscCode = ifscCode;
    }

    public long getIfscId() {

        return ifscId;
    }

    public void setIfscId(long ifscId) {

        this.ifscId = ifscId;
    }

    public String getToDate() {

        return toDate;
    }

    public void setToDate(String toDate) {

        this.toDate = toDate;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public String getCounterPartyBank() {

        return counterPartyBank;
    }

    public void setCounterPartyBank(String counterPartyBank) {

        this.counterPartyBank = counterPartyBank;
    }


    public String getBatchTime() {

        return batchTime;
    }


    public void setBatchTime(String batchTime) {

        this.batchTime = batchTime;
    }


    public String getReportType() {

        return reportType;
    }


    public void setReportType(String reportType) {

        this.reportType = reportType;
    }


    public String getInwardType() {

        return inwardType;
    }


    public void setInwardType(String inwardType) {

        this.inwardType = inwardType;
    }


    public String getStatusValue() {

        return statusValue;
    }


    public void setStatusValue(String statusValue) {

        this.statusValue = statusValue;
    }


    public String getChannel() {

        return channel;
    }


    public void setChannel(String channel) {

        this.channel = channel;
    }


    public String getResponse() {

        return response;
    }


    public void setResponse(String response) {

        this.response = response;
    }

    /**
     * To get selected bank.
     *
     * @return String.
     */
    public String[] getSelectedBank() {
		return selectedBank;
	}

    /**
     * To set selected bank.
     *
     * @param selectedBank
     */
	public void setSelectedBank(String[] selectedBank) {
		this.selectedBank = selectedBank;
	}
}
