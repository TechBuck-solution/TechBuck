package com.example.dev.techbuck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class login extends AppCompatActivity {
    EditText ed_email, password;
    Button notsignup, login;
    String Method = "add";
    String URL = com.example.dev.techbuck.URL.url+"/user_login.php";
    CallServices cs;
    ArrayList<String> key;
    ArrayList<String> value;
    String email, pw;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_email = findViewById(R.id.ed_email);
        password = findViewById(R.id.password);
        notsignup = findViewById(R.id.notsignup);
        login = findViewById(R.id.btnlogin);
        cs = new CallServices();
        key = new ArrayList<>();
        value = new ArrayList<>();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = ed_email.getText().toString();
                pw = password.getText().toString();

                if (!validemail() | !validpassword()) {
                    return;
                } else {
                    key.add("email");
                    value.add(email);
                    key.add("password");
                    value.add(pw);

                    String res = cs.CallServices(login.this, URL, Method, key, value);
                    Log.e("error===>", res);
                    if (res.trim().equals("0")) {
                        Toast.makeText(login.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                    } else if (res.trim().equals("1")) {
                        startActivity(new Intent(login.this, Home.class));
                        Session session = new Session(getApplicationContext());
                        session.setLogin(email);
                        finish();

                    }
                }
            }
        });
        notsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
    }

    private boolean validemail() {

        if (email.isEmpty()) {
            ed_email.setError("Feild Can't Be Empty");
            return false;
        } else {
            ed_email.setError(null);
            return true;
        }
    }

    private boolean validpassword() {

        if (pw.isEmpty()) {
            password.setError("Feild Can't Be Empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }


}
