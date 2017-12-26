
/** 
 * Name:  Zhaoyi Huang
 * Login: cs8bwaic
 * Date:  January 26, 2016
 * File:  Board.java
 * Sources of Help: Piazza CSE 8B,TA
 * Make Board works in this file.
 * */
import java.util.*;
import java.io.*;
/** 
 * This file is to create a board in 2048 with scores and grid used for 2048
 * game. The number generated in the grid will be random according to the
 * Random seed we create.
 * */
public class Board {
	public final int NUM_START_TILES = 2;
	public final int TWO_PROBABILITY = 90;
	public int GRID_SIZE;

	private final Random random;
	private int[][] grid;
	private int score;

	// TODO PSA3
	// Constructs a fresh board with random tiles
	/**
	 * Constructor of the board when passing in a random and board size
	 * 
	 * @param random   the random number range we passed in
	 * @param boardSize     the size of the board
	 */
	public Board(Random random, int boardSize) {
		// initializing the grids, and check the size of input board size
		
		this.random = random;
		this.GRID_SIZE = boardSize;
		this.grid = new int[GRID_SIZE][GRID_SIZE];
		this.score = 0;
		// add tile to the board
		for (int i = 0; i < NUM_START_TILES; i++) {
			this.addRandomTile();
		}

	}

	// TODO PSA3
	// Construct a board based off of an input file
	/**
	 * Constructor of the board when passing in a random and file inputBoard
	 * 
	 * @param random  the random number range we passed in
	 * @param inputBoard       the file of board we passed in
	 */
	public Board(Random random, String inputBoard) throws IOException {
		// check inputing board is not empty string
		if (inputBoard.equals("")) {
			throw new IllegalArgumentException("don't use empty string");
		}
		this.random = random;
		File file = new File(inputBoard);
		// check the file is a board
		if (!file.toString().endsWith(".board")) {
			throw new IllegalArgumentException("Put in valid file!");
		}
		Scanner in = new Scanner(file);
		GRID_SIZE = in.nextInt();
		score = in.nextInt();
		this.grid = new int[GRID_SIZE][GRID_SIZE];
		// inputting numbers into the grid
		for (int i = 0; i < this.GRID_SIZE; i++) {
			for (int j = 0; j < this.GRID_SIZE; j++) {
				grid[i][j] = in.nextInt();
			}
		}
		in.close();
	}

