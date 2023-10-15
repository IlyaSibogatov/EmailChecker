package com.example.emailchecker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.example.emailchecker.databinding.AutocompletedItemBinding

class AutoCompletedAdapter(
    val setFullName: ((item: String) -> Unit)
): RecyclerView.Adapter<AutoCompletedAdapter.ViewHolder>() {

    private var autocompletedList: MutableList<String> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<String>) {
        autocompletedList = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: AutocompletedItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AutoCompletedAdapter.ViewHolder {
        val binding = AutocompletedItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AutoCompletedAdapter.ViewHolder, position: Int) {
        val item = autocompletedList[position]
        holder.binding.apply {
            name.text = item
            name.setOnClickListener {
                setFullName(item)
            }
        }
    }

    override fun getItemCount(): Int = autocompletedList.size
}