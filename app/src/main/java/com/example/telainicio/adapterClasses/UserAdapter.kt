package com.example.telainicio.adapterClasses

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.telainicio.R
import com.example.telainicio.classes.User
import com.example.telainicio.MessageChatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(mContext: Context, mUsers: List<User>, isChatCheck: Boolean): RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    private val mContext: Context
    private val mUsers: List<User>
    private val isChatCheck: Boolean

    init {
        this.mContext = mContext
        this.mUsers = mUsers
        this.isChatCheck = isChatCheck
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout, viewGroup, false)
        return UserAdapter.ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user: User? = mUsers[position]
        holder.userNameTxt.text = user!!.getNome()
        Picasso.get().load(user!!.getFotoPerfil()).placeholder(R.drawable.ic_profile).into(holder.profileImage)

        holder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence>(
                "Enviar Mensagem",
                "Visitar Perfil"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            builder.setTitle("O que Deseja?")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                if(which == 0){
                    val intent = Intent(mContext, MessageChatActivity::class.java)
                    intent.putExtra("visit_id", user.getUid())
                    mContext.startActivity(intent)
                } else if(which == 1){

                }
            })
            builder.show()
        }

    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var userNameTxt: TextView
        var profileImage: CircleImageView
        var onlineImage: CircleImageView
        var offlineImage: CircleImageView
        var lastMessage: TextView

        init {
            userNameTxt = itemView.findViewById(R.id.username_item)
            profileImage = itemView.findViewById(R.id.profile_item_id)
            onlineImage = itemView.findViewById(R.id.image_online)
            offlineImage = itemView.findViewById(R.id.image_offline)
            lastMessage = itemView.findViewById(R.id.last_message)
        }
    }

}