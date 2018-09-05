package com.csci448.tkarol_a1.tmkarol_a2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by tkarol on 2/28/18.
 * This fragment serves as a Home Page view for the app
 */

public class WelcomeFragment extends Fragment {
    //request codes that will identify which activity is returning the intent
    private static final int REQUEST_OPTIONS = 1;
    private static final int REQUEST_SCORES = 0;

    //fragment arguments that will be used in other activities that get created
    private static final String ARG_PLAYER_SCORE = "player_score";
    private static final String ARG_DEALER_SCORE = "dealer_score";
    private static final String ARG_DRAW_SCORE = "draw_score";
    private static final String ARG_CARD_COLOR = "card_color";
    private static final String ARG_RESET_SCORE = "reset_score";
    private static final String ARG_SHOW_TOASTS = "show_toasts";

    //these are the buttons that are present on this fragment's layout and will be interacted with by the user
    private Button mPlayButton;
    private Button mOptionsButton;
    private Button mQuitButton;

    /**
     * This function is used instead of a constructor when the fragment is created so that fragment arguments
     * can be added to the instance before it is attached to the WelcomeActivity
     * @return
     */
    public static WelcomeFragment newInstance(){
        Bundle args = new Bundle();
        args.putInt(ARG_PLAYER_SCORE, 0);
        args.putInt(ARG_DEALER_SCORE, 0);
        args.putInt(ARG_DRAW_SCORE, 0);
        args.putString(ARG_CARD_COLOR, "Red");
        args.putBoolean(ARG_RESET_SCORE, false);
        args.putBoolean(ARG_SHOW_TOASTS, true);
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the fragment view
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);

        //instantiate and set onClickListeners for the buttons
        mPlayButton = (Button) v.findViewById(R.id.play_button_element);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start GameActivity that will bring up game play, given the specified settings and ongoing scores
                Intent intent = GameActivity.newIntent(getContext(), getArguments().getInt(ARG_PLAYER_SCORE), getArguments().getInt(ARG_DEALER_SCORE), getArguments().getInt(ARG_DRAW_SCORE), getArguments().getString(ARG_CARD_COLOR), getArguments().getBoolean(ARG_RESET_SCORE), getArguments().getBoolean(ARG_SHOW_TOASTS));
                startActivityForResult(intent, REQUEST_SCORES);
            }
        });

        mOptionsButton = (Button) v.findViewById(R.id.options_button_element);
        mOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start OptionsActivity that will present the user with game settings, given the previously set settings so that they will not be reset
                Intent intent = OptionsActivity.newIntent(getContext(), getArguments().getString(ARG_CARD_COLOR), getArguments().getBoolean(ARG_SHOW_TOASTS, false));
                startActivityForResult(intent, REQUEST_OPTIONS);
            }
        });

        mQuitButton = (Button) v.findViewById(R.id.quit_button_element);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //exit the app
                System.exit(0);
            }
        });
        return v;
    }

    /**
     * This function resets the Fragment arguments based on what intents are returned by the other activities
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //if the given result is defective, don't use it
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        //if the result is coming from GameActivity, update score related arguments
        else if(requestCode == REQUEST_SCORES){
            //if there is no intent, don't try to access it
            if(data == null){
                return;
            }
            getArguments().putInt(ARG_PLAYER_SCORE,GameActivity.playerScoreResult(data));
            getArguments().putInt(ARG_DEALER_SCORE,GameActivity.dealerScoreResult(data));
            getArguments().putInt(ARG_DRAW_SCORE,GameActivity.drawScoreResult(data));
            getArguments().putBoolean(ARG_RESET_SCORE, false);

        }

        //if the result is coming from OptionsActivity, update the setting related arguments
        else if(requestCode == REQUEST_OPTIONS){
            if(data == null){
                return;
            }
            getArguments().putString(ARG_CARD_COLOR, OptionsActivity.getColorResult(data));
            getArguments().putBoolean(ARG_RESET_SCORE, OptionsActivity.getResetResult(data));
            getArguments().putBoolean(ARG_SHOW_TOASTS, OptionsActivity.getShowToasts(data));
        }
    }

}
