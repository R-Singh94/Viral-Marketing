package Cascade;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.json.JSONObject;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class Implement {
	private List<Vertex[]> result;
	private Queue<Vertex> active;
	private Set<Vertex> activated;
	private Set<Vertex> inBound;
	private Set<Vertex> infuenced;
	private Set<Vertex> seed;
	private Queue<Vertex> q;
	private Map<Vertex, Integer> qMap;
	private Random rand;
	public Implement(){
		result = new ArrayList<Vertex[]>();
		active = new LinkedList<Vertex>();
		activated = new HashSet<Vertex>();
		infuenced = new HashSet<Vertex>();
		seed = new HashSet<Vertex>();
		q = new LinkedList<Vertex>();
		qMap = new HashMap<Vertex, Integer>();
		rand = new Random();
	}
	public void threshold2(Graph graph, Results res,int steps,int d){
		double ratio;
		Set<Vertex> seeds = res.getSeeds();
		for(Vertex v : seeds){
			v.setSeedActive();
			active.add(v);
			activated.add(v);
			v.setRoundActivated(0);
			while(!active.isEmpty()){
				Vertex q = active.remove();
				q.setActivated();
				for(Vertex w : q.getNeighbours()){
					for(int i=q.getRoundActivated()+1;i<d;i++){
						if(!active.contains(w) && !activated.contains(w) && !seeds.contains(w)){
							int degree = w.getDegree();
							int count = 0;
							for(Vertex nei : w.getNeighbours()){
								if(activated.contains(nei))
									count++;
							}
							ratio = (double)count/degree;
							if(ratio >= w.getThreshold()-1){
								v.incSeedActive();
								w.setRoundActivated(i);
								activated.add(w);
								active.add(w);
								result.add(new Vertex[] {q,w});
							}
						}
					}
				}
			}
		}
		res.setResult(result);
		res.setActivated(activated);
	}
	public void VirAds2(Graph g,int d,Results res, double coverage){
		int max;
		Set<Vertex> temp = g.getVertices();
		int totalSt = temp.size();
		Vertex u;
		while(infuenced.size()<=(int)(coverage*(totalSt-1))){
			max=0;
			u = null;
			for(Vertex v: temp){
				if(max < v.getActiveEdges()+v.getEdgesRequired()){
					u = v;
					max = v.getActiveEdges()+v.getEdgesRequired();
				}
				v.computeActiveEdges(seed);
			}
			if(u!=null){
				System.out.println(infuenced.size());
				temp.remove(u);
				seed.add(u);
				infuenced.add(u);
				q.add(u);
				u.setRoundActivated(0);
				for(Vertex nei: u.getNeighbours()){
					nei.computeActiveEdges();
				}
				while(!q.isEmpty()){
					Vertex t = q.remove();
					t.setActivated();
					for(Vertex w : t.getNeighbours()){
						for(int i=t.getRoundActivated()+1;i<d;i++){
							if(!q.contains(w)){
								if(t.getTotalActivated() > w.getEdgesRequired()-1){
									t.incActivated();
									infuenced.add(w);
									w.setRoundActivated(i);
									q.add(w);
									for(Vertex x: w.getNeighbours()){
										if(!seed.contains(x))
											x.computeActiveEdges();
									}
								}
							}
						}
					}
				}
			}
		}
		res.setSeeds(seed);
	}
	public void VirAds(Graph g,int d,Results res){
		int max;
		Set<Vertex> temp = g.getVertices();
		int totalSt = temp.size();
		System.out.println(totalSt);
		Vertex u;
		int activated = 0;
		while(infuenced.size()<=(int)(0.7*(totalSt-1))){
			max=0;
			u = null;
			System.out.print(infuenced.size());
			for(Vertex v: temp){
					if(max < v.getActiveEdges()+v.getEdgesRequired()){
						u = v;
						max = v.getActiveEdges()+v.getEdgesRequired();
					}
					v.computeActiveEdges(seed);
			}
			if(u!=null){
				System.out.println(" " + u.getId());
			temp.remove(u);
			seed.add(u);
			infuenced.add(u);
			q.add(u);
			qMap.put(u, u.getRoundActivated());
			u.setRoundActivated(0);
			for(Vertex nei: u.getNeighbours()){
				nei.computeActiveEdges();
			}
			while(!q.isEmpty()){
				Vertex t = q.remove();
				int rt = qMap.get(t);
				qMap.remove(t);
				for(Vertex w : t.getNeighbours()){
					for(int i=t.getRoundActivated()+1;i<d;i++)
					{
						if(!q.contains(w)){
							w.setActivatedNeighbours(i);
						if(w.getTotalActivated(i)>w.getEdgesRequired()){
							infuenced.add(w);
							if(w.getRoundActivated()>=d && i+1<=d){
								for(Vertex x: w.getNeighbours()){
									if(!seed.contains(x))
										x.computeActiveEdges();
								}
							}
							w.setRoundActivated(i);
							if(w.getRoundActivated()<d && !q.contains(w)){
								q.add(w);
								qMap.put(w, w.getRoundActivated());
							}
						}
					}
					}
				}
			}
		}
	}
		System.out.println(infuenced.size());
		res.setSeeds(seed);
	}
	private DBCursor chech(String city,String country,int age1,int age2,DBCollection collection){
		BasicDBObject query = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		DBCursor cursor = null;
		if(city!=null && age1>0 && age2 >0)
		{
			BasicDBObject ageReq = new BasicDBObject();
			ageReq.put("age", new BasicDBObject("$gt", age1).append("$lt", age2));
			obj.add(new BasicDBObject("city", city));
			obj.add(ageReq);
			query.put("$and", obj);
			cursor = collection.find(query);
		}
		else if(country!=null && age1>0 && age2 >0){
			BasicDBObject ageReq = new BasicDBObject();
			ageReq.put("age", new BasicDBObject("$gt", age1).append("$lt", age2));
			obj.add(new BasicDBObject("country", country));
			obj.add(ageReq);
			query.put("$and", obj);
			cursor = collection.find(query);
		}
		else if(city != null){
			query.put("city", city);
			cursor = collection.find(query);
		}
		else if(country != null){
			query.put("country", country);
			cursor = collection.find(query);
		}
		else if(age1>0 && age2>0){
			query.put("age", new BasicDBObject("$gt", age1).append("$lt", age2));
			cursor = collection.find(query);
		}
		else if(age1>0){
			query.put("age", new BasicDBObject("$gt", age1));
			cursor = collection.find(query);
		}
		else if(age2>0){
			query.put("age", new BasicDBObject("$lt", age2));
			cursor = collection.find(query);
		}
		else{
			cursor = collection.find();
		}
		return cursor;
	}
	private double computeX(Map<String,Integer> brandMap, Map<String,Integer> personalityMap, Map<String,Integer> categoryMap,double factor){
		double x;
		int sum = 0;
		for(int sc : brandMap.values())
			sum += sc;
		for(int sc : personalityMap.values())
			sum += sc;
		for(int sc : personalityMap.values())
			sum += sc;
		x = factor/(double)sum;
		return x;
	}
	public void findUsers(Input ip,Results res,DBCollection collection){
		double temp;
		Set<Vertex> list = new HashSet<Vertex>();
		DBCursor cursor = chech(ip.getCity(),ip.getCountry(),ip.getAge1(),ip.getAge2(),collection);
		double x;
		if(ip.getDesig().contains("null"))
			x = computeX(ip.getBrandMap(),ip.getPersonalityMap(),ip.getCategoryMap(),0.3);
		else
			x = computeX(ip.getBrandMap(),ip.getPersonalityMap(),ip.getCategoryMap(),0.2);
		try{
			if(cursor != null){
				while (cursor.hasNext()) {
					BasicDBObject obj = (BasicDBObject) cursor.next();
					Vertex v = new Vertex(obj.getInt("userid"));
					v.setAge(obj.getInt("age"));
					v.setDesignation(obj.getString("designation"));
					v.setEmail(obj.getString("email"));
					v.setName(obj.getString("name"));
					v.setCity(obj.getString("city"));
					v.setLatitude((float)obj.getDouble("latitude"));
					v.setLongitude((float)obj.getDouble("longitude"));
					v.setInfluence(0.7);
					list.add(v);
				}
				Set<String> keys = new HashSet<String>();
				Set<String> categoryKeys = new HashSet<String>();
				for(Vertex v:list){
					if(!ip.getDesig().contains("null")){
						if(ip.getDesig().contains(v.getDesignation())){
							v.setInfluence(0.1);
						}
					}
					keys.addAll(ip.getBrandMap().keySet());
					keys.addAll(ip.getPersonalityMap().keySet());
					categoryKeys.addAll(ip.getCategoryMap().keySet());
					BasicDBObject query = new BasicDBObject("userid", v.getId());
					DBCursor cursor2 = collection.find(query);
					BasicDBList userVal = (BasicDBList) cursor2.next().get("pages");
					for(int i=0;i<13;i++){
						JSONObject json = new JSONObject(userVal.get(i).toString());
						String name = json.get("name").toString().toLowerCase();
						Set<String> brandTemp = new HashSet<String>();
						brandTemp.addAll(ip.getBrandMap().keySet());
						for(String brandVal : brandTemp){
							if(name.contains(brandVal)){
								int score = ip.getBrandMap().get(brandVal);
								temp = rand.nextDouble();
								temp = temp * (score*x);
								v.setInfluence((double)temp);
								keys.remove(name);
							}
						}
						brandTemp.clear();
						Set<String> personTemp = new HashSet<String>();
						personTemp.addAll(ip.getPersonalityMap().keySet());
						for(String personVal : personTemp){
							if(name.contains(personVal)){
								int score = ip.getPersonalityMap().get(personVal);
								temp = rand.nextDouble();
								temp = temp * (score*x);
								v.setInfluence((double)temp);
								keys.remove(name);
							}
						}
						personTemp.clear();
						String category = json.getString("category").toLowerCase();
						String about = null;
						if(json.has("about"))
							about = json.getString("about").toLowerCase();
						Set<String> tempCat = new HashSet<String>();
						tempCat.addAll(categoryKeys);
						for(String catVal : tempCat){
							if(category.contains(catVal)){
								int score = ip.getCategoryMap().get(catVal);
								temp = rand.nextDouble();
								temp = temp * (score*x);
								v.setInfluence((double)temp);
								categoryKeys.remove(catVal);
							}
						}
						tempCat.clear();
						if(about != null){
							tempCat.addAll(categoryKeys);
							for(String catVal : tempCat){
								if(about.contains(catVal)){
									int score = ip.getCategoryMap().get(catVal);
									temp = rand.nextDouble();
									temp = temp * (score*x);
									v.setInfluence((double)temp);
									categoryKeys.remove(catVal);
								}
							}
							tempCat.clear();
						}
						if(keys.isEmpty())
							break;
						if(categoryKeys.isEmpty())
							break;
						}
					}
					res.setVertices(list);
				}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
