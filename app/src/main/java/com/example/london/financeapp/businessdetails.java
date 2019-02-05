package com.example.london.financeapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class businessdetails extends Fragment {
    private EditText mBlocation,mBtype,mBdetails,mMincome,mNetincome;
    private Button mProcess;
    private DatabaseReference mDatabase2;
    private FirebaseAuth mAuth;
    private String user;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_businessdetails, container, false);
        mBlocation=view.findViewById(R.id.blocation);
        mBtype=view.findViewById(R.id.btype);
        mBdetails=view.findViewById(R.id.bdetails);
        mMincome=view.findViewById(R.id.bincomemon);
        mNetincome=view.findViewById(R.id.netincome);
        mProcess=view.findViewById(R.id.next1);
        mProgress=new ProgressDialog(getContext());
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mDatabase2= FirebaseDatabase.getInstance().getReference().child("members").child(user).child("businessdetails");

        mProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Setting Up Business Info..");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                mDatabase2.child("businesslocation").setValue(mBlocation.getText().toString());
                mDatabase2.child("businesstype").setValue(mBtype.getText().toString());
                mDatabase2.child("businessdetails").setValue(mBdetails.getText().toString());
                mDatabase2.child("monthlyincome").setValue(mMincome.getText().toString());
                mDatabase2.child("netincome").setValue(mNetincome.getText().toString());
                mProgress.dismiss();
                Toast.makeText(getContext(), "Submited! Please Proceed", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

}
