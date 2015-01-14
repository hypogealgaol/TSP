import java.util.*;
import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*; 


public class TSPFrame extends JFrame {
	
	private final int WINDOW_HEIGHT = 600;
	private final int WINDOW_WIDTH = 800; 
	private final int Y_OFFSET = 50; 
	private final int X_OFFSET = 150; 
	private final int CUTOFF = 5; 
	JScrollPane scrollpane;
	Panel panel, panel2; 
	JTextField textField;
	JButton CVHButton, button2, button3, bruteForceButton; 
	int[] frequencies;   
	String userInput; 
	int N = 0; 
	ArrayList<Vertex> vertices = new ArrayList<Vertex>(); 
	ArrayList<Edge> edges = new ArrayList<Edge>(); 
	int MAX_LOW;
	int MAX_RIGHT; 
	int MAX_INDEX_PRE_SORT; 
	int startingVertex; 
 
	public TSPFrame() {
		
		//Panel Stuff
		panel = new Panel();
		panel.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT)); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		scrollpane = new JScrollPane(panel);
	    getContentPane().add(scrollpane, BorderLayout.CENTER);
		
	    button3 = new JButton("Enter a number of points"); 
	    textField = new JTextField(15); 
		CVHButton = new JButton("Draw Convex Hull"); 
		button2 = new JButton("Compute Tour"); 
		bruteForceButton = new JButton("Brute Force"); 
		
		//===ACTION LISTENERS===
		
		//Button for doing the convex hull
		CVHButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				userInput = textField.getText(); 
				try {
					N = Integer.parseInt(userInput);
					panel.setOption(1);
					panel.repaint();
				}
				catch(NumberFormatException q) {
					System.out.println("You must enter an integer value"); 
				}
			}
		}); 
		//Button for bruteforce
		bruteForceButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				userInput = textField.getText(); 
				try {
					N = Integer.parseInt(userInput);
					panel.setOption(3);
					panel.repaint();
				}
				catch(NumberFormatException q) {
					System.out.println("You must enter an integer value"); 
				}
			}
		}); 
		//Button for doing the tour
		button2.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				edges.clear(); 
				userInput = textField.getText(); 
				edges.clear(); 
				try {
					N = Integer.parseInt(userInput);
					panel.setOption(1,1);
					panel.repaint();  
				}
				catch(NumberFormatException q) {
					System.out.println("You must enter an integer value"); 
				}
				
			}
		}); 
		//Button for entering a value for N 
		button3.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				//Close previous array first
				vertices.clear(); 
				edges.clear(); 
				panel.setOption(2);
				userInput = textField.getText(); 
				try {
					N = Integer.parseInt(userInput);
					panel.setOption(2);
					panel.repaint(); 
				}
				catch(NumberFormatException q) {
					System.out.println("You must enter an integer value"); 
				}
				 
			}
		}); 
		
		panel.add(button3); 
		panel.add(textField); 
		panel.add(CVHButton); 
		panel.add(button2);
		panel.add(bruteForceButton);
		pack(); 
	}
	//==========================
	// PANEL ------------------
	//==========================
	class Panel extends JPanel{
		
		int xs;
		int yx; 
		int option, option2; 
		public Panel() {
			setBackground(Color.white);
			setForeground(Color.black);
			option = 0;  
			option2 = 0; 
		}
		protected void paintComponent(Graphics g) {
			 
			g.setColor(getBackground()); //colors the window
		    g.fillRect(0, 0, getWidth(), getHeight());
		    g.setColor(getForeground()); //set color and fonts
		    Font MyFont = new Font("Arial",Font.PLAIN,11);
		    g.setFont(MyFont);
		    
		    if(option == 0) {
		    	//Tour 
		    }
		    
			/*Drawing the Convex Hull*/ 
		    if(option == 1) {
		    
		    	
		    	//Points drawing
		    	for(int i=0; i<vertices.size(); i++) {
		    		g.setColor(Color.black);
					g.drawOval(vertices.get(i).getGlobalX(), vertices.get(i).getGlobalY(), 10, 10);
					g.setColor(Color.blue); 
					g.fillOval(vertices.get(i).getGlobalX(), vertices.get(i).getGlobalY(), 10, 10);
		    	}
		    	g.setColor(Color.pink);
		    	g.fillOval(MAX_RIGHT, MAX_LOW, 10, 10);
		    	
		    	//=======================================================
		    	// Sorting point by angle, rounding to two decimal places
		    	//=======================================================
		    	for(int i =0; i<vertices.size(); i++) {
		    		//Computing Slope
		    		double deltaY = MAX_LOW - vertices.get(i).getGlobalY();
		    		double deltaX = vertices.get(i).getGlobalX() - MAX_RIGHT;
		    		double angle = Math.toDegrees(Math.atan2(deltaX, deltaY)); 
		    		//Fixing degrees (rotate around the vertical from the origin
		    		if(angle<0) {
		    			angle*=-1;
		    			angle+=90;	
		    		}
		    		else {
		    			angle = 90-angle; 
		    		}
		    		//2 dp
		    		angle = Math.round(angle*1000.0)/1000.0;
		    		
		    		if(deltaX==0 && deltaY==0) {
		    			//Origin (could find and remove from array maybe)  
			    		vertices.get(i).setID(99);
			    		vertices.get(i).setAngleFromOrigin(0);
			    		Collections.swap(vertices, 0, i); //put this in cell 0
			    		
		    		}
		    		else {
		    			//99 is ID code for origin, so we can find it after sort
		    			
		    			vertices.get(i).setAngleFromOrigin(angle); 
		    		}
		    		
		    	}
		    	//We need to swap 0 with something else (0 should be in beginning) 
		    	
		    	//Test code
//		    	System.out.println("---\nOriginal Slopes\n---\n"); 
//		    	for(int i=0; i<vertices.size(); i++) {
//		    		System.out.println(vertices.get(i).getAngle()); 
//		    	}
//		    	System.out.println("-----------"); 
//		    	
		    	
		    	
		    	//================================
		    	//Quicksort on vertices
		    	//================================
		    	quicksort(1, vertices.size()-1); //changed quicksort left to 1, changed angle for loop to 1
		    	
		    	
		    	//Checking the sorted array 
//		    	for(int i=0; i<vertices.size(); i++) {
//		    		System.out.println(vertices.get(i).getAngle()); 
//		    	}
		    	
		    	//================================
		    	// Put vertices P0+PN-1 on a stack
		    	//================================
		    	Stack stack = new Stack(); 
		    	
		    	//Push P0 and PN-1 onto the stack
		    	stack.push(vertices.get(vertices.size()-1)); //ID of 0
		    	stack.push(vertices.get(0)); //ID of 99; 
		    	
		    	//==========================
		    	// Absolute Left Calculation
		    	// NOTE: There needs to be some sentinel for case where 2 verts
		    	//==========================
		    	int inc = 1; 
		    	while(inc<=vertices.size()-1) { //used to be vertices.size(-1)
		    		//calculate xProduct
			    		Vertex v1 = (Vertex)stack.pop();
			    		Vertex v2 = (Vertex)stack.pop(); 
			    		
			    		int v1x = v1.getGlobalX(); //origin
			    		int v2x = v2.getGlobalX();
			    		int v1y = v1.getGlobalY();
			    		int v2y = v2.getGlobalY(); 
			    		
			    		int vtx = vertices.get(inc).getGlobalX();
			    		int vty = vertices.get(inc).getGlobalY();
			    		int ax = v2x-v1x;
			    		int ay = v2y - v1y;
			    		int bx = vtx - v1x;
			    		int by = vty - v1y; 
			    		int xp = (ax*by)-(ay*bx);
			    		/*Put em back on the stack*/ 
			    		stack.push(v2); stack.push(v1); 
			    		if(xp>0) { //To the left of the line
			    			
			    			vertices.get(inc).setID(inc);
			    			stack.push(vertices.get(inc));
			    			
			    			inc++; 
			    		}
			    		else if (xp < 0) { //To the right of the line
			    			
			    			Vertex temp = (Vertex)stack.pop(); 
			    			
			    		}
			    		else { // Collinearity
			    			
			    			//In the case that they are collinear favor closeness to p0
			    			if(vty<v2y) {
			    				
			    				stack.pop(); 
			    			}
			    			else { 
			    				
			    				stack.push(vertices.get(inc)); 
			    				inc++; 
			    			}
			    		}
//		    		}//end case
		    	}
		    	
		    	//======================
		    	// DRAWING CONVEX HULL
		    	//======================
		    	
		    	//stackSize = hull
		    	ArrayList<Vertex> hull = new ArrayList<Vertex>(); 
		    	//System.out.println("Chomp"); 
		    	 
		    	
		    	//Pop the first so we can draw an edge
		    	Vertex tmp1 = (Vertex)stack.pop();
		    
		    	int fromX = 0; 
		    	int fromY = 0; 
		    	int toX = 0;
		    	int toY = 0; 
		    	int count = 0; 
		    	toX = tmp1.getGlobalX();
	    		toY = tmp1.getGlobalY();
	    		
	    		//Stores
		    	while(stack.size()>0) {
		    		fromX= toX; 
		    		fromY= toY; 
		    		Vertex tmp = (Vertex)stack.pop(); 
		    		hull.add(tmp); //they get pulled off in order so its all good
		    		
		    		toX = tmp.getGlobalX();
		    		toY = tmp.getGlobalY();
		    		g.setColor(Color.green);
		    		g.drawLine(fromX+5, fromY+5, toX+5, toY+5);
		    		g.setColor(Color.green);
		    		g.drawRect(fromX-1, fromY-1, 12, 12);
		    		count++; 
		    	}
		    	
		    	//===========================
		    	// COMPUTING TSP
		    	//===========================
		    	if(option2 ==1) {
		    		
		    		//=======================
		    		//HULL EDGES COMPUTATION
		    		//=======================
		    		
		    		for(int i=0; i<hull.size()-1;i++) {
		    			System.out.println("X: " + hull.get(i).getAngle());
		    			//Compute distance between next and this, could make 1 method
		    			fromX = hull.get(i).getGlobalX();
		    			fromY = hull.get(i).getGlobalY();
		    			toX = hull.get(i+1).getGlobalX();
		    			toY = hull.get(i+1).getGlobalY();
		    			int dx = Math.abs(toX-fromX); 
						int dy = Math.abs(toY-fromY) ;
						int dist = (int)(Math.sqrt(dx*dx + dy*dy)); 
		    					
		    			edges.add(new Edge(hull.get(i), hull.get(i+1), dist)); 
		    		}
		    		//edge case
		    		fromX = hull.get(hull.size()-1).getGlobalX();
	    			fromY = hull.get(hull.size()-1).getGlobalY();
	    			toX = hull.get(0).getGlobalX();
	    			toY = hull.get(0).getGlobalY();
	    			int dx = Math.abs(toX-fromX); 
					int dy = Math.abs(toY-fromY) ;
					int dist = (int)(Math.sqrt(dx*dx + dy*dy)); 
		    		edges.add(new Edge(hull.get(hull.size()-1), hull.get(0), dist)); 
		    		//Now size edges should be same as size of hull 
		    		System.out.println("X: "+hull.get(hull.size()-1).getAngle()); 
		    		System.out.println("Edge size: " + edges.size()); 
		    		System.out.println("Hull size: " + hull.size()); 
		    		
		    		//Now we have edge representation of hull. 
		    		
		    		//Compute an array of vertices not in the hull
		    		ArrayList<Vertex> notHull = new ArrayList<Vertex>(); 
		    		boolean found = false; 
		    		for(int i =0; i<vertices.size();i++) {
		    			for(int j= 0; j<hull.size(); j++) {
		    				if(vertices.get(i)==hull.get(j)) {
		    					 found = true; 
		    				}
		    			}
		    			if(found==false) {
		    				notHull.add(vertices.get(i));
		    			}
		    			else {
		    				found = false; 
		    			}
		    			
		    		}
//		    		System.out.println("Not hull angles: ");
//		    		for(int i = 0; i <notHull.size();i++) {
//		    			System.out.println(notHull.get(i).getAngle()); 
//		    		}
//		    		System.out.println("Not hull size: " + notHull.size()); 
		    		
		    		//==========================
		    		// NEW TOUR = CheapInsert
		    		//==========================
		    		ArrayList<Vertex> cheapInsert = new ArrayList<Vertex>(); 
		    		ArrayList<Edge> cheapEdges = new ArrayList<Edge>();  //the tour
		    		Edge[] new2; 
		    		Edge[] smallest; 
		    		cheapEdges.addAll(edges); //Now cheapEdges has all edges in convex hull
		    		cheapInsert.addAll(hull); 
		    		int minimum = 99999; //arbitrarily large
		    		int temp = 0; 
		    		Edge min1 = null;
		    		Edge min2 = null; 
		    		Edge smallest1;
		    		Edge smallest2; 
		    		Edge badEdge = null; 
		    		Edge e1;
		    		Edge e2; 
		    		Edge current; 
		    		
		    		//Processing all vertex not in hull
		    		while(notHull.size() >0) { 
		    			
		    			//Placeholders for the two edges that may or may not be less weight
		    			smallest1 = new Edge(null, null, 0); 
		    			smallest2 = new Edge(null, null, 0); 
		    			
		    			for(int i=0; i<cheapEdges.size(); i++) { //for current edge 
		    				//Algorithm from homework sheet
		    				current = cheapEdges.get(i); 
		    				int originalDistance = current.getWeight();
		    				Vertex v1 = current.getV1();
		    				Vertex v2 = current.getV2(); 
		    				
		    				//Computing a cheap insertion for all remaining cities
		    				for(int j=0; j<notHull.size(); j++) {
		    					Vertex x = notHull.get(j); 
		    					e1 = new Edge(v1, x); //these two edges continue to get used 
		    					e2 = new Edge(x, v2); 
		    					int d1 = v1.computeDistance(x);
		    					int d2 = x.computeDistance(v2); 
		    					int newDistance = (d1+d2)-originalDistance; 
		    					if(newDistance < minimum) {
		    						minimum = newDistance;
		    						min1 = new Edge(e1); //newEdges
		    						min2 = new Edge(e2); //newEdges
		    					}
		    				}//end for
		    				
		    				//Now we need some extra computation for overwriting
		    				int newDistance = min1.getWeight() + min2.getWeight(); //combined of two edges
		    				int deltaDistance = newDistance - current.getWeight();  //newst Dist.
		    				if(deltaDistance < minimum) {
		    					minimum = deltaDistance;
		    					smallest1 = min1;
		    					smallest2 = min2; 
		    					badEdge = current; 
		    				}
		    			}
		    			//Given a specific edge we can find the cheapest city
		    			//Now that we have the edges we need to replace and dump
		    			//the vertices we already used 
		    			int old = cheapEdges.indexOf(badEdge); // <--- why is this not working
		    			cheapEdges.remove(badEdge); 
		    			cheapEdges.add(old, smallest1);
		    			cheapEdges.add(old+1, smallest2);
		    			notHull.remove(smallest1.getV2()); 
		    			
		    		}
		    		
		    		for(int i=0; i<cheapEdges.size();i++) {
		    			//DRAWING
		    			g.setColor(Color.RED);
		    			g.drawLine(cheapEdges.get(i).getV1().getGlobalX(), 
		    					cheapEdges.get(i).getV1().getGlobalY(), 
		    					cheapEdges.get(i).getV2().getGlobalX(), 
		    					cheapEdges.get(i).getV2().getGlobalY());
		    		}
//		    		System.out.println("Size of cheapedges"  + cheapEdges.size()); 
		    		
		    		
//		    		for(int i=0; i<hull.size(); i++) {//iterates through convex hull
//		    			Edge e = edges.get(i); 
//		    			minimum=edges.get(i).getWeight(); //comparison
//		    			Vertex hullVert = hull.get(i);   //hull vertex we are at 
//		    			
//		    			for(int j=0; j<notHull.size(); j++) {//iterates through available vertices
//		    				
//		    				Vertex x = notHull.get(j); 
//		    				
//	    					int d1=e.getV1().computeDistance(x); //ux
//		    				int d2=e.getV2().computeDistance(x); //xv
//		    				
//		    				if((d1+d2-e.getWeight()) < minimum) {
//		    					System.out.println("Adding vert with angle ("+x.getAngle()+") is a minimum");
//		    					minimum = d1+d2; 
//		    					temp = j; //index of the one we want from the nonEdges
//		    					hullVert = notHull.get(j); 
//		    				} 
//		    				
//		    			}
//		    			//Here we need to remove the old edge and add two new edges and remove city 
//		    			System.out.println("Adding (" + hullVert.getAngle() +") to cheapInsert"); 
//	    				cheapInsert.add(hullVert);
//		    			 
//	    				
//		    			System.out.println("Hull size: " + hull.size() + " after iteration " + i); 
//		    		}
		    		
//		    		System.out.println("cheapInsert size: " + cheapInsert.size()); 
//		    		
//		    		System.out.println("CheapInsert size: " + cheapInsert.size()); 
//		    		int total = 0; 
//		    		for(int i=0; i<cheapInsert.size();i++) {
//		    			
//		    				g.setColor(Color.red); 
//		    				g.drawRect(cheapInsert.get(i).getGlobalX()-4, 
//		    						cheapInsert.get(i).getGlobalY()-4, 18, 18);
////		    				g.drawLine(cheapInsert.get(i).getGlobalX(), 
////		    						cheapInsert.get(i).getGlobalY(), 
////		    						cheapInsert.get(i+1).getGlobalX(), 
////		    						cheapInsert.get(i+1).getGlobalY());
//		    			
//		    			
//		    			
//		    			//System.out.println(cheapInsert.get(i).getAngle());  
//		    		}
//		    		//System.out.println("Total weight after cheap insert: " + total);
		    		
		    	}
		    	
		    	
		    	
		    	//Reset option 
		    	option2 = 0; 
		    	hull.clear(); 
		    }
		  //Drawing the initial points only
		    if(option == 2) {
		    	
		    	computePoints(); 
		    	
		    	//Drawing the points 
		    	for(int i=0; i<vertices.size(); i++) {
		    		g.setColor(Color.black);
					g.drawOval(vertices.get(i).getGlobalX(), vertices.get(i).getGlobalY(), 10, 10);
					g.setColor(Color.blue); 
					g.fillOval(vertices.get(i).getGlobalX(), vertices.get(i).getGlobalY(), 10, 10);
		    	}
		    	//Testing lowest point
		    	g.setColor(Color.pink);
		    	g.fillOval(MAX_RIGHT, MAX_LOW, 10, 10);
		    }
		    if(option == 3) {
		    	
		    	//==================
		    	//BRUTE FORCE IT
		    	//===================
		    	ArrayList<Vertex> optimal = new ArrayList<Vertex>(); 
		    	ArrayList<Vertex> temp = new ArrayList<Vertex>(); 
		    	int tourLength = N+1; 
		    	int tourWeight =9999999; //To find cheapest, initially arbitarily long
		    	int[] perm = new int[vertices.size()]; 
		    	for(int i=0; i<perm.length; i++) {
		    		perm[i] = i; //for basic permutations
		    	}
		    	swap(perm, 0); 
		    	
		    	System.out.println("Finished"); 
		    	
		    	//To do this we have to find all permutations of the array which is N! permutations.
		    	//We can 1) calculate a permutation and then do the next one. Saw this on wikipedia
		    	
		    }
		}
		public int calculateTourWeight(ArrayList<Vertex> opti) {
			int x = 0;  
			for(int i=0; i<opti.size()-1; i++) {
				x+=opti.get(i).computeDistance(opti.get(i+1)); 
			}
			x+=opti.get(0).computeDistance(opti.get(opti.size()-1)); 
			
			return x; 
			
		}
		public void swap(int[] input, int st) {
			int size = input.length;
			if(size==st+1){
				
			}
			else {
				for(int i = st; i<size; i++) {
					int temp = input[i];
					input[i] = input[st]; 
					input[st] = temp;
					//==================> Here is where we calculate a tour and check its weight against
					//===============> a global variable 
					swap(input, st+1); 
				}
			}
			
		}

		//This method just generates N random points
		public void computePoints() {
			Random r = new Random(); //nextInt generates a random number in range of 0 to 500
		
			//we can compute the max here
			int maxLo = 0; 
			int maxR = 0; 
			int maxIndex = 0;
			int tempLo = 0; 
			int tempR = 0; 
			
			for(int i =0; i<N; i++) {
				int xV = r.nextInt(500)+1;
				int yV = r.nextInt(500)+1;
				Vertex v = new Vertex(xV, yV); 
				xV = xV+X_OFFSET;
				yV = yV+Y_OFFSET; 
				
				tempLo = yV;
				tempR = xV; 
				//if both at same y level, pick the rightmost one 
				if(tempLo == maxLo) {
					if(tempR > maxR) {
						maxLo = tempLo; 
						maxR = tempR;
						maxIndex = i; 
					}
				}
				//just regular picking the lower one 
				else if(tempLo > maxLo) {
					maxLo = tempLo; 
					maxR = tempR; 
					maxIndex = i;
				}
				v.setGlobalX(xV);
				v.setGlobalY(yV); 
				vertices.add(v); 

			}
			//store this point right away
			MAX_LOW = maxLo; 
			MAX_RIGHT = maxR; 
			MAX_INDEX_PRE_SORT = maxIndex; 
		}
		
		public void setOption(int x) {
			option = x; 
		}
		public void setOption(int x, int y) {
			option = x; 
			option2 = y; 
		}
		
		/* After this array is sorted, the rightmost CC vertex is the one with the smallest slope
		 * So we have to process the array forward from 0 to find the rightmost, and then process the 
		 * array from index 0 to index of position 0 as the next ones (because the highest negative slope)
		 * array should be structured like a bell curve 
		 * 
		 */
		public void quicksort(int left, int right) {
			//Maybe delete duplicates with smaller y values? 
			if(left+CUTOFF<=right) {
				Vertex pivot = median3(left, right); 
				
				int i =left, j =right-1; 
				for(;;) {
					while(vertices.get(++i).compareTo(pivot)<0) {}
					while(vertices.get(--j).compareTo(pivot)>0) {}
					if(i<j)
						Collections.swap(vertices, i, j);
					else
						break; 
				}
				Collections.swap(vertices, i, right-1);
				quicksort(left, i-1);
				quicksort(i+1, right); 
			}
			else {
				//Insertion sort on small elements
				insertionSort(left, right); 
			}
		}
		/* Basic insertion sort from l to r*/ 
		public void insertionSort(int left, int right) {
			int j;
			for(int p = left+1; p<=right; p++) {
				Vertex tmp = vertices.get(p);
				for(j=p; j>left && tmp.compareTo(vertices.get(j-1))<0; j--){
					//System.out.println("tmp < j-1"); 
					vertices.set(j, vertices.get(j-1));
				}
				vertices.set(j, tmp); //overwrite 
			}
			
		}
		//don't need to pass in the array argument cuz its global 
		public Vertex median3(int left, int right) {
			int center = (left+right)/2; 
			if(vertices.get(center).compareTo(vertices.get(left))<0) 
				Collections.swap(vertices, center, left);
			if(vertices.get(right).compareTo(vertices.get(left))<0)
				Collections.swap(vertices, left, right);
			if(vertices.get(right).compareTo(vertices.get(center))<0)
				Collections.swap(vertices, right, center);
			//Pivot
			Collections.swap(vertices, center, (right-1));
			
			return vertices.get(right-1); 
		}
	}		

}
