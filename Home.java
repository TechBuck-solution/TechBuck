package com.example.dev.techbuck;

import android.app.Dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dev.techbuck.Adapters.Adapter_Category;
import com.example.dev.techbuck.Bean.CategoryBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Home extends AppCompatActivity {

    RecyclerView recyclerview;
    Adapter_Category ac;
    ArrayList<String> key, value;
    ArrayList<CategoryBean> bean = new ArrayList<>();
    String URL = com.example.dev.techbuck.URL.url + "/category.php";
    String METHOD = "add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        key = new ArrayList<>();
        value = new ArrayList<>();
        recyclerview = findViewById(R.id.recyclerview);


        CallServices cs = new CallServices();
        String res = cs.CallServices(Home.this, URL, METHOD, key, value);
        Log.e("error-", res);
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.optJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String c_id = jsonObject1.getString("c_id");
                String c_name = jsonObject1.getString("c_name");
                String c_img = jsonObject1.getString("c_image");
                CategoryBean categoryBean = new CategoryBean(c_id, c_name, c_img);
                bean.add(categoryBean);
                Log.e("c_img=", c_img);

            }
            ac = new Adapter_Category(bean, Home.this);


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Errrorrr=>", e.getLocalizedMessage());
        }

        recyclerview.setLayoutManager(new GridLayoutManager(Home.this, 2));
        recyclerview.setAdapter(ac);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu_item, menu);
        return true;
    }

    //Option Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String res = item.getTitle().toString();
        if (res.equalsIgnoreCase("profile")) {

            startActivity(new Intent(Home.this,UserProfile.class));

        } else if (res.equalsIgnoreCase("logout")) {
            Session session = new Session(getApplicationContext());
            session.logOut();
            startActivity(new Intent(getApplicationContext(), login.class));
        } else if (res.equalsIgnoreCase("feedback")) {
            openDialog();
        }


        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        Button btn_send_feedback, btn_cancel_feedback;
        final Dialog dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.feedback_dialog);
        btn_cancel_feedback = dialog.findViewById(R.id.btn_cancel_feedback);
        btn_send_feedback = dialog.findViewById(R.id.btn_send_feedback);
        final EditText feedback = dialog.findViewById(R.id.ed_feedback);
        final int[] flag = new int[1];
        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
            Session session = new Session(getApplicationContext());
            String unm = session.checkLogin();
            String url = com.example.dev.techbuck.URL.url + "/user_feedback.php";

            @Override
            public void onClick(View view) {


                key.add("email");

                Log.e("unm", unm);
                value.add(unm);
                key.add("feedback");
                value.add(feedback.getText().toString());
                CallServices cs = new CallServices();
                String res = cs.CallServices(getApplicationContext(), url, "add", key, value);
                Log.e("res==>", res);
                if (res.trim().equals("0")) {
                   flag[0] =1 ;
                }
                dialog.dismiss();
            }
        });
        btn_cancel_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if ((flag[0]==1)) {
            Toast.makeText(this, "ThankYou for Your Feedback", Toast.LENGTH_SHORT).show();
        }
        dialog.show();

    }
}
