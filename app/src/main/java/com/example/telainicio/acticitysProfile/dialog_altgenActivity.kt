package com.example.telainicio.acticitysProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.telainicio.R
import kotlinx.android.synthetic.main.activity_registerthree.*
import kotlinx.android.synthetic.main.dialog_altgenero.*

class dialog_altgenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_altgenero)

        addSpinnerGenero()

        btncancel.setOnClickListener{
            finish()
        }

        btnok.setOnClickListener{
            finish()
        }
    }

    private fun addSpinnerGenero(){
        val generosC = arrayOf("    ","Homem","Mulher","Não-Binário","Mulher-Trans","Homem-Trans","Travesti")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generosC)

        spinnernovogen.adapter = arrayAdapter

        spinnernovogen.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }
    }
}