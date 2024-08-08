package com.example.spypark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {

    TextView textViewEmail,textViewName,textViewPhone, textViewVehchileNumber, textViewVehchileType, textViewParkName,bookingTime,checkout,amount;
    CardView amountView;
    Button  navigateToBook, buttonexitPark, buttonMap, allBookings;
    FirebaseAuth auth;
    FirebaseUser user;
    SharedPreferences sharedPreferences;
    DatabaseReference reference;
    private static final String sharedPreferencesName="myPref";
    private static final String vehchileNumberPref = "0000";
    private static final String vehchileTypePref = "1111";
    private static final String bookStatusPref = "2222";
    private static final String exitBook = "3333";
    private static final String pName = "4444";
    private static final String bookTime = "5555";
    private static final String checkoutTime = "6666";
    private static final String money = "7777";
    private static final String userName = "8888";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewEmail = findViewById(R.id.userEmail);
        textViewName = findViewById(R.id.userName);
        textViewPhone = findViewById(R.id.userPhone);
        navigateToBook = findViewById(R.id.navigateToBook);
        textViewVehchileNumber = findViewById(R.id.vehchileNumber);
        textViewVehchileType = findViewById(R.id.vehchileType);
        textViewParkName = findViewById(R.id.parkName);
        textViewPhone = findViewById(R.id.userPhone);
        buttonexitPark = findViewById(R.id.exitPark);
        amountView = findViewById(R.id.amountView);
        bookingTime = findViewById(R.id.bookingTime);
        checkout = findViewById(R.id.checkout);
        amount = findViewById(R.id.amount);
        sharedPreferences = getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        buttonMap = findViewById(R.id.map);
        allBookings = findViewById(R.id.allBookings);
        String Name = sharedPreferences.getString(userName, null);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(Name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                  @Override
                                                                  public void onComplete(Task<DataSnapshot> task) {
                                                                      if (task.isSuccessful()) {
                                                                          if (task.getResult().exists()) {
                                                                              DataSnapshot dataSnapshot = task.getResult();
                                                                              String userPhone = String.valueOf(dataSnapshot.child("phone").getValue());
                                                                              textViewPhone.setText(userPhone);
                                                                          }
                                                                      }
                                                                  }
                                                              });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parkName = sharedPreferences.getString(pName, null);
                if (parkName.equals("Phoenix")){
                    gotoUrl("https://goo.gl/maps/qQSQHYDiegz249PB9");
                }
                if (parkName.equals("Orion")){
                    gotoUrl("https://goo.gl/maps/exsHyJGqYdcbPn958");
                }
            }
        });
        allBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), allBookings.class);
                startActivity(intent);
            }
        });

        String vNumber = sharedPreferences.getString(vehchileNumberPref, null);
        String vType = sharedPreferences.getString(vehchileTypePref, null);
        String bStatus = sharedPreferences.getString(bookStatusPref, null);
        String exitStatus = sharedPreferences.getString(exitBook, null);
        String parkName = sharedPreferences.getString(pName, null);
        String timeWhenBooked = sharedPreferences.getString(bookTime, null);
        bookingTime.setText(timeWhenBooked);
        String checkout_Time = sharedPreferences.getString(checkoutTime, null);
        String moneyPay = sharedPreferences.getString(money, null);
        //String Name = sharedPreferences.getString(userName, null);
        //String Phone = sharedPreferences.getString(vehchileNumberPref, null);
        textViewName.setText(Name);
        amount.setText(String.valueOf(moneyPay));
        checkout.setText(checkout_Time);


        if (vNumber != null && vType != null && bStatus != null) {
            textViewVehchileNumber.setText(vNumber);
            textViewVehchileType.setText(vType);
            textViewParkName.setText(parkName);
            if (bStatus.equals("done")) {
                navigateToBook.setEnabled(false);
            } else {
                navigateToBook.setEnabled(true);
            }
            if (exitStatus.equals("can")) {
                buttonexitPark.setEnabled(true);
                buttonMap.setEnabled(true);

            } else {
                buttonexitPark.setEnabled(false);
                buttonMap.setEnabled(false);
            }


        } else {
            Toast.makeText(MainActivity.this, "Unable to get text", Toast.LENGTH_SHORT).show();
        }


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {
            textViewEmail.setText(user.getEmail());
        }
        navigateToBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), bookingListPage.class);
                startActivity(intent);
                finish();

            }
        });
        buttonexitPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vType = sharedPreferences.getString(vehchileTypePref, null);

                updateData(parkName, vType);

                navigateToBook.setEnabled(true);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
                String timeWhenReleased= simpleDateFormat.format(calendar.getTime());
                textViewVehchileNumber.setText("");
                textViewVehchileType.setText("");
                textViewParkName.setText("");
                buttonexitPark.setEnabled(false);
                buttonMap.setEnabled(false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(vehchileNumberPref, "");
                editor.putString(vehchileTypePref, "");
                editor.putString(bookStatusPref, "");
                editor.putString(exitBook, "can't");
                editor.putString(pName, "");
                editor.putString(checkoutTime, timeWhenReleased);
                editor.apply();
                String checkin_Time = sharedPreferences.getString(bookTime, null);
                String checkout_Time = sharedPreferences.getString(checkoutTime, null);
                checkout.setText(String.valueOf(checkout_Time));
                int startHour = Integer.valueOf(checkin_Time.substring(0,2));
                int startMin = Integer.valueOf(checkin_Time.substring(3,5));
                int endHour = Integer.valueOf(checkout_Time.substring(0,2));
                int endMin = Integer.valueOf(checkout_Time.substring(3,5));

                int hour= (endHour - startHour)*60;
                int min = endMin  - startMin;
                int amounT = (hour + min)/60;
                SharedPreferences.Editor editor1 = sharedPreferences.edit();

                if(amounT<=1){
                    editor1.putString(money, "30");
                    editor1.apply();
                    amount.setText("30");
                }
                else{
                    int moneyAmount = amounT*30;
                    editor1.putString(money, String.valueOf(moneyAmount));
                    editor1.apply();
                    amount.setText(String.valueOf(moneyAmount));
                }
                String dataMoney = sharedPreferences.getString(money, null);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("bookTime",timeWhenBooked);
                hashMap.put("parkName",parkName);
                hashMap.put("vechNum",vNumber);
                hashMap.put("endTime",checkout_Time);
                hashMap.put("money",dataMoney);
                reference = FirebaseDatabase.getInstance().getReference("Bookings");
                reference.child(vNumber).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                    }
                });
                Intent intent = new Intent(MainActivity.this, payment.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void updateData(String parkName, String object) {
        //read data
        reference = FirebaseDatabase.getInstance().getReference("Parkings");
        reference.child(parkName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String bike_count = String.valueOf(dataSnapshot.child("bike").getValue());
                        String car_count = String.valueOf(dataSnapshot.child("car").getValue());
                        int bcount = Integer.valueOf(bike_count)-1;
                        int ccount = Integer.valueOf(car_count)-1;
                        HashMap parkData = new HashMap();

                        if (object=="bike")
                        {
                            parkData.put(object, String.valueOf(bcount));
                        }
                        if (object=="car")
                        {
                            parkData.put(object, String.valueOf(ccount));
                        }
                        reference = FirebaseDatabase.getInstance().getReference("Parkings");
                        reference.child(parkName).updateChildren(parkData).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Booking ended", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Failed to exit", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            }
        });
        }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout: {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }
    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}

