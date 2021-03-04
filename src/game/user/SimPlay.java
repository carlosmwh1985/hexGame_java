package game.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import game.board.*;

import java.io.FileWriter;
import java.io.IOException;



/**
 * Class used to simulated different conditions on the Hex game. Different
 * methods simulate different conditions: one player (test winning conditions),
 * two human players (to test winning and input conditions) and two "dumb"
 * players, using random moves.
 * 
 * @author Carlos Granados
 * @version 1.1, 03.03.2021
 *
 */
public class SimPlay
{

	// Attributes
	private int [] count_wins = new int[2];
	private int [][] count_ij;
	private ArrayList<CoordBoard> moves;

	private String endl = new String("\n");
	private String tabl = new String("\t");
	
	// Static Methods
	
	/**
	 * Static method to verify that the CoordBoard Class is working as desired
	 * 
	 * @param board_size	The size of the Hex board
	 */
	public static void verify_coords(int board_size) {
		
		CoordBoard p0 = new CoordBoard(3, board_size-2);
		CoordBoard p1 = new CoordBoard(1, board_size-2);
		CoordBoard p2 = new CoordBoard(3, board_size-2);
		
		System.out.println("p0: " + p0);
		System.out.println("p1: " + p1);
		System.out.println("p2: " + p2);
		
		System.out.println("p0 == p1 ? " + p0.equals(p1));
		System.out.println("p0 == p2 ? " + p0.equals(p2));
		
		// Checking conversion
		int pos = p0.get_pos(board_size);
		
		System.out.println("Checking coord. conversion for Coord " + p0);
		System.out.println("Abs. pos: " + pos);
		CoordBoard p = CoordBoard.get_coord(pos, board_size);
		System.out.println("Associated coord: " + p + "... Is it ok?");
		
	}
	
	/**
	 * Static method to check if the Player class can verify when a player
	 * wins the match.
	 *  
	 * @param board_size	The size of the Hex board
	 */
	public static void verify_one(int board_size) {

		Player player_one = new Player(1, board_size);
		HexBoard hb0 = new HexBoard(board_size);
		
		// Small code to test the conditions to win...
		for (int i = 0; i < board_size; i++) {
//		    if (i == 3)
//		    	continue;
		    CoordBoard p = new CoordBoard(i, 2);
		    System.out.println("Playing pos: " + p);
		    player_one.set_pos(p, hb0);
		    hb0.set_move(player_one.get_player(), player_one.get_pos());
		}
		
		hb0.print();
		System.out.println("Verifying who won");
		if (player_one.player_won(hb0)) {
			System.out.println("Player Won!");
		}

	}
	
	/**
	 * Static method to run the code for two (human) players. It is used to test
	 * the winning conditions, in the presence of two players and the input
	 * of the coordinates of each player
	 * 
	 * @param board_size	The size of the Hex board
	 */
	public static void two_players(int board_size) {

		int i = 0, j = 0;

		Player [] players = {new Player(1, board_size), new Player(2, board_size)};
		HexBoard hb0 = new HexBoard(board_size);

		// Initialize graph and print empty board
		hb0.print();

		// Start playing
		int count = 0;
		boolean setted;
		while (i != -1 && j != -1) {
			int id_current = count % 2;
		    int id_other = (count + 1) % 2;
		    // Set the coordinated for a new move. If not, exit loop
		    setted = (players[id_current]).set_coords(hb0, players[id_other]);
		    if (! setted)
		      break;
		    // If it is a valid move, then add to the graph and show it
		    hb0.set_move(players[id_current].get_player(), players[id_current].get_pos());
		    hb0.print();
		    if (players[id_current].player_won(hb0)) {
		      System.out.println("Player " + (id_current + 1) + " wins!");
		      break;
		    }
		    count++;
		  }
		
	}
	
	
	// Random play... Non-Static
	
