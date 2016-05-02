package com.davicaetano.soccerbuddy.data.firebase;

import android.content.Context;
import android.util.Log;

import com.davicaetano.soccerbuddy.R;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by davicaetano on 5/1/16.
 */
public class FirebaseApi {
    private final String TAG = "FirebaseApi";

    private List<FirebaseCallback> firebaseCallbackList = new ArrayList<>();
    private Firebase firebase;
    private AuthData authData;
    private Context context;
    private Firebase.AuthStateListener authStateListener;

    @Inject
    public FirebaseApi(Context context){
        this.context = context;
        Firebase.setAndroidContext(this.context);
        String url = this.context.getResources().getString(R.string.firebase);
        this.firebase = new Firebase(url);

        authStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                FirebaseApi.this.authData = authData;
//                notifyFirebaseObservers();
            }
        };
        firebase.addAuthStateListener(authStateListener);
    }

    public Firebase getFirebase() {
        return firebase;
    }

    public void connectWithToken(String token, String provider){
        firebase.authWithOAuthToken(provider, token, new AuthResultHandler(provider));
    }

    /////////               OBSERVER - Application of Observer pattern.

    public void registerFirebaseObserver(FirebaseCallback callback){
        if(!firebaseCallbackList.contains(callback)){
            firebaseCallbackList.add(callback);
        }
    }

    public void unregisterFirebaseObserver(FirebaseCallback callback) {
        if(firebaseCallbackList.contains(callback)){
            firebaseCallbackList.remove(callback);

        }
    }

    private void notifyFirebaseObservers(){
        for(FirebaseCallback callback:firebaseCallbackList){
            callback.update(authData);
//            Log.v(TAG, "authData: " + authData.toString());
        }
    }

    /////////

    private class AuthResultHandler implements Firebase.AuthResultHandler {
        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            Log.v(TAG, provider + " auth successful");
            Log.v(TAG, "authData: " + authData.toString());
            FirebaseApi.this.authData = authData;
            notifyFirebaseObservers();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Log.v(TAG, provider + " auth fail");
            Log.v(TAG, firebaseError.toString());
//          TODO: call google signin to cancel token and get it again.
        }
    }
}
