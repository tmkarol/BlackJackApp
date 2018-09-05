package com.csci448.tkarol_a1.tmkarol_a2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by tkarol on 2/28/18.
 * This activity starts the GameFragment that handles the gameplay of the app and returns
 * information to the WelcomeActivity about the score values
 */

public class GameActivity extends AppCompatActivity{
    //These extra variables are sent to the GameFragment to be used with its displays and records of the scores
    private static final String EXTRA_PLAYER_SCORE = "com.csci448.tkarol_a1.tmkarol_a2.player_score";
    private static final String EXTRA_DEALER_SCORE = "com.csci448.tkarol_a1.tmkarol_a2.dealer_score";
    private static final String EXTRA_DRAW_SCORE = "com.csci448.tkarol_a1.tmkarol_a2.draw_score";
    private static final String EXTRA_CARD_COLOR = "com.csci448.tkarol_a1.tmkarol_a2.color_result";
    private static final String EXTRA_SHOW_TOASTS = "com.csci448.tkarol_a1.tmkarol_a2.show_toasts";
    private static final String EXTRA_PLAYER_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.player_result";
    private static final String EXTRA_DEALER_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.dealer_result";
    private static final String EXTRA_DRAW_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.draw_result";
    private static final String EXTRA_RESET_SCORE = "com.csci448.tkarol_a1.tmkarol_a2.reset_score";

    //These extras are only used to save the state of the activity
    private static final String EXTRA_PLAYER_TOTAL = "player_total";
    private static final String EXTRA_DEALER_TOTAL = "dealer_total";
    private static final String EXTRA_CURRENT_CARD = "current_card";

    //Instance of this activity's fragment
    GameFragment fragment;
    /**
     * This function is used in WelcomeFragment when an intent is made to begin this activity.
     * By having GameActivity define what it expects to be in the received intent, this class
     * remains encapsulated.
     * @param packageContext
     * @param playerScore
     * @param dealerScore
     * @param drawScore
     * @param color
     * @param reset
     * @param show_toasts
     * @return
     */
    public static Intent newIntent(Context packageContext, int playerScore, int dealerScore, int drawScore, String color, boolean reset, boolean show_toasts){
        Intent intent = new Intent(packageContext, GameActivity.class);

        //These extra variables will need to be put into the intent to be used with the GameFragment
        intent.putExtra(EXTRA_PLAYER_SCORE, playerScore);
        intent.putExtra(EXTRA_DEALER_SCORE, dealerScore);
        intent.putExtra(EXTRA_DRAW_SCORE, drawScore);
        intent.putExtra(EXTRA_CARD_COLOR, color);
        intent.putExtra(EXTRA_RESET_SCORE, reset);
        intent.putExtra(EXTRA_SHOW_TOASTS, show_toasts);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //set the view to be the empty container where the fragment can be inflated
        setContentView(R.layout.fragment_container);

        //These are placeholder variables that will be passed to the GameFragment as arguments
        int saved_player_total, saved_dealer_total, saved_card_res_id, player_score, dealer_score, draw_score;

        //If the activity is being created as a result of a rotation, assign these variables with extras from the bundle
        if(savedInstanceState != null){
            saved_player_total = savedInstanceState.getInt(EXTRA_PLAYER_TOTAL, -1);
            saved_dealer_total = savedInstanceState.getInt(EXTRA_DEALER_TOTAL, -1);
            saved_card_res_id = savedInstanceState.getInt(EXTRA_CURRENT_CARD, 0);
            player_score = savedInstanceState.getInt(EXTRA_PLAYER_SCORE, 0);
            dealer_score = savedInstanceState.getInt(EXTRA_DEALER_SCORE, 0);
            draw_score = savedInstanceState.getInt(EXTRA_DRAW_SCORE, 0);
        }
        //If the activity is being created by the WelcomeFragment, assign the variables with extras from the intent
        else{
            //The variables being assigned negative values will not be used when the GameFragment is created because these aren't saved
            //when the user navigates out of the GameFragment
            saved_player_total = -1;
            saved_dealer_total = -1;
            saved_card_res_id= -1;
            player_score = getIntent().getIntExtra(EXTRA_PLAYER_SCORE, 0);
            dealer_score =  getIntent().getIntExtra(EXTRA_DEALER_SCORE, 0);
            draw_score = getIntent().getIntExtra(EXTRA_DRAW_SCORE,0);
        }

        //create a GameFragment that can be attached to this activity
                fragment = GameFragment.newInstance(player_score, dealer_score, draw_score,
                getIntent().getStringExtra(EXTRA_CARD_COLOR),
                getIntent().getBooleanExtra(EXTRA_RESET_SCORE, false),
                getIntent().getBooleanExtra(EXTRA_SHOW_TOASTS, false),
                        saved_player_total, saved_dealer_total, saved_card_res_id);


        //start fragment transaction to replace WelcomeFragment with a GameFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }


    //These functions are used to extract the information that the WelcomeFragment needs from the GameActivity's
    //resulting intent
    public static int playerScoreResult(Intent result){
        return result.getIntExtra(EXTRA_PLAYER_RESULT, 0);
    }

    public static int dealerScoreResult(Intent result){
        return result.getIntExtra(EXTRA_DEALER_RESULT,0);
    }

    public static int drawScoreResult(Intent result){
        return result.getIntExtra(EXTRA_DRAW_RESULT,0);
    }

    /**
     * This function adds extras to the bundle that will be saved before the device is rotated. The variables are being
     * accessed from the GameFragment instance.
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(EXTRA_PLAYER_TOTAL, fragment.getTotalPlayer());
        savedInstanceState.putInt(EXTRA_DEALER_TOTAL, fragment.getTotalDealer());
        savedInstanceState.putInt(EXTRA_CURRENT_CARD, fragment.getCurrentCardResID());
        savedInstanceState.putInt(EXTRA_PLAYER_SCORE, fragment.getScorePlayer());
        savedInstanceState.putInt(EXTRA_DEALER_SCORE, fragment.getScoreDealer());
        savedInstanceState.putInt(EXTRA_DRAW_SCORE, fragment.getScoreDraw());
    }

}
