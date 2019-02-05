package com.example.london.financeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class coordinator extends AppCompatActivity {
    private CardView mAddmember,mReport,myClients;
    private TextView mUsername,mDate,mClients,mWelcometitle;
    private ImageView mUserimage;
    private RecyclerView mClientstoday;
    private Toolbar mBar;
    private FirebaseAuth mAuth;
    private String user=null,fieldid;
    private DatabaseReference mDatabase,mD,mD1,mD2,offline,mD5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        mBar=findViewById(R.id.bar1);
        setSupportActionBar(mBar);
        getSupportActionBar().setTitle("Coordinator");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        mDate=findViewById(R.id.day);
        mClients=findViewById(R.id.clientnum);
        mUsername=findViewById(R.id.cname);
        mWelcometitle=findViewById(R.id.gtitle);
        mUserimage=findViewById(R.id.cimage);
        mAddmember=findViewById(R.id.adduserb);
        mReport=findViewById(R.id.reports);
        myClients=findViewById(R.id.allmyclients);
        mClientstoday=findViewById(R.id.clientstoday);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(coordinator.this,LinearLayoutManager.HORIZONTAL,false);
        mClientstoday.setHasFixedSize(true);
        mClientstoday.setLayoutManager(linearLayoutManager);

        mD1= FirebaseDatabase.getInstance().getReference().child("Employees").child(user);
        mD2= FirebaseDatabase.getInstance().getReference().child("activeclients").child(user);
        mD=FirebaseDatabase.getInstance().getReference().child("Active").child(user);



        mDate.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

        mD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mClients.setText(String.valueOf(dataSnapshot.getChildrenCount())+" Clients");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                //fieldid = dataSnapshot.child("fieldid").getValue().toString();
                mUsername.setText(name);
                mWelcometitle.setText("Welcome "+name);
                Picasso.with(coordinator.this).load(image).into(mUserimage);
                Log.e("load","loaded2");
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
                        viewHolder.setGroupAmount(getApplicationContext(),model.getImage());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent next =new Intent(coordinator.this,loanrepayment.class);
                                next.putExtra("id",model.getUid());
                                startActivity(next);
                            }
                        });

                    }
                };

                mClientstoday.setAdapter(firebaseRecyclerAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAddmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(coordinator.this,newmemberemail.class);
                next.putExtra("id",user);
                startActivity(next);
            }
        });
        mReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(coordinator.this,dateselector.class);
                startActivity(next);
            }
        });

        myClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(coordinator.this,allmyclients.class);
                startActivity(next);
            }
        });


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
        public void setGroupAmount(Context ctx,String groupname){
            ImageView mGroupname = (ImageView) mView.findViewById(R.id.rowimage);
            Glide.with(ctx).load(groupname).into(mGroupname);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exiting App")
                .setMessage("Are you sure you want to exit app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
