package com.fatec_gab_viana.navy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatec_gab_viana.navy.adapter.AdapterPesquisa
import com.fatec_gab_viana.navy.databinding.ActivityPesquisaFiltradaBinding
import com.fatec_gab_viana.navy.models.CarroNew
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PesquisaFiltrada : AppCompatActivity() {
    private lateinit var binding: ActivityPesquisaFiltradaBinding
    private lateinit var  recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapterPesquisa : AdapterPesquisa
    private var lista: ArrayList<CarroNew> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesquisaFiltradaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val filtro = intent.getStringExtra("filtro").toString()
        recyclerView = binding.pesquisaRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this@PesquisaFiltrada,
            LinearLayoutManager.VERTICAL,false)
        adapterPesquisa = AdapterPesquisa(this@PesquisaFiltrada,lista)
        recyclerView.adapter = adapterPesquisa
        adapterPesquisa.setOnItemClickListener(object : AdapterPesquisa.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val itemEscolhido = lista[position]

                val intent = Intent(this@PesquisaFiltrada,DetalheActivity::class.java)

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
        binding.pesquisaTfOrdenar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /**/
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /**/
            }


            override fun afterTextChanged(s: Editable?) {
                val listaFiltrada = if (s.isNullOrEmpty()) {
                    // Se o texto de pesquisa estiver vazio, exiba a lista completa
                    Obterdados(filtro)
                } else {
                    // Caso contrário, filtre a lista com base no texto de pesquisa
                    lista.filter {
                        it.modeloCarro?.contains(s.toString(), ignoreCase = true) ?: false
                    }
                }
                adapterPesquisa.atualizarListaFiltrada(listaFiltrada)

            }

        })
        Obterdados(filtro)
    }

    private fun MostrarOpcoesFiltro(view: View?) {
        val popupMenu = PopupMenu(this,view)
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

    private fun Obterdados(Filtro: String): ArrayList<CarroNew> {
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
                adapterPesquisa.filtrarLista(Filtro)
                adapterPesquisa.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        return lista
    }


}