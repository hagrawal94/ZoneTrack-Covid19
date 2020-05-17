package com.example.zonetrack_covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_email, et_pass, et_cnf_pass, et_name, et_travelled_district;
    Button btn_register;
    TextView tv_login;
    RadioButton rbtn_yes, rbtn_no;
    FirebaseAuth mAuth;
    DatabaseReference firebaseDatabase;
    String zone = "";
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_cnf_pass = findViewById(R.id.et_cnf_pass);
        et_name = findViewById(R.id.et_name);
        et_travelled_district = findViewById(R.id.et_travelled_district);
        btn_register = findViewById(R.id.btn_register);
        tv_login = findViewById(R.id.tv_login);
        rbtn_no = findViewById(R.id.rbtn_no);
        rbtn_yes = findViewById(R.id.rbtn_yes);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        rbtn_no.setOnClickListener(this);
        rbtn_yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register :
                if(et_pass.getText().toString().equals(et_cnf_pass.getText().toString())){
                    dialog = new SpotsDialog.Builder().setContext(this).setMessage("Registering...").setCancelable(false).build();
                    dialog.show();
                    if(rbtn_yes.isChecked()) {
                        String line = "";
                        String splitBy = ",";
                        String[] location = et_travelled_district.getText().toString().split(splitBy+" ");
                        Log.i("location ", location[0]+", "+location[1]);
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("zonedata.csv")));
                            while ((line = br.readLine()) != null)   //returns a Boolean value
                            {
                                String[] data = line.split(splitBy);    // use comma as separator
                                Log.i("data ", data[0]+", "+data[1]);
                                if (data[0].equals(location[0]) && data[1].equals(location[1])) {
                                    zone = data[2];
                                    Log.i("Zone ", zone);
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    mAuth.createUserWithEmailAndPassword(et_email.getText().toString(),et_pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String userID=mAuth.getCurrentUser().getUid();
                                HashMap<String, String> profileMap=new HashMap<>();
                                profileMap.put("Name",et_name.getText().toString());
                                profileMap.put("Zone",zone);
                                profileMap.put("Uid",userID);
                                firebaseDatabase.child("Users").child(userID).setValue(profileMap);
                                Toast.makeText(RegisterActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                dialog.dismiss();
                                startActivity(intent);
                                RegisterActivity.this.finish();
                            }
                            else {
                                dialog.dismiss();
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }else Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_login :
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
            case R.id.rbtn_no :
                et_travelled_district.setVisibility(View.GONE);
                break;
            case R.id.rbtn_yes :
                et_travelled_district.setVisibility(View.VISIBLE);
                break;
        }
    }
}
