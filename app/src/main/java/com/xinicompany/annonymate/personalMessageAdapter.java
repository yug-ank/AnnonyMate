package com.xinicompany.annonymate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class personalMessageAdapter extends RecyclerView.Adapter<personalMessageAdapter.viewHolder> {

    private final DatabaseReference databaseReference;
    ArrayList<MessageObject> chat;
    Context context;

    personalMessageAdapter(ArrayList<MessageObject> chat , Context context , DatabaseReference databaseReference){
        this.chat = chat;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_view, null);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        view.setLayoutParams(params);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        databaseReference.child(chat.get(position).msgid).child("flag").setValue(true);
        if( chat.get(position).sender.equals(SessionManager.username) ){
            holder.sendmsg.setVisibility(View.VISIBLE);
            holder.sendmsg.setText(chat.get(position).text);
        }
        else{
            holder.recivemsg.setVisibility(View.VISIBLE);
            holder.recivemsg.setText(chat.get(position).text);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class viewHolder extends RecyclerView.ViewHolder {
        public TextView sendmsg , recivemsg;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            sendmsg = itemView.findViewById(R.id.chatSentMessageTextView);
            recivemsg = itemView.findViewById(R.id.chatRecievedMessageTextView);
        }
    }
}
