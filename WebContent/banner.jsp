<%@ page language="java" contentType="text/html;"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>
$(function() {
	//$('#divBanner').height( Math.floor( window.innerHeight * 0.61 ) +"px");
        //$('#divBanner').css('line-height', Math.floor( window.innerHeight * 0.61 ) +"px");
});
</script>
<style>
     #header {position: relative;top: 0;background-color: darkgreen;color: white;padding:5px;} 
</style>
</head>
<body>
   <div id="header" >  
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
 <!-- <div id="divBanner" class="ui-widget ui-corner-all">
 	<img style="cursor: pointer;vertical-align: middle;margin: 0.5%;width: 3.25%;" onclick="window.open('http://indianrailways.gov.in')" id="logoRailway" src="/img/IRLOGO.png" title="Indian Railways" alt="Indian Railways"/>
        <img style="vertical-align: middle;margin: 0.5% 0.25%;width: 6.25%" id="logoSwachBharat" src="/img/SWACHHBHARATLOGO.png" title="Swachh Bharat" alt="Swachh Bharat"/>
        <span style="margin-left: 22.75%;vertical-align: middle;font-size: 1.35vw;"> भारतीय रेल श्रमिक कल्याण पोर्टल<br>Indian Railway Shramik Kalyan Portal</span>
        <img style="cursor:pointer;float: right;margin: 0.5%;width: 4.25%;vertical-align: middle" onclick="window.open('http://cris.org.in')" id="logoCRIS" src="/img/CRISLOGO.png" title="Center for Railway Information Systems" alt="CRIS"/>
                	
 </div>  -->
</body>
</html>