package com.example.london.financeapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userdayreport extends AppCompatActivity {
    private TextView mClientsseen,mCashcollected;
    private RecyclerView mClientslist,mTransactionlist,mClientsReg;
    private DatabaseReference mD1,mD2,mD3;
    private FirebaseAuth mAuth;
    private String user,date;
    private double amount=0;
    private LinearLayoutManager linearLayoutManager,linearLayoutManager2;
    private loanappsadapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdayreport);
        date=getIntent().getExtras().getString("date");
        Log.e("date",date);
        mClientsseen=findViewById(R.id.clientsseen);
        mClientsReg=findViewById(R.id.clientsregs);
        mCashcollected=findViewById(R.id.cashcollected);
        mClientslist=findViewById(R.id.clientstoday);
        mTransactionlist=findViewById(R.id.paymentsrecieved);

        linearLayoutManager = new LinearLayoutManager(userdayreport.this,LinearLayoutManager.HORIZONTAL,false);
        mClientslist.setHasFixedSize(true);
        mClientslist.setLayoutManager(linearLayoutManager);


        linearLayoutManager2 = new LinearLayoutManager(userdayreport.this,LinearLayoutManager.HORIZONTAL,false);
        mClientsReg.setHasFixedSize(true);
        mClientsReg.setLayoutManager(linearLayoutManager2);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mTransactionlist.setHasFixedSize(true);
        mTransactionlist.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mD1= FirebaseDatabase.getInstance().getReference().child("fieldwork").child(user).child(date);
        mD2= FirebaseDatabase.getInstance().getReference().child("activeclients").child(user);
        mD3= FirebaseDatabase.getInstance().getReference().child("officerrequests").child(user).child(date);

        final ArrayList<String> arrayList = new ArrayList<String>();

        mD3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==0){
                    TextView labl=findViewById(R.id.title5);
                    labl.setVisibility(View.GONE);
                    mClientsReg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count=dataSnapshot.child("clientseen").getChildrenCount();
                if (count==0){
                    mClientsseen.setText("No Client Seen.");

                }else {
                mClientsseen.setText(String.valueOf(count)+" Clients");
                }

                if (dataSnapshot.child("transactions").exists()){
                for (DataSnapshot snapshot:dataSnapshot.child("transactions").getChildren()){
                    double amounts= Double.parseDouble(snapshot.child("amount").getValue().toString());
                    amount=amount+amounts;
                }
                mCashcollected.setText("Kshs. "+String.valueOf(amount));
                }else {
                    mCashcollected.setText("Kshs. 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter=new loanappsadapter2(loanapps.class, R.layout.masterloanapp_row, loanappsholder.class, mD3, userdayreport.this);
        mClientsReg.setAdapter(adapter);

        FirebaseRecyclerAdapter<activeclients,clientstodayHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activeclients, clientstodayHolder>(
                activeclients.class,
                R.layout.todayclient_row,
                clientstodayHolder.class,
                mD2
        )
        {
            @Override
            protected void populateViewHolder(final clientstodayHolder viewHolder, final activeclients model, int position) {
                if (model.getPaiddate().equals(date)){
                viewHolder.setName(model.getName());
                viewHolder.setGroupAmount(getApplicationContext(),model.getUid());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next =new Intent(userdayreport.this,memberprofile.class);
                        next.putExtra("id",model.getUid());
                        startActivity(next);
                    }
                });
                }else {
                    viewHolder.Layout_hide();
                }

            }
        };

        mClientslist.setAdapter(firebaseRecyclerAdapter);

        FirebaseRecyclerAdapter<transaction,installmentsHolder> firebaseRecyclerAdapters = new FirebaseRecyclerAdapter<transaction,installmentsHolder>(
                transaction.class,
                R.layout.transactioninstallments_row,
                installmentsHolder.class,
                mD1.child("transactions")
        )
        {
            @Override
            protected void populateViewHolder(final installmentsHolder viewHolder, final transaction model, int position) {
                viewHolder.setDate(model.getDate());
                viewHolder.setGroupAmount(getApplicationContext(),model.getAmount());
                viewHolder.setmember(model.getMemberuid());
            }
        };

        mTransactionlist.setAdapter(firebaseRecyclerAdapters);
    }

    public static class clientstodayHolder extends RecyclerView.ViewHolder{
        private final CardView layout;
        final CardView.LayoutParams layoutParams;
        View mView;


        public clientstodayHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (CardView) itemView.findViewById(R.id.todayclient);
            layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.rowtitle);
            mGroupname.setText(groupname);
        }
        public void setGroupAmount(Context ctx, String groupname){
            ImageView mGroupname = (ImageView) mView.findViewById(R.id.rowimage);
            //mGroupname.setText("Kshs. "+groupname);

            Glide.with(ctx).load(groupname).into(mGroupname);
        }
    }

    public static class installmentsHolder extends RecyclerView.ViewHolder{
        private final CardView layout;
        final CardView.LayoutParams layoutParams;
        private DatabaseReference mD4= FirebaseDatabase.getInstance().getReference().child("members");
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

        public void setmember(String groupname){

            mD4.child(groupname).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("personaldetails").child("name").getValue().toString();

                    TextView mGroupnamr = (TextView)mView.findViewById(R.id.trsstatus);
                    mGroupnamr.setText(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void setGroupAmount(Context ctx,String groupname){
            TextView mGroupname = (TextView) mView.findViewById(R.id.trsamount);
            mGroupname.setText(groupname);
        }
    }
}
