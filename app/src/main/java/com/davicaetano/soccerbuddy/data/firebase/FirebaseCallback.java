package com.davicaetano.soccerbuddy.data.firebase;

import com.firebase.client.AuthData;

public interface FirebaseCallback {
    void update(AuthData authData);
}
