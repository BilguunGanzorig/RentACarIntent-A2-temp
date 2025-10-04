package edu.swin.rentacar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.swin.rentacar.model.Car

class FavoriteAdapter(
    private val items: MutableList<Car>,
    private val onClick: (Car) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.imgFav)
        val title: TextView = v.findViewById(R.id.tvFavTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val car = items[position]
        holder.img.setImageResource(car.imageRes)
        holder.title.text = "${car.name} ${car.model}"
        holder.itemView.setOnClickListener { onClick(car) }
    }

    override fun getItemCount(): Int = items.size

    /** Add if not already present. */
    fun addUnique(car: Car) {
        if (items.none { it.id == car.id }) {
            items.add(car)
            notifyItemInserted(items.lastIndex)
        }
    }

    /** Remove by object (if present). */
    fun remove(car: Car) {
        val idx = items.indexOfFirst { it.id == car.id }
        if (idx >= 0) {
            items.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }

    /** Remove by adapter position (used by swipe). */
    fun removeAt(position: Int) {
        if (position in 0 until items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /** Check if a car with this id is in favourites. */
    fun contains(id: String): Boolean = items.any { it.id == id }
}
