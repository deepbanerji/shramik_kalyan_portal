<%@ page language="java" contentType="text/html; charset=ISO-8859-1"     pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<SCRIPT type="text/javascript" src="/jquery-2.0.0.min.js"></SCRIPT>
<title>upload work order</title>
<script type="text/javascript">

$(document).ready(function() {
 // alert("function");
 $("#woFile").change(function(e){ 
  // alert("upload"); 
   var type = this.files[0].type;
  // alert(type);	
   if(type!='application/pdf')
   {
	   alert("Upload PDF file only");
       $('#woFile').val('');
	   } 	   	  
   if(this.files[0].size > 5242880)
	   {
        alert("File size is too long");
        $('#woFile').val('');
	   }
	 });


 $( "#remove" ).click(function() {
	 // alert( "remove called." );
	 $('#woFile').val('');
	});
	
});


function validateform()
{        
	if($("#woFile").val()=="")
		{  alert("Select File");
		  return false;   }  
   
}



</script>
</head>
<%
 String wono=request.getParameter("wono");
 System.out.println(wono);
%>
<body>
<form name="UploadWOForm" enctype="multipart/form-data" action="/Contractor?action=uploadWO&wono=<%=wono%>" method="POST" onsubmit="return validateform();">
 
<p align="center"><b> <font size="5"> Upload LOA PDF (Max Size 5MB)</font> </b> </p>
<p align="center"> Select File &nbsp;&nbsp; <input type="file" name="woFile" id="woFile">   </p>

<p align="center">  <input type="submit" value="Upload"> <input type="button" value="Remove" id="remove"> </p>

</form>
</body>
</html>