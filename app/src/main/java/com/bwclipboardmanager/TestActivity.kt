package com.bwclipboardmanager

import android.content.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class TestActivity : AppCompatActivity() {

    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private val clipboardReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            getData(editText2)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(clipboardReceiver, IntentFilter("com.bwclipboradmanaer"))
        setContentView(R.layout.activity_test)
        editText1 = findViewById(R.id.editText1)
        editText2 = findViewById(R.id.editText2)
        startService(Intent(this, TestService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(clipboardReceiver)
        stopService(Intent(this, TestService::class.java))
    }

    fun copyData(view: View) {
        val clipData = ClipData.newPlainText("tag" + Random.nextInt(), editText1.text.toString())
        ClipboardManagerUtil.instance.setPrimaryClip(this, clipData)
        Log.d("BWClipboardManager", "复制数据到剪贴板->" + clipData.getItemAt(0).text)
    }

    fun getData(view: View) {
        val clipData = ClipboardManagerUtil.instance.getPrimaryClip(this)
        editText2.setText(clipData?.getItemAt(0)?.text)
        Log.d("BWClipboardManager", "获取剪贴板数据->" + clipData?.getItemAt(0)?.text)
    }

    fun clearData(view: View) {
        ClipboardManagerUtil.instance.clearPrimaryClip(this)
    }
}