package com.byte_buster.tictactoe;

import static com.byte_buster.tictactoe.R.drawable.cross_prev_ui;
import static com.byte_buster.tictactoe.R.drawable.zero_prev_ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    boolean gameActive = false;
    boolean botEnabled = false;
    boolean playerEnabled = false;

    int activePlayer = 2;                                                                                   //Player Description {0-X and 1-O}
    int[] gamestate = {2, 2, 2, 2, 2, 2, 2, 2, 2};                                                         //State meanings {0-X 1-O 2-Null}

    int[][] winpos = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                        {2, 4, 6}, {0, 4, 8}};

    public void enablePlayer(View view) {
        playerEnabled = true;                                                                            // Enable player play
        botEnabled = false;
        gamereset(view);
        TextView status = findViewById(R.id.Status);
        status.setText("Player vs Player Mode Enabled: X's Turn - Tap to play");
    }

    public void enableBot(View view) {
        botEnabled = true;                                                                           // Enable bot play
        playerEnabled = false;
        gamereset(view);
        TextView status = findViewById(R.id.Status);
        status.setText("Bot Mode Enabled: X's Turn - Tap to play");
    }

    public void gameStart(View view){
       gameActive = true;
       activePlayer = 1;
                                                                                                            // Start game based on mode
        if (botEnabled) {
            TextView status = findViewById(R.id.Status);
            status.setText("Bot Mode Enabled: X's Turn - Tap to play");
        } else {
            TextView status = findViewById(R.id.Status);
            status.setText("Player vs Player Mode Enabled: X's Turn - Tap to play");
        }
    }

    public void playertap(View view){
        if(!gameActive){
            return;
        }

        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        if (gamestate[tappedImage] == 2) {
            gamestate[tappedImage]=activePlayer;

            if(activePlayer==0){
                img.setImageResource(cross_prev_ui);
                activePlayer=1;
                TextView Status = findViewById(R.id.Status);
                Status.setText("X's Turn - Tap to Play");
                checkForWin();
                if (gameActive && botEnabled) {
                    playbot(null);                                                                  // Trigger bot's move if bot mode is enabled
                }
            }
            else {
                img.setImageResource(zero_prev_ui);
                activePlayer=0;
                TextView Status = findViewById(R.id.Status);
                Status.setText("O's Turn - Tap to Play");
                checkForWin();
            }// Check for a win or draw after the move
        }
    }

    public void playbot(View view){
        if(!gameActive){
            return;
        }
        for (int pos[] : winpos){

            if (gamestate[pos[0]]==1 && gamestate[pos[1]]==1 && gamestate[pos[2]] ==2){                //Check if bot can win
                makeMove(pos[2], 1);
                return;
            }
            if (gamestate[pos[0]]==1 && gamestate[pos[2]]==1 && gamestate[pos[1]] ==2){
                makeMove(pos[1], 1);
                return;
            }
            if (gamestate[pos[1]]==1 && gamestate[pos[2]]==1 && gamestate[pos[0]] ==2){
                makeMove(pos[0], 1);
                return;
            }
        }
                                                                                                       // Check if player is about to win and block
        for (int[] pos : winpos) {
            if (gamestate[pos[0]] == 0 && gamestate[pos[1]] == 0 && gamestate[pos[2]] == 2) {
                makeMove(pos[2], 1);
                return;
            }
            if (gamestate[pos[0]] == 0 && gamestate[pos[2]] == 0 && gamestate[pos[1]] == 2) {
                makeMove(pos[1], 1);
                return;
            }
            if (gamestate[pos[1]] == 0 && gamestate[pos[2]] == 0 && gamestate[pos[0]] == 2) {
                makeMove(pos[0], 1);
                return;
            }
        }

        if (gamestate[4]==2){
            makeMove(4,1);
            return;
        }
        for(int i : new int[]{0, 2, 6, 8}){
            if (gamestate[i]==2){
                makeMove(i, 1);
                return;
            }
        }
        for (int i = 0; i < gamestate.length; i++) {
            if (gamestate[i] == 2) {
                makeMove(i, 1);
                return;
            }
        }
    }

    public void checkForWin() {
        for (int[] pos : winpos) {
            // Check if a winning position is occupied by the same player
            if (gamestate[pos[0]] == gamestate[pos[1]] &&
                    gamestate[pos[1]] == gamestate[pos[2]] &&
                    gamestate[pos[0]] != 2) {

                // Someone has won, declare the winner
                gameActive = false;
                String winnerStr;

                if (gamestate[pos[0]] == 0) {
                    winnerStr = "X has won!";
                } else {
                    winnerStr = "O has won!";
                }

                // Update the status bar
                TextView status = findViewById(R.id.Status);
                status.setText(winnerStr);
                return;
            }
        }

                                                                                                             // Check for a draw if no empty positions remain
        boolean draw = true;
        for (int state : gamestate) {
            if (state == 2) {
                draw = false;                                                                                           // Found an empty position, so no draw
                break;
            }
        }

        if (draw) {
            gameActive = false;
            TextView status = findViewById(R.id.Status);
            status.setText("It's a Draw!");
        }
    }

    public void makeMove(int position, int player) {
        ImageView img = (ImageView) findViewById(getResources().getIdentifier("imageView" + position, "id", getPackageName()));
        gamestate[position] = player;
        if (player == 0) {
            img.setImageResource(cross_prev_ui);
        } else {
            img.setImageResource(zero_prev_ui);
        }
        activePlayer = (player == 0) ? 1 : 0;
        checkForWin();

        if (!gameActive) {
            TextView status = findViewById(R.id.Status);
            status.setText("Game Over! " + ((player == 0) ? "X" : "O") + " has won!");
        }
    }

    public void gamereset(View view){
        gameActive= true;
        activePlayer = 0;

        for(int i=0;i<gamestate.length;i++){
            gamestate[i]=2;
        }
        ((ImageView)findViewById(R.id.imageView0)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView8)).setImageResource(0);

        TextView Status = findViewById(R.id.Status);
        Status.setText("X's Turn - Tap to play");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}
