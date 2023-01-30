package com.example.myfitness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.R
import com.example.myfitness.entities.DoneExercise
import com.example.myfitness.entities.Plan

class DailyWorkoutAdapter(private val items: List<DoneExercise>) : RecyclerView.Adapter<DailyWorkoutAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflatea jedan item u listi i inflatea layout tog itema (definiran u plan_list_item)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_list_item, parent, false)
        return ViewHolder(view)
    }

    //spaja item na njegov odgovarajući viewHolder (npr. exerciseName i weight svaki imaju svoj TextView)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.property1.text = item.exerciseName
        holder.property2.text = item.weight.toString()
        //holder.property3.text = item.sets.toString()
        //holder.property4.text = item.reps.toString()
    }

    //vraca broj itema u listi
    override fun getItemCount(): Int = items.size

    //cache objekta koji ce biti na recyclerViewu.... propertievi azuriraju informacije koje se prikazuju
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val property1: TextView = itemView.findViewById(R.id.tv_day_name)
        val property2: TextView = itemView.findViewById(R.id.tv_exercise_name)
        //val property3: TextView = itemView.findViewById(R.id.property3)
        //val property4: TextView = itemView.findViewById(R.id.property4)
    }
}