<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="java.util.Iterator"%>
<%@page import="org.cris.clip.dao.UtilityDao"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.cris.clip.connection.DBConnection"%>
<%@page import="java.sql.Connection"%>
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
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<link rel="stylesheet" href="/css/loader.css"></link>
<link rel="stylesheet" href="/css/w3.css">
<style>
    body {width: 100%;}
.field_set{
  border-color: white;
  border-style: solid;
}
input[type=text] {
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
input[type=text]:focus {
	border-color: #339933;
}
th:first-child {
    border-radius: 6px 0 0 0;
}

th:last-child {
    border-radius: 0 6px 0 0;
}

th:only-child{
    border-radius: 6px 6px 0 0;
}
.hoverTable{
		width:100%; 
		border-collapse:collapse; 
		color: #2F4F4F;		
	}
	.hoverTable td{ 
		padding:7px; border:#4e95f4 1px solid;
	}
	/* Define the default color for all the table rows */
	.hoverTable tr{
		background: white;
	}
	/* Define the hover highlight color for the table row */
    .hoverTable tr:hover {
          background-color: #ffff99;
    }
    tr {
        height: 3.4em;
    }
    tr td {
        text-align: left;  
        
    }
    
    td p {
        margin: 0;
        padding: 0;
    }
</style>
<script>
function modifyCreateAdminPriv(id,status) {
    $.post("/UtilityServlet",{action:"modifyCreateAdminPriv",id:id,status:status},function(data,status,xhr){
        if(data==1) {
                alert("Operation successful!");
        }
        else {
                alert("Operation failed!");
                location = "/Administrator/userMgt.jsp";
            }
        },'text');
}
function modifyContApprPriv(id,status) {
    $.post("/UtilityServlet",{action:"modifyContApprPriv",id:id,status:status},function(data,status,xhr){
        if(data==1) {
                alert("Operation successful!");
        }
        else {
                alert("Operation failed!");
                location = "/Administrator/userMgt.jsp";
            }
        },'text');
}
function modifyWOApprPriv(id,status) {
    $.post("/UtilityServlet",{action:"modifyWOApprPriv",id:id,status:status},function(data,status,xhr){
        if(data==1) {
                alert("Operation successful!");
        }
        else {
                alert("Operation failed!");
                location = "/Administrator/userMgt.jsp";
            }
        },'text');
}
function enableDisableAdmin(id,status) {
    var r = window.confirm("Are you sure you want to disable user "+id+"?");
    if(r==false)
        return;
    else {
        $.post("/UtilityServlet",{action:"enableDisableAdmin",id:id,status:status},function(data,status,xhr){
		if(data==1) {
			alert("Operation successful!");
                        location = "/Administrator/userMgt.jsp";
                }
                else
			alert("Operation failed!");
		},'text');
    }
}
function resetSubAdminPassword(id) {
	var r = prompt("Enter new password (10 chars max) for "+id,"");
	if (r == null || r=="" || r.length>10) {
		alert("Invalid input/operation cancelled!");
		return;
	}
	var s = prompt("Re-enter new password for id "+id,"");
	if (s == null || s=="" || s.length>10) {
		alert("Invalid input/operation cancelled!");
		return;
	}
	if(r!=s) {
		alert("Passwords do not match");
		return;
	}
	$.post("/UtilityServlet",{action:"resetSubAdminPassword",id:id,newpassword:r},function(data,status,xhr){
		if(data==1)
			alert("Password reset successful!");
		else
			alert("Password reset failed!");
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
    $("input[type=button]").button();
});
function seaUserids() {
  // Declare variables 
  var input, filter, table, tr, td, i;
  input = document.getElementById("seaUserid");
  filter = input.value.toUpperCase();
  table = document.getElementById("tblAdmins");
  tr = table.getElementsByTagName("tr");

  // Loop through all table rows, and hide those who don't match the search query
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[1];
    if (td) {
      if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    } 
  }
}
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
	ArrayList al = new UtilityDao().adminUserList(adto);
	Iterator i = al.iterator();
%>
<c:if test="${pageContext.session['new']}">
    <c:redirect url="/clip.jsp"/> 
</c:if>
<div id="loader"></div>
<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0;">
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Railway Authority Account Management</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>
<input type="text" id="seaUserid" onkeyup="seaUserids()" placeholder="Search User id..."></input>
<form>	
<table id="tblAdmins" class="w3-table-all w3-card-4 w3-border w3-border-green" style="margin-top: 5px;width:100%;font-size: small;table-layout: fixed;word-wrap: break-word">
	<thead>
		<tr class="w3-pale-green">
                        <th style="width:4%">#</th>
			<th>Rly. Auth. ID</th>                        
                        <th style="width:8%">Level</th>
                        <th>Designation</th>
			<th>Domain</th>
                        <th style="width: 16%">Privileges</th>
                        <th style="width:7%">Mobile</th>
                        <th>Email</th>
			<th>Password Change</th>
                        <th style="width:7%">Status</th>
		</tr>
	</thead>
	<tbody>
	<%	int j = 0;
		while(i.hasNext()) { 
			String a[] = (String[])i.next();
                        if( a[12].equals("0") ) {
                            continue;
                        }
                        String privContApprv="",privWOApprv="",privAccCreate="",privAccR="",privAccA="",chgPwd="",btnEnaDis="";
                        if( a[0].equals(adto.getUserId()) ) {
                            privAccCreate = "disabled";
                            privWOApprv = "disabled";
                            privContApprv = "disabled";
                            privAccR = "disabled";
                            privAccA = "disabled";
                        }
                        else {
                            if(adto.getPrivUserCreate().equals("0")) {
                                privAccCreate = "disabled";
                                privWOApprv = "disabled";
                                privContApprv = "disabled";
                                privAccR = "disabled";
                                privAccA = "disabled";
                                chgPwd = "disabled";
                                btnEnaDis = "disabled";
                            }    
                            else if(adto.getPrivUserCreate().equals("1")) {
                                privAccA = "disabled";
                                privContApprv = "disabled";
                                privWOApprv = "disabled";
                            }
                        }
        %>
			<tr>
                            <td><p><%=++j%>.</p></td>
                                
                            <td><p><%=a[0]%></p></td>
                            <td><p><%=level[Integer.parseInt(a[3])]%></p></td> 
                            <td><p><%=a[9]%></p></td>
                            <td width="22%">
                                    <%if(a[3].equals("1")) { %>
                                    <p><%=a[4]%>                                    
                                        <% if(a[1]!=null) { %>
                                        /<%=a[1] %>
                                        <%}%>
                                        <% if(a[2]!=null) { %>
                                        /<%=a[2] %>
                                        <%}%>
                                    </p>
                                    
                                    <%}else if(a[3].equals("2")){ %>
                                    <p><%=a[4]%>/<%=a[1] %></p>
                                    
                                    <%}else if(a[3].equals("3")){ %>
                                    <p><%=a[4]%>/<%=a[1] %>/<%=a[2] %></p>
                                    <%} %>
                                </td>
                                
                                <td width="18%">
                                    <% if(a[6].equals("1")) { %>
                                    <input class="w3-check" type="checkbox" value="1" <%=privContApprv%> checked onchange="modifyContApprPriv('<%=a[0]%>',this.checked)"></input>
                                    <% } else { %>
                                        <input class="w3-check" type="checkbox" value="1" <%=privContApprv%> onchange="modifyContApprPriv('<%=a[0]%>',this.checked)"></input>
                                    <% } %>
                                    <label>Contractor Approval</label>
                                    <br>
                                    <% if(a[7].equals("1")) { %>
                                        <input class="w3-check" type="checkbox" value="1" <%=privWOApprv%> checked onchange="modifyWOApprPriv('<%=a[0]%>',this.checked)"></input>
                                    <% } else { %>
                                        <input class="w3-check" type="checkbox" value="1" <%=privWOApprv%> onchange="modifyWOApprPriv('<%=a[0]%>',this.checked)"></input>
                                    <% } %>
                                    <label>WO Approval</label>
                                    <br>
                                    <% if(a[8].equals("0")) { %>
                                        <input name="createUser<%=a[0]%>" class="w3-radio" type="radio" value="1" <%=privAccR%> onchange="modifyCreateAdminPriv('<%=a[0]%>','1')"></input> <label>Create User - View</label><br>
                                        <input name="createUser<%=a[0]%>" class="w3-radio" type="radio" value="2" <%=privAccA%> onchange="modifyCreateAdminPriv('<%=a[0]%>','2')"></input> <label>Create User - Approve & View</label><br>
                                        <input name="createUser<%=a[0]%>" class="w3-radio" type="radio" value="0" <%=privAccCreate%> checked onchange="modifyCreateAdminPriv('<%=a[0]%>','0')"></input> <label>Cannot Create User</label><br>
                                    <% } else if(a[8].equals("1")) { %>
                                        <input name="createUser<%=a[0]%>" class="w3-radio" type="radio" value="1" <%=privAccR%> checked onchange="modifyCreateAdminPriv('<%=a[0]%>','1')"></input> <label>Create User - View</label><br>
                                        <input name="createUser<%=a[0]%>" class="w3-radio" type="radio" value="2" <%=privAccA%> onchange="modifyCreateAdminPriv('<%=a[0]%>','2')"></input> <label>Create User - Approve & View</label><br>
                                        <input name="createUser<%=a[0]%>" class="w3-radio" type="radio" value="0" <%=privAccCreate%> onchange="modifyCreateAdminPriv('<%=a[0]%>','0')"></input> <label>Cannot Create User</label><br>
                                    <% } else if(a[8].equals("2")) { %>
                                        <input name="createUser<%=a[0]%>" class="w3-radio" type="radio" value="1" <%=privAccR%> onchange="modifyCreateAdminPriv('<%=a[0]%>','1')"></input> <label>Create User - View</label><br>
                                        <input name="createUser<%=a[0]%>" class="w3-radio" type="radio" value="2" <%=privAccA%> checked onchange="modifyCreateAdminPriv('<%=a[0]%>','2')"></input> <label>Create User - Approve & View</label><br>
                                        <input name="createUser<%=a[0]%>" class="w3-radio" type="radio" value="0" <%=privAccCreate%> onchange="modifyCreateAdminPriv('<%=a[0]%>','0')"></input> <label>Cannot Create User</label><br>
                                    <% } %>
                                                                                                           
                                </td>
                                <td><p><%=a[10]%></p></td>
                                <td><p><%=a[11]%></p></td>
                                <td><p><input onclick="resetSubAdminPassword('<%=a[0]%>');" <%=chgPwd%> class='w3-btn w3-round-xlarge w3-card-2 w3-pale-green w3-hover-green' type="button" value="Change Password"></input></p></td>
                                
                                <%if(a[5].equals("0")) {%>
                                <td><p><input onclick="enableDisableAdmin('<%=a[0]%>','1');" <%=btnEnaDis%> class='w3-btn w3-round-xlarge w3-card-2 w3-pale-green w3-hover-green' type="button" value="Enable"></input></p></td>
                                <%}else if(a[5].equals("1") && !a[0].equals(adto.getUserId())) {%>
                                <td><p><input  onclick="enableDisableAdmin('<%=a[0]%>','0');" <%=btnEnaDis%> class='w3-btn w3-round-xlarge w3-card-2 w3-pale-red w3-hover-red' type="button" value="Disable"></input></p></td>
                                <%}else {%>
                                <td><p><strong>You!</strong></p></td>
                                <%}%>
			</tr>	
	<%	
		}
	%>
	
	</tbody>
</table>
<br></br>
</form>
</div>
</div>
        <br></br>
</body>
</html>