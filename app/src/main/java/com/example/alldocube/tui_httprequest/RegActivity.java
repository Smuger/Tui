package com.example.alldocube.tui_httprequest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
public class RegActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    Context x = this;
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mRePasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {




    super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        TextView login = (TextView) findViewById(R.id.login);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
@Override
public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

        try {
        attemptReg();
        } catch (Exception e) {
        e.printStackTrace();
        }
        return true;
        }
        return false;
        }
        });
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(x, LoginActivity.class);
                startActivity(i);
            }
        });

        Button mEmailRegButton = (Button) findViewById(R.id.email_reg_button);
        mEmailRegButton.setOnClickListener(new OnClickListener() {
@Override
public void onClick(View view) {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Log.d("asd",mPasswordView.getText().toString());
        String pass1=mPasswordView.getText().toString();
        mRePasswordView = (EditText) findViewById(R.id.repassword);
    String pass2=mRePasswordView.getText().toString();
    Log.d("asd",(pass1.equals( pass2))+"");
        if( pass1.equals( pass2)){
            if(mPasswordView.length() == 0 || mRePasswordView.length() == 0){
                Toast.makeText(getApplicationContext(), "Enter both passwords!.",
                        Toast.LENGTH_SHORT).show();
            } else if(mEmailView.length() == 0){
                Toast.makeText(getApplicationContext(), "Enter valid email.",
                        Toast.LENGTH_SHORT).show();
            } else {
                try {
                        attemptReg();
                    Toast.makeText(x, "Registered successfully.",
                            Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                        e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "Passwords doesn't match.",
                    Toast.LENGTH_SHORT).show();
            Log.d("test","a chuj jednak nie działa");
        }
        }
        });




        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        }

private void populateAutoComplete() {
        if (!mayRequestContacts()) {
        return;
        }

        getLoaderManager().initLoader(0, null, this);
        }

private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
        return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
        Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
        .setAction(android.R.string.ok, new View.OnClickListener() {
@Override
@TargetApi(Build.VERSION_CODES.M)
public void onClick(View v) {
        requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        });
        } else {
        requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
        }

/**
 * Callback received when a permissions request has been completed.
 */
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
@NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        populateAutoComplete();
        }
        }
        }


/**
 * Attempts to sign in or register the account specified by the login form.
 * If there are form errors (invalid email, missing fields, etc.), the
 * errors are presented and no actual login attempt is made.
 */
private void attemptReg() throws IOException, JSONException {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        String urlParameters  = "username="+email+"&password="+password;
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = "https://venuscallipyge.nexttry.pl/wp-json/vcapi/register";
        URL    url            = new URL( request );
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
        Log.d("test",result);
        /* tutaj sprawdzamy zawartosc json czy zalogowalo czy nie*/

        }

private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
        }

private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
        }

/**
 * Shows the progress UI and hides the login form.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
@Override
public void onAnimationEnd(Animator animation) {
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
@Override
public void onAnimationEnd(Animator animation) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        });
        } else {
        // The ViewPropertyAnimator APIs are not available, so simply show
        // and hide the relevant UI components.
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        }

@Override
public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
        // Retrieve data rows for the device user's 'profile' contact.
        Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

        // Select only email addresses.
        ContactsContract.Contacts.Data.MIMETYPE +
        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
        .CONTENT_ITEM_TYPE},

        // Show primary email addresses first. Note that there won't be
        // a primary email address if the user hasn't specified one.
        ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
        }

@Override
public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
        emails.add(cursor.getString(ProfileQuery.ADDRESS));
        cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
        }

@Override
public void onLoaderReset(Loader<Cursor> cursorLoader) {

        }

private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
        new ArrayAdapter<>(RegActivity.this,
        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
        }


private interface ProfileQuery {
    String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
    };

    int ADDRESS = 0;
    int IS_PRIMARY = 1;
}

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */


}
