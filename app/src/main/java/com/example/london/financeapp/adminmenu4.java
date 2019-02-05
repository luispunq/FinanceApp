package com.example.london.financeapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class adminmenu4 extends Fragment {
    private RecyclerView masteremplist;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManager;
    private employeelistadapter adapter;
    private View view;
    private FloatingActionButton fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_adminmenu4, container, false);

        masteremplist=view.findViewById(R.id.masemplist);
        fab=view.findViewById(R.id.floatingActionButton);


        linearLayoutManager = new LinearLayoutManager(getActivity());
        masteremplist.setHasFixedSize(true);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Employees");
        adapter=new employeelistadapter(employee.class, R.layout.masteremployees_row, employeesholder.class, mDatabase, getContext());
        masteremplist.setLayoutManager(linearLayoutManager);
        masteremplist.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doctorview= new Intent(getContext(),newemployeeemail.class);

                startActivity(doctorview);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Employees");
    }

}
