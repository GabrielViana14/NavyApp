package com.fatec_gab_viana.navy.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fatec_gab_viana.navy.R
import com.fatec_gab_viana.navy.models.CarroNew
import com.fatec_gab_viana.navy.models.carro
import com.google.android.material.imageview.ShapeableImageView

class AdapterPesquisa(private val context: Activity, private val arrayList: ArrayList<CarroNew>) :
    RecyclerView.Adapter<AdapterPesquisa.ViewHolder>() {

    private lateinit var mlistener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mlistener = listener
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val imageview: ShapeableImageView = itemView.findViewById(R.id.item_pesquisa_imagem_carro)
        val marca: TextView = itemView.findViewById(R.id.item_pesquisa_marca_carro)
        val modelo: TextView = itemView.findViewById(R.id.item_pesquisa_titulo_carro)
        val cambio: TextView = itemView.findViewById(R.id.item_pesquisa_cambio_carro)
        val ano: TextView = itemView.findViewById(R.id.item_pesquisa_ano_carro)
        val preco: TextView = itemView.findViewById(R.id.item_pesquisa_preco_carro)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.carro_list_item, parent, false)
        return ViewHolder(view,mlistener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val carro: CarroNew = arrayList[position]

        holder.marca.text = carro.marcaCarro
        holder.modelo.text = carro.modeloCarro
        holder.cambio.text = carro.cambioCarro
        holder.ano.text = carro.ano.toString()
        holder.preco.text = "R$" + carro.preco

        if (carro.imagem!!.isNotEmpty()) {
            Glide.with(context)
                .load(carro.imagem)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(holder.imageview)
        } else {
            holder.imageview.setImageResource(R.drawable.carro_exemplo)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun atualizarListaFiltrada(listaFiltrada: List<CarroNew>) {
        arrayList.clear()
        arrayList.addAll(listaFiltrada)
        notifyDataSetChanged()
    }

    fun filtrarLista(opcaoFiltro: String) {
        val listaFiltrada = when (opcaoFiltro) {
            // Outros casos de filtro...
            "opcao1" -> arrayList.sortedByDescending { it.preco }
            "opcao2" -> arrayList.sortedBy { it.preco }
            "opcao3" -> arrayList.sortedBy { it.modeloCarro }
            "opcao4" -> arrayList.sortedBy { it.marcaCarro }
            "sedan"  -> arrayList.filter { it.grupo == "Sedan" }
            "SUV"    -> arrayList.filter { it.grupo == "SUV" }
            "picape" -> arrayList.filter { it.grupo == "Picape" }
            "hatch"  -> arrayList.filter { it.grupo == "Hatch" }
            "60mil" -> arrayList.filter { it.preco <= 60000 }
            "60mil a 80mil" -> arrayList.filter { it.preco > 60000.0 && it.preco <= 80000.0 }
            "80mil a 120mil" -> arrayList.filter { it.preco > 80000.0 && it.preco <= 120000.0 }
            "120 mil" -> arrayList.filter { it.preco > 120000.0 }
            "Navy Mauá" -> arrayList.filter { it.filial == "Navy Mauá" }
            "Navy Santo André" -> arrayList.filter { it.filial == "Navy Santo André"  }
            "Navy São Caetano do Sul" -> arrayList.filter { it.filial == "Navy São Caetano do Sul" }
            // Outros casos de filtro...
            else -> arrayList // Filtro padrão ou sem filtro
        }
        atualizarListaFiltrada(listaFiltrada)
    }
}
    
