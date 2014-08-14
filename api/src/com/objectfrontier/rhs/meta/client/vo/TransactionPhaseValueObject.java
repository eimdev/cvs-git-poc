/*
 * Generated Code for com.objectfrontier.rhs.meta.TransactionPhase
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rhs.meta.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rhs.meta.client.vo.TransactionPhaseValueObject.java
 * @see      
 * @date     10-15-08
 */


public class TransactionPhaseValueObject implements Serializable { 

	public java.lang.String name;
	public java.sql.Timestamp startTime;
	public java.sql.Timestamp endTime;
	public long id;
	public com.objectfrontier.rhs.meta.ObjectHistory objectHistory;
	public long ObjectTimestamp;


	/**
	 * Get value of attribute - name
	 *
	 * @return name
	 */
	public java.lang.String getName(){
		return this.name;
	}


	/**
	 * Get value of attribute - startTime
	 *
	 * @return startTime
	 */
	public java.sql.Timestamp getStartTime(){
		return this.startTime;
	}


	/**
	 * Get value of attribute - endTime
	 *
	 * @return endTime
	 */
	public java.sql.Timestamp getEndTime(){
		return this.endTime;
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
	 * Set value of attribute - name
	 *
	 * @param name
	 */
	public void setName(java.lang.String name){

		this.name = name;
	}


	/**
	 * Set value of attribute - startTime
	 *
	 * @param startTime
	 */
	public void setStartTime(java.sql.Timestamp startTime){

		this.startTime = startTime;
	}


	/**
	 * Set value of attribute - endTime
	 *
	 * @param endTime
	 */
	public void setEndTime(java.sql.Timestamp endTime){

		this.endTime = endTime;
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
