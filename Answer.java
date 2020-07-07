package com.example.dev.techbuck;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class Answer extends AppCompatActivity {
    RecyclerView ansrv;
    FloatingActionButton ansflotingbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        init();
    }

    private void init() {
        ansrv=findViewById(R.id.ansrv);
        ansflotingbtn=findViewById(R.id.ansflotingbtn);

        ansflotingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Answer.this,AddAnswer.class));
            }
        });
    }
}
