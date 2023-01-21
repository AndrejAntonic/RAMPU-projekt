package com.example.myfitness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.R

class ExerciseListRecyclerViewAdapter(private val mList: List<String>) : RecyclerView.Adapter<ExerciseListRecyclerViewAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var filteredItems: List<String> = mList.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_done_exercise_dialog, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = filteredItems[position]
        holder.textView.text = item
//        holder.imageView.setImageResource(item.image)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position, item)
        }
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.card_imageview)
        val textView: TextView = itemView.findViewById(R.id.card_done_exercise_dialog_textview)
    }

    fun filter(query: String) {
        filteredItems = mList.filter {
            it.contains(query, ignoreCase = true)
        }
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: String)
    }
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}