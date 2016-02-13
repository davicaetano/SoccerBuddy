package com.davicaetano.soccerbuddy.ui;

import android.content.Intent;
import android.os.Bundle;

import com.davicaetano.soccerbuddy.CustomApplication;
import com.davicaetano.soccerbuddy.R;
import com.davicaetano.soccerbuddy.ui.modules.SplashActivityModule;
import com.davicaetano.soccerbuddy.ui.presenter.SplashActivityPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity{
    private final String TAG = "SplashActivity";
    public final int GOOGLE_SIGN_IN = 1;


    private CallbackManager callbackManager;
    @Bind(R.id.signin_button) SignInButton googleSignInButton;
    @Bind(R.id.facebook_login_button) LoginButton facebookLoginButton;

    @Inject
    SplashActivityPresenter presenter;

    @Override
    public void setupActivityComponent() {
        ((CustomApplication)getApplication()).getAppComponent().
                plus(new SplashActivityModule(this)).
                inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.screen_splash);
        ButterKnife.bind(this);

        facebookLoginButton.registerCallback(callbackManager, presenter.getFacebookCallBack());

        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setScopes(presenter.getGoogleSignInOptions().getScopeArray());
    }

    @OnClick(R.id.signin_button)
    public void signInButtonOnClick(){
        presenter.googleLoginClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            presenter.handleSignInResult(data);
        }
    }
}
