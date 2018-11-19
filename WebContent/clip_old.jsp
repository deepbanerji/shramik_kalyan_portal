<%-- 
    Document   : clip
    Created on : 20 Feb, 2018, 2:27:41 PM
    Author     : Deep Banerji
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>IR Shramik Kalyan Portal</title>
        <script type="text/javascript" src="/jquery-2.0.0.min.js"></script>  
        <script type="text/javascript" src="/js/captcha.js"></script>
        <link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
        <script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
        <script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
        
        <link rel="stylesheet" href="/css/w3.css">
        <link rel="icon" href="/img/irlogo1.png">
        <script>
            history.forward();
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
            var userType="";
            function setUserType(type) {
                userType = type;
                dialog.dialog( "open" );
            }
          /*  function checkUserType() {
            	  $("#loginForm").attr("action","/Login?action=login&type="+userType);
                  $("#loginForm").submit();  
            } */
            function checkUserType() {
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
            $(function(){
                
                generateCaptcha();
                //$('#header').height( Math.floor( window.innerHeight * 0.087 ) +"px");
                //$('#header').css('line-height', Math.floor( window.innerHeight * 0.087 ) +"px");
                
                $('#divPicHolder').height( Math.floor( window.innerHeight * 0.35 ) +"px");
                $('#divPicHolder').width( Math.floor( window.innerWidth * 0.368 ) +"px");
                
                $('#divPicHolder2').height( Math.floor( window.innerHeight * 0.35 ) +"px");
                $('#divPicHolder2').width( Math.floor( window.innerWidth * 0.368 ) +"px");
                
                $('#left').width( Math.floor( window.innerWidth * 0.77 ) +"px");
                var slideIndex = 0;
                carousel();
                function carousel() {
                    var i;
                    var x = document.getElementsByClassName("mySlides");
                    for (i = 0; i < x.length; i++) {
                      x[i].style.display = "none"; 
                    }
                    slideIndex++;
                    if (slideIndex > x.length) {
                        slideIndex = 1;
                    } 
                    x[slideIndex-1].style.display = "block"; 
                    setTimeout(carousel, 2000); // Change image every 2 seconds
                }
                carousel2();
                function carousel2() {
                    var i;
                    var x = document.getElementsByClassName("mySlides2");
                    for (i = 0; i < x.length; i++) {
                      x[i].style.display = "none"; 
                    }
                    slideIndex++;
                    if (slideIndex > x.length) {
                        slideIndex = 1;
                    } 
                    x[slideIndex-1].style.display = "block"; 
                    setTimeout(carousel2, 2000); // Change image every 2 seconds
                }
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
                dialog = $( "#dialog-form" ).dialog({
                  autoOpen: false,
                  height: 375,
                  width: 360,
                  modal: true,
                  buttons: {
                    "Sign In": checkUserType,
                    Cancel: function() {
                      dialog.dialog( "close" );
                    }
                  }
                });
                //$('#inpUsername').focus();
                chk_browser_version();
            });
            
        </script>
        <style> 
            label, input { display:block;}
            #txtInput {-moz-user-select: none; 
                       -webkit-user-select: none; 
                       -ms-user-select:none; 
                       -o-user-select:none;                       
            }
            input.text { margin-bottom:12px; width:95%; padding: .4em; }
            fieldset { padding:0; border:0; margin-top:25px; }
            
            body,html {height: 100%;width: 100%;margin:0;padding:0;}
            #container {width: 100%;height: 100%;position: relative}
            #header {background-color: darkgreen;color: white;width: 100%;}   
            
            #body {padding: 10px;position: relative;display: inline-block;white-space: nowrap;margin-top: 0.75%}
            
            #left {display: inline-block;background-color: white;white-space: normal;vertical-align: top;margin-left: 0.75%;}
            #right {width: 20%;text-align: center;display: inline-block;margin-left: 1.25%;margin-right: 0.75%;white-space: normal;float: top}
            #right1 {text-align: center;padding-top: 4%;padding-bottom: 4%;margin-bottom: 8%}
            
            #footer {background-color: darkgreen;position: fixed;right: 0;bottom: 0;left: 0;color: white;padding: 5px;margin: 0;text-align: center;line-height: 15px}
            .w3-card-4 img {display:block;margin:auto;width: 100%;height: 100%;animation-duration: 0.5s}
            #right button {margin: 1%;padding: 8px;width: 90%;}
        </style>
    </head>
    <body>
        
        <div id="wrongCaptcha" title="Error"><p>Wrong Captcha Code! Please try again.</p></div>
        <div id="blankCaptcha" title="Error"><p>Please enter Captcha Code!</p></div>
        <div id="container">
            <div id="header">             
                
                <img style="cursor: pointer;vertical-align: middle;margin: 0.5%;width: 3.25%;" onclick="window.open('http://indianrailways.gov.in')" id="logoRailway" src="/img/IRLOGO.png" title="Indian Railways" alt="Indian Railways"/>
                <img style="vertical-align: middle;margin: 0.5% 0.25%;width: 6.25%" id="logoSwachBharat" src="/img/SWACHHBHARATLOGO.png" title="Swachh Bharat" alt="Swachh Bharat"/>
                <span style="margin-left: 22.5%;vertical-align: middle;font-size: 1.35vw;">Contract Labour Payment Management Portal</span>
                <img style="cursor:pointer;float: right;margin: 0.5%;width: 4.25%;vertical-align: middle" onclick="window.open('http://cris.org.in')" id="logoCRIS" src="/img/CRISLOGO.png" title="Centre for Railway Information Systems" alt="CRIS"/>
                                                   
            </div>
            <div id="body">    
                <div id="left">
                    <!-- 
                    id="left" width: 76.859%
                    <div class="w3-card-4 w3-round-large" style="margin-bottom: 2.25%;padding: 10px;font-size: 1.5vh;text-align: justify">
                        The Portal is created for monitoring compliance of labour payment and other benefits to the contract workers under the Contract Labour 
                        (Regulation & Abolition) Rules. 1971. The in-house developed application will maintain a comprehensive database of all contract workers engaged by different 
                        contractors in Indian Railways.
                        <br><br>The Contract Labour (Regulation and Abolition) Act, 1970 provides for regulation of the employment of contract labour and its abolition under certain circumstances

