package com.fatec_gab_viana.navy

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fatec_gab_viana.navy.databinding.FragmentOpcoesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener


class opcoes : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private var _binding:FragmentOpcoesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("FragmentOpcoesBinding is not initialized yet.")
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private var usuario: usuario = usuario()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            activity?.window?.statusBarColor = Color.parseColor("#764967")
        }
        activity?.actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#764967")))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpcoesBinding.inflate(inflater,container,false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        val intent = Intent(activity,CadastroFilial::class.java)



        databaseReference = FirebaseDatabase.getInstance().getReference("Usuario")
        if(uid.isNotEmpty()){
            pegarDadosUsuario()
        }
        binding.opcoesBtnLogoutUsuario.setOnClickListener{
            auth.signOut()
            binding.opcoesNomeUsuario.setText("Nome")
            binding.opcoesEmailUsuario.setText("E-mail")
            val intent = Intent(activity,LoginScreen::class.java)
            startActivity(intent)
        }
        binding.opcoesBtnCarroUsuario.setOnClickListener {
            val intent = Intent(activity,CadastroCarroActivity::class.java)
            startActivity(intent)
        }
        binding.opcoesBtnEditarUsuario.setOnClickListener {
            trocar_fragmento(EditarCadastroUsuario())
        }
        binding.opcoesBtnFilialUsuario.setOnClickListener{
            startActivity(intent)
        }



        return view
    }

    private fun pegarDadosUsuario() {


        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded){
                    if (snapshot.exists()) {
                        // Dados existem, você pode acessá-los com segurança
                        val data = snapshot.value
                        if (data is Map<*, *>){
                            val usuarioFirebase = usuario(
                                nome = data["nome"] as? String,
                                email = data["email"] as? String,
                                imagem = data["imagem"] as? String,
                            )
                            val imagemurl = usuarioFirebase.imagem ?: ""
                            binding.opcoesNomeUsuario.setText(usuarioFirebase.nome)
                            binding.opcoesEmailUsuario.setText(usuarioFirebase.email)
                            Glide.with(requireContext())
                                .load(imagemurl)
                                .into(binding.opcoesImagemUsuario)
                        }

                    } else {
                        // Dados não existem ou estão vazios
                        Toast.makeText(requireContext(), "Dados não encontrados", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                if (error.code == DatabaseError.PERMISSION_DENIED) {
                    // Exemplo: Permissão negada ao acessar os dados
                    Toast.makeText(requireContext(), "Acesso negado aos dados", Toast.LENGTH_SHORT).show()
                } else {
                    // Outros tratamentos de erros podem ser adicionados conforme necessário
                    Toast.makeText(requireContext(), "Erro desconhecido: " + error.message, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
    private fun trocar_fragmento(fragment: Fragment){
        val fragmentmanager = requireActivity().supportFragmentManager.beginTransaction()
        fragmentmanager.replace(R.id.frame_layout, fragment)
        fragmentmanager.addToBackStack(null)
        fragmentmanager.commit()

    }

}