package com.example.london.financeapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminmenu3 extends Fragment {
    private TextView mLoansgiven,mArreas;
    private CardView mLoanReports,mArreasreport,mDefaulters;
    private DatabaseReference mD1,mD2,mD3,mD4,mDatabasehelp,Database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_adminmenu3, container, false);
        mLoansgiven=view.findViewById(R.id.activeloans);
        mArreas=view.findViewById(R.id.activedeafults);
        mLoanReports=view.findViewById(R.id.allloanscard);
        mArreasreport=view.findViewById(R.id.monthlycard);
        mDefaulters=view.findViewById(R.id.defaultscard);

        mD2= FirebaseDatabase.getInstance().getReference().child("activeclients").child("all");
        mD4=FirebaseDatabase.getInstance().getReference().child("activedefaults").child("all");

        mD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLoansgiven.setText(String.valueOf(dataSnapshot.getChildrenCount())+" Clients");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArreas.setText(String.valueOf(dataSnapshot.getChildrenCount())+" Clients");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mLoanReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Report Type")
                        .setCancelable(true)
                        .setItems(R.array.coloanoptions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent payments = new Intent(getContext(),dateselector3.class);
                                        startActivity(payments);
                                        break;
                                    case 1:
                                        Intent paymentse = new Intent(getContext(),monthselector2.class);
                                        startActivity(paymentse);
                                        break;
                                    case 2:
                                        Intent paymentsw = new Intent(getContext(),rangeselector.class);
                                        startActivity(paymentsw);
                                        break;
                                    case 3:
                                        Intent paymentsw3 = new Intent(getContext(),loananalysisall.class);
                                        startActivity(paymentsw3);
                                        break;
                                }
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

        mArreasreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Report Type")
                        .setCancelable(true)
                        .setItems(R.array.coloanoptions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent payments = new Intent(getContext(),dateselector4.class);
                                        startActivity(payments);
                                        break;
                                    case 1:
                                        Intent paymentse = new Intent(getContext(),monthselector4.class);
                                        startActivity(paymentse);
                                        break;
                                    case 2:
                                        Intent paymentsw = new Intent(getContext(),rangeselector4.class);
                                        startActivity(paymentsw);
                                        break;
                                    case 3:
                                        Intent paymentswe = new Intent(getContext(),loananalysiswitharreasall.class);
                                        startActivity(paymentswe);
                                        break;
                                }
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

        mDefaulters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Report Type")
                        .setCancelable(true)
                        .setItems(R.array.coloanoptions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent payments = new Intent(getContext(),dateselector5.class);
                                        startActivity(payments);
                                        break;
                                    case 1:
                                        Intent paymentse = new Intent(getContext(),monthselector5.class);
                                        startActivity(paymentse);
                                        break;
                                    case 2:
                                        Intent paymentsw = new Intent(getContext(),rangeselector5.class);
                                        startActivity(paymentsw);
                                        break;
                                    case 3:
                                        Intent paymentsw3 = new Intent(getContext(),defaultanalysisall.class);
                                        startActivity(paymentsw3);
                                        break;
                                }
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Loans");
    }
}
