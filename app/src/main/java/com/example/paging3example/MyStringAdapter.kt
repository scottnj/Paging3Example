package com.example.paging3example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.string_item.view.*

class MyStringAdapter : PagingDataAdapter<String, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private const val LOADING_VIEW_TYPE = 0
        private const val ITEM_VIEW_TYPE = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (getItemViewType(position)) {
            LOADING_VIEW_TYPE -> holder as LoadingViewHolder
            ITEM_VIEW_TYPE -> (holder as ItemViewHolder).setItemDetails(item!!)
            else -> throw IndexOutOfBoundsException("Unknown view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null) LOADING_VIEW_TYPE else ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LOADING_VIEW_TYPE -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.loading_item,
                    parent,
                    false
                )
            )
            ITEM_VIEW_TYPE -> ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.string_item,
                    parent,
                    false
                )
            )
            else -> throw IndexOutOfBoundsException("Unknown view type")
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItemDetails(item: String) {
            itemView.tvMessage.text = item
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return true // oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}