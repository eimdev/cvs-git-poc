/*
 * Generated Code for com.objectfrontier.user.User
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.user.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.user.client.vo.UserValueObject.java
 * @see      
 * @date     10-15-08
 */


public class UserValueObject implements Serializable { 

	public java.lang.String id;
	public java.lang.String name;
	public java.lang.String description;
	public java.lang.String password;
	public boolean active;
	public long failedAttempts;
	public java.sql.Date inActivityDate;
	public long ObjectTimestamp;


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public java.lang.String getId(){
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
	 * Get value of attribute - description
	 *
	 * @return description
	 */
	public java.lang.String getDescription(){
		return this.description;
	}


	/**
	 * Get value of attribute - password
	 *
	 * @return password
	 */
	public java.lang.String getPassword(){
		return this.password;
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
	 * Get value of attribute - failedAttempts
	 *
	 * @return failedAttempts
	 */
	public long getFailedAttempts(){
		return this.failedAttempts;
	}


	/**
	 * Get value of attribute - inActivityDate
	 *
	 * @return inActivityDate
	 */
	public java.sql.Date getInActivityDate(){
		return this.inActivityDate;
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
	public void setId(java.lang.String id){

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
	 * Set value of attribute - description
	 *
	 * @param description
	 */
	public void setDescription(java.lang.String description){

		this.description = description;
	}


	/**
	 * Set value of attribute - password
	 *
	 * @param password
	 */
	public void setPassword(java.lang.String password){

		this.password = password;
	}


	/**
	 * Set value of attribute - active
	 *
	 * @param active
	 */
	public void setActive(boolean active){

		this.active = active;
	}


	/**
	 * Set value of attribute - failedAttempts
	 *
	 * @param failedAttempts
	 */
	public void setFailedAttempts(int failedAttempts){

		this.failedAttempts = failedAttempts;
	}


	/**
	 * Set value of attribute - inActivityDate
	 *
	 * @param inActivityDate
	 */
	public void setInActivityDate(java.sql.Date inActivityDate){

		this.inActivityDate = inActivityDate;
	}


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
