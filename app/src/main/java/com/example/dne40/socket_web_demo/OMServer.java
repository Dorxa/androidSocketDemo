package com.example.dne40.socket_web_demo;

/**
 * Created by duoxuan on 2017/7/18.
 */



        import android.util.Log;

        import com.koushikdutta.async.http.WebSocket;

        import java.util.Timer;
        import java.util.TimerTask;

/**
 *
 * Created by duoxuan on 2017/6/30.
 */
class MyTimer {
    private Timer timer = new Timer();
    private int t;
    private boolean start;

    public MyTimer(int delayTime, final ScheduleCallback callback) {
        this.timer.schedule(new TimerTask() {
            public void run() {
                if(MyTimer.this.start) {
                    MyTimer.this.t = MyTimer.this.t + 1;
                    if(callback != null) {

                        try {
                            callback.onTimeChange(MyTimer.this.t);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }, 0L, (long)delayTime);
    }

    public void stop() {
        this.start = false;
    }

    public void start() {
        this.start = true;
    }

    public void cancel() {
        this.start = false;
        if(this.timer != null) {
            this.timer.cancel();
        }

    }

    public interface ScheduleCallback {
        void onTimeChange(int var1);
    }
}
class OMServer {

    private static final String TAG = "C_OpWebSock" ;
    private static  C_OpWebSock _instance = null;

    private static MyTimer myTask = new MyTimer(
            3000,
            new MyTimer.ScheduleCallback() {

                @Override
                public void onTimeChange(int arg0) {
                    if (_instance.isOpen())
                    {
                        _instance.client.ping("");
                        _instance.Send_MacCode(1000);

                    }else
                    {
                        _instance.Open();
                    }
                }

            }
    );


    static void  StartServer() {
//        C_OpWebSock.timeout = 30000;
        Log.i(TAG, "StartServer");
        C_OpWebSock.on_Connect_Fail = new C_OpWebSock.ConnectCallback() {
            public void onCompleted() {
                Log.i(TAG, "连接失败");

            }
        };

        C_OpWebSock.on_rece_string = new WebSocket.StringCallback() {
            public void onStringAvailable(String s) {

                Log.i(TAG, "onStringAvailable: " + s);

            }
        };

        C_OpWebSock.on_Succee_Open = new C_OpWebSock.ConnectCallback() {
            public void onCompleted() {
//                _instance.client.send(DaemonService.TAG);
            }
        };

        C_OpWebSock.on_Ping_back   = new WebSocket.PingCallback() {
            public void onPingReceived(String s) {

                Log.i(TAG, "on_Ping_back: " + s);

            }
        };
        _instance =  C_OpWebSock.getInstance();
        myTask.start();
    }
}

