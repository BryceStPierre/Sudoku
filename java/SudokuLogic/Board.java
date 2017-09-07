package SudokuLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Board 
{
	private int[][] board;		// Active board manipulated by the player.
    private int[][] original;	// Original board after shuffling and blanks.
	private int[][] solution;	// Solution board the player is working towards.

	// Constructor.
	public Board (Difficulty d) {
		// Begin with a valid Sudoku solution.
		board = new int[][] {
			{3, 2, 9, 6, 5, 7, 8, 4, 1},
			{7, 4, 5, 8, 3, 1, 2, 9, 6},
			{6, 1, 8, 2, 4, 9, 3, 7, 5},
			{1, 9, 3, 4, 6, 8, 5, 2, 7},
			{2, 7, 6, 1, 9, 5, 4, 8, 3},
			{8, 5, 4, 3, 7, 2, 6, 1, 9},
			{4, 3, 2, 7, 1, 6, 9, 5, 8},
			{5, 8, 7, 9, 2, 3, 1, 6, 4},
			{9, 6, 1, 5, 8, 4, 7, 3, 2}
		};
		shuffleCells(); // Shuffle the board to make it unique.

		// Create copy of board for original values.
		solution = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++)
				solution[i][j] = board[i][j];
		}

		hideCells(d); // Hide cells for presentation to the player.

		// Create copy of board for original values.
		original = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                original[i][j] = board[i][j];
        }
	}

	// Swap rows of a Sudoku board.
	private void swapRows (int i, int j) {
		int[] temporary = board[i];
		board[i] = board[j];
		board[j] = temporary;
	}

	// Swap columns of a Sudoku board.
	private void swapColumns (int i, int j) {
		int temporary;
		for (int k = 0; k < 9; k++) {
			temporary = board[k][i];
			board[k][i] = board[k][j];
			board[k][j] = temporary;
		}
	}

	// Shuffle the rows and columns to generate a unique Sudoku board.
	private void shuffleCells () {
		ArrayList<Integer> indices = new ArrayList<Integer>(Arrays.asList(0, 1, 2));
		
		for (int i = 0; i < 3; i++) {
			Collections.shuffle(indices);
			swapRows(indices.get(0).intValue() + (i * 3), indices.get(1).intValue() + (i * 3));
		}
		
		for (int i = 0; i < 3; i++) {
			Collections.shuffle(indices);
			swapColumns(indices.get(0).intValue() + (i * 3), indices.get(1).intValue() + (i * 3));
		}
	}

	// Hide a random number of cells, depending on the difficulty.
	private void hideCells (Difficulty d) {
		ArrayList<Integer> spaces;
		switch (d) {
			case EASY:   spaces = new ArrayList<Integer>(Arrays.asList(2,3,4));
				break; 
			case MEDIUM: spaces = new ArrayList<Integer>(Arrays.asList(3,4,5));
				break; 
			case HARD: 	 spaces = new ArrayList<Integer>(Arrays.asList(4,5,6));
				break;
			default: 	 spaces = new ArrayList<Integer>(Arrays.asList(3,4,5));
				break; 
		}
		
		ArrayList<Integer> indices;
		for (int i = 0; i < 9; i++) {
			Collections.shuffle(spaces);
			indices = generateHideIndices(spaces.get(1));
			for (int j : indices)
				board[i][j] = 0;
		}	
	}

	// Generate the random positions for which to hide cells.
	private ArrayList<Integer> generateHideIndices (int number) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Random random = new Random();
		
		for (int i = 0; i < number; i++) {
			int value;
			do {
				value = random.nextInt(9);
			} while (indices.contains(value));
			indices.add(value);
		}
		return indices;
	}

	// Get a specific row from the board.
	public ArrayList<Integer> getRow (int row) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 9; i++)
			list.add(board[row][i]);
		return list;
	}

	// Get a column row from the board.
	public ArrayList<Integer> getColumn (int column) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 9; i++)
			list.add(board[i][column]);
		return list;
	}

	// Get a sub-grid from the board, based on its corner.
	public ArrayList<Integer> getSubGrid (int a, int b) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = a * 3; i < (a * 3) + 3; i++) {
			for (int j = b * 3; j < (b * 3) + 3; j++)
				list.add(board[i][j]);
		}
		return list;
	}

	// Set the value of a particular cell.
	public void setCell (int row, int column, int value) {
		board[row][column] = value;
	}

	// Get the value of a particular cell.
	public int getCell (int row, int column) {
		return board[row][column];
	}

	// Set the value of a particular cell in the original board.
	public void setOriginalCell (int row, int column, int value) {
		original[row][column] = value;
	}

	// Get the value of a particular cell in the original board.
	public int getOriginalCell (int row, int column) {
		return original[row][column];
	}

	// Get the two-dimensional array of integers used in the board.
	public int[][] getBoardArray () {
		return board;
	}

	// Get the two-dimensional array of integers used in the original board.
	public int[][] getOriginalBoardArray () {
		return original;
	}

	// Determine if the cell at the particular row and column is original.
	public boolean isOriginalCell (int row, int column) {
		return original[row][column] != 0;
	}

	// Return the Sudoku board solution as a string.
	public String toSolutionString () {
		String s = "";

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if ((j + 1) % 3 == 0)
					s += " " + solution[i][j] + " |";
				else
					s += " " + solution[i][j];
			}
			if ((i + 1) % 3 == 0)
				s += "\n------------------------\n";
			else
				s += "\n";
		}
		return s;
	}

	// Return the Sudoku board as a string.
	public String toString () {
		String s = "";
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if ((j + 1) % 3 == 0)
					s += " " + board[i][j] + " |";
				else
					s += " " + board[i][j];
			}
			if ((i + 1) % 3 == 0)
				s += "\n------------------------\n";
			else
				s += "\n";	
		}
		return s;
	}
}
