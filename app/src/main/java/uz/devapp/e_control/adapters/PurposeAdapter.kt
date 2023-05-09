package uz.devapp.e_control.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.devapp.e_control.data.model.Purpose
import uz.devapp.e_control.databinding.ItemPurposeBinding

interface PurposeAdapterCallback{
    fun onClickListener(item:Purpose)
}

class PurposeAdapter(val items: List<Purpose>,val callback: PurposeAdapterCallback) : RecyclerView.Adapter<PurposeAdapter.Vh>() {
    inner class Vh(val binding: ItemPurposeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemPurposeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val purpose = items[position]
        holder.binding.title.text = purpose.purpose
        if (position==0){
            holder.binding.card.setCardBackgroundColor(Color.parseColor("#F44336"))
        }else if (position==1){
            holder.binding.card.setCardBackgroundColor(Color.parseColor("#4CAF50"))
        }
        holder.itemView.setOnClickListener {
            callback.onClickListener(purpose)
        }
    }
}