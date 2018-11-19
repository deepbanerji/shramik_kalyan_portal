<%@ page language="java" contentType="text/html; charset=ISO-8859-1"     pageEncoding="ISO-8859-1"%>
<%@ page import="org.cris.clip.dto.ContractorDTO" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page import="java.util.ArrayList,org.cris.clip.dao.WorkOrder,org.cris.clip.dto.WorkOrderDTO"%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">

<link rel="stylesheet" href="/css/w3.css">

<link rel="stylesheet" href="/css/datatables/jquery.dataTables.min.css">
<link rel="stylesheet" href="/css/datatables/dataTables.jqueryui.min.css">
<link rel="stylesheet" href="/css/datatables/buttons.dataTables.min.css">

<script src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>

<script src="/js/datatables/jquery-1.12.4.js"></script>
<script src="/js/datatables/jquery.dataTables.min.js"></script>
<script src="/js/datatables/dataTables.jqueryui.min.js"></script>
<script src="/js/datatables/dataTables.buttons.min.js"></script>
<script src="/js/datatables/jszip.min.js"></script>
<script src="/js/datatables/pdfmake.min.js"></script>
<script src="/js/datatables/vfs_fonts.js"></script>
<script src="/js/datatables/buttons.html5.min.js"></script>
<script src="/js/datatables/buttons.print.min.js"></script>

<script>

$(document).ready(function() {
	
	var table = $('#workOrderTable').DataTable( {

		"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		
		dom: 'Bfrtip',

		buttons: [
            'copyHtml5',
            'excelHtml5',
            'csvHtml5',
            {
                extend: 'pdfHtml5',
                orientation: 'landscape',
                pageSize: 'LEGAL'
            },
            'print'
        ]
	    
	} );

	/*
	$('#workOrderTable tbody').on('mouseover', 'tr', function () {
        var data = table.row( this ).data();

        //this.addClass('font-size:15px');
        
        //alert( 'You clicked on '+data[0]+'\'s row' );

        
    } );
    */

} );

</script>


<title>Work Order Details </title>

</head>

<body>

<%
	HttpSession httpSession=request.getSession(false);
	ContractorDTO conDTO=(ContractorDTO) httpSession.getAttribute("contractor");
	ArrayList al1 = new ArrayList();
	al1 = new WorkOrder().listWO(Integer.parseInt(conDTO.getId()));		
%>

<form>

<div class="w3-panel w3-pale-blue">	 

	<!--  
	<h3 class="ui-corner-all" style="color: white;background-color: #000080;padding: 10px;display: inline-block;margin-top: 1em;">Work Order Details</h3>
	<p> <input type="button" name="Create New Work Order" value="Create New Work Order" onclick="window.location='NewWorkOrder.jsp'" /> </p>
	-->
 			
	<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'>
		<h4> <b> Work Order Details : </b> </h4>
	</div>
	
	<div class='w3-animate-bottom'>
		<input type="button" class="w3-btn w3-round-xlarge w3-card-4 w3-pale-green w3-hover-green" id="createNewWorkOrder" value="Create New Work Order" style="display:inline;float:left" onclick="window.location='NewWorkOrder.jsp'">
	</div>
	<br>
	<div class='w3-animate-right'>		
		<hr>
		<table id="workOrderTable" class="display cell-border w3-card-4 w3-pale-green w3-border w3-border-green" style="font-size:12px;" cellspacing="0" width="100%">
		 <thead>
	            <tr>
	                <th>Sl. No.</th>
					<th>Number</th>
					<th>Work Name</th>					
					<th>Commencement Date</th>
					<th>Completion Date</th>
					<th>Extended Completion Date</th>
					<th>Location</th>	
					<th>Issued By</th>									
					<th>Principal Employer</th>
					<th>Principal Employer Address</th>					
					<th>Zone/ Division/ Department</th>
					<th>Area</th>
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
					<td><%=wodto.getWO()%> &nbsp;&nbsp; <% if(wodto.getWoFileNo()==0){ %>
							<a href="#" onclick="uploadWOrder(<%=wodto.getId()%>)"> <img src="images/upload.jpg" height="20" width="20" title="Upload Work Order">  </a> 
						<% } else { %>
							<a href="/Contractor?action=downloadWO&fileno=<%=wodto.getWoFileNo()%>"> <img src="images/attachment.png" height="20" width="20">  </a>
						<% } %>			
					</td>
					<td><%=wodto.getWork_Name()%></td>
					<td><%=wodto.getCommDate()%></td>
					<td><%=wodto.getEndDt()%></td>
					<td><%=wodto.getExtEndDt()%></td>
					<td><%=wodto.getWorkLoc()%></td>
					<td><%=wodto.getIssueBy()%></td>
					<td><%=wodto.getPrincEmp()%></td>
					<td><%=wodto.getPincEmpAddr()%></td>
					<td><%=wodto.getWoZone()%>/<br><%=wodto.getWoDiv()%>/<br><%=wodto.getWoDept()%></td>
					<td><%=wodto.getWoArea()%></td>
					<td><%=wodto.getApprovalStatus()%></td>			
				</tr>
				
				<% } } %>
			</tbody>
			
		</table>
		<hr class="w3-black">
		
	</div>
	

	
	<br>
	<br>
	<br>
	
</div>

</form>

</body>

</html>
