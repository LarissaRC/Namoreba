package com.example.telainicio.activitysRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.telainicio.InicialActivity
import com.example.telainicio.R
import kotlinx.android.synthetic.main.activity_registertwo.*

class registertwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registertwo)

        buttonproxCt.setOnClickListener {
            val intent: Intent = Intent(this, registerthreeActivity::class.java)
            startActivity(intent)
        }
    }
}