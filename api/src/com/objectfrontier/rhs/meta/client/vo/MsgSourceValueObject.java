/*
 * Generated Code for com.objectfrontier.rhs.meta.MsgSource
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rhs.meta.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rhs.meta.client.vo.MsgSourceValueObject.java
 * @see      
 * @date     10-15-08
 */


public class MsgSourceValueObject implements Serializable { 

	public java.lang.String name;
	public java.lang.String desc;
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
	 * Get value of attribute - desc
	 *
	 * @return desc
	 */
	public java.lang.String getDesc(){
		return this.desc;
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
	 * Set value of attribute - desc
	 *
	 * @param desc
	 */
	public void setDesc(java.lang.String desc){

		this.desc = desc;
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
