package com.gacrnd.gcs.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

/**
 * IntentService和JobIntentService的好处：
 * 任务会在异步线程中有序执行，当第一步执行完成了之后才会执行下一步intent
 *
 *  区别：Android o以及以后不能使用IntentService了，可以使用JobIntentService代替
 *  使用IntentService需要使用Comtext.startService，而使用JobIntentService只需要将job放入任务队列中即可
 *  IntentService在有任务执行的时候，会立马执行，不管当时任务是否繁忙，而JobIntentService会将任务放入队列后
 *  视当时任务繁忙程度执行。如果当时系统资源吃紧，会延后执行任务
 *
 *  JobIntentService的使用注意事项：
 *  1.  申请权限  <uses-permission android:name="android.permission.WAKE_LOCK"/>
 *  2.  在service中申请android:permission="android.permission.BIND_JOB_SERVICE"权限
 *  3.  千万别重写onBind方法，否则可能收不到onHandleWork消息，如果重写了，需要return super.onBind()；
 *  4.  调用enqueueWork发送任务即可
 * @author Jack_Ou  created on 2020/10/3.
 */
public class MyIntentService extends JobIntentService {

    private static final String TAG = MyIntentService.class.getSimpleName();
    static final int JOB_ID = 1000;

    public MyIntentService() {

    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyIntentService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e(TAG, "Executing work: " + intent);
        String label = intent.getStringExtra("label");
        if (label == null) {
            label = intent.toString();
        }
        toast("Executing: " + label);
        for (int i = 0; i < 5; i++) {
            Log.i(TAG, "Running service " + (i + 1)
                    + "/5 @ " + SystemClock.elapsedRealtime());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        Log.e(TAG, "Completed service @ " + SystemClock.elapsedRealtime());
        Log.e(TAG, "label: " + label +", current thread :" + Thread.currentThread());
    }

    final Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(MyIntentService.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };
    
    void toast(final CharSequence text) {
        Message msg = mHandler.obtainMessage();
        msg.obj = text.toString();
        mHandler.sendMessage(msg);
    }

}
