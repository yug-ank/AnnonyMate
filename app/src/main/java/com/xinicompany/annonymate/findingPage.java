package com.xinicompany.annonymate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class findingPage extends AppCompatActivity {

    private Button findMatch;
    private TextView text1 , text2;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_page);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null || !user.isEmailVerified()){
            Intent intent=new Intent(findingPage.this , WelcomePage.class);
            startActivity(intent);
            finish();

        }
        findMatch = findViewById(R.id.find_match);
        text1 = findViewById(R.id.finding);
        text2 = findViewById(R.id.tagline);
        intent = new Intent(findingPage.this, personalMessage.class);

    }

    public void findMatch(View view) {
        String user=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        findMatch.setVisibility(View.GONE);
        text1.setVisibility(View.VISIBLE);
        text2.setVisibility(View.VISIBLE);
        long matchId = new Date().getTime()/100;
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("finding");
        Log.i("recitfy" , ""+databaseReference);
        databaseReference.child(user).setValue(matchId).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Log.i("rectify" , ""+e);
            }
        });
        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    long max = (long) (1e9+7);
                    long temp = (long) (1e9+7);
                    String foundUser = null;
                    for(DataSnapshot children : snapshot.getChildren()){
                        temp = (long) children.getValue();
                        if(Math.abs( temp - matchId) < max){
                            max = Math.abs(temp - matchId);
                            foundUser = children.getKey();
                        }
                    }
                    // show match found
                    // open chatroom with match key
                    // create a message room
                    String chatroom = Long.toString(Math.max(temp, matchId));
                    Log.i("foundUser : ", foundUser);
                    Log.i("chatroom", chatroom);
                    intent.putExtra("foundUser", foundUser);
                    intent.putExtra("chatroom", chatroom);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
}