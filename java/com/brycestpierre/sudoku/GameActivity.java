package com.brycestpierre.sudoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import SudokuLogic.Difficulty;

public class GameActivity extends AppCompatActivity
{
    // View variables.
    private Button clearButton;
    private TextView bestTimeText;
    private SudokuView sudokuView;
    private Chronometer chronometer;
    private NumberPicker numberPicker;

    // Game state variables.
    private boolean newBest;                // Indicator of a new best time.
    private long milliseconds;              // Elapsed completion time for puzzle.
    private Difficulty difficulty;          // Difficulty enum of the puzzle.
    private Intent splashActivityIntent;    // Intent from the launching activity.
    private SharedPreferences preferences;  // Preferences object for player data.

    // Bundle key string variables.
    public static final String TIME_KEY = "time";
    public static final String SCORE_KEY = "scores";
    public static final String CURRENT_BOARD_KEY = "board";
    public static final String ORIGINAL_BOARD_KEY = "original";
    public static final String EASY_SCORE = "easy";
    public static final String MEDIUM_SCORE = "medium";
    public static final String HARD_SCORE = "hard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize game state variables.
        newBest = false;
        milliseconds = 0;
        preferences = getSharedPreferences(SCORE_KEY, MODE_PRIVATE);
        splashActivityIntent = new Intent(this, SplashActivity.class);
        difficulty = (Difficulty) getIntent().getSerializableExtra("difficulty");

        // Initialize the SudokuView component.
        sudokuView = (SudokuView) findViewById(R.id.sudokuView);

        // Initialize the Chronometer component.
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        // Initialize the NumberPicker component and add ChangeListener.
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(9);
        numberPicker.setWrapSelectorWheel(false);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sudokuView.onSetCellValue(newVal);
                if (sudokuView.isComplete()) {
                    milliseconds = SystemClock.elapsedRealtime() - chronometer.getBase();
                    checkTime();
                    showDialog();
                }
            }
        });

        // Initialize the Clear button.
        clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                sudokuView.onSetCellValue(0);
            }
        });

        // Initialize the text displaying the player's best time.
        bestTimeText = (TextView) findViewById(R.id.bestTimeText);
        if (preferences.contains(getScoreKey()))
            bestTimeText.setText("Best: " + getTimeString(preferences.getLong(getScoreKey(), -1)));
        else
            bestTimeText.setText("No best time yet.");
    }

    // Sends the Bundle to save game state before pausing Activity.
    protected void onSaveInstanceState (Bundle outState) {
        outState.putLong(TIME_KEY, SystemClock.elapsedRealtime() - chronometer.getBase());
        outState.putSerializable(CURRENT_BOARD_KEY, sudokuView.getBoard());
        outState.putSerializable(ORIGINAL_BOARD_KEY, sudokuView.getOriginalBoard());
    }

    // Receives the Bundle to restore game state after resuming Activity.
    protected void onRestoreInstanceState (Bundle inState) {
        int[][] board = (int[][]) inState.getSerializable(CURRENT_BOARD_KEY);
        int[][] original = (int[][]) inState.getSerializable(ORIGINAL_BOARD_KEY);
        sudokuView.setBoard(board);
        sudokuView.setOriginalBoard(original);

        long initialTime = inState.getLong(TIME_KEY);
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime() - initialTime);
        chronometer.start();
    }

    // Accesses the SharedPreferences object to update the player's new best time.
    private void checkTime () {
        SharedPreferences.Editor editor = preferences.edit();

        if (!preferences.contains(getScoreKey())) {
            editor.putLong(getScoreKey(), milliseconds);
            editor.commit();
            newBest = true;
        }

        long time = preferences.getLong(getScoreKey(), -1);
        if (milliseconds < time) {
            editor.putLong(getScoreKey(), milliseconds);
            editor.commit();
            newBest = true;
        }
    }

    // Display a dialog when the user successfully completes the Sudoku puzzle.
    private void showDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String message = "Great work!";
        if (newBest)
            message += (" A new best time of " + getTimeString(milliseconds) + "!");

        builder.setMessage(message);
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                startSplashActivity();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Return a string that displays the time value of the provided milliseconds.
    private String getTimeString (long m) {
        return String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(m),
            TimeUnit.MILLISECONDS.toMinutes(m) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(m)),
            TimeUnit.MILLISECONDS.toSeconds(m) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(m)));
    }

    // Return the appropriate key for the difficulty of the current board.
    private String getScoreKey () {
        switch (difficulty) {
            case EASY: return EASY_SCORE;
            case MEDIUM: return MEDIUM_SCORE;
            case HARD: return HARD_SCORE;
            default: return EASY_SCORE;
        }
    }

    // Return to the introduction activity.
    private void startSplashActivity () {
        startActivity(splashActivityIntent);
    }

    // Set the NumberPicker component's value.
    public void setNumberPicker (int value) {
        numberPicker.setValue(value);
    }

    // Return the Difficulty value selected for this game.
    public Difficulty getGameDifficulty () {
        return difficulty;
    }
}
