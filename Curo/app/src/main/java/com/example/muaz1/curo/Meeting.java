package com.example.muaz1.curo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Meeting extends AppCompatActivity {


    ArrayList<String> ProjectNames;
    Spinner ProjectNameSpinner;
    String Selected_Project_name;
    String Agenda;
    EditText edtAgenda;
    TimePicker timePicker;
    int Hour;
    int min;
    String UserID;
    String U_id;
    String time;
    String Pid;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Tasks:
                    Intent task_intent = getIntent();
                     UserID=task_intent.getStringExtra("UserId");
                    task_intent.putExtra("UserId",UserID);
                    task_intent.setClass(Meeting.this,Tasks.class);
                    startActivity(task_intent);
                    break;
                case R.id.Meeting:




                    break;

                case R.id.Notes:
                    Intent note_intent = getIntent();
                    UserID=note_intent.getStringExtra("UserId");
                    note_intent.putExtra("UserId",UserID);
                    note_intent.setClass(Meeting.this,Notes.class);
                    startActivity(note_intent);

                    break;



            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ProjectNames = new ArrayList<String>();
        ProjectNameSpinner =(Spinner)findViewById(R.id.ProjectSpinner);

        edtAgenda=(EditText)findViewById(R.id.Agenda);


        edtAgenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Agenda= charSequence.toString();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Agenda=charSequence.toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Agenda=editable.toString();




            }
        });




        timePicker=(TimePicker)findViewById(R.id.TimePicker);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        Intent intent = getIntent();
        U_id=intent.getStringExtra("UserId");

        new Meeting.RetriveProjects().execute("http://192.168.1.100:80/CuroServices.svc/GetProjects/"+U_id);

        ProjectNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                Selected_Project_name = ProjectNameSpinner.getSelectedItem().toString();
                Log.i("SelectedProject:",Selected_Project_name);

                try
                {
                     new RetriveProjectId().execute("http://192.168.1.100:80/CuroServices.svc/GetProjectID/"+Selected_Project_name);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }





            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
                                        {
                                            @Override
                                            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                                                Hour=i ;
                                                min=i1;

                                                Log.i("Time:","Hour:"+Integer.toString(Hour)+"Min:"+Integer.toString(min));

                                                time=Integer.toString(i)+":"+Integer.toString(min)+":"+"00";
                                            }
                                        }

    );

















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
                    ProjectNameSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinneritem,ProjectNames));







                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }





            }else
            {
                Toast.makeText(Meeting.this,"Could not get any data.", Toast.LENGTH_LONG).show();
            }





        }
    }

    public  void Submit_Data(View view)
    {
        Agenda= edtAgenda.getText().toString();
        Log.i("Agenda",Agenda);
      Log.i("Hour",Integer.toString(Hour));
        Log.i("min",Integer.toString(min));
        Log.i("pid",Pid);
        new Submitdata().execute("http://192.168.1.100:80/CuroServices.svc/Create_Meeting");
    }
   class Submitdata extends  AsyncTask<String,Void,String>
   {
       String status=null;


       @Override
       protected String doInBackground(String... strings) {
           HttpURLConnection connection=null;
           BufferedReader bufferedReader;

           try {
               final URL url= new URL(strings[0]);
               connection=(HttpURLConnection) url.openConnection();
               connection.setDoOutput(true);
               connection.setDoInput(true);
               connection.setChunkedStreamingMode(0);
               connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
               connection.setRequestMethod("POST");

               JSONObject jsonObject = new JSONObject();
               jsonObject.put("P_ID",Pid);
               jsonObject.put("Agenda",Agenda);
               jsonObject.put("Hour",Integer.toString(Hour));
               jsonObject.put("Min",Integer.toString(min));

               Log.e("params",jsonObject.toString());

               OutputStream out = new BufferedOutputStream(connection.getOutputStream());
               out.write(jsonObject.toString().getBytes());
               out.flush();
               out.close();

               int result = connection.getResponseCode();


               if(result==200)
               {
                   InputStream in=new BufferedInputStream(connection.getInputStream());
                   bufferedReader = new BufferedReader(new InputStreamReader(in));
                   StringBuilder sb = new StringBuilder();
                   String line =null;
                   while ((line=bufferedReader.readLine())!=null)
                   {
                       status=line;
                   }

               }



           }
           catch ( Exception e)
           {
               e.printStackTrace();
           }
           return  status;
       }

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);
           if(s!=null)
           {
               Toast.makeText(Meeting.this,"Saved Successful",Toast.LENGTH_LONG).show();
           }
           else
           {
               Toast.makeText(Meeting.this,"Failed",Toast.LENGTH_LONG).show();
           }


       }
   }

    class  RetriveProjectId extends AsyncTask<String,Void,String>
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


             if(s!=null) {

                 try
                 {
                     JSONObject object = new JSONObject(s);

                     Pid= object.getString("P_ID");
                 }catch ( Exception e)
                 {
                     e.printStackTrace();
                 }




             } else
             {
                 Toast.makeText(Meeting.this,"Could not get any data.", Toast.LENGTH_LONG).show();
             }












        }
    }
}
