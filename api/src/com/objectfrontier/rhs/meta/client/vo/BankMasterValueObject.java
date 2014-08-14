/*
 * Generated Code for com.objectfrontier.rhs.meta.BankMaster
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rhs.meta.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rhs.meta.client.vo.BankMasterValueObject.java
 * @see      
 * @date     10-15-08
 */


public class BankMasterValueObject implements Serializable { 

	public long id;
	public java.lang.String code;
	public java.lang.String name;
	public java.lang.String description;
	public com.objectfrontier.rhs.meta.ObjectHistory objectHistory;
	public long ObjectTimestamp;


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public long getId(){
		return this.id;
	}


	/**
	 * Get value of attribute - code
	 *
	 * @return code
	 */
	public java.lang.String getCode(){
		return this.code;
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
	 * Get value of attribute - description
	 *
	 * @return description
	 */
	public java.lang.String getDescription(){
		return this.description;
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
	 * Set value of attribute - id
	 *
	 * @param id
	 */
	public void setId(long id){

		this.id = id;
	}


	/**
	 * Set value of attribute - code
	 *
	 * @param code
	 */
	public void setCode(java.lang.String code){

		this.code = code;
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
	 * Set value of attribute - description
	 *
	 * @param description
	 */
	public void setDescription(java.lang.String description){

		this.description = description;
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
