    function load_result(n){
    	var source;
    	switch(n){
    		case 1:{
    		source="http://localhost:8081/FinalProject/seedCoverage.jsp";
    		break;
    		}
    		case 2:{
        		source="http://localhost:8081/FinalProject/seedsActive.jsp";
        		break;
        		}
    		case 3:{
    			source="http://localhost:8081/FinalProject/cityPerc.jsp";
        		break;
        		}
    		case 4:{
    			source="http://localhost:8081/FinalProject/cityLayer.jsp";
        		break;
        		}
    		default:{
    			source="http://localhost:8081/FinalProject/individualLayer.jsp";
        		break;
        		}
    		}
    		document.getElementById('myIframe').src = source;
}