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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.cris.clip.connection.DBConnection;
import org.cris.clip.dao.ContractorDao;
import org.cris.clip.dao.LoginDao;
import org.cris.clip.dao.WorkMen;
import org.cris.clip.dto.ContractorDTO;
import org.cris.clip.dto.WageDTO;
import org.cris.clip.dto.WageReportDTO;
import org.cris.clip.dto.WorkMenDTO;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class WorkMenServlet
 */

public class WorkMenServlet extends HttpServlet implements Servlet  {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WorkMenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	 //	doGet(request, response);
		System.out.println("WorkMenServlet");
		
		HttpSession httpSession=request.getSession(false);
		if(httpSession==null)
		{
			System.out.println("Session out");
			response.sendRedirect("/clip.jsp");	
			
		}
		
		ContractorDTO conDTO=(ContractorDTO) httpSession.getAttribute("contractor");
		
		String action=request.getParameter("action");
		if(action.equals("AddWorkman"))
		{
			int result=0;
			System.out.println("AddWorkman");
			WorkMenDTO wmDTO=new WorkMenDTO();
			wmDTO.setName(request.getParameter("name").toUpperCase());
			wmDTO.setContractorID(Integer.parseInt(conDTO.getId()));
			wmDTO.setWoid(Integer.parseInt(request.getParameter("WONo")));
			wmDTO.setFname(request.getParameter("fname").toUpperCase());
			wmDTO.setDob(request.getParameter("dob"));
			wmDTO.setSex(request.getParameter("sex"));
			wmDTO.setPermAddr(request.getParameter("address").toUpperCase());
			wmDTO.setPermPin(request.getParameter("pincode").trim());
			wmDTO.setMobno(request.getParameter("mobno").trim());
			wmDTO.setEmail(request.getParameter("email").trim());
			wmDTO.setDesg(request.getParameter("desg").toUpperCase());
			wmDTO.setEmplType(Integer.parseInt(request.getParameter("empltype")));
			wmDTO.setSkillType(Integer.parseInt(request.getParameter("skillType")));
			wmDTO.setPan(request.getParameter("pan").trim().toUpperCase());
			wmDTO.setAdhar(request.getParameter("adhar").trim());
			wmDTO.setBankname(request.getParameter("bank").toUpperCase());
			wmDTO.setBankno(request.getParameter("bankaccount").trim());			
			wmDTO.setPenallow(request.getParameter("pension"));
			wmDTO.setPfType(request.getParameter("pftype"));
			wmDTO.setPfno(request.getParameter("pfno").trim());
			wmDTO.setPfallow(request.getParameter("pf"));		
			wmDTO.setPenPercent(Integer.parseInt(request.getParameter("pnpercent")));
			wmDTO.setPfPercent(Integer.parseInt(request.getParameter("pfpercent")));		
			wmDTO.setEduQual(request.getParameter("qualification").toUpperCase());
			wmDTO.setStartDt(request.getParameter("StartDt"));
			wmDTO.setTermDt(request.getParameter("TermDt"));
			wmDTO.setWageRate(Integer.parseInt(request.getParameter("BasicWage")));
			wmDTO.setIcardType(request.getParameter("icardType"));
			wmDTO.setIcardNo(request.getParameter("icardNo").trim().toUpperCase());		
		//	System.out.println("ICARDNO:"+wmDTO.getIcardNo());
			wmDTO.setBonusallow(request.getParameter("bonus"));
			wmDTO.setBonuspercent(Integer.parseInt(request.getParameter("bonuspercent")));
			wmDTO.setIncrallow(request.getParameter("increment"));
			wmDTO.setIncrpercent(Integer.parseInt(request.getParameter("incrpercent")));
		//	wmDTO.setWoArea(request.getParameter("WOAREA"));	
			wmDTO.setLocid(request.getParameter("WOLOC"));
			wmDTO.setLocoth(request.getParameter("loctxt"));
			wmDTO.setContFirmName(conDTO.getFirmName());
			WorkMen workmen=new WorkMen();
			result=workmen.addWorkMan(wmDTO);
                        response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			if(result==0)
			{	
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Operation Failed!!!');");
				out.println("location='/WorkMen/WorkMenDetails.jsp';");
				out.println("</script>");			
			
			}
			else
			{
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Work Man Added Successfully');");
				out.println("location='/WorkMen/WorkMenDetails.jsp';");
				out.println("</script>");
				
			}		
			
		   //	response.sendRedirect("/WorkMen/WorkMenDetails.jsp");
			
		}
		
		
		else if(action.equals("workMenAllocate"))
		{
			System.out.println("workMenAllocate");
			
			String WOrderId=request.getParameter("WONo");
			
			String[] WMenIds=request.getParameterValues("workMenChk");
		  /*  for(int i=0;i<WMenIds.length;i++)
		    {
		    	System.out.println(WMenIds[i]);
		    }	*/
			
		    WorkMen workmen=new WorkMen();
			workmen.workMenAllocate(WOrderId,WMenIds); 
			
			response.sendRedirect("/contractor/WManAlloc.jsp");
		    
		}	
		
