package com.fatec_gab_viana.navy

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.fatec_gab_viana.navy.databinding.FragmentEditarCadastroUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class EditarCadastroUsuario : Fragment() {
    private lateinit var _binding: FragmentEditarCadastroUsuarioBinding
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private lateinit var uid: String
    private var usuario: usuario = usuario()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarCadastroUsuarioBinding.inflate(inflater,container,false)
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuario")
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        var imageurl = ""
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            if (it != null ){
                imageUri = it
                binding?.editUsuarioImagem?.setImageURI(imageUri)
                val imagem = imageUri
                val nomeImagem = binding.editNomeTf.text.toString() + "_fotoDePerfil.png"
                storageReference = FirebaseStorage.getInstance().getReference("Carros/"+nomeImagem)
                if(imagem!=null){
                    storageReference.putFile(imagem).addOnCompleteListener{
                        if (it.isSuccessful){
                            storageReference.downloadUrl.addOnCompleteListener { url ->
                                imageurl = url.result.toString()
                                databaseReference.child("Usuario").child(uid).child("imagem").setValue(imageurl)

                            }
                        }
                    }
                }


            }else{
                Toast.makeText(requireContext(),"Nenhuma imagem selecionada",Toast.LENGTH_SHORT).show()
            }
        }
        if(uid.isNotEmpty()){
            pegarDadosUsuario()
        }
        binding.btnVoltar.setOnClickListener {
            trocar_fragmento(opcoes())
        }
        binding.btnEditar.setOnClickListener {
            val nome = binding.editNomeTf.text.toString()
            val cpf = binding.editCpfTf.text.toString()
            val email = binding.editEmailTf.text.toString()
            val ddi = binding.editCelDdiTf.text.toString()
            val ddd = binding.editCelDddTf.text.toString()
            val celular = binding.editCelNumberTf.text.toString()
            var sexo = ""
            if (usuario.sexo.toString() =="Masculino"){
                sexo = "Masculino"
            }else if (usuario.sexo.toString() =="Feminino"){
                sexo = "Feminino"
            } else if (usuario.sexo.toString() =="Outro"){
                sexo = "Outro"
            } else{

            }

            update_data(nome,cpf,email,ddi,ddd,celular,sexo)
        }
        binding.editUsuarioImagem.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        return binding.root
    }
    private fun trocar_fragmento(fragment: Fragment){
        val fragmentmanager = requireActivity().supportFragmentManager.beginTransaction()
        fragmentmanager.replace(R.id.frame_layout, fragment)
        fragmentmanager.addToBackStack(null)
        fragmentmanager.commit()

    }
    private fun pegarDadosUsuario() {


        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Dados existem, você pode acessá-los com segurança
                    usuario = snapshot.getValue(usuario::class.java)!!
                    val imagemurl = usuario.imagem
                    binding.editNomeTf.setText(usuario.nome)
                    binding.editEmailTf.setText(usuario.email)
                    Glide.with(requireContext())
                        .load(imagemurl)
                        .into(binding.editUsuarioImagem)
                    binding.editCpfTf.setText(usuario.cpf)
                    binding.editCelDdiTf.setText(usuario.celular!!.substring(usuario.celular!!.indexOf("",1)))
                    binding.editCelDddTf.setText(usuario.celular!!.substringAfter(" "))
                    binding.editCelNumberTf.setText(usuario.celular!!.substring(usuario.celular!!.lastIndexOf(" ")))
                    if (usuario.sexo.toString() =="Masculino"){
                       binding.editRadioMas.isChecked = true
                    }else if (usuario.sexo.toString() =="Feminino"){
                        binding.editRadioFem.isChecked = true
                    } else if (usuario.sexo.toString() =="Outro"){
                        binding.editRadioOut.isChecked = true
                    }

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
    private fun update_data(nome:String,cpf:String,email:String,ddi:String,ddd:String,celular:String,sexo:String){
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuario")
        val usuarioFirebase = mapOf<String, String>(
            "nome" to nome,
            "cpf" to cpf,
            "celular" to "+"+ddi+" "+ddd+" "+celular,
            "email" to email,
            "sexo" to sexo
        )
        databaseReference.child(uid).updateChildren(usuarioFirebase).addOnCompleteListener {
            trocar_fragmento(opcoes())
        }

    }

}