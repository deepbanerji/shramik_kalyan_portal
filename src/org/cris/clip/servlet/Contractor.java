package org.cris.clip.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.cris.clip.connection.DBConnection;
import org.cris.clip.dao.ContractorDao;
import org.cris.clip.dao.UtilityDao;
import org.cris.clip.dto.AdministratorDTO;
import org.cris.clip.dto.ContractorDTO;
import org.cris.clip.dao.WorkOrder;
import org.json.JSONArray;
import org.json.JSONObject;


import oracle.sql.BLOB;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class Contractor
 */
@WebServlet("/Contractor")
public class Contractor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Contractor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession(false);
		if(session==null) {
			RequestDispatcher rd = req.getRequestDispatcher("/clip.jsp");  
                    rd.include(req, resp); 
                    return;
		}
		
		
		ContractorDTO conDTO=(ContractorDTO) session.getAttribute("contractor");
		
		String action=req.getParameter("action");
                        if (action.equalsIgnoreCase("registerContractor")) {
			
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			Connection con=null;
			ContractorDTO cdto = new ContractorDTO();
                        resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
                        String veri_auth_id="";
			try {
				con = DBConnection.getInstance().getDBConnection();
				String sql = "INSERT INTO CONTRACT_WORKORDER_FILE VALUES (CONTRACT_WORKORDER_FILE_SEQ.nextval,?,?,?)";
				PreparedStatement pst=con.prepareStatement(sql);
				List items = upload.parseRequest(req);
				FileItem item = null;
				Iterator iter = items.iterator();
				boolean docSizeOk=false;				
				while (iter.hasNext()) {
					item = (FileItem) iter.next();
				    if (item.isFormField()) {				    	
				    	String fieldname = item.getFieldName();
				        String fieldvalue = item.getString();
				        if (fieldname.equals("inpAadhar"))
				        	cdto.setAadhar(fieldvalue);
				        else if (fieldname.equals("inpFirmName"))				        
				        	cdto.setFirmName(fieldvalue);
				        else if (fieldname.equals("inpFirmRegNo"))
				        	cdto.setFirmRegNo(fieldvalue);
				        else if (fieldname.equals("inpName"))
				        	cdto.setName(fieldvalue);
				        else if (fieldname.equals("inpFName"))
				        	cdto.setFName(fieldvalue);
				        else if (fieldname.equals("inpAdd"))
				        	cdto.setAddress(fieldvalue);
				        else if (fieldname.equals("inpIssAuth"))
				        	cdto.setIssAuth(fieldvalue);
				        else if (fieldname.equals("inpMobile"))
				        	cdto.setMobile(fieldvalue);
				        else if (fieldname.equals("inpLL"))
				        	cdto.setLL(fieldvalue);
				        else if (fieldname.equals("inpPAN"))
				        	cdto.setPan(fieldvalue);
				        else if (fieldname.equals("inpEmail"))
				        	cdto.setEMail(fieldvalue);
				        else if (fieldname.equals("inpWO_LOI"))
				        	cdto.setWoLoiNo(fieldvalue);
				        else if (fieldname.equals("inpPFNo"))
				        	cdto.setPF(fieldvalue);
				        else if (fieldname.equals("inpWO_LOI_Date"))
				        	cdto.setWoLoiDate(fieldvalue);
				        else if (fieldname.equals("inpPin"))
				        	cdto.setPin(Integer.parseInt(fieldvalue));				        
				        else if (fieldname.equals("selState"))
				        	cdto.setState(fieldvalue);
				        else if (fieldname.equals("inpPwd"))
				        	cdto.setPassword(fieldvalue);
				        else if (fieldname.equals("selZone"))
				        	cdto.setZoneId(fieldvalue);
				        else if (fieldname.equals("selDiv"))
				        	cdto.setDivId(fieldvalue);
				        else if (fieldname.equals("selDept"))
				        	cdto.setDeptId(fieldvalue);
                                        else if (fieldname.equals("selDesig"))
				        	veri_auth_id = fieldvalue;
                                        else if (fieldname.equals("inpIrepsId"))
				        	cdto.setIrepsId(fieldvalue);
                                        else if (fieldname.equals("selEpfStat"))
				        	cdto.setEPFRegStatus(fieldvalue);
				    }
				    else {				    	
				    	InputStream is = item.getInputStream();
				    	final byte[] bytes = new byte[is.available()];
				    	if(bytes.length>0) {
                                            is.read(bytes);
                                            pst.setBytes(1,bytes);				    	
                                            pst.setString(2,item.getName());
                                            pst.setLong(3,item.getSize());
                                            docSizeOk=true;
				    	}			    					    	
				    }				    
				}								
				cdto.setCompany(0);
				if(docSizeOk==true) {
                                    int result = pst.executeUpdate();					
                                    if(result==1) {
                                        String sql1 = "Select CONTRACT_WORKORDER_FILE_SEQ.currval as cv from dual";
                                        PreparedStatement pst1=con.prepareStatement(sql1);
                                        ResultSet rs1 = pst1.executeQuery();
                                        int fileNum=0;
                                        if(rs1.next())
                                                fileNum = rs1.getInt("cv");
                                        if(fileNum!=0) {
                                            cdto.setWoRefFileNo(String.valueOf(fileNum));
                                            					
                                            out.println("<script type=\"text/javascript\">");
                                            if((new ContractorDao()).regContractor(cdto,veri_auth_id)==2) {				
                                                out.println("alert('Registered successfully!');");
                                                out.println("location='/clip.jsp';");				
                                            }				
                                            else {
                                                out.println("alert('Registration Failed!');");
                                                out.println("location='/clip.jsp';");				
                                            }
                                            out.println("</script>");
                                            out.close();
                                        }
                                        else {
                                            out.println("<script type=\"text/javascript\">");
                                            out.println("alert('Registration Failed!');");
                                            out.println("location='/clip.jsp';");	
                                            out.println("</script>");
                                            out.close();
                                        }
                                    }
                                    else {
                                        out.println("<script type=\"text/javascript\">");
                                        out.println("alert('Registration Failed!');");
                                        out.println("location='/clip.jsp';");	
                                        out.println("</script>");
                                        out.close();
                                    }
				}
                                else { 
                                    cdto.setWoRefFileNo(null);
                                    					
                                    out.println("<script type=\"text/javascript\">");
                                    if((new ContractorDao()).regContractor(cdto,veri_auth_id)==2) {				
                                        out.println("alert('Registered successfully!');");
                                        out.println("location='/clip.jsp';");				
                                    }				
                                    else {
                                        out.println("alert('Registration Failed!');");
                                        out.println("location='/clip.jsp';");				
                                    }
                                    out.println("</script>");
                                    out.close();                                                                        
                                }
				con.close();
			}
			catch(Exception e) {
                            e.printStackTrace();
                            //try {
                            //        con.rollback();
                            //} catch (SQLException e1) {
                                    // TODO Auto-generated catch block
                            //        e1.printStackTrace();
                            //}
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
		else if (action.equalsIgnoreCase("listAll")) {
			AdministratorDTO adto = (AdministratorDTO)(req.getSession(false)).getAttribute("administrator");
			if(adto==null) {
                            
				resp.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = (new ContractorDao()).getContractorsList(adto,req.getParameter("status"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				ContractorDTO cdto = (ContractorDTO)i.next();
				row = new JSONObject();
				row.put("name", cdto.getName());
				row.put("firmname", cdto.getFirmName());
				row.put("firmregno", cdto.getFirmRegNo());
				row.put("state", cdto.getState());
				row.put("landline", cdto.getLL());
				row.put("mobile", cdto.getMobile());
				row.put("email", cdto.getEMail());
				row.put("pan", cdto.getPan());
				row.put("aadhar", cdto.getAadhar());
				row.put("pfno", cdto.getPF());
				row.put("regnDate", cdto.getRegnDate());
				row.put("approvalStatus", cdto.getApprovalStatus());
				row.put("uid", cdto.getId());
				row.put("woref", cdto.getWoLoiNo());
				row.put("worefdoc", cdto.getWoRefFileNo());
                                row.put("zone", cdto.getZoneId());
                                row.put("div", cdto.getDivId());
                                row.put("dept", cdto.getDeptId());
                                row.put("approver", cdto.getApprover());
                                row.put("approvalDate", cdto.getApprovalDate());
                                row.put("appliedto", cdto.getVerAppliedTo());
                                row.put("appliedtoId", cdto.getVerAppliedToId());
                                row.put("approverPost", cdto.getApproverPost());
				rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = resp.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("approveContractor")) {
			AdministratorDTO adto = (AdministratorDTO)(req.getSession(false)).getAttribute("administrator");
			int retVal = (new ContractorDao()).approveContractor(adto,req.getParameter("action"),req.getParameter("uid"),req.getParameter("status"),req.getParameter("mobile"),req.getParameter("ll"),req.getParameter("pan"),req.getParameter("aadhar"),req.getParameter("email"),req.getParameter("pfregno"));
			PrintWriter out = resp.getWriter();
			out.println(retVal);
		}
		else if (action.equalsIgnoreCase("addNewWO"))
        {
			
	        System.out.println("addNewWO");
			Connection con=null;
			WorkOrder WorkOrder=new WorkOrder(); 
			UtilityDao utildao=new UtilityDao();
			int result = 0;
			
			try {			
				
				
				int WorkOrdId=WorkOrder.getWorkOrdId();
				
				con=DBConnection.getInstance().getDBConnection();				
				con.setAutoCommit(false);
				
				//--------------------------
				
				String sql="INSERT INTO CONTRACT_WORK_ORDER (WOID,WORKORDERNO,NAMEOFTHEWORK,"+
				"DATEOFCOMMENCEMENT,DATEOFCOMPLETIONWORK,EXTDATEOFCOMPLETION,MAXNOOFEMPLOYEES,WORKORDERISSUEDBY,LABOURLICENCE,NAMEOFTHEPRINCIPALEMPLOYER,"+
				"PRINCIPALEMPLOYERADDRESS,CONTRACTORID,CREATEDBY,WOZONE,WODIV,WODEPT,LOAVALUE)" +
				"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pst=con.prepareStatement(sql);
				 
				pst.setInt(1,WorkOrdId); 
				pst.setString(2,req.getParameter("WorkOrd").trim().toUpperCase());
				pst.setString(3,req.getParameter("WorkName").toUpperCase());
			//	pst.setString(4,req.getParameter("LocName").toUpperCase());
				pst.setDate(4, java.sql.Date.valueOf(req.getParameter("StartDate")));
		    	pst.setDate(5, java.sql.Date.valueOf(req.getParameter("EndDate")));
		    	System.out.println("ExtDate"+req.getParameter("ExtDate"));
		    	
		    	if(req.getParameter("ExtDate")!=null && !(req.getParameter("ExtDate").isEmpty()))
		    	{
		    		pst.setDate(6, java.sql.Date.valueOf(req.getParameter("ExtDate")));
		    	}	
		    	else
		    	{
		    	    pst.setDate(6, java.sql.Date.valueOf(req.getParameter("EndDate")));
		    	}				
				pst.setInt(7,Integer.parseInt(req.getParameter("MaxEmp")));
			//	pst.setString(8,req.getParameter("IssBy").toUpperCase());
				pst.setString(8,req.getParameter("inpIssAuth"));
				pst.setString(9,req.getParameter("LabLic").toUpperCase());
			//	pst.setString(10,req.getParameter("PrincEmp").toUpperCase());
				pst.setString(10,req.getParameter("inpIssAuth"));
				pst.setString(11,req.getParameter("PrincEmpAdd").toUpperCase());			
				pst.setInt(12,Integer.parseInt(conDTO.getId()));
			//	pst.setDate(14, java.sql.Date.valueOf(LocalDate.now()));			
				pst.setString(13,(new ContractorDao()).getUser(Integer.parseInt(conDTO.getId())));
				pst.setString(14,req.getParameter("WOZONE")); 
				pst.setString(15,req.getParameter("WODIV")); 
				pst.setString(16,req.getParameter("WODEPT")); 
				pst.setString(17,req.getParameter("LoaValue"));
				
				pst.executeUpdate();		 
				
			    String[] locid=req.getParameterValues("WOLOC"); 
			    String[] locoth=req.getParameterValues("LOCOTH");
			    String[] locstate=req.getParameterValues("LOCSTATE");
			    String[] locdist=req.getParameterValues("LOCDIST");
				for(int i=0;i<locid.length;i++)
				{
				 System.out.println("woloc"+locid[i]); 	
				 String location = locid[i];
				 				
				  sql="INSERT INTO CONTRACT_WORKORDER_LOCATION"
				  		+ " (WOID,LOCID,LOCROI,STATEID,DISTRICTID) VALUES(?,?,?,?,?)";
				  pst=con.prepareStatement(sql);					 
				  pst.setInt(1,WorkOrdId); 
				  pst.setString(2,location);
				  if(location.equals("1"))
				  {	  
					  System.out.println("locoth"+locoth[i]);
					  pst.setString(3,locoth[i].toUpperCase());
					  pst.setString(4, locstate[i]);
					  pst.setString(5, locdist[i]);	
				  }	 
				  else
				  {
					  System.out.println(utildao.getLocation(location));
				      pst.setString(3, utildao.getLocation(location));
				      pst.setString(4, utildao.getState(location));
					  pst.setString(5, utildao.getDistrict(location));	
				  }	  
				 
				  
				  result=pst.executeUpdate();
				  pst.close();
								
				}		
				
				resp.setContentType("text/html");
				PrintWriter out = resp.getWriter();	
				
				// Added for testing
				
				if(result!=0) {
					out.println("<script type=\"text/javascript\">");
					out.println("alert('Work Order Registered Successfully!');");
					out.println("location='/contractor/WorkOrder.jsp';");
					out.println("</script>");
					out.close();
				}
				else {
					con.rollback();
					out.println("<script type=\"text/javascript\">");
					out.println("alert('Process failed');");
					out.println("location='/contractor/WorkOrder.jsp';");
					out.println("</script>");
					out.close();
				}	
			  	con.commit();   
				con.close();
			}
			catch(SQLIntegrityConstraintViolationException e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
                                resp.setContentType("text/html");
				PrintWriter out = resp.getWriter();		
				out.println("<script type=\"text/javascript\">");
				out.println("alert('This Work Order ID already exists in the database!');");
				out.println("location='/contractor/WorkOrder.jsp';");
				out.println("</script>");
				out.close();
			}
			catch(Exception e) {
				try {
					if (con != null)
					{	
					 con.rollback();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
                                resp.setContentType("text/html");
				PrintWriter out = resp.getWriter();		
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Process failed - "+e.getClass()+"');");
				out.println("location='/contractor/WorkOrder.jsp';");
				out.println("</script>");
				out.close();
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
		
		
		else if (action.equalsIgnoreCase("getWorkOrderList")) {
			Connection con=null;
			
			try {
				con=DBConnection.getInstance().getDBConnection();
				
				
				resp.setContentType("text/json");
				String sql="select WOID, WORKORDERNO from CONTRACT_WORK_ORDER WHERE CONTRACTORID='"+conDTO.getId()+"' AND APPROVED='1'";
				PreparedStatement pst=con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery();
				PrintWriter out = resp.getWriter();				
				int i=0;
				String jsonText=null;
				if (rs!=null) { 
					jsonText="[";
				    while(rs.next()) {
						if (i==0)
							jsonText = jsonText + "{\"code\":\""+rs.getString("WOID")+"\",\"name\":\""+rs.getString("WORKORDERNO")+"\"}";
						else
							jsonText = jsonText + ",{\"code\":\""+rs.getString("WOID")+"\",\"name\":\""+rs.getString("WORKORDERNO")+"\"}";
						++i;
				    }
				    jsonText = jsonText + "]";
				}
				System.out.println("Work Order List :"+jsonText);
				out.println(jsonText);
				rs.close();
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
		}
		
		else if (action.equalsIgnoreCase("uploadWO"))
		{
			System.out.println("uploadWO");
			String wono=req.getParameter("wono");
			System.out.println(wono);
			int result=0;
			 resp.setContentType("text/html;charset=UTF-8");
			 PrintWriter out=resp.getWriter();
			 InputStream is =null;
			 FileItemFactory factory = new DiskFileItemFactory();
			 ServletFileUpload upload = new ServletFileUpload(factory);
			 
			 
			//  upload.setFileSizeMax(500 * 1024 ); 
			 
			 Connection con=null;
			 try
			 {
			 
			 con = DBConnection.getInstance().getDBConnection();			 
		     con.setAutoCommit(false);
			 String sql="SELECT CONTRACT_WORKORDER_FILE_SEQ.nextval AS DOCNO FROM DUAL";
			 PreparedStatement pst=con.prepareStatement(sql);
			 ResultSet rs = pst.executeQuery();
			 rs.next();
			 int maxDocId = rs.getInt("DOCNO"); 
			 
			 rs.close();
			 pst.close();
			 
			 sql = "INSERT INTO CONTRACT_WORKORDER_FILE VALUES(?,?,?,?)";
			 pst=con.prepareStatement(sql);		
			 pst.setInt(1, maxDocId);
			
			 List items = upload.parseRequest(req);
			 FileItem item = null;
			 Iterator iter = items.iterator();
			 boolean docSizeOk=false;
			 while (iter.hasNext()) 
			 {
					item = (FileItem) iter.next();
				    if (item.isFormField())
				    {
				    	String fieldname = item.getFieldName();
				        String fieldvalue = item.getString();
				      //  System.out.println(fieldname);
				      //  System.out.println(fieldvalue);
				    }
				    else
				    {
				      //	System.out.println("inside else");
				    	is = item.getInputStream(); 
				    	docSizeOk=true;				    	
				    	
				    	final byte[] bytes = new byte[is.available()];				    
				    	
				    	if(bytes.length>0) 
				    	{
				    		is.read(bytes);
				    		pst.setBytes(2,bytes);				    	
							pst.setString(3,item.getName());
							pst.setLong(4,item.getSize());							
						  //	System.out.println(item.getName());
							docSizeOk=true;
				    	}
				    }	
				    
				    if(docSizeOk==false) 
				    {
			    		out.println("<script type=\"text/javascript\">");
						out.println("alert('Documents of size is invalid!');");
						out.println("location='regp.jsp';");
						out.println("</script>");
						out.close();
					}
				    
				    else
				    {	
				     // System.out.println("before insert");
			    	 pst.executeUpdate();
			    	 pst.close();
			    	 
			    	// System.out.print("after insert");	
			    	 
			    	
			    		 
			    	 sql = "UPDATE CONTRACT_WORK_ORDER SET WO_FILENO='"+maxDocId+"' WHERE WOID='"+wono+"'";
					 pst=con.prepareStatement(sql);	
					 result =pst.executeUpdate();		    	 
			    	 con.commit();
			    	 
			    	 pst.close();
			    	 con.close();
			    	 
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
			 if(result==1)
			 {
				 resp.sendRedirect("/contractor/UploadWOAck.jsp?message=ok"); 
			 }	 
			 else
			 {		 
			  resp.sendRedirect("/contractor/UploadWOAck.jsp?message=fail");
			 } 
			 
		}
		
		else if (action.equalsIgnoreCase("downloadWO"))
		{
			System.out.println("downloadWO");
			String fileno=req.getParameter("fileno");
		 //	System.out.println(fileno);
			
			Connection con=null;
			try
			{
			  con=DBConnection.getInstance().getDBConnection();
				String sql="Select FILEDATA,FILE_NAME from CONTRACT_WORKORDER_FILE where FILENO="+fileno;
				System.out.println(sql);
				
				PreparedStatement pst=con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {
				    String docName=rs.getString("FILE_NAME");
				    System.out.println("DOC_Name"+docName);
		            Blob blob =  rs.getBlob("FILEDATA");
		            InputStream inputStream = blob.getBinaryStream();
		            int fileLength = (int)blob.length();
		            System.out.println("fileLength"+fileLength);
		            
		            ServletContext context = getServletContext();		            
		            String mimeType = context.getMimeType(docName);
		            System.out.println("mimeType:"+mimeType);
		            
			       if (mimeType == null) {        
			            mimeType = "application/octet-stream";
			        }
			        resp.setContentType(mimeType);
			        resp.setContentLength(fileLength);   
		            		         
			        String headerKey = "Content-Disposition";
			        String headerValue = String.format("attachment; filename=\"%s\"", docName);
			        resp.setHeader(headerKey, headerValue);
			         
			        OutputStream outStream = resp.getOutputStream();			         
			        byte[] buffer = new byte[4096];
			        int bytesRead = -1;
			         
			        while ((bytesRead = inputStream.read(buffer)) != -1) {
			            outStream.write(buffer, 0, bytesRead);
			        }
			         
			        inputStream.close();
			        outStream.close(); 		        
				}
			
			
				else {	resp.setContentType("text/html");  
					PrintWriter out=resp.getWriter();
					out = resp.getWriter();	
					out.println("<script type=\"text/javascript\">");
					out.println("alert('File not found!);");
					//out.println("location='uploadPO.jsp';");
					out.println("</script>");
					out.close();
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
		}	
		else if (action.equalsIgnoreCase("listBasedOnZnDvDpt")) {
			AdministratorDTO adto = (AdministratorDTO)(req.getSession(false)).getAttribute("administrator");
			if(adto==null) {
				resp.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = (new ContractorDao()).getContractorsList(adto,req.getParameter("zone"),req.getParameter("div"),req.getParameter("dept"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				ContractorDTO cdto = (ContractorDTO)i.next();
				row = new JSONObject();
				row.put("name", cdto.getName());
				row.put("firmname", cdto.getFirmName());
				//row.put("firmregno", cdto.getFirmRegNo());
				//row.put("state", cdto.getState());
				//row.put("landline", cdto.getLL());
				//row.put("mobile", cdto.getMobile());
				//row.put("email", cdto.getEMail());
				//row.put("pan", cdto.getPan());
				//row.put("aadhar", cdto.getAadhar());
				//row.put("pfno", cdto.getPF());
				//row.put("regnDate", cdto.getRegnDate());
				//row.put("approvalStatus", cdto.getApprovalStatus());
				row.put("uid", cdto.getId());
				rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = resp.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("getConProfile"))
		{
			System.out.println("getConProfile");
			ContractorDTO contrDTO = (new ContractorDao()).getConProfile(conDTO.getId());
			
			if(contrDTO!=null) {	
			
			JSONObject row = new JSONObject();			
			row.put("conID", contrDTO.getId());
			row.put("name", contrDTO.getName());
			row.put("firmName", contrDTO.getFirmName());
			row.put("firmReg", contrDTO.getFirmRegNo());
			row.put("permState", contrDTO.getState());
			row.put("permaddr", contrDTO.getAddress());
			row.put("permpin", contrDTO.getPin());
			row.put("pan", contrDTO.getPan());			
			row.put("mobno", contrDTO.getMobile());
			row.put("email", contrDTO.getEMail());	
			
			// System.out.println(row.toString());
			PrintWriter out = resp.getWriter();
			out.println(row.toString());
			}
			
		}
		
		else if (action.equalsIgnoreCase("resetConPass"))
		{
			System.out.println("resetConPass");
			int result = new ContractorDao().resetConPass(conDTO.getId(),req.getParameter("inpPwd"));
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();	
			if(result==1) {
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Password Reset Successful!');");
				out.println("location='/contractor/contractorProfile.jsp';");
				out.println("</script>");
				out.close();
			}
			else 
			{			
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Password Reset failed');");
				out.println("location='/contractor/contractorProfile.jsp';");
				out.println("</script>");
				out.close();
			}	
			
		}	
		else if (action.equalsIgnoreCase("countPending")) {
			AdministratorDTO adto = (AdministratorDTO)(req.getSession(false)).getAttribute("administrator");
			if(adto==null) {
				resp.sendRedirect("/clip.jsp");
				return;
			}
			int count = (new ContractorDao()).getPendingContractorsCount(adto,req.getParameter("adminLevel"));
			PrintWriter out = resp.getWriter();
			out.println(count);
		}
		else if (action.equalsIgnoreCase("resetContractorPassword")) {					
			int v = new ContractorDao().resetContractorPassword(Integer.parseInt(req.getParameter("id")),req.getParameter("newpassword"));			
			PrintWriter out = resp.getWriter();
			out.println(v);
		}
		else if (action.equalsIgnoreCase("getContDetails")) {			
			ContractorDTO contrDTO = (new ContractorDao()).getConProfile(req.getParameter("uid"));
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row = new JSONObject();			
			row.put("conID", contrDTO.getId());
			row.put("name", contrDTO.getName());
			row.put("firmName", contrDTO.getFirmName());
			row.put("firmReg", contrDTO.getFirmRegNo());
			row.put("permState", contrDTO.getState());
			row.put("permaddr", contrDTO.getAddress());
			row.put("permpin", contrDTO.getPin());
			row.put("pan", contrDTO.getPan());			
			row.put("mobno", contrDTO.getMobile());
			row.put("email", contrDTO.getEMail());			
			row.put("fname", contrDTO.getFName());
			row.put("ll", contrDTO.getLL());
			row.put("aadhar", contrDTO.getAadhar());
			row.put("pfno", contrDTO.getPF());
			row.put("woloi", contrDTO.getWoLoiNo());
			row.put("issauth", contrDTO.getIssAuth());
			row.put("status", contrDTO.getApprovalStatus());
                        row.put("irepsid", contrDTO.getIrepsId());
                        row.put("qualifiedLoaIssuer", contrDTO.getQualifiedLoaIssuer());
			rows.put(row);
			json.put("rows", rows);
			PrintWriter out = resp.getWriter();
			out.println(json.toString());			
			
		}

		else if(req.getParameter("action").equalsIgnoreCase("fetchWO"))
		{
			System.out.println("fetchletter");
			String wofile=req.getParameter("wofile");
			Connection con=null;
			PreparedStatement pst=null;
			ResultSet rs =null;
			try
			{
			 Blob image = null;  
			  byte[] imgData = null; 
			  con=DBConnection.getInstance().getDBConnection();
			  
			  String sql="Select FILEDATA,FILE_NAME from CONTRACT_WORKORDER_FILE where FILENO='"+wofile+"'";
			  System.out.println(sql);
							pst=con.prepareStatement(sql);
							System.out.println(sql);
							rs = pst.executeQuery();
						
							
				while(rs.next())
				 {

			        Blob bl = rs.getBlob("FILEDATA");
			        byte[] pict = bl.getBytes(1,(int)bl.length());			       
			        
			       resp.setContentType("application/pdf");
			       OutputStream o = resp.getOutputStream();
			        o.write(pict);
				    o.flush();    
			        o.close();
			        resp.flushBuffer();
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
		}
		
		else if (action.equalsIgnoreCase("checkWONO")) {	
			System.out.println("checkWONO");
			int v = new ContractorDao().checkWONO(req.getParameter("wono"));			
			PrintWriter out = resp.getWriter();
			out.println(v);
		}
                else if (action.equalsIgnoreCase("verifiedContractors")) {
                    AdministratorDTO adto = (AdministratorDTO)(req.getSession(false)).getAttribute("administrator");
                    if(adto==null) {                            
                            resp.sendRedirect("/clip.jsp");
                            return;
                    }
                    String GLOBAL_SEARCH_TERM = "";
                    String COLUMN_NAME;
                    String DIRECTION;
                    int INITIAL;
                    int RECORD_SIZE;
                    String[] columnNames = {"NAME", "FATHERNAME", "FIRMREGISTRATIONNO", "LANDLINE", "MOBILENO","EMAILID","PANNO","AADHAR","PFREGISTRATIONO","CREATIONDATE","APPROVALDATE","APPROVEDBY"};
                    JSONObject jsonResult = new JSONObject();
                    int listDisplayAmount = 10;
                    int start = 0;
                    int column = 0;
                    String dir = "asc";
                    String pageNo = req.getParameter("iDisplayStart");
                    String pageSize = req.getParameter("iDisplayLength");
                    String colIndex = req.getParameter("iSortCol_0");
                    String sortDirection = req.getParameter("sSortDir_0");
                    
                    if (pageNo != null) {
                        start = Integer.parseInt(pageNo);
                        if (start < 0) {
                        start = 0;
                        }
                    }
                    if (pageSize != null) {
                        listDisplayAmount = Integer.parseInt(pageSize);
                        if (listDisplayAmount < 10 || listDisplayAmount > 50) {
                            listDisplayAmount = 10;
                        }
                    }
                    if (colIndex != null) {
                        column = Integer.parseInt(colIndex);
                        if (column < 0 || column > 5)
                            column = 0;
                    }
                    if (sortDirection != null) {
                        if (!sortDirection.equals("asc"))
                            dir = "desc";
                    }
                    
                    String colName = columnNames[column];                                        
                    ArrayList al = (new ContractorDao()).getContractorsList(adto,"1");
                    int totalRecords = al.size();                    
                    RECORD_SIZE = listDisplayAmount;
                    
                    if(req.getParameter("sSearch")!=null)
                        GLOBAL_SEARCH_TERM = req.getParameter("sSearch").toLowerCase();
                    
                    COLUMN_NAME = colName;
                    DIRECTION = dir;
                    INITIAL = start;
                    Connection con=null;	
                    try {
                            int totalAfterSearch;
                            JSONArray array = new JSONArray();
                            String sql = "Select * from CONTRACTORREGISTRATION ";
                            String patch = "";
                            if(adto.getLevelId().equals("0")) {
				patch = " where approved='1' ";                                        
                            }
                            else {
                                patch = " where (( approved='1' and approvedby in (Select userid from ADMIN_USERS where ZONEID='"+adto.getZoneId()+"' ) )  "
                                        + "or ( id in (Select CONTRACTORID from CONTRACT_WORK_ORDER where APPROVED='1' and WOZONE="+adto.getZoneId()+") ) )";                                        
                            }
                            if (GLOBAL_SEARCH_TERM.equals("")) {
                                sql = sql + patch;
                            }
                            else {
                                sql = sql + patch + " and ( lower(NAME) like '%"+GLOBAL_SEARCH_TERM+"%' or lower(FIRMNAME) like '%"+GLOBAL_SEARCH_TERM+"%'"
                                    + " or lower(FIRMREGISTRATIONNO) like '%"+GLOBAL_SEARCH_TERM+"%' "
                                    + " or LANDLINE like '%"+GLOBAL_SEARCH_TERM+"%' or MOBILENO like '%"+GLOBAL_SEARCH_TERM+"%' or lower(EMAILID) like '%"+GLOBAL_SEARCH_TERM+"%'"
                                    + " or lower(PANNO) like '%"+GLOBAL_SEARCH_TERM+"%' or AADHAR like '%"+GLOBAL_SEARCH_TERM+"%'"
                                    + " or lower(PFREGISTRATIONO) like '%"+GLOBAL_SEARCH_TERM+"%'"
                                    + " or to_char(CREATIONDATE,'yyyy-dd-mm') like '%"+GLOBAL_SEARCH_TERM+"%' or to_char(APPROVALDATE,'yyyy-dd-mm') like '%"+GLOBAL_SEARCH_TERM+"%' "
                                    + " or lower(  concat(" +
                                        "      (SELECT short_desig" +
                                        "         FROM admin_designation" +
                                        "        WHERE id=" +
                                        "        (SELECT designation" +
                                        "           FROM admin_users" +
                                        "          WHERE userid=approvedby" +
                                        "        )" +
                                        "      ),concat('/',concat(" +
                                        "      (SELECT zone_code" +
                                        "         FROM contract_zone_master" +
                                        "        WHERE zone_id=" +
                                        "        (SELECT zoneid FROM admin_users WHERE userid=approvedby" +
                                        "        )" +
                                        "      ), concat('/',concat(" +
                                        "      (SELECT divcode" +
                                        "         FROM contract_div_master" +
                                        "        WHERE divid=" +
                                        "        (SELECT divid FROM admin_users WHERE userid=approvedby" +
                                        "        )" +
                                        "      ), concat('/',( SELECT deptname" +
                                        "         FROM department_master" +
                                        "        WHERE deptid=" +
                                        "        (SELECT deptid FROM admin_users WHERE userid=approvedby" +
                                        "        ) ) ) ) ) ) ) ) ) like '%"+GLOBAL_SEARCH_TERM+"%')";
                            }
                            sql += " order by " + COLUMN_NAME + " " + DIRECTION;
                            con=DBConnection.getInstance().getDBConnection();
                            PreparedStatement ps0 = con.prepareStatement("Select count(*) as count from ("+sql+")");
                            ResultSet rs0 = ps0.executeQuery();                            
                            if (GLOBAL_SEARCH_TERM.equals(""))
                                totalAfterSearch = totalRecords;
                            else {
                                rs0.next();
                                totalAfterSearch = rs0.getInt("count");
                            }
                            sql = "Select * from ( Select rownum rn, name,FIRMNAME,FIRMREGISTRATIONNO,landline,mobileno,emailid,panno,aadhar,PFREGISTRATIONO,"
                                    + "TO_CHAR(CREATIONDATE,'dd-mm-yyyy hh24:mi') AS CREATIONDATE,TO_CHAR(approvaldate,'dd-mm-yyyy hh24:mi') AS approvaldate,approvedby FROM " 
                                    + "  ("+sql+")) where rn between "+(INITIAL+1)+" and "+ (INITIAL+RECORD_SIZE);                            
                            System.out.println(sql);
                            
                            PreparedStatement ps = con.prepareStatement(sql);
                            ResultSet rs = ps.executeQuery();
                            while(rs.next()) {
				JSONArray ja = new JSONArray();				
				ja.put(rs.getString("NAME"));
                                ja.put(rs.getString("FIRMNAME"));
                                ja.put(rs.getString("FIRMREGISTRATIONNO"));
                                ja.put(rs.getString("LANDLINE"));
                                ja.put(rs.getString("MOBILENO"));
                                ja.put(rs.getString("EMAILID"));
                                ja.put(rs.getString("PANNO"));
                                ja.put(rs.getString("AADHAR"));
                                ja.put(rs.getString("PFREGISTRATIONO"));
                                ja.put(rs.getString("CREATIONDATE"));
                                ja.put(rs.getString("approvaldate"));
                                ja.put((new UtilityDao()).getAdminPostFromId(rs.getString("approvedby")));                                
                                array.put(ja);
                            }
                            //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ sEcho "+req.getParameter("sEcho"));
                            //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ totalRecords "+totalRecords);
                            //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ totalAfterSearch "+totalAfterSearch);
                            jsonResult.put("sEcho",Integer.parseInt(req.getParameter("sEcho")));
                            jsonResult.put("iTotalRecords", totalRecords);
                            jsonResult.put("iTotalDisplayRecords", totalAfterSearch);
                            jsonResult.put("aaData", array);                            
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
                    resp.setContentType("application/json");
                    resp.setHeader("Cache-Control", "no-store");
                    PrintWriter out = resp.getWriter();
                    out.print(jsonResult);
                    }
                else if (action.equalsIgnoreCase("changeLOAIssAuth")) {					
                    int v = new ContractorDao().changeLOAIssAuth(req.getParameter("zoneid"),req.getParameter("divid"),req.getParameter("deptid"),req.getParameter("desigid"),req.getParameter("uid"));			
                    PrintWriter out = resp.getWriter();
                    out.println(v);
		}
	}
 }
		
