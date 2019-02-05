package com.example.london.financeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class scripts extends AppCompatActivity {
    private DatabaseReference mDatabase3,mD3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripts);

        mDatabase3= FirebaseDatabase.getInstance().getReference().child("members");
        mD3= FirebaseDatabase.getInstance().getReference().child("allmembers");

        mD3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String newname=snapshot.child("name").getValue().toString().toUpperCase();
                    String iid=snapshot.child("uid").getValue().toString();

                    DatabaseReference newmem=mD3.child(snapshot.getKey());
                    newmem.child("name").setValue(newname);

                    mDatabase3.child(iid).child("personaldetails").child("name").setValue(newname);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
