package com.fatec_gab_viana.navy

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fatec_gab_viana.navy.databinding.FragmentUserScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference

class user_screen : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding:FragmentUserScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var usuario: usuario = usuario()
    private lateinit var dialog: Dialog
    private lateinit var uid: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserScreenBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        Toast.makeText(requireContext(),"uid: "+uid,Toast.LENGTH_SHORT).show()


        databaseReference = FirebaseDatabase.getInstance().getReference("Usuario")
        if(uid.isNotEmpty()){
            pegarDadosUsuario()
        }
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            binding.userNome.setText("Usuario")
            binding.userEmail.setText("E-mail")
            binding.userCelular.setText("Celular")
            trocar_fragmento(login())
        }
        binding.btnIrCadFrota.setOnClickListener {
            trocar_fragmento(cadastroFrota())
        }


        return binding.root
    }

    private fun pegarDadosUsuario() {


        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Dados existem, você pode acessá-los com segurança
                    usuario = snapshot.getValue(usuario::class.java)!!
                    binding.userNome.setText(usuario.nome)
                    binding.userEmail.setText(usuario.email)
                    binding.userCelular.setText(usuario.celular)
                } else {
                    // Dados não existem ou estão vazios
                    Toast.makeText(requireContext(), "Dados não encontrados", Toast.LENGTH_SHORT).show()
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
