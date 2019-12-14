package io.btkit.btlib

import android.app.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import io.btkit.btlib.model.DecodeFormat5
import io.btkit.btlib.model.RuuviTag

class BTScanner: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var callback: ((a: RuuviTag) -> Unit)? = null

    inner class LocalBinder : Binder() {
        internal val service: BTScanner
            get() = this@BTScanner
    }

    private val leScanCallback = object : BluetoothAdapter.LeScanCallback {
        override fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {
            // offset = 7
            val ruuviTag = DecodeFormat5.decode(scanRecord, 7)
            callback?.invoke(ruuviTag)
        }
    }

    private val binder = LocalBinder()

    override fun onBind(p0: Intent): IBinder? {
        return binder
    }

    public fun subscribeToLeScan(callback: (a: RuuviTag) -> Unit) {
        this.callback = callback
    }

    override fun onCreate() {
        super.onCreate()
        startRunningInForeground()
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.startLeScan(leScanCallback)
    }

    override fun onDestroy() {
        Log.e("dfsa", "onDestroy Servise stoped")
        bluetoothAdapter?.stopLeScan(leScanCallback)
        callback = null
        super.onDestroy()
    }

    private fun startRunningInForeground() {

        //if more than or equal to 26
        if (Build.VERSION.SDK_INT >= 26) {

            //if more than 26
            if (Build.VERSION.SDK_INT > 26) {
                val CHANNEL_ONE_ID = "Package.Service"
                val CHANNEL_ONE_NAME = "Screen service"
                var notificationChannel: NotificationChannel? = null
                notificationChannel = NotificationChannel(
                    CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_MIN
                )
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.setShowBadge(true)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(notificationChannel)

                val icon =
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_bt)
                val notification = Notification.Builder(getApplicationContext())
                    .setChannelId(CHANNEL_ONE_ID)
                    .setContentTitle("Recording data")
                    .setContentText("App is running background operations")
                    .setSmallIcon(R.drawable.ic_bt)
                    .setLargeIcon(icon)
                    .build()

                val notificationIntent = Intent()
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                notification.contentIntent =
                    PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0)

                startForeground(101, notification)
            } else {
                startForeground(101, updateNotification())
            }//if version 26
        } else {
            val notification = NotificationCompat.Builder(this)
                .setContentTitle("App")
                .setContentText("App is running background operations")
                .setSmallIcon(R.drawable.ic_bt)
                .setOngoing(true).build()

            startForeground(101, notification)
        }//if less than version 26
    }

    private fun updateNotification(): Notification {

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(), 0
        )

        return NotificationCompat.Builder(this)
            .setContentTitle("Activity log")
            .setTicker("Ticker")
            .setContentText("app is running background operations")
            .setSmallIcon(R.drawable.ic_bt)
            .setContentIntent(pendingIntent)
            .setOngoing(true).build()
    }
}