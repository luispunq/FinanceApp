package com.example.london.financeapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

public class loanappsadapter2 extends FirebaseRecyclerAdapter<loanapps,loanappsholder> {

    private Context context;


    loanappsadapter2(Class<loanapps> modelClass, int modelLayout, Class<loanappsholder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(loanappsholder viewHolder, loanapps model, int position) {
        final String key=getRef(position).getKey();
        if (model.getStatus().equals("pending")){
            viewHolder.setPhoto(context,model.getImage());
            viewHolder.setapplierName(model.getMembername());
            viewHolder.setapplydate(model.getRequestdate());
            viewHolder.setapplyamount(model.getAmountrequested());

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userwindow = new Intent(context,loandetailspop.class);
                    userwindow.putExtra("id",key);
                    context.startActivity(userwindow);
                }
            });
        }else{
            viewHolder.Layout_hide();
        }
    }
}
