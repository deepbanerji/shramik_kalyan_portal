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
<link rel="stylesheet" href="/css/loader.css">
<script type="text/javascript" src="/js/common-ajax-calls.js"></script>
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<link rel="stylesheet" href="/css/w3.css">

<script>
var approvalReqdFor=0;
var dialog,dialog2,viewEditFlag;
var ajaxCallInProgress=0;
var contractorUidOfInterest;
function approveContractor() {
	var uid = $('#modalContId').val();
        //alert($('#modalMobile').val());
        //alert($('#modalEmail').val());
	var r = window.confirm('Do you want to continue saving?');		
	if(r==true) {
		approvalReqdFor=uid;
		$.post("/Contractor",{action:"approveContractor", uid:uid, status:$('#actionType').val(), mobile:$('#modalMobile').val() ,ll:$('#modalLL').val() , pan:$('#modalPan').val().toLowerCase() , aadhar:$('#modalAadhar').val() , email:$('#modalEmail').val() , pfregno:$('#modalPfRegNo').val() },updateContractorApprovalResult,'text');
	}
}
function updateContractorApprovalResult(retVal) {
	dialog.dialog( "close" );
	if(retVal==2) {
            alert('Contractor modification successfull!');
	}
	else {
            alert('Contractor modification failed!');
	}	
	getContractorsList();
}
function getContractorDetails(uid,view1edit2) {
	viewEditFlag = view1edit2;
	$.post("/Contractor",{action:"getContDetails", uid:uid},fillContractorDetails,'text');
}
function fillContractorDetails(entries) {
	var dataArray = JSON.parse(entries);
	if(dataArray.rows.length>0) {
		$.each(dataArray.rows, function(index,data) {
			$('#modalFirmName').val(data.firmName);
			$('#modalFirmRegNo').val(data.firmReg);
			$('#modalContractorName').val(data.name);
			$('#modalFName').val(data.fname);
			$('#modalAddress').val(data.permaddr);
			$('#modalState').val(data.permState);
			$('#modalPin').val(data.permpin);
			$('#modalMobile').val(data.mobno);
			$('#modalLL').val(data.ll);
			$('#modalPan').val(data.pan.toUpperCase());
			$('#modalAadhar').val(data.aadhar);
			$('#modalEmail').val(data.email);
			$('#modalPfRegNo').val(data.pfno);
			$('#modalWOLOI').val(data.woloi);
			$('#modalWOIssAuth').val(data.qualifiedLoaIssuer);
			$('#modalContId').val(data.conID);
			$('#actionType').val(data.status);
                        $('#modalIrepsId').val(data.irepsid);
			if(data.status==0) {
				$('#actionType').prop("disabled",false);
                                $('#modalPan').removeAttr('readonly');
                        }
                        else {
				$('#actionType').prop("disabled",true);
                                $('#modalPan').attr('readonly', true);
                        }            
			$('#modalFirmName2').val(data.firmName);
			$('#modalFirmRegNo2').val(data.firmReg);
			$('#modalContractorName2').val(data.name);
			$('#modalFName2').val(data.fname);
			$('#modalAddress2').val(data.permaddr);
			$('#modalState2').val(data.permState);
			$('#modalPin2').val(data.permpin);
			$('#modalMobile2').val(data.mobno);
			$('#modalLL2').val(data.ll);
			$('#modalPan2').val(data.pan.toUpperCase());
			$('#modalAadhar2').val(data.aadhar);
			$('#modalEmail2').val(data.email);
			$('#modalPfRegNo2').val(data.pfno);
			$('#modalWOLOI2').val(data.woloi);
			$('#modalWOIssAuth2').val(data.qualifiedLoaIssuer);
			$('#actionType2').val(data.status);
                        $('#modalIrepsId2').val(data.irepsid);                        
		});
	}	
	if(viewEditFlag=="2")	
		dialog.dialog( "open" ); // edit		
	else
		dialog2.dialog( "open" );
}
function fnShowDialogChangeLOAIssAuth(uid) {
    contractorUidOfInterest = uid;
    $( "#modalChangeLOAIssAuth" ).dialog("open");
}
function fnChangeLOAIssAuth(uid) {
    if($('#selDiv').val()=="" || $('#selDesig').val()=="") { 
        alert('Division and Designation are mandatory!');
        return;
    }
    var r = confirm("Are you sure you want to redirect this contractor registration request?");
    if(r==true)
        $.post("/Contractor",{action:"changeLOAIssAuth",uid:contractorUidOfInterest,zoneid:$('#selZone').val(),divid:$('#selDiv').val(),deptid:$('#selDept').val(),desigid:$('#selDesig').val()},showChangeLOAIssAuthResult,'text');
    else
        alert("You cancelled the process!");
    $( "#modalChangeLOAIssAuth" ).dialog("close");
}
function showChangeLOAIssAuthResult(retval) {
    if(retval==1) {
        alert("Operation successful!");
        getContractorsList();
    }
    else {
        alert("Operation Failed!");
    }
}
function getContractorsList() {
	$.post("/Contractor",{action:"listAll",status:"0,1"},fillContractorsList,'text');
}
function fillContractorsList(entries) {
	var dataArray = JSON.parse(entries);
	var table=document.getElementById('tblContractors');
	$("#tblContractors").find("tr:gt(0)").remove();
		if(dataArray.rows.length>0) {
			var i=0;
			$.each(dataArray.rows, function(index,data) {
				if(data.approvalStatus==0 || data.approvalStatus==1) {
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

				cell5.id='cellMobile'+data.uid;
				cell6.id='cellEmail'+data.uid;
				cell12.id='cellApproveBtn'+data.uid;
				
				cell1.innerHTML=i;
				cell2.innerHTML=data.name;
				if(data.worefdoc==null)
					cell3.innerHTML=data.woref; 
				else
					//cell3.innerHTML="<a title='Download' href=/UtilityServlet?action=download&id="+data.worefdoc+">"+data.woref+"</a>";
                                        cell3.innerHTML="<a title='Show'  style='cursor:pointer' onclick=window.open('/Contractor?action=fetchWO&wofile="+data.worefdoc+"','')>"+data.woref+"</a>";    
				cell4.innerHTML=data.firmname;
                                
                                if(data.mobile!=null)
                                    cell5.innerHTML=data.mobile;
                                else    
                                    cell5.innerHTML="";
                                
                                if(data.email!=null)
				cell6.innerHTML=data.email;
                                else	
                                cell6.innerHTML="";
                                
                                if(data.pan!=null)
                                cell7.innerHTML=data.pan.toString().toUpperCase();
				else
                                cell7.innerHTML="";    
                                    
                                cell8.innerHTML=data.regnDate.toString().split(' ')[0];
                                
                                cell9.title=data.appliedtoId;
                                cell9.innerHTML=data.appliedto+"<br>"+data.zone;
                                if(data.div!=null)        
                                cell9.innerHTML = cell9.innerHTML +"/"+data.div;
                                if(data.dept!=null)
                                cell9.innerHTML = cell9.innerHTML +"/"+data.dept;
                                
				if(data.approvalStatus==0) {
                                    cell10.style.color = "red";
                                    cell10.innerHTML="Pending";
				
                                }
                                else
					cell10.innerHTML="Verified";
				cell11.innerHTML="<input type='button' onclick=getContractorDetails("+data.uid+",1) class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green' value='View'>";
				if(data.appliedtoId==document.getElementById('hidAdminId').value)
                                    x = "enabled";
                                else
                                    x = "disabled";
                                cell12.innerHTML="<input "+x+" type='button' onclick=getContractorDetails("+data.uid+",2) class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green' value='Edit'>";
				//cell13.innerHTML="<input "+x+" type='button' onclick=fnShowDialogChangeLOAIssAuth("+data.uid+") class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green' value='Transfer'>";
			}	
			});
		}
}

