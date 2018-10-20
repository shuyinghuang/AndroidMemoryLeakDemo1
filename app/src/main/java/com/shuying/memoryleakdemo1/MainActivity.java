
package com.shuying.memoryleakdemo1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

public class MainActivity extends Activity {

  private HttpRequestHelper httpRequestHelper;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    View button = findViewById(R.id.async_work);
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startAsyncWork();
      }
    });

    httpRequestHelper = (HttpRequestHelper) getLastNonConfigurationInstance();
    if (httpRequestHelper == null) {
      httpRequestHelper = new HttpRequestHelper(button);
    }
  }

  @Override public Object onRetainNonConfigurationInstance() {
    return httpRequestHelper;
  }

  @SuppressLint("StaticFieldLeak")
  void startAsyncWork() {
    // This runnable is an anonymous class and therefore has a hidden reference to the outer
    // class MainActivity. If the activity gets destroyed before the thread finishes (e.g. rotation),
    // the activity instance will leak.
    Runnable work = new Runnable() {
      @Override public void run() {
        // Do some slow work in background
        SystemClock.sleep(2000000);
      }
    };
    new Thread(work).start();
  }
}


