package com.example.london.financeapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;


public class adminmenu2 extends Fragment {
    private TextView mClientbase,mActivemembers;
    private EditText mFilter;
    private Spinner mSpinner;
    private DatabaseReference mD1,mD2,mD3,mD4;
    private FloatingActionButton fab;
    private RecyclerView mMemberlist;
    private int op1=0;
    private CardView mTab1,mTab2,mTab3;
    private String order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_adminmenu2, container, false);
        mClientbase=view.findViewById(R.id.loanbalance);
        mActivemembers=view.findViewById(R.id.loaninstallment);
        mFilter=view.findViewById(R.id.filter);
        mTab1=view.findViewById(R.id.tab1);
        mTab2=view.findViewById(R.id.tab2);
        mTab3=view.findViewById(R.id.tab3);
        fab=view.findViewById(R.id.floatingActionButton);
        mMemberlist=view.findViewById(R.id.allmemberslist);


        mFilter.addTextChangedListener(filterTextWatcher);

        mD2= FirebaseDatabase.getInstance().getReference().child("activeclients").child("all");
        mD3= FirebaseDatabase.getInstance().getReference().child("allmembers");
        mD4=FirebaseDatabase.getInstance().getReference().child("memberratings");

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mMemberlist.setHasFixedSize(true);
        mMemberlist.setLayoutManager(layoutManager);


        mTab1.setCardBackgroundColor(getResources().getColor(R.color.colorGreen));

        mD3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("members",String.valueOf(dataSnapshot.getChildrenCount()));
                mClientbase.setText(String.valueOf(dataSnapshot.getChildrenCount())+" Clients");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mActivemembers.setText(String.valueOf(dataSnapshot.getChildrenCount())+" Clients");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTab2.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                mTab1.setCardBackgroundColor(getResources().getColor(R.color.colorGreen));
                mTab3.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                firebaseuserserach2();
                op1=1;
            }
        });

        mTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTab2.setCardBackgroundColor(getResources().getColor(R.color.colorGreen));
                mTab1.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                mTab3.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                firebaseuserserach3();
                op1=2;
            }
        });

        mTab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTab3.setCardBackgroundColor(getResources().getColor(R.color.colorGreen));
                mTab1.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                mTab2.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                firebaseuserserac2();
                op1=3;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("Sort")
                        .setItems(R.array.filters, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        order="name";
                                        break;
                                    case 1:
                                        order="idnum";
                                        break;
                                }
                            }
                        });
                AlertDialog alert121 = builder.create();
                alert121.show();
            }
        });



        return view;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS

            String uname=mFilter.getText().toString().trim();
            try {
                if (op1==1){
                firebaseuserserach(uname);
                }else if (op1==2){
                    firebaseuserserach4(uname);
                }else if (op1==3){
                    firebaseuserserac(uname);
                }
            }catch (Exception e){
                Log.e("exception","Error!");
            }

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void firebaseuserserach(String uname) {
        //super.onStart();
        Query firebaseSearchQuery = mD3.orderByChild("name").startAt(uname.toUpperCase()).endAt(uname.toUpperCase() + "\uf8ff");

        FirebaseRecyclerAdapter<member,membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member, membersViewHolder>(
                member.class,
                R.layout.memberslist2_row,
                membersViewHolder.class,
                firebaseSearchQuery
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder viewHolder, final member model, int position) {


                viewHolder.setMemberPhoto(getContext(),model.getImage());
                viewHolder.setMemberName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent payments = new Intent(getContext(),memberprofile.class);
                        payments.putExtra("id",model.getUid());
                        startActivity(payments);
                    }
                });


            }
        };
        mMemberlist.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseuserserach2() {
        //super.onStart();
        Query firebaseSearchQuery = mD3.orderByChild("name");

        FirebaseRecyclerAdapter<member,membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member, membersViewHolder>(
                member.class,
                R.layout.memberslist2_row,
                membersViewHolder.class,
                firebaseSearchQuery
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder viewHolder, final member model, int position) {


                viewHolder.setMemberPhoto(getContext(),model.getImage());
                viewHolder.setMemberName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent payments = new Intent(getContext(),memberprofile.class);
                        payments.putExtra("id",model.getUid());
                        startActivity(payments);
                    }
                });


            }
        };
        mMemberlist.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseuserserac(String uname) {
        //super.onStart();
        Query firebaseSearchQuery = mD4.orderByChild("rating");

        FirebaseRecyclerAdapter<memberperfomance,membersViewHolder3> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<memberperfomance, membersViewHolder3>(
                memberperfomance.class,
                R.layout.memberslistperfomance_row,
                membersViewHolder3.class,
                firebaseSearchQuery
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder3 viewHolder, final memberperfomance model, int position) {



                viewHolder.setMemberPhoto(getContext(),model.getImage());
                viewHolder.setMemberName(model.getName());
                viewHolder.setMemberRating(model.getRating());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent payments = new Intent(getContext(),memberprofile.class);
                        payments.putExtra("id",model.getUid());
                        startActivity(payments);
                    }
                });


            }
        };
        mMemberlist.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseuserserac2() {
        //super.onStart();
        Query firebaseSearchQuery =mD4.orderByChild("rating");

        FirebaseRecyclerAdapter<memberperfomance,membersViewHolder3> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<memberperfomance, membersViewHolder3>(
                memberperfomance.class,
                R.layout.memberslistperfomance_row,
                membersViewHolder3.class,
                firebaseSearchQuery
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder3 viewHolder, final memberperfomance model, int position) {



                viewHolder.setMemberPhoto(getContext(),model.getImage());
                viewHolder.setMemberName(model.getName());
                viewHolder.setMemberRating(model.getRating());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent payments = new Intent(getContext(),memberprofile.class);
                        payments.putExtra("id",model.getUid());
                        startActivity(payments);
                    }
                });


            }
        };
        mMemberlist.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseuserserach3() {
        //super.onStart();
        Query firebaseSearchQuery = mD2.orderByChild("name");

        FirebaseRecyclerAdapter<activeclients,membersViewHolder2> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<activeclients, membersViewHolder2>(
                activeclients.class,
                R.layout.memberslist2_row,
                membersViewHolder2.class,
                firebaseSearchQuery
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder2 viewHolder, final activeclients model, int position) {


                viewHolder.setMemberPhoto(getContext(),model.getImage());
                viewHolder.setMemberName(model.getName());
                viewHolder.setMemberloan(model.getLoanid());
                viewHolder.setMemberloandate(model.getLoanid());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent payments = new Intent(getContext(),memberprofile.class);
                        payments.putExtra("id",model.getUid());
                        startActivity(payments);
                    }
                });


            }
        };
        mMemberlist.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseuserserach4(String uname) {
        //super.onStart();
        Query firebaseSearchQuery = mD2.orderByChild("name").startAt(uname).endAt(uname + "\uf8ff");

        FirebaseRecyclerAdapter<activeclients,membersViewHolder2> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<activeclients, membersViewHolder2>(
                activeclients.class,
                R.layout.memberslist2_row,
                membersViewHolder2.class,
                firebaseSearchQuery
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder2 viewHolder, final activeclients model, int position) {


                viewHolder.setMemberPhoto(getContext(),model.getImage());
                viewHolder.setMemberName(model.getName());
                viewHolder.setMemberloan(model.getLoanid());
                viewHolder.setMemberloandate(model.getLoanid());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent payments = new Intent(getContext(),memberprofile.class);
                        payments.putExtra("id",model.getUid());
                        startActivity(payments);
                    }
                });


            }
        };
        mMemberlist.setAdapter(firebaseRecyclerAdapter);
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

    public static class membersViewHolder3 extends RecyclerView.ViewHolder{
        private DatabaseReference mDe;
        View mView;

        public membersViewHolder3(View itemView) {
            super(itemView);
            mDe=FirebaseDatabase.getInstance().getReference().child("allmembers");
            mView = itemView;
        }


        public void setMemberName(String name){
            TextView membername = (TextView)mView.findViewById(R.id.memname);
            membername.setText(name);
        }
        public void setMemberRating(long name){
            TextView membernames = (TextView)mView.findViewById(R.id.score);
            membernames.setText(String.valueOf(name));
        }
        public void setMemberPhoto(final Context ctx, final String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.memberpic);
            Picasso.with(ctx).load(image).into(memberphoto);
        }
    }

    public static class membersViewHolder2 extends RecyclerView.ViewHolder{
        private DatabaseReference mDe;
        View mView;


        public membersViewHolder2(View itemView) {
            super(itemView);
            mDe=FirebaseDatabase.getInstance().getReference().child("activeloans").child("all");

            mView = itemView;
        }


        public void setMemberName(String name){
            TextView membername = (TextView)mView.findViewById(R.id.memname);
            membername.setText(name);
        }
        public void setMemberloan(String name){
            mDe.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView membername = (TextView)mView.findViewById(R.id.loanamount);
                    membername.setText("Kshs. "+dataSnapshot.child("balance").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setMemberloandate(String name){
            mDe.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView membername = (TextView)mView.findViewById(R.id.loandate);
                    membername.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(dataSnapshot.child("datedisbursed").getValue().toString())));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            TextView membernames = (TextView)mView.findViewById(R.id.memstatus);
            membernames.setText("Active");
        }
        public void setMemberPhoto(Context ctx, String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.memberpic);
            Picasso.with(ctx).load(image).into(memberphoto);
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Clients");
    }
}
