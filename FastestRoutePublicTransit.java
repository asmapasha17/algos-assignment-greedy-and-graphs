/*
 /*
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package fastestroutepublictransit;

/**
 * Public Transit
 * Author: Asma Pasha and Carolyn Yao
 * Does this compile? Y/N Y
 */

/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest-paths algorithm. As it is, you can run this class and get the
 * solutions from the existing shortest-path algorithm.
 */
public class FastestRoutePublicTransit {

	/**
	 * The algorithm that could solve for shortest travel time from a station S to a
	 * station T given various tables of information about each edge (u,v)
	 *
	 * @param S         the s th vertex/station in the transit map, start From
	 * @param T         the t th vertex/station in the transit map, end at
	 * @param startTime the start time in terms of number of minutes from 5:30am
	 * @param lengths   lengths[u][v] The time it takes for a train to get between
	 *                  two adjacent stations u and v
	 * @param first     first[u][v] The time of the first train that stops at u on
	 *                  its way to v, int in minutes from 5:30am
	 * @param freq      freq[u][v] How frequently is the train that stops at u on
	 *                  its way to v
	 * @return shortest travel time between S and T
	 */
	public int myShortestTravelTime(int S, int T, int startTime, int[][] lengths, int[][] first, int[][] freq) {
		// Your code along with comments here. Feel free to borrow code from any
		// of the existing method. You can also make new helper methods.
		int path[] = shortestPathUsingDijestrea(lengths, S, T); // find the shortest path from start station to
																// destination
		int index = first[0].length - 1;
		// find the starting part
		while (path[index] != S && index > 0) {
			index--;
		}
		// keeps count of smallest time
		int smallTime = 0;
		int comingPointTime;
		int currentTime = startTime;
		for (int i = index; i >= 1; i--) {
			comingPointTime = comingPoint(first, freq, currentTime, path, i, lengths);
			smallTime = smallTime + (comingPointTime - currentTime);
			currentTime = comingPointTime;
		}
		return smallTime;
	}

	public int[] shortestPathUsingDijestrea(int[][] graph, int source, int T) {
		int numVertices = graph[0].length;
		// This is the array where we'll store all the final shortest times
		int[] times = new int[numVertices];
		// This array stores the shortest path from starting station source, to target
		// station, T
		int[] path = new int[numVertices];
		// keeps track of the previous stations from the current station
		int[] prev = new int[numVertices];
		prev[source] = -1;// source is the starting station, so there is no previous station from the
							// source

		// processed[i] will true if vertex i's shortest time is already finalized
		Boolean[] processed = new Boolean[numVertices];
		// Initialize all distances as INFINITE and processed[] as false
		for (int v = 0; v < numVertices; v++) {
			times[v] = Integer.MAX_VALUE;
			path[v] = -1; // initialize to -1 to indicate the end of the path
			processed[v] = false;
		}
		// Distance of source vertex from itself is always 0
		times[source] = 0;

		// Find shortest path to all the vertices
		for (int count = 0; count < numVertices - 1; count++) {
			// Pick the minimum distance vertex from the set of vertices not yet processed.
			// u is always equal to source in first iteration.
			// Mark u as processed.
			int u = findNextToProcess(times, processed);
			processed[u] = true;

			// Update time value of all the adjacent vertices of the picked vertex.
			for (int v = 0; v < numVertices; v++) {
				// Update time[v] only if is not processed yet, there is an edge from u to v,
				// and total weight of path from source to v through u is smaller than current
				// value of time[v]
				if (!processed[v] && graph[u][v] != 0 && times[u] != Integer.MAX_VALUE
						&& times[u] + graph[u][v] < times[v]) {
					times[v] = times[u] + graph[u][v];
					prev[v] = u;
				}
			}
		}

		int index = T; // keep track of the stations
		int currentIndex = 0; // keep track of current station
		// backtrack from the destination to the source with the help from the prev
		// array (the array that holds the previous station)
		// at -1, we know that we hit the source or starting station
		while (prev[index] != -1) {
			path[currentIndex] = index;
			currentIndex++;
			index = prev[index];
		}
		path[currentIndex] = index;// add the source station
		return path;

	}

	public int comingPoint(int first[][], int freq[][], int currentTime, int path[], int index, int lengths[][]) {
		// were at our destination
		if (index == 0) {
			return 0;
		} // current position
		int currentNum = path[index];
		int upComingPosition = path[index - 1]; // the next point the train
		// the time of the first train
		int trainTimes = first[currentNum][upComingPosition];
		int i = 0;
		while (trainTimes < currentTime) {
			trainTimes = first[currentNum][upComingPosition] + (i * freq[currentNum][upComingPosition]);
			i++;
		}
		return trainTimes + lengths[currentNum][upComingPosition];
	}

