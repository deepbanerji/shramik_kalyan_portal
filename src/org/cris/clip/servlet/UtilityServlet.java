package org.cris.clip.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.RequestDispatcher;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cris.clip.connection.DBConnection;
import org.cris.clip.dao.ContractorDao;
import org.cris.clip.dao.UtilityDao;
import org.cris.clip.dto.AdministratorDTO;
import org.cris.clip.dto.ContractorDTO;
import org.cris.clip.dto.WOLocationDTO;
import org.cris.clip.dto.WorkMenDTO;
import org.cris.clip.dto.WorkOrderDTO;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class UtilityServlet
 */
@WebServlet("/UtilityServlet")
public class UtilityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UtilityServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
		if(session==null) {
			RequestDispatcher rd = request.getRequestDispatcher("/clip.jsp");  
                    rd.include(request, response); 
                    return;
		}
		
		
		String action= request.getParameter("action");
		System.out.println("UtilityServlet --> "+action);
		HttpSession httpSession=request.getSession(false);
		ContractorDTO conDTO=(ContractorDTO) httpSession.getAttribute("contractor");
				
		
		if(action.equals("getZoneList"))
		{
			HashMap<Integer,String> map= new UtilityDao().zoneList();  
			
			PrintWriter out = response.getWriter();				
			JSONObject json = new JSONObject();
			String jsonText = "[";
			int i = 0;
			 
			 for(Entry<Integer, String> entry: map.entrySet()) 
			 {
			        System.out.println(entry.getKey() + " : " + entry.getValue());
			        if(i==0)
			        {
						json= json.put("option", entry.getKey());
						json.put("display", entry.getValue());
						jsonText = jsonText + json.toString();
			        }
			        else
			        {   
			        	json= json.put("option", entry.getKey());
						json.put("display", entry.getValue());
			        	jsonText = jsonText + "," + json.toString();
			        }
					i++;

			 }					    
			
			jsonText = jsonText + "]";
			out.println(jsonText);
		   //	System.out.println(jsonText);
			
		}
		
		else if(action.equals("getDivision"))
		{
			int zone= Integer.parseInt(request.getParameter("zone"));
			HashMap<Integer,String> map= new UtilityDao().divisionList(zone);  
			
			PrintWriter out = response.getWriter();				
			JSONObject json = new JSONObject();
			String jsonText = "[";
			int i = 0;
			 
			 for(Entry<Integer, String> entry: map.entrySet()) 
			 {
			        System.out.println(entry.getKey() + " : " + entry.getValue());
			        if(i==0)
			        {
						json= json.put("option", entry.getKey());
						json.put("display", entry.getValue());
						jsonText = jsonText + json.toString();
			        }
			        else
			        {   
			        	json= json.put("option", entry.getKey());
						json.put("display", entry.getValue());
			        	jsonText = jsonText + "," + json.toString();
			        }
					i++;

			 }					    
			
			jsonText = jsonText + "]";
			out.println(jsonText);
		   //	System.out.println(jsonText);
			
		}
		else if(action.equals("getDepartmentList"))		
		{
			System.out.println("getDepartmentList");
			
			int div= Integer.parseInt(request.getParameter("div"));
			HashMap<Integer,String> map= new UtilityDao().getDepartmentList(div);  
			
			PrintWriter out = response.getWriter();				
			
			JSONArray rows= new JSONArray(); 
			 for(Entry<Integer, String> entry: map.entrySet()) 
			 { 
				 JSONObject row = new JSONObject();
				 row.put("option", entry.getKey());
				 row.put("display", entry.getValue());
				 rows.put(row);
			 }
			 
			 out.println(rows);
			// System.out.println(rows); 			  
		
			
		}		
		else if(action.equals("getMinWage"))
		{
			int minWage=new UtilityDao().getMinWage(Integer.parseInt(request.getParameter("empType")),Integer.parseInt(request.getParameter("skillType")),Integer.parseInt(request.getParameter("woid")));
			
			PrintWriter out = response.getWriter();	
			out.println(minWage);
			
		}
		
		
		else if(action.equals("getWOListByDate"))
		{
			
			HashMap<Integer,String> map= new UtilityDao().getWOListByDate(request.getParameter("month"),request.getParameter("year"),conDTO.getId());
			PrintWriter out = response.getWriter();				
			JSONObject json = new JSONObject();
			String jsonText = "[";
			int i = 0;
			 
			 for(Entry<Integer, String> entry: map.entrySet()) 
			 {
			        System.out.println(entry.getKey() + " : " + entry.getValue());
			        if(i==0)
			        {
						json= json.put("option", entry.getKey());
						json.put("display", entry.getValue());
						jsonText = jsonText + json.toString();
			        }
			        else
			        {   
			        	json= json.put("option", entry.getKey());
						json.put("display", entry.getValue());
			        	jsonText = jsonText + "," + json.toString();
			        }
					i++;

			 }					    
			
			jsonText = jsonText + "]";
			out.println(jsonText);
		 //	System.out.println(jsonText);
			
			
		}
		else if (action.equalsIgnoreCase("getZones")) {
			HashMap<Integer,String> map= new UtilityDao().zoneList();			
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			for(Entry<Integer, String> entry: map.entrySet()) {				
				row = new JSONObject();
				row.put("value", entry.getKey());
				row.put("name", entry.getValue());				
				rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("getDivisions")) {
			int zone= Integer.parseInt(request.getParameter("zone"));
			HashMap<Integer,String> map= new UtilityDao().divisionList(zone);			
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			for(Entry<Integer, String> entry: map.entrySet()) {				
				row = new JSONObject();
				row.put("value", entry.getKey());
				row.put("name", entry.getValue());				
				rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("getDepartments")) {                    
                        AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			                     
                        int div= Integer.parseInt(request.getParameter("div"));
			HashMap<Integer,String> map= new UtilityDao().departmentList(div,adto);			
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			for(Entry<Integer, String> entry: map.entrySet()) {				
				row = new JSONObject();
				row.put("value", entry.getKey());
				row.put("name", entry.getValue());				
				rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("createUser")) {
			AdministratorDTO adto = new AdministratorDTO();
			adto.setLevelId(request.getParameter("selLevel"));
			adto.setZoneId(request.getParameter("selZone"));
                        adto.setMobile(request.getParameter("inpMobile"));
                        adto.setEmail(request.getParameter("inpEmail"));
                        adto.setDesigId(request.getParameter("selDesig"));
                        
                        if(request.getParameter("selDiv")==null || request.getParameter("selDiv").equals(""))
                            adto.setDivId(null);
                        else
                            adto.setDivId(request.getParameter("selDiv"));
                        
                        if(request.getParameter("selDept")==null || request.getParameter("selDept").equals(""))
                            adto.setDeptId(null);
                        else
                            adto.setDeptId(request.getParameter("selDept"));
                            
			adto.setUserId(request.getParameter("inpUserid").trim());
			adto.setPassword(request.getParameter("inpPwd").trim());
			if(request.getParameter("chkEnableUser")==null)
				adto.setActiveStatus("0");
			else
				adto.setActiveStatus("1");
			if(request.getParameter("chkApproveContr")==null)
				adto.setPrivContrApprove("0");
			else
				adto.setPrivContrApprove("1");
			if(request.getParameter("chkApproveWO")==null)
				adto.setPrivWoApprove("0");
			else
				adto.setPrivWoApprove("1");
			if(request.getParameter("chkCreateUser")==null)
				adto.setPrivUserCreate("0");
			else 
				adto.setPrivUserCreate(request.getParameter("selPriv"));
			AdministratorDTO a = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			int result = new UtilityDao().createUser(adto,a.getUserId());
			
                        response.setContentType("text/html");                        
                        PrintWriter pw = response.getWriter();
                        
			pw.println("<script type=\"text/javascript\">");
			if (result==1) {				
				pw.println("alert('User registered successfully!');");				
			}
			else {				
				pw.println("alert('An error occurred while registering user!');");				
			}
			pw.println("location='/Administrator/createUser.jsp';");
			pw.println("</script>");
			pw.close();
		}
		else if (action.equalsIgnoreCase("checkUserid")) {
			
			int v = new UtilityDao().checkUserid(request.getParameter("userid"));			
			PrintWriter out = response.getWriter();
			out.println(v);
		}
		else if (action.equalsIgnoreCase("listWOs")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {
                                
				response.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = new UtilityDao().listWOs(adto);
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				WorkOrderDTO cdto = (WorkOrderDTO)i.next();
				row = new JSONObject();
				row.put("woId", cdto.getId());
				row.put("woNo", cdto.getWO());
				row.put("workName", cdto.getWork_Name());
				row.put("prinEmp", cdto.getPrincEmp());
				row.put("approvalStatus", cdto.getApprovalStatus());
				row.put("contname", cdto.getContName());
				row.put("contmobile", cdto.getContMobileNo());
				row.put("contemail", cdto.getContEmailId());
				row.put("wofilenum", cdto.getWoFileNo());
                                row.put("zone", cdto.getWoZone());
                                row.put("div", cdto.getWoDiv());
                                row.put("dept", cdto.getWoDept());
                                row.put("approverPost", cdto.getApproverPost());
                                row.put("approvedby", cdto.getApproveBy());
                                row.put("approvaldate", cdto.getAppoveDt());
                                row.put("commdate", cdto.getCommDate().toString());
                                row.put("enddate", cdto.getEndDt().toString());
                                row.put("extenddate", cdto.getExtEndDt().toString());
                                row.put("verAppliedToPost", cdto.getVerAppliedToPost());
                                row.put("verAppliedToId", cdto.getVerAppliedToId());
				rows.put(row);
			}
			json.put("rows", rows);
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("approveWO")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			int retVal = (new UtilityDao()).approveWO(request.getParameter("wono"),request.getParameter("action"),adto,request.getParameter("uid"),
                                request.getParameter("mobile"),request.getParameter("email"),request.getParameter("value"));
			PrintWriter out = response.getWriter();
			out.println(retVal);
		}
		else if (action.equalsIgnoreCase("listWOBasedOnSelector")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {
                            
				response.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = new UtilityDao().listWOs(adto,request.getParameter("id"),request.getParameter("selector"),request.getParameter("extra"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				WorkOrderDTO cdto = (WorkOrderDTO)i.next();
				row = new JSONObject();
				row.put("woId", cdto.getId());
				row.put("woNo", cdto.getWO());
				//row.put("workName", cdto.getWork_Name());
				//row.put("prinEmp", cdto.getPrincEmp());
				//row.put("approvalStatus", cdto.getApprovalStatus());
				rows.put(row);
			}
			json.put("rows", rows);
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("listLabours")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {
                            
				response.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = new UtilityDao().listLabours(request.getParameter("woid"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				WorkMenDTO wdto = (WorkMenDTO)i.next();
				row = new JSONObject();
				row.put("id", wdto.getId());
				row.put("name", wdto.getName());
				row.put("icardtype", wdto.getIcardType());
				row.put("icardnumber", wdto.getIcardNo());
				row.put("sex", wdto.getSex());
                                row.put("wonum", wdto.getWoNum());
                                row.put("zone", wdto.getWoZone());
                                row.put("div", wdto.getWoDiv());
                                row.put("dept", wdto.getWoDept());
                                row.put("mobile", wdto.getMobno());
                                row.put("desig", wdto.getDesg());
                                row.put("email", wdto.getEmail());
                                row.put("dob", wdto.getDob());
				rows.put(row);
			}
			json.put("rows", rows);
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("woCountPending")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {
                            
				response.sendRedirect("/clip.jsp");
				return;
			}
			int count = (new UtilityDao()).getPendingWOCount(adto,request.getParameter("adminLevel"));
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(count);
		}
		else if (action.equalsIgnoreCase("divWiseContCount")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {
                            
				response.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = new UtilityDao().getDivWiseContCount(adto);
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				String a[] = (String[])i.next();
				row = new JSONObject();
				row.put("divcode", a[0]);
				row.put("count", a[1]);
				rows.put(row);
			}
			json.put("rows", rows);
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if(action.equals("getWOPeriod"))
		{
			System.out.println("getWOPeriod");
			String result=new UtilityDao().getWOPeriod(request.getParameter("woid"));
                        
			PrintWriter out = response.getWriter();
		  //	System.out.println(result);
			out.println(result);
		}
		else if(action.equals("getSkillType"))
		{
            HashMap<Integer,String> map= new UtilityDao().getSkillType(request.getParameter("employ"));			
			PrintWriter out = response.getWriter();				
			JSONObject json = new JSONObject();
			String jsonText = "[";
			int i = 0;
			 
			 for(Entry<Integer, String> entry: map.entrySet()) 
			 {
			        System.out.println(entry.getKey() + " : " + entry.getValue());
			        if(i==0)
			        {
						json= json.put("option", entry.getKey());
						json.put("display", entry.getValue());
						jsonText = jsonText + json.toString();
			        }
			        else
			        {   
			        	json= json.put("option", entry.getKey());
						json.put("display", entry.getValue());
			        	jsonText = jsonText + "," + json.toString();
			        }
					i++;

			 }					    
			
			jsonText = jsonText + "]";
			out.println(jsonText);
		//	System.out.println(jsonText);
			
		}
		else if (action.equalsIgnoreCase("dashboardTbl1")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {
                            
				response.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = new UtilityDao().getDashboardTbl1(adto,request.getParameter("adminLevel"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				String a[] = (String[])i.next();
				row = new JSONObject();
				row.put("wodiv", a[0]);
				row.put("wodept", a[1]);
				row.put("wo", a[2]);
				row.put("wmen", a[3]);
				row.put("wages", a[4]);
                                row.put("wozone", a[5]);
				rows.put(row);
			}
			json.put("rows", rows);
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("checkPAN")) {	
                        int v = new UtilityDao().checkPAN(request.getParameter("pan"),request.getParameter("contid"));	                        
			PrintWriter out = response.getWriter();
			out.println(v);
		}
		else if (action.equalsIgnoreCase("listLabourWages")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {
                            
				response.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = new UtilityDao().listLabourWages(request.getParameter("year"),request.getParameter("month"),request.getParameter("woid"),
                                request.getParameter("bankDepDt"),request.getParameter("pfDepDt"),request.getParameter("yearTo"),request.getParameter("monthTo"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				String a[] = (String[])i.next();
				row = new JSONObject();
				row.put("id", a[0]);
				row.put("name", a[1]);
				row.put("startdate", a[2]);
				row.put("termdate", a[3]);
				row.put("pan", a[4]);
				row.put("aadhar", a[5]);
				row.put("netamount", a[6]);
                                row.put("wonum", a[7]);
                                row.put("zone", a[8]);
                                row.put("div", a[9]);
                                row.put("dept", a[10]);
                                row.put("attendance", a[11]);
                                row.put("deduction", a[12]);
                                row.put("month", a[13]);
                                row.put("year", a[14]);
				rows.put(row);
			}
			json.put("rows", rows);
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("resetSubAdminPassword")) {					
			int v = new UtilityDao().resetSubAdminPassword(request.getParameter("id"),request.getParameter("newpassword"));			
			PrintWriter out = response.getWriter();
			out.println(v);
		}
		else if (action.equalsIgnoreCase("changeAdminPassword")) {					
			int v = new UtilityDao().changeAdminPassword(request.getParameter("id"),request.getParameter("newpassword"));			
			PrintWriter out = response.getWriter();
			out.println(v);
		}
		else if (action.equalsIgnoreCase("dashboardTbl2")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {
                            
				response.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = new UtilityDao().getDashboardTbl2(adto,request.getParameter("adminLevel"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				String a[] = (String[])i.next();
				row = new JSONObject();
				row.put("div", a[0]);
				row.put("dept", a[1]);
				row.put("regd", a[2]);
				row.put("approved", a[3]);
				row.put("pending", a[4]);
				row.put("rejected", a[5]);
                                row.put("zone", a[6]);
				rows.put(row);
			}
			json.put("rows", rows);
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		else if (action.equalsIgnoreCase("download")) {			
		    Connection con=null;
		    try {
		    	con=DBConnection.getInstance().getDBConnection();
				String sql="Select * from CONTRACT_WORKORDER_FILE where FILENO="+request.getParameter("id");
				PreparedStatement pst=con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {
					String fileName = rs.getString("FILE_NAME");
		            Blob blob = rs.getBlob("FILEDATA");
		            InputStream inputStream = blob.getBinaryStream();
		            int fileLength = (int)blob.length();
		            
		            ServletContext context = getServletContext();		            
		            String mimeType = context.getMimeType(fileName);
			        if (mimeType == null) {        
			            mimeType = "application/octet-stream";
			        }
			        response.setContentType(mimeType);
			        response.setContentLength(fileLength);
			         
			        String headerKey = "Content-Disposition";
			        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
			        response.setHeader(headerKey, headerValue);
			         
			        OutputStream outStream = response.getOutputStream();			         
			        byte[] buffer = new byte[4096];
			        int bytesRead = -1;
			         
			        while ((bytesRead = inputStream.read(buffer)) != -1) {
			            outStream.write(buffer, 0, bytesRead);
			        }
			         
			        inputStream.close();
			        outStream.close(); 			        
				}
				else {	 
                                        response.setContentType("text/html");
					PrintWriter out = response.getWriter();	
					out.println("<script type=\"text/javascript\">");
					out.println("alert('File not found!);");
					out.println("</script>");
					out.close();
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
		}
                else if (action.equalsIgnoreCase("publicDashboardTbl1")) {
			ArrayList al = new UtilityDao().getPublicDashboardTbl1();
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				String a[] = (String[])i.next();
				row = new JSONObject();
                                row.put("wozone", a[0]);
				row.put("wodiv", a[1]);
				row.put("wodept", a[2]);
				row.put("wo", a[3]);
				row.put("wmen", a[4]);
				row.put("wages", a[5]);
				rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
                else if (action.equalsIgnoreCase("publicDashboardTbl2")) {
			ArrayList al = new UtilityDao().getPublicDashboardTbl2();
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				String a[] = (String[])i.next();
				row = new JSONObject();
                                row.put("zone", a[0]);
				row.put("div", a[1]);
				row.put("dept", a[2]);
				row.put("regd", a[3]);
				row.put("approved", a[4]);
				row.put("pending", a[5]);
				row.put("rejected", a[6]);
				rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
                else if (action.equalsIgnoreCase("publicDashboardTbl3")) {
			ArrayList al = new UtilityDao().getPublicDashboardTbl3(request.getParameter("month"),request.getParameter("year"),request.getParameter("zoneid"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
                            String a[] = (String[])i.next();
                            row = new JSONObject();
                            row.put("wname", a[0]);
                            row.put("contname", a[1]);
                            row.put("contfirmname", a[2]);
                            row.put("empcount", a[3]);
                            row.put("totwages", a[4]);
                            row.put("avgsalary", a[5]);
                            row.put("zone", a[6]);
                            row.put("div", a[7]);
                            row.put("dept", a[8]);
                            row.put("workdays", a[9]);
                            row.put("wonum", a[10]);
                            row.put("woid", a[11]);
                            rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
                else if (action.equalsIgnoreCase("getPublicDashboardWorkerList")) {
			ArrayList al = new UtilityDao().getPublicDashboardWorkerList(request.getParameter("month"),request.getParameter("year"),request.getParameter("zoneid"),request.getParameter("woid"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
                            String a[] = (String[])i.next();
                            row = new JSONObject();
                            row.put("workerid", a[0]);
                            row.put("workername", a[1]);
                            row.put("workerphotono", a[2]);
                            rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		
                else if (action.equalsIgnoreCase("checkWMPan"))
                {
                	System.out.println(request.getParameter("pan"));
                	int result=new UtilityDao().checkWMPan(request.getParameter("pan").toUpperCase());
                	System.out.println("result"+result);
                	PrintWriter out = response.getWriter();
        			out.println(result);
                	
                }
		
                else if (action.equalsIgnoreCase("checkWMAdhar"))
                {
                	System.out.println(request.getParameter("adhar"));
                	int result=new UtilityDao().checkWMAdhar(request.getParameter("adhar"));
                	System.out.println("result"+result);
                	PrintWriter out = response.getWriter();
        			out.println(result);
                	
                }
                else if (action.equalsIgnoreCase("enableDisableAdmin")) {
                        AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                                
				response.sendRedirect("/clip.jsp");
				return;
			}
			int v = new UtilityDao().enableDisableAdmin(request.getParameter("id"),request.getParameter("status"));			
			PrintWriter out = response.getWriter();
			out.println(v);
		}
                
                else if (action.equalsIgnoreCase("checkMobile")) {					
			int v  = new UtilityDao().checkMobile(request.getParameter("mobile"),request.getParameter("contid"));                        
                        PrintWriter out = response.getWriter();
			out.println(v);
		}
                else if (action.equalsIgnoreCase("checkAadhar")) {					
			int v = new UtilityDao().checkAadhar(request.getParameter("aadhar"),request.getParameter("contid"));	                        
			PrintWriter out = response.getWriter();
			out.println(v);
		}
                else if (action.equalsIgnoreCase("checkFirmRegNo")) {					
			int v = new UtilityDao().checkFirmRegNo(request.getParameter("firmregno"));                        
			PrintWriter out = response.getWriter();
			out.println(v);
		}
                else if (action.equalsIgnoreCase("checkPFNo")) {					
			int v = new UtilityDao().checkPFNo(request.getParameter("pfno"),request.getParameter("contid"));	                        
			PrintWriter out = response.getWriter();
			out.println(v);
		}
                else if (action.equalsIgnoreCase("checkEmail")) {					
			int v = new UtilityDao().checkEmail(request.getParameter("email"),request.getParameter("contid"));                        
			PrintWriter out = response.getWriter();
			out.println(v);
		}
                else if (action.equalsIgnoreCase("checkWOLOI")) {					
			int v = new UtilityDao().checkWOLOI(request.getParameter("woloi"));	                        
			PrintWriter out = response.getWriter();
			out.println(v);
		}
                else if (action.equalsIgnoreCase("zonalContractorCount")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                            
				response.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = new UtilityDao().getZonalContCount(adto,request.getParameter("adminLevel"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
				String a[] = (String[])i.next();
				row = new JSONObject();
				row.put("zoneCode", a[0]);
				row.put("zoneCount", a[1]);
				rows.put(row);
			}
			json.put("rows", rows);
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
		
                else if (action.equalsIgnoreCase("checkWMMobile")) {
                //	System.out.println(request.getParameter("mobile"));
                	int result=new UtilityDao().checkWMMobile(request.getParameter("mobile"),request.getParameter("wmid"));
                //	System.out.println("result"+result);
                	PrintWriter out = response.getWriter();
        			out.println(result);
                	
                }
                else if (action.equalsIgnoreCase("checkWMPf")) {
                	System.out.println(request.getParameter("pfno"));
                	int result=new UtilityDao().checkWMPf(request.getParameter("pfno"));
                	System.out.println("result"+result);
                	PrintWriter out = response.getWriter();
        			out.println(result);
                	
                }
		
                else if(action.equals("getMinWageByDt")) {
                    System.out.println("startdt"+request.getParameter("startDt"));
                    int minWage=new UtilityDao().getMinWageByDt(Integer.parseInt(request.getParameter("empType")),Integer.parseInt(request.getParameter("skillType")),
                    		request.getParameter("startDt"),request.getParameter("woarea"));

                    PrintWriter out = response.getWriter();	
                    out.println(minWage);

            } 
                else if(action.equals("getMinWageProfile")) {
                    System.out.println("woloc"+request.getParameter("woloc"));
                    int minWage=new UtilityDao().getMinWageProfile(request.getParameter("startDt"),request.getParameter("wmid"),request.getParameter("woloc"));

                    PrintWriter out = response.getWriter();	
                    out.println(minWage);

            }
                else if (action.equalsIgnoreCase("modifyCreateAdminPriv")) {
                        AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                                
				response.sendRedirect("/clip.jsp");
				return;
			}
			int v = new UtilityDao().modifyCreateAdminPriv(request.getParameter("id"),request.getParameter("status"));			
			PrintWriter out = response.getWriter();
			out.println(v);
		}
        	else if (action.equalsIgnoreCase("modifyContApprPriv")) {
                        AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                                
				response.sendRedirect("/clip.jsp");
				return;
			}
			int v = new UtilityDao().modifyContApprPriv(request.getParameter("id"),request.getParameter("status"));			
			PrintWriter out = response.getWriter();
			out.println(v);
		}
                else if (action.equalsIgnoreCase("modifyWOApprPriv")) {
                        AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                                
				response.sendRedirect("/clip.jsp");
				return;
			}
			int v = new UtilityDao().modifyWOApprPriv(request.getParameter("id"),request.getParameter("status"));			
			PrintWriter out = response.getWriter();
			out.println(v);
		}
                else if (action.equalsIgnoreCase("getWageRows")) {
                        AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                                
				response.sendRedirect("/clip.jsp");
				return;
			}
			ArrayList al = new UtilityDao().getWageRows(request.getParameter("period"));
			Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject row;
			while(i.hasNext()) {
                            String a[] = (String[])i.next();
                            row = new JSONObject();
                            row.put("woarea", a[0]);
                            row.put("employment_type_id", a[1]);
                            row.put("skill_type_id", a[2]);
                            row.put("minwage", a[3]);
                            row.put("employment_type", a[4]);
                            row.put("skill_type", a[5]);
                            row.put("validfrom", a[6]);
                            row.put("validto", a[7]);                            
                            rows.put(row);
			}
			json.put("rows", rows);
			PrintWriter out = response.getWriter();
			out.println(json.toString());
		}
                else if (action.equalsIgnoreCase("savenewwage")) {
                        AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                                
				response.sendRedirect("/clip.jsp");
				return;
			}
			int rows = Integer.parseInt(request.getParameter("hRowCount"));
                        ArrayList al = new ArrayList();
                        int result = 0;
                        for(int i = 0;i<rows;++i) {
                            String AreaEmpidSkill = request.getParameter("hw"+i);
                            String wage = request.getParameter(AreaEmpidSkill);
                            al.add(AreaEmpidSkill+"."+wage);
                            //System.out.println(rows+"  "+AreaEmpidSkill+"."+wage);                            
                        }
			result = new UtilityDao().saveNewWages(al,request.getParameter("hPeriod"));                        
                        response.setContentType("text/html");
                        PrintWriter out = response.getWriter();	
                        out.println("<script type=\"text/javascript\">");
                        if(result==al.size())
                            out.println("alert('Wage details saved!');");
                        else
                            out.println("alert('Failed to save wage details!');");
                        out.println("location = '/Administrator/newminwages.jsp';");
                        out.println("</script>");
                        out.close();
		}
                else if (action.equalsIgnoreCase("editWage")) {	
                        AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                                
				response.sendRedirect("/clip.jsp");
				return;
			}
                        int result = new UtilityDao().editWage(request.getParameter("period"),request.getParameter("id"),request.getParameter("newval"));
                        PrintWriter out = response.getWriter();	
                        out.println(result);
                        out.close();
		}
                else if (action.equalsIgnoreCase("regGrievance")) {	
                    String usertype = request.getParameter("user_type");
                    String userid = request.getParameter("user_id");
                    String probDesc = request.getParameter("probDesc");
                    String wo = request.getParameter("wo");
                    if(wo.equals(""))
                        wo = null;
                    String fwdTo = request.getParameter("selFwdTo");
                    int result = new UtilityDao().regGrievance(usertype,probDesc,wo,fwdTo,userid);
                    PrintWriter out = response.getWriter();	
                    out.println(result);
                    out.close();
                }
                else if (action.equalsIgnoreCase("getGrievanceList")) {	
                    String usertype = request.getParameter("user_type");
                    String userid = request.getParameter("user_id");
                    AdministratorDTO adto;
                    if(usertype.equals("a")) {
                        adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                                
				response.sendRedirect("/clip.jsp");
				return;
			}
                    }
                    else 
                        adto = null;
                    ArrayList al = new UtilityDao().getGrievances(usertype,userid,adto);
                    Iterator i = al.iterator();
                    JSONObject json = new JSONObject();
                    JSONArray rows = new JSONArray();
                    JSONObject row;
                    while(i.hasNext()) {
                        String a[] = (String[])i.next();
                        row = new JSONObject();
                        row.put("gid", a[0]);
                        row.put("regd_by_id", a[1]);
                        row.put("regd_on", a[2]);
                        row.put("desc", a[3]);
                        row.put("wono", a[4]);
                        row.put("status", a[5]); 
                        row.put("regd_by_type", a[6]);
                        row.put("contname", a[7]);
                        row.put("contfirmname", a[8]);
                        row.put("admindesig", a[9]);
                        row.put("adminzone", a[10]);
                        row.put("admindiv", a[11]);
                        row.put("admindept", a[12]);
                        row.put("woid", a[13]);
                        row.put("wofile", a[14]);
                        row.put("updtTime", a[15]);
                        row.put("fwdtoid", a[16]);
                        row.put("fwdtotype", a[17]);
                        row.put("contpan", a[18]);
                        rows.put(row);
                    }
                    json.put("rows", rows);
                    PrintWriter out = response.getWriter();
                    out.println(json.toString());
                }
                else if (action.equalsIgnoreCase("getDesigList")) {	
                    String levelid = request.getParameter("levelid");
                    String deptid = request.getParameter("deptid");
                    String divid = request.getParameter("divid");
                    String zoneid = request.getParameter("zoneid");
                    
                    String divorhq = "";
                    if(!divid.equals(""))
                        divorhq = new UtilityDao().checkdivhq(divid);
                    
                    ArrayList al = new UtilityDao().getDesigList(levelid,deptid,divorhq,zoneid,divid);
                    Iterator i = al.iterator();
                    JSONObject json = new JSONObject();
                    JSONArray rows = new JSONArray();
                    JSONObject row;
                    while(i.hasNext()) {
                        String a[] = (String[])i.next();
                        row = new JSONObject();
                        row.put("shortdesig", a[1]);
                        row.put("desigid", a[0]);
                        rows.put(row);
                    }
                    json.put("rows", rows);
                    PrintWriter out = response.getWriter();
                    out.println(json.toString());
                }
                else if (action.equalsIgnoreCase("getDesigList1")) {
                    String zoneid = request.getParameter("zoneid");
                    String divid = request.getParameter("divid");
                    String deptid = request.getParameter("deptid");
                    
                    String divorhq = "";
                    if(!divid.equals(""))
                        divorhq = new UtilityDao().checkdivhq(divid);
                    
                    ArrayList al = new UtilityDao().getDesigList1(zoneid,divid,deptid,divorhq);
                    Iterator i = al.iterator();
                    JSONObject json = new JSONObject();
                    JSONArray rows = new JSONArray();
                    JSONObject row;
                    while(i.hasNext()) {
                        String a[] = (String[])i.next();
                        row = new JSONObject();
                        row.put("shortdesig", a[1]);
                        row.put("authid", a[0]);
                        rows.put(row);
                    }
                    json.put("rows", rows);
                    PrintWriter out = response.getWriter();
                    out.println(json.toString());
                }
                else if (action.equalsIgnoreCase("checkdivhq")) {                    
                    String divid = request.getParameter("divid");
                    String r = new UtilityDao().checkdivhq(divid);
                    PrintWriter out = response.getWriter();
                    out.print(r);
                }
                 else if (action.equalsIgnoreCase("updateProblem")) {                    
                    String pid = request.getParameter("pid");
                    String probUpdate = request.getParameter("probUpdate");
                    String userid = request.getParameter("userid");
                    String usertype = request.getParameter("usertype");
                    String userdesigid = request.getParameter("userdesigid");
                    String actiontype = request.getParameter("actiontype");
                    String actionbyid = request.getParameter("actionById");
                    String actionbytype = request.getParameter("actionByType");
                    String rlyAuth = request.getParameter("rlyAuth");
                    String classifier = request.getParameter("classifier");
                    int r = new UtilityDao().updateProb(pid,probUpdate,userid,usertype,actiontype,userdesigid,rlyAuth,classifier);
                    PrintWriter out = response.getWriter();
                    out.print(r);
                }
                else if (action.equalsIgnoreCase("probHist")) {	
                    String usertype = request.getParameter("usertype");
                    String userid = request.getParameter("userid");
                    String pid = request.getParameter("pid");
                    ArrayList al = new UtilityDao().getProbHist(pid,usertype,userid);
                    Iterator i = al.iterator();
                    JSONObject json = new JSONObject();
                    JSONArray rows = new JSONArray();
                    JSONObject row;
                    while(i.hasNext()) {
                        String a[] = (String[])i.next();
                        row = new JSONObject();
                        row.put("gid", a[0]);
                        row.put("regd_by_id", a[1]);
                        row.put("regd_by_type", a[2]);
                        row.put("desc", a[3]);
                        row.put("regntime", a[4]);
                        row.put("woid", a[5]); 
                        row.put("wonum", a[6]);
                        row.put("zoneid", a[7]);
                        row.put("divid", a[8]);
                        row.put("deptid", a[9]);
                        row.put("zone", a[10]);
                        row.put("div", a[11]);
                        row.put("dept", a[12]);
                        row.put("action_type", a[13]);
                        row.put("action_by_id", a[14]);
                        row.put("action_by_type", a[15]);
                        row.put("contname", a[16]);
                        row.put("contfirmname", a[17]);
                        row.put("step", a[18]);
                        row.put("admindesig", a[19]);
                        row.put("adminzone", a[20]);
                        row.put("admindiv", a[21]);
                        row.put("admindept", a[22]);
                        row.put("wofilenum", a[23]);
                        row.put("fwdtoid", a[24]);
                        row.put("fwdtotype", a[25]);
                        row.put("status", a[26]);
                        row.put("fwdcontname", a[27]);
                        row.put("fwdcontfirmname", a[28]);
                        row.put("fwdadminpost", a[29]);
                        row.put("contpan", a[30]);
                        rows.put(row);
                    }
                    json.put("rows", rows);
                    PrintWriter out = response.getWriter();
                    out.println(json.toString());
                }
		
                else if(action.equals("getWoLocation"))
                {
                 //  System.out.println("getWoLocation");	
                  
                  HashMap<Integer,String> map= new UtilityDao().getWoLocation();  
      			
      			  PrintWriter out = response.getWriter();				
      			
      			 JSONArray rows= new JSONArray(); 
      			 for(Entry<Integer, String> entry: map.entrySet()) 
      			 { 
      				 JSONObject row = new JSONObject();
      				 row.put("option", entry.getKey());
      				 row.put("display", entry.getValue());
      				 rows.put(row);
      			 }
      			 
      			 out.println(rows);
      			// System.out.println(rows); 			  
                	
                }
                else if (action.equalsIgnoreCase("checkIrepsId")) {					
			int v  = new UtilityDao().checkIrepsId(request.getParameter("irepsid"));                        
                        PrintWriter out = response.getWriter();
			out.println(v);
		       }
		
                else if(action.equals("getLocForWM"))
                {
                 	System.out.println("getLocForWM");
                	String woid=request.getParameter("woid");
                	ArrayList arl= new UtilityDao().getLocForWM(woid);  
          			
        			PrintWriter out = response.getWriter();		
        			JSONArray rows= new JSONArray(); 
        			for(int i=0;i<arl.size();i++)
        			{
        				WOLocationDTO wldto=(WOLocationDTO) arl.get(i);
        				JSONObject row = new JSONObject();
       				    row.put("option", wldto.getLocid());
       				    row.put("display", wldto.getLocother());
       				    rows.put(row);
        				
        				
        			}		
        			
        			 
        			/* for(Entry<Integer, String> entry: map.entrySet()) 
        			 { 
        				 System.out.println(entry.getValue());
        				 JSONObject row = new JSONObject();
        				 row.put("option", entry.getKey());
        				 row.put("display", entry.getValue());
        				 rows.put(row);
        			 }*/
        			 
        			 out.println(rows);
        			 System.out.println(rows); 	
                	
                }	
		
                else if(action.equals("getMinWageByLoc")) {
                    System.out.println("startdt"+request.getParameter("startDt"));
                    int minWage=new UtilityDao().getMinWageByLoc(Integer.parseInt(request.getParameter("empType")),Integer.parseInt(request.getParameter("skillType")),
                    		request.getParameter("startDt"),request.getParameter("woloc"));

                    PrintWriter out = response.getWriter();	
                    out.println(minWage);

                  } 
		
                else if(action.equals("getWoState"))
        		{
        			HashMap<Integer,String> map= new UtilityDao().getWoState();  
        			
        			PrintWriter out = response.getWriter();				
        			JSONObject json = new JSONObject();
        			String jsonText = "[";
        			int i = 0;
        			 
        			 for(Entry<Integer, String> entry: map.entrySet()) 
        			 {
        			        System.out.println(entry.getKey() + " : " + entry.getValue());
        			        if(i==0)
        			        {
        						json= json.put("option", entry.getKey());
        						json.put("display", entry.getValue());
        						jsonText = jsonText + json.toString();
        			        }
        			        else
        			        {   
        			        	json= json.put("option", entry.getKey());
        						json.put("display", entry.getValue());
        			        	jsonText = jsonText + "," + json.toString();
        			        }
        					i++;

        			 }					    
        			
        			jsonText = jsonText + "]";
        			out.println(jsonText);
        		   //	System.out.println(jsonText);
        			
        		}
        		
                else if(action.equals("getWoDistrict"))
        		{
        			
                	
                	HashMap<Integer,String> map= new UtilityDao().getWoDistrict(request.getParameter("stateid"));  
        			
        			PrintWriter out = response.getWriter();				
        			JSONObject json = new JSONObject();
        			String jsonText = "[";
        			int i = 0;
        			 
        			 for(Entry<Integer, String> entry: map.entrySet()) 
        			 {
        			        System.out.println(entry.getKey() + " : " + entry.getValue());
        			        if(i==0)
        			        {
        						json= json.put("option", entry.getKey());
        						json.put("display", entry.getValue());
        						jsonText = jsonText + json.toString();
        			        }
        			        else
        			        {   
        			        	json= json.put("option", entry.getKey());
        						json.put("display", entry.getValue());
        			        	jsonText = jsonText + "," + json.toString();
        			        }
        					i++;

        			 }					    
        			
        			jsonText = jsonText + "]";
        			out.println(jsonText);
        		   //	System.out.println(jsonText);
        			
        		}
		
                else if(action.equals("checkOthloc"))
                {
                	int result=0;
                    System.out.println("Othloc"+request.getParameter("locoth"));
                    result=new UtilityDao().checkOthloc(request.getParameter("locoth")); 
                    PrintWriter out = response.getWriter();		
                    out.print(result);
                    
                    
                } 
		else if (action.equalsIgnoreCase("newFdbkCount")) {
			AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
			if(adto==null) {                            
				response.sendRedirect("/clip.jsp");
				return;
			}
			int count = (new UtilityDao()).getPendingFdbkCount(adto);
                        response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(count);
		}
		
		 else if(action.equals("getWoMonth"))
         {
         	String result="",months="",pendmonth="";
             System.out.println("wo"+request.getParameter("wo"));
             months=new UtilityDao().getWoMonth(request.getParameter("wo")); 
             System.out.println(months);
             
             String month[]=months.split(",");
             
           //  System.out.println(month[0]);
             
             if(month[0].equals("null"))
   			{	
             	month[0]="";
   				pendmonth=new UtilityDao().getPendingMonth(request.getParameter("wo"),request.getParameter("yr"),request.getParameter("mon"),month[2],month[3]); 			
   			}    			
   			else
   			{
   			   
   			   String[] parts = month[1].split("-");   
   			  // System.out.println(parts[0]+parts[1]);
   				 pendmonth=new UtilityDao().getPendingMonth(request.getParameter("wo"),request.getParameter("yr"),request.getParameter("mon"),parts[0],parts[1]);      				
   			}
             
             result=pendmonth+","+month[0];
             		
             System.out.println(result);
             PrintWriter out = response.getWriter();		
             out.print(result);
             
             
         } 
	
         else if(action.equals("getWOLastMonth"))
         {
         	String result="",months="";
             System.out.println("wo"+request.getParameter("wo"));
             months=new UtilityDao().getWoMonth(request.getParameter("wo")); 
             String month[]=months.split(",");
            
             result=month[1]+","+new UtilityDao().getWOPeriod(request.getParameter("wo"));
             		
            // System.out.println(result);
             PrintWriter out = response.getWriter();		
             out.print(result);            
             
         } 
	else if (action.equalsIgnoreCase("extendLoaDate")) {
                AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
                int retVal = (new UtilityDao()).extLoaDate(request.getParameter("uid"),request.getParameter("newdate"),adto.getUserId());
                PrintWriter out = response.getWriter();
                out.println(retVal);
        }	
        else if (action.equalsIgnoreCase("servertime")) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String dateString = format.format( new Date() );
                PrintWriter out = response.getWriter();
                out.println(dateString);
        }
        else if (action.equalsIgnoreCase("CheckVisitor")) {	
                int v = new UtilityDao().CheckVisitor();	                        
                PrintWriter out = response.getWriter();
                out.println(v);
        }
        }
	
}
