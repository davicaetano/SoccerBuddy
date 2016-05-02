package com.davicaetano.soccerbuddy.data.signIn;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.davicaetano.soccerbuddy.data.user.UserManager;
import com.davicaetano.soccerbuddy.ui.signin.SignInContract;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

import javax.inject.Inject;

public class GoogleSignIn implements SignInApi,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = "GoogleSignInApi";
    public static final int RC_SIGN_IN2 = 2;
    @Inject UserManager userManager;
    @Inject Context context;
    @Inject SignInContract.View view;

    private SignInContract.Presenter presenter;

    private GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;
    private String token;
    private boolean googleIntentInProgress;

    public GoogleSignIn(Context context, UserManager userManager, SignInContract.View view){
        this.context = context;
        this.userManager = userManager;
        this.view = view;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestScopes(new Scope(Scopes.PROFILE), new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity)view, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void getToken() {
        final String email = userManager.getUser().getEmail();
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            String errorMessage = null;
            @Override
            protected String doInBackground(Void... params) {
                token = null;
                try {
                    String scopes = "oauth2:" + Scopes.PROFILE;
                    token = GoogleAuthUtil.getToken(context, email, scopes);
                } catch (IOException e) {
                    Log.e(TAG,"IOException: " + e.getMessage());
                    errorMessage = "Network error: " + e.getMessage();
                }
                catch (UserRecoverableAuthException e) {
                    Log.e(TAG,"UserRecoverableAuthException: " + e);
                    errorMessage = "UserRecoverableAuthException error: " + e.getMessage();
                    if(!googleIntentInProgress) {
                        googleIntentInProgress = true;
                        ((FragmentActivity) view).startActivityForResult(e.getIntent(), RC_SIGN_IN2);
                    }
                } catch (GoogleAuthException e) {
                    Log.e(TAG,"GoogleAuthException: " + e);
                    errorMessage = "Error authenticating with Google: " + e.getMessage();
                }

                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                if(token != null){
                    presenter.tokenCallback(token);
                }else{

                }
            }
        };
        task.execute();
    }

    @Override
    public void clearToken() {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Log.e(TAG, "cleaning token 0:");
                    GoogleAuthUtil.clearToken(context, token);
                }catch (IOException e){
                    Log.e(TAG, "Error cleaning token 1: " + e.getMessage());
                }catch (GoogleAuthException e){
                    Log.e(TAG, "Error cleaning token 2: " + e.getMessage());
                }
                return null;
            }
        };
        task.execute();
    }

    public GoogleSignInOptions getGso() {
        return gso;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(TAG, "onConnectionFailed");
    }

    public void setPresenter(SignInContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
