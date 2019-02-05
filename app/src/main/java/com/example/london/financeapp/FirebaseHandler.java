package com.example.london.financeapp;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHandler extends Application {
    private ProgressDialog mProgress;
    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mProgress=new ProgressDialog(getApplicationContext());
        mProgress.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        final Handler hand=new Handler();
        hand.post(new Runnable() {
            @Override
            public void run() {
                mProgress.setTitle("Loading..");
                mProgress.setMessage("Please Wait Establishing Connection..");
                mProgress.setCanceledOnTouchOutside(false);
                //mProgress.show();


                DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);

                        if (connected) {
                            System.out.println("connected");
                            FirebaseDatabase.getInstance().goOnline();
                            //mProgress.dismiss();
                        } else {
                            System.out.println("not connected");
                            FirebaseDatabase.getInstance().goOnline();
                            //mProgress.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Listener was cancelled");
                        mProgress.dismiss();
                    }
                });
            }
        });

        ctx = this;
        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }


        class MyApplication extends MultiDexApplication {
            @Override
            protected void attachBaseContext(Context context) {
                super.attachBaseContext(context);
                MultiDex.install(this);
            }
        }

        MyApplication application=new MyApplication();
        application.attachBaseContext(getApplicationContext());
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


}

