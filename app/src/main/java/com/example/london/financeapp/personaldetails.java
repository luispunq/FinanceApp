package com.example.london.financeapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class personaldetails extends Fragment {
    private EditText  mFname,mSname,mLname,mIdnum,mDob,mMarital,mPhone,mAddress,mEducation,mEmail;
    private ImageView mImage;
    private Button mProcess;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase2,mD1,mD3,mDatabases4,mDatabase3;
    private StorageReference mProfileImageStore;
    private String user,officerid,longt,latt;
    private static final int GALLERY_REQUEST=1;
    private Uri mImageuri=null;
    private ProgressDialog mProgress;

    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private EditText mResidence;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_personaldetails, container, false);
        mProgress=new ProgressDialog(getContext());
        mFname=view.findViewById(R.id.firstname);
        mSname=view.findViewById(R.id.middlename);
        mLname=view.findViewById(R.id.lastname);
        mIdnum=view.findViewById(R.id.idno);
        mDob=view.findViewById(R.id.dob);
        mMarital=view.findViewById(R.id.maritalsts);
        mPhone=view.findViewById(R.id.phone);
        mAddress=view.findViewById(R.id.address);
        mResidence=view.findViewById(R.id.residence);
        //mResidence.setThreshold(3);


        /*mPlaceArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);*/
        //mResidence.setAdapter(mPlaceArrayAdapter);
        mEducation=view.findViewById(R.id.education);
        mImage=view.findViewById(R.id.userimage);
        mProcess=view.findViewById(R.id.next1);
        mEmail=view.findViewById(R.id.email);

        //mResidence.setOnItemClickListener(mAutocompleteClickListener);


        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mDatabase3= FirebaseDatabase.getInstance().getReference().child("members");
        mDatabase2= FirebaseDatabase.getInstance().getReference().child("members").child(user).child("personaldetails");
        mProfileImageStore= FirebaseStorage.getInstance().getReference().child("profileimages");
        mD1=FirebaseDatabase.getInstance().getReference().child("Active");
        mD3= FirebaseDatabase.getInstance().getReference().child("allmembers");

        mD1.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                officerid=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSetup();
            }
        });


        return  view;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            mResidence.setText(Html.fromHtml(place.getName() + ""));
            longt=String.valueOf(Html.fromHtml(String.valueOf(place.getLatLng().longitude)));
            latt=String.valueOf(Html.fromHtml(String.valueOf(place.getLatLng().latitude)));

        }
    };

    private void finishSetup() {
        final String Username=mFname.getText().toString()+" "+mSname.getText().toString()+" "+mLname.getText().toString();
        final String ID=mIdnum.getText().toString().trim();
        final String Dob=mDob.getText().toString();
        final String Marital=mMarital.getText().toString().trim();
        final String Phone=mPhone.getText().toString();
        final String Address=mAddress.getText().toString().trim();
        final String Residence=mResidence.getText().toString().trim();
        final String Education=mEducation.getText().toString().trim();
        final String Email=mAuth.getCurrentUser().getEmail();


        String ima=mImageuri.getLastPathSegment();

        if(!ima.isEmpty()&&!TextUtils.isEmpty(Username)&&!TextUtils.isEmpty(ID)&&!TextUtils.isEmpty(Dob)&&!TextUtils.isEmpty(Marital)){
            mProgress.setMessage("Setting Up Account..");
            mProgress.setMessage("Registering: "+Username);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            StorageReference filepath = mProfileImageStore.child(user);
            filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl=taskSnapshot.getDownloadUrl().toString();



                    mDatabase2.child("name").setValue(Username.toUpperCase());
                    mDatabase2.child("uid").setValue(user);
                    mDatabase2.child("idnum").setValue(ID);
                    mDatabase2.child("maritalstatus").setValue(Marital);
                    mDatabase2.child("image").setValue(downloadUrl);
                    mDatabase2.child("phone").setValue(removeFirstChar("+254"+Phone));
                    mDatabase2.child("address").setValue(Address);
                    mDatabase2.child("residence").setValue(Residence);
                    mDatabase2.child("education").setValue(Education);
                    mDatabase2.child("dob").setValue(Dob);
                    mDatabase2.child("email").setValue(Email);
                    mDatabase2.child("latitude").setValue("");
                    mDatabase2.child("longitude").setValue("");

                    DatabaseReference officermembers=mDatabase3.child(officerid).child(user);
                    officermembers.child("name").setValue(Username.toUpperCase());
                    officermembers.child("uid").setValue(user);
                    officermembers.child("idnum").setValue(ID);
                    officermembers.child("image").setValue(downloadUrl);

                    DatabaseReference newmem=mD3.push();
                    newmem.child("name").setValue(Username.toUpperCase());
                    newmem.child("uid").setValue(user);
                    newmem.child("image").setValue(downloadUrl);
                    newmem.child("idnum").setValue(ID);



                    mProgress.dismiss();
                    Toast.makeText(getContext(), "Personal Details Submited!", Toast.LENGTH_LONG).show();
                }
            });



        }else {
            Toast.makeText(getContext(), "Please Enter all details", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            mImageuri=data.getData();
            mImage.setImageURI(mImageuri);
        }
    }

    private void displaySelectedScreen(int itemId,View view) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case 1:
                fragment = new businessdetails();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

    }


    public String removeFirstChar(String s){
        return s.substring(1);
    }



}
