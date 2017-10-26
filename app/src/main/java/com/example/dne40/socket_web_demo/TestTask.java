package com.example.dne40.socket_web_demo;

import android.content.Context;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.Timer;

/**
 * Created by DnE40 on 2017/6/29.
 */

public class TestTask {
    public  static Timer timer;
    public static void Start(java.util.TimerTask tast){
        timer = new Timer();
        timer.schedule(tast, 100, 200);//在1秒后执行此任务,每次间隔2秒执行一次,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.
    }

    static class MyTask extends java.util.TimerTask{
        public void run(){
            System.out.println("________");
        }
    }

    public static BaseDownloadTask  downloadFile(String url, String path,Context mContext)
    {
        path = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "tmpdir1";
        boolean isDir = false;
        isDir =true;
     return    FileDownloader.getImpl().create(url)
                .setPath(path, isDir)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener() {


                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        Log.i("C_OpWebSock", "updatePending ");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        Log.i("C_OpWebSock", String.format("%dKB/s",  task.getSpeed()) + String.format("sofar: %d total: %d", soFarBytes, totalBytes));

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);

                         Log.i("C_OpWebSock",String.format("error %d %s",  task.getSpeed() , e));
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        Log.i("C_OpWebSock",String.format("connected %s",  task.getFilename()));

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i("C_OpWebSock",String.format("paused %s",  task.getSpeed()));

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        Log.i("C_OpWebSock",String.format("completed %s",  String.format("completed %s",task.getTargetFilePath())));


                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                        Log.i("C_OpWebSock","warn");

                    }
                });
    }
}


