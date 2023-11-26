package com.fatec_gab_viana.navy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatec_gab_viana.navy.adapter.AdapterPesquisa
import com.fatec_gab_viana.navy.databinding.FragmentPesquisaNewBinding
import com.fatec_gab_viana.navy.models.CarroNew
import com.fatec_gab_viana.navy.models.carro
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PesquisaNew : Fragment() {
    private lateinit var _binding: FragmentPesquisaNewBinding
    private val binding get() = _binding
    private lateinit var  recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapterPesquisa : AdapterPesquisa
    private var lista: ArrayList<CarroNew> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPesquisaNewBinding.inflate(inflater,container,false)
        recyclerView = binding.pesquisaRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        Obterdados()
        adapterPesquisa = AdapterPesquisa(requireActivity(),lista)
        recyclerView.adapter = adapterPesquisa
        adapterPesquisa.setOnItemClickListener(object : AdapterPesquisa.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val itemEscolhido = lista[position]

                val intent = Intent(activity,DetalheActivity::class.java)

                intent.putExtra("imagem",itemEscolhido.imagem)
                intent.putExtra("placa",itemEscolhido.placaCarro)
                intent.putExtra("modelo",itemEscolhido.modeloCarro)
                intent.putExtra("cambio","Câmbio: ${itemEscolhido.cambioCarro}")
                intent.putExtra("grupo","Grupo: ${itemEscolhido.grupo}")
                intent.putExtra("preco","R$${itemEscolhido.preco}")
                intent.putExtra("ano","Ano: ${itemEscolhido.ano}")
                intent.putExtra("Combustivel","Combustivel: ${itemEscolhido.combustivelCarro}")
                intent.putExtra("marca","Fabricante: ${itemEscolhido.marcaCarro}")
                intent.putExtra("preco por hora","Preço por hora: R$${itemEscolhido.valorHora}")
                intent.putExtra("preco por km","Preço por KM: R$${itemEscolhido.valorKmRodado}")
                intent.putExtra("potencia","Potência: ${itemEscolhido.potencia}")
                intent.putExtra("assentos","Assentos: ${itemEscolhido.assentos} lugares")
                intent.putExtra("localizacao","Localização: ${itemEscolhido.filial}")
                intent.putExtra("imagem",itemEscolhido.imagem)

                startActivity(intent)




            }

        })

        binding.pesquisaBtnOrdenar.setOnClickListener{
            MostrarOpcoesFiltro(it)
        }
        binding.pesquisaTfOrdenar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /**/
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /**/
            }


            override fun afterTextChanged(s: Editable?) {
                val listaFiltrada = if (s.isNullOrEmpty()) {
                    // Se o texto de pesquisa estiver vazio, exiba a lista completa
                    Obterdados()
                } else {
                    // Caso contrário, filtre a lista com base no texto de pesquisa
                    lista.filter {
                        it.modeloCarro?.contains(s.toString(), ignoreCase = true) ?: false
                    }
                }
                adapterPesquisa.atualizarListaFiltrada(listaFiltrada)

            }

        })


        return binding.root
    }

    private fun MostrarOpcoesFiltro(view: View?) {
        val popupMenu = PopupMenu(requireContext(),view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.ordenar,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.ordenar_opcao1 ->{
                    adapterPesquisa.filtrarLista("opcao1")
                    true
                }
                R.id.ordenar_opcao2 ->{
                    adapterPesquisa.filtrarLista("opcao2")
                    true
                }
                R.id.ordenar_opcao3 ->{
                    adapterPesquisa.filtrarLista("opcao3")
                    true
                }
                R.id.ordenar_opcao4 ->{
                    adapterPesquisa.filtrarLista("opcao4")
                    true
                }

                else -> false
            }
        }
        popupMenu.show()

    }

    private fun Obterdados(): ArrayList<CarroNew> {
        databaseReference = FirebaseDatabase.getInstance().getReference("Carros")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val novosdados = ArrayList<CarroNew>()

                for (homeSnapshot in snapshot.children) {
                    val carrodata = homeSnapshot.getValue(CarroNew::class.java)

                    carrodata?.let {
                        novosdados.add(it)
                    }

                }
                lista.clear()
                lista.addAll(novosdados)
                adapterPesquisa.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        return lista
    }

}