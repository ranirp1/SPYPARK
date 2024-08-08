package com.example.spypark;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spypark.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class register extends AppCompatActivity {
    TextInputEditText editTextName, editTextEmail, editTextPhone, editTextPassword;
    Button buttonRegister;
    TextView buttonLogin;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    ActivityMainBinding binding;
    FirebaseDatabase db;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    private static final String sharedPreferencesName="myPref";
    private static final String userName = "8888";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_register);
        editTextName = findViewById(R.id.Name);
        editTextEmail= findViewById(R.id.register_email);
        editTextPhone= findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.register_password);
        buttonRegister = findViewById(R.id.register_button);
        buttonLogin= findViewById(R.id.navigate_to_login);
        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.register_progress);
        sharedPreferences = getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, phone, name, password;
                name= editTextName.getText().toString();
                email= editTextEmail.getText().toString();
                phone= editTextPhone.getText().toString();
                password= editTextPassword.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(register.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(register.this, "Enter Phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NotNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(register.this, "User Registered.",Toast.LENGTH_SHORT).show();
                                    users users = new users(name, email, phone, password);
                                    db = FirebaseDatabase.getInstance();
                                    reference = db.getReference("Users");
                                    reference.child(name).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(userName, name);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            editTextName.setText("");
                                            editTextEmail.setText("");
                                            editTextPhone.setText("");
                                            editTextPassword.setText("");

                                        }
                                    });

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
    }

}