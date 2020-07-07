package com.example.dev.techbuck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    EditText profile_name, profile_email, profile_password;
    ImageButton btn_name, btn_email, btn_password,importprofileimage;
    RecyclerView profile_rv;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();
    }

    private void init() {
        profile_email = findViewById(R.id.profile_email);
        profile_name = findViewById(R.id.profile_name);
        profile_password = findViewById(R.id.profile_password);
        btn_email = findViewById(R.id.btn_email);
        btn_name = findViewById(R.id.btn_name);
        btn_password = findViewById(R.id.btn_password);
        profile_rv = findViewById(R.id.profile_rv);
        profile_image= findViewById(R.id.profile_image);
        importprofileimage= findViewById(R.id.importprofileimage);

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profile_password.setEnabled(true);
            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_email.setEnabled(true);
            }
        });
        btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_name.setEnabled(true);
            }
        });
    }
}
