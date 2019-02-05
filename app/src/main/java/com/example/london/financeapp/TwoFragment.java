package com.example.london.financeapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TwoFragment extends Fragment {
    private TextView mUsername,mIDnum,mLoanbal,mInstallmentamt,mInstallments,mLoangivn,
            mYearlbl,mTab1lbl,mTab2lbl,mTab3lbl,mDategiven;
    private ImageView mUserimage;
    private FirebaseAuth mAuth;
    private RecyclerView mPaymenthist;
    private CardView mYear,mTab1,mTab2,mTab3;
    private FloatingActionButton fab;
    private DatabaseReference mDatabase,mD,mD1,mD2,offline,mD5;
    private String user=null,op=null,fieldid,loanid="",yearx="'19",monthx=new SimpleDateFormat("MMM").format(new Date());
    private String loan,loaninstallment,dategive,totloan,nextpaymen,duratio,officerid,loanidd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_two, container, false);

        memberprofile activity = (memberprofile) getActivity();
        user = activity.getMyData();

        mAuth=FirebaseAuth.getInstance();
        op=mAuth.getCurrentUser().getUid();
        mUsername=view.findViewById(R.id.memname);
        mUserimage=view.findViewById(R.id.mempic);
        mDategiven=view.findViewById(R.id.dategiven);
        mLoangivn=view.findViewById(R.id.loangiven);
        mIDnum=view.findViewById(R.id.accnum);
        mLoanbal=view.findViewById(R.id.loanbalance);
        mInstallmentamt=view.findViewById(R.id.loaninstallment);
        mInstallments=view.findViewById(R.id.installments);
        mYearlbl=view.findViewById(R.id.t);
        mTab2lbl=view.findViewById(R.id.mb4);
        mTab1lbl=view.findViewById(R.id.mnw);
        mTab3lbl=view.findViewById(R.id.maf);
        mPaymenthist=view.findViewById(R.id.paymenthist);
        mYear=view.findViewById(R.id.yearlbl);
        mTab1=view.findViewById(R.id.monthnowlbl);
        mTab2=view.findViewById(R.id.monthb4lbl);
        mTab3=view.findViewById(R.id.monthaflbl);
        fab=view.findViewById(R.id.floatingActionButton3);


        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        mPaymenthist.setHasFixedSize(true);
        mPaymenthist.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mYearlbl.setText(new SimpleDateFormat("yyyy").format(new Date()));
        mTab1lbl.setText(new SimpleDateFormat("MMMM").format(new Date()));
        mTab2lbl.setText(lastmonth());
        mTab3lbl.setText(nextmonth());

        mD= FirebaseDatabase.getInstance().getReference().child("activeloans");
        mD1= FirebaseDatabase.getInstance().getReference().child("members").child(user);

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsername.setText(dataSnapshot.child("personaldetails").child("name").getValue().toString());
                mIDnum.setText(dataSnapshot.child("personaldetails").child("idnum").getValue().toString());
                Picasso.with(getContext()).load(dataSnapshot.child("personaldetails").child("image").getValue().toString()).into(mUserimage);

                if (dataSnapshot.child("loans").child("info").exists()){
                    mD.child(user).child(dataSnapshot.child("loans").child("info").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                mLoanbal.setText(dataSnapshot.child("balance").getValue().toString());
                                mLoangivn.setText(dataSnapshot.child("amount").getValue().toString());
                                mDategiven.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(dataSnapshot.child("datedisbursed").getValue().toString())));
                                mInstallmentamt.setText(dataSnapshot.child("installment").getValue().toString());
                            }else {
                                LinearLayout linearLayout=view.findViewById(R.id.l1);
                                linearLayout.setVisibility(View.GONE);

                                CardView cardView=view.findViewById(R.id.ataglance);
                                cardView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userwindow = new Intent(getActivity(),newloanrecurrentmember.class);
                userwindow.putExtra("id",user);
                startActivity(userwindow);
            }
        });


        mYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Year")
                        .setCancelable(true)
                        .setItems(R.array.Years, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        mYearlbl.setText("2017");
                                        //yearx="'17";
                                        break;
                                    case 1:
                                        mYearlbl.setText("2018");
                                        //yearx="'18";
                                        break;
                                    case 2:
                                        mYearlbl.setText("2019");
                                        //yearx="'19";
                                        break;
                                    case 3:
                                        mYearlbl.setText("2020");
                                        //yearx="'20";
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
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                builder2.setTitle("Select Month")
                        .setCancelable(true)
                        .setItems(R.array.Months, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        mTab1lbl.setText("January");
                                        mTab2lbl.setText("December");
                                        mTab3lbl.setText("February");
                                        transactions(yearx,"Jan");
                                        break;
                                    case 1:
                                        mTab1lbl.setText("February");
                                        mTab2lbl.setText("January");
                                        mTab3lbl.setText("March");
                                        transactions(yearx,"Feb");
                                        break;
                                    case 2:
                                        mTab1lbl.setText("March");
                                        mTab2lbl.setText("February");
                                        mTab3lbl.setText("April");
                                        transactions(yearx,"Mar");
                                        break;
                                    case 3:
                                        mTab1lbl.setText("April");
                                        mTab2lbl.setText("March");
                                        mTab3lbl.setText("May");
                                        transactions(yearx,"Apr");
                                        break;
                                    case 4:
                                        mTab1lbl.setText("May");
                                        mTab2lbl.setText("April");
                                        mTab3lbl.setText("June");
                                        transactions(yearx,"May");
                                        break;
                                    case 5:
                                        mTab1lbl.setText("June");
                                        mTab2lbl.setText("May");
                                        mTab3lbl.setText("July");
                                        transactions(yearx,"Jun");
                                        break;
                                    case 6:
                                        mTab1lbl.setText("July");
                                        mTab2lbl.setText("June");
                                        mTab3lbl.setText("May");
                                        transactions(yearx,"Jul");
                                        break;
                                    case 7:
                                        mTab1lbl.setText("August");
                                        mTab2lbl.setText("July");
                                        mTab3lbl.setText("September");
                                        transactions(yearx,"Aug");
                                        break;
                                    case 8:
                                        mTab1lbl.setText("September");
                                        mTab2lbl.setText("August");
                                        mTab3lbl.setText("October");
                                        transactions(yearx,"Sep");
                                        break;
                                    case 9:
                                        mTab1lbl.setText("October");
                                        mTab2lbl.setText("September");
                                        mTab3lbl.setText("November");
                                        transactions(yearx,"Oct");
                                        break;
                                    case 10:
                                        mTab1lbl.setText("November");
                                        mTab2lbl.setText("October");
                                        mTab3lbl.setText("December");
                                        transactions(yearx,"Nov");
                                        break;
                                    case 11:
                                        mTab1lbl.setText("December");
                                        mTab2lbl.setText("November");
                                        mTab3lbl.setText("January");
                                        transactions(yearx,"Dec");
                                        break;

                                    default:
                                        mYearlbl.setText(new SimpleDateFormat("yyyy").format(new Date()));
                                        mTab1lbl.setText(new SimpleDateFormat("MMMM").format(new Date()));
                                        mTab2lbl.setText(lastmonth());
                                        mTab3lbl.setText(nextmonth());
                                        transactions(yearx,"Jan");
                                        break;
                                }
                            }
                        });
                AlertDialog alert12 = builder2.create();
                alert12.show();
            }
        });

        switch (mYearlbl.getText().toString()){
            case "2017":
                yearx="'17";
                break;
            case "2018":{
                yearx="'18";
                break;
            }
            case "2019":{
                yearx="'19";
                break;
            }
            case "2020":{
                yearx="'20";
                break;
            }
        }



        transactions(yearx,monthx);
        
        return view;
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

    public static class installmentsHolder extends RecyclerView.ViewHolder{
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
            mGroupname.setText(groupname);
        }
        public void setGroupAmount(String groupname){
            TextView mGroupname = (TextView) mView.findViewById(R.id.trsamount);
            mGroupname.setText(groupname);
        }
        public void setTraname(String groupname){
            TextView mGroupname = (TextView) mView.findViewById(R.id.trstype);
            mGroupname.setText(groupname);
        }

        public void setTrsstatus(String groupname){
            TextView mGroupname = (TextView) mView.findViewById(R.id.trsstatus);
            mGroupname.setText(groupname);
        }
        public void setImage(Drawable icon){
            ImageView imageView = (ImageView) mView.findViewById(R.id.transcpic);
            imageView.setImageDrawable(icon);
        }
    }

    private void transactions(final String year,final String month) {
        FirebaseRecyclerAdapter<transaction,installmentsHolder> firebaseRecyclerAdaptr = new FirebaseRecyclerAdapter<transaction,installmentsHolder>(

                transaction.class,
                R.layout.transaction_row,
                installmentsHolder.class,
                mD1.child("transactions").orderByChild("timestamp")
        )
        {
            @Override
            protected void populateViewHolder(final installmentsHolder viewHolder, final transaction model, final int position) {

                if (model.getDate().contains(year)&&model.getDate().contains(month)) {
                    viewHolder.setDate(model.getDate());
                    viewHolder.setGroupAmount(model.getAmount());
                    viewHolder.setTraname(model.getType());
                    viewHolder.setTrsstatus(model.getStatus());

                    switch (model.getType()) {
                        case "Loan":
                            viewHolder.setImage(getResources().getDrawable(R.drawable.loanlabel));
                            break;
                        case "Installment":
                            viewHolder.setImage(getResources().getDrawable(R.drawable.installmentlabel));
                            break;
                        case "Default":
                            viewHolder.setImage(getResources().getDrawable(R.drawable.defaultlabel));
                            break;
                        case "Arreas":
                            viewHolder.setImage(getResources().getDrawable(R.drawable.arreaslabel));
                            break;
                    }

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.getType().equals("Loan")){
                                Intent userwindow = new Intent(getContext(),loandetails.class);
                                userwindow.putExtra("id",getRef(position).getKey());
                                startActivity(userwindow);
                            }
                        }
                    });

                }else {
                    viewHolder.Layout_hide();
                }

            }
        };

        mPaymenthist.setAdapter(firebaseRecyclerAdaptr);
    }

}
