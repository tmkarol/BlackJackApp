package com.csci448.tkarol_a1.tmkarol_a2;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tkarol on 2/28/18.
 * This activity hosts an OptionsFragment that will present the user with different settings for the game
 */

public class OptionsActivity extends AppCompatActivity {
    //These extras are settings related and are passed between activities throughout the lifetime of the app
    private static final String EXTRA_CARD_COLOR = "com.csci448.tkarol_a1.tmkarol_a2.card_color";
    private static final String EXTRA_COLOR_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.color_result";
    private static final String EXTRA_RESET_PRESSED = "com.csci448.tkarol_a1.tmkarol_a2.reset_pressed";
    private static final String EXTRA_RESET_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.reset_result";
    private static final String EXTRA_SHOW_TOASTS = "com.csci448.tkarol_a1.tmkarol_a2.show_toasts";

    private OptionsFragment fragment;

    /**
     * This function allows WelcomeFragment to create an intent that includes everything
     * OptionsActivity needs and is expecting
     * @param context
     * @param color
     * @param showToasts
     * @return
     */
    public static Intent newIntent(Context context, String color, boolean showToasts){
        Intent intent = new Intent(context, OptionsActivity.class);
        //These extras need to be passed in so the the OptionsFragment displays will not revert to the default settings
        intent.putExtra(EXTRA_CARD_COLOR, color);
        intent.putExtra(EXTRA_SHOW_TOASTS, showToasts);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //set the view to be the container layout
        setContentView(R.layout.fragment_container);

        //These variables act as placeholders that will be correctly assigned before being passed in as fragment arguments to OptionsFragment
        String colorOption;
        boolean reset_pressed, show_toasts;

        //If the activity has been started again resulting from a device rotation, assign the variables with the bundle
        if(savedInstanceState != null){
            colorOption = savedInstanceState.getString(EXTRA_CARD_COLOR);
            show_toasts = savedInstanceState.getBoolean(EXTRA_SHOW_TOASTS);
            reset_pressed = savedInstanceState.getBoolean(EXTRA_RESET_PRESSED);
        }
        //If the activity has been started from an intent from WelcomeFragment, assign the variables with the intent extras
        else{
            colorOption = getIntent().getStringExtra(EXTRA_CARD_COLOR);
            show_toasts = getIntent().getBooleanExtra(EXTRA_SHOW_TOASTS, false);
            reset_pressed = false;
        }

        //create new OptionsFragment that will be hosted
        fragment = OptionsFragment.newInstance(colorOption, show_toasts, reset_pressed);
        //switch out the current WelcomeFragment with the new OptionsFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

    }

    /**
     * This function is overriden to add extras to the bundle that can be used to restore the state of the activity and fragment
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(EXTRA_CARD_COLOR, fragment.getColorSelection());
        savedInstanceState.putBoolean(EXTRA_RESET_PRESSED, fragment.isResetPressed());
        savedInstanceState.putBoolean(EXTRA_SHOW_TOASTS, fragment.isShowToastsChecked());
    }

    //These functions return the extras that are stored in this activity's resulting intent
    public static String getColorResult(Intent result){
        return result.getStringExtra(EXTRA_COLOR_RESULT);
    }

    public static boolean getResetResult(Intent result){
        return result.getBooleanExtra(EXTRA_RESET_RESULT, false);
    }

    public static boolean getShowToasts(Intent result){
        return result.getBooleanExtra(EXTRA_SHOW_TOASTS, false);
    }

}
