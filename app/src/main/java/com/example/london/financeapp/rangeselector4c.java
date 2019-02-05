package com.example.london.financeapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class rangeselector4c extends AppCompatActivity {
    private TextView mStart,mEnd;
    private Button mBtn;
    private Calendar myCalendar;
    private String startd,endd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rangeselector);
        mStart=findViewById(R.id.startd);
        mEnd=findViewById(R.id.endd);
        myCalendar=Calendar.getInstance();
        mBtn=findViewById(R.id.button2);

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                startd=String.valueOf(calendar.getTimeInMillis());
                String myFormat = "EEE, MMM d, ''yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                mStart.setText(sdf.format(myCalendar.getTime()));
            }

        };

        final DatePickerDialog.OnDateSetListener date= new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                endd=String.valueOf(calendar.getTimeInMillis());
                String myFormat = "EEE, MMM d, ''yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                mEnd.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(rangeselector4c.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(rangeselector4c.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(rangeselector4c.this,profitability.class);
                Bundle extras = new Bundle();
                extras.putString("type", "all");
                extras.putString("date", "-");
                extras.putString("year", "-");
                extras.putString("month", "-");
                extras.putString("start", startd);
                extras.putString("end", endd);
                back.putExtras(extras);
                startActivity(back);
            }
        });
    }
}
