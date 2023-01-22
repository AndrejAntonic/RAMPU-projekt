package com.example.myfitness.charts

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.example.myfitness.R
import com.example.myfitness.helpers.DateHelper
import io.data2viz.charts.chart.*
import io.data2viz.charts.chart.mark.line
import io.data2viz.charts.dimension.Discrete
import io.data2viz.geom.Size
import io.data2viz.viz.VizContainerView
import model.DoneExercise

class TestChart(context: Context, exerciseDataSet : List<DoneExercise>, period: String) : VizContainerView(context) {

    private val chart: Chart<DoneExercise> = chart(exerciseDataSet) {
        size = Size(vizSize, vizSize)
        title = "Napredak snage"
        background = ColorDrawable(ContextCompat.getColor(context, R.color.bg_dark_2))

        // Create a discrete dimension for the year of the census
        val date : Discrete<DoneExercise, String>
        if (period == "Last Month") {
            date = discrete({ DateHelper.getDate(domain.date) })
        } else {
            date = discrete({ DateHelper.getMonth(domain.date) })
        }

        // Create a continuous numeric dimension for the population
        val weight = quantitative({ domain.weight.toDouble() }) {
            name = "Weight"
        }

        // Using a discrete dimension for the X-axis and a continuous one for the Y-axis
        line(date, weight) {
            joinMissingValues = true
            showMarkers = true
            strokeWidth = constant(10.0)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chart.size = Size(vizSize, vizSize * h / w)
    }
}

const val vizSize = 400.0