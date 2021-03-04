package game.board;

import java.util.*;



/** 
 * Abstract class to create a graph object. Created in C++, migrated to Java.
 * NOTE: Some of the original methods were comment, since they are not
 * needed in this implementation
 * 
 * @author Carlos Granados
 * @version 1.1, 01.03.2021
 */
public abstract class Graph
{	
	// Class Attributes
	
	protected int n_nodes, n_edges=0, id;

	protected Vector<Integer> val_nodes = new Vector<Integer>();
	protected double [][] val_edges;
	protected boolean [][] g;

	// Class Methods
	


	// getters and setters

	// Get vertices
	public abstract int get_num_nodes();
	
//	// Get number of neighbors
//	public abstract int get_num_neighbors();

	// Get the number of edges (or size)
	public abstract int get_size();

	// Other methods

//	// Check if the nodes i and j are adjacent (an edge from i to j)
//	public abstract boolean adjacent();
//
//	// Add an edge between i and j if it is not there
//	public abstract void add();
//
//	// Delete an edge between i and j if it is there
//	public abstract void del();

	// Function to create the graph and initialize it to 0
	protected abstract void make_graph();
	
	// Resize vectors to save nodes and edges
	protected abstract void resize_storage();
	
	// Function to print the graph in a matrix form.
	// It prints the values associated to each edge
	public abstract void print();	

}