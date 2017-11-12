package com.example.alldocube.tui_httprequest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CruiseActivity extends AppCompatActivity {

    ArrayList <Object> list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("asd",""+ShipActivity.selectedid);
        try{

        String urlParameters  = "ID="+ShipActivity.selectedid;

        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = "https://venuscallipyge.nexttry.pl/wp-json/vcapi/getcruise";
        URL url            = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write( postData );

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = IOUtils.toString(in, "UTF-8");
        JSONObject jsonObject = new JSONObject(result);
            Log.d("asd",""+result);
        }

        catch (Exception e) {
            e.printStackTrace();
        }

// Create an ArrayAdapter using the string array and a default spinner layout
    //    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
          //      R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
     //   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
       // spinner.setAdapter(adapter);


    }

}