		else if(action.equals("addWages"))
		{
			System.out.println("addWages");
			ArrayList arl = new ArrayList();			
			int result = 0;
			String[] WMenIds=request.getParameterValues("WMenId");
			 for(int i=0;i<WMenIds.length;i++)
			  {
				 WageDTO wgDTO = new WageDTO();
				 
				 wgDTO.setMonth(request.getParameter("month"));
				 wgDTO.setYear(request.getParameter("year"));
				 wgDTO.setWOrderId(Integer.parseInt(request.getParameter("WONo")));
			//	 wgDTO.setMonth(request.getParameter("monid"));
			//	 wgDTO.setYear(request.getParameter("yrid"));
			//	 wgDTO.setWOrderId(Integer.parseInt(request.getParameter("woid")));
				 wgDTO.setWorkmanId(Integer.parseInt(request.getParameterValues("WMenId")[i]));
				 wgDTO.setWorkstartdt(request.getParameterValues("WorkStart")[i]);
				 wgDTO.setContId(Integer.parseInt(request.getParameterValues("ContID")[i]));	
				 wgDTO.setWageRate(Integer.parseInt(request.getParameterValues("WageRate")[i]));	
				 wgDTO.setAttendance(Integer.parseInt(request.getParameterValues("PsnDay")[i]));	
				 wgDTO.setOthers(Integer.parseInt(request.getParameterValues("OthAllow")[i]));
				 wgDTO.setNetAmount(Integer.parseInt(request.getParameterValues("NetAmount")[i]));
				 wgDTO.setDeduct(Integer.parseInt(request.getParameterValues("OthDeduct")[i]));
				 wgDTO.setPf(Integer.parseInt(request.getParameterValues("PfDeduct")[i]));
				 wgDTO.setOtWage(Integer.parseInt(request.getParameterValues("OtWage")[i]));
				 wgDTO.setBonus(Integer.parseInt(request.getParameterValues("Bonus")[i]));
				 wgDTO.setPension(Integer.parseInt(request.getParameterValues("Pension")[i]));
				 wgDTO.setLastSalMonth(request.getParameterValues("LastSalMon")[i]);
				 wgDTO.setBankDeposiDt(request.getParameterValues("BakDepositDt")[i]);
				 wgDTO.setPfDepositDt(request.getParameterValues("PfDepositDt")[i]);
				 wgDTO.setMobno(request.getParameterValues("mobno")[i]);
				 wgDTO.setWagerecovery(Integer.parseInt(request.getParameterValues("WageRecovery")[i]));
				 
				 
				 arl.add(wgDTO);
			  }
			
			 
			 String wagemonth=request.getParameter("year")+"-"+request.getParameter("month");
			 String lastmonth=request.getParameter("WOLASTMON");
				 
			 result = new WorkMen().addWorkmenWage(arl,wagemonth,lastmonth,request.getParameter("WONo"),conDTO.getFirmName());
			 
             response.setContentType("text/html");
			 PrintWriter out = response.getWriter();
				if(result==0)
				{	
					out.println("<script type=\"text/javascript\">");
					out.println("alert('Operation Failed!!!');");
					out.println("location='/wages/AddWages.jsp';");
					out.println("</script>");			
				
				}
				else
				{
					out.println("<script type=\"text/javascript\">");
					out.println("alert('Wages Added Successfully');");
					out.println("location='/wages/AddWages.jsp';");
					out.println("</script>");
					
				}		
			 
			 
		}	
		
		else if(action.equals("getWManBYWO"))
		{
			System.out.println("getWManBYWO");
			ArrayList al = (new WorkMen()).getWMenByWorder(request.getParameter("woid"));
			int i=0;
		//	Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
		//	JSONArray row = new JSONArray();
		//	JSONObject row;
			while(i<al.size()) {
				WorkMenDTO wmdto = (WorkMenDTO)al.get(i++) ;			
				JSONObject row = new JSONObject();
				row.put("workman", wmdto.getId());
				row.put("name", wmdto.getName());
				row.put("desg", wmdto.getDesg());
				row.put("startDt", wmdto.getStartDt());
				row.put("endDt", wmdto.getTermDt());
			/*	row.put("mobile", cdto.getMobile());
				row.put("email", cdto.getEMail());
				row.put("pan", cdto.getPan());
				row.put("aadhar", cdto.getAadhar());
				row.put("pfno", cdto.getPF());
				row.put("regnDate", cdto.getRegnDate());
				row.put("approvalStatus", cdto.getApprovalStatus());
				row.put("uid", cdto.getId()); */
				rows.put(row);
			}
		//	json.put("rows", rows);
			System.out.println(rows.toString());
			PrintWriter out = response.getWriter();
			out.println(rows.toString());
		/*	System.out.println("getWManBYWO");
			PrintWriter out = response.getWriter();
			String json = "{ \"demo\":[[\"First Name\",\"Last Name\",\"Address1\",\"Address2\"],[\"First Name\",\"Last Name\",\"Address1\",\"Address2\"]]}";
			out.println(json); */
		}	
		
		else if(action.equals("getWMWageByMon"))
		{
			System.out.println("getWMWageByMon");
			Integer.parseInt(request.getParameter("woid"));
			
			ArrayList al = (new WorkMen()).getWMWageByMon(Integer.parseInt(request.getParameter("woid")),
					Integer.parseInt(conDTO.getId()),request.getParameter("month"),request.getParameter("year"));
			int i=0;
		//	Iterator i = al.iterator();
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONArray row = new JSONArray();
		//	JSONObject row;
			while(i<al.size()) {
				WageReportDTO wgRepDTO = (WageReportDTO)al.get(i++) ;
				
				row = new JSONArray();
				row.put(wgRepDTO.getWorkmanId());
				row.put(wgRepDTO.getName());
				row.put(wgRepDTO.getDesg());
				row.put(wgRepDTO.getWorkstartdt());
				row.put(wgRepDTO.getWageRate());
				row.put(wgRepDTO.getAttendance());
				row.put(wgRepDTO.getOtWage());
				row.put(wgRepDTO.getBonus());
				row.put(wgRepDTO.getOthers());
				row.put(wgRepDTO.getPf());
				row.put(wgRepDTO.getPension());
				row.put(wgRepDTO.getDeduct());
				row.put(wgRepDTO.getNetAmount());
			/*	row.put("landline", cdto.getLL());
				row.put("mobile", cdto.getMobile());
				row.put("email", cdto.getEMail());
				row.put("pan", cdto.getPan());
				row.put("aadhar", cdto.getAadhar());
				row.put("pfno", cdto.getPF());
				row.put("regnDate", cdto.getRegnDate());
				row.put("approvalStatus", cdto.getApprovalStatus());
				row.put("uid", cdto.getId()); */
				rows.put(row);
			}
			json.put("rows", rows);
		//	System.out.println(json.toString());
			PrintWriter out = response.getWriter();
			out.println(json.toString());
			
		}
		
