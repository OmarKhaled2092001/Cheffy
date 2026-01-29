package com.example.cheffy.common.base;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class BasePresenter<V> {

    protected V view;
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void attachView(V view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
        compositeDisposable.clear();
    }

    protected boolean isViewAttached() {
        return view != null;
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}
