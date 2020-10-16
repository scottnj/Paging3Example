package com.example.paging3example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_item_item.view.*
import javax.inject.Inject

class MyItemAdapter @Inject constructor() :
    PagingDataAdapter<MyItem, RecyclerView.ViewHolder>(DiffCallback) {
    companion object {
        private const val ITEM_VIEW_KEY = 0
        private const val LOADING_VIEW_KEY = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) (holder as ItemViewHolder).bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) != null) ITEM_VIEW_KEY else LOADING_VIEW_KEY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_KEY -> ItemViewHolder(parent)
            LOADING_VIEW_KEY -> LoadingViewHolder(parent)
            else -> throw IndexOutOfBoundsException("Unknown view type")
        }
    }

    inner class ItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.my_item_item, parent, false)
    ) {
        fun bind(item: MyItem) {
            itemView.tvText.text = item.text
        }
    }

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
    )

    object DiffCallback : DiffUtil.ItemCallback<MyItem>() {
        override fun areItemsTheSame(oldItem: MyItem, newItem: MyItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MyItem, newItem: MyItem) = oldItem == newItem
    }
}