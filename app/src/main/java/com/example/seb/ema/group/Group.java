package com.example.seb.ema.group;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.seb.ema.Main2Activity;
import com.example.seb.ema.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Group extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference mRef;
    private DatabaseReference myRef;
    private FirebaseUser user;
    String username;
    ScrollView scrollView;
    LinearLayout linearLayout;
    Context context;
    private Map<String, String> map;
    private String grouppath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        mRef=database.getReference();
        myRef=database.getReference();
        user=auth.getCurrentUser();
        Button button= findViewById(R.id.mButtonGroupIn);





        scrollView = (ScrollView) findViewById(R.id.mScrollViewGroup);
        linearLayout = findViewById(R.id.mLinearLayoutGroup);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        context=this;

        EditText editText=findViewById(R.id.mEditTextGroupIn);
        String string=editText.getText().toString();
        mRef.child("userGroups").child(user.getUid()).addValueEventListener(new ValueEventListener() {
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
                try {
                    map = (Map) dataSnapshot.getValue();
                }catch (Exception e){
                    return;
                }
                if(map==null){
                    Log.d("1000", "FEHLER");
                    return;
                }
                for(Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    Button button1 = new Button(context);
                    button1.setText(String.format(key));
                    button1.setBackground(ContextCompat.getDrawable(context,R.drawable.buttonshape));
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Group.this, SingleGroupView.class);
                            intent.putExtra("Path", button1.getText().toString());
                            startActivity(intent);
                        }
                    });

                    linearLayout.addView(button1);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=findViewById(R.id.mEditTextGroupIn);
                String string=editText.getText().toString();

                if(string.isEmpty()){
                    Toast.makeText(Group.this, "Groupname missing",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("string",string+ username);


                mRef.child("groups").child(string).child(user.getUid()).setValue(username);

                mRef.child("userGroups").child(user.getUid()).child(string).setValue("false");

            }
        });


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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}
