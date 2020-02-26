package com.hellmates.fastercheckout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.phone_number);
    }

    public void gotocart(View view){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("UserName",userName.getText().toString().trim());
        intent.putExtra("PhoneNumber",phoneNumber.getText().toString().trim());
        startActivity(intent);
    }
}
