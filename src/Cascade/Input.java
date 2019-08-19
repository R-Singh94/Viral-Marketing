package Cascade;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Input {
	private String city;
	private String country;
	private int age1;
	private int age2;
	private Set<String> desig;
	private Map<String,Integer> brandMap;
	private Map<String,Integer> personalityMap;
	private Map<String,Integer> categoryMap;
	public Input(){
		desig = new HashSet<String>();
		brandMap = new HashMap<String,Integer>();
		personalityMap = new HashMap<String,Integer>();
		categoryMap = new HashMap<String,Integer>();
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getAge1() {
		return age1;
	}
	public void setAge1(int age1) {
		this.age1 = age1;
	}
	public int getAge2() {
		return age2;
	}
	public void setAge2(int age2) {
		this.age2 = age2;
	}
	public Set<String> getDesig() {
		return desig;
	}
	public void setDesig(String desig) {
		this.desig.add(desig);
	}
	public Map<String, Integer> getBrandMap() {
		return brandMap;
	}
	public void setBrandMap(String key, int score) {
		brandMap.put(key, score);
	}
	public Map<String, Integer> getPersonalityMap() {
		return personalityMap;
	}
	public void setPersonalityMap(String key, int score) {
		personalityMap.put(key, score); 
	}
	public Map<String, Integer> getCategoryMap() {
		return categoryMap;
	}
	public void setCategoryMap(String key, int score) {
		categoryMap.put(key, score);
	}
}
