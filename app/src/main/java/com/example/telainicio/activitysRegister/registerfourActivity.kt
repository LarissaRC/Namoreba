package com.example.telainicio.activitysRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.telainicio.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registerfour.*
import kotlinx.android.synthetic.main.activity_registerfour.buttonproxCthf
import kotlinx.android.synthetic.main.activity_registerthree.*

class registerfourActivity : AppCompatActivity() {

    //Dados a serem armazenados
    var generoP = -1
    lateinit var orientacaoP: String
    lateinit var idade: String

    //Firebase
    private var refUsers: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerfour)

        //Retornar dados do banco de dados
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        addSpinnerGenero()

        buttonproxCthf.setOnClickListener {
            //Pegar valores dos spinners
            generoP = spinnergenpf.selectedItemPosition
            orientacaoP = edtorientp.getText().toString().trim()
            idade = edtIdade.getText().toString().trim()

            registerUser3()
        }
    }

    private fun addSpinnerGenero(){
        val generosC = arrayOf("    ","Homem","Mulher","Não-Binário","Mulher-Trans","Homem-Trans","Travesti")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generosC)

        spinnergenpf.adapter = arrayAdapter

        spinnergenpf.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }
    }

    private fun registerUser3(){
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        val mapData = HashMap<String, Any>()

        if (!orientacaoP!!.isEmpty()) {
            mapData["orientacaoPref"] = "" + orientacaoP
        }
        if (!idade!!.isEmpty()) {
            mapData["idadePref"] = "" + idade
        }
        if (generoP != 0) {
            mapData["generoPref"] = generoP
        }

        refUsers!!.updateChildren(mapData).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent: Intent = Intent(this, registerfiveActivity::class.java)
                startActivity(intent)
            }
        }

    }
}