<br><br>It covers every establishment in which 20 or more workmen are employed on any day of the preceding 12 months as ‘contract labour’ and every contractor who employs or who employed on any day of the preceding 12 months, 20 or more contract employee. It does not apply to establishments where the work is of intermittent and casual nature unless work performed is more than 120 days and 60 days in a year respectively (Section 1).  The Act provides for setting up of Central and State Advisory Contract Labour Boards by the Central and State Governments to advise the respective Governments on matters arising out of the administration of the Act (Section 3 & 4).  The establishments covered under the Act are required to be registered as principal employers with the appropriate authorities. Every contractor is required to obtain a licence and not to undertake or execute any work through contract labour, except under and in accordance with the licence issued in that behalf by the licensing officer. The licence granted is subject to conditions relating to hours of work, fixation of wages and other essential amenities in respect of contract as prescribed in the rules (Section 7 & 12). 

 
<br><br>The Act has laid down certain amenities to be provided by the contractor to the contract labour for establishment of Canteens and rest rooms; arrangements for sufficient supply of wholesome drinking water, latrines and urinals, washing facilities and first aid facilities have been made obligatory. In case of failure on the part of the contractor to provide these facilities, the Principal Employer is liable to provide the same (Section 16, 17, 18, 19 and 20).

                    </div>
                    -->
                    
                    <div class="w3-card-4 w3-round-large" style="margin-bottom: 2.25%;padding: 0.5%;font-size: 2.5vh;text-align: center;width: 100%">
                        <p>Under Development / Testing</p>
                    </div>
                    
                    <div id="divPicHolder" class="w3-card-4" style="padding: 0;display: inline-block">
                        <img class="mySlides w3-animate-right w3-image" src="/img/pic1.jpg">
                        <img class="mySlides w3-animate-right w3-image" src="/img/pic2.jpg">
                        <img class="mySlides w3-animate-right w3-image" src="/img/pic3.jpg">
                        <img class="mySlides w3-animate-right w3-image" src="/img/pic4.jpg">
                        <img class="mySlides w3-animate-right w3-image" src="/img/pic5.jpg">
                        <img class="mySlides w3-animate-right w3-image" src="/img/pic6.jpg">
                        <img class="mySlides w3-animate-right w3-image" src="/img/pic7.jpg">
                        <img class="mySlides w3-animate-right w3-image" src="/img/pic8.jpg">
                        <img class="mySlides w3-animate-right w3-image" src="/img/pic9.jpg">
                    </div>
                    <div id="divPicHolder2" class="w3-card-4" style="padding: 0;display: inline-block;float: right">                        
                        <img class="mySlides2 w3-animate-right w3-image" src="/img/pic2.jpg">
                        <img class="mySlides2 w3-animate-right w3-image" src="/img/pic3.jpg">
                        <img class="mySlides2 w3-animate-right w3-image" src="/img/pic4.jpg">
                        <img class="mySlides2 w3-animate-right w3-image" src="/img/pic5.jpg">
                        <img class="mySlides2 w3-animate-right w3-image" src="/img/pic6.jpg">
                        <img class="mySlides2 w3-animate-right w3-image" src="/img/pic7.jpg">
                        <img class="mySlides2 w3-animate-right w3-image" src="/img/pic8.jpg">
                        <img class="mySlides2 w3-animate-right w3-image" src="/img/pic9.jpg">
                        <img class="mySlides2 w3-animate-right w3-image" src="/img/pic1.jpg">
                    </div>
                    <br><br>
                </div>
                <div id="right" >
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
                    <!--<div class="w3-card-4 w3-round-large">                     
                        <img src="/img/oneIndiaoneRly.jpg" style="vertical-align: middle">
                    </div>
                    -->
                    <br><br>
                </div>
                
            </div>
            
            <div id="footer"><span style="font-size: 11px">© 2018, Ministry of Railways, Govt. of India. All rights reserved.<br>Designed & Developed by <a title="Centre for Railway Information Systems" style="text-decoration: underline;cursor:pointer;color: white;" onclick=window.open("http://www.cris.org.in")>CRIS</a> | <a title="Indian Railway Shramik Kalyan Portal" href="/clip.jsp">Home</a> | <a style="text-decoration: underline;cursor:pointer;color: white;" href="/manual/RAILWAY_MODULE.pdf" download>Railway Authority Help</a> | <a style="text-decoration: underline;cursor:pointer;color: white;" href="/manual/CLIP_CONTRACTOR_MANUAL.pdf" download>Contractor Help</a> | <strong> For Technical Assistance Contact : <font style="color:yellow;font-size:36"> (033)22425909 (10AM-6PM) </font> </strong> </span></div>
        </div> 
        <div id="dialog-form" title="Login - IR Shramik Kalyan Portal">
            <form id="loginForm" method="post">
                <fieldset>
                <label for="inpUsername">Username </label>
                <input id="inpUsername" onblur="this.value=this.value.toString().toLowerCase()" autocomplete="off" name="inpUsername" maxlength="10" class="text ui-widget-content ui-corner-all"></input>
                <label for="inpPwd">Password </label>
                <input type="password" id="inpPwd" name="inpPwd" maxlength="10" value="xxxxxxx" class="text ui-widget-content ui-corner-all"></input>
                <label for="inpPwd">Captcha Code </label>
                <input type="text" onkeypress="submitFunction(event)" class="text ui-widget-content ui-corner-all" id="txtInput" autocomplete="off" maxlength="5"/>
                <input type="text" onmousedown="return false" onselect="return false" size="13" id="mainCaptcha" readonly="readonly" style="display: inline;font-size: 20px;font-weight: bold;text-align: center;background-image: url('/img/captcha.jpg')"/>
                <input type="button" id="refresh" value="Refresh" onclick="generateCaptcha();" style="display: inline"/>                    
                </fieldset>
            </form>
        </div>
    </body>
</html>
