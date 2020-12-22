package com.example.telainicio.fragmentsInicial

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.telainicio.PrincipalL
import com.example.telainicio.R
import com.example.telainicio.adapterClasses.UserAdapter
import com.example.telainicio.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.ClassCastException
import java.util.*
import kotlin.collections.ArrayList


class settingsFragment : Fragment(), View.OnClickListener {

    private lateinit var mBottomS: PrincipalL

    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    private var userAdapter: UserAdapter? = null
    private var mUsers: List<User>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        //Display data
        refUsers!!.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user: User? = p0.getValue(User::class.java)
                    if(!user!!.getFotoPerfil().equals("")) {
                        Picasso.get().load(user!!.getFotoPerfil()).placeholder(R.drawable.ic_profile).into(imgSheet)
                    } else{
                        Picasso.get().load(R.drawable.ic_profile).into(imgSheet)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view.findViewById(R.id.search_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        searchEditText = view.findViewById(R.id.txtSearchName)

        mUsers = ArrayList()
        retrieveAllUsers()

        searchEditText!!.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUser(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        return view
    }

    override fun onClick(view: View) {
        val id = view.id

        if (id ==  R.id.imgSheet){
            mBottomS.goBottom()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListners()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mBottomS = activity as PrincipalL
        } catch (e: ClassCastException){
        }
    }

    private fun getListners(){
        imgSheet.setOnClickListener(this)
    }

    private fun retrieveAllUsers() {

        var firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers = FirebaseDatabase.getInstance().reference.child("Users")
        refUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<User>).clear()

                if(searchEditText!!.text.toString() == ""){
                    for(snapshot in p0.children) {
                        val user: User? = snapshot.getValue(User::class.java)
                        if(!(user!!.getUid()).equals(firebaseUserUid)) {
                            (mUsers as ArrayList<User>).add(user)
                        }
                    }
                    userAdapter = UserAdapter(context!!, mUsers!!, false)
                    recyclerView!!.adapter = userAdapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    private fun searchUser(str: String) {
        var firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        val queryUsers = FirebaseDatabase.getInstance().reference
            .child("Users")
            .orderByChild("lowerNome")
            .startAt(str)
            .endAt(str + "\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<User>).clear()
                for(snapshot in p0.children) {
                    val user: User? = snapshot.getValue(User::class.java)
                    if(!(user!!.getUid()).equals(firebaseUserUid)) {
                        (mUsers as ArrayList<User>).add(user)
                    }
                }
                userAdapter = UserAdapter(context!!, mUsers!!, false)
                recyclerView!!.adapter = userAdapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}