package io.btkit.btlibtester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.btkit.btlib.BTLib

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BTLib.scanner.print()
    }
}
