package com.example.telainicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.telainicio.activitysRegister.registerthreeActivity
import com.example.telainicio.activitysRegister.registertwoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registerone.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var userId: String = ""

    lateinit var email: String
    lateinit var senha: String
    lateinit var nome: String
    lateinit var sobrenome: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerone)

        mAuth = FirebaseAuth.getInstance()

        buttonproxoneC.setOnClickListener {

            if(verifyData()){

                registerUser()

                val intent = Intent(this, registerthreeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun registerUser(){
        mAuth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    userId = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                    //Inserir o objeto finalmente
                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = userId
                    userHashMap["nome"] = nome
                    userHashMap["lowerNome"] = nome.toLowerCase() // Prara quando for pesquisado
                    userHashMap["sobrenome"] = sobrenome
                    userHashMap["email"] = email
                    userHashMap["senha"] = senha
                    userHashMap["apelido"] = ""
                    userHashMap["status"] = "off"
                    userHashMap["dataNasc"] = ""
                    userHashMap["areaTrab"] = ""
                    userHashMap["orientacao"] = ""
                    userHashMap["genero"] = -1
                    userHashMap["preferencia"] = -1
                    userHashMap["generoPref"] = -1
                    userHashMap["orientacaoPref"] = ""
                    userHashMap["idadePref"] = ""
                    userHashMap["fotoPerfil"] = ""
                    userHashMap["foto1"] = ""
                    userHashMap["foto2"] = ""
                    userHashMap["foto3"] = ""
                    userHashMap["bio"] = ""

                    refUsers.updateChildren(userHashMap)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                //Toast.makeText(applicationContext, "Salvou", Toast.LENGTH_SHORT).show()
                            }
                        }

                } else{
                    Toast.makeText(applicationContext, "Erro: " + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun verifyData(): Boolean{

        email = edtEmailC.getText().toString().trim()
        senha = edtSenhaC.getText().toString().trim()
        nome = edtNomeC.getText().toString().trim()
        sobrenome = edtSobrenomeC.getText().toString().trim()
        val confirmars = edtCnfSenhaC.getText().toString().trim()

        if (nome.isEmpty()) {
            edtNomeC.error = "Insira seu primeiro nome para continuar"
            edtNomeC.requestFocus()
            return false
        }
        if (sobrenome.isEmpty()) {
            edtSobrenomeC.error = "Insira seu último nome para continuar"
            edtSobrenomeC.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            edtEmailC.error = "Insira seu Email para continuar"
            edtEmailC.requestFocus()
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmailC.error = "Insira um Email válido para continuar"
            edtEmailC.requestFocus()
            return false
        }

        if (senha.isEmpty()) {
            edtSenhaC.error = "Insira uma Senha para continuar"
            edtSenhaC.requestFocus()
            return false
        }

        if(senha.length < 6){
            edtSenhaC.error = "A senha deve ter no mínimo 6 caracteres"
            edtSenhaC.requestFocus()
            return false
        }

        if (confirmars.isEmpty()) {
            edtCnfSenhaC.error = "Confirma sua senha, repitindo-a, para continuar"
            edtCnfSenhaC.requestFocus()
            return false
        }

        if (!confirmars.equals(senha)) {
            edtCnfSenhaC.error = "Confirmação incorreta"
            edtCnfSenhaC.requestFocus()
            return false
        }
        return true
    }

}