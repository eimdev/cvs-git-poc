/*
 * Generated Code for com.objectfrontier.rtgs.msg.defn.ChannelDefn
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rtgs.msg.defn.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.rtgs.msg.defn.client.vo.ChannelDefnValueObject.java
 * @see      
 * @date     10-15-08
 */


public class ChannelDefnValueObject implements Serializable { 

	public int id;
	public java.lang.String version;
	public java.sql.Timestamp effectiveDate;
	public int msgTypeStartIndex;
	public int msgTypeLength;
	public int msgSubTypeStartIndex;
	public int msgSubTypeLength;
	public com.objectfrontier.rhs.meta.ObjectHistory objectHistory;
	public java.lang.String name;
	public java.lang.String msgStartDelimiter;
	public java.lang.String msgEndDelimiter;
	public boolean active;
	public long ObjectTimestamp;


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public int getId(){
		return this.id;
	}


	/**
	 * Get value of attribute - version
	 *
	 * @return version
	 */
	public java.lang.String getVersion(){
		return this.version;
	}


	/**
	 * Get value of attribute - effectiveDate
	 *
	 * @return effectiveDate
	 */
	public java.sql.Timestamp getEffectiveDate(){
		return this.effectiveDate;
	}


	/**
	 * Get value of attribute - msgTypeStartIndex
	 *
	 * @return msgTypeStartIndex
	 */
	public int getMsgTypeStartIndex(){
		return this.msgTypeStartIndex;
	}


	/**
	 * Get value of attribute - msgTypeLength
	 *
	 * @return msgTypeLength
	 */
	public int getMsgTypeLength(){
		return this.msgTypeLength;
	}


	/**
	 * Get value of attribute - msgSubTypeStartIndex
	 *
	 * @return msgSubTypeStartIndex
	 */
	public int getMsgSubTypeStartIndex(){
		return this.msgSubTypeStartIndex;
	}


	/**
	 * Get value of attribute - msgSubTypeLength
	 *
	 * @return msgSubTypeLength
	 */
	public int getMsgSubTypeLength(){
		return this.msgSubTypeLength;
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
	 * Get value of attribute - name
	 *
	 * @return name
	 */
	public java.lang.String getName(){
		return this.name;
	}


	/**
	 * Get value of attribute - msgStartDelimiter
	 *
	 * @return msgStartDelimiter
	 */
	public java.lang.String getMsgStartDelimiter(){
		return this.msgStartDelimiter;
	}


	/**
	 * Get value of attribute - msgEndDelimiter
	 *
	 * @return msgEndDelimiter
	 */
	public java.lang.String getMsgEndDelimiter(){
		return this.msgEndDelimiter;
	}


	/**
	 * Get value of attribute - active
	 *
	 * @return active
	 */
	public boolean getActive(){
		return this.active;
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
	public void setId(int id){

		this.id = id;
	}


	/**
	 * Set value of attribute - version
	 *
	 * @param version
	 */
	public void setVersion(java.lang.String version){

		this.version = version;
	}


	/**
	 * Set value of attribute - effectiveDate
	 *
	 * @param effectiveDate
	 */
	public void setEffectiveDate(java.sql.Timestamp effectiveDate){

		this.effectiveDate = effectiveDate;
	}


	/**
	 * Set value of attribute - msgTypeStartIndex
	 *
	 * @param msgTypeStartIndex
	 */
	public void setMsgTypeStartIndex(int msgTypeStartIndex){

		this.msgTypeStartIndex = msgTypeStartIndex;
	}


	/**
	 * Set value of attribute - msgTypeLength
	 *
	 * @param msgTypeLength
	 */
	public void setMsgTypeLength(int msgTypeLength){

		this.msgTypeLength = msgTypeLength;
	}


	/**
	 * Set value of attribute - msgSubTypeStartIndex
	 *
	 * @param msgSubTypeStartIndex
	 */
	public void setMsgSubTypeStartIndex(int msgSubTypeStartIndex){

		this.msgSubTypeStartIndex = msgSubTypeStartIndex;
	}


	/**
	 * Set value of attribute - msgSubTypeLength
	 *
	 * @param msgSubTypeLength
	 */
	public void setMsgSubTypeLength(int msgSubTypeLength){

		this.msgSubTypeLength = msgSubTypeLength;
	}


	/**
	 * Set value of attribute - objectHistory
	 *
	 * @param objectHistory
	 */
	public void setObjectHistory(com.objectfrontier.rhs.meta.ObjectHistory objectHistory){

		this.objectHistory = objectHistory;
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
	 * Set value of attribute - msgStartDelimiter
	 *
	 * @param msgStartDelimiter
	 */
	public void setMsgStartDelimiter(java.lang.String msgStartDelimiter){

		this.msgStartDelimiter = msgStartDelimiter;
	}


	/**
	 * Set value of attribute - msgEndDelimiter
	 *
	 * @param msgEndDelimiter
	 */
	public void setMsgEndDelimiter(java.lang.String msgEndDelimiter){

		this.msgEndDelimiter = msgEndDelimiter;
	}


	/**
	 * Set value of attribute - active
	 *
	 * @param active
	 */
	public void setActive(boolean active){

		this.active = active;
	}


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
