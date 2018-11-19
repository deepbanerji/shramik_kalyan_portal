<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%> 
<%@ page import="org.cris.clip.dto.ContractorDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/css/w3.css">
<SCRIPT type="text/javascript" src="/contractor/theme/json2.js"></SCRIPT>
<SCRIPT type="text/javascript" src="/jquery-2.0.0.min.js"></SCRIPT>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script type="text/javascript">

function validate() {
	 if (confirm("Do you want to save new work order?") == false) 
   		return false; 
	else 		
		return true;	
}	

function getZone()
{
	$('input[type=button]').button();
	$('input[type=submit]').button();
	$('#WODIV').selectmenu();
	$('#WODEPT').selectmenu();
	$('#WOAREA').selectmenu();
	$.post("/UtilityServlet",{action:"getZoneList"},fillZone,'text');
	}

function fillZone(list)
{
	var dataArray = JSON.parse(list);
	if(dataArray!=null) {
		$("#WOZONE").contents().remove();
		$("#WOZONE").append("<option value=''>- Select -</option");
		$.each(dataArray, function(index,data) {
		    $("#WOZONE").append("<option value="+data.option+">" + data.display.toUpperCase() + "</option");
	  	});		
	}
	else
		alert("Could not load device types!");
	$('#WOZONE').selectmenu({change: function(event, ui) { getDivision(); } });
}

function getDivision()
{
  //	alert("getDivision");
	$.post("/UtilityServlet",{action:"getDivision",zone:$("#WOZONE").val()},fillDivision,'text');
	}

function fillDivision(list)
{
	var dataArray = JSON.parse(list);
	if(dataArray!=null) {
		$("#WODIV").contents().remove();
		$("#WODIV").append("<option value=''>- Select -</option");
		$.each(dataArray, function(index,data) {
		    $("#WODIV").append("<option value="+data.option+">" + data.display.toUpperCase() + "</option");
	  	});		
	}
	else
		alert("Could not load device types!");
	$('#WODIV').selectmenu({change: function(event, ui) { getDepartmentList(); } }).selectmenu("refresh");
}

function getDepartmentList()
{
  //	alert("getDivision");
	$.post("/UtilityServlet",{action:"getDepartmentList",div:$("#WODIV").val()},fillDepartmentList,'text');
	}

function fillDepartmentList(list)
{
	var dataArray = JSON.parse(list);
	if(dataArray!=null) {
		$("#WODEPT").contents().remove();
		$("#WODEPT").append("<option value=''>- Select -</option");
		$.each(dataArray, function(index,data) {
		    $("#WODEPT").append("<option value="+data.option+">" + data.display.toUpperCase() + "</option");
	  	});		
	}
	else
		alert("Could not load device types!");
	$('#WODEPT').selectmenu("refresh");
}


function validate_required(field,alerttxt) {
  //	alert("validate_required");
	  with (field){
	      if (value==null||value=="") {
	      	alert(alerttxt);
	      	return false;
	      }
	   
	      else {
	      	return true;
	      }
	  }
	}

	function validate_form(thisform) {
	  //	alert("validate_form");
	  //	alert( $("#WorkOrd").val());
	  with (thisform){
		if (validate_required(WorkOrd,"WORK ORDER NUMBER CAN'T BE BLANK!!")==false){WorkOrd.focus();return false}
		if (validate_required(WorkName,"WORK ORDER NAME CAN'T BE BLANK!!")==false){WorkName.focus();return false}
		if (validate_required(StartDate,"WORK ORDER COMMENCEMENT DATE CAN'T BE BLANK!!")==false){StartDate.focus();return false}
		if (validate_required(EndDate,"WORK ORDER COMPLETION DATE CAN'T BE BLANK!!")==false){EndDate.focus();return false}
		if (validate_required(WOZONE,"WORK ORDER ZONE CAN'T BE BLANK!!")==false){WOZONE.focus();return false}
		if (validate_required(WODIV,"WORK ORDER DIVISION CAN'T BE BLANK!!")==false){WODIV.focus();return false}
		if (validate_required(WODEPT,"WORK ORDER DEPARTMENT CAN'T BE BLANK!!")==false){WODEPT.focus();return false}
		if (validate_required(WOAREA,"WORK ORDER AREA CAN'T BE BLANK!!")==false){WOAREA.focus();return false}
		if($("#EndDate").val()<$("#StartDate").val()){alert("WORK ORDER COMPLETION DATE CAN'T BE LESS THAN WORK ORDER COMMENCEMENT DATE");$("#EndDate").focus();return false}
		if($("#ExtDate").val()<$("#EndDate").val()){alert("WORK ORDER COMPLETION DATE CAN'T BE LESS THAN WORK ORDER EXTENDED COMPLETION DATE");$("#ExtDate").focus();return false}
	  }	
	}


</script>

<style type="text/css">

 html, body{
   height:100%;
   width: 100%; 
   
  }
  

table.gridtable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #f8f5f9; 
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
		color: grey;		
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

