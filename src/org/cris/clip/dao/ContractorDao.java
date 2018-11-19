/*
 * Created on Nov 18, 2017
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.cris.clip.dao;

import java.io.PrintWriter;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.cris.clip.connection.DBConnection;
import org.cris.clip.dto.AdministratorDTO;
import org.cris.clip.dto.ContractorDTO;

/**
 * @author Deep
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ContractorDao {
	public ArrayList getContractorsList(AdministratorDTO adto,String zone,String div,String dept) {
		ArrayList al = new ArrayList();
		Connection con=null;		
		try {
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
                        //System.out.println("Hey I got zone:"+zone+" div:"+div+" dept:"+dept);
			String patch1="";			
                        String divorhq = "";
                                                
                        if(dept!=null && !dept.equals("")) {
                            patch1 = " where id in (select contractorid from contract_work_order where wodept="+dept+") ";
                        }
                        else if(div!=null && !div.equals("")) {
                            divorhq = new UtilityDao().checkdivhq(div);
                            if(divorhq.equals("d"))
                                patch1 = " where id in (select contractorid from contract_work_order where wodiv="+div+") ";
                            else
                                patch1 = " where id in (select contractorid from contract_work_order where wozone=(Select zoneid from CONTRACT_DIV_MASTER where divid="+div+" ) ) ";
                        }
                        else if(zone!=null && !zone.equals("")) {
                            patch1 = " where id in (select contractorid from contract_work_order where wozone="+zone+") ";
                        }
//			if(adto.getLevelId().equals("1")) {
//				patch1=" where zoneid='"+adto.getZoneId()+"' ";
//				if(!div.equals("")) {
//					patch1 = patch1 + " and divid='"+div+"'";
//				}
//				if(!dept.equals("")) {
//					patch1 = patch1 + " and deptid='"+dept+"'";
//				}
//			}	
//			else if(adto.getLevelId().equals("2")) {
//				patch1=" where divid='"+adto.getDivId()+"' ";
//				if(!dept.equals("")) {
//					patch1 = patch1 + " and deptid='"+dept+"'";
//				}
//			}	
//			else if(adto.getLevelId().equals("3")) {
//				patch1=" where deptid='"+adto.getDeptId()+"' ";			
//			}			
			patch1 = patch1+ " and APPROVED='1' and APPROVEDBY is not null";
			String sql="Select * from CONTRACTORREGISTRATION "+patch1;
			System.out.println(sql);
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				ContractorDTO cdto = new ContractorDTO();
				cdto.setId(rs.getString("ID"));
				//cdto.setAadhar(rs.getString("AADHAR"));
				cdto.setFirmName(rs.getString("FIRMNAME"));
				//cdto.setFirmRegNo(rs.getString("FIRMREGISTRATIONNO"));
				cdto.setName(rs.getString("NAME"));
				//cdto.setAddress(rs.getString("PERMANENTADDRESS"));
				//cdto.setIssAuth(rs.getString("WOISSUEDBY"));
				//cdto.setMobile(rs.getString("MOBILENO"));
				//cdto.setLL(rs.getString("LANDLINE"));
				//cdto.setPan(rs.getString("PANNO"));
				//cdto.setEMail(rs.getString("EMAILID"));
				//cdto.setWoLoiNo(rs.getString("WOREFERNCE"));
				//cdto.setPF(rs.getString("PFREGISTRATIONO"));
				//cdto.setWoLoiDate(rs.getString(""));
				//cdto.setPin(Integer.parseInt(rs.getString("PERMANENTPINCODE")));
				//cdto.setCompany(Integer.parseInt(rs.getString("")));
				//cdto.setState(rs.getString("PERMANENTPLACE"));
				//cdto.setRegnDate(rs.getString("CREATIONDATE"));
				//cdto.setApprovalStatus(rs.getString("APPROVED"));
				al.add(cdto);
			}		
		}
		catch(Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		return al;
	}
	public ArrayList getContractorsList(AdministratorDTO adto,String status) {
		ArrayList al = new ArrayList();
		Connection con=null;	
		
		try {
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
			String patch="";
			if(status.equals("0,1")) {
                            if(!adto.getLevelId().equals("0")) {
                                
				if(adto.getLevelId().equals("1")) {
                                        
                                    if(adto.getDeptId()==null)
                                        patch=" where ( (zoneid='"+adto.getZoneId()+"' and approved='0') or  ( approved='1' and approvedby in (Select userid from ADMIN_USERS where ZONEID='"+adto.getZoneId()+"' ) ) or ( id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getZoneId()+") ) )";
                                    //else
                                        //patch=" where ( (zoneid='"+adto.getZoneId()+"' and approved='0' and deptid in (Select DEPTID from DEPARTMENT_MASTER where ZONEID='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"' ) ) or  ( approved='1' and approvedby in (Select userid from ADMIN_USERS where ZONEID='"+adto.getZoneId()+"' ) )  or ( id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getZoneId()+") ) )";

                                }
                                else if(adto.getLevelId().equals("2"))
					patch=" where ( (divid='"+adto.getDivId()+"' and approved='0') or  ( approved='1' and approvedby in (Select userid from ADMIN_USERS where ZONEID='"+adto.getZoneId()+"' ) )  or ( id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getZoneId()+") ) )";
				
                                else if(adto.getLevelId().equals("3")) {
                                    if(adto.getDivOrHq().equals("d"))
                                        patch=" where ( (deptid='"+adto.getDeptId()+"' and approved='0') or  ( approved='1' and approvedby in (Select userid from ADMIN_USERS where ZONEID='"+adto.getZoneId()+"' ) )  or ( id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getZoneId()+") ) )";
                                    else
                                        patch=" where ( (zoneid='"+adto.getZoneId()+"' and approved='0' and deptid in (Select DEPTID from DEPARTMENT_MASTER where ZONEID='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"' ) ) or  ( approved='1' and approvedby in (Select userid from ADMIN_USERS where ZONEID='"+adto.getZoneId()+"' ) )  or ( id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getZoneId()+") ) )";
                                }
                            }
			}
			else if(status.equals("1")) {
                            if(adto.getLevelId().equals("0")) {
				patch = " where approved='1' ";
                            }
                            else {
                                patch = " where ( approved='1' and approvedby in (Select userid from ADMIN_USERS where ZONEID='"+adto.getZoneId()+"' ) )  or ( id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getZoneId()+") ) ";
                            }
                        }                        
                        else if(status.equals("2")) { // contractors who have verified local (zonal) work orders
                            if(adto.getLevelId().equals("1")) {
                                if(adto.getDeptId()==null) {
                                    patch =" where approved='1' and id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getZoneId()+") ";
                                }
                                else {
                                    patch =" where approved='1' and id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WODEPT in ( Select DEPTID from DEPARTMENT_MASTER where ZONEID='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"' )) ";
                                }
                            }
                            else if(adto.getLevelId().equals("2")) {
                                patch =" where approved='1' and id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getDivId()+") ";
                            }
                            else if(adto.getLevelId().equals("3")) {
                                patch =" where approved='1' and id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getDeptId()+") ";
                            }
                            else if(adto.getLevelId().equals("0")) {
				patch = " where approved='1' ";
                            }
                        }
			
			String sql="Select (Select SHORT_DESIG from ADMIN_DESIGNATION where id=(Select designation from ADMIN_USERS where userid=VER_APPLIED_TO)) as appliedtodesig,"
                                + "id,AADHAR,FIRMNAME,FIRMREGISTRATIONNO,name,PERMANENTADDRESS,WOISSUEDBY,mobileno,landline,panno,emailid,WOREFERNCE,PFREGISTRATIONO,PERMANENTPINCODE,"
                                + " PERMANENTPLACE,CREATIONDATE,approved,approvaldate,approvedby,worefdoc,(select zone_code from CONTRACT_ZONE_MASTER where zone_id=zoneid) as zone, "
                                + "(select divcode from CONTRACT_DIV_MASTER where divid=cr.divid) as div, VER_APPLIED_TO,"
                                + "(select deptname from DEPARTMENT_MASTER where deptid=cr.deptid) as dept from CONTRACTORREGISTRATION cr "+patch+" order by APPROVED asc,CREATIONDATE desc ";
			System.out.println(sql);
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				ContractorDTO cdto = new ContractorDTO();
				cdto.setId(rs.getString("ID"));
				cdto.setAadhar(rs.getString("AADHAR"));
				cdto.setFirmName(rs.getString("FIRMNAME"));
				cdto.setFirmRegNo(rs.getString("FIRMREGISTRATIONNO"));
				cdto.setName(rs.getString("NAME"));
				cdto.setAddress(rs.getString("PERMANENTADDRESS"));
				cdto.setIssAuth(rs.getString("WOISSUEDBY"));
				cdto.setMobile(rs.getString("MOBILENO"));
				cdto.setLL(rs.getString("LANDLINE"));
				cdto.setPan(rs.getString("PANNO"));
				cdto.setEMail(rs.getString("EMAILID"));
				cdto.setWoLoiNo(rs.getString("WOREFERNCE"));
				cdto.setPF(rs.getString("PFREGISTRATIONO"));
				//cdto.setWoLoiDate(rs.getString(""));
				cdto.setPin(Integer.parseInt(rs.getString("PERMANENTPINCODE")));
				//cdto.setCompany(Integer.parseInt(rs.getString("")));
				cdto.setState(rs.getString("PERMANENTPLACE"));
				cdto.setRegnDate(rs.getString("CREATIONDATE"));
				cdto.setApprovalStatus(rs.getString("APPROVED"));
				cdto.setWoRefFileNo(rs.getString("WOREFDOC"));
                                cdto.setZoneId(rs.getString("zone"));
                                cdto.setDivId(rs.getString("div"));
                                cdto.setDeptId(rs.getString("dept"));
                                cdto.setApprover(rs.getString("approvedby"));
                                cdto.setApprovalDate(rs.getString("approvaldate"));
                                cdto.setVerAppliedTo(rs.getString("appliedtodesig"));
                                cdto.setVerAppliedToId(rs.getString("VER_APPLIED_TO"));
                                cdto.setApproverPost( (new UtilityDao()).getAdminPostFromId(rs.getString("approvedby")) );
				al.add(cdto);
			}		
		}
		catch(Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		return al;
	}
	public int regContractor(ContractorDTO cdto,String authid) {	
		Connection con=null;	
		int retVal=0, retVal1=0;
		try {
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
                        con.commit();
                        
                        CallableStatement proc = con.prepareCall("{ call CONTREGNDUPLICATEFIELDS(?,?,?,?,?,?,?,?) }");
                        proc.setString(           1, cdto.getPan()       );
                        proc.setString(           2, cdto.getMobile()    );
                        proc.setString(           3, cdto.getAadhar()    );
                        proc.setString(           4, cdto.getFirmRegNo() );
                        proc.setString(           5, cdto.getEMail()     );
                        proc.setString(           6, cdto.getPF()        );
                        proc.setString(           7, cdto.getWoLoiNo()   );
                        proc.registerOutParameter(8, java.sql.Types.INTEGER);
                        proc.executeUpdate();
                        if( proc.getInt(8) == 0) {                        
                            String sql="Insert into CONTRACTORREGISTRATION values (CONTRACTER_REGISTRATION_SEQ.nextval,?,?,?,?,?,?,?,null,null,null,?,?,?,?,?,?,sysdate,null,?,0,null,null,?,?,?,null,?,null,?,?,(Select deptid from ADMIN_USERS where USERID='"+authid+"'),?,?)";			
                            PreparedStatement pst = con.prepareStatement(sql);
                            pst.setString(1, cdto.getName());
                            pst.setString(2, cdto.getFName());
                            pst.setString(3, cdto.getFirmName());			
                            pst.setString(4, cdto.getFirmRegNo());
                            pst.setString(5, cdto.getAddress());
                            pst.setString(6, cdto.getState());
                            pst.setInt(7, cdto.getPin());

                            pst.setString(8, cdto.getLL());
                            pst.setString(9, cdto.getMobile());
                            pst.setString(10, cdto.getEMail());
                            pst.setString(11, cdto.getPan());
                            pst.setString(12, cdto.getAadhar());
                            
                            if(cdto.getEPFRegStatus().equals("0")) {
                                pst.setString(13, cdto.getPF());
                                pst.setString(14, null);
                            }
                            else {
                                pst.setString(13, null);
                                pst.setString(14, cdto.getEPFRegStatus());
                            }                            
                            
                            pst.setString(15, cdto.getWoLoiNo()+" "+cdto.getWoLoiDate());
                            pst.setString(16, cdto.getIssAuth());
                            pst.setInt(   17, cdto.getCompany());
                            pst.setString(18, cdto.getIrepsId());    
                            pst.setString(19, cdto.getZoneId());
                            pst.setString(20, cdto.getDivId());
                            pst.setString(21, cdto.getWoRefFileNo());
                            pst.setString(22, authid);
                            
                            retVal = pst.executeUpdate();

                            String sql1="Insert into CONTRACTORPASSWORD values(?,?,null,null,sysdate,CONTRACTER_REGISTRATION_SEQ.currval,0,null,null)";
                            PreparedStatement pst1 = con.prepareStatement(sql1);
                            pst1.setString(1, cdto.getPan());
                            pst1.setString(2, cdto.getPassword());
                            retVal1 = pst1.executeUpdate();

                            con.commit();			
                        }                        
		}
		catch(Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		return retVal+retVal1;
	}
	public int approveContractor(AdministratorDTO adto,String action,String uid ,String status,String mobile,String ll,String pan,String aadhar,String email,String pfregno) {	
		Connection con=null;	
		int retVal=0,retVal1=0;
		try {
			con=DBConnection.getInstance().getDBConnection();
                        con.setAutoCommit(false);
                        con.commit();
                        String sql,sql1;
                        PreparedStatement ps,ps1;
                        String s[] = {"","VERIFIED","REJECTED"};
                        if(status.equals("0")) {
                            sql = "update CONTRACTORREGISTRATION set LANDLINE=?,MOBILENO=?,EMAILID=?,PANNO=?,AADHAR=?,PFREGISTRATIONO=?, MODIFICATIONDATE=sysdate,MODIFIEDBY=? where ID=?";
                            ps = con.prepareStatement(sql);
                            ps.setString(1, ll);
                            ps.setString(2, mobile);
                            ps.setString(3, email);
                            ps.setString(4, pan);
                            ps.setString(5, aadhar);
                            ps.setString(6, pfregno);
                            ps.setString(7, adto.getUserId());
                            ps.setInt(8, Integer.parseInt(uid));
                            retVal = ps.executeUpdate();
                            
                            sql1 = "update CONTRACTORPASSWORD set PANNO=? where CONTRACTORID=?";
                            ps1 = con.prepareStatement(sql1);
                            ps1.setString(1, pan);
                            ps1.setInt(2, Integer.parseInt(uid));
                            retVal1 = ps1.executeUpdate();
                            
                            con.commit();
                        }
                        else {
                            sql = "update CONTRACTORREGISTRATION set LANDLINE=?,MOBILENO=?,EMAILID=?,PANNO=?,AADHAR=?,PFREGISTRATIONO=?, APPROVED=?,APPROVEDBY=?,APPROVALDATE=sysdate,MODIFICATIONDATE=sysdate,MODIFIEDBY=? where ID=?";
                            ps = con.prepareStatement(sql);
                            ps.setString(1, ll);
                            ps.setString(2, mobile);
                            ps.setString(3, email);
                            ps.setString(4, pan);
                            ps.setString(5, aadhar);
                            ps.setString(6, pfregno);
                            ps.setString(7, status);
                            ps.setString(8, adto.getUserId());                            
                            ps.setString(9, adto.getUserId());
                            ps.setInt(10, Integer.parseInt(uid));
                            retVal = ps.executeUpdate();
                            
                            sql1 = "update CONTRACTORPASSWORD set PANNO=?,APPROVED=? where CONTRACTORID=?";
                            ps1 = con.prepareStatement(sql1);
                            ps1.setString(1, pan);
                            ps1.setString(2, status);
                            ps1.setInt(3, Integer.parseInt(uid));
                            retVal1 = ps1.executeUpdate();
                            
                            con.commit();
                            
//                            if(!email.equals("")) {
//                                SendEmail x = new SendEmail("utskolkata@cris.org.in",new String[]{email},"Your Registration application in IR Shramik Kalyan Portal is "+s[Integer.parseInt(status)],"IR Shramik Kalyan Portal Registration","","lotusnotes",false);
//                                ExecutorService executor = Executors.newSingleThreadExecutor();
//                                executor.execute(x);
//                                executor.shutdown();
//                                try {
//                                    if(!executor.awaitTermination(250, TimeUnit.MILLISECONDS)) {
//                                        executor.shutdownNow();
//                                    }
//                                }
//                                catch(InterruptedException e) {
//                                    executor.shutdownNow();
//                                }
//                            }
                            if(!mobile.equals("")) {
                                SendSms y = new SendSms(mobile,"Your Registration application in IR Shramik Kalyan Portal is "+s[Integer.parseInt(status)]);
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
		}
		catch(Exception e) {
                    e.printStackTrace();
                    try {
                        con.rollback();
                    }
                    catch(SQLException ez) {}
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
		return retVal+retVal1;
	}

	
   public String getUser(int conId)
   {
	   String user="";
	   Connection con=null;	
	   try {
                con=DBConnection.getInstance().getDBConnection();
                con.setAutoCommit(false);
                String sql="Select PANNO from CONTRACTORPASSWORD WHERE CONTRACTORID='"+conId+"'";
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                rs.next();
                user=rs.getString("PANNO");
                rs.close();
                ps.close();
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
	   return user;
   }
   public int getPendingContractorsCount(AdministratorDTO adto,String adminLevel) {
		Connection con=null;	
		int count = 0;
		try {
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
//			String patch="";                        
//                        if(!adminLevel.equals("0")) {
//                            if(adto.getLevelId().equals("1"))                                 
//                                patch=" where zoneid='"+adto.getZoneId()+"' and ";
//                            else if(adto.getLevelId().equals("2"))
//                                    patch=" where divid='"+adto.getDivId()+"' and ";
//                            else if(adto.getLevelId().equals("3")) {
//                                if(adto.getDivOrHq().equals("d"))    
//                                    patch=" where deptid='"+adto.getDeptId()+"' and ";
//                                else if(adto.getDivOrHq().equals("h"))
//                                    patch=" where zoneid='"+adto.getZoneId()+"' and deptid in (Select DEPTID from DEPARTMENT_MASTER where ZONEID='"+adto.getZoneId()+"' and deptcode='"+adto.getDeptCode()+"' ) and ";
//                            }
//                            else
//                                patch = " where ";
//                        }
//                        else
//                            patch = " where ";                        
			String sql="Select count(*) as count from CONTRACTORREGISTRATION where VER_APPLIED_TO='"+adto.getUserId()+"' and APPROVED='0'";
			System.out.println(sql);
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();			
			if(rs.next()) 
				count = rs.getInt("count");
		}
		catch(Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		return count;
	}
   public ContractorDTO getConProfile(String conid)
   {
	   ContractorDTO contrDTO=null;
	   Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			String sql="SELECT id,name,firmname,FIRMREGISTRATIONNO,PERMANENTADDRESS,PERMANENTPLACE,PERMANENTPINCODE,MOBILENO,emailid,panno,ireps_id,fathername,"
                                + "landline,aadhar,PFREGISTRATIONO,WOREFERNCE,WOISSUEDBY,APPROVED,"
                                + "(select SHORT_DESIG from admin_designation where id=(Select designation from admin_users where userid=WOISSUEDBY)) as loaDesig, "
                                + "(select zone_code from CONTRACT_ZONE_MASTER where zone_id=(Select zoneid from admin_users where userid=WOISSUEDBY)) as zone,"
                                + "(select divcode from CONTRACT_DIV_MASTER where divid=(Select divid from admin_users where userid=WOISSUEDBY)) as div,"
                                + "(select deptname from DEPARTMENT_MASTER where deptid=(Select deptid from admin_users where userid=WOISSUEDBY)) as dept "
                                + "FROM CONTRACTORREGISTRATION WHERE ID='"+conid+"'";	
			System.out.println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			rs.next();
			contrDTO = new ContractorDTO();
			contrDTO.setId(rs.getString("ID"));
			contrDTO.setName(rs.getString("NAME"));
			contrDTO.setFirmName(rs.getString("FIRMNAME"));
			contrDTO.setFirmRegNo(rs.getString("FIRMREGISTRATIONNO"));
			contrDTO.setAddress(rs.getString("PERMANENTADDRESS"));
			contrDTO.setState(rs.getString("PERMANENTPLACE"));
			contrDTO.setPin(rs.getInt("PERMANENTPINCODE"));
			contrDTO.setMobile(rs.getString("MOBILENO"));
			contrDTO.setEMail(rs.getString("EMAILID"));
			contrDTO.setPan(rs.getString("PANNO"));
			contrDTO.setIrepsId(rs.getString("IREPS_ID"));
			contrDTO.setFName(rs.getString("FATHERNAME"));
			contrDTO.setLL(rs.getString("LANDLINE"));
			contrDTO.setAadhar(rs.getString("AADHAR"));
			contrDTO.setPF(rs.getString("PFREGISTRATIONO"));
			contrDTO.setWoLoiNo(rs.getString("WOREFERNCE"));
			contrDTO.setIssAuth(rs.getString("WOISSUEDBY"));
			contrDTO.setApprovalStatus(rs.getString("APPROVED"));
                        String temp=rs.getString("loaDesig")+"/"+rs.getString("zone");
                        if(rs.getString("div")!=null) {
                            temp = temp +"/"+rs.getString("div");
                            if(rs.getString("dept")!=null)
                                temp = temp +"/"+rs.getString("dept");
                        }
			contrDTO.setQualifiedLoaIssuer(temp);
			rs.close();
			pst.close();			
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
		return contrDTO;		
   }
   
   public int  resetConPass(String conid,String pass)
   {
	   int result = 0;
	   Connection con=null;
		
		try
		{
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
			String sql="UPDATE CONTRACTORPASSWORD SET PASSWD=? WHERE CONTRACTORID=?";			
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, pass);
			pst.setString(2, conid);
			result = pst.executeUpdate();	
			con.commit();			
			pst.close();
			
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
   public int resetContractorPassword(int id,String newpass) {
		Connection con=null;		
		int retVal=0;
		String sql = "Update CONTRACTORPASSWORD set PASSWD=? where CONTRACTORID=?";  
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
   
   public int checkWONO(String wono)
   {
	   int result=0;
	   Connection con=null;	   
	   String sql = "SELECT * FROM CONTRACT_WORK_ORDER WHERE WORKORDERNO=? AND APPROVED <> '2'";  
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, wono.toUpperCase());			
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				result =1;
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
	   
	   return result;
	   
   }
   public int changeLOAIssAuth(String zoneid,String divid,String deptid,String desigid,String uid)
   {
	   int result=0;
	   Connection con=null;	
           String deptCheck;
           if(!deptid.equals(""))
               deptCheck = " , deptid="+deptid;
           else
               deptCheck = " , deptid=null";
	   String sql = "update CONTRACTORREGISTRATION set ZONEID="+zoneid+", DIVID="+divid+deptCheck+", VER_APPLIED_TO='"+desigid+"' where approved=0 and id="+uid;  
		try {
			con = DBConnection.getInstance().getDBConnection();
			PreparedStatement ps = con.prepareStatement(sql);
				
			System.out.println(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				result =1;
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
	   
	   return result;
	   
   }
}
