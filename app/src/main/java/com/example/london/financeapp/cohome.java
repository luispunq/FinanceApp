package com.example.london.financeapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;


public class cohome extends Fragment {
    private TextView mAdminname,mClientbase,mLoanCount,mInboxitems;
    private ImageView mAdminimage;
    private RecyclerView mClientstoday;
    private DatabaseReference mD1,mD2,mD3,mD4,mDatabasehelp,Database;
    private FirebaseAuth mAuth;
    private String user;
    private double amount=0;
    private CardView card1,card2,card3,mLoanlink,mQuerrieslink;
    private double todaycount=0,todaycount2=0,todaycount3=0,todaycount4=0,todaycount5=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_cohome, container, false);

        mAdminname=view.findViewById(R.id.cname);
        mClientbase=view.findViewById(R.id.clientbase);
        mLoanCount=view.findViewById(R.id.loanintray);
        mInboxitems=view.findViewById(R.id.inboxcount);
        mAdminimage=view.findViewById(R.id.userimage);
        mLoanlink=view.findViewById(R.id.loantask);
        mClientstoday=view.findViewById(R.id.clientstoday);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mClientstoday.setHasFixedSize(true);
        mClientstoday.setLayoutManager(linearLayoutManager);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mD1= FirebaseDatabase.getInstance().getReference().child("Employees").child(user);
        mD2= FirebaseDatabase.getInstance().getReference().child("activeclients").child("all");
        mD4= FirebaseDatabase.getInstance().getReference().child("activeloans").child("all");
        mD3= FirebaseDatabase.getInstance().getReference().child("allmembers");
        mDatabasehelp= FirebaseDatabase.getInstance().getReference().child("helpquerris");
        Database=FirebaseDatabase.getInstance().getReference().child("loanrequests");

        mLoanlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.nav_loanapprovalsco,view);
            }
        });


        Database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    todaycount5=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("status").getValue().toString().equals("pending")){
                            todaycount5=todaycount5+1;
                            Log.e("count",String.valueOf(todaycount5));
                        }

                        mLoanCount.setText(String.valueOf((int) todaycount5)+" loans to review");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                mAdminname.setText(name);
                Picasso.with(getContext()).load(image).into(mAdminimage);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amount=0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("nextinstallmentdate").getValue().toString().equals(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()))){
                            amount=amount+1;
                        }
                    }
                    mClientbase.setText(String.valueOf(amount)+" Loans");
                }else {
                    mClientbase.setText("No loans yet.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                todaycount5=0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        todaycount5=todaycount5+1;
                        Log.e("count",String.valueOf(todaycount5));
                    }

                    //mInboxitems.setText(String.valueOf((int) todaycount5)+" loans to review");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<activeclients,clientstodayHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activeclients, clientstodayHolder>(
                activeclients.class,
                R.layout.todayclient_row,
                clientstodayHolder.class,
                mD2
        )
        {
            @Override
            protected void populateViewHolder(final clientstodayHolder viewHolder, final activeclients model, int position) {

                if (model.getDate().equals(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()))){
                viewHolder.setName(model.getName());
                viewHolder.setGroupAmount(getContext(),model.getImage());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent next =new Intent(getContext(),loanrepayment.class);
                            next.putExtra("id",model.getUid());
                            startActivity(next);
                        }
                    });
                }else {
                    viewHolder.Layout_hide();
                }


            }
        };
        mClientstoday.setAdapter(firebaseRecyclerAdapter);


        return view;
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
            Log.e("image",groupname);
            ImageView mGroupname = (ImageView) mView.findViewById(R.id.rowimage);
            Glide.with(ctx).load(groupname).into(mGroupname);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }
    private void displaySelectedScreen(int itemId,View view) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_homeco:
                fragment = new cohome();
                break;
            case R.id.nav_employeesco:
                fragment = new adminmenu4();
                break;
            case R.id.nav_loanapprovalsco:
                fragment = new coloanapplications();
                break;
            case R.id.nav_loansco:
                fragment = new coloans();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

    }

}
