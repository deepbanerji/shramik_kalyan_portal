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
<title>Workman List</title>

<script type="text/javascript" src="/jquery-2.0.0.min.js"></script>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<link rel="stylesheet" href="/css/loader.css"></link>
<link rel="stylesheet" href="/css/w3.css">
<link rel="stylesheet" type="text/css" href="/DataTables/datatables.min.css"/> 
<script type="text/javascript" src="/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="/js/common-ajax-calls.js"></script>
<script type="text/javascript">
var serverTime;
function getServerTime() {
    $.post("/UtilityServlet",{action:"servertime"},function(data,status) {serverTime = data;},'text');
}
var dataTable;
var contraList = [];
function createDataTable() {
	dataTable = $('#tblLabours').DataTable( {

		"lengthMenu": [[10, 25, 50, "All"]],
		
		dom: 'Bfrtip',

		buttons: [
            'copyHtml5',
            'excelHtml5',
            'csvHtml5',
            {
                extend: 'pdfHtml5',
                orientation: 'landscape',
                pageSize: 'A4',
                title: 'Engaged Workmen List - Indian Railway Shramik Kalyan Portal, Ministry of Railways, Govt. of India.',
                download: 'open',
                customize: function (doc) { 
                    doc.defaultStyle.fontSize = 11;
                    doc.styles.tableHeader.fontSize = 11;
                },
                messageTop: function() {
                    var retmsg='';
                    if( $('#selZone').val()!=null && $('#selZone').val()!="" )
                        retmsg = 'Zone: '+$('#selZone option:selected').text();
                    if( $('#selDiv').val()!=null && $('#selDiv').val()!="" )
                        retmsg += ', Division: '+$('#selDiv option:selected').text();
                    if( $('#selDept').val()!=null && $('#selDept').val()!="" )
                        retmsg += ', Department: '+$('#selDept option:selected').text();
                    if( $('#selCont').val()!=null && $('#selCont').val()!="" )
                        retmsg += '  Contractor: '+ $('#selCont option:selected').text();
                    if( $('#selWO').val()!=null && $('#selWO').val()!="" )
                        retmsg += '  LOA: '+ $('#selWO option:selected').text();
                    return retmsg+'\nReport Generated on : '+serverTime;
                },
                exportOptions: {
                    columns: ':visible'
                }
            },
            {   extend:'print',
                title: 'Engaged Workmen List - Indian Railway Shramik Kalyan Portal, Ministry of Railways, Govt. of India.',                
                messageTop: function() {
                    var retmsg='';
                    if( $('#selZone').val()!=null && $('#selZone').val()!="" )
                        retmsg = 'Zone: '+$('#selZone option:selected').text();
                    if( $('#selDiv').val()!=null && $('#selDiv').val()!="" )
                        retmsg += ', Division: '+$('#selDiv option:selected').text();
                    if( $('#selDept').val()!=null && $('#selDept').val()!="" )
                        retmsg += ', Department: '+$('#selDept option:selected').text();
                    if( $('#selCont').val()!=null && $('#selCont').val()!="" )
                        retmsg += '  Contractor: '+ $('#selCont option:selected').text();
                    if( $('#selWO').val()!=null && $('#selWO').val()!="" )
                        retmsg += '  LOA: '+ $('#selWO option:selected').text();
                    return retmsg+'\nReport Generated on : '+serverTime;
                },
                exportOptions: {
                    columns: ':visible'
                }
            },
            {
                extend: 'colvis',
                columns: ':not(.noVis)'
            }
        ]
	    
	} );
}
function getLabourList() {	
	if($('#selWO').val()=="" || $('#selWO').val()==null) {
		alert('Select LOA');
		return;
	}
	dataTable.destroy();
	$("#tblLabours tbody").find("tr").remove();
	$.post("/UtilityServlet",{action:"listLabours",woid:$('#selWO').val()},fillLabourList,'text');
}
function fillLabourList(names) {
	var dataArray = JSON.parse(names);
	//var table=document.getElementById('tblLabours');
		if(dataArray.rows.length>0) {
			var i=-1;
			$.each(dataArray.rows, function(index,data) {
				var row = document.getElementById('mainTableBody').insertRow(++i);
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
                                
				//cell1.innerHTML=i+1;
                                cell1.innerHTML=data.wonum;
                                cell2.innerHTML=data.zone;
                                if(data.div!=null)        
                                cell2.innerHTML = cell2.innerHTML +"/"+data.div;
                                if(data.dept!=null)
                                cell2.innerHTML = cell2.innerHTML +"/"+data.dept;
                                cell3.innerHTML=data.id;
				cell4.innerHTML=data.name;
                                cell5.innerHTML=data.desig;
				cell6.innerHTML=data.sex;
                                cell7.innerHTML=data.dob;
				cell8.innerHTML=data.icardtype;
				cell9.innerHTML=data.icardnumber;
                                if(data.mobile!=null)
                                    cell10.innerHTML=data.mobile;
                                else
                                    cell10.innerHTML="";
                                if(data.email!=null)
                                    cell11.innerHTML=data.email;
                                else
                                    cell11.innerHTML="";
			});
		}
		createDataTable();		   
}
$(function(){
        $('#loader').hide();
	$(document).ajaxStart(function(){
            $("#loader").css("display", "block");
        });
        $(document).ajaxComplete(function(){
            $("#loader").css("display", "none");
        });

	createDataTable();	   
	getZoneList();
	
        if( $('#userLevel').val()=='0' )
            getWOList("","");
        $('#selCont').width(Math.floor(window.innerHeight*0.3));
        $('#selWO').width(Math.floor(window.innerHeight*0.3));
        getServerTime();
        setInterval(getServerTime, 60000);
});
</script>
<style>
body {width: 100%;}
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
fieldset {
    display: inline-block;
    border-color: black;
}
</style>
</head>
<body>
<%	AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
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
<form>
<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0">	
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Workman List</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>
<fieldset style="min-width: 45%">
    <legend> LOA - Domain </legend>
	<label for="selZone" >Zone </label><select id="selZone" name="selZone" onchange="$('#selCont').prop('selectedIndex', 0) ;getDivisionList(); getWOList(this.value,'zone');getContractorsList(this.value,null,null)"></select>
	<label for="selDiv" style="margin-left: 1.8%">Division </label><select id="selDiv" name="selDiv" onchange="$('#selCont').prop('selectedIndex', 0) ;getDepartmentList(); getWOList(this.value,'div');getContractorsList( $('#selZone').val() ,this.value ,null)"></select>
        <label for="selDept" style="margin-left: 1.8%;">Department </label><select id="selDept" name="selDept" onchange="$('#selCont').prop('selectedIndex', 0) ;getWOList(this.value,'dept');getContractorsList( $('#selZone').val() , $('#selDiv').val() ,this.value)"></select>
