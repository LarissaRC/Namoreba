package com.example.telainicio.activitysRegister

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.telainicio.InicialActivity
import com.example.telainicio.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_registerfive.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class registerfiveActivity : AppCompatActivity() {

    //Variáveis para trabalhar com imagens
    //Cosntantes de permissão
    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 101
    //Image pick constantes
    private val IMAGE_PICK_CAMERA_CODE = 102
    private val IMAGE_PICK_GALLERY_CODE = 103
    //Arrays de permissão
    private lateinit var cameraPermissions : Array<String> //Camera e Storage
    private lateinit var storagePermissions : Array<String> //Storage apenas
    //Uri das imagens
    private var imageUri : Uri? = null
    private var imageUri1 : Uri? = null
    private var imageUri2 : Uri? = null
    private var imageUri3 : Uri? = null
    private var imageUri4 : Uri? = null
    //Identificar qual imageView vai a imagem
    private var foto = 0

    //Dados a serem armazenados
    lateinit var descricao: String

    //Firebase
    private var refUsers: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null
    private var storageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerfive)

        //Firebase hihi
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        //Inicializar permissões para o tratamento de imagens
        cameraPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        cardvPerfil.setOnClickListener{
            //Show image
            foto = 1
            imagePickDialog()
        }

        imagePerfil1.setOnClickListener{
            //Show image
            foto = 2
            imagePickDialog()
        }

        imagePerfil2.setOnClickListener{
            //Show image
            foto = 3
            imagePickDialog()
        }

        imagePerfil3.setOnClickListener{
            //Show image
            foto = 4
            imagePickDialog()
        }

        buttonConcluir.setOnClickListener {
            descricao = txtDescricao.getText().toString().trim()
            registerUser4()
        }

        buttonMenu.setOnClickListener {
            val intent = Intent(this, InicialActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun imagePickDialog() {
        //Opções ao usuário
        val options = arrayOf("Câmera", "Galeria")
        //dialog
        val builder = AlertDialog.Builder(this)
        //Title
        builder.setTitle("Escolha uma imagem")
        //Set itens/options
        builder.setItems(options){ dialog, wich ->
            //handle item clicks
            if(wich == 0){
                //câmera
                if(!checkCameraPermissions()){
                    //Permissão não concedida
                    requestCameraPermission()
                } else{
                    //Permissão concedida
                    pickFromCamera()
                }
            } else{
                //Galeria
                if(!checkStoragePermission()){
                    //Permissão não concedida
                    requestStoragePermission()
                } else{
                    //Permissão concedida
                    pickFromGallery()
                }
            }
        }
        //Show dialog
        builder.show()
    }

    private fun pickFromGallery() {
        //Pegar imagem da galeria usando Intent
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*" //somente imagens
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun requestStoragePermission() {
        //Request srorage permission
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {
        //Checar se a permissão para usar o armazenamento foi concedida
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickFromCamera() {
        //Pegar imagem da câmera usando Intente
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Image Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Description")
        //Put Image uri
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //Tentativa de abrir a câmera
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(
            cameraIntent,
            IMAGE_PICK_CAMERA_CODE
        )
    }

    private fun requestCameraPermission() {
        //Request Camera permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE)

    }

    private fun checkCameraPermissions(): Boolean {
        //Checar se a permissão para usar a câmera foi concedida
        val results = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val results1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        return results && results1
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    //se for permitido retornar true
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera()
                    } else {
                        Toast.makeText(this, "Câmera e Galeria são necessárias", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    //se for permitido retornar true
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        pickFromGallery()
                    } else {
                        Toast.makeText(this, "Galeria é necessária", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Imagem selecionada pela câmera ou galeria será aceita aqui
        if(resultCode == RESULT_OK) {
            //Imagem foi selecionada
            if(requestCode == IMAGE_PICK_GALLERY_CODE) {
                //Selecionada pela galeria
                //Crop Image
                if(foto == 1){
                    CropImage.activity(data!!.data)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this)
                } else{
                    CropImage.activity(data!!.data)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 2)
                        .start(this)
                }
            } else if(requestCode ==  IMAGE_PICK_CAMERA_CODE) {
                //Selecionada pela camera
                //Crop Image
                if(foto == 1){
                    CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this)
                } else{
                    CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 2)
                        .start(this)
                }
            } else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //Cropped image received
                val result = CropImage.getActivityResult(data)
                if(resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    imageUri = resultUri
                    //set image
                    when(foto) {
                        1 -> {
                            imagePerfilAddCad.setImageURI(resultUri)
                            imageUri1 = imageUri
                        }
                        2 -> {
                            imagePerfil1.setImageURI(resultUri)
                            imageUri2 = imageUri
                        }
                        3 -> {
                            imagePerfil2.setImageURI(resultUri)
                            imageUri3 = imageUri
                        }
                        4 -> {
                            imagePerfil3.setImageURI(resultUri)
                            imageUri4 = imageUri
                        }
                    }
                } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //error
                    val erro = result.error
                    Toast.makeText(this, "" + erro, Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun registerUser4(){

        val mapData = HashMap<String, Any>()

        if (!descricao!!.isEmpty()) {
            mapData["bio"] = "" + descricao
        }
        refUsers!!.updateChildren(mapData).addOnCompleteListener { task ->
            if(task.isSuccessful){
                uploadImages()
            }
        }
    }

    private fun uploadImages() {

        if(imageUri != null){

            if(imageUri1 != null){
                val progressBar1 = ProgressDialog(this)
                progressBar1.setMessage("Imagens sendo enviadas, aguarde...")
                progressBar1.show()

                val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri1!!)

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

                        val map = HashMap<String, Any>()
                        map["fotoPerfil"] = url
                        refUsers!!.updateChildren(map)
                        foto = 0
                        progressBar1.dismiss()
                    }
                }
            }
            if(imageUri2 != null){
                val progressBar2 = ProgressDialog(this)
                progressBar2.setMessage("Imagens sendo enviadas, aguarde...")
                progressBar2.show()

                val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri2!!)

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

                        val map = HashMap<String, Any>()
                        map["foto1"] = url
                        refUsers!!.updateChildren(map)
                        foto = 0
                        progressBar2.dismiss()
                    }
                }
            }
            if(imageUri3 != null){
                val progressBar3 = ProgressDialog(this)
                progressBar3.setMessage("Imagens sendo enviadas, aguarde...")
                progressBar3.show()

                val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri3!!)

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

                        val map = HashMap<String, Any>()
                        map["foto2"] = url
                        refUsers!!.updateChildren(map)
                        foto = 0
                        progressBar3.dismiss()
                    }
                }
            }
            if(imageUri4 != null){
                val progressBar4 = ProgressDialog(this)
                progressBar4.setMessage("Imagens sendo enviadas, aguarde...")
                progressBar4.show()
                val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri4!!)

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

                        val map = HashMap<String, Any>()
                        map["foto3"] = url
                        refUsers!!.updateChildren(map)
                        foto = 0
                        progressBar4.dismiss()
                    }
                }
            }
        }

    }
}