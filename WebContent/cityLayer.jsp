<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
       $.ajax({
            type: 'POST',
            url: 'http://localhost:8081/FinalProject/CityLayer',
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
	mapProp = {
    		center:new google.maps.LatLng(51.5087422012012,-0.1208500120120),
    		zoom:2,
    		mapTypeId:google.maps.MapTypeId.ROADMAP
  	};
	map=new google.maps.Map(document.getElementById("googleMap"), mapProp);
	for(var i=0;i<json.link.length;i++){
		var pos1 = new google.maps.LatLng(json.link[i].lat1.toFixed(4),json.link[i].lon1.toFixed(4));
		var pos2 = new google.maps.LatLng(json.link[i].lat2.toFixed(4),json.link[i].lon2.toFixed(4));
		var flightPath=new google.maps.Polyline({
			  path: [pos1, pos2],
			  strokeColor:"#0000FF",
			  strokeOpacity:0.8,
			  strokeWeight:2
			  });
		flightPath.setMap(map);
	}
	for(var j=0;j<json.title.length;j++){
		var content = '<h3>' + json.title[j].city + '</h3>' + '<p><b>Seeds</b> : ' + json.title[j].seeds + '</p><p><b>Active</b> : ' + json.title[j].active + '</p>';
		var cityPos = new google.maps.LatLng(json.title[j].lat.toFixed(4),json.title[j].log.toFixed(4));
		var activeMarker = new google.maps.Marker({
			  position:cityPos
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
}
</script>
</head>
<body>
<div id="googleMap" style="width:1000px;height:1000px;"></div>
</body>
</html>