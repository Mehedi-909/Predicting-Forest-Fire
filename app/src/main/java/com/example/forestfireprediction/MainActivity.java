package com.example.forestfireprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Button fileButton;
    //TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileButton = findViewById(R.id.Read);
        //textView = findViewById(R.id.textView);
        editText = findViewById(R.id.kEditText);


        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(editText.getText().toString().trim().length() > 0){
                    Intent send = new Intent(MainActivity.this, LungCancer.class);
                    int k = Integer.parseInt(editText.getText().toString());
                    send.putExtra("k",k);
                    startActivity(send);
                }
                else {
                    editText.requestFocus();
                }

            }
        });
    }


}