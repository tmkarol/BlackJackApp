package com.csci448.tkarol_a1.tmkarol_a2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tkarol on 2/28/18.
 * This fragment class serves as the view and handler of the gameplay of this app
 */

public class GameFragment extends Fragment {
    //Fragment arguments that will be used in the gameplay and display
    private static final String ARG_PLAYER_SCORE = "player_score";
    private static final String ARG_DEALER_SCORE = "dealer_score";
    private static final String ARG_DRAW_SCORE = "draw_score";
    private static final String ARG_CARD_COLOR = "card_color";
    private static final String ARG_RESET_SCORE = "reset_score";
    private static final String ARG_SHOW_TOASTS = "show_toasts";
    private static final String ARG_PLAYER_TOTAL = "player_total";
    private static final String ARG_DEALER_TOTAL = "dealer_total";
    private static final String ARG_CURRENT_CARD = "current_card";

    //These extras will be sent in GameActivity's resulting intent to be accessed by WelcomeFragment.
    //These values will need to be saved through cycles of different fragments and activities in
    //the app so they are passed through via intent extras.
    private static final String EXTRA_PLAYER_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.player_result";
    private static final String EXTRA_DEALER_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.dealer_result";
    private static final String EXTRA_DRAW_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.draw_result";



    //These view element variables control the displays on the fragment and are updated throughout gameplay
    private TextView mPlayerScore;
    private TextView mDealerScore;
    private TextView mDrawScore;
    private TextView mDealerTotal;
    private TextView mPlayerTotal;
    private ImageView mDeckCard;
    private ImageView mDrawnCard;

    //These buttons will be interacted with by the user to play the game
    private Button mTakeButton;
    private Button mHoldButton;
    private Button mNewGameButton;
    private Button mBackButton;

    private int mDeckIndex; //used to iterate through deck array of Card objects
    private int mCurrentCardResID; //the resId of the current card will need to be accessed by GameActivity to save the current card when the device is rotated

    //Array storing the deck of Cards, instantiated with the drawable resources and card values
    private Card[] mDeck = new Card[]{
            new Card(R.drawable.ac, 1), new Card(R.drawable.ad, 1), new Card(R.drawable.ah, 1), new Card(R.drawable.as,1), new Card(R.drawable.twoc, 2), new Card(R.drawable.twod,2), new Card(R.drawable.twoh, 2), new Card(R.drawable.twos, 2), new Card(R.drawable.threec, 3), new Card(R.drawable.threed, 3), new Card(R.drawable.threeh,3), new Card(R.drawable.threes,3), new Card(R.drawable.fourc,4), new Card(R.drawable.fourd, 4), new Card(R.drawable.fourh,4), new Card(R.drawable.fours,4), new Card(R.drawable.fivec,5), new Card(R.drawable.fived,5), new Card(R.drawable.fiveh,5), new Card(R.drawable.fives,5), new Card(R.drawable.sixc,6), new Card(R.drawable.sixd,6), new Card(R.drawable.sixh,6), new Card(R.drawable.sixs,6), new Card(R.drawable.sevenc,7), new Card(R.drawable.sevend,7), new Card(R.drawable.sevenh,7), new Card(R.drawable.sevens,7), new Card(R.drawable.eightc,8), new Card(R.drawable.eightd,8), new Card(R.drawable.eighth,8), new Card(R.drawable.eights,8), new Card(R.drawable.ninec,9), new Card(R.drawable.nined,9), new Card(R.drawable.nineh,9), new Card(R.drawable.nines,9), new Card(R.drawable.tenc,10), new Card(R.drawable.tend,10), new Card(R.drawable.tenh,10), new Card(R.drawable.tens,10), new Card(R.drawable.jacks,10), new Card(R.drawable.jc,10), new Card(R.drawable.jd,10), new Card(R.drawable.jh,10), new Card(R.drawable.qc,10), new Card(R.drawable.qd,10), new Card(R.drawable.qh,10), new Card(R.drawable.qs,10), new Card(R.drawable.kc,10), new Card(R.drawable.kd,10), new Card(R.drawable.kh,10), new Card(R.drawable.ks,10)
    };

    //These variables store the scores and totals for the gameplay.
    //"Totals" is the sum of Card values in each player's hand while "scores" are the number of games won.
    private int mTotalPlayer;
    private int mTotalDealer;
    private int mScorePlayer;
    private int mScoreDealer;
    private int mScoreDraw;

