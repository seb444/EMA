package com.example.seb.ema.framents;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import com.example.seb.ema.adapters.MyPagerAdapter;
import com.example.seb.ema.fragmentpagerefresh.Utils;
import com.example.seb.ema.R;

/**
 * Created by noor on 10/04/15.
 */
public class PagerAdapterFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "PagerAdapterFragment";
    private MyPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private  String test;
   // private  TextView textView;
    private Button button;
    private static int i=0;
    private ArrayList<Utils.DummyItem> dummyItems;
    private static ArrayList<Utils.DummyItem> dummyItems2;
    private  static  ArrayList<String> images=new ArrayList<>();
    private static ArrayList<Double> weights=new ArrayList<>();
    Activity activity;


    /* Avoid non-default constructors in fragments: use a default constructor plus Fragment.setArguments(Bundle) instead and use Type value = getArguments().getType("key") to retrieve back the values in the bundle in onCreateView()*/
    public PagerAdapterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  rootView = inflater.inflate(R.layout.fragment_sample,container, false);
        //rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        Button buttonThumb = (Button) rootView.findViewById(R.id.button_thumb);
        Button buttonFull = (Button) rootView.findViewById(R.id.button_full);
        buttonThumb.setOnClickListener(this);
        buttonFull.setOnClickListener(this);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        dummyItems=new ArrayList<>();




        dummyItems.addAll(Utils.getThumbImageList());
        dummyItems2=new ArrayList<>();
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        button=rootView.findViewById(R.id.button_thumb);
            //  textView=rootView.findViewById(R.id.teeeest);

        mPagerAdapter = new MyPagerAdapter(dummyItems, getActivity());
        mViewPager.setAdapter(mPagerAdapter);



        return  rootView;
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.button_thumb:
                boolean bool=true;
                dummyItems.clear();
                TextView textView1=v.getRootView().findViewById(R.id.textViewExerciseN);
                TextView tv =v.getRootView().findViewById(R.id.textViewWeight);


                    dummyItems2.add(new Utils.DummyItem(textView1.getText().toString(),Double.parseDouble(tv.getText().toString()),i++));//textView.getText().toString()
                    images.add(textView1.getText().toString()+1);

                    // Utils.setExerciseNames(images);
                    dummyItems.addAll(dummyItems2);
                    mPagerAdapter.notifyDataSetChanged();

                    Toast.makeText(this.getActivity(),textView1.getText().toString() ,
                            Toast.LENGTH_SHORT).show();



                    hideInput(v);
                    showOutput(v);

                break;

            case R.id.button_full:
               // dummyItems.clear();
               // dummyItems.addAll(dummyItems2);
                hideOutput(v);
                showInput(v);
              //  mPagerAdapter.notifyDataSetChanged();
                break;

        }
    }

    public void hideInput(View v){
        TextView textView = v.getRootView().findViewById(R.id.textViewWeight);
        TextView textView1=v.getRootView().findViewById(R.id.textViewExerciseN);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
    }

    public void showInput(View v){
        TextView textView = v.getRootView().findViewById(R.id.textViewWeight);
        TextView textView1=v.getRootView().findViewById(R.id.textViewExerciseN);
        textView.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.VISIBLE);
    }

    public void hideOutput(View v){
        TextView textView = v.getRootView().findViewById(R.id.textViewWeightO);
        TextView textView1= v.getRootView().findViewById(R.id.title);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
    }

    public void showOutput(View v){
        TextView textView = v.getRootView().findViewById(R.id.textViewWeightO);
        TextView textView1= v.getRootView().findViewById(R.id.title);
        textView.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.VISIBLE);
    }
}
