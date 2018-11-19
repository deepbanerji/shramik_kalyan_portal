/*
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.cris.clip.dto;

import java.io.Serializable;

/**
 * @author Deep
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ContractorDTO implements Serializable {
	
	private String password;
	private String contName;
	private String Address;
	private String pan;
	private String aadhar;
	private String mobile;
	private String landline;
	private String eMail;
	private String woLoiNo;
	private String woLoiDate;
	private String issuingAuth;
	private int company;
	private String firmName;
	private String firmRegNo;
	private String state;
	private int pin;
	private String pfRegNo;
	private String id;
	private String regnDate;
        private String approver;
        private String approverPost;
        private String approvalDate;
	private String approvalStatus;
	private String zoneid;
	private String divid;
	private String deptid;
	private String fatherName;
	private String woRefFileNo;
        private String appliedto;
        private String appliedtoId;
        private String irepsid;
        private String qualifiedLoaIssuer;
        private String epfRegStatus;
	/**
	 * @return 
	 */
	public String getVerAppliedToId() {
		return appliedtoId;
	}
	public void setVerAppliedToId(String n) {
		this.appliedtoId = n;
	}
        public String getApproverPost() {
		return approverPost;
	}
	public void setApproverPost(String n) {
		this.approverPost = n;
	}
        public String getEPFRegStatus() {
		return epfRegStatus;
	}
	public void setEPFRegStatus(String n) {
		this.epfRegStatus = n;
	}
        public String getQualifiedLoaIssuer() {
		return qualifiedLoaIssuer;
	}
	public void setQualifiedLoaIssuer(String n) {
		this.qualifiedLoaIssuer = n;
	}
        public String getIrepsId() {
		return irepsid;
	}
	public void setIrepsId(String n) {
		this.irepsid = n;
	}
        public String getVerAppliedTo() {
		return appliedto;
	}
	public void setVerAppliedTo(String n) {
		this.appliedto = n;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String n) {
		this.approver = n;
	}
        public String getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(String n) {
		this.approvalDate = n;
	}
        
        public String getWoRefFileNo() {
		return woRefFileNo;
	}
	public void setWoRefFileNo(String n) {
		this.woRefFileNo = n;
	}
	
	public String getZoneId() {
		return zoneid;
	}
	public void setZoneId(String n) {
		this.zoneid = n;
	}
	
	public String getDivId() {
		return divid;
	}
	public void setDivId(String n) {
		this.divid = n;
	}
	
	public String getDeptId() {
		return deptid;
	}
	public void setDeptId(String n) {
		this.deptid = n;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String n) {
		this.approvalStatus = n;
	}
	
	public String getRegnDate() {
		return regnDate;
	}
	public void setRegnDate(String n) {
		this.regnDate = n;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String n) {
		this.id = n;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String n) {
		this.password = n;
	}
	
	public String getPF() {
		return pfRegNo;
	}
	public void setPF(String n) {
		this.pfRegNo = n;
	}
	
	public String getFirmRegNo() {
		return firmRegNo;
	}
	public void setFirmRegNo(String n) {
		this.firmRegNo = n;
	}
	
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String n) {
		this.firmName = n;
	}
	
	public int getCompany() {
		return company;
	}
	public void setCompany(int com) {
		this.company = com;
	}
	
	public String getIssAuth() {
		return issuingAuth;
	}
	public void setIssAuth(String n) {
		this.issuingAuth = n;
	}
	
	public String getWoLoiDate() {
		return woLoiDate;
	}
	public void setWoLoiDate(String d) {
		this.woLoiDate = d;
	}
	
	public String getName() {
		return contName;
	}
	public void setName(String n) {
		this.contName = n;
	}
	public String getFName() {
		return fatherName;
	}
	public void setFName(String n) {
		this.fatherName = n;
	}
	public String getAddress() {
		return Address;
	}
	/**
	 * 
	 */
	public void setAddress(String add) {
		this.Address = add;
	}
	/**
	 * 
	 */
	public String getEMail() {
		return eMail;
	}
	/**
	 * @param 
	 */
	public void setEMail(String mail) {
		this.eMail = mail;
	}
	
	/**
	 * @return 
	 */
	public String getPan() {
		return pan;
	}
	/**
	 * @param 
	 */
	public void setPan(String pan) {
		this.pan = pan;
	}
	/**
	 * @return 
	 */
	public String getAadhar() {
		return aadhar;
	}
	/**
	 * @param 
	 */
	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}
	/**
	 * @return 
	 */
	public String getWoLoiNo() {
		return woLoiNo;
	}
	/**
	 * @param
	 */
	public void setWoLoiNo(String num) {
		this.woLoiNo = num;
	}
	/**
	 * @return 
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param 
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return 
	 */
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getLL() {
		return landline;
	}
	/**
	 * @param
	 */
	public void setLL(String ll) {
		this.landline = ll;
	}
	
	
	
}
