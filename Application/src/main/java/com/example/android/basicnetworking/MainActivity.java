/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.basicnetworking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

/**
 * Sample application demonstrating how to test whether a device is connected,
 * and if so, whether the connection happens to be wifi or mobile (it could be
 * something else).
 *
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */

/*
    장치가 연결되어 있는지 만약 연결되어 있다면 연결이 와이파이 또는 모바일 인지 여부를 테스트하는 방법을 보여주는 샘플 응용 프로그램 (그것은 다른 뭔가를 할 수있다).

    이 샘플은 log fragment(LogFragment)에 로그 출력을 표시 할 수있는 로깅 프레임 워크를 사용합니다.
*/

public class MainActivity extends FragmentActivity {

    public static final String TAG = "Basic Network Demo";
    // Whether there is a Wi-Fi connection.
    // Wi-Fi 연결이 있는지 여부
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    // 모바일 연결이 있는지 여부
    private static boolean mobileConnected = false;

    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    // 이벤트를 보여주는 프래그먼트를 참조, 그래서 우리는 필요에 따라 클리어 버튼을 누를 수 있다.
    private LogFragment mLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        // Initialize text fragment that displays intro text.
        // 인트로 텍스트를 표시하는 프래그먼트를 초기화
        SimpleTextFragment introFragment = (SimpleTextFragment)
                    getSupportFragmentManager().findFragmentById(R.id.intro_fragment);
        introFragment.setText(R.string.intro_message);
        introFragment.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);

        // Initialize the logging framework.
        // 로깅 프레임워크를 초기화
        initializeLogging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks TEST, display the connection status.
            // 유저가 TEST를 클릭할때, 연결상태 표시
            case R.id.test_action:
                checkNetworkConnection();
                return true;
            // Clear the log view fragment.
            // 로그 뷰 프래그먼트를 클리어
            case R.id.clear_action:
                mLogFragment.getLogView().setText("");
                return true;
        }
        return false;
    }

    /**
     * Check whether the device is connected, and if so, whether the connection
     * is wifi or mobile (it could be something else).
     */

    /*
        장치가 연결되어 있는지 여부를 확인하고, 만약 그렇다면, 와이파이 연결 또는 모바일 무선인지 확인한다. (그것은 다른 것이 될 수있다).
     */
    private void checkNetworkConnection() {
      // BEGIN_INCLUDE(connect)
      ConnectivityManager connMgr =
          (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
      if (activeInfo != null && activeInfo.isConnected()) {
          wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
          mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
          if(wifiConnected) {
              Log.i(TAG, getString(R.string.wifi_connection));
          } else if (mobileConnected){
              Log.i(TAG, getString(R.string.mobile_connection));
          }
      } else {
          Log.i(TAG, getString(R.string.no_wifi_or_mobile));
      }
      // END_INCLUDE(connect)
    }

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {

        // Using Log, front-end to the logging chain, emulates
        // android.util.log method signatures.

        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
    }
}
