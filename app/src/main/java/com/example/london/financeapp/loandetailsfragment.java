package com.example.london.financeapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;


public class loandetailsfragment extends Fragment {
    private EditText mLoanApp,mLoanpup,mInstallments;
    private TextView mNetloan,mInstallment;
    private Button mProcess;
    private DatabaseReference mDatabase2,mD,mD1,mD3;
    private FirebaseAuth mAuth;
    private String user,name,image,officerid,installmentype,offname;
    private double finalamount,intr;
    private Spinner mInstype;
    private int monnthlypay=0;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mProgress=new ProgressDialog(getContext());
        View view=inflater.inflate(R.layout.fragment_loandetailsfragment, container, false);
        mLoanApp=view.findViewById(R.id.lamount);
        mLoanpup=view.findViewById(R.id.lpurpose);
        mInstype=(Spinner)view.findViewById(R.id.instype);
        mInstallments=view.findViewById(R.id.linstallments);

        mNetloan=view.findViewById(R.id.lnetamount);
        mInstallment=view.findViewById(R.id.linstallment);
        mProcess=view.findViewById(R.id.next1);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mInstallments.addTextChangedListener(filterTextWatcher);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),R.array.instype,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mInstype.setAdapter(adapter);

        mD=FirebaseDatabase.getInstance().getReference().child("members").child(user).child("personaldetails");
        mDatabase2= FirebaseDatabase.getInstance().getReference().child("loanrequests");
        mD1=FirebaseDatabase.getInstance().getReference().child("Active").child(user);
        mD3=FirebaseDatabase.getInstance().getReference().child("Employees");

        mInstype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                installmentype=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name= (String) dataSnapshot.child("name").getValue();
                image= (String) dataSnapshot.child("image").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                officerid=dataSnapshot.getValue().toString();
                mD3.child(officerid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        offname=dataSnapshot.child("name").getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Submitting Loan Application..");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                DatabaseReference loanrequest=mDatabase2.push();
                loanrequest.child("requestdate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                loanrequest.child("amountrequested").setValue(String.valueOf(finalamount));
                loanrequest.child("amountcash").setValue(String.valueOf(mLoanApp.getText().toString()));
                loanrequest.child("interest").setValue(String.valueOf(intr));
                loanrequest.child("memberuid").setValue(user);
                loanrequest.child("membername").setValue(name);
                loanrequest.child("period").setValue(mInstallments.getText().toString());
                loanrequest.child("monthlypay").setValue(String.valueOf(monnthlypay));
                loanrequest.child("status").setValue("pending");
                loanrequest.child("installmenttype").setValue(installmentype);
                loanrequest.child("image").setValue(image);
                loanrequest.child("state").setValue("default");
                loanrequest.child("officeruid").setValue(officerid);
                loanrequest.child("officername").setValue(offname);
                loanrequest.child("loanpurpose").setValue(mLoanpup.getText().toString());

                DatabaseReference myapps=FirebaseDatabase.getInstance().getReference().child("officerrequests").child(officerid).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(loanrequest.getKey());
                myapps.child("requestdate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                myapps.child("amountrequested").setValue(String.valueOf(finalamount));
                myapps.child("memberuid").setValue(user);
                myapps.child("membername").setValue(name);
                myapps.child("period").setValue(mInstallments.getText().toString());
                myapps.child("monthlypay").setValue(String.valueOf(monnthlypay));
                myapps.child("status").setValue("pending");
                myapps.child("installmenttype").setValue(installmentype);
                myapps.child("image").setValue(image);
                myapps.child("state").setValue("default");
                myapps.child("officeruid").setValue(officerid);
                myapps.child("loanpurpose").setValue(mLoanpup.getText().toString());


                DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("members").child(user).child("loans").child("guarantor");
                db.child("loankey").setValue(loanrequest.getKey());

                mProgress.dismiss();
                Toast.makeText(getContext(), "Loan Applied!", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS

            double principal=Double.parseDouble(mLoanApp.getText().toString());
            double rate=0.1;
            String timet=mInstallments.getText().toString();

            if (timet.equals("")){
                timet="1";

            }

            double time=Double.parseDouble(timet);

            finalamount=(principal*rate*time)+principal;

            monnthlypay=(int)(finalamount/time);

            intr=finalamount-principal;

            mNetloan.setText("Kshs. "+String.valueOf(finalamount));
            mInstallment.setText("Kshs. "+String.valueOf(monnthlypay));

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
