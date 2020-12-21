package com.example.telainicio.acticitysProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.telainicio.MainActivity
import com.example.telainicio.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_configuracoes.*
import kotlinx.android.synthetic.main.dialog_altsenha.*
import kotlinx.android.synthetic.main.fragment_profile.*

class configuracoesActivity : AppCompatActivity() {


    //Firebase
    private var refUsers: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        showEdtEmail()
        showEdtSenha()

        //Retornar dados do banco de dados
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        buttonaltgen.setOnClickListener{
            val intent: Intent = Intent(this, dialog_altgenActivity::class.java)
            startActivity(intent)
            //finish()
        }

        btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttondesativarc.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser!!

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Usuário deletado com sucesso", Toast.LENGTH_SHORT).show()
                    }
                }
            FirebaseAuth.getInstance().signOut()
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        showEdtOrient()
        showEdtIdade()
    }

    private fun showEdtEmail(){
        buttonaltemail.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_altemail, null)

            with(builder) {
                setPositiveButton("Ok"){dialog, which ->

                }
                setNegativeButton("Cancel"){dialog, which ->
                    Log.d("Main","Negative button clicked")
                }

                setView(dialogLayout)
                show()
            }
        }
    }

    private fun showEdtSenha(){
        buttonaltsenha.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_altsenha, null)

            with(builder) {
                setPositiveButton("Ok") { dialog, which ->
                    /*
                    var senha: String = edtsenhatual.getText().toString().trim()
                    var novaSenha: String = edtnovasenha.getText().toString().trim()
                    var novaSenhaConfirmass: String = edtconfirmas.getText().toString().trim()

                    var senhaAtual: String? = null

                    refUsers!!.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                val user: User? = snapshot.getValue(User::class.java)

                                if(context != null){
                                    senhaAtual = user!!.getSenha()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })

                    if(senha.isEmpty()){
                        edtsenhatual.error = "Preencha este campo"
                        edtsenhatual.requestFocus()
                    } else if(!senha.equals(senhaAtual)){
                        edtsenhatual.error = "Senha incorreta"
                        edtsenhatual.requestFocus()
                    }else if(novaSenha.isEmpty()){
                        edtnovasenha.error = "Preencha este campo"
                        edtnovasenha.requestFocus()
                    } else if(novaSenha.length < 6){
                        edtnovasenha.error = "6 caracteres no mínimo"
                        edtnovasenha.requestFocus()
                    }else if(novaSenhaConfirmass.isEmpty()){
                        edtconfirmas.error = "Preencha este campo"
                        edtconfirmas.requestFocus()
                    } else if(!novaSenhaConfirmass.equals(novaSenha)){
                        edtconfirmas.error = "Repita a nova senha scolhida"
                        edtconfirmas.requestFocus()
                    } else{
                        firebaseUser!!.updatePassword(novaSenha)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    //mensagem: Salvou
                                    Toast.makeText(builder.context, "Senha Alterada!", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }

                     */
                }
                setNegativeButton("Cancel") { dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }

                setView(dialogLayout)
                show()
            }
        }
    }

    private fun showEdtOrient(){
        buttonaltorient.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_altorient, null)

            with(builder) {
                setPositiveButton("Ok") { dialog, which ->

                }
                setNegativeButton("Cancel") { dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }

                setView(dialogLayout)
                show()
            }
        }
    }

    private fun showEdtIdade(){
        buttonaltidade.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_altidade, null)

            with(builder) {
                setPositiveButton("Ok") { dialog, which ->

                }
                setNegativeButton("Cancel") { dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }

                setView(dialogLayout)
                show()
            }
        }
    }



}
