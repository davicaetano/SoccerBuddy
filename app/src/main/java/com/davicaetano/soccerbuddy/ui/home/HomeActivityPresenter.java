package com.davicaetano.soccerbuddy.ui.home;

public class HomeActivityPresenter implements HomeActivityContract.Presenter{

    private HomeActivity homeActivity;

    public HomeActivityPresenter(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
    }
}
