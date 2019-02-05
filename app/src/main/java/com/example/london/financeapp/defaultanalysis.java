package com.example.london.financeapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class defaultanalysis extends AppCompatActivity {
    private RecyclerView mloanhist;
    private DatabaseReference mDatabase,mD,mD2,mD3,mD4,mD5;
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
        setContentView(R.layout.activity_defaultanalysis);
        Bundle extras = getIntent().getExtras();
        mtype = extras.getString("type");
        mdate = extras.getString("date");
        year = extras.getString("year");
        month = extras.getString("month");
        start =Long.parseLong( extras.getString("start"));
        end = Long.parseLong(extras.getString("end"));
        Log.e("start-end",start+"  "+end);

        mloanhist=findViewById(R.id.memberstat_list);
        mloanhist.setHasFixedSize(true);
        mloanhist.setLayoutManager(new LinearLayoutManager(this));
        fab=findViewById(R.id.floatingActionButton3);

        mD4= FirebaseDatabase.getInstance().getReference().child("activedefaults").child("all");
        mD5= FirebaseDatabase.getInstance().getReference().child("activedefaults").child("daily");
        mD3= FirebaseDatabase.getInstance().getReference().child("activedefaults").child(year).child(month);

        mloanhist.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        screen(mloanhist);
                    }
                });

                mloanhist.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        switch (mtype) {
            case "daily": {


                FirebaseRecyclerAdapter<activedefaults, memberstatViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activedefaults, memberstatViewHolder>(

                        activedefaults.class,
                        R.layout.defaults_row,
                        memberstatViewHolder.class,
                        mD5.child(mdate)
                ) {
                    @Override
                    protected void populateViewHolder(final memberstatViewHolder viewHolder, final activedefaults model, int position) {

                        viewHolder.setName(model.getMembername());
                        viewHolder.setLoan(model.getLoan());
                        viewHolder.setLastpaid(model.getLastinst());
                        viewHolder.setDays(model.getDays());
                        viewHolder.setFees(model.getFee());
                        viewHolder.setOfficer(model.getMembername());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent next =new Intent(defaultanalysis.this,memberprofile.class);
                                next.putExtra("id",model.getMemberid());
                                startActivity(next);
                            }
                        });
                    }
                };

                mloanhist.setAdapter(firebaseRecyclerAdapter);

                break;
            }
            case "monthly": {

                
                FirebaseRecyclerAdapter<activedefaults, memberstatViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activedefaults, memberstatViewHolder>(

                        activedefaults.class,
                        R.layout.defaults_row,
                        memberstatViewHolder.class,
                        mD3
                ) {
                    @Override
                    protected void populateViewHolder(final memberstatViewHolder viewHolder, final activedefaults model, int position) {


                        viewHolder.setName(model.getMembername());
                        viewHolder.setLoan(model.getLoan());
                        viewHolder.setLastpaid(model.getLastinst());
                        viewHolder.setDays(model.getDays());
                        viewHolder.setFees(model.getFee());
                        viewHolder.setOfficer(model.getMembername());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent next =new Intent(defaultanalysis.this,memberprofile.class);
                                next.putExtra("id",model.getMemberid());
                                startActivity(next);
                            }
                        });
                    }
                };

                mloanhist.setAdapter(firebaseRecyclerAdapter);

                break;
            }
            default: {
                

                FirebaseRecyclerAdapter<activedefaults, memberstatViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<activedefaults, memberstatViewHolder>(

                        activedefaults.class,
                        R.layout.defaults_row,
                        memberstatViewHolder.class,
                        mD4.orderByChild("timestamp").startAt(start)
                ) {
                    @Override
                    protected void populateViewHolder(final memberstatViewHolder viewHolder, final activedefaults model, int position) {

                        viewHolder.setName(model.getMembername());
                        viewHolder.setLoan(model.getLoan());
                        viewHolder.setLastpaid(model.getLastinst());
                        viewHolder.setDays(model.getDays());
                        viewHolder.setFees(model.getFee());
                        viewHolder.setOfficer(model.getMembername());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent next =new Intent(defaultanalysis.this,memberprofile.class);
                                next.putExtra("id",model.getMemberid());
                                startActivity(next);
                            }
                        });
                    }
                };

                mloanhist.setAdapter(firebaseRecyclerAdapter);
                break;
            }
        }



    }

    public static class memberstatViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private DatabaseReference check,mD;
        private String iid;



        public memberstatViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }


        public void setName(String date){
            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatname);
            amountgiven.setText(date);
        }
        public void setLoan(String detail){
            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatsav);
            amountgiven.setText("Kshs. "+detail);
        }
        public void setLastpaid(String amount){

            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatadv);
            amountgiven.setText(amount);

        }
        public void setDays(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatloan);
            amountgiven.setText(amount+" Days");
        }
        public void setFees(final String amount){

            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk);
            amountgiven.setText("Kshs. "+amount);

        }
        public void setOfficer(final String amount){

            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk2);
            amountgiven.setText(amount);

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
