/*
 * Generated Code for com.objectfrontier.rtgs.msg.defn.MsgBlockDefn
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rtgs.msg.defn.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rtgs.msg.defn.client.vo.MsgBlockDefnValueObject.java
 * @see      
 * @date     10-15-08
 */


public class MsgBlockDefnValueObject implements Serializable { 

	public long id;
	public java.lang.String name;
	public int seqNo;
	public boolean mandatory;
	public java.lang.String description;
	public java.lang.String type;
	public long ObjectTimestamp;
	public long FORMAT_ID_1;
	public long MSG_DEFN_ID;


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
	 * Get value of attribute - description
	 *
	 * @return description
	 */
	public java.lang.String getDescription(){
		return this.description;
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
	 * Get value of attribute - FORMAT_ID_1
	 *
	 * @return FORMAT_ID_1
	 */
	public long getFORMAT_ID_1(){
		return this.FORMAT_ID_1;
	}


	/**
	 * Get value of attribute - MSG_DEFN_ID
	 *
	 * @return MSG_DEFN_ID
	 */
	public long getMSG_DEFN_ID(){
		return this.MSG_DEFN_ID;
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


	/**
	 * Set value of attribute - description
	 *
	 * @param description
	 */
	public void setDescription(java.lang.String description){

		this.description = description;
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
