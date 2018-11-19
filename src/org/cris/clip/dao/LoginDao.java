package org.cris.clip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cris.clip.connection.DBConnection;
import org.cris.clip.dto.AdministratorDTO;
import org.cris.clip.dto.ContractorDTO;
import org.cris.clip.dto.WorkMenDTO;

public class LoginDao {
	public boolean login(String pan, String password) {
		Connection con=null;
		try {
			con=DBConnection.getInstance().getDBConnection();
			String sql="Select PASSWD from CONTRACTORPASSWORD where PANNO=? and (Select APPROVED from CONTRACTORREGISTRATION where PANNO=?)='1'";			
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1,pan);
                        pst.setString(2,pan);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
                            if(password.equals(rs.getString("PASSWD")))
                                    return true;
                            else
                                    return false;
			}
			else 
				return false;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
                finally {		
			if (con != null) {
				try {
					con.close();
				} 
				catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public ContractorDTO contractorLogin(String userName)
	{	
		ContractorDTO conDTO=new ContractorDTO();
		Connection con=null;
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql=" SELECT * FROM contractorregistration WHERE id=(SELECT contractorid FROM contractorpassword WHERE panno='"+userName+"')";			
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if(rs.next())
			{
				
				conDTO.setId(rs.getString("ID"));
				conDTO.setName(rs.getString("NAME"));
				conDTO.setFirmName(rs.getString("FIRMNAME"));
				conDTO.setPan(rs.getString("PANNO"));
				conDTO.setAadhar(rs.getString("AADHAR"));
				conDTO.setAddress(rs.getString("PERMANENTADDRESS"));				
				conDTO.setApprovalStatus(rs.getString("APPROVED"));
				conDTO.setMobile(rs.getString("MOBILENO"));
				
			}		
				
			
		}
		catch(Exception e) {
			e.printStackTrace();			
		}
		finally {		
			if (con != null) {
				try {
					con.close();
				} 
				catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
		return conDTO;
		
	}
	public AdministratorDTO adminLogin(String userName)
	{	
		AdministratorDTO aDTO=new AdministratorDTO();
		Connection con=null;
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql=" SELECT (Select zone_code from CONTRACT_ZONE_MASTER where ZONE_ID=zoneid) as zone, "
                                        + "(Select SHORT_DESIG from ADMIN_DESIGNATION where ID=(Select DESIGNATION from ADMIN_USERS where USERID=a.userid)) as shortdesig, "
					+ "(Select divcode from CONTRACT_DIV_MASTER where divid=a.divid) as div, "
					+ "(Select DEPTNAME from DEPARTMENT_MASTER where DEPTID=a.deptid) as dept,"
                                        + "(Select DEPTcode from DEPARTMENT_MASTER where DEPTID=a.deptid) as deptcode,"
                                        + "(Select DIV_HQ from CONTRACT_DIV_MASTER where divid=a.divid) as divorhq,"
					+ "zoneid,divid,deptid,levelid,PRIV_APPROVE_CONTR,PRIV_APPROVE_WO,PRIV_CREATE_USER,userid,DESIGNATION FROM ADMIN_USERS a WHERE userid='"+userName+"'";	
			//System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if(rs.next())
			{	aDTO.setDeptCode(rs.getString("deptcode"));			
				aDTO.setZoneId(rs.getString("ZONEID"));
				aDTO.setDivId(rs.getString("DIVID"));
				aDTO.setDeptId(rs.getString("DEPTID"));
				aDTO.setLevelId(rs.getString("LEVELID"));
				aDTO.setPrivContrApprove(rs.getString("PRIV_APPROVE_CONTR"));
				aDTO.setPrivWoApprove(rs.getString("PRIV_APPROVE_WO"));				
				aDTO.setPrivUserCreate(rs.getString("PRIV_CREATE_USER"));
				aDTO.setUserId(rs.getString("USERID"));
				aDTO.setZoneName(rs.getString("zone"));
				aDTO.setDivisionName(rs.getString("div"));
				aDTO.setDeptName(rs.getString("dept"));
                                aDTO.setDivOrHq(rs.getString("divorhq"));
                                aDTO.setDesigCode(rs.getString("shortdesig"));
                                aDTO.setDesigId(rs.getString("DESIGNATION"));
			}		
		}
		catch(Exception e) {
			e.printStackTrace();			
		}
		finally {		
			if (con != null) {
				try {
					con.close();
				} 
				catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
		return aDTO;
		
	}
	public boolean loginAdmin(String userid, String password) {
		Connection con=null;
		try {
			con=DBConnection.getInstance().getDBConnection();
			String sql="Select PASSWORD from ADMIN_USERS where USERID=? and ACTIVE=1";			
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1,userid);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				if(password.equals(rs.getString("PASSWORD")))
					return true;
				else
					return false;
			}
			else 
				return false;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {		
			if (con != null) {
				try {
					con.close();
				} 
				catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
        public boolean loginEmployee(String wmid, String password) {
		Connection con=null;
		System.out.println("loginEmployee");
		try {
			con=DBConnection.getInstance().getDBConnection();
			String sql="SELECT PASSWORD FROM CONTRACT_WORKMAN_PASSWORD WHERE WORKMANID=?";			
			PreparedStatement pst = con.prepareStatement(sql);	
			pst.setInt(1, Integer.parseInt(wmid));
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				if(password.equals(rs.getString("PASSWORD")))
					return true;
				else
					return false;
			}
			else 
				return false;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
                finally {		
			if (con != null) {
				try {
					con.close();
				} 
				catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
        public WorkMenDTO getWorkmanDTO(String wmid)
    	{	
    		WorkMenDTO wmDTO=new WorkMenDTO();
    		Connection con=null;
    		try
    		{
    			con=DBConnection.getInstance().getDBConnection();
    			String sql="select a.workmanid,a.name,a.sex,to_char(dateofbirth,'DD-MON-YYYY') as dateofbirth,fathername,permanentaddress,"
    					+ "permanentpincode,panno,aadharno,mobileno,email,eduqual,to_char(b.startdate,'DD-MON-YYYY') as startdate,to_char(b.terminationdate,'DD-MON-YYYY') as terminationdate,"
    					+ "(select name from contractorregistration where id=a.contractorid) as contname,(select firmname from contractorregistration where id=a.contractorid) as firmname,"
    					+ "(select workorderno from contract_work_order where woid=a.woid) as workorderno,(select nameofthework from contract_work_order where woid=a.woid) as loaname,"
    					+ "(select icardtype from contract_icard_type where icardid=a.icardtype) as icardtype,a.icardno,a.photono,b.wagerate,b.pftype,b.pfno,b.bankname,b.bankacno,b.designation,"
    					+ "(select employment_type from contract_employment_type where employment_type_id=b.employmenttype) as employmenttype,"
    					+ "(select unique(skill_type) from contract_skill_type where skill_type_id=b.skilltype) as skilltype from contract_workman_details a,contract_workman_employ_rec b where "
    					+ "a.workmanid=? and a.workmanid=b.workmanid and b.active='1' ";	
    			System.out.println(sql);
    			PreparedStatement pst = con.prepareStatement(sql);
    			pst.setString(1, wmid);
    			ResultSet rs = pst.executeQuery();
    			if(rs.next())
    			{
    			  wmDTO.setId(rs.getInt("workmanid"));	
    			  wmDTO.setName(rs.getString("name"));
    			  wmDTO.setSex(rs.getString("sex"));
    			  wmDTO.setDob(rs.getString("dateofbirth"));
    			  wmDTO.setFname(rs.getString("fathername"));
    			  wmDTO.setPermAddr(rs.getString("permanentaddress"));
    			  wmDTO.setPermPin(rs.getString("permanentpincode"));
    			  wmDTO.setIcardType(rs.getString("icardtype"));
    			  wmDTO.setIcardNo(rs.getString("icardno"));
    			  wmDTO.setPan(rs.getString("panno"));
    			  wmDTO.setAdhar(rs.getString("aadharno"));
    			  wmDTO.setMobno(rs.getString("mobileno"));
    			  wmDTO.setEmail(rs.getString("email"));
    			  wmDTO.setEduQual(rs.getString("eduqual"));
    			  wmDTO.setStartDt(rs.getString("startdate"));
    			  wmDTO.setTermDt(rs.getString("terminationdate"));
    			  wmDTO.setContName(rs.getString("contname"));
    			  wmDTO.setContFirmName(rs.getString("firmname"));
    			  wmDTO.setWoNum(rs.getString("workorderno"));
    			  wmDTO.setPhotono(rs.getInt("photono"));
    			  wmDTO.setWageRate(rs.getInt("wagerate"));
    			  wmDTO.setPfType(rs.getString("pftype"));
    			  wmDTO.setPfno(rs.getString("pfno"));
    			  wmDTO.setBankname(rs.getString("bankname"));
    			  wmDTO.setBankno(rs.getString("bankacno"));
    			  wmDTO.setDesg(rs.getString("designation"));
    			  wmDTO.setEmpTypeDesc(rs.getString("employmenttype"));
    		      wmDTO.setSkillTypeDesc(rs.getString("skilltype"));
    		      wmDTO.setLoaname(rs.getString("loaname"));
    			}			
    		}
    		catch(Exception e) {
    			e.printStackTrace();			
    		}
    		finally {		
    			if (con != null) {
    				try {
    					con.close();
    				} 
    				catch (SQLException ex) {
    					ex.printStackTrace();
    				}
    			}
    		}
    		return wmDTO;
    	}
}
