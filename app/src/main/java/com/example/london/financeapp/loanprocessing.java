package com.example.london.financeapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class loanprocessing extends AppCompatActivity {
    private TextView mMembername,mLoanapplied,mInstallment,mInstallments,mPurpose,
    mLoanofficer,mGname,mGidnum,mGcontact,mGresidence,mGoccupation,mGrelationship,mInstype,mPreviousloans,mRatngs;
    private Button mAccept,mDecline;
    private ImageView mMemberImage;
    private DatabaseReference mD1,mD2,mD3,mD4,mD5,mD6,mD7,mD8,mD92,mD9,mDatabaseloanextras,mDatabases4,mDe;
    private RecyclerView mGlist,mSlist;
    private ProgressDialog mProgress;
    private String id,lapplied,linstall,linstalls,lpurp,lofficer,lgname,lgid,lgcontact,lgresid,lgocc,memimag,uid,installtype,sedate;
    private double loanapp;
    private long nextpaymen;
    private String loancash,officername,loaninterest,memname,locatiion,contaact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loanprocessing);
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
        mPreviousloans=findViewById(R.id.previuosloans);
        mRatngs=findViewById(R.id.creditscore);

        mSlist = findViewById(R.id.seclist);
        mSlist.setHasFixedSize(true);
        mSlist.setLayoutManager(new LinearLayoutManager(this));

        mD1= FirebaseDatabase.getInstance().getReference().child("loanrequests").child(id);
        mD2=FirebaseDatabase.getInstance().getReference().child("members");
        mD92=FirebaseDatabase.getInstance().getReference().child("loanhistory");
        mD3=FirebaseDatabase.getInstance().getReference().child("activeclients");
        mD4=FirebaseDatabase.getInstance().getReference().child("activeloans");
        mDe=FirebaseDatabase.getInstance().getReference().child("onceloans");
        mD5=FirebaseDatabase.getInstance().getReference().child("loansapproved");
        mD8=FirebaseDatabase.getInstance().getReference().child("loansdecined");
        mD6=FirebaseDatabase.getInstance().getReference().child("guarantors").child(id);
        mD7=FirebaseDatabase.getInstance().getReference().child("Employees");
        mD9=FirebaseDatabase.getInstance().getReference().child("loanhistory");
        mDatabaseloanextras=FirebaseDatabase.getInstance().getReference().child("loanextras").child(id);
        mDatabases4=FirebaseDatabase.getInstance().getReference().child("fees");

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid=dataSnapshot.child("memberuid").getValue().toString();

                mD92.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String numm=dataSnapshot.child("numbers").getValue().toString();
                            mPreviousloans.setText(numm+" Loans");
                        }else {
                            mPreviousloans.setText("No loans Taken Yet");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseDatabase.getInstance().getReference().child("memberratings").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String numm=dataSnapshot.child("rating").getValue().toString();
                            mRatngs.setText(numm+" Score");
                        }else {
                            mRatngs.setText("No loans Taken Yet");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mD2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        memname=dataSnapshot.child("personaldetails").child("name").getValue().toString();
                        memimag=dataSnapshot.child("personaldetails").child("image").getValue().toString();
                        locatiion=dataSnapshot.child("personaldetails").child("residence").getValue().toString();
                        contaact=dataSnapshot.child("personaldetails").child("phone").getValue().toString();


                        mMembername.setText(memname);
                        Picasso.with(loanprocessing.this).load(memimag).into(mMemberImage);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mLoanapplied.setText("Kshs. "+dataSnapshot.child("amountrequested").getValue().toString());
                loanapp=Double.parseDouble(dataSnapshot.child("amountrequested").getValue().toString());
                lapplied=dataSnapshot.child("amountrequested").getValue().toString();
                linstall=dataSnapshot.child("monthlypay").getValue().toString();
                installtype=dataSnapshot.child("installmenttype").getValue().toString();
                mInstype.setText(installtype);
                mInstallment.setText("Kshs. "+dataSnapshot.child("monthlypay").getValue().toString());
                mInstallments.setText(dataSnapshot.child("period").getValue().toString());
                linstalls=dataSnapshot.child("period").getValue().toString();
                mPurpose.setText(dataSnapshot.child("loanpurpose").getValue().toString());
                lofficer=dataSnapshot.child("officeruid").getValue().toString();

                loancash=dataSnapshot.child("amountcash").getValue().toString();
                loaninterest=dataSnapshot.child("interest").getValue().toString();

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

        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptloan();
            }
        });
        mDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineloan();
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

    private void acceptloan() {

        mProgress.setMessage("Submitting Loan Application..");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        if (installtype.equals("Weekly")){
            sedate=setdate2();
        }else {
            sedate=setdate();
        }


        DatabaseReference setstatus=mD1;
        setstatus.child("status").setValue("cleared");



        DatabaseReference loanrequest=mD5.push();
        String loanid=loanrequest.getKey();
        loanrequest.child("cleardate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        loanrequest.child("amountrequested").setValue(String.valueOf(lapplied));
        loanrequest.child("memberuid").setValue(uid);
        loanrequest.child("membername").setValue(mMembername.getText().toString());
        loanrequest.child("period").setValue(mInstallments.getText().toString());
        loanrequest.child("monthlypay").setValue(String.valueOf(linstall));
        loanrequest.child("status").setValue("cleared");
        loanrequest.child("installmenttype").setValue(installtype);
        loanrequest.child("image").setValue(memimag);
        loanrequest.child("state").setValue("default");
        loanrequest.child("officeruid").setValue(lofficer);
        loanrequest.child("timestamp").setValue(ServerValue.TIMESTAMP);
        loanrequest.child("loanpurpose").setValue(mPurpose.getText().toString());


        DatabaseReference activeloans=mD4.child(uid).push();
        activeloans.child("balance").setValue(String.valueOf(lapplied));
        activeloans.child("memberid").setValue(uid);
        activeloans.child("officerid").setValue(lofficer);
        activeloans.child("amount").setValue(String.valueOf(lapplied));
        activeloans.child("installment").setValue(linstall);
        activeloans.child("interest").setValue(loaninterest);
        activeloans.child("amountcash").setValue(loancash);
        activeloans.child("officername").setValue(mLoanofficer.getText().toString());
        activeloans.child("datedisbursed").setValue(ServerValue.TIMESTAMP);
        activeloans.child("nextinstallmentdate").setValue(sedate);
        activeloans.child("installmenttype").setValue(installtype);
        activeloans.child("arreas").setValue("0");
        activeloans.child("timestamp").setValue(ServerValue.TIMESTAMP);
        activeloans.child("checkdate").setValue(nextpaymen);
        activeloans.child("lastinstalmentdate").setValue("");
        activeloans.child("location").setValue(locatiion);
        activeloans.child("contact").setValue(contaact);
        activeloans.child("membername").setValue(memname);

        DatabaseReference allactiveloans=mD4.child("all").child(activeloans.getKey());
        allactiveloans.child("balance").setValue(String.valueOf(lapplied));
        allactiveloans.child("memberid").setValue(uid);
        allactiveloans.child("officerid").setValue(lofficer);
        allactiveloans.child("amount").setValue(String.valueOf(lapplied));
        allactiveloans.child("installment").setValue(linstall);
        allactiveloans.child("interest").setValue(loaninterest);
        allactiveloans.child("amountcash").setValue(loancash);
        allactiveloans.child("officername").setValue(mLoanofficer.getText().toString());
        allactiveloans.child("datedisbursed").setValue(ServerValue.TIMESTAMP);
        allactiveloans.child("nextinstallmentdate").setValue(sedate);
        allactiveloans.child("installmenttype").setValue(installtype);
        allactiveloans.child("arreas").setValue("0");
        allactiveloans.child("timestamp").setValue(ServerValue.TIMESTAMP);
        allactiveloans.child("lastinstalmentdate").setValue(ServerValue.TIMESTAMP);
        allactiveloans.child("location").setValue(locatiion);
        allactiveloans.child("checkdate").setValue(nextpaymen);
        allactiveloans.child("contact").setValue(contaact);
        allactiveloans.child("membername").setValue(memname);


        DatabaseReference allactiveloansday=mD4.child("daily").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(activeloans.getKey());
        allactiveloansday.child("balance").setValue(String.valueOf(lapplied));
        allactiveloansday.child("memberid").setValue(uid);
        allactiveloansday.child("officerid").setValue(lofficer);
        allactiveloansday.child("amount").setValue(String.valueOf(lapplied));
        allactiveloansday.child("installment").setValue(linstall);
        allactiveloansday.child("interest").setValue(loaninterest);
        allactiveloansday.child("amountcash").setValue(loancash);
        allactiveloansday.child("officername").setValue(mLoanofficer.getText().toString());
        allactiveloansday.child("datedisbursed").setValue(ServerValue.TIMESTAMP);
        allactiveloansday.child("nextinstallmentdate").setValue(sedate);
        allactiveloansday.child("installmenttype").setValue(installtype);
        allactiveloansday.child("arreas").setValue("0");
        allactiveloansday.child("timestamp").setValue(ServerValue.TIMESTAMP);
        allactiveloansday.child("lastinstalmentdate").setValue(ServerValue.TIMESTAMP);
        allactiveloansday.child("location").setValue(locatiion);
        allactiveloansday.child("contact").setValue(contaact);
        allactiveloansday.child("checkdate").setValue(nextpaymen);
        allactiveloansday.child("membername").setValue(memname);

        DatabaseReference allactiveloanstimely=mD4.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(activeloans.getKey());
        allactiveloanstimely.child("balance").setValue(String.valueOf(lapplied));
        allactiveloanstimely.child("memberid").setValue(uid);
        allactiveloanstimely.child("officerid").setValue(lofficer);
        allactiveloanstimely.child("amount").setValue(String.valueOf(lapplied));
        allactiveloanstimely.child("installment").setValue(linstall);
        allactiveloanstimely.child("interest").setValue(loaninterest);
        allactiveloanstimely.child("amountcash").setValue(loancash);
        allactiveloanstimely.child("officername").setValue(mLoanofficer.getText().toString());
        allactiveloanstimely.child("datedisbursed").setValue(ServerValue.TIMESTAMP);
        allactiveloanstimely.child("nextinstallmentdate").setValue(sedate);
        allactiveloanstimely.child("installmenttype").setValue(installtype);
        allactiveloanstimely.child("arreas").setValue("0");
        allactiveloanstimely.child("checkdate").setValue(nextpaymen);
        allactiveloanstimely.child("timestamp").setValue(ServerValue.TIMESTAMP);
        allactiveloanstimely.child("lastinstalmentdate").setValue(ServerValue.TIMESTAMP);
        allactiveloanstimely.child("location").setValue(locatiion);
        allactiveloanstimely.child("contact").setValue(contaact);
        allactiveloanstimely.child("membername").setValue(memname);

        DatabaseReference onceloans=mDe.child("all").push();
        onceloans.child("balance").setValue(String.valueOf(lapplied));
        onceloans.child("memberid").setValue(uid);
        onceloans.child("officerid").setValue(lofficer);
        onceloans.child("amount").setValue(String.valueOf(lapplied));
        onceloans.child("installment").setValue(linstall);
        onceloans.child("interest").setValue(loaninterest);
        onceloans.child("amountcash").setValue(loancash);
        onceloans.child("officername").setValue(mLoanofficer.getText().toString());
        onceloans.child("datedisbursed").setValue(ServerValue.TIMESTAMP);
        onceloans.child("nextinstallmentdate").setValue(sedate);
        onceloans.child("installmenttype").setValue(installtype);
        onceloans.child("arreas").setValue("0");
        onceloans.child("timestamp").setValue(ServerValue.TIMESTAMP);
        onceloans.child("checkdate").setValue(ServerValue.TIMESTAMP);
        onceloans.child("lastinstalmentdate").setValue("");
        onceloans.child("location").setValue(locatiion);
        onceloans.child("contact").setValue(contaact);
        onceloans.child("membername").setValue(memname);


        DatabaseReference activeclients=mD3.child(uid).push();
        activeclients.child("name").setValue(mMembername.getText().toString());
        activeclients.child("uid").setValue(uid);
        activeclients.child("image").setValue(memimag);
        activeclients.child("officer").setValue(lofficer);
        activeclients.child("date").setValue(sedate);
        activeclients.child("paiddate").setValue("");
        activeclients.child("loanid").setValue(activeloans.getKey());

        DatabaseReference officeractiveclients=mD3.child(lofficer).push();
        officeractiveclients.child("name").setValue(mMembername.getText().toString());
        officeractiveclients.child("uid").setValue(uid);
        officeractiveclients.child("image").setValue(memimag);
        officeractiveclients.child("officer").setValue(lofficer);
        officeractiveclients.child("date").setValue(sedate);
        officeractiveclients.child("paiddate").setValue("");
        officeractiveclients.child("loanid").setValue(activeloans.getKey());

        DatabaseReference allactiveclients=mD3.child("all").push();
        allactiveclients.child("name").setValue(mMembername.getText().toString());
        allactiveclients.child("uid").setValue(uid);
        allactiveclients.child("image").setValue(memimag);
        allactiveclients.child("officer").setValue(lofficer);
        allactiveclients.child("date").setValue(sedate);
        allactiveclients.child("paiddate").setValue("");
        allactiveclients.child("loanid").setValue(activeloans.getKey());

        DatabaseReference mherm=mD2.child(uid).child("transactions").child(activeloans.getKey());
        mherm.child("name").setValue(mMembername.getText().toString());
        mherm.child("type").setValue("Loan");
        mherm.child("amount").setValue(lapplied);
        mherm.child("status").setValue("Received");
        mherm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        mherm.child("officer").setValue(lofficer);
        mherm.child("timestam").setValue(ServerValue.TIMESTAMP);
        mherm.child("memberuid").setValue(uid);

        DatabaseReference amherm=FirebaseDatabase.getInstance().getReference().child("transactions").child("all").child(activeloans.getKey());
        amherm.child("name").setValue(mMembername.getText().toString());
        amherm.child("type").setValue("Loan");
        amherm.child("amount").setValue(lapplied);
        amherm.child("status").setValue("Received");
        amherm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        amherm.child("officer").setValue(lofficer);
        amherm.child("timestam").setValue(ServerValue.TIMESTAMP);
        amherm.child("memberuid").setValue(uid);

        DatabaseReference amhermmy=FirebaseDatabase.getInstance().getReference().child("transactions").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(activeloans.getKey());
        amhermmy.child("name").setValue(mMembername.getText().toString());
        amhermmy.child("type").setValue("Loan");
        amhermmy.child("amount").setValue(lapplied);
        amhermmy.child("status").setValue("Received");
        amhermmy.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        amhermmy.child("officer").setValue(lofficer);
        amhermmy.child("timestam").setValue(ServerValue.TIMESTAMP);
        amhermmy.child("memberuid").setValue(uid);

        DatabaseReference memberhist=mD2.child(uid).child("credithist").child(activeloans.getKey());
        memberhist.child("amount").setValue(String.valueOf(lapplied));
        memberhist.child("expectedinstallments").setValue(mInstallments.getText().toString());
        memberhist.child("totalarreas").setValue("0");
        memberhist.child("defaultdays").setValue("0");
        memberhist.child("actualinstallments").setValue("0");


        DatabaseReference newpersonalloan=mD2.child(uid).child("loans");
        newpersonalloan.child("info").setValue(activeloans.getKey());
        newpersonalloan.child("totalloan").setValue(lapplied);
        newpersonalloan.child("installment").setValue(linstall);
        newpersonalloan.child("nextloanpaydate").setValue(sedate);
        newpersonalloan.child("flag").setValue("1");

        DatabaseReference loanflag=mD2.child(uid);
        loanflag.child("loanflag").setValue("yes");

        mD9.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String numm=dataSnapshot.child("numbers").getValue().toString();
                    double nuum=Double.parseDouble(numm);
                    double updatednuum=1+nuum;
                    DatabaseReference updatehist=mD9.child(uid);
                    updatehist.child("numbers").setValue(String.valueOf(updatednuum));
                }else {
                    DatabaseReference updatehist=mD9.child(uid);
                    updatehist.child("numbers").setValue("1");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (loanapp<=20000) {
            DatabaseReference lfee = mDatabases4.child("all").child("loanprocessing").push();
            lfee.child("amount").setValue("300");
            lfee.child("loanid").setValue(activeloans.getKey());

            DatabaseReference morerm=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
            morerm.child("name").setValue(mMembername.getText().toString());
            morerm.child("type").setValue("Fees");
            morerm.child("amount").setValue("300");
            morerm.child("status").setValue("Received");
            morerm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            morerm.child("officer").setValue(lofficer);
            morerm.child("timestamp").setValue(ServerValue.TIMESTAMP);
            morerm.child("memberuid").setValue(uid);

            DatabaseReference allerm=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("all").push();
            allerm.child("name").setValue(mMembername.getText().toString());
            allerm.child("type").setValue("Fees");
            allerm.child("amount").setValue("300");
            allerm.child("status").setValue("Received");
            allerm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            allerm.child("officer").setValue(lofficer);
            allerm.child("timestamp").setValue(ServerValue.TIMESTAMP);
            allerm.child("memberuid").setValue(uid);

            DatabaseReference allermd=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("daily").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            allermd.child("name").setValue(mMembername.getText().toString());
            allermd.child("type").setValue("Fees");
            allermd.child("amount").setValue("300");
            allermd.child("status").setValue("Received");
            allermd.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            allermd.child("officer").setValue(lofficer);
            allermd.child("timestamp").setValue(ServerValue.TIMESTAMP);
            allermd.child("memberuid").setValue(uid);

        }else if(loanapp>20000&&loanapp<50000){
            DatabaseReference lfee = mDatabases4.child("all").child("loanprocessing").push();
            lfee.child("amount").setValue("500");
            lfee.child("loanid").setValue(activeloans.getKey());

            DatabaseReference morerm=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
            morerm.child("name").setValue(mMembername.getText().toString());
            morerm.child("type").setValue("Fees");
            morerm.child("amount").setValue("500");
            morerm.child("status").setValue("Received");
            morerm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            morerm.child("officer").setValue(lofficer);
            morerm.child("timestamp").setValue(ServerValue.TIMESTAMP);
            morerm.child("memberuid").setValue(uid);

            DatabaseReference allerm=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("all").push();
            allerm.child("name").setValue(mMembername.getText().toString());
            allerm.child("type").setValue("Fees");
            allerm.child("amount").setValue("500");
            allerm.child("status").setValue("Received");
            allerm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            allerm.child("officer").setValue(lofficer);
            allerm.child("timestamp").setValue(ServerValue.TIMESTAMP);
            allerm.child("memberuid").setValue(uid);

            DatabaseReference allermd=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("daily").push();
            allermd.child("name").setValue(mMembername.getText().toString());
            allermd.child("type").setValue("Fees");
            allermd.child("amount").setValue("500");
            allermd.child("status").setValue("Received");
            allermd.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            allermd.child("officer").setValue(lofficer);
            allermd.child("timestamp").setValue(ServerValue.TIMESTAMP);
            allermd.child("memberuid").setValue(uid);

        }else {
            DatabaseReference lfee = mDatabases4.child("all").child("loanprocessing").push();
            lfee.child("amount").setValue("1000");
            lfee.child("loanid").setValue(activeloans.getKey());

            DatabaseReference morerm=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
            morerm.child("name").setValue(mMembername.getText().toString());
            morerm.child("type").setValue("Fees");
            morerm.child("amount").setValue("1000");
            morerm.child("status").setValue("Received");
            morerm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            morerm.child("officer").setValue(lofficer);
            morerm.child("timestamp").setValue(ServerValue.TIMESTAMP);
            morerm.child("memberuid").setValue(uid);

            DatabaseReference allerm=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("all").push();
            allerm.child("name").setValue(mMembername.getText().toString());
            allerm.child("type").setValue("Fees");
            allerm.child("amount").setValue("1000");
            allerm.child("status").setValue("Received");
            allerm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            allerm.child("officer").setValue(lofficer);
            allerm.child("timestamp").setValue(ServerValue.TIMESTAMP);
            allerm.child("memberuid").setValue(uid);

            DatabaseReference allermd=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("daily").push();
            allermd.child("name").setValue(mMembername.getText().toString());
            allermd.child("type").setValue("Fees");
            allermd.child("amount").setValue("1000");
            allermd.child("status").setValue("Received");
            allermd.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
            allermd.child("officer").setValue(lofficer);
            allermd.child("timestamp").setValue(ServerValue.TIMESTAMP);
            allermd.child("memberuid").setValue(uid);
        }



        DatabaseReference newmessagenextpay=FirebaseDatabase.getInstance().getReference().child("notifications").child("loandisbursed").push();
        newmessagenextpay.child("membername").setValue(memname);
        newmessagenextpay.child("memberid").setValue(uid);
        newmessagenextpay.child("message").setValue(memname+" your loan of "+lapplied+" has been disbursed, your first installment is on, "+sedate);
        newmessagenextpay.child("phone").setValue(contaact);

        mProgress.dismiss();
        Intent loans = new Intent(loanprocessing.this,adminhome.class);
        startActivity(loans);
        finish();
        Toast.makeText(loanprocessing.this, "Loan Approved!", Toast.LENGTH_LONG).show();

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

    private void declineloan() {
        Toast.makeText(loanprocessing.this, "Loan Declined", Toast.LENGTH_LONG).show();
        DatabaseReference setstatus=mD1;
        setstatus.child("status").setValue("declined");

        final EditText misccash=new EditText(loanprocessing.this);
        misccash.setHint("Enter Reason");
        final AlertDialog.Builder builders = new AlertDialog.Builder(loanprocessing.this);
        builders.setTitle("Decline Reason")
                .setMessage("Why has this loan request been declined")
                .setCancelable(true)
                .setView(misccash)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference loanrequest=mD8.child(id);
                        loanrequest.child("userid").setValue(uid);
                        loanrequest.child("amountrequested").setValue(lapplied);
                        loanrequest.child("monthlypay").setValue(mInstallments.getText().toString());
                        loanrequest.child("status").setValue("declined");
                        loanrequest.child("reason").setValue(misccash.getText().toString());
                        loanrequest.child("datedeclined").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        loanrequest.child("loanofficer").setValue(mLoanofficer.getText().toString());

                        DatabaseReference myapps=FirebaseDatabase.getInstance().getReference().child("officerrequests").child(lofficer).child(id);
                        myapps.child("userid").setValue(uid);
                        myapps.child("amountrequested").setValue(lapplied);
                        myapps.child("monthlypay").setValue(mInstallments.getText().toString());
                        myapps.child("status").setValue("declined");
                        myapps.child("reason").setValue(misccash.getText().toString());
                        myapps.child("datedeclined").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        myapps.child("loanofficer").setValue(mLoanofficer.getText().toString());

                        DatabaseReference myappsall=FirebaseDatabase.getInstance().getReference().child("officerrequests").child("all").child(id);
                        myappsall.child("userid").setValue(uid);
                        myappsall.child("amountrequested").setValue(lapplied);
                        myappsall.child("monthlypay").setValue(mInstallments.getText().toString());
                        myappsall.child("status").setValue("declined");
                        myappsall.child("reason").setValue(misccash.getText().toString());
                        myappsall.child("datedeclined").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        myappsall.child("loanofficer").setValue(mLoanofficer.getText().toString());


                        dialog.dismiss();
                        Toast.makeText(loanprocessing.this, "Loan Declined!", Toast.LENGTH_LONG).show();
                        Intent loans = new Intent(loanprocessing.this,adminhome.class);
                        startActivity(loans);
                        finish();
                    }
                })
                .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent loans = new Intent(loanprocessing.this,adminhome.class);
                        startActivity(loans);
                        finish();
                    }
                });
        AlertDialog alert121 = builders.create();
        alert121.show();

    }
    private String setdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 30); // Adding 30 days
        String output = sdf.format(c.getTime());
        nextpaymen=c.getTimeInMillis();
        return output;
    }
    private String setdate2() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 7); // Adding 30 days
        String output = sdf.format(c.getTime());
        nextpaymen=c.getTimeInMillis();

        return output;
    }
}
