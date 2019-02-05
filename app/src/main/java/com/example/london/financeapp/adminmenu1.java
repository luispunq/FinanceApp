package com.example.london.financeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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


public class adminmenu1 extends Fragment {
    private TextView mAdminname,mClientbase,mLoansgiven,mLoanCount,mInboxitems,mIncomebtn;
    private ImageView mAdminimage;
    private CardView daily,month;
    private RecyclerView mClientstoday;
    private DatabaseReference mD1,mD2,mD3,mD4,mDatabasehelp,Database,mFee;
    private FirebaseAuth mAuth;
    private String user;
    private double amount=0,intere=0,fee=0;
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
        final View view=inflater.inflate(R.layout.fragment_adminmenu1, container, false);
        mAdminname=view.findViewById(R.id.cname);
        mClientbase=view.findViewById(R.id.clientbase);
        mLoansgiven=view.findViewById(R.id.loansgiven);
        mIncomebtn=view.findViewById(R.id.incomess);
        mLoanCount=view.findViewById(R.id.loanintray);
        mInboxitems=view.findViewById(R.id.inboxcount);
        mAdminimage=view.findViewById(R.id.userimage);
        mAdminimage.setVisibility(View.VISIBLE);
        mLoanlink=view.findViewById(R.id.loantask);
        daily=view.findViewById(R.id.dailyr);
        month=view.findViewById(R.id.monthlyr);
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
        mFee=FirebaseDatabase.getInstance().getReference().child("fees").child("all");

        mLoanlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.nav_loanapprovals,view);
            }
        });

        mIncomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Report Type")
                        .setCancelable(true)
                        .setItems(R.array.coloanoptionsc, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent payments = new Intent(getContext(),dateselector4c.class);
                                        startActivity(payments);
                                        break;
                                    case 1:
                                        Intent paymentse = new Intent(getContext(),monthselector4.class);
                                        startActivity(paymentse);
                                        break;
                                    case 2:
                                        Intent paymentsw = new Intent(getContext(),rangeselector4.class);
                                        startActivity(paymentsw);
                                        break;
                                }
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

        mD3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mClientbase.setText(String.valueOf(dataSnapshot.getChildrenCount())+" Clients");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amount=0;
                intere=0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        double amounts= Double.parseDouble(snapshot.child("balance").getValue().toString());
                        double amountinterest= Double.parseDouble(snapshot.child("interest").getValue().toString());
                        amount=amount+amounts;
                        intere=intere+amountinterest;
                    }
                    mLoansgiven.setText("Kshs. "+String.valueOf(amount));

                }else {
                    mLoansgiven.setText("Kshs. 0");
                }
                Log.e("Intre",String.valueOf(intere));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFee.child("loanprocessing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        double amountsfees = Double.parseDouble(snapshot.child("amount").getValue().toString());
                        todaycount2 = todaycount2 + amountsfees;
                        todaycount=todaycount2+intere;

                        //mIntrval.setText("Kshs. "+String.valueOf(todaycount));
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

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent card = new Intent(getContext(),dateselector2.class);
                startActivity(card);
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent card2 = new Intent(getContext(),monthselector.class);
                startActivity(card2);
            }
        });

        mDatabasehelp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                todaycount4=0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        todaycount4=todaycount4+1;
                    }

                    mInboxitems.setText(String.valueOf(todaycount4)+" unread");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                    }

                    mLoanCount.setText(String.valueOf((int) todaycount5)+" loans to review");
                }
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
            case R.id.nav_home:
                fragment = new adminmenu1();
                break;
            case R.id.nav_clients:
                fragment = new adminmenu2();
                break;
            case R.id.nav_employees:
                fragment = new adminmenu4();
                break;
            case R.id.nav_loanapprovals:
                fragment = new loanapplications();
                break;
            case R.id.nav_loans:
                fragment = new adminmenu3();
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
