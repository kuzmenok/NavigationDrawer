package com.example.sok.navigationdrawer.activity;

import android.accounts.Account;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.listener.ServerAuthHandler;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates Google Sign-In, retrieval of user's profile information, and
 * offline server authorization.
 */
public class LoginActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener,
        CheckBox.OnCheckedChangeListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final int RC_SIGN_IN = 0;

    // Client ID for a web server that will receive the auth code and exchange it for a
    // refresh token if offline access is requested.
    private static final String WEB_CLIENT_ID = "AIzaSyCkSzMKbZRk2wJB6gbiKkThUvWzLI567y8";
    private static final String SERVER_CLIENT_ID = "818475893304-c5a8qe9b4dvau83fnqndd7sga6sgufdt.apps.googleusercontent.com";

    // Bundle keys for instance state
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a ConnectionResult?
    private boolean mIsResolving = false;

    // Should we resolve sign-in errors?
    private boolean mShouldResolve = false;

    // Separate object to handle the logic for Server Auth Code exchange, which is optional
    private ServerAuthHandler mServerAuthHandler;

    // Used to determine if we should ask for a server auth code when connecting the
    // GoogleApiClient.  False by default so that this sample can be used without configuring
    // a WEB_CLIENT_ID and SERVER_BASE_URL.
    private boolean mRequestServerAuthCode = false;

    private TextView mStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mStatus = (TextView) findViewById(R.id.sign_in_status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.revoke_access_button).setOnClickListener(this);

        // CheckBox listeners
        ((CheckBox) findViewById(R.id.request_auth_code_checkbox)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.has_token_checkbox)).setOnCheckedChangeListener(this);

        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }

        mServerAuthHandler = new ServerAuthHandler(this);

        mGoogleApiClient = buildGoogleApiClient();
    }

    GoogleApiClient buildGoogleApiClient() {
        // Build a GoogleApiClient with access to basic profile information.  We also request
        // the Plus API so we have access to the Plus.AccountApi functions, but note that we are
        // not actually requesting any Plus Scopes so we will not ask for or get access to the
        // user's Google+ Profile.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .addScope(new Scope(Scopes.PLUS_ME))
                .addScope(new Scope(Scopes.EMAIL))
                .requestServerAuthCode(SERVER_CLIENT_ID, mServerAuthHandler);

        if (mRequestServerAuthCode) {
            mServerAuthHandler.checkServerAuthConfiguration(WEB_CLIENT_ID);
            builder = builder.requestServerAuthCode(WEB_CLIENT_ID, mServerAuthHandler);
        }

        return builder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                // If the error resolution was successful we should continue
                // processing errors.  Otherwise, stop resolving.
                mShouldResolve = (resultCode == RESULT_OK);

                mIsResolving = false;
                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {
            // We only process button clicks when GoogleApiClient is not transitioning
            // between connected and not connected.
            switch (v.getId()) {
                case R.id.sign_in_button:
                    mStatus.setText(R.string.status_signing_in);
                    mShouldResolve = true;
                    mGoogleApiClient.connect();
                    break;
                case R.id.sign_out_button:
                    // Clear the default account so that Google Play services will not return an
                    // onConnected callback without interaction.
                    if (mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                    }
                    onSignedOut();
                    break;
                case R.id.revoke_access_button:
                    // Clear the default account and then revoke all permissions granted by the
                    // user. If the user wants to sign in again, they will have to accept
                    // the consent dialog.
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    if (status.isSuccess()) {
                                        onSignedOut();
                                    }
                                    // After we revoke permissions for the user with a
                                    // GoogleApiClient we must discard it and create a new one.
                                    mGoogleApiClient = buildGoogleApiClient();
                                    mGoogleApiClient.connect();
                                }
                            }
                    );
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.request_auth_code_checkbox:
                mRequestServerAuthCode = isChecked;
                buildGoogleApiClient();

                if (mRequestServerAuthCode) {
                    findViewById(R.id.layout_has_token).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.layout_has_token).setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.has_token_checkbox:
                mServerAuthHandler.setServerHasToken(true);
                break;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.i(TAG, "onConnected");

        // Update the user interface to reflect that the user is signed in.
        updateUI(true);

