/*
 * Generated Code for com.objectfrontier.rhs.meta.RHSClientTxnDetails
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rhs.meta.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rhs.meta.client.vo.RHSClientTxnDetailsValueObject.java
 * @see      
 * @date     10-15-08
 */


public class RHSClientTxnDetailsValueObject implements Serializable { 

	public long id;
	public com.objectfrontier.rhs.meta.ObjectHistory objectHistory;
	public long ObjectTimestamp;
	public long rHSClientTxnDetails_B_1_msgId;


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public long getId(){
		return this.id;
	}


	/**
	 * Get value of attribute - objectHistory
	 *
	 * @return objectHistory
	 */
	public com.objectfrontier.rhs.meta.ObjectHistory getObjectHistory(){
		return this.objectHistory;
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
	 * Get value of attribute - rHSClientTxnDetails_B_1_msgId
	 *
	 * @return rHSClientTxnDetails_B_1_msgId
	 */
	public long getRHSClientTxnDetails_B_1_msgId(){
		return this.rHSClientTxnDetails_B_1_msgId;
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
	 * Set value of attribute - objectHistory
	 *
	 * @param objectHistory
	 */
	public void setObjectHistory(com.objectfrontier.rhs.meta.ObjectHistory objectHistory){

		this.objectHistory = objectHistory;
	}


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
