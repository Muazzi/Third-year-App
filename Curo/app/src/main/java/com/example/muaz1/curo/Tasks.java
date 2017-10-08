package com.example.muaz1.curo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Tasks extends AppCompatActivity {

    TextView prompt ;
    Spinner myspinner;
    ArrayList<Project> Projects;
    ArrayList<UserTask> UserTasks;
    ArrayList<String> ProjectNames;
    String UserID;
    String Selected_Project_name;
    RecyclerView mRecycleView;
    private  TaskAdapter adapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Tasks:


                    break;
                case R.id.Meeting:
                    Intent meeting_intent = getIntent();
                    String id=meeting_intent.getStringExtra("UserId");
                    meeting_intent.putExtra("UserId",id);
                    meeting_intent.setClass(Tasks.this,Meeting.class);


                    startActivity(meeting_intent);
                    break;



            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        UserTasks = new ArrayList<UserTask>();


        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        prompt=(TextView)findViewById(R.id.edtPrompt);
        myspinner=(Spinner) findViewById(R.id.MySpinner);

        Projects = new  ArrayList<Project>();
        ProjectNames = new ArrayList<String>();

        Intent task_intent = getIntent();
        UserID=task_intent.getStringExtra("UserId");


        new RetriveProjects().execute("http://192.168.1.100:80/CuroServices.svc/GetProjects/"+UserID);


            /**


             */
       ;
        mRecycleView=(RecyclerView)findViewById(R.id.recycleview);
        mRecycleView.setLayoutManager(new LinearLayoutManager(Tasks.this));

        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                Selected_Project_name = myspinner.getSelectedItem().toString();
                UserTasks.clear();

                new GetUserTasks().execute("http://192.168.1.100:80/CuroServices.svc/GetTasks/"+Selected_Project_name);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







    }

    class  RetriveProjects extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... param) {


            HttpURLConnection connection;
            BufferedReader reader;
            String id=null;
            try {

                final URL url = new URL(param[0]);
                connection=(HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");

                connection.setRequestMethod("GET");
                int result= connection.getResponseCode();
                if(result==200)
                {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null)
                    {
                        id= line;



                    }
                }

            }
            catch ( Exception e)
            {
                e.printStackTrace();
            }

            return id;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s!=null)
            {

                try
                {

                    JSONArray jsonArray = new JSONArray(s);

                    for(int i=0 ;i<jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                       String name = obj.getString("Name");

                        ProjectNames.add(name);

                    }
                  myspinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinneritem,ProjectNames));







                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }





            }else
            {
                Toast.makeText(Tasks.this,"Could not get any data.", Toast.LENGTH_LONG).show();
            }





        }
    }

    class  GetUserTasks extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... param) {


            HttpURLConnection connection;
            BufferedReader reader;
            String Tasks=null;
            try {

                final URL url = new URL(param[0]);
                connection=(HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");

                connection.setRequestMethod("GET");
                int result= connection.getResponseCode();
                if(result==200)
                {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null)
                    {
                        Tasks= line;



                    }
                }

            }
            catch ( Exception e)
            {
                e.printStackTrace();
            }

            return Tasks;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s!=null)
            {

                try
                {

                    JSONArray jsonArray = new JSONArray(s);
                    UserTasks.clear();

                    for(int i=0 ;i<jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        UserTask task = new UserTask();

                        task.Definition= obj.getString("Definition");
                          task.Priority_level=obj.getString("Priority");

                          UserTasks.add(task);




                    }

                     adapter = new TaskAdapter(Tasks.this,UserTasks);
                    mRecycleView.setAdapter(adapter);








                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }





            }else
            {
                Toast.makeText(Tasks.this,"Could not get any data.", Toast.LENGTH_LONG).show();
            }





        }
    }




}
