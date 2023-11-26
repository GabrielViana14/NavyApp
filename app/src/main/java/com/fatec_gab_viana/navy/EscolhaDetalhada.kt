package com.fatec_gab_viana.navy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.fatec_gab_viana.navy.databinding.FragmentEscolhaDetalhadaBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class EscolhaDetalhada : Fragment() {
    private lateinit var _binding: FragmentEscolhaDetalhadaBinding
    private val binding get() = _binding
    private var imageurl: String? = null
    private var placa: String? = null
    private var modelo: String? = null
    private var cambio: String? = null
    private var porta: String? = null
    private var preco: String? = null
    private var grupo: String? = null
    private var ano: String? = null
    private var combustivel: String? = null
    private var cor: String? = null
    private var marca: String? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageurl = it.getString("imagem")
            placa = it.getString("placa")
            modelo = it.getString("modelo")
            cambio = it.getString("cambio")
            porta = it.getString("porta")
            preco = it.getString("preco")
            grupo = it.getString("grupo")
            ano = it.getString("ano")
            combustivel = it.getString("Combustivel")
            cor = it.getString("cor")
            marca = it.getString("marca")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEscolhaDetalhadaBinding.inflate(inflater,container,false)

        imageurl?.let {
                Glide.with(this)
                    .load(it)
                    .into(binding.detalheImagemCarro)
        }


        binding.detalheModeloCarro.text = modelo
        binding.detalhePlacaCarro.text = placa
        binding.detalheCambioCarro.text = cambio
        binding.detalhePortaCarro.text = porta
        binding.detalhePrecoCarro.text = preco
        binding.detalheGrupoCarro.text = grupo
        binding.detalheAnoCarro.text = ano
        binding.detalheCombustivelCarro.text = combustivel
        binding.detalheCorCarro.text = cor
        binding.detalheMarcaCarro.text = marca



        val params = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0,0,0,0)
        binding.root.layoutParams = params



        return binding.root
    }

}