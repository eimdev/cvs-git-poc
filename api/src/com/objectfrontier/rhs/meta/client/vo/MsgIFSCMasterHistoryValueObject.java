/*
 * Generated Code for com.objectfrontier.rhs.meta.MsgIFSCMasterHistory
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rhs.meta.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rhs.meta.client.vo.MsgIFSCMasterHistoryValueObject.java
 * @see      
 * @date     10-15-08
 */


public class MsgIFSCMasterHistoryValueObject implements Serializable { 

	public com.objectfrontier.rhs.meta.ObjectHistory objectHistory;
	public java.lang.String remarks;
	public java.lang.String operationCode;
	public int seqNo;
	public long id;
	public java.lang.String to_branch_code;
	public java.lang.String from_branch_code;
	public long ObjectTimestamp;
	public long toIFSCMaster_id;
	public long fromIFSCMaster_id;
	public long message_msgId;


	/**
	 * Get value of attribute - objectHistory
	 *
	 * @return objectHistory
	 */
	public com.objectfrontier.rhs.meta.ObjectHistory getObjectHistory(){
		return this.objectHistory;
	}


	/**
	 * Get value of attribute - remarks
	 *
	 * @return remarks
	 */
	public java.lang.String getRemarks(){
		return this.remarks;
	}


	/**
	 * Get value of attribute - operationCode
	 *
	 * @return operationCode
	 */
	public java.lang.String getOperationCode(){
		return this.operationCode;
	}


	/**
	 * Get value of attribute - seqNo
	 *
	 * @return seqNo
	 */
	public int getSeqNo(){
		return this.seqNo;
	}


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public long getId(){
		return this.id;
	}


	/**
	 * Get value of attribute - to_branch_code
	 *
	 * @return to_branch_code
	 */
	public java.lang.String getTo_branch_code(){
		return this.to_branch_code;
	}


	/**
	 * Get value of attribute - from_branch_code
	 *
	 * @return from_branch_code
	 */
	public java.lang.String getFrom_branch_code(){
		return this.from_branch_code;
	}


	/**
	 * Get value of attribute - ObjectTimestamp
	 *
	 * @return ObjectTimestamp
	 */
	public long getObjectTimestamp(){
		return this.ObjectTimestamp;
	}


	/**
	 * Get value of attribute - toIFSCMaster_id
	 *
	 * @return toIFSCMaster_id
	 */
	public long getToIFSCMaster_id(){
		return this.toIFSCMaster_id;
	}


	/**
	 * Get value of attribute - fromIFSCMaster_id
	 *
	 * @return fromIFSCMaster_id
	 */
	public long getFromIFSCMaster_id(){
		return this.fromIFSCMaster_id;
	}


	/**
	 * Get value of attribute - message_msgId
	 *
	 * @return message_msgId
	 */
	public long getMessage_msgId(){
		return this.message_msgId;
	}


	/**
	 * Set value of attribute - objectHistory
	 *
	 * @param objectHistory
	 */
	public void setObjectHistory(com.objectfrontier.rhs.meta.ObjectHistory objectHistory){

		this.objectHistory = objectHistory;
	}


	/**
	 * Set value of attribute - remarks
	 *
	 * @param remarks
	 */
	public void setRemarks(java.lang.String remarks){

		this.remarks = remarks;
	}


	/**
	 * Set value of attribute - operationCode
	 *
	 * @param operationCode
	 */
	public void setOperationCode(java.lang.String operationCode){

		this.operationCode = operationCode;
	}


	/**
	 * Set value of attribute - seqNo
	 *
	 * @param seqNo
	 */
	public void setSeqNo(int seqNo){

		this.seqNo = seqNo;
	}


	/**
	 * Set value of attribute - id
	 *
	 * @param id
	 */
	public void setId(long id){

		this.id = id;
	}


	/**
	 * Set value of attribute - to_branch_code
	 *
	 * @param to_branch_code
	 */
	public void setTo_branch_code(java.lang.String to_branch_code){

		this.to_branch_code = to_branch_code;
	}


	/**
	 * Set value of attribute - from_branch_code
	 *
	 * @param from_branch_code
	 */
	public void setFrom_branch_code(java.lang.String from_branch_code){

		this.from_branch_code = from_branch_code;
	}


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
