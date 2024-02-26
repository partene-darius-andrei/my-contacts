package com.my.contacts.repositories

import com.my.contacts.network.ContactsService
import javax.inject.Inject

class ContactsRepository @Inject constructor() {

    @Inject
    lateinit var contactsService: ContactsService

    suspend fun getContacts() = contactsService.getContacts()

}