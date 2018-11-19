<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="org.cris.clip.dto.AdministratorDTO"%>
<%@ page language="java" contentType="text/html; "
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>IR Shramik Kalyan Portal</title>
<script type="text/javascript" src="/jquery-2.0.0.min.js"></script>
<link rel="stylesheet" href="/css/loader.css"></link>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<link rel="icon" href="/img/irlogo1.png">
<style>
#divBanner {position: relative;top: 0;background-color: darkgreen;color: white;margin-bottom: 10px;padding: 5px} 
#right {	
	width: 83%;
	float : right;
	border: thin solid transparent;
}
#footer {background-color: darkgreen;position: fixed;right: 0;bottom: 0;left: 0;color: white;padding: 5px;text-align: center;line-height: 17px;}
/* Fixed sidenav, full height */
.sidenav {
    height: 100%;
    float:left;
    /*padding : 3px;*/
    /*margin-left : 5px;*/
    width: 16.4%; 
    z-index: 1;
    /*top: 0;*/
    /*left: 0;*/
    background-color: #111;
    overflow-x: hidden;
    
}

/* Style the sidenav links and the dropdown button */
.sidenav a, .dropdown-btn {
    padding: 6px 8px 6px 16px;
    text-decoration: none;    
    /*color: #818181;*/
    color: white;
    display: block;
    border: none;
    background: none;
    width: 100%;
    text-align: left;
    cursor: pointer;
    outline: none;
}

/* On mouse-over */
.sidenav a:hover, .dropdown-btn:hover {
    /*color: #f1f1f1;*/
    color : #00ffff;
    font-size: 14;
    /*background-color: #DCDCDC;*/
}

/* Add an active class to the active dropdown button */
.active {
    background-color: green;
    color: white;
}

/* Dropdown container (hidden by default). Optional: add a lighter background color and some left padding to change the design of the dropdown content */
.dropdown-container {
    display: none;
    background-color: white;
    /*background-color: #262626;*/
    padding-left: 8px;
}
.dropdown-container a:HOVER font {
	font-weight: bold;
        color: gray;
}
body {
   background-image: url("/img/background.png");
}
</style>
<script>
history.forward();
var lastHyperlinkClicked = "";
function doLogout() {
	$.post("/Login",{action:"logout"},function(v){
            if(v==1)
                    alert("You've successfully logged out!");
            else
                    alert("Session timed out!");
            window.location='/clip.jsp';},'text');
}
function approveContractor(id) {
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/contractorApproval.jsp";
}
function showApprovedContractors(id) {
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/contractorsApproved.jsp";
}
function displayDashboard() {
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }        
	document.getElementById("content").src="/Administrator/adminDashboard.jsp";
}
function createUser(id) {
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/createUser.jsp";
}
function approveWO(id){
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/woApproval.jsp";
}
function laborReport(id){
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/laborReport.jsp";
}
function wagesReport(id){
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/wagesReport.jsp";
}
function resetContPwd(id){
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/resetContPassword.jsp";
}
function userMgt(id){
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/userMgt.jsp";
}
function minWages(id){
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/newminwages.jsp";
}
function profile(){
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
	document.getElementById("content").src="/Administrator/profile.jsp";
}
function probRegn(){
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
	document.getElementById("content").src="/regGrievance.jsp";
}
function initialize(){
	document.getElementById("content").height=Math.floor(window.innerHeight/1.10);
	document.getElementById("content").width=Math.floor(window.innerWidth/1.25);
}
function showApprovedWorkOrders(id) {
        if(lastHyperlinkClicked!="") {
            $('#'+lastHyperlinkClicked).css('font-weight', 'normal');
        }
        lastHyperlinkClicked = id;
        $('#'+id).css("font-weight", "bold");
	document.getElementById("content").src="/Administrator/workOrdersApproved.jsp";
}
$(function() {	
	//$('#divBanner').height( Math.floor( window.innerHeight * 0.087 ) +"px");
        //$('#divBanner').css('line-height', Math.floor( window.innerHeight * 0.087 ) +"px");
	$('#loader').hide();
	$('#right').height(window.innerHeight*0.840);
	$('#content').width(window.innerWidth*0.8229);
	$('#content').height(window.innerHeight*0.840);
	
	initialize();
	var lastMenuButtonClickedId="";

	var dropdown = document.getElementsByClassName("dropdown-btn");
	var i;
	for (i = 0; i < dropdown.length; i++) {
	  dropdown[i].addEventListener("click", function() {
	    this.classList.toggle("active");
	    var dropdownContent = this.nextElementSibling;
            
	    if (dropdownContent.style.display == "block") {	                    
                $("#"+dropdownContent.id).slideUp("slow");
	    } 
	    else {
                $("#"+dropdownContent.id).slideDown("fast");
                if(lastMenuButtonClickedId!="" && lastMenuButtonClickedId!=this.id) {
                    $('#contDiv'+lastMenuButtonClickedId).slideUp("slow");
                    $('#'+lastMenuButtonClickedId).removeClass("active");	
                }	  
	        lastMenuButtonClickedId = this.id;   
	    }
	    
	  });
	}
	
});
</script>
</head>
    <body class="ui-corner-all">
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
<div id="divBanner" class="ui-widget ui-corner-all">
    <div style="display: table-cell;float: left;width: 20%;vertical-align: middle">
        <img style="cursor: pointer;vertical-align: middle;margin: 0.5%;width: 58px;" onclick="window.open('http://indianrailways.gov.in')" id="logoRailway" src="/img/IRLOGO.png" title="Indian Railways" alt="Indian Railways"/>
        <img style="vertical-align: middle;margin: 0.5% 0.25%;width: 110px;" id="logoSwachBharat" src="/img/SWACHHBHARATLOGO.png" title="Swachh Bharat" alt="Swachh Bharat"/>
    </div>
    <div style="display: table-cell;font-size: x-large;text-align: center;padding-right: 19.5%;vertical-align: middle">
        भारतीय रेल श्रमिक कल्याण पोर्टल<br>Indian Railway Shramik Kalyan Portal
    </div>
    <div style="display: table-cell;width: 0.05%;vertical-align: middle">
        <img style="cursor:pointer;width: 78px;vertical-align: middle;padding: 0px;margin: 0px" onclick="window.open('http://cris.org.in')" id="logoCRIS" src="/img/CRISLOGO.png" title="Centre for Railway Information Systems" alt="CRIS"/>                                                   
    </div>           