		else if(action.equals("WMAllocChange"))			
		{
		  //	System.out.println("WMAllocChange");
			ArrayList arl=new ArrayList();
			int result=0;
			String checkbox[]=request.getParameterValues("selwman");
			
			for(int i=0;i<checkbox.length;i++)
			{			
				    int seq=Integer.parseInt(checkbox[i]);
			      //  System.out.println(seq);
				  //	System.out.println(request.getParameterValues("workman")[seq]);
					WorkMenDTO wmDTO=new WorkMenDTO();
					wmDTO.setId(Integer.parseInt(request.getParameterValues("workman")[seq]));
					wmDTO.setWoid(Integer.parseInt(request.getParameter("WONo"))); // Old Work Oder
					wmDTO.setNewWoid(request.getParameter("WONew"));// New Work Oder
					wmDTO.setOdlTermDt(request.getParameterValues("termDt")[seq]); // Old Work Oder End Date
				    wmDTO.setStartDt(request.getParameterValues("newDt")[seq]);  // New Work Oder Start Date
				    wmDTO.setTermDt(request.getParameterValues("newEndDt")[seq]); // New Work Oder End Date
				    wmDTO.setWageRate(Integer.parseInt(request.getParameterValues("baseWage")[seq]));
				 //   wmDTO.setWoArea(request.getParameterValues("woarea")[seq]);
				    wmDTO.setLocid(request.getParameterValues("woloc")[seq]);
				    wmDTO.setLocoth(request.getParameterValues("loctxt")[seq]);
				    arl.add(wmDTO);    
							
			}
			
			result=new WorkMen().WMAllocChange(arl);
                        response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			if(result==0)
			{	
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Operation Failed!!!');");
				out.println("location='/WorkMen/WManAllocChange.jsp';");
				out.println("</script>");			
			
			}
			else
			{
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Workman Allocation Changed Successfully');");
				out.println("location='/WorkMen/WManAllocChange.jsp';");
				out.println("</script>");
				
			}		
			
		}
		
