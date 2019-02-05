package com.example.london.financeapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.LruCache;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class loandetails extends AppCompatActivity {
    private TextView mUsername,mIDnum,mLoanbal,mInstallmentamt,mInstallments,mLoangivn,
            mOfficer,mTab1lbl,mTab2lbl,mTab3lbl,mDategiven;
    private ImageView mUserimage;
    private FirebaseAuth mAuth;
    private RecyclerView mPaymenthist;
    private CardView mYear,mTab1,mTab2,mTab3;
    private DatabaseReference mDatabase,mD,mD1,mD2,offline,mD5;
    private Bitmap b1,b2;
    private ByteArrayOutputStream bytes,bytesw;
    private String user=null,op=null,fieldid,loanid,yearx="'19",monthx=new SimpleDateFormat("MMM").format(new Date());
    private String loan,loaninstallment,dategive,totloan,nextpaymen,duratio,officerid,loanidd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loandetails);
        user= getIntent().getExtras().getString("id");
        mAuth=FirebaseAuth.getInstance();
        op=mAuth.getCurrentUser().getUid();
        mUsername=findViewById(R.id.memname);
        mUserimage=findViewById(R.id.mempic);
        mDategiven=findViewById(R.id.dategiven);
        mLoangivn=findViewById(R.id.loangiven);
        mIDnum=findViewById(R.id.accnum);
        mOfficer=findViewById(R.id.loanofficer);
        mLoanbal=findViewById(R.id.loanbalance);
        mInstallmentamt=findViewById(R.id.loaninstallment);
        mInstallments=findViewById(R.id.installments);
        mPaymenthist=findViewById(R.id.paymenthist);

        LinearLayoutManager layoutManager=new LinearLayoutManager(loandetails.this);
        mPaymenthist.setHasFixedSize(true);
        mPaymenthist.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        mD2= FirebaseDatabase.getInstance().getReference().child("loanrepayment").child(user);
        mD= FirebaseDatabase.getInstance().getReference().child("activeloans");
        mD1= FirebaseDatabase.getInstance().getReference().child("members").child(user);
        mD5= FirebaseDatabase.getInstance().getReference().child("Employees");

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsername.setText(dataSnapshot.child("personaldetails").child("name").getValue().toString());
                mIDnum.setText(dataSnapshot.child("personaldetails").child("idnum").getValue().toString());
                Picasso.with(loandetails.this).load(dataSnapshot.child("personaldetails").child("image").getValue().toString()).into(mUserimage);
                loanid=dataSnapshot.child("loans").child("info").getValue().toString();

                mD.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long count=dataSnapshot.getChildrenCount();
                        mInstallments.setText(String.valueOf(count)+" Installments");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mD.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mLoanbal.setText(dataSnapshot.child("balance").getValue().toString());
                    mLoangivn.setText(dataSnapshot.child("amount").getValue().toString());
                    mDategiven.setText(dataSnapshot.child("datedisbursed").getValue().toString());
                    mInstallmentamt.setText(dataSnapshot.child("installment").getValue().toString());

                    mD5.child(dataSnapshot.child("officerid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mOfficer.setText(dataSnapshot.child("name").getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<transaction,installmentsHolder> firebaseRecyclerAdapters = new FirebaseRecyclerAdapter<transaction,installmentsHolder>(
                transaction.class,
                R.layout.transactionloans_row,
                installmentsHolder.class,
                mD1.child("transactions")
        )
        {
            @Override
            protected void populateViewHolder(final installmentsHolder viewHolder, final transaction model, int position) {
                if (model.getName().equals("Installment")) {
                    viewHolder.setDate(model.getDate());
                    viewHolder.setGroupAmount(model.getAmount());
                    viewHolder.setTraname(model.getName());
                    viewHolder.setTrastaus(model.getStatus());
                }else {
                    viewHolder.Layout_hide();
                }
            }
        };

        mPaymenthist.setAdapter(firebaseRecyclerAdapters);

    }

    public class installmentsHolder extends RecyclerView.ViewHolder{
        private final CardView layout;
        final CardView.LayoutParams layoutParams;
        View mView;


        public installmentsHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (CardView) itemView.findViewById(R.id.transactioncard);
            layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.trsdate);
            mGroupname.setText(groupname);
        }
        public void setGroupAmount(String groupname){
            TextView mGroupname = (TextView) mView.findViewById(R.id.trsamount);
            mGroupname.setText(groupname);
        }
        public void setTraname(String groupname){
            TextView mGroupname = (TextView) mView.findViewById(R.id.trsamount);
            mGroupname.setText(groupname);
        }

        public void setTrastaus(String groupname){
            TextView mGroupname = (TextView) mView.findViewById(R.id.trsamount);
            mGroupname.setText(groupname);
        }
    }

    private void screen(RecyclerView view) {
        // get view group using reference
        CardView rlyy=findViewById(R.id.memstat);
        TextView title1=findViewById(R.id.title);
        LinearLayout ll1=findViewById(R.id.l1);
        CardView memsstat=findViewById(R.id.memstat21);
        TextView title2=findViewById(R.id.title2);

        Bitmap bitt=getBitmapFromView(rlyy,rlyy.getHeight(),rlyy.getWidth());
        Bitmap bitmap = getBitmapFromView(title1,title1.getHeight(),title1.getWidth());
        Bitmap bitmaps = getBitmapFromView(ll1,ll1.getHeight(),ll1.getWidth());
        Bitmap bitmapss = getBitmapFromView(memsstat,memsstat.getHeight(),memsstat.getWidth());
        Bitmap bitmapsss = getBitmapFromView(title2,title2.getHeight(),title2.getWidth());

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

            bigCanvas.drawBitmap(bitt,0f,0f,paint);
            bigCanvas.drawBitmap(bitmap,0f,bitt.getHeight(),paint);
            bigCanvas.drawBitmap(bitmaps,0f,bitt.getHeight()+bitmap.getHeight(),paint);
            bigCanvas.drawBitmap(bitmapss,0f,bitt.getHeight()+bitmap.getHeight()+bitmaps.getHeight(),paint);
            bigCanvas.drawBitmap(bitmapsss,0f,bitt.getHeight()+bitmap.getHeight()+bitmaps.getHeight()+bitmapsss.getHeight(),paint);
            iHeight=iHeight+bitt.getHeight()+bitmap.getHeight()+bitmaps.getHeight()+bitmapsss.getHeight();

            for (int i = 0; i < size; i++) {
                Bitmap bitmapz = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmapz, 0f, iHeight, paint);
                iHeight += bitmapz.getHeight();
                bitmapz.recycle();
            }


            bytes = new ByteArrayOutputStream();
            bigBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }


        try {
            String folder = Environment.getExternalStorageDirectory()+"/LoanDetails";
            File f = new File(folder,"/Statement/");
            File fil = new File(f, mUsername.getText().toString()+".jpg");
            if (!f.exists()){
                if (f.mkdirs()) {
                    Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                            //printBill();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(this, f+ " already exits.", Toast.LENGTH_SHORT).show();

                try {
                    boolean sucess  = fil.createNewFile();

                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        fo.write(bytes.toByteArray());
                        //printBill();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(loandetails.this, "Not Saved", Toast.LENGTH_SHORT).show();
        }
        //imageToPDF();

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
