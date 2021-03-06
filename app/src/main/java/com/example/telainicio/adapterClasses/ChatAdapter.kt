package com.example.telainicio.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.telainicio.R
import com.example.telainicio.classes.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(
    mContext: Context,
    mChatList: List<Chat>,
    imageUrl: String
    ) : RecyclerView.Adapter<ChatAdapter.ViewHolder?>()
{
    private val mContext: Context?
    private val mChatList: List<Chat>
    private val imageUrl: String?
    var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    init {
        this.mChatList = mChatList
        this.mContext = mContext
        this.imageUrl = imageUrl
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        // Position 1 == message_item_left
        //Position 0 == message_item_right
        return if(position == 1) {
            val view: View = LayoutInflater.from(mContext).inflate(com.example.telainicio.R.layout.message_item_right, parent, false)
            ViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(mContext).inflate(com.example.telainicio.R.layout.message_item_left, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mChatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val chat: Chat = mChatList[position]

        if(!imageUrl.equals("")){
            Picasso.get().load(imageUrl).into(holder.profile_image)
        } else {
            Picasso.get().load(R.drawable.ic_profile).into(holder.profile_image)
        }

        //Se tratando de uma imagem...
        if(chat.getMessage().equals("enviou uma imagem.") && !chat.getUrl().equals("")){

            //Image message - right side
            if(chat.getSender().equals(firebaseUser!!.uid)){
                holder.show_text_message!!.visibility = View.GONE
                holder.right_image_view!!.visibility = View.VISIBLE
                Picasso.get().load(chat.getUrl()).into(holder.right_image_view)
            }
            //Image message - left side
            else if(!chat.getSender().equals(firebaseUser!!.uid)){
                holder.show_text_message!!.visibility = View.GONE
                holder.left_image_view!!.visibility = View.VISIBLE
                Picasso.get().load(chat.getUrl()).into(holder.left_image_view)
            }

        }
        //Se tratando de um texto...
        else{
            holder.show_text_message!!.text = chat.getMessage()

        }

        //Sent and seen message
        if(position == mChatList.size-1){
            if(chat.isIsseen()){
                holder.text_seen!!.text = "Seen"

                if(chat.getMessage().equals("enviou uma imagem.") && !chat.getUrl().equals("")){
                    val lp: RelativeLayout.LayoutParams? = holder.text_seen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0, 245, 10, 10)
                    holder.text_seen!!.layoutParams = lp
                }
            } else {
                holder.text_seen!!.text = "Sent"

                if(chat.getMessage().equals("enviou uma imagem.") && !chat.getUrl().equals("")){
                    val lp: RelativeLayout.LayoutParams? = holder.text_seen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0, 245, 10, 10)
                    holder.text_seen!!.layoutParams = lp
                }
            }
        } else {
            holder.text_seen!!.visibility = View.GONE
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profile_image: CircleImageView? = null
        var show_text_message: TextView? = null
        var left_image_view: ImageView? = null
        var right_image_view: ImageView? = null
        var text_seen: TextView? = null

        init {
            profile_image = itemView.findViewById(R.id.profile_image)
            show_text_message = itemView.findViewById(R.id.show_text_message)
            left_image_view = itemView.findViewById(R.id.left_image_view)
            right_image_view = itemView.findViewById(R.id.right_image_view)
            text_seen = itemView.findViewById(R.id.text_seen)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return  if(mChatList[position].getSender().equals(firebaseUser!!.uid)){
            1
        } else{
            0
        }
    }
}