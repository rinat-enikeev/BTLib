package io.btkit.btlibtester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import io.btkit.btlib.BTScannerWrapper

class MainActivity : AppCompatActivity() {

    private val scanner = BTScannerWrapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scanner.startService(applicationContext)
        scanner.mutableLiveData.observe(this, Observer {
            Log.e("", it.temperature.toString())
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
