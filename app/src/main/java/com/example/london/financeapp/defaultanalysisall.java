package com.example.london.financeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class defaultanalysisall extends AppCompatActivity {
    private RecyclerView mloanhist;
    private DatabaseReference mDatabase,mD,mD2,mD4;
    private String st2=null,st4=null,st3=null,st1=null;
    private String mdate,mtype,year,month;
    private long start,end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaultanalysis);


        mloanhist=findViewById(R.id.memberstat_list);
        mloanhist.setHasFixedSize(true);
        mloanhist.setLayoutManager(new LinearLayoutManager(this));

        mD4= FirebaseDatabase.getInstance().getReference().child("activedefaults").child("all");


        FirebaseRecyclerAdapter<activedefaults, memberstatViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activedefaults, memberstatViewHolder>(

                activedefaults.class,
                R.layout.defaults_row,
                memberstatViewHolder.class,
                mD4
        ) {
            @Override
            protected void populateViewHolder(final memberstatViewHolder viewHolder, final activedefaults model, int position) {

                viewHolder.setName(model.getLoanid());
                viewHolder.setLoan(model.getLoanid());
                viewHolder.setLastpaid(model.getLoanid());
                viewHolder.setDays(model.getDays());
                viewHolder.setFees(model.getFee());
                viewHolder.setOfficer(model.getLoanid());

                viewHolder.link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next =new Intent(defaultanalysisall.this,memberprofile.class);
                        next.putExtra("id",model.getMemberid());
                        startActivity(next);
                    }
                });
            }
        };

        mloanhist.setAdapter(firebaseRecyclerAdapter);



    }

    public static class memberstatViewHolder extends RecyclerView.ViewHolder{
        private TextView link;
        View mView;
        private DatabaseReference check,mD;
        private String iid;



        public memberstatViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            link=mView.findViewById(R.id.mstatname);
            mD= FirebaseDatabase.getInstance().getReference().child("activeloans").child("all");

        }


        public void setName(String date){
            mD.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView amountgiven = (TextView)mView.findViewById(R.id.mstatname);
                    amountgiven.setText(dataSnapshot.child("membername").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setLoan(String detail){
            mD.child(detail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView amountgiven = (TextView)mView.findViewById(R.id.mstatsav);
                    amountgiven.setText("Kshs. "+dataSnapshot.child("balance").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setLastpaid(String amount){

            mD.child(amount).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView amountgiven = (TextView)mView.findViewById(R.id.mstatadv);
                    amountgiven.setText(dataSnapshot.child("lastinstalmentdate").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        public void setDays(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatloan);
            amountgiven.setText(amount+" Days");
        }
        public void setFees(final String amount){

            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk);
            amountgiven.setText("Kshs. "+amount);

        }
        public void setOfficer(final String amount){

            mD.child(amount).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk2);
                        amountgiven.setText(dataSnapshot.child("officername").getValue().toString());
                    }else {
                        TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk2);
                        amountgiven.setText("-");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }
}
