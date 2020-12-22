package com.example.telainicio.fragmentsInicial

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.telainicio.R
import com.example.telainicio.acticitysProfile.configuracoesActivity
import com.example.telainicio.acticitysProfile.editarperfilActivity
import com.example.telainicio.acticitysProfile.infoActivity
import com.example.telainicio.classes.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_registerfive.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class profileFragment : Fragment(), View.OnClickListener {

    //RequestCode
    private var RequestCode = 438
    //Uri das imagens
    private var imageUri : Uri? = null
    //Identificar qual imageView vai a imagem
    private var foto = 0

    //Firebase
    private var refUsers: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null
    private var storageRef: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment7
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        //Retornar dados do banco de dados
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        refUsers!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user: User? = snapshot.getValue(User::class.java)

                    if(context != null){
                        view.userName.text = user!!.getNome()
                        view.txtBio.text = user!!.getBio()
                        if(!user!!.getFotoPerfil().equals("")){
                            Picasso.get().load(user!!.getFotoPerfil()).placeholder(R.drawable.ic_profile).into(picture_profile)
                        } else {
                            Picasso.get().load(R.drawable.ic_profile).into(picture_profile)
                        }

                        if(!user!!.getFoto1().equals(""))
                            Picasso.get().load(user!!.getFoto1()).placeholder(R.drawable.ic_profile).into(imagePerfilEdit1)

                        if(!user!!.getFoto2().equals(""))
                            Picasso.get().load(user!!.getFoto2()).placeholder(R.drawable.ic_profile).into(imagePerfilEdit2)

                        if(!user!!.getFoto3().equals(""))
                            Picasso.get().load(user!!.getFoto3()).placeholder(R.drawable.ic_profile).into(imagePerfilEdit3)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        return view
    }

    override fun onClick(view: View) {
        val id = view.id

        if(id == R.id.buttonconfig){
            startActivity(Intent(context!!, configuracoesActivity::class.java))
        }else
            if(id == R.id.buttoneditarp){
                startActivity(Intent(context!!, editarperfilActivity::class.java))
        }else
                if(id == R.id.buttoninfo){
                    startActivity(Intent(context!!, infoActivity::class.java))
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListners()
    }

    private fun getListners(){
        buttonconfig.setOnClickListener(this)
        buttoneditarp.setOnClickListener(this)
        buttoninfo.setOnClickListener(this)
    }
}