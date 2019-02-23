package com.example.seb.ema.weightProg;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seb.ema.R;
import com.example.seb.ema.Utils.mWeightProgress;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeightProgressInput extends AppCompatActivity {

    private FirebaseUser user;
    private Context context;
    private String date;
    static private List<mWeightProgress>mWeightProgressList= new ArrayList<>();

    SharedPreferences sp;
    FirebaseDatabase database;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_progress_input);

        Button button =findViewById(R.id.mButtonWeightProgressIn);
        Button button1= findViewById(R.id.mButtonWpDateIn);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();

        DatabaseReference mRef=database.getReference().child("weightProgress");
        sp = getSharedPreferences("EMA", Context.MODE_PRIVATE);

        context=this;


        button.setOnClickListener(v -> {
            EditText weightIn = findViewById(R.id.mEditTextWeightPIn);
            EditText noteIn = findViewById(R.id.mEditTextNotePIn);

            if(date==null){
                Toast.makeText(WeightProgressInput.this, "Enter date",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if(mWeightProgressList.stream().anyMatch(a -> a.getDate().equals(date))){
                Toast.makeText(WeightProgressInput.this, "An entry already exists on this date.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            try{
                double weight= Double.parseDouble(weightIn.getText().toString());

                if(weight>300){
                    Toast.makeText(WeightProgressInput.this, "Weight too high",
                            Toast.LENGTH_LONG).show();

                    return;
                }


                mWeightProgressList.add(new mWeightProgress(weight,date,noteIn.getText().toString()));
            }catch (Exception e){
                e.printStackTrace();
            }

            mRef.child(user.getUid()).setValue(mWeightProgressList);

            Gson gson = new Gson();
            String string= gson.toJson(mWeightProgressList);

            SharedPreferences.Editor edit= sp.edit();

            edit.putString("liste",string);
            edit.apply();

            Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
            startActivity(intent);
            finish();
        });


        button1.setOnClickListener(v -> {
           Calendar c = Calendar.getInstance();
           int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year, monthOfYear, dayOfMonth) -> {

                        button1.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                        date=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
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

        String string = sp.getString("liste","Fail");

        ObjectMapper mapper = new ObjectMapper();
        try{
            List<mWeightProgress> myObjects = mapper.readValue(string, new TypeReference<List<mWeightProgress>>(){});
            mWeightProgressList.clear();
            mWeightProgressList.addAll(myObjects);

        }catch (Exception e){
            Log.v("4451", "E in toJson");

            e.printStackTrace();
        }

    }
}

