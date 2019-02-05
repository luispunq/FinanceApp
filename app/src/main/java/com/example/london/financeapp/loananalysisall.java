package com.example.london.financeapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.LruCache;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class loananalysisall extends AppCompatActivity {
    private TextView mTotloans,mTotInterest,mTotLoanCash,mTotBalance;
    private double dTotLoans=0,dTotInterest=0,dLoanCash=0,dBal=0;
    private RecyclerView mTrans;
    private DatabaseReference mDatabase,mD,mD2,mD4;
    private String st2=null,st4=null,st3=null,st1=null;
    private String mdate,mtype,year,month;
    private long start,end;
    private FloatingActionButton fab,fabe;
    private Bitmap b1,b2;
    private ByteArrayOutputStream bytes,bytesw;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loananalysis);
        mTotloans = findViewById(R.id.totloans);
        mTotInterest=findViewById(R.id.totloancash);
        mTotLoanCash=findViewById(R.id.totintrest);
        mTotBalance=findViewById(R.id.totloanbalance);
        fab=findViewById(R.id.floatingActionButton3);

        mTrans = findViewById(R.id.transactionlist);
        mTrans.setHasFixedSize(true);
        mTrans.setLayoutManager(new LinearLayoutManager(this));


        mTrans.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        screen(mTrans);
                    }
                });

                mTrans.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mD4= FirebaseDatabase.getInstance().getReference().child("activeloans").child("all");

        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dBal = 0;
                dLoanCash = 0;
                dTotLoans = 0;
                dTotInterest = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String loantot = snapshot.child("amount").getValue().toString();
                    String loantotcash = snapshot.child("amountcash").getValue().toString();
                    String loantotint = snapshot.child("interest").getValue().toString();
                    String loantotbal = snapshot.child("balance").getValue().toString();

                    double val1 = Double.parseDouble(loantot);
                    double val2 = Double.parseDouble(loantotcash);
                    double val3 = Double.parseDouble(loantotint);
                    double val4 = Double.parseDouble(loantotbal);


                    dTotLoans = dTotLoans + val1;
                    dLoanCash = dLoanCash + val2;
                    dTotInterest = dTotInterest + val3;
                    dBal = dBal + val4;

                    st1 = String.valueOf(dTotLoans);
                    st2 = String.valueOf(dLoanCash);
                    st3 = String.valueOf(dTotInterest);
                    st4 = String.valueOf(dBal);

                    mTotloans.setText("Kshs. " + st1);
                    mTotLoanCash.setText("Kshs. " + st2);
                    mTotInterest.setText("Kshs. " + st3);
                    mTotBalance.setText("Kshs. " + st4);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<activeloans, transViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activeloans, transViewHolder>(

                activeloans.class,
                R.layout.coloanstrans_row,
                transViewHolder.class,
                mD4
        ) {
            @Override
            protected void populateViewHolder(final transViewHolder viewHolder, final activeloans model, int position) {

                final String rk = getRef(position).getKey();

                viewHolder.setName(model.getMembername());
                viewHolder.setTotalRepaid(model.getAmount());
                viewHolder.setLSF(model.getDatedisbursed());
                viewHolder.setAdvance(model.getInterest());
                viewHolder.setLoanInst(model.getAmountcash());
                viewHolder.setPaymode(model.getLocation());
                viewHolder.setInsu1(model.getContact());
                viewHolder.setLoangivn(model.getOfficername());
                viewHolder.setAdvcgvn(model.getBalance());

                viewHolder.setLoancf(model.getMemberid());


                viewHolder.link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next =new Intent(loananalysisall.this,memberprofile.class);
                        next.putExtra("id",model.getMemberid());
                        startActivity(next);
                    }
                });


            }
        };

        mTrans.setAdapter(firebaseRecyclerAdapter);
    }

    public static class transViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;
        private TextView link;


        public transViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            link=mView.findViewById(R.id.grdlink);
            layout = (RelativeLayout) itemView.findViewById(R.id.transaccard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setName(String name){
            TextView mGroupname = mView.findViewById(R.id.gridmembername);
            mGroupname.setText(name);
        }
        public void setTotalRepaid(String totalrepaid){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdtotalloan);
            mGroupname.setText("Kshs. "+totalrepaid);
        }
        public void setLSF(long lsf){
            //long lssf=Long.parseLong(lsf);
            TextView mGroupname = (TextView)mView.findViewById(R.id.grddateissued);
            mGroupname.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(lsf));
        }
        public void setLoanInst(String loaninst){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdintrest);
            mGroupname.setText("Kshs. "+loaninst);
        }
        public void setAdvance(String advance){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdloancash);
            mGroupname.setText("Kshs. "+advance);
        }
        public void setPaymode(String paymode){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdloanlocation);
            mGroupname.setText(paymode);
        }
        public void setInsu1(String insu1){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdcontact);
            mGroupname.setText(insu1);
        }
        public void setLoangivn(final String loangiv){
            TextView mGroupame = mView.findViewById(R.id.grdofficer);
            mGroupame.setText(loangiv);
        }
        public void setAdvcgvn(final String advcgvn){
            TextView mGrouname = (TextView) mView.findViewById(R.id.grdbalance);
            mGrouname.setText("Kshs. "+advcgvn);
        }
        public void setLoancf(final String loancf){
            TextView mGroupame = mView.findViewById(R.id.grdlink);
            mGroupame.setText("Payment History");
        }
    }

    private void screen(RecyclerView view) {

        View u = findViewById(R.id.tebo);
        View w = findViewById(R.id.tebototals);


        b1 = getBitmapFromView(u,u.getHeight(),u.getWidth());
        b2 = getBitmapFromView(w,w.getHeight(),w.getWidth());


        RelativeLayout z = (RelativeLayout) findViewById(R.id.top);
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();

        RecyclerView.Adapter adapter = view.getAdapter();

        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = getBitmapFromView(holder.itemView,holder.itemView.getHeight(),totalWidth);
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
//                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
                height += holder.itemView.getMeasuredHeight();
            }
            Bitmap bigBitmap = Bitmap.createBitmap(z.getMeasuredWidth(), height+(height/2), Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            bigCanvas.drawBitmap(b1,0f,0f,paint);
            iHeight=iHeight+b1.getHeight();

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

            bigCanvas.drawBitmap(b2,0f,iHeight,paint);

            bytes = new ByteArrayOutputStream();
            bigBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStorageDirectory()+"/Jitihada";
                File f = new File(folder,"/LoanAnalysis/");
                File fil = new File(f,new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())+".jpg");
                Log.d("STATE", Environment.getExternalStorageState());
                Log.d("PATH", f.getAbsolutePath());
                if (!f.exists()) {
                    if (f.mkdirs()) {
                        Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                        try {
                            boolean sucess = fil.createNewFile();
                            if (sucess){
                                FileOutputStream fo = new FileOutputStream(fil);
                                fo.write(bytes.toByteArray());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                    try {
                        boolean sucess  = fil.createNewFile();

                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                Log.e("file", folder);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            String folder = Environment.getExternalStorageDirectory()+"/Jitihada";
            File f = new File(folder,"/LoanAnalysis/");
            File fil = new File(f,new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())+".jpg");
            Log.d("STATE", Environment.getExternalStorageState());
            Log.d("PATH", f.getAbsolutePath());
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                try {
                    boolean sucess  = fil.createNewFile();

                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        fo.write(bytes.toByteArray());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            Log.e("file", folder);
        }

    }

    private Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
}
