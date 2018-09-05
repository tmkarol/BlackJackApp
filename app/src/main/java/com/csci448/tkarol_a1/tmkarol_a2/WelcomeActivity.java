package com.csci448.tkarol_a1.tmkarol_a2;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * This is the root activity for this app that creates the Welcome Fragment
 */
public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This sets up for the WelcomeFragment view to be inflated in the empty container layout
        setContentView(R.layout.fragment_container);
        //This introduces the fragment manager that will handle the transitions of fragments
        FragmentManager fm = getSupportFragmentManager();
        //Check to see if there is already a fragment in place
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        //if the container is empty, fill it with a WelcomeFragment
        if(fragment == null){
            fragment = WelcomeFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }


}
