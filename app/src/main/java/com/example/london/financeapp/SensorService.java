package com.example.london.financeapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.Interval;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SensorService extends Service{
    //private DatabaseReference mD1,mD2;


    public int counter=0;
    public SensorService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public SensorService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Toast.makeText(getApplicationContext(), "App Stopped!", Toast.LENGTH_LONG).show();
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();


        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }



    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //Log.i("in timer", "in timer ++++  "+ (counter++));
                ShowToastInIntentService(String.valueOf(counter));
            }
        };

        Toast.makeText(getApplicationContext(),String.valueOf(counter),Toast.LENGTH_SHORT).show();
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void ShowToastInIntentService(final String sText) {
        final Context MyContext = this;
        final DatabaseReference mD1=FirebaseDatabase.getInstance().getReference().child("activedefaults");
        final DatabaseReference mD2=FirebaseDatabase.getInstance().getReference().child("activeloans");
        final DatabaseReference mDe=FirebaseDatabase.getInstance().getReference().child("oncedefaults");
        final DatabaseReference mDc=FirebaseDatabase.getInstance().getReference().child("members");
        final DatabaseReference mDa=FirebaseDatabase.getInstance().getReference().child("allmembers");
        final DatabaseReference mDofficer=FirebaseDatabase.getInstance().getReference().child("Employees");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Date dNow = new Date( );
                //String ttt=String.valueOf(dNow);

                SimpleDateFormat ft =
                        new SimpleDateFormat ("h:mm a");
                //ft.setTimeZone(TimeZone.getTimeZone("GMT"));

                String testtime=ft.format(dNow);

                if (testtime.equals("11:59 PM")){

                    mD2.child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final SimpleDateFormat ft =
                                    new SimpleDateFormat ("EEE, MMM d, ''yy");
                            final String ackey;
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                final String lastinst=snapshot.child("lastinstalmentdate").getValue().toString();
                                final String chkdate=snapshot.child("checkdate").getValue().toString();
                                final String memid=snapshot.child("memberid").getValue().toString();
                                final String nextpaydate=snapshot.child("nextinstalmentdate").getValue().toString();
                                final String memname=snapshot.child("membername").getValue().toString();
                                final String loanamount=snapshot.child("balance").getValue().toString();
                                final String officerid=snapshot.child("officerid").getValue().toString();
                                final String officername=snapshot.child("officername").getValue().toString();
                                final String loaniid=snapshot.getKey();

                                long lstdatedat=Long.parseLong(chkdate);
                                Date nextinstdat=new Date(lstdatedat);

                                DateTime dateTime = new DateTime(new Date());
                                DateTime dateTimez = new DateTime(nextinstdat);

                                if ((new Date().after(nextinstdat))){
                                    final int dday= Days.daysBetween(dateTimez.toLocalDate(), dateTime.toLocalDate()).getDays();
                                    final double feez=100*dday;

                                    mD1.child(memid).child(loaniid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){

                                                DatabaseReference activedefault=mD1.child(memid).child(loaniid);
                                                activedefault.child("memberid").setValue(memid);
                                                activedefault.child("membername").setValue(memname);
                                                activedefault.child("days").setValue(String.valueOf(dday));
                                                activedefault.child("fee").setValue(String.valueOf(feez));
                                                activedefault.child("officerid").setValue(officerid);
                                                activedefault.child("officername").setValue(officername);
                                                activedefault.child("loanid").setValue(loaniid);
                                                activedefault.child("loan").setValue(loanamount);
                                                activedefault.child("lastinst").setValue(lastinst);
                                                activedefault.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                activedefault.child("daterecorded").setValue(ft.format(new Date()));

                                                DatabaseReference activedefaultall=mD1.child("all").child(loaniid);
                                                activedefaultall.child("memberid").setValue(memid);
                                                activedefaultall.child("membername").setValue(memname);
                                                activedefaultall.child("days").setValue(String.valueOf(dday));
                                                activedefaultall.child("fee").setValue(String.valueOf(feez));
                                                activedefaultall.child("officerid").setValue(officerid);
                                                activedefaultall.child("officername").setValue(officername);
                                                activedefaultall.child("loanid").setValue(loaniid);
                                                activedefaultall.child("loan").setValue(loanamount);
                                                activedefaultall.child("lastinst").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst)));
                                                activedefaultall.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                activedefaultall.child("daterecorded").setValue(ft.format(new Date()));

                                                DatabaseReference oncedefault=mDe.child(memid).child(loaniid);
                                                oncedefault.child("memberid").setValue(memid);
                                                oncedefault.child("membername").setValue(memname);
                                                oncedefault.child("days").setValue(String.valueOf(dday));
                                                oncedefault.child("fee").setValue(String.valueOf(feez));
                                                oncedefault.child("officerid").setValue(officerid);
                                                oncedefault.child("officername").setValue(officername);
                                                oncedefault.child("loanid").setValue(loaniid);
                                                oncedefault.child("loan").setValue(loanamount);
                                                oncedefault.child("lastinst").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst)));
                                                oncedefault.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                oncedefault.child("daterecorded").setValue(ft.format(new Date()));

                                                DatabaseReference activedefauldayt=mD1.child("daily").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(loaniid);
                                                activedefauldayt.child("memberid").setValue(memid);
                                                activedefauldayt.child("membername").setValue(memname);
                                                activedefauldayt.child("days").setValue(String.valueOf(dday));
                                                activedefauldayt.child("fee").setValue(String.valueOf(feez));
                                                activedefauldayt.child("officerid").setValue(officerid);
                                                activedefauldayt.child("officername").setValue(officername);
                                                activedefauldayt.child("loanid").setValue(loaniid);
                                                activedefauldayt.child("loan").setValue(loanamount);
                                                activedefauldayt.child("lastinst").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst)));
                                                activedefauldayt.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                activedefauldayt.child("daterecorded").setValue(ft.format(new Date()));

                                                DatabaseReference activedefaulttimeli=mD1.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(loaniid);
                                                activedefaulttimeli.child("memberid").setValue(memid);
                                                activedefaulttimeli.child("membername").setValue(memname);
                                                activedefaulttimeli.child("days").setValue(String.valueOf(dday));
                                                activedefaulttimeli.child("fee").setValue(String.valueOf(feez));
                                                activedefaulttimeli.child("officerid").setValue(officerid);
                                                activedefaulttimeli.child("officername").setValue(officername);
                                                activedefaulttimeli.child("loanid").setValue(loaniid);
                                                activedefaulttimeli.child("loan").setValue(loanamount);
                                                activedefaulttimeli.child("lastinst").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst)));
                                                activedefaulttimeli.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                activedefaulttimeli.child("daterecorded").setValue(ft.format(new Date()));


                                                DatabaseReference allarr=FirebaseDatabase.getInstance().getReference().child("transactions").child("all").push();
                                                allarr.child("name").setValue(memname);
                                                allarr.child("type").setValue("Default");
                                                allarr.child("amount").setValue(String.valueOf(feez));
                                                allarr.child("status").setValue("Recorded");
                                                allarr.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                allarr.child("officer").setValue(officerid);
                                                allarr.child("timestam").setValue(ServerValue.TIMESTAMP);
                                                allarr.child("memberuid").setValue(memid);

                                                DatabaseReference allarrny=FirebaseDatabase.getInstance().getReference().child("transactions").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
                                                allarrny.child("name").setValue(memname);
                                                allarrny.child("type").setValue("Default");
                                                allarrny.child("amount").setValue(String.valueOf(feez));
                                                allarrny.child("status").setValue("Recorded");
                                                allarrny.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                allarrny.child("officer").setValue(officerid);
                                                allarrny.child("timestam").setValue(ServerValue.TIMESTAMP);
                                                allarrny.child("memberuid").setValue(memid);

                                                DatabaseReference memberhist=mDc.child(memid).child("credithist").child(loaniid);
                                                memberhist.child("defaultdays").setValue(dday);

                                                FirebaseDatabase.getInstance().getReference().child("members").child(memid).child("personaldetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String phone=dataSnapshot.child("phone").getValue().toString();

                                                        DatabaseReference newmessagenextpay=FirebaseDatabase.getInstance().getReference().child("notifications").child("loandefault").push();
                                                        newmessagenextpay.child("membername").setValue(memname);
                                                        newmessagenextpay.child("memberid").setValue(memid);
                                                        newmessagenextpay.child("message").setValue(memname+" you have defaulted on your loan of "+loanamount+" that was to be paid on "+new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst))+" please get in contact with Jitihada Credit to clear.");
                                                        newmessagenextpay.child("phone").setValue(phone);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });




                                            }else {
                                                DatabaseReference activedefault=mD1.child(memid).child(loaniid);
                                                activedefault.child("memberid").setValue(memid);
                                                activedefault.child("membername").setValue(memname);
                                                activedefault.child("days").setValue(String.valueOf(dday));
                                                activedefault.child("fee").setValue(String.valueOf(feez));
                                                activedefault.child("officerid").setValue(officerid);
                                                activedefault.child("officername").setValue(officername);
                                                activedefault.child("loanid").setValue(loaniid);
                                                activedefault.child("loan").setValue(loanamount);
                                                activedefault.child("lastinst").setValue(lastinst);
                                                activedefault.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                activedefault.child("daterecorded").setValue(ft.format(new Date()));

                                                DatabaseReference activedefaultall=mD1.child("all").child(loaniid);
                                                activedefaultall.child("memberid").setValue(memid);
                                                activedefaultall.child("membername").setValue(memname);
                                                activedefaultall.child("days").setValue(String.valueOf(dday));
                                                activedefaultall.child("fee").setValue(String.valueOf(feez));
                                                activedefaultall.child("officerid").setValue(officerid);
                                                activedefaultall.child("officername").setValue(officername);
                                                activedefaultall.child("loanid").setValue(loaniid);
                                                activedefaultall.child("loan").setValue(loanamount);
                                                activedefaultall.child("lastinst").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst)));
                                                activedefaultall.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                activedefaultall.child("daterecorded").setValue(ft.format(new Date()));

                                                DatabaseReference oncedefault=mDe.child(memid).child(loaniid);
                                                oncedefault.child("memberid").setValue(memid);
                                                oncedefault.child("membername").setValue(memname);
                                                oncedefault.child("days").setValue(String.valueOf(dday));
                                                oncedefault.child("fee").setValue(String.valueOf(feez));
                                                oncedefault.child("officerid").setValue(officerid);
                                                oncedefault.child("officername").setValue(officername);
                                                oncedefault.child("loanid").setValue(loaniid);
                                                oncedefault.child("loan").setValue(loanamount);
                                                oncedefault.child("lastinst").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst)));
                                                oncedefault.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                oncedefault.child("daterecorded").setValue(ft.format(new Date()));

                                                DatabaseReference activedefauldayt=mD1.child("daily").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(loaniid);
                                                activedefauldayt.child("memberid").setValue(memid);
                                                activedefauldayt.child("membername").setValue(memname);
                                                activedefauldayt.child("days").setValue(String.valueOf(dday));
                                                activedefauldayt.child("fee").setValue(String.valueOf(feez));
                                                activedefauldayt.child("officerid").setValue(officerid);
                                                activedefauldayt.child("officername").setValue(officername);
                                                activedefauldayt.child("loanid").setValue(loaniid);
                                                activedefauldayt.child("loan").setValue(loanamount);
                                                activedefauldayt.child("lastinst").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst)));
                                                activedefauldayt.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                activedefauldayt.child("daterecorded").setValue(ft.format(new Date()));

                                                DatabaseReference activedefaulttimeli=mD1.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(loaniid);
                                                activedefaulttimeli.child("memberid").setValue(memid);
                                                activedefaulttimeli.child("membername").setValue(memname);
                                                activedefaulttimeli.child("days").setValue(String.valueOf(dday));
                                                activedefaulttimeli.child("fee").setValue(String.valueOf(feez));
                                                activedefaulttimeli.child("officerid").setValue(officerid);
                                                activedefaulttimeli.child("officername").setValue(officername);
                                                activedefaulttimeli.child("loanid").setValue(loaniid);
                                                activedefaulttimeli.child("loan").setValue(loanamount);
                                                activedefaulttimeli.child("lastinst").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst)));
                                                activedefaulttimeli.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                activedefaulttimeli.child("daterecorded").setValue(ft.format(new Date()));

                                                DatabaseReference allarr=FirebaseDatabase.getInstance().getReference().child("transactions").child("all").push();
                                                allarr.child("name").setValue(memname);
                                                allarr.child("type").setValue("Default");
                                                allarr.child("amount").setValue(String.valueOf(feez));
                                                allarr.child("status").setValue("Recorded");
                                                allarr.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                allarr.child("officer").setValue(officerid);
                                                allarr.child("timestam").setValue(ServerValue.TIMESTAMP);
                                                allarr.child("memberuid").setValue(memid);

                                                DatabaseReference allarrny=FirebaseDatabase.getInstance().getReference().child("transactions").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
                                                allarrny.child("name").setValue(memname);
                                                allarrny.child("type").setValue("Default");
                                                allarrny.child("amount").setValue(String.valueOf(feez));
                                                allarrny.child("status").setValue("Recorded");
                                                allarrny.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                allarrny.child("officer").setValue(officerid);
                                                allarrny.child("timestam").setValue(ServerValue.TIMESTAMP);
                                                allarrny.child("memberuid").setValue(memid);

                                                DatabaseReference memberhist=mDc.child(memid).child("credithist").child(loaniid);
                                                memberhist.child("defaultdays").setValue(dday);

                                                FirebaseDatabase.getInstance().getReference().child("members").child(memid).child("personaldetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String phone=dataSnapshot.child("phone").getValue().toString();

                                                        DatabaseReference newmessagenextpay=FirebaseDatabase.getInstance().getReference().child("notifications").child("loandefault").push();
                                                        newmessagenextpay.child("membername").setValue(memname);
                                                        newmessagenextpay.child("memberid").setValue(memid);
                                                        newmessagenextpay.child("message").setValue(memname+" you have defaulted on your loan of "+loanamount+" that was to be paid on "+new SimpleDateFormat("EEE, MMM d, ''yy").format(Double.parseDouble(lastinst))+" please get in contact with Jitihada Credit to clear.");
                                                        newmessagenextpay.child("phone").setValue(phone);
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


                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mDa.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot snapshot1:dataSnapshot.getChildren()){
                                final String uid=snapshot1.child("uid").getValue().toString();
                                final String name=snapshot1.child("name").getValue().toString();
                                final String image=snapshot1.child("image").getValue().toString();
                                final String idnum=snapshot1.child("idnum").getValue().toString();


                                mDc.child(uid).child("credithist").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        double memloans=0;
                                        double memdefaults=0;
                                        double memarreas=0;
                                        double lorating=0;
                                        double derating=0;
                                        double arrating=0;
                                        double genrating=0;
                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                            String llo=snapshot.child("amount").getValue().toString();
                                            String lde=snapshot.child("defaultdays").getValue().toString();
                                            String lar=snapshot.child("totalarreas").getValue().toString();

                                            memloans=memloans+Double.parseDouble(llo);
                                            memdefaults=memdefaults+Double.parseDouble(lde);
                                            memarreas=memarreas+Double.parseDouble(lar);

                                            if (memloans>1&&memloans<20000){
                                                lorating=20;
                                            }else if (memloans>20000&&memloans<50000){
                                                lorating=30;
                                            }else if (memloans>50000&&memloans<100000){
                                                lorating=40;
                                            }else if (memloans>100000){
                                                lorating=50;
                                            }

                                            if (memdefaults>=1&&memdefaults<=5){
                                                derating=5;
                                            }else if (memdefaults>5&&memdefaults<=10){
                                                derating=10;
                                            }else if (memdefaults>10&&memdefaults<=15){
                                                derating=15;
                                            }else if (memdefaults>15&&memdefaults<=20){
                                                derating=20;
                                            }else if (memdefaults>20){
                                                derating=25;
                                            }

                                            if (memarreas>=1&&memarreas<=1000){
                                                arrating=5;
                                            }else if (memarreas>1000&&memarreas<10000){
                                                arrating=10;
                                            }else if (memarreas>10000){
                                                arrating=15;
                                            }

                                            genrating=lorating-(derating+arrating);

                                            DatabaseReference newRating=FirebaseDatabase.getInstance().getReference().child("memberratings").child(uid);
                                            newRating.child("name").setValue(name);
                                            newRating.child("image").setValue(image);
                                            newRating.child("idnum").setValue(idnum);
                                            newRating.child("uid").setValue(uid);
                                            newRating.child("rating").setValue(genrating);


                                        }
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



                    /*Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_LONG);
                    toast1.show();*/
                }


                if (testtime.equals("8:00 AM")) {

                    mD2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                final String chkdate=snapshot.child("checkdate").getValue().toString();
                                long lstdatedat=Long.parseLong(chkdate);
                                Date nextinstdat=new Date(lstdatedat);

                                DateTime dateTime = new DateTime(new Date());
                                DateTime dateTimez = new DateTime(nextinstdat);

                                final int dday= Days.daysBetween(dateTimez.toLocalDate(), dateTime.toLocalDate()).getDays();

                                if (dday==2){
                                    final String memname=snapshot.child("membername").getValue().toString();
                                    final String memid=snapshot.child("memberid").getValue().toString();
                                    final String loanamount=snapshot.child("balance").getValue().toString();
                                    final String loaninstall=snapshot.child("installment").getValue().toString();
                                    final String message=memname+" you have an upcoming loan installment of "+loaninstall+" on "+nextinstdat+", your balance is "+loanamount;

                                    FirebaseDatabase.getInstance().getReference().child("members").child(memid).child("personaldetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                             String phone=dataSnapshot.child("phone").getValue().toString();

                                            DatabaseReference newmessagenextpay=FirebaseDatabase.getInstance().getReference().child("notifications").child("loandue").push();
                                            newmessagenextpay.child("membername").setValue(memname);
                                            newmessagenextpay.child("memberid").setValue(memid);
                                            newmessagenextpay.child("message").setValue(message);
                                            newmessagenextpay.child("phone").setValue(phone);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });



                                }else if (dday==1){
                                    final String memname=snapshot.child("membername").getValue().toString();
                                    final String memid=snapshot.child("memberid").getValue().toString();
                                    final String loanamount=snapshot.child("balance").getValue().toString();
                                    final String loaninstall=snapshot.child("installment").getValue().toString();
                                    final String message=memname+" you have an upcoming loan installment of "+loaninstall+" on "+nextinstdat+", your balance is "+loanamount;

                                    FirebaseDatabase.getInstance().getReference().child("members").child(memid).child("personaldetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String phone=dataSnapshot.child("phone").getValue().toString();

                                            DatabaseReference newmessagenextpay=FirebaseDatabase.getInstance().getReference().child("notifications").child("loandue").push();
                                            newmessagenextpay.child("membername").setValue(memname);
                                            newmessagenextpay.child("memberid").setValue(memid);
                                            newmessagenextpay.child("message").setValue(message);
                                            newmessagenextpay.child("phone").setValue(phone);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }

            }
        });
    }
}
