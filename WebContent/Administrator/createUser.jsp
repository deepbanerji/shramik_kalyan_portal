<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="org.cris.clip.dto.AdministratorDTO"%>
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
<script type="text/javascript" src="/js/common-ajax-calls.js"></script>
<link rel="stylesheet" href="/css/w3.css">
<link rel="stylesheet" href="/css/loader.css"></link>
    <style>
.row {
	margin-top: 1.25%;
	text-align: center;
        font-size: small;
}
.field_set{
  border-color: darkgreen;
  border-style: solid;  
  font-size: small;
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
body {width: 100%;}
</style>
<script>
function validate() {
	
	if(document.getElementById('selLevel').value=="") {
		alert("Please select User Level");
		return;
	}
	if(document.getElementById('selLevel').value=="1" && ($('#selZone').val()=="" || $('#selZone').val()==null)) {
		alert("Please select Zone");
		return;
	}
	if(document.getElementById('selLevel').value=="2" && ($('#selDiv').val()=="" || $('#selDiv').val()==null)) {
		alert("Please select Division");
		return;
	}
	if(document.getElementById('selLevel').value=="3" && ($('#selDept').val()=="" || $('#selDept').val()==null)) {
		alert("Please select Department");
		return;
	}	
	if( document.getElementById('selDesig').value=="" ) {
		alert("Please select Designation");
		return;
	}
	if($('#chkCreateUser').prop("checked")==true && $('#selPriv').val()=="") {
		alert("Please select Privilege that this user can grant");
		return;
	}
	if($('#inpUserid').val().trim()=="") {
		alert("Please enter Userid");
		return;
	}
	if($('#inpPwd').val().trim()=="" || $('#inpConfPwd').val().trim()=="") {
		alert("Please input Password");
		return;
	}
	if($('#inpPwd').val().trim().length<8) {
		alert("Password length is minimum 8 characters");
		return;
	}
	if($('#inpPwd').val().trim()!=$('#inpConfPwd').val().trim()) {
		alert("Passwords do not match");
		return;
	}
	if($('#inpMobile').val()=="") {
		alert("Please enter mobile number");
		return;
	}
        if (!/^[0-9]+$/.test($('#inpMobile').val()) || $('#inpMobile').val().length!==10) {
                alert('Enter valid Mobile number');
		return;
        }
        if($('#inpEmail').val()=="") {
		alert("Please enter email address");
		return;
	}
        if ( !/^[a-z0-9]+[_a-z0-9.-]*[a-z0-9]+@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$/.test($('#inpEmail').val()) ) {
                alert('Enter valid email address');
		return;
        }
	var r = window.confirm('Do you want to continue saving?');		
	if(r==true) {
                $('#selZone').prop('disabled', false);
                $('#selDiv').prop('disabled', false);
                $('#selDept').prop('disabled', false);
                
		$('#form').submit();
                
                //document.getElementById("selZone").disabled = false;
                //document.getElementById("selDiv").disabled = false;
                //document.getElementById("selDept").disabled = false;
        }        
}
function checkUserid() {
        $('#inpUserid').val($('#inpUserid').val().toString().toLowerCase());
	if($('#inpUserid').val().trim()=="") {
		return;
	}	
	$.post("/UtilityServlet",{action:"checkUserid",userid:$('#inpUserid').val().trim()},actOnUserid,'text');
}
function actOnUserid(v) {
	if(v==1) {
		alert('This userid is already taken');
		$('#inpUserid').val("");
                $('#inpUserid').focus();
	}			
}
function fillDesigList(entries) {
    var dataArray = JSON.parse(entries);
	var sml = document.getElementById("selDesig");	
	var opt = document.createElement("option");
	opt.text = "Select";
	opt.value = "";
	opt.disabled = false;
	opt.selected = true;
        $('#selDesig').empty();
	sml.add(opt);
	if (dataArray.rows.length > 0) {
            $.each(dataArray.rows, function(index, data) {
                    opt = document.createElement("option");
                    opt.text = data.shortdesig;
                    opt.value = data.desigid;
                    sml.add(opt);
            });
	}
}
function getDesigList() {    
    if( $('#selLevel').val()=="" ) {
        return;
    }
    if( $('#selZone').val()=="" ) {
        document.getElementById("selLevel").value="";
            return;
    }
    $('#selDesig').empty();
    $.post("/UtilityServlet",{action:"getDesigList",zoneid:$('#selZone').val(),levelid:$('#selLevel').val(),divid:$('#selDiv').val(),deptid:$('#selDept').val()},fillDesigList,'text');
}
function checkMobile() {
	if($('#inpMobile').val().trim()=="") {
		return;
	}
        if (!/^[0-9]+$/.test($('#inpMobile').val()) || $('#inpMobile').val().length!==10) {
                alert('Enter valid Mobile number');
                document.getElementById('inpMobile').style.color='red';
		return;
        }
        else {
            document.getElementById('inpMobile').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkMobile",mobile:$('#inpMobile').val().trim()},actOnMobile,'text');
}
function actOnMobile(v) {
	if(v==1) {
		alert('This Mobile number is already registered');
		$('#inpMobile').val("");
	}
        ajaxCallInProgress=0;
}
function checkEmail() {        
	if($('#inpEmail').val().trim()=="") {
		//alert('Please enter PAN');
		return;
	}
        if ( !/^[a-z0-9]+[_a-z0-9.-]*[a-z0-9]+@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$/.test($('#inpEmail').val()) ) {
                alert('Enter valid email address');
                document.getElementById('inpEmail').style.color='red';
		return;
        }
        else {
            document.getElementById('inpEmail').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkEmail",email:$('#inpEmail').val().trim()},actOnEmail,'text');
}
function actOnEmail(v) {
	if(v==1) {
		alert('This Email id is already registered');
		$('#inpEmail').val("");
	}
        ajaxCallInProgress=0;
}
function enforce(v) {    
        
    if(v==1) {
        document.getElementById("selDiv").value="";
        document.getElementById("selDept").value="";
        
        document.getElementById("selDiv").disabled = true;
        document.getElementById("selDept").disabled = true;
        
    }
    else if(v==2) {  
        if( document.getElementById("userLevel").value=="1" ) {
            document.getElementById("selDiv").value="";
            $('#selDept').empty();
        }    
        document.getElementById("selDept").value="";
        
        if( document.getElementById("userLevel").value!="2" && document.getElementById("userDivHq").value!='d' )
            document.getElementById("selDiv").disabled = false; 
        
        document.getElementById("selDept").disabled = true;
    }
    else if(v==3) {    
        if( document.getElementById("userLevel").value=="1" ) {
            document.getElementById("selDiv").disabled = false;
            document.getElementById("selDept").disabled = false;
        }
        if( document.getElementById("userLevel").value=="2" ) {
            document.getElementById("selDiv").disabled = true;
            document.getElementById("selDept").disabled = false;
        }    
        if( document.getElementById("userLevel").value=="3" && document.getElementById("userDivHq").value=='d' ) {
            document.getElementById("selDiv").disabled = true;
            document.getElementById("selDept").disabled = false;
        }    
        else if( document.getElementById("userLevel").value=="3" && document.getElementById("userDivHq").value=='h' ) {
            document.getElementById("selDiv").disabled = false;
            document.getElementById("selDept").disabled = false;           
        }    
    }
}
function checkDivHq() {    
    if( document.getElementById("selLevel").value!="" ) {        
        if( document.getElementById("selLevel").value=="2" ) { 
            ajaxCallInProgress=1;
            $.post("/UtilityServlet",{action:"checkdivhq",divid:$('#selDiv').val()},function(v) {
                if(v=="h") {
                    alert('HQ cannot be selected for Divisional user level');
                    document.getElementById("selDiv").value="";
                    $('#selDept').empty();
                }
                else {
                    getDepartmentList();
                    getDesigList();
                }
                ajaxCallInProgress=0;    
            } ,'text');            
        }
        else {
            getDepartmentList();
            getDesigList();                
        }
    }
}
$(document).ready(function() {
        $('#loader').hide();
        $(document).ajaxStart(function(){
            $("#loader").css("display", "block");
        });
        $(document).ajaxComplete(function(){
            $("#loader").css("display", "none");
        });

	$('#selPriv').selectmenu({width : 190});
	//$('#selLevel').selectmenu({width : 170});
	$('input[type="checkbox"]').checkboxradio();
	$( "input[type=button]" ).button();
	getZoneList();
});

</script>

</head>
<body>
<%
	AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
	if(adto==null) {
		response.sendRedirect("/clip.jsp");
		return;
	}		
%>
<c:if test="${pageContext.session['new']}">
    <c:redirect url="/clip.jsp"/> 
</c:if>
<div id="loader"></div>
<input type="hidden" id="userDivHq" value="<%=adto.getDivOrHq()%>"/>
<input type="hidden" id="userLevel" value="<%=adto.getLevelId()%>"/>
<input type="hidden" id="userZone" value="<%=adto.getZoneId()%>"/>
<input type="hidden" id="userDiv" value="<%=adto.getDivId()%>"/>
<input type="hidden" id="userDept" value="<%=adto.getDeptId()%>"/>
<form id="form" action="/UtilityServlet?action=createUser" method="post">
<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0;">
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Railway Authority Account Creation</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>
	<fieldset class="field_set"><legend>User Details</legend>		
	<div class="row">
            <label for="selZone" >Zone </label><select id="selZone" name="selZone" onchange="getDivisionList();"></select>
            <label for="selLevel" style="margin-left: 3.5%" >User Level </label><select id="selLevel" name="selLevel" onchange="getDesigList();enforce(this.value);">
                                                <option value="" selected>Select</option>
                                                <%if( adto.getLevelId().equals("0") || adto.getLevelId().equals("1") ) {%>
                                                <option value="1">Zonal</option>
                                                <option value="2">Divisional</option>
                                                <option value="3">Departmental</option>
                                                <%} else if(adto.getLevelId().equals("2")) {%>
                                                <option value="2">Divisional</option>
                                                <option value="3">Departmental</option>
                                                <%} else if(adto.getLevelId().equals("3")) {%>
                                                <option value="3">Departmental</option>
                                                <%} %>
                                                </select>
					
					<label for="selDiv" style="margin-left: 3.5%">Division </label><select id="selDiv" name="selDiv" onchange="checkDivHq(); "></select>
					<label for="selDept" style="margin-left: 3.5%">Department </label><select id="selDept" name="selDept" onchange="getDesigList();"><option value="" selected disabled>Select</option></select>
                                        <label for="selDesig" style="margin-left: 3.5%">Designation </label><select id="selDesig" name="selDesig" ><option value="" selected>Select</option></select>
	</div>
	
	<div class="row">
					<span><input name="chkEnableUser" type="checkbox" id="chkEnableUser"></input><label for="chkEnableUser" style="margin-right: 3.5%">Activate User</label></span>
					<%if(adto.getPrivUserCreate().equals("2")) { %>
					<input id="chkApproveContr" name="chkApproveContr" type="checkbox" ></input><label for="chkApproveContr" style="margin-right: 3.5%">Can Verify Contractor</label>
					<input id="chkApproveWO" name="chkApproveWO" type="checkbox"></input><label for="chkApproveWO" style="margin-right: 3.5%">Can Verify Work Order</label>
					<%} %>	
					<%if(!adto.getPrivUserCreate().equals("0")) { %>				
					<input id="chkCreateUser" name="chkCreateUser" type="checkbox" ></input><label for="chkCreateUser" >Can create Administrator account with</label>
						<select id="selPriv" name="selPriv">	
							<option value="">- Select -</option>						
							<option value="1">View Privilege</option>
							<%if(adto.getPrivUserCreate().equals("2")) { %>
							<option value="2">Verify Privilege</option>
							<%} %>
						</select>
					<%} %>
					<br></br>
	</div>
                                                                    
	</fieldset>
        <fieldset class="field_set"><legend>Contact Details</legend>
            <div class="row">		
                    Mobile <input required style="margin-right: 5%" placeholder="10-digit mobile number" onchange="checkMobile()" id="inpMobile" type="text" name="inpMobile" maxlength="10"></input>
                    Email <input required maxlength="40" onchange="checkEmail()" id="inpEmail" type="email" name="inpEmail" ></input>
                    <br></br>
            </div>
	</fieldset>
	<fieldset class="field_set"><legend>Login Credentials</legend>
	<div class="row">		
		Userid <input  id="inpUserid" name="inpUserid" style="width: 18%;margin-right: 5%" onblur="checkUserid()" maxlength="10"></input>
		Password <input type="password" id="inpPwd" name="inpPwd" style="width: 18%;margin-right: 5%" maxlength="10"></input>
		Re-enter Password <input type="password" id="inpConfPwd" name="inpConfPwd" style="width: 18%" maxlength="10"></input>
		<br></br>
	</div>
	</fieldset>
        
	<br>
	<center><input type="button" class='w3-btn w3-round-xlarge w3-card-2 w3-pale-green w3-hover-green' onclick="validate();" style="margin-left: 0.25%" value=" Submit "></input></center>
        <br>
	
</div>
</div>
</form>
</body>
</html>