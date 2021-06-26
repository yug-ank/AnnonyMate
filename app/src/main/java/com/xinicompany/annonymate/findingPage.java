package com.xinicompany.annonymate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.lang.Math.min;

public class findingPage extends AppCompatActivity {

    private Button findMatch;
    private TextView text1 , text2;
    private Intent intent;
    private FirebaseUser user;
    private String username;
    private DatabaseReference databaseReference;
    private long currentTime;
    private long curTime;
    private long prevTime=0;
    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_page);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null || !user.isEmailVerified()){
            Intent intent=new Intent(findingPage.this , WelcomePage.class);
            startActivity(intent);
            finish();

        }
        intent = new Intent(findingPage.this, personalMessage.class);
        findMatch = findViewById(R.id.find_match);
        text1 = findViewById(R.id.finding);
        text2 = findViewById(R.id.tagline);

    }

    public void findMatch(View view) {
        currentTime = new Date().getTime()/100;
        username = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getDisplayName();

        findMatch.setVisibility(View.GONE);
        text1.setVisibility(View.VISIBLE);
        text2.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("finding");
        databaseReference.child(username).setValue(currentTime);

        listener =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    long bestUserEntryTime = Long.MAX_VALUE;
                    long temp = Long.MAX_VALUE;
                    String foundUser = null;

                    for(DataSnapshot children : snapshot.getChildren()){
                        if(children.getValue() == null
                                || currentTime == (long) children.getValue()){
                            continue;
                        }

                        temp = (long) children.getValue();
                        if (Math.abs(temp - currentTime) < bestUserEntryTime) {
                            bestUserEntryTime = Math.abs(temp - currentTime);
                            foundUser = children.getKey();
                        }
                    }

                    if(foundUser != null) {
                        databaseReference.removeEventListener(this);
                        String chatroom = Math.max(temp, currentTime) + "";
                        intent.putExtra("foundUser", foundUser);
                        intent.putExtra("chatroom", chatroom);
                        Log.i("yash found", foundUser);
                        databaseReference.child(foundUser).removeValue().isSuccessful();

                        findMatch.setVisibility(View.VISIBLE);
                        text1.setVisibility(View.GONE);
                        text2.setVisibility(View.GONE);

                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.addValueEventListener(listener);
    }

    @Override
    public void onBackPressed() {
        curTime= new Date().getTime();
        if(findMatch.getVisibility() == View.GONE) {
            if (curTime - prevTime <= 2500) {
                databaseReference.removeEventListener(listener);
                databaseReference.child(username).removeValue();
                findMatch.setVisibility(View.VISIBLE);
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "press again to stop searching", Toast.LENGTH_SHORT).show();
                prevTime = new Date().getTime();
            }
        }
        else{
            if (curTime - prevTime <= 2500) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "press again to close Application", Toast.LENGTH_SHORT).show();
                prevTime = new Date().getTime();
            }
        }
    }
}