		else if(action.equals("getActiveWman"))
		{
			System.out.println("getActiveWman");
			ArrayList arl= (new WorkMen()).getActiveWman(conDTO.getId());	
            int i=0;
			
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();			
			
				while(i<arl.size()) {
					WorkMenDTO wmdto = (WorkMenDTO)arl.get(i++) ;			
					JSONArray row = new JSONArray();
					row.put(i);
					row.put(wmdto.getId());
					row.put(wmdto.getName());
					row.put(wmdto.getSex());
					row.put(wmdto.getDob());
					row.put(wmdto.getFname());					
					row.put(wmdto.getWoName());
					row.put(wmdto.getDesg());
					row.put(wmdto.getStartDt());
					row.put(wmdto.getTermDt());				
					row.put(wmdto.getWageRate());
					row.put(wmdto.getIcardType());
				    row.put(wmdto.getIcardNo());
				    row.put(wmdto.getAdhar());
				    row.put(wmdto.getPan()); 
				    row.put(wmdto.getPermAddr());
				    row.put(wmdto.getPermPin());
				    row.put(wmdto.getMobno());
				    row.put(wmdto.getEmail());	
				   // row.put(wmdto.getWoArea());
				    row.put(wmdto.getLocoth());
				    row.put(wmdto.getEmpTypeDesc());
				    row.put(wmdto.getSkillTypeDesc());
				    row.put(wmdto.getEduQual());
				    row.put(wmdto.getBankname());
				    row.put(wmdto.getBankno());
				    row.put(wmdto.getPfType());
				    row.put(wmdto.getPfno());
				    
				    
					rows.put(row);
				}
			    
				json.put("rows", rows);
			//	System.out.println(json.toString());
				PrintWriter out = response.getWriter();
				out.println(json.toString());		
		
		}
		else if(action.equals("WMPeriodChange"))
		{
			ArrayList arl=new ArrayList();
			int result=0;
			String checkbox[]=request.getParameterValues("selwman");
			
			for(int i=0;i<checkbox.length;i++)
			{			
				    int seq=Integer.parseInt(checkbox[i]);
			        System.out.println(seq);	
			        System.out.println("termdt"+request.getParameterValues("termDt")[seq]);
					WorkMenDTO wmDTO=new WorkMenDTO();
					wmDTO.setId(Integer.parseInt(request.getParameterValues("workman")[seq]));
					wmDTO.setContractorID(Integer.parseInt(conDTO.getId()));
					wmDTO.setStartDt(request.getParameterValues("startDt")[seq]);
					wmDTO.setTermDt(request.getParameterValues("termDt")[seq]);		   
				    arl.add(wmDTO);    
							
			}
			
			result=new WorkMen().WMPeriodChange(arl);
                        response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			if(result==0)
			{	
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Operation Failed!!!');");
				out.println("location='/WorkMen/WManDeactive.jsp';");
				out.println("</script>");			
			}
			else
			{
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Work Period Changed Successfully');");
				out.println("location='/WorkMen/WManDeactive.jsp';");
				out.println("</script>");				
			}			
		}
		else if(action.equals("getInactiveWman"))
		{
			System.out.println("getInactiveWman");
			
			ArrayList arl= (new WorkMen()).getInactiveWman(conDTO.getId());
			
			int i=0;
			
				JSONObject json = new JSONObject();
				JSONArray rows = new JSONArray();
			
				while(i<arl.size()) {
					WorkMenDTO wmdto = (WorkMenDTO)arl.get(i++) ;			
					JSONObject row = new JSONObject();
					row.put("workman", wmdto.getId());
					row.put("name", wmdto.getName());
					row.put("wono", wmdto.getWoName());
					row.put("desg", wmdto.getDesg());
					row.put("startDt", wmdto.getStartDt());
					row.put("endDt", wmdto.getTermDt());
					row.put("woEndDt", wmdto.getWoTermDt());
					row.put("icardType", wmdto.getIcardType());
				    row.put("icardNo", wmdto.getIcardNo());
				    row.put("termReason", wmdto.getTermreason() );
				    
					rows.put(row);
				}
			
			//	System.out.println(rows.toString());
				PrintWriter out = response.getWriter();
				out.println(rows.toString());			
		}
		else if(action.equals("WMPeriodExtend"))
		{
			ArrayList arl=new ArrayList();
			int result=0;
			String checkbox[]=request.getParameterValues("selwman");
			
			for(int i=0;i<checkbox.length;i++)
			{			
				    int seq=Integer.parseInt(checkbox[i]);
			        System.out.println(seq);	
			        System.out.println("termdt"+request.getParameterValues("termDt")[seq]);
					WorkMenDTO wmDTO=new WorkMenDTO();
					wmDTO.setId(Integer.parseInt(request.getParameterValues("workman")[seq]));
					wmDTO.setContractorID(Integer.parseInt(conDTO.getId()));
					wmDTO.setStartDt(request.getParameterValues("startDt")[seq]);
					wmDTO.setTermDt(request.getParameterValues("termDt")[seq]);		   
				    arl.add(wmDTO);    
							
			}
			
			result=new WorkMen().WMPeriodChange(arl);
                        response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			if(result==0)
			{	
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Operation Failed!!!');");
				out.println("location='/WorkMen/WManExtendPeriod.jsp';");
				out.println("</script>");			
			}
			else
			{
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Work Period Extended Successfully');");
				out.println("location='/WorkMen/WManExtendPeriod.jsp';");
				out.println("</script>");				
			}			
		}
		else if(action.equals("getWMAllocChange"))
		{
			System.out.println("getWMAllocChange");
						
			ArrayList al = (new WorkMen()).getWMAllocChange(request.getParameter("woid"),request.getParameter("newWoid"));
			int i=0;		
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
		
			while(i<al.size()) {
				WorkMenDTO wmdto = (WorkMenDTO)al.get(i++) ;	
				System.out.println("lastsalMon"+wmdto.getLastSalMonth());
				JSONObject row = new JSONObject();
				
				row.put("workman", wmdto.getId());
				row.put("name", wmdto.getName());
				row.put("desg", wmdto.getDesg());
				row.put("startDt", wmdto.getStartDt());
				row.put("endDt", wmdto.getTermDt());
				row.put("lastsalMon", wmdto.getLastSalMonth());
			//	row.put("minWage", wmdto.getMinWage());
		    	
			   
				rows.put(row);
			}
		
		  //	System.out.println(rows.toString());
			PrintWriter out = response.getWriter();
			out.println(rows.toString());
		
					
		}
		else if(action.equals("getWorkManByID"))
		{
			System.out.println("getWorkManByID");
			WorkMenDTO wmDTO = (new WorkMen()).getWorkManByID(request.getParameter("ictype"),request.getParameter("icno").toUpperCase());
			
			if(wmDTO!=null)
			{	
			
			JSONObject row = new JSONObject();			
			row.put("workman", wmDTO.getId());
			row.put("name", wmDTO.getName());
			row.put("dob", wmDTO.getDob());
			row.put("sex", wmDTO.getSex());
			row.put("fname", wmDTO.getFname());
			row.put("permaddr", wmDTO.getPermAddr());
			row.put("permpin", wmDTO.getPermPin());
			row.put("pan", wmDTO.getPan());
			row.put("adhar", wmDTO.getAdhar());
			row.put("mobno", wmDTO.getMobno());
			row.put("email", wmDTO.getEmail());
			row.put("eduqual", wmDTO.getEduQual());
			row.put("termDt", wmDTO.getTermDt());
			row.put("conID", wmDTO.getContractorID());
			row.put("pftype", wmDTO.getPfType());
			row.put("pfno", wmDTO.getPfno());
			row.put("bank", wmDTO.getBankname());
			row.put("bankacno", wmDTO.getBankno());
			
		 //	System.out.println(row.toString());
			PrintWriter out = response.getWriter();
			out.println(row.toString());
			}
		}	
		else if(action.equals("reemplyWorkMan"))
		{
			System.out.println("reemplyWorkMan");	
			int result=0;
			
			WorkMenDTO wmDTO=new WorkMenDTO();
			wmDTO.setId(Integer.parseInt(request.getParameter("wmanID")));
			wmDTO.setName(request.getParameter("name").toUpperCase());
			wmDTO.setContractorID(Integer.parseInt(conDTO.getId()));
			wmDTO.setWoid(Integer.parseInt(request.getParameter("WONo")));
			wmDTO.setFname(request.getParameter("fname").toUpperCase());
			wmDTO.setDob(request.getParameter("dob"));
			wmDTO.setSex(request.getParameter("sex"));
			wmDTO.setPermAddr(request.getParameter("address").toUpperCase());
			wmDTO.setPermPin(request.getParameter("pincode").trim());
			wmDTO.setMobno(request.getParameter("mobno").trim());
			wmDTO.setEmail(request.getParameter("email").trim());
			wmDTO.setDesg(request.getParameter("desg").toUpperCase());
			wmDTO.setEmplType(Integer.parseInt(request.getParameter("empltype")));
			wmDTO.setSkillType(Integer.parseInt(request.getParameter("skillType")));
			wmDTO.setPan(request.getParameter("pan").trim().toUpperCase());
			wmDTO.setAdhar(request.getParameter("adhar").trim());
			wmDTO.setBankname(request.getParameter("bank").toUpperCase());
			wmDTO.setBankno(request.getParameter("bankaccount").trim());
			wmDTO.setPenallow(request.getParameter("pension"));
			wmDTO.setPfType(request.getParameter("pftype"));
			wmDTO.setPfno(request.getParameter("pfno").trim());
			wmDTO.setPfallow(request.getParameter("pf"));
			wmDTO.setPenPercent(Integer.parseInt(request.getParameter("pnpercent")));
			wmDTO.setPfPercent(Integer.parseInt(request.getParameter("pfpercent")));
			wmDTO.setEduQual(request.getParameter("qualification").toUpperCase());
			wmDTO.setStartDt(request.getParameter("StartDt"));
			wmDTO.setTermDt(request.getParameter("TermDt"));
			wmDTO.setWageRate(Integer.parseInt(request.getParameter("BasicWage")));
			wmDTO.setIcardType(request.getParameter("icardType"));
			wmDTO.setIcardNo(request.getParameter("icardNo").trim().toUpperCase());	
			System.out.println("ICARDNO:"+wmDTO.getIcardNo());
			//wmDTO.setWoArea(request.getParameter("WOAREA"));
			wmDTO.setLocid(request.getParameter("WOLOC"));
			wmDTO.setLocoth(request.getParameter("loctxt"));
			
			WorkMen workmen=new WorkMen();
			
			if(conDTO.getId().equals(request.getParameter("lastConID")))   // Reassign Terminated workman by Same Contractor
			{
				System.out.println("lastConID");
				result = workmen.reassignWorkMan(wmDTO);	
			}	
			else
			{
				result = workmen.reemplyWorkMan(wmDTO);	 // Employ Terminated workman by Different Contractor
			}
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			if(result==0)
			{	
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Operation Failed!!!');");
				out.println("location='/WorkMen/WorkMenDetails.jsp';");
				out.println("</script>");			
			
			}
			else
			{
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Work Man Added Successfully');");
				out.println("location='/WorkMen/WorkMenDetails.jsp';");
				out.println("</script>");
				
			}	
			
			
		}
		else if(action.equals("WMPeriodTerminate"))
		{
			System.out.println("WMPeriodTerminate");
			ArrayList arl=new ArrayList();
			int result=0;
			String checkbox[]=request.getParameterValues("selwman");
			
			for(int i=0;i<checkbox.length;i++)
			{			
				    int seq=Integer.parseInt(checkbox[i]);
			      //  System.out.println(seq);	
			      //  System.out.println("termdt"+request.getParameterValues("termDt")[seq]);
					WorkMenDTO wmDTO=new WorkMenDTO();
					wmDTO.setId(Integer.parseInt(request.getParameterValues("workman")[seq]));
					wmDTO.setContractorID(Integer.parseInt(conDTO.getId()));
					wmDTO.setStartDt(request.getParameterValues("startDt")[seq]);
					wmDTO.setTermDt(request.getParameterValues("termDt")[seq]);
					if(request.getParameterValues("termReason")[seq]!=null && !request.getParameterValues("termReason")[seq].isEmpty())
					{
						wmDTO.setTermreason(request.getParameterValues("termReason")[seq].toUpperCase());
					}
					else
					{
						wmDTO.setTermreason("TERMINATED BY CONTRACTOR");
					}						
					
				    arl.add(wmDTO);    
							
			}
			
			result=new WorkMen().WMPeriodTerminate(arl);
                        response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			if(result==0)
			{	
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Operation Failed!!!');");
				out.println("location='/WorkMen/WMenTerminate.jsp';");
				out.println("</script>");			
			}
			else
			{
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Work Man Terminated Successfully');");
				out.println("location='/WorkMen/WMTerminateList.jsp';");
				out.println("</script>");				
			}			
		}
		
