package io.btkit.btlibtester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import io.btkit.btlib.BTScannerWrapper

class MainActivity : AppCompatActivity() {

    private val scanner = BTScannerWrapper()
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        textView = findViewById<TextView>(R.id.textView)
        scanner.startService(applicationContext)
        scanner.mutableLiveData.observe(this, Observer {
            textView?.text = it.temperature.toString()
            textView?.invalidate()
            textView?.requestLayout()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
