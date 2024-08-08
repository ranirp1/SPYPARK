package com.example.spypark;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spypark.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class bookInfo extends AppCompatActivity {

    DatabaseReference reference;
    TextView bikeTv, carTv,vehchileNumber;
    Button buttonBike, buttonCar, buttonMap;
    ActivityMainBinding binding;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    private static final String sharedPreferencesName="myPref";
    private static final String vehchileNumberPref = "0000";
    private static final String vehchileTypePref = "1111";
    private static final String bookStatusPref = "2222";
    private static final String exitBook = "3333";
    private static final String pName = "4444";
    private static final String bookTime = "5555";
    private static final String checkoutTime = "6666";
    private static final String money = "7777";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_book_info);


        bikeTv= findViewById(R.id.bike);
        carTv= findViewById(R.id.car);
        buttonBike = findViewById(R.id.bikeBook);
        buttonCar = findViewById(R.id.carBook);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        vehchileNumber = findViewById(R.id.vechileNumber);
        sharedPreferences = getSharedPreferences(sharedPreferencesName,MODE_PRIVATE );
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        buttonMap = findViewById(R.id.map);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String parkName = intent.getStringExtra("actionbarTitle");
                if (parkName.equals("Phoenix")){
                    gotoUrl("https://goo.gl/maps/qQSQHYDiegz249PB9");
                }
                if (parkName.equals("Orion")){
                    gotoUrl("https://goo.gl/maps/exsHyJGqYdcbPn958");
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        String parkName = intent.getStringExtra("actionbarTitle");

        actionBar.setTitle(parkName);

        readData(parkName);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(bookInfo.this, bookInfo.class);
                intent.putExtra("actionbarTitle",parkName);
                startActivity(intent);
                finish();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        buttonBike.setOnClickListener(new View.OnClickListener() {
            String bike = "bike";

            @Override
            public void onClick(View v) {
                String VN= String.valueOf(vehchileNumber.getText());
                if(TextUtils.isEmpty(VN)){
                    Toast.makeText(bookInfo.this, "Enter Vechile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateData(parkName,bike);
                readData(parkName);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
                String time= simpleDateFormat.format(calendar.getTime());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(vehchileNumberPref, VN);
                editor.putString(vehchileTypePref, bike);
                editor.putString(bookStatusPref, "done");
                editor.putString(exitBook, "can");
                editor.putString(pName, parkName);
                editor.putString(bookTime, time);
                editor.putString(checkoutTime, "");
                editor.putString(money, "");
                editor.apply();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("bookTime",time);
                hashMap.put("parkName",parkName);
                hashMap.put("vechNum",VN);
                hashMap.put("endTime","");
                hashMap.put("money","");
                databaseReference.child("Bookings")
                        .child(VN)
                        .setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess(Void aVoid){
                            }
                        });
                Intent intent = new Intent(bookInfo.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });

        buttonCar.setOnClickListener(new View.OnClickListener() {

            String car = "car";

            @Override

            public void onClick(View view) {
                String VN= String.valueOf(vehchileNumber.getText());
                if(TextUtils.isEmpty(VN)){
                    Toast.makeText(bookInfo.this, "Enter Vechile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateData(parkName,car);
                readData(parkName);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
                String time= simpleDateFormat.format(calendar.getTime());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(vehchileNumberPref, VN);
                editor.putString(vehchileTypePref, car);
                editor.putString(bookStatusPref, "done");
                editor.putString(exitBook, "can");
                editor.putString(pName, parkName);
                editor.putString(bookTime, time);
                editor.putString(checkoutTime, "");
                editor.putString(money, "");
                editor.apply();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("bookTime",time);
                hashMap.put("parkName",parkName);
                hashMap.put("vechNum",VN);
                hashMap.put("endTime","");
                hashMap.put("money","");
                databaseReference.child("Bookings")
                        .child(VN)
                        .setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess(Void aVoid){
                                Toast.makeText(bookInfo.this, "Booking Confirmed", Toast.LENGTH_SHORT).show();
                            }
                        });
                Intent intent = new Intent(bookInfo.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void updateData(String parkName, String object) {

        String bikeCount =  String.valueOf(bikeTv.getText());

        String carCount =  String.valueOf(carTv.getText());
        int cCount= Integer.valueOf(carCount) + 1;
        int bCount= Integer.valueOf(bikeCount) + 1;


        HashMap parkData = new HashMap();

        if (object=="bike")
        {
            parkData.put(object, String.valueOf(bCount));
        }
        if (object=="car")
        {
            parkData.put(object, String.valueOf(cCount));
        }
        reference = FirebaseDatabase.getInstance().getReference("Parkings");
        reference.child(parkName).updateChildren(parkData).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                if(task.isSuccessful()){
                    if (object=="bike")
                    {
                        bikeTv.setText(String.valueOf(bCount));
                    }
                    if (object=="car")
                    {
                        carTv.setText(String.valueOf(cCount));
                    }
                }
                else{
                    Toast.makeText(bookInfo.this, "Failed to book", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void readData(String parkName) {
        reference = FirebaseDatabase.getInstance().getReference("Parkings");
        reference.child(parkName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String bike_count = String.valueOf(dataSnapshot.child("bike").getValue());
                        String car_count = String.valueOf(dataSnapshot.child("car").getValue());
                        int bcount = Integer.valueOf(bike_count);
                        int ccount = Integer.valueOf(car_count);
                        if(parkName.equals("Phoenix") && bcount>=120){
                            bikeTv.setText(bike_count);
                            buttonBike.setEnabled(false);
                        }
                        else{
                            bikeTv.setText(bike_count);
                        }
                        if(parkName.equals("Phoenix") && ccount>=80){
                            carTv.setText(car_count);
                            buttonCar.setEnabled(false);
                        }
                        else{
                            carTv.setText(car_count);
                        }
                        if(parkName.equals("Orion") && bcount>=80){
                            bikeTv.setText(bike_count);
                            buttonBike.setEnabled(false);
                        }
                        else{
                            bikeTv.setText(bike_count);
                        }
                        if(parkName.equals("Orion") && ccount>=50){
                            carTv.setText(car_count);
                            buttonCar.setEnabled(false);
                        }
                        else{
                            carTv.setText(car_count);
                        }



                    }
                    else{
                        Toast.makeText(bookInfo.this, "Please wait there is some server error 1", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(bookInfo.this, "Please wait there is some server error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}