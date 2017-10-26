
package com.example.dne40.socket_web_demo;

import android.util.Log;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.WebSocket;




/**
 *
 * Created by DnE40 on 2017/6/29.
 */


public class C_OpWebSock {

    public static int timeout = AsyncHttpRequest.DEFAULT_TIMEOUT;
//    private final String uri = "ws://39.108.60.80:8000/Funcap/wscommunicate";
    private final String uri = "ws://192.168.18.119:9999/AppMonitorChat";
    //    private final String uri = "ws://192.168.18.130:8000/Funcap/wscommunicate";
//    private final String uri = "ws://192.168.18.130:9000";
    private static final C_OpWebSock single = new C_OpWebSock();

    //静态工厂方法
    public static C_OpWebSock getInstance() {
        return single;
    }


    private static final String TAG = "C_OpWebSock";

    private  long startConning = 0;
    public static WebSocket.StringCallback on_rece_string = null;

    public static WebSocket.PingCallback on_Ping_back = null;

    private static DataCallback on_data_recd = null;

    public static ConnectCallback on_Connect_Fail = null;

    public static ConnectCallback on_Succee_Open = null;

    public static CompletedCallback when_Close = null;


    public interface ConnectCallback {
        void onCompleted();
    }

    private final AsyncHttpClient.WebSocketConnectCallback  init_callback
            =new AsyncHttpClient.WebSocketConnectCallback() {

        @Override
        public void onCompleted(Exception ex, WebSocket webSocket) {

            if (ex != null) {
                Log.i(TAG, "has Error: " + startConning);
                if (on_Connect_Fail != null)
                {
                    on_Connect_Fail.onCompleted();
                }
                ex.printStackTrace();
                return;
            }

            client = webSocket;
            webSocket.setStringCallback(on_rece_string);

            webSocket.setDataCallback(on_data_recd);

            webSocket.setPingCallback(on_Ping_back);

            webSocket.setClosedCallback(when_Close);

            if (on_Succee_Open != null)
            {
                on_Succee_Open.onCompleted();
            }
        }
    };


    public WebSocket  client = null;
    public void Open()
    {
        if (java.lang.System.currentTimeMillis() - startConning > timeout)
            if (client == null || !client.isOpen() )
            {
                startConning =  java.lang.System.currentTimeMillis();

                AsyncHttpGet get = new AsyncHttpGet(uri.replace("ws://", "http://").replace("wss://", "https://"));
                get.setTimeout(timeout);

                AsyncHttpClient.getDefaultInstance().websocket(
                        get ,// webSocket地址
                        "",//
                        init_callback
                );
            }
    }

    public  boolean isOpen()
    {
        return !(client == null || !client.isOpen());
    }

    public  void Send_MacCode (int _em)
    {
        client.send("{\"Machine_code\":" + _em + "}"  );
    }

    public void Close()
    {
        try
        {
            if (client != null  )
            {
                client.close();
            }
            client = null;
        }
        finally {
            client = null;
        }
    }
}

