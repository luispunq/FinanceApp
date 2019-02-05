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

public class allmyclients2 extends AppCompatActivity {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(allmyclients2.this);
        mClientstoday.setHasFixedSize(true);
        mClientstoday.setLayoutManager(linearLayoutManager);

        mD2= FirebaseDatabase.getInstance().getReference().child("activeclients").child(user);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<activeclients,clientstodayHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activeclients, clientstodayHolder>(
                activeclients.class,
                R.layout.todayclient2_row,
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
                        Intent next =new Intent(allmyclients2.this,memberprofile.class);
                        next.putExtra("id",model.getUid());
                        startActivity(next);
                    }
                });

            }
        };

        mClientstoday.setAdapter(firebaseRecyclerAdapter);
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

}
