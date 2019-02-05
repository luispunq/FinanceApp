package com.example.london.financeapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class coloanapplications extends Fragment {
    private RecyclerView masterloanslist;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManager;
    private loanappsadapter3 adapter;
    private View view;
    private CardView c1,c2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_coloanapplications, container, false);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        masterloanslist=view.findViewById(R.id.loansapplicants);
        masterloanslist.setHasFixedSize(true);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("loanrequests");
        adapter=new loanappsadapter3(loanapps.class, R.layout.masterloanapp_row, loanappsholder.class, mDatabase, getContext());
        masterloanslist.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        masterloanslist.setAdapter(adapter);

        return view;
    }

}
