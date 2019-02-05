package com.example.london.financeapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

public class loanappsadapter extends FirebaseRecyclerAdapter<loanapps,loanappsholder> {

    private String mFilter;
    private Context context;


    loanappsadapter(Class<loanapps> modelClass, int modelLayout, Class<loanappsholder> viewHolderClass, DatabaseReference ref, Context context,String filter) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        this.mFilter=filter;
    }

    @Override
    protected void populateViewHolder(loanappsholder viewHolder, loanapps model, int position) {
        final String key=getRef(position).getKey();
        if (mFilter.equals("accepted")){
        if (model.getStatus().equals("cleared-accepted")){
            viewHolder.setPhoto(context,model.getImage());
            viewHolder.setapplierName(model.getMembername());
            viewHolder.setapplydate(model.getRequestdate());
            viewHolder.setapplyamount(model.getAmountrequested());

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userwindow = new Intent(context,loanprocessing.class);
                    userwindow.putExtra("id",key);
                    context.startActivity(userwindow);
                }
            });
        }else{
            viewHolder.Layout_hide();
        }
        }else if (mFilter.equals("declined")){
            if (model.getStatus().equals("cleared-declined")){
                viewHolder.setPhoto(context,model.getImage());
                viewHolder.setapplierName(model.getMembername());
                viewHolder.setapplydate(model.getRequestdate());
                viewHolder.setapplyamount(model.getAmountrequested());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent userwindow = new Intent(context,loanprocessing.class);
                        userwindow.putExtra("id",key);
                        context.startActivity(userwindow);
                    }
                });
            }else{
                viewHolder.Layout_hide();
            }
        }else {
            viewHolder.setPhoto(context,model.getImage());
            viewHolder.setapplierName(model.getMembername());
            viewHolder.setapplydate(model.getRequestdate());
            viewHolder.setapplyamount(model.getAmountrequested());

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userwindow = new Intent(context,loanprocessing.class);
                    userwindow.putExtra("id",key);
                    context.startActivity(userwindow);
                }
            });
        }
    }
}
