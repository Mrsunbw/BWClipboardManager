package com.bwclipboardmanager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class ClipboardManagerUtil private constructor() {

    private val mPrimaryClipChangedListeners: MutableList<OnPrimaryClipChangedListener> =
        ArrayList()

    private val clipChangeListener =
        ClipboardManager.OnPrimaryClipChangedListener { notifyPrimaryClipChanged() }

    /**
     * 从剪贴板取数据
     */
    fun getPrimaryClip(context: Context): ClipData? {
        try {
            val mClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            return mClipboardManager.primaryClip
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 向剪贴板存数据
     */
    fun setPrimaryClip(context: Context, clip: ClipData) {
        try {
            val mClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            mClipboardManager.setPrimaryClip(clip)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @RequiresApi()
    fun clearPrimaryClip(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val mClipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                mClipboardManager.clearPrimaryClip()
            } else {
                setPrimaryClip(context, ClipData.newPlainText("", ""))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 剪贴板是否有数据
     */
    fun hasPrimaryClip(context: Context): Boolean {
        try {
            val mClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            mClipboardManager.hasPrimaryClip()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 添加剪贴板监听
     */
    @Synchronized
    fun addPrimaryClipChangedListener(context: Context, listener: OnPrimaryClipChangedListener) {
        try {
            mPrimaryClipChangedListeners.add(listener)
            val mClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if (mPrimaryClipChangedListeners.size == 1) { //保证ClipboardManager只添加一次监听
                mClipboardManager.addPrimaryClipChangedListener(clipChangeListener)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 移除剪贴板监听
     */
    @Synchronized
    fun removePrimaryClipChangedListener(context: Context, listener: OnPrimaryClipChangedListener) {
        try {
            mPrimaryClipChangedListeners.remove(listener)
            val mClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if (mPrimaryClipChangedListeners.size == 0) { //保证ClipboardManager只移除一次监听
                mClipboardManager.removePrimaryClipChangedListener(clipChangeListener)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun notifyPrimaryClipChanged() {
        //循环调用每个监听器的onPrimaryClipChanged()方法
        mPrimaryClipChangedListeners.forEach {
            it.onPrimaryClipChanged()
        }
    }

    fun getPrimaryClipDescription(context: Context) {
        try {
            val mClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            mClipboardManager.primaryClipDescription
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @JvmStatic
        val instance: ClipboardManagerUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ClipboardManagerUtil()
        }
    }
}