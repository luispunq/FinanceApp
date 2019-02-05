package com.example.london.financeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class loanrepayment extends AppCompatActivity {
    private TextView mUsername,mIDnum,mLoanbal,mInstallmentamt,
            mYearlbl,mTab1lbl,mTab2lbl,mTab3lbl;
    private ImageView mUserimage;
    private FloatingActionButton fab;
    private RecyclerView mPaymenthist;
    private FirebaseAuth mAuth;
    private CardView mYear,mTab1,mTab2,mTab3;
    private DatabaseReference mDatabase,mD,mD1,mD2,mD4,mD5;
    private long nextpaymen;
    private String user=null,installtype,sedate,op=null,fieldid,loanid=null;
    private String loan,loaninstallment,dategive,totloan,duratio,officerid,loanidd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loanrepayment);
        user=getIntent().getExtras().getString("id");
        Log.e("user",user);
        mAuth=FirebaseAuth.getInstance();
        op=mAuth.getCurrentUser().getUid();
        mUsername=findViewById(R.id.memname);
        mUserimage=findViewById(R.id.cordprofpic);
        mIDnum=findViewById(R.id.accnum);
        mLoanbal=findViewById(R.id.loanbalance);
        mInstallmentamt=findViewById(R.id.loaninstallment);
        mYearlbl=findViewById(R.id.t);
        mTab1lbl=findViewById(R.id.b1);
        mTab2lbl=findViewById(R.id.b2);
        mTab3lbl=findViewById(R.id.b3);
        fab=findViewById(R.id.addpayment);
        mPaymenthist=findViewById(R.id.paymenthist);
        mYear=findViewById(R.id.yearlbl);
        mTab1=findViewById(R.id.monthnowlbl);
        mTab2=findViewById(R.id.monthb4lbl);
        mTab3=findViewById(R.id.monthaflbl);

        mYearlbl.setText(new SimpleDateFormat("yyyy").format(new Date()));
        mTab1lbl.setText(new SimpleDateFormat("MMMM").format(new Date()));
        mTab2lbl.setText(lastmonth());
        mTab3lbl.setText(nextmonth());

        LinearLayoutManager layoutManager=new LinearLayoutManager(loanrepayment.this);
        mPaymenthist.setHasFixedSize(true);
        mPaymenthist.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mD1= FirebaseDatabase.getInstance().getReference().child("members").child(user);
        mD2= FirebaseDatabase.getInstance().getReference().child("loanrepayment").child(user);
        mD= FirebaseDatabase.getInstance().getReference().child("activeclients");
        mD4=FirebaseDatabase.getInstance().getReference().child("activeloans");
        mD5=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(op);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("fieldwork").child("all");



        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("personaldetails").child("name").getValue().toString();

                if (dataSnapshot.child("loans").child("installment").exists()){
                    loan = dataSnapshot.child("loans").child("totalloan").getValue().toString();
                    loanid = dataSnapshot.child("loans").child("info").getValue().toString();
                    loaninstallment = dataSnapshot.child("loans").child("installment").getValue().toString();
                mLoanbal.setText("Kshs. "+loan);
                mInstallmentamt.setText("Kshs. "+loaninstallment);
                }else {
                    mLoanbal.setText("Kshs. 0");
                    mInstallmentamt.setText("Kshs. 0");
                }
                String image = dataSnapshot.child("personaldetails").child("image").getValue().toString();
                fieldid = dataSnapshot.child("personaldetails").child("idnum").getValue().toString();
                mUsername.setText(name);
                mIDnum.setText(fieldid);
                Picasso.with(loanrepayment.this).load(image).into(mUserimage);

                mD4.child("all").child(loanid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        installtype=dataSnapshot.child("installmenttype").getValue().toString();
                        dategive=dataSnapshot.child("datedisbursed").getValue().toString();
                        totloan=dataSnapshot.child("amount").getValue().toString();
                        officerid=dataSnapshot.child("officerid").getValue().toString();
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText misccash=new EditText(loanrepayment.this);
                misccash.setHint("Enter Payment Received");
                misccash.setInputType(InputType.TYPE_CLASS_NUMBER);
                final AlertDialog.Builder builders = new AlertDialog.Builder(loanrepayment.this);
                builders.setTitle("Payment Confirmation")
                        .setMessage("Receiving Installment of\n Kshs. "+mInstallmentamt.getText().toString()+" from "+mUsername.getText().toString())
                        .setCancelable(true)
                        .setView(misccash)
                        .setPositiveButton("Bank Transaction", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                processpayment(misccash.getText().toString());
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Mpesa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                processpayment(misccash.getText().toString());
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert121 = builders.create();
                alert121.show();

            }
        });

        mYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(loanrepayment.this);
                builder.setTitle("Select Year")
                        .setCancelable(true)
                        .setItems(R.array.Years, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        mYearlbl.setText("2017");
                                        break;
                                    case 1:
                                        mYearlbl.setText("2018");
                                        break;
                                    case 2:
                                        mYearlbl.setText("2019");
                                        break;
                                    case 3:
                                        mYearlbl.setText("2020");
                                        break;
                                }
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });
        mTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(loanrepayment.this);
                builder2.setTitle("Select Month")
                        .setCancelable(true)
                        .setItems(R.array.Months, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        mTab1lbl.setText("January");
                                        mTab2lbl.setText("December");
                                        mTab3lbl.setText("February");
                                        transactions(mYearlbl.getText().toString(),"Jan");
                                        break;
                                    case 1:
                                        mTab1lbl.setText("February");
                                        mTab2lbl.setText("January");
                                        mTab3lbl.setText("March");
                                        transactions(mYearlbl.getText().toString(),"Feb");
                                        break;
                                    case 2:
                                        mTab1lbl.setText("March");
                                        mTab2lbl.setText("February");
                                        mTab3lbl.setText("April");
                                        transactions(mYearlbl.getText().toString(),"Mar");
                                        break;
                                    case 3:
                                        mTab1lbl.setText("April");
                                        mTab2lbl.setText("March");
                                        mTab3lbl.setText("May");
                                        transactions(mYearlbl.getText().toString(),"Apr");
                                        break;
                                    case 4:
                                        mTab1lbl.setText("May");
                                        mTab2lbl.setText("April");
                                        mTab3lbl.setText("June");
                                        transactions(mYearlbl.getText().toString(),"May");
                                        break;
                                    case 5:
                                        mTab1lbl.setText("June");
                                        mTab2lbl.setText("May");
                                        mTab3lbl.setText("July");
                                        transactions(mYearlbl.getText().toString(),"Jun");
                                        break;
                                    case 6:
                                        mTab1lbl.setText("July");
                                        mTab2lbl.setText("June");
                                        mTab3lbl.setText("May");
                                        transactions(mYearlbl.getText().toString(),"Jul");
                                        break;
                                    case 7:
                                        mTab1lbl.setText("August");
                                        mTab2lbl.setText("July");
                                        mTab3lbl.setText("September");
                                        transactions(mYearlbl.getText().toString(),"Aug");
                                        break;
                                    case 8:
                                        mTab1lbl.setText("September");
                                        mTab2lbl.setText("August");
                                        mTab3lbl.setText("October");
                                        transactions(mYearlbl.getText().toString(),"Sep");
                                        break;
                                    case 9:
                                        mTab1lbl.setText("October");
                                        mTab2lbl.setText("September");
                                        mTab3lbl.setText("November");
                                        transactions(mYearlbl.getText().toString(),"Oct");
                                        break;
                                    case 10:
                                        mTab1lbl.setText("November");
                                        mTab2lbl.setText("October");
                                        mTab3lbl.setText("December");
                                        transactions(mYearlbl.getText().toString(),"Nov");
                                        break;
                                    case 11:
                                        mTab1lbl.setText("December");
                                        mTab2lbl.setText("November");
                                        mTab3lbl.setText("January");
                                        transactions(mYearlbl.getText().toString(),"Dec");
                                        break;

                                        default:
                                            mYearlbl.setText(new SimpleDateFormat("yyyy").format(new Date()));
                                            mTab1lbl.setText(new SimpleDateFormat("MMMM").format(new Date()));
                                            mTab2lbl.setText(lastmonth());
                                            mTab3lbl.setText(nextmonth());
                                            transactions2();
                                            break;
                                }
                            }
                        });
                AlertDialog alert12 = builder2.create();
                alert12.show();
            }
        });

    }

    private void processpayment(final String installmentamount) {

        if (installtype.equals("Weekly")){
            sedate=setdate2();
        }else {
            sedate=setdate();
        }

        mD1.child("loans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String flc;
                String oldloans = (String) dataSnapshot.child("totalloan").getValue();
                double oldloan = Double.parseDouble(oldloans);
                double temploan = Double.parseDouble(installmentamount);
                double newinstallment = Double.parseDouble(installmentamount);
                double newloan = oldloan - newinstallment;
                String updatedloans = String.valueOf(newloan);
                DatabaseReference loansupdate = mD1.child("loans");
                loansupdate.child("totalloan").setValue(updatedloans);
                loansupdate.child("nextloanpaydate").setValue(sedate);

                mD.child(op).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if (snapshot.child("uid").getValue().toString().equals(user)){
                                DatabaseReference paid=mD.child(op);
                                paid.child(snapshot.getKey()).child("paiddate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mD.child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if (snapshot.child("uid").getValue().toString().equals(user)){
                                DatabaseReference paid=mD.child("all");
                                paid.child(snapshot.getKey()).child("paiddate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if (dataSnapshot.child("flag").exists()){
                    String comp = (String) dataSnapshot.child("installment").getValue();
                    String fl = (String) dataSnapshot.child("flag").getValue();

                    if (dataSnapshot.child("compflag").exists()){
                        flc = (String) dataSnapshot.child("compflag").getValue();
                    }else {
                        flc = "1";
                    }


                    double oldflag = Double.parseDouble(fl);
                    double oldflagc = Double.parseDouble(flc);
                    double compp = Double.parseDouble(comp);

                    if (((compp*(oldflagc+1))-((compp*(oldflag))))>compp+1000){
                        DatabaseReference rr=FirebaseDatabase.getInstance().getReference().child("loanarreas").child(user).push();
                        rr.child("membername").setValue(mUsername.getText().toString());
                        rr.child("totalloan").setValue(updatedloans);
                        rr.child("installment").setValue(installmentamount);
                        rr.child("loanbalance").setValue(updatedloans);
                        rr.child("arreas").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));
                        rr.child("dategiven").setValue(dategive);
                        rr.child("nextpayment").setValue(sedate);
                        rr.child("officerid").setValue(officerid);
                        rr.child("memberuid").setValue(user);
                        rr.child("loanid").setValue(loanid);
                        rr.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference rrr=FirebaseDatabase.getInstance().getReference().child("loanarreas").child("all").push();
                        rrr.child("membername").setValue(mUsername.getText().toString());
                        rrr.child("totalloan").setValue(totloan);
                        rrr.child("installment").setValue(installmentamount);
                        rrr.child("loanbalance").setValue(updatedloans);
                        rrr.child("arreas").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));
                        rrr.child("dategiven").setValue(dategive);
                        rrr.child("nextpayment").setValue(sedate);
                        rrr.child("officerid").setValue(officerid);
                        rrr.child("memberuid").setValue(user);
                        rrr.child("loanid").setValue(loanid);
                        rrr.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference activeloans=mD4.child(user).child(loanid);
                        activeloans.child("arreas").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));

                        DatabaseReference memberhist=mD1.child("credithist").child(loanid);
                        memberhist.child("totalarreas").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));


                        DatabaseReference allactiveloans=mD4.child("all").child(activeloans.getKey());
                        allactiveloans.child("arreas").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));


                        DatabaseReference allactiveloansall=mD4.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(activeloans.getKey());
                        allactiveloansall.child("arreas").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));


                        DatabaseReference arr=mD1.child("transactions").push();
                        arr.child("name").setValue(mUsername.getText().toString());
                        arr.child("type").setValue("Arreas");
                        arr.child("amount").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));
                        arr.child("status").setValue("Recorded");
                        arr.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        arr.child("officer").setValue(op);
                        arr.child("timestamp").setValue(ServerValue.TIMESTAMP);
                        arr.child("memberuid").setValue(user);

                        DatabaseReference allarr=FirebaseDatabase.getInstance().getReference().child("transactions").child("all").push();
                        allarr.child("name").setValue(mUsername.getText().toString());
                        allarr.child("type").setValue("Arreas");
                        allarr.child("amount").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));
                        allarr.child("status").setValue("Recorded");
                        allarr.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        allarr.child("officer").setValue(op);
                        allarr.child("timestamp").setValue(ServerValue.TIMESTAMP);
                        allarr.child("memberuid").setValue(user);

                        DatabaseReference allarrny=FirebaseDatabase.getInstance().getReference().child("transactions").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
                        allarrny.child("name").setValue(mUsername.getText().toString());
                        allarrny.child("type").setValue("Arreas");
                        allarrny.child("amount").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));
                        allarrny.child("status").setValue("Recorded");
                        allarrny.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        allarrny.child("officer").setValue(op);
                        allarrny.child("timestamp").setValue(ServerValue.TIMESTAMP);
                        allarrny.child("memberuid").setValue(user);
                    }

                    loansupdate.child("flag").setValue(String.valueOf(oldflag+1));
                    loansupdate.child("compflag").setValue(String.valueOf(oldflagc+1));

                }else {
                    loansupdate.child("flag").setValue("1");
                }



                if (newloan == 0) {

                    DatabaseReference rmm = mD1;
                    rmm.child("loanflag").removeValue();

                    DatabaseReference rrm=mD.child(user);
                    rrm.removeValue();

                    DatabaseReference rrml=mD4.child(user).child(loanid);
                    rrml.removeValue();

                }

                mD1.child(user).child("credithist").child(loanid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("actualinstallments").exists()){
                        String acinst=dataSnapshot.child("actualinstallments").getValue().toString();
                        double actinstt=Double.parseDouble(acinst);
                        double newact=1+actinstt;

                        DatabaseReference memberhist=mD1.child("credithist").child(loanid);
                        memberhist.child("actualinstallments").setValue(String.valueOf(newact));
                        }else {
                            String acinst="0";
                            double actinstt=Double.parseDouble(acinst);
                            double newact=1+actinstt;

                            DatabaseReference memberhist=mD1.child("credithist").child(loanid);
                            memberhist.child("actualinstallments").setValue(String.valueOf(newact));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                DatabaseReference activeloans=mD4.child(user).child(loanid);
                activeloans.child("balance").setValue(updatedloans);
                activeloans.child("nextinstallmentdate").setValue(sedate);
                activeloans.child("checkdate").setValue(nextpaymen);
                activeloans.child("lastinstalmentdate").setValue(ServerValue.TIMESTAMP);

                DatabaseReference allactiveloans=mD4.child("all").child(activeloans.getKey());
                allactiveloans.child("balance").setValue(updatedloans);
                allactiveloans.child("nextinstallmentdate").setValue(sedate);
                allactiveloans.child("checkdate").setValue(nextpaymen);
                allactiveloans.child("lastinstalmentdate").setValue(ServerValue.TIMESTAMP);

                DatabaseReference allactiveloansall=mD4.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(activeloans.getKey());
                allactiveloansall.child("balance").setValue(updatedloans);
                allactiveloansall.child("nextinstallmentdate").setValue(sedate);
                allactiveloansall.child("checkdate").setValue(nextpaymen);
                allactiveloansall.child("lastinstalmentdate").setValue(ServerValue.TIMESTAMP);

                DatabaseReference rerm=mD2.child(loanid).child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
                rerm.child("name").setValue(mUsername.getText().toString());
                rerm.child("type").setValue("Installment");
                rerm.child("amount").setValue(installmentamount);
                rerm.child("status").setValue("Received");
                rerm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                rerm.child("officer").setValue(op);
                rerm.child("timestamp").setValue(ServerValue.TIMESTAMP);
                rerm.child("memberuid").setValue(user);

                DatabaseReference erm=mD2.child(loanid).child("all").push();
                erm.child("name").setValue(mUsername.getText().toString());
                erm.child("type").setValue("Installment");
                erm.child("amount").setValue(installmentamount);
                erm.child("status").setValue("Received");
                erm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                erm.child("officer").setValue(op);
                erm.child("timestamp").setValue(ServerValue.TIMESTAMP);
                erm.child("memberuid").setValue(user);

                DatabaseReference morerm=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
                morerm.child("name").setValue(mUsername.getText().toString());
                morerm.child("type").setValue("Installment");
                morerm.child("amount").setValue(installmentamount);
                morerm.child("status").setValue("Received");
                morerm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                morerm.child("officer").setValue(op);
                morerm.child("timestamp").setValue(ServerValue.TIMESTAMP);
                morerm.child("memberuid").setValue(user);

                DatabaseReference allerm=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("all").push();
                allerm.child("name").setValue(mUsername.getText().toString());
                allerm.child("type").setValue("Installment");
                allerm.child("amount").setValue(installmentamount);
                allerm.child("status").setValue("Received");
                allerm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                allerm.child("officer").setValue(op);
                allerm.child("timestamp").setValue(ServerValue.TIMESTAMP);
                allerm.child("memberuid").setValue(user);

                DatabaseReference allermd=FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("daily").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                allermd.child("name").setValue(mUsername.getText().toString());
                allermd.child("type").setValue("Installment");
                allermd.child("amount").setValue(installmentamount);
                allermd.child("status").setValue("Received");
                allermd.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                allermd.child("officer").setValue(op);
                allermd.child("timestamp").setValue(ServerValue.TIMESTAMP);
                allermd.child("memberuid").setValue(user);

                DatabaseReference herm=mD5.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("transactions").push();
                herm.child("name").setValue(mUsername.getText().toString());
                herm.child("type").setValue("Installment");
                herm.child("amount").setValue(installmentamount);
                herm.child("status").setValue("Received");
                herm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                herm.child("officer").setValue(op);
                herm.child("timestamp").setValue(ServerValue.TIMESTAMP);
                herm.child("memberuid").setValue(user);

                DatabaseReference allherm=mD5.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("transactions").push();
                allherm.child("name").setValue(mUsername.getText().toString());
                allherm.child("type").setValue("Installment");
                allherm.child("amount").setValue(installmentamount);
                allherm.child("status").setValue("Received");
                allherm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                allherm.child("officer").setValue(op);
                allherm.child("timestamp").setValue(ServerValue.TIMESTAMP);
                allherm.child("memberuid").setValue(user);

                DatabaseReference mherm=mD1.child("transactions").push();
                mherm.child("name").setValue(mUsername.getText().toString());
                mherm.child("type").setValue("Installment");
                mherm.child("amount").setValue(installmentamount);
                mherm.child("status").setValue("Received");
                mherm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                mherm.child("officer").setValue(op);
                mherm.child("timestamp").setValue(ServerValue.TIMESTAMP);
                mherm.child("memberuid").setValue(user);

                DatabaseReference amherm=FirebaseDatabase.getInstance().getReference().child("transactions").child("all").push();
                amherm.child("name").setValue(mUsername.getText().toString());
                amherm.child("type").setValue("Installment");
                amherm.child("amount").setValue(installmentamount);
                amherm.child("status").setValue("Received");
                amherm.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                amherm.child("officer").setValue(op);
                amherm.child("timestamp").setValue(ServerValue.TIMESTAMP);
                amherm.child("memberuid").setValue(user);

                DatabaseReference amhermmy=FirebaseDatabase.getInstance().getReference().child("transactions").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
                amhermmy.child("name").setValue(mUsername.getText().toString());
                amhermmy.child("type").setValue("Installment");
                amhermmy.child("amount").setValue(installmentamount);
                amhermmy.child("status").setValue("Received");
                amhermmy.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                amhermmy.child("officer").setValue(op);
                amhermmy.child("timestamp").setValue(ServerValue.TIMESTAMP);
                amhermmy.child("memberuid").setValue(user);

                DatabaseReference clientseen=mDatabase.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("clientseen").push();
                clientseen.child("user").setValue(user);

                DatabaseReference clientseens=mDatabase.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("clientseen").push();
                clientseens.child("user").setValue(user);

                DatabaseReference myclientseen=mD5.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("clientseen").push();
                myclientseen.child("user").setValue(user);

                DatabaseReference miclientseens=mD5.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("clientseen").push();
                miclientseens.child("user").setValue(user);

                Intent next =new Intent(loanrepayment.this,coordinator.class);
                startActivity(next);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private String lastmonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.UK);
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.MONTH, -1); //Remove 1 year

        return sdf.format(c.getTime());
    }

    private String nextmonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.UK);
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, +1);//Nowusetodaydate.

        return sdf.format(c.getTime());
    }

    public class installmentsHolder extends RecyclerView.ViewHolder{
        private final CardView layout;
        final CardView.LayoutParams layoutParams;
        View mView;


        public installmentsHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (CardView) itemView.findViewById(R.id.transactioncard);
            layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.trsdate);
            TextView mGroupnam = (TextView)mView.findViewById(R.id.trstype);
            TextView mGroupnamr = (TextView)mView.findViewById(R.id.trsstatus);
            mGroupnamr.setText("Received");
            mGroupnam.setText("Installment");
            mGroupname.setText(groupname);
        }
        public void setGroupAmount(Context ctx,String groupname){
            TextView mGroupname = (TextView) mView.findViewById(R.id.trsamount);
            mGroupname.setText(groupname);
        }
    }

    private void transactions(String year,String month) {
        FirebaseRecyclerAdapter<transaction,installmentsHolder> firebaseRecyclerAdaptr = new FirebaseRecyclerAdapter<transaction,installmentsHolder>(

                transaction.class,
                R.layout.transactioninstallments_row,
                installmentsHolder.class,
                mD2.child(loanid).child(year).child(month).orderByChild("timestamp")
        )
        {
            @Override
            protected void populateViewHolder(final installmentsHolder viewHolder, final transaction model, int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setGroupAmount(getApplicationContext(),model.getAmount());

            }
        };

        mPaymenthist.setAdapter(firebaseRecyclerAdaptr);
    }

    private void transactions2() {
        FirebaseRecyclerAdapter<transaction,installmentsHolder> firebaseRecyclerAdaptr = new FirebaseRecyclerAdapter<transaction,installmentsHolder>(

                transaction.class,
                R.layout.transactioninstallments_row,
                installmentsHolder.class,
                mD2.child(loanid).child("all").orderByChild("timestamp")
        )
        {
            @Override
            protected void populateViewHolder(final installmentsHolder viewHolder, final transaction model, int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setGroupAmount(getApplicationContext(),model.getAmount());

            }
        };

        mPaymenthist.setAdapter(firebaseRecyclerAdaptr);
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
