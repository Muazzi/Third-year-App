package com.example.muaz1.curo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Tasks:
                    Intent task_intent = getIntent();
                    String userid=task_intent.getStringExtra("UserId");
                    task_intent.putExtra("UserId",userid);
                    task_intent.setClass(MainActivity.this,Tasks.class);
                    startActivity(task_intent);

                    break;
                case R.id.Meeting:
                    Intent meeting_intent = getIntent();
                    String id=meeting_intent.getStringExtra("UserId");
                    meeting_intent.putExtra("UserId",id);
                    meeting_intent.setClass(MainActivity.this,Meeting.class);


                    startActivity(meeting_intent);
                    break;

                case  R.id.Notes:
                    Intent Notes_intent = getIntent();
                    String Uid=Notes_intent.getStringExtra("UserId");
                    Notes_intent.putExtra("UserId",Uid);
                    Notes_intent.setClass(MainActivity.this,Notes.class);


                    startActivity(Notes_intent);


                    break;





            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }
}
