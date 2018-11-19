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
<title>Insert title here</title>
<script type="text/javascript" src="/jquery-2.0.0.min.js"></script>
<link rel="stylesheet" href="/jquery-ui-1.12.1.custom/jquery-ui.css"></link>
<link rel="stylesheet" href="/css/loader.css"></link>
<script type="text/javascript" src="/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script type="text/javascript" src="/js/Chart.min.js"></script>
<link rel="stylesheet" href="/css/w3.css"></link>
<script src="/js/d3.js"></script>
<style type="text/css">
a { color:brown; 
    text-decoration: none;
}
a:VISITED {color: brown;}
body {width: 100%;}
td {text-align:center;}

</style>
<script>
    
function getPendingContractorsCount() {
	$.post("/Contractor",{action:"countPending",adminLevel:$('#hidLevel').val()},alertPendingContractors,'text');
}
function alertPendingContractors(count) {
	$('#paraPendingContractors').html($('#paraPendingContractors').html() + "&nbsp;<a title='Contractors' href='contractorApproval.jsp'><strong>"+count+"</strong> Contractor verifications pending</a>");
}

function getPendingWOCount() {
	$.post("/UtilityServlet",{action:"woCountPending",adminLevel:$('#hidLevel').val()},alertPendingWOs,'text');
}
function alertPendingWOs(count) {
	$('#paraPendingWOs').html($('#paraPendingWOs').html() + "&nbsp;<a title='Work Orders' href='woApproval.jsp'><strong>"+count+"</strong> LOA verifications pending</a>");
}
function getPendingFdbkCount() {
	$.post("/UtilityServlet",{action:"newFdbkCount",adminLevel:$('#hidLevel').val()},alertPendingFdbk,'text');
}
function alertPendingFdbk(count) {
    if(parseInt(count)>0) {
	$('#paraPendingFdbk').html($('#paraPendingFdbk').html() + "&nbsp;<a href='/regGrievance.jsp'><strong>"+count+"</strong>Feedback(s) forwarded to you</a>");
        $('#pendFdbk').show();
    }
}

