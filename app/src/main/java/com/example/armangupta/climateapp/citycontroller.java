package com.example.armangupta.climateapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class citycontroller extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citycontroller);
        final EditText editText=(EditText)findViewById(R.id.editcity);
        Button button=(Button)findViewById(R.id.button);
        Button button2=(Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityname=editText.getText().toString();
                Intent mIntent=new Intent(citycontroller.this,MainActivity.class);
                mIntent.putExtra("city",cityname);
                startActivity(mIntent);
            }
        });
    }
}
