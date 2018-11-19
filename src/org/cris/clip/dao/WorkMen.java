package org.cris.clip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.cris.clip.connection.DBConnection;
import org.cris.clip.dto.WageDTO;
import org.cris.clip.dto.WageReportDTO;
import org.cris.clip.dto.WorkMenDTO;

public class WorkMen 
{
	public int addWorkMan(WorkMenDTO wmDTO)
	{
		Connection con=null;
		PreparedStatement pstmt;
		int result=0;
		System.out.println("addWorkMan");
		try
		{
		 	con=DBConnection.getInstance().getDBConnection();
		 	con.setAutoCommit(false);
		 	
		 	String sql="SELECT CONTRACT_WORKMAN_SEQ.NEXTVAL AS WMANID FROM DUAL";
		 	pstmt=con.prepareStatement(sql);
		 	ResultSet rs = pstmt.executeQuery();
		 	rs.next();
		 	int wmanid=rs.getInt("WMANID");
		 	
		 	sql="INSERT INTO CONTRACT_WORKMAN_DETAILS(WORKMANID,NAME,DATEOFBIRTH,SEX,FATHERNAME,"
		 			+ "PERMANENTADDRESS,PERMANENTPINCODE,PANNO,AADHARNO,MOBILENO,EMAIL,EDUQUAL,"
		 			+ "CONTRACTORID,WOID,DESIGNATION,STARTDATE,TERMINATIONDATE,CREATIONDATE,CREATEDBY,ICARDTYPE,ICARDNO) "
		 			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 	pstmt=con.prepareStatement(sql);
		 	pstmt.setInt(1, wmanid);
		 	pstmt.setString(2, wmDTO.getName());		 	
		 	pstmt.setDate(3, java.sql.Date.valueOf(wmDTO.getDob()));
		 	pstmt.setString(4, wmDTO.getSex());
		 	pstmt.setString(5, wmDTO.getFname());		 	
		 	pstmt.setString(6, wmDTO.getPermAddr());
		 	pstmt.setString(7, wmDTO.getPermPin());
		 	pstmt.setString(8, wmDTO.getPan());
		 	pstmt.setString(9, wmDTO.getAdhar());
		 	pstmt.setString(10, wmDTO.getMobno());
		 	pstmt.setString(11, wmDTO.getEmail());
		 	pstmt.setString(12, wmDTO.getEduQual());		 	
		 	pstmt.setInt(13, wmDTO.getContractorID());
		 	pstmt.setInt(14, wmDTO.getWoid());
		 	pstmt.setString(15, wmDTO.getDesg());
		 	pstmt.setDate(16, java.sql.Date.valueOf(wmDTO.getStartDt()));
		 	pstmt.setDate(17, java.sql.Date.valueOf(wmDTO.getTermDt()));
		 	pstmt.setDate(18, java.sql.Date.valueOf(LocalDate.now()));
		 	pstmt.setString(19, (new ContractorDao()).getUser(wmDTO.getContractorID()));
		 	pstmt.setString(20, wmDTO.getIcardType());
		 	pstmt.setString(21, wmDTO.getIcardNo());
		 	pstmt.executeUpdate();
		 	
		 	sql="INSERT INTO CONTRACT_WORKMAN_EMPLOY_REC(WORKMANID,WOID,CONTRACTORID,STARTDATE,TERMINATIONDATE,"
		 			+ "DESIGNATION,EMPLOYMENTTYPE,SKILLTYPE,WAGERATE,PFALLOWED,PFPER,"
		 			+ "PENSIONALLOWED,PENSIONALLOWEDPER,PFTYPE,PFNO,BANKNAME,BANKACNO,BONUSALLOWED,BONUSALLOWEDPER,"
		 			+ "ADDLINCRALLOWED,ADDLINCRPER,LOCID,LOCROI) "
		 			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 	
		 	pstmt=con.prepareStatement(sql);
		 	pstmt.setInt(1, wmanid);
		 	pstmt.setInt(2, wmDTO.getWoid());
		 	pstmt.setInt(3, wmDTO.getContractorID());
		 	pstmt.setDate(4, java.sql.Date.valueOf(wmDTO.getStartDt()));
		 	pstmt.setDate(5, java.sql.Date.valueOf(wmDTO.getTermDt()));
		 	pstmt.setString(6, wmDTO.getDesg());
		 	pstmt.setInt(7, wmDTO.getEmplType());
		 	pstmt.setInt(8, wmDTO.getSkillType());
		 	pstmt.setInt(9, wmDTO.getWageRate());
		 	pstmt.setString(10, wmDTO.getPfallow());
		 	pstmt.setInt(11, wmDTO.getPfPercent());
		 	pstmt.setString(12, wmDTO.getPenallow());
		 	pstmt.setInt(13, wmDTO.getPenPercent());
		 	pstmt.setString(14, wmDTO.getPfType());
		 	pstmt.setString(15, wmDTO.getPfno());
		 	pstmt.setString(16, wmDTO.getBankname());
		 	pstmt.setString(17, wmDTO.getBankno());
		 	pstmt.setString(18, wmDTO.getBonusallow());
		 	pstmt.setInt(19, wmDTO.getBonuspercent());
		 	pstmt.setString(20, wmDTO.getIncrallow());
		 	pstmt.setInt(21, wmDTO.getIncrpercent());
		 	pstmt.setString(22, wmDTO.getLocid());
		 	pstmt.setString(23, wmDTO.getLocoth());
		 	
		 	pstmt.executeUpdate();
		 	
		 	sql="INSERT INTO CONTRACT_WORKMAN_PASSWORD(WORKMANID,PASSWORD) VALUES(?,?)";
		 	pstmt=con.prepareStatement(sql);
		 	pstmt.setInt(1, wmanid);
		 	pstmt.setString(2, String.valueOf(wmanid));
		 	result=pstmt.executeUpdate();	 	
		 	
		 	con.commit();
		 	
		 	pstmt.close();
		 	con.close();
		 	
		 	 if(wmDTO.getMobno()!=null) {		 		
                 SendSms y = new SendSms(wmDTO.getMobno(),"You are registered in Indian Railway Shramik Kalyan Portal by "+wmDTO.getContFirmName()+
                		 " for LOA no "+new UtilityDao().getLOANumber(wmDTO.getWoid())+".Please login to the portal with Userid : "+wmanid+" & Password : "+wmanid);
                 ExecutorService executor1 = Executors.newSingleThreadExecutor();
                 executor1.execute(y);
                 executor1.shutdown();
                 try {
                         if(!executor1.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                                 executor1.shutdownNow();
                         }
                 }
                 catch(InterruptedException e) {
                         executor1.shutdownNow();
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
		
		return result;
	}
	
	public ArrayList getWorkMenList(String contractor)
	{
		ArrayList arl=new ArrayList();
		Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql=" SELECT * FROM CONTRACT_WORKMAN_DETAILS WHERE CONTRACTORID='"+contractor+"'";			
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("WORKMANID"));
				wmDTO.setName(rs.getString("NAME"));
				wmDTO.setDesg(rs.getString("DESIGNATION"));
				wmDTO.setFname(rs.getString("FATHERNAME"));				
				wmDTO.setAdhar(rs.getString("AADHARNO"));
				wmDTO.setPan(rs.getString("PANNO"));
				arl.add(wmDTO);
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
		
		
		return arl;
		
	}
	
	public ArrayList getMenUnallocated(String contractor)
	{
		ArrayList arl=new ArrayList();
		Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql=" SELECT * FROM CONTRACT_WORKMAN_DETAILS WHERE CONTRACTORID='"+contractor+"' AND LOI IS NULL";			
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("ID"));
				wmDTO.setName(rs.getString("NAME"));
				wmDTO.setDesg(rs.getString("DESIGNATION"));
				wmDTO.setFname(rs.getString("FATHERNAME"));
				wmDTO.setBankno(rs.getString("BANKACNO"));
				wmDTO.setAdhar(rs.getString("AADHARNO"));
				wmDTO.setPan(rs.getString("PANNO"));
				arl.add(wmDTO);
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
		
		
		return arl;
		
	}
	
	public void workMenAllocate(String WOrderId,String[] WMenIds)
	{
		Connection con=null;
		
		try
		{	
			con=DBConnection.getInstance().getDBConnection();
		 	con.setAutoCommit(false);
		    for(int i=0;i<WMenIds.length;i++)
		     {
		    	System.out.println(WMenIds[i]);		
			 	
		    	String sql="UPDATE CONTRACT_WORKMAN_DETAILS SET LOI='"+WOrderId+"' WHERE ID='"+WMenIds[i]+"'";
		    	PreparedStatement pstmt=con.prepareStatement(sql);
			 	pstmt.executeUpdate();
			 	pstmt.close();
		    	
		    }
		    
            con.commit();		 	
		 	con.close();
		}
		catch(Exception e) 
		{
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
		
	}
	
	public ArrayList getWMenByWorder(String wo)
	{
		ArrayList arl=new ArrayList();
		Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql="SELECT UNIQUE(A.WORKMANID),A.NAME,A.DESIGNATION,TO_CHAR(B.STARTDATE,'MM/DD/YYYY') AS STARTDATE,"
					+ "TO_CHAR(B.TERMINATIONDATE,'MM/DD/YYYY') AS TERMINATIONDATE  FROM CONTRACT_WORKMAN_DETAILS A,"
					+ "CONTRACT_WORKMAN_EMPLOY_REC B WHERE A.WORKMANID=B.WORKMANID AND A.TERMINATIONDATE > SYSDATE "
					+ "AND A.WOID='"+wo+"' AND A.ACTIVESTATUS='1' AND B.ACTIVE='1' ";			
			System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("WORKMANID"));
				wmDTO.setName(rs.getString("NAME"));
				wmDTO.setDesg(rs.getString("DESIGNATION"));			
				wmDTO.setStartDt(rs.getString("STARTDATE"));
				wmDTO.setTermDt(rs.getString("TERMINATIONDATE"));
				arl.add(wmDTO);
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
		return arl;
	}
	
	public ArrayList getWMAllocChange(String wo,String nwo)
	{
		ArrayList arl=new ArrayList();
		Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
		  /*	String sql="SELECT UNIQUE(A.WORKMANID),A.NAME,A.DESIGNATION,TO_CHAR(B.STARTDATE,'DD-MON-YYYY') AS STARTDATE,"
					+ "TO_CHAR(B.TERMINATIONDATE,'DD-MON-YYYY') AS TERMINATIONDATE,(SELECT MINWAGE FROM CONTRACT_MINWAGE_MASTER "
					+ "WHERE EMPLOYMENTTYPE=B.EMPLOYMENTTYPE AND SKILLTYPE=B.SKILLTYPE AND WOAREA=(SELECT WOAREA FROM "
					+ "CONTRACT_WORK_ORDER WHERE WOID=?) ) AS MINWAGE,B.LASTSALMONTH FROM CONTRACT_WORKMAN_DETAILS A,"
					+ "CONTRACT_WORKMAN_EMPLOY_REC B WHERE A.WORKMANID=B.WORKMANID AND A.TERMINATIONDATE > SYSDATE "
					+ "AND A.WOID=? AND B.ACTIVE='1' ";		*/	
			
			String sql="SELECT UNIQUE(A.WORKMANID),A.NAME,A.DESIGNATION,TO_CHAR(B.STARTDATE,'DD-MON-YYYY') AS STARTDATE,"
					+ "TO_CHAR(B.TERMINATIONDATE,'DD-MON-YYYY') AS TERMINATIONDATE,B.LASTSALMONTH FROM CONTRACT_WORKMAN_DETAILS A,"
					+ "CONTRACT_WORKMAN_EMPLOY_REC B WHERE A.WORKMANID=B.WORKMANID AND TO_DATE(A.TERMINATIONDATE, 'DD-MM-YYYY') >= TO_DATE(SYSDATE,'DD-MM-YYYY') "
					+ "AND A.WOID=? AND B.ACTIVE='1' ";	
			System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setString(1, wo);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("WORKMANID"));
				wmDTO.setName(rs.getString("NAME"));
				wmDTO.setDesg(rs.getString("DESIGNATION"));			
				wmDTO.setStartDt(rs.getString("STARTDATE"));
				wmDTO.setTermDt(rs.getString("TERMINATIONDATE"));
			//	wmDTO.setMinWage(rs.getInt("MINWAGE"));				
				wmDTO.setLastSalMonth(rs.getString("LASTSALMONTH"));
				arl.add(wmDTO);
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
		return arl;
	}
	
