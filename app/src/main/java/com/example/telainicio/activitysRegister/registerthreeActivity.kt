package com.example.telainicio.activitysRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.telainicio.R
import com.example.telainicio.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_registerthree.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class registerthreeActivity : AppCompatActivity() {

    //Dados a serem armazenados
    lateinit var nomeSocial: String
    lateinit var dataNasc: String
    lateinit var orientacaoSex: String
    lateinit var areaTrabalho: String
    var genero = -1
    var opcao = -1

    //Firebase
    private var refUsers: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerthree)

        addSpinnerGenero()
        addSpinnerAmizade()

        buttonproxCthf.setOnClickListener {

            //Pegar valores dos spinners
            genero = spinnergen.selectedItemPosition
            opcao = spinneramsd.selectedItemPosition
            nomeSocial = edtApelido.getText().toString().trim()
            dataNasc = edtDataN.getText().toString().trim()
            areaTrabalho = edtAreaT.getText().toString().trim()
            orientacaoSex = edtOriS.getText().toString().trim()

            registerUser2()
        }
    }

    private fun addSpinnerGenero(){
        val generosC = arrayOf("    ","Homem","Mulher","Não-Binário","Mulher-Trans","Homem-Trans","Travesti")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generosC)

        spinnergen.adapter = arrayAdapter

        spinnergen.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }
    }

    private fun addSpinnerAmizade(){
        val amizade = arrayOf("   ","Apenas amizades", "Apenas parceiros amorosos", "Ambos")
        val arrayAdpt = ArrayAdapter(this, android.R.layout.simple_spinner_item, amizade)

        spinneramsd.adapter = arrayAdpt

        spinneramsd.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }
    }

    private fun registerUser2(){
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        val mapData = HashMap<String, Any>()

        if (!nomeSocial!!.isEmpty()) {
            mapData["apelido"] = "" + nomeSocial
        }
        if (!dataNasc!!.isEmpty()) {
            mapData["dataNasc"] = "" + dataNasc
        }
        if (!areaTrabalho!!.isEmpty()) {
            mapData["areaTrab"] = "" + areaTrabalho
        }
        if (!orientacaoSex!!.isEmpty()) {
            mapData["orientacao"] = "" + orientacaoSex
        }
        if (genero != 0) {
            mapData["genero"] = genero
        }
        if (opcao != 0) {
            mapData["preferencia"] = opcao
        }

        refUsers!!.updateChildren(mapData).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent = Intent(this, registerfourActivity::class.java)
                startActivity(intent)
            }
        }

    }
}