		else if (action.equalsIgnoreCase("uploadWMPhoto"))
		{
			System.out.println("uploadWMPhoto");
			String wmid=request.getParameter("wmid");
		//	System.out.println(wmid);
			int result=0;
			 response.setContentType("text/html;charset=UTF-8");
			 PrintWriter out=response.getWriter();
			 InputStream is =null;
			 FileItemFactory factory = new DiskFileItemFactory();
			 ServletFileUpload upload = new ServletFileUpload(factory);
			 
			 
			//  upload.setFileSizeMax(500 * 1024 ); 
			 
			 Connection con=null;
			 try
			 {
			 
			 con = DBConnection.getInstance().getDBConnection();			 
		     con.setAutoCommit(false);
			 String sql="SELECT CONTRACT_WORKMAN_PHOTO_SEQ.nextval AS DOCNO FROM DUAL";
			 PreparedStatement pst=con.prepareStatement(sql);
			 ResultSet rs = pst.executeQuery();
			 rs.next();
			 int maxDocId = rs.getInt("DOCNO"); 
			 
			 rs.close();
			 pst.close();
			 
			 sql = "INSERT INTO CONTRACT_WORKMAN_PHOTO VALUES(?,?,?,?)";
			 pst=con.prepareStatement(sql);		
			 pst.setInt(1, maxDocId);
			
			 List items = upload.parseRequest(request);
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
				        System.out.println(fieldname);
				        System.out.println(fieldvalue);
				    }
				    else
				    {
				    	System.out.println("inside else");
				    	is = item.getInputStream(); 
				    	docSizeOk=true;				    	
				    	
				    	final byte[] bytes = new byte[is.available()];				    
				    	
				    	if(bytes.length>0) 
				    	{
				    		is.read(bytes);
				    		pst.setBytes(2,bytes);				    	
							pst.setString(3,item.getName());
							pst.setLong(4,item.getSize());							
							System.out.println(item.getName());
							docSizeOk=true;
				    	}
				    }	
				    
				    if(docSizeOk==false) 
				    {   response.setContentType("text/html");
			    		out.println("<script type=\"text/javascript\">");
						out.println("alert('Documents of size is invalid!');");
						out.println("location='regp.jsp';");
						out.println("</script>");
						out.close();
					}
				    
				    else
				    {	
				     //  System.out.println("before insert");
			    	 pst.executeUpdate();
			    	 pst.close();
			    	 
			    	// System.out.print("after insert");	
			    	 
			    	
			    		 
			    	 sql = "UPDATE CONTRACT_WORKMAN_DETAILS SET PHOTONO='"+maxDocId+"' WHERE WORKMANID='"+wmid+"'";
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
				 response.sendRedirect("/WorkMen/UploadPhotoAck.jsp?message=ok"); 
			 }	 
			 else
			 {		 
				 response.sendRedirect("/WorkMen/UploadPhotoAck.jsp?message=fail");
			 } 
			 
		}
		
		else if (action.equalsIgnoreCase("getWMInactAlloc"))
		{
			System.out.println("getWMInactAlloc");
			
			ArrayList al = (new WorkMen()).getWMInactAlloc(request.getParameter("woid"),conDTO.getId());
			int i=0;		
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
		
			while(i<al.size()) {
				WorkMenDTO wmdto = (WorkMenDTO)al.get(i++) ;	
				System.out.println("lastsalMon"+wmdto.getLastSalMonth());
				JSONObject row = new JSONObject();
				
				row.put("workman", wmdto.getId());
				row.put("name", wmdto.getName());
				row.put("desg", wmdto.getDesg());
				row.put("startDt", wmdto.getStartDt());
				row.put("endDt", wmdto.getTermDt());
				row.put("lastsalMon", wmdto.getLastSalMonth());
			//	row.put("minWage", wmdto.getMinWage());
		    	
			   
				rows.put(row);
			}
		
		  //	System.out.println(rows.toString());
			PrintWriter out = response.getWriter();
			out.println(rows.toString());			
		}
		
