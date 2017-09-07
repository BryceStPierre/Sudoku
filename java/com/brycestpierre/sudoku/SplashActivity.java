package com.brycestpierre.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import SudokuLogic.Difficulty;

public class SplashActivity extends AppCompatActivity
{
    // Button variables.
    Button easy, medium, hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Add OnClickListener to buttons to start GameActivity with corresponding Difficulty.
        easy = (Button) findViewById(R.id.easyButton);
        easy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startGame(Difficulty.EASY);
            }
        });

        medium = (Button) findViewById(R.id.mediumButton);
        medium.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startGame(Difficulty.MEDIUM);
            }
        });

        hard = (Button) findViewById(R.id.hardButton);
        hard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startGame(Difficulty.HARD);
            }
        });
    }

    // Start the GameActivity, passing the Difficulty enum value selected.
    private void startGame (Difficulty d) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("difficulty", d);
        startActivity(intent);
    }
}
