package edu.swin.rentacar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.swin.rentacar.model.Rental

class RentedAdapter(
    private val items: MutableList<Rental>
) : RecyclerView.Adapter<RentedAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.imgRent)
        val title: TextView = v.findViewById(R.id.tvRentTitle)
        val meta: TextView = v.findViewById(R.id.tvRentMeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rental, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val r = items[pos]
        val c = r.car
        h.img.setImageResource(c.imageRes)
        h.title.text = "${c.name} ${c.model} (${c.year})"
        val dayLabel = if (r.days == 1) "day" else "days"
        h.meta.text = "${r.days} $dayLabel • ${r.totalCost} cr total"
    }

    override fun getItemCount(): Int = items.size

    fun add(rental: Rental) {
        items.add(0, rental)
        notifyItemInserted(0)
    }

    /** Remove by adapter position (used by swipe). */
    fun removeAt(position: Int) {
        if (position in 0 until items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
