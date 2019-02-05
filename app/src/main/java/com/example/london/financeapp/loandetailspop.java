package com.example.london.financeapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class loandetailspop extends AppCompatActivity {
    private TextView mMembername,mLoanapplied,mInstallment,mInstallments,mPurpose,
            mLoanofficer,mGname,mGidnum,mGcontact,mGresidence,mGoccupation,mGrelationship,mInstype;
    private Button mAccept,mDecline;
    private ImageView mMemberImage;
    private DatabaseReference mD1,mD2,mD3,mD4,mD5,mD6,mD7,mD8,mD9,mDatabaseloanextras;
    private RecyclerView mGlist,mSlist;
    private ProgressDialog mProgress;
    private String id,lapplied,linstall,linstalls,lpurp,lofficer,lgname,lgid,lgcontact,lgresid,lgocc,memimag,uid,installtype,sedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loandetailspop);

        mProgress=new ProgressDialog(this);
        mMembername=findViewById(R.id.memname);
        mLoanapplied=findViewById(R.id.reqloan);
        mMemberImage=findViewById(R.id.memberpic);
        id=getIntent().getExtras().getString("id");
        mInstallment=findViewById(R.id.reqloaninstall);
        mInstallments=findViewById(R.id.installmentnumber);
        mInstype=findViewById(R.id.installmentype);
        mPurpose=findViewById(R.id.loanreason);
        mAccept=findViewById(R.id.next1);
        mDecline=findViewById(R.id.next);
        mLoanofficer=findViewById(R.id.loanofficer);
        mGname=findViewById(R.id.gname);
        mGidnum=findViewById(R.id.gid);
        mGcontact=findViewById(R.id.contact);
        mGresidence=findViewById(R.id.residence);
        mGoccupation=findViewById(R.id.occupation);
        mGrelationship=findViewById(R.id.relationshipp);

        mSlist = findViewById(R.id.seclist);
        mSlist.setHasFixedSize(true);
        mSlist.setLayoutManager(new LinearLayoutManager(this));

        mD1= FirebaseDatabase.getInstance().getReference().child("loanrequests").child(id);
        mD2=FirebaseDatabase.getInstance().getReference().child("members");
        mD3=FirebaseDatabase.getInstance().getReference().child("activeclients");
        mD4=FirebaseDatabase.getInstance().getReference().child("activeloans");
        mD5=FirebaseDatabase.getInstance().getReference().child("loansapproved");
        mD8=FirebaseDatabase.getInstance().getReference().child("loansdecined");
        mD6=FirebaseDatabase.getInstance().getReference().child("guarantors").child(id);
        mD7=FirebaseDatabase.getInstance().getReference().child("Employees");
        mD9=FirebaseDatabase.getInstance().getReference().child("loanhistory");
        mDatabaseloanextras=FirebaseDatabase.getInstance().getReference().child("loanextras").child(id);


        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid=dataSnapshot.child("memberuid").getValue().toString();

                mD2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String memname=dataSnapshot.child("personaldetails").child("name").getValue().toString();
                        memimag=dataSnapshot.child("personaldetails").child("image").getValue().toString();


                        mMembername.setText(memname);
                        Picasso.with(loandetailspop.this).load(memimag).into(mMemberImage);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mLoanapplied.setText("Kshs. "+dataSnapshot.child("amountrequested").getValue().toString());
                lapplied=dataSnapshot.child("amountrequested").getValue().toString();
                linstall=dataSnapshot.child("monthlypay").getValue().toString();
                installtype=dataSnapshot.child("installmenttype").getValue().toString();
                mInstype.setText(installtype);
                mInstallment.setText("Kshs. "+dataSnapshot.child("monthlypay").getValue().toString());
                mInstallments.setText(dataSnapshot.child("period").getValue().toString());
                linstalls=dataSnapshot.child("period").getValue().toString();
                mPurpose.setText(dataSnapshot.child("loanpurpose").getValue().toString());
                lofficer=dataSnapshot.child("officeruid").getValue().toString();
                mD7.child(dataSnapshot.child("officeruid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        mLoanofficer.setText(dataSnapshot1.child("name").getValue().toString());
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

        mD6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGname.setText(dataSnapshot.child("name").getValue().toString());
                mGidnum.setText(dataSnapshot.child("idnum").getValue().toString());
                mGcontact.setText(dataSnapshot.child("phone").getValue().toString());
                mGresidence.setText(dataSnapshot.child("residence").getValue().toString());
                mGoccupation.setText(dataSnapshot.child("occupation").getValue().toString());
                mGrelationship.setText(dataSnapshot.child("relationship").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<security,securitiesViewHolder> adapter2 = new FirebaseRecyclerAdapter<security, securitiesViewHolder>(

                security.class,
                R.layout.securities_row,
                securitiesViewHolder.class,
                mDatabaseloanextras.child("security")
        ) {
            @Override
            protected void populateViewHolder(securitiesViewHolder viewHolder, security model, int position) {
                viewHolder.setName(model.getItemname());
                viewHolder.setVal1(model.getMarketvalue());
                viewHolder.setVal2(model.getCompvalue());
            }
        };
        mSlist.setAdapter(adapter2);
    }

    public static class securitiesViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public securitiesViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }


        public void setName(String name){
            TextView itemname1 = (TextView)mView.findViewById(R.id.scardname1);
            itemname1.setText(name);
        }
        public void setVal1(String val1){
            TextView value1 = (TextView)mView.findViewById(R.id.scardval1);
            value1.setText(val1);
        }
        public void setVal2(String val2){
            TextView value2 = (TextView)mView.findViewById(R.id.scardval2);
            value2.setText(val2);
        }

    }

}
