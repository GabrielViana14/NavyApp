package com.fatec_gab_viana.navy

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.fatec_gab_viana.navy.databinding.ActivityCadastroCarroBinding
import com.fatec_gab_viana.navy.models.CarroNew
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CadastroCarroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroCarroBinding
    private lateinit var imageUri: Uri
    private lateinit var  databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroCarroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            if (it != null ){
                imageUri = it
                binding.cadCarroImagem.setImageURI(imageUri)
            }else{
                Toast.makeText(this,"Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
            }

        }
        binding.cadFrotaBtnVoltar.setOnClickListener{
            finish()
        }

        binding.cadCarroImagem.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val grupoitems = arrayOf("Sedan","SUV","Picape","Hatch")
        val grupoadapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,grupoitems)
        binding.cadCarroGrupoAutotext.setAdapter(grupoadapter)
        val combitens = arrayOf("Gasolina","Alcool","Flex","Diesel","Gás Natural Veicular (GNV)")
        val combadapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,combitens)
        binding.cadCarroCombustivelAutotext.setAdapter(combadapter)
        val cambitens = arrayOf("Automatico","Manual")
        val cambadapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,cambitens)
        binding.cadCarroCambioAutotext.setAdapter(cambadapter)
        val assenitens = arrayOf(2,5,7)
        val assenadapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,assenitens)
        binding.cadCarroAssentoAutotext.setAdapter(assenadapter)
        val database = FirebaseDatabase.getInstance()
        val filialref = database.getReference("Filial")
        filialref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val filialList = snapshot.children.mapNotNull {
                    it.child("nome").getValue(String::class.java) }
                val filialadapter = ArrayAdapter(this@CadastroCarroActivity,android.R.layout.simple_dropdown_item_1line,filialList)
                binding.cadCarroFilialAutotext.setAdapter(filialadapter)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.cadFrotaBtnCadastrar.setOnClickListener{
            val grupo = binding.cadCarroGrupoAutotext.text.toString()
            val modelo = binding.cadCarroModeloTf.text.toString()
            val marca = binding.cadFrotaMarcaTf.text.toString()
            val placa = binding.cadFrotaPlacaTf.text.toString()
            val anotext = binding.cadFrotaAnoTf.text.toString()
            val kmtext = binding.cadFrotaKmTf.text.toString()
            val potenciatext = binding.cadFrotaPotenciaTf.text.toString()
            val combustivel = binding.cadCarroCombustivelAutotext.text.toString()
            val precotext = binding.cadFrotaPrecoTf.text.toString()
            val valorHoratext = binding.cadFrotaValorhoraTf.text.toString()
            val valorKMtext = binding.cadFrotaValorkmTf.text.toString()
            val cambio = binding.cadCarroCambioAutotext.text.toString()
            val assentotext = binding.cadCarroAssentoAutotext.text.toString()
            val filial = binding.cadCarroFilialAutotext.text.toString()
            val disponivel = true
            val ipva = true

            if (grupo.isEmpty()||
                modelo.isEmpty()||
                marca.isEmpty()||
                placa.isEmpty()||
                anotext.isEmpty()||
                kmtext.isEmpty()||
                potenciatext.isEmpty()||
                combustivel.isEmpty()||
                precotext.isEmpty()||
                valorHoratext.isEmpty()||
                valorKMtext.isEmpty()||
                cambio.isEmpty()||
                assentotext.isEmpty()||
                filial.isEmpty()){
                Toast.makeText(this,"Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }else{
                val ano = anotext.toInt()
                val km = kmtext.toDouble()
                val potencia = potenciatext.toDouble()
                val preco = precotext.toDouble()
                val valorHora = valorHoratext.toDouble()
                val valorKM = valorKMtext.toDouble()
                val assento = assentotext.toInt()
                val imageSelecionada = imageUri

                val imageName = modelo + ano + placa +".jpg"
                storageReference = FirebaseStorage.getInstance().getReference("Carros/"+imageName)
                storageReference.putFile(imageSelecionada).addOnCompleteListener {
                    if (it.isSuccessful) {
                        storageReference.downloadUrl.addOnCompleteListener { url ->
                            val imageurl = url.result.toString()
                            val cad_carro = CarroNew(imageurl,grupo,modelo,marca,placa,ano,km,potencia,combustivel,preco,valorHora,valorKM,cambio,assento,filial,disponivel,ipva)
                            databaseReference.child(placa).setValue(cad_carro).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(this,"Carro cadastrado com sucesso",Toast.LENGTH_LONG).show()
                                    finish()
                                }else{
                                    Toast.makeText(this,"Falha no cadastro, tente novamente",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }

        }



    }
}