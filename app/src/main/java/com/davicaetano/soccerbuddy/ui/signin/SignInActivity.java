package com.davicaetano.soccerbuddy.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.davicaetano.soccerbuddy.CustomApplication;
import com.davicaetano.soccerbuddy.R;
import com.davicaetano.soccerbuddy.data.user.User;
import com.davicaetano.soccerbuddy.ui.BaseActivity;
import com.davicaetano.soccerbuddy.ui.home.HomeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity implements SignInContract.View,
        GoogleApiClient.OnConnectionFailedListener{
    private final String TAG = "SignInActivity";
    public final int GOOGLE_SIGN_IN = 1;

    @Inject
    SignInContract.Presenter presenter;

    //Google login
    private GoogleSignInAccount acct;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    @Bind(R.id.signin_button) SignInButton googleSignInButton;

    //Facebook login
    private FacebookCallback<LoginResult> facebookCallBack;
    private AccessToken facebookAccessToken;
    private CallbackManager callbackManager;
    @Bind(R.id.facebook_login_button) LoginButton facebookLoginButton;

    @Override
    public void setupActivityComponent() {
        ((CustomApplication)getApplication()).getAppComponent().
                plus(new SignInModule(this)).
                inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.sign_in);
        ButterKnife.bind(this);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setScopes(googleSignInOptions.getScopeArray());

        facebookCallBack = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookAccessToken = loginResult.getAccessToken();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        };
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.registerCallback(callbackManager, facebookCallBack);
    }

    @OnClick(R.id.signin_button)
    public void signInButtonOnClick(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            handleSignInResult(data);
        }
    }

    public void handleSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
            User user = new User();
            user.setEmail(acct.getEmail());
            user.setEmail(acct.getDisplayName());
            user.setPictureUrl(acct.getPhotoUrl().toString());
            user.setLoginMode(User.GOOGLE);
            presenter.recordUser(user);
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Login not authorized by Google", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
