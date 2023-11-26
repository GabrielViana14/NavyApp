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
import com.fatec_gab_viana.navy.databinding.FragmentCadastroFrotaBinding
import com.fatec_gab_viana.navy.models.carro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class cadastroFrota : Fragment() {
    private var _binding:FragmentCadastroFrotaBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var imageUri:Uri? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var cambio:String = ""
    private var portas:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCadastroFrotaBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            if (it != null){
                imageUri = it
                binding.cadFrotaBtnEnviarFoto.text = "$it"
            }else{
                Toast.makeText(requireContext(),"Nenhuma imagem selecionada",Toast.LENGTH_SHORT).show()
            }
        }

        binding.cadFrotaCambioBtnManual.setOnClickListener {
            cambio = binding.cadFrotaCambioBtnManual.text.toString()
        }

        binding.cadFrotaCambioBtnAutomatico.setOnClickListener {
            cambio = binding.cadFrotaCambioBtnAutomatico.text.toString()
        }

        binding.cadFrota2portasBtn.setOnClickListener {
            portas = binding.cadFrota2portasBtn.text.toString()
        }

        binding.cadFrota4portasBtn.setOnClickListener {
            portas = binding.cadFrota4portasBtn.text.toString()
        }

        binding.cadFrotaBtnCadastrar.setOnClickListener {
            databaseReference = FirebaseDatabase.getInstance().getReference("Carros")
            val dono = auth.currentUser?.uid.toString()
            val modelo = binding.cadFrotaModeloTf.text.toString()
            val grupo = binding.cadFrotaGrupoTf.text.toString()
            val marca = binding.cadFrotaMarcaTf.text.toString()
            val ano = binding.cadFrotaAnoTf.text.toString()
            val cor = binding.cadFrotaCorTf.text.toString()
            val preco = binding.cadFrotaPrecoTf.text.toString()
            val diaria = binding.cadFrotaDiariaTf.text.toString()
            val combustivel = binding.cadFrotaCombustivelTf.text.toString()
            val placa = binding.cadFrotaPlacaTf.text.toString()
            val imageSelecionada = imageUri
            if (modelo.isEmpty() || grupo.isEmpty() || marca.isEmpty() || ano.isEmpty() || cor.isEmpty() || preco.isEmpty() || diaria.isEmpty() || combustivel.isEmpty() || placa.isEmpty()){
                Toast.makeText(requireContext(),"Preencha todos os campos",Toast.LENGTH_SHORT).show()
            }else{
                if (imageSelecionada != null){
                    val imageName = modelo + cor + placa +".jpg"
                    storageReference = FirebaseStorage.getInstance().getReference("Carros/"+imageName)
                    storageReference.putFile(imageSelecionada).addOnCompleteListener{
                        if (it.isSuccessful){
                            storageReference.downloadUrl.addOnCompleteListener { url ->
                                val imageurl = url.result.toString()
                                val cad_carro = carro(dono,grupo, modelo, marca, ano, cor, preco, diaria, combustivel, placa,imageurl,cambio,portas)
                                databaseReference.child(placa).setValue(cad_carro).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        Toast.makeText(requireContext(),"Carro cadastrado com sucesso",Toast.LENGTH_LONG).show()
                                    }else{
                                        Toast.makeText(requireContext(),"Falha no cadastro, tente novamente",Toast.LENGTH_LONG).show()
                                    }
                                }
                            }.addOnFailureListener { exception ->
                                Toast.makeText(requireContext(), "Erro ao obter URL da imagem: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            val exception = it.exception
                            if (exception != null){
                                Toast.makeText(requireContext(), "Erro ao fazer upload da imagem: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

        }
        binding.cadFrotaBtnEnviarFoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.cadFrotaBtnVoltar.setOnClickListener {
            trocar_fragmento(opcoes())
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
