package com.example.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private String greeting = "Hello from RxJava";

    /// we define an Observable of type String,
    /// that will be responsible for emitting data to us
    private Observable<String> myObservable;

    /// we define an Observer of type String,
    /// that will be responsible for getting data from the observer
    private Observer<String> myObserver;

    private TextView mainTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTextView = findViewById(R.id.mainTextView);

        /// we create an Observable with just one String value
        myObservable = Observable.just(greeting);

        /// we add this `subscribeOn` Scheduler (Schedulers.io()) to our Observable,
        /// which means, all the operations of our Observable will happen on this thread
        myObservable.subscribeOn(Schedulers.io());

        /// we add this `observeOn` (AndroidSchedulers.mainThread()) to our Observable,
        /// which means, our Observable can be observed on this thread
        myObservable.observeOn(AndroidSchedulers.mainThread());

        /// we create an Observer of type string
        myObserver = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d("RxDemo", "myObserver subscribed with " + d.toString());
            }

            @Override
            public void onNext(@NonNull String s) {
                mainTextView.setText(s);
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

        /// we attach the observer to the observable
        /// this is how our observer will start getting data emitted from our observable
        myObservable.subscribe(myObserver);

        /// here we are not setting any schedulers(threads), so it will all happen in our main thread
    }
}