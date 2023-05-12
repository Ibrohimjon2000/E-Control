package uz.devapp.e_control.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.devapp.e_control.database.entity.AttendsEntity
import uz.devapp.e_control.database.entity.EmployeeEntity
import uz.devapp.e_control.database.entity.PurposeEntity
import uz.devapp.e_control.databinding.ItemAttendsBinding
import java.text.SimpleDateFormat
import java.util.*

class AttendsAdapter(val items:List<AttendsEntity>,val employeeEntityList:List<EmployeeEntity>,val purposeEntityList:List<PurposeEntity>):RecyclerView.Adapter<AttendsAdapter.Vh>() {

    inner class Vh(val binding:ItemAttendsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemAttendsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView)
            .load(item.image)
            .into(holder.binding.image)
        employeeEntityList.forEach {
            if (it.id==item.employeeId){
                holder.binding.tvName.text=it.name
            }
        }
        purposeEntityList.forEach {
            if (it.id==item.purposeId){
                holder.binding.tvPurpose.text=it.purpose
            }
        }
        val sdf = SimpleDateFormat("HH:mm")
        val resultdate = Date(item.date)
        val format = sdf.format(resultdate)
        when(item.type){
            "input"->{
                holder.binding.tvTime.text="Keldi: $format"
            }
            "output"->{
                holder.binding.tvTime.text="Ketdi: $format"
            }
        }
    }
}