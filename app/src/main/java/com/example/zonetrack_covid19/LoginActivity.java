package com.example.zonetrack_covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_login;
    EditText et_email, et_pass;
    TextView tv_register;
    private AlertDialog dialog;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login :
                dialog = new SpotsDialog.Builder().setContext(this).setMessage("Logging In...").setCancelable(false).build();
                dialog.show();
                String username=et_email.getText().toString();
                String password=et_pass.getText().toString();
                mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if(task.isSuccessful()){
                                        String device_id=task.getResult().getToken();
                                        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("deviceID").setValue(device_id);
                                        Toast.makeText(LoginActivity.this, "Sign in successfull!!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }
                                }
                            });

                        }
                        else {
                            dialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.tv_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
        }
    }
}
