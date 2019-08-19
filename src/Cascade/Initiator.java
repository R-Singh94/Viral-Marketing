package Cascade;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Initiator {
	Results res;
	BufferedReader br1;
	BufferedReader br2;
	Graph g;
	Implement imp;
	Input inp;
	public Initiator(){
		res = new Results();
		g = new Graph();
		imp = new Implement();
	}
	public void generateResult(Input input){
		try{
			Mongo mongo = new Mongo();
			DB db = mongo.getDB("ViralMarket");
			DBCollection collection = db.getCollection("userInfo");
			inp = input;
			String curLine;
			imp.findUsers(inp, res, collection);
			Set<Vertex> list = res.getVertices();
			for(Vertex v : list){
				v.setThreshold();
				v.setRoundActivated(3);
				g.addVetex(v);
			}
			br2 = new BufferedReader(new FileReader("C:\\Users\\RAVINDER SINGH\\workspace\\FinalProject\\src\\Cascade\\Edges4"));
			int edges = 0;
			while((curLine = br2.readLine()) != null){
				String[] line = curLine.split(" ");
				if(g.containsVertex(Integer.parseInt(line[0])) && g.containsVertex(Integer.parseInt(line[1]))){
					Vertex v1 = g.getVertex(Integer.parseInt(line[0]));
					v1.setNeighbours(g.getVertex(Integer.parseInt(line[1])));
					Vertex v2 = g.getVertex(Integer.parseInt(line[1]));
					v2.setNeighbours(g.getVertex(Integer.parseInt(line[0])));
					edges++;
				}
			}
			g.setNumEdges(edges);
			Set<Vertex> temp = g.getVertices();
			for(Vertex vTemp : temp){
				vTemp.setDegree();
			}
			imp.VirAds2(g, 3, res, 0.75);
			imp.threshold2(g, res, 1, 3);
			GenerateResults gen = new GenerateResults(res);
			gen.genCityJson();
			gen.genIndividualLayer();
			Map<Double,Integer> seedsReq = new HashMap<Double,Integer>();
			Results resTemp;
			Implement imp2;
			for(double i=0.5;i<=0.9;i+=0.1){
				resTemp = new Results();
				imp2 = new Implement();
				for(Vertex vTemp : temp){
					vTemp.setDegree();
				}
				if(inp.getCountry() != null)
					imp.VirAds(g, 3, resTemp);
				else
					imp2.VirAds2(g, 3, resTemp, i);
				seedsReq.put(i, resTemp.getSeeds().size());
			}
			gen.genSeedsRequired(seedsReq);
			gen.genCityAnalytics();
			gen.genSeedActive();
			System.out.println("Done");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
