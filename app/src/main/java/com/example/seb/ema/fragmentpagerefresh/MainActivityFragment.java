package com.example.seb.ema.fragmentpagerefresh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.seb.ema.R;
import com.example.seb.ema.framents.FragmentMain;


public class MainActivityFragment extends FragmentActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //In the fragment activity If you want to show the action bar you have to put following request.
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_main_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new FragmentMain()).commit();
        }



        //android.app.ActionBar actionBar = getActionBar();
        //actionBar.show();


        //Navigation Modes have been depricated.
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

    }

}
