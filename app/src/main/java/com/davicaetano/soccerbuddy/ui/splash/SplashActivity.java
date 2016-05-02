package com.davicaetano.soccerbuddy.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.davicaetano.soccerbuddy.CustomApplication;
import com.davicaetano.soccerbuddy.R;
import com.davicaetano.soccerbuddy.ui.BaseActivity;
import com.davicaetano.soccerbuddy.ui.home.HomeActivity;
import com.davicaetano.soccerbuddy.ui.signin.SignInActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity implements SplashContract.View {
    private final String TAG = "SplashActivity";
    public final int GOOGLE_SIGN_IN = 1;

    @Bind(R.id.progressBar) ProgressBar progressBar;

    @Inject
    SplashContract.Presenter presenter;

    @Override
    public void setupActivityComponent() {
        ((CustomApplication)getApplication()).getAppComponent().
                plus(new SplashModule(this)).
                inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter.hasCachedUser()){
            goesToHome();
        }else{
            goesToSignIn();
        }
    }

    @Override
    public void progressBar(boolean turnOn) {
        if(turnOn){
            progressBar.setVisibility(android.view.View.VISIBLE);
        }else{
            progressBar.setVisibility(android.view.View.GONE);
        }
    }

    @Override
    public void goesToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void goesToSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
