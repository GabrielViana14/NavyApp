import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fatec_gab_viana.navy.R
import com.fatec_gab_viana.navy.models.carro
import com.google.android.material.imageview.ShapeableImageView
import android.app.Activity
import com.fatec_gab_viana.navy.models.CarroNew

class AdapterHome(private val context: Activity, private val arrayList: ArrayList<CarroNew>) :
    RecyclerView.Adapter<AdapterHome.ViewHolder>() {

    private lateinit var mlistener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mlistener = listener
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val imageview: ShapeableImageView = itemView.findViewById(R.id.item_home_imagem_carro)
        val marca: TextView = itemView.findViewById(R.id.item_home_marca_carro)
        val modelo: TextView = itemView.findViewById(R.id.item_home_titulo_carro)
        val cambio: TextView = itemView.findViewById(R.id.item_home_cambio_carro)
        val ano: TextView = itemView.findViewById(R.id.item_home_ano_carro)
        val preco: TextView = itemView.findViewById(R.id.item_home_preco_carro)
        val km:TextView = itemView.findViewById(R.id.item_home_km_carro)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(view,mlistener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val carro: CarroNew = arrayList[position]

        holder.marca.text = carro.marcaCarro
        holder.modelo.text = carro.modeloCarro
        holder.cambio.text = carro.cambioCarro
        holder.ano.text = carro.ano.toString()
        holder.preco.text = "R$" + carro.preco
        holder.km.text = carro.km.toString()

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
}
