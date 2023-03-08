package com.example.gallery

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.owllearn.R
import com.example.owllearn.databinding.FragmentDashboardBinding
import com.example.owllearn.data.model.DeckPreview
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class DeckPreviewAdapter(private val binding: FragmentDashboardBinding) :
    ListAdapter<DeckPreview, DeckPreviewAdapter.DeckPreviewViewHolder>(DiffCallback()) {

    private lateinit var itemView: View
    private var pieChart: PieChart? = null
    private var pieChartHolder: LinearLayout? = null
    private var lastSelectedView: CardView? = null

    var lastSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckPreviewViewHolder {
        val v: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.deck_preview, parent, false)
        itemView = v
        return DeckPreviewViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun fillPieChart(deck: DeckPreview) {
        if (pieChart != null) {
            binding.previewTitle.text = deck.deckName
            binding.unmarkedText.text = String.format("Unmarked: %d", deck.unmarked)
            binding.easyText.text = String.format("Easy: %d", deck.easy)
            binding.mediumText.text = String.format("Medium: %d", deck.medium)
            binding.hardText.text = String.format("Hard: %d", deck.hard)


            val dataArray = ArrayList<PieEntry>()
            dataArray.add(PieEntry(deck.unmarked.toFloat()))
            dataArray.add(PieEntry(deck.easy.toFloat()))
            dataArray.add(PieEntry(deck.medium.toFloat()))
            dataArray.add(PieEntry(deck.hard.toFloat()))
            val dataSet = PieDataSet(dataArray, "")
            dataSet.valueTextSize = 0f

            //Color set for the chart
            val colorSet = java.util.ArrayList<Int>()
            colorSet.add(Color.LTGRAY)  //red
            colorSet.add(binding.root.context.getColor(R.color.green))
            colorSet.add(binding.root.context.getColor(R.color.orange))
            colorSet.add(binding.root.context.getColor(R.color.red))
            dataSet.setColors(colorSet)

            val data = PieData(dataSet)

            //chart description
            pieChart!!.description.text = ""
            pieChart!!.description.textSize = 20f

            //Chart data and other styling
            pieChart!!.data = data
            pieChart!!.centerTextRadiusPercent = 0f
            pieChart!!.isDrawHoleEnabled = false
            pieChart!!.legend.isEnabled = false
            pieChart!!.description.isEnabled = true

        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: DeckPreviewViewHolder, position: Int) {
        val card = holder.itemView.findViewById<CardView>(R.id.deck_preview)
        if (pieChart == null) {
            pieChart = binding.previewPieChart
            pieChartHolder = binding.previewStats
        }
//        if (position == 0) {
//            card.setCardBackgroundColor(ContextCompat.getColor(binding.deckPreviews.context, R.color.selected))
//            lastSelectedView = card
//            pieChartHolder?.visibility = VISIBLE
//            fillPieChart(getItem(position))
//            pieChart!!.animateXY(1000, 1000, Easing.EaseInSine, Easing.EaseInSine)
//
//
//        }

        holder.itemView.setOnClickListener {
            if (position == lastSelectedPos) {
                card.setCardBackgroundColor(ContextCompat.getColor(binding.deckPreviews.context, R.color.back))
                pieChartHolder?.visibility = INVISIBLE
                binding.previewTitle.text = ""
                lastSelectedPos = -1

            } else {
                card.setCardBackgroundColor(ContextCompat.getColor(binding.deckPreviews.context, R.color.selected))
                pieChartHolder?.visibility = VISIBLE
                lastSelectedView?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.deckPreviews.context,
                        R.color.back
                    )
                )
                lastSelectedView = card
                lastSelectedPos = position
                fillPieChart(getItem(position))
                pieChart!!.animateXY(1000, 1000, Easing.EaseInSine, Easing.EaseInSine)
            }
        }
        holder.bind(getItem(position))
    }

    inner class DeckPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.deck_preview_text)
        fun bind(item: DeckPreview?) {

            textView.text = item?.deckName
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<DeckPreview>() {
        override fun areItemsTheSame(oldItem: DeckPreview, newItem: DeckPreview): Boolean {
            return oldItem.deckName == newItem.deckName
        }

        override fun areContentsTheSame(oldItem: DeckPreview, newItem: DeckPreview): Boolean {
            return oldItem == newItem
        }
    }
}


