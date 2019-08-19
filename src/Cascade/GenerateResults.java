package Cascade;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GenerateResults {
	private Results res;
	private Set<String> mapCities;
	private Map<String,Integer> citySeeds;
	private Map<String,Integer> cityActive;
	private List<String []> bLink;
	private Map<String, double[]> cityCord;
	private int[][] interim;
	public GenerateResults(Results res){
		this.res = res;
		mapCities = new HashSet<String>();
		citySeeds = new HashMap<String,Integer>();
		cityActive = new HashMap<String,Integer>();
		bLink = new ArrayList<String []>();
		cityCord = new HashMap<String,double[]>();
		interim = new int[50][2];
	}
	private void uniqueCities(){
		for(Vertex v: res.getSeeds()){
			if(!mapCities.contains(v.getCity()))
				mapCities.add(v.getCity());
		}	
		for(Vertex v: res.getActivated()){
			if(!mapCities.contains(v.getCity()))
				mapCities.add(v.getCity());
		}
		for(String cityVal : mapCities){
			citySeeds.put(cityVal, 0);
			cityActive.put(cityVal, 0);
		}
		for(Vertex v : res.getSeeds()){
			citySeeds.put(v.getCity(), citySeeds.get(v.getCity())+1);
		}
		for(Vertex v : res.getActivated()){
			cityActive.put(v.getCity(), cityActive.get(v.getCity())+1);
		}
	}
	private void cityLinks(){
		uniqueCities();
		for(Vertex[] row : res.getResult()){
			String city1 = row[0].getCity();
			String city2 = row[1].getCity();
			int flag = 0;
			for(String[] valRet : bLink){
				String cityTemp1 = valRet[0];
				String cityTemp2 = valRet[1];
				if(!city1.equals(city2)){
					if((cityTemp1.equals(city1) && cityTemp2.equals(city2)) || (cityTemp1.equals(city2) && cityTemp2.equals(city1))){
						flag = 1;
						break;
					}
				}
			}
			if(flag == 0){
				bLink.add(new String[] {city1,city2});
			}
		}
	}
	public void genCityJson() throws IOException, JSONException{
		cityLinks();
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\RAVINDER SINGH\\workspace\\FinalProject\\src\\Cascade\\location.txt"));
		FileWriter fw = new FileWriter("C:\\Users\\RAVINDER SINGH\\workspace\\FinalProject\\src\\Cascade\\cityLayer");
		String curLine;
		while((curLine = br.readLine()) != null){
			String[] line = curLine.split(" ");
			double lat = Double.parseDouble(line[2]);
			double lon = Double.parseDouble(line[3]);
			cityCord.put(line[0], new double[] {lat,lon});
		}
		JSONObject cityLayer = new JSONObject();
		JSONArray title = new JSONArray();
		JSONArray link = new JSONArray();
		JSONObject valTemp;
		for(String cityVal : mapCities){
			double[] temp = cityCord.get(cityVal);
			valTemp = new JSONObject();
			valTemp.put("city", cityVal);
			valTemp.put("seeds", citySeeds.get(cityVal));
			valTemp.put("active", cityActive.get(cityVal));
			valTemp.put("lat", temp[0]);
			valTemp.put("log", temp[1]);
			title.put(valTemp);
		}
		cityLayer.put("title", title);
		for(String[] row : bLink){
			double[] temp1 = cityCord.get(row[0]);
			double[] temp2 = cityCord.get(row[1]);
			valTemp = new JSONObject();
			valTemp.put("lat1", temp1[0]);
			valTemp.put("lon1", temp1[1]);
			valTemp.put("lat2", temp2[0]);
			valTemp.put("lon2", temp2[1]);
			link.put(valTemp);
		}
		cityLayer.put("link", link);
		fw.write(cityLayer.toString());
		fw.close();
		br.close();
	}
	private void findHighestSeeds(){
		Set<Vertex> seeds = res.getSeeds();
		int i=0;
		for(Vertex s : seeds){
			interim[i][0] = s.getId();
			interim[i][1] = s.getSeedActive();
			i++;
		}
		for(int j=0;j<(i-1);j++){
			for(int k=0;k<(i-j-1);k++){
				if(interim[k][1] < interim[k+1][1]){
					int tempId = interim[k][0];
					int tempA = interim[k][1];
					interim[k][0] = interim[k+1][0];
					interim[k][1] = interim[k+1][1];
					interim[k+1][0] = tempId;
					interim[k+1][1] = tempA;
				}
			}
		}
	}
	public void genIndividualLayer()throws IOException{
		findHighestSeeds();
		FileWriter fw = new FileWriter("C:\\Users\\RAVINDER SINGH\\workspace\\FinalProject\\src\\Cascade\\individualLayer");
		JSONObject json1 = new JSONObject();
		JSONArray  forwardSeeds = new JSONArray();
		JSONObject value1;
		JSONArray  forwardActive = new JSONArray();
		JSONObject value2;
		JSONArray  forwardRes = new JSONArray();
		JSONObject value3;
		try{
			for(Vertex v : res.getSeeds()){
				value1 = new JSONObject();
				value1.put("id", v.getId());
				value1.put("name", v.getName());
				value1.put("age", v.getAge());
				value1.put("email", v.getEmail());
				value1.put("designation", v.getDesignation());
				value1.put("lat", (float)v.getLatitude());
				value1.put("lon", (float)v.getLongitude());
				forwardSeeds.put(value1);
			}
			json1.put("Seeds",forwardSeeds);
			json1.put("HighestSeed", interim[0][0]);
			int j=0;
			for(Vertex v : res.getActivated()){
				value2 = new JSONObject();
				value2.put("id", v.getId());
				value2.put("name", v.getName());
				value2.put("age", v.getAge());
				value2.put("email", v.getEmail());
				value2.put("designation", v.getDesignation());
				value2.put("lat", (float)v.getLatitude());
				value2.put("lon", (float)v.getLongitude());
				forwardActive.put(value2);
			}
			json1.put("Active",forwardActive);
			for(Vertex[] row : res.getResult()){
				Vertex v1 = row[0];
				Vertex v2 = row[1];
				value3 = new JSONObject();
				value3.put("From", v1.getId());
				value3.put("lat1", (float)v1.getLatitude());
				value3.put("lon1", (float)v1.getLongitude());
				value3.put("To", v2.getId());
				value3.put("lat2", (float)v2.getLatitude());
				value3.put("lon2", (float)v2.getLongitude());
				forwardRes.put(value3);
			}
			json1.put("Result", forwardRes);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		fw.write(json1.toString());
		fw.close();
	}
	public void genSeedsRequired(Map<Double, Integer> result)throws IOException{
		FileWriter fw = new FileWriter("C:\\Users\\RAVINDER SINGH\\workspace\\FinalProject\\src\\Cascade\\seedsRequired");
		JSONObject finalVal = new JSONObject();
		JSONArray cols = new JSONArray();
		JSONObject colsEle;
		JSONArray rows = new JSONArray();
		JSONArray c;
		JSONObject v;
		JSONObject tempJ;
		try{
			colsEle = new JSONObject();
			colsEle.put("id", "percent");
			colsEle.put("label", "Percent");
			colsEle.put("type", "string");
			cols.put(colsEle);
			colsEle = new JSONObject();
			colsEle.put("id", "seed");
			colsEle.put("label", "Seeds");
			colsEle.put("type", "number");
			cols.put(colsEle);
			for(Map.Entry<Double, Integer> entry : result.entrySet()){
				tempJ = new JSONObject();
				c = new JSONArray();
				v = new JSONObject();
				String temp = String.valueOf((int)(entry.getKey()*100));
				temp = temp + "%";
				v.put("v", temp);
				c.put(v);
				v = new JSONObject();
				v.put("v", entry.getValue());
				c.put(v);
				tempJ.put("c", c);
				rows.put(tempJ);
			}
			finalVal.put("cols", cols);
			finalVal.put("rows", rows);
		}catch(JSONException e){
			e.printStackTrace();
		}
		fw.write(finalVal.toString());
		fw.close();
	}
	public void genCityAnalytics()throws IOException{
		FileWriter fw = new FileWriter("C:\\Users\\RAVINDER SINGH\\workspace\\FinalProject\\src\\Cascade\\cityAnalytics");
		JSONObject finalVal = new JSONObject();
		JSONArray cols = new JSONArray();
		JSONObject colsEle;
		JSONArray rows = new JSONArray();
		JSONArray c;
		JSONObject v;
		JSONObject tempJ;
		try{
			colsEle = new JSONObject();
			colsEle.put("id", "city");
			colsEle.put("label", "City");
			colsEle.put("type", "string");
			cols.put(colsEle);
			colsEle = new JSONObject();
			colsEle.put("id", "user");
			colsEle.put("label", "Users");
			colsEle.put("type", "number");
			cols.put(colsEle);
			for(String city : mapCities){
				tempJ = new JSONObject();
				c = new JSONArray();
				v = new JSONObject();
				v.put("v", city);
				c.put(v);
				v = new JSONObject();
				v.put("v", (citySeeds.get(city) + cityActive.get(city)) );
				c.put(v);
				tempJ.put("c", c);
				rows.put(tempJ);
			}
			finalVal.put("cols", cols);
			finalVal.put("rows", rows);
		}catch(JSONException e){
			e.printStackTrace();
		}
		fw.write(finalVal.toString());
		fw.close();
	}
	public void genSeedActive()throws IOException{
		FileWriter fw = new FileWriter("C:\\Users\\RAVINDER SINGH\\workspace\\FinalProject\\src\\Cascade\\seedsActive");
		JSONObject finalVal = new JSONObject();
		JSONArray cols = new JSONArray();
		JSONObject colsEle;
		JSONArray rows = new JSONArray();
		JSONArray c;
		JSONObject v;
		JSONObject tempJ;
		try{
			colsEle = new JSONObject();
			colsEle.put("id", "user");
			colsEle.put("label", "User ID");
			colsEle.put("type", "string");
			cols.put(colsEle);
			colsEle = new JSONObject();
			colsEle.put("id", "active");
			colsEle.put("label", "Activated");
			colsEle.put("type", "number");
			cols.put(colsEle);
			for(int i=0;i<res.getSeeds().size();i++){
				if(interim[i][1] > 0){
					tempJ = new JSONObject();
					c = new JSONArray();
					v = new JSONObject();
					String temp = String.valueOf(interim[i][0]);
					temp = "User " + temp;
					v.put("v", temp);
					c.put(v);
					v = new JSONObject();
					v.put("v", interim[i][1]);
					c.put(v);
					tempJ.put("c", c);
					rows.put(tempJ);
				}
			}
			finalVal.put("cols", cols);
			finalVal.put("rows", rows);
		}catch(JSONException e){
			e.printStackTrace();
		}
		fw.write(finalVal.toString());
		fw.close();
	}
}
