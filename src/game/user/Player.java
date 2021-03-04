package game.user;

import java.util.*;

import game.board.*;



/**
 * Implementation of class Player. Moved from C++ to Java
 * 
 * @author Carlos Granados
 * @version 1.1, 02.03.2021
 */
public class Player
{

	// Class' Attributes
	
	private int self_id;
	private int board_size;
	
	// To save played moves
	private Set<CoordBoard> pos_marks = new HashSet<CoordBoard>();
	private CoordBoard first_move = new CoordBoard(-1, -1);
	private CoordBoard last_move;

	// Used to define and check played moves on edges: N-S or W-E
	private HashSet<CoordBoard> played_starts = new HashSet<CoordBoard>();
	private Set<CoordBoard> checked  = new HashSet<CoordBoard>();

	private boolean start_played = false;
	private boolean end_played = false;
	
	// Used to verify the visited nodes
	PriorityQueue<CoordBoard> visited = new PriorityQueue<CoordBoard>();
	
	// Constructors
	
	/**
	 * Create a void player, with ID = 0
	 * 
	 */
	public Player() {
		this.self_id = 0;
	}

	/**
	 * Create a player with the given ID, and it assumes a Hex board of size 5
	 * 
	 * @param i		int, player ID
	 */
	public Player(int i) {
		this.self_id = i;
		this.board_size = 5;
	}

	/**
	 * Create a player with the given ID, and a Hex board of size b_size
	 * 
	 * @param i			int, player ID
	 * @param b_size	int, board size
	 */
	public Player(int i, int b_size) {
		this.self_id = i;
		this.board_size = b_size;
	}
	
	// Methods

	// Setters and getters

	/**
	 * Returns first move made by player
	 * 
	 * @return first_move 	CoordBoard, 1st move by player
	 */
	public CoordBoard get_first() {
		return first_move;
	}
	
	/**
	 * Returns the last played position
	 * 
	 * @return first_move 	CoordBoard, last move made by player
	 */
	public CoordBoard get_pos() {
		return last_move;
	}

	/**
	 * Returns the player ID
	 * 
	 * @return id			int, player ID
	 */
	public int get_player() {
		return self_id;
	}
	

	
	/**
	 * Function to set a played position in a given hex board
	 * 
	 * @param move			CoordBoard, the move made by player
	 * @param hb			HexBoard, the board where the match is played
	 */
	public void set_pos(CoordBoard move, HexBoard hb) {
		pos_marks.add(move);
		last_move = move;
		
		int ref_indx = ref_pos(move);
		
		// Check if the move is in one of the edges
		if (ref_indx == 0) {
			played_starts.add(move);
			if (!start_played) {
				start_played = true;
			}
		}
		if (!end_played && ref_indx == board_size - 1) {
			end_played = true;
		}
		// Save 1st move
		if (first_move.first() == -1) {
			first_move = move;
		}
	}
	
	/**
	 * Function to set a played position in a given hex board
	 * 
	 * @param i		int, the row of the move made by player
	 * @param j 	int, the col of the move made by player
	 * @param hb	HexBoard, the board where the match is played
	 */
	public void set_pos(int i, int j, HexBoard hb) {
		set_pos(new CoordBoard(i, j), hb);
	}

	/**
	 * Function to read from the screen the next move for current player.
	 * If valid, save the move. If not, ask again for the coordinates.
	 * If (-1, -1) is given, false is return, and the game is finished.
	 * 
	 * @param hb			HexBoard, the board where the match is played
	 * @param other_player	Player, the other player in the game
	 * @return 				<code>true<\code> when valid coordinates were
	 * 						given
	 */
	public boolean set_coords(HexBoard hb, Player other_player) {
		
		int [] new_ij;
		
		// Get Coords
		System.out.println("Player " + self_id + " move (-1, -1 to exit):");
		new_ij = InterfaceIO.read_input_coord();

		// Exit program
		if (new_ij[0] == -1 && new_ij[1] == -1)
			return false;

		CoordBoard move = hb.check_coords(new_ij);

		boolean valid = valid_move(move, other_player);

		// If not a valid move, call again
		if (valid) {
			set_pos(move, hb);
		} else {
			System.out.println("Not a valid move. Try again!");
			set_coords(hb, other_player);
		}

		return true;
	}



	// Other methods

	/**
	 * Check if the new move is valid (not played before for any player)
	 * 
	 * @param move			CoordBoard, the move made by player
	 * @param other_player	Player, the other player in the game
	 * @return 				<code>true<\code> when a move is valid (inside the
	 * 						board and not played by any of the players).
	 *  					<code>false<\code> otherwise.
	 */
	public boolean valid_move(CoordBoard move, Player other_player) {

		// Check if the other player has play it already
		boolean check_other = other_player.played(move);
		boolean check_self = played(move);

		if (check_other || check_self) {		// Played before...
			System.out.println("Move already played!");
			return false;
		} else {			 					// Not played before
			return true;
		}

	}

