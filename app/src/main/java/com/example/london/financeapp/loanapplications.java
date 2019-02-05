package com.example.london.financeapp;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class loanapplications extends Fragment {
    private RecyclerView masterloanslist;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManager;
    private loanappsadapter adapter;
    private View view;
    private CardView c1,c2;
    private FloatingActionButton fab;
    private String filter="accepted";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_loanapplications, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        masterloanslist=view.findViewById(R.id.loansapplicants);
        masterloanslist.setHasFixedSize(true);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("loanrequests");
        adapter=new loanappsadapter(loanapps.class, R.layout.masterloanapp_row, loanappsholder.class, mDatabase, getContext(),filter);
        masterloanslist.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        fab=view.findViewById(R.id.floatingActionButton2);
        masterloanslist.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("Sort")
                        .setItems(R.array.filters2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        filter="accepted";
                                        adapter=new loanappsadapter(loanapps.class, R.layout.masterloanapp_row, loanappsholder.class, mDatabase, getContext(),filter);
                                        masterloanslist.setAdapter(adapter);
                                        break;
                                    case 1:
                                        filter="declined";
                                        adapter=new loanappsadapter(loanapps.class, R.layout.masterloanapp_row, loanappsholder.class, mDatabase, getContext(),filter);
                                        masterloanslist.setAdapter(adapter);
                                        break;
                                    case 2:
                                        filter="all";
                                        adapter=new loanappsadapter(loanapps.class, R.layout.masterloanapp_row, loanappsholder.class, mDatabase, getContext(),filter);
                                        masterloanslist.setAdapter(adapter);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Loan Requests");
    }

}
