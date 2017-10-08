package com.example.muaz1.curo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Authentication extends AppCompatActivity {

    EditText Username;
    EditText Email;
    EditText Pasword;
    Button btnlogin;
    Button Signup;
    String urlservice;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Username = (EditText) findViewById(R.id.edtUsername);
        Pasword = (EditText) findViewById(R.id.edtPassword);
        Email = (EditText) findViewById(R.id.edtEmailAdress);
        btnlogin=(Button) findViewById(R.id.btnLogin);

        btnlogin.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view) {
                                            GetUserID task = new GetUserID();

                                            name =Username.getText().toString();

                                            urlservice="http://192.168.1.100:80/CuroServices.svc/ValidateUser/"+name;


                                            try {
                                                final String id = task.execute(urlservice).get();


                                                if (id.equals("777777"))
                                                {
                                                    Toast.makeText(Authentication.this,"Wrong Credentials",Toast.LENGTH_LONG).show();
                                                }
                                                else
                                                {
                                                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                                    i.putExtra("UserId",id);

                                                    startActivity(i);

                                                }



                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }


        );






    }

    class  GetUserID extends AsyncTask<String,Void,String>
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

            }else
            {
                Toast.makeText(Authentication.this,"Could not get any data.", Toast.LENGTH_LONG).show();
            }





        }
    }



}
