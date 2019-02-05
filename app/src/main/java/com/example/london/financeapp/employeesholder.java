package com.example.london.financeapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class employeesholder extends RecyclerView.ViewHolder {
    private static final String TAG = employeesholder.class.getSimpleName();
    public CardView layout;
    public TextView empname;
    public ImageView empimage;
    public TextView empstatus;
    View mView;

    public employeesholder(View itemView) {
        super(itemView);
        mView=itemView;

        empname = (TextView)mView.findViewById(R.id.masempname);
        empimage = (ImageView) mView.findViewById(R.id.masempimage);
        empstatus=(TextView) mView.findViewById(R.id.textView12);
    }
}
