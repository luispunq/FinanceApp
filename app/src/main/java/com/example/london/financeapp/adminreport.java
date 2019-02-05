package com.example.london.financeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminreport extends AppCompatActivity {
    private TextView mClientcount,mCashcollected,mMpesa,mLoanGiven;
    private RecyclerView mClientsseen,mTransaction;
    private String type,date,monthx,yearx,month,year;
    private DatabaseReference mD1,mD2,mD3;
    private LinearLayoutManager linearLayoutManager;
    private double amount=0,seencountalll=0,seencountdayi=0,seencountdayd=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminreport);
        Bundle extras = getIntent().getExtras();
        date = extras.getString("date");
        type = extras.getString("type");
        mClientcount=findViewById(R.id.clientsseen);
        mCashcollected=findViewById(R.id.cashcollected);
        mMpesa=findViewById(R.id.mpesacollected);
        mLoanGiven=findViewById(R.id.loansgiven);

        if (type.equals("month")){
            String string = date;
            String[] parts = string.split("-");
            monthx = parts[0];
            year = parts[1];
            yearx =parts[2];



        }

        mClientsseen=findViewById(R.id.clientstoday);
        mTransaction=findViewById(R.id.paymentsrecieved);

        linearLayoutManager = new LinearLayoutManager(adminreport.this,LinearLayoutManager.HORIZONTAL,false);
        mClientsseen.setHasFixedSize(true);
        mClientsseen.setLayoutManager(linearLayoutManager);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mTransaction.setHasFixedSize(true);
        mTransaction.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mD1= FirebaseDatabase.getInstance().getReference().child("fieldwork");
        mD2= FirebaseDatabase.getInstance().getReference().child("transactions");
        mD3= FirebaseDatabase.getInstance().getReference().child("activeclients");

        if (type.equals("daily")){

            mD1.child("all").child(date).child("clientseen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mClientcount.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mD2.child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("date").getValue().toString().equals(date)){
                        if (snapshot.child("type").getValue().toString().equals("Installment")){
                            double amt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            seencountdayi=seencountdayi+amt;
                        }
                        }
                    }
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("date").getValue().toString().equals(date)){
                        if (snapshot.child("type").getValue().toString().equals("Default")){
                            double amts=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            seencountdayd=seencountdayd+amts;
                        }
                        }
                    }
                    double tot=seencountdayi+seencountdayd;
                    mCashcollected.setText("Kshs. "+String.valueOf(tot));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mD2.child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("date").getValue().toString().equals(date)){
                        if (snapshot.child("type").getValue().toString().equals("Loan")){
                            double amt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            seencountalll=seencountalll+amt;
                        }
                        }
                    }
                    mLoanGiven.setText("Kshs. "+String.valueOf(seencountalll));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseRecyclerAdapter<activeclients,clientstodayHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activeclients, clientstodayHolder>(
                    activeclients.class,
                    R.layout.todayclient_row,
                    clientstodayHolder.class,
                    mD3.child("all")
            )
            {
                @Override
                protected void populateViewHolder(final clientstodayHolder viewHolder, final activeclients model, int position) {
                    if (model.getPaiddate().equals(date)) {
                        viewHolder.setName(model.getName());
                        viewHolder.setGroupAmount(getApplicationContext(), model.getUid());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent next = new Intent(adminreport.this, memberprofile.class);
                                next.putExtra("id", model.getUid());
                                startActivity(next);
                            }
                        });
                    }else {
                        viewHolder.Layout_hide();
                    }

                }
            };

            mClientsseen.setAdapter(firebaseRecyclerAdapter);

            FirebaseRecyclerAdapter<transaction,installmentsHolder> firebaseRecyclerAdaptr = new FirebaseRecyclerAdapter<transaction,installmentsHolder>(

                    transaction.class,
                    R.layout.transaction_row,
                    installmentsHolder.class,
                    mD2.child("all").orderByChild("timestamp")
            )
            {
                @Override
                protected void populateViewHolder(final installmentsHolder viewHolder, final transaction model, int position) {

                    if (model.getDate().equals(date)) {
                        viewHolder.setDate(model.getDate());
                        viewHolder.setGroupAmount(model.getAmount());
                        viewHolder.setTraname(model.getType());
                        viewHolder.setTrsstatus(model.getName());

                        switch (model.getType()) {
                            case "Loan":
                                Log.e("Op","L");
                                viewHolder.setImage(getResources().getDrawable(R.drawable.loanlabel));
                                break;
                            case "Installment":
                                Log.e("Op","I");
                                viewHolder.setImage(getResources().getDrawable(R.drawable.installmentlabel));
                                break;
                            case "Default":
                                Log.e("Op","D");
                                viewHolder.setImage(getResources().getDrawable(R.drawable.defaultlabel));
                                break;
                            case "Arreas":
                                Log.e("Op","A");
                                viewHolder.setImage(getResources().getDrawable(R.drawable.arreaslabel));
                                break;
                        }

                    }else {
                        viewHolder.Layout_hide();
                    }

                }
            };

            mTransaction.setAdapter(firebaseRecyclerAdaptr);


        }else {
            Log.e("Yearx Monthx Year",yearx+" "+monthx+" "+year);

            mD1.child("all").child(yearx).child(monthx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mClientcount.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mD2.child(yearx).child(monthx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("type").getValue().toString().equals("Installment")){
                            double amt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            seencountdayi=seencountdayi+amt;
                        }
                    }
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("type").getValue().toString().equals("Default")){
                            double amts=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            seencountdayd=seencountdayd+amts;
                        }
                    }
                    double tot=seencountdayi+seencountdayd;
                    mCashcollected.setText("Kshs. "+String.valueOf(tot));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mD2.child(yearx).child(monthx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("type").getValue().toString().equals("Loan")){
                            double amt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            seencountalll=seencountalll+amt;
                        }
                    }
                    mLoanGiven.setText("Kshs. "+String.valueOf(seencountalll));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseRecyclerAdapter<activeclients,clientstodayHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activeclients, clientstodayHolder>(
                    activeclients.class,
                    R.layout.todayclient_row,
                    clientstodayHolder.class,
                    mD3.child("all")
            )
            {
                @Override
                protected void populateViewHolder(final clientstodayHolder viewHolder, final activeclients model, int position) {
                    if (model.getPaiddate().contains(monthx)&&model.getPaiddate().contains(year)) {
                        viewHolder.setName(model.getName());
                        viewHolder.setGroupAmount(getApplicationContext(), model.getUid());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent next = new Intent(adminreport.this, memberprofile.class);
                                next.putExtra("id", model.getUid());
                                startActivity(next);
                            }
                        });
                    }else {
                        viewHolder.Layout_hide();
                    }

                }
            };

            mClientsseen.setAdapter(firebaseRecyclerAdapter);

            FirebaseRecyclerAdapter<transaction,installmentsHolder> firebaseRecyclerAdaptr = new FirebaseRecyclerAdapter<transaction,installmentsHolder>(

                    transaction.class,
                    R.layout.transaction_row,
                    installmentsHolder.class,
                    mD2.child(yearx).child(monthx).orderByChild("timestamp")
            )
            {
                @Override
                protected void populateViewHolder(final installmentsHolder viewHolder, final transaction model, int position) {

                    viewHolder.setDate(model.getDate());
                    viewHolder.setGroupAmount(model.getAmount());
                    viewHolder.setTraname(model.getType());
                    viewHolder.setTrsstatus(model.getName());

                    switch (model.getType()) {
                        case "Loan":
                            Log.e("Op","L");
                            viewHolder.setImage(getResources().getDrawable(R.drawable.loanlabel));
                            break;
                        case "Installment":
                            Log.e("Op","I");
                            viewHolder.setImage(getResources().getDrawable(R.drawable.installmentlabel));
                            break;
                        case "Default":
                            Log.e("Op","D");
                            viewHolder.setImage(getResources().getDrawable(R.drawable.defaultlabel));
                            break;
                        case "Arreas":
                            Log.e("Op","A");
                            viewHolder.setImage(getResources().getDrawable(R.drawable.arreaslabel));
                            break;
                    }

                }
            };

            mTransaction.setAdapter(firebaseRecyclerAdaptr);

        }

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
            Glide.with(ctx).load(groupname).into(mGroupname);
        }
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

}
