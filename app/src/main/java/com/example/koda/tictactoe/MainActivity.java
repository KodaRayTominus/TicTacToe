package com.example.koda.tictactoe;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

public class MainActivity extends AppCompatActivity
    implements OnClickListener {

    private String currentPlayer;
    private String CurrentStartingPlayer;
    private Button topRightSquare;
    private Button topMiddleSquare;
    private Button topLeftSquare;
    private Button rightSquare;
    private Button middleSquare;
    private Button leftSquare;
    private Button bottomRightSquare;
    private Button bottomMiddleSquare;
    private Button bottomLeftSquare;
    private TextView turnDisplay;
    private ArrayList<Button> gameBoard;
    private Boolean gameOver;
    private Boolean PreviousStartingPlayerChanged = false;

    private SharedPreferences prefs;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
            case R.id.menu_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        // save the instance variables
        SharedPreferences.Editor editor = prefs.edit();
        editor.apply();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //You can read preference value anywhere in the app like following.
        boolean isPlayerOTurn = prefs.getBoolean("starting_player", false);

        if(isPlayerOTurn){
            if(!(CurrentStartingPlayer.equals(getString(R.string.PlayerO)))){
                CurrentStartingPlayer = getString(R.string.PlayerO);
                currentPlayer = getString(R.string.PlayerO);
                PreviousStartingPlayerChanged = true;
            }
        }
        else{
            if(!(CurrentStartingPlayer.equals(getString(R.string.PlayerX)))){
                CurrentStartingPlayer = getString(R.string.PlayerX);
                currentPlayer = getString(R.string.PlayerX);
                PreviousStartingPlayerChanged = true;
            }
        }
        if(PreviousStartingPlayerChanged){
            startGame(currentPlayer);
            PreviousStartingPlayerChanged = false;
        }
    }

    @Override
    public void onClick(View v) {
        Button b = getGameButton(v.getId());
        if(!gameOver && b.getText().equals("")) {
            switch (getCurrentPlayer()) {
                case "X":
                    b.setText(getString(R.string.PlayerX));
                    b.setTextSize(36);
                    changeTurn();
                    break;
                case "O":
                    b.setText(getString(R.string.PlayerO));
                    b.setTextSize(36);
                    changeTurn();
                    break;
            }
            checkForWinner();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPlayer = getString(R.string.PlayerX);
        CurrentStartingPlayer = getString(R.string.PlayerX);

        setContentView(R.layout.activity_main);

        getBoardButtons();

        Button newGameButton = findViewById(R.id.NewGameButton);

        startGame(currentPlayer);

        setOnClickForGame(newGameButton);

        // set the default values for the preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private Button getGameButton(int newGameButton) {
        return findViewById(newGameButton);
    }

    private void setOnClickForGame(Button newGameButton) {
        topRightSquare.setOnClickListener(this);
        topLeftSquare.setOnClickListener(this);
        topMiddleSquare.setOnClickListener(this);
        rightSquare.setOnClickListener(this);
        leftSquare.setOnClickListener(this);
        middleSquare.setOnClickListener(this);
        bottomRightSquare.setOnClickListener(this);
        bottomLeftSquare.setOnClickListener(this);
        bottomMiddleSquare.setOnClickListener(this);
        newGameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(CurrentStartingPlayer);
            }
        });
    }

    private void getBoardButtons() {
        gameBoard = new ArrayList<>();
        topLeftSquare = findViewById(R.id.TopLeftSquare);
        gameBoard.add(topLeftSquare);

        topMiddleSquare = findViewById(R.id.TopMiddleSquare);
        gameBoard.add(topMiddleSquare);

        topRightSquare = findViewById(R.id.TopRightSquare);
        gameBoard.add(topRightSquare);

        leftSquare = findViewById(R.id.LeftSquare);
        gameBoard.add(leftSquare);

        middleSquare = findViewById(R.id.MiddleSquare);
        gameBoard.add(middleSquare);

        rightSquare = findViewById(R.id.RightSquare);
        gameBoard.add(rightSquare);

        bottomLeftSquare = findViewById(R.id.BottomLeftSquare);
        gameBoard.add(bottomLeftSquare);

        bottomMiddleSquare = findViewById(R.id.BottomMiddleSquare);
        gameBoard.add(bottomMiddleSquare);

        bottomRightSquare = findViewById(R.id.BottomRightSquare);
        gameBoard.add(bottomRightSquare);

        turnDisplay = findViewById(R.id.TurnDisplay);
    }

    public void checkForWinner(){
        if(threeInARow(getString(R.string.PlayerX))) {
            setWinner(getString(R.string.PlayerX));
        }
        else if(threeInARow(getString(R.string.PlayerO))) {
            setWinner(getString(R.string.PlayerO));
        }
        else if(checkForTie()) {
            setTie();
        }
    }

    private boolean threeInARow(String player){
                //Horizontal wins
        return (player.equals(gameBoard.get(0).getText().toString()) && player.equals(gameBoard.get(1).getText().toString()) && player.equals(gameBoard.get(2).getText().toString())) ||
                (player.equals(gameBoard.get(3).getText().toString()) && player.equals(gameBoard.get(4).getText().toString()) && player.equals(gameBoard.get(5).getText().toString())) ||
                (player.equals(gameBoard.get(6).getText().toString()) && player.equals(gameBoard.get(7).getText().toString()) && player.equals(gameBoard.get(8).getText().toString())) ||
                //Vertical wins
                (player.equals(gameBoard.get(0).getText().toString()) && player.equals(gameBoard.get(3).getText().toString()) && player.equals(gameBoard.get(6).getText().toString())) ||
                (player.equals(gameBoard.get(1).getText().toString()) && player.equals(gameBoard.get(4).getText().toString()) && player.equals(gameBoard.get(7).getText().toString())) ||
                (player.equals(gameBoard.get(2).getText().toString()) && player.equals(gameBoard.get(5).getText().toString()) && player.equals(gameBoard.get(8).getText().toString())) ||
                //Diagonal wins
                (player.equals(gameBoard.get(0).getText().toString()) && player.equals(gameBoard.get(4).getText().toString()) && player.equals(gameBoard.get(8).getText().toString())) ||
                (player.equals(gameBoard.get(2).getText().toString()) && player.equals(gameBoard.get(4).getText().toString()) && player.equals(gameBoard.get(6).getText().toString()));
    }

    private void setWinner(String player){
        turnDisplay.setText(String.format(getString(R.string.PlayerWins), player));
        setGameOver();
    }

    private void setGameOver() {
        gameOver = true;
    }

    private boolean checkForTie(){
        for (Button b: gameBoard) {
            if(b.getText().equals("")){
                return false;
            }
        }
        setGameOver();
        return true;
    }
    
    private void setTie(){
        turnDisplay.setText(getString(R.string.TieGame));
    }


    public String getCurrentPlayer(){
        return currentPlayer;
    }

    public void changeTurn(){
        switch (getCurrentPlayer()){
            case "X":
                currentPlayer = getString(R.string.PlayerO);
                break;
            case "O":
                currentPlayer = getString(R.string.PlayerX);
                break;
        }

        turnDisplay.setText(getString(R.string.PlayerTurnString1) + currentPlayer + getString(R.string.PlayerTurnString2));
    }

    public void startGame(String player){
        currentPlayer = player;

        turnDisplay.setText(getString(R.string.PlayerTurnString1) + player + getString(R.string.PlayerTurnString2));
        gameOver = false;
        clearBoard();
    }

    public void clearBoard(){
        topRightSquare.setText("");
        topLeftSquare.setText("");
        topMiddleSquare.setText("");
        rightSquare.setText("");
        leftSquare.setText("");
        middleSquare.setText("");
        bottomRightSquare.setText("");
        bottomLeftSquare.setText("");
        bottomMiddleSquare.setText("");
    }
}
