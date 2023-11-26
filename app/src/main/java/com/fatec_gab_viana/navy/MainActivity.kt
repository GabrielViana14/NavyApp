package com.fatec_gab_viana.navy

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.fatec_gab_viana.navy.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (android.os.Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            window.navigationBarColor= Color.parseColor("#3535B5")
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = FirebaseAuth.getInstance()


        trocar_fragmento(home())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_icon -> trocar_fragmento(home())
                R.id.mapa_icon -> trocar_fragmento(Mapa())
                R.id.conta_icon -> {
                    if(userIsAuthenticated()){
                        trocar_fragmento(opcoes())
                    }else{
                        val login = Intent(this,LoginScreen::class.java)
                        startActivity(login)
                    }
                }
                R.id.pesquisa_icon -> trocar_fragmento(PesquisaNew())
                else ->{

                }
            };true
        }

    }

    fun trocar_fragmento(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
    fun userIsAuthenticated(): Boolean {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        return user != null // Retorna true se o usuário estiver autenticado, caso contrário, retorna false
    }



}