package com.example.london.financeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class profitability extends AppCompatActivity {
    private TextView mTotIncomeinst,mFeesTot,mTotEarned;
    private DatabaseReference mD5,mD4,mD3;
    private RecyclerView mClients,mOfficers;
    private double dTotLoans=0,dTotInterest=0,dLoanCash=0,dBal=0;
    private String st2=null,st4=null,st3=null,st1=null;
    private String mdate,mtype,year,month;
    private long start,end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profitability);

        Bundle extras = getIntent().getExtras();
        mtype = extras.getString("type");
        mdate = extras.getString("date");
        year = extras.getString("year");
        month = extras.getString("month");
        start =Long.parseLong( extras.getString("start"));
        end = Long.parseLong(extras.getString("end"));
        

        mTotIncomeinst=findViewById(R.id.totalincome);
        mFeesTot=findViewById(R.id.totalfees);
        mTotEarned=findViewById(R.id.totalincomeearned);

        mClients=findViewById(R.id.memtransactionlist);
        mOfficers=findViewById(R.id.offtransactionlist);

        mClients.setHasFixedSize(true);
        mClients.setLayoutManager(new LinearLayoutManager(this));

        mOfficers.setHasFixedSize(true);
        mOfficers.setLayoutManager(new LinearLayoutManager(this));

        mD4= FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("all");
        mD5= FirebaseDatabase.getInstance().getReference().child("loanrepayment").child("daily");
        mD3= FirebaseDatabase.getInstance().getReference().child("loanrepayment").child(year).child(month);

        switch (mtype) {
            case "daily": {
                Log.e("type","day");
                mD5.child(mdate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dBal = 0;
                        dLoanCash = 0;
                        dTotLoans = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            if (snapshot.child("type").getValue().toString().equals("Installment")){
                                String loantot = snapshot.child("amount").getValue().toString();
                                double rdBal=Double.parseDouble(loantot);
                                double inco=0.2*rdBal;
                                dLoanCash=dLoanCash+inco;

                                mTotIncomeinst.setText("Kshs. "+String.valueOf(dLoanCash));
                            }
                            if (snapshot.child("type").getValue().toString().equals("Fees")){
                                String loantot = snapshot.child("amount").getValue().toString();
                                double rdTotLoans=Double.parseDouble(loantot);
                                double inco=0.2*rdTotLoans;
                                dBal=dBal+inco;

                                mFeesTot.setText("Kshs. "+String.valueOf(dBal));
                            }
                            dTotInterest=dBal+dLoanCash;
                            mTotEarned.setText("Kshs. "+dTotInterest);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<transaction, transViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<transaction, transViewHolder>(

                        transaction.class,
                        R.layout.clientincome_row,
                        transViewHolder.class,
                        mD5.child(mdate)
                ) {
                    @Override
                    protected void populateViewHolder(final transViewHolder viewHolder, final transaction model, int position) {

                        final String rk = getRef(position).getKey();

                        viewHolder.setName(model.getName());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setLoancf(model.getAmount());
                        viewHolder.setIncome(model.getType(),model.getAmount());
                        
                    }
                };

                mClients.setAdapter(firebaseRecyclerAdapter);

                FirebaseRecyclerAdapter<transaction, transViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<transaction, transViewHolder>(

                        transaction.class,
                        R.layout.clientincome_row,
                        transViewHolder.class,
                        mD5.child(mdate)
                ) {
                    @Override
                    protected void populateViewHolder(final transViewHolder viewHolder, final transaction model, int position) {

                        final String rk = getRef(position).getKey();


                    }
                };

                mOfficers.setAdapter(firebaseRecyclerAdapter1);

                break;
            }
            case "monthly": {
                Log.e("type","month");
                mD3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dBal = 0;
                        dLoanCash = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            if (snapshot.child("type").getValue().toString().equals("Installment")){
                                String loantot = snapshot.child("amount").getValue().toString();
                                double rdBal=Double.parseDouble(loantot);
                                double inco=0.2*rdBal;
                                dLoanCash=dLoanCash+inco;

                                mTotIncomeinst.setText("Kshs. "+String.valueOf(dLoanCash));
                            }
                            if (snapshot.child("type").getValue().toString().equals("Fees")){
                                String loantot = snapshot.child("amount").getValue().toString();
                                double rdTotLoans=Double.parseDouble(loantot);
                                double inco=0.2*rdTotLoans;
                                dBal=dBal+inco;

                                mFeesTot.setText("Kshs. "+String.valueOf(dBal));
                            }
                            dTotInterest=dBal+dLoanCash;
                            mTotEarned.setText("Kshs. "+dTotInterest);
                            
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<transaction, transViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<transaction, transViewHolder>(

                        transaction.class,
                        R.layout.clientincome_row,
                        transViewHolder.class,
                        mD3
                ) {
                    @Override
                    protected void populateViewHolder(final transViewHolder viewHolder, final transaction model, int position) {

                        final String rk = getRef(position).getKey();

                        viewHolder.setName(model.getName());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setLoancf(model.getAmount());
                        viewHolder.setIncome(model.getType(),model.getAmount());

                    }
                };

                mClients.setAdapter(firebaseRecyclerAdapter);

                FirebaseRecyclerAdapter<transaction, transViewHolder> firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<transaction, transViewHolder>(

                        transaction.class,
                        R.layout.clientincome_row,
                        transViewHolder.class,
                        mD3
                ) {
                    @Override
                    protected void populateViewHolder(final transViewHolder viewHolder, final transaction model, int position) {

                        final String rk = getRef(position).getKey();



                    }
                };

                mOfficers.setAdapter(firebaseRecyclerAdapter2);

                break;
            }
            default: {
                Log.e("type","range");
                mD4.orderByChild("datedisbursed").startAt(start).endAt(end).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dBal = 0;
                        dLoanCash = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("type").getValue().toString().equals("Installment")){
                                String loantot = snapshot.child("amount").getValue().toString();
                                double rdBal=Double.parseDouble(loantot);
                                double inco=0.2*rdBal;
                                dLoanCash=dLoanCash+inco;

                                mTotIncomeinst.setText("Kshs. "+String.valueOf(dLoanCash));
                            }
                            if (snapshot.child("type").getValue().toString().equals("Fees")){
                                String loantot = snapshot.child("amount").getValue().toString();
                                double rdTotLoans=Double.parseDouble(loantot);
                                double inco=0.2*rdTotLoans;
                                dBal=dBal+inco;

                                mFeesTot.setText("Kshs. "+String.valueOf(dBal));
                            }
                            dTotInterest=dBal+dLoanCash;
                            mTotEarned.setText("Kshs. "+dTotInterest);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<transaction, transViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<transaction, transViewHolder>(

                        transaction.class,
                        R.layout.clientincome_row,
                        transViewHolder.class,
                        mD4.orderByChild("datedisbursed").startAt(start).endAt(end)
                ) {
                    @Override
                    protected void populateViewHolder(final transViewHolder viewHolder, final transaction model, int position) {


                        

                    }
                };

                mClients.setAdapter(firebaseRecyclerAdapter);

                FirebaseRecyclerAdapter<transaction, transViewHolder> firebaseRecyclerAdapter3 = new FirebaseRecyclerAdapter<transaction, transViewHolder>(

                        transaction.class,
                        R.layout.clientincome_row,
                        transViewHolder.class,
                        mD4.orderByChild("datedisbursed").startAt(start).endAt(end)
                ) {
                    @Override
                    protected void populateViewHolder(final transViewHolder viewHolder, final transaction model, int position) {




                    }
                };

                mOfficers.setAdapter(firebaseRecyclerAdapter3);
                break;
            }
        }

    }

    public static class transViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;
        private DatabaseReference mD;

        public transViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mD=FirebaseDatabase.getInstance().getReference().child("transaction");
            layout = (RelativeLayout) itemView.findViewById(R.id.clientincome_card);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setName(String name){
            TextView mGroupname = mView.findViewById(R.id.clientname);
            mGroupname.setText(name);
        }

        public void setDate(String advcgvn){
            TextView mGrouname = (TextView) mView.findViewById(R.id.clientdate);
            mGrouname.setText(advcgvn);
        }
        public void setLoancf(String loancf){
            TextView mGroupame = mView.findViewById(R.id.clientamount);
            mGroupame.setText("Kshs. "+loancf);
        }
        public void setIncome(String type,String loancf){
            TextView mGroupame = mView.findViewById(R.id.clientincome);
            double dloaninst=Double.parseDouble(loancf);

            if (type.equals("Installment")){
                double inco=0.2*dloaninst;
                mGroupame.setText("Kshs. "+String.valueOf(inco));
            }else {
                mGroupame.setText("Kshs. "+loancf);
            }

        }
    }


}
