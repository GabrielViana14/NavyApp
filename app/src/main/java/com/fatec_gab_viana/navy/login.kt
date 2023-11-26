package com.fatec_gab_viana.navy

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fatec_gab_viana.navy.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class login : Fragment() {

    private var _binding:FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var auth = FirebaseAuth.getInstance()
    private lateinit var uid:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container,false)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        Toast.makeText(requireContext(),"uid: "+uid,Toast.LENGTH_SHORT).show()
        binding.btnCadastro.setOnClickListener {
            trocar_fragmento(cadastro())
        }

        binding.btnLogin.setOnClickListener{
            val email_login = binding.emailTfLogin.text.toString()
            val senha_login = binding.senhaTfLogin.text.toString()
            if (email_login.isEmpty()||senha_login.isEmpty()){
                Toast.makeText(requireContext(),"Por favor preencha todos os dados",Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email_login,senha_login).addOnCompleteListener{login ->
                    if(login.isSuccessful){
                        Toast.makeText(requireContext(),"Login realizado com sucesso",Toast.LENGTH_SHORT).show()
                        trocar_fragmento(opcoes())
                    }
                }

            }

        }

        return binding.root
        }

    private fun trocar_fragmento(fragment: Fragment){
        val fragmentmanager = requireActivity().supportFragmentManager.beginTransaction()
        fragmentmanager.replace(R.id.frame_layout, fragment)
        fragmentmanager.addToBackStack(null)
        fragmentmanager.commit()

    }

}