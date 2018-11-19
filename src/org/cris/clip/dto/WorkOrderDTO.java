package org.cris.clip.dto;

import java.util.ArrayList;
import java.util.Date;


public class WorkOrderDTO {
	
	private String wo;
	private String work_name;
	private String princ_emp;
	private String work_loc;
	private Date comm_date;
	int id;
	int woFileNo;
	Date endDt;
	Date extEndDt;
	String issueBy;
	String pincEmpAddr;
	String labourLicense;
	String appoveDt;
	String approveBy;
	String woZone;
	String woDiv;
	String woDept;
	String woArea;
	private String contMobileNo;
        private String contEmailId;
	private String contName;
	private String approverPost;
	ArrayList<WOLocationDTO> woLocList;
	private String appliedtoId;
        private String appliedtoPost;
    long loavalue;
    int maxworkman; 
    String wolastmon;
        
        
         public String getVerAppliedToPost() {
		return appliedtoPost;
	}
	public void setVerAppliedToPost(String n) {
		this.appliedtoPost = n;
	}
        public String getVerAppliedToId() {
		return appliedtoId;
	}
	public void setVerAppliedToId(String n) {
		this.appliedtoId = n;
	}
        public String getApproverPost() {
		return approverPost;
	}
	public void setApproverPost(String approveBy) {
		this.approverPost = approveBy;
	}
	public void setContName(String name) {
    	contName = name;
	}	
	public String getContName() {
		return contName;
	}
	
	public void setContMobileNo(String mob) {
    	contMobileNo = mob;
	}	
	public String getContMobileNo() {
		return contMobileNo;
	}
    
	public void setContEmailId(String email) {
    	contEmailId = email;
	}	
	public String getContEmailId() {
		return contEmailId;
	}
	
	
        private String approvalStatus;
	
	public void setApprovalStatus(String wono) {
		approvalStatus = wono;
	}	
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setWO(String wono) {
		wo = wono;
	}
	
	public String getWO() {
		if(wo!=null)
			return wo;
		else
			return "";
	}
	
	public void setWorkName(String name) {
		work_name = name;
	}
	public String getWork_Name() {
		if(work_name!=null)
			return work_name;
		else
			return "";
	}
	
	public void setPrincEmp(String name) {
		princ_emp = name;
	}
	public String getPrincEmp() {
		if(princ_emp!=null)
			return princ_emp;
		else
			return "";
	}
	
	public void setWorkLoc(String loc) {
		work_loc = loc;
	}
	public String getWorkLoc() {
		if(work_loc!=null)
			return work_loc;
		else
			return "";
	}
	
	public void setCommDate(Date comdt) {
		comm_date = comdt;
	}
	public Date getCommDate() {
		if(comm_date!=null)
			return comm_date;
		else
			return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWoFileNo() {
		return woFileNo;
	}

	public void setWoFileNo(int woFileNo) {
		this.woFileNo = woFileNo;
	}
	public String getWo() {
		return wo;
	}
	public void setWo(String wo) {
		this.wo = wo;
	}
	public String getWork_name() {
		return work_name;
	}
	public void setWork_name(String work_name) {
		this.work_name = work_name;
	}
	public String getPrinc_emp() {
		return princ_emp;
	}
	public void setPrinc_emp(String princ_emp) {
		this.princ_emp = princ_emp;
	}
	public String getWork_loc() {
		return work_loc;
	}
	public void setWork_loc(String work_loc) {
		this.work_loc = work_loc;
	}
	public Date getComm_date() {
		return comm_date;
	}
	public void setComm_date(Date comm_date) {
		this.comm_date = comm_date;
	}

	public Date getExtEndDt() {
		return extEndDt;
	}
	public void setExtEndDt(Date extEndDt) {
		this.extEndDt = extEndDt;
	}
	public String getIssueBy() {
		return issueBy;
	}
	public void setIssueBy(String issueBy) {
		this.issueBy = issueBy;
	}
	public String getPincEmpAddr() {
		return pincEmpAddr;
	}
	public void setPincEmpAddr(String pincEmpAddr) {
		this.pincEmpAddr = pincEmpAddr;
	}
	public String getLabourLicense() {
		return labourLicense;
	}
	public void setLabourLicense(String labourLicense) {
		this.labourLicense = labourLicense;
	}
	public String getAppoveDt() {
		return appoveDt;
	}
	public void setAppoveDt(String appoveDt) {
		this.appoveDt = appoveDt;
	}
	public String getApproveBy() {
		return approveBy;
	}
	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}
	public String getWoZone() {
		return woZone;
	}
	public void setWoZone(String woZone) {
		this.woZone = woZone;
	}
	public String getWoDiv() {
		return woDiv;
	}
	public void setWoDiv(String woDiv) {
		this.woDiv = woDiv;
	}
	public String getWoDept() {
		return woDept;
	}
	public void setWoDept(String woDept) {
		this.woDept = woDept;
	}
	public String getWoArea() {
		return woArea;
	}
	public void setWoArea(String woArea) {
		this.woArea = woArea;
	}
	public Date getEndDt() {
		return endDt;
	}
	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}
	public ArrayList<WOLocationDTO> getWoLocList() {
		return woLocList;
	}
	public void setWoLocList(ArrayList<WOLocationDTO> woLocList) {
		this.woLocList = woLocList;
	}
	public long getLoavalue() {
		return loavalue;
	}
	public void setLoavalue(long loavalue) {
		this.loavalue = loavalue;
	}
	public int getMaxworkman() {
		return maxworkman;
	}
	public void setMaxworkman(int maxworkman) {
		this.maxworkman = maxworkman;
	}
	public String getWolastmon() {
		return wolastmon;
	}
	public void setWolastmon(String wolastmon) {
		this.wolastmon = wolastmon;
	}
	
	
	
	
	
	
	
}
