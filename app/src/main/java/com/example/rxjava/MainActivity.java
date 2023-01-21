package com.example.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private String greeting = "Hello from RxJava";

    /// we define an Observable of type String,
    /// that will be responsible for emitting data to us
    private Observable<String> myObservable;

    /// a CompositeDisposable, can help us dispose a stream of Observers,
    /// this way, we will not have to call dispose() methods on all Observers one by one
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// we pass an array of values,
        /// it will returned one by one, in our `onNext()` param
        myObservable = Observable.just("Item 1", "Item 2", "Item 3");

        /// subscribeWith returns us a Disposable, so, we can directly add it to our compositeDisposable
        compositeDisposable.add(
                myObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getObserver())
        );
    }

    private DisposableObserver<String> getObserver() {
        DisposableObserver<String> observer = new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {
                Log.d("RxDemo", "myObserver onNext called " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("RxDemo", "myObserver onError called");
            }

            @Override
            public void onComplete() {
                Log.d("RxDemo", "myObserver onComplete called");
            }
        };

        return observer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }
}