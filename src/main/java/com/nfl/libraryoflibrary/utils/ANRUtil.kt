package com.nfl.libraryoflibrary.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.MessageQueue
import android.util.Log

class ANRUtil {

    companion object {
        const val TAG = "ANRUtil"
        fun watchANR(context: Context, onProcessErrorCallback: OnProcessErrorCallback?) {
            val idleHandler = object : MessageQueue.IdleHandler {
                private val handler = Handler(Looper.getMainLooper())
                private var startTime = System.currentTimeMillis()
                private var endTime = System.currentTimeMillis()
                private val callback = Runnable {
                    Log.d(TAG, "ANR callback message")
                }
                private val activityManager =
                    context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                private var mainProcessKilled = false
                private var anrThreadStopped = false

                init {
                    Thread {
                        while (!mainProcessKilled) {
                            Thread.sleep(4 * 1000)
                            if (!anrThreadStopped) {
                                if (System.currentTimeMillis() - startTime > 5 * 1000) {
                                    // 说明 startTime 至少 5s 没更新了，即 queueIdle 至少 5s 未执行了
                                    // processesInErrorState 中会存储不同类型的 anr
                                    activityManager.processesInErrorState?.apply {
                                        if (this.size > 0) {
                                            onProcessErrorCallback?.onError(this)
                                            // 开启其他进程来记录 ANR
                                            // context.startActivity(Intent(context, ANRActivity::class.java))
                                            anrThreadStopped = true
                                            this.forEach {
                                                Log.e(TAG, "error state:${it.longMsg}")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }.start()
                }

                override fun queueIdle(): Boolean {
                    anrThreadStopped = false
                    handler.removeCallbacks(callback)
                    endTime = System.currentTimeMillis()
                    Log.i(TAG, "发送检测 ANR 的消息")
                    handler.postDelayed(callback, 4 * 1000)
                    startTime = endTime
                    return true
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Looper.getMainLooper().queue.addIdleHandler(idleHandler)
            }
        }
    }
}

interface OnProcessErrorCallback {
    /**
     * 检测到 app 发生 anr 时就会触发
     */
    fun onError(errors: List<ActivityManager.ProcessErrorStateInfo>)
}