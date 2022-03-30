package me.neeejm.starz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import me.neeejm.starz.adapters.StarAdapter;
import me.neeejm.starz.beans.Star;
import me.neeejm.starz.services.StarService;

public class StarActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int IMAGE_REQ = 1;
    private static final String TAG = "Uploading Image >>> ";
    private StarService starService;
    private TextView lastname;
    private TextView firstname;
    private TextView ville;
    private RadioButton male;
    private ImageView face;
    private Button addBtn;
    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);

        starService = StarService.getInstance(this);

        lastname = findViewById(R.id.input_lastname);
        firstname = findViewById(R.id.input_firstname);
        ville = findViewById(R.id.input_ville);
        male = findViewById(R.id.male);
        face = findViewById(R.id.face);
        face.setOnClickListener(this);
        addBtn = findViewById(R.id.btn_add);
        addBtn.setOnClickListener(this);

        initCloudinaryConfig();
    }

    private void initCloudinaryConfig() {
        try {
            Map config = new HashMap();
            config.put("cloud_name", "degodfgeg");
            config.put("api_key", "873434775679928");
            config.put("api_secret", "ew5tg6CIY_DA4ytkKIUQgZGdw7U");
            MediaManager.init(this, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_star, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_cancel) {
            startActivity(new Intent(StarActivity.this, MainActivity.class));
            finish();
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == addBtn) {
            if (
                    firstname.getText().toString().equals("") ||
                    lastname.getText().toString().equals("") ||
                    ville.getText().toString().equals("")
            ) {
                Toast.makeText(this, "Veuillez remplir les champs vide", Toast.LENGTH_LONG).show();
            } else {
                addBtn.setClickable(false);
                addBtn.setBackgroundColor(Color.GRAY);
                if (imagePath != null) {
                    MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            Log.d(TAG, "onStart: started");
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            Log.d(TAG, "onProgress: uploading");
                            addBtn.setText("Uploading...");
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            Log.d(TAG, "onSuccess: uploaded url: " + resultData.get("url").toString());
                            starService.create(new Star(lastname.getText().toString(),
                                    firstname.getText().toString(),
                                    ville.getText().toString(),
                                    male.isChecked(),
                                    resultData.get("url").toString()));
                            Toast.makeText(StarActivity.this, "Star ajouté", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(StarActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Log.e(TAG, "onError: " + error);
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            Log.e(TAG, "onReschedule: " + error);
                        }
                    }).dispatch();
                } else {
                    starService.create(new Star(lastname.getText().toString(),
                            firstname.getText().toString(),
                            ville.getText().toString(),
                            male.isChecked(),
                            null));
                    Toast.makeText(StarActivity.this, "Star ajouté", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(StarActivity.this, MainActivity.class));
                    finish();
                }
            }
        } else if (view == face) {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(StarActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            ActivityCompat.requestPermissions(StarActivity.this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, IMAGE_REQ);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQ && resultCode == Activity.RESULT_OK && data != null && data.getData() !=null) {
            imagePath = data.getData();
            Glide
                    .with(this)
                    .load(imagePath)
                    .into(face);
        }

    }
}