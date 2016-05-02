package com.davicaetano.soccerbuddy.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davicaetano.soccerbuddy.AppComponent;
import com.davicaetano.soccerbuddy.CustomApplication;

/**
 * Created by davicaetano on 2/13/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();
    }

    public abstract void setupActivityComponent();

    public AppComponent getAppComponent(){
        return ((CustomApplication)getApplication()).getAppComponent();
    }
}
