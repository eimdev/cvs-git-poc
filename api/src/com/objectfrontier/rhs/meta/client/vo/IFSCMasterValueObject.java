/*
 * Generated Code for com.objectfrontier.rhs.meta.IFSCMaster
 * Please do not alter the code.
 *
 * @date     10-15-08
 */

package com.objectfrontier.rhs.meta.client.vo;


import java.io.Serializable;

public class IFSCMasterValueObject implements Serializable { 

	private static final long serialVersionUID = 1777187714284352121L;
    public long id;
	public java.lang.String ifsc;
	public java.lang.String name;
	public java.lang.String city;
	public java.lang.String state;
	public java.lang.String address;
	public java.lang.String pincode;
	public com.objectfrontier.rhs.meta.ObjectHistory objectHistory;
	public boolean active;
	public long ObjectTimestamp;
	public long bankMaster_id;


	/**
	 * Get value of attribute - id
	 *
	 * @return id
	 */
	public long getId(){
		return this.id;
	}


	/**
	 * Get value of attribute - ifsc
	 *
	 * @return ifsc
	 */
	public java.lang.String getIfsc(){
		return this.ifsc;
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
	 * Get value of attribute - city
	 *
	 * @return city
	 */
	public java.lang.String getCity(){
		return this.city;
	}


	/**
	 * Get value of attribute - state
	 *
	 * @return state
	 */
	public java.lang.String getState(){
		return this.state;
	}


	/**
	 * Get value of attribute - address
	 *
	 * @return address
	 */
	public java.lang.String getAddress(){
		return this.address;
	}


	/**
	 * Get value of attribute - pincode
	 *
	 * @return pincode
	 */
	public java.lang.String getPincode(){
		return this.pincode;
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
	 * Get value of attribute - bankMaster_id
	 *
	 * @return bankMaster_id
	 */
	public long getBankMaster_id(){
		return this.bankMaster_id;
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
	 * Set value of attribute - ifsc
	 *
	 * @param ifsc
	 */
	public void setIfsc(java.lang.String ifsc){

		this.ifsc = ifsc;
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
	 * Set value of attribute - city
	 *
	 * @param city
	 */
	public void setCity(java.lang.String city){

		this.city = city;
	}


	/**
	 * Set value of attribute - state
	 *
	 * @param state
	 */
	public void setState(java.lang.String state){

		this.state = state;
	}


	/**
	 * Set value of attribute - address
	 *
	 * @param address
	 */
	public void setAddress(java.lang.String address){

		this.address = address;
	}


	/**
	 * Set value of attribute - pincode
	 *
	 * @param pincode
	 */
	public void setPincode(java.lang.String pincode){

		this.pincode = pincode;
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
