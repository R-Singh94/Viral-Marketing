package Cascade;

import java.util.HashSet;
import java.util.Set;

public class Graph {
	private int numOfEdges;
	protected Set<Vertex> vertex;
	public Graph(){
		vertex = new HashSet<Vertex>();
	}
	public Set<Vertex> getVertices(){
		return vertex;
	}
	public Vertex getVertex(int id){
		Vertex VertexFound = null; 
		for(Vertex v: vertex)
			if(v.getId() == id){
				VertexFound = v;
				break;
			}
		return VertexFound;
	}
	public int getNumVertices(){
		return vertex.size();
	}
	public int getNumEdges(){
		return numOfEdges;
	}
	public void addVetex(Vertex v){
		vertex.add(v);
	}
	public void setNumEdges(int num){
		numOfEdges = num;
	}
	public boolean containsVertex(int id){
		for(Vertex v: vertex)
			if(v.getId() == id)
				return true;
		return false;
	}
}