//        TODO execute task and (if success) start MainActivity
//        new GetIdTokenTask().execute();
        mStatus.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            }
        }, 1500);


        // Retrieve some profile information to personalize our app for the user.
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        mStatus.setText(String.format(
                getResources().getString(R.string.signed_in_as),
                currentUser.getDisplayName()));

        // Indicate that the sign in process is complete.
        mShouldResolve = false;
        mIsResolving = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in.
        // Refer to the javadoc for ConnectionResult to see possible error codes.
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());

        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // An API requested for GoogleApiClient is not available. The device's current
            // configuration might not be supported with the requested API or a required component
            // may not be installed, such as the Android Wear application. You may need to use a
            // second GoogleApiClient to manage the application's optional APIs.
            Log.w(TAG, "API Unavailable.");
        } else if (!mIsResolving && mShouldResolve) {
            // The user already clicked the sign in button, we should resolve errors until
            // success or they click cancel.
            resolveSignInError(result);
        } else {
            Log.w(TAG, "Already resolving.");
        }

        // Not connected, show signed-out UI
        onSignedOut();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
    }

    /**
     * Starts an appropriate intent or dialog for user interaction to resolve the current error
     * preventing the user from being signed in.  This is normally the account picker dialog or the
     * consent screen where the user approves the scopes you requested,
     */
    private void resolveSignInError(ConnectionResult result) {
        if (result.hasResolution()) {
            // Google play services provided a resolution
            try {
                // Attempt to resolve the Google Play Services connection error
                result.startResolutionForResult(this, RC_SIGN_IN);
                mIsResolving = true;
            } catch (SendIntentException e) {
                Log.e(TAG, "Sign in intent could not be sent.", e);

                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mGoogleApiClient.connect();
            }
        } else {
            // Google play services did not provide a resolution, display error message
            displayError(result.getErrorCode());
        }
    }

    private void displayError(int errorCode) {
        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            // Show the default Google Play services error dialog which may still start an intent
            // on our behalf if the user can resolve the issue.
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.w(TAG, "Google Play services resolution cancelled");
                            mShouldResolve = false;
                            mStatus.setText(R.string.status_signed_out);
                        }
                    }).show();
        } else {
            // No default Google Play Services error, display a Toast
            String errorMsg = "Google Play services error could not be resolved: " + errorCode;
            Log.e(TAG, errorMsg);

            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            mShouldResolve = false;
            mStatus.setText(R.string.status_signed_out);
        }
    }

    private void onSignedOut() {
        updateUI(false);
        mStatus.setText(R.string.status_signed_out);
    }

    private void updateUI(boolean isSignedIn) {
        findViewById(R.id.sign_in_button).setEnabled(!isSignedIn);
        findViewById(R.id.sign_out_button).setEnabled(isSignedIn);
        findViewById(R.id.revoke_access_button).setEnabled(isSignedIn);

        if (isSignedIn) {
            findViewById(R.id.layout_server_auth).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_server_auth).setVisibility(View.VISIBLE);
        }
    }

    private class GetIdTokenTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);

//            String sc = "https://www.googleapis.com/auth/userinfo.profile " +
//                    "https://www.googleapis.com/auth/userinfo.email " +
//                    "https://www.googleapis.com/auth/plus.login " +
//                    "https://www.googleapis.com/auth/plus.me";
//            String scopes = "oauth2:server:client_id:" + SERVER_CLIENT_ID + ":api_scope:" + sc; throws unknown error

            String scopes = "audience:server:client_id:" + SERVER_CLIENT_ID;
            try {
                String code = GoogleAuthUtil.getToken(getApplicationContext(), account, scopes);
                // Successfully retrieved ID Token
                boolean sendSuccessfully = sendCodeToServer(code);
                return sendSuccessfully ? code : null;
            } catch (Exception e) {
                Log.e(TAG, "Error retrieving ID token.", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "ID token: " + result);
            if (result != null) {
                //TODO show "success", then start LoginActivity
            } else {
                // There was some error getting the ID Token
                // ...
                Toast.makeText(getApplicationContext(), "ERROR (LoginActivity.GetIdTokenTask.onPostExecute)", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean sendCodeToServer(String code) {
        //TODO заменить на retrofit
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://www.projectB.com/auth/google_oauth2/callback"); // TODO set proper url

        try {
            List<BasicNameValuePair> nameValuePairs = new ArrayList<>(1);
            nameValuePairs.add(new BasicNameValuePair("idToken", code));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            final String responseBody = EntityUtils.toString(response.getEntity());
            //TODO if response SUCCESS - return true, if ERROR - return false;
            Log.i(TAG, "Signed in as: " + responseBody);
            return true;
        } catch (IOException e) {
            //TODO как-нибудь обработать
            Log.e(TAG, "Error sending ID token to backend.", e);
            return false;
        }
    }
}
