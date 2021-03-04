package game.board;

import java.util.Objects;



/**
 * Class to create and manipulate 2D coordinates in a given Hex board.
 * 
 * @author Carlos Granados
 * @version 1.1, 03.03.2021
 *
 */
public class CoordBoard implements Comparable<CoordBoard>
{
	
	private int x;
	private int y;
	
	/**
	 * Creates a simple coordinate <code>(i, j)<\code>
	 * 
	 * @param i		x-component
	 * @param j		y-component
	 */
	public CoordBoard(int i, int j) {
		this.x = i;
		this.y = j;
	}
	
	/**
	 * Returns the x-component of the coordinate
	 * @return		x-component
	 */
	public int first() {
		return x;
	}
	
	/**
	 * Returns the y-component of the coordinate
	 * @return		y-componet
	 */
	public int second() {
		return y;
	}

	/**
	 * Function to return, for the current Obj. coordinate, the corresponding
	 * position in a 1D array (flatten 2D position to a 1D position array)
	 * 
	 * @param board_size	The size of the considered 2D board
	 * @return				The 1D position
	 */
	public int get_pos(int board_size) {
		return x * board_size + y;
	}
	
	/**
	 * Function to return, for a given Obj. coordinate, the corresponding
	 * position in a 1D array (flatten 2D position to a 1D position array)
	 * 
	 * @param i				The x-component of the coordinate
	 * @param j				The y-component of the coordinate
	 * @param board_size	The size of the considered 2D board
	 * @return				The 1D position
	 */
	public static int get_pos(int i, int j, int board_size) {
		return i * board_size + j;
	}
	
	/**
	 * Function to return, for a given Obj. coordinate, the corresponding
	 * position in a 1D array (flatten 2D position to a 1D position array)
	 * 
	 * @param cb			The coordinate in the board
	 * @param board_size	The size of the considered 2D board
	 * @return				The 1D position
	 */
	public static int get_pos(CoordBoard cb, int board_size) {
		return get_pos(cb.first(), cb.second(), board_size);
	}
	
	/**
	 * Function to return, for a given position in a 1D array,
	 * the corresponding Obj. coordinate, for a board of given size
	 * 
	 * @param pos			1D position
	 * @param board_size	The size of the considered 2D board
	 * @return				The associated CoordBoard object 
	 */
	public static CoordBoard get_coord(int pos, int board_size) {
		int i = (int) pos / board_size;
		int j = pos % board_size;
		return new CoordBoard(i, j);
	}
	
	@Override
    public String toString() {
        return "(" + x + ", " + y + ")";
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
		CoordBoard cb = (CoordBoard) obj;
		return Objects.equals(this.x, cb.first()) && Objects.equals(this.y, cb.second());
	}
	
	@Override
	public int hashCode() {
	    return x * 1000 + y;
	}
	
	
	// Important to use in Priority Queues
	@Override
	public int compareTo(CoordBoard cb) {
		
		int i = cb.first();
		
		if (this.x > i) {
			return 1;
		} else if (this.x < i) {
			return -1;
		} else {
			return 0;
		}
		
	}
}
