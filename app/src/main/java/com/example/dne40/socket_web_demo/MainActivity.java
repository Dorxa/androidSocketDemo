
package com.example.dne40.socket_web_demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {




    /**
     * 主 变量
     */

    // 主线程Handler
    // 用于将从服务器获取的消息显示出来
    private Handler mMainHandler;




    // 线程池
    // 为了方便展示,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;

    /**
     * 接收服务器消息 变量
     */
    // 输入流对象
    InputStream is;

    // 输入流读取器对象
    InputStreamReader isr ;
    BufferedReader br ;

    // 接收服务器发送过来的消息
    String response;


    /**
     * 发送消息到服务器 变量
     */
    // 输出流对象
    OutputStream outputStream;

    /**
     * 按钮 变量
     */

    // 连接 断开连接 发送数据到服务器 的按钮变量
    private Button btnConnect, btnDisconnect, btnSend, btncheck, btntimer, btnDownload;

    // 显示接收服务器消息 按钮
    private TextView Receive,receive_message;

    // 输入需要发送的消息 输入框
    private EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * 初始化操作
         */

        // 初始化所有按钮
        btnConnect = (Button) findViewById(R.id.connect);
        btnDisconnect = (Button) findViewById(R.id.disconnect);
        btncheck = (Button) findViewById(R.id.check);
        btntimer = (Button) findViewById(R.id.timerbu);
        btnDownload = (Button) findViewById(R.id.downFile);

        btnSend = (Button) findViewById(R.id.send);
        mEdit = (EditText) findViewById(R.id.edit);
        receive_message = (TextView) findViewById(R.id.receive_message);


        // 初始化线程池
        mThreadPool = Executors.newCachedThreadPool();


        // 实例化主线程,用于更新接收过来的消息
        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        receive_message.setText(response);
                        break;
                }
            }
        };

        /**
         * 创建客户端 & 服务器的连接
         */
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!C_OpWebSock.getInstance().isOpen())
                {
                    btnDisconnect.setEnabled(false);
                    C_OpWebSock.getInstance().Open();
                }else
                {
                    C_OpWebSock.getInstance().client.send("Main Send");
                    btnConnect.setEnabled(false);
                    btnDisconnect.setEnabled(true);
                }
            }
        });


        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(C_OpWebSock.getInstance());
                System.out.println(C_OpWebSock.getInstance().client);

                if (C_OpWebSock.getInstance().client != null) {

                    System.out.println(C_OpWebSock.getInstance().client.getServer());
                    System.out.println(C_OpWebSock.getInstance().client.getServer().isRunning());
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String versionUrl ="http://192.168.18.130:8000/funcap/download" ;
                String versionName = "dd.txt";
                Context mContext = v.getContext();
                System.out.println("C_OpWebSock + " +  "to start down-----------");

                TestTask.downloadFile(versionUrl, versionName, mContext).start();
            }
        });


        btntimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TestTask.timer != null)
                {
                    TestTask.timer.cancel(); TestTask.timer = null;
                    return;
                }
                System.out.println("start test task");
                TestTask.Start(new  java.util.TimerTask(){
                    boolean _sta = false;
                    public void run(){
                        System.out.println("C_OpWebSock + " +  Thread.currentThread().getId());

                        if (!C_OpWebSock.getInstance().isOpen())
                        {
                            C_OpWebSock.getInstance().Open();
                        }
                        else
                        {
                            C_OpWebSock.getInstance().Close();
                        }
                    }
            });
            }});


        /**
         * 发送消息 给 服务器
         */
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (C_OpWebSock.getInstance().client != null) {
                    C_OpWebSock.getInstance().client.send(
                            (mEdit.getText().toString()+"\n"));

                }
                System.out.println("C_OpWebSock + " +  Thread.currentThread().getId());

            }
        });

        /**
         * 断开客户端 & 服务器的连接
         */
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (C_OpWebSock.getInstance().isOpen()) {

                    C_OpWebSock.getInstance().Close();

                }else
                {
                    btnConnect.setEnabled(true);
                }

            }
        });

    }
}
