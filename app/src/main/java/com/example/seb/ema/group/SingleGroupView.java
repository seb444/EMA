package com.example.seb.ema.group;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.seb.ema.Activities.MapsActivity;
import com.example.seb.ema.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SingleGroupView extends AppCompatActivity {

    private DatabaseReference mRef;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private LinearLayout linearLayout;
    private Context context;
    private String username;
    private static final String TAG = "SingleGroupView";

    FirebaseDatabase database;
    FirebaseAuth auth;


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
        linearLayout.setDividerDrawable(Drawable.createFromPath("@drawable/empty_tall_divider"));

        context=this;

        String string = getIntent().getStringExtra("Path");

        Button button = findViewById(R.id.mButtonSingleGroupLeave);

        button.setOnClickListener(v -> {
            mRef.child("groups").child(string).child(user.getUid()).removeValue();
            mRef.child("userGroups").child(user.getUid()).child(string).removeValue();
            mRef.child("userReady").child(string).child(username).removeValue();

            Intent intent = new Intent(getApplicationContext(), Group.class);
            startActivity(intent);
            finish();
        });

        mRef.child("groups").child(string).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                linearLayout.removeAllViews();
                Map<String,String> map  = (Map) dataSnapshot.getValue();

                if(map==null) {
                    Log.d("1453", "Map is null");
                    return;
                }
                for(Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    Button button1 = new Button(context);
                    button1.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    button1.setText(String.format(value));
                    button1.setBackgroundColor(Color.RED);


                    button1.setOnClickListener(v -> {
                        Log.d("1001", key+"\n"+user.getUid());
                        if(key.equals(user.getUid())) {
                            button1.setBackgroundColor(Color.GREEN);
                            mRef.child("userReady").child(string).child(value).setValue("true");
                            mRef.child("userGroups").child(user.getUid()).child(string).setValue("true");
                        }
                    });
                    linearLayout.addView(button1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mRef.child("userReady").child(string).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Map<String,String> map  = (Map) dataSnapshot.getValue();
                if(map==null){
                    Log.d("1612", "Map is null");

                    return;
                }
                int count=0;

                if(linearLayout.getChildCount()==0) return;

                for(Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    Button button1 = new Button(context);
                    button1.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    button1.setText(String.format(key));
                    button1.setBackgroundColor(Color.RED);

                    Log.d("1005", value+"\n"+key);

                    if(value.equals("true")) {

                            for(int j=0;j<linearLayout.getChildCount();j++){
                                View btn = linearLayout.getChildAt(j);

                                if(btn!=null&&((Button) btn).getText().toString().toLowerCase().equals(key)){
                                    btn.setBackgroundColor(Color.GREEN);
                                    count++;
                                }
                            }
                    }


                    button1.setOnClickListener(v -> {
                        Log.d("10088", button1.getText().toString().toLowerCase());

                        if(!(button1.getText().toString().toLowerCase().equals(username))) return;

                        for (int i1 = 0; i1 < linearLayout.getChildCount(); i1++) {
                            View btn = linearLayout.getChildAt(i1);
                            if (btn instanceof Button) {
                                if(((Button) btn).getText().toString().toLowerCase().equals(username)){
                                    button.setBackgroundColor(Color.GREEN);
                                }
                            }
                        }

                        mRef.child("userReady").child(string).child(button1.getText().toString()).setValue("true");

                        Log.d("998", "richtig "+button1.getText().toString().toLowerCase());
                    });
                }

                if(count==map.size()){
                    for ( int i = 0; i < linearLayout.getChildCount(); i++) {
                        View btn = linearLayout.getChildAt(i);
                        if (btn instanceof Button) {
                            mRef.child("userReady").child(string).child(((Button) btn).getText().toString()).setValue("false");
                        }
                    }
                    if(isServicesOK()){
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }
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

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SingleGroupView.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");

            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SingleGroupView.this, available, 9901);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
