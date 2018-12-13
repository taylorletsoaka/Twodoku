import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

import javax.swing.JOptionPane;

public class Twodoku {
	
	//Creates n x n grid
	static int[][] grid;
	//Letters representing numbers greater than 9
	static String[] letters =  {"A", "B","C", "D","E", "F", "G"};
	static int rows, cols, mode, max;
	static boolean pcTurn, player1Turn, player2Turn;
	
	/*
	 * Game starts here. 
	 * Game should be initialized from the command line or 'run configurations'
	 */
	public static void main(String[] args) throws InterruptedException {
	
		//Get game dimensions and mode from command line
		rows = Integer.parseInt(args[0]) * 2;
		cols = Integer.parseInt(args[1]) * 2;
		mode = Integer.parseInt(args[2]);
		max = (Integer.parseInt(args[0]));
		max = max * max;
		
		//Verify if dimensions are valid
		if (rows == cols && rows > 1){
			grid = new int[rows][cols];
			initialize(mode);
		}
		
		else{
			System.out.println("Illegal grid size.  Please use 2x2, 3x3, or 4x4.");
			System.exit(0);
		}
	}
	
	
	public static void initialize(int mode) throws InterruptedException{
		
		System.out.println("+------ Welcome to Twodoku ------+");
		//graphicsMode();
		if (mode == 0){
			singlePlayer();
		}
		else if(mode == 1){
			printGrid();
			twoPlayers();
		}
		else if (mode == 2){
			graphicsMode();
		}
		
	}
	
	private static void graphicsMode() throws InterruptedException {
		
		StdDraw.setCanvasSize(cols*100, rows*100);
		StdDraw.setXscale(0, cols);
		StdDraw.setYscale(rows, 0);
	    StdDraw.setPenColor(Color.BLACK);
	    StdDraw.enableDoubleBuffering();
	    StdDraw.show();
	    
	    Random rnd = new Random();
		int row = rnd.nextInt(rows);
		int col = rnd.nextInt(cols);
		int digit = rnd.nextInt(max) + 1;

		grid[row][col] = digit;
		
	    display();
	    singPlayerGMode();
	}

	/*
	 * GUI, display grid
	 */
	private static void display() {
		
		StdDraw.clear();
		String number;
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {

				StdDraw.setPenColor(StdDraw.DARK_GRAY);
				StdDraw.filledSquare(x + 0.5, y + 0.5, 0.495);
				if (grid[y][x] != 0){
					number = Integer.toString(grid[y][x]);
					StdDraw.setPenColor(StdDraw.CYAN);
					StdDraw.text(x + 0.5, y + 0.5, number);
				}
			}
		}
		
		StdDraw.enableDoubleBuffering();

