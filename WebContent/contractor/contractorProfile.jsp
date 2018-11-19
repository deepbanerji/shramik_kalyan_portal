<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@ page import="org.cris.clip.dto.ContractorDTO" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/css/w3.css">
<SCRIPT type="text/javascript" src="/contractor/theme/json2.js"></SCRIPT>
<SCRIPT type="text/javascript" src="/jquery-2.0.0.min.js"></SCRIPT>
<SCRIPT type="text/javascript" src="/js/clip-js-utils.js"></SCRIPT>
<SCRIPT type="text/javascript" src="/js/jQueryTable/jquery.dataTables.min.js"></SCRIPT>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<title>Contractor Profile</title>
<script type="text/javascript">
 
function getConProfile()
{
  //	alert("getConProfile");
    $('input[type=submit]').button();
	$.post("/Contractor",{action:"getConProfile"},fillConProfile,'text');
}

function fillConProfile(names)
{
   var data = JSON.parse(names);	
  // alert(data.name);	 
   $("#conName").val(data.name);  
   $("#firmName").val(data.firmName);
   $("#firmReg").val(data.firmReg);
   $("#permAdddr").val(data.permaddr);
   $("#permPin").val(data.permpin);
   $("#permState").val(data.permState);
   $("#mobNo").val(data.mobno);
   $("#email").val(data.email);
   $("#pan").val(data.pan.toUpperCase());
   $("#conID").val(data.conID);	
	
}	


function validateForm()
{  
 alert("validateForm");
 if($("#inpPwd").val()!=$("#rePwd").val())
  {
   alert("Password and Reenter Password do not match ")
   return false;
	  }	 

	}

</script>

<style type="text/css">

html, body{
   height:100%;
   width:100%;  

  }

th:first-child {
    border-radius: 6px 0 0 0;
}

th:last-child {
    border-radius: 0 6px 0 0;
}

th:only-child{
    border-radius: 6px 6px 0 0;
}
.hoverTable{
		width:100%; 
		border-collapse:collapse; 
		color: grey;		
	}
	.hoverTable td{ 
		padding:7px; border:#4e95f4 1px solid;
	}
	/* Define the default color for all the table rows */
	.hoverTable tr{
		background: white;
	}
	/* Define the hover highlight color for the table row */
    .hoverTable tr:hover {
          background-color: #ffff99;
    }
    
  input {  
   border: none;
   border-color: transparent;  
  }
 /*input {
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
} */



</style>

</head>
<%
HttpSession httpSession=request.getSession(false);
	ContractorDTO conDTO=(ContractorDTO) httpSession.getAttribute("contractor");
if(conDTO==null)
{
	System.out.println("invalid dto");
	response.sendRedirect("/clip.jsp");		
}

%>
<body onload="getConProfile();">
 
  <input type="hidden" name="conID" id="conID">
   <div class="w3-panel w3-light-green ui-corner-all" >	 
   <div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Contractor Profile</b></h4></div>
   <div>
   <table class="hoverTable">
    <tr> <td > Name &nbsp; &nbsp;<input type="text" name="conName" id="conName" readonly> </td>
    <td> Firm Name &nbsp; &nbsp;<input type="text" name="firmName" id="firmName" readonly> </td>
    <td> Firm Registration &nbsp;&nbsp;<input type="text" name="firmReg" id="firmReg" readonly> </td>
    </tr>
    <tr> <td > Address &nbsp;&nbsp;<input type="text" name="permAdddr" id="permAdddr" readonly>  </td>
       <td> PIN &nbsp;&nbsp;<input type="text" name="permPin" id="permPin" readonly></td> 
      <td> State &nbsp;&nbsp;<input type="text" name="permState" id="permState" readonly> </td> 
    </tr>
    <tr> <td> Mobile &nbsp;&nbsp;<input type="text" name="mobNo" id="mobNo" readonly> </td>
       <td> Email &nbsp;&nbsp;<input type="text" name="email" id="email" readonly> </td>
     <td> PAN &nbsp;&nbsp; <input type="text" name="pan" id="pan" readonly>  </td>      
     </tr>
   </table>
   </div>
   <br>
   <div>
    <fieldset style="width: 50%;color: #000080" class="ui-corner-all">
      <form action="/Contractor?action=resetConPass" method="post" onsubmit="return validateForm()">
      <label for="inpPwd">Password </label><input type="password" id="inpPwd" name="inpPwd" style="width: 25%;"  minlength="8"  maxlength="10" required></input>
      &nbsp; &nbsp; <label for="inpPwd">Reenter Password </label><input type="password" id="rePwd" name="rePwd" style="width: 25%;" minlength="8" maxlength="10" required></input><br><br>
      <input type="submit" value="Reset Password" ></input>
      </form> 
     </fieldset>
   </div>
  </div> 
</body>
</html>