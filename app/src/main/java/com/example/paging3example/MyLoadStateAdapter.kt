package com.example.paging3example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.load_state_item.view.*

class MyLoadStateAdapter : LoadStateAdapter<MyLoadStateAdapter.LoadStateViewHolder>() {

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        //TODO: When NotLoading -> false, the header is never shown.
        //      When true, it's shown, but never updated.
        return when (loadState) {
            is LoadState.NotLoading -> true
            is LoadState.Loading -> true
            is LoadState.Error -> true
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        return LoadStateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.load_state_item, parent, false)
    ) {
        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.NotLoading -> {
                    itemView.tvTitle.text = "Not Loading"
                }
                is LoadState.Loading -> {
                    itemView.tvTitle.text = "Loading"
                }
                is LoadState.Error -> {
                    itemView.tvTitle.text = "Error: ${loadState.error.localizedMessage}"
                }
            }
        }
    }
}