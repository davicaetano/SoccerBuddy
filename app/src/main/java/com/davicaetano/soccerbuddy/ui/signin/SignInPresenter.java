package com.davicaetano.soccerbuddy.ui.signin;

import android.content.Intent;
import android.util.Log;

import com.davicaetano.soccerbuddy.data.firebase.FirebaseApi;
import com.davicaetano.soccerbuddy.data.firebase.FirebaseCallback;
import com.davicaetano.soccerbuddy.data.signIn.GoogleSignIn;
import com.davicaetano.soccerbuddy.data.user.User;
import com.davicaetano.soccerbuddy.data.user.UserManager;
import com.davicaetano.soccerbuddy.ui.BaseActivity;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Scope;

import javax.inject.Inject;

public class SignInPresenter implements SignInContract.Presenter, FirebaseCallback{
    private static final String TAG = "SignInPresenter";
    private static final int RC_SIGN_IN = 1;
    private static final int RC_SIGN_IN2 = 2;

    @Inject SignInContract.View view;
    @Inject UserManager userManager;
    @Inject FirebaseApi firebaseApi;
    @Inject GoogleSignIn googleSignIn;

    @Inject
    public SignInPresenter(SignInContract.View view, UserManager userManager, GoogleSignIn googleSignIn, FirebaseApi firebaseApi){
        this.view = view;
        this.userManager = userManager;
        this.googleSignIn = googleSignIn;
        this.firebaseApi = firebaseApi;
        googleSignIn.setPresenter(this);
        firebaseApi.registerFirebaseObserver(this);
    }

    @Override
    public void onClickGoogleSignIn() {
        view.changeProgressDialog(true);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleSignIn.getGoogleApiClient());
        ((BaseActivity)view).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        view.changeProgressDialog(false);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount acct = result.getSignInAccount();
                User user = new User();
                user.setEmail(acct.getEmail());
                user.setName(acct.getDisplayName());
                user.setLoginMode(2);
                userManager.recordLocalUser(user);
                view.changeProgressDialog(true);
                googleSignIn.getToken();
            }else{
                Log.v(TAG, "result.isSuccess = false");
            }
        }
        if(requestCode == RC_SIGN_IN2){
            googleSignIn.getToken();
        }
    }

//    @Override//This methos is used by google stuff to create its things.
//    public SignInContract.View getView() {
//        return view;
//    }

    @Override//The firebase uses this method to callback after it connects or not.
    public void update(AuthData authData) {
        view.changeProgressDialog(false);
        if(authData == null){
            view.showMessage("Error authenticating. Try again.");
        }else{
            User user = userManager.getUser();
            user.setID(authData.getUid());
            userManager.recordLocalUser(user);
//            firebaseApi.getFirebase().
            firebaseApi.getFirebase().child("users").child(user.getID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v(TAG, "onDataChange: ");
                    Log.v(TAG, "exists: " + dataSnapshot.exists());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.v(TAG, "onCancelled: ");
                    Log.v(TAG, firebaseError.toString());

                }
            });
        }
    }

    @Override//This method receives the token from google and sends it to firebase.
    public void tokenCallback(String token) {
        firebaseApi.connectWithToken(token, "google");
    }

    @Override//This method gets the scopes form google and send it to the view. This is necessary to create the google sign in button.
    public Scope[] getScopeArray() {
        return googleSignIn.getGso().getScopeArray();
    }
}