var dataVal = [];
var dataCode = [];
var scaledMin = 1, scaledMax, dataHighest=0, dataLowest=0; 
function scaledD(d) {
    var x = scaledMin + ((d-dataLowest)*(scaledMax - scaledMin)) / (dataHighest-dataLowest);
    return x;
}
function getDivCont() {
	$.post("/UtilityServlet",{action:"divWiseContCount"},drawDivContBarchart,'text');
}
function drawDivContBarchart(entries) {
	var barColors = ["#ffb3ba","#ffdfba","#CACAFF","#baffc9","#bae1ff","#A8E4FF"];
	var dataArray = JSON.parse(entries);           
	var width, scaleFactor, barWidth = Math.floor(window.innerWidth/15), height;
	
        dataHighest = 0;
        dataLowest = 999999999;
	if (dataArray.rows.length > 0) {
		width = dataArray.rows.length * barWidth;
		$.each(dataArray.rows, function(index, data) {
                        
			if(dataHighest<parseInt(data.count))
				dataHighest = parseInt(data.count);
                        if(dataLowest>parseInt(data.count))
				dataLowest = parseInt(data.count);    
			dataVal.push(parseInt(data.count));
			dataCode.push(data.divcode);
		});
	}
        if(dataHighest===dataLowest) {
            if(dataHighest!==1) {
                dataLowest = dataHighest - 1;
                scaledMax = dataHighest/dataLowest;
            }
            else {
                dataLowest = parseFloat(dataHighest) - 0.1;
                scaledMax = parseFloat(dataHighest)/dataLowest;
            }                
        }
        else {
            scaledMax = dataHighest/dataLowest;
        }    
        //alert(dataLowest+" "+dataHighest+" "+scaledMin+" "+scaledMax);
        scaleFactor = (Math.floor(window.innerHeight/2.5))/scaledMax;
	height = scaledMax * scaleFactor;	         
	var graph = d3.select("#svgDivCont")
	.attr("width", width)
	.attr("height", height);
	                  
	var bar = graph.selectAll("g")
	.data(dataVal)
	.enter()
	.append("g")
	.attr("transform", function(d, i) {
		return "translate("+ i * barWidth + "," + (height-(scaledD(d)*scaleFactor)) + ")";
	});

	bar.append("rect")
	.attr("y", height)
        .attr("height", 0)
	.attr("width", barWidth-2)	
	
	.transition()
            //.delay(function(d,i) { return 500*i; })
            .duration(function(d,i) { return 200*(i+1); })
            .attr("y", 0)
            .attr("height", function(d) {
                return scaledD(d) * scaleFactor;                
                } )            
            .style("fill", function(d,i) { return barColors[i%6] } );	
	
	bar.append("text")
		.style("fill", "black")
		.style("font-size", "0.75em")
    	.attr("x", 5)
    	.attr("y", function(d,i){
			return ((scaledD(d) * scaleFactor)-5);
    	})    
    	.text(function(d,i) {
			return dataCode[i];
    	});

    bar.append("title")
    	.text(function(d,i) {
			return d;
		});
    	
}
function function1() {
	$.post("/UtilityServlet",{action:"dashboardTbl1",adminLevel:$('#hidLevel').val()},callback1,'text');
}
function callback1(entries) {
    var dataArray = JSON.parse(entries);
    var table=document.getElementById('tbl1');
        if(dataArray.rows.length>0) {
            var i=0;
            $.each(dataArray.rows, function(index,data) {
                row = table.insertRow(++i);	
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

                cell1.innerHTML=data.wozone;
                cell2.innerHTML=data.wodiv;
                if(data.wodept==null)
                    cell3.innerHTML="";
                else
                    cell3.innerHTML=data.wodept;
                cell4.innerHTML=data.wo;
                cell5.innerHTML=data.wmen;
                cell6.innerHTML=data.wages;
            });
        }	
    function2();
}

