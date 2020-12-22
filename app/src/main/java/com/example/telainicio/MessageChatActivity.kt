package com.example.telainicio

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.telainicio.adapterClasses.ChatAdapter
import com.example.telainicio.classes.Chat
import com.example.telainicio.classes.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*

class MessageChatActivity : AppCompatActivity() {

    var userIdVisit: String? = null
    var firebaseUser: FirebaseUser? = null
    var chatAdapter: ChatAdapter? = null
    var mChatList: List<Chat>? = null
    lateinit var recycleV_chats: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        intent = intent
        userIdVisit = intent.getStringExtra("visit_id")
        firebaseUser = FirebaseAuth.getInstance().currentUser

        recycleV_chats = findViewById(R.id.recycleV_chats)
        recycleV_chats.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recycleV_chats.layoutManager = linearLayoutManager

        val reference = FirebaseDatabase.getInstance().reference
            .child("Users").child(userIdVisit!!)
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)

                username_chat.text = user!!.getNome()
                if(!user.getFotoPerfil().equals("")) {
                    Picasso.get().load(user.getFotoPerfil()).into(profile_image_chat)
                } else{
                    Picasso.get().load(R.drawable.ic_profile).into(profile_image_chat)
                }

                retrieveMessages(firebaseUser!!.uid, userIdVisit!!, user.getFotoPerfil())
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        send_message_btn.setOnClickListener {
            val message = text_message.text.toString().trim()
            if(message == ""){
                Toast.makeText(this, "Campo vazio", Toast.LENGTH_SHORT).show()
            } else {
                sendMessageToUser(firebaseUser!!.uid, userIdVisit, message)
            }
            text_message.setText("")
        }

        attach_image_file.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"

            startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 438)
        }

    }

    private fun sendMessageToUser(senderId: String, receiverId: String?, message: String) {

        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key
        val messageMap = HashMap<String, Any?>()
        messageMap["sender"] = senderId
        messageMap["message"] = message
        messageMap["receiver"] = receiverId
        messageMap["isseen"] = false
        messageMap["url"] = ""
        messageMap["messageId"] = messageKey
        reference.child("Chats")
            .child(messageKey!!)
            .setValue(messageMap)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val chatsListReference = FirebaseDatabase.getInstance()
                        .reference
                        .child("chatList")
                        .child(firebaseUser!!.uid)
                        .child(userIdVisit!!)

                    chatsListReference.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()){
                                chatsListReference.child("id").setValue(userIdVisit)
                            }

                            val chatsListReceiverRef = FirebaseDatabase.getInstance()
                                .reference
                                .child("chatList")
                                .child(userIdVisit!!)
                                .child(firebaseUser!!.uid)
                            chatsListReceiverRef.child("id").setValue(firebaseUser!!.uid)
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                    //implement the push notifications

                    val reference = FirebaseDatabase.getInstance().reference
                        .child("Users").child(firebaseUser!!.uid)
                }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==438 && resultCode == RESULT_OK && data != null && data!!.data != null){
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("Por favor, aguarde o envio da imagem...")
            progressBar.show()

            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageId = ref.push().key
            val filePath = storageReference.child("$messageId.jpg")

            var uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{task ->
                if(!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val messageMap = HashMap<String, Any?>()
                    messageMap["sender"] = firebaseUser!!.uid
                    messageMap["message"] = "enviou uma imagem."
                    messageMap["receiver"] = userIdVisit
                    messageMap["isseen"] = false
                    messageMap["url"] = url
                    messageMap["messageId"] = messageId

                    ref.child("Chats").child(messageId!!).setValue(messageMap)

                    progressBar.dismiss()
                }
            }
        }
    }



    private fun retrieveMessages(senderId: String, receiverId: String, receiverImageUrl: String?) {

        mChatList = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mChatList as ArrayList<Chat>).clear()
                for(snapshot in p0.children){
                    val chat = snapshot.getValue(Chat::class.java)

                    if(chat!!.getReceiver().equals(senderId) &&chat.getSender().equals(receiverId)
                        || chat.getReceiver().equals(receiverId) && chat.getSender().equals(senderId)){
                        (mChatList as ArrayList<Chat>).add(chat)
                    }
                    chatAdapter = ChatAdapter(this@MessageChatActivity, (mChatList as ArrayList<Chat>), receiverImageUrl!!)
                    recycleV_chats.adapter = chatAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}