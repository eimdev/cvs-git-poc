/*
 * Generated Code for com.objectfrontier.rtgs.msg.defn.MsgCompoundFieldDefn
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rtgs.msg.defn.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rtgs.msg.defn.client.vo.MsgCompoundFieldDefnValueObject.java
 * @see      
 * @date     10-15-08
 */


public class MsgCompoundFieldDefnValueObject implements Serializable { 

	public long id;
	public int seqNo;
	public long ObjectTimestamp;
	public long MSG_FIELD_TYPE_ID;
	public long CMP_MSG_FIELD_TYPE_ID;


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
	 * Get value of attribute - ObjectTimestamp
	 *
	 * @return ObjectTimestamp
	 */
	public long getObjectTimestamp(){
		return this.ObjectTimestamp;
	}


	/**
	 * Get value of attribute - MSG_FIELD_TYPE_ID
	 *
	 * @return MSG_FIELD_TYPE_ID
	 */
	public long getMSG_FIELD_TYPE_ID(){
		return this.MSG_FIELD_TYPE_ID;
	}


	/**
	 * Get value of attribute - CMP_MSG_FIELD_TYPE_ID
	 *
	 * @return CMP_MSG_FIELD_TYPE_ID
	 */
	public long getCMP_MSG_FIELD_TYPE_ID(){
		return this.CMP_MSG_FIELD_TYPE_ID;
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


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
