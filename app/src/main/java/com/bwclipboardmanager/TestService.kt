package com.bwclipboardmanager

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class TestService : Service() {

    private val clipListener = object : OnPrimaryClipChangedListener {
        override fun onPrimaryClipChanged() {
            Log.d("BWClipboardManager","剪贴板发生改变了")
            sendBroadcast(Intent().setAction("com.bwclipboradmanaer"))
        }
    }

    override fun onCreate() {
        super.onCreate()
        ClipboardManagerUtil.instance.addPrimaryClipChangedListener(this, clipListener)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        ClipboardManagerUtil.instance.removePrimaryClipChangedListener(this, clipListener)
    }
}