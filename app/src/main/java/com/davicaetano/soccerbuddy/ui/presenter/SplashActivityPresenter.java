package com.davicaetano.soccerbuddy.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.davicaetano.soccerbuddy.CustomApplication;
import com.davicaetano.soccerbuddy.data.model.User;
import com.davicaetano.soccerbuddy.ui.HomeActivity;
import com.davicaetano.soccerbuddy.ui.SplashActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class SplashActivityPresenter implements GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = "SplashActivityPresenter";

    private SplashActivity splashActivity;
    private final Context context;


    private FacebookCallback<LoginResult> facebookCallBack;
    private AccessToken facebookAccessToken;

    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount acct;



    public SplashActivityPresenter(SplashActivity splashActivity, final Context context){
        this.splashActivity = splashActivity;
        this.context = context;


        facebookCallBack = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookAccessToken = loginResult.getAccessToken();
                Toast.makeText(context, "Logou - Facebook", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(context, "Logou - Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(context, "Logou - Error", Toast.LENGTH_LONG).show();
            }
        };

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(splashActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

    }

    public FacebookCallback<LoginResult> getFacebookCallBack() {
        return facebookCallBack;
    }

    //Google Login
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void handleSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
//            Toast.makeText(context, "Logou - " + acct.getEmail(), Toast.LENGTH_LONG).show();
            User user = new User();
            user.setEmail(acct.getEmail());
            user.setLoginMode(User.GOOGLE);
            CustomApplication.get(context).createUserComponent(user);
            splashActivity.startActivity(new Intent(splashActivity, HomeActivity.class));
            splashActivity.finish();
        } else {
            Toast.makeText(context, "Não logou - " + result.getStatus(), Toast.LENGTH_LONG).show();
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    public void googleLoginClick() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        splashActivity.startActivityForResult(signInIntent, splashActivity.GOOGLE_SIGN_IN);
    }

    public GoogleSignInOptions getGoogleSignInOptions() {
        return googleSignInOptions;
    }
}
