package Cascade;

import java.util.HashSet;
import java.util.Set;

public class Vertex {
	private int id;
	private int degree;
	private String name;
	private String email;
	private int age;
	private String designation;
	private double influence;
	private float latitude;
	private float longitude;
	private float threshold;
	private int roundActivated;
	private int activeEdges;
	private int edgesRequired;
	private int[] activatedNeighbours;
	private Set<Vertex> neighbours;
	private int totalActivated;
	private int activated;
	private int seedActive;
	private String city;
	public Vertex(int id){
		this.id = id;
		neighbours = new HashSet<Vertex>();
		activatedNeighbours = new int[6];
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public double getInfluence() {
		return influence;
	}
	public void setInfluence(double increment) {
		if(influence+increment<1.0)
			influence += increment;
	}
	public int getActivatedNeighbours(int i) {
		return activatedNeighbours[i];
	}
	public void setActivatedNeighbours(int i) {
		activatedNeighbours[i]++;
	}
	public int getActiveEdges() {
		return activeEdges;
	}
	public int getEdgesRequired() {
		return edgesRequired;
	}
	public int getId() {
		return id;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree() {
		degree = neighbours.size();
		activeEdges = degree;
		edgesRequired = (int)Math.ceil(threshold * degree);
	}
	public float getThreshold() {
		return threshold;
	}
	public void setThreshold() {
		if(influence <= 0.95)
			threshold = (float) (1.0 - influence);
		else 
			threshold = (float)0.05;
	}
	public int getRoundActivated() {
		return roundActivated;
	}
	public void setRoundActivated(int roundActivated) {
		if(roundActivated<=6)
			this.roundActivated = roundActivated;
	}
	public Set<Vertex> getNeighbours() {
		return neighbours;
	}
	public void setNeighbours(Vertex v) {
		if(!neighbours.contains(v))
			neighbours.add(v);
	}
	public void computeActiveEdges(Set<Vertex> terminal){
		Set<Vertex> temp = new HashSet<Vertex>();
		for(Vertex temporary: terminal)
			temp.add(temporary);
		temp.retainAll(neighbours);
		activeEdges -= temp.size();
	}
	public void computeActiveEdges(){
		edgesRequired = Math.max(edgesRequired-1, 0);
	}
	public int getTotalActivated(int j){
		totalActivated=0;
		for(int i=0;i<j;i++)
			totalActivated+=activatedNeighbours[i];
		return totalActivated;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public void setActivated(){
		activated = 0;
	}
	public int getTotalActivated(){
		return activated;
	}
	public void incActivated(){
		activated++;
	}
	public void setSeedActive(){
		seedActive = 0;
	}
	public int getSeedActive(){
		return seedActive;
	}
	public void incSeedActive(){
		seedActive++;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
