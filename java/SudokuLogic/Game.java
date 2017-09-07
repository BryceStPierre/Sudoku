package SudokuLogic;

import java.util.ArrayList;

public class Game 
{
	private Board board; // The game board.

	// Constructor.
	public Game (Difficulty d) {
		board = new Board(d);
	}

	// Set the value of a particular cell.
	public void setCell (int row, int column, int value) {
		board.setCell(row, column, value);
	}

	// Get the value of a particular cell.
	public int getCell (int row, int column) {
		return board.getCell(row, column);
	}

	// Set the value of a particular cell in the original board..
	public void setOriginalCell (int row, int column, int value) {
		board.setOriginalCell(row, column, value);
	}

	// Get the value of a particular cell from the original board.
	public int getOriginalCell (int row, int column) {
		return board.getOriginalCell(row, column);
	}

	// Get the two-dimensional array of integers used in the board.
	public int[][] getBoardArray () {
		return board.getBoardArray();
	}

	// Get the two-dimensional array of integers used in the original board.
	public int[][] getOriginalBoardArray () {
		return board.getOriginalBoardArray();
	}

	// Determine if the given cell at row and column is original.
	public boolean isOriginalCell (int row, int column) {
		return board.isOriginalCell(row, column);
	}

	// Determine if the Sudoku puzzle is complete.
	public boolean isComplete () {
		boolean complete = true;
		for (int i = 0; i < 9; i++) {
			complete = complete && isCompleteList(board.getRow(i));
			complete = complete && isCompleteList(board.getColumn(i));
		}	
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
				complete = complete && isCompleteList(board.getSubGrid(i, j));
		}
		return complete;
	}

	// Determine if the given list contains digits 1 through 9.
	private boolean isCompleteList (ArrayList<Integer> list) {
		boolean complete = true;
		for (int i = 1; i <= 9; i++)
			complete = complete && list.contains(i);
		return complete;
	}

	// Return the solution to the Sudoku puzzle as a string.
	public String toSolutionString () {
		return board.toSolutionString();
	}

	// Return the Sudoku puzzle's state as a string.
	public String toString () {
		return board.toString();
	}
}
