package com.fatec_gab_viana.navy

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.fatec_gab_viana.navy.databinding.FragmentCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class cadastro : Fragment() {

    private var _binding:FragmentCadastroBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private lateinit var  databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var sexo:String = ""
    private var imageUri: Uri? = null
    private var foto:String? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        Toast.makeText(requireContext(),"uid: "+uid,Toast.LENGTH_SHORT).show()
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            if (it != null ){
                imageUri = it
                foto = "Escolhida"
                binding?.cadUsuarioImagem?.setImageURI(imageUri)


            }else{
                Toast.makeText(requireContext(),"Nenhuma imagem selecionada",Toast.LENGTH_SHORT).show()
            }
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuario")
        binding.btnVoltar.setOnClickListener {
            trocar_fragmento(login())
        }

        binding.radioMas.setOnClickListener{
            sexo = binding.radioMas.text.toString()
            Toast.makeText(requireContext(), "você escolheu "+sexo, Toast.LENGTH_SHORT).show()
        }
        binding.radioFem.setOnClickListener {
            sexo = binding.radioFem.text.toString()
            Toast.makeText(requireContext(), "você escolheu "+sexo, Toast.LENGTH_SHORT).show()
        }
        binding.radioOut.setOnClickListener {
            sexo = binding.radioOut.text.toString()
            Toast.makeText(requireContext(), "você escolheu "+sexo, Toast.LENGTH_SHORT).show()
        }
        binding.cadUsuarioImagem.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))


        }

        binding.btnCadastrar.setOnClickListener{
            val email = binding.emailTf.text.toString()
            val senha = binding.senhaTf.text.toString()
            val nome = binding.nomeTf.text.toString()
            val cpf = binding.cpfTf.text.toString()
            val celular = "+"+binding.celDdiTf.text.toString()+" "+binding.celDddTf.text.toString()+" "+binding.celNumberTf.text.toString()
            val imagem = imageUri
            var imagemurl = ""



            if (email.isEmpty() || senha.isEmpty() || nome.isEmpty() || cpf.isEmpty() || celular.isEmpty()){
                val snackbar = Snackbar.make(it,"Preenchar todos os campos",Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.WHITE)
                snackbar.show()
            }else{
                    auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener{cadastro ->
                        if (cadastro.isSuccessful){
                            auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(requireContext(),"E-mail de verificação enviado",Toast.LENGTH_SHORT).show()
                                }
                            }
                            val uid = auth.currentUser?.uid
                            if (uid != null){
                                val nomeImagem = nome + "_fotoDePerfil.png"
                                storageReference = FirebaseStorage.getInstance().getReference("FotoDePerfil/"+nomeImagem)
                                if (imagem != null) {
                                    storageReference.putFile(imagem).addOnCompleteListener{
                                        if (it.isSuccessful){
                                            storageReference.downloadUrl.addOnCompleteListener { url->
                                                imagemurl = url.result.toString()
                                                val usuario = usuario(nome,cpf,sexo,celular,email,imagemurl)
                                                databaseReference.child(uid).setValue(usuario).addOnCompleteListener{
                                                    if (it.isSuccessful){
                                                        trocar_fragmento(login())
                                                    }else{
                                                        Toast.makeText(requireContext(),"Falha ao criar cadastro",Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
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