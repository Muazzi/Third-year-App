package com.example.muaz1.curo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;

public class Welcome extends AppCompatActivity {

    public static int progressInterval = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                //TODO Auto-generated method sub
                Intent i = new Intent(Welcome.this, Authentication.class);
                startActivity(i);

                this.finish();
            }
            private void finish(){
                //TODO Auto-generated method sub

            }

        },progressInterval);
    };
}