	// TODO PSA3
	// Saves the current board to a file
	/**
	 * Method to save the board we created
	 * 
	 * @param outputBoard  the file we want to save the board in
	 */
	public void saveBoard(String outputBoard) throws IOException {
		// initilizing the output board
		File output = new File(outputBoard);
		PrintWriter writer = new PrintWriter(output);
		writer.print(GRID_SIZE);
		writer.println();
		writer.print(this.getScore());
		writer.println();
		// put numbers into the output grid
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				writer.print(grid[i][j] + " ");
				if (j == GRID_SIZE - 1) {
					writer.println();
				}
			}
		}
		writer.close();
	}

	// TODO PSA3
	// Adds a random tile (of value 2 or 4) to a
	// random empty space on the board
	/**
	 * Method to add random tile to the board
	 */
	public void addRandomTile() {
		int count = 0;
		// find the empty space in grid and count them
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 0) {
					count++;
				}
			}
		}
		// if there is empty space, find the count and get random number
		if (count != 0) {
			// find the random based on count
			int a = random.nextInt(count);
			// find the random in range 0 to 99
			int b = random.nextInt(100);
			if (b < TWO_PROBABILITY) {
				b = 2;
			} else {
				b = 4;
			}
			// add the random number b into the empty space of grid
			for (int x = 0, countOfemp = 0; x < GRID_SIZE; x++) {
				for (int y = 0; y < GRID_SIZE; y++) {
					if (grid[x][y] == 0)
						countOfemp++;
					if (countOfemp == a + 1) {
						grid[x][y] = b;
						return;
					}
				}
			}
		}

	}

	// TODO PSA3
	// Flip the board horizontally or vertically,
	// Rotate the board by 90 degrees clockwise or counter-clockwise.

	/**
	 * Method to flip the board according to input
	 * 
	 * @param random
	 *            the random number range we passed in
	 */
	public void flip(int change) {
		int i = 0, j = 0;
		// put in the grids
		int[][] grid1 = this.getGrid();
		// flip the board horizontally
		if (change == 1) {
			for (i = 0; i < grid1.length; i++) {
				for (j = 0; j < grid1[i].length / 2; j++) {
					// swamp the value in grid
					int temp = grid1[i][j];
					grid1[i][j] = grid1[i][grid1.length - 1 - j];
					grid1[i][grid1.length - 1 - j] = temp;
				}
			}
		} // flip the board vertically
		if (change == 2) {
			for (i = 0; i < grid1.length / 2; i++) {
				for (j = 0; j < grid1[i].length; j++) {
					// swamp the value in grid
					int temp = grid1[i][j];
					grid1[i][j] = grid1[grid1.length - 1 - i][j];
					grid1[grid1.length - 1 - i][j] = temp;
				}
			}
		} // flip the board clockwise
		if (change == 3) {
			int n = grid.length;
			for (i = 0; i < grid1.length; i++) {
				for (j = i; j < grid1.length - i - 1; j++) {
					// swamp the value in grid
					int temp = grid1[i][j];
					grid1[i][j] = grid[n - j - 1][i];
					grid1[n - j - 1][i] = grid1[n - i - 1][n - j - 1];
					grid1[n - i - 1][n - j - 1] = grid1[j][n - i - 1];
					grid1[j][n - i - 1] = temp;

				}
			}

		} // flip the board anticlockwise
		if (change == 4) {
			int n = grid1.length;
			for (i = 0; i < n; i++) {
				for (j = i; j < n - i - 1; j++) {
					// swamp the value in the grid
					int temp = grid1[i][j];
					grid1[i][j] = grid1[j][n - i - 1];
					grid1[j][n - i - 1] = grid1[n - i - 1][n - j - 1];
					grid1[n - i - 1][n - j - 1] = grid1[n - j - 1][i];
					grid1[n - j - 1][i] = temp;
				}
			}
		}
		// flip the board diagnotically
		if (change == 5) {
			for (i = 0; i < grid.length - 1; i++) {
				for (j = 0; j < grid.length - 1; j++) {
					// swamp the value in the grid
					int temp = grid[i][j];
					grid[i][j] = grid[grid.length - 1 - j][grid.length - 1 - i];
					grid[grid.length - 1 - j][grid.length - 1 - i] = temp;
				}
			}
		}

	}

	// Complete this method ONLY if you want to attempt at getting the extra
	// credit
	// Returns true if the file to be read is in the correct format,
	// else return false

	// Complete this method ONLY if you want to attempt at getting the extra
	// credit
	// Returns true if the file to be read is in the correct format,
	// else return false
	/**
	 * Method to check the input file is correctly formated
	 * 
	 * @param inputFile
	 *            the input file of board
	 */
	public static boolean isInputFileCorrectFormat(String inputFile) {
		// The try and catch block are used to handle any exceptions
		// Do not worry about the details, just write
		// all your conditions inside the
		// try block
		try {
			// get the content from the board
			Scanner in = new Scanner(new File(inputFile));
			int GS = in.nextInt();
			// check the second line to be the score value
			String Ts = in.nextLine();
			int Tscore = Integer.parseInt(Ts);
			// get the grid
			int[][] Tg = new int[GS][GS];
			// if the score is not a multiple of 4
			if (Tscore % 4 != 0) {
				System.out.println("Wrong score format");
				in.close();
				return false;
			}
			// if grid size is not the multiple of 4
			if (GS < 2) {
				System.out.println("Wrong size!");
				in.close();
				return false;
			}
			int coun = 0;
			// get content from the grid
			for (int i = 0; i < GS; i++) {
				for (int j = 0; j < GS; j++) {
					Tg[i][j] = in.nextInt();
					coun++;
				}
			}
			if (coun < (GS * GS)) {
				System.out.println("not enough integer in the grid!");
				in.close();
				return false;
			}
			in.close();
			// check the numbers in the grid is right and the grid is
			// in correct format
			int count0 = 0;
			for (int i = 0; i < GS; i++) {
				for (int j = 0; j < GS; j++) {
					if (Tg[i][j] < 0) {
						System.out.println(Tg[i][j] + "is negative number!");
						return false;
					} else if (Tg[i][j] == 0) {
						count0++;
					} else if (Tg[i][j] > 0) {
						if (((Tg[i][j] & -Tg[i][j]) != Tg[i][j])) {
							System.out.println(Tg[i][j] + "is not legit");
							return false;
						}
					}
				}
			}
			// check the numbers in the grid.
			if (count0 > (GS * GS - 2)) {
				System.out.println("The grid should at least has 2 numbers");
				return false;
			}

			// write your code to check for all conditions
			// and return true if it satisfies
			// all conditions else return false
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method to swap the numbers with empty space.
	 * 
	 * @param direction   the direction input
	 */
	public void swmp(Direction direction) {
		int G = GRID_SIZE;
		// using switch for different cases
		switch (direction) {
		case RIGHT:
			// loop through the grid and find empty space
			for (int i = 0; i < 2; i++) {
				for (int x = 0; x < GRID_SIZE; x++) {
					for (int y = G - 1, index = G - 1; y >= 0; y--) {
						// swap number with empty space
						if (grid[x][y] != 0) {
							int temp = grid[x][y];
							grid[x][y] = grid[x][index];
							grid[x][index] = temp;
							index--;
						}
					}
				}
			}
			break;
		case LEFT:
			// repeat the same thing,loop through grid, swap the space
			for (int i = 0; i < 2; i++) {
				for (int x = 0; x < GRID_SIZE; x++) {
					for (int y = 0, index = 0; y < GRID_SIZE; y++) {
						if (grid[x][y] != 0) {
							int temp = grid[x][y];
							grid[x][y] = grid[x][index];
							grid[x][index] = temp;
							index++;
						}
					}
				}
			}
			break;
		case UP:
			for (int y = 0; y < GRID_SIZE; y++) {
				for (int x = 0, index = 0; x < GRID_SIZE; x++) {
					if (grid[x][y] != 0) {
						int temp = grid[x][y];
						grid[x][y] = grid[index][y];
						grid[index][y] = temp;
						index++;
					}
				}
			}
			break;
		case DOWN:
			// loop through the grid and swap the numbers
			for (int y = 0; y < GRID_SIZE; y++) {
				for (int x = G - 1, index = G - 1; x >= 0; x--) {
					if (grid[x][y] != 0) {
						int temp = grid[x][y];
						grid[x][y] = grid[index][y];
						grid[index][y] = temp;
						index--;
					}
				}
			}
			break;
		}
	}

	// No need to change this for PSA3
	// Performs a move Operation
	/**
	 * Method to move numbers on the grid,filling the empty space also check
	 * the addition of numbers
	 * 
	 * @param direction   the direction of moving
	 */
	public void moveAddition(Direction direction) {

		if (direction == Direction.RIGHT) {
			// first swap the numbers with empty space
			this.swmp(Direction.RIGHT);
			// then check addition, if so add something
			for (int x = 0; x < GRID_SIZE; x++) {
				for (int y = GRID_SIZE - 1; y > 0; y--) {
					if (grid[x][y] == grid[x][y - 1]) {
						grid[x][y] *= 2;
						grid[x][y - 1] = 0;

						// add up the score if addition happen
						score += grid[x][y];
					}
				}
			}
			// swap again due to change in empty space
			this.swmp(Direction.RIGHT);
		}

		if (direction == Direction.LEFT) {
			// do the swap
			this.swmp(Direction.LEFT);
			// check addition
			for (int x = 0; x < GRID_SIZE; x++) {
				for (int y = 0; y < GRID_SIZE - 1; y++) {
					if (grid[x][y] == grid[x][y + 1]) {
						grid[x][y] *= 2;
						grid[x][y + 1] = 0;

						// add up the score
						score += grid[x][y];
					}
				}
			}
			this.swmp(Direction.LEFT);
		}

		if (direction == Direction.UP) {
			this.swmp(Direction.UP);
			// check addition
			for (int y = 0; y < GRID_SIZE; y++) {
				for (int x = 0; x < GRID_SIZE - 1; x++) {
					if (grid[x][y] == grid[x + 1][y]) {
						grid[x][y] *= 2;
						grid[x + 1][y] = 0;

						// add up the score if addition happen
						score += grid[x][y];
					}
				}
			}
			this.swmp(Direction.UP);
		}

		if (direction == Direction.DOWN) {
			this.swmp(Direction.DOWN);
			// for each column, if two adjacent tiles are of same value
			// double the lower one and leave the other as zero
			for (int y = 0; y < GRID_SIZE; y++) {
				for (int x = GRID_SIZE - 1; x > 0; x--) {
					if (grid[x][y] == grid[x - 1][y]) {
						grid[x][y] *= 2;
						grid[x - 1][y] = 0;

						// add up the score
						score += grid[x][y];
					}
				}
			}
			this.swmp(Direction.DOWN);
		}
	}

	/**
	 * Method to actually move numbers in 2048 grids
	 * 
	 * @param direction   the direction moving
	 * @throws IOException
	 */
	public boolean move(Direction direction) throws IOException {
		// save board on a file called undo.board for undo()method
		this.saveBoard("undo.board");
		// if the numbers can move then move the numbers
		if (canMove(direction) == true) {
			this.moveAddition(direction);
			return true;
		}
		return false;
	}

	/**
	 * Method to determine whether the number can really move
	 * 
	 * @param direction    the direction of moving
	 */
	public boolean canMove(Direction direction) {
		boolean result = false;
		// using switch to call each help method and check movement
		switch (direction) {
		case UP:
			result = this.canMoveUp();
			break;
		case DOWN:
			result = this.canMoveDown();
			break;
		case LEFT:
			result = this.canMoveLeft();
			break;
		case RIGHT:
			result = this.canMoveRight();
			break;
		}
		return result;

	}

	/**
	 * Method to check addition of near by numbers by addition is true
	 * 
	 * @param i the condition code for different addition,1 for left or right
	 *  0 for up and down
	 */
	public boolean nearbyAddition(int i) {
		boolean add = false;
		// check case for left and right addition
		if (i == 1) {
			for (int x = 0; x < GRID_SIZE; x++) {
				for (int y = GRID_SIZE - 1; y > 0; y--) {
					if (grid[x][y] == grid[x][y - 1] && grid[x][y] != 0)
						add = true;
				}
			}
		}
		// check case for up and down addition
		if (i == 0) {
			for (int y = 0; y < GRID_SIZE; y++) {
				for (int x = GRID_SIZE - 1; x > 0; x--) {
					if (grid[x][y] == grid[x - 1][y] && grid[x][y] != 0)
						add = true;
				}
			}
		}
		return add;
	}

	/**
	 * Method to check the numbers can move right
	 */
	public boolean canMoveRight() {
		// loop through the grid
		
		for (int x = 0; x < GRID_SIZE; x++) {
		      int countOfEmptySpace = 0;

		      // obtain the count of empty space of the xth row
		      for (int y = 0; y < GRID_SIZE; y++) {
		        if (grid[x][y] == 0)
		          countOfEmptySpace++;
		      }

		      // check if all the empty space is on the left side
		      for (int y = 0; y < countOfEmptySpace; y++) {
		        if (grid[x][y] != 0)
		          return true;
		      }
		}
		// if near by numbers can add always return true
		if (this.nearbyAddition(1) == true) {
			return true;
		}
		// otherwise return false
		return false;
	}

	/**
	 * Method to check the number can move left
	 */
	public boolean canMoveLeft() {
		for (int x = 0; x < GRID_SIZE; x++) {
		      int countOfEmptySpace = 0;

		      // obtain the count of empty space in a row
		      for (int y = 0; y < GRID_SIZE; y++) {
		        if (grid[x][y] == 0)
		          countOfEmptySpace++;
		      }

		      // check if all the empty space is on the right side
		      for (int y = GRID_SIZE-1; y > GRID_SIZE-countOfEmptySpace-1;  y--) {
		        if (grid[x][y] != 0)
		          return true;
		      }
		}
		// true condition if nearby numbers can add up
		if (this.nearbyAddition(1) == true) {
			return true;
		}
		return false;
	}

	/**
	 * Method to check the number can move up
	 */
	public boolean canMoveUp() {
		for (int y = 0; y < GRID_SIZE; y++) {
		      int countOfEmptySpace = 0;

		      // obtain the count of empty space within a column
		      for (int x = 0; x < GRID_SIZE; x++) {
		        if (grid[x][y] == 0)
		          countOfEmptySpace++;
		      }

		      // check if all the empty space is on the bottom of a column
		      for (int x = GRID_SIZE-1; x > GRID_SIZE-countOfEmptySpace-1; x--) {
		        if (grid[x][y] != 0)
		          return true;
		      }
		}
		if (this.nearbyAddition(0) == true) {
			return true;
		}
		return false;
	}

	/**
	 * Method to check the number can move down
	 */
	public boolean canMoveDown() {
		// check if there are empty space between two tiles within a column
	    for (int y = 0; y < GRID_SIZE; y++) {
	      int countOfEmptySpace = 0;

	      // obtain the count of empty space in a column
	      for (int x = 0; x < GRID_SIZE; x++) {
	        if (grid[x][y] == 0)
	          countOfEmptySpace++;
	      }

	      // check if all the empty space is on the top of a column
	      for (int x = 0; x < countOfEmptySpace; x++) {
	        if (grid[x][y] != 0)
	          return true;
	      }
	}
		if (this.nearbyAddition(0) == true) {
			return true;
		}
		return false;
	}

	/**
	 * Method to expand the whole grid
	 */
	public void expand() {
		int G = GRID_SIZE + 1;
		// create replacing array
		int[][] replacement = new int[G][G];
		for (int x = 0; x < GRID_SIZE; x++) {
			for (int y = 0; y < GRID_SIZE; y++) {
				replacement[x][y] = grid[x][y];
			}
		}
		// replace the grid and grid size
		GRID_SIZE = G;
		grid = replacement;
	}

	/**
	 * Method to undo the movement
	 * 
	 * @throws IOException
	 */
	public void undo() throws IOException {
		// using the exception to handle the files
		try {
			Board old = new Board(random, "undo.board");
			// case when undo.board already contain the file from last game
			if (old.getScore() > this.getScore()) {
				return;
			}
			// case for undoing the expansion
			if (old.getGrid().length < this.getGrid().length) {
				this.grid = old.getGrid();
			}
			// return the result of undo
			this.score = old.getScore();
			this.grid = old.getGrid();
		}
		// create files when it doesn't exist,like playing the game for
		// the first time
		catch (FileNotFoundException e) {
			new FileOutputStream("undo.board", true).close();
		}

	}

	/**
	 * Method to check the game is over or not
	 */
	public boolean isGameOver() {
		boolean up = this.canMoveUp();
		boolean down = this.canMoveDown();
		boolean left = this.canMoveLeft();
		boolean right = this.canMoveRight();

		// if cannot move in all four direction, then game is over
		if (!(up || down || left || right)) {
			return true;
		} else
			return false;
	}

	// Return the reference to the 2048 Grid
	public int[][] getGrid() {
		return grid;
	}

	// Return the score
	public int getScore() {
		return score;
	}

	@Override
	public String toString() {
		StringBuilder outputString = new StringBuilder();
		outputString.append(String.format("Score: %d\n", score));
		for (int row = 0; row < GRID_SIZE; row++) {
			for (int column = 0; column < GRID_SIZE; column++)
				outputString.append(grid[row][column] == 0 ? "    -" : String.
						format("%5d", grid[row][column]));

			outputString.append("\n");
		}
		return outputString.toString();
	}
}
