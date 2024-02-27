package com.my.contacts.repositories

import com.my.contacts.network.ContactsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepository @Inject constructor(
    private val contactsService: ContactsService
) {

    suspend fun getContacts() = withContext(Dispatchers.IO) {
        contactsService.getContacts().body()!!.data
    }

}