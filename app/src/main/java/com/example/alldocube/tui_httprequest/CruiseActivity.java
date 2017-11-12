package com.example.alldocube.tui_httprequest;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
    public static EditText editText = null;
    ArrayList<Object> list = null;
    public static int selectedid = 1;
    Context x = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruise);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView title = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("Trip Choosing");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        Button mButton2 = (Button) findViewById(R.id.button9);
        editText = (EditText) findViewById(R.id.editText);
        mButton2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() == 6)
                    changeActivity();
            }

            private void changeActivity() {

                Intent intent4 = new Intent(x, HarborActivity.class);
                startActivity(intent4);

            }
        });

// Create an ArrayAdapter using the string array and a default spinner layout
        //    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //      R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        //   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        // spinner.setAdapter(adapter);


    }

}
