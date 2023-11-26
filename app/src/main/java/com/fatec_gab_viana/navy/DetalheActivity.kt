package com.fatec_gab_viana.navy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fatec_gab_viana.navy.databinding.ActivityDetalheBinding

class DetalheActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalheBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imagemurl = intent.getStringExtra("imagem")
        imagemurl.let {
            Glide.with(this)
                .load(it)
                .into(binding.detalheImagemCarro)
        }

        binding.detalheModeloCarro.text = intent.getStringExtra("modelo")
        binding.detalhePrecoCarro.text = intent.getStringExtra("preco")
        binding.detalheValorhoraCarro.text = intent.getStringExtra("preco por hora")
        binding.detalheValorkmCarro.text = intent.getStringExtra("preco por km")
        binding.detalheAssentosCarro.text = intent.getStringExtra("assentos")
        binding.detalheGrupoCarro.text = intent.getStringExtra("grupo")
        binding.detalheCambioCarro.text = intent.getStringExtra("cambio")
        binding.detalheAnoCarro.text = intent.getStringExtra("ano")
        binding.detalheCombustivelCarro.text = intent.getStringExtra("Combustivel")
        binding.detalhePotenciaCarro.text = intent.getStringExtra("potencia")
        binding.detalheMarcaCarro.text = intent.getStringExtra("marca")
        binding.detalheDescricaoCarro.text = intent.getStringExtra("localizacao")
        binding.detalhePlacaCarro.text = intent.getStringExtra("placa")

        binding.btnDetalheSair.setOnClickListener{
            finish()
        }

    }
}