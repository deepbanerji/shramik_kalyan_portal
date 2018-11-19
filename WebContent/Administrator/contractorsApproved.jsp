<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Pragma" content="no-cache"></meta>
<meta http-equiv="Cache-Control" content="no-cache">
<title>Verified Contractors</title>

<script type="text/javascript" src="/jquery-2.0.0.min.js"></script>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<link rel="stylesheet" href="/css/loader.css"></link>
<link rel="stylesheet" href="/css/w3.css">
<link rel="stylesheet" type="text/css" href="/DataTables/datatables.min.css"/> 
<script type="text/javascript" src="/DataTables/datatables.min.js"></script>

<script>
var serverTime;
var approvalReqdFor=0;
function getServerTime() {
    $.post("/UtilityServlet",{action:"servertime"},function(data,status) {serverTime = data;},'text');
}
function getContractorsList() {
	$.post("/Contractor",{action:"listAll",status:"1"},fillContractorsList,'text');
}
function fillContractorsList(entries) {
	var dataArray = JSON.parse(entries);
	var table=document.getElementById('tblBody');
		if(dataArray.rows.length>0) {
			var i=-1;
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
					
                                cell11.title=data.approver;            
            
				cell1.innerHTML=data.name;
				//cell3.innerHTML=data.woref;
				cell2.innerHTML=data.firmname;
                                
                                if(data.firmregno!=null)
                                    cell3.innerHTML=data.firmregno;
                                else
                                    cell3.innerHTML="";
                            
                                cell4.innerHTML="(M)"+data.mobile;
                                if(data.landline!=null)
                                    cell4.innerHTML=cell4.innerHTML+"<br>(L)"+data.landline;
                                
                                if(data.email!=null)
                                    cell5.innerHTML=data.email;
                                else
                                    cell5.innerHTML="";
                                    
				cell6.innerHTML=data.pan.toString().toUpperCase();
                                
                                if(data.aadhar!=null)
                                    cell7.innerHTML=data.aadhar;
                                else
                                    cell7.innerHTML="";
                                    
				if(data.pfno!=null)
                                    cell8.innerHTML=data.pfno;
                                else
                                    cell8.innerHTML="";
                                
				cell9.innerHTML=data.regnDate.toString().split(' ')[0];
                                cell10.innerHTML=data.approvalDate.toString().split(' ')[0];
                                
                                if(data.approverPost!="null/null")
                                    cell11.innerHTML=data.approverPost;
                                else
                                    cell11.innerHTML="";
				}	
			});
		}
	    
	   var table = $('#tblContractors').DataTable( {
                        lengthMenu: [[10, 25, 50, "All"]],
			dom: 'Bfrtip',
			buttons: [
                            'copyHtml5',
                            'excelHtml5',
                            'csvHtml5',
                            {
                                extend: 'pdfHtml5',
                                orientation: 'landscape',
                                pageSize: 'A4',
                                title: 'List of Verified Contractors - Indian Railway Shramik Kalyan Portal, Ministry of Railways, Govt. of India.',
                                messageTop: 'Report Generated on : '+serverTime,
                                download: 'open',
                                customize: function (doc) { 
                                    doc.defaultStyle.fontSize = 7;
                                    doc.styles.tableHeader.fontSize = 7;
                                },
                                exportOptions: {
                                    columns: ':visible'
                                }
                            },
                            {   extend:'print',
                                title: 'List of Verified Contractors - Indian Railway Shramik Kalyan Portal, Ministry of Railways, Govt. of India.',
                                messageTop: 'Report Generated on : '+serverTime,
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
//$('#tblContractors tbody tr').hover(function() {
  //  $(this).addClass('hover');
//}, function() {
  //  $(this).removeClass('hover');
//});

$(function(){
    $('#loader').hide();
    $(document).ajaxStart(function(){
        $("#loader").css("display", "block");
    });
    $(document).ajaxComplete(function(){
        $("#loader").css("display", "none");
    });
    getContractorsList();
    getServerTime();
    setInterval(getServerTime, 60000);
    
});
</script>
<style>
a {color: blue;}
a:visited {color: blue}
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
</style>
</head>
<body>
<c:if test="${pageContext.session['new']}">
    <c:redirect url="/clip.jsp"/> 
</c:if>
<div id="loader"></div>
<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0">	
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Verified Contractors</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>
<table id="tblContractors" class="display cell-border w3-card-4 w3-pale-green w3-border w3-border-green" style="font-size: small;word-wrap:break-word; table-layout: fixed">
    <thead >
        <tr>			
            <th>Contractor Name</th>
            <th>Firm Name</th>
            <th>Firm Reg.No.</th>
            <th>Contact Nos.</th>
            <th>Email</th>
            <th>PAN</th>
            <th>Aadhar</th>
            <th>PF Reg.No.</th>
            <th title="Registration application date and time">Applied On</th>
            <th>Verification Date</th>
            <th>Verifier</th>
        </tr>
    </thead>

    <tbody id="tblBody"></tbody>
</table>
<br>
</div>
</div>
    <br></br>
</body>
</html>