package com.example.london.financeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class newloanrecurrentmember extends AppCompatActivity {
    private EditText mLoanApp,mLoanpup,mInstallments;
    private TextView mNetloan,mInstallment;
    private Button mProcess;
    private DatabaseReference mDatabase2,mD,mD1;
    private FirebaseAuth mAuth;
    private String user,name,image,op;
    private double finalamount;
    private int monnthlypay=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newloanrecurrentmember);
        op=getIntent().getExtras().getString("id");
        mLoanApp=findViewById(R.id.lamount);
        mLoanpup=findViewById(R.id.lpurpose);
        mInstallments=findViewById(R.id.linstallments);

        mNetloan=findViewById(R.id.lnetamount);
        mInstallment=findViewById(R.id.linstallment);
        mProcess=findViewById(R.id.next1);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mInstallments.addTextChangedListener(filterTextWatcher);

        mD= FirebaseDatabase.getInstance().getReference().child("members").child(op).child("personaldetails");
        mDatabase2= FirebaseDatabase.getInstance().getReference().child("loanrequests");

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


        mProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference loanrequest=mDatabase2.push();
                loanrequest.child("requestdate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                loanrequest.child("amountrequested").setValue(String.valueOf(finalamount));
                loanrequest.child("memberuid").setValue(op);
                loanrequest.child("membername").setValue(name);
                loanrequest.child("period").setValue(mInstallments.getText().toString());
                loanrequest.child("monthlypay").setValue(String.valueOf(monnthlypay));
                loanrequest.child("status").setValue("pending");
                loanrequest.child("image").setValue(image);
                loanrequest.child("state").setValue("default");
                loanrequest.child("officeruid").setValue(user);

                Toast.makeText(newloanrecurrentmember.this, "Loan Applied!", Toast.LENGTH_LONG).show();

                Intent next =new Intent(newloanrecurrentmember.this,coordinator.class);
                startActivity(next);
            }
        });
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS

            double principal=Double.parseDouble(mLoanApp.getText().toString());
            double rate=1;
            String timet=mInstallments.getText().toString();

            if (timet.equals("")){
                timet="1";

            }

            double time=Double.parseDouble(timet);

            finalamount=(principal*rate*time)+principal;

            monnthlypay=(int)(finalamount/time);

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
