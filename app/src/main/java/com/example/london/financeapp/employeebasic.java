package com.example.london.financeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class employeebasic extends AppCompatActivity {
    private TextView t1,t2,t3,t4,basicpay,comms,others,mNHIF,mNSSF,mPAYE,mLOAN,mAdv;
    private ImageView im1,imsett,editded,editpays;
    private CardView cc1,c2,mGiveloan,mGiveadv;
    private DatabaseReference mDatabase,mD2;
    private String s1,s2,bp,cp,op,nssfp,nhifp,payep;
    private double amount=0,amount2=0,amount3=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeebasic);
        s1=getIntent().getExtras().getString("id");
        t1=findViewById(R.id.allgroups);
        t2=findViewById(R.id.type1grp);
        t3=findViewById(R.id.type2grp);
        t4=findViewById(R.id.type3grp);
        im1=findViewById(R.id.cordprofpic);
        cc1=findViewById(R.id.chosegroup);
        c2=findViewById(R.id.managegroups);
        imsett=findViewById(R.id.imageView13);

        editpays=findViewById(R.id.edit3);
        editded=findViewById(R.id.edit2);

        basicpay=findViewById(R.id.basicpay);
        comms=findViewById(R.id.commbon);
        others=findViewById(R.id.otherpay);
        mNHIF=findViewById(R.id.nhifpay);
        mNSSF=findViewById(R.id.nssfpay);
        mPAYE=findViewById(R.id.paye);
        mLOAN=findViewById(R.id.loanded);
        mAdv=findViewById(R.id.advded);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Employees").child(s1);
        mD2= FirebaseDatabase.getInstance().getReference().child("activeloans").child("all");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                t1.setText(dataSnapshot.child("name").getValue().toString());
                if (dataSnapshot.child("phone").exists()) {
                    t2.setText(dataSnapshot.child("phone").getValue().toString());
                }else {
                    t2.setText("07..");
                }
                t3.setText(dataSnapshot.child("startdate").getValue().toString());
                t4.setText(dataSnapshot.child("salary").getValue().toString());
                Picasso.with(employeebasic.this).load(dataSnapshot.child("image").getValue().toString()).into(im1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()&&dataSnapshot.hasChildren()){
                    amount=0;
                    amount2=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("officerid").getValue().toString().equals(s1)){
                            double amou=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            double intre=Double.parseDouble(snapshot.child("interest").getValue().toString());
                            amount=amount+amou;
                            amount2=amount2+intre;

                            basicpay.setText("Kshs. "+String.valueOf(amount));
                            comms.setText("Kshs. "+String.valueOf(amount2));
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forw=new Intent(employeebasic.this,allmyclients2.class);
                forw.putExtra("id",s1);
                startActivity(forw);
            }
        });
        cc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usermodify = new Intent(employeebasic.this,allmyclients3.class);
                usermodify.putExtra("id",s1);
                startActivity(usermodify);
                finish();
            }
        });

    }
}
