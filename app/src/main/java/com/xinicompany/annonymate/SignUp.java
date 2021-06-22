package com.xinicompany.annonymate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {
    EditText emailText;
    EditText passwordText;
    EditText userNameText;
    Button signUpButton;
    String email;
    String password;
    String username;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userNameText=(EditText)findViewById(R.id.name);
        emailText=(EditText)findViewById(R.id.email);
        passwordText=(EditText)findViewById(R.id.password);
        signUpButton=(Button)findViewById(R.id.SignUpButton);
        mauth=FirebaseAuth.getInstance();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=emailText.getText().toString().trim();
                password=passwordText.getText().toString().trim();
                username=userNameText.getText().toString().trim();
                Log.i("rectify" , email+" "+password);
                mauth.createUserWithEmailAndPassword(email , password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                    user.updateProfile(new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build());
                                    Intent intent=new Intent(SignUp.this , LoginPage.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(SignUp.this , ""+task.getException() , Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}