function function2() {
	$.post("/UtilityServlet",{action:"dashboardTbl2",adminLevel:$('#hidLevel').val()},callback2,'text');
}
function callback2(entries) {
	var dataArray = JSON.parse(entries);
	var table=document.getElementById('tbl2');
            if(dataArray.rows.length>0) {
                    var i=0;
                    $.each(dataArray.rows, function(index,data) {
                            row = table.insertRow(++i);	
                            var cell1 = row.insertCell(0);			
                            var cell2 = row.insertCell(1);
                            var cell3 = row.insertCell(2);
                            var cell4 = row.insertCell(3);
                            var cell5 = row.insertCell(4);
                            var cell6 = row.insertCell(5);
                            var cell7 = row.insertCell(6);

                            cell1.align="center";
                            cell2.align="center";
                            cell3.align="center";
                            cell4.align="center";
                            cell5.align="center";
                            cell6.align="center";
                            cell7.align="center";

                            cell1.innerHTML=data.zone;
                            cell2.innerHTML=data.div;
                            cell3.innerHTML=data.dept;
                            cell4.innerHTML=data.regd;
                            cell5.innerHTML=data.approved;
                            cell6.innerHTML=data.rejected;
                            cell7.innerHTML=data.pending;
                    });
            }
        var icons = {
                      header: "ui-icon-circle-arrow-e",
                      activeHeader: "ui-icon-circle-arrow-s"
                    };
        $( "#accordion" ).accordion({
            icons:icons,
            heightStyle: "fill"
        });                        
}
var zoneCodes = [];
var zoneCodes2 = [];
var zonewiseWOCount = [];
var zonewiseContractorCount = [];
function getZonalWOCount() {
    $.post("/UtilityServlet",{action:"publicDashboardTbl1"},function3,'text');
}
function function3(entries) {  
        var dataArray = JSON.parse(entries);                    
        if(dataArray.rows.length>0) {
            var j=-1;
                $.each(dataArray.rows, function(index,data) {
                        if(zoneCodes2.includes(data.wozone)) {
                            zonewiseWOCount[j] = parseInt( zonewiseWOCount[j] ) + parseInt( data.wo );
                        }
                        else {
                            ++j;
                            zoneCodes2.push(data.wozone);
                            zonewiseWOCount.push(data.wo);
                        }
                });
        }
        else {
            return;
        }
        config = {
            type: 'pie',
            data: {
                labels: zoneCodes2,
                datasets: [{
                //label: "Population (millions)",
                backgroundColor: ["#3e95cd","#8e5ea2","#3cba9f","#e8c3b9","#c45850",
                                  "#e6194b","#3cb44b","#ffe119","#46f0f0","#f58231",
                                  "#f032e6","#d2f53c","#fabebe","#008080","#e6beff",
                                  "#aa6e28","#800000","#808000","#808080","#ffd8b1",
                                  "#B8860B","#EEE8AA","#C71585","#FF69B4","#BC8F8F",
                                  "#FFDEAD","#FFE4E1","#E6E6FA","#DEB887","#F5DEB3"],
                data: zonewiseWOCount,
              }]
            },
            options: {
              title: {
                display: true
                //text: 'Predicted world population (millions) in 2050'
              },
              responsive: true,
              //maintainAspectRatio: false,
              responsiveAnimationDuration: 1000,
              legend: {
                position: 'bottom'
              },
            }
        };
        var ctx = document.getElementById('pie-chart2').getContext('2d');
        window.myPie = new Chart(ctx, config);
}
function getZonalContCount() {
    $.post("/UtilityServlet",{action:"zonalContractorCount",adminLevel:$('#hidLevel').val()},function4,'text');
}
function function4(entries) {    
    
    var dataArray = JSON.parse(entries);                    
    if(dataArray.rows.length>0) {
        $.each(dataArray.rows, function(index,data) {
            if(data.zoneCount!=0) {
                zoneCodes.push(data.zoneCode);
                zonewiseContractorCount.push(data.zoneCount);
            }
        });
    }    
    else {
        return;
    }
    config = {
        type: 'pie',
        data: {
            labels: zoneCodes,
            datasets: [{
            backgroundColor: ["#3e95cd","#8e5ea2","#3cba9f","#e8c3b9","#c45850",
                              "#e6194b","#3cb44b","#ffe119","#46f0f0","#f58231",
                              "#f032e6","#d2f53c","#fabebe","#008080","#e6beff",
                              "#aa6e28","#800000","#808000","#808080","#ffd8b1",
                              "#B8860B","#EEE8AA","#C71585","#FF69B4","#BC8F8F",
                              "#FFDEAD","#FFE4E1","#E6E6FA","#DEB887","#F5DEB3"],
            data: zonewiseContractorCount,
          }]
        },
        options: {
          title: {
            display: true
            //text: 'Predicted world population (millions) in 2050'
          },
          responsive: true,
          //maintainAspectRatio: false,
          responsiveAnimationDuration: 1000,
          legend: {
            position: 'bottom'
          },
        }
    };
    var ctx = document.getElementById('pie-chart1').getContext('2d');
    window.myPie = new Chart(ctx, config);                
}

$(function() {
        $('#loader').hide();
        document.getElementById('accordion-resizer').style.height = Math.floor(window.innerHeight*0.815) + 'px';
        
        $(document).ajaxStart(function(){
            $("#loader").css("display", "block");
        });
        $(document).ajaxComplete(function(){
            $("#loader").css("display", "none");
        });
	if($('#hidPrivContrAppr').val()=="1") {
		document.getElementById('pendContr').style.display="block";
		getPendingContractorsCount();
	}
	if($('#hidPrivWOApp').val()=="1") {
		document.getElementById('pendWO').style.display="block";
		getPendingWOCount();
	}
        getPendingFdbkCount();
	if($('#hidLevel').val()!=="0") {		
                $('#svgDivCont').show();
                $('#pie-chart1').hide();
                $('#divPie2').hide();
                //$('#divPie1').width("100%");
                document.getElementById("spanBreak").innerHTML="<br><br>";
                getDivCont();
	}
        else {            
            $('#svgDivCont').hide();
            $('#pie-chart1').show();
            $('#divPie2').show();
            //$('#divPie1').width("100%");
            getZonalContCount();
            getZonalWOCount();
        }
        function1();	
	
});
</script>
</head>
<body>
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
<input type="hidden" id="hidPrivContrAppr" value="<%=adto.getPrivContrApprove()%>"></input>
<input type="hidden" id="hidPrivWOApp" value="<%=adto.getPrivWoApprove()%>"></input>
<input type="hidden" id="hidLevel" value="<%=adto.getLevelId()%>"></input>

