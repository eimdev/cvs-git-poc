/*
 * Generated Code for com.objectfrontier.rhs.meta.MsgHistory
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rhs.meta.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rhs.meta.client.vo.MsgHistoryValueObject.java
 * @see      
 * @date     10-15-08
 */


public class MsgHistoryValueObject implements Serializable { 

	public java.lang.String fromStatus;
	public java.lang.String toStatus;
	public java.lang.String entryBy;
	public java.sql.Timestamp dated;
	public long id;
	public java.lang.String remarks;
	public java.lang.String operationCode;
	public long ObjectTimestamp;
	public long msgHistory_B_1_msgId;


	/**
	 * Get value of attribute - fromStatus
	 *
	 * @return fromStatus
	 */
	public java.lang.String getFromStatus(){
		return this.fromStatus;
	}


	/**
	 * Get value of attribute - toStatus
	 *
	 * @return toStatus
	 */
	public java.lang.String getToStatus(){
		return this.toStatus;
	}


	/**
	 * Get value of attribute - entryBy
	 *
	 * @return entryBy
	 */
	public java.lang.String getEntryBy(){
		return this.entryBy;
	}


	/**
	 * Get value of attribute - dated
	 *
	 * @return dated
	 */
	public java.sql.Timestamp getDated(){
		return this.dated;
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
	 * Get value of attribute - ObjectTimestamp
	 *
	 * @return ObjectTimestamp
	 */
	public long getObjectTimestamp(){
		return this.ObjectTimestamp;
	}


	/**
	 * Get value of attribute - msgHistory_B_1_msgId
	 *
	 * @return msgHistory_B_1_msgId
	 */
	public long getMsgHistory_B_1_msgId(){
		return this.msgHistory_B_1_msgId;
	}


	/**
	 * Set value of attribute - fromStatus
	 *
	 * @param fromStatus
	 */
	public void setFromStatus(java.lang.String fromStatus){

		this.fromStatus = fromStatus;
	}


	/**
	 * Set value of attribute - toStatus
	 *
	 * @param toStatus
	 */
	public void setToStatus(java.lang.String toStatus){

		this.toStatus = toStatus;
	}


	/**
	 * Set value of attribute - entryBy
	 *
	 * @param entryBy
	 */
	public void setEntryBy(java.lang.String entryBy){

		this.entryBy = entryBy;
	}


	/**
	 * Set value of attribute - dated
	 *
	 * @param dated
	 */
	public void setDated(java.sql.Timestamp dated){

		this.dated = dated;
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


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
