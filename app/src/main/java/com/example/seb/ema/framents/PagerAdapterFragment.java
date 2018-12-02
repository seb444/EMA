package com.example.seb.ema.framents;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public static boolean first;
    public static boolean realfirst;
   // private  TextView textView;
    private FloatingActionButton button;
    private static int i=0;
    private ArrayList<Utils.DummyItem> dummyItems;
    private static ArrayList<Utils.DummyItem> dummyItems2;
    private  static  ArrayList<String> images=new ArrayList<>();
    private static ArrayList<Double> weights=new ArrayList<>();
    Activity activity;
    public static ViewPager vp;

    /* Avoid non-default constructors in fragments: use a default constructor plus Fragment.setArguments(Bundle) instead and use Type value = getArguments().getType("key") to retrieve back the values in the bundle in onCreateView()*/
    public PagerAdapterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  rootView = inflater.inflate(R.layout.fragment_sample,container, false);
        //rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        FloatingActionButton buttonThumb =  rootView.findViewById(R.id.mButtonShowIn);
      //  Button buttonFull = (Button) rootView.findViewById(R.id.button_full);
        buttonThumb.setOnClickListener(this);


       // buttonFull.setOnClickListener(this);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        dummyItems=new ArrayList<>();





        dummyItems2=new ArrayList<>();
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        button=rootView.findViewById(R.id.mButtonShowIn);
            //  textView=rootView.findViewById(R.id.teeeest);

        mPagerAdapter = new MyPagerAdapter(dummyItems, getActivity());
        mViewPager.setAdapter(mPagerAdapter);

        vp=mViewPager;

        return  rootView;
    }


    @Override
    public void onClick(View v) {
        FloatingActionButton btnSIn = v.getRootView().findViewById(R.id.mButtonShowIn);
       // FloatingActionButton btnAdd = v.getRootView().findViewById(R.id.mButtonAddTP);

        switch (v.getId()){



            case R.id.mButtonShowIn:

                if(first){


                        hideOutput(v);




                    showInput(v);




                    first=false;
                    break;
                }

                if(!first){

                    dummyItems.clear();
                    TextView enIn=v.getRootView().findViewById(R.id.mExerciseNIn);
                    TextView weightIn =v.getRootView().findViewById(R.id.mWeightIn);
                    TextView setsIn  =v.getRootView().findViewById(R.id.mSetsIn);



                   // dummyItems2.add(new Utils.DummyItem(enIn.getText().toString(),Double.parseDouble(weightIn.getText().toString()),i++,));//textView.getText().toString()
                    images.add(enIn.getText().toString()+1);

                    hideInput(v);
                    showOutput(v);
                    // Utils.setExerciseNames(images);
                    dummyItems.addAll(dummyItems2);
                    mPagerAdapter.notifyDataSetChanged();

                    Toast.makeText(this.getActivity(),enIn.getText().toString() ,
                            Toast.LENGTH_SHORT).show();




                    first=true;


                }


               // dummyItems.clear();
               // dummyItems.addAll(dummyItems2);


              //  mPagerAdapter.notifyDataSetChanged();
                break;

        }
    }

    public void hideInput(View v){
        TextView textView =  v.getRootView().findViewById(R.id.mWeightIn);
        TextView textView1=v.getRootView().findViewById(R.id.mExerciseNIn);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
    }

    public void showInput(View v){
        TextView textView = v.getRootView().findViewById(R.id.mWeightIn);
        TextView textView1=v.getRootView().findViewById(R.id.mExerciseNIn);
        textView.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.VISIBLE);
    }

    public void hideOutput(View v){
            try{
                TextView textView = v.findViewById(R.id.mWeightOut);
              //  TextView textView1= v.getRootView().findViewById(R.id.title);
                textView.setVisibility(View.INVISIBLE);
             //   textView1.setVisibility(View.INVISIBLE);
            }catch(Exception e){}






    }

    public void showOutput(View v){
        try{
        TextView textView = v.getRootView().findViewById(R.id.mWeightOut);
        TextView textView1= v.getRootView().findViewById(R.id.title);
        textView.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.VISIBLE);
        }catch (Exception e){

        }
    }
}
