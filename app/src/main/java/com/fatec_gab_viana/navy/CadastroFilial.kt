package com.fatec_gab_viana.navy

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doAfterTextChanged
import com.fatec_gab_viana.navy.cep_api.RetrofitInitializer
import com.fatec_gab_viana.navy.cep_api.cep
import com.fatec_gab_viana.navy.databinding.ActivityCadastroFilialBinding
import com.fatec_gab_viana.navy.models.Filial
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class CadastroFilial : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroFilialBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#FFF1F0")
        binding = ActivityCadastroFilialBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.cadFilialCepTf.doAfterTextChanged { editable ->
            val cep = editable.toString()
            if (cep.length ==8 ){
                binding.cadFilialLoadingBar.visibility = View.VISIBLE
                val call = RetrofitInitializer().apiRetrofitService().getEnderecoByCEP(binding.cadFilialCepTf.text.toString())

                call.enqueue(object : Callback<cep>{
                    override fun onResponse(call: Call<cep>, response: Response<cep>) {
                        response?.let {
                            val CEPs:cep? = it.body()
                            binding.cadFilialCidadeTf.setText(CEPs?.localidade.toString())
                            binding.cadFilialBairroTf.setText(CEPs?.bairro.toString())
                            binding.cadFilialRuaTf.setText(CEPs?.logradouro.toString())
                            binding.cadFilialEstadoTf.setText(CEPs?.uf.toString())
                            Log.i("CEP",CEPs.toString())
                            Toast.makeText(this@CadastroFilial,"CEP: $CEPs",Toast.LENGTH_LONG).show()
                            binding.cadFilialLoadingBar.visibility = View.INVISIBLE

                        }
                    }

                    override fun onFailure(call: Call<cep>?, t: Throwable?) {
                        Toast.makeText(this@CadastroFilial, "Erro ao carregar CEP", Toast.LENGTH_LONG).show()
                        binding.cadFilialLoadingBar.visibility = View.INVISIBLE
                    }

                })
            }
        }

        binding.cadFilialCadastrarBtn.setOnClickListener{
            databaseReference = FirebaseDatabase.getInstance().getReference("Filial")
            val nome = binding.cadFilialNomeTf.text.toString()
            val cep = binding.cadFilialCepTf.text.toString()
            val estado = binding.cadFilialEstadoTf.text.toString()
            val cidade = binding.cadFilialCidadeTf.text.toString()
            val bairro = binding.cadFilialBairroTf.text.toString()
            val rua = binding.cadFilialRuaTf.text.toString()
            val numero = binding.cadFilialNumeroTf.text.toString()
            val complemento = binding.cadFilialComplementoTf.text.toString()


            if (rua.isEmpty()||numero.isEmpty()||cidade.isEmpty()||estado.isEmpty()){
                Toast.makeText(this@CadastroFilial, "Preencha todas as informações", Toast.LENGTH_SHORT).show()
            }else{
                val endereco =
                    rua+", "+
                            numero+", "+
                            cidade+" - "+
                            estado
                val coordenadas = ObterLatLon(this@CadastroFilial,endereco)
                if (coordenadas!=null){
                    val latitude = coordenadas.first
                    val longitude = coordenadas.second
                    if (rua.isEmpty()||numero.isEmpty()||cidade.isEmpty()||estado.isEmpty()||nome.isEmpty()||cep.isEmpty()||bairro.isEmpty()){
                        Toast.makeText(this@CadastroFilial, "Preencha todas as informações", Toast.LENGTH_SHORT).show()
                    }else{
                        val cad_filial = Filial(nome,cep,estado,cidade, bairro, rua, numero, complemento,latitude, longitude)

                        databaseReference.child(nome).setValue(cad_filial).addOnCompleteListener{
                            if (it.isSuccessful){
                                Toast.makeText(this@CadastroFilial, "Filial cadastrada com sucesso", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@CadastroFilial,MainActivity::class.java))

                            }else{
                                Toast.makeText(this@CadastroFilial, "Erro ao cadastrar, tente novamente", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
            }

        }

        binding.cadFilialBtnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun ObterLatLon(context: Context,endereco:String): Pair<Double,Double>?{
        val geocoder = Geocoder(context)
        try {
            val enderecos: List<Address>? = geocoder.getFromLocationName(endereco,1)

            if (enderecos!!.isNotEmpty()) {
                val latitude = enderecos[0].latitude
                val longitude = enderecos[0].longitude

                return Pair(latitude, longitude)
            }
        } catch (e: IOException){
            Toast.makeText(this@CadastroFilial, "Erro ao obter coordenadas", Toast.LENGTH_LONG).show()
        }

        return null
    }


}