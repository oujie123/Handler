package com.gacrnd.gcs.handler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private HandlerThread handlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThread = new HandlerThread("JackOu Thread");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.e("JackOu","子线程中处理:" + msg.what);
                return false;
            }
        });
        Log.e("JackOu","主线程ID：" + Thread.currentThread());
        Log.e("JackOu","主线程ID：" + handlerThread.getThreadId());
        handler.sendEmptyMessage(1);
        Message m = handler.obtainMessage();
        m.what = 2;
        handler.sendMessage(m);
        Message m2 = Message.obtain();
        m2.what = 3;
        handler.sendMessage(m2);

        Intent intent = new Intent();
        intent.putExtra("label","1");
        MyIntentService.enqueueWork(this,intent);

        intent.putExtra("label","2");
        MyIntentService.enqueueWork(this,intent);

        intent.putExtra("label","3");
        MyIntentService.enqueueWork(this,intent);
    }
}
