package com.example.telainicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.telainicio.acticitysProfile.infoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registerone.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    lateinit var email: String
    lateinit var senha: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        buttonlogin.setOnClickListener {
            if(verifyData()){
                loginUser()
            }
        }

        buttoncad.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginUser(){
        mAuth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val intent = Intent(this, InicialActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else{
                    Toast.makeText(this, "Erro: " + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun verifyData(): Boolean{

        email = edittextemail.getText().toString().trim()
        senha = edittextsenha.getText().toString().trim()

        if (email.isEmpty()) {
            edittextemail.error = "Insira um Email"
            edittextemail.requestFocus()
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edittextemail.error = "Insira um Email válido"
            edittextemail.requestFocus()
            return false
        }

        if (senha.isEmpty()) {
            edittextsenha.error = "Insira uma Senha para continuar"
            edittextsenha.requestFocus()
            return false
        }
        return true
    }
}