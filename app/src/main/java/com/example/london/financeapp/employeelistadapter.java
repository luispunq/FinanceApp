package com.example.london.financeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;

public class employeelistadapter extends FirebaseRecyclerAdapter<employee,employeesholder> {

    private Context context;
    private DatabaseReference mD;
    private String user;
    private FirebaseAuth mAuth;
    private double val1=0;


    public employeelistadapter(Class<employee> modelClass, int modelLayout, Class<employeesholder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        mD= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
    }

    @Override
    protected void populateViewHolder(employeesholder viewHolder, final employee model, int position) {
        final String key=getRef(position).getKey();


        
        viewHolder.empname.setText(model.getName());
        Glide.with(context).load(model.getImage()).into(viewHolder.empimage);
        viewHolder.empstatus.setText(model.getStatus());

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent moresavings= new Intent(context,employeebasic.class);
                moresavings.putExtra("id",key);
                context.startActivity(moresavings);

            }
        });

    }
}