<div class="w3-panel w3-light-green ui-corner-all" style="margin: 0;padding-bottom: 0.75%">	
<div class='w3-panel w3-animate-right w3-round-large w3-pale-green w3-leftbar w3-border w3-border-green w3-text-black'><h4> <b>Home</b></h4></div>
<div class='w3-animate-right' style="height: 100%;">

<div class="ui-widget" id="divAlertAndPie" style="display: inline-block;width:49%">
	<div class="ui-corner-all ui-state-highlight" id="pendContr" style="display: none;margin-bottom: 2%">
		<p id="paraPendingContractors"><span class="ui-icon ui-icon-info"></span></p>		
	</div>	
	<div class="ui-corner-all ui-state-highlight" id="pendWO" style="display: none;margin-bottom: 2%">
		<p id="paraPendingWOs"><span class="ui-icon ui-icon-info"></span></p>		
	</div>
        <div class="ui-corner-all ui-state-highlight" id="pendFdbk" style="display: none;margin-bottom: 2%">
		<p id="paraPendingFdbk"><span class="ui-icon ui-icon-info"></span></p>		
	</div>
	
    <div id="divPie1" style="position: relative; width: 48vw">
        <fieldset class="ui-corner-all" style="width: 100%; background-color: white; border: none;">
		<legend class="ui-corner-all" style="background-color: green; color: white;padding: 5px">Verified Contractors</legend>
                <span id="spanBreak"></span>
		<svg id='svgDivCont'></svg>
                <canvas id="pie-chart1" height="39%" width="100%"></canvas> 
	</fieldset>
    </div>
    
    <div id="divPie2" style="position: relative; width: 48vw">
        <fieldset class="ui-corner-all" style="width: 100%; background-color: white; border: none;margin-top: 1%">
		<legend class="ui-corner-all" style="background-color: green; color: white;padding: 5px">Verified Work Orders</legend>
                <canvas id="pie-chart2" height="39%" width="100%"></canvas> 
	</fieldset>
    </div>
    <br></br>
</div>
<div id="accordion-resizer" class="ui-widget-content w3-light-green" style="display: inline-block;width: 49%;vertical-align: top;border: none;float:right">
    <div id="accordion" class="ui-widget-content w3-light-green" style="border:none">
	<h3>Work Orders & Wages</h3>
	<div>
	<table id="tbl1" class="w3-table-all w3-card-4 w3-border w3-border-green" width="100%">
            <thead>			
                <tr class="w3-pale-green">
                    <th>Zone</th>
                    <th>Division</th>
                    <th>Department</th>
                    <th>Work Orders</th>
                    <th>Workmen</th>
                    <th>Gross Wages</th>
                </tr>	
            </thead>
	</table>
	</div>
	<h3>Contractors</h3>
	<div>
	<table id="tbl2" class="w3-table-all w3-card-4 w3-border w3-border-green" width="100%">
            <thead>				
                <tr class="w3-pale-green">
                    <th>Zone</th>
                    <th>Division</th>
                    <th>Department</th>
                    <th>Registered</th>
                    <th>Verified</th>
                    <th>Rejected</th>
                    <th>Pending</th>
                </tr>		
            </thead>
	</table>
	</div>
</div>
</div>
</div>
</div>
</body>
</html>