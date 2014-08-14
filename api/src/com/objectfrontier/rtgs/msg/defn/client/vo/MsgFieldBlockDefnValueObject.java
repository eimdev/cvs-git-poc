/*
 * Generated Code for com.objectfrontier.rtgs.msg.defn.MsgFieldBlockDefn
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rtgs.msg.defn.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rtgs.msg.defn.client.vo.MsgFieldBlockDefnValueObject.java
 * @see      
 * @date     10-15-08
 */


public class MsgFieldBlockDefnValueObject implements Serializable { 

	public long id;
	public int seqNo;
	public boolean mandatory;
	public java.lang.String type;
	public long ObjectTimestamp;
	public long BLOCK_ID_1;


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public long getId(){
		return this.id;
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
	 * Get value of attribute - type
	 *
	 * @return type
	 */
	public java.lang.String getType(){
		return this.type;
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
	 * Get value of attribute - BLOCK_ID_1
	 *
	 * @return BLOCK_ID_1
	 */
	public long getBLOCK_ID_1(){
		return this.BLOCK_ID_1;
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


	/**
	 * Set value of attribute - type
	 *
	 * @param type
	 */
	public void setType(java.lang.String type){

		this.type = type;
	}


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
