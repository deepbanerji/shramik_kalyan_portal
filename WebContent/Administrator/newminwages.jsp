<%-- 
    Document   : newminwages
    Created on : 16 May, 2018, 3:19:32 PM
    Author     : Deep Banerji
--%>

<%@page import="org.cris.clip.dto.AdministratorDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>New minimum wages</title>
        <script type="text/javascript" src="/jquery-2.0.0.min.js"></script>
        <link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css">
        <script type="text/javascript" src="/js/common-ajax-calls.js"></script>
        <script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
        <script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
        <link rel="stylesheet" href="/css/w3.css">
        <link rel="stylesheet" href="/css/loader.css">
        <style>
            body {width: 100%;}
            input[type=text] {
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
            input[type=text]:focus {
                    border-color: #339933;
            }
            .button {
                background-color: white;
                color: black;
                border: 2px solid #4CAF50;
                padding: 2px 6px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: small;
                margin-left:20px;
                }
            .button:hover {
                background-color: #4CAF50;
                color: white;
                cursor: pointer;
            }

        </style>
        <script>
            var ids = [];
            function editWage(temp) {
                var newwage = prompt("Please enter new wage");
                if (newwage != null) {
                    if(newwage>=99 && newwage<=1000) {
                        $.post("/UtilityServlet",{action:"editWage",period:$('#hPeriod').val(),id:temp,newval:newwage},function(data){
                            if(data==1) {
                                alert("Wage revision successful!");
                                location = "/Administrator/newminwages.jsp";
                            }
                            else
                                alert('Operation Failed!');
                        },'text');
                    }
                    else {
                        alert('Invalid input!');
                    }
                }
            }
            function validate() {
                for(var i=0;i<ids.length;++i) {
                    if( document.getElementById( ids[i]).value=="" ) {
                        alert("Please fill all wage boxes");
                        return;
                    }
                }
                document.getElementById("form").submit();
            }
            function validateWage(id) {
                if($('#'+id).val().trim()=="") {
                    return;
                }
                if ( !/^[0-9]{3,4}$/.test($('#'+id).val()) ) {
                        alert('Enter valid minimum wage');
                        $('#'+id).val("");
                        return;
                }
                if( $('#'+id).val()<100 || $('#'+id).val()>1000 ) {
                    alert('Enter valid minimum wage');
                    $('#'+id).val("");
                }
            }
            function displayWagesInputTable() {
                $('#tblWageBody').empty();
                document.getElementById("hPeriod").value = document.getElementById("validityPeriod").value;
                $.post("/UtilityServlet",{action:"getWageRows",period:$('#validityPeriod').val()},fillWagesInputTable,'text');
            }
            function fillWagesInputTable(entries) {
                var dataArray = JSON.parse(entries);
                var tableBody = document.getElementById('tblWageBody');
		if(dataArray.rows.length>0) {
                    document.getElementById("btnSubmit").style.display = "";
                    document.getElementById("hRowCount").value = dataArray.rows.length;
                    var i=-1;
                    ids = [];
                    $.each(dataArray.rows, function(index,data) {
                        var row = tableBody.insertRow(++i);				
                        
                        var cell1 = row.insertCell(0);			
                        var cell2 = row.insertCell(1);
                        var cell3 = row.insertCell(2);
                        var cell4 = row.insertCell(3);
                        var cell5 = row.insertCell(4);
                        var cell6 = row.insertCell(5);

                        cell1.align="center";
                        cell2.align="center";
                        cell3.align="center";
                        cell4.align="center";
                        cell5.align="center";
                        cell6.align="center";
                        
                        cell1.innerHTML = data.woarea;
                        cell2.innerHTML = data.employment_type;
                        cell3.innerHTML = data.skill_type;
                        var temp = data.woarea + "." + data.employment_type_id + "." + data.skill_type_id;
                        
                        if(data.minwage==null) {
                            cell4.innerHTML = "<input type='text' maxlength='4' size='8' id='w"+index+"' name='"+temp+"' onblur='validateWage(this.id)'></input><input type='hidden' name='hw"+index+"' value='"+temp+"'></input>";
                            ids.push("w"+index);
                            cell5.innerHTML = document.getElementById("hPeriod").value.split(" to ")[0];
                            cell6.innerHTML = document.getElementById("hPeriod").value.split(" to ")[1];
                        }
                        else {
                            if( document.getElementById("hidUserDesigId").value=="0" )
                                cell4.innerHTML = data.minwage + "&nbsp;<a class='button'>Revise</a>";
                            else
                                cell4.innerHTML = data.minwage + "&nbsp;<a class='button' onclick=editWage('"+temp+"')>Revise</a>";
                            document.getElementById("btnSubmit").style.display = "none";
                            cell5.innerHTML = data.validfrom;
                            cell6.innerHTML = data.validto;
                        }                        
                    });
                }
            }
            $(function() {
                $('#loader').hide();
                $(document).ajaxStart(function(){
                    $("#loader").css("display", "block");
                });
                $(document).ajaxComplete(function(){
                    $("#loader").css("display", "none");
                });
                if( document.getElementById("hidUserDesigId").value=="0" ) {
                    document.getElementById("btnSubmit").disabled = true;
                }    
            });
        </script>
    </head>
    <body>
        <c:if test="${pageContext.session['new']}">
            <c:redirect url="/clip.jsp"/> 
        </c:if>
        <div id="loader"></div>
        <%  AdministratorDTO adto = (AdministratorDTO)(request.getSession(false)).getAttribute("administrator");
            if(adto==null) {
                response.sendRedirect("/clip.jsp");
                return;
            }		
        %>
        <input type="hidden" id="hidUserDesigId" value="<%=adto.getDesigId()%>">
        <div class="w3-panel w3-light-green ui-corner-all" style="margin: 0">	
        <div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Minimum Wages</b></h4></div>
        <div class='w3-animate-right' style="height: 100%;">
        <hr></hr>
        <form id="form" action="/UtilityServlet?action=savenewwage" method="post">
        <label for="validityPeriod">Period&nbsp;</label><select id="validityPeriod" style="padding: 2px">            
            <option value="01-OCT-15 to 31-MAR-16">01-OCT-15 to 31-MAR-16</option>
            <option value="01-APR-16 to 30-SEP-16">01-APR-16 to 30-SEP-16</option>
            <option value="01-OCT-16 to 31-MAR-17">01-OCT-16 to 31-MAR-17</option>            
            <option value="01-APR-17 to 30-SEP-17">01-APR-17 to 30-SEP-17</option>
            
            <option value="01-OCT-17 to 31-MAR-18">01-OCT-17 to 31-MAR-18</option>
            <option value="01-APR-18 to 30-SEP-18">01-APR-18 to 30-SEP-18</option>
            <option value="01-OCT-18 to 31-MAR-19">01-OCT-18 to 31-MAR-19</option>            
            <option value="01-APR-19 to 30-SEP-19">01-APR-19 to 30-SEP-19</option>
            <option value="01-OCT-19 to 31-MAR-20">01-OCT-19 to 31-MAR-20</option>
            <option value="01-APR-20 to 30-SEP-20">01-APR-20 to 30-SEP-20</option>
        </select>
        <input type="button" onclick="displayWagesInputTable();" value="Show" class='w3-btn w3-round-large w3-card-4 w3-pale-green w3-hover-green'>  
        <br><br>
        <input type="hidden" id="hRowCount" name="hRowCount">
        <input type="hidden" id="hPeriod" name="hPeriod">
        <table id="tblWage" class="w3-table-all w3-card-4 w3-border w3-border-green" style="font-size: small;width:100%; word-wrap:break-word;">
            <thead>
                <tr class="w3-pale-green">
                    <th>WO Area</th>
                    <th>Employment Type</th>
                    <th>Skill Type</th>
                    <th>Minimum Wage</th>                    
                    <th>Valid From</th>
                    <th>Valid Upto</th>
                </tr>
            </thead>
            <tbody id="tblWageBody"></tbody>
        </table>
        <br>
        <center><input type="button" value="Submit" id="btnSubmit" onclick="validate();" style="display:none" class='w3-btn w3-round-large w3-card-4 w3-pale-green w3-hover-green'></center>
        <br>
        </form>
        </div>
        </div>
        <br><br>
    </body>
</html>
