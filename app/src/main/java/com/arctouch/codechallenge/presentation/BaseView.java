package com.arctouch.codechallenge.presentation;

public interface BaseView {

    void showProgress();

    void hideProgress();

    void onError(String msg);

}
