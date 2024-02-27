package com.my.contacts.models

import com.google.gson.annotations.SerializedName

data class Response(
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val data: List<Contact>
)