		else if(action.equals("WMInactAlloc"))			
		{
		  //	System.out.println("WMAllocChange");
			ArrayList arl=new ArrayList();
			int result=0;
			String checkbox[]=request.getParameterValues("selwman");
		  //	System.out.println("workorder:"+request.getParameter("WONo"));
			for(int i=0;i<checkbox.length;i++)
			{			
				    int seq=Integer.parseInt(checkbox[i]);
			       // System.out.println(seq);
				  //	System.out.println(request.getParameterValues("workman")[seq]);
					WorkMenDTO wmDTO=new WorkMenDTO();
					wmDTO.setId(Integer.parseInt(request.getParameterValues("workman")[seq]));
					wmDTO.setWoid(Integer.parseInt(request.getParameter("WONo"))); //  	New Work Oder				
				    wmDTO.setStartDt(request.getParameterValues("newDt")[seq]);  // New Work Oder Start Date
				    wmDTO.setTermDt(request.getParameterValues("newEndDt")[seq]); // New Work Oder End Date
				    wmDTO.setWageRate(Integer.parseInt(request.getParameterValues("baseWage")[seq]));
				  //  wmDTO.setWoArea(request.getParameterValues("woarea")[seq]);
				    wmDTO.setLocid(request.getParameterValues("woloc")[seq]);
				    wmDTO.setLocoth(request.getParameterValues("loctxt")[seq]);
				    arl.add(wmDTO);    
							
			}
			
			result=new WorkMen().WMInactAlloc(arl);
                        response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			if(result==0)
			{	
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Operation Failed!!!');");
				out.println("location='/WorkMen/WMInctAlloc.jsp';");
				out.println("</script>");			
			
			}
			else
			{
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Workman Allocated Successfully');");
				out.println("location='/WorkMen/WMInctAlloc.jsp';");
				out.println("</script>");
				
			}		
			
		}
		
		else if(action.equals("WageCategoryRep"))
		{
			System.out.println("WageCategoryRep");
			ArrayList arl = (new WorkMen()).WageCategoryRep(request.getParameter("wo"),conDTO.getId(),request.getParameter("month"),request.getParameter("year"),request.getParameter("emp"));
			
			int i=0;
			
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			
		
			while(i<arl.size())
			{				
				WageReportDTO wgRepDTO=(WageReportDTO) arl.get(i++);	
			  //	System.out.println(wgRepDTO.getWorkmanId() );
				
				JSONArray row= new JSONArray();
				row.put(i);
				row.put(wgRepDTO.getWorkmanId());
				row.put(wgRepDTO.getName());				
				row.put(wgRepDTO.getDesg());
				row.put(wgRepDTO.getWorkstartdt());
				row.put(wgRepDTO.getWageRate());
				row.put(wgRepDTO.getAttendance());
				row.put(wgRepDTO.getNetAmount());	
				row.put(wgRepDTO.getOtWage());
				row.put(wgRepDTO.getBonus());
				row.put(wgRepDTO.getOthers());
				row.put(wgRepDTO.getPf());
				row.put(wgRepDTO.getPension());
				row.put(wgRepDTO.getDeduct());	
				row.put(wgRepDTO.getWagerecovery());
				row.put(wgRepDTO.getBankDeposiDt());	
				row.put(wgRepDTO.getPfDepositDt());	
				rows.put( row);
				
			}
			
			json.put("rows",rows);
		//	System.out.println(json.toString());
			PrintWriter out = response.getWriter();
			out.println(json.toString());	
		}
		
		else if(action.equals("getWMByCon"))
		{
			System.out.println("getWMByCon");
			ArrayList arl =new ArrayList();
			
		    arl=new WorkMen().getWMAllbyCon(conDTO.getId());		
			
		    int i=0;
			
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();			
			
				while(i<arl.size()) {
					WorkMenDTO wmdto = (WorkMenDTO)arl.get(i++) ;			
					JSONArray row = new JSONArray();
					row.put(i);
					row.put(wmdto.getId());
					row.put(wmdto.getName());
					row.put(wmdto.getSex());
					row.put(wmdto.getDob());
					row.put(wmdto.getIcardType());
				    row.put(wmdto.getIcardNo());
				    row.put(wmdto.getPermAddr());
				    row.put(wmdto.getPermPin());
				    row.put(wmdto.getMobno());
				    row.put(wmdto.getEmail());				    
					rows.put(row);
				}
			    
				json.put("rows", rows);
				// System.out.println(json.toString());
				PrintWriter out = response.getWriter();
				out.println(json.toString());			
		}
		
		else if(action.equals("getInactiveWMList"))
		{
			System.out.println("getInactiveWMList");
			ArrayList arl =new ArrayList();
			
			arl=new WorkMen().getInactiveWman(conDTO.getId());		
			
			int i=0;
			
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();			
			
				while(i<arl.size()) {
					WorkMenDTO wmdto = (WorkMenDTO)arl.get(i++) ;			
					JSONArray row = new JSONArray();
					row.put(i);
					row.put(wmdto.getId());
					row.put(wmdto.getName());
					row.put(wmdto.getSex());
					row.put(wmdto.getDob());								
					row.put(wmdto.getWoName());
					row.put(wmdto.getDesg());
					row.put(wmdto.getStartDt());
					row.put(wmdto.getTermDt());
					row.put(wmdto.getTermreason());					
					row.put(wmdto.getIcardType());
				    row.put(wmdto.getIcardNo());
				    row.put(wmdto.getWageRate());
				    row.put(wmdto.getEmpTypeDesc());
				    row.put(wmdto.getSkillTypeDesc());
				   			    
					rows.put(row);
				}
			    
				json.put("rows", rows);
			  //	System.out.println(json.toString());
				PrintWriter out = response.getWriter();
				out.println(json.toString());			
		}
		
		else if(action.equals("getWMForTerminate"))
		{
			System.out.println("getWMForTerminate");
            ArrayList arl= (new WorkMen()).getActiveWman(conDTO.getId());
			
			int i=0;
			
				JSONObject json = new JSONObject();
				JSONArray rows = new JSONArray();
			
				while(i<arl.size()) {
					
					WorkMenDTO wmdto = (WorkMenDTO)arl.get(i++) ;	
				//	System.out.println(wmdto.getIcardType() );
					JSONObject row = new JSONObject();
					row.put("workman", wmdto.getId());
					row.put("name", wmdto.getName());
					row.put("wono", wmdto.getWoName());
					row.put("desg", wmdto.getDesg());
					row.put("startDt", wmdto.getStartDt());
					row.put("endDt", wmdto.getTermDt());
					row.put("woEnd", wmdto.getWoTermDt());
					row.put("lastsalMon", wmdto.getLastSalMonth());
				    row.put("icardType", wmdto.getIcardType());
				    row.put("icardNo", wmdto.getIcardNo());
					rows.put(row);
				}
			
			//	System.out.println(rows.toString());
				PrintWriter out = response.getWriter();
				out.println(rows.toString());			
		}
		