	/**
	 * Method used to generate 2 "dumb" players, which will use random generated
	 * moves in the board. At the end it generated a small statistic of the winning
	 * moves, mainly of the 1st moves. All results are shown in the screen and
	 * written down to a file.
	 * 
	 * @param board_size	The size of the Hex board
	 * @param num_sim		Number of simulations to perform
	 */
	public void two_players_rnd(int board_size, int num_sim) {

		count_ij = new int[4][board_size];

		for (int i = 0; i < num_sim; i++) {

			// Initialize players and graph. Objects for this scope only
			Player [] players = {new Player(1, board_size), new Player(2, board_size)};
			HexBoard hb = new HexBoard(board_size);

			// Get vector with all possible moves, randomize
			rnd_moves(board_size);
		
			
			// Simulate one match, with the random generated moves
			full_play(players, hb);

			// To determine who won the match and save the initial moves
			save_winner(players, hb);

		}

		// Display statistics
		show_stats(board_size, num_sim);

	}


	// Function to simulate a full match, using the random generated moves
	private void full_play(Player[] players, HexBoard hb) {
		// Start playing
		int count = 0;
		while (count < moves.size()) {
			int id_current = count % 2;
//			int id_other = (count + 1) % 2;
			// Get coordinates for the next move
			CoordBoard p = moves.get(count);
			// Set move (assumed valid) and set it in the graph
			(players[id_current]).set_pos(p, hb);
			hb.set_move(players[id_current].get_player(), players[id_current].get_pos());
			count++;
		}
	}



	// Function to generate a vector of random moves!
	private void rnd_moves(int board_size) {

		moves = new ArrayList<CoordBoard>();

		// Generate all possible positions in the board
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				CoordBoard p = new CoordBoard(i, j);
				moves.add(p);
			}
		}

		// obtain a time-based seed:
		Calendar cal = Calendar.getInstance();
		long seed = cal.getTimeInMillis();
		Random rnd = new Random(seed);

		// Shuffle the elements in moves
		Collections.shuffle(moves, rnd);

	}



	// Function to save the results after each simulated match
	private void save_winner(Player [] players, HexBoard hb) {

		int id;

		if (players[0].player_won(hb)) {
			id = 0;
		} else if (players[1].player_won(hb)) {
			id = 1;
		} else{
			System.out.println("A draw!");
			return;
		}

		count_wins[id]++;
		CoordBoard p_first = players[id].get_first();

		// To do some statistics. Save the winner 1st movement...
		// Row, AKA i
		count_ij[2*id][p_first.first()]++;
		// Column, AKA j
		count_ij[2*id+1][p_first.second()]++;

	}
	
	// Function to display stats, both in screen and into a file
	private void show_stats(int board_size, int num_sim) {

		String file_name = new String("Simulation_statistics_n_" + num_sim + "_board_" + board_size + ".dat");
		
		// To buffer all output stream, to have exactly the same Info. on screen and in the file
		String buffer = new String();

		// Results
		buffer += "Some statistics:" + endl + endl;

		buffer += "Number of matches : " + num_sim + endl;
		buffer += "Board size        : " + board_size + endl + endl;

		buffer += "1st Player won " + count_wins[0] + " times. Ratio : " + (1.0 * count_wins[0] / num_sim) + endl;
		buffer += "2nd Player won " + count_wins[1] + " times. Ratio : " + (1.0 * count_wins[1] / num_sim) + endl;
		buffer += endl;

		buffer += "Winning first moves:" + endl + endl;

		// head
		int[] indx = new int[board_size];
		for (int i = 0; i < board_size; i++)
			indx[i] = i;

		for (int i = 0; i < 2; i++) {
			buffer += "Player " + i + 1 + endl;
			buffer += print_table(' ', indx);
			buffer += print_table('i', 2*i);
			buffer += print_table('j', 2*i+1);
			buffer += endl;
		}

		// Print buffer in the screen
		System.out.println(buffer);
		
		// Print buffer into the file
		try {
			FileWriter file = new FileWriter(file_name);
			file.write(buffer);
			file.close();
		} catch (IOException e) {
			System.out.println("An error ocurring with file "+ file_name);
			e.printStackTrace();
		}

	}



	// Function to print a vector with a table format...
	private String print_table(char head, int row) {
		int[] temp = count_ij[row];
		return print_table(head, temp);
	}

	private String print_table(char head, int[] vec) {
		String buffer = new String();
		buffer += head + tabl;
		for (int i : vec)
			buffer += i + tabl;
		buffer += endl;
		return buffer;
	}

	
}