<%@ page language="java" contentType="text/html; charset=ISO-8859-1"     pageEncoding="ISO-8859-1"%>
<%@ page import="org.cris.clip.dto.ContractorDTO" %>
<!DOCTYPE html>
<html>
<head>
<%@ page import="java.util.ArrayList,org.cris.clip.dao.WorkOrder,org.cris.clip.dto.WorkOrderDTO,org.cris.clip.dto.WOLocationDTO,org.cris.clip.dao.UtilityDao"%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>
  Indian Railway Shramik Kalyan Portal 
       LOA Details
</title>

<link rel="stylesheet" href="/css/w3.css">
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/DataTables/datatables.min.css"/> 
<SCRIPT type="text/javascript" src="/contractor/theme/json2.js"></SCRIPT>
<script src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<SCRIPT type="text/javascript" src="/jquery-2.0.0.min.js"></SCRIPT>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script type="text/javascript" src="/DataTables/datatables.min.js"></script>



<style type="text/css">

html, body{
   height:100%;
   width: 100%; 
   
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


.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* Modal Content/Box */
.modal-content {
    background-color: #82CAFF;
    margin: 10% auto; /* 15% from the top and centered */
    padding: 20px;
    border: 1px solid #888;
    width: 80%; /* Could be more or less, depending on screen size */
}

/* The Close Button */
.close {
    color: #aaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: black;
    text-decoration: none;
    cursor: pointer;
}

.dataTables_scroll
{
    overflow:auto;
    clear:both;
}



</style>


</head>

<body onload="getZone();getWoLocation('0');getWoState('0');">

<%
   HttpSession httpSession=request.getSession(false);
 	ContractorDTO conDTO=(ContractorDTO) httpSession.getAttribute("contractor");
	if(conDTO==null)
	{
		System.out.println("invalid dto");
		response.sendRedirect("/clip.jsp");		
	}
	ArrayList al1 = new ArrayList();
	al1 = new WorkOrder().listWO(Integer.parseInt(conDTO.getId()));		
%>

<form>
 <input type="hidden" name="chkWONO" id="chkWONO" disabled>
 <div class="w3-panel w3-light-green">	 	 			
	<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'>
		<h4> <b> LOA Details : </b> </h4>
	</div>	
	<div class='w3-animate-bottom'>
		<input type="button" class="w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green" id="myBtn" value="Create LOA" style="display:inline;float:left" onclick="window.location='NewWorkOrder.jsp'">
	</div>
	<br>
	<div class='w3-animate-right'>		
		<hr>
		<table id="workOrderTable" id="workOrderTable" class="display cell-border w3-card-4  w3-border w3-border-green" style="font-size:12px;" >			
			<thead>
	            <tr style="background-color: #000080;color: white;">
	                <th>Sl. No.</th>
					<th>Number</th>
					<th>Work Name</th>	
					<th>Work Value(Rs)</th>					
					<th>Commencement Date</th>
					<th>Completion Date</th>
					<th>Extended Completion Date</th>	
					<th>Location/ State/ District </th>					
					<th>Issued By/ Principal Employer</th>
					<th>Principal Employer Address</th>					
					<th>Zone/ Division/ Department</th>	
					<th>Max Workman</th>	
					<th>Labour License</th>							
					<th>Verification Status</th>
	            </tr>
        	</thead>
			
			<tbody>
				<% if(al1!=null) {
						int i = al1.size();
						for(int j=0;j<i;++j) {
							WorkOrderDTO wodto = new WorkOrderDTO();
							wodto = (WorkOrderDTO)al1.get(j);						
				%>	
				
				<tr>
					<td><%=j+1%></td>
					<td> <% if(wodto.getWoFileNo()==0){ %>
							<a href="#" onclick="uploadWOrder(<%=wodto.getId()%>)"> <%=wodto.getWO()%>  </a> 
						<% } else { %>						 
						 <a href="#" style="text-decoration:none" onclick="viewWorkOrder('<%=wodto.getWoFileNo()%>');"> <%=wodto.getWO()%> </a>						  
						<% } %>			
					</td>
					<td><%=wodto.getWork_Name()%></td>
					<td><%=wodto.getLoavalue()%></td>
					<td><%=wodto.getCommDate()%></td>
					<td><%=wodto.getEndDt()%></td>
					<td><%=wodto.getExtEndDt()%></td>
					<td>
					<%
					 ArrayList locarl=wodto.getWoLocList();
					 int k=0;
					 while(k<locarl.size())
					 {
						 WOLocationDTO locdto = new WOLocationDTO();
						 locdto = (WOLocationDTO)locarl.get(k++);					
					%>
					 <%=locdto.getLocother()%> / <%=locdto.getStatename()%><%if(locdto.getDistname()!=null){%> / <%=locdto.getDistname()%><%}%>,
					 <%}%>
					</td>	
					 <%  UtilityDao utildao=new UtilityDao();
					     String priceemployer= utildao.getAdminPostFromId(wodto.getIssueBy());
					%>				
					<td><%=priceemployer%></td>					
					<td><%=wodto.getPincEmpAddr()%></td>
					<td><%=wodto.getWoZone()%>/ <%=wodto.getWoDiv()%><%if(wodto.getWoDept()!=null){%> / <%=wodto.getWoDept()%><%}%></td>				
					<td><%=wodto.getMaxworkman()%></td>	
					<td><% if(wodto.getLabourLicense()!=null){ %><%=wodto.getLabourLicense() %>  <% } %> </td>		
					<td><%=wodto.getApprovalStatus()%></td>		
				</tr>
				
				<% } } %>
			</tbody>			
		</table>		
		<hr class="w3-black">		
	</div>	
   </div>
   </form>
   <!-- The Modal -->
   <div id="myModal" class="modal">
   <!-- Modal content -->
   <div class="modal-content">   
    <span class="close">&times;</span>
    <form name="NewWOForm" id="NewWOForm" action="/Contractor?action=addNewWO" onsubmit="return validate_form(this);" method="post">
		
	<h3 class="ui-corner-all" style="color: white;background-color: #000080;padding: 10px;display: inline-block;margin-top: 1em;font-size:10">Add New LOA</h3>
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font style="color:red;font-size:10">*&nbsp;Mandatory</font>	 
	<br>
		<table style="font-siz: 10">
					
					<tr>
						<td width="30%" >LOA No <font style="color:red;font-size:12">*</font></td>
						<td width="70%"><input  name="WorkOrd" maxlength="100" id="WorkOrd" type="text" value=""  onblur="checkWONO();"/></td>
					</tr>
					<tr>
						<td >Name of the Work <font style="color:red;font-size:12">*</font></td>
						<td><input  name="WorkName" maxlength="400" id="WorkName"  type="text" value="" /></td>
					</tr>
					<tr>
						<td  >Value of the Work(Rs)<font style="color:red;font-size:12">*</font></td>
						<td ><input  name="LoaValue" id="LoaValue"  type="number"  min="0" value="" /></td>
					</tr>
				<!--  	<tr>
						<td width="40%" >Location of the Work <font style="color:red;font-size:12">*</font></td>
						<td width="70%"><input  name="LocName" maxlength="500" id="LocName" size="60" type="text" value="" /></td>
					</tr>	-->					
					<tr>
						<td >Date of Commencement of Work <font style="color:red;font-size:12">*</font></td>
						<td ><input id="StartDate" name="StartDate" type="date"></td>
							</tr>
						<tr>
					    <td >Date of Completion of Work <font style="color:red;font-size:12">*</font></td>
						<td><input id="EndDate" name="EndDate" type="date"></td>
						</tr>
						<tr>
					    <td  >Extended Date of Completion of Work</td>
						<td><input id="ExtDate" name="ExtDate" type="date"></td>
						</tr>
						<tr>
						<td>Max No of Workman <font style="color:red;font-size:12">*</font></td>
						<td><input   name="MaxEmp"  id="MaxEmp" size="20" type="number" min="0" /></td>
					</tr>
				<!-- 	<tr>
						<td width="50%" >Work Order Issued By <font style="color:red;font-size:12">*</font></td>
						<td width="50%"><input  name="IssBy" maxlength="100" id="IssBy" size="60" type="text" value="" /></td>
					</tr>   -->
					<tr>
						<td width="40%" >Labour License <font style="color:red;font-size:12">*</font></td>
						<td width="60%"><input  name="LabLic" maxlength="60" id="LabLic" type="text" value="" /></td>
					</tr>
				<!-- 	<tr>
						<td width="40%" >Name of the Principal Employer <font style="color:red;font-size:12">*</font></td>
						<td width="50%"><input  name="PrincEmp" maxlength="100" id="PrincEmp" size="60" type="text" value="" /></td>
					</tr>   -->				
			
					   
						<tr >
						 <td >LOA Location <font style="color:red;font-size:12">*</font></td> 
						 <td class='loctr' width="65%" >  <div class='locbr'> <select name='WOLOC' >  </select> 
						                      <input type='text' name='LOCOTH' maxlength="60" style='display:none'> 
						                      State <select name='LOCSTATE' style='display:none' onchange="getWoDistrict('0');"> <option value=''> Select </option> </select> 
						                      District<select name='LOCDIST' style='display:none'> <option value=''> Select </option> </select> 
						 <input type="button" id="locadd" value='Add More'> <input type="button" id="locdel" value='Remove'> </div>  </td> 
						</tr>
						
												
					<tr>
						<td >LOA Issuing Authority/Principal Employer </td>
						<td >Zone <font style="color:red;font-size:12">*</font>
							<select id="WOZONE" name="WOZONE" onchange="getDivision();getDesigList();">
								<option value="">-Select-</option>
							</select>
							Division <font style="color:red;font-size:12">*</font>
							<select id="WODIV" name="WODIV" onchange="getDepartmentList();getDesigList();">
								<option value="">-Select-</option>
							</select>	
							Department
							<select id="WODEPT" name="WODEPT" onchange="getDesigList();">
								<option value="">-Select-</option>
							</select>	<br>
							 Designation <font style="color:red;font-size:12">*</font>
							 <select id="inpIssAuth" name="inpIssAuth" >
							    <option value="" selected>Select</option>
							 </select>	
						<!--	 Principal Employer <font style="color:red;font-size:12">*</font>
							  <select id="selDesig" name="selDesig" >
							    <option value="" selected>Select</option>
							  </select>		-->						
						</td>		
					</tr>
					<tr>
						<td >Principal Employer Address <font style="color:red;font-size:12">*</font></td>
						<td><input name="PrincEmpAdd" maxlength="100" id="PrincEmpAdd"  type="text" value="" /></td>
					</tr>
				<!-- <tr>
						<td width="50%" >Work Order Area <font style="color:red;font-size:12">*</font> </td>
						<td>
							 <select id="WOAREA" name="WOAREA">
							 <option value="">-Select-</option>
							 <option value="A">A</option>
							 <option value="B">B</option>
							 <option value="C">C</option>
							</select>								
						</td>		
					 </tr>   -->					
						
					
						 <tr><td colspan="2" align="center"> <input type="submit" value="Submit"/>   </td> </tr>
						</table>
	 <span> <font style="color:yellow;font-size:36">* Select <b> A & B </b> category LOA location from given location list only, for <b>C</b> Category LOA location select <b>OTHER</b>. Add multiple LOA location if applicable. </font> </span>		
	 </form>
	
    </div>
    </div>
    <script type="text/javascript">


    // Setting servertime and contracor details for document heading
    
    var serverTime;
    function getServerTime() {
        $.post("/UtilityServlet",{action:"servertime"},function(data,status) {serverTime = data;},'text');
    } 
    
    var  ContName="";
    var farmName="";
        function Contractor(){ $.ajax({type: 'POST',url: '/Contractor?action=getConProfile',async: false,success: function(names){var data = JSON.parse(names);
    	                    ContName=data.name;farmName=data.firmName;}}); return ''+ContName+'   ('+farmName+')';}
          

        getServerTime();   
        Contractor();

        
     //  checking and intitializing jquery datepicker
     
      var elem = document.createElement('input');
      elem.setAttribute('type', 'date');
 
      if ( elem.type === 'text' ) {
    	//  alert('test date2'); 
         $('#StartDate').datepicker({
             dateFormat: 'yy-mm-dd',
             changeMonth: true,
             changeYear: true,
             minDate: new Date('2017-10-01'),
             maxDate: new Date().toISOString().split("T")[0]
          }); 
         $('#EndDate').datepicker({
             dateFormat: 'yy-mm-dd',
             changeMonth: true,
             changeYear: true
         });  
         $('#ExtDate').datepicker({
             dateFormat: 'yy-mm-dd',
             changeMonth: true,
             changeYear: true
         });
      } 
      else
       {
    		StartDate.max = new Date().toISOString().split("T")[0];   // Restrict Date to Current Date
    	  	StartDate.min = "2017-10-01";   // Resctrict minimum date to Database Minimum Wage Validfrom
          }



       // Initializing datatables 
    	
	    var table = $('#workOrderTable').DataTable( {

		"destroy": true,
		"scrollX": false,	

		"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		
		dom: 'Bfrtip',


		 "columnDefs": [
		                { "visible": false, "targets": 7 },
		                { "visible": false, "targets": 11 },
		                { "visible": false, "targets": 12 }		             	               
		              ],	

		              buttons: [
		    		            'colvis', 		
		    		            { extend: 'copyHtml5',
		    		            	 messageTop: function() 
		    			                {
		    			                 return 'Contractor: '+ContName+'('+farmName+')  '+'Report Generated On: '+serverTime;  
		    			                },
		    		               exportOptions: {
		    		                    columns: ':visible'
		    		                }
		    		             },
		    		             { extend: 'excelHtml5',
		    		            	 messageTop: function() 
		    			                {
		    			                 return 'Contractor: '+ContName+'('+farmName+')  '+'Report Generated On: '+serverTime;  
		    			                },
		    		               orientation: 'landscape',
		    		                pageSize: 'A4',		              
		    		                exportOptions: {
		    		                    columns: ':visible'
		    		                }
		    		             },
		    		             { extend: 'csvHtml5',
		    		            	 messageTop: function() 
		    			                {
		    			                 return 'Contractor: '+ContName+'('+farmName+')  '+'Report Generated On: '+serverTime;  
		    			                },
		    		               exportOptions: {
		    		                    columns: ':visible'
		    		                }
		    		             },
		    		                
		    		            {
		    		                extend: 'pdfHtml5',
		    		                messageTop: function() 
		    		                {
		    		                 return 'Contractor: '+ContName+'('+farmName+')  '+'Report Generated On: '+serverTime;  
		    		                },
		    		                orientation: 'landscape',
		    		                pageSize: 'A4',
		    		                exportOptions: {
		    		                    columns: ':visible'
		    		                }
		    		            },
		    		            {
		    		                extend: 'print',
		    		                exportOptions: {
		    		                    columns: ':visible'
		    		                }
		    		            }
		    		        ]

        /*
        responsive: {
            details: {
                display: $.fn.dataTable.Responsive.display.modal( {
                    header: function ( row ) {
                        var data = row.data();
                        return 'Details for '+data[0]+' '+data[1];
                    }
                } ),
                renderer: $.fn.dataTable.Responsive.renderer.tableAll()
            }
        }
		*/
	
	} );


	jQuery('.dataTable').wrap('<div class="dataTables_scroll" />');   // wrapping datatable for sroll function




	$('#LoaValue').keypress(function( e ) {    
	    if(!/[0-9]/.test(String.fromCharCode(e.which)))
	        return false;
	});   
   
	$('#MaxEmp').keypress(function( e ) {    
	    if(!/[0-9]/.test(String.fromCharCode(e.which)))
	        return false;
	});

	
    
	//Get the modal
	var modal = document.getElementById('myModal');

	// Get the button that opens the modal
	var btn = document.getElementById("myBtn");

	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];

	// When the user clicks on the button, open the modal
	btn.onclick = function() {
	  //	alert('button');
	    modal.style.display = "block";
	}

	// When the user clicks on <span> (x), close the modal
	span.onclick = function() {
	    modal.style.display = "none";
	}

	// When the user clicks anywhere outside of the modal, close it
	window.onclick = function(event) {
	    if (event.target == modal) {
	        modal.style.display = "none";
	    }
	} 
    

  
  //  alert(new Date().toISOString());


	var loclist = document.getElementsByName('WOLOC');   // Get nodelist of location select box
	var first = loclist[0];
	var statelist = document.getElementsByName('LOCSTATE');
	//var othlist =  document.getElementsByName('LOCOTH');
    
    
	// Add event listener to first element 
	first.addEventListener("change", function() {   
	  //	alert(first.value);
    	changeLoc(first,'0');
    });	


    $('#locadd').click(function( e ) {    
	  //  alert('test');

	    $(".loctr").append(" <div class='locbr'>  <select name='WOLOC'> <option value=''>-Select-</option>"+
                " </select> <input type='text' name='LOCOTH' maxlength='60' style='display:none'> "+
                " State <select name='LOCSTATE' style='display:none'> <option value=''>-Select-</option> </select>"+
                " District <select name='LOCDIST' style='display:none'> <option value=''>-Select-</option> </select></div>  ");

		var ci = loclist.length- 1;	   
		var last = loclist[ci];
		var state=statelist[ci];
		// var locoth=othlist[ci];
		getWoState(ci);
		// Add event listener to last element and pass node and index as parameter
		last.addEventListener("change", function() {
		changeLoc(last,ci);
		});

		state.addEventListener("change", function() {
		getWoDistrict(ci);
		});  



        // Call Function to populate select box along with element index    
        getWoLocation(ci);  
	});
   
    
 // Remove Last location
    
	 $('#locdel').click(function( e ) {
	     //   alert("test");
	     
	    var locno=loclist.length; 
	    if(locno!=1)  
		{    
	      /* var brs=document.getElementsByClassName('locbr');
	       brs[brs.length - 1].remove();           // remove Division */

		   var locdiv=document.getElementsByClassName('locbr')[locno-1];    
		   locdiv.parentNode.removeChild(locdiv);

		} 
	}); 
		

	 function validate() {
		 if (confirm("Do you want to save new work order?") == false) 
	  		return false; 
		else 		
			return true;	
	}	

	function getZone()
	{
	  // alert("getZone");	
	  //	$('input[type=button]').button();
	  //	$('input[type=submit]').button();
	 //	$('#WODIV').selectmenu();
	 //	$('#WODEPT').selectmenu();
	 //	$('#WOAREA').selectmenu();
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
	   //	$('#WOZONE').selectmenu({change: function(event, ui) { getDivision(); } });
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
	  //	$('#WODIV').selectmenu({change: function(event, ui) { getDepartmentList(); } }).selectmenu("refresh");
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
	  //	$('#WODEPT').selectmenu("refresh");
	}


	function fillDesigList1(entries) {
	    var dataArray = JSON.parse(entries);
		//var sml = document.getElementById("selDesig");	
	        var sml1 = document.getElementById("inpIssAuth");
		//var opt = document.createElement("option");
	       var opt1 = document.createElement("option");
//		opt.text = "Select";
//		opt.value = "";
//		opt.disabled = false;
//		opt.selected = true;
	        opt1.text = "Select";
		opt1.value = "";
		opt1.disabled = false;
		opt1.selected = true;
	     //   $('#selDesig').empty();
	        $('#inpIssAuth').empty();
	        
		//sml.add(opt);
	        sml1.add(opt1);
		if (dataArray.rows.length > 0) {
	            $.each(dataArray.rows, function(index, data) {
	                  //  opt = document.createElement("option");
	                    opt1 = document.createElement("option");
	                  //  opt.text = data.shortdesig;
	                  //  opt.value = data.authid;
	                    opt1.text = data.shortdesig;
	                    opt1.value = data.authid;
	                  //  sml.add(opt);
	                    sml1.add(opt1);
	            });
		}
	}

	function getDesigList() {    
	    $('#selDesig').empty();
	    $('#inpIssAuth').empty();
	    $.post("/UtilityServlet",{action:"getDesigList1",zoneid:$('#WOZONE').val(),divid:$('#WODIV').val(),deptid:$('#WODEPT').val()},fillDesigList1,'text');
	}





	function validate_required(field,alerttxt) {
	 //	alert("validate_required");
		  with (field){
		      if (value.trim()=="") {
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

			  var loclist = document.getElementsByName('WOLOC');
			  var othlist = document.getElementsByName('LOCOTH');
			  var statelist = document.getElementsByName('LOCSTATE');
			  var distlist =  document.getElementsByName('LOCDIST');   

			   
				
			//alert('test');	   
			checkWONO();  
			if (validate_required(WorkOrd,"LOA NUMBER CAN'T BE BLANK!!")==false){return false}		
		//	if($("#chkWONO").val()==1){alert("WORK ORDER ALREADY  REGISTERED");return false;};
		//	 $.post("/Contractor",{action:"checkWONO",wono:$("#WorkOrd").val()},function(data){ if(data==1) {  alert("WORK ORDER ALREADY REGISTERED"); return false; } } );
			if (validate_required(WorkName,"LOA NAME CAN'T BE BLANK!!")==false){WorkName.focus();return false}
			if (validate_required(LoaValue,"LOA VALUE CAN'T BE BLANK!!")==false){LoaValue.focus();return false}
		//	if (validate_required(LocName,"LOA LOCATION CAN'T BE BLANK!!")==false){LocName.focus();return false}
			if (validate_required(StartDate,"LOA COMMENCEMENT DATE CAN'T BE BLANK!!")==false){StartDate.focus();return false}
			if (validate_required(EndDate,"LOA COMPLETION DATE CAN'T BE BLANK!!")==false){EndDate.focus();return false}
			if (validate_required(MaxEmp,"MAXIMUM NO OF EMPLOYEE CAN'T BE BLANK!!")==false){MaxEmp.focus();return false}
			if (validate_required(LabLic,"ENTER LABOUR LICENSE!!")==false){LabLic.focus();return false}		// added on 24.10.2018
		//	if (validate_required(selDesig,"PRINCIPAL EMPLOYER CAN'T BE BLANK!!")==false){selDesig.focus();return false}		
	        if (validate_required(WOZONE,"SELECT LOA ISSUING AUTHORITY ZONE !!")==false){WOZONE.focus();return false}
			if (validate_required(WODIV,"SELECT LOA ISSUING AUTHORITY DIVISION !!")==false){WODIV.focus();return false}
			if (validate_required(inpIssAuth,"SELECT LOA ISSUING AUTHORITY DESIGNATION !!")==false){inpIssAuth.focus();return false}
			if (validate_required(PrincEmpAdd,"PRINCIPAL EMPLOYER ADDRESS CAN'T BE BLANK!!")==false){PrincEmpAdd.focus();return false}
		//	if (validate_required(WODEPT,"LOA DEPARTMENT CAN'T BE BLANK!!")==false){ WODEPT.focus();return false}
		//	if (validate_required(WOAREA,"WORK ORDER AREA CAN'T BE BLANK!!")==false){WOAREA.focus();return false}
			if($("#EndDate").val()<$("#StartDate").val()){alert("LOA COMPLETION DATE CAN'T BE LESS THAN LOA COMMENCEMENT DATE");$("#EndDate").focus();return false}
		    if($('#ExtDate').val().trim()!="" && $("#ExtDate").val()<$("#EndDate").val()){alert("LOA COMPLETION DATE CAN'T BE LESS THAN LOA EXTENDED COMPLETION DATE");$("#ExtDate").focus();return false}
		/*	if($("#MaxEmp").val()>19)    //desabled on 24.10.2018
				{
		          if($("#LabLic").val().trim()=="")
		           {
		             alert("ENTER LABOUR LICENSE");  
		             return false;
		            }              
				}	*/	
			  for (i = 0; i < loclist.length; i++) 
				{
		         	var node=loclist[i];
		         	//alert(node.value);
		         	if(node.value=="")
			        {
	                 alert('LOA LOCATION CANT BE BLANK!!'); 
	                 return false;
				     }
		         	else if(node.value!='1')
			        {	
		         	 for(var j=i+1; j<loclist.length; j++)   // check duplicate entry in other location
			          {
				        if(loclist[i].value.trim().toUpperCase()==loclist[j].value.trim().toUpperCase())
	                   {
	                     alert('Location Already Selected!!Delete Or Select Different One');
	                    loclist[j].focus();
	                    return false;
	                   }                
				      } 
			        }  
		         	else
			        {
			         	  //alert(othlist[i].value);
			         	  //alert(statelist[i].value)
						  if(othlist[i].value.trim()=="")
						  {
							  alert('ENTER LOCATION!!'); 
							  return false;
						  }	
						  else
						  {
							  var result=checkOthloc(i);  
					         	// alert(result);
				               if(result==1)
				               {
				                 alert('Select This Location From Dropdown');
				                 othlist[i].focus();
				                 return false;
				               }  
					         	 
					         	 
					         	for(var j=i+1; j<othlist.length; j++)   // check duplicate entry in other location
						        {
							        if(othlist[i].value.trim().toUpperCase()==othlist[j].value.trim().toUpperCase())
				                 {
				                   alert('Location Already Entered!!Delete Or Add Different One');
				                   othlist[j].focus();
				                   return false;
				                  }    
				                 
							    }
							  
						   }	  	
						  
						  if(statelist[i].value=="")
						  {
							  alert('SELECT STATE!!'); 
							  return false; 
	                      
						  } 
						  if(distlist[i].value=="")
						  {
							  alert('SELECT DISTRICT!!'); 
							  return false; 
	                      
						  } 	  
	              	}
		      	} 

			}	
		}




		function uploadWOrder(woid)
		{
		  //alert(wono);
		 window.open('UploadWO.jsp?wono='+woid,'','menubar=no, toolbar=no, scrollbar=yes, width=700, height=200');
			
		}


		function viewWorkOrder(wofile)
		{
		  // alert(wofile);	
		  window.open('/Contractor?action=fetchWO&wofile='+wofile,'', 'menubar=no, toolbar=no, scrollbars=yes, resizable=no, width=300, height=700');
		}
			

	    function checkWONO()
	    {
	      $.ajax({
	    		   type: 'POST',
	    		   url: '/Contractor?action=checkWONO&wono='+$("#WorkOrd").val().trim(), 
	    		   async: false, // <<== THAT makes us wait until the server is done.
	    		   success: function(data){        		   
	    			 //  $("#chkWONO").val(data); // So we can use it
	    			   if(data==1)
	        		   {	   
	    			   $("#WorkOrd").val('');
	    			   alert("LOA ALREADY REGISTERED");
	        		   }
	    			   
	    		   }
	    	});   

	     //  $.post("/Contractor",{action:"checkWONO",wono:$("#WorkOrd").val()},function(data){ if(data==1) {  alert("WORK ORDER ALREADY REGISTERED"); $("#WorkOrd").val(''); } } );	
	      //	$.post("/Contractor",{action:"checkWONO",wono:$("#WorkOrd").val()},resultWONO,'text');
	    	 

	    }

	    function resultWONO(result)
	    { 
	      //alert(result);
	      $("input[type=submit]").removeAttr('disabled');
	      if(result==1)
	      {
	          alert("LOA No already registered");
	          $("input[type=submit]").attr('disabled','disabled');
	       }     
	     }  


	    //   Populate selectbox of the intended node

	    function getWoLocation(si)
	    {
	       // alert(si);
	    	 $.post(
	    	        '/UtilityServlet', 
	    	        {action:'getWoLocation'},
	    		    function(data){    	        	 
	    	        	 var dataArray = JSON.parse(data);
	    	        		if(dataArray!=null) {

	    	        			 var node = document.getElementsByName('WOLOC');
	    	        			 var nodelast=node[si];
	                         //    alert(nodelast.value);                             
	                         /*    for (i = 0; i < nodelast.length; i++) {
	                            	 nodelast.remove(0);
	                         	}  */
	                             var opt = document.createElement("option");
	                         	 opt.text = "Select";
	                         	 opt.value = "";
	                         	// opt.disabled = false;
	                         	 opt.selected = true;
	                         	 nodelast.add(opt);
	    	        		   
	    	        			$.each(dataArray, function(index,data) {
	        	        		  //	alert(data.display);
	        	        			opt = document.createElement("option");
	        	                    opt.text = data.display;
	        	                    opt.value = data.option;
	        	                    nodelast.add(opt);

	      	        			});	
	    	        			
	    	        	}    			  
	    		   }
	    	 );
	   }


	    // Show/hide intended other location
	    
	    function changeLoc(node,si)
	    {
	    //  alert('changeLoc');
	    //  alert(node.value);
	    //  alert(si);
	      var othlist=document.getElementsByName("LOCOTH");
	      var nodeoth=othlist[si];
	      var statelist=document.getElementsByName("LOCSTATE"); 
	      var state=statelist[si];
	      var distlist=document.getElementsByName("LOCDIST");
	      var dist=distlist[si];
	      if(node.value==1)
	      {
	    	  nodeoth.style.display='inline';
	    	  state.style.display='inline';
	    	  dist.style.display='inline';
	          }   
	      else
	          {
	    	  nodeoth.style.display='none';
	    	  state.style.display='none';
	    	  dist.style.display='none';
	    	  nodeoth.value="";
	    	  state.value="";
	    	  dist.value="";
	           
	          }
	     }


	    function getWoState(si)
	    {
	       // alert(si);
	    	 $.post(
	    	        '/UtilityServlet', 
	    	        {action:'getWoState'},
	    		    function(data){    	        	 
	    	        	 var dataArray = JSON.parse(data);
	    	        		if(dataArray!=null) {

	    	        			 var node = document.getElementsByName('LOCSTATE');
	    	        			 var nodelast=node[si];
	                         //    alert(nodelast.value);                             
	                         /*    for (i = 0; i < nodelast.length; i++) {
	                            	 nodelast.remove(0);
	                         	}  */
	                             var opt = document.createElement("option");
	                         	 opt.text = "Select";
	                         	 opt.value = "";
	                         	// opt.disabled = false;
	                         	 opt.selected = true;
	                         	 nodelast.add(opt);
	    	        		   
	    	        			$.each(dataArray, function(index,data) {
	        	        		  //	alert(data.display);
	        	        			opt = document.createElement("option");
	        	                    opt.text = data.display;
	        	                    opt.value = data.option;
	        	                    nodelast.add(opt);

	      	        			});	
	    	        			
	    	        	}    			  
	    		   }
	    	 );
	   }

	    function getWoDistrict(si)
	    {
	     //  alert(si);
	       var statelist=document.getElementsByName("LOCSTATE"); 
	       var state=statelist[si];   
	    //   alert(state.value);    
	    	 $.post(
	    	        '/UtilityServlet', 
	    	        {action:'getWoDistrict',stateid:state.value},
	    		    function(data){    	        	 
	    	        	 var dataArray = JSON.parse(data);
	    	        		if(dataArray!=null) {

	    	        			 var node = document.getElementsByName('LOCDIST');
	    	        			 var nodelast=node[si];
	    	        			 var len = nodelast.length;
	 	        				 for (i = 0; i < len; i++) {
	 	        					nodelast.remove(0);
	 	        				 }
	                         //    alert(nodelast.value);                             
	                         /*    for (i = 0; i < nodelast.length; i++) {
	                            	 nodelast.remove(0);
	                         	}  */
	                             var opt = document.createElement("option");
	                         	 opt.text = "Select";
	                         	 opt.value = "";
	                         	// opt.disabled = false;
	                         	 opt.selected = true;
	                         	 nodelast.add(opt);
	    	        		   
	    	        			$.each(dataArray, function(index,data) {
	        	        		  //	alert(data.display);
	        	        			opt = document.createElement("option");
	        	                    opt.text = data.display;
	        	                    opt.value = data.option;
	        	                    nodelast.add(opt);

	      	        			});	
	    	        			
	    	        	}    			  
	    		   }
	    	 );
	   }  


	   function checkOthloc(si)
	   {
	     // alert(si);
	     // alert("loc"+$('input[name="LOCOTH"]').eq(si).val());
	      var result=0;
	      $.ajax({
			   type: 'POST',
			   url: '/UtilityServlet?action=checkOthloc&locoth='+$('input[name="LOCOTH"]').eq(si).val().trim(), 
			   async: false, 
			   success: function(data){      		   
				 
			   if(data==1)
	   		   {	   
				  result=1;  	   
	   		   }
				   
			}
		}); 

	    return result;
	         
	 } 

	    

    </script>
    
</body>

</html>
