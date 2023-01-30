package com.example.myfitness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.R
import com.example.myfitness.entities.Plan

class PlanAdapter(private val plan: MutableList<Plan>) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dayName: TextView
        private val exerciseName: TextView

        init {
            dayName = view.findViewById(R.id.tv_day_name)
            exerciseName = view.findViewById(R.id.tv_exercise_name)
        }

        fun bind(plan: Plan) {
            dayName.text = plan.day
            val string = StringBuilder()
            for(exercise in plan.exercise) {
                string.append(exercise.exercise)
                string.append("\n")
            }
            exerciseName.text = string
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        //Kreiranje novog ViewHoldera koji sadrži pogled za svaki item u recycleview
        val planView = LayoutInflater.from(parent.context).inflate(R.layout.plan_list_item, parent, false)
        return PlanViewHolder(planView)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        //Prikazivanje podataka na određenim mjestima
        holder.bind(plan[position])
    }

    override fun getItemCount() = plan.size
}