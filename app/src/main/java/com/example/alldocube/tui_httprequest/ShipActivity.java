package com.example.alldocube.tui_httprequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ShipActivity extends AppCompatActivity {

    JSONArray ships= null;
    JSONObject jsonObject=null;
    JSONObject shiphelper= null;
    JSONObject ship = null;
    public static int selectedid=0;
    String shipname = null;
    int shipid = 0;
    Context x = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("asd","Asdasd+"+LoginActivity.user);

        try{


        String request        = "https://venuscallipyge.nexttry.pl/wp-json/vcapi/getship";
        URL url            = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setUseCaches( false );

        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = IOUtils.toString(in, "UTF-8");
         jsonObject = new JSONObject(result);
        Log.d("asd",""+jsonObject);


        Log.d("test",ships+"asd");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            addRadioButtons((int)jsonObject.get("shipcount"),jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });*/
        Button mButton2 = (Button) findViewById(R.id.button);
        mButton2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                if(selectedid!=0)
                changeActivity();
            }
            private void changeActivity(){

                Intent intent4 = new Intent( x, CruiseActivity.class );
                startActivity(intent4);

            }
        });
    }

    public void addRadioButtons(int number, final JSONObject ships) {


        for (int row = 0; row < 1; row++) {
            RadioGroup ll = new RadioGroup(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            try {
                 shiphelper= ships.getJSONObject("ships");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("asd","asd1"+shiphelper);
            for (int i = 0; i < number; i++) {
                try {
                     ship = (JSONObject) shiphelper.get("a"+i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RadioButton rdbtn = new RadioButton(this);
                try {
                    shipid=Integer.valueOf((String)ship.get("ID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rdbtn.setId(shipid);
                rdbtn.setOnClickListener(new RadioButton.OnClickListener( ) {
                    @Override
                public void onClick(View view) {
                        RadioButton s=(RadioButton)view;
                        selectedid=s.getId();
                        Log.d("est",s.getId()+"");
                        // Is the button now checked?
                    }

                        // Check which radio button was clicked



                });
                try {
                    shipname=(String)ship.get("NAME");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rdbtn.setText(shipname);
                ll.addView(rdbtn);
            }
            ((ViewGroup) findViewById(R.id.radiogroup)).addView(ll);
        }



    }




}
