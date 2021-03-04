package game.board;

import java.util.*;

import game.user.*;

import java.lang.Runtime;



/** 
 * Implements the abstract Graph class. Created in C++, migrated to Java.
 * It creates a graph, of degree 6, used to simulate a Hex board game.
 * 
 * @author Carlos Granados
 * @version 1.1, 01.03.2021
 */

public class HexBoard extends Graph
{
	
	// Attributes

	protected int edge_size;
	
	protected int val_none = 46;		// Print '.'	
	protected int hor_line = 45;        // Print horizontal line, '-'
	protected int diags [] = {47, 92};  // Print '\' and '/'
	
	private char W_SPACE = ' ';
	private char NONE = (char)val_none;
	private char H_LINE = (char)hor_line;
	private char DIAGS [] = {(char)diags[0], (char)diags[1]};
	private char PLAYER_MARKS [] = {'X', 'O'};
	
	private String H_FULL;
	
	// Constructors
	
	/**
	 * Generates a void board 
	 */
	public HexBoard() {
		this.n_nodes = 0;
		this.edge_size = 0;
	}

	
	/**
	 * Generated a Hex board of the given size. A square board is assumed.
	 * 
	 * @param size
	 */
	public HexBoard(int size) {
		this.edge_size = size;
		this.n_nodes = edge_size * edge_size;

		// Resize the vectors to save nodes and edges
		resize_storage();

		// Initialize the graph to 0
		make_graph();

	}

	
	// Methods

	// setters and getters

	/**
	 *Function to get the size of the board
	 */
	public int get_size() {
		return edge_size;
	}
	
	/**
	 *Function to get the number of nodes (size*size)
	 */
	public int get_num_nodes() {
		return n_nodes;
	}
	
	/**
	 * Returns the number of neighbors for a given position, given
	 * specifically the i and j coordinates
	 * 
	 * @param i						row value of pos
	 * @param j						col value of pos
	 * @return num_neighbors		number of neighbors for a given pos (i, j)
	 */
	public int get_num_neighbors(int i, int j) {
		// Corners
		if (i == 0 && j == 0) {
			return 2;
		} else if (i == edge_size-1 && j == edge_size-1) {
			return 2;
		} else if (i == 0 && j == edge_size-1) {
			return 3;
		} else if (i == edge_size-1 && j == 0) {
			return 3;
		}

		// 	Borders
		if (i == 0 || i == edge_size-1) {
			return 4;
		}
		if (j == 0 || j == edge_size-1) {
			return 4;
		}

		
		// All others
		return 6;
	}
	
	/**
	 * Returns the number of neighbors for a given position, given
	 * specifically the coordinate in the board
	 * 
	 * @param coord					position (node) in the board
	 * @return num_neighbors		number of neighbors for a given coord
	 */
	public int get_num_neighbors(CoordBoard coord) {
		// Extract individual values
		int i = coord.first();
		int j = coord.second();
		
		return get_num_neighbors(i, j);
	}

	/**
	 * For a given coordinate, it returns a set with all its neighbors
	 * 
	 * @param coord			Position (node) on the board 
	 * @return neighbors	Set with all neighbors of coord
	 */
	public Set<CoordBoard> get_neighbors(CoordBoard coord) {

		int i = coord.first();
		int j = coord.second();

		Set<CoordBoard> neighbors = new HashSet<CoordBoard>();

		int row = i - 1;                          // Upper row
		if (row >= 0) {
			for (int col=j; col <= j+1; col++) {  // col = j, j+1
				CoordBoard neigh = new CoordBoard(row, col);
				if (col >= 0 && col < edge_size) {
					add_pair(neighbors, neigh);
				}
			}
		}

		row++;                                    // Current row
		for (int col=j-1; col <= j+1; col++) {    // col = j-1, j+1
			CoordBoard neigh = new CoordBoard(row, col);
			if (col == j) {
				continue;
			}
			if (col >= 0 && col < edge_size) {
				add_pair(neighbors, neigh);
			}
		}

		row++;                                    // Lower row
		if (row < edge_size){                     // col = j-1, j
			for (int col=j-1; col <= j; col++) {
				CoordBoard neigh = new CoordBoard(row, col);
				if (col >= 0 && col < edge_size) {
					add_pair(neighbors, neigh);
				}
			}
		}

		return neighbors;

	}
	
	/**
	 * For a given player and its new move (coord), add the corresponding
	 * mark to the graph (board)
	 * 
	 * @param player_id		ID of the current player
	 * @param coord			move to set
	 */
	public void set_move(int player_id, CoordBoard coord) {

		int pos = coord.get_pos(edge_size);

		if (player_id == 1 || player_id == 2) {
			val_nodes.add(pos, player_id);
		}

	}



	// Other methods

