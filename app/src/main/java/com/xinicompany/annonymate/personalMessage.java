package com.xinicompany.annonymate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class personalMessage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private personalMessageAdapter adapter;
    private ArrayList<MessageObject> chat;
    private Intent intent;
    private String chatId;
    private EditText editText;
    private DatabaseReference databaseReference;
    private TextView textView;
    public String foundUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_message);
        recyclerView = findViewById(R.id.personal_message_recyclerView);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.name);

        foundUser = intent.getStringExtra("foundUser");
        intent = getIntent();
        chatId = intent.getStringExtra("chatroom");
        databaseReference = FirebaseDatabase.getInstance().getReference().child(chatId);
        chat = new ArrayList<>();
        adapter = new personalMessageAdapter(chat, this , databaseReference);
        recyclerView.setAdapter(adapter);
        textView.setText(foundUser);

        getChat();
    }

    public void sendMessage(View view) {
        Map<String , Object> msg = new HashMap<>();
        String time = SimpleDateFormat.getDateInstance().format(new Date());
        msg.put("text" , editText.getText().toString());
        msg.put("sender", SessionManager.username);
        msg.put("flag", false);
        msg.put("time", time);
        databaseReference.push().updateChildren(msg);
    }

    private void getChat() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    MessageObject msg = new MessageObject(
                            snapshot.child("text").getValue().toString(),
                            snapshot.child("time").getValue().toString(),
                            //use username for the value of sender ////done
                            snapshot.child("sender").getValue().toString(),
                            (Boolean) snapshot.child("flag").getValue(),
                            snapshot.getKey()
                    );
                    chat.add(msg);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}