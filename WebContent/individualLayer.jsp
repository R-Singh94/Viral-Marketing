<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %> 
<%@ page import="org.json.JSONObject" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
       $.ajax({
            type: 'POST',
            url: 'http://localhost:8081/FinalProject/IndividualLayer',
            success: function (data) {
               //alert(data); //DatO ANY PROCESS HERE
               var json = JSON.parse(data);
               initialize(json);
               //document.write("<p>" + data.toString() + "</p>");
             },
            
        });
});
</script>
<script src="http://maps.googleapis.com/maps/api/js"></script>
<script type="text/javascript">
var mapProp;
var map;
function initialize(json) {
		var pinColor = "69FE75";
		var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
	    new google.maps.Size(21, 34),
	    new google.maps.Point(0,0),
	    new google.maps.Point(10, 34));
		var pinShadow = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
	    new google.maps.Size(40, 37),
	    new google.maps.Point(0, 0),
	    new google.maps.Point(12, 35));
		mapProp = {
    		center:new google.maps.LatLng(51.5087422012012,-0.1208500120120),
    		zoom:2,
    		mapTypeId:google.maps.MapTypeId.ROADMAP
  		};
  		map=new google.maps.Map(document.getElementById("googleMap"), mapProp);
  		for(var i=0;i<json.Result.length;i++){
  		var pos1 = new google.maps.LatLng(json.Result[i].lat1.toFixed(4),json.Result[i].lon1.toFixed(4));
        var pos2 = new google.maps.LatLng(json.Result[i].lat2.toFixed(4),json.Result[i].lon2.toFixed(4));
   		var flightPath=new google.maps.Polyline({
		  path: [pos1, pos2],
		  strokeColor:"#0000FF",
		  strokeOpacity:0.8,
		  strokeWeight:2
		  });
	  	flightPath.setMap(map);
  		}
  		for(var k=0;k<json.Active.length;k++){
  			var content = '<h3>Active User</h3>' + '<p><b>Name</b> : ' + json.Active[k].name + '</p><p><b>Age</b> : ' + json.Active[k].age + '</p><p><b>Designation</b> : ' + json.Active[k].designation + '</p><p><b>E-mail</b> : ' + json.Active[k].email + '</p>';
  			var activePos = new google.maps.LatLng(json.Active[k].lat.toFixed(4),json.Active[k].lon.toFixed(4));
  			var activeMarker = new google.maps.Marker({
  			  position:activePos
  			 });
  			var infowindow = new google.maps.InfoWindow();
  			activeMarker.setMap(map);
  			google.maps.event.addListener(activeMarker,'click', (function(marker,content,infowindow){ 
  		        return function() {
  		           infowindow.setContent(content);
  		           infowindow.open(map,marker);
  		        };
  		    })(activeMarker,content,infowindow)); 
  		}
  		for(var j=0;j<json.Seeds.length;j++){
  			var content = '<h3>Seed</h3>' + '<p><b>Name</b> : ' + json.Seeds[j].name + '</p><p><b>Age</b> : ' + json.Seeds[j].age + '</p><p><b>Designation</b> : ' + json.Seeds[j].designation + '</p><p><b>E-mail</b> : ' + json.Seeds[j].email + '</p>';
  			var seedPos = new google.maps.LatLng(json.Seeds[j].lat.toFixed(4),json.Seeds[j].lon.toFixed(4));
  			var infowindow = new google.maps.InfoWindow();
  			var seedMarker = new google.maps.Marker({
  			  position:seedPos,
  			  animation: google.maps.Animation.BOUNCE,
  			  icon:pinImage
  		    });
  			seedMarker.setMap(map);
  			google.maps.event.addListener(seedMarker,'click', (function(marker,content,infowindow){ 
  		        return function() {
  		           infowindow.setContent(content);
  		           infowindow.open(map,marker);
  		        };
  		    })(seedMarker,content,infowindow)); 
  		}
 }
//google.maps.event.addDomListener(window, 'load', poll);
</script>
</head>
<body>
<div id="googleMap" style="width:1000px;height:1000px;"></div>
<a href="cityLayer.jsp">City Layer</a>
</body>
</html>