	/**
	 * Check if the given value is inside the board (row or col) 
	 * 
	 * @param i				coordinate component of a move.
	 * @return boolean		<code>true<\code> if the move is valid.
	 * 						<code>false<\code> otherwise.
	 */
	public boolean valid(int i) {
		if (i >= 0 && i < edge_size) {
				return true;
		}
		return false;
	}

	/**
	 * Check if the points i and j make a valid coordinate. If true, returns
	 * the new coordinate. Otherwise for a new coordinate.
	 * 
	 * @param i			x-component of the move
	 * @param j			y-component of the move
	 * @return			valid <code>(i, j)<\code> coordinate
	 */
	public CoordBoard check_coords(int i, int j) {
		boolean coord_valid = false;
		int new_i = i, new_j = j;
		int [] new_ij = {new_i, new_j};
		
		coord_valid = valid(i) && valid(j);

		while (!coord_valid) {
			System.out.println("Not a valid move, Coords. out of range. Try again:");
			new_ij = InterfaceIO.read_input_coord();
			coord_valid = (valid(new_ij[0]) && valid(new_ij[0]));
		}
		return (new CoordBoard(new_ij[0], new_ij[1]));
	}
	
	/**
	 * Check if the two first components of ij make a valid coordinate. If true,
	 * returns the new coordinate. Otherwise ask for a new coordinate.
	 * 
	 * @param ij		array with input coordinates
	 * @return			valid <code>(i, j)<\code> coordinate
	 */
	public CoordBoard check_coords(int [] ij) {
		return check_coords(ij[0], ij[1]);
	}
	
	/**
	 * Print the hex graph, including the marks for each player, if any.
	 * It try first to clean the screen.
	 *
	 */
	public void print() {

		H_FULL = to_string(new char [] {W_SPACE, H_LINE, W_SPACE});
		
		// Clear the screen
		clearScreen();					// Should work from a terminal

		// Print Col. Numbers
		String s0 = to_string(new char [] {W_SPACE});
		String s1 = to_string(new char [] {W_SPACE, W_SPACE, W_SPACE});
		
		System.out.print(s1);
		for (int i=0; i < edge_size; i++) {
			System.out.print(i + s1);
		}
		System.out.print('\n');

		// Loop rows
		for (int i=0; i < edge_size; i++) {
//			System.out.println(i);
			// Print row number + white spaces at the beginning of each new line
			System.out.print(i + s0);
			print_white_space(i);

			// Loop columns
			for (int j=0; j < edge_size; j++) {
				print_player(i, j);
			}

			// Print diagonal lines between rows
			if (i < edge_size - 1) {
				print_white_space(i);
				print_lines(i);
			}
		}
	}

	
	
	// It adds a coordinate to a set
	private void add_pair(Set<CoordBoard> all_coords, CoordBoard coord) {
		all_coords.add(coord);
	}

	// Resize vectors to save nodes and edges
	protected void resize_storage() {
		val_nodes.setSize(edge_size * edge_size);
//		val_edges.resize(n_nodes, vector<double>(n_nodes));
//		g.resize(n_nodes, vector<bool>(n_nodes));
	}

	
	// Function to create the graph and initialize it to 0
	protected void make_graph() {

		for (int i = 0; i < edge_size * edge_size; i++) {
			val_nodes.add(i, 0);
		}

	}
	

	// Auxiliary function to print the hex graph. It prints the corresponding
	// mark assigned to each player.
	private void print_player(int i, int j) {

		int pos = CoordBoard.get_pos(i, j, edge_size);
		
		int player_in_node = val_nodes.get(pos);

		if (player_in_node == 0) {
			System.out.print(NONE);
		} else if (player_in_node == 1 || player_in_node == 2) {
			System.out.print(PLAYER_MARKS[player_in_node-1]);
		}

		if (j < edge_size - 1) {
			System.out.print(H_FULL);
		} else {
			System.out.print('\n');
		}

	}

	// Auxiliary function to print white spaces, to make look the graph more like
	// the traditional hex board. It prints white spaces as the number of the row
	// that precedes it
	private void print_white_space(int i) {
		int count = 0;
		while (count <= i) {
			System.out.print(W_SPACE);
			count++;
		}
	}

	// Auxiliary function to print the diagonal lines for the hex graph after
	// each row...
	private void print_lines(int i) {
		// first line;
		String s = to_string(new char[] {W_SPACE, W_SPACE, W_SPACE, DIAGS[1], W_SPACE});
		System.out.print(s);
		for (int count=0; count < edge_size-1; count++) {
			for (char val : DIAGS) {
				String d_lines = to_string(new char [] {val, W_SPACE});
				System.out.print(d_lines);
			}
		}
		System.out.print('\n');
	}
	
	private String to_string(char [] cs) {
		StringBuilder s = new StringBuilder().append("");
		for (char c : cs) {
			s.append(c);
		}
		return s.toString();
	}
	
	private void clearScreen() {
//		System.out.println("\033[H\033[2J");
//		System.out.flush();
		try {
			final String os = System.getProperty("os.name");
			
			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}