package game.user;

import java.util.Scanner;



/**
 * Class used to manipulate the inputs of a Hex game.
 * 
 * @author Carlos Granados
 * @version 1.0, 02.03.2021
 *
 */
public class InterfaceIO
{
	
	// Attributes
	private static Scanner inputs;
	
	private static String REGEX = "\\s*,[,\\s]*";
	
	/**
	 * Method to initialize the System Scanner. Must be called at the
	 * beginning of the game.
	 * 
	 */
	public static void start() {
		inputs = new Scanner(System.in);
	}
	
	/**
	 * Method to read a single integer
	 * 
	 * @return		Readed integer
	 */
	public static int read_input_i() {
		int i = inputs.nextInt();
		return i;
	}

	/**
	 * Method to read several integers
	 * @return		Readed integers in an array
	 */
	public static int[] read_input_coord() {
		
		String s = inputs.nextLine();
		String [] s_ins = s.split(REGEX);

		int [] vals = new int[s_ins.length];

		int indx = 0;
		for (String si : s_ins) {
			vals[indx] = Integer.parseInt(si);
			indx++;
		}

		return vals;
	}
	
	/**
	 * Method to close the System Scanner. Must be called at the end of the program.
	 * 
	 */
	public static void close() {
		inputs.close();
	}
	
}
