package org.cris.clip.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cris.clip.dao.LoginDao;
import org.cris.clip.dto.AdministratorDTO;
import org.cris.clip.dto.ContractorDTO;
import org.cris.clip.dto.WorkMenDTO;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		ContractorDTO conDTO=null;
		AdministratorDTO adto=null;
		String action=request.getParameter("action");
		if(action==null)
			request.getRequestDispatcher("/clip.jsp").forward(request, response);
                
		if (action.equalsIgnoreCase("login")) {
                    System.out.println(request.getParameter("type"));
			response.setContentType("text/html");  
			PrintWriter pw = response.getWriter();
			pw.println("<script type=\"text/javascript\">");
			
			if(request.getParameter("type").equalsIgnoreCase("a")) {
                            if((new LoginDao()).loginAdmin(request.getParameter("inpUsername"),request.getParameter("inpPwd"))) {	
                                    adto=new LoginDao().adminLogin(request.getParameter("inpUsername"));
                                    HttpSession httpSession=request.getSession(true);
                                    httpSession.setAttribute("administrator", adto);
                                    pw.println("location='/Administrator/AdminHome.jsp';");
                                    httpSession.setAttribute("usertype", "a");
                            }				
                            else {
                                    pw.println("alert('Login Failed!');");
                                    pw.println("location='/clip.jsp';");				
                            }					
			}
			else if(request.getParameter("type").equalsIgnoreCase("c")) {				
                            if((new LoginDao()).login(request.getParameter("inpUsername"),request.getParameter("inpPwd"))) {	
                                    conDTO=new LoginDao().contractorLogin(request.getParameter("inpUsername"));
                                    HttpSession httpSession=request.getSession(true);
                                    httpSession.setAttribute("contractor", conDTO);
                                    pw.println("location='/clipHome.jsp';");
                                    httpSession.setAttribute("usertype", "c");
                            }				
                            else {
                                    pw.println("alert('Login Failed!');");
                                    pw.println("location='/clip.jsp';");				
                            }
			}
                        else {				
                            if((new LoginDao()).loginEmployee(request.getParameter("inpUsername"),request.getParameter("inpPwd")))
                            {	
                                  WorkMenDTO wmDTO=new LoginDao().getWorkmanDTO(request.getParameter("inpUsername"));
                                    HttpSession httpSession=request.getSession(true);
                                    httpSession.setAttribute("employee", wmDTO);
                                    pw.println("location='/Employee/EmployeeHome.jsp';");
                                    httpSession.setAttribute("usertype", "e");
                            }				
                            else {
                                    pw.println("alert('Login Failed!');");
                                    pw.println("location='/clip.jsp';");				
                            }
			}
			pw.println("</script>");
			pw.close();
		}
		else if (action.equalsIgnoreCase("logout")) {
			HttpSession session=request.getSession(false); 
			PrintWriter pw = response.getWriter();
			if(session==null) {
				pw.println(0);
			}
			else {
				session.invalidate();
				pw.println(1);
			}
			pw.close();
		}
		
	}
}
