package com.example.paging3example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3example.databinding.LoadingItemBinding
import com.example.paging3example.databinding.MyItemItemBinding
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
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_VIEW_KEY -> ItemViewHolder(
                MyItemItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            LOADING_VIEW_KEY -> LoadingViewHolder(
                LoadingItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            else -> throw IndexOutOfBoundsException("Unknown view type")
        }
    }

    inner class ItemViewHolder(private val binding: MyItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyItem) {
            binding.tvText.text = item.text
        }
    }

    class LoadingViewHolder(private val binding: LoadingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<MyItem>() {
        override fun areItemsTheSame(oldItem: MyItem, newItem: MyItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MyItem, newItem: MyItem) = oldItem == newItem
    }
}