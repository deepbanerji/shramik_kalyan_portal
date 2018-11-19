<?xml version="1.0"?>
<%@ page language="java" contentType="text/html;"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Contractor Registration</title>
<script type="text/javascript" src="/jquery-2.0.0.min.js"></script>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script type="text/javascript" src="/js/common-ajax-calls.js"></script>
<link rel="stylesheet" href="/css/loader.css"></link>
<link rel="stylesheet" href="/css/w3.css">
<link rel="icon" href="/img/irlogo1.png">
<style>
input[type=text],input[type=number],input[type=date],input[type=password],input[type=email] {
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
.cellHeading {
	display: inline-block;
	width: 10%;
	vertical-align: top;        
        text-align: center
}
.cellInput {
	display: inline-block;
	width: 40%;
	vertical-align: top;
}
input {
	width: 99%;
}
.row {
	margin-top: 1.38%;
}
body {width:100%;height:100%;background-image: url("/img/background.png");}
.field_set{
  border-color: darkgreen;
  border-style: solid;
}
#header {position: relative;top: 0;background-color: darkgreen;color: white;padding:5px} 
#footer {background-color: darkgreen;position: fixed;right: 0;bottom: 0;left: 0;color: white;padding: 5px;text-align: center;line-height: 16px}
#divHomeBtn {position: fixed;right: 0.5%;top:96px;z-index: 1}
#imgHome:focus {outline: none}
sup {
    color: red;
}
</style>
<script type="text/javascript">
var ajaxCallInProgress=0;
function validate() {
	if(ajaxCallInProgress==1)
            return;        
	if( !/^[ a-zA-Z0-9.&]{1,300}$/.test($('#inpFirmName').val()) ) {
		alert('Enter valid Firm Name');
                //$('#inpFirmName').focus();
		return;
	}
	if( !/^[a-zA-Z0-9./#-]{0,30}$/.test($('#inpFirmRegNo').val()) ) {
		alert('Enter valid Firm Registration Number');
		return;
	}
        
	if( !/^[ a-zA-Z.]{1,60}$/.test($('#inpName').val()) ) {
		alert('Enter valid Name');
                //$('#inpName').focus();
		return;
	}
        if( !/^[ a-zA-Z.]{0,60}$/.test($('#inpFName').val()) ) {
		alert('Enter Father\'s Name');
                //$('#inpName').focus();
		return;
	} 
	if($('#inpAdd').val().trim()=="") {
		alert('Enter Address');
                //$('#inpAdd').focus();
		return;
	}
        if($('#selState').val()=="") {
		alert('Select State');
                //('#selState').focus();
		return;
	}
	if($('#inpPin').val().trim()=="") {
		alert('Enter valid Pincode');
                //$('#inpPin').focus();
		return;
	} 
        if ( !/^[0-9]+$/.test($('#inpPin').val()) || $('#inpPin').val().length!==6) {
                alert('Enter valid Pincode');
                //$('#inpPin').focus();
		return;
        }
	
	if($('#inpMobile').val().trim()=="") {
		alert('Enter valid Mobile number');
		return;
	}
        if ( !/^[0-9]+$/.test($('#inpMobile').val()) || $('#inpMobile').val().length!==10) {
                alert('Enter valid Mobile number');
		return;
        } 
	
        if ( !/^[0-9]{0,12}$/.test($('#inpLL').val()) ) {
                alert('Enter valid landline number');
                //$('#inpLL').focus();
		return;
        }
	if($('#inpPAN').val().trim()=="" || $('#inpPAN').val().trim().length!=10) {
		alert('Enter PAN');
                //$('#inpPAN').focus();
		return;
	}
        if( isValidPAN( $('#inpPAN').val() )==false ) {
                alert("Invalid PAN number");
                //$('#inpPAN').focus();
                return;
        }
	if($('#inpAadhar').val().trim()!="" && $('#inpAadhar').val().trim().length!=12) {
		alert('Enter valid Aadhar');
                //$('#inpAadhar').focus();
		return;
	}
        if (!/^[0-9]{0,12}$/.test($('#inpAadhar').val()) ) {
                alert('Enter valid Aadhar');
                //$('#inpAadhar').focus();
		return;
        }
	if( document.getElementById("inpPFNo").disabled=="" && !/^[a-zA-Z0-9_@./#&]{0,40}$/.test($('#inpPFNo').val()) ) {
		alert('Enter valid EPFO Registration number');
                //$('#inpPFNo').focus();
		return;
	}
        if(document.getElementById("inpPFNo").disabled=="" && $('#inpPFNo').val().trim()=="" && $('#selEpfStat').val()=="0") {
                alert('Input EPFO registration number (Or) Select reason for not supplying EPF registration number from dropdown list');
                return;
        }
        if ( $('#inpEmail').val()!="" && !/^[a-z0-9]+[_a-z0-9.-]*[a-z0-9]+@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$/.test($('#inpEmail').val()) ) {
                alert('Enter valid email address');
		return;
        }
	if( !/^[a-zA-Z0-9_@./#&+-]{1,40}$/.test($('#inpWO_LOI').val()) ) {
		alert('Enter valid LOA number');
		return;
	}
	if($('#inpWO_LOI_Date').val()=="") {
		alert('Enter LOA Date');
                //$('#inpWO_LOI_Date').focus();
		return;
	}
	if($('#inpIssAuth').val()=="") {
		alert('Enter LOA issuing authority');
                //$('#inpIssAuth').focus();
		return;
	}
	if($('#selZone').val()=="" || $('#selZone').val()==null) {
		alert('Select Zone');
                //$('#selZone').focus();
		return;
	}
        if($('#selDiv').val()=="" || $('#selDiv').val()==null) {
		alert('Select Division');
		return;
	}
        if( $('#selDesig').val()=="") {
            alert('Select Registration Approving Authority');
            return;
        }
	if($('#inpPwd').val().trim()=="" || $('#inpPwd').val().trim().length<8 || $('#inpPwd').val().trim().indexOf(" ")!=-1) {
		alert('Invalid password! Enter valid password of minimum 8 characters');
                //$('#inpPwd').focus();
		return;
	}
	if($('#inpConfPwd').val().trim()=="" || $('#inpPwd').val().trim()!=$('#inpConfPwd').val().trim()) {
		alert('Re-entered password does not match');
                //$('#inpConfPwd').focus();
		return;
	}
	var r = window.confirm('Do you want to continue saving?');		
	if(r==true) {
            document.getElementById("inpPFNo").disabled="";
            document.getElementById("form").submit();                
        }        
}
function checkLL() {
        if ( !/^[0-9]{0,12}$/.test($('#inpLL').val()) ) {
            alert("Invalid Landline number");
            document.getElementById('inpLL').style.color='red';
            return;
        }
        else {
            document.getElementById('inpLL').style.color='black';
        }
}
function checkPAN() {
        $('#inpPAN').val($('#inpPAN').val().toString().toLowerCase());
	if($('#inpPAN').val().trim()=="") {
		return;
	}        
        if( isValidPAN( $('#inpPAN').val() )==false ) {
            alert("Invalid PAN number");
            document.getElementById('inpPAN').style.color='red';
            return;
        }
        else {
            document.getElementById('inpPAN').style.color='black';
        }
        ajaxCallInProgress=1;  
	$.post("/UtilityServlet",{action:"checkPAN",pan:$('#inpPAN').val().trim()},actOnPAN,'text');
}
function actOnPAN(v) {
	if(v==1) {
		alert('This PAN number is already registered');
		$('#inpPAN').val("");
	}
        ajaxCallInProgress=0;
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
function checkAadhar() {       
	if($('#inpAadhar').val().trim()=="") {
		return;
	}        
        if ( $('#inpAadhar').val().trim().length!=12 ||  !/^[0-9]{0,12}$/.test($('#inpAadhar').val()) ) {
                alert('Enter valid Aadhar');
                document.getElementById('inpAadhar').style.color='red';
		return;
        }
        else {
            document.getElementById('inpAadhar').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkAadhar",aadhar:$('#inpAadhar').val().trim()},actOnAadhar,'text');
}
function actOnAadhar(v) {
	if(v==1) {
		alert('This Aadhar is already registered');
		$('#inpAadhar').val("");
	}
        ajaxCallInProgress=0;
}
function checkFirmRegNo() {
	if($('#inpFirmRegNo').val().trim()=="") {
		return;
	}
        if ( !/^[a-zA-Z0-9./#-]{1,30}$/.test($('#inpFirmRegNo').val()) ) {
                alert('Enter valid Firm Registration number');
                document.getElementById('inpFirmRegNo').style.color='red';
                return;
        }
        else {
            document.getElementById('inpFirmRegNo').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkFirmRegNo",firmregno:$('#inpFirmRegNo').val().trim()},actOnFirmRegNo,'text');
}
function actOnFirmRegNo(v) {
	if(v==1) {
		alert('This Firm Reg No. is already registered');
		$('#inpFirmRegNo').val("");
	}
        ajaxCallInProgress=0;
}
function checkPFNo() {
	if($('#inpPFNo').val().trim()=="") {
		//alert('Please enter PAN');
		return;
	}
        if ( !/^[a-zA-Z0-9_@./#&]{0,40}$/.test($('#inpPFNo').val()) ) {
                alert('Enter valid EPFO registration number');
                document.getElementById('inpPFNo').style.color='red';
		return;
        }
        else {
            document.getElementById('inpPFNo').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkPFNo",pfno:$('#inpPFNo').val().trim()},actOnPFNo,'text');
}
function actOnPFNo(v) {
	if(v==1) {
		alert('This EPFO number is already registered');
		$('#inpPFNo').val("");
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
function actOnIrepsId(v) {
    if(v==1) {
        alert('This IREPS ID is already registered!');
        $('#inpIrepsId').val("");
    }
    ajaxCallInProgress=0;
}
function checkIrepsId() {
    if($('#inpIrepsId').val().trim()=="") {
        return;
    }
    ajaxCallInProgress=1;
    $.post("/UtilityServlet",{action:"checkIrepsId",irepsid:$('#inpIrepsId').val().trim()},actOnIrepsId,'text');
}
function checkWOLOI() {        
	if($('#inpWO_LOI').val().trim()=="") {
		//alert('Please enter PAN');
		return;
	}
        if ( !/^[a-zA-Z0-9_@./#&+-]{1,60}$/.test($('#inpWO_LOI').val()) ) {
                alert('Enter valid LOA number');
                document.getElementById('inpWO_LOI').style.color='red';
		return;
        }
        else {
            document.getElementById('inpWO_LOI').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkWOLOI",woloi:$('#inpWO_LOI').val().trim()},actOnWOLOI,'text');
}
function actOnWOLOI(v) {
	if(v==1) {
		alert('This LOA is already registered');
		$('#inpWO_LOI').val("");
	}
        ajaxCallInProgress=0;
}
function fillDesigList1(entries) {
    var dataArray = JSON.parse(entries);
	var sml = document.getElementById("selDesig");	
        var sml1 = document.getElementById("inpIssAuth");
	var opt = document.createElement("option");
        var opt1 = document.createElement("option");
	opt.text = "Select";
	opt.value = "";
	opt.disabled = false;
	opt.selected = true;
        opt1.text = "Select";
	opt1.value = "";
	opt1.disabled = false;
	opt1.selected = true;
        $('#selDesig').empty();
        $('#inpIssAuth').empty();
        
	sml.add(opt);
        sml1.add(opt1);
	if (dataArray.rows.length > 0) {
            $.each(dataArray.rows, function(index, data) {
                opt = document.createElement("option");
                opt1 = document.createElement("option");
                opt.text = data.shortdesig;
                opt.value = data.authid;
                opt1.text = data.shortdesig;
                opt1.value = data.authid;
                sml.add(opt);
                sml1.add(opt1);
            });
	}
}
function getDesigList() {    
    $('#selDesig').empty();
    $('#inpIssAuth').empty();
    if($('#selZone').val()!="")
        $.post("/UtilityServlet",{action:"getDesigList1",zoneid:$('#selZone').val(),divid:$('#selDiv').val(),deptid:$('#selDept').val()},fillDesigList1,'text');
}
function epfCheck(v) {
    if(v=="0") {
        document.getElementById("inpPFNo").disabled="";
    }
    else if(v=="1") {
        document.getElementById("inpPFNo").value="";
        document.getElementById("inpPFNo").disabled="true";
    }
    else if(v=="2") {
        document.getElementById("inpPFNo").value="";
        document.getElementById("inpPFNo").disabled="true";
    }
}
$(function() {
	//$('#header').height( Math.floor( window.innerHeight * 0.087 ) +"px");
        //$('#header').css('line-height', Math.floor( window.innerHeight * 0.087 ) +"px");
        $('#loader').hide();
        $(document).ajaxStart(function(){
            $("#loader").css("display", "block");
        });
        $(document).ajaxComplete(function(){
            $("#loader").css("display", "none");
        });
        $('#form').each(function() { this.reset() });
        //$('#imgHome').height( Math.floor( window.innerHeight * 0.07 ) );
        //$('#imgHome').width( Math.floor( window.innerHeight * 0.07 ) );
	getZoneList();
        $( "#inpWO_LOI_Date" ).datepicker({
            changeMonth: true,
            changeYear: true            
          });  
        $( "#inpWO_LOI_Date" ).datepicker( "option", "dateFormat", "yy-mm-dd" );  
        document.getElementById("LFILE").onchange = function() {
        if(this.files[0].size > 5242880) { // 5MB
           alert("File is too big! Max file size 5 MB");
           this.value = "";
        };
};
});
</script>
</head>
<%
   response.setHeader( "Pragma", "no-cache" );
   response.setHeader( "Cache-Control", "no-cache" );
   response.setDateHeader( "Expires", 0 );
%>
<body>
    <div id="loader"></div>
<div id="header">
    <div style="display: table-cell;float: left;width: 20%;vertical-align: middle">
        <img style="cursor: pointer;vertical-align: middle;margin: 0.5%;width: 66px;" onclick="window.open('http://indianrailways.gov.in')" id="logoRailway" src="/img/IRLOGO.png" title="Indian Railways" alt="Indian Railways"/>
        <img style="vertical-align: middle;margin: 0.5% 0.25%;width: 112px;" id="logoSwachBharat" src="/img/SWACHHBHARATLOGO.png" title="Swachh Bharat" alt="Swachh Bharat"/>
    </div>
    <div style="display: table-cell;font-size: x-large;text-align: center;padding-right: 19.5%;vertical-align: middle">
        भारतीय रेल श्रमिक कल्याण पोर्टल<br>Indian Railway Shramik Kalyan Portal
    </div>
    <div style="display: table-cell;width: 0.05%;vertical-align: middle">
        <img style="cursor:pointer;width: 78px;vertical-align: middle;padding: 0px;margin: 0px" onclick="window.open('http://cris.org.in')" id="logoCRIS" src="/img/CRISLOGO.png" title="Centre for Railway Information Systems" alt="CRIS"/>                                                   
    </div>          
</div>
    <div id="divHomeBtn"><input type="image" style="width:50px" id="imgHome" title="Home" src="/img/home.png" onclick=window.location="/clip.jsp"></input></div>    
<div class="w3-panel w3-light-green ui-corner-bottom w3-card-4" style="margin: 1%">	
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4><b>Contractor Registration</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">

<input type="hidden" id="userLevel" value="0"/>
<form id="form" action="/Contractor?action=registerContractor" method="post" enctype="multipart/form-data">
    
    <div class="row"><div class="cellHeading">Firm's Name<sup>*</sup></div><div class="cellInput"><input type="text" maxlength="300" id="inpFirmName" name="inpFirmName"></input></div><div class="cellHeading">Firm's Reg. No.</div><div class="cellInput"><input maxlength="40" type="text" onchange="checkFirmRegNo()" id="inpFirmRegNo" name="inpFirmRegNo"></input></div></div>
	<div class="row"><div class="cellHeading">Name<sup>*</sup></div><div class="cellInput"><input maxlength="60" type="text" id="inpName" name="inpName"></input></div><div class="cellHeading">Father's Name</div><div class="cellInput"><input maxlength="60" type="text" id="inpFName" name="inpFName"></input></div></div>
	<div class="row"><div class="cellHeading">Address<sup>*</sup></div><div class="cellInput"><input maxlength="1000" type="text" id="inpAdd" name="inpAdd"></input></div><div class="cellHeading">PIN Code.<sup>*</sup></div><div class="cellInput"><input placeholder="6-digit PIN" type="text" id="inpPin" name="inpPin" maxlength="6" style="width: 25%"></input></div></div>
	<div class="row"><div class="cellHeading">State<sup>*</sup></div><div class="cellInput"><select id="selState" name="selState">
		<option value="" selected>Select</option>
		<option value="Andaman and Nicobar Islands">Andaman and Nicobar Islands</option>
		<option value="Andhra Pradesh">Andhra Pradesh</option>
		<option value="Arunachal Pradesh">Arunachal Pradesh</option>
		<option value="Assam">Assam</option>
		<option value="Bihar">Bihar</option>
		<option value="Chandigarh">Chandigarh</option>
		<option value="Chhattisgarh">Chhattisgarh</option>
		<option value="Dadra and Nagar Haveli">Dadra and Nagar Haveli</option>
		<option value="Daman and Diu">Daman and Diu</option>
		<option value="Delhi">Delhi</option>
		<option value="Goa">Goa</option>
		<option value="Gujarat">Gujarat</option>
		<option value="Haryana">Haryana</option>
		<option value="Himachal Pradesh">Himachal Pradesh</option>
		<option value="Jammu and Kashmir">Jammu and Kashmir</option>
		<option value="Jharkhand">Jharkhand</option>
		<option value="Karnataka">Karnataka</option>
		<option value="Kerala">Kerala</option>
		<option value="Lakshadweep">Lakshadweep</option>
		<option value="Madhya Pradesh">Madhya Pradesh</option>
		<option value="Maharashtra">Maharashtra</option>
		<option value="Manipur">Manipur</option>
		<option value="Meghalaya">Meghalaya</option>
		<option value="Mizoram">Mizoram</option>
		<option value="Nagaland">Nagaland</option>
		<option value="Orissa">Orissa</option>
		<option value="Pondicherry">Pondicherry</option>
		<option value="Punjab">Punjab</option>
		<option value="Rajasthan">Rajasthan</option>
		<option value="Sikkim">Sikkim</option>
		<option value="Tamil Nadu">Tamil Nadu</option>
                <option value="Telangana">Telangana</option>
		<option value="Tripura">Tripura</option>
		<option value="Uttaranchal">Uttaranchal</option>
		<option value="Uttar Pradesh">Uttar Pradesh</option>
		<option value="West Bengal">West Bengal</option>
                </select></div><div class="cellHeading">IREPS ID</div><div class="cellInput"><input placeholder="" type="text" id="inpIrepsId" name="inpIrepsId" maxlength="50" style="" onblur="checkIrepsId()"></input></div></div>
	<div class="row"><div class="cellHeading">Mobile Number<sup>*</sup></div><div class="cellInput"><input value="" placeholder="10-digit mobile number" onchange="checkMobile()" id="inpMobile" type="text" name="inpMobile" maxlength="10"></input></div><div class="cellHeading">Landline</div><div class="cellInput"><input onchange="checkLL()" type="text" id="inpLL" name="inpLL" maxlength="12"></input></div></div>
	<div class="row"><div class="cellHeading">PAN No.<sup>*</sup></div><div class="cellInput"><input onchange="checkPAN()" placeholder="10 chars PAN" id="inpPAN" name="inpPAN" type="text" maxlength="10"></input></div><div class="cellHeading">Aadhar No.</div><div class="cellInput"><input type="text" onchange="checkAadhar()" placeholder="12-digit Aadhar" id="inpAadhar" name="inpAadhar" maxlength="12"></input></div></div>
        <div class="row"><div class="cellHeading">EPFO Regn. No.</div><div class="cellInput"><input style="width:48%;margin-right: 1%" maxlength="40" id="inpPFNo" onchange="checkPFNo()" name="inpPFNo" type="text"></input> OR 
                <select id="selEpfStat" name="selEpfStat" style="margin-left: 1%" onblur="epfCheck(this.value)">
                    <option value="0">Select</option>
                    <option value="1">Firm employs less than 20 workers</option>
                    <option value="2">EPFO Registration has been applied for</option></select></div><div class="cellHeading">Email</div><div class="cellInput"><input maxlength="40" onchange="checkEmail()" id="inpEmail" type="email" name="inpEmail"></input></div></div>
        <div class="row"><div class="cellHeading">LOA No. & Date<sup>*</sup></div><div class="cellInput"><input style="width: 64.5%" type="text" onchange="checkWOLOI()" id="inpWO_LOI" name="inpWO_LOI" maxlength="60"></input>
		<input style="width: 34%" type="text" readonly id="inpWO_LOI_Date" name="inpWO_LOI_Date" placeholder="Click"></input><br></br>
                <input style="width: 60%" name="LFILE" id="LFILE" type="file"></input></div><div class="cellHeading">Authority</div><div class="cellInput">
                    <!--<input id="inpIssAuth" name="inpIssAuth" type="text"></input>-->
                    <fieldset class="field_set">
                                                                
                                <label for="selZone">Zone<sup>*</sup> </label><select id="selZone" name="selZone" onchange="getDesigList();getDivisionList();"></select>
                                <label for="selDiv" style="margin-left: 2%">Division<sup>*</sup> </label>
                                <select id="selDiv" name="selDiv" onchange="getDesigList();getDepartmentList();">
                                    <option value="">Select</option>
                                </select>
                                <label for="selDept" style="margin-left: 2%;">Department </label><select id="selDept" name="selDept" onchange="getDesigList();"><option value="">Select</option></select>
                                <br></br>
                                
                                <label for="inpIssAuth" >LoA Issuing Authority<sup>*</sup>&nbsp;</label><select onchange="document.getElementById('selDesig').value=this.value;" id="inpIssAuth" name="inpIssAuth" ><option value="" selected>Select</option></select>
                                <span style="display: none"><label for="selDesig" style="margin-left: 2%;">Registration Approving Authority<sup>*</sup>&nbsp;</label><select id="selDesig" name="selDesig" ><option value="" selected>Select</option></select></span>
                        </fieldset>
                </div></div>
	<div class="row">
            <div style="display: inline-block;width: 49.5%;">
                <select id="selCompany" name="selCompany" style="display:none">
                        <option value="" selected disabled>Select</option>
                        <option value="1">Eastern Railway</option>				
                </select>
                <fieldset class="field_set">
                    <legend>Login Details</legend>
                    Password<sup>*</sup> <input type="password" id="inpPwd" name="inpPwd" maxlength="10" autocomplete="off" style="width: 21%;margin-right: 5%"></input>
                    Re-enter Password<sup>*</sup> <input maxlength="10" type="password" id="inpConfPwd" autocomplete="off" name="inpConfPwd" style="width: 21%"></input>
                </fieldset>
            </div>
            <div style="display: inline-block;width: 47.5%;margin-left: 2%;">
                    <center><input class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green' type="button" onclick="validate();" value="Submit" style="width:16%;margin: 0.8%"></input></center>	
            </div>
	</div>
        <sup>* </sup>Mandatory fields
	<br></br>
</form>
</div>
</div> <br></br>   
<div id="footer"><span style="font-size: 11px">© 2018, Ministry of Railways, Govt. of India. All rights reserved.<br>Designed & Developed by <a title="Centre for Railway Information Systems" style="text-decoration: underline;cursor:pointer;color: white;" onclick=window.open("http://www.cris.org.in")>CRIS</a> | <a title="Indian Railway Shramik Kalyan Portal" href="/clip.jsp">Home</a>| <a style="text-decoration: underline;cursor:pointer;color: white;" href="/manual/CLIP_CONTRACTOR_MANUAL.pdf" download>Contractor Help</a></span></div>
</body>
</html>