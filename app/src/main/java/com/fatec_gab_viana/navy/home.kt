    package com.fatec_gab_viana.navy

    import AdapterHome
    import HorizontalSpaceItemDecoration
    import android.content.Intent
    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.fatec_gab_viana.navy.databinding.FragmentHomeBinding
    import com.fatec_gab_viana.navy.models.CarroNew
    import com.fatec_gab_viana.navy.models.carro
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.DatabaseReference
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.ValueEventListener


    class home : Fragment() {

        private lateinit var _binding: FragmentHomeBinding
        private val binding get() = _binding
        private lateinit var RecyclerView: RecyclerView
        private lateinit var adapterHome : AdapterHome
        private lateinit var databaseReference: DatabaseReference
        private lateinit var HomeMaisVend: ArrayList<CarroNew>



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)

            RecyclerView = binding.listviewhomemaisvend
            RecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            val espacamento = resources.getDimensionPixelSize(R.dimen.horizontal_spacing)
            RecyclerView.addItemDecoration(HorizontalSpaceItemDecoration(espacamento))

            val ListMaisvendidos = Obterdados()

            adapterHome = AdapterHome(requireActivity(), ListMaisvendidos)
            RecyclerView.adapter = adapterHome

            adapterHome.setOnItemClickListener(object : AdapterHome.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    val itemEscolhido = ListMaisvendidos[position]

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

            binding.carviewHomeHatches.setOnClickListener{
                val intent = Intent(requireContext(),PesquisaFiltrada::class.java)
                intent.putExtra("filtro","hatch")
                startActivity(intent)
            }
            binding.carviewHomePicape.setOnClickListener{
                val intent = Intent(requireContext(),PesquisaFiltrada::class.java)
                intent.putExtra("filtro","picape")
                startActivity(intent)
            }
            binding.carviewHomeSedans.setOnClickListener{
                val intent = Intent(requireContext(),PesquisaFiltrada::class.java)
                intent.putExtra("filtro","sedan")
                startActivity(intent)
            }

            binding.carviewHomeSuvs.setOnClickListener {
                val intent = Intent(requireContext(),PesquisaFiltrada::class.java)
                intent.putExtra("filtro","SUV")
                startActivity(intent)
            }
            binding.valor60mil.setOnClickListener{
                val intent = Intent(requireContext(),PesquisaFiltrada::class.java)
                intent.putExtra("filtro","60mil")
                startActivity(intent)
            }
            binding.valor60a80mil.setOnClickListener {
                val intent = Intent(requireContext(),PesquisaFiltrada::class.java)
                intent.putExtra("filtro","60mil a 80mil")
                startActivity(intent)
            }
            binding.valor80a120mil.setOnClickListener {
                val intent = Intent(requireContext(),PesquisaFiltrada::class.java)
                intent.putExtra("filtro","80mil a 120mil")
                startActivity(intent)
            }

            binding.valorMais120mil.setOnClickListener{
                val intent = Intent(requireContext(),PesquisaFiltrada::class.java)
                intent.putExtra("filtro","120mil")
                startActivity(intent)
            }


            return binding.root
        }

        private fun Obterdados(): ArrayList<CarroNew> {
            HomeMaisVend = arrayListOf()
            databaseReference = FirebaseDatabase.getInstance().getReference("Carros")
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    HomeMaisVend.clear()

                    for (homeSnapshot in snapshot.children) {
                        val carrodata = homeSnapshot.getValue(CarroNew::class.java)

                        carrodata?.let {

                            HomeMaisVend.add(it)
                        }

                    }
                    adapterHome.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



            return HomeMaisVend
        }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
            }

        }
