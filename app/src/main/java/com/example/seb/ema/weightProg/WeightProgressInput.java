package com.example.seb.ema.weightProg;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seb.ema.Main2Activity;
import com.example.seb.ema.MainActivity;
import com.example.seb.ema.R;
import com.example.seb.ema.fragmentpagerefresh.mWeightProgress;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeightProgressInput extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private Context context;
    private String date;
    SharedPreferences sp;
    static private List<mWeightProgress>mWeightProgressList= new ArrayList<>();
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_progress_input);

        Button button =findViewById(R.id.mButtonWeightProgressIn);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();

        DatabaseReference mRef=database.getReference().child("weightProgress");
        sp = getSharedPreferences("EMA", Context.MODE_PRIVATE);

     //   mToJsunon();
        context=this;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText weightIn = findViewById(R.id.mEditTextWeightPIn);
                EditText noteIn = findViewById(R.id.mEditTextNotePIn);
              //  mToJson();
                Calendar calendar = Calendar.getInstance();
                Date d1 = calendar.getTime();
                if(date==null){
                    Toast.makeText(WeightProgressInput.this, "Datum eingeben",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                try{
                    double weight= Double.parseDouble(weightIn.getText().toString());

                    if(weight>500){
                        Toast.makeText(WeightProgressInput.this, "Unzulässiges gewicht",
                                Toast.LENGTH_LONG).show();

                        return;
                    }
                    mWeightProgressList.add(new mWeightProgress(weight,date,noteIn.getText().toString()));
                }catch (Exception e){
                    e.printStackTrace();
                }



              //  mRef.child(user.getUid()).removeValue();
                mRef.child(user.getUid()).setValue(mWeightProgressList);

                Gson gson = new Gson();
                String string= gson.toJson(mWeightProgressList);


                SharedPreferences.Editor edit= sp.edit();

                edit.putString("liste",string);
                edit.apply();
                Log.v("454", string);
            }
        });

        Button button1= findViewById(R.id.mButtonWpDateIn);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Calendar c = Calendar.getInstance();
               int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                button1.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                                date=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        mToJson();

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
    private void mToJson(){
        Gson gson = new Gson();



        String string = sp.getString("liste","Fail");

        Log.v("123", string);
        ObjectMapper mapper = new ObjectMapper();
        try{
            List<mWeightProgress> myObjects = mapper.readValue(string, new TypeReference<List<mWeightProgress>>(){});
            mWeightProgressList.clear();
            mWeightProgressList.addAll(myObjects);

        }catch (Exception e){
            Log.v("KEYIS", "FEHLERLAWERAERTEATAt");

            e.printStackTrace();
        }

    }
}

