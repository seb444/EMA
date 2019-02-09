package com.example.seb.ema.weightProg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seb.ema.R;
import com.example.seb.ema.fragmentpagerefresh.DataPointCompare;
import com.jjoe64.graphview.series.DataPoint;
import com.example.seb.ema.fragmentpagerefresh.mWeightProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class WeightProgress extends Fragment {
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private LineGraphSeries<DataPoint> mSeries1;
    private double graph2LastXValue = 5d;
    static int is=0;
    static private List<mWeightProgress>mWeightProgressList= new ArrayList<>();
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    DatabaseReference mRef;

    ScrollView scrollView;
    LinearLayout linearLayout;
    View mRootview;
    GraphView graph;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_weight_progress, container, false);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        graph= (GraphView) rootView.findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<>(generateData());
        graph.addSeries(mSeries1);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));



        Button button = rootView.findViewById((R.id.myBtnWeightProgress));
        Button buttonWl= rootView.findViewById(R.id.mButtonRemoveWeightList);
        scrollView = (ScrollView) rootView.findViewById(R.id.mScrollView);
        linearLayout = rootView.findViewById(R.id.mLinearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* DataPoint []dataPoint =new DataPoint[]{new DataPoint(d1,1),new DataPoint(d2,2)};
                mSeries1.resetData(dataPoint);

                Button button1 = new Button(getActivity());
                button1.setText("Some text");
                linearLayout.addView(button1);
*/
                Intent intent = new Intent(getContext(), WeightProgressInput.class);
                startActivity(intent);
                getActivity().finish();


            }
        });

        buttonWl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        database.getReference().child("weightProgress/"+user.getUid()).removeValue();
                        linearLayout.removeAllViews();

                        DatabaseReference mRef=database.getReference().child("weightProgress");
                        SharedPreferences sp = Objects.requireNonNull(getContext()).getSharedPreferences("EMA", Context.MODE_PRIVATE);

                        sp.edit().remove("liste").apply();

            }
        });



        mRootview=rootView;

        return rootView;
    }



    @Override
    public void onResume() {

       /*
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                mSeries1.resetData(generateData());
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer1, 300);*/
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();


        mRef=database.getReference().child("weightProgress/"+user.getUid());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();
                 is=0;
/*
                Button button1 = new Button(mRootview.getContext());
                button1.setText(dataSnapshot.getValue(String.class));

                TextView test= mRootview.findViewById(R.id.Mtest);
                test.setText(dataSnapshot.getValue(String.class));
                linearLayout.addView(button1);*/
                GenericTypeIndicator<List<mWeightProgress>> t = new GenericTypeIndicator<List<mWeightProgress>>() {};

                mWeightProgressList=dataSnapshot.getValue(t);
                if(mWeightProgressList==null) return;
                DataPoint []dataPoint =new DataPoint[mWeightProgressList.size()];
                Date start=d1;
                Date end=d2;
                List<DataPoint> dataPoints = new ArrayList<>();

                    for (mWeightProgress m:mWeightProgressList) {
                        if(m==null) return;
                        Button button1 = new Button(mRootview.getContext());
                        button1.setText(String.format("%.2f", m.getWeight())+"\t"+m.getNote());


                        linearLayout.addView(button1);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                        try {
                            Log.v("454", m.getDate());
                            Date date= format.parse(m.getDate());
                            if(is==0)start=date;
                            if(is==mWeightProgressList.size()-1) end=date;
                             dataPoint[is]= new DataPoint(date,m.getWeight());
                            Log.v("DATE", date.toString());
                           // mSeries1.appendData(new DataPoint(date, getRandom()), true, 40);
                            Log.v("START", start.toString());

                            Log.v("ENDE",end.toString());

                             is++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                Arrays.sort(dataPoint,new DataPointCompare());
                graph.getViewport().setMinX(dataPoint[0].getX());
                graph.getViewport().setMaxX(dataPoint[dataPoint.length-1].getX());
                graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
                graph.getGridLabelRenderer().setHumanRounding(true);

                mSeries1.resetData(dataPoint);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSeries1.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });


        super.onResume();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);


        super.onPause();
    }

    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double f = mRand.nextDouble()*0.15+0.3;
            double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }
}
