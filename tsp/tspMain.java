import java.math.BigDecimal;

import FileIO.java;
/*
 *  My solution to the tsp will be generated by mixing various algorithms
 *  first I will store the distances between all towns in a 2d array
 *  then calculate nearest neighbor routes for all airports as the starting point
 *  finally i will use 2-opt to optimize my route further.
 */
public class tspMain {

	public static void main(String[] args) {
		//first i will load the airports into 2d array
		FileIO reader = new FileIO(); 
	    String[] contents = reader.load("C:/1000airports.txt");
	    String[][] seperated = new String[1000][4];
	    double[][] distances = new double[1000][1000];
	    String route = "";
	    double totalDistance = 0.0;
	    double finalD = 10000000;
	    for(int i = 0; i < contents.length; i++)
	    {
	    	String[] tempSplit = contents[i].split(",");
	    	for(int j = 0; j < tempSplit.length; j++)
	    	{
	    		seperated[i][j] = tempSplit[j];
	    	}
	    }
	    //printArray(seperated);
	    //Next i will fill distances[][] with info from seperated[][]
	    for(int row = 0; row < distances.length; row++)
	    {
	    	for(int column = 0; column < distances.length; column++)
	    	{
	    		if(row != column)
	    		{
	    			distances[row][column] = distFrom(Double.parseDouble(seperated[row][2]), Double.parseDouble(seperated[row][3]), Double.parseDouble(seperated[column][2]), Double.parseDouble(seperated[column][3]), "Mi"); 
	    		}
	    	}
	    }
	    //printArray(distances);
	    //with distances filled its to implement nearest neighbor 1000 times wooh
	    boolean[] visited = new boolean[1000];
	    double minValue = 1000000;
	    int current;
	    int next = 0;
	    String finalRoute = "";
	    
	    for(int difStarts = 0; difStarts < distances.length; difStarts++)
	    {
	    	current = difStarts;
	    	route = "";
	    	totalDistance = 0.0;
	    	for(int i = 0; i < visited.length; i++)
	    	{
	    		visited[i] = false;
	    	}
	    	for(int difPoints = 0; difPoints < distances.length; difPoints++)
	    	{
	    		visited[current] = true;
	    		minValue = 1000000;
	    		for(int arraySearch = 0; arraySearch< distances.length; arraySearch++)
	    		{
	    			
	    			if((distances[current][arraySearch] != 0) && (distances[current][arraySearch] < minValue) && (visited[arraySearch] == false))
	    			{
	    				minValue = distances[current][arraySearch];
	    				next = arraySearch;
	    			}
	    			
	    		}
	    		route = route + (current+1) + ".";
	    		totalDistance = totalDistance + minValue;
	    		current = next;
	    	}
	    	if(totalDistance < finalD)
	    	{
	    		finalRoute = route;
	    		finalD = totalDistance;
	    	}
	    }
	    //System.out.println(finalD);
	    System.out.println("After 1000 nearest neighbours " + finalRoute);
	    
	    
	    //double randoms[][] = new double[1000][1000];
	    //String carlos[][] = new String[1000][2];
	    /*
	    //monte carlo sim
	    for(int monteCarlo = 0; monteCarlo < 1000; monteCarlo++)
	    {
	    	
	    	for(int row = 0; row < randoms.length; row++)//fill random[][] with distances + random between 0-20
	    	{
	    		for(int column = 0; column < randoms.length; column++)
	    		{
	    			if(row != column)
	    			{
	    				randoms[row][column] = distances[row][column] + ((int)(Math.random() * 5));
	    			}
	    		}
	    	}
	    	
	    	current = finalRoute.charAt(0);
	    	route = "";
	    	totalDistance = 0.0;
	    	for(int i = 0; i < visited.length; i++)
	    	{
	    		visited[i] = false;
	    	}
	    	for(int difPoints = 0; difPoints < distances.length; difPoints++)
	    	{
	    		visited[current] = true;
	    		minValue = 1000000;
	    		for(int arraySearch = 0; arraySearch< distances.length; arraySearch++)
	    		{
	    			
	    			if((distances[current][arraySearch] != 0) && (distances[current][arraySearch] < minValue) && (visited[arraySearch] == false))
	    			{
	    				minValue = distances[current][arraySearch];
	    				next = arraySearch;
	    			}
	    			
	    		}
	    		route = route + (current+1) + ".";
	    		totalDistance = totalDistance + minValue;
	    		current = next;
	    	}
	    	if(totalDistance < finalD)
	    	{
	    		finalRoute = route;
	    		finalD = totalDistance;
	    	}
	    	carlos[monteCarlo][0] = finalRoute;
	    	carlos[monteCarlo][1] = (""+finalD);
	    	
	    }
	    double minV = 100000000;
	    int shortestR = 0;
	    for(int i = 0; i < 1000; i++)
	    {
	    	if(Double.parseDouble(carlos[i][1]) < minV)
	    	{
	    		minV = Double.parseDouble(carlos[i][1]);
	    		shortestR = i;
	    	}
	    }
	    System.out.println("After 1000 monte carlo sim " + (carlos[shortestR+1][0]));
	    //System.out.println(route);
	     * 
	     */
	    
	    //2 OPT
	    String[] irrelevant = finalRoute.split("\\.");
	    int[] stops = new int[irrelevant.length];
	    for(int i = 0; i < stops.length; i++)
	    {
	    	stops[i] = Integer.parseInt(irrelevant[i]);
	    }
	    System.out.println(calculateDistance(stops, distances));
	}
	/*
	 * This method takes an unrounded double an int for the amount of numbers after decimal point and a method to round the number
	 * It returns the rounded double and is used with distFrom()
	 */
	public static double round(double unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}
	/*
	 * This is the method to calculate the distance between two airports
	 * It takes in two latitudes and two longitudes from the airports in question and I will default the unit to miles
	 * It returns the distance rounded to two decimal places
	 */
	public static double distFrom(double lat1, double lng1, double lat2, double lng2, String unit)
	{
	    double theta = lng1 - lng2;

	    double distance = (
	        Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
	     )+(
	        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta))
	     );
	    distance = Math.acos(distance);
	    distance = Math.toDegrees(distance);
	    distance = distance * 60 * 1.1515;

	    switch(unit)
	    {
	        /* Mi = miles, Km = Kilometers */
	        case "Mi"   : 
	            break;
	        case "Km"   : 
	            distance = distance *1.609344;
	            break;
	    }
	    distance = round(distance, 2, BigDecimal.ROUND_HALF_UP);
	    return distance;
	}
	
	public static void printArray(double matrix[][]) {
	    for (int row = 0; row < matrix.length; row++) {
	        for (int column = 0; column < matrix[row].length; column++) {
	            System.out.print(matrix[row][column] + " ");
	        }
	        System.out.println();
	    }
	}
	
	public static double calculateDistance(int[] route, double[][] distances)
	{
		double totalDistance = 0;
		for(int i = 0; i < route.length - 1; i++)
		{
			totalDistance = totalDistance + distances[route[i]-1][route[i+1]-1];
		}
		totalDistance = totalDistance + distances[route[0]-1][route[route.length-1]-1];
		return totalDistance * 1.6;
	}
	
}
