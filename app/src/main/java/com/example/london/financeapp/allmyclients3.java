package com.example.london.financeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class allmyclients3 extends AppCompatActivity {
    private RecyclerView mClientstoday;
    private DatabaseReference mDatabase,mD,mD1,mD2,offline,mD5;
    private FirebaseAuth mAuth;
    private String user=null,fieldid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmyclients);
        mClientstoday=findViewById(R.id.clientstoday);
        mAuth=FirebaseAuth.getInstance();
        user=getIntent().getExtras().getString("id");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(allmyclients3.this);
        mClientstoday.setHasFixedSize(true);
        mClientstoday.setLayoutManager(linearLayoutManager);

        mD2= FirebaseDatabase.getInstance().getReference().child("members").child(user);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<member,membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member, membersViewHolder>(
                member.class,
                R.layout.memberslist2_row,
                membersViewHolder.class,
                mD2
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder viewHolder, final member model, int position) {


                viewHolder.setMemberPhoto(getApplicationContext(),model.getImage());
                viewHolder.setMemberName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent payments = new Intent(allmyclients3.this,memberprofile.class);
                        payments.putExtra("id",model.getUid());
                        startActivity(payments);
                    }
                });


            }
        };

        mClientstoday.setAdapter(firebaseRecyclerAdapter);
    }

    public static class membersViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public membersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setMemberName(String name){
            TextView membername = (TextView)mView.findViewById(R.id.memname);
            membername.setText(name);
            TextView membernames = (TextView)mView.findViewById(R.id.memstatus);
            membernames.setText("Inactive");
        }
        public void setMemberPhoto(Context ctx, String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.memberpic);
            Picasso.with(ctx).load(image).into(memberphoto);
        }
    }

}