	public int addWorkmenWage(ArrayList arl,String wagemonth,String lastmonth,String woid,String firm)
	{
		System.out.println("addWorkmenWage");
		
		Connection con=null;
		PreparedStatement pstmt;
		int result = 0;
		int wagecount=0;;
		HashMap<String,String> map=new HashMap<String,String>();
		ArrayList smsarl=new ArrayList();
		String fullmonth="";
		
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
		 	con.setAutoCommit(false);
		 	
		 	int i=0;
		 	
		 	String months[]=wagemonth.split("-");
		 	
		 	fullmonth=new UtilityDao().getFullMonth(months[1]);
		 	
		 	wagecount=new UtilityDao().getWMWageCount(woid, months[1], months[0]);		 	
		 	System.out.println("wagecount"+wagecount);	 	
		 	
		 	while(i<arl.size())
		 	{	
		 	 WageDTO wgDTO=(WageDTO)arl.get(i++);	
		 	 
		 	
		 	 String sql="INSERT INTO CONTRACT_WORKMAN_WAGES(WORKMANID,CONTRACTORID,WOID,MONTH,YEAR,"
		 			+ "ATTENDANCE,WAGERATE,NETAMOUNT,OTWAGES,BONUS,OTHERALLOWANCE,PFGROSS,PENSION,OTHERDEDUCTION,WORKSTARTDATE,"
		 			+ "BANKDEPOSITDATE,PFDEPOSITDATE,WAGERECOVERY)"
		 			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 	 pstmt=con.prepareStatement(sql);
		 	 pstmt.setInt(1, wgDTO.getWorkmanId());
		 	 pstmt.setInt(2, wgDTO.getContId());
		 	 pstmt.setInt(3, wgDTO.getWOrderId());
		 	 pstmt.setString(4, wgDTO.getMonth());
		 	 pstmt.setString(5, wgDTO.getYear());
		 	 pstmt.setInt(6, wgDTO.getAttendance());		 	
		 	 pstmt.setInt(7, wgDTO.getWageRate());
		 	 pstmt.setInt(8, wgDTO.getNetAmount());
		 	 pstmt.setInt(9, wgDTO.getOtWage());
		 	 pstmt.setInt(10, wgDTO.getBonus());
		 	 pstmt.setInt(11, wgDTO.getOthers());
		 	 pstmt.setInt(12, wgDTO.getPf());
		 	 pstmt.setInt(13, wgDTO.getPension());
		 	 pstmt.setInt(14, wgDTO.getDeduct());
		 	 pstmt.setDate(15, java.sql.Date.valueOf(new UtilityDao().convertDate(wgDTO.getWorkstartdt())));
		 	 
		 	if(wgDTO.getBankDeposiDt()!=null && !wgDTO.getBankDeposiDt().isEmpty())
		 	{
		 		pstmt.setDate(16, java.sql.Date.valueOf(wgDTO.getBankDeposiDt()));		 		
		 	}	
		 	else
		 	{
		 		pstmt.setDate(16, null);	
		 	}	
		 	if(wgDTO.getPfDepositDt()!=null && !wgDTO.getPfDepositDt().isEmpty())
		 	{
		 		 pstmt.setDate(17, java.sql.Date.valueOf(wgDTO.getPfDepositDt()));		 		
		 	}		
		 	else
		 	{
		 		pstmt.setDate(17, null);	
		 	}	 
		 	pstmt.setInt(18, wgDTO.getWagerecovery());
		 	
		 	 pstmt.executeUpdate();
		 	 pstmt.close();
		 	 
		 	 String salmonth=wgDTO.getYear()+"-"+wgDTO.getMonth();
		 	// System.out.println("salmonth"+salmonth);
		 	// System.out.println("getLastSalMonth"+wgDTO.getLastSalMonth());
		 	// if(wgDTO.getLastSalMonth()null||salmonth.compareTo(wgDTO.getLastSalMonth())>0)
		 	 
		 	 
		 	 // Updating Last Salary Month For The Work Period 
		 	 if(wgDTO.getLastSalMonth().equals("null")||salmonth.compareTo(wgDTO.getLastSalMonth())>0)
		 	 {
		 	   //	System.out.println("Test SAL"); 
		 		sql="UPDATE CONTRACT_WORKMAN_EMPLOY_REC SET LASTSALMONTH=? WHERE WORKMANID=? AND WOID=? "
		 		    + "AND CONTRACTORID=? AND STARTDATE=?"; 
		 		 
		 		 pstmt=con.prepareStatement(sql);
		 		 pstmt.setString(1, salmonth);
		 		 pstmt.setInt(2, wgDTO.getWorkmanId());
		 		 pstmt.setInt(3, wgDTO.getWOrderId());
		 		 pstmt.setInt(4, wgDTO.getContId());
		 		 pstmt.setDate(5, java.sql.Date.valueOf(new UtilityDao().convertDate(wgDTO.getWorkstartdt())));
		 	     pstmt.executeUpdate();
			 	 pstmt.close();
		 		 
		 	 }	
		 	 
		 	 
		 	 // Updating Wage Rate For The Work Period
		 	 
		 	sql="UPDATE CONTRACT_WORKMAN_EMPLOY_REC SET WAGERATE=? WHERE WORKMANID=? AND WOID=?  "
		 		    + "AND CONTRACTORID=? AND STARTDATE=?"; 
		 		 
		 		 pstmt=con.prepareStatement(sql);
		 		 pstmt.setInt(1, wgDTO.getWageRate());
		 		 pstmt.setInt(2, wgDTO.getWorkmanId());
		 		 pstmt.setInt(3, wgDTO.getWOrderId());
		 		 pstmt.setInt(4, wgDTO.getContId());
		 		 pstmt.setDate(5, java.sql.Date.valueOf(new UtilityDao().convertDate(wgDTO.getWorkstartdt())));
		 	     pstmt.executeUpdate();
			 	 pstmt.close();
		 	 
		 	 
		 	
			 // Constructing Wage SMS	 
					 	 
		  	 if(!wgDTO.getMobno().equals(""))
		 	 {	 
		 	
		 		String msg="Your Net Wage Rs. "+wgDTO.getNetAmount()+" registered in Indian Railway Shramik Kalyan Portal for LOA no "
		 				+new UtilityDao().getLOANumber(wgDTO.getWOrderId())+" for "+fullmonth+" "+months[0]+" by "+firm+".Please check your bank account no." 
		 				+new UtilityDao().getWMBankacno(wgDTO.getWorkmanId(),wgDTO.getWOrderId(),wgDTO.getWorkstartdt())+" for details." ; 
		 	 	System.out.println(msg);
		 		org.cris.clip.dto.WageSMSDTO smsdto=new org.cris.clip.dto.WageSMSDTO();	 
		   		smsdto.setMobno(wgDTO.getMobno());
		   		smsdto.setMessage(msg);
		   		smsarl.add(smsdto);
		 	 }  
		 	 
		 	}	
		 	
		 	
		 // Update last wage month for LOA 
              
             if(wagecount<=20)
             {	 
			  if("".equals(lastmonth) || (wagemonth.compareTo(lastmonth)>0))
			  {
			  	// System.out.println("WAGE UPDATE");
			     String sql="UPDATE CONTRACT_WORK_ORDER SET WOLASTMONTH=? WHERE WOID=?"; 
			 	 pstmt=con.prepareStatement(sql);
			 	 pstmt.setString(1, wagemonth);
			 	 pstmt.setString(2, woid);
			 	 pstmt.executeUpdate();
			 	 pstmt.close();	 	 
				 
			  }			
            }
		 	con.commit();		 	
			con.close();
		 	
			result=1;   // Update result for successful wage entry
			 
			     if(smsarl.size()!=0)
			     {	 
                 WageSMS y = new WageSMS(smsarl);
                 ExecutorService executor1 = Executors.newSingleThreadExecutor();
                 executor1.execute(y);
                 executor1.shutdown();
                 try {
                         if(!executor1.awaitTermination(3000, TimeUnit.MILLISECONDS)) {
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
		
		return result;
		
	}
	
	public ArrayList getWMenForWage(int woid,String month,String year)
	{
		ArrayList arl=new ArrayList();
		Connection con=null;
		PreparedStatement pstmt;
		ResultSet rs;
		String monYear= month + "-" + year;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
		  /*	String sql="SELECT UNIQUE(A.WORKMANID),TO_CHAR(A.STARTDATE,'DD-MON-YYYY') AS STARTDATE,A.TERMINATIONDATE,A.WAGERATE,A.DESIGNATION,A.BANKACNO,"
					+ "A.WAGERATE,B.NAME,B.FATHERNAME,B.PANNO,B.AADHARNO,A.CONTRACTORID,A.LASTSALMONTH "
					+ "FROM (SELECT * FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE CONTRACTORID='"+conid+"' AND WOID='"+woid+"' "
					+ "AND (SELECT LAST_DAY(TO_DATE( '"+monYear+"','MM-YY')) FROM DUAL) >= STARTDATE "
					+ "AND (SELECT TO_DATE( '"+monYear+"','MM-YY') FROM DUAL) <= TERMINATIONDATE ) A,CONTRACT_WORKMAN_DETAILS B "
					+ "WHERE A.WORKMANID=B.WORKMANID AND A.CONTRACTORID=B.CONTRACTORID "
					+ "AND A.WORKMANID NOT IN (SELECT WORKMANID FROM CONTRACT_WORKMAN_WAGES "
					+ " WHERE CONTRACTORID='"+conid+"' AND WOID='"+woid+"' AND MONTH ='"+month+"' AND YEAR='"+year+"' AND WORKSTARTDATE = A.STARTDATE)";  */
			
		/*	String sql="SELECT UNIQUE(A.WORKMANID),TO_CHAR(A.STARTDATE,'DD-MON-YYYY') AS STARTDATE,A.TERMINATIONDATE,A.WAGERATE,A.DESIGNATION,A.BANKACNO,"
					+ "A.WAGERATE,B.NAME,B.FATHERNAME,B.PANNO,B.AADHARNO,A.CONTRACTORID,A.LASTSALMONTH,B.MOBILENO,"
					+ "(SELECT MINWAGE FROM CONTRACT_MINWAGE_MASTER C WHERE C.EMPLOYMENTTYPE=A.EMPLOYMENTTYPE AND C.SKILLTYPE=A.SKILLTYPE AND WOAREA="
					+ "(SELECT WOAREA FROM CONTRACT_WORK_ORDER WHERE WOID=?) AND (SELECT TO_DATE(?,'MM-YYYY') FROM DUAL )  BETWEEN VALIDFROM AND VALIDTO ) AS MINWAGE "
					+ "FROM (SELECT * FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE WOID=? "
					+ "AND (SELECT LAST_DAY(TO_DATE(?,'MM-YY')) FROM DUAL) >= STARTDATE "
					+ "AND (SELECT TO_DATE(?,'MM-YY') FROM DUAL) <= TERMINATIONDATE ) A,CONTRACT_WORKMAN_DETAILS B "
					+ "WHERE A.WORKMANID=B.WORKMANID AND A.WORKMANID NOT IN (SELECT WORKMANID FROM CONTRACT_WORKMAN_WAGES "
					+ " WHERE WOID=? AND MONTH =? AND YEAR=? AND WORKSTARTDATE = A.STARTDATE)";  */
			
		String sql="SELECT UNIQUE(A.WORKMANID),TO_CHAR(A.STARTDATE,'DD-MON-YYYY') AS STARTDATE,A.TERMINATIONDATE,A.WAGERATE,A.DESIGNATION,A.BANKACNO,"
					+ "A.WAGERATE,B.NAME,B.FATHERNAME,B.PANNO,B.AADHARNO,A.CONTRACTORID,A.LASTSALMONTH,B.MOBILENO,"
					+ "(SELECT MINWAGE FROM CONTRACT_MINWAGE_MASTER C WHERE C.EMPLOYMENTTYPE=A.EMPLOYMENTTYPE AND C.SKILLTYPE=A.SKILLTYPE "
					+ " AND C.WOAREA=(SELECT AREA FROM CONTRACT_LOCATION_MASTER WHERE LOCID=A.LOCID) "
					+ "AND (SELECT TO_DATE(?,'MM-YYYY') FROM DUAL )  BETWEEN VALIDFROM AND VALIDTO ) AS MINWAGE "
					+ "FROM (SELECT * FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE WOID=? "
					+ "AND (SELECT LAST_DAY(TO_DATE(?,'MM-YY')) FROM DUAL) >= STARTDATE "
					+ "AND (SELECT TO_DATE(?,'MM-YY') FROM DUAL) <= TERMINATIONDATE ) A,CONTRACT_WORKMAN_DETAILS B "
					+ "WHERE A.WORKMANID=B.WORKMANID AND A.WORKMANID NOT IN (SELECT WORKMANID FROM CONTRACT_WORKMAN_WAGES "
					+ " WHERE WOID=? AND MONTH =? AND YEAR=? AND WORKSTARTDATE = A.STARTDATE) AND ROWNUM < 21"; 
			
			System.out.println(sql);
			pstmt=con.prepareStatement(sql);
		//	pstmt.setInt(1, woid);
			pstmt.setString(1, monYear);
			pstmt.setInt(2, woid);
			pstmt.setString(3, monYear);
			pstmt.setString(4, monYear);
			pstmt.setInt(5, woid);
			pstmt.setString(6, month);
			pstmt.setString(7, year);
			rs=pstmt.executeQuery();
			
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("WORKMANID"));
				wmDTO.setName(rs.getString("NAME"));
				wmDTO.setDesg(rs.getString("DESIGNATION"));
				wmDTO.setFname(rs.getString("FATHERNAME"));
				wmDTO.setBankno(rs.getString("BANKACNO"));
				wmDTO.setAdhar(rs.getString("AADHARNO"));
				wmDTO.setPan(rs.getString("PANNO"));
				wmDTO.setContractorID(rs.getInt("CONTRACTORID"));
				wmDTO.setWageRate(rs.getInt("WAGERATE"));
				wmDTO.setStartDt(rs.getString("STARTDATE"));
				wmDTO.setLastSalMonth(rs.getString("LASTSALMONTH"));
				wmDTO.setMobno(rs.getString("MOBILENO"));
				wmDTO.setMinWage(rs.getInt("MINWAGE"));
			  //	System.out.println("minwage="+wmDTO.getMinWage());
				arl.add(wmDTO);
			}
			
			rs.close();
			pstmt.close();
			
			
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
		return arl;
		
	}
	
	
	
	public ArrayList getWMWageByMon(int woid,int conid,String month,String year)
	{
		ArrayList arl=new ArrayList();
		Connection con=null;
		PreparedStatement pstmt;
		ResultSet rs;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql="SELECT A.WORKMANID,B.NAME,B.DESIGNATION,TO_CHAR(A.WORKSTARTDATE,'DD-MON-YYYY') AS WORKSTARTDATE,WAGERATE,ATTENDANCE,OTWAGES,BONUS,OTHERALLOWANCE,PFGROSS,PENSION,OTHERDEDUCTION,"
					+ "NETAMOUNT FROM CONTRACT_WORKMAN_WAGES A,CONTRACT_WORKMAN_DETAILS B WHERE A.CONTRACTORID='"+conid+"'"
					+ " AND A.WOID='"+woid+"' AND A.MONTH='"+month+"' AND A.YEAR='"+year+"' AND A.WORKMANID=B.WORKMANID";			
			
			System.out.println(sql);
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next())
			{
				WageReportDTO wgRepDTO=new WageReportDTO();
				wgRepDTO.setWorkmanId(rs.getString("WORKMANID"));
				wgRepDTO.setName(rs.getString("NAME"));
				wgRepDTO.setDesg(rs.getString("DESIGNATION"));
				wgRepDTO.setWorkstartdt(rs.getString("WORKSTARTDATE"));
				wgRepDTO.setWageRate(rs.getInt("WAGERATE"));
				wgRepDTO.setAttendance(rs.getInt("ATTENDANCE"));				
				wgRepDTO.setOtWage(rs.getInt("OTWAGES"));
				wgRepDTO.setBonus(rs.getInt("BONUS"));
				wgRepDTO.setOthers(rs.getInt("OTHERALLOWANCE"));
				wgRepDTO.setPf(rs.getInt("PFGROSS"));
				wgRepDTO.setPension(rs.getInt("PENSION"));
				wgRepDTO.setDeduct(rs.getInt("OTHERDEDUCTION"));
				wgRepDTO.setNetAmount(rs.getInt("NETAMOUNT"));
				arl.add(wgRepDTO);
			}
			
			rs.close();
			pstmt.close();
			
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
		return arl;
	}
	
