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
<title>Verified Work Orders</title>
<script type="text/javascript" src="/jquery-2.0.0.min.js"></script>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<link rel="stylesheet" href="/css/loader.css"></link>
<link rel="stylesheet" href="/css/w3.css">
<link rel="stylesheet" type="text/css" href="/DataTables/datatables.min.css"/> 
<script type="text/javascript" src="/DataTables/datatables.min.js"></script>

<script type="text/javascript">
var approvalReqdFor=0;
var serverTime;
function getServerTime() {
    $.post("/UtilityServlet",{action:"servertime"},function(data,status) {serverTime = data;},'text');
}
function callback1(retVal) {
    if(retVal==0) {
        alert('Operation failed!');
    }
    else {
        alert('Operation successful!');
        location.reload();
    }
}
function fn1(olddate,uid) {
    
    var ndate = prompt("Please enter extended date of completion", "yyyy-mm-dd");
    if (ndate != null) {
        if( /(20)\d\d[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])$/.test(ndate) ) {
            var od = new Date(olddate);
            var nd = new Date(ndate);
            if(nd>od) {
                var r = confirm("Are you sure you want to extend the date?");
                if(r==true) {
                    $.post("/UtilityServlet",{action:"extendLoaDate",uid:uid,newdate:ndate},callback1,'text');
                }    
            }
            else {
                alert('Extended date must be greater than old date!');
            }
        }
        else {
            alert('Invalid date!');
        }
    }
}
function getWoList() {
	$.post("/UtilityServlet",{action:"listWOs"},fillWoList,'text');
}
function fillWoList(names) {
	var dataArray = JSON.parse(names);
	var table=document.getElementById('tblBody');
		if(dataArray.rows.length>0) {
			var i=-1;
			$.each(dataArray.rows, function(index,data) {
				if(data.approvalStatus=='1') {
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
				
				cell11.id='cellEmail'+data.woId;
				cell12.id='cellMobile'+data.woId;
				
				
				if(data.workName.length>100) {
                                    cell1.innerHTML="<span title='"+data.workName+"'>"+data.workName.substr(0,100)+"...</span>";
                                }
                                else {
                                    cell1.innerHTML=data.workName;
                                }
                                
				if(data.wofilenum=="0")
					cell2.innerHTML=data.woNo;
				else
					//cell3.innerHTML="<a title='Download' href=/UtilityServlet?action=download&id="+data.wofilenum+">"+data.woNo+"</a>";
                                        cell2.innerHTML="<a title='Show'  style='cursor:pointer' onclick=window.open('/Contractor?action=fetchWO&wofile="+data.wofilenum+"','')>"+data.woNo+"</a>";
				cell3.innerHTML=data.zone;
                                if(data.div!=null)        
                                    cell3.innerHTML = cell3.innerHTML +"/"+data.div;
                                if(data.dept!=null)
                                    cell3.innerHTML = cell3.innerHTML +"/"+data.dept;
                                cell4.innerHTML=data.prinEmp;
                                
                                cell5.innerHTML=data.commdate.toString().split(' ')[0];
                                cell6.innerHTML=data.enddate.toString().split(' ')[0];
                                
                                if(document.getElementById('userlevel').value!="0")
                                    cell7.innerHTML="<a title='Click to extend date' onclick=fn1('"+String(data.extenddate.toString().split(' ')[0])+"','"+String(data.woId)+"');>"+data.extenddate.toString().split(' ')[0]+"</a>";
                                else
                                    cell7.innerHTML=data.extenddate.toString().split(' ')[0];
                                //cell7.innerHTML=data.extenddate.toString().split(' ')[0];
                                if(data.approvaldate!=null)
                                    cell8.innerHTML=data.approvaldate.toString().split(' ')[0];
                                else
                                    cell8.innerHTML="";
                                cell9.innerHTML=data.approverPost;
                                
				cell10.innerHTML=data.contname;
                                
                                if(data.contemail!=null)
                                    cell11.innerHTML=data.contemail;
                                else
                                    cell11.innerHTML="";
                                
				cell12.innerHTML=data.contmobile;	
				}			
			});
		}

		
	    
	   var table = $('#tblWorkOrders').DataTable( {
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
                                title: 'List of Verified Work Orders - Indian Railway Shramik Kalyan Portal, Ministry of Railways, Govt. of India.',
                                customize: function (doc) { 
                                    doc.defaultStyle.fontSize = 7;
                                    doc.styles.tableHeader.fontSize = 7;
                                },                                
                                exportOptions: {
                                    columns: ':visible'
                                },
                                download: 'open',
                                messageTop: 'Report Generated on : '+serverTime
                            },
                            {   extend:'print',
                                title: 'List of Verified Work Orders - Indian Railway Shramik Kalyan Portal, Ministry of Railways, Govt. of India.',
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
$(function(){   
    $('#loader').hide();
    $(document).ajaxStart(function(){
        $("#loader").css("display", "block");
    });
    $(document).ajaxComplete(function(){
        $("#loader").css("display", "none");
    });
    getWoList();
    getServerTime();
    setInterval(getServerTime, 60000);
});
</script>
<style>
body {width: 100%;}
a:hover {
 cursor:pointer;
}
a {color: blue;text-decoration: underline}
a:visited {color: blue}
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
<input type="hidden" id="userlevel" value="<%=adto.getLevelId()%>"></input>
<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0">	
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Verified LOAs</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>	
<table id="tblWorkOrders" class="display cell-border w3-card-4 w3-pale-green w3-border w3-border-green" style="font-size: small;width:100%; word-wrap:break-word; table-layout: fixed">
	<thead>
		<tr>			
                    <th>Work Name</th>
                    <th>LOA Number</th>
                    <th>Zone/Div/Dept</th>
                    <th>Principal Employer</th>
                    <th title="Work Commencement Date">Work Start Date</th>
                    <th title="Work Completion Date">Work End Date</th>
                    <th title="Extended Completion Date">Extended End Date</th>
                    <th title="WO Verification Date">Verification Date</th>
                    <th>Verified by</th>
                    <th>Contractor's Name</th>
                    <th>Contractor's Email</th>
                    <th>Contractor's Mobile</th>
		</tr>
	</thead>
	<tbody id="tblBody">	
	</tbody>
	
</table>
<br>
</div>
</div>
    <br></br>
</body>
</html>