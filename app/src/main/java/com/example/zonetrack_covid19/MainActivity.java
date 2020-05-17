package com.example.zonetrack_covid19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends FragmentActivity {
    Button btn_track;
    TextView tv_name, tv_status, tv_location;
    String name, status, location;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Name").getValue().toString();
                status = dataSnapshot.child("Zone").getValue().toString();
                location = "Bharatpur, Rajasthan";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_track = findViewById(R.id.btn_track);
        tv_name = findViewById(R.id.tv_name);
        tv_status = findViewById(R.id.tv_status);
        tv_location = findViewById(R.id.tv_location);
        tv_location.setText(tv_location.getText()+"Bharatpur, Rajasthan");
        tv_status.setText(tv_status.getText()+"Red");
        tv_name.setText(tv_name.getText()+"Harshit Agrawal");
        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
