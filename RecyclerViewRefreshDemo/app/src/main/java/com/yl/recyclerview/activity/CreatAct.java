package com.yl.recyclerview.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yl.recyclerview.R;

public class CreatAct extends AppCompatActivity implements View.OnClickListener {
    private Button button1;
    private Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat);
        button1= (Button) findViewById(R.id.button1);
        button2= (Button) findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==button1){
            startActivity(new Intent(this,OpenGL.class));
        }else  if (view==button2){
            startActivity(new Intent(this,OpenGLAct.class));
        }
    }
}