</div>


<div class="sidenav ui-corner-all" style="display:inline-block">
  <a href="#" onclick="displayDashboard();"><span class=""></span>&nbsp;<font size="4" face="arial">Home</font></a>

  <button id="1" class="dropdown-btn" ><span class=""></span>&nbsp;<font size="4" face="arial">Contractor</font></button>
        <div id="contDiv1" class="dropdown-container">
                
           <a href="#" id="a1" onclick="approveContractor(this.id);" ><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">Contractor Verification</font></a>
           
           <a href="#" id="a2" onclick="showApprovedContractors(this.id);"><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">Verified Contractors</font></a>
           <%if(adto.getPrivContrApprove().equals("1")) { %>
           <a href="#" id="a3"  onclick="resetContPwd(this.id);"><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">Change Contractor Password</font></a>
           <%} %>
        </div>

  <button id="2" class="dropdown-btn" style="font-size: 18px"><span class=""></span>&nbsp;<font size="4" face="arial">Letter of Acceptance (LOA)</font></button>
        <div id="contDiv2" class="dropdown-container">
        
           <a href="#" id="a4"  onclick="approveWO(this.id);"><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">LOA Verification</font></a>
           
           <a href="#" id="a5"  onclick="showApprovedWorkOrders(this.id);"><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">Verified LOAs</font></a>
        </div>

<%if( adto.getLevelId().equals("0") || adto.getPrivUserCreate().equals("1") || adto.getPrivUserCreate().equals("2") ) {%>
  <button id="3" class="dropdown-btn" ><span class=""></span>&nbsp;<font size="4" face="arial">Administration</font></button>
          <div id="contDiv3" class="dropdown-container">
            <%if( adto.getPrivUserCreate().equals("1") || adto.getPrivUserCreate().equals("2") ) { %>
            <a href="#" id="a6"  onclick="createUser(this.id);"><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">Railway Authority Account Creation</font></a>
            <%} %>            
            
            <a href="#" id="a7"  onclick="userMgt(this.id);"><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">Railway Authority Account Management</font></a>
                        
            <%if( adto.getLevelId().equals("0") ) {%>
            <a href="#" id="a8"  onclick="minWages(this.id);"><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">Minimum Wages</font></a>
            <%} %>
          </div>
<%} %>

  <button id="4" class="dropdown-btn" ><span class=""></span>&nbsp;<font size="4" face="arial">Reports</font></button>
        <div id="contDiv4" class="dropdown-container">
            <a href="#" id="a9" onclick="laborReport(this.id);"><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">Engaged Workman List</font></a>
            <a href="#" id="a10" onclick="wagesReport(this.id);"><span class=""></span>&nbsp;<font size="3" face="arial" color="#111">Workman Wages</font></a>
          </div>

    
  <a href="#"  onclick="probRegn();"><span class=""></span>&nbsp;<font size="4" face="arial">Feedback</font></a>
  
  <a href="#"  onclick="profile();"><span class=""></span>&nbsp;<font size="4" face="arial">My Profile</font></a>

  <a href="#"  onclick="doLogout();"><span class=""></span>&nbsp;<font size="4" face="arial">Logout</font></a>

</div>

<div id="right" style="display:inline-block">
	<iframe frameborder="0" scrolling="yes" name="content" id="content" src="/Administrator/adminDashboard.jsp"></iframe>
</div>
<div id="footer"><span style="font-size: 14px">© 2018, Ministry of Railways, Govt. of India. All rights reserved.<br>Designed & Developed by <a title="Centre for Railway Information Systems" style="text-decoration: underline;cursor:pointer;color: white;" onclick=window.open("http://www.cris.org.in")>CRIS | <a style="text-decoration: underline;cursor:pointer;color: white;" href="/manual/RAILWAY_MODULE.pdf" download>Help</a></span></div>

</body>
</html>