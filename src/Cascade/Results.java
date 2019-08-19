package Cascade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Results {
	private Set<Vertex> vertices;
	private Set<Vertex> seeds;
	private List<Vertex[]> result;
	private Set<Vertex> activated;
	public Results(){
		activated = new HashSet<Vertex>();
		seeds = new HashSet<Vertex>();
		result = new ArrayList<Vertex[]>();
	}
	public Set<Vertex> getActivated() {
		return activated;
	}
	public void setActivated(Set<Vertex> activ) {
		this.activated = activ;
		for(Vertex v : seeds){
			activated.remove(v);
		}
	}
	public Set<Vertex> getVertices() {
		return vertices;
	}
	public void setVertices(Set<Vertex> vertices) {
		this.vertices = vertices;
	}
	public List<Vertex[]> getResult() {
		return result;
	}
	public void setResult(List<Vertex[]> result) {
		this.result = result;
	}
	public Set<Vertex> getSeeds() {
		return seeds;
	}
	public void setSeeds(Set<Vertex> seeds) {
		this.seeds = seeds;
	}
}
