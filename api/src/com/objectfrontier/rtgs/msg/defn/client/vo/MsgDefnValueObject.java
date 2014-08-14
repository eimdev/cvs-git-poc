/*
 * Generated Code for com.objectfrontier.rtgs.msg.defn.MsgDefn
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rtgs.msg.defn.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rtgs.msg.defn.client.vo.MsgDefnValueObject.java
 * @see      
 * @date     10-15-08
 */


public class MsgDefnValueObject implements Serializable { 

	public long id;
	public java.lang.String type;
	public java.lang.String subType;
	public java.lang.String transactionType;
	public java.lang.String name;
	public boolean isOriginating;
	public int maxSize;
	public int minSize;
	public boolean isMultiple;
	public long ObjectTimestamp;
	public long TRANSACTION_PHASE_ID;
	public int RTGS_DEFN_ID;
	public long MSG_VALIDATOR_ID;


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public long getId(){
		return this.id;
	}


	/**
	 * Get value of attribute - type
	 *
	 * @return type
	 */
	public java.lang.String getType(){
		return this.type;
	}


	/**
	 * Get value of attribute - subType
	 *
	 * @return subType
	 */
	public java.lang.String getSubType(){
		return this.subType;
	}


	/**
	 * Get value of attribute - transactionType
	 *
	 * @return transactionType
	 */
	public java.lang.String getTransactionType(){
		return this.transactionType;
	}


	/**
	 * Get value of attribute - name
	 *
	 * @return name
	 */
	public java.lang.String getName(){
		return this.name;
	}


	/**
	 * Get value of attribute - isOriginating
	 *
	 * @return isOriginating
	 */
	public boolean getIsOriginating(){
		return this.isOriginating;
	}


	/**
	 * Get value of attribute - maxSize
	 *
	 * @return maxSize
	 */
	public int getMaxSize(){
		return this.maxSize;
	}


	/**
	 * Get value of attribute - minSize
	 *
	 * @return minSize
	 */
	public int getMinSize(){
		return this.minSize;
	}


	/**
	 * Get value of attribute - isMultiple
	 *
	 * @return isMultiple
	 */
	public boolean getIsMultiple(){
		return this.isMultiple;
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
	 * Get value of attribute - TRANSACTION_PHASE_ID
	 *
	 * @return TRANSACTION_PHASE_ID
	 */
	public long getTRANSACTION_PHASE_ID(){
		return this.TRANSACTION_PHASE_ID;
	}


	/**
	 * Get value of attribute - RTGS_DEFN_ID
	 *
	 * @return RTGS_DEFN_ID
	 */
	public int getRTGS_DEFN_ID(){
		return this.RTGS_DEFN_ID;
	}


	/**
	 * Get value of attribute - MSG_VALIDATOR_ID
	 *
	 * @return MSG_VALIDATOR_ID
	 */
	public long getMSG_VALIDATOR_ID(){
		return this.MSG_VALIDATOR_ID;
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
	 * Set value of attribute - type
	 *
	 * @param type
	 */
	public void setType(java.lang.String type){

		this.type = type;
	}


	/**
	 * Set value of attribute - subType
	 *
	 * @param subType
	 */
	public void setSubType(java.lang.String subType){

		this.subType = subType;
	}


	/**
	 * Set value of attribute - transactionType
	 *
	 * @param transactionType
	 */
	public void setTransactionType(java.lang.String transactionType){

		this.transactionType = transactionType;
	}


	/**
	 * Set value of attribute - name
	 *
	 * @param name
	 */
	public void setName(java.lang.String name){

		this.name = name;
	}


	/**
	 * Set value of attribute - isOriginating
	 *
	 * @param isOriginating
	 */
	public void setIsOriginating(boolean isOriginating){

		this.isOriginating = isOriginating;
	}


	/**
	 * Set value of attribute - maxSize
	 *
	 * @param maxSize
	 */
	public void setMaxSize(int maxSize){

		this.maxSize = maxSize;
	}


	/**
	 * Set value of attribute - minSize
	 *
	 * @param minSize
	 */
	public void setMinSize(int minSize){

		this.minSize = minSize;
	}


	/**
	 * Set value of attribute - isMultiple
	 *
	 * @param isMultiple
	 */
	public void setIsMultiple(boolean isMultiple){

		this.isMultiple = isMultiple;
	}


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
