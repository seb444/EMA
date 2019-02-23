package com.example.seb.ema.fragments;

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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.example.seb.ema.adapter.MyPagerAdapter;
import com.example.seb.ema.Utils.Utils;
import com.example.seb.ema.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

public class PagerAdapterFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "PagerAdapterFragment";
    private MyPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private static int i = 0;
    private ArrayList<Utils.TrainingPlan> trainingPlans=new ArrayList<>();;
    private static ArrayList<Utils.TrainingPlan> dummyItems2 =new ArrayList<>();

    private Context context;

    Calendar c, c2;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    ViewPager vp;
    Button btnDatePicker, btnTimePicker;
    FloatingActionButton btnDeleteAllTps;
    int mYear, mMonth, mDay;


    public PagerAdapterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trainings_plan_input, container, false);
        FloatingActionButton buttonThumb = rootView.findViewById(R.id.mButtonShowIn);
        btnDatePicker = rootView.findViewById(R.id.mStartDateIn);
        btnTimePicker = rootView.findViewById(R.id.mEndDateIn);
        btnDeleteAllTps=rootView.findViewById(R.id.mButtonDeleteAllTP);

        context = getContext();


        btnDeleteAllTps.setOnClickListener(this);
        buttonThumb.setOnClickListener(this);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        user =mAuth.getCurrentUser();

        mViewPager = rootView.findViewById(R.id.viewpager);

        mPagerAdapter = new MyPagerAdapter( trainingPlans, context);
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

                GenericTypeIndicator<List<Utils.TrainingPlan>> t = new GenericTypeIndicator<List<Utils.TrainingPlan>>() {};

                try{

                    trainingPlans=(ArrayList<Utils.TrainingPlan>) dataSnapshot.getValue(t);

                    if(trainingPlans==null) trainingPlans= new ArrayList<>();

                    mPagerAdapter = new MyPagerAdapter( trainingPlans, context);
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
                        (view, year, monthOfYear, dayOfMonth) -> btnDatePicker.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year), mYear, mMonth, mDay);
                datePickerDialog.show();

                break;

            case R.id.mEndDateIn:
                //Get current date

                c2 = Calendar.getInstance();
                mYear = c2.get(Calendar.YEAR);
                mMonth = c2.get(Calendar.MONTH);
                mDay = c2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog2 = new DatePickerDialog(context,
                        (view, year, monthOfYear, dayOfMonth) -> btnTimePicker.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year), mYear, mMonth, mDay);
                datePickerDialog2.show();

                break;

            case R.id.mButtonDeleteAllTP:

                myRef.removeValue();

                break;

            case R.id.mButtonShowIn:

                    //Get all Input Trainingsplan
                    TextView enIn = v.getRootView().findViewById(R.id.mExerciseNIn);
                    TextView weightIn = v.getRootView().findViewById(R.id.mWeightIn);
                    TextView setsIn = v.getRootView().findViewById(R.id.mSetsIn);
                    Button startDateIn = v.getRootView().findViewById(R.id.mStartDateIn);
                    Button endDateIn = v.getRootView().findViewById(R.id.mEndDateIn);
                    TextView increaseWeightTimeIn = v.getRootView().findViewById(R.id.mIncreaseWeightTimeIn);
                    TextView weightIncreaseIn = v.getRootView().findViewById(R.id.mIncreaseWeightIn);

                    String startI = startDateIn.getText().toString();
                    String endI = endDateIn.getText().toString();

                    if(startI.equals("Pick start date")|| endI.equals("Pick end date")){
                        Toast.makeText(context, "Enter Date",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        //Create new Object and notify Paderadapter about change
                        dummyItems2.clear();
                        dummyItems2.add(new Utils.TrainingPlan(enIn.getText().toString(), Double.parseDouble(weightIn.getText().toString()), i++, Integer.parseInt(setsIn.getText().toString()), startI, endI,
                                        Double.parseDouble(increaseWeightTimeIn.getText().toString()), Double.parseDouble(weightIncreaseIn.getText().toString()), "s"));


                        trainingPlans.addAll(dummyItems2);
                        mPagerAdapter.notifyDataSetChanged();
                    }catch(Exception ex){
                        Toast.makeText(context, "Enter all fields",
                                Toast.LENGTH_LONG).show();

                        return;
                    }


                    myRef.setValue(trainingPlans);


                    //Remove previous Input
                    enIn.setText("");
                    weightIn.setText("");
                    setsIn.setText("");
                    startDateIn.setText("Pick start date");
                    endDateIn.setText("Pick end date");
                    increaseWeightTimeIn.setText("");
                    weightIncreaseIn.setText("");
                }
        }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