</fieldset>
<fieldset>
    <legend> LOA - Contractor </legend>
        <label for="selCont" style="margin-left: 1.8%;"></label><select id="selCont" name="selCont" onchange="getWOList(this.value,'cont');"></select>
</fieldset>
<fieldset>
    <legend>LOA #</legend>
    <label for="selWO" style="margin-left: 1.8%;"></label><select id="selWO" name="selWO"></select>
</fieldset>    
	<input type="button" value="Show" style="margin-left: 1.8%" onclick="getLabourList()" class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green'/>
<hr></hr>
<div>
<table id="tblLabours" class="display cell-border w3-card-4 w3-pale-green w3-border w3-border-green" style="font-size: small;width:100%; word-wrap:break-word; table-layout: fixed">
	<thead>
		<tr>			
                    <th>LOA No.</th>
                    <th>Zone/Div/Dept</th>
                    <th>Workman ID</th>
                    <th>Workman Name</th>
                    <th>Designation</th>
                    <th>Gender</th>
                    <th>DoB</th>
                    <th>ID Proof</th>
                    <th>ID Proof No.</th>
                    <th>Mobile</th>
                    <th>Email ID</th>
		</tr>
	</thead>
	<tbody id="mainTableBody">	
	</tbody>
		
</table>
<br></br>
</div>
</div>
</div>
    <br></br>
</form>
</body>
</html>