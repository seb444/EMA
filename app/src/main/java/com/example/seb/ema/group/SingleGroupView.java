package com.example.seb.ema.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seb.ema.MapsActivity;
import com.example.seb.ema.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleGroupView extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference mRef;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private LinearLayout linearLayout;
    private Context context;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_group_view);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        mRef=database.getReference();
        myRef=database.getReference();
        user=auth.getCurrentUser();
        linearLayout = findViewById(R.id.mLinearLayoutSingleGroup);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        context=this;
        String string = getIntent().getStringExtra("Path");

        Button button = findViewById(R.id.mButtonSingleGroupLeave);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child("groups").child(string).child(user.getUid()).removeValue();
                mRef.child("userGroups").child(user.getUid()).child(string).removeValue();

                Intent intent = new Intent(getApplicationContext(), Group.class);
                startActivity(intent);
                finish();
            }
        });

        mRef.child("groups").child(string).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // grouppath = dataSnapshot.getValue(String.class);



/*
                Button button1 = new Button(mRootview.getContext());
                button1.setText(dataSnapshot.getValue(String.class));

                TextView test= mRootview.findViewById(R.id.Mtest);
                test.setText(dataSnapshot.getValue(String.class));
                linearLayout.addView(button1);*/
                linearLayout.removeAllViews();
               Map<String,String> map  = (Map) dataSnapshot.getValue();
                if(map==null) {
                    Log.d("1001", "FEHLER");
                    return;
                }
                for(Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    Button button1 = new Button(context);
                    button1.setText(String.format(value));
                    button1.setBackgroundColor(Color.RED);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("1001", key+"\n"+user.getUid());
                            if(key.equals(user.getUid())) {
                                button1.setBackgroundColor(Color.GREEN);
                                mRef.child("userReady").child(string).child(value).setValue("true");
                                mRef.child("userGroups").child(user.getUid()).child(string).setValue("true");
                            }

                        }
                    });
                    linearLayout.addView(button1);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("1003", string);
        mRef.child("userReady").child(string).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Map<String,String> map  = (Map) dataSnapshot.getValue();
                if(map==null){Log.d("1002", "FEHLER");
//                    Log.d("1001", dataSnapshot.getValue().toString());
                            return;}
                int i=0;
                int count=0;
                List<Button> buttons= new ArrayList<>();
                for(Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    Button button1 = new Button(context);
                    button1.setText(String.format(key));
                    button1.setBackgroundColor(Color.RED);

                    Log.d("1005", value+"\n"+key);
                    if(value.equals("true")) {
                        View btn = linearLayout.getChildAt(i);

                        btn.setBackgroundColor(Color.GREEN);

                        count++;
                    }

                    if(value.equals("false")) {
                        View btn = linearLayout.getChildAt(i);

                        btn.setBackgroundColor(Color.RED);
                    }
                    i++;
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!key.equals(username)) return;
                             mRef.child("userReady").child(string).child(button1.getText().toString()).setValue("true");

                            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                                View btn = linearLayout.getChildAt(i);
                                if (btn instanceof Button) {
                                    if(((Button) btn).getText().toString().toLowerCase().equals(username)){
                                        button.setBackgroundColor(Color.GREEN);
                                    }
                                }
                            }
                        }
                    });
                }
                if(count==map.size()){
                    for ( i = 0; i < linearLayout.getChildCount(); i++) {
                        View btn = linearLayout.getChildAt(i);
                        if (btn instanceof Button) {
                            mRef.child("userReady").child(string).child(((Button) btn).getText().toString()).setValue("false");
                        }
                    }


                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Group.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        myRef.child("users/"+user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                username=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onResume();
    }
}
