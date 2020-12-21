package com.example.telainicio.acticitysProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.telainicio.InicialActivity
import com.example.telainicio.R
import com.example.telainicio.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_editarperfil.*
import kotlinx.android.synthetic.main.activity_registerone.*
import kotlinx.android.synthetic.main.activity_registerthree.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class editarperfilActivity : AppCompatActivity() {

    private var nome: String? = null
    private var dataNasc: String? = null
    private var areaTrab: String? = null
    private var orientacao: String? = null
    private var genero: Int = -1
    private var opcao: Int = -1

    //Firebase
    private var refUsers: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarperfil)

        addSpinnerGenero()
        addSpinnerAmizade()

        //Retornar dados do banco de dados
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        buttoneditarfotos.setOnClickListener {
            val intent = Intent(this, editarFotosPerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSalvarAlteracoes.setOnClickListener {
            getData()
            updateData()
        }

    }

    private fun getData() {
        nome = edtnome.getText().toString().trim()
        dataNasc = edtdatan.getText().toString().trim()
        areaTrab = edtareat.getText().toString().trim()
        orientacao = edtorient.getText().toString().trim()
        genero = spinnergenedtp.selectedItemPosition
        opcao = spinneramzdedtp.selectedItemPosition
    }

    private fun updateData() {

        val mapNewData = HashMap<String, Any>()

        if (!nome!!.isEmpty()) {
            mapNewData["nome"] = "" + nome
            mapNewData["lowerNome"] = "" + nome!!.toLowerCase()
        }
        if (!dataNasc!!.isEmpty()) {
            mapNewData["dataNasc"] = "" + dataNasc
        }
        if (!areaTrab!!.isEmpty()) {
            mapNewData["areaTrab"] = "" + areaTrab
        }
        if (!orientacao!!.isEmpty()) {
            mapNewData["orientacao"] = "" + orientacao
        }
        if (genero != 0) {
            mapNewData["genero"] = genero
        }
        if (opcao != 0) {
            mapNewData["preferencia"] = opcao
        }

        refUsers!!.updateChildren(mapNewData).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show()
                limparCampos()
            }
        }
    }

    private fun limparCampos() {
        edtnome.text = null
        edtnome.text = null
        edtdatan.text = null
        edtareat.text = null
        edtorient.text = null
    }

    private fun addSpinnerGenero(){
        val generosC = arrayOf("    ","Homem","Mulher","Não-Binário","Mulher-Trans","Homem-Trans","Travesti")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generosC)

        spinnergenedtp.adapter = arrayAdapter

        spinnergenedtp.onItemSelectedListener = object :
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

        spinneramzdedtp.adapter = arrayAdpt

        spinneramzdedtp.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }
    }

}