	/**
	 * Finds the vertex with the minimum time from the source that has not been
	 * processed yet.
	 * 
	 * @param times     The shortest times from the source
	 * @param processed boolean array tells you which vertices have been fully
	 *                  processed
	 * @return the index of the vertex that is next vertex to process
	 */
	public int findNextToProcess(int[] times, Boolean[] processed) {
		int min = Integer.MAX_VALUE;
		int minIndex = -1;

		for (int i = 0; i < times.length; i++) {
			if (processed[i] == false && times[i] <= min) {
				min = times[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	public void printShortestTimes(int times[]) {
		System.out.println("Vertex Distances (time) from Source");
		for (int i = 0; i < times.length; i++)
			System.out.println(i + ": " + times[i] + " minutes");
	}

	/**
	 * Given an adjacency matrix of a graph, implements
	 * 
	 * @param graph  The connected, directed graph in an adjacency matrix where if
	 *               graph[i][j] != 0 there is an edge with the weight graph[i][j]
	 * @param source The starting vertex
	 */
	public void shortestTime(int graph[][], int source) {
		int numVertices = graph[0].length;

		// This is the array where we'll store all the final shortest times
		int[] times = new int[numVertices];

		// processed[i] will true if vertex i's shortest time is already finalized
		Boolean[] processed = new Boolean[numVertices];

		// Initialize all distances as INFINITE and processed[] as false
		for (int v = 0; v < numVertices; v++) {
			times[v] = Integer.MAX_VALUE;
			processed[v] = false;
		}

		// Distance of source vertex from itself is always 0
		times[source] = 0;

		// Find shortest path to all the vertices
		for (int count = 0; count < numVertices - 1; count++) {
			// Pick the minimum distance vertex from the set of vertices not yet processed.
			// u is always equal to source in first iteration.
			// Mark u as processed.
			int u = findNextToProcess(times, processed);
			processed[u] = true;

			// Update time value of all the adjacent vertices of the picked vertex.
			for (int v = 0; v < numVertices; v++) {
				// Update time[v] only if is not processed yet, there is an edge from u to v,
				// and total weight of path from source to v through u is smaller than current
				// value of time[v]
				if (!processed[v] && graph[u][v] != 0 && times[u] != Integer.MAX_VALUE
						&& times[u] + graph[u][v] < times[v]) {
					times[v] = times[u] + graph[u][v];
				}
			}
		}

		printShortestTimes(times);
	}

	public static void main(String[] args) {
		// You can create a test case for your implemented method for extra credit below
		/* length(e) */
		int lengthTimeGraph[][] = new int[][] { { 0, 4, 0, 0, 0, 0, 0, 8, 0 }, { 4, 0, 8, 0, 0, 0, 0, 11, 0 },
				{ 0, 8, 0, 7, 0, 4, 0, 0, 2 }, { 0, 0, 7, 0, 9, 14, 0, 0, 0 }, { 0, 0, 0, 9, 0, 10, 0, 0, 0 },
				{ 0, 0, 4, 14, 10, 0, 2, 0, 0 }, { 0, 0, 0, 0, 0, 2, 0, 1, 6 }, { 8, 11, 0, 0, 0, 0, 1, 0, 7 },
				{ 0, 0, 2, 0, 0, 0, 6, 7, 0 } };
		FastestRoutePublicTransit t = new FastestRoutePublicTransit();
		t.shortestTime(lengthTimeGraph, 0);
		int first[][] = new int[][] { { 0, 5, 0, 8, 0, 2, 0, 1, 1 }, { 16, 0, 2, 0, 0, 1, 0, 3, 0 },
				{ 0, 7, 0, 24, 0, 0, 0, 0, 0 }, { 5, 0, 11, 0, 9, 0, 0, 14, 0 }, { 0, 0, 0, 40, 0, 28, 0, 0, 0 },
				{ 6, 48, 0, 0, 2, 0, 12, 0, 0 }, { 0, 2, 0, 0, 0, 6, 0, 20, 0 }, { 0, 7, 0, 8, 3, 0, 23, 0, 2 },
				{ 5, 0, 0, 4, 0, 0, 0, 8, 0 } };

		/* freq(e) */
		int freq[][] = new int[][] { { 10, 2, 3, 10, 0, 4, 0, 0, 2 }, { 12, 0, 3, 0, 0, 3, 0, 7, 0 },
				{ 0, 7, 1, 3, 0, 0, 3, 0, 0 }, { 5, 0, 10, 0, 5, 0, 0, 6, 0 }, { 0, 0, 0, 2, 0, 14, 4, 0, 0 },
				{ 8, 5, 0, 0, 4, 0, 16, 4, 0 }, { 0, 0, 0, 0, 0, 4, 0, 6, 0 }, { 0, 7, 0, 20, 0, 0, 8, 0, 5 },
				{ 1, 0, 0, 0, 0, 0, 0, 5, 0 } };
		/* length(e) */
		int length[][] = new int[][] { { 1, 1, 0, 5, 0, 5, 0, 0, 1 }, { 6, 2, 2, 0, 0, 3, 3, 2, 0 },
				{ 3, 2, 0, 1, 0, 0, 0, 0, 0 }, { 5, 0, 3, 0, 5, 0, 0, 2, 0 }, { 0, 0, 0, 5, 0, 4, 2, 0, 0 },
				{ 5, 3, 0, 0, 4, 0, 2, 0, 0 }, { 0, 0, 0, 0, 0, 2, 0, 2, 0 }, { 0, 7, 0, 2, 1, 0, 2, 3, 5 },
				{ 1, 0, 0, 0, 0, 0, 0, 5, 0 } };
		FastestRoutePublicTransit test = new FastestRoutePublicTransit();
		System.out.println(test.myShortestTravelTime(6, 3, 32, length, first, freq) + " minutes...");
		System.out.println(test.myShortestTravelTime(6, 6, 2, length, first, freq) + " minutes...");
		System.out.println(test.myShortestTravelTime(6, 8, 32, length, first, freq) + " minutes...");
		System.out.println(test.myShortestTravelTime(1, 2, 3, length, first, freq) + " minutes...");
		System.out.println(test.myShortestTravelTime(1, 2, 31, length, first, freq) + " minutes...");
		System.out.println(test.myShortestTravelTime(5, 8, 3, length, first, freq) + " minutes...");

	}
}
