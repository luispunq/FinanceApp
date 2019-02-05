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


public class securities extends Fragment {
    private EditText mAmountreq,mInstallmentperiod,gurname1,gurname2,gurname3,gurid1
            ,gurid2,gurid3,gurphone1,gurphone2,gurphone3,itemname1,itemname2,itemname3,mkt1,mkt2
            ,mkt3,cmp1,cmp2,cmp3,sno1,sno2,sno3,reco;
    private Button mCommit,sav1,sav2,sav3,sav4,sav5,sav6;
    private DatabaseReference mDatabasemembers,mDatabaseguarantors,loanrequest,extras,reference;
    private String user,officerid,loanid;
    private ProgressDialog mProgressdialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase2,mD1,mD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_securities, container, false);

        itemname1=view.findViewById(R.id.itemname1);
        itemname2=view.findViewById(R.id.itemname2);
        itemname3=view.findViewById(R.id.itemname3);
        mkt1=view.findViewById(R.id.itemmktval1);
        mkt2=view.findViewById(R.id.itemmktval2);
        mkt3=view.findViewById(R.id.itemmktval3);
        cmp1=view.findViewById(R.id.itemcmpval1);
        cmp2=view.findViewById(R.id.itemcmpval2);
        cmp3=view.findViewById(R.id.itemcmpval3);
        sno1=view.findViewById(R.id.itemSNO1);
        sno2=view.findViewById(R.id.itemSNO2);
        sno3=view.findViewById(R.id.itemSNO3);
        sav1=view.findViewById(R.id.save4);
        sav2=view.findViewById(R.id.save5);
        sav3=view.findViewById(R.id.save6);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mProgressdialog=new ProgressDialog(getContext());

        mD=FirebaseDatabase.getInstance().getReference().child("members").child(user);
        mDatabaseguarantors=FirebaseDatabase.getInstance().getReference().child("loanextras");

        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("loans").child("guarantor").child("loankey").exists()) {
                    loanid = dataSnapshot.child("loans").child("guarantor").child("loankey").getValue().toString();

                }else {
                    Toast.makeText(getContext(), "Please apply loan!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        sav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Wait..");
                mProgressdialog.show();
                extras=mDatabaseguarantors.child(loanid).child("security").push();

                extras.child("itemname").setValue(itemname1.getText().toString());
                extras.child("marketvalue").setValue(mkt1.getText().toString());
                extras.child("compvalue").setValue(cmp1.getText().toString());
                extras.child("sno").setValue(sno1.getText().toString());

                Toast.makeText(getContext(), "Item Added!", Toast.LENGTH_LONG).show();

                mProgressdialog.dismiss();
                sav1.setVisibility(View.GONE);
            }
        });

        sav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Wait..");
                mProgressdialog.show();
                extras=mDatabaseguarantors.child(loanid).child("security").push();

                extras.child("itemname").setValue(itemname2.getText().toString());
                extras.child("marketvalue").setValue(mkt2.getText().toString());
                extras.child("compvalue").setValue(cmp2.getText().toString());
                extras.child("sno").setValue(sno2.getText().toString());

                Toast.makeText(getContext(), "Item Added!", Toast.LENGTH_LONG).show();

                mProgressdialog.dismiss();
                sav2.setVisibility(View.GONE);
            }
        });

        sav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Wait..");
                mProgressdialog.show();
                extras=mDatabaseguarantors.child(loanid).child("security").push();

                extras.child("itemname").setValue(itemname3.getText().toString());
                extras.child("marketvalue").setValue(mkt3.getText().toString());
                extras.child("compvalue").setValue(cmp3.getText().toString());
                extras.child("sno").setValue(sno3.getText().toString());

                Toast.makeText(getContext(), "Item Added!", Toast.LENGTH_LONG).show();

                mProgressdialog.dismiss();
                sav3.setVisibility(View.GONE);
            }
        });


        return view;
    }

}
