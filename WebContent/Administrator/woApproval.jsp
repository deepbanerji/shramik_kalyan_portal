<%@page import="org.cris.clip.dto.AdministratorDTO"%>
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script type="text/javascript">
var approvalReqdFor=0;
function approveWorkOrder(uid,val,wono) {
    var r;
    if(val==1) 
        r = window.confirm("Are you sure you want to Verify LOA "+wono+"?");		
    else
        r = window.confirm("Are you sure you want to Reject LOA "+wono+"?");
    if(r==true) {
        approvalReqdFor=uid;
        $.post("/UtilityServlet",{action:"approveWO",value:val,uid:uid, mobile:document.getElementById("cellMobile"+uid).innerHTML, email:document.getElementById("cellEmail"+uid).innerHTML, wono: document.getElementById("cellWoNo"+uid).innerHTML},updateWOApprovalResult,'text');
    }
}
function updateWOApprovalResult(retVal) {
    if(retVal==0) {
        alert('Operation failed!');
    }
    else {
        alert('Operation successful!');
        //document.getElementById('cellApproveBtn'+approvalReqdFor).innerHTML="Verified";
        getWoList();
    }
}
function getWoList() {
        $("#tblWorkOrders").find("tr:gt(0)").remove();
	$.post("/UtilityServlet",{action:"listWOs"},fillWoList,'text');
}
function fillWoList(names) {
	var dataArray = JSON.parse(names);
	var table=document.getElementById('tblWorkOrders');
		if(dataArray.rows.length>0) {
			var i=0;
			$.each(dataArray.rows, function(index,data) {
				if(data.approvalStatus==0) {
				row = table.insertRow(++i);	
				var cell1 = row.insertCell(0);			
				var cell2 = row.insertCell(1);
				var cell3 = row.insertCell(2);
				var cell4 = row.insertCell(3);
				var cell5 = row.insertCell(4);			
				var cell6 = row.insertCell(5);
				var cell7 = row.insertCell(6);			
				var cell8 = row.insertCell(7);
				var cell9 = row.insertCell(8);
				var cell10 = row.insertCell(9);
								
				cell1.align="center";
				cell2.align="center";
				cell3.align="center";
				cell4.align="center";
				cell5.align="center";
				cell6.align="center";
				cell7.align="center";
				cell8.align="center";
                                cell9.align="center";
                                cell10.align="center";
				
				cell3.id='cellWoNo'+data.woId;
                                cell7.id='cellEmail'+data.woId;
				cell8.id='cellMobile'+data.woId;				
				cell9.id='cellApproveBtn'+data.woId;
				
				cell1.innerHTML=i;                                
								
				if(data.workName.length>100) {
                                    cell2.innerHTML="<span title='"+data.workName+"'>"+data.workName.substr(0,100)+"...</span>";
                                }
                                else {
                                    cell2.innerHTML=data.workName;
                                }
            
                                if(data.wofilenum=="0")
                                    cell3.innerHTML=data.woNo;
				else
					//cell3.innerHTML="<a title='Download' href=/UtilityServlet?action=download&id="+data.wofilenum+">"+data.woNo+"</a>";
                                    cell3.innerHTML="<a title='Show'  style='cursor:pointer' onclick=window.open('/Contractor?action=fetchWO&wofile="+data.wofilenum+"','')>"+data.woNo+"</a>";            
				
                                //cell4.innerHTML=data.zone;
                                //if(data.div!=null)        
                                //cell4.innerHTML = cell4.innerHTML +"/"+data.div;
                                //if(data.dept!=null)
                                //cell4.innerHTML = cell4.innerHTML +"/"+data.dept;
                                cell4.innerHTML=data.verAppliedToPost;
                                
				cell5.innerHTML=data.prinEmp;
				cell6.innerHTML=data.contname;
                                
                                if(data.contemail!=null)
				cell7.innerHTML=data.contemail;
                                else
                                cell7.innerHTML="";
                                
                                cell8.innerHTML=data.contmobile;
                                if(data.verAppliedToId==document.getElementById('hidAdminId').value)
                                    x = "enabled";
                                else
                                    x = "disabled";
				cell9.innerHTML="<input type='button' "+x+" class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green' onclick=approveWorkOrder('"+data.woId+"',1,'"+data.woNo+"') value='Verify'>";
                                cell10.innerHTML="<input type='button' "+x+" class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green' onclick=approveWorkOrder('"+data.woId+"',2,'"+data.woNo+"') value='Reject'>";
				}
			});
		}
}
$(function(){
    $('#loader').hide();
    $(document).ajaxStart(function(){
        $("#loader").css("display", "block");
    });
    $(document).ajaxComplete(function(){
        $("#loader").css("display", "none");
    });
    getWoList();
    $("input[type=button]").button();
});
function seaWOs() {
  // Declare variables 
  var input, filter, table, tr, td, i;
  input = document.getElementById("seaWO");
  filter = input.value.toUpperCase();
  table = document.getElementById("tblWorkOrders");
  tr = table.getElementsByTagName("tr");

  // Loop through all table rows, and hide those that don't match the search query
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[2];
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
<style>
body {	
	width: 100%;
}
a {color: blue;}
a:visited {color: blue}
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
</style>
</head>
<body >
<c:if test="${pageContext.session['new']}">
    <c:redirect url="/clip.jsp"/> 
</c:if>
    <div id="loader"></div>
<%
    AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
    if(adto==null) {
        response.sendRedirect("/clip.jsp");
        return;
    }
%>
<input type='hidden' value='<%=adto.getUserId()%>' id='hidAdminId'></input>
<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0;">
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Letter of Acceptance (LOA) Verification</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>
<input type="text" id="seaWO" onkeyup="seaWOs()" placeholder="Search LOA nos.."></input>
<table id="tblWorkOrders" class="w3-table-all w3-card-4 w3-border w3-border-green ui-corner-all" style="font-size: small;margin-top: 5px;width:100%; word-wrap:break-word; table-layout: fixed">
    <thead>
        <tr class="w3-pale-green">
            <th>S.No.</th>
            <th>Work Name</th>
            <th>LOA Number</th>
            <th>Applied To</th>
            <th>Principal Employer</th>
            <th>Contractor Name</th>
            <th>Contractor Email</th>
            <th>Contractor Mobile</th>
            <th></th>
            <th></th>
        </tr>
    </thead>
    <tbody>	
    </tbody>
</table>
<br></br>
</div>
</div>
    <br></br>
</body>
</html>