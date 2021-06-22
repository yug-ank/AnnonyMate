package com.xinicompany.annonymate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends Activity {
    FirebaseAuth mauth;
    EditText emailField;
    EditText passwordField;
    TextView createOne;
    TextView forgotPassword;
    Button loginButton;
    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mauth=FirebaseAuth.getInstance();
        emailField=(EditText)findViewById(R.id.email);
        passwordField=(EditText)findViewById(R.id.password);
        createOne=(TextView)findViewById(R.id.createOne);
        forgotPassword=(TextView)findViewById(R.id.forgotPassword);
        loginButton=(Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=emailField.getText().toString().trim();
                password=passwordField.getText().toString().trim();
                mauth.signInWithEmailAndPassword(email , password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    checkIfEmailVerified();
                                }
                                else{
                                    Toast.makeText(LoginPage.this , ""+task.getException() , Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        createOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginPage.this  , SignUp.class);
                startActivity(intent);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginPage.this , ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    private void checkIfEmailVerified(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user.isEmailVerified()){
            Toast.makeText(LoginPage.this , "You have successfully Logged in" , Toast.LENGTH_LONG).show();
            Intent intent=new Intent(LoginPage.this , findingPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }
        else{
            View view=getLayoutInflater().inflate(R.layout.activity_verify_email_page , null);
            MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(LoginPage.this);
            builder.setView(view);
            AlertDialog dialog=builder.create();
            dialog.show();
            WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            DisplayMetrics displayMetrics=new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            layoutParams.width=displayMetrics.widthPixels;
            layoutParams.height=displayMetrics.heightPixels/2;
            dialog.getWindow().setAttributes(layoutParams);
            Button sendButton=(Button)view.findViewById(R.id.SendButton);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginPage.this , "Please verify email using the link sent on your email" , Toast.LENGTH_LONG);
                                dialog.dismiss();

                            }else{
                                Toast.makeText(LoginPage.this , ""+task.getException() , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }
    }
}