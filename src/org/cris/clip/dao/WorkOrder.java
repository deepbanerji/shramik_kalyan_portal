package org.cris.clip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.cris.clip.connection.DBConnection;
import org.cris.clip.dto.WOLocationDTO;
import org.cris.clip.dto.WorkOrderDTO;


public class WorkOrder {
	
	public int getWorkOrdId() throws Exception {
		
		int WorkOrdId=0;
		Connection con=null;
		PreparedStatement pst; 
		ResultSet rs;
			
		try {
			con=DBConnection.getInstance().getDBConnection();
			pst=con.prepareStatement("select CONTRACT_WORK_ORDER_SEQ.nextval NEXTVAL FROM DUAL");
			rs=pst.executeQuery();
			if(rs.next()){
				WorkOrdId = rs.getInt("NEXTVAL");
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e;
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
		if(WorkOrdId==0){
			throw new Exception("Cannot generate Work Order ID.");
		}else
			return WorkOrdId;
	}
	
	public ArrayList<WorkOrderDTO> listWO(int contId) {
		ArrayList<WorkOrderDTO> al = new ArrayList<WorkOrderDTO>();
		Connection con=null;
		try {
			 con=DBConnection.getInstance().getDBConnection();
			
			String sql="select WOID,WORKORDERNO,NAMEOFTHEWORK,NAMEOFTHEPRINCIPALEMPLOYER,LOCATIONOFWORK,PRINCIPALEMPLOYERADDRESS,DATEOFCOMMENCEMENT,DATEOFCOMPLETIONWORK,EXTDATEOFCOMPLETION,"
					+ "( CASE APPROVED WHEN '1' THEN 'VERIFIED' WHEN '2' THEN 'REJECTED' ELSE 'UNVERIFIED' END ) AS APPROVED,"
					+ "APPROVALDATE,(SELECT ZONE_CODE FROM CONTRACT_ZONE_MASTER WHERE ZONE_ID=WOZONE) AS WOZONE,(SELECT DIVCODE FROM CONTRACT_DIV_MASTER WHERE DIVID=WODIV) AS WODIV,"
					+ "(SELECT DEPTNAME FROM DEPARTMENT_MASTER WHERE DEPTID=WODEPT) AS WODEPT,WORKORDERISSUEDBY,WO_FILENO,CREATIONDATE,LOAVALUE,MAXNOOFEMPLOYEES,LABOURLICENCE from CONTRACT_WORK_ORDER WHERE CONTRACTORID=? ORDER BY CREATIONDATE DESC";	
			System.out.println(sql);	
			PreparedStatement pst=con.prepareStatement(sql);
			pst.setInt(1, contId);
			ResultSet rs = pst.executeQuery();			
			if(rs!=null) {
				
				while(rs.next()) {
					WorkOrderDTO wodto = new WorkOrderDTO();
					wodto.setId(rs.getInt("WOID"));
					wodto.setWO(rs.getString("WORKORDERNO"));
					wodto.setWorkName(rs.getString("NAMEOFTHEWORK"));
					wodto.setPrincEmp(rs.getString("NAMEOFTHEPRINCIPALEMPLOYER"));
					wodto.setPincEmpAddr(rs.getString("PRINCIPALEMPLOYERADDRESS"));
					wodto.setIssueBy(rs.getString("WORKORDERISSUEDBY"));
					wodto.setWorkLoc(rs.getString("LOCATIONOFWORK"));					
					wodto.setCommDate(rs.getDate("DATEOFCOMMENCEMENT"));;
					wodto.setEndDt(rs.getDate("DATEOFCOMPLETIONWORK"));
					wodto.setExtEndDt(rs.getDate("EXTDATEOFCOMPLETION"));
					wodto.setApprovalStatus(rs.getString("APPROVED"));
				/*	if(rs.getString("APPROVED").equals("1"))
					{
						wodto.setApprovalStatus("VERIFIED");
					}	
					else
					{
						wodto.setApprovalStatus("UNVERIFIED");
					}	*/				
					wodto.setAppoveDt(rs.getString("APPROVALDATE"));
					wodto.setWoZone(rs.getString("WOZONE"));
					wodto.setWoDiv(rs.getString("WODIV"));
					wodto.setWoDept(rs.getString("WODEPT"));
				//	wodto.setWoArea(rs.getString("WOAREA"));
					wodto.setWoFileNo(rs.getInt("WO_FILENO"));  
					wodto.setLoavalue(rs.getLong("LOAVALUE"));
					wodto.setMaxworkman(rs.getInt("MAXNOOFEMPLOYEES"));
					wodto.setLabourLicense(rs.getString("LABOURLICENCE"));
					
					ArrayList<WOLocationDTO> locarl = new ArrayList<WOLocationDTO>();
				/*	sql=" SELECT LOCNAME FROM CONTRACT_LOCATION_MASTER WHERE LOCID IN "
						+ "( SELECT LOCID FROM CONTRACT_WORKORDER_LOCATION WHERE WOID=? AND LOCID <> '1' )  "
						+ "UNION SELECT LOCROI AS LOCNAME FROM CONTRACT_WORKORDER_LOCATION WHERE WOID=? AND LOCID = '1'"; 
					String sql1=" SELECT ( SELECT LOCNAME FROM contract_location_master WHERE LOCID IN (SELECT LOCID FROM CONTRACT_WORKORDER_LOCATION WHERE WOID=A.WOID AND LOCID <> '1' AND LOCID=A.LOCID ) "
					+ "UNION (SELECT LOCROI FROM CONTRACT_WORKORDER_LOCATION WHERE WOID=A.WOID AND A.LOCID = '1' AND LOCID=A.LOCID ) ) AS LOCNAME,"
					+ "( ( SELECT STATENAME FROM STATE_MASTER WHERE STATEID IN (SELECT STATEID FROM CONTRACT_WORKORDER_LOCATION WHERE WOID=A.WOID AND A.LOCID<>'1' AND LOCID=A.LOCID AND LOCID=A.LOCID ) )"
					+ "UNION (SELECT STATENAME FROM STATE_MASTER WHERE STATEID IN (SELECT STATEID FROM CONTRACT_WORKORDER_LOCATION WHERE WOID=A.WOID AND A.LOCID= '1'  AND LOCID=A.LOCID ) ) ) AS STATENAME "
					+ "FROM CONTRACT_WORKORDER_LOCATION A WHERE A.WOID=? "; */
					
					String sql1="SELECT LOCROI,(SELECT STATENAME FROM STATE_MASTER WHERE STATEID=A.STATEID ) AS STATENAME,"
							+ "(SELECT DISTRICTNAME FROM DISTRICT_MASTER WHERE DISTRICTID=A.DISTRICTID ) AS DISTRICTNAME "
							+ "FROM CONTRACT_WORKORDER_LOCATION A WHERE A.WOID=?";
					System.out.println(sql1);	
					PreparedStatement pst1=con.prepareStatement(sql1);
					pst1.setInt(1, wodto.getId());
					ResultSet rs1 = pst1.executeQuery();			
									
					while(rs1.next()) {
					  	WOLocationDTO locdto= new WOLocationDTO();
					 // 	locdto.setWoid(rs1.getInt("WOID"));
					 // 	locdto.setLocid(rs1.getInt("LOCID"));
					  	locdto.setLocother(rs1.getString("LOCROI"));
					  	System.out.println(rs1.getString("STATENAME"));
					  	locdto.setStatename(rs1.getString("STATENAME"));
					  	locdto.setDistname(rs1.getString("DISTRICTNAME"));
					  	locarl.add(locdto);
					}
					
					wodto.setWoLocList(locarl);
					
					pst1.close();
					rs1.close();
					
					al.add(wodto);
				}
			}
			con.close();
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
		return al;
	}




}
