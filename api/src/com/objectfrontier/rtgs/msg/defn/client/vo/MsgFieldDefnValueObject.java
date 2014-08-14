/*
 * Generated Code for com.objectfrontier.rtgs.msg.defn.MsgFieldDefn
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rtgs.msg.defn.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rtgs.msg.defn.client.vo.MsgFieldDefnValueObject.java
 * @see      
 * @date     10-15-08
 */


public class MsgFieldDefnValueObject implements Serializable { 

	public long id;
	public java.lang.String name;
	public int seqNo;
	public boolean mandatory;
	public long ObjectTimestamp;
	public long DEFAULT_FIELD_TYPE_ID;
	public long FIELD_BLOCK_ID;


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public long getId(){
		return this.id;
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
	 * Get value of attribute - seqNo
	 *
	 * @return seqNo
	 */
	public int getSeqNo(){
		return this.seqNo;
	}


	/**
	 * Get value of attribute - mandatory
	 *
	 * @return mandatory
	 */
	public boolean getMandatory(){
		return this.mandatory;
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
	 * Get value of attribute - DEFAULT_FIELD_TYPE_ID
	 *
	 * @return DEFAULT_FIELD_TYPE_ID
	 */
	public long getDEFAULT_FIELD_TYPE_ID(){
		return this.DEFAULT_FIELD_TYPE_ID;
	}


	/**
	 * Get value of attribute - FIELD_BLOCK_ID
	 *
	 * @return FIELD_BLOCK_ID
	 */
	public long getFIELD_BLOCK_ID(){
		return this.FIELD_BLOCK_ID;
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
	 * Set value of attribute - name
	 *
	 * @param name
	 */
	public void setName(java.lang.String name){

		this.name = name;
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
	 * Set value of attribute - mandatory
	 *
	 * @param mandatory
	 */
	public void setMandatory(boolean mandatory){

		this.mandatory = mandatory;
	}


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