    //This variable is used in order to dictate whether certain buttons should be visible or enabled on the layout
    private boolean mGameInProgress;

    //Refers to one of the option related arguments
    private boolean mShowToasts;
    private String mCardColor;

    /**
     * Similar to WelcomeFragment's newInstance method, this sets the fragment arguments before the instance can be
     * attached to the GameActivity that calls it
     * @param playerScore
     * @param dealerScore
     * @param drawScore
     * @param color
     * @param reset
     * @param show_toasts
     * @return
     */
    public static GameFragment newInstance(int playerScore, int dealerScore, int drawScore, String color, boolean reset, boolean show_toasts, int player_total, int dealer_total, int current_card_res_id){
        Bundle args = new Bundle();
        args.putInt(ARG_PLAYER_SCORE, playerScore);
        args.putInt(ARG_DEALER_SCORE, dealerScore);
        args.putInt(ARG_DRAW_SCORE, drawScore);
        args.putString(ARG_CARD_COLOR, color);
        args.putBoolean(ARG_RESET_SCORE, reset);
        args.putBoolean(ARG_SHOW_TOASTS, show_toasts);
        args.putInt(ARG_PLAYER_TOTAL, player_total);
        args.putInt(ARG_DEALER_TOTAL, dealer_total);
        args.putInt(ARG_CURRENT_CARD, current_card_res_id);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the fragment view into the container
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        shuffleDeck();//shuffles Card array before game starts

        //instantiate the faceup card image view
        mDrawnCard = (ImageView) v.findViewById(R.id.drawn_card);

        //If the player total argument is greater than zero, that means that the hosting activity was created from a saved state and has passed in
        //the saved state variables to be used in the fragment
        if(getArguments().getInt(ARG_PLAYER_TOTAL) > 0){
            mTotalPlayer = getArguments().getInt(ARG_PLAYER_TOTAL);
            mTotalDealer = getArguments().getInt(ARG_DEALER_TOTAL);
            //If the dealer's total is over zero, the game must have ended before the device was rotated
            if(mTotalDealer > 0){
                mGameInProgress = false;
            }
            //otherwise the user is still in the middle of playing a game
            else{
                mGameInProgress = true;
            }

            mCurrentCardResID = getArguments().getInt(ARG_CURRENT_CARD);
            //Given the id of the Card needing to be displayed, search through the new deck and find the card to be displayed and initialize the deck index
            //to that card to resume the game
            for(int i = 0; i < mDeck.length; ++i){
                if(mDeck[i].getResID() == mCurrentCardResID){
                    mDrawnCard.setImageResource(mDeck[i].getResID());
                    mDeckIndex = i;
                }
            }

        }
        //if the fragment is being freshly created, initialize everything accordingly
        else{
            mTotalDealer = 0;//dealer does not have a total yet as the user goes first
            mDeckIndex = 0;//index begins at the start of the deck
            mTotalPlayer += mDeck[0].getValue();//user starts with one card dealt, that card being the first in the deck
            mGameInProgress = true;//signify that a game has started
            mDrawnCard.setImageResource(mDeck[mDeckIndex].getResID());//show the first card
            mCurrentCardResID = mDeck[mDeckIndex].getResID();//update the what the resId of the current card is
            ++mDeckIndex;
        }
            //if the reset score option had not previously been selected in the OptionsFragment, set the scores
            //to the passed in score values
            if (!getArguments().getBoolean(ARG_RESET_SCORE)) {
                mScorePlayer = getArguments().getInt(ARG_PLAYER_SCORE);
                mScoreDealer = getArguments().getInt(ARG_DEALER_SCORE);
                mScoreDraw = getArguments().getInt(ARG_DRAW_SCORE);
            }
            //otherwise reset them
            else {
                mScorePlayer = 0;
                mScoreDealer = 0;
                mScoreDraw = 0;
            }

        //Instantiate fragment's views and update their displays
        mPlayerScore = (TextView) v.findViewById(R.id.player_score_display);
        mPlayerScore.setText("Your Score: " + mScorePlayer);

        mDealerScore = (TextView) v.findViewById(R.id.dealer_score_display);
        mDealerScore.setText("Dealer Score: " + mScoreDealer);

        mDrawScore = (TextView) v.findViewById(R.id.draw_display);
        mDrawScore.setText("Draws: " + mScoreDraw);

        mDealerTotal = (TextView) v.findViewById(R.id.dealer_total_display);
        mDealerTotal.setText("Dealer Total: " + mTotalDealer);

        mPlayerTotal = (TextView) v.findViewById(R.id.player_total_display);
        mPlayerTotal.setText("Your Total: " + mTotalPlayer);

        //set the card color to the appropriate setting
        mCardColor = getArguments().getString(ARG_CARD_COLOR);

        //instantiate the image view
        mDeckCard = (ImageView) v.findViewById(R.id.deck_card);

        //set the toast preference given in the argument
        mShowToasts = getArguments().getBoolean(ARG_SHOW_TOASTS);

        //set the face down card image corresponding to the set card color
        if(mCardColor.equals("Red")) {
            mDeckCard.setImageResource(R.drawable.red_card);
        }
        else{
            mDeckCard.setImageResource(R.drawable.blue_card);
        }


        //instantiate buttons and set onClickListeners to them
        mTakeButton = (Button) v.findViewById(R.id.take_button);

        //if the saved state was a finished game, disable this button
        if(!mGameInProgress){
            mTakeButton.setEnabled(false);
        }

        mTakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the index exceeds the Card array length, loop to the beginning of the deck
               if(mDeckIndex >= mDeck.length) {
                   mDeckIndex = 0;
               }
                //draw next card and add to player's total
                   ++mDeckIndex;
                   mDrawnCard.setImageResource(mDeck[mDeckIndex].getResID());
                   mTotalPlayer += mDeck[mDeckIndex].getValue();
                   mCurrentCardResID = mDeck[mDeckIndex].getResID();
                   //update the player's total display
                   mPlayerTotal.setText("Your Total: " + mTotalPlayer);


               //if user's total goes over 21, their turn is over and they can't decide to hold or take anymore
               if(mTotalPlayer > 21){
                   //if the user has set toasts to be on, show result
                   if(mShowToasts) {
                       provideMessage(mShowToasts, "Bust!");
                   }
                   //disable these buttons so the user can't try to play the round any further
                   mTakeButton.setEnabled(false);
                   mHoldButton.setEnabled(false);
                   //dealer takes their turn
                   dealerTurn();
               }
            }
        });

        mHoldButton = (Button) v.findViewById(R.id.hold_button);
        //if the saved state was a finished game, disable this button
        if(!mGameInProgress){
            mHoldButton.setEnabled(false);
        }

        mHoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user has signaled that their turn is done so disable buttons and let dealer take turn
                mTakeButton.setEnabled(false);
                mHoldButton.setEnabled(false);
                dealerTurn();
                mGameInProgress = false;
            }
        });

        mNewGameButton = (Button) v.findViewById(R.id.new_game_button);
        //This button will not appear unless current game is finished
        if(mGameInProgress){
            mNewGameButton.setVisibility(View.GONE);
        }

        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reset the totals for the new round
                mTotalPlayer = 0;
                mTotalDealer = 0;
                //reshuffle the deck array
                shuffleDeck();
                //start index back at the beginning of the deck
                mDeckIndex = 0;
                //reenable the buttons for the user to take their turn
                mHoldButton.setEnabled(true);
                mTakeButton.setEnabled(true);
                //deal first card to the user
                ++mDeckIndex;
                mDrawnCard.setImageResource(mDeck[mDeckIndex].getResID());
                mTotalPlayer += mDeck[mDeckIndex].getValue();
                mCurrentCardResID = mDeck[mDeckIndex].getResID();
                //update total displays
                mPlayerTotal.setText("Your Total: " + mTotalPlayer);
                mDealerTotal.setText("Dealer Total: " + mTotalDealer);
                //remove these buttons during gameplay
                mNewGameButton.setVisibility(View.GONE);
                mBackButton.setVisibility(View.GONE);
                //a game has now been started
                mGameInProgress = true;
            }
        });

        mBackButton = (Button) v.findViewById(R.id.back_button);
        //button will not be visible during the game
        if(mGameInProgress){
            mBackButton.setVisibility(View.GONE);
        }
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when user wants to return to the home screen, GameActivity will be destroyed and will need to send a
                //result to the WelcomeFragment, so set GameActivity's results with the score variables
                Intent data = new Intent();
                data.putExtra(EXTRA_PLAYER_RESULT, mScorePlayer);
                data.putExtra(EXTRA_DEALER_RESULT, mScoreDealer);
                data.putExtra(EXTRA_DRAW_RESULT, mScoreDraw);
                getActivity().setResult(Activity.RESULT_OK, data);
                //destroy GameActivity
                getActivity().finish();
            }
        });
        return v;
    }

    /**
     * Shuffles the Card array that represents the card deck
     */
    private void shuffleDeck(){
        int i = 0, x, y;
        Card temp;
        Random rand = new Random();
        while(i < 100000){
            x = rand.nextInt(mDeck.length - 1);
            y = rand.nextInt(mDeck.length - 1);
            temp = mDeck[x];
            mDeck[x] = mDeck[y];
            mDeck[y] = temp;
            ++i;
        }
    }

    /**
     * This function contains the logic for the AI dealer to take their turn
     */
    private void dealerTurn(){
        //dealer will choose to take a card until they reach a total of 17
        while(mTotalDealer < 17){
            //update the dealer's total and the corresponding displays
            ++mDeckIndex;
            mTotalDealer += mDeck[mDeckIndex].getValue();
            mDrawnCard.setImageResource(mDeck[mDeckIndex].getResID());
            mCurrentCardResID = mDeck[mDeckIndex].getResID();
            mDealerTotal.setText("Dealer Total: " + mTotalDealer);
        }
        //if the dealer goes above 21, they have busted so show the toast is the settings allow
        if(mTotalDealer > 21){
            provideMessage(mShowToasts, "Dealer busted!");
        }
        //evaluate the results of the round once both turns are complete
        decideWinner();
    }

    /**
     * This function evaluates the totals and decides who won the round
     */
    private void decideWinner(){
        //if the dealer busted and the user didn't, user wins
        if(mTotalDealer > 21 && mTotalPlayer <= 21){
            //show results, increment user's score and update display
            provideMessage(mShowToasts, "You won!");
            mScorePlayer++;
            mPlayerScore.setText("Your Score: " + mScorePlayer);
        }
        //if user busted and dealer didn't, dealer wins
        else if(mTotalPlayer > 21 && mTotalDealer <= 21){
            provideMessage(mShowToasts, "Dealer won!");
            mScoreDealer++;
            mDealerScore.setText("Dealer Score: " + mScoreDealer);
        }
        //if both players busted, they tie
        else if(mTotalPlayer > 21 && mTotalDealer > 21){
            provideMessage(mShowToasts, "You both busted, it's a draw!");
            mScoreDraw++;
            mDrawScore.setText("Draws: " + mScoreDraw);
        }

        //if no one busted, check the totals
        else{
            //if user has higher score without exceeding 21, they win
            if(mTotalPlayer > mTotalDealer){
                provideMessage(mShowToasts, "You won!");
                mScorePlayer++;
                mPlayerScore.setText("Your Score: " + mScorePlayer);
            }
            //if dealer has higher score without exceeding 21, they win
            else if(mTotalDealer > mTotalPlayer){
                provideMessage(mShowToasts, "Dealer won!");
                mScoreDealer++;
                mDealerScore.setText("Dealer Score: " + mScoreDealer);
            }
            //otherwise the players have tied
            else{
                provideMessage(mShowToasts, "It's a draw!");
                mScoreDraw++;
                mDrawScore.setText("Draws: " + mScoreDraw);
            }
        }
        //since round has ended, show the other buttons
        mNewGameButton.setVisibility(View.VISIBLE);
        mBackButton.setVisibility(View.VISIBLE);
    }

    /**
     * This creates and displays a toast if the user has decided to show toasts
     * @param toast

     * @param message
     */
    private void provideMessage(boolean toast, String message){
        if(toast){
            Toast toastMessage = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            toastMessage.show();
        }
    }

    //These getter functions are used by GameActivity when saving its state
    public int getTotalPlayer() {
        return mTotalPlayer;
    }

    public int getTotalDealer() {
        return mTotalDealer;
    }

    public int getCurrentCardResID() {
        return mCurrentCardResID;
    }

    public int getScorePlayer() {
        return mScorePlayer;
    }

    public int getScoreDraw() {
        return mScoreDraw;
    }

    public int getScoreDealer() {
        return mScoreDealer;
    }
}
