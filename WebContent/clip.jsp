<%-- 
    Document   : clip
    Created on : 20 Feb, 2018, 2:27:41 PM
    Author     : Deep Banerji
--%>

<%@page import="org.cris.clip.dto.WorkMenDTO"%>
<%@page import="org.cris.clip.dto.ContractorDTO"%>
<%@page import="org.cris.clip.dto.AdministratorDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Cache-Control" content="no-cache">
        <title>IR Shramik Kalyan Portal</title>
        <script type="text/javascript" src="/jquery-2.0.0.min.js"></script>  
        <script type="text/javascript" src="/js/captcha.js"></script>
        <link href='https://fonts.googleapis.com/css?family=Michroma' rel='stylesheet'>
        <link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
        <script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
        <script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
        <link rel="stylesheet" href="/css/loader.css"></link>
        <link rel="stylesheet" href="/css/w3.css">
        <link rel="icon" href="/img/irlogo1.png">
        <script>
            //history.forward();
            if (window.self != window.top) 
                window.top.location.replace(window.self.location.href);
            function chk_browser_version() {	
                var ua = window.navigator.userAgent;
                var mozff = ua.indexOf ( "Firefox/" );
                var chrome = ua.indexOf ( "Chrome/" );
                if (mozff<0 && chrome<0) {
                    alert("Please use Google Chrome or Mozilla Firefox web browsers!");
                    buttons = document.getElementsByTagName("button");
                    for (var i = 0; i < buttons.length; i++) {
                        buttons[i].disabled = true;
                    }
                }
            }
            var userType="a";
            function setUserType(type) {
                userType = type;
            }
          /*  function checkUserType() {
            	  $("#loginForm").attr("action","/Login?action=login&type="+userType);
                  $("#loginForm").submit();  
            } */
            function checkUserType() {
                if($('#inpUsername').val()=="") {
                    alert('Please input username!');
                    return;
                }
                if($('#inpPwd').val()=="") {
                    alert('Please input password!');
                    return;
                }
                forceMatchCaptcha();
                if($('#txtInput').val().trim()=="") {                
                    $( "#blankCaptcha" ).dialog("open");                    
                    return;
                } 
                if(CheckValidCaptcha()) {               
                    $("#loginForm").attr("action","/Login?action=login&type="+userType);
                    $("#loginForm").submit();
                }
                else {
                    $( "#wrongCaptcha" ).dialog("open");
                    generateCaptcha();
                    $('#txtInput').val('');                    
                }    
            }  
            function submitFunction(event) {
                if(event.which===13)
                    checkUserType();
            }
            function submitFunction(event) {
                if(event.which===13)
                    checkUserType();
            }
            $(function() {
                $('#loader').hide();
                $(document).ajaxStart(function(){
                    $("#loader").css("display", "block");
                });
                $(document).ajaxComplete(function(){
                    $("#loader").css("display", "none");
                });
                generateCaptcha();
                var rand = Math.floor((Math.random() * 4) + 1);
                document.getElementById('divPicHolder').style.backgroundImage = "url('/img/pic"+rand+".jpg')";
                var temp = $( window ).height()-$( '#header' ).height()-$( '#footer' ).height() - 50;
                document.getElementById('divPicHolder').style.height = temp+"px";
                
                //document.getElementById("siteintro").rows = Math.floor( window.innerHeight * 0.006 );
                
                $('#divPicHolder').width( window.innerWidth +"px");
                //$('#dialog-form').width("250px");
                $('#dialog-form').width( ($('#pSignInRly').width() + 55) + "px");
                $( "#wrongCaptcha" ).dialog({
                    autoOpen: false,
                    modal:true,
                    buttons: {
                        Close: function() {                            
                          $( "#wrongCaptcha" ).dialog( "close" );
                          $('#txtInput').focus();
                        }
                      }
                });
                $( "#blankCaptcha" ).dialog({
                    autoOpen: false,
                    modal:true,
                    buttons: {
                        Close: function() {                            
                          $( "#blankCaptcha" ).dialog( "close" );
                          $('#txtInput').focus();
                        }
                      }
                });
                chk_browser_version();
            });
            
        </script>
        <style> 
            label, input[type=text],input[type=password] { display:block;}
            button[type=button] {margin-top: 10px;margin-bottom: 10px}
            #txtInput {-moz-user-select: none; 
                       -webkit-user-select: none; 
                       -ms-user-select:none; 
                       -o-user-select:none;                       
            }
            input.text { margin-bottom:12px; width:97%; padding: .35em; }
            fieldset { padding:0; border:0;}            
            body,html {width: 100%;margin:0;padding:0;}
            #container {width: 100%;position: relative}
            #header {background-color: darkgreen;color: white;position: fixed;right: 0;top: 0;left: 0;padding: 5px}            
            #body {position: relative;white-space: nowrap;width:100%;margin-top: 96px;}                       
            #right1 {text-align: center;padding-top: 4%;padding-bottom: 4%;margin-bottom: 8%}
            #footer {background-color: darkgreen;position: fixed;right: 0;bottom: 0;left: 0;color: white;padding: 5px;margin: 0;text-align: center;line-height: 15px}
            .w3-card-4 img {display:block;margin:auto;width: 100%;height: 100%;animation-duration: 0.5s}
            #right button {margin: 1%;padding: 8px;width: 90%;}
            textarea {
                -webkit-box-sizing: border-box;
                -moz-box-sizing: border-box;
                box-sizing: border-box;
                -webkit-box-shadow: none;
                -moz-box-shadow: none;
                box-shadow: none;
                outline:none;
                width: 100%;
                resize: none;
                border: none;
                font-style: italic;
                font-family: cursive;
                font-size: 24px;
                display: inline;
            }
            @font-face {
                font-family: Diner-Regular;
                src: url('/Diner-Skinny/Diner-Regular.ttf');
            }

        </style>
    </head>
    <%  response.setHeader("Pragma","no-cache"); // HTTP 1.0
        response.setHeader("Cache-Control","no-store"); // HTTP 1.1
        response.setDateHeader("Expires", 0);
        String usertype = (String)(request.getSession(false)).getAttribute("usertype");
        if(usertype!=null) {
            if(usertype.equalsIgnoreCase("a")) {                
                AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
                if(adto!=null)
                    response.sendRedirect("/Administrator/AdminHome.jsp");
            }    
            else if(usertype.equalsIgnoreCase("c")) {   
                ContractorDTO cdto = (ContractorDTO)(request.getSession(false)).getAttribute("contractor");
                if(cdto!=null)
                     response.sendRedirect("/clipHome.jsp");
            }
            else if(usertype.equalsIgnoreCase("e")) {
                WorkMenDTO wdto = (WorkMenDTO)(request.getSession(false)).getAttribute("employee");
                if(wdto!=null)
                    response.sendRedirect("/Employee/EmployeeHome.jsp");
            }
            return;
        }        
    %>
    <body>
        <div id="loader"></div>
        <div id="wrongCaptcha" title="Error"><p>Wrong Captcha Code! Please try again.</p></div>
        <div id="blankCaptcha" title="Error"><p>Please enter Captcha Code!</p></div>
        <div id="container">
            <div id="header">  
                <div style="display: table-cell;float: left;width: 20%;vertical-align: middle">
                    <img style="cursor: pointer;vertical-align: middle;margin: 0.5%;width: 66px;" onclick="window.open('http://indianrailways.gov.in')" id="logoRailway" src="/img/IRLOGO.png" title="Indian Railways" alt="Indian Railways"/>
                    <img style="vertical-align: middle;margin: 0.5% 0.25%;width: 112px;" id="logoSwachBharat" src="/img/SWACHHBHARATLOGO.png" title="Swachh Bharat" alt="Swachh Bharat"/>
                </div>
                <div style="display: table-cell;font-size: x-large;text-align: center;padding-right: 19.5%;vertical-align: middle">
                    भारतीय रेल श्रमिक कल्याण पोर्टल<br>Indian Railway Shramik Kalyan Portal
                </div>
                <div style="display: table-cell;width: 0.05%;vertical-align: middle">
                    <img style="cursor:pointer;width: 78px;vertical-align: middle;padding: 0px;margin: 0px" onclick="window.open('http://cris.org.in')" id="logoCRIS" src="/img/CRISLOGO.png" title="Centre for Railway Information Systems" alt="CRIS"/>                                                   
                </div>
            </div>
            <div id="body">                                  
                <!--<div width="98%" class="w3-card-4 w3-round-large" style="margin: 0.75%;padding: 10px;font-size: large;">
                    <textarea spellcheck="false" id="siteintro" onmousedown="return false" onselect="return false">The Portal is created for monitoring compliance of labour payment and other benefits to the contract workers under the Contract Labour (Regulation & Abolition) Rules. 1971. The in-house developed application will maintain a comprehensive database of all contract workers engaged by different contractors in Indian Railways.&#13;&#10;The Contract Labour (Regulation and Abolition) Act, 1970 provides for regulation of the employment of contract labour and its abolition under certain circumstances.&#13;&#10;It covers every establishment in which 20 or more workmen are employed on any day of the preceding 12 months as ‘contract labour’ and every contractor who employs or who employed on any day of the preceding 12 months, 20 or more contract employee. It does not apply to establishments where the work is of intermittent and casual nature unless work performed is more than 120 days and 60 days in a year respectively (Section 1).  The Act provides for setting up of Central and State Advisory Contract Labour Boards by the Central and State Governments to advise the respective Governments on matters arising out of the administration of the Act (Section 3 & 4).  The establishments covered under the Act are required to be registered as principal employers with the appropriate authorities. Every contractor is required to obtain a licence and not to undertake or execute any work through contract labour, except under and in accordance with the licence issued in that behalf by the licensing officer. The licence granted is subject to conditions relating to hours of work, fixation of wages and other essential amenities in respect of contract as prescribed in the rules (Section 7 & 12).&#13;&#10;The Act has laid down certain amenities to be provided by the contractor to the contract labour for establishment of Canteens and rest rooms; arrangements for sufficient supply of wholesome drinking water, latrines and urinals, washing facilities and first aid facilities have been made obligatory. In case of failure on the part of the contractor to provide these facilities, the Principal Employer is liable to provide the same (Section 16, 17, 18, 19 and 20).</textarea>
                    <div style="display: table-cell;width: 100%">
                    <textarea rows="3" spellcheck="false" id="siteintro" onmousedown="return false" onselect="return false">&#13;&#10;"Obedience to the law of bread labour will bring about a silent revolution in the structure of society..."</textarea>
                    <textarea rows="3" spellcheck="false" id="siteintro" onmousedown="return false" onselect="return false">&#13;&#10;"Labour has its unique place in a cultured human family"</textarea> 
                    </div>
                    <div style="display:table-cell">
                    <img src="/img/mgandhi.jpg" style="height:100px; width:100px;vertical-align: middle">
                    </div>
                </div>
                <div class="w3-card-4 w3-round-large" style="margin-bottom: 2.25%;padding: 0.5%;font-size: 2.5vh;text-align: center;width: 100%">
                    <p>Under Development / Testing</p>
                </div>-->
                <div id="divPicHolder" class="w3-card-4" style="padding: 3%;display: inline-block;background-repeat:no-repeat;background-size: 100% 100%;">                
                <div id="dialog-form" class="w3-card-4 w3-round-large" style="padding:10px;position:relative;background:rgba(255,255,255,1);" >
                    <form autocomplete="off" id="loginForm" method="post">
                        <center><p style="padding:1px;margin:0;font-family: 'Michroma';font-size: 25px;" class="w3-text-indigo">Login</p></center>
                        <input readonly onfocus="this.removeAttribute('readonly');" placeholder="Username" type="text" value="" id="inpUsername" onblur="this.value=this.value.toString().toLowerCase()" autocomplete="off" name="inpUsername" maxlength="10" class="text ui-widget-content ui-corner-all"></input>
                        <input onkeypress="submitFunction(event)" placeholder="Password" type="password" value="" id="inpPwd" name="inpPwd" maxlength="10" class="text ui-widget-content ui-corner-all" autocomplete="off"></input>
                        <input style="display: none" placeholder="Captcha Code" type="text" value="" id="txtInput" onkeypress="submitFunction(event)" class="text ui-widget-content ui-corner-all" autocomplete="off" maxlength="5"/>
                        <input type="text" onmousedown="return false" onselect="return false" size="13" id="mainCaptcha" readonly="readonly" style="display: none;font-size: 20px;font-weight: bold;text-align: center;background-image: url('/img/captcha.jpg')"/>
                        <!--<input src="/img/refresh.png" type="button" id="refresh" onclick="generateCaptcha();" style="display: inline"/><br>-->
                        <input title="Refresh captcha" type="image" height="26px" src="/img/refresh.png" id="refresh" alt="Refresh" onclick="generateCaptcha();" style="display: none;vertical-align: middle">
                        
                        <input class="w3-radio" checked name="chkusertype" onclick="setUserType('a')" type="radio" style="margin-left: 19px"> <p id="pSignInRly" class="w3-text-indigo" style="font-family:arial;display: inline;">Sign-In as Railway Authority</p>
                        <br><input class="w3-radio" name="chkusertype" onclick="setUserType('c')" type="radio" style="margin-left: 19px"> <p class="w3-text-indigo" style="font-family:arial;color: navy;display: inline;">Sign-In as Contractor</p>
                        <br><input class="w3-radio" onclick="setUserType('e')" name="chkusertype" type="radio" style="margin-left: 19px;"> <p class="w3-text-indigo" style="font-family:arial;color: navy;display: inline;">Sign-In as Worker</p>
                        <br><br>
                        <button type="button" class="w3-button w3-indigo w3-hover-indigo w3-card-2 w3-round-large w3-hover-text-white" onclick="checkUserType()">Sign In</button>
                        <button style="margin-left: 0.5%;width: 146px" type="button" class="w3-button w3-indigo w3-hover-indigo w3-card-2 w3-round-large w3-hover-text-white" onclick="window.location='/publicView.jsp'">Public View</button>
                        <button id="btnContRegn" type="button" style="display:block;width: 240px" class="w3-button w3-indigo w3-hover-indigo w3-card-2 w3-round-large w3-hover-text-white" onclick="window.location='/contractor/ContractorRegn.jsp'">Contractor  Registration</button>
                    </form>
                </div>
                </div>
                <!--
                <div class="w3-card-4 w3-round-large" id="right1">
                    <button class="w3-button w3-card-2 w3-round-large w3-hover-light-green w3-hover-text-white" onclick="window.location='/contractor/ContractorRegn.jsp'">Contractor Registration</button><br>
                    <button class="w3-button w3-card-2 w3-round-large w3-hover-light-green w3-hover-text-white" onclick="setUserType('a');">Railway Authority Login</button><br>
                    <button class="w3-button w3-card-2 w3-round-large w3-hover-light-green w3-hover-text-white" onclick="setUserType('c');">Contractor Login</button><br>
                    <button class="w3-button w3-card-2 w3-round-large w3-hover-light-green w3-hover-text-white" onclick="setUserType('e');">Workman Login</button><br>
                    <button class="w3-button w3-card-2 w3-round-large w3-hover-light-green w3-hover-text-white" onclick="window.location='/publicView.jsp'">Public View</button><br>                    
                </div>                
                <div class="w3-card-4 w3-round-large" style="margin-bottom: 8%">                     
                    <img src="/img/homeAdv.jpg" style="vertical-align: middle;cursor: pointer" title="Swachh Rail Abhiyaan" onclick="window.open('http://www.indianrailways.gov.in/Swachh_Bharat.htm')">
                </div>
                <div class="w3-card-4 w3-round-large">                     
                    <img src="/img/oneIndiaoneRly.jpg" style="vertical-align: middle">
                </div>
                -->                
            </div>
            
            <div id="footer">
                <span style="font-size: 11px">© 2018, Ministry of Railways, Govt. of India. All rights reserved.<br>Designed & Developed by <a title="Centre for Railway Information Systems" style="text-decoration: underline;cursor:pointer;color: white;" onclick=window.open("http://www.cris.org.in")>CRIS</a> | <a title="Indian Railway Shramik Kalyan Portal" href="/clip.jsp">Home</a> | <a style="text-decoration: underline;cursor:pointer;color: white;" href="/manual/RAILWAY_MODULE.pdf" download>Railway Authority Help</a> | <a style="text-decoration: underline;cursor:pointer;color: white;" href="/manual/CLIP_CONTRACTOR_MANUAL.pdf" download>Contractor Help</a> | <strong> For Technical Assistance Contact : <font style="color:yellow;font-size:36"> 7044474048, 9433030830 (10 AM to 6 PM - On All Working Days) </font> </strong> </span>
            </div>
        </div> 
        
    </body>
</html>
