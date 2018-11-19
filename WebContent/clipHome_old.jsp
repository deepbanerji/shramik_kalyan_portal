<%@ page language="java" contentType="text/html; charset=ISO-8859-1"   pageEncoding="ISO-8859-1"%>
<%@ page import="org.cris.clip.dto.ContractorDTO" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>IR Shramik Kalyan Portal</title>
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<link rel="icon" href="/img/irlogo1.png">
<script type="text/javascript">

</script>



</head>
<%
HttpSession httpSession=request.getSession(false);
ContractorDTO conDTO=(ContractorDTO) httpSession.getAttribute("contractor");
if(conDTO==null)
{
	System.out.println("invalid dto");   
 %>
  <script type="text/javascript"> 
    alert("Session timed out!");
	window.parent.location='/clip.jsp'; </script>
  
  <%

}
%>

 <frameset rows="8%,86%,*"  FRAMEBORDER="no" BORDER="0" framespacing="0">

  <frame name="header" title='Header Frame' scrolling="no" noresize src="./banner.jsp" marginheight="0" marginwidth="10">         
  
  
  <frameset cols="18%,*">
		  	<FRAME title='Index Frame' src="./index.jsp" name="index_tree"  noresize marginwidth="10" marginheight="0" >
		
		 <% if(conDTO!=null)
		    { if(conDTO.getApprovalStatus().equals("1")) { %> 	
	    	<FRAME id="detail" title='Contractor Home' SRC="./WorkMen/WorkManHome.jsp"  noresize marginwidth="10" marginheight="0" name="detail" class="ui-corner-all">
	     <%  }  } %>
  </frameset>  
 
  <frame name="Footer" title='Footer Frame' scrolling="no" noresize src="./FooterFrame.jsp" marginheight="0" marginwidth="10" >
	
</frameset>


</html>