		else if(action.equals("getWManMoved"))
		{
			System.out.println("getWManMoved");
			ArrayList arl =new ArrayList();
			
			arl=new WorkMen().getWManMoved(conDTO.getId());		
			
			int i=0;
			
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();			
			
				while(i<arl.size()) {
					WorkMenDTO wmdto = (WorkMenDTO)arl.get(i++) ;			
					JSONArray row = new JSONArray();
					row.put(i);
					row.put(wmdto.getId());
					row.put(wmdto.getName());
					row.put(wmdto.getSex());
					row.put(wmdto.getDob());	
					row.put(wmdto.getContFirmName());
					row.put(wmdto.getIcardType());
				    row.put(wmdto.getIcardNo());
				    row.put(wmdto.getPermAddr());
				    row.put(wmdto.getPermPin());
				    row.put(wmdto.getMobno());
				   
				   			    
					rows.put(row);
				}
			    
				json.put("rows", rows);
			//	System.out.println(json.toString());
				PrintWriter out = response.getWriter();
				out.println(json.toString());			
		}
		
		else if(action.equals("getEmployeeWage"))
		{
			System.out.println("getEmployeeWage");
			ArrayList arl = (new WorkMen()).getEmployeeWage(request.getParameter("wmid"),request.getParameter("year"),request.getParameter("month"));
			
			int i=0;			
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			
			while(i<arl.size())
			{				
				WageReportDTO wgRepDTO=(WageReportDTO) arl.get(i++);
		
		    	System.out.println(wgRepDTO.getWorkmanId() );
				
				JSONArray row= new JSONArray();
				row.put(i);
				row.put(wgRepDTO.getWorkmanId());
				row.put(wgRepDTO.getName());				
				row.put(wgRepDTO.getDesg());
				row.put(wgRepDTO.getContName());
				row.put(wgRepDTO.getWoName());
				row.put(wgRepDTO.getWorkstartdt());
				row.put(wgRepDTO.getNetAmount());
				row.put(wgRepDTO.getWageRate());
				row.put(wgRepDTO.getAttendance());
				row.put(wgRepDTO.getOtWage());
				row.put(wgRepDTO.getBonus());
				row.put(wgRepDTO.getOthers());
				row.put(wgRepDTO.getPf());
				row.put(wgRepDTO.getPension());
				row.put(wgRepDTO.getDeduct());	
				row.put(wgRepDTO.getWagerecovery());
				row.put(wgRepDTO.getBankDeposiDt());	
				row.put(wgRepDTO.getPfDepositDt());	
				rows.put( row);
			}	
				
			json.put("rows",rows);
		 //	System.out.println(json.toString());
			PrintWriter out = response.getWriter();
			out.println(json.toString());	
		}
		
		else if (action.equalsIgnoreCase("resetEmpPassword"))
		{					
			int v = new WorkMen().resetEmpPassword(Integer.parseInt(request.getParameter("wmid")),request.getParameter("inpPwd"));	
                        response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			if(v==1) {
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Password Reset Successful!');");
				out.println("location='/Employee/EmpPasswordChange.jsp';");
				out.println("</script>");
				out.close();
			}
			else 
			{			
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Password Reset failed');");
				out.println("location='/Employee/EmpPasswordChange.jsp';");
				out.println("</script>");
				out.close();
			}	
			
		}
		
