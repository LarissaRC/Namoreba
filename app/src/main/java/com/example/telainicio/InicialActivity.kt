package com.example.telainicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentTransaction
import com.example.telainicio.acticitysProfile.configuracoesActivity
import com.example.telainicio.acticitysProfile.editarperfilActivity
import com.example.telainicio.fragmentsInicial.homeFragment
import com.example.telainicio.fragmentsInicial.profileFragment
import com.example.telainicio.fragmentsInicial.settingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_profile.*

class InicialActivity : AppCompatActivity(), PrincipalL {

    lateinit var homeFragment: homeFragment
    lateinit var profileFragment: profileFragment
    lateinit var settingsFragment: settingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entrada_screen)



        var bottomnav : BottomNavigationView = findViewById<BottomNavigationView>(R.id.BottomNavMenu)
        var frame : FrameLayout = findViewById(R.id.frameLayout)

        homeFragment = homeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        bottomnav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    homeFragment =
                        homeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.profile -> {
                    profileFragment =
                        profileFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, profileFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.chat -> {
                    settingsFragment =
                        settingsFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, settingsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }

    }



    override fun goBottom() {
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
    }

}