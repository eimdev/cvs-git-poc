/*
 * Generated Code for com.objectfrontier.user.AuthorityType
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.user.client.vo;


import java.io.Serializable;
/**
 * 
 *
 * @class    com.objectfrontier.user.client.vo.AuthorityTypeValueObject.java
 * @see      
 * @date     10-15-08
 */


public class AuthorityTypeValueObject implements Serializable { 

	public long id;
	public boolean authorityType;
	public long ObjectTimestamp;
	public java.lang.String userUser_id;
	public java.lang.String reportingAuthorityUser_id;


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public long getId(){
		return this.id;
	}


	/**
	 * Get value of attribute - authorityType
	 *
	 * @return authorityType
	 */
	public boolean getAuthorityType(){
		return this.authorityType;
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
	 * Get value of attribute - userUser_id
	 *
	 * @return userUser_id
	 */
	public java.lang.String getUserUser_id(){
		return this.userUser_id;
	}


	/**
	 * Get value of attribute - reportingAuthorityUser_id
	 *
	 * @return reportingAuthorityUser_id
	 */
	public java.lang.String getReportingAuthorityUser_id(){
		return this.reportingAuthorityUser_id;
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
	 * Set value of attribute - authorityType
	 *
	 * @param authorityType
	 */
	public void setAuthorityType(boolean authorityType){

		this.authorityType = authorityType;
	}


	//Enterprise Rules Start Here





	//Enterprise Rules End Here

}
