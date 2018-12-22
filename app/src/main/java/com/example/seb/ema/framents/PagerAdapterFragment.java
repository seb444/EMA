package com.example.seb.ema.framents;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.example.seb.ema.adapters.MyPagerAdapter;
import com.example.seb.ema.fragmentpagerefresh.Utils;
import com.example.seb.ema.R;
import com.example.seb.ema.fragmentpagerefresh.mWeightProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by noor on 10/04/15.
 */
public class PagerAdapterFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "PagerAdapterFragment";
    private MyPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private String test;
    public static boolean first;
    public static boolean realfirst;
    // private  TextView textView;
    private FloatingActionButton button;
    private static int i = 0;
    private ArrayList<Utils.TrainingPlan> trainingPlans=new ArrayList<>();;
    private static ArrayList<Utils.TrainingPlan> dummyItems2 =new ArrayList<>();
    private static ArrayList<Utils.TrainingPlan> dummyItems3 =new ArrayList<>();;
    private static ArrayList<String> images = new ArrayList<>();
    private static ArrayList<Double> weights = new ArrayList<>();
    Activity activity;
    public static ViewPager vp;
    // private DatabaseReference mDatabase;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context context;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar c, c2;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    Button btnDatePicker, btnTimePicker;


    /* Avoid non-default constructors in fragments: use a default constructor plus Fragment.setArguments(Bundle) instead and use Type value = getArguments().getType("key") to retrieve back the values in the bundle in onCreateView()*/
    public PagerAdapterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
        FloatingActionButton buttonThumb = rootView.findViewById(R.id.mButtonShowIn);
        btnDatePicker = rootView.findViewById(R.id.mStartDateIn);
        btnTimePicker = rootView.findViewById(R.id.mEndDateIn);

        buttonThumb.setOnClickListener(this);

        context = getContext();




        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        //Get firebase instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
         user =mAuth.getCurrentUser();

        mViewPager = rootView.findViewById(R.id.viewpager);

        mPagerAdapter = new MyPagerAdapter( trainingPlans, getActivity());
        mViewPager.setAdapter(mPagerAdapter);

        vp = mViewPager;

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };

        myRef= database.getReference().child("trainingPlan/"+user.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //   trainingPlans.clear();

                // Utils.setExerciseNames(images);
                //trainingPlans.addAll(dummyItems2);


                GenericTypeIndicator<List<Utils.TrainingPlan>> t = new GenericTypeIndicator<List<Utils.TrainingPlan>>() {};



               // if(trainingPlans==null) return;

                try{

                    trainingPlans=(ArrayList<Utils.TrainingPlan>) dataSnapshot.getValue(t);
                    if(trainingPlans==null) trainingPlans= new ArrayList<>();
                    mPagerAdapter = new MyPagerAdapter( trainingPlans, getActivity());
                    mViewPager.setAdapter(mPagerAdapter);

                    mPagerAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.mStartDateIn:

                //Get current date
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        (view, year, monthOfYear, dayOfMonth) -> btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
                datePickerDialog.show();

                break;

            case R.id.mEndDateIn:
                //Get current date

                c2 = Calendar.getInstance();
                mYear = c2.get(Calendar.YEAR);
                mMonth = c2.get(Calendar.MONTH);
                mDay = c2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog2 = new DatePickerDialog(context,
                        (view, year, monthOfYear, dayOfMonth) -> btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
                datePickerDialog2.show();

                break;

            case R.id.mButtonShowIn:


                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                    Date startIn;
                    Date endIn;
                    TextView enIn = v.getRootView().findViewById(R.id.mExerciseNIn);
                    TextView weightIn = v.getRootView().findViewById(R.id.mWeightIn);
                    TextView setsIn = v.getRootView().findViewById(R.id.mSetsIn);
                    Button startDateIn = v.getRootView().findViewById(R.id.mStartDateIn);
                    Button endDateIn = v.getRootView().findViewById(R.id.mEndDateIn);
                    TextView increaseWeightTimeIn = v.getRootView().findViewById(R.id.mIncreaseWeightTimeIn);
                    TextView weightIncreaseIn = v.getRootView().findViewById(R.id.mIncreaseWeightIn);

                    String startI = startDateIn.getText().toString();
                    String endI = endDateIn.getText().toString();
                        startIn = c.getTime();
                        endIn = c2.getTime();
                    dummyItems2.clear();
                    dummyItems2.add(new Utils.TrainingPlan(enIn.getText().toString(), Double.parseDouble(weightIn.getText().toString()), i++, Integer.parseInt(setsIn.getText().toString()), startIn, endIn, Double.parseDouble(increaseWeightTimeIn.getText().toString()), Double.parseDouble(weightIncreaseIn.getText().toString()), "s"));//textView.getText().toString()
                    images.add(enIn.getText().toString() + 1);


                    // Utils.setExerciseNames(images);


                    trainingPlans.addAll(dummyItems2);
                    mPagerAdapter.notifyDataSetChanged();

                    Toast.makeText(this.getActivity(), enIn.getText().toString(),
                            Toast.LENGTH_SHORT).show();


                    Map<String, String> user_info = new HashMap<String, String>();

                    user_info.put("uid",user.getUid());

                    myRef.setValue(trainingPlans);

                }
        }

    public void hideInput(View v) {
        TextView textView = v.getRootView().findViewById(R.id.mWeightIn);
        TextView textView1 = v.getRootView().findViewById(R.id.mExerciseNIn);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
    }

    public void showInput(View v) {
        TextView textView = v.getRootView().findViewById(R.id.mWeightIn);
        TextView textView1 = v.getRootView().findViewById(R.id.mExerciseNIn);
        textView.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.VISIBLE);
    }

    public void hideOutput(View v) {
        try {
            TextView textView = v.findViewById(R.id.mWeightOut);
            //  TextView textView1= v.getRootView().findViewById(R.id.title);
            textView.setVisibility(View.INVISIBLE);
            //   textView1.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
        }


    }

    public void showOutput(View v) {
        try {
            TextView textView = v.getRootView().findViewById(R.id.mWeightOut);
            TextView textView1 = v.getRootView().findViewById(R.id.title);
            textView.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