	    StdDraw.show();	
	     	 
	}

	/*
	 * Single player mode activated when mode value is 0
	 */
	private static void singlePlayer() throws InterruptedException {
		
		Random rnd = new Random();
		int row = rnd.nextInt((rows));
		int col = rnd.nextInt(cols);
		int digit = rnd.nextInt(max) + 1;
		
		grid[row][col] = digit;
		printGrid();
		
		while (true){
			if(pcTurn)
				computer();
			else
				human();
		}
		
	}

	/*
	 * Single player graphics mode
	 * Activated in mode 2
	 */
	private static void singPlayerGMode() throws InterruptedException{
		
		
		while (true){
			
			if(pcTurn){
				Thread.sleep(2000);
				computer();
			}
			else if (StdDraw.mousePressed()){
				int position_y, position_x, digit;
				position_y = (int) StdDraw.mouseY();
				position_x = (int) StdDraw.mouseX();
				while(!StdDraw.hasNextKeyTyped()){
					
				}
				
				//Get user input
				digit = Character.getNumericValue(StdDraw.nextKeyTyped());
				
				if (digitValid(digit) && validMove(position_y, position_x, digit) && subGridValid(position_y, position_x, digit)){
					//inserts digit into grid as per user command
					grid[position_y][position_x] = digit;
					
					if(gridFull()){
						outcome();
					}
					else{
						pcTurn = true;
						player1Turn = false;
					}
				}
			}
			display();
		}
	}
	/*
	 * Two player mode activated when mode value is 1
	 */
	private static void twoPlayers() throws InterruptedException{
		
		int position_y, position_x, digit;
		Scanner input = new Scanner(System.in);
		player1Turn = true;
		
		while(true){
			
			if(player1Turn){
				
				System.out.print("Enter your move:\nPlayer 1 > ");
				
				//Get user input
				position_y = Integer.parseInt(input.next()) - 1;
				position_x = Integer.parseInt(input.next()) - 1;
				digit = Integer.parseInt(input.next());
				
				if (digitValid(digit) && validMove(position_y, position_x, digit) && subGridValid(position_y, position_x, digit)){
					//inserts digit into grid as per user command
					grid[position_y][position_x] = digit;
					
					if(gridFull() || noPossibleMove()){
						outcome();
					}
					else{
						player1Turn = false;
						player2Turn = true;
						printGrid();
					}
					
				}
			}
			else{
				System.out.print("Enter your move:\nPlayer 2 > ");
				
				//Get user input
				position_y = Integer.parseInt(input.next()) - 1;
				position_x = Integer.parseInt(input.next()) - 1;
				digit = Integer.parseInt(input.next());
				
				if (digitValid(digit) && validMove(position_y, position_x, digit) && subGridValid(position_y, position_x, digit)){
					
					//inserts digit into grid as per user command
					grid[position_y][position_x] = digit;
					
					if(gridFull() || noPossibleMove()){
						outcome();
					}
					else{
						player2Turn = false;
						player1Turn = true;
						printGrid();
					}
				}
			}
		}
	}
	
	/*
	 * Checks if digit doesn't already exist in subgrid
	 * where it is to be inserted
	 */
	private static boolean subGridValid(int position_y, int position_x, int digit){
		
		ArrayList<Integer> subgrid = new ArrayList<>();
		
		//Top left subgrid
		if(position_y < (rows)/2 && position_x < (rows)/2){
			
			for (int i = 0; i < rows/2; i++) {
				for (int j = 0; j < cols/2; j++) {
					subgrid.add(grid[i][j]);
				}
			}
		}
		
		//Top right subgrid
		else if(position_y < (rows)/2 && position_x >= (rows)/2){
			
			for (int i = 0; i < rows/2; i++) {
				for (int j = cols/2; j < cols; j++) {
					subgrid.add(grid[i][j]);
				}
			}
		}
		
		//Bottom left subgrid
		else if(position_y >= (rows)/2 && position_x < (rows)/2){
			
			for (int i = rows/2; i < rows; i++) {
				for (int j = 0; j < cols/2; j++) {
					subgrid.add(grid[i][j]);
				}
			}
		}
		
		//Bottom right subgrid
		else if(position_y >= (rows)/2 && position_x >= (rows)/2){
			
			for (int i = rows/2; i < rows; i++) {
				for (int j = cols/2; j < cols; j++) {
					subgrid.add(grid[i][j]);
				}
			}
		}
		
		//Check if digit is already in subgrid
		
		for (int i = 0; i < subgrid.size(); i++) {
			if (digit == subgrid.get(i)){
				if(pcTurn){
					return false;	
				}
				else{
					System.out.println("Illegal move");
					System.exit(0);
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * Used before computer or player plays to check for solution
	 */
	private static boolean subGridValid1(int position_y, int position_x, int digit){
		
		ArrayList<Integer> subgrid = new ArrayList<>();
		
		//Top left subgrid
		if(position_y < (rows)/2 && position_x < (rows)/2){
			
			for (int i = 0; i < rows/2; i++) {
				for (int j = 0; j < cols/2; j++) {
					subgrid.add(grid[i][j]);
				}
			}
		}
		
		//Top right subgrid
		else if(position_y < (rows)/2 && position_x >= (rows)/2){
			
			for (int i = 0; i < rows/2; i++) {
				for (int j = cols/2; j < cols; j++) {
					subgrid.add(grid[i][j]);
				}
			}
		}
		
		//Bottom left subgrid
		else if(position_y >= (rows)/2 && position_x < (rows)/2){
			
			for (int i = (rows)/2; i < rows; i++) {
				for (int j = 0; j < cols/2; j++) {
					subgrid.add(grid[i][j]);
				}
			}
		}
		
		//Bottom right subgrid
		else if(position_y >= (rows)/2 && position_x >= (rows)/2){
			
			for (int i = rows/2; i < rows; i++) {
				for (int j = cols/2; j < cols; j++) {
					subgrid.add(grid[i][j]);
				}
			}
		}
		
		//Check if digit is already in subgrid
		
		for (int i = 0; i < subgrid.size(); i++) {
			if (digit == subgrid.get(i)){
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Determines who wins during two player mode
	 */
	public static void outcome() throws InterruptedException{
		
		String outcome;
		
		if(mode == 0){
			printGrid();
			if (pcTurn){
				System.out.println("Human wins.");
			}
			else{
				System.out.println("Computer wins.");
			}
		}
		else if (mode == 1){
			printGrid();
			if (player1Turn){
				System.out.println("Player 1 wins.");
			}
			else{
				System.out.println("Player 2 wins.");
			}
		}
		
		else if (mode == 2){
			display();
			if(pcTurn){
				outcome = "Human wins.";
				dialog(outcome);
			}
			else{
				outcome = "Computer wins.";
				dialog(outcome);
			}
		}
		System.exit(0);
	}

	/*
	 * Executes during single player mode
	 * and prompts user for command (move0
	 */
	private static void human() {

			int position_y, position_x, digit;
			Scanner input = new Scanner(System.in);
			System.out.print("Enter your move:\n> ");
			
			//Get user input
			position_y = Integer.parseInt(input.next()) - 1;
			position_x = Integer.parseInt(input.next()) - 1;
			digit = Integer.parseInt(input.next());
			
			if (digitValid(digit) && validMove(position_y, position_x, digit) && subGridValid(position_y, position_x, digit)){
				//inserts digit into grid as per user command
				grid[position_y][position_x] = digit;
				pcTurn = true;
				player1Turn = false;
				
			}	
			
	}

	/*
	 * Checks if digit is to be entered into grid is non-negative
	 * and less than or equal to row or col value
	 */
	private static boolean digitValid(int digit){
		
		if(digit > 0 && digit <= max)
			return true;
		else{
			if (pcTurn){
				return false;
			}
			else{
				System.out.println("Illegal move");
				System.exit(0);
			}
		}
			return false;
	}
	
	/*
	 * Displays current game state
	 */
	private static void printGrid() {
		
		System.out.println();
		
		int value;
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 0)
					System.out.print(". ");
				else{
					value = grid[i][j];
					
					if (value < 10){
						System.out.print(value + " ");
					}
					else{
						switch(value){
							case 10:
								System.out.print("A" + " ");
								break;
							case 11:
								System.out.print("B" + " ");
								break;
							case 12:
								System.out.print("C" + " ");
								break;
							case 13:
								System.out.print("D" + " ");
								break;
							case 14:
								System.out.print("E" + " ");
								break;
							case 15:
								System.out.print("F" + " ");
								break;
							case 16:
								System.out.print("G" + " ");
								break;
						}
					}
				}
					
			}
			System.out.println();
		}
		
		System.out.println();
	}
	
	/*
	 * Computer vs human mode
	 */
	private static void computer() throws InterruptedException{
		
		int i;
		
		outer:
		for (i  = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				
				for(int digit = 0; digit <= rows ; ++digit){
					if (insert(digit, i, j)){
						pcTurn = false;
						player1Turn = true;
						break outer;
					}
				}
			}
		}
		
		if (i == grid.length){
			if (mode == 2){
				outcome();
			}
			else{
				System.out.println("Human wins.");
				System.exit(0);
			}
		}
		
		
		if (gridFull()){
			outcome();
		}
		else{
			pcTurn = false;
			player1Turn = true;
		}
		
	}
	
	/*
	 * Generates number when it's the computer's turn to
	 * play and inserts it in a random location on the grid
	 */
	private static boolean insert(int digit, int i, int j) throws InterruptedException {
		
		if (digitValid(digit) && validMove(i, j, digit) && subGridValid(i, j, digit)){
			//inserts digit into grid as per user command
			grid[i][j] = digit;
			
			if (mode != 2){
				printGrid();
				//Check if grid is full and display winner
				if (gridFull() || noPossibleMove()){
					outcome();
				}
			}
			
			return true;
		}
		return false;
	}

	/*
	 * Explores valid moves still available
	 * returns true if no possible moves are left
	 */
	private static boolean noPossibleMove() {

		int i;
		
		outer:
		for (i  = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				
				for(int digit = 1; digit <= rows ; digit++){
					
					if ( subGridValid1(i, j, digit) & validMove1(i, j, digit)){
						break outer;
					}
				}
			}
		}
		
		//No possible move found if we reach end of grid
		if (i == grid.length){
			return true;
		}
		return false;
	}

	/*
	 * Checks if row and column from user don't exceed array dimension
	 * Also check if digit being inserted doesn't already exist in the 
	 * given row or column
	 */
	private static boolean validMove(int position_y, int position_x, int digit){
		
		//Checks if dimensions are not out of range
		if (position_x + 1 > cols || position_y + 1> rows){
			if (pcTurn)
				return false;
			else{
				System.out.println("Illegal move");
				System.exit(0);
				return false;
			}
		}
		
		//Checks if there's no number in given position already
		else if (grid[position_y][position_x] != 0){
			if (pcTurn)
				return false;
			else{
				System.out.println("Illegal move");
				System.exit(0);
				return false;
			}
		}
		else if (duplicate(position_y, position_x, digit)){
			if (pcTurn)
				return false;
			else{
				System.out.println("Illegal move");
				System.exit(0);
				return false;
			}
		}
		else
			return true;
	}
	/*
	 * Checks if row and column from user don't exceed array dimension
	 * Also check if digit being inserted doesn't already exist in the 
	 * given row or column
	 * used for testing before player plays
	 */
	private static boolean validMove1(int position_y, int position_x, int digit){
		
		//Checks if there's no number in given position already
		if (grid[position_y][position_x] != 0){
			return false;
		}
		else if (duplicate1(position_y, position_x, digit)){
			return false;
		}
		else
			return true;
	}
	/*
	 * Checks if digit doesn't already exist in given row or column
	 */
	private static boolean duplicate(int position_y, int position_x, int digit){
		
		//Checks for duplicate in row where digit is to be inserted
		for (int x = 0; x < grid[position_y].length; x++) {
			
			if (grid[position_y][x] == digit){
				if(pcTurn){
					return true;
				}
				else{
					System.out.println("Illegal move");
					System.exit(0);
					return true;
				}		
			}	
		}
		
		
		//Checks for duplicate in column where digit is to be inserted
		for (int y = 0; y < grid.length; y++) {
			
			if (grid[y][position_x] == digit){
				if(pcTurn){
					return true;
				}
				else{
					System.out.println("Illegal move");
					System.exit(0);
					return true;
				}
			}
				
		}
		return false;
	}
	
	/*
	 * Use for trial run before next player plays
	 */
	private static boolean duplicate1(int position_y, int position_x, int digit){
		
		//Checks for duplicate in row where digit is to be inserted
		for (int x = 0; x < grid[position_y].length; x++) {
			
			if (grid[position_y][x] == digit){
					return true;	
			}	
		}
		
		
		//Checks for duplicate in column where digit is to be inserted
		for (int y = 0; y < grid.length; y++) {
			
			if (grid[y][position_x] == digit){
					return true;
			}
				
		}
		return false;
	}
	
	/*
	 * Checks if grid is full and determines the winnner
	 */
	private static boolean gridFull(){
		for (int i  = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 0)
					return false;
			}
		}
		return true;
	}
	
	/*
	 * Shows message to player after all the moves have been executed
	 * @param number of moves
	 * @throws FileNotFoundException, InterruptedException
	 */
    public static void dialog(String outcome) throws InterruptedException{
	    JOptionPane.showMessageDialog(null, outcome);
	    System.exit(0);
	}
}






























