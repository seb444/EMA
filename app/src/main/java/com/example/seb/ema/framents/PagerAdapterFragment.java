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
    private TextView textView;
    private Button button;
    private ArrayList<Utils.DummyItem> dummyItems;
    private static ArrayList<Utils.DummyItem> dummyItems2;

    private  static  ArrayList<String> images=new ArrayList<>();
    private static ArrayList<String> andre=new ArrayList<>();
    Activity activity;

    /* Avoid non-default constructors in fragments: use a default constructor plus Fragment.setArguments(Bundle) instead and use Type value = getArguments().getType("key") to retrieve back the values in the bundle in onCreateView()*/
    public PagerAdapterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample,container, false);
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
                dummyItems.clear();
                textView=getActivity().findViewById(R.id.teeeest);
                dummyItems2.add(new Utils.DummyItem("TEST","TEST"));//textView.getText().toString()
                images.add("esaiofeawiofaw");
                Utils.setImageThumbUrls(images);
                Utils.setImageUrls(images);
                dummyItems.addAll(Utils.getThumbImageList());
                mPagerAdapter.notifyDataSetChanged();




                break;
            case R.id.button_full:
                dummyItems.clear();
                dummyItems.addAll(Utils.getFullImageList());
                mPagerAdapter.notifyDataSetChanged();
                break;

        }
    }
}
