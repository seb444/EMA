package com.example.seb.ema.weightProg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.seb.ema.R;
import com.example.seb.ema.fragments.DataPointCompare;
import com.jjoe64.graphview.series.DataPoint;
import com.example.seb.ema.Utils.mWeightProgress;
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
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class WeightProgress extends Fragment {
    private final Handler mHandler = new Handler();
    private LineGraphSeries<DataPoint> mSeries1;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private Context context;

    static int is=0;
    static private List<mWeightProgress>mWeightProgressList= new ArrayList<>();

    DatabaseReference mRef;
    ScrollView scrollView;
    LinearLayout linearLayout;
    View mRootview;
    GraphView graph;
    FirebaseAuth mAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_weight_progress, container, false);
        context=getContext();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();

        graph= rootView.findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<>();
        graph.addSeries(mSeries1);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));


        Button button = rootView.findViewById((R.id.myBtnWeightProgress));
        Button buttonWl= rootView.findViewById(R.id.mButtonRemoveWeightList);
        scrollView = rootView.findViewById(R.id.mScrollView);
        linearLayout = rootView.findViewById(R.id.mLinearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        button.setOnClickListener(v -> {

            Intent intent = new Intent(getContext(), WeightProgressInput.class);
            startActivity(intent);
            getActivity().finish();


        });

        buttonWl.setOnClickListener(v -> {
                    database.getReference().child("weightProgress/"+user.getUid()).removeValue();
                    linearLayout.removeAllViews();

                    SharedPreferences sp = Objects.requireNonNull(getContext()).getSharedPreferences("EMA", Context.MODE_PRIVATE);

                    sp.edit().remove("liste").apply();
                    mSeries1.resetData(new DataPoint[0]);

        });

        mRootview=rootView;

        return rootView;
    }



    @Override
    public void onResume() {

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);

        mRef=database.getReference().child("weightProgress/"+user.getUid());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();
                 is=0;

                GenericTypeIndicator<List<mWeightProgress>> t = new GenericTypeIndicator<List<mWeightProgress>>() {};

                mWeightProgressList=dataSnapshot.getValue(t);

                if(mWeightProgressList==null) return;

                DataPoint []dataPoint =new DataPoint[mWeightProgressList.size()];

                Date start=d1;
                Date end=d2;

                    for (mWeightProgress m:mWeightProgressList) {

                        if(m==null) return;

                        Button button1 = new Button(context);
                        button1.setText(String.format("%.2f", m.getWeight())+"\t\t\t"+m.getNote());
                        button1.setBackground(ContextCompat.getDrawable(context,R.drawable.buttonshape));

                        linearLayout.addView(button1);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                        try {
                            Log.v("454", m.getDate());
                            Date date= format.parse(m.getDate());
                            if(is==0)start=date;
                            if(is==mWeightProgressList.size()-1) end=date;
                             dataPoint[is]= new DataPoint(date,m.getWeight());
                            Log.v("DATE", date.toString());

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

        super.onPause();
    }

}
