<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="org.cris.clip.dto.AdministratorDTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>Insert title here</title>
<script type="text/javascript" src="/jquery-2.0.0.min.js"></script>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<link rel="stylesheet" href="/css/loader.css"></link>
    <script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<link rel="stylesheet" href="/css/w3.css">
<style>
.field_set{
  border-color: darkgreen;
  border-style: solid;
}
input[type=password] {
	border: 1px solid #ccc;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
	border-radius: 10px;
	-moz-box-shadow: 2px 2px 3px #666;
	-webkit-box-shadow: 2px 2px 3px #666;
	box-shadow: 2px 2px 3px #666;
	font-size: 15px;
	padding: 4px 7px;
	outline: 0;
	-webkit-appearance: none;
}
input[type=password]:focus {
	border-color: #339933;
}
</style>
<script>
function validate(){
	if($('#inpPwd').val().trim()=="" || $('#inpPwd').val().trim().length<8) {
		alert('Enter Password of minimum 8 characters');
		return;
	}
	if($('#inpConfPwd').val().trim()=="" || $('#inpPwd').val().trim()!=$('#inpConfPwd').val().trim()) {
		alert('Re-entered password does not match');
		return;
	}
	$.post("/UtilityServlet",{action:"changeAdminPassword",id:document.getElementById('id').value,newpassword:document.getElementById('inpPwd').value},function(data,status,xhr){
		if(data==1) {
			alert("Password changed!");
			$('#inpPwd').val("");
			$('#inpConfPwd').val("");
		}
		else
			alert("Password change failed!");
		},'text');
}
$(function(){
        $('#loader').hide();
	$(document).ajaxStart(function(){
            $("#loader").css("display", "block");
        });
        $(document).ajaxComplete(function(){
            $("#loader").css("display", "none");
        });

});
</script>
</head>
<body >
<%
	AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
	if(adto==null) {
		response.sendRedirect("/clip.jsp");
		return;
	}
	String level[] = {"SUPER-ADMIN","ZONAL","DIVISIONAL","DEPARTMENTAL"};
	String privilege[] = {"NO","YES"};	
	String admin[] = {"NO","YES - ONLY VIEW PRIVILEGE","YES - VIEW & VERIFY PRIVILEGES"};
%>
<c:if test="${pageContext.session['new']}">
    <c:redirect url="/clip.jsp"/> 
</c:if>
    <div id="loader"></div>
<input type="hidden" id="id" value="<%=adto.getUserId()%>"></input>
<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0">
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4><b>My Profile</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>	
<fieldset class="field_set">
	<table class="w3-table-all w3-card-4 w3-border w3-border-green ui-corner-all" style="width:100%;">
		<tbody>
			<tr>
                            <td align="center"><strong>Userid</strong></td><td><%=adto.getUserId() %></td>
                            <td align="center"><strong>Level</strong></td><td><%=level[Integer.parseInt(adto.getLevelId())] %></td>
                            <td align="center"><strong>Designation</strong></td>
                            <%if(adto.getLevelId().equals("1")) { %>                                
                                <td align="center"><%=adto.getDesigCode()%>&nbsp;(<%=adto.getZoneName()%>
                                    <% if(adto.getDivisionName()!=null) { %>
                                    /<%=adto.getDivisionName() %>
                                    <%}%>
                                    <% if(adto.getDeptName()!=null) { %>
                                    /<%=adto.getDeptName() %>
                                    <%}%>)
                                </td>
                            <%}
                            else if(adto.getLevelId().equals("2")){ %>
                                <td align="center"><%=adto.getDesigCode()%>&nbsp;(<%=adto.getZoneName()%>/<%=adto.getDivisionName() %>)</td>
                            <%}
                            else if(adto.getLevelId().equals("3")){ %>
                                <td align="center"><%=adto.getDesigCode()%>&nbsp;(<%=adto.getZoneName()%>/<%=adto.getDivisionName() %>/<%=adto.getDeptName() %>)</td>
                            <%}
                            else if(adto.getLevelId().equals("0")){ %>
                                <td align="center">ALL ZONES</td>
                            <%} %>
				
			</tr>
			<tr>
                            <td align="center"><strong>Work Order Verification Privilege</strong></td><td><%=privilege[Integer.parseInt(adto.getPrivWoApprove())] %></td>
                            <td align="center"><strong>Contractor Verification Privilege</strong></td><td><%=privilege[Integer.parseInt(adto.getPrivContrApprove())] %></td>
                            <td align="center"><strong>Administrator Creation Privilege</strong></td><td><%=admin[Integer.parseInt(adto.getPrivUserCreate())] %></td>
			</tr>
		</tbody>
	</table>
</fieldset>

</div>
<br></br>
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h5> <b>Change Password</b></h5></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>	
<fieldset class="field_set"><legend>Enter New Password</legend>
	Password: <input type="password" id="inpPwd" name="inpPwd" maxlength="50" style="width: 15%;margin-right: 3%"></input>
	Confirm Password: <input maxlength="50" type="password" id="inpConfPwd" name="inpConfPwd" style="width: 15%;margin-right: 3%"></input>
	<input type="button" class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green' onclick="validate();" value="Submit"></input>
</fieldset>
<br></br>
</div>

</div>
                        <br></br>
</body>
</html>