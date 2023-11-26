package com.fatec_gab_viana.navy

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.fatec_gab_viana.navy.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private var auth = FirebaseAuth.getInstance()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            window.navigationBarColor= Color.parseColor("#00007E")
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.loginBtnVoltar.setOnClickListener{
            val voltar = Intent(this,MainActivity::class.java)
            startActivity(voltar)
        }

        binding.loginBtnLogar.setOnClickListener{
            val email = binding.loginEmailTf.text.toString()
            val senha = binding.loginSenhaTf.text.toString()
            if (email.isEmpty()||senha.isEmpty()){
                Toast.makeText(this,"Por favor preencha todos os dados", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener{login ->
                    if(login.isSuccessful){
                        Toast.makeText(this,"Login realizado com sucesso",Toast.LENGTH_SHORT).show()
                        finish()
                    } else{
                        Toast.makeText(this,"e-mail ou senha incorreta",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.loginBtnMudarSenha.setOnClickListener{
            trocar_senha()
        }
    }

    private fun trocar_fragmento(fragment: Fragment){
        val fragmentmanager = this.supportFragmentManager.beginTransaction()
        fragmentmanager.replace(R.id.frame_layout, fragment)
        fragmentmanager.addToBackStack(null)
        fragmentmanager.commit()

    }

    private fun trocar_senha(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mudar senha")
        builder.setMessage("Digite seu E-mail: ")

        val view = LayoutInflater.from(this).inflate(R.layout.mudar_senha, null)
        builder.setView(view)


        builder.setPositiveButton("Enviar",DialogInterface.OnClickListener{ dialog, _ ->
            val editemail = view.findViewById<EditText>(R.id.editTextEmail)
            val email = editemail.text.toString()

            if (email.isNotEmpty()) {
                enviarEmailRedefinicaoSenha(email)
            } else {
                Toast.makeText(this, "Por favor, insira um endereço de e-mail válido", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        })

        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
        })

        builder.show()


    }

    private fun enviarEmailRedefinicaoSenha(email: String) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Um e-mail para redefinir a senha foi enviado.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Falha ao enviar o e-mail de redefinição de senha.", Toast.LENGTH_SHORT).show()
                }
            }
    }



}