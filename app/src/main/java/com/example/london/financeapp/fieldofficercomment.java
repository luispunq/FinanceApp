package com.example.london.financeapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class fieldofficercomment extends Fragment {

    private EditText mComments;
    private Button mSubmit,mDone;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase2,mD1,mD;
    private StorageReference mProfileImageStore;
    private String user,officerid,loanid;
    private ProgressDialog mProgress;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fieldofficercomment, container, false);
        mComments=view.findViewById(R.id.officercomments);
        mSubmit=view.findViewById(R.id.button2);
        mDone=view.findViewById(R.id.button3);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mDatabase2= FirebaseDatabase.getInstance().getReference().child("loanrequests");
        mD1=FirebaseDatabase.getInstance().getReference().child("Active").child(user);
        mD=FirebaseDatabase.getInstance().getReference().child("members").child(user);

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                officerid=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("loans").child("guarantor").child("loankey").exists()) {
                    loanid = dataSnapshot.child("loans").child("guarantor").child("loankey").getValue().toString();

                }else {
                    Toast.makeText(getContext(), "Please apply loan before Making Comment", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference loanrequest=mDatabase2.child(loanid);
                loanrequest.child("comments").setValue(mComments.getText().toString());
                Toast.makeText(getContext(), "Comment Added!", Toast.LENGTH_LONG).show();
            }
        });

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent card2 = new Intent(getContext(),login.class);
                startActivity(card2);
                Toast.makeText(getContext(), "Loan Pending Clearance!", Toast.LENGTH_LONG).show();
            }
        });




        return view;
    }

}
