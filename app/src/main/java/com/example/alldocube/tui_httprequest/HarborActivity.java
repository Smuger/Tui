package com.example.alldocube.tui_httprequest;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HarborActivity extends AppCompatActivity {
    JSONObject jsonObject=null;
    JSONObject jsonObject2=null;
    JSONObject ports =null;
    Context x = this;
    FrameLayout mTabHost =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harbor);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView title=(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("Avaible Excursions");
        try {

            String urlParameters = "ID=" + CruiseActivity.editText.getText().toString();

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            String request = "https://venuscallipyge.nexttry.pl/wp-json/vcapi/getportfocall";
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);

            } catch (Exception e) {
                e.printStackTrace();
            }
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");

            jsonObject = new JSONObject(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

           int count=0;
        try {
            count=(int)jsonObject.get("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
           ports = (JSONObject) jsonObject.get("ports");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabid);

        for (int i = 0; i < count; i++) {
            try {
               JSONObject port = (JSONObject) ports.get("a"+i);

                TextView asd = new TextView(x);
                asd.setText((String)port.get("NAME"));
                TabLayout.Tab firstTab = tabLayout.newTab(); // Create a new Tab names "First Tab"
                firstTab.setText((String)port.get("NAME"));
                firstTab.setContentDescription((String)port.get("ID")+','+(String)port.get("TIME_IN_PORT")+','+(String)port.get("TIME_OUT_PORT"));
                tabLayout.addTab(firstTab);
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        LinearLayout linLayout = (LinearLayout) findViewById(R.id.givemepower);
                        linLayout.removeAllViews();
                        try {
                            String[] parts = tab.getContentDescription().toString().split(",");

                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                            String dateStart =(String)parts[1];

                            String dateStop = (String)parts[2];
                            Date d1 = null;
                            Date d2 = null;
                            try {
                                d1 = format.parse(dateStart);
                                d2 = format.parse(dateStop);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long diff = d2.getTime() - d1.getTime();

                            long diffHours = diff / (60 * 60 * 1000);
                            long diffMinutes = (diff / (60 * 1000))-diffHours*60;
                            int min = (int)(Math.round(diffMinutes/60 * 2) / 2.0f)*10;




                            String urlParameters = "ID=" + (String)parts[0]+"&duration="+diffHours+'.'+min;


                            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                            int postDataLength = postData.length;
                            String request = "https://venuscallipyge.nexttry.pl/wp-json/vcapi/getexcursion";
                            URL url = new URL(request);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoOutput(true);
                            conn.setInstanceFollowRedirects(false);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            conn.setRequestProperty("charset", "utf-8");
                            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                            conn.setUseCaches(false);
                            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                                wr.write(postData);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            InputStream in = new BufferedInputStream(conn.getInputStream());
                            String result2 = IOUtils.toString(in, "UTF-8");

                            jsonObject2 = new JSONObject(result2);
                            JSONObject JSONObject3 = (JSONObject) jsonObject2.get("excursion");
                            for(int j=0;j<(int)jsonObject2.get("count");j++) {
                                JSONObject helper = (JSONObject)JSONObject3.get("a"+j);
                                LinearLayout l = new LinearLayout(x);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                layoutParams.setMargins(0, 0, 0, 20);
                                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                layoutParams2.setMargins(30, 20, 30, 20);
                                TextView textview = new TextView(x);
                                textview.setText((String)helper.get("NAME"));
                                l.addView((View) textview,layoutParams2);
                                TextView textviewtime = new TextView(x);
                                textviewtime.setText((String)helper.get("CONTENT"));
                                l.addView((View) textviewtime,layoutParams2);

                                if(j%2==0) {
                                    l.setBackgroundResource(R.color.odd);
                                }else{
                                    l.setBackgroundResource(R.color.notodd);
                                }
                                l.setLayoutParams(layoutParams);
                                l.setOrientation(LinearLayout.VERTICAL);
                                linLayout.addView((View) l);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }





                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                if(i==0){
                    LinearLayout linLayout = (LinearLayout) findViewById(R.id.givemepower);


                    try {
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                        String dateStart =(String)port.get("TIME_IN_PORT");

                        String dateStop = (String)port.get("TIME_OUT_PORT");
                        Date d1 = null;
                        Date d2 = null;
                        try {
                            d1 = format.parse(dateStart);
                            d2 = format.parse(dateStop);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = d2.getTime() - d1.getTime();

                        long diffHours = diff / (60 * 60 * 1000);
                        long diffMinutes = (diff / (60 * 1000))-diffHours*60;
                        int min = (int)(Math.round(diffMinutes/60 * 2) / 2.0f)*10;




                        String urlParameters = "ID=" + (String)port.get("ID")+"&duration="+diffHours+'.'+min;

                        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                        int postDataLength = postData.length;
                        String request = "https://venuscallipyge.nexttry.pl/wp-json/vcapi/getexcursion";
                        URL url = new URL(request);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setInstanceFollowRedirects(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn.setRequestProperty("charset", "utf-8");
                        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                        conn.setUseCaches(false);
                        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                            wr.write(postData);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        String result2 = IOUtils.toString(in, "UTF-8");

                        jsonObject2 = new JSONObject(result2);
                        JSONObject JSONObject3 = (JSONObject) jsonObject2.get("excursion");
                        for(int j=0;j<(int)jsonObject2.get("count");j++) {
                            JSONObject helper = (JSONObject)JSONObject3.get("a"+j);
                            LinearLayout l = new LinearLayout(x);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            layoutParams.setMargins(0, 0, 0, 20);
                            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            layoutParams2.setMargins(30, 20, 30, 20);
                            TextView textview = new TextView(x);
                            textview.setText((String)helper.get("NAME"));
                            l.addView((View) textview,layoutParams2);
                            TextView textviewtime = new TextView(x);
                            textviewtime.setText((String)helper.get("CONTENT"));
                            l.addView((View) textviewtime,layoutParams2);

                            if(j%2==0) {
                            l.setBackgroundResource(R.color.odd);
                            }else{
                                l.setBackgroundResource(R.color.notodd);
                            }
                            l.setLayoutParams(layoutParams);
                            l.setOrientation(LinearLayout.VERTICAL);
                            linLayout.addView((View) l);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


}
