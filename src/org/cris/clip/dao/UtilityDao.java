package org.cris.clip.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cris.clip.connection.DBConnection;
import org.cris.clip.dto.AdministratorDTO;
import org.cris.clip.dto.WOLocationDTO;
import org.cris.clip.dto.WorkMenDTO;
import org.cris.clip.dto.WorkOrderDTO;


public class UtilityDao 
{
	public HashMap<Integer, String> zoneList()
	{
		System.out.println("UtilityDao --> zonelist");
		HashMap<Integer,String> map=new HashMap<Integer,String>();
		Connection con=null;
		
		try
		{
			con= DBConnection.getInstance().getDBConnection();
			String sql="SELECT * FROM CONTRACT_ZONE_MASTER";
			PreparedStatement pst=con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				map.put(rs.getInt("ZONE_ID"), rs.getString("ZONE_CODE"));
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
		return map; 
	}
	public HashMap<Integer, String> departmentList(int div,AdministratorDTO adto)
	{       System.out.println("UtilityDao --> departmentList");
		HashMap<Integer,String> map=new HashMap<>();  
		Connection con=null;
		try
		{       String sql="";
                        if(adto==null)
                            sql="SELECT * FROM DEPARTMENT_MASTER WHERE DIVID="+div+" order by DEPTNAME";
                        else if( adto.getLevelId().equals("3") && adto.getDeptCode()!=null )
                            sql="SELECT * FROM DEPARTMENT_MASTER WHERE DIVID="+div+" and deptcode='"+adto.getDeptCode()+"'";
                        else {
                            sql="SELECT * FROM DEPARTMENT_MASTER WHERE DIVID="+div+" order by DEPTNAME";
                        }    
                        System.out.println(sql);
			con= DBConnection.getInstance().getDBConnection();			
			PreparedStatement pst=con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();                        
			while(rs.next()) {
				map.put(rs.getInt("DEPTID"), rs.getString("DEPTNAME"));
			}			
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return map; 
	}
	public HashMap<Integer, String> divisionList(int zone)
	{
		System.out.println("UtilityDao --> divisionList");
		HashMap<Integer,String> map=new HashMap<Integer,String>();  
		Connection con=null;
		try
		{
			con= DBConnection.getInstance().getDBConnection();
			String sql="SELECT * FROM CONTRACT_DIV_MASTER WHERE ZONEID='"+zone+"'";
			PreparedStatement pst=con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				map.put(rs.getInt("DIVID"), rs.getString("DIVCODE"));
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
		return map; 
	}
	
	public HashMap<Integer, String> getDepartmentList(int div)
	{
		HashMap<Integer,String> map=new HashMap<Integer,String>();  
		Connection con=null;
		try
		{
			con= DBConnection.getInstance().getDBConnection();
			String sql="SELECT * FROM DEPARTMENT_MASTER WHERE DIVID='"+div+"'";
			PreparedStatement pst=con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				map.put(rs.getInt("DEPTID"), rs.getString("DEPTNAME"));
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
		return map; 
		
		
	}
	
	public int getMinWage(int empType,int skillType,int woid)
	{
		int minWage=0;
		Connection con=null;
		try
		{
			con= DBConnection.getInstance().getDBConnection();
			String sql="SELECT MINWAGE FROM CONTRACT_MINWAGE_MASTER WHERE EMPLOYMENTTYPE='"+empType+"' "
				+ "AND SKILLTYPE='"+skillType+"' AND WOAREA=(SELECT WOAREA FROM CONTRACT_WORK_ORDER WHERE WOID='"+woid+"')";
			System.out.println(sql);
			PreparedStatement pst=con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				minWage = rs.getInt("MINWAGE");
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
		
		return minWage;
	}
	

	public HashMap<Integer, String> getWOListByDate(String month,String year,String conid)
	{
		
		String monYear=month+"-"+year;
		Connection con=null;
		HashMap<Integer,String> map=new HashMap<Integer,String>();
		
		try
		{
		con= DBConnection.getInstance().getDBConnection();
		String sql="SELECT WOID,WORKORDERNO FROM CONTRACT_WORK_ORDER WHERE (SELECT LAST_DAY(TO_DATE( '"+monYear+"','MM-YY')) FROM DUAL) >= "
				+ " DATEOFCOMMENCEMENT AND(SELECT TO_DATE( '"+monYear+"','MM-YY') FROM DUAL) <= EXTDATEOFCOMPLETION AND CONTRACTORID='"+conid+"' AND APPROVED='1'";
				
		System.out.println(sql);
		PreparedStatement pst=con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		while(rs.next())
		{
			map.put(rs.getInt("WOID"), rs.getString("WORKORDERNO"));
		}	
		
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
		return map;
	}
	public int createUser(AdministratorDTO adto,String userid) {
		Connection con=null;		
		int retVal=0;
		try {
			con = DBConnection.getInstance().getDBConnection();
                        String sql="Insert into ADMIN_USERS values('"+adto.getUserId()+"','"+adto.getPassword()+"',"+adto.getZoneId()+","+adto.getDivId()+","+adto.getDeptId()+",'"+adto.getLevelId()+"',sysdate,'"+userid+"',"+adto.getActiveStatus()+",sysdate,'"+userid+"','"+adto.getPrivContrApprove()+"','"+adto.getPrivWoApprove()+"','"+adto.getPrivUserCreate()+"','"+adto.getMobile()+"','"+adto.getEmail()+"','"+adto.getDesigId()+"')";
			System.out.println(sql);
                        PreparedStatement ps = con.prepareStatement(sql);
			retVal = ps.executeUpdate();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
		return retVal; 
	}
	public int checkUserid(String userid) {
		Connection con=null;		
		int retVal=0;
		try {
                    con = DBConnection.getInstance().getDBConnection();
                    String sql="Select * from ADMIN_USERS where USERID='"+userid+"'";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				retVal=1;
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
	public ArrayList listWOs(AdministratorDTO adto,String id, String selector,String extra) {
		ArrayList al = null;
		Connection con=null;
		String patch="",patch2="";
                
                if(adto.getLevelId().equals("0")) {                           
                    patch2=" where ";
                    if(selector.equals("zone"))
                            patch = "  wozone='"+id+"' ";
                    else if(selector.equals("div"))
                            patch = "  wodiv='"+id+"' ";
                    else if(selector.equals("dept"))			
                            patch = "  wodept='"+id+"' ";
                    else if(selector.equals("cont"))
                            patch = "  CONTRACTORID='"+id+"' " + extra;
                    else if(selector.equals("all"))
                            patch = "  APPROVED='1' ";
                }
                else if(adto.getLevelId().equals("1")) {                           
                    patch2=" where WOZONE='"+adto.getZoneId()+"' ";
                    if(selector.equals("div"))
                            patch = " and wodiv='"+id+"' ";
                    else if(selector.equals("dept"))			
                            patch = " and wodept='"+id+"' ";
                    else if(selector.equals("cont"))
                            patch = " and CONTRACTORID='"+id+"' " + extra;
                    else if(selector.equals("all"))
                            patch = " and APPROVED='1' ";
                }
                else if(adto.getLevelId().equals("2")) {
                        patch2=" where WODIV='"+adto.getDivId()+"' ";
                        if(selector.equals("dept"))			
                                patch = " and wodept='"+id+"' ";
                        else if(selector.equals("cont"))
                                patch = " and CONTRACTORID='"+id+"' " + extra;
                        else if(selector.equals("all"))
                                patch = " and APPROVED='1' ";
                }
                else if(adto.getLevelId().equals("3")) {
                    if(adto.getDivOrHq().equals("d"))    
                        patch2=" where WODIV='"+adto.getDivId()+"' and WODEPT='"+adto.getDeptId()+"' ";
                    else
                       patch2=" where WOZONE='"+adto.getZoneId()+"' and wodept in (Select DEPTID from DEPARTMENT_MASTER where ZONEID='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"' ) ";             
       
                        if(selector.equals("cont"))
                                patch = " and CONTRACTORID='"+id+"' " + extra;
                        else if(selector.equals("all"))
                                patch = " and APPROVED='1' ";
                }
		
		patch = patch2 + patch + " and APPROVED='1' and APPROVALBY is not null ";
		String sql = "Select * from CONTRACT_WORK_ORDER "+patch+ " order by WORKORDERNO";
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				WorkOrderDTO wdto = new WorkOrderDTO();
				wdto.setId(rs.getInt("WOID"));
				wdto.setWO(rs.getString("WORKORDERNO"));
				//wdto.setWorkName(rs.getString("NAMEOFTHEWORK"));
				//wdto.setPrincEmp(rs.getString("NAMEOFTHEPRINCIPALEMPLOYER"));
				//wdto.setApprovalStatus(rs.getString("APPROVED"));
				al.add(wdto);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;
	}
	public ArrayList listWOs(AdministratorDTO adto) {
		ArrayList al = null;
		Connection con=null;
		String patch="";
                
		if(adto.getLevelId().equals("1"))
                        patch=" and WOZONE='"+adto.getZoneId()+"' ";                            
                else if(adto.getLevelId().equals("2"))
			patch=" and WODIV='"+adto.getDivId()+"' ";
		else if(adto.getLevelId().equals("3")) {
                    if(adto.getDivOrHq().equals("d"))
                        patch=" and WODIV='"+adto.getDivId()+"' and WODEPT='"+adto.getDeptId()+"' ";
                    else
                        patch=" and WOZONE='"+adto.getZoneId()+"' and wodept in (Select DEPTID from DEPARTMENT_MASTER where ZONEID='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"' ) ";
                }
		String sql = "Select (SELECT zone_code FROM CONTRACT_zone_MASTER WHERE zone_id=wozone) as zone, (SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=wodiv) as div,"
                    + "(SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=wodept) as dept, (select name from CONTRACTORREGISTRATION where id=CONTRACTORID) as name,"
                    + "(select mobileno from CONTRACTORREGISTRATION where id=CONTRACTORID) as mobileno,"
                    + "(select emailid from CONTRACTORREGISTRATION where id=CONTRACTORID) as email, "
                    + "woid,WORKORDERNO,NAMEOFTHEWORK,NAMEOFTHEPRINCIPALEMPLOYER,DATEOFCOMMENCEMENT,DATEOFCOMPLETIONWORK,EXTDATEOFCOMPLETION,workorderissuedby,CREATIONDATE,CREATEDBY,"
                    + "MODIFICATIONDATE,MODIFIEDBY,APPROVED,APPROVALBY,APPROVALDATE,WO_FILENO from CONTRACT_WORK_ORDER where CONTRACTORID in "
                    + "(Select ID from CONTRACTORREGISTRATION where APPROVED='1' and APPROVEDBY is not null) "+patch+" order by APPROVALDATE";
		System.out.println(sql);
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
                            WorkOrderDTO wdto = new WorkOrderDTO();
                            wdto.setId(rs.getInt("WOID"));
                            wdto.setContName(rs.getString("name"));
                            wdto.setContMobileNo(rs.getString("mobileno"));
                            wdto.setContEmailId(rs.getString("email"));
                            wdto.setWO(rs.getString("WORKORDERNO"));
                            wdto.setWorkName(rs.getString("NAMEOFTHEWORK"));
                            wdto.setPrincEmp((new UtilityDao()).getAdminPostFromId(rs.getString("NAMEOFTHEPRINCIPALEMPLOYER")));
                            wdto.setApprovalStatus(rs.getString("APPROVED"));
                            wdto.setWoFileNo(rs.getInt("WO_FILENO"));
                            wdto.setWoZone(rs.getString("zone"));
                            wdto.setWoDiv(rs.getString("div"));
                            wdto.setWoDept(rs.getString("dept"));
                            wdto.setApproverPost((new UtilityDao()).getAdminPostFromId(rs.getString("APPROVALBY")));
                            wdto.setVerAppliedToPost((new UtilityDao()).getAdminPostFromId(rs.getString("workorderissuedby")));
                            wdto.setVerAppliedToId(rs.getString("workorderissuedby"));
                            wdto.setAppoveDt(rs.getString("APPROVALDATE"));
                            wdto.setApproveBy(rs.getString("APPROVALBY"));
                            wdto.setCommDate(rs.getDate("DATEOFCOMMENCEMENT"));
                            wdto.setEndDt(rs.getDate("DATEOFCOMPLETIONWORK"));
                            wdto.setExtEndDt(rs.getDate("EXTDATEOFCOMPLETION"));
                            al.add(wdto);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;
	}
	public int approveWO(String wono,String action,AdministratorDTO adto,String uid,String mobile,String email,String value) {	
		Connection con=null;	
		int retVal=0;
		try {
			String s[] = {"","VERIFIED","REJECTED"};
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
			String sql="update CONTRACT_WORK_ORDER set APPROVED='"+value+"',APPROVALBY='"+adto.getUserId()+"',APPROVALDATE=sysdate where WOID="+uid;
			PreparedStatement ps = con.prepareStatement(sql);
			retVal = ps.executeUpdate();
			con.commit();
                        
//                        if(!email.equals("")) {
//                            SendEmail x = new SendEmail("utskolkata@cris.org.in",new String[]{email},"LOA "+wono+" is "+s[Integer.parseInt(value)]+" in IR Shramik Kalyan Portal","IR Shramik Kalyan Portal LOA","","lotusnotes",false);
//                            ExecutorService executor = Executors.newSingleThreadExecutor();
//                            executor.execute(x);
//                            executor.shutdown();
//                            try {
//                                if(!executor.awaitTermination(250, TimeUnit.MILLISECONDS)) {
//                                        executor.shutdownNow();
//                                }
//                            }
//                            catch(InterruptedException e) {
//                                executor.shutdownNow();
//                            }
//                        }
                        if(!mobile.equals("")) {                        	
                            SendSms y = new SendSms(mobile,"LOA "+wono+" is "+s[Integer.parseInt(value)]+" in IR Shramik Kalyan Portal");
                            ExecutorService executor1 = Executors.newSingleThreadExecutor();
                            executor1.execute(y);
                            executor1.shutdown();
                            try {
                                    if(!executor1.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                                            executor1.shutdownNow();
                                    }
                            }
                            catch(InterruptedException e) {
                                    executor1.shutdownNow();
                            }
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
		return retVal;
	}
	public ArrayList listLabours(String woid) {
		ArrayList al = null;
		Connection con=null;
		String sql = "Select (Select (SELECT zone_code FROM CONTRACT_zone_MASTER WHERE zone_id=wozone) from CONTRACT_WORK_ORDER where woid="+woid+") as zone, "
                        + "(Select (SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=wodiv) from CONTRACT_WORK_ORDER where woid="+woid+") as div,"
			+ "(Select (SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=wodept) from CONTRACT_WORK_ORDER where woid="+woid+") as dept, "
                        + "(Select WORKORDERNO from CONTRACT_WORK_ORDER where woid="+woid+") as wonum,"
                        + "workmanid,name,(Select icardtype from CONTRACT_ICARD_TYPE where icardid=a.icardtype) as icardtype,icardno,sex,MOBILENO,EMAIL,DESIGNATION,"
                        + "to_char(DATEOFBIRTH,'dd-mm-yyyy') as dob from CONTRACT_WORKMAN_DETAILS a where WOID="+woid+" and ACTIVESTATUS=1 and (trunc(sysdate) between STARTDATE and TERMINATIONDATE)";
		try {
                    con = DBConnection.getInstance().getDBConnection();
                    al = new ArrayList();
                    PreparedStatement ps = con.prepareStatement(sql);
                    System.out.println(sql);
                    ResultSet rs = ps.executeQuery();			
                    while(rs.next()) {
                        WorkMenDTO wdto = new WorkMenDTO();
                        wdto.setId(rs.getInt("WORKMANID"));
                        wdto.setName(rs.getString("NAME"));
                        wdto.setIcardType(rs.getString("icardtype"));
                        wdto.setIcardNo(rs.getString("icardno"));
                        wdto.setSex(rs.getString("SEX"));
                        wdto.setWoNum(rs.getString("wonum"));
                        wdto.setWoZone(rs.getString("zone"));
                        wdto.setWoDiv(rs.getString("div"));
                        wdto.setWoDept(rs.getString("dept"));
                        wdto.setMobno(rs.getString("MOBILENO"));
                        wdto.setEmail(rs.getString("EMAIL"));
                        wdto.setDob(rs.getString("dob"));
                        wdto.setDesg(rs.getString("DESIGNATION"));
                        al.add(wdto);
                    }
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;
	}
	public int getPendingWOCount(AdministratorDTO adto,String adminLevel) {
		Connection con=null;
                int count = 0;
//		String patch="";		
//                if(!adto.getLevelId().equals("0")) {
//                    if(adto.getLevelId().equals("1"))
//                        patch=" and WOZONE='"+adto.getZoneId()+"' ";
//                    else if(adto.getLevelId().equals("2"))
//                            patch=" and WODIV='"+adto.getDivId()+"' ";
//                    else if(adto.getLevelId().equals("3")) {
//                        if(adto.getDivOrHq().equals("d"))    
//                            patch=" and WODIV='"+adto.getDivId()+"' and WODEPT='"+adto.getDeptId()+"' ";
//                        else
//                            patch = " and WOZONE='"+adto.getZoneId()+"' and wodept in(Select DEPTID from DEPARTMENT_MASTER where zoneid='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"')";               
//             
//                    }        
//                }                                
		String sql = "Select count(*) as count from CONTRACT_WORK_ORDER where CONTRACTORID in (Select ID from CONTRACTORREGISTRATION where APPROVED='1') "
                        + "and WORKORDERISSUEDBY='"+adto.getUserId()+"' and APPROVED='0'";
		System.out.println(sql);
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				count = rs.getInt("count");
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return count;
	}
	public ArrayList getDivWiseContCount(AdministratorDTO adto) {
		ArrayList al = null;
		Connection con=null;
		String sql = "select nvl( (SELECT DIVcode FROM CONTRACT_DIV_MASTER WHERE DIVID=cr.divid), (Select zone_code from contract_zone_master where zone_id='"+adto.getZoneId()+"') ) as division,count(*) as count from CONTRACTORREGISTRATION cr where ZONEID='"+adto.getZoneId()+"' and approved='1' group by divid order by divid desc";
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				String a[] = {rs.getString("division"),rs.getString("count")};
				al.add(a);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;		
	}
	
	public String convertDate(String inputdt)
	{
		 String date[] = inputdt.split("-");
		 String months[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN","JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		 for(int j=0;j<months.length;j++){
		     if(date[1].equalsIgnoreCase(months[j]))
		     {
		    	  System.out.println(months[j]);
		    	  if(j+1<10)
		    	  {
		    	   date[1]= "0"+String.valueOf(j+1); 
		    	  }	
		    	  else
		    	  {	  
		           date[1]= String.valueOf(j+1);
		    	  }
		      }                      
		 } 
	                     
		 String formattedDate = date[2]+"-"+date[1]+"-"+date[0]; 
		 System.out.println("formattedDate"+formattedDate);
		 return formattedDate;
		
	}
	
	public String getWOPeriod(String woid)
	{
		String result = "";		
		Connection con=null;
		try
		{
			con= DBConnection.getInstance().getDBConnection();
			String sql="SELECT TO_CHAR(DATEOFCOMMENCEMENT,'DD-MON-YYYY') AS DATEOFCOMMENCEMENT,TO_CHAR(EXTDATEOFCOMPLETION,'DD-MON-YYYY') AS EXTDATEOFCOMPLETION "
					+ "FROM CONTRACT_WORK_ORDER WHERE WOID=?";
			System.out.println(sql);
			PreparedStatement pst=con.prepareStatement(sql);
			pst.setString(1, woid);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				result = rs.getString("DATEOFCOMMENCEMENT")+","+rs.getString("EXTDATEOFCOMPLETION");
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
		
		
		return result;
	}
	public HashMap<Integer, String> getSkillType(String emptype)
	{
		System.out.println("getSkillType");
		HashMap<Integer,String> map=new HashMap<Integer,String>();
		Connection con=null;
		
		try
		{
			con= DBConnection.getInstance().getDBConnection();
			String sql="SELECT * FROM CONTRACT_SKILL_TYPE WHERE EMPLOYMENT_TYPE_ID='"+emptype+"'";
			PreparedStatement pst=con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				map.put(rs.getInt("SKILL_TYPE_ID"), rs.getString("SKILL_TYPE"));
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
		return map; 
	}
	public ArrayList getDashboardTbl1(AdministratorDTO adto,String adminLevel) {
		ArrayList al = null;
		Connection con=null;
		String suffix = "";
                if(!adminLevel.equals("0")) {
                    if(adto.getLevelId().equals("1"))
                        suffix = " and WOZONE="+adto.getZoneId();
                    else if(adto.getLevelId().equals("2"))
                            suffix = " and WODIV="+adto.getDivId();
                    else if(adto.getLevelId().equals("3")) {
                        if(adto.getDivOrHq().equals("d"))
                            suffix = " and WODIV="+adto.getDivId()+" and WODEPT='"+adto.getDeptId()+"'";
                        else    
                            suffix = " and WOZONE='"+adto.getZoneId()+"' and wodept in(Select DEPTID from DEPARTMENT_MASTER where zoneid='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"')";
                    }                            
                }
		String sql = "select wozone, wodiv,wodept,nvl(count(*),0) as wo,nvl(sum(totwmen),0) as wmen, nvl(sum(totwage),0) as wages from ( SELECT "
                        + "(SELECT zone_code FROM CONTRACT_ZONE_MASTER WHERE zone_id=wozone ) AS wozone, (SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=wodiv ) AS wodiv, "
                        + "(SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=wodept ) AS wodept, "
			+ "woid , (SELECT COUNT(distinct(workmanid)) FROM CONTRACT_WORKMAN_DETAILS WHERE WOID =cwo.woid ) AS totwmen, "
			+ "(select sum(NETAMOUNT+PFGROSS+PENSION+OTHERDEDUCTION) from CONTRACT_WORKMAN_WAGES where WOID=cwo.woid) as totwage "
                        + "FROM CONTRACT_WORK_ORDER cwo WHERE approved  ='1' and approvalby is not null "
                        + "and sysdate>=DATEOFCOMMENCEMENT and sysdate<=EXTDATEOFCOMPLETION "+ suffix +" GROUP BY wozone, wodiv , wodept , woid) group by wozone, "
                        + "wodiv,wodept order by wozone, wodiv,wodept";
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				String a[] = {rs.getString("wodiv"),rs.getString("wodept"),String.valueOf(rs.getInt("wo")),String.valueOf(rs.getInt("wmen")),String.valueOf(rs.getInt("wages")),rs.getString("wozone")};
				al.add(a);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;		
	}
	public int checkPAN(String pan,String contid) {
		Connection con=null;
		int retVal=0;
		try {
                        con = DBConnection.getInstance().getDBConnection();
//                        String patch1 = "";
//                        if(contid==null)
//                            patch1=" and approved!='2' ";
//                        else
//                            patch1=" and ID!=" + contid;
//                        
//			String sql="Select * from CONTRACTORREGISTRATION where PANNO='"+pan+"'"+patch1;
//			PreparedStatement ps = con.prepareStatement(sql);
//			ResultSet rs = ps.executeQuery();
//			
//                        String patch2="";
//                        if(contid==null)
//                            patch2=" and approved!='2' ";
//                        else
//                            patch2=" and CONTRACTORID!="+contid;
//                        
//			String sql1="Select * from CONTRACTORPASSWORD where PANNO='"+pan+"'"+patch2;
//			PreparedStatement ps1 = con.prepareStatement(sql1);
//			ResultSet rs1 = ps1.executeQuery();
//			
//			if(rs.next() || rs1.next())
//				retVal=1;
                        CallableStatement proc = con.prepareCall("{ call checkpan(?,?,?) }");
                        proc.setString(1, pan);
                        proc.setString(2, contid);
                        proc.registerOutParameter(3, java.sql.Types.INTEGER);
                        proc.executeUpdate();
                        retVal = proc.getInt(3);
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
	public ArrayList listLabourWages(String year,String month,String woid,String bankDepDt,String pfDepDt,String yearTo,String monthTo) {
		ArrayList al = null;
		Connection con=null;
                String sql="",patch="",patch1="";
                int contid;
                String sql2="Select CONTRACTORID from CONTRACT_WORK_ORDER where WOID="+woid;
                
                try {
                    con = DBConnection.getInstance().getDBConnection();
                    PreparedStatement ps2 = con.prepareStatement(sql2);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    contid= rs2.getInt("CONTRACTORID");
                    String temp1 = "",temp2 = "";
                    if(Integer.parseInt(month)==Integer.parseInt(monthTo) && Integer.parseInt(year)==Integer.parseInt(yearTo)) {
                        temp1 = "MONTH ='"+month+"' AND YEAR='"+year+"'";
                        temp2 = "a.MONTH ='"+month+"' AND a.YEAR='"+year+"'";
                    }
                    else if(Integer.parseInt(month)<Integer.parseInt(monthTo) && Integer.parseInt(year)==Integer.parseInt(yearTo)) {
                        temp1 = "to_number(MONTH)>='"+Integer.parseInt(month)+"' and to_number(month)<='"+Integer.parseInt(monthTo)+"' AND YEAR='"+year+"'";
                        temp2 = "to_number(a.MONTH)>='"+Integer.parseInt(month)+"' and to_number(a.month)<='"+Integer.parseInt(monthTo)+"' AND a.YEAR='"+year+"'";
                    }
                    else if( Integer.parseInt(year)<Integer.parseInt(yearTo)) {
                        temp1 = "(  (MONTH ='"+month+"' AND YEAR='"+year+"' ) ";
                        temp2 = "(  (a.MONTH ='"+month+"' AND a.YEAR='"+year+"' ) ";
                        for (int m=Integer.parseInt(month),y=Integer.parseInt(year);m!=Integer.parseInt(monthTo) || y!=Integer.parseInt(yearTo);) {
                            m=(m+1)%13;
                            if(m==0) {
                                ++y;
                                m = 1;
                            }
                            String mm="";
                            if(m<10) 
                                mm = "0"+String.valueOf(m);
                            else
                                mm = String.valueOf(m);
                            temp1 = temp1 + " or ( MONTH ='"+mm+"' AND YEAR='"+y+"' ) ";
                            temp2 = temp2 + " or ( a.MONTH ='"+mm+"' AND a.YEAR='"+y+"' ) ";
                        }
                        temp1 = temp1 + ")";                        
                        temp2 = temp2 + ")";  
                        //temp1 = "MONTH ='"+month+"' AND to_number(YEAR)>='"+Integer.parseInt(year)+"' and to_number(YEAR)<='"+Integer.parseInt(yearTo)+"'";
                        //temp2 = "a.MONTH ='"+month+"' AND a.YEAR='"+year+"'";                      
                        
                    }                        
                    
                patch1 = "(Select (SELECT zone_code FROM CONTRACT_zone_MASTER WHERE zone_id=wozone) from CONTRACT_WORK_ORDER where woid="+woid+") as zone, "
                                  + "(Select (SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=wodiv) from CONTRACT_WORK_ORDER where woid="+woid+") as div,"
				  + "(Select (SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=wodept) from CONTRACT_WORK_ORDER where woid="+woid+") as dept,"
                                  + "(Select WORKORDERNO from CONTRACT_WORK_ORDER where woid="+woid+") as wonum,";
                if(bankDepDt.equals("") && pfDepDt.equals("")) {
                    sql = "(SELECT "+patch1+ " (A.WORKMANID),TO_CHAR(A.STARTDATE,'DD-MON-YYYY') AS WORKSTARTDATE,TO_CHAR(A.terminationdate,'DD-MON-YYYY') as terminationdate,"
                        + "null as year,null as month,b.name,b.PANNO,B.AADHARNO,null as netamount,0 AS attendance,0 as deduction "
                        + "FROM (SELECT * FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE WOID='"+woid+"' AND "
                        + "(SELECT LAST_DAY(TO_DATE( '"+monthTo+"-"+yearTo+"','MM-YY')) FROM DUAL) >= STARTDATE AND "
                        + "(SELECT TO_DATE( '"+month+"-"+year+"','MM-YY') FROM DUAL) <= TERMINATIONDATE ) A,"
                        + "CONTRACT_WORKMAN_DETAILS B WHERE A.WORKMANID=B.WORKMANID AND A.WORKMANID NOT IN "
                        + "(SELECT WORKMANID FROM CONTRACT_WORKMAN_WAGES  WHERE WOID='"+woid+"' AND "+temp1+" AND "
                        + "WORKSTARTDATE = A.STARTDATE)) union (Select "+patch1+" a.workmanid,TO_CHAR(A.workSTARTDATE,'DD-MON-YYYY') AS workstartdate,"
                        + "(Select TO_CHAR(terminationdate,'DD-MON-YYYY') from CONTRACT_WORKMAN_EMPLOY_REC where startdate=a.WORKSTARTDATE and workmanid=a.workmanid and woid='"+woid+"' "
                        + "and CONTRACTORID='"+contid+"') as terminationdate,a.year,a.month,b.name,B.panno,b.aadharno,a.netamount,a.attendance,nvl(a.pfgross+a.pension+a.otherdeduction,0) as deduction "
                        + "FROM CONTRACT_WORKMAN_WAGES a left join "
                        + "CONTRACT_WORKMAN_DETAILS b on a.workmanid=b.workmanid  where a.CONTRACTORID='"+contid+"' AND a.WOID='"+woid+"' AND "+temp2+")";  
                }
                else {
                    if(!bankDepDt.equals("") && pfDepDt.equals(""))
                        patch = " to_char(a.BANKDEPOSITDATE,'yyyy-mm-dd')='"+bankDepDt+"' and ";
                    else if(bankDepDt.equals("") && !pfDepDt.equals(""))
                        patch = " to_char(a.PFDEPOSITDATE,'yyyy-mm-dd')='"+pfDepDt+"' and ";
                    else if(!bankDepDt.equals("") && !pfDepDt.equals(""))        
                        patch = " to_char(a.PFDEPOSITDATE,'yyyy-mm-dd')='"+pfDepDt+"' and to_char(a.BANKDEPOSITDATE,'yyyy-mm-dd')='"+bankDepDt+"' and ";
                    sql = " Select "+patch1+" a.workmanid,TO_CHAR(A.workSTARTDATE,'DD-MON-YYYY') AS workstartdate,"
			+ "(Select TO_CHAR(terminationdate,'DD-MON-YYYY') from CONTRACT_WORKMAN_EMPLOY_REC where startdate=a.WORKSTARTDATE and workmanid=a.workmanid and woid='"+woid+"' "
			+ "and CONTRACTORID='"+contid+"') as terminationdate,a.year,a.month,b.name,B.panno,b.aadharno,a.netamount,a.attendance,nvl(a.pfgross+a.pension+a.otherdeduction,0) as deduction "
                        + "FROM CONTRACT_WORKMAN_WAGES a left join "
                        + "CONTRACT_WORKMAN_DETAILS b on a.workmanid=b.workmanid where "+patch+" a.CONTRACTORID='"+contid+"' AND a.WOID='"+woid+"' AND "+temp2;
                }
		
                al = new ArrayList();
                PreparedStatement ps = con.prepareStatement(sql);
                System.out.println(sql);
                ResultSet rs = ps.executeQuery();			
                while(rs.next()) {
                 String a[] = {rs.getString("workmanid"),rs.getString("name"),rs.getString("workstartdate"),rs.getString("terminationdate"),
                     rs.getString("panno"),rs.getString("aadharno"),String.valueOf(rs.getInt("netamount")),rs.getString("wonum"),rs.getString("zone"),rs.getString("div"),rs.getString("dept"),
                     rs.getString("attendance"),rs.getString("deduction"),rs.getString("month"),rs.getString("year")};
                 al.add(a);
                }
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;
	}
	public ArrayList adminUserList(AdministratorDTO adto) {
		Connection con=null;		
		ArrayList al = null;
                String patch = "";
                
                if(adto.getLevelId().equals("1"))
                    patch = " where zoneid='"+adto.getZoneId()+"' ";
                if(adto.getLevelId().equals("2"))
                    patch = " where divid="+adto.getDivId();
                if(adto.getLevelId().equals("3")) {
                    if(adto.getDivOrHq().equals("d"))
                        patch = " where divid="+adto.getDivId()+" and deptid="+adto.getDeptId();
                    else
                        patch=" where zoneid='"+adto.getZoneId()+"' and deptid in (Select DEPTID from DEPARTMENT_MASTER where ZONEID='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"' ) ";
                }                
                
                String sql = "Select nvl( (SELECT short_desig FROM ADMIN_DESIGNATION WHERE id=a.DESIGNATION),' ') as desig,nvl(DESIGNATION,' ') as desigid,nvl(email,' ') as email,nvl(mobile,' ') as mobile,PRIV_APPROVE_CONTR,PRIV_APPROVE_WO,PRIV_CREATE_USER,USERID,active,(Select ZONE_CODE from CONTRACT_ZONE_MASTER where ZONE_ID=a.zoneid) as ZONE,"
                        + "(Select DIVCODE from CONTRACT_DIV_MASTER where DIVID=a.divid) as DIV,(Select DEPTNAME from DEPARTMENT_MASTER where deptid=a.deptid) as DEPT,LEVELID "
                        + "from ADMIN_USERS a "+patch;  
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {	
				String a[] = {rs.getString("userid"),rs.getString("div"),rs.getString("dept"),rs.getString("levelid"),rs.getString("zone"),
                                    String.valueOf(rs.getInt("active")),rs.getString("PRIV_APPROVE_CONTR"),rs.getString("PRIV_APPROVE_WO"),rs.getString("PRIV_CREATE_USER"),
                                    rs.getString("desig"),rs.getString("mobile"),rs.getString("email"),rs.getString("desigid")};
				al.add(a);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al; 		
	}
	public int resetSubAdminPassword(String id,String pwd) {
		Connection con=null;		
		int retVal=0;
		String sql = "Update ADMIN_USERS set PASSWORD=? where USERID=?";  
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, pwd);
			ps.setString(2, id);
			System.out.println(sql);
			retVal = ps.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
	public int changeAdminPassword(String id,String password) {
		Connection con=null;		
		int retVal=0;
		String sql = "Update ADMIN_USERS set PASSWORD='"+password+"' where USERID='"+id+"'";  
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			retVal = ps.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
	public ArrayList getDashboardTbl2(AdministratorDTO adto,String adminLevel) {
		ArrayList al = null;
		Connection con=null;
		String suffix = "";
		//if(adto.getLevelId().equals("1"))
		//	suffix = "ZONEID="+adto.getZoneId();
		//else if(adto.getLevelId().equals("2"))
		//	suffix = "DIVID="+adto.getDivId();
		//else if(adto.getLevelId().equals("3"))
		//	suffix = "DEPTID='"+adto.getDeptId()+"'";
                if(!adminLevel.equals("0")) {                    
                    if(adto.getDeptId()==null)
                        suffix = " where ZONEID="+adto.getZoneId();
                    else
                        suffix=" where zoneid='"+adto.getZoneId()+"' and deptid in (Select DEPTID from DEPARTMENT_MASTER where ZONEID='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"' ) ";

                }		
		String sql = "select (SELECT zone_code FROM CONTRACT_ZONE_MASTER WHERE zone_id=con.zoneid ) AS zone, "
                                + "nvl((SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=con.divid),' ') as div,"
				+ "nvl((SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=con.deptid),' ') as dept,"
				+ "count(*) as regd,"
                                + "sum(case when approved='1' and approvedby is not null then 1 else 0 end) as approved, "
                                + "sum(case when approved='0' then 1 else 0 end) as pending ,"
				+ "sum(case when approved='2' and approvedby is not null then 1 else 0 end) as rejected "
                                + "from CONTRACTORREGISTRATION con "+suffix+ " group by zoneid, divid,deptid order by zoneid asc,divid desc";
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				String a[] = {rs.getString("div"),rs.getString("dept"),String.valueOf(rs.getInt("regd")),String.valueOf(rs.getInt("approved")),String.valueOf(rs.getInt("pending")),String.valueOf(rs.getInt("rejected")),rs.getString("zone")};
				al.add(a);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;		
	}
        public ArrayList getPublicDashboardTbl1() {
		ArrayList al = null;
		Connection con=null;
			
		String sql = "select wozone,wodiv,nvl(wodept,' ') as wodept,nvl(count(*),0) as wo,nvl(sum(totwmen),0) as wmen, nvl(sum(totwage),0) as wages "
                        + "from ( SELECT (SELECT zone_code FROM CONTRACT_zone_MASTER WHERE zone_id=wozone) AS wozone, "
                        + "(SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=wodiv ) AS wodiv, "
                        + "(SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=wodept ) AS wodept, woid, "
                        + "(SELECT COUNT(distinct(workmanid)) FROM CONTRACT_WORKMAN_DETAILS WHERE WOID =cwo.woid ) AS totwmen, "
                        + "(select sum(NETAMOUNT+PFGROSS+PENSION+OTHERDEDUCTION) from CONTRACT_WORKMAN_WAGES where WOID=cwo.woid) as totwage "
                        + "FROM CONTRACT_WORK_ORDER cwo WHERE approved  ='1' and approvalby is not null and sysdate>=DATEOFCOMMENCEMENT and sysdate<=EXTDATEOFCOMPLETION GROUP BY wozone,"
                        + "wodiv , wodept , woid) group by wozone,wodiv,wodept order by wozone,wodiv,wodept";
                //DATEOFCOMMENCEMENT>add_months(sysdate,-12)
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				String a[] = {rs.getString("wozone"),rs.getString("wodiv"),rs.getString("wodept"),String.valueOf(rs.getInt("wo")),
                                    String.valueOf(rs.getInt("wmen")),String.valueOf(rs.getInt("wages"))};
				al.add(a);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;		
	}
        public ArrayList getPublicDashboardTbl2() {
		ArrayList al = null;
		Connection con=null;
			
		String sql = "select (SELECT zone_code FROM CONTRACT_zone_MASTER WHERE zone_id=con.zoneid) as zone, "
                        + "nvl((SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=con.divid),'HQRS.') as div,"
			+ "nvl((SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=con.deptid),' ') as dept,"
			+ "count(*) as regd,sum(case approved when '1' then 1 else 0 end) as approved, sum(case approved when '0' then 1 else 0 end) as pending ,"
			+ "sum(case approved when '2' then 1 else 0 end) as rejected from CONTRACTORREGISTRATION con group by zoneid,divid,deptid order by zoneid,divid desc";
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				String a[] = {rs.getString("zone"),rs.getString("div"),rs.getString("dept"),String.valueOf(rs.getInt("regd")),String.valueOf(rs.getInt("approved")),String.valueOf(rs.getInt("pending")),String.valueOf(rs.getInt("rejected"))};
				al.add(a);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;		
	}
        public ArrayList getPublicDashboardTbl3(String month,String year,String zoneid) {
		ArrayList al = null;
		Connection con=null;
                String zonePatch="";
                if(!zoneid.equals(""))
                    zonePatch = " and (Select WOZONE from CONTRACT_WORK_ORDER where WOID=a.woid)='"+zoneid+"' ";
		String sql = "Select (SELECT NAMEOFTHEWORK FROM CONTRACT_WORK_ORDER WHERE woid=a.woid) AS woname, woid,"
                        + "(SELECT WORKORDERNO FROM CONTRACT_WORK_ORDER WHERE woid=a.woid) AS wonum, "
                        //+ "(Select locid from CONTRACT_WORKORDER_LOCATION where woid=a.woid) as locid, "
                        //+ "(Select locroi from CONTRACT_WORKORDER_LOCATION where woid=a.woid) as locroi, "
                        //+ "(Select locname from CONTRACT_LOCATION_master where locid=(Select locid from contract_workorder_location where woid=a.woid)) as locname, "
                        + "(Select EXTDATEOFCOMPLETION-DATEOFCOMMENCEMENT+1 from contract_work_order where woid=a.woid) AS workDays, "
                        + "(SELECT name FROM CONTRACTORREGISTRATION WHERE id=a.CONTRACTORID) AS contName, "
                        + "(SELECT FIRMNAME FROM CONTRACTORREGISTRATION WHERE id=a.CONTRACTORID) AS contFirmName,count(distinct(workmanid)) as empCount,"
                        + "sum(netamount+pfgross+pension+OTHERDEDUCTION) as totwages,round(avg(netamount+pfgross+pension+OTHERDEDUCTION),2) as avgsalary,"
                        + "(Select (SELECT zone_code FROM CONTRACT_zone_MASTER WHERE zone_id=wozone) from CONTRACT_WORK_ORDER where WOID=a.woid) as zone,"
                        + "(Select (SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=wodiv) from CONTRACT_WORK_ORDER where WOID=a.woid) as div,"
                        + "(Select (SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=wodept) from CONTRACT_WORK_ORDER where WOID=a.woid) as dept "
                        + "from CONTRACT_WORKMAN_WAGES a where MONTH ='"+month+"' AND YEAR ='"+year+"' " + zonePatch + " group by a.woid,a.CONTRACTORID order by woname asc";
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				String a[] = {rs.getString("woname"),rs.getString("contname"),rs.getString("contfirmname"),String.valueOf(rs.getInt("empcount")),
                                    String.valueOf(rs.getInt("totwages")),String.valueOf(rs.getInt("avgsalary")),rs.getString("zone"),rs.getString("div"),rs.getString("dept"),
                                    String.valueOf(rs.getInt("workDays")),rs.getString("wonum"),rs.getString("woid")};
				al.add(a);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;		
	}
        
      public int checkWMPan(String pan)
      {
    	  int result=1;
    	  
    	  Connection con=null;
    	  try
    	  {
 
    	  con = DBConnection.getInstance().getDBConnection();
    	  String sql="SELECT * FROM CONTRACT_WORKMAN_DETAILS WHERE PANNO=?";    	  
    	  PreparedStatement ps = con.prepareStatement(sql);
    	  ps.setString(1, pan);
		  System.out.println(sql);
		  ResultSet rs = ps.executeQuery();	
		  if(rs.next())
		  {
			  result=0;
		  }	  
    	  }
    	  catch (Exception ex) {
				ex.printStackTrace();
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
    	  return result;
    	  
      }
      
      public int checkWMAdhar(String adhar)
      {
    	  int result=1;
    	  
    	  Connection con=null;
    	  try
    	  {
 
    	  con = DBConnection.getInstance().getDBConnection();
    	  String sql="SELECT * FROM CONTRACT_WORKMAN_DETAILS WHERE AADHARNO=?";    	  
    	  PreparedStatement ps = con.prepareStatement(sql);
    	  ps.setString(1, adhar);
		  System.out.println(sql);
		  ResultSet rs = ps.executeQuery();	
		  if(rs.next())
		  {
			  result=0;
		  }	  
    	  }
    	  catch (Exception ex) {
				ex.printStackTrace();
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
    	  return result;
    	  
      }
      public int enableDisableAdmin(String id,String status) {
		Connection con=null;		
		int retVal=0;
		String sql = "Update ADMIN_USERS set active="+status+" where USERID='"+id+"'";  
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			retVal = ps.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public int checkAadhar(String pan,String contid) {
		Connection con=null;		
		int retVal=0;
		try {
			con = DBConnection.getInstance().getDBConnection();
                        String patch="";
                        if(contid==null)
                            patch=" and approved!='2' ";
                        else
                            patch=" and ID!="+contid+" and approved!='2' ";
                        String sql="Select * from CONTRACTORREGISTRATION where AADHAR='"+pan+"'"+patch;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();			
			if(rs.next() )
				retVal=1;
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public int checkMobile(String pan,String contid) {
		Connection con=null;		
		int retVal=0;
		try {
                    con = DBConnection.getInstance().getDBConnection();
                    String patch="";
                        if(contid==null)
                            patch=" and approved!='2' ";
                        else
                            patch=" and ID!="+contid+" and approved!='2' ";
                        String sql="Select * from CONTRACTORREGISTRATION where MOBILENO='"+pan+"'" + patch;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next() )
				retVal=1;
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public int checkFirmRegNo(String pan) {
		Connection con=null;		
		int retVal=0;
                
		try {
                    con = DBConnection.getInstance().getDBConnection();
                    String sql="Select * from CONTRACTORREGISTRATION where lower(FIRMREGISTRATIONNO)='"+pan.toLowerCase()+"' and approved!='2' ";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next() )
				retVal=1;
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public int checkPFNo(String pan,String contid) {
		Connection con=null;		
		int retVal=0;
		try {
			con = DBConnection.getInstance().getDBConnection();
                        String patch="";
                        if(contid==null)
                            patch=" and approved!='2' ";
                        else
                            patch=" and ID!="+contid+" and approved!='2' ";
                        String sql="Select * from CONTRACTORREGISTRATION where lower(PFREGISTRATIONO)='"+pan.toLowerCase()+"'"+patch;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next() )
                            retVal=1;
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public int checkEmail(String pan,String contid) {
		Connection con=null;		
		int retVal=0;
		try {
                    con = DBConnection.getInstance().getDBConnection();
                    String patch="";
                        if(contid==null)
                            patch=" and approved!='2' ";
                        else
                            patch=" and ID!="+contid+" and approved!='2' ";
                        String sql="Select * from CONTRACTORREGISTRATION where lower(EMAILID)='"+pan.toLowerCase()+"'"+patch;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next() )
				retVal=1;
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public int checkWOLOI(String pan) {
		Connection con=null;		
		int retVal=0;
		try {
                    con = DBConnection.getInstance().getDBConnection();
			String sql="Select * from CONTRACTORREGISTRATION where lower(WOREFERNCE) like '"+pan.toLowerCase()+"%' and approved!='2' ";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next() )
				retVal=1;
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public ArrayList getZonalContCount(AdministratorDTO adto,String adminLevel) {
		ArrayList al = null;
		Connection con=null;
			
		String sql = "select (select zone_code from CONTRACT_ZONE_MASTER where zone_id=au.zoneid) as zonecode,count(*) as count "
                        + "from contractorregistration cr,admin_users au where cr.approvedby=au.userid and cr.approved=1 group by au.zoneid ";
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
                            String a[] = {rs.getString("zonecode"),String.valueOf(rs.getInt("count"))};
                            al.add(a);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;		
	}
      
      public int checkWMMobile(String mobile,String wmid) {
  		Connection con=null;		
  		int retVal=0;
  		System.out.println("wmid"+wmid);
  		try {
  			con = DBConnection.getInstance().getDBConnection();
                    String sql="Select workmanid from contract_workman_details where mobileno=?";
  			PreparedStatement ps = con.prepareStatement(sql);
  			ps.setString(1, mobile);
  			ResultSet rs = ps.executeQuery();
  			if(rs.next() )
  			{	
  				if(!rs.getString("WORKMANID").equals(wmid))
  				{ 		
                              retVal=1;
  				}             
  			}                  
  		}
  		catch(Exception ex) {
  			ex.printStackTrace();
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
  		return retVal; 		
  	} 
      
      public int checkWMPf(String pfno) {
    		Connection con=null;		
    		int retVal=0;
    		try {
    			con = DBConnection.getInstance().getDBConnection();
                    String sql="";
    			PreparedStatement ps = con.prepareStatement(sql);
    			ps.setString(1, pfno);
    			ResultSet rs = ps.executeQuery();
    			if(rs.next() )
                                retVal=1;
    		}
    		catch(Exception ex) {
    			ex.printStackTrace();
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
    		return retVal; 		
    	}   
      
       public int getMinWageByDt(int empType,int skillType,String startDt,String woarea)
  	{
  		int minWage=0;
  		Connection con=null;
  		try
  		{
  		  /*	String validFrom,validTo;
  		    int month=new Integer(startDt.substring(5,7));	
  		    System.out.println("month"+month);
  		    if(month>3 && month <=9 )
  		    {
  		      	validFrom= "01-APR-"+startDt.substring(0,4);
  		      System.out.println("validFrom"+validFrom);
  		         validTo="30-SEP-"+startDt.substring(0,4);
  		    } 
  		    else if(month>9 && month <=12)
  		    {
  		    	 int year=new Integer(startDt.substring(0,4)) + 1;  // get next year
  	  		      validFrom= "01-OCT-"+startDt.substring(0,4);		  
  	  		      validTo="31-MAR-"+year;	
  	  		    System.out.println("validTo"+validTo);
  		    	
  		    }	
  		    else if(month>=1 && month <4)
  		    {
  		      int year=new Integer(startDt.substring(0,4)) - 1;   // gdet Previous year
  		      validFrom= "01-OCT-"+startDt.substring(0,4);		  
  		      validTo="31-MAR-"+year;	
  		    System.out.println("validTo"+validTo);
  		    } 	 */
  		    
  		 	con= DBConnection.getInstance().getDBConnection();
  		/*	String sql="SELECT MINWAGE,TO_DATE(VALIDFROM,'DD-MON-YYYY') AS VALIDFROM,TO_DATE(VALIDTO,'DD-MM-YYYY') AS VALIDTO "
  					+ "FROM CONTRACT_MINWAGE_MASTER WHERE EMPLOYMENTTYPE='"+empType+"' "
  				+ "AND SKILLTYPE='"+skillType+"' AND WOAREA=(SELECT WOAREA FROM CONTRACT_WORK_ORDER WHERE WOID='"+woid+"') "
  				+ "AND (SELECT TO_DATE('"+startDt+"','YYYY-MM-DD') FROM DUAL ) BETWEEN VALIDFROM AND VALIDTO "; */
  		 	
  		 	String sql="SELECT MINWAGE,TO_DATE(VALIDFROM,'DD-MON-YYYY') AS VALIDFROM,TO_DATE(VALIDTO,'DD-MM-YYYY') AS VALIDTO "
  					+ "FROM CONTRACT_MINWAGE_MASTER WHERE EMPLOYMENTTYPE=? "
  				+ "AND SKILLTYPE=? AND WOAREA=? AND (SELECT TO_DATE(?,'YYYY-MM-DD') FROM DUAL ) BETWEEN VALIDFROM AND VALIDTO "; 
  			System.out.println(sql);
  			PreparedStatement pst=con.prepareStatement(sql);
  			pst.setInt(1, empType);
  			pst.setInt(2, skillType);
  			pst.setString(3, woarea);
  			pst.setString(4, startDt);
  			ResultSet rs = pst.executeQuery();
  			while(rs.next())
  			{
  				minWage = rs.getInt("MINWAGE");
  			}	
  			
  		}
  		catch(Exception ex)
  		{
  			ex.printStackTrace();
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
  		
  		return minWage;
  	}
    
      
       
       public int getMinWageProfile(String startDt,String wmid,String woloc)
     	{
     		int minWage=0;
     		Connection con=null;
     		try
     		{
     	  		    
     		 	con= DBConnection.getInstance().getDBConnection();
     			String sql="SELECT MINWAGE,TO_DATE(VALIDFROM,'DD-MON-YYYY') AS VALIDFROM,TO_DATE(VALIDTO,'DD-MM-YYYY') AS VALIDTO "
     				+ "FROM CONTRACT_MINWAGE_MASTER A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE B.WORKMANID=? AND B.ACTIVE='1' "
     				+ "AND A.EMPLOYMENTTYPE=B.EMPLOYMENTTYPE AND A.SKILLTYPE=B.SKILLTYPE AND A.WOAREA=(SELECT AREA FROM CONTRACT_LOCATION_MASTER WHERE LOCID=?) "
     				+ "AND (SELECT TO_DATE(?,'YYYY-MM-DD') FROM DUAL ) BETWEEN VALIDFROM AND VALIDTO ";
     			System.out.println(sql);
     			PreparedStatement pst=con.prepareStatement(sql);
     			pst.setString(1, wmid);
     			pst.setString(2, woloc);
     			pst.setString(3, startDt);
     			ResultSet rs = pst.executeQuery();
     			while(rs.next())
     			{
     				minWage = rs.getInt("MINWAGE");
     			}	
     			
     		}
     		catch(Exception ex)
     		{
     			ex.printStackTrace();
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
     		System.out.println("minWage"+minWage);
     		return minWage;
     	}
      public int modifyCreateAdminPriv(String id,String status) {
		Connection con=null;		
		int retVal=0;
		String sql = "Update ADMIN_USERS set PRIV_CREATE_USER="+status+" where USERID='"+id+"'";  
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			retVal = ps.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public int modifyContApprPriv(String id,String status) {
		Connection con=null;		
		int retVal=0;
                if(status.equalsIgnoreCase("true"))
                    status = "1";
                else
                    status = "0";
		String sql = "Update ADMIN_USERS set PRIV_APPROVE_CONTR="+status+" where USERID='"+id+"'";  
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			retVal = ps.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public int modifyWOApprPriv(String id,String status) {
		Connection con=null;		
		int retVal=0;
                if(status.equalsIgnoreCase("true"))
                    status = "1";
                else
                    status = "0";
		String sql = "Update ADMIN_USERS set PRIV_APPROVE_WO="+status+" where USERID='"+id+"'";  
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			retVal = ps.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retVal; 		
	}
      public ArrayList getWageRows(String period) {
		ArrayList al = null;
		Connection con=null;  
                String fromdate = period.split(" to ")[0];
                String sql = "select to_char(validfrom,'dd-MON-yy') as validfrom,to_char(validto,'dd-MON-yy') as validto,WOAREA,EMPLOYMENTTYPE,SKILLTYPE,MINWAGE,(Select EMPLOYMENT_TYPE from CONTRACT_EMPLOYMENT_TYPE where employment_type_id=EMPLOYMENTTYPE) as et,(Select SKILL_TYPE from CONTRACT_SKILL_TYPE where SKILL_TYPE_ID=SKILLTYPE and EMPLOYMENT_TYPE_ID=EMPLOYMENTTYPE ) as st from CONTRACT_MINWAGE_MASTER where VALIDFROM='"+fromdate+"' order by et";
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				String a[] = {rs.getString("woarea"),String.valueOf(rs.getInt("employmenttype")),String.valueOf(rs.getInt("skilltype")),String.valueOf(rs.getInt("minwage")),rs.getString("et"),rs.getString("st"),rs.getString("validfrom"),rs.getString("validto")};
				al.add(a);
			}
                        if(al.size()==0) {
                            sql = "Select null as validfrom,null as validto,woarea,EMPLOYMENT_TYPE_id,SKILL_TYPE_id,null as minwage,employment_type,skill_type from contract_woarea,(select et.employment_type_id, employment_type,skill_type_id,skill_type from contract_employment_type et left join CONTRACT_SKILL_TYPE st on et.EMPLOYMENT_TYPE_ID=st.EMPLOYMENT_TYPE_ID) order by employment_type";
                            ps = con.prepareStatement(sql);
                            System.out.println(sql);
                            rs = ps.executeQuery();			
                            while(rs.next()) {
                                String a[] = {rs.getString("woarea"),String.valueOf(rs.getInt("employment_type_id")),String.valueOf(rs.getInt("skill_type_id")),rs.getString("minwage"),rs.getString("employment_type"),rs.getString("skill_type"),rs.getString("validfrom"),rs.getString("validto")};
                                al.add(a);
                            }
                        }
		}
		catch(Exception ex) {
                    ex.printStackTrace();
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
		return al;		
	}
      public int saveNewWages(ArrayList al,String period) {
		
		Connection con=null;  
                String validfrom = period.split(" to ")[0];
                String validto = period.split(" to ")[1];
                int result = 0;
		try {
                        con = DBConnection.getInstance().getDBConnection();
                        con.setAutoCommit(false);
                        con.commit();
			for(int i = 0;i<al.size();++i) {
                            String t = (String)al.get(i);
                            String vals[] = t.split("\\.");
                            String sql = "Insert into CONTRACT_MINWAGE_MASTER values (?,?,?,?,?,?)";
                            PreparedStatement ps = con.prepareStatement(sql);
                            ps.setString(1, vals[0]);
                            ps.setInt   (2, Integer.parseInt(vals[1]) );
                            ps.setInt   (3, Integer.parseInt(vals[2]) );
                            ps.setInt   (4, Integer.parseInt(vals[3]) );
                            ps.setString(5, validfrom);
                            ps.setString(6, validto);                                                        
                            result = result + ps.executeUpdate();                            
                        }
                        con.commit();
		}
		catch(Exception ex) {
                    ex.printStackTrace();
                    try {
                        con.rollback();
                    } catch (SQLException ex1) {
                        Logger.getLogger(UtilityDao.class.getName()).log(Level.SEVERE, null, ex1);
                    }
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
		return result;		
	}
      public int editWage(String period,String id,String newval) {
		Connection con=null;  
                String validfrom = period.split(" to ")[0];
                String ids[] = id.split("\\.");
                int retval = 0;
                String sql = "Update CONTRACT_MINWAGE_MASTER set minwage=? where WOAREA=? and EMPLOYMENTTYPE=? and SKILLTYPE=? and VALIDFROM=?";
		try {
                    con = DBConnection.getInstance().getDBConnection();
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt   (1,Integer.parseInt(newval));
                    ps.setString(2,ids[0]);
                    ps.setInt   (3,Integer.parseInt(ids[1]));
                    ps.setInt   (4,Integer.parseInt(ids[2]));
                    ps.setString(5,validfrom);                        
                    retval = ps.executeUpdate();			
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retval;		
	}
      public int regGrievance(String usertype,String probDesc,String wo,String fwdTo,String userid) {
		Connection con=null;  
                int retval = 0;
                String fwdToType=null,fwdToId=null,zoneid=null,divid=null,deptid=null;
                if(usertype.equals("a")) {
                    fwdToType = "'cris'";
                    fwdToId = null;
                    zoneid = "(Select ZONEID from ADMIN_USERS where USERID='"+userid+"')";
                    divid = "(Select divid from ADMIN_USERS where USERID='"+userid+"')";
                    deptid = "(Select deptid from ADMIN_USERS where USERID='"+userid+"')";
                }
                else if(usertype.equals("c")) {
                    fwdToType = "'a'";
                    if(wo!=null) {
                        fwdToId = "(Select APPROVALBY from CONTRACT_WORK_ORDER where WOID="+wo+")";
                        zoneid = "(Select WOZONE from CONTRACT_WORK_ORDER where woid="+wo+")";
                        divid = "(Select WOdiv from CONTRACT_WORK_ORDER where woid="+wo+")";
                        deptid = "(Select WOdept from CONTRACT_WORK_ORDER where woid="+wo+")";
                    }
                    else {
                        fwdToId = "(Select APPROVEDBY from CONTRACTORREGISTRATION where ID="+userid+")";
                        zoneid = "(Select ZONEID from ADMIN_USERS where USERID="+fwdToId+")";
                        divid = "(Select divid from ADMIN_USERS where USERID="+fwdToId+")";
                        deptid = "(Select deptid from ADMIN_USERS where USERID="+fwdToId+")";
                    }
                }
                else if(usertype.equals("e")) {
                    if(fwdTo.equals("a")) {
                        fwdToType = "'a'";
                        fwdToId = "(Select NAMEOFTHEPRINCIPALEMPLOYER from CONTRACT_WORK_ORDER where WOID="+wo+")";
                    }
                    else if(fwdTo.equals("c")) {
                        fwdToType = "'c'";
                        fwdToId = "(Select CONTRACTORID from CONTRACT_WORK_ORDER where woid="+wo+")";
                    }
                    zoneid = "(Select WOZONE from CONTRACT_WORK_ORDER where woid="+wo+")";
                    divid = "(Select WOdiv from CONTRACT_WORK_ORDER where woid="+wo+")";
                    deptid = "(Select WOdept from CONTRACT_WORK_ORDER where woid="+wo+")";
                }
                    
                String sql = "insert into GRIEVANCE_MASTER values ( (select nvl(max(gid),0)+1 from grievance_master), '"+userid+"',?,'open',sysdate,"+wo+","+fwdToType+","+fwdToId+","+zoneid+","+divid+","+deptid+",'"+usertype+"','REG',1,'"+userid+"','"+usertype+"',null)";
		System.out.println(sql);
                try {
                    con = DBConnection.getInstance().getDBConnection();
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, probDesc);
                    retval = ps.executeUpdate();			
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return retval;		
	}
    public ArrayList getGrievances(String usertype,String userid,AdministratorDTO adto) {
          ArrayList al = new ArrayList();
          Connection con = null;
          try {
              con = DBConnection.getInstance().getDBConnection();
              String whereClause = "";
              if( usertype.equals("e") )
                  whereClause = " where REGD_BY_ID='"+userid+"' and step=1 ";
              else if( usertype.equals("c") )
                  whereClause = " where (REGD_BY_ID='"+userid+"' or FWD_TO_ID='"+userid+"') and step=1 ";
              else if( usertype.equals("a") && !adto.getDesigId().equals("0")) { // i.e. railway user
                    if(adto.getLevelId().equals("1")) {                            
                        whereClause=" where ( zoneid='"+adto.getZoneId()+"' or REGD_BY_ID='"+userid+"' ) and "
                                //+ "( (Select FWD_TO_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a' or (Select ACTION_BY_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a' or (Select REGD_BY_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a') and "
                                + "step=1 ";
                    }
                    else if(adto.getLevelId().equals("2"))
                        whereClause=" where ( DIVid='"+adto.getDivId()+"' or REGD_BY_ID='"+userid+"' )  and "
                                //+ "( (Select FWD_TO_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a' or (Select ACTION_BY_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a' or (Select REGD_BY_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a') and "
                                + "step=1 ";
                    else if(adto.getLevelId().equals("3")) {
                        if(adto.getDivOrHq().equals("d"))    
                            whereClause=" where ( (DIVid='"+adto.getDivId()+"' and DEPTid='"+adto.getDeptId()+"') or REGD_BY_ID='"+userid+"' )  and "
                                //+ "( (Select FWD_TO_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a' or (Select ACTION_BY_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a' or (Select REGD_BY_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a') and "
                                + "step=1 ";
                        else
                           whereClause=" where ( (ZONEid='"+adto.getZoneId()+"' and deptid in (Select DEPTID from DEPARTMENT_MASTER where ZONEID='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"' ) ) or REGD_BY_ID='"+userid+"' )  and "
                                //+ "( (Select FWD_TO_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a' or (Select ACTION_BY_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a' or (Select REGD_BY_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid))='a') and "
                                + "step=1 ";                    
                    }
                    else
                        whereClause = " where step=1 ";
              }
              else
                  whereClause = " where step=1 ";
              String sql = "Select GID,REGD_BY_ID,DESCRIPTION,"
                  + "(Select status from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid)) as STATUS,"
                  + "(Select FWD_TO_ID from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid)) as FWD_TO_ID,"
                  + "(Select FWD_TO_TYPE from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid)) as FWD_TO_TYPE,"
                  + "(Select to_char(REGN_TIMESTAMP,'dd-mm-yyyy hh24:mi') from GRIEVANCE_MASTER where gid=gm.gid and step=(Select max(step) from GRIEVANCE_MASTER where gid=gm.gid)) as lastUpdateTime,"
                  + "to_char(REGN_TIMESTAMP,'dd-mm-yyyy hh24:mi') as regd_on,woid,"
                  + "(Select WO_FILENO from CONTRACT_WORK_ORDER where WOID=gm.woid) as wofile,(select WORKORDERNO from CONTRACT_WORK_ORDER "
                  + "where WOID=gm.woid) as wono,ZONEID,DIVID,DEPTID,REGD_BY_TYPE,ACTION_TYPE,STEP,ACTION_BY_ID,ACTION_BY_TYPE, "
                  + "(Select name from CONTRACTORREGISTRATION where to_char(id)=REGD_BY_ID) as contname,"
                  + "(Select firmname from CONTRACTORREGISTRATION where to_char(id)=REGD_BY_ID) as contfirmname, "
                  + "(SELECT short_desig FROM admin_designation WHERE to_char(id)= (SELECT designation FROM admin_users WHERE userid=action_by_id )) AS admindesig,"
                  + "(SELECT zone_code FROM CONTRACT_zone_MASTER WHERE zone_id=(SELECT zoneid FROM admin_users WHERE userid=action_by_id ) ) AS adminzone, "
                  + "(SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=(SELECT divid FROM admin_users WHERE userid=action_by_id)) AS admindiv,"
                  + "(SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=(SELECT deptid FROM admin_users WHERE userid=action_by_id)) AS admindept, "
                  + "(SELECT PANNO FROM CONTRACTORREGISTRATION WHERE to_char(id)=action_by_id) AS contPan "
                  + "from GRIEVANCE_MASTER gm "+whereClause+" order by REGN_TIMESTAMP desc,status desc ";
              System.out.println(sql);
              PreparedStatement ps = con.prepareStatement(sql);
              ResultSet rs = ps.executeQuery();
              while(rs.next()) {
                  String a[] = { rs.getString("GID"), rs.getString("REGD_BY_ID"), rs.getString("regd_on"), rs.getString("DESCRIPTION"), rs.getString("wono"), 
                      rs.getString("status"),rs.getString("regd_by_type"),rs.getString("contname"),rs.getString("contfirmname"), rs.getString("admindesig"),
                      rs.getString("adminzone"),rs.getString("admindiv"),rs.getString("admindept"),rs.getString("woid"),rs.getString("wofile"),rs.getString("lastUpdateTime"),
                      rs.getString("FWD_TO_ID"),rs.getString("FWD_TO_TYPE"),rs.getString("contPan")};
                  al.add(a);
              }
          }
          catch(Exception ex) {
                        ex.printStackTrace();
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
          return al;
      }
      public ArrayList getDesigList(String levelid,String deptid,String divorhq,String zoneid,String divid) {
          ArrayList al = new ArrayList();
          Connection con = null;
          try {
              con = DBConnection.getInstance().getDBConnection();
              String divClause="", zoneClause="", deptClause;
              
              if(divid==null || divid.equals(""))
                  divClause = " and divid is null ";
              else
                  divClause = " and divid= "+divid;
              
              if(zoneid==null || zoneid.equals(""))
                  zoneClause = " and zoneid is null ";
              else
                  zoneClause = " and zoneid= "+zoneid;
              
              if(deptid==null || deptid.equals(""))
                  deptClause = " and deptid is null ";
              else
                  deptClause = " and deptid= "+deptid;
              
              String checkDuplicateAdminDesigClause = " and (Select count(*) from admin_users where levelid='"+levelid+"' and designation=id "+zoneClause+" "+divClause+" "+deptClause+" )=0 ";
              
              String sql = "";
              if(!deptid.equals("") && levelid.equals("3"))
                sql = "Select id,short_desig from ADMIN_DESIGNATION where div_hq='"+divorhq+"' and active=1 and deptcode=(Select deptcode from DEPARTMENT_MASTER where "
                        + "deptid="+deptid+") and levelid='"+levelid+"'"+checkDuplicateAdminDesigClause;
              else
                  sql = "Select id,short_desig from ADMIN_DESIGNATION where active=1 and deptcode is null and levelid='"+levelid+"'"+checkDuplicateAdminDesigClause;
              System.out.println(sql);
              PreparedStatement ps = con.prepareStatement(sql);
              ResultSet rs = ps.executeQuery();
              while(rs.next()) {
                  String a[] = { rs.getString("ID"), rs.getString("short_desig") };
                  al.add(a);
              }
          }
          catch(Exception ex) {
			ex.printStackTrace();
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
          return al;
      }
      public ArrayList getDesigList1(String zoneid,String divid,String deptid,String divorhq) {
          ArrayList al = new ArrayList();
          Connection con = null;
          try {
              con = DBConnection.getInstance().getDBConnection();
              String sql = "";
              if( deptid!=null && !deptid.equals("") )
                    sql = "Select USERID,designation,(Select SHORT_DESIG from ADMIN_DESIGNATION where id=designation) as shortdesig from admin_users au where zoneid="+zoneid+" and divid="+divid+" and deptid="+deptid;
              else  {                 
                  if( !divid.equals("") && divorhq.equals("d"))
                    sql = "Select USERID,designation,(Select SHORT_DESIG from ADMIN_DESIGNATION where id=designation) as shortdesig from admin_users au where zoneid="+zoneid+" and divid="+divid+" and levelid=2";
                  else 
                    sql = "Select USERID,designation,(Select SHORT_DESIG from ADMIN_DESIGNATION where id=designation) as shortdesig from admin_users au where zoneid="+zoneid+" and divid is null";  
              }
              System.out.println(sql);
              PreparedStatement ps = con.prepareStatement(sql);
              ResultSet rs = ps.executeQuery();
              while(rs.next()) {                  
                    String a[] = { rs.getString("USERID"), rs.getString("shortdesig") };
                    al.add(a);                  
              }
          }
          catch(Exception ex) {
                ex.printStackTrace();
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
          return al;
      }
      public String checkdivhq(String divid) {
          String s = "";
          Connection con = null;
          try {
              con = DBConnection.getInstance().getDBConnection();
              String sql = "Select div_hq from CONTRACT_DIV_MASTER where divid="+divid;
              System.out.println(sql);
              PreparedStatement ps = con.prepareStatement(sql);
              ResultSet rs = ps.executeQuery();
              if(rs.next()) {
                  s = rs.getString("div_hq");
              }
          }
          catch(Exception ex) {
			ex.printStackTrace();
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
          return s;
      }
      public int updateProb(String pid,String probUpdate,String userid,String usertype,String actiontype,String userdesigid,String rlyAuth,String classifier) {
          String cs = "",closedStatus="";
          String FWD_TO_TYPE="",FWD_TO_ID="";
          int r=0;
          Connection con = null;
          try {
              if( usertype.equals("a") && userdesigid.equals("0") ) {                  
                  usertype = "cris";
              }              
              if(actiontype.equals("a") ) {
                  cs = "FWD";
                  closedStatus = "open";
                  if(usertype.equals("cris"))
                    FWD_TO_ID = "'"+rlyAuth+"',";
                  else
                    FWD_TO_ID="(Select NAMEOFTHEPRINCIPALEMPLOYER from CONTRACT_WORK_ORDER where WOID=(Select distinct(WOID) from GRIEVANCE_MASTER where gid="+pid+")),";  
                  FWD_TO_TYPE = "'a'";
                  classifier = null;
              }
              else if(actiontype.equals("CLO") ) {
                  cs = "CLO";
                  closedStatus = "closed";
                  FWD_TO_ID = "null,";
                  FWD_TO_TYPE = "null";
                  classifier = "'"+classifier+"'";
              }
              else if(actiontype.equals("c") ) {
                  cs = "FWD";
                  closedStatus = "open";
                  FWD_TO_ID = "(Select CONTRACTORID from CONTRACT_WORK_ORDER where woid=(Select distinct (WOID) from GRIEVANCE_MASTER where gid="+pid+")),";
                  FWD_TO_TYPE = "'c'";
                  classifier = null;
              }
              else if(actiontype.equals("cris") ) {
                  cs = "FWD";
                  closedStatus = "open";
                  FWD_TO_ID = "null,";
                  FWD_TO_TYPE = "'cris'";
                  classifier = null;
              }
              else if(actiontype.equals("REP") ) {
                  if(usertype.equals("e")) {
                      cs = "FWD";
                      closedStatus = "open";
                      FWD_TO_ID = "(Select FWD_TO_ID from GRIEVANCE_MASTER where gid="+pid+" and step=1),";
                      FWD_TO_TYPE = "(Select FWD_TO_TYPE from GRIEVANCE_MASTER where gid="+pid+" and step=1)";
                      classifier = null;
                  }
                  else {
                      cs = "REP";
                      closedStatus = "open";
                      FWD_TO_ID = "null,";
                      FWD_TO_TYPE = "null";
                      classifier = null;
                  }                      
              }
                 
              con = DBConnection.getInstance().getDBConnection();
              String sql = "insert into GRIEVANCE_MASTER values ("+pid+",(Select REGD_BY_ID from GRIEVANCE_MASTER where gid="+pid+" and step=1),?,'"+closedStatus+"',"
                      + "sysdate,(Select woid from GRIEVANCE_MASTER where gid="+pid+" and step=1),"+FWD_TO_TYPE+","+FWD_TO_ID
                      + "(Select zoneid from GRIEVANCE_MASTER where gid="+pid+" and step=1),"
                      + "(Select divid from GRIEVANCE_MASTER where gid="+pid+" and step=1),(Select deptid from GRIEVANCE_MASTER where gid="+pid+" and step=1),"
                      + "(Select REGD_BY_TYPE from GRIEVANCE_MASTER where gid="+pid+" and step=1),'"+cs+"',"
                      + "(Select max(step) from GRIEVANCE_MASTER where gid="+pid+")+1, '"+userid+"','"+usertype+"',"+classifier+") ";
              System.out.println(sql);
              PreparedStatement ps = con.prepareStatement(sql);
              ps.setString(1, probUpdate);
              r = ps.executeUpdate();              
              return r;
          }
          catch(Exception ex) {
			ex.printStackTrace();
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
          return r;
      }
      public ArrayList getProbHist(String pid,String usertype,String userid) {
          ArrayList al = new ArrayList();
          Connection con = null;
          try {
              con = DBConnection.getInstance().getDBConnection();
              String sql = "Select GID,REGD_BY_ID,DESCRIPTION,STATUS,to_char(REGN_TIMESTAMP,'dd-mm-yy hh24:mi') as regnTime,WOID,"
                      + "(Select WO_FILENO from CONTRACT_WORK_ORDER where WOID=gm.woid) as wofilenum,"
                      + "(Select WORKORDERNO from CONTRACT_WORK_ORDER where woid=gm.woid) as wonum,FWD_TO_TYPE,FWD_TO_ID,ZONEID,DIVID,DEPTID,REGD_BY_TYPE,"
                      + "(SELECT zone_code FROM CONTRACT_zone_MASTER WHERE zone_id=ZONEID) as zone, "
                      + "(SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=gm.DIVID) as div,"
                      + "(SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=gm.DEPTID) as dept,"
                      + "(Select name from CONTRACTORREGISTRATION where to_char(id)=ACTION_BY_ID) as contname,"
                      + "(Select firmname from CONTRACTORREGISTRATION where to_char(id)=ACTION_BY_ID) as contfirmname,"
                      + "(Select name from CONTRACTORREGISTRATION where to_char(id)=FWD_TO_ID) as fwdcontname,"
                      + "(Select firmname from CONTRACTORREGISTRATION where to_char(id)=FWD_TO_ID) as fwdcontfirmname,"
                      + "(SELECT short_desig FROM admin_designation WHERE to_char(id)= (SELECT designation FROM admin_users WHERE userid=action_by_id )) AS admindesig,"
                      + "(SELECT zone_code FROM CONTRACT_zone_MASTER WHERE zone_id=(SELECT zoneid FROM admin_users WHERE userid=action_by_id ) ) AS adminzone, "
                      + "(SELECT divcode FROM CONTRACT_DIV_MASTER WHERE divid=(SELECT divid FROM admin_users WHERE userid=action_by_id)) AS admindiv,"
                      + "(SELECT deptname FROM DEPARTMENT_MASTER WHERE deptid=(SELECT deptid FROM admin_users WHERE userid=action_by_id)) AS admindept, "
                      + "(SELECT PANNO FROM CONTRACTORREGISTRATION WHERE to_char(id)=action_by_id) AS contPan, "
                      + "ACTION_TYPE,STEP,ACTION_BY_ID,ACTION_BY_TYPE from GRIEVANCE_MASTER gm where gid="+pid+" order by step asc";
              
              System.out.println(sql);
              PreparedStatement ps = con.prepareStatement(sql);
              ResultSet rs = ps.executeQuery();
              while(rs.next()) {                  
                    String a[] = { rs.getString("gid"), rs.getString("REGD_BY_ID"), rs.getString("regd_by_type"),rs.getString("description"), rs.getString("REGnTime"), 
                        rs.getString("woid"), rs.getString("wonum"),rs.getString("zoneid"),rs.getString("divid"),rs.getString("deptid"),rs.getString("zone"),rs.getString("div"),
                        rs.getString("dept"),rs.getString("action_type"),rs.getString("action_by_id"),rs.getString("action_by_type"),rs.getString("contname"),
                        rs.getString("contfirmname"),rs.getString("step"),rs.getString("admindesig"),rs.getString("adminzone"),rs.getString("admindiv"),rs.getString("admindept"),
                        rs.getString("wofilenum"),rs.getString("fwd_to_ID"), rs.getString("fwd_to_type"), rs.getString("status"),rs.getString("fwdcontname"),
                        rs.getString("fwdcontfirmname"),(new UtilityDao()).getAdminPostFromId(rs.getString("FWD_TO_ID")),rs.getString("contPan")};
                    al.add(a);                  
              }
          }
          catch(Exception ex) {
			ex.printStackTrace();
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
          return al;
      }
      
      public HashMap<Integer, String> getWoLocation()
      {
    	System.out.println("Utilitydao --- getWoLocation"); 
    	HashMap<Integer,String> map=new HashMap<Integer,String>();  
  		Connection con=null;
  		try
  		{
  			con= DBConnection.getInstance().getDBConnection();
  			String sql="SELECT LOCID,LOCNAME FROM CONTRACT_LOCATION_MASTER";
  			PreparedStatement pst=con.prepareStatement(sql);
  			ResultSet rs = pst.executeQuery();
  			while(rs.next())
  			{
  				map.put(rs.getInt("LOCID"), rs.getString("LOCNAME"));
  			}	
  			
  		}
  		catch(Exception ex)
  		{
  			ex.printStackTrace();
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
  		return map;    	  
      }
      public int checkIrepsId(String irepsid) {
            Connection con=null;		
            int retVal=0;
            try {
                con = DBConnection.getInstance().getDBConnection();
                String sql="Select * from CONTRACTORREGISTRATION where ireps_id='"+irepsid+"' and approved!=2 ";
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if( rs.next() )
                    retVal=1;
            }
            catch(Exception ex) {
                ex.printStackTrace();
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
            return retVal; 		
	}
        public String getAdminPostFromId(String adminid) {
            Connection con=null;
            String retval="";
            try {
                con = DBConnection.getInstance().getDBConnection();
                String sql="select (Select SHORT_DESIG from ADMIN_DESIGNATION where id=(Select designation from ADMIN_USERS where userid='"+adminid+"')) as appliedto,"
                        + "(select zone_code from CONTRACT_ZONE_MASTER where zone_id=(Select zoneid from admin_users where userid='"+adminid+"')) as zone,"
                        + "(select divcode from CONTRACT_DIV_MASTER where divid=(Select divid from admin_users where userid='"+adminid+"')) as div,"
                        + "(select deptname from DEPARTMENT_MASTER where deptid=(Select deptid from admin_users where userid='"+adminid+"')) as dept from dual";
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if( rs.next() ) {
                    retval = rs.getString("appliedto");
                    retval+="/"+rs.getString("zone");
                    if(rs.getString("div")!=null) {
                        retval+="/"+rs.getString("div");
                        if(rs.getString("dept")!=null) {
                            retval+="/"+rs.getString("dept");
                        }
                    }
                }                    
            }
            catch(Exception ex) {
                ex.printStackTrace();
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
            return retval; 		
	      }
        
        public ArrayList getLocForWM(String woid)
        {
      	System.out.println("Utilitydao --- getLocForWM"); 
       //	HashMap<Integer,String> map=new HashMap<Integer,String>();  
      	    ArrayList arl = new ArrayList();
    		Connection con=null;
    		try
    		{
    			con= DBConnection.getInstance().getDBConnection();
    			String sql=" SELECT LOCNAME,LOCID FROM CONTRACT_LOCATION_MASTER WHERE LOCID IN "
						+ "( SELECT LOCID FROM CONTRACT_WORKORDER_LOCATION WHERE WOID=? AND LOCID <> '1' )  "
						+ "UNION SELECT LOCROI AS LOCNAME,LOCID FROM CONTRACT_WORKORDER_LOCATION WHERE WOID=? AND LOCID = '1'";
    			PreparedStatement pst=con.prepareStatement(sql);
    			pst.setString(1, woid);
    			pst.setString(2, woid);
    			ResultSet rs = pst.executeQuery();
    			while(rs.next())
    			{
    				WOLocationDTO wldto=new WOLocationDTO();
    				wldto.setLocid(rs.getInt("LOCID"));
    				wldto.setLocother(rs.getString("LOCNAME"));
    				arl.add(wldto);
    			}
    		/*	while(rs.next())
    			{
    				System.out.println(rs.getString("LOCNAME"));
    				map.put(rs.getInt("LOCID"), rs.getString("LOCNAME"));
    				System.out.println("DAO"+map);
    			}	
    			System.out.println("DAO"+map);  */
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
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
    		return arl;    	  
        }
        public int getMinWageByLoc(int empType,int skillType,String startDt,String woloc)
      	{
      		int minWage=0;
      		Connection con=null;
      		try
      		{
      		  	con= DBConnection.getInstance().getDBConnection();
      		 		 	
      		 	String sql="SELECT MINWAGE,TO_DATE(VALIDFROM,'DD-MON-YYYY') AS VALIDFROM,TO_DATE(VALIDTO,'DD-MM-YYYY') AS VALIDTO "
      					+ "FROM CONTRACT_MINWAGE_MASTER WHERE EMPLOYMENTTYPE=? "
      				+ "AND SKILLTYPE=? AND WOAREA=(SELECT AREA FROM CONTRACT_LOCATION_MASTER WHERE LOCID=?) "
      				+ "AND (SELECT TO_DATE(?,'YYYY-MM-DD') FROM DUAL ) BETWEEN VALIDFROM AND VALIDTO "; 
      			System.out.println(sql);
      			PreparedStatement pst=con.prepareStatement(sql);
      			pst.setInt(1, empType);
      			pst.setInt(2, skillType);
      			pst.setString(3, woloc);
      			pst.setString(4, startDt);
      			ResultSet rs = pst.executeQuery();
      			while(rs.next())
      			{
      				minWage = rs.getInt("MINWAGE");
      			}	
      			
      		}
      		catch(Exception ex)
      		{
      			ex.printStackTrace();
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
      		
      		return minWage;
      	}
        
        public HashMap<Integer, String> getWoState()
    	{
    		System.out.println("UtilityDao --> getWoState");
    		HashMap<Integer,String> map=new HashMap<Integer,String>();
    		Connection con=null;
    		
    		try
    		{
    			con= DBConnection.getInstance().getDBConnection();
    			String sql="SELECT * FROM STATE_MASTER";
    			PreparedStatement pst=con.prepareStatement(sql);
    			ResultSet rs = pst.executeQuery();
    			while(rs.next()) {
    				map.put(rs.getInt("STATEID"), rs.getString("STATENAME"));
    			}	
    			
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
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
    		return map; 
    	}
        
        public HashMap<Integer, String> getWoDistrict(String stateid)
    	{
    		System.out.println("UtilityDao --> getWoDistrict");
    		HashMap<Integer,String> map=new HashMap<Integer,String>();
    		Connection con=null;
    		
    		try
    		{
    			con= DBConnection.getInstance().getDBConnection();
    			String sql="SELECT DISTRICTID,DISTRICTNAME FROM DISTRICT_MASTER WHERE STATEID=?";
    			PreparedStatement pst=con.prepareStatement(sql);
    			pst.setString(1, stateid);
    			ResultSet rs = pst.executeQuery();
    			while(rs.next()) {
    				map.put(rs.getInt("DISTRICTID"), rs.getString("DISTRICTNAME"));
    			}	
    			
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
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
    		return map; 
    	}
        
        public String getState(String locid)
        {
        	Connection con=null;
        	String stateid="";
        	try
    		{
    			con= DBConnection.getInstance().getDBConnection();
    			String sql="SELECT STATEID FROM CONTRACT_LOCATION_MASTER WHERE LOCID=?";
    			PreparedStatement pst=con.prepareStatement(sql);
    			pst.setString(1, locid);
    			ResultSet rs = pst.executeQuery();
    			while(rs.next()) 
    			{
    				stateid=rs.getString("STATEID");
    			}	
    			
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
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
			return stateid;       	
        	
        }
        
        public String getDistrict(String locid)
        {
        	Connection con=null;
        	String distid="";
        	try
    		{
    			con= DBConnection.getInstance().getDBConnection();
    			String sql="SELECT DISTRICTID FROM CONTRACT_LOCATION_MASTER WHERE LOCID=?";
    			PreparedStatement pst=con.prepareStatement(sql);
    			pst.setString(1, locid);
    			ResultSet rs = pst.executeQuery();
    			while(rs.next()) 
    			{
    				distid=rs.getString("DISTRICTID");
    			}	
    			
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
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
			return distid;
        	
        	
        	
        }
        
        public String getLocation(String locid)
        {
        	Connection con=null;
        	String locname="";
        	try
    		{
    			con= DBConnection.getInstance().getDBConnection();
    			String sql="SELECT LOCNAME FROM CONTRACT_LOCATION_MASTER WHERE LOCID=?";
    			PreparedStatement pst=con.prepareStatement(sql);
    			pst.setString(1, locid);
    			ResultSet rs = pst.executeQuery();
    			while(rs.next()) 
    			{
    				locname=rs.getString("LOCNAME");
    			}	
    			
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
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
			return locname;        	
        }
        
        
        public int checkOthloc(String locoth)
      	{
      		int result=0;
      		Connection con=null;
      		try
      		{
      	  		    
      		 	con= DBConnection.getInstance().getDBConnection();
      			String sql="SELECT LOCNAME FROM CONTRACT_LOCATION_MASTER WHERE UPPER(LOCNAME) LIKE ?";
      			System.out.println(sql);
      			PreparedStatement pst=con.prepareStatement(sql);
      			pst.setString(1, locoth.toUpperCase() + "%");      			
      			ResultSet rs = pst.executeQuery();
      			if(rs.next())
      			{
      				result=1;
      			}	
      			
      		}
      		catch(Exception ex)
      		{
      			ex.printStackTrace();
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
      		
      		return result;
      	}
        public int getPendingFdbkCount(AdministratorDTO adto) {
		Connection con=null;
		int count = 0;
                String temp = "";
                if(adto.getDesigId().equals("0"))
                    temp = "FWD_TO_TYPE='cris'";
                else    
                    temp = "FWD_TO_ID='"+adto.getUserId()+"'";
                
		String sql = "Select count(*) as count from grievance_master gm where step=(SELECT MAX(step) from grievance_master WHERE gid=gm.gid) and "+temp;
		System.out.println(sql);
		try {
                    con = DBConnection.getInstance().getDBConnection();
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()) 
                            count = rs.getInt("count");
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return count;
	}
        
        public String getWoMonth(String wo)
        {
        	String womonth="",pendmonth="",wonextmonth="";
        	String startmonth="",startyear="";
        	Connection con=null;
        	
        	
      		try
      		{
      	  		    
      		 	con= DBConnection.getInstance().getDBConnection();
      		  /*	String sql="SELECT to_char(dateofcommencement,'YYYY') AS STARTYEAR,to_char(dateofcommencement,'MM') AS STARTMONTH,WOLASTMONTH "
      					+ "FROM CONTRACT_WORK_ORDER WHERE WOID=?"; */
      		 	
      		 	
      		 	String sql="SELECT to_char(dateofcommencement,'YYYY') AS STARTYEAR,to_char(dateofcommencement,'MM') AS STARTMONTH,"
      		 			+ "(CASE  WHEN WOLASTMONTH IS NULL THEN ''  ELSE to_char(add_months(TO_Date(WOLASTMONTH,'YYYY-MM'),1),'YYYY-MM')  END ) AS WONEXTMONTH,"
      		 			+ "WOLASTMONTH FROM CONTRACT_WORK_ORDER WHERE WOID=?";
      			System.out.println(sql);
      			PreparedStatement pst=con.prepareStatement(sql);
      			pst.setString(1, wo);      			
      			ResultSet rs = pst.executeQuery();
      			if(rs.next())
      			{ 
      				wonextmonth=rs.getString("WONEXTMONTH");
      				womonth=rs.getString("WOLASTMONTH");
      				startmonth=rs.getString("STARTMONTH");
      				startyear=rs.getString("STARTYEAR");
      			}	
      			
      		/*	if(womonth==null)
      			{	
      				womonth="";
      				pendmonth=getPendingMonth(wo,yr,mon,startmonth,startyear); 			
      			}    			
      			else
      			{
      			   String[] parts = wonextmonth.split("-");
            	               
      				 pendmonth=getPendingMonth(wo,yr,mon,parts[1],parts[0]);      				
      			}	*/
      		}
      		catch(Exception ex)
      		{
      			ex.printStackTrace();
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
        	
        	return (womonth+","+wonextmonth+","+startyear+","+startmonth);
        	
        }
        
        
        public String getPendingMonth(String wo,String yr,String mon,String startyr,String startmon)
        {
           String pendmonth="";
           Connection con=null;
           String month="",year="";
           
           try
           {   
        	   
        	   String startmonth="";
        	/*   String[] parts = womonth.split("-");
        	   int nextmonth=Integer.parseInt(parts[1]) + 1;
        	   if(nextmonth<10)
        	   {
        		   startmonth= "0"+ String.valueOf(nextmonth); 
        		   
        	   }
        	   else
        	   {
        		   startmonth=  String.valueOf(nextmonth); 
        		   
        	   }  */
               
        	   String startdt=startyr+"-"+startmon+"-"+"01";
        	   String enddt=yr+"-"+mon+"-"+"01";
        	   
        	 //  System.out.println("startdt: " + startdt);
             //  System.out.println("enddt: " + enddt);
        	   
               
                con= DBConnection.getInstance().getDBConnection();
     			String sql="WITH t AS ( SELECT DATE '"+startdt+"'  start_date,DATE '"+enddt+"' AS end_date FROM dual )"
     					+ " SELECT TO_CHAR(add_months(TRUNC(start_date,'mm'),level - 1),'mm') MONTH, "
     					+ " TO_CHAR(add_months(TRUNC(start_date,'mm'),level - 1),'yyyy') YEAR  FROM t"
     					+ " CONNECT BY TRUNC(end_date,'mm') >= add_months(TRUNC(start_date,'mm'),level - 1)";
     			System.out.println(sql);
     			Statement stmt=con.createStatement();
     		//	PreparedStatement pst=con.prepareStatement(sql);
     		//	pst.setString(1, startdt);
     		//	pst.setString(2, enddt);
     			ResultSet rs = stmt.executeQuery(sql);
     			while(rs.next())
     			{
     				if(pendmonth.equals(""))
     				{		
     			//	System.out.println("YEAR: " + rs.getString("MONTH"));
     			//	System.out.println("MONTH: " + rs.getString("YEAR"));
     				
     				month=rs.getString("MONTH");
     				year=rs.getString("YEAR");
     				
     				String monthyear=year+"-"+month;
     				
     			 /*	sql="SELECT COUNT(*) AS COUNT FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE WOID=? AND "
     						+ "(SELECT LAST_DAY(TO_DATE(?,'YYYY-MM')) FROM DUAL) >= STARTDATE "
     						+ " AND (SELECT TO_DATE(?,'YYYY-MM') FROM DUAL) <= TERMINATIONDATE ";  */
     				
     				sql="SELECT COUNT(*) AS COUNT FROM (SELECT * FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE WOID=? "
     						+ "AND (SELECT LAST_DAY(TO_DATE(?,'YYYY-MM')) FROM DUAL) >= STARTDATE "
     						+ "AND (SELECT TO_DATE(?,'YYYY-MM') FROM DUAL) <= TERMINATIONDATE ) A "
     						+ "WHERE A.WORKMANID NOT IN (SELECT WORKMANID FROM CONTRACT_WORKMAN_WAGES "
     						+ "WHERE WOID=? AND MONTH=? AND YEAR=? AND WORKSTARTDATE= A.STARTDATE)";
     				
     				System.out.println(sql);
     				PreparedStatement pst1=con.prepareStatement(sql);
     				pst1.setString(1, wo);
     				pst1.setString(2, monthyear);
     				pst1.setString(3, monthyear);
     				pst1.setString(4, wo);
     				pst1.setString(5, month);
     				pst1.setString(6, year);
         			ResultSet rs1 = pst1.executeQuery();
         			if(rs1.next())
         			{ 
         			 	
         			 if(rs1.getInt("COUNT")>0)
         			 {	 
         				pendmonth=year+"-"+month;	 
         			  // System.out.println(pendmonth);	
         			 }	
         			}	
     			  }	
         			
     				
     			}	
        	   
           }
           catch(Exception ex)
     		{
     			ex.printStackTrace();
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
           
           
           return pendmonth;       	
        	
        }
        
        public int getWMWageCount(String woid,String month,String year)
        {
        	int result=0;
        	 Connection con=null;
        	 String monYear= month + "-" + year;
        	
        	try
        	{
        		con= DBConnection.getInstance().getDBConnection();
        		
        		String sql="SELECT COUNT(*) AS COUNT "
        					+ "FROM (SELECT * FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE WOID=? "
        					+ "AND (SELECT LAST_DAY(TO_DATE(?,'MM-YY')) FROM DUAL) >= STARTDATE "
        					+ "AND (SELECT TO_DATE(?,'MM-YY') FROM DUAL) <= TERMINATIONDATE ) A,CONTRACT_WORKMAN_DETAILS B "
        					+ "WHERE A.WORKMANID=B.WORKMANID AND A.WORKMANID NOT IN (SELECT WORKMANID FROM CONTRACT_WORKMAN_WAGES "
        					+ " WHERE WOID=? AND MONTH =? AND YEAR=? AND WORKSTARTDATE = A.STARTDATE)"; 
      			System.out.println(sql);
      			PreparedStatement pstmt=con.prepareStatement(sql);
      			pstmt=con.prepareStatement(sql);    		
    			pstmt.setString(1, woid);
    			pstmt.setString(2, monYear);
    			pstmt.setString(3, monYear);
    			pstmt.setString(4, woid);
    			pstmt.setString(5, month);
    			pstmt.setString(6, year);    			
      			ResultSet rs = pstmt.executeQuery();
      			if(rs.next())
      			{
      				result=rs.getInt("COUNT");
      			}	
        		
        		
        	}
        	catch(Exception ex)
      		{
      			ex.printStackTrace();
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
        	
        	return result;
        }
        public int extLoaDate(String uid,String newdate,String adminid) {	
		Connection con=null;	
		int retVal=0;
		try {
			String s[] = {"","VERIFIED","REJECTED"};
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
			String sql="update CONTRACT_WORK_ORDER set END_DATE_EXT_ADMIN='"+adminid+"',END_DATE_EXT_TIME=sysdate,EXTDATEOFCOMPLETION=to_date('"+newdate+"','yyyy-mm-dd') where WOID="+uid;
			PreparedStatement ps = con.prepareStatement(sql);
			retVal = ps.executeUpdate();
			con.commit(); 
                        System.out.println(sql);
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
		return retVal;
	}
        public ArrayList getPublicDashboardWorkerList(String month,String year,String zoneid,String woid) {
		ArrayList al = null;
		Connection con=null;
                String zonePatch="";
                if(!zoneid.equals(""))
                    zonePatch = " and (Select WOZONE from CONTRACT_WORK_ORDER where WOID=a.woid)='"+zoneid+"' ";
		String sql = "Select WORKMANID,(Select NAME from CONTRACT_WORKMAN_DETAILS where WORKMANID=a.WORKMANID) as workername,"
                        + " (Select photono from CONTRACT_WORKMAN_DETAILS where WORKMANID=a.WORKMANID) as workerphotono "
                        + " from CONTRACT_WORKMAN_WAGES a where woid="+woid+" and MONTH ='"+month+"' AND YEAR ='"+year+"' " + zonePatch + " group by workmanid";
		try {
			con = DBConnection.getInstance().getDBConnection();
			al = new ArrayList();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				String a[] = {rs.getString("WORKMANID"),rs.getString("workername"),rs.getString("workerphotono")};
				al.add(a);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return al;		
	}
        public String getLOANumber(int loaid)
        {
        	String loano = "";
            Connection con = null;
            try {
                con = DBConnection.getInstance().getDBConnection();
                String sql = "SELECT WORKORDERNO FROM CONTRACT_WORK_ORDER WHERE WOID=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, loaid);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                   loano=rs.getString("WORKORDERNO");
                }
            }
            catch(Exception ex) {
                    ex.printStackTrace();
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
            return loano;
        	
        }
        
        public String getWMBankacno(int wmid,int woid,String startdt)
        {
        	String bankacno = "";
            Connection con = null;
            System.out.println(wmid);
            System.out.println(startdt);
            System.out.println(woid);
            try {
                con = DBConnection.getInstance().getDBConnection();
                String sql = "SELECT BANKACNO FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE WORKMANID=? AND WOID=? "
                		+ "AND  STARTDATE=?";
                System.out.println(sql);
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, wmid);
                ps.setInt(2, woid);
                ps.setString(3, startdt);                
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                	bankacno=rs.getString("BANKACNO");
                	System.out.println(bankacno);
                	
                }
            }
            catch(Exception ex) {
                    ex.printStackTrace();
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
            return bankacno;
        	
        }
        
        public String getFullMonth(String mon)
        {
        	String month = "";
            
            try {
            	
            	if(mon.equals("01"))
            	{
            	 month="JANUARY";	
            	}	
            	if(mon.equals("02"))
            	{
            	 month="FEBRUARY";	
            	}	if(mon.equals("03"))
            	{
               	 month="MARCH";	
               	}	if(mon.equals("04"))
            	{
               	 month="APRIL";	
               	}	if(mon.equals("05"))
            	{
               	 month="MAY";	
               	}	if(mon.equals("06"))
            	{
               	 month="JUNE";	
               	}	if(mon.equals("07"))
            	{
               	 month="JULY";	
               	}	if(mon.equals("08"))
            	{
               	 month="AUGUST";	
               	}	if(mon.equals("09"))
            	{
               	 month="SEPTEMBER";	
               	}	if(mon.equals("10"))
            	{
               	 month="OCTOBER";	
               	}	if(mon.equals("11"))
            	{
               	 month="NOVEMBER";	
               	}	if(mon.equals("12"))
            	{
               	 month="DECEMBER";	
               	}	
            		
            }
            catch(Exception ex) {
                    ex.printStackTrace();
            }
         
            return month;        	
        }
        public int CheckVisitor() {
		Connection con=null;
		int retVal=0;
                int Val=0;
		try {
                        con = DBConnection.getInstance().getDBConnection();
                        con.setAutoCommit(false);
                        String sql="select id, prev_visitor, curr_visitor from visitor_counter";
      			System.out.println(sql);
                        PreparedStatement pst=con.prepareStatement(sql);
      			ResultSet rs = pst.executeQuery();
      			if(rs.next())
      			{
      				 retVal=rs.getInt("curr_visitor");
      			}
                         
                        Val=retVal+1;
                        int length = String.valueOf(retVal).length();
                        System.out.println(length);
                        sql="update visitor_counter set curr_visitor='"+Val+"',prev_visitor='"+retVal+"'";
                        System.out.println(sql);
                        pst=con.prepareStatement(sql);
                        pst.executeUpdate();
                        con.commit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
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
		return Val; 		
	}
  
}
