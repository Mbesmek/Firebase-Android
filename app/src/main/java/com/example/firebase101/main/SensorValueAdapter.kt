package com.example.firebase101.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase101.R

/**
 * Created by mgx1905 on 3/28/21.
 */

class SensorValueAdapter(private val stringList: ArrayList<SensorItem>) :
    ListAdapter<String, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sensor_value, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tvValue: TextView = holder.itemView.findViewById(R.id.tvValue)
        val tvHeader: TextView = holder.itemView.findViewById(R.id.tvHeader)

        val currentItem: SensorItem = stringList[position]
        tvHeader.text = currentItem.header
        tvValue.text = currentItem.value
    }

    override fun getItemCount(): Int = stringList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView)

}