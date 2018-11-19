package org.cris.clip.dto;

public class AdministratorDTO {
	private String zoneid;
	private String divid;
	private String deptid;
	private String levelid;
	private String approveContractor;
	private String approveWO;
	private String createUser;
	private String userid;
	private String password;
	private String active;
	private String zone;
	private String division;
	private String dept;
	private String deptcode;
        private String email;
        private String mobile;
        private String desigid;
        private String desigCode;
        private String divorhq;
	/**
	 * @return 
	 */
        public String getDivOrHq() {
		return divorhq;
	}
	public void setDivOrHq(String n) {
		this.divorhq = n;
	}
        public String getEmail() {
		return email;
	}
	public void setEmail(String n) {
		this.email = n;
	}
        public String getMobile() {
		return mobile;
	}
	public void setMobile(String n) {
		this.mobile = n;
	}
        public String getDesigId() {
		return desigid;
	}
	public void setDesigId(String n) {
		this.desigid = n;
	}
        public String getDesigCode() {
		return desigCode;
	}
	public void setDesigCode(String n) {
		this.desigCode = n;
	}
	public String getDeptCode() {
		return deptcode;
	}
	public void setDeptCode(String n) {
		this.deptcode = n;
	}
        public String getDeptName() {
		return dept;
	}
	public void setDeptName(String n) {
		this.dept = n;
	}
	public String getDivisionName() {
		return division;
	}
	public void setDivisionName(String n) {
		this.division = n;
	}
	public String getZoneName() {
		return zone;
	}
	public void setZoneName(String n) {
		this.zone = n;
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
	
	public String getLevelId() {
		return levelid;
	}
	public void setLevelId(String n) {
		this.levelid = n;
	}
	
	public String getPrivContrApprove() {
		return approveContractor;
	}
	public void setPrivContrApprove(String n) {
		this.approveContractor = n;
	}
	
	public String getPrivWoApprove() {
		return approveWO;
	}
	public void setPrivWoApprove(String n) {
		this.approveWO = n;
	}
	
	public String getPrivUserCreate() {
		return createUser;
	}
	public void setPrivUserCreate(String n) {
		this.createUser = n;
	}
	public String getUserId() {
		return userid;
	}
	public void setUserId(String n) {
		this.userid = n;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String n) {
		this.password = n;
	}
	public String getActiveStatus() {
		return active;
	}
	public void setActiveStatus(String n) {
		this.active = n;
	}
}
