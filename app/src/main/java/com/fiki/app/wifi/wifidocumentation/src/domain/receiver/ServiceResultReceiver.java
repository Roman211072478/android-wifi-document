package com.fiki.app.wifi.wifidocumentation.src.domain.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by root on 2017/10/16.
 */

public class ServiceResultReceiver extends ResultReceiver {
    private Receiver myReceiver;

    public ServiceResultReceiver(Handler handler) {
        super(handler);
        // TODO Auto-generated constructor stub
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    public void setReceiver(Receiver receiver) {
        myReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (myReceiver != null) {
            myReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
