package com.my.contacts.network

import com.my.contacts.models.ContactsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ContactsService {

    @GET(CONTACTS_PATH)
    suspend fun getContacts(
        @Query(PER_PAGE) page: Int = 10
    ): Response<ContactsResponse>

    companion object {
        private const val CONTACTS_PATH = "api/users"
        private const val PER_PAGE = "per_page"
    }
}