package com.davicaetano.soccerbuddy.ui.home;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.davicaetano.soccerbuddy.CustomApplication;
import com.davicaetano.soccerbuddy.R;
import com.davicaetano.soccerbuddy.ui.BaseActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by davicaetano on 2/13/16.
 */
public class HomeActivity extends BaseActivity implements HomeContract.View {

    @Bind(R.id.bt_login) Button btLogin;
    @Bind(R.id.bt_disconnect) Button btDisconnect;
    @Bind(R.id.list_item) TextView listItem;

    @Inject HomeContract.Presenter presenter;

    @Override
    public void setupActivityComponent() {
        ((CustomApplication)getApplication()).getAppComponent().plus(new HomeModule(this)).inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_login)
    public void onBtConnect(){
        presenter.login();
    }
    @OnClick(R.id.bt_disconnect)
    public void onBtDisconnect(){
        presenter.disconnect();
    }
    @OnClick(R.id.button_get_list)
    public void onBtGetList(){
        presenter.getList();
    }


    @Override
    public void writeOnListItem(String line) {
        listItem.setText(listItem.getText()+"\n"+line);
    }
}
