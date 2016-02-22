package com.davicaetano.soccerbuddy.ui.home;

public class HomePresenter implements HomeActivityContract.Presenter{

    private HomeActivity homeActivity;

    public HomePresenter(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
    }
}
