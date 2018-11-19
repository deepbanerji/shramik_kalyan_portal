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

<script>
var approvalReqdFor=0;
function approveContractor(uid) {
	var r = window.confirm('Do you want to continue saving?');		
	if(r==true) {
		approvalReqdFor=uid;
		$.post("/Contractor",{action:"approveContractor", uid:uid, mobile:"cellMobile"+uid, email:"cellEmail"+uid},updateContractorApprovalResult,'text');
	}
}
function updateContractorApprovalResult(retVal) {
	if(retVal==0) {
		alert('Contractor approval failed!');
	}
	else {
		alert('Contractor approved!');
		document.getElementById('cellApproveBtn'+approvalReqdFor).innerHTML="Approved";
	}
}
function getContractorsList() {
	$.post("/Contractor",{action:"listAll",status:"1"},fillContractorsList,'text');
}
function fillContractorsList(entries) {
	var dataArray = JSON.parse(entries);
	var table=document.getElementById('tblContractors');
		if(dataArray.rows.length>0) {
			var i=0;
			$.each(dataArray.rows, function(index,data) {
				if(data.approvalStatus==1) {
				
				row = table.insertRow(++i);
				row.className="border_bottom";	
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
				var cell11 = row.insertCell(10);			
				var cell12 = row.insertCell(11);	
				//var cell13 = row.insertCell(12);
				
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
				cell11.align="center";
				cell12.align="center";
				//cell13.align="center";

				cell6.id='cellMobile'+data.uid;
				cell7.id='cellEmail'+data.uid;
				cell12.id='cellApproveBtn'+data.uid;
				
				cell1.innerHTML=i;
				cell2.innerHTML=data.name;
				//cell3.innerHTML=data.woref;
				cell3.innerHTML=data.firmname;
                                
                                if(data.firmregno!=null)
                                    cell4.innerHTML=data.firmregno;
                                else
                                    cell4.innerHTML="";
                                
				if(data.landline!=null)
				cell5.innerHTML=data.landline;
                                else
                                cell5.innerHTML="";
                            
				cell6.innerHTML=data.mobile;
                                
				if(data.email!=null)
				cell7.innerHTML=data.email;
                                else
                                    cell7.innerHTML="";
                                
				cell8.innerHTML=data.pan.toString().toUpperCase();
                                
				if(data.aadhar!=null)
				cell9.innerHTML=data.aadhar;
                                else
                                    cell9.innerHTML="";
                                
				if(data.pfno!=null)
                                    cell10.innerHTML=data.pfno;
                                else
                                    cell10.innerHTML="";
                                
				cell11.innerHTML=data.regnDate.toString().split(' ')[0];				
				cell12.innerHTML="<input type='button' onclick=resetContractorPassword('"+data.uid+"','"+data.pan+"') class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green' value='Reset'>";
				}
					
			});
		}
		$("input[type=button]").button();
}
$('#tblContractors tbody tr').hover(function() {
    $(this).addClass('hover');
}, function() {
    $(this).removeClass('hover');
});

function resetContractorPassword(id,pan) {
	var r = prompt("Enter new password (min 8 chars and max 10 chars) for Contractor with PAN "+pan.toString().toUpperCase(),"");
	if (r == null || r=="" || r.length>10 || r.length<8 || r.indexOf(" ")!=-1) {
		alert("Invalid input/operation cancelled!");
		return;
	}
        
	var s = prompt("Re-enter new password for Contractor with PAN "+pan.toString().toUpperCase(),"");
	if (s == null) {
		alert("Invalid input/operation cancelled!");
		return;
	}
	if(r!=s) {
		alert("Passwords do not match");
		return;
	}	
	$.post("/Contractor",{action:"resetContractorPassword",id:id,newpassword:r},function(data,status,xhr){
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
	getContractorsList();
	$("input[type=button]").button();
});
function seaNames() {
  // Declare variables 
  var input, filter, table, tr, td, i;
  input = document.getElementById("seaName");
  filter = input.value.toUpperCase();
  table = document.getElementById("tblContractors");
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
<style>
body {
	width: 100%;
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
</style>
</head>
<body >
<c:if test="${pageContext.session['new']}">
    <c:redirect url="/clip.jsp"/> 
</c:if>
    <div id="loader"></div>
<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0;">
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Change Contractor Password</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>
<input type="text" id="seaName" onkeyup="seaNames()" placeholder="Search names.."></input>
<form>
<table class="w3-table-all w3-card-4 w3-border w3-border-green ui-corner-all" style="font-size: small;margin-top: 5px;width:100%; word-wrap:break-word; table-layout: fixed" id="tblContractors">
	<thead >
		<tr class="w3-pale-green">
			<th>S.No.</th>
			<th>Contractor Name</th>
			
			<th>Firm Name</th>
			<th>Firm Reg.No.</th>
			<th>Landline</th>
			<th>Mobile</th>
			<th>Email</th>
			<th>PAN</th>
			<th>Aadhar</th>
			<th>PF Reg.No.</th>
			<th title="Registration application date and time">Applied On</th>
			<th>Reset Password</th>
		</tr>
	</thead>
	<tbody>
	
	</tbody>
</table>
<br></br>
</form>
</div>
</div>
    <br></br>
</body>
</html>