		else if(action.equalsIgnoreCase("getWMPhoto"))
		{ 
			System.out.println("getWMPhoto");
			String photono=request.getParameter("photono");
			System.out.println("photono"+photono);
                        Connection con=null;
			try
			{
			con=DBConnection.getInstance().getDBConnection();
			con.setAutoCommit(false);
		    String sql="select filedata from contract_workman_photo where fileno=?";
		    PreparedStatement pstmt=con.prepareStatement(sql);
		    pstmt.setString(1, photono);
		 	ResultSet rs = pstmt.executeQuery();
		 	while(rs.next())
		 	{	
		 	Blob bl = rs.getBlob("filedata");
		        byte[] pict = bl.getBytes(1,(int)bl.length());			       
		        
		       response.setContentType("image/jpeg");
		       OutputStream o = response.getOutputStream();
		       o.write(pict);
			   o.flush();    
		       o.close();
		       response.flushBuffer();
		 	
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
		
		else if(action.equalsIgnoreCase("getWMIcard"))
		{
			WorkMenDTO wmdto=new LoginDao().getWorkmanDTO(request.getParameter("workman"));	
			
			JSONArray rows = new JSONArray();
			JSONObject row = new JSONObject();
			row.put("workman", wmdto.getId());
			row.put("name", wmdto.getName());
			row.put("dob",wmdto.getDob());
			row.put("sex",wmdto.getSex());
			row.put("cont", wmdto.getContName());
			row.put("firmname", wmdto.getContFirmName());
			row.put("wono", wmdto.getWoName());
			row.put("desg", wmdto.getDesg());
			row.put("startDt", wmdto.getStartDt());
			row.put("endDt", wmdto.getTermDt());		
		    row.put("icardType", wmdto.getIcardType());
		    row.put("icardNo", wmdto.getIcardNo());
		    row.put("empaddr", wmdto.getPermAddr());
		    row.put("emppin",wmdto.getPermPin());
		    row.put("mobno", wmdto.getMobno());
		    row.put("wono", wmdto.getWoNum());
		    row.put("loaname", wmdto.getLoaname());
		    
			rows.put(row);
			
			// System.out.println(rows.toString());
			PrintWriter out = response.getWriter();
			out.println(rows.toString());
			
		}
		
		else if(action.equals("getWMenForWage"))
		{
			System.out.println("getWMenForWage");
			
			ArrayList al = (new WorkMen()).getWMenForWage(Integer.parseInt(request.getParameter("woid")),request.getParameter("month"),request.getParameter("year"));
			int i=0;		
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
		
			while(i<al.size()) {
				WorkMenDTO wmdto = (WorkMenDTO)al.get(i++) ;	
				
				JSONObject row = new JSONObject();
				
				row.put("workman", wmdto.getId());
				row.put("name", wmdto.getName());
				row.put("desg", wmdto.getDesg());
				row.put("conid", wmdto.getContractorID());
				row.put("lastsalmon", wmdto.getLastSalMonth());
				row.put("startdt", wmdto.getStartDt());
				row.put("wagerate", wmdto.getWageRate());
				row.put("mobno", wmdto.getMobno()); 
				row.put("minwage", wmdto.getMinWage());
			   
				rows.put(row);
			}
		
		  //	System.out.println(rows.toString());
			PrintWriter out = response.getWriter();
			out.println(rows.toString());
			
		}
		
		
		else if(action.equals("EditProfileByEmp"))
		{
			System.out.println("EditProfileByEmp");
			int result=0;
			
			WorkMenDTO wmDTO=new WorkMenDTO();
			wmDTO.setId(Integer.parseInt(request.getParameter("empid")));
		//	wmDTO.setFname(request.getParameter("fname").toUpperCase());
			wmDTO.setPermAddr(request.getParameter("permaddr").toUpperCase());
			wmDTO.setPermPin(request.getParameter("permpin"));
		//	wmDTO.setBankname(request.getParameter("bankname"));
		//	wmDTO.setBankno(request.getParameter("bankno"));
			wmDTO.setMobno(request.getParameter("mobno"));
			wmDTO.setEmail(request.getParameter("email"));
			wmDTO.setEduQual(request.getParameter("eduqual").toUpperCase());
			
			result=new WorkMen().EditProfileByEmp(wmDTO);
                        response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			if(result==1) {
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Profile Changed');");
				out.println("location='/Employee/EmpProfile.jsp';");
				out.println("</script>");
				out.close();
			}
			else 
			{			
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Profile Change failed');");
				out.println("location='/Employee/EmpProfile.jsp';");
				out.println("</script>");
				out.close();
			}	
			
		}
                else if(action.equals("getWMWOs")) {
                        ArrayList al = (new WorkMen()).getWMenWO(request.getParameter("wmid"));
			int i=0;
			JSONArray rows = new JSONArray();		
			while(i<al.size()) {
                            String  a[] = (String[])al.get(i++);				
                            JSONObject row = new JSONObject();				
                            row.put("woid", a[0]);
                            row.put("wono", a[1]);
                            rows.put(row);
			}
		
			PrintWriter out = response.getWriter();
			out.println(rows.toString());
                }
		
                else if(action.equals("WMWageByRange"))
        		{
        			System.out.println("getWMWageByRange");
        			ArrayList arl = (new WorkMen()).WMWageByRange(request.getParameter("loa"),conDTO.getId(),request.getParameter("stmonth"),
        					request.getParameter("endmonth"),request.getParameter("styear"),request.getParameter("emp"));
        			
        			int i=0;
        			
        			JSONObject json = new JSONObject();
        			JSONArray rows = new JSONArray();
        			
        		
        			while(i<arl.size())
        			{				
        				WageReportDTO wgRepDTO=(WageReportDTO) arl.get(i++);	
        			  //	System.out.println(wgRepDTO.getWorkmanId() );
        				
        				JSONArray row= new JSONArray();
        				row.put(i);
        				row.put(wgRepDTO.getWorkmanId());        				       				
        				row.put(wgRepDTO.getName());
        				row.put(wgRepDTO.getMonth()); 
        				row.put(wgRepDTO.getDesg());
        				row.put(wgRepDTO.getWorkstartdt());
        				row.put(wgRepDTO.getWageRate());
        				row.put(wgRepDTO.getAttendance());
        				row.put(wgRepDTO.getNetAmount());	
        				row.put(wgRepDTO.getOtWage());
        				row.put(wgRepDTO.getBonus());
        				row.put(wgRepDTO.getOthers());
        				row.put(wgRepDTO.getPf());
        				row.put(wgRepDTO.getPension());
        				row.put(wgRepDTO.getDeduct());	
        				row.put(wgRepDTO.getWagerecovery());
        				row.put(wgRepDTO.getBankDeposiDt());	
        				row.put(wgRepDTO.getPfDepositDt());	
        				rows.put(row);
        				
        			}
        			
        			json.put("rows",rows);
        			System.out.println(json.toString());
        			PrintWriter out = response.getWriter();
        			out.println(json.toString());	
        		}
		
                else if(action.equals("getEmpWageRange"))
        		{
        			System.out.println("getEmpWageRange");
        			ArrayList arl = (new WorkMen()).getEmpWageRange(request.getParameter("wmid"),request.getParameter("styear"),request.getParameter("stmonth"),request.getParameter("endmonth"));
        			
        			int i=0;			
        			JSONObject json = new JSONObject();
        			JSONArray rows = new JSONArray();
        			
        			while(i<arl.size())
        			{				
        				WageReportDTO wgRepDTO=(WageReportDTO) arl.get(i++);
        		
        		    	System.out.println(wgRepDTO.getWorkmanId() );
        				
        				JSONArray row= new JSONArray();
        				row.put(i);
        				row.put(wgRepDTO.getWorkmanId());
        				row.put(wgRepDTO.getName());				
        				row.put(wgRepDTO.getDesg());
        				row.put(wgRepDTO.getMonth());
        				row.put(wgRepDTO.getWoName());
        				row.put(wgRepDTO.getContName());
        				row.put(wgRepDTO.getFirmName());
        				row.put(wgRepDTO.getWorkstartdt());
        				row.put(wgRepDTO.getNetAmount());
        				row.put(wgRepDTO.getWageRate());
        				row.put(wgRepDTO.getAttendance());
        				row.put(wgRepDTO.getOtWage());
        				row.put(wgRepDTO.getBonus());
        				row.put(wgRepDTO.getOthers());
        				row.put(wgRepDTO.getPf());
        				row.put(wgRepDTO.getPension());
        				row.put(wgRepDTO.getDeduct());	
        				row.put(wgRepDTO.getWagerecovery());
        				row.put(wgRepDTO.getBankDeposiDt());	
        				row.put(wgRepDTO.getPfDepositDt());	
        				rows.put( row);
        			}	
        				
        			json.put("rows",rows);
        		 //	System.out.println(json.toString());
        			PrintWriter out = response.getWriter();
        			out.println(json.toString());	
        		}
        		
	}

}
