package com.example.london.financeapp;


import android.app.ProgressDialog;
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


public class guarantordetails extends Fragment {
    private EditText mFname,mSname,mLname,mIdnum,mOcc,mRelation,mPhone,mResidence,mBusiness;
    private Button mProcess;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase2,mD1,mD;
    private StorageReference mProfileImageStore;
    private String user,officerid;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_guarantordetails, container, false);
        mProgress=new ProgressDialog(getContext());
        mFname=view.findViewById(R.id.firstname);
        mSname=view.findViewById(R.id.middlename);
        mLname=view.findViewById(R.id.lastname);
        mIdnum=view.findViewById(R.id.idno);
        mOcc=view.findViewById(R.id.occup);
        mRelation=view.findViewById(R.id.relationship);
        mPhone=view.findViewById(R.id.phone);
        mBusiness=view.findViewById(R.id.gbusiness);
        mResidence=view.findViewById(R.id.residence);
        mProcess=view.findViewById(R.id.next1);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mD1= FirebaseDatabase.getInstance().getReference().child("guarantors");
        mD=FirebaseDatabase.getInstance().getReference().child("members").child(user);

        mProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mD.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mProgress.setMessage("Adding Guarantor Information..");
                        mProgress.setCanceledOnTouchOutside(false);
                        mProgress.show();
                        if (dataSnapshot.child("loans").child("guarantor").child("loankey").exists()) {
                            String loan = dataSnapshot.child("loans").child("guarantor").child("loankey").getValue().toString();

                            DatabaseReference addg = mD1.child(loan);
                            addg.child("name").setValue(mFname.getText().toString() + " " + mSname.getText().toString() + " " + mLname.getText().toString());
                            addg.child("idnum").setValue(mIdnum.getText().toString());
                            addg.child("occupation").setValue(mOcc.getText().toString());
                            addg.child("relationship").setValue(mRelation.getText().toString());
                            addg.child("phone").setValue(mPhone.getText().toString());
                            addg.child("business").setValue(mBusiness.getText().toString());
                            addg.child("residence").setValue(mResidence.getText().toString());

                            mProgress.dismiss();
                            Toast.makeText(getContext(), mFname.getText().toString() + " has been submitted as Guarantor", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getContext(), "Please apply loan before setting Guarantor", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        return view;
    }

}
