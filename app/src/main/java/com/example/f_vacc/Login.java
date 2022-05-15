package com.example.f_vacc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.List;

public class Login extends AppCompatActivity implements DatabaseAccessCallback{

    private CircularProgressButton btLogin, btSignup;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
//        userInfo = new UserInfo();

        this.btLogin = findViewById(R.id.btLogin);
        this.btSignup = findViewById(R.id.btSignUp);
        this.etUsername = findViewById(R.id.etLoginUsername);
        this.etPassword = findViewById(R.id.etLoginPassword);

        this.btLogin.setProgress(0);
        DatabaseAccess dbAccess = new DatabaseAccess(this);

        this.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btLogin.setProgress(0);
                String inputUsername = etUsername.getText().toString();
                String inputPassword = etPassword.getText().toString();



                btLogin.setIndeterminateProgressMode(true);
                btLogin.setProgress(50);
                dbAccess.executeQuery("SELECT id FROM persons WHERE username = '" + inputUsername + "' AND password  = '" + inputPassword + "'");

            }
        });
        this.btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void QueryResponse(List<String[]> data) {
        if(data.size() > 0){
            Intent mIntent = new Intent(this, Dashboard.class);
            writeConfig(data.get(0)[0]);
            startActivity(mIntent);
            finish();
        } else {
            this.btLogin.setProgress(-1);

            this.etUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btLogin.setProgress(0);
                }
            });

            this.etPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btLogin.setProgress(0);
                }
            });
        }
    }


    private void writeConfig(String id){
        File file = new File(Login.this.getFilesDir(), "text");
        try {
            File gpxfile = new File(file, "config");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(id);
            writer.flush();
            writer.close();
        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

}