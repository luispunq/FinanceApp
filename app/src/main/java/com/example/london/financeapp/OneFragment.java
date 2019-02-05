package com.example.london.financeapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class OneFragment extends Fragment {
    private TextView mMembername,mID,mPhone,mAddress,mResidence,mDob,mEmail,
    mMarital,mEducation;
    private ImageView mUserimage;
    private DatabaseReference mD1,mD2,mD3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_one, container, false);

        memberprofile activity = (memberprofile) getActivity();
        String uid = activity.getMyData();

        mMembername=view.findViewById(R.id.memname);
        mID=view.findViewById(R.id.accnum);
        mPhone=view.findViewById(R.id.memcontact);
        mAddress=view.findViewById(R.id.memaddress);
        mResidence=view.findViewById(R.id.memresidence);
        mDob=view.findViewById(R.id.dob);
        mMarital=view.findViewById(R.id.maritalsts);
        mEducation=view.findViewById(R.id.education);
        mEmail=view.findViewById(R.id.mememail);
        mUserimage=view.findViewById(R.id.mempic);



        mD1= FirebaseDatabase.getInstance().getReference().child("members").child(uid);

        mD1.child("personaldetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMembername.setText(dataSnapshot.child("name").getValue().toString());
                mID.setText(dataSnapshot.child("idnum").getValue().toString());
                mPhone.setText(dataSnapshot.child("phone").getValue().toString());
                mAddress.setText(dataSnapshot.child("address").getValue().toString());
                mResidence.setText(dataSnapshot.child("residence").getValue().toString());
                mEmail.setText(dataSnapshot.child("email").getValue().toString());

                mMarital.setText(dataSnapshot.child("maritalstatus").getValue().toString());
                mDob.setText(dataSnapshot.child("dob").getValue().toString());
                mEducation.setText(dataSnapshot.child("education").getValue().toString());
                Picasso.with(getContext()).load(dataSnapshot.child("image").getValue().toString()).into(mUserimage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

}
