
public class Edge implements Comparable {
	
	private Vertex v1;
	private Vertex v2; 
	private int weight; 
	
	public Edge(Vertex c1, Vertex c2, int w) {
		v1 = c1;
		v2 = c2;
		weight = w; 
	}
	public Edge(Vertex c1, Vertex c2) {
		v1 = c1;
		v2 = c2;
		weight = v1.computeDistance(v2); 
	}
	public Edge(Edge e) {
		v1 = e.getV1();
		v2 = e.getV2();
		weight = e.getWeight(); 
	}
	
	public int getWeight() {
		return weight; 
	}
	public int compareTo(Object c) {
		Edge x = (Edge)c; 
		if(weight>x.weight) return 1;
		if(weight == x.weight) return 0;
		return -1;
	}
	public Vertex getV1() {
		return v1;
	}
	public Vertex getV2() {
		return v2; 
	}

}