	public int WMAllocChange(ArrayList arl)
	{
		System.out.println("WORKMEN WMAllocChange");
		int result=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql=null;
		System.out.println(arl.size());
		int i=0;
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
			while(i<arl.size())
			{
			  //	System.out.println(i);
				WorkMenDTO wmDTO=(WorkMenDTO) arl.get(i++);
			  //	System.out.println("test"+wmDTO.getId());
				
				sql="SELECT * FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE WORKMANID=? AND WOID=? AND ACTIVE='1'";		
				
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, wmDTO.getId());
				pstmt.setInt(2, wmDTO.getWoid());
				rs=pstmt.executeQuery();
				rs.next();
				// copy data from OLD Work order record
				wmDTO.setContractorID(rs.getInt("CONTRACTORID"));			
				wmDTO.setDesg(rs.getString("DESIGNATION"));
				wmDTO.setEmplType(rs.getInt("EMPLOYMENTTYPE"));
				wmDTO.setSkillType(rs.getInt("SKILLTYPE"));
			//	wmDTO.setWageRate(rs.getInt("WAGERATE"));
				wmDTO.setPfallow(rs.getString("PFALLOWED"));
				wmDTO.setPfPercent(rs.getInt("PFPER"));
				wmDTO.setPenallow(rs.getString("PENSIONALLOWED"));
				wmDTO.setPenPercent(rs.getInt("PENSIONALLOWEDPER"));
				wmDTO.setBankname(rs.getString("BANKNAME"));
				wmDTO.setBankno(rs.getString("BANKACNO"));
				wmDTO.setPfType(rs.getString("PFTYPE"));
				wmDTO.setPfno(rs.getString("PFNO"));				
				
				rs.close();
				pstmt.close();
				
				// Update Old recornd CHANEG ACTIVE AND TERMINATION DATE,TERMINATION REASON
				
				sql="UPDATE CONTRACT_WORKMAN_EMPLOY_REC SET TERMINATIONDATE=?,TERMINATIONREASON=?,ACTIVE=? "
						+ "WHERE WORKMANID=? AND WOID=? AND ACTIVE='1' ";									
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);
				pstmt.setDate(1, java.sql.Date.valueOf(wmDTO.getOdlTermDt()));
			    pstmt.setString(2, "CHANGE_WORKORDER");
				pstmt.setInt(3, 0);
				pstmt.setInt(4, wmDTO.getId());
				pstmt.setInt(5, wmDTO.getWoid());
			
				pstmt.executeUpdate();				
				pstmt.close();
				
				
				// Update New record
				
				sql="INSERT INTO CONTRACT_WORKMAN_EMPLOY_REC(WORKMANID,WOID,CONTRACTORID,STARTDATE,TERMINATIONDATE,"
			 			+ "DESIGNATION,EMPLOYMENTTYPE,SKILLTYPE,WAGERATE,PFALLOWED,PFPER,"
			 			+ "PENSIONALLOWED,PENSIONALLOWEDPER,PFTYPE,PFNO,BANKNAME,BANKACNO,LOCID,LOCROI) "
			 			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				System.out.println(sql);			 	
			 	pstmt=con.prepareStatement(sql);
			 	pstmt.setInt(1, wmDTO.getId());
			 	pstmt.setString(2, wmDTO.getNewWoid());
			 	pstmt.setInt(3, wmDTO.getContractorID());
			 	pstmt.setDate(4, java.sql.Date.valueOf(wmDTO.getStartDt()));
			 	pstmt.setDate(5, java.sql.Date.valueOf(wmDTO.getTermDt()));
			 	pstmt.setString(6, wmDTO.getDesg());
			 	pstmt.setInt(7, wmDTO.getEmplType());
			 	pstmt.setInt(8, wmDTO.getSkillType());
			 	pstmt.setInt(9, wmDTO.getWageRate());
			 	pstmt.setString(10, wmDTO.getPfallow());
			 	pstmt.setInt(11, wmDTO.getPfPercent());
			 	pstmt.setString(12, wmDTO.getPenallow());
			 	pstmt.setInt(13, wmDTO.getPenPercent());
			 	pstmt.setString(14, wmDTO.getPfType());
			 	pstmt.setString(15, wmDTO.getPfno());
			 	pstmt.setString(16, wmDTO.getBankname());
			 	pstmt.setString(17, wmDTO.getBankno());
			 	pstmt.setString(18, wmDTO.getLocid());
			 	pstmt.setString(19, wmDTO.getLocoth());
			 	pstmt.executeUpdate();
			 	pstmt.close();
			 	
			 	// UPDATE WORKMAN DETAILS SET TERMINATIONDATE
			 	
