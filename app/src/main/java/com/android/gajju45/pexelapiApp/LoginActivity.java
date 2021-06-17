package com.android.gajju45.pexelapiApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {
    private CountryCodePicker countryCodePicker;
    private EditText number;
    AppCompatButton next;
    TextView skipTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }

        countryCodePicker = findViewById(R.id.ccp);
        number = findViewById(R.id.editText_carrierNum);

        next = findViewById(R.id.next);
        skipTV=findViewById(R.id.skipTV);
        countryCodePicker.registerCarrierNumberEditText(number);
        skipTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(number.getText().toString())) {

                    number.setError("Enter Your Mobile Number");
                    //Toast.makeText(LoginActivity.this, "Enter Your Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (number.getText().toString().replace(" ", "").length()!=10) {

                    number.setError("Enter Your Valid Number");
                    //Toast.makeText(LoginActivity.this, "Please Enter A Valid  Number", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                    intent.putExtra("number", countryCodePicker.getFullNumberWithPlus().replace(" ", ""));
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
}
