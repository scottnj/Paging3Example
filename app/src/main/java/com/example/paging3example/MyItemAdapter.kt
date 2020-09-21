package com.example.paging3example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_item_item.view.*

class MyItemAdapter :
    PagingDataAdapter<MyItemAdapter.Model, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val forceExhaustive: Any = when (val item = getItem(position)) {
            is Model.Item -> (holder as ItemViewHolder).bind(item)
            null -> holder as LoadingViewHolder
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return ViewType.LOADING.ordinal
        return item.viewType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.fromInt(viewType)) {
            ViewType.LOADING -> LoadingViewHolder(parent)
            ViewType.ITEM -> ItemViewHolder(parent)
        }
    }

    enum class ViewType {
        LOADING, ITEM;

        companion object {
            private val map = values().associateBy(ViewType::ordinal)
            fun fromInt(type: Int) =
                map[type] ?: throw IndexOutOfBoundsException("Unknown View Type")
        }
    }

    sealed class Model(val viewType: ViewType) {
        data class Item(val myItem: MyItem) : Model(viewType = ViewType.ITEM)
    }

    inner class ItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.my_item_item, parent, false)
    ) {
        fun bind(item: Model.Item) {
            itemView.tvId.text = item.myItem.id.toString()
            itemView.tvIndex.text = item.myItem.index.toString()
            itemView.tvPagingSourceCount.text = item.myItem.pagingSourceCount.toString()
            itemView.tvLimit.text = item.myItem.limit.toString()
            itemView.tvOffset.text = item.myItem.offset.toString()
        }
    }

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
    )

    object DiffCallback : DiffUtil.ItemCallback<Model>() {
        override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
            return when (oldItem) {
                is Model.Item -> newItem is Model.Item && oldItem.myItem.id == newItem.myItem.id
            }
        }

        override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
            return when (oldItem) {
                is Model.Item -> newItem is Model.Item && oldItem.myItem == newItem.myItem
            }
        }
    }
}