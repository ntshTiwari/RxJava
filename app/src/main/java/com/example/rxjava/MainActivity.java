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

    /// we define an Observer of type String,
    /// that will be responsible for getting data from the observer
    // private Observer<String> myObserver;

    /// we change our Observer to a DisposableObserver type
    private DisposableObserver<String> myObserver, myObserver2;

    /// a CompositeDisposable, can help us dispose a stream of Observers,
    /// this way, we will not have to call dispose() methods on all Observers one by one
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    /// we create our disposable, this will help us to dispose all the subscriptions when this activity is disposed
    // private Disposable disposable;
    /// we don't need a Disposable now, as our Observer will be a DisposableObserver

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
//        myObserver = new Observer<String>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//                disposable = d;
//                Log.d("RxDemo", "myObserver subscribed with " + d.toString());
//            }
//
//            @Override
//            public void onNext(@NonNull String s) {
//                mainTextView.setText(s);
//                Log.d("RxDemo", "myObserver onNext called " + s);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                Log.d("RxDemo", "myObserver onError called");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d("RxDemo", "myObserver onComplete called");
//            }
//        };

        /// we declare a new DisposableObserver
        /// in a DisposableObserver, we don't get an `onSubscribe` method, as
        /// we get the Disposable directly from `DisposableObserver`
        myObserver = new DisposableObserver<String>() {
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
        /// whenever we attach an Observer, we add it to out compositeDisposable, to dispose it later
        compositeDisposable.add(myObserver);

        /// we create one more Observer
        myObserver2 = new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {
                Toast.makeText(MainActivity.this, "Observer 2 " + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        myObservable.subscribe(myObserver2);
        compositeDisposable.add(myObserver2);

        /// here we are not setting any schedulers(threads), so it will all happen in our main thread
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /// as our disposable has the subscription in it,
        /// calling dispose on it will remove the subscription
        // disposable.dispose();

        /// now, we can directly call the dispose method of the `DisposableObserver`
        // myObserver.dispose();

        compositeDisposable.clear();
    }
}