	/**
	 * Check if the current player already played the given move
	 * 
	 * @param move		CoordBoard, the move made by player
	 * @return 			<code>true<\code> if the current player already played
	 * 					the given move. <code>false<\code> otherwise.
	 */
	public boolean played(CoordBoard move) {
		return pos_marks.contains(move);
	}

	/**
	 * Function to determine if the current player had
	 * complete the game (won): Complete path N-S or W-E
	 * 
	 * @param hb		HexBoard, the board where the match is played
	 * @return 			<code>true<\code> if the current player has a complete
	 * 					path (i.e. if it has won the match). <code>false<\code>
	 * 					otherwise
	 */
	public boolean player_won(HexBoard hb) {
		
		// Path does not have yet any extreme points (N+S or W+E)
		if (!start_played || !end_played) {
			return false;
		}
		
		// Not enough points to make a full path...
		if (pos_marks.size() < board_size) {
			return false;
		}

		return chk_player_path(hb);
	}

	/**
	 * Function to determine if there is a complete path (N-S or W-E).
	 * It calls a modified Dijkstra algorithm, for each played node on the
	 * starting edge (N for player 1, W for player 2).
	 * 
	 * @param hb			HexBoard, the board where the match is played
	 * @return				<code>true<\code> if player has any complete path.
	 * 						<code>false<\code> otherwise.
	 */
	protected boolean chk_player_path(HexBoard hb) {

		// Starting from N or W, verify if the path is complete (walkable
		// from start to end), If it is, the path is complete.
		// A modified Dijkstra algorithm
		for (CoordBoard start : played_starts) {
			clear_visits();
			add_visits(start);
			if (hex_Dijkstra(hb)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Function to check if the path is walkable from the start to
	 * the end points (complete). Uses a modified Dijkstra algorithm
	 * 
	 * @param hb	HexBoard, the board where the match is played
	 * @return		<code>true<\code> if player has any complete path.
	 * 				<code>false<\code> otherwise.
	 */
	protected boolean hex_Dijkstra(HexBoard hb) {

		while (visited.size() > 0) {
			// Get and delete from the queue the first position
			CoordBoard pos = visited.poll();

			// Check if next node is a edge node, making it a complete path
			if (end_edge(pos)) {
				return true;
			}

			// Add all visited neighbors of pos in the queue
			add_neighbors_queue(pos, hb);
		}

		// No complete path to the ending node
		return false;
	}
	
	/**
	 * Function to count the number of visited neighbors,
	 * for a given coordinate cb in the board.
	 * It updates the PriorityQueue visited
	 * 
	 * @param pos	CoordBoard the current position in board
	 * @param hb	HexBoard, the board where the match is played
	 */
	protected void add_neighbors_queue(CoordBoard pos, HexBoard hb) {

		Set<CoordBoard> neighbors = hb.get_neighbors(pos);

		for (CoordBoard n : neighbors) {
			// If the given neighbor n is in the marked positions, add it to the queue
			if (pos_marks.contains(n) && !checked.contains(n)) {
				add_visits(n);
			}
		}
	}
	
	/**
	 * Check if the current pos is in a final edge (S or E)
	 * @param pos		CoordBoard, the current position
	 * @return 			<code>true<\code> if pos is in a final edge (S for
	 * 					player 1, E for player 2. <code>false<\code> otherwise.
	 */
	private boolean end_edge(CoordBoard pos) {
		int ref_val = ref_pos(pos);

		if (ref_val == board_size-1) {
			return true;
		}

		return false;
	}
	
	/**
	 * Clean visited queue and checked set
	 * 
	 */
	private void clear_visits() {
		visited.clear();
		checked.clear();
	}
	
	/**
	 * Add pos to PriorityQueue visited and to HashSet checked
	 * 
	 * @param pos		CoordBoard, the current position
	 */
	private void add_visits(CoordBoard pos) {
		visited.add(pos);
		checked.add(pos);
	}
	
	/**
	 * Returns the reference value for pos: row (Player 1) or col (Player 2).
	 * 
	 * @param pos		CoordBoard, the current pos
	 * @return 			The reference value for the current player (x-coordinate
	 * 					for player 1, y-coordinate for player 2)
	 */
	private int ref_pos(CoordBoard pos) {
		if (self_id == 1) {
			return pos.first();
		}
		else if(self_id == 2) {
			return pos.second();
		}
		else {
			return -1;
		}
	}
	
	
	// To make easier the manipulation by lib methods...
	
	@Override
    public String toString() {
        return "Player ID : " + self_id;
    }
	
	@Override
	public boolean equals(Object obj) {
		// Self check
		if (this == obj) {
			return true;
		}
		// Null check
		if (obj == null) {
			return false;
		}
		// Type check
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		// Cast and field comparison
		Player ply = (Player) obj;
		return Objects.equals(this.self_id, ply.self_id);
	}
	
	@Override
	public int hashCode() {
	    return self_id;
	}

}