function validate() {
	if(ajaxCallInProgress==1)
            return;
	if($('#modalMobile').val().trim()=="") {
		alert('Enter valid Mobile number');
                //$('#modalMobile').focus();
		return;
	}
        if (!/^[0-9]+$/.test($('#modalMobile').val()) || $('#modalMobile').val().length!==10) {
                alert('Enter valid Mobile number');
                //$('#modalMobile').focus();
		return;
        } 
        
	if (!/^[0-9]{0,12}$/.test($('#modalLL').val()) ) {
                alert('Enter valid landline number');
		return;
        }
        
	if($('#modalPan').val().trim()=="" || $('#modalPan').val().trim().length!=10) {
		alert('Enter PAN');
                //$('#modalPan').focus();
		return;
	}
        if( isValidPAN( $('#modalPan').val().toLowerCase() )==false ) {
                alert("Invalid PAN number");
                return;
        }
	if($('#modalAadhar').val().trim()!="" && $('#modalAadhar').val().trim().length!=12) {
		alert('Enter valid Aadhar');
                //$('#modalAadhar').focus();
		return;
	} 
        if ( !/^[0-9]{0,12}$/.test($('#modalAadhar').val()) ) {
                alert('Enter valid Aadhar');
		return;
        }
	if ( $('#modalEmail').val()!="" && !/^[a-z0-9]+[_a-z0-9.-]*[a-z0-9]+@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$/.test($('#modalEmail').val()) ) {
                alert('Enter valid email address');
		return;
        }
        if( !/^[a-zA-Z0-9_@./#&]{0,40}$/.test($('#modalPfRegNo').val()) ) {    
		alert('Enter valid EPF Registration number');
		return;
	}	
	approveContractor();
}
function getDesigList() {    
    $('#selDesig').empty();
    $('#inpIssAuth').empty();
    if($('#selZone').val()!="")
        $.post("/UtilityServlet",{action:"getDesigList1",zoneid:$('#selZone').val(),divid:$('#selDiv').val(),deptid:$('#selDept').val()},fillDesigList1,'text');
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
	var form;
	//allFields = $( [] ).add( name ).add( email ).add( password ),	
	dialog2 = $( "#dialog-form2" ).dialog({
	      autoOpen: false,
	      height: 400,
	      width: 830,
	      modal: true,
	      buttons: {
		        Cancel: function() {
		          dialog2.dialog( "close" );
		        }
		      }
	    });
	 
	dialog = $( "#dialog-form" ).dialog({
	      autoOpen: false,
	      height: 445,
	      width: 830,
	      modal: true,
	      buttons: {
	        "Save": function(){ validate(); },
	        Cancel: function() {
	          dialog.dialog( "close" );
	        }
	      },
	      close: function() {
	        form[ 0 ].reset();
	        //allFields.removeClass( "ui-state-error" );
	      }
	    });
	 
	    form = dialog.find( "form" ).on( "submit", function( event ) {
	      event.preventDefault();
	      //addUser();
	    });
	
        $( "#modalChangeLOAIssAuth" ).dialog({
                    autoOpen: false,
                    modal:true,
                    maxWidth:500,
                    maxHeight: 500,
                    width: 500
                });
    if($('#hidZoneId').val()!="null") {        
        getDesigList();
        getDivisionList();        
    }
    
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
function checkPAN() {
        $('#modalPan').val($('#modalPan').val().toString().toUpperCase());
	if($('#modalPan').val().trim()=="") {
		return;
	}
        if( isValidPAN( $('#modalPan').val().toLowerCase() )==false ) {
            alert("Invalid PAN number");
            document.getElementById('modalPan').style.color='red';
            return;
        }
        else {
            document.getElementById('modalPan').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkPAN",pan:$('#modalPan').val().trim().toLowerCase(),contid:$('#modalContId').val() },actOnPAN,'text');
}
function actOnPAN(v) {
	if(v==1) {
		alert('This PAN number is already registered');
		$('#modalPan').val("");
	}
        ajaxCallInProgress=0;
}
function checkMobile() {
	if($('#modalMobile').val().trim()=="") {
		//alert('Please enter PAN');
		return;
	}
        if (!/^[0-9]+$/.test($('#modalMobile').val()) || $('#modalMobile').val().length!==10) {
                alert('Enter valid Mobile number');
                document.getElementById('modalMobile').style.color='red';
		return;
        }
        else {
            document.getElementById('modalMobile').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkMobile",mobile:$('#modalMobile').val().trim(),contid:$('#modalContId').val()},actOnMobile,'text');
}
function actOnMobile(v) {
	if(v==1) {
		alert('This Mobile number is already registered');
		$('#modalMobile').val("");
	}
        ajaxCallInProgress=0;
}
function checkAadhar() {       
	if($('#modalAadhar').val().trim()=="") {
		return;
	}        
        if ( $('#modalAadhar').val().trim().length!=12 ||  !/^[0-9]+$/.test($('#modalAadhar').val()) ) {
                alert('Enter valid Aadhar');
                document.getElementById('modalAadhar').style.color='red';
		return;
        }
        else {
            document.getElementById('modalAadhar').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkAadhar",aadhar:$('#modalAadhar').val().trim(),contid:$('#modalContId').val()},actOnAadhar,'text');
}
function actOnAadhar(v) {
	if(v==1) {
		alert('This Aadhar is already registered');
		$('#modalAadhar').val("");
	}
        ajaxCallInProgress=0;
}
function checkPFNo() {
	if($('#modalPfRegNo').val().trim()=="") {
		return;
	}
        if ( !/^[a-zA-Z0-9_@./#&]{0,40}$/.test($('#modalPfRegNo').val()) ) {
                alert('Enter valid EPFO registration number');
                document.getElementById('modalPfRegNo').style.color='red';
		return;
        }
        else {
            document.getElementById('modalPfRegNo').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkPFNo",pfno:$('#modalPfRegNo').val().trim(),contid:$('#modalContId').val()},actOnPFNo,'text');
}
function actOnPFNo(v) {
	if(v==1) {
		alert('This EPF number is already registered');
		$('#modalPfRegNo').val("");
	}
        ajaxCallInProgress=0;
}
function checkEmail() {        
	if($('#modalEmail').val().trim()=="") {
		return;
	}
        if ( !/^[a-z0-9]+[_a-z0-9.-]*[a-z0-9]+@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$/.test($('#modalEmail').val()) ) {
                alert('Enter valid email address');
                document.getElementById('modalEmail').style.color='red';
		return;
        }
        else {
            document.getElementById('modalEmail').style.color='black';
        }
        ajaxCallInProgress=1;
	$.post("/UtilityServlet",{action:"checkEmail",email:$('#modalEmail').val().trim(),contid:$('#modalContId').val()},actOnEmail,'text');
}
function actOnEmail(v) {
	if(v==1) {
		alert('This Email id is already registered');
		$('#modalEmail').val("");
	}
        ajaxCallInProgress=0;
}

</script>
<style>
a {color: blue;}
a:visited {color: blue}
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
<body>
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
<input type='hidden' value='<%=adto.getZoneId()%>' id='hidZoneId'></input>
<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0">
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Contractor Modification/Verification</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>
<input type="text" id="seaName" onkeyup="seaNames()" placeholder="Search names.."></input>
<table id="tblContractors" class="w3-table-all w3-card-4 w3-border w3-border-green ui-corner-all" style="font-size: small;margin-top: 5px;width:100%; word-wrap:break-word; table-layout: fixed">
    <thead >
        <tr class="w3-pale-green">
            <th>S.No.</th>
            <th>Contractor Name</th>
            <th>LOA Reference</th>
            <th>Firm Name</th>
            <th>Mobile</th>
            <th>Email</th>
            <th>PAN</th>
            <th title="Registration application date and time">Applied On</th>
            <th>Applied To</th>
            <th>Status</th>
            <th></th>
            <th></th>
            <!--<th></th>-->
        </tr>            
    </thead>
    <tbody id="tblBody">

    </tbody>
</table>
<br></br>
</div>

<div id="dialog-form" title="Edit Contractor Details" style="height: 100%;">
<p>* Editable field&nbsp;&nbsp;&nbsp;# Editable & Mandatory field</p>
  <form>
  <input type="hidden" id="modalContId" value=""></input>
    <fieldset>
      <table style="width: 100%">
      		<tr>
      			<td><label for="modalName">Name of Firm </label></td><td><input readonly="readonly" id="modalFirmName"></input></td>
      			<td><label for="modalFirmRegNo">Registration Number of Firm </label></td><td><input readonly="readonly" id="modalFirmRegNo"></input></td>
      		</tr>
      		<tr>
      			<td><label for="modalContractorName">Name of Contractor</label></td><td><input readonly="readonly" id="modalContractorName"></input></td>
      			<td><label for="modalFName">Father's Name </label></td><td><input readonly="readonly" id="modalFName"></input></td>
      		</tr>
      		<tr>
      			<td align="left" colspan="4"><label for="modalAddress">Address </label><input readonly="readonly" id="modalAddress" style="margin-right: 13px"></input>
                            <label for="modalState">State </label>
                            <select disabled id="modalState" style="margin-right: 13px">
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
                            </select>
                            <label for="modalPin">Pincode </label><input readonly="readonly" id="modalPin" size="18"></input>
      			</td>
      		</tr>
      		<tr>
      			<td><label for="modalWOLOI">LOA</label></td><td><input readonly="readonly" id="modalWOLOI"></input></td>
      			<td><label for="modalWOIssAuth">LOA Issuing Authority </label></td><td><input readonly="readonly" id="modalWOIssAuth"></input></td>
      		</tr>
      		<tr>
                    <td><label for="modalMobile"># Mobile </label></td><td><input onchange="checkMobile()" maxlength="10" id="modalMobile" placeholder="10-digit mobile no"></input></td>
                    <td><label for="modalLL">* Landline </label></td><td><input id="modalLL" maxlength="12"></input></td>
      		</tr>
      		<tr>
                    <td><label for="modalPan"># PAN </label></td><td><input onchange="checkPAN()" maxlength="10" id="modalPan" placeholder="10 char PAN"></input></td>
                    <td><label for="modalAadhar">* Aadhar </label></td><td><input maxlength="12" onchange="checkAadhar()" id="modalAadhar" placeholder="12-digit Aadhar"></input></td>
      		</tr>
      		<tr>
                    <td><label for="modalEmail">* Email </label></td><td><input onchange="checkEmail()" maxlength="40" id="modalEmail" type="email"></input></td>
                    <td><label for="modalPfRegNo">* EPF Registration Number </label></td><td><input onchange="checkPFNo()" maxlength="40" id="modalPfRegNo"></input></td>
      		</tr>
      		 
      		<tr><td><label for="modalIrepsId">IREPS ID </label></td><td><input readonly="readonly" id="modalIrepsId" ></input></td>
                    <td><label for="actionType">* Status </label></td><td><select id="actionType">
      			<option value="0">Pending</option>
      			<option value="1">Verified</option>
      			<option value="2">Rejected</option>
      			</select></td>
                    </tr>  		
      </table>
    </fieldset>
  </form>
</div>

<div id="dialog-form2" title="View Contractor Details" style="height: 100%;">   
    <fieldset>
      <table style="width: 100%" >
      		<tr>
      			<td><label for="modalName2">Name of Firm </label></td><td><input readonly="readonly" id="modalFirmName2"></input></td>
      			<td><label for="modalFirmRegNo2">Registration Number of Firm </label></td><td><input  readonly="readonly" id="modalFirmRegNo2"></input></td>
      		</tr>
      		<tr>
      			<td><label for="modalContractorName2">Name of Contractor</label></td><td><input readonly="readonly" id="modalContractorName2"></input></td>
      			<td><label for="modalFName2">Father's Name </label></td><td><input readonly="readonly" id="modalFName2"></input></td>
      		</tr>
      		<tr>
      			<td align="left" colspan="4"><label for="modalAddress2">Address </label><input readonly="readonly"  id="modalAddress2" style="margin-right: 13px"></input>
                            <label for="modalState2">State </label>
                            <select disabled id="modalState2" style="margin-right: 13px">
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
                            </select>
                            <label for="modalPin2">Pincode </label><input readonly="readonly" id="modalPin2" size="18"></input>
      			</td>
      		</tr>
      		<tr>
      			<td><label for="modalWOLOI2">LOA # </label></td><td><input readonly="readonly" id="modalWOLOI2"></input></td>
      			<td><label for="modalWOIssAuth2">LOA Issuing Authority </label></td><td><input readonly="readonly" id="modalWOIssAuth2"></input></td>
      		</tr> 
      		<tr>
      			<td><label for="modalMobile2">Mobile </label></td><td><input readonly="readonly"  id="modalMobile2"></input></td>
      			<td><label for="modalLL2">Landline </label></td><td><input  readonly="readonly" id="modalLL2"></input></td>
      		</tr>
      		<tr>
      			<td><label for="modalPan2">PAN </label></td><td><input readonly="readonly" id="modalPan2"></input></td>
      			<td><label for="modalAadhar2">Aadhar </label></td><td><input readonly="readonly" id="modalAadhar2"></input></td>
      		</tr>
      		<tr>
      			<td><label for="modalEmail2">Email </label></td><td><input readonly="readonly" id="modalEmail2"></input></td>
      			<td><label for="modalPfRegNo2">PF Registration Number </label></td><td><input readonly="readonly"  id="modalPfRegNo2"></input></td>
      		</tr>
      		<tr><td><label for="modalIrepsId2">IREPS ID </label></td><td><input readonly="readonly" id="modalIrepsId2"></input></td>
                    <td><label for="actionType2">Status </label></td><td><select disabled="disabled" id="actionType2">
      			<option value="0">Pending</option>
      			<option value="1">Verified</option>
      			<option value="2">Rejected</option>
      			</select></td>
                    </tr> 
      		 		
      </table>
    </fieldset>
  
</div>
</div>
    <br></br>
    <div id="modalChangeLOAIssAuth" title="Change LoA Issuing Authority">
        <label for="selZone">Zone<sup>*</sup> </label><select id="selZone" name="selZone"><option value="<%=adto.getZoneId()%>"><%=adto.getZoneName()%></option></select>
        <label for="selDiv" style="margin-left: 2%">Division<sup>*</sup> </label>
        <select id="selDiv" name="selDiv" onchange="getDesigList();getDepartmentList();">
            <option value="">Select</option>
        </select>
        <label for="selDept" style="margin-left: 2%;">Department </label><select id="selDept" name="selDept" onchange="getDesigList();"><option value="">Select</option></select>
        <br></br>
        <label for="inpIssAuth" >LoA Issuing Authority<sup>*</sup>&nbsp;</label><select onchange="document.getElementById('selDesig').value=this.value;" id="inpIssAuth" name="inpIssAuth" ><option value="" selected>Select</option></select>
        <span style="display: none"><label for="selDesig" style="margin-left: 2%;">Registration Approving Authority<sup>*</sup>&nbsp;</label><select id="selDesig" name="selDesig" ><option value="" selected>Select</option></select></span>
        <input type="button" value="Save" style="margin-left: 2%;" onclick="fnChangeLOAIssAuth()"></input>
    </div>
    
        
</body>
</html>