input {
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
input:focus {
	border-color: #339933;
}



</style>


<title>Insert title here</title>
</head>
<%

HttpSession httpSession=request.getSession(false);
ContractorDTO conDTO=(ContractorDTO) httpSession.getAttribute("contractor");

%>

<body onload="getZone();">

	<form name="NewWOForm" id="NewWOForm" action="/Contractor?action=addNewWO" onsubmit="return validate_form(this);" method="post">
	<div class="w3-panel w3-pale-blue ui-corner-all" style="height: 100%;width: 100%;margin: 0" >	
	<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>New Work Order</b></h4></div>
	 
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font style="color:red;font-size:12">*&nbsp;Mandatory</font>	
	<br>
	<table  class="hoverTable" >
		
	<TBODY>
		<tr>
			<td>
				<table style="width:100%;font-size:14px" >
				 <!-- 	<tr>
						<td colspan="2" width="100%" align="right"><font style="color:red;font-size:12">*&nbsp;Mandatory</font></td>
					</tr>
					<tr>
						<td width="50%" >Contractor Details</td>
						<td width="50%">
						 <textarea style="background-color:lightgray" name="ContraDet" maxlength="100" id="ContraDet" rows="3" cols="80" readonly="readonly"> 
						   <%=conDTO.getName().toUpperCase()%>,<%=conDTO.getFirmName().toUpperCase()%>						 
						 </textarea>						
						</td>
					</tr>  -->
		
					<tr>
						<td width="50%" >Work Order No <font style="color:red;font-size:12">*</font></td>
						<td width="50%"><input style="background-color:lightgray" name="WorkOrd" maxlength="100" id="WorkOrd" size="100" type="text" value="" /></td>
					</tr>
					<tr>
						<td width="50%" >Name of the Work <font style="color:red;font-size:12">*</font></td>
						<td width="50%"><input style="background-color:lightgray" name="WorkName" maxlength="100" id="WorkName" size="100" type="text" value="" /></td>
					</tr>
					<tr>
						<td width="50%" >Location of the Work</td>
						<td width="50%"><input style="background-color:lightgray" name="LocName" maxlength="100" id="LocName" size="100" type="text" value="" /></td>
					</tr>						
					<tr>
						<td width="50%" >Date of Commencement of Work <font style="color:red;font-size:12">*</font></td>
						<td><input id="StartDate" name="StartDate" type="date"></td>
							</tr>
						<tr>
					    <td width="50%" >Date of Completion of Work <font style="color:red;font-size:12">*</font></td>
						<td><input id="EndDate" name="EndDate" type="date"></td>
						</tr>
						<tr>
					    <td width="50%" >Extended Date of Completion of Work</td>
						<td><input id="ExtDate" name="ExtDate" type="date"></td>
						</tr>
						<tr>
						<td width="50%" >Max No of Employees</td>
						<td width="50%"><input style="background-color:lightgray" name="MaxEmp" maxlength="20" id="MaxEmp" size="20" type="text" value="0" /></td>
					</tr>
					<tr>
						<td width="50%" >Work Order Issued By</td>
						<td width="50%"><input style="background-color:lightgray" name="IssBy" maxlength="100" id="IssBy" size="100" type="text" value="" /></td>
					</tr>
					<tr>
						<td width="50%" >Labour License</td>
						<td width="50%"><input style="background-color:lightgray" name="LabLic" maxlength="100" id="LabLic" size="100" type="text" value="" /></td>
					</tr>
					<tr>
						<td width="50%" >Name of the Principal Employer</td>
						<td width="50%"><input style="background-color:lightgray" name="PrincEmp" maxlength="100" id="PrincEmp" size="100" type="text" value="" /></td>
					</tr>
					<tr>
						<td width="50%" >Principal Employer Address</td>
						<td width="50%"><input style="background-color:lightgray" name="PrincEmpAdd" maxlength="100" id="PrincEmpAdd" size="100" type="text" value="" /></td>
					</tr>
				<!-- 	<tr>
						<td width="50%" >Contract Type</td>
						<td>
							<select id="ConType" name="ConType">
								<option value="">-Select Type-</option>
								
							</select>
								
						</td>
						</tr>	 -->					
					<tr>
						<td width="50%" >Work Order Issuing Establishment <font style="color:red;font-size:12">*</font></td>
						<td>Zone
							<select id="WOZONE" name="WOZONE" onchange="getDivision();">
								<option value="">-Select-</option>
							</select>
							Division
							<select id="WODIV" name="WODIV" onchange="getDepartmentList();">
								<option value="">-Select-</option>
							</select>	
							Department
							<select id="WODEPT" name="WODEPT">
								<option value="">-Select-</option>
							</select>										
						</td>		
						</tr>
						<tr>
						<td width="50%" >Work Order Area <font style="color:red;font-size:12">*</font> </td>
						<td>
							 <select id="WOAREA" name="WOAREA">
							 <option value="">-Select-</option>
							 <option value="A">A</option>
							 <option value="C">B</option>
							 <option value="C">C</option>
							</select>								
						</td>		
						</tr>
						</table>
						</td>
						</tr>
						<TR>
						<td align="center"><input type="submit" value="Submit"/> </td>
					</TR>
			</TBODY>
		</table>
	 </div>	
	</form>
</body>
</html>