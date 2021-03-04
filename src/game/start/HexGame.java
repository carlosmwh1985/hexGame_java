package game.start;
import game.user.*;

/**
 * Java code to implement the Hex game, using Graphs to build the game board.
 * 
 * @author Carlos Granados
 * @version 1.1, 03.03.2021
 *
 */
public class HexGame {

	public static void main(String [] args) {
		
		System.out.println("Easy implementation of Hex game.");
		System.out.println("Select: ");
		
		InterfaceIO.start();			// To start reader
		while (true) {
			System.out.println("1. Check one player ");
			System.out.println("2. Play with two human players ");
			System.out.println("3. Several random matches.");
			System.out.println("4. Exit");
			int sel = InterfaceIO.read_input_i();
			
			if (sel == 1) {
				System.out.println("Enter board size:");
				int b_size = InterfaceIO.read_input_i();
				SimPlay.verify_one(b_size);
				break;
			} else if (sel == 2){
				System.out.println("Enter board size:");
				int b_size = InterfaceIO.read_input_i();
				SimPlay.two_players(b_size);
				break;
			} else if (sel == 3) {
				SimPlay sim0 = new SimPlay();
				SimPlay sim1 = new SimPlay();
				SimPlay sim2 = new SimPlay();
				SimPlay sim3 = new SimPlay();
		
				sim0.two_players_rnd(5, 10);
				sim1.two_players_rnd(5, 10000);

				sim2.two_players_rnd(11, 10);
				sim3.two_players_rnd(11, 10000);
				break;
			} else if (sel == 4) {
				break;
			}
		}
		InterfaceIO.close();
		
		System.out.println("Program Finished. Have a nice day!");
		
	}
}
