<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="org.cris.clip.dto.AdministratorDTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>Workman Wages</title>
<script type="text/javascript" src="/jquery-2.0.0.min.js"></script>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css"></link>
<link rel="stylesheet" href="/css/StyleCalender.css"></link>
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<link rel="stylesheet" href="/css/loader.css"></link>
<link rel="stylesheet" href="/css/w3.css">
<link rel="stylesheet" type="text/css" href="/DataTables/datatables.min.css"/> 
<script type="text/javascript" src="/DataTables/datatables.min.js"></script>
<script src="/js/CalendarControl.js" > </script>
<script type="text/javascript" src="/js/common-ajax-calls.js"></script>
<script type="text/javascript">
var serverTime;
function getServerTime() {
    $.post("/UtilityServlet",{action:"servertime"},function(data,status) {serverTime = data;},'text');
}
var dataTable;
function createDataTable() {
	dataTable = $('#tblLabours').DataTable( {
                "autoWidth": true,
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
                        title: 'Workmen Wages Report - Indian Railway Shramik Kalyan Portal, Ministry of Railways, Govt. of India.',
                        download: 'open',
                        customize: function (doc) { 
                            doc.defaultStyle.fontSize = 12;
                            doc.styles.tableHeader.fontSize = 12;
                        },
                        exportOptions: {
                            columns: ':visible'
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
                            if( $('#inpMonth').val()!=null && $('#inpMonth').val()!="" )
                                retmsg += '  From: '+ $('#inpMonth').val();
                            if( $('#inpMonthTo').val()!=null && $('#inpMonthTo').val()!="" )
                                retmsg += '  To: '+ $('#inpMonthTo').val();
                            if( $('#inpBankDepDt').val()!=null && $('#inpBankDepDt').val()!="" )
                                retmsg += '  Bank Deposit Dt: '+ $('#inpBankDepDt').val();
                            if( $('#inpPFDepDt').val()!=null && $('#inpPFDepDt').val()!="" )
                                retmsg += '  PF Deposit Dt: '+ $('#inpPFDepDt').val();
                            return retmsg+'\nReport Generated on : '+serverTime;
                        }
                    },
                    {   extend:'print',
                        title: 'Workmen Wages Report - Indian Railway Shramik Kalyan Portal, Ministry of Railways, Govt. of India.',                        
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
                            if( $('#inpMonth').val()!=null && $('#inpMonth').val()!="" )
                                retmsg += '  From: '+ $('#inpMonth').val();
                            if( $('#inpMonthTo').val()!=null && $('#inpMonthTo').val()!="" )
                                retmsg += '  To: '+ $('#inpMonthTo').val();
                            if( $('#inpBankDepDt').val()!=null && $('#inpBankDepDt').val()!="" )
                                retmsg += '  Bank Deposit Dt: '+ $('#inpBankDepDt').val();
                            if( $('#inpPFDepDt').val()!=null && $('#inpPFDepDt').val()!="" )
                                retmsg += '  PF Deposit Dt: '+ $('#inpPFDepDt').val();
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
function getLabourWages() {
	if( $('#selWO').val()==null || $('#selWO').val()=="" ) {
            alert('Select LOA');
            return;
	}	
	if($('#inpMonth').val()=="" || $('#inpMonthTo').val()=="") {
            alert('Select period From and To!');
            return;
	}        
        if ( new Date( $('#inpMonth').val().split('-')[1] + "-" + $('#inpMonth').val().split('-')[0] ) > new Date( $('#inpMonthTo').val().split('-')[1] + "-" + $('#inpMonthTo').val().split('-')[0] ) ) {
            alert('To must be later than From month');
            return;
        }
                  
	dataTable.destroy();
	$("#tblLabours tbody").find("tr").remove();
	$.post("/UtilityServlet",{action:"listLabourWages",woid:$('#selWO').val(),month:$('#inpMonth').val().substr(0,2),year:$('#inpMonth').val().substr(3,4),
            monthTo:$('#inpMonthTo').val().substr(0,2),yearTo:$('#inpMonthTo').val().substr(3,4),
            bankDepDt: $('#inpBankDepDt').val(),pfDepDt: $('#inpPFDepDt').val()},fillLabourWages,'text');
}
function fillLabourWages(names) {
	var dataArray = JSON.parse(names);	
		if(dataArray.rows.length>0) {
			var i=-1;
			$.each(dataArray.rows, function(index,data) {
                            if(data.month!=null && data.year!=null) {
				var row = document.getElementById('mainTableBody').insertRow(++i);
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
								
				//cell1.innerHTML=i+1;
                                cell1.innerHTML=data.wonum;
                                cell2.innerHTML=data.zone;
                                if(data.div!=null)        
                                cell2.innerHTML = cell2.innerHTML +"/"+data.div;
                                if(data.dept!=null)
                                cell2.innerHTML = cell2.innerHTML +"/"+data.dept;
                                
                                cell3.innerHTML=data.month+"-"+data.year;
                                cell4.innerHTML=data.attendance;
				cell5.innerHTML=data.id;
				cell6.innerHTML=data.name;
				cell7.innerHTML=data.startdate;
                                
                                if(data.termdate!=null)
                                    cell8.innerHTML=data.termdate;	
                                else
                                    cell8.innerHTML="";	
				
                                cell9.innerHTML=data.netamount;	
                                cell10.innerHTML = data.deduction;
                            }
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
        
        //$( "#inpMonth" ).datepicker();  
        //$( "#inpMonth" ).datepicker( "option", "dateFormat", "yy-mm" );
        var d = new Date();
        var month = d.getMonth() + 1;
        if(month<10)
            month = "0" + month; 
        $('#inpMonth').val(month+"-"+d.getFullYear());
        $('#inpMonthTo').val(month+"-"+d.getFullYear());
        
        $( "#inpBankDepDt" ).datepicker({
            changeMonth: true,
            changeYear: true            
          });  
        $( "#inpBankDepDt" ).datepicker( "option", "dateFormat", "yy-mm-dd" );
        
        $( "#inpPFDepDt" ).datepicker({
            changeMonth: true,
            changeYear: true            
          });  
        $( "#inpPFDepDt" ).datepicker( "option", "dateFormat", "yy-mm-dd" );
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
<body >
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
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Workman Wages</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">
<hr></hr>
<fieldset style="min-width: 45%">
    <legend> LOA - Domain </legend>
	<label for="selZone" >Zone </label><select id="selZone" name="selZone" onchange="$('#selCont').prop('selectedIndex', 0) ;getDivisionList(); getWOList(this.value,'zone');getContractorsList(this.value,null,null)"></select>
	<label for="selDiv" style="margin-left: 1.8%">Division </label><select id="selDiv" name="selDiv" onchange="$('#selCont').prop('selectedIndex', 0) ;getDepartmentList(); getWOList(this.value,'div');getContractorsList( $('#selZone').val() ,this.value ,null)"></select>
	<label for="selDept" style="margin-left: 1.8%;max-width: 10%">Department </label><select id="selDept" name="selDept" onchange="$('#selCont').prop('selectedIndex', 0) ;getWOList(this.value,'dept');getContractorsList( $('#selZone').val() , $('#selDiv').val() ,this.value)"></select>
</fieldset>
<fieldset>
    <legend> LOA - Contractor </legend>
<label for="selCont" style="margin-left: 1.8%;"></label><select id="selCont" name="selCont" onchange="getWOList(this.value,'cont');"></select>	
</fieldset>
<fieldset>
    <legend>LOA #</legend>
	<label for="selWO" style="margin-left: 1.8%;"></label><select id="selWO" name="selWO"></select>
</fieldset>	
        <br></br>
    <fieldset>
        <legend>From</legend>
        <label for="selMonth" ></label><input id="inpMonth" name="inpMonth" readonly size="7"></input>
        <img alt="Month/Year Picker" onclick="showCalendarControl('inpMonth');" src="/img/datepicker.gif" />
    </fieldset>
    <fieldset>
        <legend>To</legend>
        <label for="selMonthTo" ></label><input id="inpMonthTo" name="inpMonthTo" readonly size="7"></input>
        <img alt="Month/Year Picker" onclick="showCalendarControl('inpMonthTo');" src="/img/datepicker.gif" />
    </fieldset>    
        <fieldset>
    <legend>Bank Deposit Date</legend>
        <label for="inpBankDepDt" style="margin-left: 1.8%"></label><input placeholder="Click" id="inpBankDepDt" name="inpBankDepDt" ></input>
        </fieldset>
        <fieldset>
    <legend>PF Deposit Date</legend>
        <label for="inpPFDepDt" style="margin-left: 1.8%"></label><input placeholder="Click" id="inpPFDepDt" name="inpPFDepDt" ></input>
        </fieldset>
	<input type="button" class='w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green' style="margin-left: 1.8%" value="Show" onclick="getLabourWages()"/>

<br></br>
<div>
<table id="tblLabours" class="display cell-border w3-card-4 w3-pale-green w3-border w3-border-green" style="font-size: small;width:100%; word-wrap:break-word; table-layout: fixed">
    <thead>
        <tr>			
            <th>LOA #</th>
            <th>Zone/Div/Dept</th>
            <th>Period</th>
            <th>Days Present</th>
            <th>Workman ID</th>
            <th>Workman Name</th>
            <th>Start Date</th>
            <th>Termination Date</th>
            <th>Net Wage</th>
            <th>Deduction</th>
        </tr>
    </thead>
    <tbody id="mainTableBody"></tbody>
</table>
<br></br>
</div>
</div>
</div>
    <br></br>
</form>
</body>
</html>