package com.brycestpierre.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import SudokuLogic.Game;

public class SudokuView extends View
{
    // String identifier for the logging mechanism.
    private static final String TAG = "SudokuView";

    // Logic variables.
    private Game game;

    // Layout variables.
    private float unitSize;
    private int viewWidth, viewHeight;
    private int selectedRow, selectedColumn;

    // GUI variables.
    private GameActivity activity;
    private Paint linePaint, enabledTextPaint, disabledTextPaint;

    // Constructor.
    public SudokuView (Context context, AttributeSet attrs) {
        super(context, attrs);

        selectedRow = -1;
        selectedColumn = -1;

        disabledTextPaint = new Paint();
        enabledTextPaint = new Paint();
        linePaint = new Paint();
    }

    // Called when the view is created.
    protected void onSizeChanged (int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        viewWidth = w;
        viewHeight = h;
        unitSize = viewWidth / 9; // Store size of a Sudoku board cell.

        // Store reference to the Activity containing this view.
        activity = (GameActivity) getContext();

        // Create new Sudoku game with the selected Difficulty value.
        game = new Game(activity.getGameDifficulty());

        //Log.d(TAG, game.toSolutionString());
    }

    // Handles touch event on the Sudoku board.
    public boolean onTouchEvent (MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_DOWN)
            onSelectCell(event.getX(), event.getY());
        return true;
    }

    // Drawing method.
    protected void onDraw (Canvas c) {
        linePaint.setStrokeWidth(1.5f);
        linePaint.setColor(Color.BLACK);
        enabledTextPaint.setColor(Color.BLACK);
        disabledTextPaint.setColor(Color.GRAY);

        // Draw lines.
        for (int i = 0; i < 10; i++) {
            if (i == 0 || i == 9 || i % 3 == 0)
                linePaint.setStrokeWidth(7f);

            c.drawLine(i * unitSize, 0, i * unitSize, 9 * unitSize, linePaint);
            c.drawLine(0, i * unitSize, 9 * unitSize, i * unitSize, linePaint);

            if (i == 0 || i == 9 || i % 3 == 0)
                linePaint.setStrokeWidth(1.5f);
        }

        // Draw selected Sudoku cell.
        if (selectedRow != -1 && selectedColumn != -1) {
            linePaint.setColor(getResources().getColor(R.color.colorAccent));

            float y = selectedRow * unitSize;
            float x = selectedColumn * unitSize;

            if (!game.isOriginalCell(selectedRow, selectedColumn)) {
                c.drawRect(x, y, x + unitSize, y + unitSize, linePaint);
                activity.setNumberPicker(game.getCell(selectedRow, selectedColumn));
            }
        }

        // Draw Sudoku cell values.
        enabledTextPaint.setTextSize(unitSize - (unitSize / 8));
        disabledTextPaint.setTextSize(unitSize - (unitSize / 8));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int cell = game.getCell(i, j);
                float x = (j * unitSize) + (unitSize / 4);
                float y = ((i + 1) * unitSize) - (unitSize / 8);

                // Color text black if it can be changed by the user.
                if (!game.isOriginalCell(i, j)) {
                    if (cell != 0)
                        c.drawText(String.valueOf(cell), x, y, enabledTextPaint);
                }
                // Color text grey if it cannot be changed by the user.
                else
                    c.drawText(String.valueOf(cell), x, y, disabledTextPaint);
            }
        }
    }

    // Select a particular cell via the TouchEvent for this view.
    private void onSelectCell (float x, float y) {
        selectedRow = (int) Math.floor(y / unitSize);
        selectedColumn = (int) Math.floor(x / unitSize);

        if (x < 0 || x >= 9 * unitSize || y < 0 || y >= 9 * unitSize) {
            selectedRow = -1;
            selectedColumn = -1;
        }
        invalidate(); // Call onDraw again.
    }

    // Set cell value via the NumberPicker control.
    public void onSetCellValue (int value) {
        if (selectedRow != -1 && selectedColumn != -1) {
            game.setCell(selectedRow, selectedColumn, value);
            invalidate();
        }
    }

    // Set the two-dimensional array of integers used in the board.
    public void setBoard (int[][] values) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                game.setCell(i, j, values[i][j]);
        }
    }

    // Set the two-dimensional array of integers used in the original board.
    public void setOriginalBoard (int[][] values) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                game.setOriginalCell(i, j, values[i][j]);
        }
    }

    // Get the two-dimensional array of integers used in the board.
    public int[][] getBoard () {
        return game.getBoardArray();
    }

    // Get the two-dimensional array of integers used in the original board.
    public int[][] getOriginalBoard () {
        return game.getOriginalBoardArray();
    }

    // Determine if the Sudoku board is complete.
    public boolean isComplete () {
        return game.isComplete();
    }
}
