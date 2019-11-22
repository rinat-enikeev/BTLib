package io.btkit.btlib

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import io.btkit.btlib.model.RuuviTag

class BTScannerWrapper {

    var isBound = false
    var btService: BTScanner? = null
    val mutableLiveData = MutableLiveData<RuuviTag>()

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as BTScanner.LocalBinder
            btService = binder.service
            isBound = true
            btService?.subscribeToLeScan { it ->
                mutableLiveData.postValue(it)
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    fun startService(applicationContext: Context) {
        val intent = Intent(applicationContext, BTScanner::class.java)
        applicationContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun stopService(applicationContext: Context) {
        if (isBound) {
            applicationContext.unbindService(serviceConnection)
            isBound = false
        }
    }
}