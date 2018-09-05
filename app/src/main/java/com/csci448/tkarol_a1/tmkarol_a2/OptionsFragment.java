package com.csci448.tkarol_a1.tmkarol_a2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by tkarol on 2/28/18.
 */

public class OptionsFragment extends Fragment {
    //These fragment arguments are passed in so that the corresponding displays in the fragment will be
    //initialized properly
    private static final String ARG_COLOR = "card_color";
    private static final String ARG_SHOW_TOASTS = "show_toasts";
    private static final String ARG_RESET_PRESSED = "reset_pressed";

    //These extras will be passed in the resulting intent so that the WelcomeFragment can send the updated settings to
    //the next activity
    private static final String EXTRA_COLOR_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.color_result";
    private static final String EXTRA_RESET_RESULT = "com.csci448.tkarol_a1.tmkarol_a2.reset_result";
    private static final String EXTRA_SHOW_TOASTS = "com.csci448.tkarol_a1.tmkarol_a2.show_toasts";

    //These are elements in the fragment layout that will process user interaction
    private Spinner mColorSpinner;
    private Button mResetButton;
    private Button mBackButton;
    private Switch mShowToastsSwitch;
    private String mColorSelection;

    //This variable keeps track of whether the user clicked the reset score button
    private boolean mResetPressed;

    /**
     * This function is called by OptionsActivity to declare a new OptionsFragment rather than the constructor
     * so that the fragment arguments can be added to the fragment before it is attached.
     * @param color
     * @param show_toasts
     * @return
     */
    public static OptionsFragment newInstance(String color, boolean show_toasts, boolean reset_pressed){
        Bundle args = new Bundle();
        args.putString(ARG_COLOR, color);
        args.putBoolean(ARG_SHOW_TOASTS, show_toasts);
        args.putBoolean(ARG_RESET_PRESSED, reset_pressed);
        OptionsFragment fragment  = new OptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the fragment view into the container
        View v = inflater.inflate(R.layout.fragment_options, container, false);

        //instantiate the spinner element from the layout
        mColorSpinner = (Spinner) v.findViewById(R.id.color_spinner);
        //Declare an array adapter to control the spinner object's options and connect the adapter to the string array of color options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.colors_array, android.R.layout.simple_spinner_item);
        //set the style of the dropdown menu
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //connect the adapter to the spinner object
        mColorSpinner.setAdapter(adapter);
        //set the current option to be the option that was passed in as the argument
        mColorSpinner.setSelection(adapter.getPosition(getArguments().getString(ARG_COLOR)));
        //set a listener to the spinner
        mColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //when the user selects an item, assign that item to the color selection
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mColorSelection = adapterView.getItemAtPosition(i).toString();
            }
            //this class requires that this function be implemented, but there is no use for it, so leave it empty
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //instantiate the switch element from the fragment layout
        mShowToastsSwitch = (Switch) v.findViewById(R.id.show_toast_switch);
        //set it to be checked based on the argument passed in based on the user's previously made settings
        mShowToastsSwitch.setChecked(getArguments().getBoolean(ARG_SHOW_TOASTS));

        //this argument comes in handy when the user has pressed the reset score button before rotating the device, this way that choice isn't forgotten
        mResetPressed = getArguments().getBoolean(ARG_RESET_PRESSED);

        //instantiate and set listeners to the buttons on the fragment layout
        mResetButton = (Button) v.findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            //designate that the user wants the scores to be reset when pressed
            @Override
            public void onClick(View view) {
                mResetPressed = true;
            }
        });

        mBackButton = (Button) v.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when user wants to exit, set OptionsActivity's result intent with the current status of all the available options
                Intent intent = new Intent();
                intent.putExtra(EXTRA_COLOR_RESULT, mColorSelection);
                intent.putExtra(EXTRA_RESET_RESULT, mResetPressed);
                intent.putExtra(EXTRA_SHOW_TOASTS, mShowToastsSwitch.isChecked());
                getActivity().setResult(getActivity().RESULT_OK, intent);
                getActivity().finish();
            }
        });

        return v;
    }

    //These getter functions are used by OptionsActivity when saving its state
    public boolean isResetPressed() {
        return mResetPressed;
    }

    public String getColorSelection() {
        return mColorSelection;
    }

    public boolean isShowToastsChecked(){
        return mShowToastsSwitch.isChecked();
    }
}
