package com.example.london.financeapp;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.Interval;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class login extends AppCompatActivity {
    private EditText mloginemail, mloginpass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,mData;
    private ProgressDialog mProgress;
    private Boolean op=true;
    private long downsize;
    private String refreshedToken;
    private StorageReference storageRef;
    private long enqueue;
    private DownloadManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mData= FirebaseDatabase.getInstance().getReference().child("Employees");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("UpdateManager");
        mloginemail = (EditText) findViewById(R.id.username);
        mloginpass = (EditText) findViewById(R.id.password);
        Button mloginbtn = (Button) findViewById(R.id.loginb);
        mProgress=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignin();
            }
        });
        storageRef= FirebaseStorage.getInstance().getReference();

    }

    private void startSignin() {
        final String snemail = mloginemail.getText().toString().trim();
        String snpass = mloginpass.getText().toString().trim();
        if (!TextUtils.isEmpty(snemail) && !TextUtils.isEmpty(snpass)) {
            mProgress.setTitle("Please wait");
            mProgress.setMessage("Signing In.. ");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            mAuth.signInWithEmailAndPassword(snemail,snpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mProgress.dismiss();
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                mData.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot1) {
                                        final String toks;
                                        if (dataSnapshot1.child("device_token").exists()){
                                            toks=dataSnapshot1.child("device_token").getValue().toString();
                                        }else {
                                            toks="new";
                                        }
                                        if (!dataSnapshot.child("Updatedusers").child(toks).exists()){
                                            final AlertDialog.Builder builders = new AlertDialog.Builder(login.this);
                                            builders.setTitle("Update Available")
                                                    .setCancelable(true)
                                                    .setMessage("New Update Available, "+String.valueOf(downsize)+" Mbs.")
                                                    .setPositiveButton("Download now", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            mProgress.setTitle("Please wait");
                                                            mProgress.setMessage("Downloading Update.. ");
                                                            mProgress.setIcon(R.drawable.ic_file_download_black_24dp);
                                                            mProgress.setCanceledOnTouchOutside(true);
                                                            mProgress.show();

                                                            StorageReference islandRef = storageRef.child("apks/app-release.apk");
                                                            final File localFile;
                                                            String folde = Environment.getExternalStorageDirectory()+"/Jitihada";
                                                            File folder=new File(folde,"/apk/");
                                                            localFile = new File(folder, "Jitihada.apk");

                                                            if (!folder.exists()) {
                                                                if (folder.mkdirs()) {
                                                                    try {
                                                                        boolean sucess = localFile.createNewFile();
                                                                        if (sucess){
                                                                            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                                                    // Local temp file has been created
                                                                                    mProgress.dismiss();
                                                                                    Log.e("download","success");
                                                                                    mDatabase.child("Updatedusers").addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            if (!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())){
                                                                                                DatabaseReference reference=mDatabase.child("Updatedusers").child(toks);
                                                                                                reference.setValue(mAuth.getCurrentUser().getUid());
                                                                                                //userTypelogin(refreshedToken);

                                                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                                                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/SmartServe/apk/" + "SmartServe.apk")), "application/vnd.android.package-archive");
                                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                                startActivity(intent);
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception exception) {
                                                                                    // Handle any errors
                                                                                    mProgress.dismiss();
                                                                                    Log.e("download","fail");
                                                                                    userTypelogin2();
                                                                                }
                                                                            });
                                                                        }
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), folder + " can't be created.", Toast.LENGTH_SHORT).show();

                                                                }
                                                            } else {

                                                                Toast.makeText(getApplicationContext(), folder+ " already exits.", Toast.LENGTH_SHORT).show();

                                                                try {
                                                                    boolean sucess  = localFile.createNewFile();

                                                                    if (sucess){
                                                                        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                                                // Local temp file has been created
                                                                                mProgress.dismiss();
                                                                                Log.e("download","success");
                                                                                mDatabase.child("Updatedusers").addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        if (!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())){
                                                                                            DatabaseReference reference=mDatabase.child("Updatedusers").child(toks);
                                                                                            reference.setValue(mAuth.getCurrentUser().getUid());
                                                                                            //userTypelogin2();

                                                                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                                            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/SmartServe/apk/" + "SmartServe.apk")), "application/vnd.android.package-archive");
                                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                            startActivity(intent);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                    }
                                                                                });
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception exception) {
                                                                                // Handle any errors
                                                                                mProgress.dismiss();
                                                                                Log.e("download","fail");
                                                                                userTypelogin2();
                                                                            }
                                                                        });
                                                                    }
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                        }
                                                    })
                                                    .setNegativeButton("Download Later", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            userTypelogin2();
                                                        }
                                                    });

                                            AlertDialog alert121 = builders.create();
                                            alert121.show();
                                        }else {
                                            userTypelogin2();
                                        }
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

                    }
                    else{
                        Toast.makeText(login.this,"Login Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Toast.makeText(login.this, "Enter All Details", Toast.LENGTH_LONG).show();
        }
    }

    private void userTypelogin2() {
        final String user_id=mAuth.getCurrentUser().getUid();
        mProgress.setTitle("Please wait");
        mProgress.setMessage("Signing in: "+mAuth.getCurrentUser().getEmail());
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        Log.e("Empp",user_id);
        @SuppressLint("HardwareIds") final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mData.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switch (dataSnapshot.child("position").getValue().toString()) {
                    case "Admin":
                        mData.child(user_id).child("device_token").setValue(refreshedToken);
                        Intent doctorview = new Intent(login.this, adminhome.class);
                        mProgress.dismiss();
                        Toast.makeText(login.this, "Login Successful", Toast.LENGTH_LONG).show();
                        startActivity(doctorview);
                        finish();

                        break;
                    case "user": {
                        Intent addpatients = new Intent(login.this, coordinator.class);
                        mData.child(user_id).child("device_token").setValue(android_id);
                        startActivity(addpatients);
                        mProgress.dismiss();
                        Toast.makeText(login.this, "Login Successful", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    }
                    case "co": {
                        Intent addpatients = new Intent(login.this, creditofficerhome.class);
                        mData.child(user_id).child("device_token").setValue(android_id);
                        startActivity(addpatients);
                        mProgress.dismiss();
                        Toast.makeText(login.this, "Login Successful", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
