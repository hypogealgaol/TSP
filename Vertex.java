//don't think first comparable is needed on City but definitely needed on Edge 

public class Vertex implements Comparable {
	//Coordinates + name 
	private String name;
	private int xC;
	private int yC; 
	private int globalX;
	private int globalY; 
	private boolean visited; 
	private int ID; 
	private double angle; 
	
	public Vertex(int x, int y) {
		xC = x;
		yC = y; 
		globalX = 0;
		globalY = 0; 
		visited = false; 
	}
	public int getX() {
		return xC; 
	}
	public int getY() {
		return yC;
	}
	public String getName() {
		return name; 
	}
	//Coordinates after modification
	public void setGlobalX(int a) {
		globalX = a; 	
	}
	public void setGlobalY(int a) {
		globalY = a; 
	}
	public int getGlobalX() {
		return globalX;  	
	}
	public int getGlobalY() {
		return globalY;  
	}
	public void setVisited() {
		visited = true; 
	}
	public void setNotVisited() {
		visited = false; 
	}
	public boolean getVisited() {
		return visited; 
	}
	public void setID(int i) {
		ID = i; 
	}
	public int getID() {
		return ID; 
	}
	public void setAngleFromOrigin(double x) {
		angle = x; 
		
	}
	public double getAngle() {
		return angle; 
	}
	public int computeDistance(Vertex second) {
		int fromX = second.getGlobalX();
		int fromY = second.getGlobalY();
		int dx = Math.abs(this.globalX-fromX); 
		int dy = Math.abs(this.globalY-fromY) ;
		int dist = (int)(Math.sqrt(dx*dx + dy*dy));
		return dist; 
		
	}
	//For traversing through vertexes based on slope
	public int compareTo(Object c) {
		Vertex x = (Vertex)c; 
		if(angle>x.angle) return 1;
		if(angle == x.angle) return 0;
		return -1;
	}
}
