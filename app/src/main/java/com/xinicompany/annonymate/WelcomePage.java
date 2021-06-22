package com.xinicompany.annonymate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends Activity {
    Button SignUp;
    Button LogIn;
    TextView Guest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        SignUp=(Button)findViewById(R.id.signUpButton);
        LogIn=(Button)findViewById(R.id.loginButton);
        Guest=(TextView)findViewById(R.id.guest);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WelcomePage.this , SignUp.class);
                startActivity(intent);
            }
        });
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WelcomePage.this , LoginPage.class);
                startActivity(intent);
            }
        });

    }
}