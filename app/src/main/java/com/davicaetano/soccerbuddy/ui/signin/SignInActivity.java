package com.davicaetano.soccerbuddy.ui.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.davicaetano.soccerbuddy.CustomApplication;
import com.davicaetano.soccerbuddy.R;
import com.davicaetano.soccerbuddy.ui.BaseActivity;
import com.google.android.gms.common.SignInButton;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity implements SignInContract.View {
    private final String TAG = "SignInActivity";
    @Inject SignInContract.Presenter presenter;

    @Bind(R.id.write) Button write;
    @Bind(R.id.signin_button) SignInButton signInButton;

    private ProgressDialog progressDialog;

    @Override
    public void setupActivityComponent() {
        ((CustomApplication)getApplication()).getAppComponent().
                plus(new SignInModule(this)).
                inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        ButterKnife.bind(this);

        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(presenter.getScopeArray());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Authenticating");
        progressDialog.setCancelable(false);

    }

    @OnClick(R.id.signin_button)
    public void onClickSignInButton(){
        presenter.onClickGoogleSignIn();
    }

    @OnClick(R.id.write)
    public void onClickWrite(){
//        firebase.child("users").child(authData.getUid()).child("name").setValue("Davi");
//        firebase.child("users").child(authData.getUid()).child("email").setValue(userEmail);
////        firebase.child("users").updateChildren(new);
////        firebase.setValue("teste123");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void changeProgressDialog(boolean on) {
        if(on) {
            progressDialog.show();
        }else{
            progressDialog.hide();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
