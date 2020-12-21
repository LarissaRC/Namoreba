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
                        Picasso.get().load(user!!.getFotoPerfil()).placeholder(R.drawable.ic_profile).into(picture_profile)
                        Picasso.get().load(user!!.getFoto1()).placeholder(R.drawable.ic_profile).into(imagePerfilEdit1)
                        Picasso.get().load(user!!.getFoto2()).placeholder(R.drawable.ic_profile).into(imagePerfilEdit2)
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

    //Selecionar imagens
    private fun imagePick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null){
            imageUri = data.data

            //Cortar a imagem
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                if(foto == 1){
                    CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this.activity!!)
                } else{
                    CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 2)
                        .start(this.activity!!)
                }
            }

            if(requestCode != CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                val result = CropImage.getActivityResult(data)
                if(resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    imageUri = resultUri
                    //set image
                    when(foto) {
                        1 -> imagePerfilAddCad.setImageURI(resultUri)
                        2 -> imagePerfil1.setImageURI(resultUri)
                        3 -> imagePerfil2.setImageURI(resultUri)
                        4 -> imagePerfil3.setImageURI(resultUri)
                    }
                } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //error
                    val erro = result.error
                    Toast.makeText(this.context, "" + erro, Toast.LENGTH_SHORT).show()
                }
            }

            uploadImage()
        }
    }

    private fun uploadImage() {
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("Imagem sendo enviada, aguarde...")
        progressBar.show()

        if(imageUri != null){
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
            val uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                if(!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    when(foto){
                        1 -> {
                            val map = HashMap<String, Any>()
                            map["fotoPerfil"] = url
                            refUsers!!.updateChildren(map)
                            foto = 0
                        }
                        2 -> {
                            val map = HashMap<String, Any>()
                            map["foto1"] = url
                            refUsers!!.updateChildren(map)
                            foto = 0
                        }
                        3 -> {
                            val map = HashMap<String, Any>()
                            map["foto2"] = url
                            refUsers!!.updateChildren(map)
                            foto = 0
                        }
                        4 -> {
                            val map = HashMap<String, Any>()
                            map["foto3"] = url
                            refUsers!!.updateChildren(map)
                            foto = 0
                        }
                    }
                    progressBar.dismiss()
                }
            }
        }
    }
}