			 	sql="UPDATE CONTRACT_WORKMAN_DETAILS SET TERMINATIONDATE=?,WOID=?,MODIFIEDBY=?,MODIFICATIONDATE=? WHERE WORKMANID=?";									
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);
				pstmt.setDate(1, java.sql.Date.valueOf(wmDTO.getTermDt()));	
				pstmt.setString(2, wmDTO.getNewWoid());
				pstmt.setString(3, (new ContractorDao()).getUser(wmDTO.getContractorID()));	
				pstmt.setTimestamp(4,getCurrentTimeStamp());
				pstmt.setInt(5, wmDTO.getId());
				result = pstmt.executeUpdate();				
				pstmt.close();
				
			 	con.commit();			
				
			  // System.out.println(wmDTO.getContractorID());
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
		
		
		return result;
	}
	
	
	
	
	public ArrayList getActiveWman(String conid)
	{
		ArrayList arl=new ArrayList();	
		
		Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
		/*	String sql=" SELECT A.WORKMANID,A.NAME,(SELECT WORKORDERNO FROM CONTRACT_WORK_ORDER WHERE WOID=A.WOID) AS WONAME,(SELECT ICARDTYPE FROM CONTRACT_ICARD_TYPE WHERE ICARDID=A.ICARDTYPE) AS ICARDTYPE,"
					+ "ICARDNO,TO_CHAR(DATEOFBIRTH,'DD-MON-YYYY') AS DATEOFBIRTH,SEX,FATHERNAME,MOBILENO,EMAIL,PHOTONO,"
					+ "(SELECT TO_CHAR(EXTDATEOFCOMPLETION,'YYYY-MM-DD') FROM CONTRACT_WORK_ORDER WHERE WOID=A.WOID) AS WOCOMPLETION,WAGERATE,PERMANENTADDRESS,PERMANENTPINCODE,"
					+ "A.DESIGNATION,A.AADHARNO,A.PANNO,B.LASTSALMONTH,TO_CHAR(B.STARTDATE,'DD-MON-YYYY') AS STARTDATE,TO_CHAR(B.TERMINATIONDATE,'DD-MON-YYYY') AS TERMINATIONDATE,"
					+ "(SELECT EMPLOYMENT_TYPE FROM CONTRACT_EMPLOYMENT_TYPE WHERE EMPLOYMENT_TYPE_ID=B.EMPLOYMENTTYPE) AS  EMPLOYMENTTYPE,"
					+ "(SELECT UNIQUE(SKILL_TYPE) FROM CONTRACT_SKILL_TYPE WHERE SKILL_TYPE_ID=B.SKILLTYPE) AS SKILLTYPE,WOAREA "
					+ "FROM CONTRACT_WORKMAN_DETAILS A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE A.WORKMANID=B.WORKMANID AND A.CONTRACTORID=? "
					+ "AND B.TERMINATIONDATE > SYSDATE AND B.ACTIVE='1'";		*/
			
		/*	String sql="SELECT A.WORKMANID,A.NAME,(SELECT WORKORDERNO FROM CONTRACT_WORK_ORDER WHERE WOID=A.WOID) AS WONAME,(SELECT ICARDTYPE FROM CONTRACT_ICARD_TYPE WHERE ICARDID=A.ICARDTYPE) AS ICARDTYPE,"
					+ "ICARDNO,TO_CHAR(DATEOFBIRTH,'DD-MON-YYYY') AS DATEOFBIRTH,SEX,FATHERNAME,MOBILENO,EMAIL,PHOTONO,"
					+ "(SELECT TO_CHAR(EXTDATEOFCOMPLETION,'YYYY-MM-DD') FROM CONTRACT_WORK_ORDER WHERE WOID=A.WOID) AS WOCOMPLETION,WAGERATE,PERMANENTADDRESS,PERMANENTPINCODE,"
					+ "A.DESIGNATION,A.AADHARNO,A.PANNO,B.LASTSALMONTH,TO_CHAR(B.STARTDATE,'DD-MON-YYYY') AS STARTDATE,TO_CHAR(B.TERMINATIONDATE,'DD-MON-YYYY') AS TERMINATIONDATE,"
					+ "(SELECT EMPLOYMENT_TYPE FROM CONTRACT_EMPLOYMENT_TYPE WHERE EMPLOYMENT_TYPE_ID=B.EMPLOYMENTTYPE) AS  EMPLOYMENTTYPE,"
					+ "(SELECT UNIQUE(SKILL_TYPE) FROM CONTRACT_SKILL_TYPE WHERE SKILL_TYPE_ID=B.SKILLTYPE) AS SKILLTYPE,"
					+ "(SELECT LOCNAME FROM CONTRACT_LOCATION_MASTER WHERE LOCID=B.LOCID AND B.LOCID<>'1' UNION SELECT B.LOCROI FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE B.LOCID='1') AS LOCNAME,"
					+ "B.BANKNAME,B.BANKACNO,B.PFTYPE,B.PFNO "
					+ "FROM CONTRACT_WORKMAN_DETAILS A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE A.WORKMANID=B.WORKMANID AND A.CONTRACTORID=? "
					+ "AND B.TERMINATIONDATE > SYSDATE AND B.ACTIVE='1'";  */
			
			String sql="SELECT A.WORKMANID,A.NAME,(SELECT WORKORDERNO FROM CONTRACT_WORK_ORDER WHERE WOID=A.WOID) AS WONAME,(SELECT ICARDTYPE FROM CONTRACT_ICARD_TYPE WHERE ICARDID=A.ICARDTYPE) AS ICARDTYPE,"
					+ "ICARDNO,TO_CHAR(DATEOFBIRTH,'DD-MON-YYYY') AS DATEOFBIRTH,SEX,FATHERNAME,MOBILENO,EMAIL,PHOTONO,"
					+ "(SELECT TO_CHAR(EXTDATEOFCOMPLETION,'YYYY-MM-DD') FROM CONTRACT_WORK_ORDER WHERE WOID=A.WOID) AS WOCOMPLETION,WAGERATE,PERMANENTADDRESS,PERMANENTPINCODE,"
					+ "A.DESIGNATION,A.AADHARNO,A.PANNO,B.LASTSALMONTH,TO_CHAR(B.STARTDATE,'DD-MON-YYYY') AS STARTDATE,TO_CHAR(B.TERMINATIONDATE,'DD-MON-YYYY') AS TERMINATIONDATE,"
					+ "(SELECT EMPLOYMENT_TYPE FROM CONTRACT_EMPLOYMENT_TYPE WHERE EMPLOYMENT_TYPE_ID=B.EMPLOYMENTTYPE) AS  EMPLOYMENTTYPE,"
					+ "(SELECT UNIQUE(SKILL_TYPE) FROM CONTRACT_SKILL_TYPE WHERE SKILL_TYPE_ID=B.SKILLTYPE) AS SKILLTYPE,B.LOCROI AS LOCNAME,"
					+ "B.BANKNAME,B.BANKACNO,B.PFTYPE,B.PFNO,A.EDUQUAL "
					+ "FROM CONTRACT_WORKMAN_DETAILS A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE A.WORKMANID=B.WORKMANID AND A.CONTRACTORID=? "
					+ "AND TO_DATE(B.TERMINATIONDATE, 'DD-MM-YYYY') >= TO_DATE(SYSDATE,'DD-MM-YYYY') AND B.ACTIVE='1'"; 
			
			System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, conid);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("WORKMANID"));
				wmDTO.setName(rs.getString("NAME"));
				wmDTO.setWoName(rs.getString("WONAME"));
				wmDTO.setDesg(rs.getString("DESIGNATION"));				
				wmDTO.setAdhar(rs.getString("AADHARNO"));
				wmDTO.setPan(rs.getString("PANNO"));
				wmDTO.setStartDt(rs.getString("STARTDATE"));
				wmDTO.setTermDt(rs.getString("TERMINATIONDATE"));	
				wmDTO.setWoTermDt(rs.getString("WOCOMPLETION"));
				wmDTO.setLastSalMonth(rs.getString("LASTSALMONTH"));
				wmDTO.setIcardType(rs.getString("ICARDTYPE"));
				wmDTO.setIcardNo(rs.getString("ICARDNO"));
				wmDTO.setDob(rs.getString("DATEOFBIRTH"));
				wmDTO.setSex(rs.getString("SEX"));
				wmDTO.setFname(rs.getString("FATHERNAME"));
				wmDTO.setMobno(rs.getString("MOBILENO"));
				wmDTO.setEmail(rs.getString("EMAIL"));
				wmDTO.setWageRate(rs.getInt("WAGERATE"));
				wmDTO.setPermAddr(rs.getString("PERMANENTADDRESS"));
				wmDTO.setPermPin(rs.getString("PERMANENTPINCODE"));	
				wmDTO.setPhotono(rs.getInt("PHOTONO"));
				wmDTO.setEmpTypeDesc(rs.getString("EMPLOYMENTTYPE"));
				wmDTO.setSkillTypeDesc(rs.getString("SKILLTYPE"));
			//	wmDTO.setWoArea(rs.getString("WOAREA"));
				System.out.println(rs.getString("LOCNAME"));
				wmDTO.setLocoth(rs.getString("LOCNAME"));
				wmDTO.setBankname(rs.getString("BANKNAME"));
				wmDTO.setBankno(rs.getString("BANKACNO"));
				wmDTO.setPfType(rs.getString("PFTYPE"));
				wmDTO.setPfno(rs.getString("PFNO"));
				wmDTO.setEduQual(rs.getString("EDUQUAL"));
				arl.add(wmDTO);
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
		return arl;
	}
	



	
	public int WMPeriodChange(ArrayList arl)
	{
		int result=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql=null;
		System.out.println(arl.size());
		int i=0;
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
			while(i<arl.size())
			{
				System.out.println(i);
				WorkMenDTO wmDTO=(WorkMenDTO) arl.get(i++);
				System.out.println("test"+wmDTO.getId());
				
				 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				 Date startdate=new SimpleDateFormat("dd-MMM-yyyy").parse(wmDTO.getStartDt());
				 String dtStart=sdf.format(startdate);
				 System.out.println(dtStart);
							
				// Update CONTRACT_WORKMAN_EMPLOY_REC TERMINATION DATE
				
				sql="UPDATE CONTRACT_WORKMAN_EMPLOY_REC SET TERMINATIONDATE=? WHERE WORKMANID='"+wmDTO.getId()+"' AND ACTIVE='1' ";									
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);
				pstmt.setDate(1, java.sql.Date.valueOf(wmDTO.getTermDt()));
			  
			
				pstmt.executeUpdate();				
				pstmt.close();
				
                // UPDATE WORKMAN DETAILS SET TERMINATIONDATE
			 	
			 	sql="UPDATE CONTRACT_WORKMAN_DETAILS SET TERMINATIONDATE=?,MODIFIEDBY=?,MODIFICATIONDATE=SYSTIMESTAMP "
						+ "WHERE WORKMANID='"+wmDTO.getId()+"'";									
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);
				pstmt.setDate(1, java.sql.Date.valueOf(wmDTO.getTermDt()));			
				pstmt.setString(2, (new ContractorDao()).getUser(wmDTO.getContractorID()));				
				result = pstmt.executeUpdate();				
				pstmt.close();
				
				
				// Insert Inactive Workman
				
			/*	sql="INSERT INTO CONTRACT_WORKMAN_INACTIVE(WORKMANID,WOID,CONTRACTORID,STARTDATE,TERMINATIONDATE,CREATEBY"
			 		+ " VALUES(?,?,?,?,?,?)";
				
				System.out.println(sql);			 	
			 	pstmt=con.prepareStatement(sql);
			 	pstmt.setInt(1, wmDTO.getId());
			 	pstmt.setString(2, wmDTO.getNewWoid());
			 	pstmt.setInt(3, wmDTO.getContractorID());
			 	pstmt.setDate(4, java.sql.Date.valueOf(dtStart));
			 	pstmt.setDate(5, java.sql.Date.valueOf(wmDTO.getTermDt()));			 	
			 	pstmt.setString(6, (new ContractorDao()).getUser(wmDTO.getContractorID()));			 	
			 	result=pstmt.executeUpdate();
			 	pstmt.close();		*/	
				
			 	con.commit();			
				
			  
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
		return result;
		
	}
	
	public ArrayList getInactiveWman(String conid )
	{
		ArrayList arl =new ArrayList();		
		Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql=" SELECT A.WORKMANID,A.NAME,A.SEX,TO_CHAR(A.DATEOFBIRTH,'DD-MON-YYYY') AS DATEOFBIRTH,(SELECT WORKORDERNO FROM CONTRACT_WORK_ORDER WHERE WOID=A.WOID) AS WONAME,"
					+ "(SELECT ICARDTYPE FROM CONTRACT_ICARD_TYPE WHERE ICARDID=A.ICARDTYPE) AS ICARDTYPE,ICARDNO,"
					+ "(SELECT TO_CHAR(EXTDATEOFCOMPLETION,'YYYY-MM-DD') FROM CONTRACT_WORK_ORDER WHERE WOID=A.WOID) AS WOCOMPLETION,"
					+ "A.DESIGNATION,AADHARNO,PANNO,TO_CHAR(B.STARTDATE,'DD-MON-YYYY') AS STARTDATE,TO_CHAR(B.TERMINATIONDATE,'DD-MON-YYYY') AS TERMINATIONDATE, B.TERMINATIONREASON,B.WAGERATE,"
					+ "(SELECT EMPLOYMENT_TYPE FROM CONTRACT_EMPLOYMENT_TYPE WHERE EMPLOYMENT_TYPE_ID=B.EMPLOYMENTTYPE) AS  EMPLOYMENTTYPE,"
					+ "(SELECT UNIQUE(SKILL_TYPE) FROM CONTRACT_SKILL_TYPE WHERE SKILL_TYPE_ID=B.SKILLTYPE) AS SKILLTYPE "
					+ "FROM CONTRACT_WORKMAN_DETAILS A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE A.WORKMANID=B.WORKMANID AND A.CONTRACTORID=? AND to_date(B.TERMINATIONDATE, 'DD-MM-YYYY')< to_date(sysdate,'DD-MM-YYYY') AND B.ACTIVE='1'";			
			System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, conid);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("WORKMANID"));
				wmDTO.setName(rs.getString("NAME"));
				wmDTO.setSex(rs.getString("SEX"));
				wmDTO.setDob(rs.getString("DATEOFBIRTH"));
				wmDTO.setWoName(rs.getString("WONAME"));
				wmDTO.setDesg(rs.getString("DESIGNATION"));				
				wmDTO.setAdhar(rs.getString("AADHARNO"));
				wmDTO.setPan(rs.getString("PANNO"));
				wmDTO.setStartDt(rs.getString("STARTDATE"));
				wmDTO.setTermDt(rs.getString("TERMINATIONDATE"));
				wmDTO.setWoTermDt(rs.getString("WOCOMPLETION"));
				wmDTO.setIcardType(rs.getString("ICARDTYPE"));
				wmDTO.setIcardNo(rs.getString("ICARDNO"));
				wmDTO.setWageRate(rs.getInt("WAGERATE"));
				wmDTO.setEmpTypeDesc(rs.getString("EMPLOYMENTTYPE"));
				wmDTO.setSkillTypeDesc(rs.getString("SKILLTYPE"));
				if(rs.getString("TERMINATIONREASON")!=null)
				{
					wmDTO.setTermreason(rs.getString("TERMINATIONREASON"));
				}	
				else
				{
					wmDTO.setTermreason("WORK PERIOD EXPIRED");
				}
				arl.add(wmDTO);
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
		return arl;	
	}
	public WorkMenDTO getWorkManByID(String ictype,String icno)
	{
		WorkMenDTO wmDTO=null;
		Connection con=null;		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
		/*	String sql="SELECT WORKMANID,NAME,TO_CHAR(DATEOFBIRTH,'YYYY-MM-DD') AS DATEOFBIRTH,SEX,FATHERNAME,PERMANENTADDRESS,"
					+ "PERMANENTPINCODE,PANNO,AADHARNO,MOBILENO,EMAIL,EDUQUAL,TO_CHAR(TERMINATIONDATE,'YYYY-MM-DD') AS TERMINATIONDATE,CONTRACTORID"
					+ " FROM CONTRACT_WORKMAN_DETAILS WHERE ICARDTYPE='"+ictype+"' AND ICARDNO='"+icno+"'" ;  */
			
			String sql= "SELECT A.WORKMANID,A.NAME,TO_CHAR(DATEOFBIRTH,'YYYY-MM-DD') AS DATEOFBIRTH,SEX,FATHERNAME,PERMANENTADDRESS,"
					+ "PERMANENTPINCODE,PANNO,AADHARNO,MOBILENO,EMAIL,EDUQUAL,TO_CHAR(A.TERMINATIONDATE,'YYYY-MM-DD') AS TERMINATIONDATE,A.CONTRACTORID,"
					+ "B.PFTYPE,B.PFNO,B.BANKNAME,B.BANKACNO FROM CONTRACT_WORKMAN_DETAILS A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE ICARDTYPE=? AND ICARDNO=? "
					+ "AND A.WORKMANID=B.WORKMANID AND B.ACTIVE='1'" ; 
			
			System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, ictype);
			pst.setString(2, icno);
			ResultSet rs = pst.executeQuery();
			while( rs.next())
			{
			wmDTO = new WorkMenDTO();	
			wmDTO.setId(Integer.parseInt(rs.getString("WORKMANID")));
			wmDTO.setName(rs.getString("NAME"));
			wmDTO.setDob(rs.getString("DATEOFBIRTH"));
			wmDTO.setSex(rs.getString("SEX"));
			wmDTO.setFname(rs.getString("FATHERNAME"));
			wmDTO.setPermAddr(rs.getString("PERMANENTADDRESS"));
			wmDTO.setPermPin(rs.getString("PERMANENTPINCODE"));
			wmDTO.setPan(rs.getString("PANNO"));
			wmDTO.setAdhar(rs.getString("AADHARNO"));
			wmDTO.setMobno(rs.getString("MOBILENO"));
			wmDTO.setEmail(rs.getString("EMAIL"));
			wmDTO.setEduQual(rs.getString("EDUQUAL"));
			wmDTO.setTermDt(rs.getString("TERMINATIONDATE"));
			wmDTO.setContractorID(rs.getInt("CONTRACTORID"));
			wmDTO.setPfType(rs.getString("PFTYPE"));
			wmDTO.setPfno(rs.getString("PFNO"));
			wmDTO.setBankname(rs.getString("BANKNAME"));
			wmDTO.setBankno(rs.getString("BANKACNO"));
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
	
	
	public int reemplyWorkMan(WorkMenDTO wmDTO)
	{
		int result=0;
		Connection con=null;
		PreparedStatement pstmt;
		String sql="";
		WorkMenDTO oldwmDTO=null;
		System.out.println("addWorkMan");
		try
		{
		 	con=DBConnection.getInstance().getDBConnection();
		 	con.setAutoCommit(false);
		 	
		 // Get Past Workman
		 	
		 	sql="SELECT WOID,CONTRACTORID,TO_CHAR(STARTDATE,'YYYY-MM-DD') AS STARTDATE,TO_CHAR(TERMINATIONDATE,'YYYY-MM-DD') AS TERMINATIONDATE,"
		 			+ "ICARDTYPE,ICARDNO FROM CONTRACT_WORKMAN_DETAILS WHERE WORKMANID=?";
		 	PreparedStatement pst = con.prepareStatement(sql);
		 	pst.setInt(1, wmDTO.getId());
			ResultSet rs = pst.executeQuery();
			while( rs.next())
			{
				oldwmDTO = new WorkMenDTO();
				oldwmDTO.setWoid(Integer.parseInt(rs.getString("WOID")));
				oldwmDTO.setContractorID(Integer.parseInt(rs.getString("CONTRACTORID")));
				oldwmDTO.setStartDt(rs.getString("STARTDATE"));
				oldwmDTO.setTermDt(rs.getString("TERMINATIONDATE"));
				oldwmDTO.setIcardType(rs.getString("ICARDTYPE"));
				oldwmDTO.setIcardNo(rs.getString("ICARDNO"));				
			}		 	
			
			// Insert Past Workman
			
			sql="INSERT INTO CONTRACT_WORKMAN_PAST(WORKMANID,WOID,CONTRACTORID,STARTDATE,TERMINATIONDATE,ICARDTYPE,"
					+ "ICARDNO) VALUES(?,?,?,?,?,?,?)";
			
			System.out.println(sql);			 	
		 	pstmt=con.prepareStatement(sql);
		 	pstmt.setInt(1, wmDTO.getId());
		 	pstmt.setInt(2, oldwmDTO.getWoid());
		 	pstmt.setInt(3, oldwmDTO.getContractorID());
		 	pstmt.setDate(4, java.sql.Date.valueOf(oldwmDTO.getStartDt()));
		 	pstmt.setDate(5, java.sql.Date.valueOf(oldwmDTO.getTermDt()));			 	
		 	pstmt.setString(6, oldwmDTO.getIcardType());
		 	pstmt.setString(7, oldwmDTO.getIcardNo());
		 	pstmt.executeUpdate();
		 	pstmt.close();		
		 	
		 	// Update WORKMAN DETAILS
		 	
		 	sql="UPDATE CONTRACT_WORKMAN_DETAILS SET NAME=?,DATEOFBIRTH=?,SEX=?,FATHERNAME=?,"
		 			+ "PERMANENTADDRESS=?,PERMANENTPINCODE=?,PANNO=?,AADHARNO=?,MOBILENO=?,EMAIL=?,EDUQUAL=?,"
		 			+ "CONTRACTORID=?,WOID=?,DESIGNATION=?,STARTDATE=?,TERMINATIONDATE=?,MODIFICATIONDATE=?,MODIFIEDBY=?,"
		 			+ "ICARDTYPE=?,ICARDNO=? WHERE WORKMANID=?";
		 			
		 	pstmt=con.prepareStatement(sql);		 	
		 	pstmt.setString(1, wmDTO.getName());		 	
		 	pstmt.setDate(2, java.sql.Date.valueOf(wmDTO.getDob()));
		 	pstmt.setString(3, wmDTO.getSex());
		 	pstmt.setString(4, wmDTO.getFname());		 	
		 	pstmt.setString(5, wmDTO.getPermAddr());
		 	pstmt.setString(6, wmDTO.getPermPin());
		 	pstmt.setString(7, wmDTO.getPan());
		 	pstmt.setString(8, wmDTO.getAdhar());
		 	pstmt.setString(9, wmDTO.getMobno());
		 	pstmt.setString(10, wmDTO.getEmail());
		 	pstmt.setString(11, wmDTO.getEduQual());		 	
		 	pstmt.setInt(12, wmDTO.getContractorID());
		 	pstmt.setInt(13, wmDTO.getWoid());
		 	pstmt.setString(14, wmDTO.getDesg());
		 	pstmt.setDate(15, java.sql.Date.valueOf(wmDTO.getStartDt()));
		 	pstmt.setDate(16, java.sql.Date.valueOf(wmDTO.getTermDt()));
		 	pstmt.setDate(17, java.sql.Date.valueOf(LocalDate.now()));
		 	pstmt.setString(18, (new ContractorDao()).getUser(wmDTO.getContractorID()));
		 	pstmt.setString(19, wmDTO.getIcardType());
		 	pstmt.setString(20, wmDTO.getIcardNo());
		 	pstmt.setInt(21, wmDTO.getId());
		 	pstmt.executeUpdate();
		 	
		 	// UPDATE WORKMAN EMPLOYEMENT RECORD SET ACTIVE=0
		 	
		 	sql="UPDATE CONTRACT_WORKMAN_EMPLOY_REC SET ACTIVE='0' WHERE WORKMANID=? AND ACTIVE='1' ";									
			System.out.println(sql);
			pstmt=con.prepareStatement(sql);	
			pstmt.setInt(1, wmDTO.getId());
			pstmt.executeUpdate();				
			pstmt.close();
		 	
		 	
		 	// Insert new 	WORKMAN EMPLOYEMENT RECORD
		 	
		 	sql="INSERT INTO CONTRACT_WORKMAN_EMPLOY_REC(WORKMANID,WOID,CONTRACTORID,STARTDATE,TERMINATIONDATE,"
		 			+ "DESIGNATION,EMPLOYMENTTYPE,SKILLTYPE,WAGERATE,PFALLOWED,PFPER,"
		 			+ "PENSIONALLOWED,PENSIONALLOWEDPER,PFTYPE,PFNO,BANKNAME,BANKACNO,LOCID,LOCROI) "
		 			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 	
		 	pstmt=con.prepareStatement(sql);
		 	pstmt.setInt(1, wmDTO.getId());
		 	pstmt.setInt(2, wmDTO.getWoid());
		 	pstmt.setInt(3, wmDTO.getContractorID());
		 	pstmt.setDate(4, java.sql.Date.valueOf(wmDTO.getStartDt()));
		 	pstmt.setDate(5, java.sql.Date.valueOf(wmDTO.getTermDt()));
		 	pstmt.setString(6, wmDTO.getDesg());
		 	pstmt.setInt(7, wmDTO.getEmplType());
		 	pstmt.setInt(8, wmDTO.getSkillType());
		 	pstmt.setInt(9, wmDTO.getWageRate());
		 	pstmt.setString(10, wmDTO.getPfallow());
		 	pstmt.setInt(11, wmDTO.getPfPercent());
		 	pstmt.setString(12, wmDTO.getPenallow());
		 	pstmt.setInt(13, wmDTO.getPenPercent());
		 	pstmt.setString(14, wmDTO.getPfType());
		 	pstmt.setString(15, wmDTO.getPfno());
		 	pstmt.setString(16, wmDTO.getBankname());
		 	pstmt.setString(17, wmDTO.getBankno());
		    pstmt.setString(18, wmDTO.getLocid());
		    pstmt.setString(19, wmDTO.getLocoth());
		 	result = pstmt.executeUpdate();
		 	con.commit();
		 	
		 	pstmt.close();
		 	con.close();		 	
			
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
	
	
	public int reassignWorkMan(WorkMenDTO wmDTO)
	{
		System.out.println("reassignWorkMan");
		int result=0;
		Connection con=null;
		PreparedStatement pstmt;
		String sql="";		
		
		try
		{
		 	con=DBConnection.getInstance().getDBConnection();
		 	con.setAutoCommit(false);
		 
		 		 	
           // Update WORKMAN Details STARTDATE NOT CHANGED 	

		 	
		 	sql="UPDATE CONTRACT_WORKMAN_DETAILS SET NAME=?,DATEOFBIRTH=?,SEX=?,FATHERNAME=?,"
		 			+ "PERMANENTADDRESS=?,PERMANENTPINCODE=?,PANNO=?,AADHARNO=?,MOBILENO=?,EMAIL=?,EDUQUAL=?,"
		 			+ "WOID=?,DESIGNATION=?,TERMINATIONDATE=?,MODIFICATIONDATE=?,MODIFIEDBY=? WHERE WORKMANID=?";
		 			
		 	pstmt=con.prepareStatement(sql);		 	
		 	pstmt.setString(1, wmDTO.getName());		 	
		 	pstmt.setDate(2, java.sql.Date.valueOf(wmDTO.getDob()));
		 	pstmt.setString(3, wmDTO.getSex());
		 	pstmt.setString(4, wmDTO.getFname());		 	
		 	pstmt.setString(5, wmDTO.getPermAddr());
		 	pstmt.setString(6, wmDTO.getPermPin());
		 	pstmt.setString(7, wmDTO.getPan());
		 	pstmt.setString(8, wmDTO.getAdhar());
		 	pstmt.setString(9, wmDTO.getMobno());
		 	pstmt.setString(10, wmDTO.getEmail());
		 	pstmt.setString(11, wmDTO.getEduQual());		 	
		 	pstmt.setInt(12, wmDTO.getWoid());
		 	pstmt.setString(13, wmDTO.getDesg());		 	
		 	pstmt.setDate(14, java.sql.Date.valueOf(wmDTO.getTermDt()));
		 	pstmt.setDate(15, java.sql.Date.valueOf(LocalDate.now()));
		 	pstmt.setString(16, (new ContractorDao()).getUser(wmDTO.getContractorID()));
		 	pstmt.setInt(17, wmDTO.getId());
		 	
		 	pstmt.executeUpdate();
		
		 	
		 	// UPDATE WORKMAN EMPLOYEMENT RECORD SET ACTIVE=0
		 	
		 	sql="UPDATE CONTRACT_WORKMAN_EMPLOY_REC SET ACTIVE='0' WHERE WORKMANID=? AND ACTIVE='1' ";									
			System.out.println(sql);
			pstmt=con.prepareStatement(sql);	
			pstmt.setInt(1, wmDTO.getId());
			pstmt.executeUpdate();				
			pstmt.close();
		 	
		 	
		 	// Insert new 	WORKMAN EMPLOYEMENT RECORD
		 	
		 	sql="INSERT INTO CONTRACT_WORKMAN_EMPLOY_REC(WORKMANID,WOID,CONTRACTORID,STARTDATE,TERMINATIONDATE,"
		 			+ "DESIGNATION,EMPLOYMENTTYPE,SKILLTYPE,WAGERATE,PFALLOWED,PFPER,"
		 			+ "PENSIONALLOWED,PENSIONALLOWEDPER,PFTYPE,PFNO,BANKNAME,BANKACNO,LOCID,LOCROI) "
		 			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 	
		 	pstmt=con.prepareStatement(sql);
		 	pstmt.setInt(1, wmDTO.getId());
		 	pstmt.setInt(2, wmDTO.getWoid());
		 	pstmt.setInt(3, wmDTO.getContractorID());
		 	pstmt.setDate(4, java.sql.Date.valueOf(wmDTO.getStartDt()));
		 	pstmt.setDate(5, java.sql.Date.valueOf(wmDTO.getTermDt()));
		 	pstmt.setString(6, wmDTO.getDesg());
		 	pstmt.setInt(7, wmDTO.getEmplType());
		 	pstmt.setInt(8, wmDTO.getSkillType());
		 	pstmt.setInt(9, wmDTO.getWageRate());
		 	pstmt.setString(10, wmDTO.getPfallow());
		 	pstmt.setInt(11, wmDTO.getPfPercent());
		 	pstmt.setString(12, wmDTO.getPenallow());
		 	pstmt.setInt(13, wmDTO.getPenPercent());
		 	pstmt.setString(14, wmDTO.getPfType());
		 	pstmt.setString(15, wmDTO.getPfno());
		 	pstmt.setString(16, wmDTO.getBankname());
		 	pstmt.setString(17, wmDTO.getBankno());
		 	pstmt.setString(18, wmDTO.getLocid());
		    pstmt.setString(19, wmDTO.getLocoth());
		 	
		 	result = pstmt.executeUpdate();
		 	con.commit();
		 	
		 	pstmt.close();
		 	con.close();		 	
			
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
	
	public int WMPeriodTerminate(ArrayList arl)
	{
		int result=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql=null;
	//	System.out.println(arl.size());
		int i=0;
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
			while(i<arl.size())
			{
				System.out.println(i);
				WorkMenDTO wmDTO=(WorkMenDTO) arl.get(i++);
				System.out.println("test"+wmDTO.getId());
				
				 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				 Date startdate=new SimpleDateFormat("dd-MMM-yyyy").parse(wmDTO.getStartDt());
				 String dtStart=sdf.format(startdate);
				 System.out.println(dtStart);
							
				// Update CONTRACT_WORKMAN_EMPLOY_REC TERMINATION DATE
				
				sql="UPDATE CONTRACT_WORKMAN_EMPLOY_REC SET TERMINATIONDATE=?,TERMINATIONREASON=? WHERE WORKMANID=? AND ACTIVE='1' ";									
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);
				pstmt.setDate(1, java.sql.Date.valueOf(wmDTO.getTermDt()));
				pstmt.setString(2, wmDTO.getTermreason());
				pstmt.setInt(3, wmDTO.getId());
			  
			
				pstmt.executeUpdate();				
				pstmt.close();
				
                // UPDATE WORKMAN DETAILS SET TERMINATIONDATE
			 	
			 	sql="UPDATE CONTRACT_WORKMAN_DETAILS SET TERMINATIONDATE=?,MODIFIEDBY=?,MODIFICATIONDATE=? "
						+ "WHERE WORKMANID=?";									
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);
				pstmt.setDate(1, java.sql.Date.valueOf(wmDTO.getTermDt()));			
				pstmt.setString(2, (new ContractorDao()).getUser(wmDTO.getContractorID()));		
				pstmt.setTimestamp(3, getCurrentTimeStamp());
				pstmt.setInt(4, wmDTO.getId());
				result = pstmt.executeUpdate();				
				pstmt.close();
							
			 	con.commit();			
				
			  
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
		return result;
		
	}
	
	public ArrayList getWMInactAlloc(String wo,String conid)
	{
		ArrayList arl=new ArrayList();
		Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
		 /*	String sql="SELECT UNIQUE(A.WORKMANID),A.NAME,A.DESIGNATION,TO_CHAR(B.STARTDATE,'DD-MON-YYYY') AS STARTDATE,"
					+ "TO_CHAR(B.TERMINATIONDATE,'DD-MON-YYYY') AS TERMINATIONDATE,(SELECT MINWAGE FROM CONTRACT_MINWAGE_MASTER "
					+ "WHERE EMPLOYMENTTYPE=B.EMPLOYMENTTYPE AND SKILLTYPE=B.SKILLTYPE AND WOAREA=(SELECT WOAREA FROM "
					+ "CONTRACT_WORK_ORDER WHERE WOID=?) ) AS MINWAGE,B.LASTSALMONTH FROM CONTRACT_WORKMAN_DETAILS A,"
					+ "CONTRACT_WORKMAN_EMPLOY_REC B WHERE A.CONTRACTORID=? AND  A.WORKMANID=B.WORKMANID AND  to_date(A.TERMINATIONDATE, 'DD-MM-YYYY') < to_date(sysdate,'DD-MM-YYYY') "
					+ "AND B.ACTIVE=1";		*/
			
			String sql="SELECT UNIQUE(A.WORKMANID),A.NAME,A.DESIGNATION,TO_CHAR(B.STARTDATE,'DD-MON-YYYY') AS STARTDATE,"
					+ "TO_CHAR(B.TERMINATIONDATE,'DD-MON-YYYY') AS TERMINATIONDATE,B.LASTSALMONTH FROM CONTRACT_WORKMAN_DETAILS A,"
					+ "CONTRACT_WORKMAN_EMPLOY_REC B WHERE A.CONTRACTORID=? AND  A.WORKMANID=B.WORKMANID"
					+ " AND  to_date(A.TERMINATIONDATE, 'DD-MM-YYYY') < to_date(sysdate,'DD-MM-YYYY') "
					+ "AND B.ACTIVE=1";	
			System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, conid);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("WORKMANID"));
				wmDTO.setName(rs.getString("NAME"));
				wmDTO.setDesg(rs.getString("DESIGNATION"));			
				wmDTO.setStartDt(rs.getString("STARTDATE"));
				wmDTO.setTermDt(rs.getString("TERMINATIONDATE"));
			//	wmDTO.setMinWage(rs.getInt("MINWAGE"));				
				wmDTO.setLastSalMonth(rs.getString("LASTSALMONTH"));
				arl.add(wmDTO);
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
		return arl;
	}
	
	public int WMInactAlloc(ArrayList arl)
	{
		System.out.println("WORKMEN allocation");
		int result=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql=null;
	  //	System.out.println(arl.size());
		int i=0;
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
			while(i<arl.size())
			{
				System.out.println(i);
				WorkMenDTO wmDTO=(WorkMenDTO) arl.get(i++);
			//	System.out.println("test"+wmDTO.getId());
				
			
				sql="SELECT * FROM CONTRACT_WORKMAN_EMPLOY_REC WHERE WORKMANID=? AND ACTIVE='1'";	
				
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, wmDTO.getId());
				rs=pstmt.executeQuery();
				rs.next();
				// copy data from OLD Work order record
				wmDTO.setContractorID(rs.getInt("CONTRACTORID"));			
				wmDTO.setDesg(rs.getString("DESIGNATION"));
				wmDTO.setEmplType(rs.getInt("EMPLOYMENTTYPE"));
				wmDTO.setSkillType(rs.getInt("SKILLTYPE"));
			//	wmDTO.setWageRate(rs.getInt("WAGERATE"));
				wmDTO.setPfallow(rs.getString("PFALLOWED"));
				wmDTO.setPfPercent(rs.getInt("PFPER"));
				wmDTO.setPenallow(rs.getString("PENSIONALLOWED"));
				wmDTO.setPenPercent(rs.getInt("PENSIONALLOWEDPER"));
				wmDTO.setBankname(rs.getString("BANKNAME"));
				wmDTO.setBankno(rs.getString("BANKACNO"));
				wmDTO.setPfType(rs.getString("PFTYPE"));
				wmDTO.setPfno(rs.getString("PFNO"));				
				
				rs.close();
				pstmt.close();
				
				// Update Old record CHANEG ACTIVE 
				
				sql="UPDATE CONTRACT_WORKMAN_EMPLOY_REC SET ACTIVE=? WHERE WORKMANID=? AND ACTIVE='1' ";									
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);				
			    pstmt.setInt(1, 0);	
			    pstmt.setInt(2, wmDTO.getId());
			
				pstmt.executeUpdate();				
				pstmt.close();
				
				
				// Insert New record
				
				sql="INSERT INTO CONTRACT_WORKMAN_EMPLOY_REC(WORKMANID,WOID,CONTRACTORID,STARTDATE,TERMINATIONDATE,"
			 			+ "DESIGNATION,EMPLOYMENTTYPE,SKILLTYPE,WAGERATE,PFALLOWED,PFPER,"
			 			+ "PENSIONALLOWED,PENSIONALLOWEDPER,PFTYPE,PFNO,BANKNAME,BANKACNO,LOCID,LOCROI) "
			 			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				System.out.println(sql);			 	
			 	pstmt=con.prepareStatement(sql);
			 	pstmt.setInt(1, wmDTO.getId());
			 	pstmt.setInt(2, wmDTO.getWoid());
			 	pstmt.setInt(3, wmDTO.getContractorID());
			 	pstmt.setDate(4, java.sql.Date.valueOf(wmDTO.getStartDt()));
			 	pstmt.setDate(5, java.sql.Date.valueOf(wmDTO.getTermDt()));
			 	pstmt.setString(6, wmDTO.getDesg());
			 	pstmt.setInt(7, wmDTO.getEmplType());
			 	pstmt.setInt(8, wmDTO.getSkillType());
			 	pstmt.setInt(9, wmDTO.getWageRate());
			 	pstmt.setString(10, wmDTO.getPfallow());
			 	pstmt.setInt(11, wmDTO.getPfPercent());
			 	pstmt.setString(12, wmDTO.getPenallow());
			 	pstmt.setInt(13, wmDTO.getPenPercent());
			 	pstmt.setString(14, wmDTO.getPfType());
			 	pstmt.setString(15, wmDTO.getPfno());
			 	pstmt.setString(16, wmDTO.getBankname());
			 	pstmt.setString(17, wmDTO.getBankno());
			 	pstmt.setString(18, wmDTO.getLocid());
			 	pstmt.setString(19, wmDTO.getLocoth());
			 	pstmt.executeUpdate();
			 	pstmt.close();
			 	
			 	// UPDATE WORKMAN DETAILS SET TERMINATIONDATE, workorder
			 	
			 	sql="UPDATE CONTRACT_WORKMAN_DETAILS SET TERMINATIONDATE=?,MODIFIEDBY=?,MODIFICATIONDATE=?,WOID=? "
						+ "WHERE WORKMANID=?";									
				System.out.println(sql);
				pstmt=con.prepareStatement(sql);
				pstmt.setDate(1, java.sql.Date.valueOf(wmDTO.getTermDt()));	
			    pstmt.setString(2, (new ContractorDao()).getUser(wmDTO.getContractorID()));	
			    pstmt.setTimestamp(3,getCurrentTimeStamp());
			    pstmt.setInt(4, wmDTO.getWoid());
			    pstmt.setInt(5, wmDTO.getId());
			    
				result = pstmt.executeUpdate();				
				pstmt.close();
				
			 	con.commit();			
				
			   System.out.println(wmDTO.getContractorID());
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
		
		
		return result;
	}

	
	
	
	public ArrayList WageCategoryRep(String woid,String conid,String month,String year,String emptype)
	{
		ArrayList arl=new ArrayList();
		Connection con=null;
		PreparedStatement pstmt;
		ResultSet rs;
		String sql="";
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			if(!emptype.equals("0"))
			{			
			 sql="SELECT A.WORKMANID,(SELECT NAME FROM CONTRACT_WORKMAN_DETAILS WHERE CONTRACT_WORKMAN_DETAILS.WORKMANID=A.WORKMANID) AS NAME,B.DESIGNATION,TO_CHAR(A.WORKSTARTDATE,'DD-MON-YYYY') AS WORKSTARTDATE,A.WAGERATE,"
					+ "A.ATTENDANCE,A.OTWAGES,A.BONUS,A.OTHERALLOWANCE,A.PFGROSS,A.PENSION,A.OTHERDEDUCTION,A.WAGERECOVERY,"
					+ "A.NETAMOUNT,TO_CHAR(A.BANKDEPOSITDATE,'DD-MON-YYYY') AS BANKDEPOSITDATE,TO_CHAR(A.PFDEPOSITDATE,'DD-MON-YYYY') AS PFDEPOSITDATE FROM CONTRACT_WORKMAN_WAGES A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE "
					+ " A.WOID=? AND A.MONTH=? AND A.YEAR=? AND B.EMPLOYMENTTYPE=? AND A.WORKMANID=B.WORKMANID AND A.WORKSTARTDATE=B.STARTDATE "
					+ "AND A.WOID = B.WOID";	
			 pstmt=con.prepareStatement(sql);
			 pstmt.setString(1, woid);
			 pstmt.setString(2, month);
			 pstmt.setString(3, year);
			 pstmt.setString(4, emptype);
			 
			}
			else
			{
				sql="SELECT A.WORKMANID,(SELECT NAME FROM CONTRACT_WORKMAN_DETAILS WHERE CONTRACT_WORKMAN_DETAILS.WORKMANID=A.WORKMANID) AS NAME,B.DESIGNATION,TO_CHAR(A.WORKSTARTDATE,'DD-MON-YYYY') AS WORKSTARTDATE,A.WAGERATE,"
						+ "A.ATTENDANCE,A.OTWAGES,A.BONUS,A.OTHERALLOWANCE,A.PFGROSS,A.PENSION,A.OTHERDEDUCTION,A.WAGERECOVERY,"
						+ "A.NETAMOUNT,TO_CHAR(A.BANKDEPOSITDATE,'DD-MON-YYYY') AS BANKDEPOSITDATE,TO_CHAR(A.PFDEPOSITDATE,'DD-MON-YYYY') AS PFDEPOSITDATE FROM CONTRACT_WORKMAN_WAGES A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE "
						+ " A.WOID=? AND A.MONTH=? AND A.YEAR=? AND A.WORKMANID=B.WORKMANID AND A.WORKSTARTDATE=B.STARTDATE AND A.WOID = B.WOID ";	
				 pstmt=con.prepareStatement(sql);
				 pstmt.setString(1, woid);
				 pstmt.setString(2, month);
				 pstmt.setString(3, year);
				
			}	
			System.out.println(sql);
			
			rs=pstmt.executeQuery();
			
			while(rs.next())
			{
				WageReportDTO wgRepDTO=new WageReportDTO();
				wgRepDTO.setWorkmanId(rs.getString("WORKMANID"));
				wgRepDTO.setName(rs.getString("NAME"));
				wgRepDTO.setDesg(rs.getString("DESIGNATION"));
				wgRepDTO.setWorkstartdt(rs.getString("WORKSTARTDATE"));
				wgRepDTO.setWageRate(rs.getInt("WAGERATE"));
				wgRepDTO.setAttendance(rs.getInt("ATTENDANCE"));				
				wgRepDTO.setOtWage(rs.getInt("OTWAGES"));
				wgRepDTO.setBonus(rs.getInt("BONUS"));
				wgRepDTO.setOthers(rs.getInt("OTHERALLOWANCE"));
				wgRepDTO.setPf(rs.getInt("PFGROSS"));
				wgRepDTO.setPension(rs.getInt("PENSION"));
				wgRepDTO.setDeduct(rs.getInt("OTHERDEDUCTION"));
				wgRepDTO.setNetAmount(rs.getInt("NETAMOUNT"));
				wgRepDTO.setBankDeposiDt(rs.getString("BANKDEPOSITDATE"));
				wgRepDTO.setPfDepositDt(rs.getString("PFDEPOSITDATE"));
				wgRepDTO.setWagerecovery(rs.getInt("WAGERECOVERY"));
				arl.add(wgRepDTO);
			}
			
			rs.close();
			pstmt.close();
			
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
		return arl;
	}
	
	public ArrayList getWManMoved(String conid)
	{
		ArrayList arl=new ArrayList();	
		
		Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql="SELECT UNIQUE(A.WORKMANID),B.NAME,B.SEX,TO_CHAR(B.DATEOFBIRTH,'DD-MON-YYYY') AS DATEOFBIRTH,B.PERMANENTADDRESS,B.PERMANENTPINCODE,A.STARTDATE,"
					+ "A.TERMINATIONDATE,(SELECT ICARDTYPE FROM CONTRACT_ICARD_TYPE WHERE ICARDID=A.ICARDTYPE) AS ICARDTYPE,"
					+ "A.ICARDNO,B.MOBILENO,B.EMAIL,(SELECT NAME || '/' || FIRMNAME FROM CONTRACTORREGISTRATION WHERE ID=B.CONTRACTORID) AS CURRENTWORKING FROM CONTRACT_WORKMAN_PAST A,"
					+ "CONTRACT_WORKMAN_DETAILS B WHERE A.CONTRACTORID=? AND A.WORKMANID=B.WORKMANID AND A.CONTRACTORID!=B.CONTRACTORID";			
			System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, conid);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("WORKMANID"));
				wmDTO.setName(rs.getString("NAME"));		
				wmDTO.setStartDt(rs.getString("STARTDATE"));
				wmDTO.setTermDt(rs.getString("TERMINATIONDATE"));			
				wmDTO.setIcardType(rs.getString("ICARDTYPE"));
				wmDTO.setIcardNo(rs.getString("ICARDNO"));
				wmDTO.setDob(rs.getString("DATEOFBIRTH"));
				wmDTO.setSex(rs.getString("SEX"));			
				wmDTO.setMobno(rs.getString("MOBILENO"));
				wmDTO.setEmail(rs.getString("EMAIL"));				
				wmDTO.setPermAddr(rs.getString("PERMANENTADDRESS"));
				wmDTO.setPermPin(rs.getString("PERMANENTPINCODE"));	
				wmDTO.setContFirmName(rs.getString("CURRENTWORKING"));
			
				arl.add(wmDTO);
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
		return arl;
	}
	
	public ArrayList getWMAllbyCon(String conid)
	{
		ArrayList arl=new ArrayList();	
		
		Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql="SELECT UNIQUE(B.WORKMANID),B.NAME,B.SEX,TO_CHAR(B.DATEOFBIRTH,'DD-MON-YYYY') AS DATEOFBIRTH,B.PERMANENTADDRESS,B.PERMANENTPINCODE,B.STARTDATE,"
					+ "B.TERMINATIONDATE,(SELECT ICARDTYPE FROM CONTRACT_ICARD_TYPE WHERE ICARDID=B.ICARDTYPE) AS ICARDTYPE,"
					+ "B.ICARDNO,B.MOBILENO,B.EMAIL FROM CONTRACT_WORKMAN_DETAILS B WHERE B.CONTRACTORID=?" ;			
			System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, conid);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				WorkMenDTO wmDTO=new WorkMenDTO();
				wmDTO.setId(rs.getInt("WORKMANID"));
				wmDTO.setName(rs.getString("NAME"));		
				wmDTO.setStartDt(rs.getString("STARTDATE"));
				wmDTO.setTermDt(rs.getString("TERMINATIONDATE"));			
				wmDTO.setIcardType(rs.getString("ICARDTYPE"));
				wmDTO.setIcardNo(rs.getString("ICARDNO"));
				wmDTO.setDob(rs.getString("DATEOFBIRTH"));
				wmDTO.setSex(rs.getString("SEX"));			
				wmDTO.setMobno(rs.getString("MOBILENO"));
				wmDTO.setEmail(rs.getString("EMAIL"));				
				wmDTO.setPermAddr(rs.getString("PERMANENTADDRESS"));
				wmDTO.setPermPin(rs.getString("PERMANENTPINCODE"));	
			
				arl.add(wmDTO);
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
		return arl;
	}
	
	public ArrayList getEmployeeWage(String wmid,String year,String month)
	{
	  //	System.out.println("wmid"+wmid);
		
		ArrayList arl=new ArrayList();
		Connection con=null;
		PreparedStatement pstmt;
		ResultSet rs;
		String sql="";
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			
			sql="SELECT A.WORKMANID,(SELECT NAME FROM CONTRACT_WORKMAN_DETAILS WHERE CONTRACT_WORKMAN_DETAILS.WORKMANID=A.WORKMANID) AS NAME,B.DESIGNATION,TO_CHAR(A.WORKSTARTDATE,'DD-MON-YYYY') AS WORKSTARTDATE,A.WAGERATE,"
					+ "A.ATTENDANCE,A.OTWAGES,A.BONUS,A.OTHERALLOWANCE,A.PFGROSS,A.PENSION,A.OTHERDEDUCTION,A.WAGERECOVERY,(select name from contractorregistration where id=a.contractorid) as contname,(select workorderno from contract_work_order where woid=a.woid) as workorderno,"
					+ "A.NETAMOUNT,TO_CHAR(A.BANKDEPOSITDATE,'DD-MON-YYYY') AS BANKDEPOSITDATE,TO_CHAR(A.PFDEPOSITDATE,'DD-MON-YYYY') AS PFDEPOSITDATE FROM CONTRACT_WORKMAN_WAGES A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE "
					+ "A.WORKMANID=? AND A.MONTH=? AND A.YEAR=? AND A.WORKMANID=B.WORKMANID AND A.WORKSTARTDATE=B.STARTDATE AND A.WOID = B.WOID AND A.CONTRACTORID=B.CONTRACTORID";				
			System.out.println(sql);
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, wmid);
			pstmt.setString(2, month);
			pstmt.setString(3, year);
			rs=pstmt.executeQuery();
			
			while(rs.next())
			{
				WageReportDTO wgRepDTO=new WageReportDTO();
				wgRepDTO.setWorkmanId(rs.getString("WORKMANID"));
				wgRepDTO.setName(rs.getString("NAME"));
				wgRepDTO.setDesg(rs.getString("DESIGNATION"));
				wgRepDTO.setWorkstartdt(rs.getString("WORKSTARTDATE"));
				wgRepDTO.setWageRate(rs.getInt("WAGERATE"));
				wgRepDTO.setAttendance(rs.getInt("ATTENDANCE"));				
				wgRepDTO.setOtWage(rs.getInt("OTWAGES"));
				wgRepDTO.setBonus(rs.getInt("BONUS"));
				wgRepDTO.setOthers(rs.getInt("OTHERALLOWANCE"));
				wgRepDTO.setPf(rs.getInt("PFGROSS"));
				wgRepDTO.setPension(rs.getInt("PENSION"));
				wgRepDTO.setDeduct(rs.getInt("OTHERDEDUCTION"));
				wgRepDTO.setWagerecovery(rs.getInt("WAGERECOVERY"));
				wgRepDTO.setNetAmount(rs.getInt("NETAMOUNT"));
				wgRepDTO.setBankDeposiDt(rs.getString("BANKDEPOSITDATE"));
				wgRepDTO.setPfDepositDt(rs.getString("PFDEPOSITDATE"));
				wgRepDTO.setContName(rs.getString("contname"));
				wgRepDTO.setWoName(rs.getString("workorderno"));
				arl.add(wgRepDTO);
			}
			
			rs.close();
			pstmt.close();
			
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
		
		return arl;
		
	}
	
	public int resetEmpPassword(int id,String newpass) {
		Connection con=null;		
		int retVal=0;
		String sql = "Update contract_workman_password set password=? where workmanid=?";  
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, newpass);
			ps.setInt(2, id);
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
	
	public static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}
   
	public int EditProfileByEmp(WorkMenDTO wmdto) 
	{
		int result=0;
		Connection con=null;
		// System.out.println("ID "+wmdto.getId());
		
		try
		{
		con=DBConnection.getInstance().getDBConnection();
		con.setAutoCommit(false);
		String sql="UPDATE CONTRACT_WORKMAN_DETAILS SET PERMANENTADDRESS=?,PERMANENTPINCODE=?,MOBILENO=?,"
				+ "EMAIL=?,EDUQUAL=?,MODIFIEDBY=?,MODIFICATIONDATE=? WHERE WORKMANID=?";
	 		    
	 		 
	 	PreparedStatement pstmt=con.prepareStatement(sql);
	 //	pstmt.setString(1, wmdto.getFname());
	 	pstmt.setString(1, wmdto.getPermAddr());
	 	pstmt.setString(2, wmdto.getPermPin());
	 	pstmt.setString(3, wmdto.getMobno());
	 	pstmt.setString(4, wmdto.getEmail());
	 	pstmt.setString(5, wmdto.getEduQual());
	 	pstmt.setInt(6, wmdto.getId());
	 	pstmt.setTimestamp(7, getCurrentTimeStamp());
	 	pstmt.setInt(8, wmdto.getId());
	 	
	 	result=pstmt.executeUpdate();
		pstmt.close();
		
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
		return result;	

	}
        public ArrayList getWMenWO(String id) {
            ArrayList al = new ArrayList();
            Connection con = null;
            try {
                con = DBConnection.getInstance().getDBConnection();
                String sql = " Select distinct(woid),(select WORKORDERNO from contract_work_order where woid=wmerec.woid) as wono from CONTRACT_WORKMAN_EMPLOY_REC wmerec where workmanid="+id;
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    String a[] = {String.valueOf(rs.getInt("woid")), rs.getString("wono")};
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
        
        public ArrayList WMWageByRange(String woid,String conid,String stmonth,String endmonth,String year,String emptype)
    	{
    		ArrayList arl=new ArrayList();
    		Connection con=null;
    		PreparedStatement pstmt;
    		ResultSet rs;
    		String sql="";
    		System.out.println(woid+"-"+stmonth+"-"+endmonth+"-"+year+"-"+emptype);
    		
    		try
    		{
    			con=DBConnection.getInstance().getDBConnection();
    			if(!emptype.equals("0"))
    			{			
    			 sql="SELECT A.WORKMANID,(A.YEAR||'-'||( SELECT TO_CHAR(TO_DATE(A.MONTH, 'MM'), 'MONTH') FROM DUAL)) AS WAGEMONTH,"
    			 		+ "(SELECT NAME FROM CONTRACT_WORKMAN_DETAILS WHERE CONTRACT_WORKMAN_DETAILS.WORKMANID=A.WORKMANID) AS NAME,B.DESIGNATION,TO_CHAR(A.WORKSTARTDATE,'DD-MON-YYYY') AS WORKSTARTDATE,A.WAGERATE,"
    					+ "A.ATTENDANCE,A.OTWAGES,A.BONUS,A.OTHERALLOWANCE,A.PFGROSS,A.PENSION,A.OTHERDEDUCTION,A.WAGERECOVERY,"
    					+ "A.NETAMOUNT,TO_CHAR(A.BANKDEPOSITDATE,'DD-MON-YYYY') AS BANKDEPOSITDATE,TO_CHAR(A.PFDEPOSITDATE,'DD-MON-YYYY') AS PFDEPOSITDATE FROM CONTRACT_WORKMAN_WAGES A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE "
    					+ " A.WOID=? AND A.MONTH BETWEEN ? AND ? AND A.YEAR=? AND B.EMPLOYMENTTYPE=? AND A.WORKMANID=B.WORKMANID AND A.WORKSTARTDATE=B.STARTDATE "
    					+ "AND A.WOID = B.WOID ORDER BY A.MONTH";	
    			
    			 pstmt=con.prepareStatement(sql);
    			 pstmt.setString(1, woid);
    			 pstmt.setString(2, stmonth);
				 pstmt.setString(3, endmonth);
    			 pstmt.setString(4, year);
    			 pstmt.setString(5, emptype);
    			 
    			}
    			else
    			{
    				sql="SELECT A.WORKMANID,(A.YEAR||'-'||( SELECT TO_CHAR(TO_DATE(A.MONTH, 'MM'), 'MONTH') FROM DUAL)) AS WAGEMONTH,"
    						+ "(SELECT NAME FROM CONTRACT_WORKMAN_DETAILS WHERE CONTRACT_WORKMAN_DETAILS.WORKMANID=A.WORKMANID) AS NAME,B.DESIGNATION,TO_CHAR(A.WORKSTARTDATE,'DD-MON-YYYY') AS WORKSTARTDATE,A.WAGERATE,"
    						+ "A.ATTENDANCE,A.OTWAGES,A.BONUS,A.OTHERALLOWANCE,A.PFGROSS,A.PENSION,A.OTHERDEDUCTION,A.WAGERECOVERY,"
    						+ "A.NETAMOUNT,TO_CHAR(A.BANKDEPOSITDATE,'DD-MON-YYYY') AS BANKDEPOSITDATE,TO_CHAR(A.PFDEPOSITDATE,'DD-MON-YYYY') AS PFDEPOSITDATE FROM CONTRACT_WORKMAN_WAGES A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE "
    						+ " A.WOID=? AND A.MONTH BETWEEN ? AND ? AND A.YEAR=? AND A.WORKMANID=B.WORKMANID AND A.WORKSTARTDATE=B.STARTDATE AND A.WOID = B.WOID ORDER BY A.MONTH ";	
    				
    				pstmt=con.prepareStatement(sql);
    				 pstmt.setString(1, woid);
    				 pstmt.setString(2, stmonth);
    				 pstmt.setString(3, endmonth);
    				 pstmt.setString(4, year);
    				
    			}	
    			System.out.println(sql);
    			
    			rs=pstmt.executeQuery();
    			
    			while(rs.next())
    			{
    				WageReportDTO wgRepDTO=new WageReportDTO();
    				wgRepDTO.setWorkmanId(rs.getString("WORKMANID"));
    				wgRepDTO.setName(rs.getString("NAME"));
    				wgRepDTO.setDesg(rs.getString("DESIGNATION"));
    				wgRepDTO.setWorkstartdt(rs.getString("WORKSTARTDATE"));
    				wgRepDTO.setWageRate(rs.getInt("WAGERATE"));
    				wgRepDTO.setAttendance(rs.getInt("ATTENDANCE"));				
    				wgRepDTO.setOtWage(rs.getInt("OTWAGES"));
    				wgRepDTO.setBonus(rs.getInt("BONUS"));
    				wgRepDTO.setOthers(rs.getInt("OTHERALLOWANCE"));
    				wgRepDTO.setPf(rs.getInt("PFGROSS"));
    				wgRepDTO.setPension(rs.getInt("PENSION"));
    				wgRepDTO.setDeduct(rs.getInt("OTHERDEDUCTION"));
    				wgRepDTO.setNetAmount(rs.getInt("NETAMOUNT"));
    				wgRepDTO.setBankDeposiDt(rs.getString("BANKDEPOSITDATE"));
    				wgRepDTO.setPfDepositDt(rs.getString("PFDEPOSITDATE"));
    				wgRepDTO.setWagerecovery(rs.getInt("WAGERECOVERY"));
    				wgRepDTO.setMonth(rs.getString("WAGEMONTH"));
    				arl.add(wgRepDTO);
    			}
    			
    			rs.close();
    			pstmt.close();
    			
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
    		return arl;
    	}
        public ArrayList getEmpWageRange(String wmid,String year,String stmonth,String endmonth)
    	{
    	  	System.out.println("wmid"+wmid);
    		
    		ArrayList arl=new ArrayList();
    		Connection con=null;
    		PreparedStatement pstmt;
    		ResultSet rs;
    		String sql="";
    		
    		try
    		{
    			con=DBConnection.getInstance().getDBConnection();
    			
    			sql="SELECT A.WORKMANID,(SELECT NAME FROM CONTRACT_WORKMAN_DETAILS WHERE CONTRACT_WORKMAN_DETAILS.WORKMANID=A.WORKMANID) AS NAME,B.DESIGNATION,TO_CHAR(A.WORKSTARTDATE,'DD-MON-YYYY') AS WORKSTARTDATE,A.WAGERATE,"
    					+ "(A.YEAR||'-'||( SELECT TO_CHAR(TO_DATE(A.MONTH, 'MM'), 'MONTH') FROM DUAL)) AS WAGEMONTH,A.ATTENDANCE,A.OTWAGES,A.BONUS,A.OTHERALLOWANCE,A.PFGROSS,A.PENSION,A.OTHERDEDUCTION,A.WAGERECOVERY,"
    					+ "(select name from contractorregistration where id=a.contractorid) as contname,(select FIRMNAME from contractorregistration where id=a.contractorid) as firmname,(select workorderno from contract_work_order where woid=a.woid) as workorderno,"
    					+ "A.NETAMOUNT,TO_CHAR(A.BANKDEPOSITDATE,'DD-MON-YYYY') AS BANKDEPOSITDATE,TO_CHAR(A.PFDEPOSITDATE,'DD-MON-YYYY') AS PFDEPOSITDATE FROM CONTRACT_WORKMAN_WAGES A,CONTRACT_WORKMAN_EMPLOY_REC B WHERE "
    					+ "A.WORKMANID=? AND A.MONTH BETWEEN ? AND ? AND A.YEAR=? AND A.WORKMANID=B.WORKMANID AND A.WORKSTARTDATE=B.STARTDATE AND A.WOID = B.WOID AND A.CONTRACTORID=B.CONTRACTORID ORDER BY A.MONTH";				
    			System.out.println(sql);
    			pstmt=con.prepareStatement(sql);
    			pstmt.setString(1, wmid);
    			pstmt.setString(2, stmonth);
    			pstmt.setString(3, endmonth);
    			pstmt.setString(4, year);
    			rs=pstmt.executeQuery();
    			
    			while(rs.next())
    			{
    				WageReportDTO wgRepDTO=new WageReportDTO();
    				wgRepDTO.setWorkmanId(rs.getString("WORKMANID"));
    				wgRepDTO.setName(rs.getString("NAME"));
    				wgRepDTO.setDesg(rs.getString("DESIGNATION"));
    				wgRepDTO.setWorkstartdt(rs.getString("WORKSTARTDATE"));
    				wgRepDTO.setWageRate(rs.getInt("WAGERATE"));
    				wgRepDTO.setAttendance(rs.getInt("ATTENDANCE"));				
    				wgRepDTO.setOtWage(rs.getInt("OTWAGES"));
    				wgRepDTO.setBonus(rs.getInt("BONUS"));
    				wgRepDTO.setOthers(rs.getInt("OTHERALLOWANCE"));
    				wgRepDTO.setPf(rs.getInt("PFGROSS"));
    				wgRepDTO.setPension(rs.getInt("PENSION"));
    				wgRepDTO.setDeduct(rs.getInt("OTHERDEDUCTION"));
    				wgRepDTO.setWagerecovery(rs.getInt("WAGERECOVERY"));
    				wgRepDTO.setNetAmount(rs.getInt("NETAMOUNT"));
    				wgRepDTO.setBankDeposiDt(rs.getString("BANKDEPOSITDATE"));
    				wgRepDTO.setPfDepositDt(rs.getString("PFDEPOSITDATE"));
    				wgRepDTO.setContName(rs.getString("contname"));
    				wgRepDTO.setWoName(rs.getString("workorderno"));
    				wgRepDTO.setFirmName(rs.getString("firmname"));
    			    wgRepDTO.setMonth(rs.getString("WAGEMONTH"));
    				arl.add(wgRepDTO);
    			}
    			
    			rs.close();
    			pstmt.close();
    			
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
    		
    		return arl;
    		
    	}


}
