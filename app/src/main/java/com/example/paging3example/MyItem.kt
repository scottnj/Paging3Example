package com.example.paging3example

data class MyItem(
    val id: Int,
    val pagingSourceCount: Int,
    val limit: Int,
    val offset: Int,
    val index: Int,
)