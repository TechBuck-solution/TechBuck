package com.example.dev.techbuck;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddQuestion extends AppCompatActivity {
    EditText question;
    Button btn_iamge, post;
    Spinner catspiner;
    ImageView image;
    ArrayList<String> key, cat, value;
    String URL = com.example.dev.techbuck.URL.url + "/category.php";
    String URL_username = com.example.dev.techbuck.URL.url + "/getusername.php";
    String URL_usernameimage = com.example.dev.techbuck.URL.url + "/getuserimage.php";
    String URL_addquestion = com.example.dev.techbuck.URL.url + "/addquestion.php";
    String URL_addquestionwithimage = com.example.dev.techbuck.URL.url + "/addquestionwithimage.php";
    String METHOD = "add";
    ArrayAdapter<String> adapter;
    Session session;
    String username, path, Date, usernameimage;
    Calendar c;
    SimpleDateFormat df;
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;

    boolean image_status = false;
    //Uri to store the image uri
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        init();

        final CallServices cs = new CallServices();
        String res = cs.CallServices(AddQuestion.this, URL, METHOD, key, value);
        Log.e("error-", res);


        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.optJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String c_id = jsonObject1.getString("c_id");
                String c_name = jsonObject1.getString("c_name");
                String c_img = jsonObject1.getString("c_image");
                cat.add(c_name);
                Log.e("c_img=", c_img);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Errrorrr=>", e.getLocalizedMessage());
        }
        adapter = new ArrayAdapter<String>(AddQuestion.this, R.layout.support_simple_spinner_dropdown_item, cat);
        catspiner.setAdapter(adapter);

        //get username
        key.add("email");
        value.add(session.preferences.getString("unm", ""));
        username = cs.CallServices(AddQuestion.this, URL_username, METHOD, key, value);
        usernameimage = cs.CallServices(AddQuestion.this, URL_usernameimage, METHOD, key, value);
        Log.e("Username", username);

        btn_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionManager.checkStoragePermission(AddQuestion.this)) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(AddQuestion.this);
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((question.getText().toString().trim() == null))
                    Toast.makeText(AddQuestion.this, "Please Write Question or Select Category", Toast.LENGTH_SHORT).show();
                else {
                    c = Calendar.getInstance();
                    df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date = df.format(c.getTime());
                    if (!image_status) {
                        key.clear();
                        value.clear();
                        key.add("question");
                        value.add(question.getText().toString());
                        key.add("email");
                        value.add(session.preferences.getString("unm", ""));
                        key.add("date");
                        value.add(Date);
                        key.add("cat");
                        value.add(catspiner.getSelectedItem().toString());
                        key.add("unm");
                        value.add(username);
                        key.add("userimage");
                        value.add(usernameimage);
                        String res = cs.CallServices(AddQuestion.this, URL_addquestion, METHOD, key, value);
                        if (res.trim().equalsIgnoreCase("1")) {
                            Toast.makeText(AddQuestion.this, "Your Question Posted", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(AddQuestion.this, "Can't Post Question", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddQuestion.this, "I am Call", Toast.LENGTH_SHORT).show();
                        uploadMultipart();
                    }

                }
            }
        });
    }

    private void openDialog() {
        ImageView img;
        Dialog d = new Dialog(getApplicationContext());
        d.setContentView(R.layout.open_image_dialog);
        img = d.findViewById(R.id.bigimg);
        img.setImageURI(filePath);
        d.show();

    }

    private void init() {
        question = findViewById(R.id.question);
        btn_iamge = findViewById(R.id.btn_iamge);
        post = findViewById(R.id.post);
        catspiner = findViewById(R.id.catspiner);
        image = findViewById(R.id.image);
        cat = new ArrayList<>();
        key = new ArrayList<>();
        value = new ArrayList<>();
        session = new Session(AddQuestion.this);
        c = Calendar.getInstance();
        df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = df.format(c.getTime());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            filePath = result.getUri();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
                image.setVisibility(View.VISIBLE);
                image_status = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void uploadMultipart() {
        //getting name for the image

        Log.d("Filepath===>", String.valueOf(filePath));
        //getting the actual path of the image
        path = getPath(filePath);
        //Log.d("IMage Path===>", path);
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request

            String res = new MultipartUploadRequest(this, uploadId, URL_addquestionwithimage)
                    .addParameter("email", session.preferences.getString("unm", ""))
                    .addParameter("unm", username)
                    .addParameter("cat", catspiner.getSelectedItem().toString())
                    .addParameter("question", question.getText().toString())
                    .addParameter("date", Date)
                    .addParameter("usernameimage", usernameimage)
                    .addFileToUpload(path, "image") //Adding file
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

        } catch (Exception exc) {
            Toast.makeText(this, "Error==" + exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


}
